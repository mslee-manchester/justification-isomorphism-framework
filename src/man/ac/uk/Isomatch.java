package man.ac.uk;
import org.semanticweb.HermiT.Reasoner.ReasonerFactory;
import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.model.AxiomType;
import org.semanticweb.owlapi.model.OWLAxiom;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;
import org.semanticweb.owlapi.model.OWLOntologyManager;
import org.semanticweb.owl.explanation.*;
import org.semanticweb.owl.explanation.api.Explanation;
import org.semanticweb.owl.explanation.api.ExplanationGenerator;
import org.semanticweb.owl.explanation.api.ExplanationGeneratorFactory;
import org.semanticweb.owl.explanation.api.ExplanationManager;
import static java.nio.file.StandardCopyOption.*;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

/*
 * Takes a corpus (a directory containing .owl Ontologies) 
 * and produces a dir containing paired .owl and list of
 * files isomorphic to that justification.
 */

public class Isomatch {

	OWLOntologyManager ontoman;
	
	public Isomatch(){	
	}
	
	public ArrayList<String> makeList(File dir) throws IOException{
		ArrayList<String> foundList = new ArrayList<String>();
		if(dir.isDirectory())
		{	
			File[] list  = dir.listFiles();
			for(File file:list)
			{
				String path = file.getPath();
				if(path.contains(".owl"))
				{
					foundList.add(path);
				}
				else if(file.isDirectory()){
					ArrayList<String> tempList = this.makeList(file);
					foundList.addAll(tempList);
				}
			}
			return foundList;
		}
		else{		
		return null;
		}
	}
	
	public void sortJusts(File dir, File dir2) throws IOException, OWLOntologyCreationException{
		OWLOntologyManager ontoman = OWLManager.createOWLOntologyManager();
		Isosearch is = new Isosearch();
		ArrayList<String> MasterList = this.makeList(dir);
		//System.out.println(MasterList);
		while(!MasterList.isEmpty())
		{
			ArrayList<String> DeleteList = new ArrayList<String>();
			DeleteList.add(MasterList.get(0));
			String name =  MasterList.get(0).substring(MasterList.get(0).lastIndexOf("/") + 1);
			//File savedFile = File.createTempFile("isoList" + "_" + name, ".txt", dir2);
			//savedFile.setWritable(true);
			//PrintWriter outStr = new PrintWriter(new BufferedWriter(new FileWriter(savedFile, true)));
			File checkFile = new File(MasterList.get(0));
			//System.out.println("Checking isomorphic to file: " + checkFile);
			OWLOntology checkJust = ontoman.loadOntologyFromOntologyDocument(checkFile);
			Files.copy(Paths.get(MasterList.get(0)), Paths.get(dir2.toString() +  "/" + name));
			Set<OWLAxiom> setJust = new HashSet<OWLAxiom>();
			for(OWLAxiom ax:checkJust.getAxioms())
			{
				if(!ax.isAnnotationAxiom() && !ax.isOfType(AxiomType.DECLARATION))
				{
					setJust.add(ax);
				}
			}
			ontoman.removeOntology(checkJust);
			Explanation<OWLAxiom> checkExp = new Explanation<OWLAxiom>(is.entailSearch(checkJust), setJust);
			DeleteList.addAll(is.dirSearch(checkExp, dir));
			MasterList.removeAll(DeleteList);
			//outStr.close();
			//System.out.println("Delete list: "+DeleteList);
			//System.out.println("Master list: "+MasterList);

		}
		
	}
}		
		
		/**
		while(aLine != null)
		{
			File savedFile = File.createTempFile("isoList" + "_" + aLine, ".txt", dir2);
			PrintWriter outStr2 = new PrintWriter(new BufferedWriter(new FileWriter(savedFile, true)));
			File checkFile = new File(aLine);
			OWLOntology checkJust = ontoman.loadOntologyFromOntologyDocument(checkFile);
			Set<OWLAxiom> setJust = new HashSet<OWLAxiom>();
			for(OWLAxiom ax:checkJust.getAxioms())
			{
				if(!ax.isAnnotationAxiom())
				{
					setJust.add(ax);
				}
			}
			Explanation<OWLAxiom> checkExp = new Explanation<OWLAxiom>(Isosearch.entailSearch(checkJust), setJust);
			FileInputStream fis2 = new FileInputStream(is.dirSearch(checkExp, dir));
			BufferedReader in2 = new BufferedReader(new InputStreamReader(fis));
			String aLine2 = null;
			while((aLine2 = in.readLine()) != null)
			{
				outStr2.println(aLine2);
			}
			in2.close();
			outStr2.close();
			FileInputStream fis3 = new FileInputStream(savedFile);
			BufferedReader in3 = new BufferedReader(new InputStreamReader(fis3));
			String aLine3 = null;
			in.close();
			corpus.delete();
			File corpus2 = File.createTempFile("tempList2", ".txt", dir);
			corpus2.setWritable(true);
			while((aLine3 = in3.readLine()) != null)
			{
				String aLine4 = null;
				FileInputStream fis4 = new FileInputStream(corpus2);
				BufferedReader in4 = new BufferedReader(new InputStreamReader(fis4));
				BufferedWriter writer = new BufferedWriter(new FileWriter(corpus));
           		while((aLine4 = in4.readLine()) != null){
				    if(aLine4.equals(aLine3)) continue;
				    writer.write(aLine4 + System.getProperty("line.separator"));
				}
           		writer.close();
           		in4.close();
			}
		}
		**/
