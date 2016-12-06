package man.ac.uk;

import java.awt.List;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Set;

import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.model.OWLAnnotationAssertionAxiom;
import org.semanticweb.owlapi.model.OWLAxiom;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLDataFactory;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;
import org.semanticweb.owlapi.model.OWLOntologyManager;
import org.semanticweb.owlapi.model.OWLSubClassOfAxiom;

public class CorpusSorter {
	
	OWLOntologyManager ontoman;
	OWLOntologyManager ontoman2;
	static OWLDataFactory df;
	
	public CorpusSorter(){
		this.ontoman = OWLManager.createOWLOntologyManager();
		this.ontoman2 = OWLManager.createOWLOntologyManager();
		this.df = OWLManager.getOWLDataFactory();
	}
	
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
		System.out.println(cond1);
		System.out.println(cond2);
		System.out.println(subclass);
		System.out.println(supclass);
		if(cond1 && cond2)
		{
			return df.getOWLSubClassOfAxiom(subclass, supclass);
		}
		else 
		{
			return null;
		}
		
	}
	
	public Boolean sameJust(Set<OWLAxiom> just1, OWLAxiom ax1, Set<OWLAxiom> just2, OWLAxiom ax2) throws OWLOntologyCreationException{
		if(ax1.equals(ax2)){
			return just1.equals(just2);
		}
		else{
			return false;
		}
	}
	
	public ArrayList<String> findMatches(File dir, OWLOntology just1) throws OWLOntologyCreationException{
		String path = dir.getPath();
		ArrayList<String> matches = new ArrayList<String>();
		File[] list = dir.listFiles();
		int dirCount = 0;
		for(File fl:list)
		{
			if(fl.isDirectory())
			{
				dirCount++;
			}
		}
		//if(dirCount > 1){
			for(File fl:dir.listFiles())
			{
				//if(fl.isDirectory())
				//{
					//for(File fl2:fl.listFiles())
					//{
						if(fl.getPath().contains(".owl") && !fl.getPath().contains("~"))
						{
							OWLOntology checkJust = ontoman2.loadOntologyFromOntologyDocument(fl);
							
							if(this.sameJust(just1.getAxioms(),this.entailSearch(just1),checkJust.getAxioms(),this.entailSearch(checkJust))){
								matches.add(fl.getPath());
							}
							ontoman2.removeOntology(checkJust);
						}
					//}
				//}
			//}
		}
		ontoman.removeOntology(just1);
		return matches;
	}
	
	public ArrayList<String> corpusList(File dir){
		ArrayList<String> corpus = new ArrayList<String>();
		File[] list = dir.listFiles();
		for(File fl:list)
		{
			String path = fl.getPath();
			if(path.contains(".owl") && !path.contains("~"))
			{
				corpus.add(path);
			}
			else if(fl.isDirectory())
			{
				ArrayList<String> subCorpus = this.corpusList(fl);
				corpus.addAll(subCorpus);
			}
		}
		return corpus;
	}
	
	public void eliminateRedundantJusts(File dir, File dir2) throws IOException, OWLOntologyCreationException{
		ArrayList<String> MasterList = this.corpusList(dir);
		while(!MasterList.isEmpty())
		{
			ArrayList<String> DeleteList = new ArrayList<String>();
			DeleteList.add(MasterList.get(0));
			String name =  MasterList.get(0).substring(MasterList.get(0).lastIndexOf("/") + 1);
			//System.out.println(name);
			File checkFile = new File(MasterList.get(0));
			//String entailmentDir = MasterList.get(0).replaceFirst(dir.toString(), "");
			//System.out.println(entailmentDir);
			//entailmentDir = entailmentDir.replaceFirst("/" + name, "");
			//System.out.println(entailmentDir);
			//entailmentDir = entailmentDir.replaceFirst(entailmentDir.substring(entailmentDir.lastIndexOf("/")), "");
			OWLOntology checkJust = ontoman.loadOntologyFromOntologyDocument(checkFile);
			Files.copy(Paths.get(MasterList.get(0)), Paths.get(dir2.toString() +  "/" + name));
			//System.out.println(dir.toString() + entailmentDir);
			DeleteList.addAll(this.findMatches(dir, checkJust));
			//System.out.println(DeleteList);
			MasterList.removeAll(DeleteList);
		}
	}
	
}
