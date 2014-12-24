package ca.mcmaster.magarveylab.wasp.session;

import java.text.ParseException;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

import ca.mcmaster.magarveylab.wasp.WebApplication;
import ca.mcmaster.magarveylab.wasp.util.TimeUtil;

/**
 * Session manager for a web application. 
 * @author skinnider, Lian
 *
 */
public class SessionManager implements Runnable {
	
	private final static SessionManager singleton = new SessionManager();
	private final static Logger logger = Logger.getLogger(SessionManager.class.getName());
	private static Thread thread = null;
	
	public static SessionManager getSessionManager() {
		synchronized(singleton) {
			if (thread == null) {
				thread = new Thread();
				thread.start();
			}	
		}
		return singleton;
	}

	private volatile ConcurrentHashMap<String, Session> sessionMap = null;

	private SessionManager() {
		sessionMap = new ConcurrentHashMap<String, Session>();
	}

	@Override
	public void run() {
		logger.log(Level.INFO, "Started session manager");
		while (true) {
			try {
				try {
					Set<String> idSet = sessionMap.keySet();
					for (String id : idSet) {
						Session session = sessionMap.get(id);
						if (session == null || session.lastHeartBeat() == null)
							continue;

						WebApplication webapp = session.webapp();
						// if 30 seconds has past since last heartbeat, stop the worker thread
						if (TimeUtil.timeDiff(session.lastHeartBeat()) > 30 && webapp != null && !webapp.isTerminated()) {
							logger.log(Level.INFO, "Session worker timeout, id = " + id);
							webapp.terminate();
						}

						// if 30 minutes have past since since last heartbeat AND the session has been initiated, unregister the session
						if (TimeUtil.timeDiff(session.lastHeartBeat()) > 30 * 60) {
							logger.log(Level.INFO, "Session timeout, id = " + id);
							removeSession(id);
						}
					}
				} catch (ParseException e) {
					System.err.println(e);
				}
				// sleep 30 seconds
				Thread.sleep(30_000);
			} catch (InterruptedException e) {
				logger.log(Level.WARNING, "Session stopped, id = " + e);
				return;
			}
		}
	}

	public void addSession(String sessionID, Session session) {
		sessionMap.put(sessionID, session);
		logger.log(Level.INFO, "Session added, id = " + sessionID);
	}
	
	public void removeSession(String sessionID) {
		Session session = sessionMap.get(sessionID);
		
		if (session != null) {
			WebApplication webapp = session.webapp();
			if (webapp != null && !webapp.isTerminated())
				webapp.terminate();
			webapp = null;
			session.setListener(null);
		}

		sessionMap.remove(sessionID);
		logger.log(Level.INFO, "Session removed, id = " + sessionID);
	}

	public Session getSession(String sessionID) {
		return sessionMap.get(sessionID);
	}
	
}
