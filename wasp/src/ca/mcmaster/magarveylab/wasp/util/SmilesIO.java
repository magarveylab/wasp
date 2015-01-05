package ca.mcmaster.magarveylab.wasp.util;

import java.io.IOException;

import org.openscience.cdk.DefaultChemObjectBuilder;
import org.openscience.cdk.config.IsotopeFactory;
import org.openscience.cdk.exception.InvalidSmilesException;
import org.openscience.cdk.interfaces.IAtomContainer;
import org.openscience.cdk.interfaces.IChemObjectBuilder;
import org.openscience.cdk.interfaces.IMolecule;
import org.openscience.cdk.smiles.SmilesGenerator;
import org.openscience.cdk.smiles.SmilesParser;

/**
 * Utility class for SMILES input/output.
 * @author skinnider
 *
 */
public class SmilesIO {
	
	/**
	 * Generate an IMolecule from a SMILES string.
	 * @param smiles	The structure to generate an IMolecule instance from, as a SMILES string
	 * @return			An IMolecule object corresponding to the input SMILES
	 * @throws InvalidSmilesException 
	 * @throws IOException 
	 */
	public static IMolecule molecule(String smiles) throws InvalidSmilesException, IOException {
		IMolecule mol = null;
		synchronized (SmilesIO.class) {
			IChemObjectBuilder builder = DefaultChemObjectBuilder.getInstance();
			SmilesParser parser = new SmilesParser(builder);
	        parser.setPreservingAromaticity(true);
			mol = parser.parseSmiles(smiles);
		}
		IsotopeFactory fact = IsotopeFactory.getInstance(mol.getBuilder());
		fact.configureAtoms(mol);
		return mol;
	}

	/**
	 * Generate a SMILES string from an IAtomContainer
	 * @param mol	The molecule to be parsed
	 * @return		The molecule's structures as a SMILES string
	 */
	public static String smiles(IAtomContainer mol) {
		SmilesGenerator sg = new SmilesGenerator();
		sg.setUseAromaticityFlag(true);
		String smiles = sg.createSMILES(mol);
		return smiles;
	}

}
