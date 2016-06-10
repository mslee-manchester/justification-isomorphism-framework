package man.ac.uk;
import org.semanticweb.HermiT.Configuration;
import org.semanticweb.HermiT.Reasoner.ReasonerFactory;
import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.model.AddAxiom;
import org.semanticweb.owlapi.model.AxiomType;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLAnnotation;
import org.semanticweb.owlapi.model.OWLAnnotationAssertionAxiom;
import org.semanticweb.owlapi.model.OWLAnnotationSubject;
import org.semanticweb.owlapi.model.OWLAxiom;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLDataFactory;
import org.semanticweb.owlapi.model.OWLDataProperty;
import org.semanticweb.owlapi.model.OWLEntity;
import org.semanticweb.owlapi.model.OWLLogicalAxiom;
import org.semanticweb.owlapi.model.OWLNamedIndividual;
import org.semanticweb.owlapi.model.OWLObjectProperty;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;
import org.semanticweb.owlapi.model.OWLOntologyManager;
import org.semanticweb.owlapi.model.OWLOntologyManagerFactory;
import org.semanticweb.owlapi.model.OWLOntologyStorageException;
import org.semanticweb.owlapi.model.OWLSubClassOfAxiom;
import org.semanticweb.owlapi.model.PrefixManager;
import org.semanticweb.owlapi.util.BidirectionalShortFormProvider;
import org.semanticweb.owlapi.util.OWLEntityRenamer;
import org.semanticweb.owl.explanation.*;
import org.semanticweb.owl.explanation.api.Explanation;
import org.semanticweb.owl.explanation.api.ExplanationGenerator;
import org.semanticweb.owl.explanation.api.ExplanationGeneratorFactory;
import org.semanticweb.owl.explanation.api.ExplanationManager;

import iso.checker.*;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;
import java.io.*;
import java.nio.file.*;
public class Isosearch {
	
	
	StrictIso si;
	SubexIso subi;
	LemmaIso li;
	ExtStrictIso esi;
	Ext2StrIso e2si;
	static OWLDataFactory df;
	ReasonerFactory hermitfac;
	OWLOntologyManager ontoman;
	Configuration configuration;
	/**
	 * @param repository
	 * Instantiates the reasoner factory (based off HermiT), the isomorphism checkers, ontology manager and data factory. .
	 * @throws IOException 
	 * @throws OWLOntologyCreationException 
	 */
	
	public Isosearch() throws IOException, OWLOntologyCreationException{
		hermitfac = new ReasonerFactory();
		si = new StrictIso(hermitfac);
        subi = new SubexIso(hermitfac);
        li = new LemmaIso(hermitfac);
        esi = new ExtStrictIso(hermitfac);
        e2si = new Ext2StrIso(hermitfac);
        ontoman = OWLManager.createOWLOntologyManager();
        df = ontoman.getOWLDataFactory();
	}
	
	
	public ArrayList<String> dirSearch(Explanation<OWLAxiom> just, File searchDir) throws OWLOntologyCreationException, IOException{
		
		//System.out.println("Performing isomorphism search on dir...");
		File[] list  = searchDir.listFiles();		
	    //System.out.println("Writable file: " + tempStrIso.setWritable(true));
		ArrayList<String> foundList = new ArrayList<String>();
		int JustSearchSize = just.getSize();
		for(File file:list)
		{
			String path = file.getPath();
			if(path.contains(".owl") && !path.contains("~"))
			{
				//System.out.println(path);
				OWLOntology checkJust = ontoman.loadOntologyFromOntologyDocument(file);
				Set<OWLAxiom> setJust = new HashSet<OWLAxiom>();
				for(OWLAxiom ax:checkJust.getAxioms())
				{
					if(!ax.isAnnotationAxiom())
					{
						setJust.add(ax);
					}
				}
				if(JustSearchSize == setJust.size())
				{
					OWLSubClassOfAxiom sb = entailSearch(checkJust);
					Explanation<OWLAxiom> checkExp = new Explanation<OWLAxiom>(sb,setJust);
					//System.out.println(checkExp);
					//System.out.println(just);
					ontoman.removeOntology(checkJust);
					if(e2si.equivalent(just, checkExp))
					{
						//System.out.println(esi.equivalent(just, checkExp) + just.toString() + checkExp.toString());
						foundList.add(path);
					}
				}
				else
				{
					ontoman.removeOntology(checkJust);
				}
			}
			else if(file.isDirectory())
			{
				ArrayList<String> tempList = this.dirSearch(just, file);
				//System.out.println("Copying directory results...");
				foundList.addAll(tempList);
			}
		}
		return foundList;
		/**
		System.out.println("Performing isomorphism search on dir...");
		File[] list  = searchDir.listFiles();
		System.out.println("Directory Space:");
		File tempStrIso = File.createTempFile("tempStrIso", ".txt", searchDir);
		File tempSubIso = File.createTempFile("tempSubIso", ".txt", searchDir);
		System.out.println("Writable file 1: " + tempStrIso.setWritable(true));
		System.out.println("Writable file 2: " + tempSubIso.setWritable(true));
		@SuppressWarnings("resource")
		PrintWriter outStr = new PrintWriter(new BufferedWriter(new FileWriter(tempStrIso, true)));
		PrintWriter outSub = new PrintWriter(new BufferedWriter(new FileWriter(tempSubIso, true)));
		outStr.println("Strictly Isomorphic");
		outSub.println("SubExpression Isomorphic");
		int JustSearchSize = just.getSize();
		for(File file:list)
		{
			String path = file.getPath();
			System.out.println(file.getPath());
			if(path.contains(".owl"))
			{
				OWLOntology checkJust = ontoman.loadOntologyFromOntologyDocument(file);
				Set<OWLAxiom> setJust = new HashSet<OWLAxiom>();
				for(OWLAxiom ax:checkJust.getAxioms())
				{
					if(!ax.isAnnotationAxiom())
					{
						setJust.add(ax);
					}
					
				}
				
				
				if(JustSearchSize == setJust.size())
				{
					OWLSubClassOfAxiom sb = Isosearch.entailSearch(checkJust);
					Explanation<OWLAxiom> checkExp = new Explanation<OWLAxiom>(sb,setJust);
					ontoman.removeOntology(checkJust);
					if(si.equivalent(just, checkExp))
					{
						System.out.println("Strictly Isomorphic");
						outStr.println(path);
					}
				}
				else
				{
					ontoman.removeOntology(checkJust);
				}
			}
			else if(file.isDirectory())
			{
				FileInputStream fis = new FileInputStream(this.dirSearch(just, file));
				System.out.println("Copying directory results...");
				BufferedReader in = new BufferedReader(new InputStreamReader(fis));
				String aLine = null;
				while((aLine = in.readLine()) != "SubExpression Isomorphic")
				{
					if(aLine != null)
					{
						if(!aLine.equals("Strictly Isomorphic"))
						{
							if(!aLine.equals("SubExpression Isomorphic"))
							{
								System.out.println(aLine + " is Strictly Isomorphic.");
								outStr.println(aLine);
							}
							else
							{
								break;
							}
						}
					}
					else
					{
						break;
					}
				}
				System.out.println("Leo's stop point. Hello 2.");
				while((aLine = in.readLine()) != null)
				{
					if(!aLine.equals("Strictly Isomorphic"))
					{
						if(!aLine.equals("SubExpression Isomorphic"))
						{
							System.out.println(aLine + " is SubExpression Isomorphic.");
							outSub.println(aLine);
						}
					}
				}
				in.close();
			}
		}
		outStr.close();
		outSub.close();
		//Merging of temporary files together. Puts the list of sub and strict isomorphic justs together.
		System.out.println("Collecting search data...");
		File temp = File.createTempFile("temp", ".txt", searchDir);
		FileInputStream inStream1 = new FileInputStream(tempStrIso);
		FileOutputStream outStream = new FileOutputStream(temp,true);
		byte[] buffer = new byte[1024];
	    int length;
	    while ((length = inStream1.read(buffer)) > 0){
	    	outStream.write(buffer, 0, length);
	    }

	    if (inStream1 != null)inStream1.close();
	    FileInputStream inStream2 = new FileInputStream(tempSubIso);
	     while ((length = inStream2.read(buffer)) > 0){
	    	outStream.write(buffer, 0, length);
	    }
	    if (inStream2 != null)inStream2.close();
	    if (outStream != null)outStream.close();
		tempStrIso.deleteOnExit();
		tempSubIso.deleteOnExit();
		temp.deleteOnExit();
		return temp;
		**/
	}
	
	public void autoAnnotateDir(File dir) throws OWLOntologyCreationException, OWLOntologyStorageException{
		File[] list = dir.listFiles();
		for(File file:list)
		{
			String path = file.getPath();
			if(path.contains(".owl"))
			{
				autoAnnotate(file);
			}
			else if(file.isDirectory())
			{
				autoAnnotateDir(file);
			}
		}
	}

	
	
	public OWLOntology sigRenamer(OWLOntology exp, OWLSubClassOfAxiom ent) throws OWLOntologyCreationException{
		//Produces the first justification to check via Strict Isomorphism. 
		//Because the isomorphism checker does not handle annotations, 
		//we remove them by getting only logical axioms.
		Set<OWLAxiom> expSet1 = new HashSet<OWLAxiom>();
		for(OWLLogicalAxiom ax:exp.getLogicalAxioms())
		{
			expSet1.add(ax);
		}
		Explanation<OWLAxiom> just1 = new Explanation<OWLAxiom>(ent,expSet1);
		System.out.println("Constructing obfuscated version of justification...");
		Set<OWLOntology> sont = new HashSet<OWLOntology>();
		sont.add(exp);
		Set<OWLClass> sbclass = new HashSet<OWLClass>();
		sbclass.add(ent.getSubClass().asOWLClass());
		sbclass.add(ent.getSuperClass().asOWLClass());
		Set<OWLClass> justClass = exp.getClassesInSignature();
		Set<OWLDataProperty> justDataProp = exp.getDataPropertiesInSignature();
		Set<OWLObjectProperty> justObjProp = exp.getObjectPropertiesInSignature();
		Set<OWLNamedIndividual> justInd = exp.getIndividualsInSignature();
		justClass.removeAll(sbclass);
		int classCount = 1;
		int propCount = 1;
		int dataCount = 1;
		int indCount = 1;
		OWLEntityRenamer ontorename = new OWLEntityRenamer(ontoman,sont);
		OWLClass cl1;
		OWLClass cl2;
		if(ent.getSubClass().isBottomEntity() || ent.getSubClass().isTopEntity())
		{
			if(ent.getSubClass().isBottomEntity())
			{
				cl1 = df.getOWLNothing();
			}
			else
			{
				cl1 = df.getOWLThing();
			}
		}
		else
		{
		ontoman.applyChanges(ontorename.changeIRI(ent.getSubClass().asOWLClass(), IRI.create("http://www.abstractednames.com/" + "A")));
		cl1 = df.getOWLClass(IRI.create("http://www.abstractednames.com/" + "A"));
		}
		
		if(ent.getSuperClass().isBottomEntity() || ent.getSuperClass().isTopEntity())
		{
			if(ent.getSuperClass().isBottomEntity())
			{
				cl2 = df.getOWLNothing();
			}
			else
			{
				cl2 = df.getOWLThing();
			}
		}
		else
		{
		ontoman.applyChanges(ontorename.changeIRI(ent.getSuperClass().asOWLClass(), IRI.create("http://www.abstractednames.com/" + "B")));
		cl2 = df.getOWLClass(IRI.create("http://www.abstractednames.com/" + "B"));
		}
		
		for(OWLClass sigPart:justClass)
		{
				ontoman.applyChanges(ontorename.changeIRI(sigPart, IRI.create("http://www.abstractednames.com/" + "C" + classCount)));
				classCount++;
		}
		for(OWLDataProperty sigPart:justDataProp)
		{
				ontoman.applyChanges(ontorename.changeIRI(sigPart, IRI.create("http://www.abstractednames.com/" + "D" + dataCount)));
				dataCount++;
		}
		for(OWLObjectProperty sigPart: justObjProp)
		{
				ontoman.applyChanges(ontorename.changeIRI(sigPart, IRI.create("http://www.abstractednames.com/" + "R" + propCount)));
				propCount++;
		}
		for(OWLNamedIndividual sigPart:justInd)
		{
				ontoman.applyChanges(ontorename.changeIRI(sigPart, IRI.create("http://www.abstractednames.com/" + "a" + indCount)));
				indCount++;
		}
			
		//SANITY CHECK USE ISOMORPHISM TO CHECK THIS SHIT!
		System.out.println("Performing Sanity Check to see if that this is a correct justification.");
		Set<OWLAxiom> expSet2 = new HashSet<OWLAxiom>();
		for(OWLLogicalAxiom ax:exp.getLogicalAxioms())
		{
			expSet2.add(ax);
		}
		
		
		//ontoman.removeAxioms(exp, exp.getAnnotationAssertionAxioms(IRI.create("http://www.abstractednames.com/" + "A")));
		//ontoman.removeAxioms(exp, exp.getAnnotationAssertionAxioms(IRI.create("http://www.abstractednames.com/" + "B")));
		//ontoman.removeAxioms(exp, exp.getAnnotationAssertionAxioms(df.getOWLNothing().getIRI()));
		//ontoman.removeAxioms(exp, exp.getAnnotationAssertionAxioms(df.getOWLThing().getIRI()));		
		//OWLSubClassOfAxiom ent2 = df.getOWLSubClassOfAxiom(cl1, cl2);
		return exp;
		/**
		System.out.println(ent);
		System.out.println(ent2);
		Explanation<OWLAxiom> just2 = new Explanation<OWLAxiom>(ent2,expSet2);
		if(esi.equivalent(just1, just2))
		{
			System.out.println("Strict Isomorphism holds.");
			//System.out.println(expSet2);
			//System.out.println(ent2);
			//System.out.println(expSet1);
			//System.out.println(ent);
			ontoman.removeOntology(exp);
			return exp;
		}
		else
		{
			System.out.println("Strict Isomorphism doesn't hold.");
			System.out.println(expSet2);
			System.out.println(ent2);
			System.out.println(expSet1);
			System.out.println(ent);
			ontoman.removeOntology(exp);
			return exp;
		}
		**/
	}
	
	
	public void autoAnnotate(File file) throws OWLOntologyCreationException, OWLOntologyStorageException {
		OWLOntology just = ontoman.loadOntologyFromOntologyDocument(file);
		String path = file.toString();
		System.out.println(path);
		Set<OWLClass> test = just.getClassesInSignature();
		ArrayList<String> classNames = new ArrayList<String>();
		for(OWLClass cl : test)
		{
			System.out.println(cl.getIRI().toString());
			int nameBegin = cl.toString().lastIndexOf("/");
			if(cl.getIRI().toString().contains("#"))
			{
				int hashBegin = cl.getIRI().toString().lastIndexOf("#") + 1;
				classNames.add(cl.getIRI().toString().substring(hashBegin));
			}
			else
			{
				if(nameBegin > -1)
				{
				classNames.add(cl.getIRI().toString().substring(nameBegin));
				}
			}
		}
		test.add(df.getOWLNothing());
		test.add(df.getOWLThing());
		classNames.add("Nothing");
		classNames.add("Thing");
		ArrayList<String> subClasses = new ArrayList<String>();
		ArrayList<String> supClasses = new ArrayList<String>();
		String sub = "";
		String sup = "";
		String lastBack1 = path.substring(0, path.lastIndexOf("/"));
		String lastBack2 = lastBack1.substring(0, lastBack1.lastIndexOf("/"));
		String lastBack3 = lastBack2.substring(lastBack2.lastIndexOf("/"));
		System.out.println(lastBack3);
		for(String cls : classNames)
		{
			if(lastBack3.contains(cls))
			{
				System.out.println(lastBack3.lastIndexOf(cls));
				if(lastBack3.lastIndexOf(cls) == 1)
				{
					subClasses.add(cls);
				}
				else
				{
					supClasses.add(cls);
				}
			}
		}
		
		//Sanity check so that classes match those in path name
		
		for(String cls1:subClasses)
		{
			for(String cls2:supClasses)
			{
				String toCheck = "/" + cls1 + "_" + cls2;
				if(lastBack3.equals(toCheck))
				{
					sub = cls1;
					sup = cls2;
				}
			}
		}
				
		System.out.println(sub);
		System.out.println(sup);
		OWLClass subcl = null;
		OWLClass supcl = null;
		if(!sub.isEmpty() && !sup.isEmpty())
		{
		
		for(OWLClass cl: test)
		{
			if(cl.toString().contains(sub))
			{
				if(cl.toString().contains("Nothing"))
				{
					System.out.println("Found nothing!");
				}
				else if(cl.toString().contains("Thing"))
				{
					System.out.println("Found everything!");
				}
				else
				{	
					subcl = cl;
				}
			}
			else if(cl.toString().contains(sup))
			{
				supcl = cl;
			}
		}
		System.out.println(subcl);
		System.out.println(supcl);
		
		OWLAnnotation commentSub = df.getOWLAnnotation(df.getRDFSComment(),
                df.getOWLLiteral("This is the subclass of the entailment that follows from this justification.", "en"));
		OWLAnnotation commentSup = df.getOWLAnnotation(df.getRDFSComment(),
                df.getOWLLiteral("This is the superclass of the entailment that follows from this justification.", "en"));
		OWLAxiom ax1 = df.getOWLAnnotationAssertionAxiom((OWLAnnotationSubject) subcl.getIRI(), commentSub);
		OWLAxiom ax2 = df.getOWLAnnotationAssertionAxiom((OWLAnnotationSubject) supcl.getIRI(), commentSup);
		ontoman.applyChange(new AddAxiom(just,ax1));
		ontoman.applyChange(new AddAxiom(just,ax2));
		ontoman.saveOntology(just);
		}
		else
		{
		System.out.println("One class is not present in Justification");
		}
		ontoman.removeOntology(just);
	}
	
	
	//This finds the relevant annotations in the justification file and returns the relevant subclass axiom.
	
	public OWLSubClassOfAxiom entailSearch(OWLOntology just) throws OWLOntologyCreationException{
		Set<OWLClass> anjuClass = just.getClassesInSignature();
		anjuClass.add(df.getOWLThing());
		anjuClass.add(df.getOWLNothing());
		OWLClass subclass = null;
		OWLClass supclass = null;
		Boolean cond1 = false;
		Boolean cond2 = false;
		for(OWLClass cl:anjuClass)
		{
			Set<OWLAnnotationAssertionAxiom> anno = cl.getAnnotationAssertionAxioms(just);
			if(!anno.isEmpty())
			{
				for(OWLAnnotationAssertionAxiom aax : anno)
				{
					if(aax.getAnnotation().getValue().toString().contains("superclass") && !cond1)
					{
							cond1 = true;
							supclass = cl;
					}
					else if(aax.getAnnotation().getValue().toString().contains("subclass") && !cond2)
					{
							cond2 = true;
							subclass = cl;
					}
				}
				
			}
		}
		//System.out.println(cond1);
		//System.out.println(cond2);
		
		if(cond1 && cond2)
		{
			return df.getOWLSubClassOfAxiom(subclass, supclass);
		}
		else 
		{
			return null;
		}
		
	}
	
}
