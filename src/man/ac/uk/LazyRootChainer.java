package man.ac.uk;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.semanticweb.owl.explanation.api.Explanation;
import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLAxiom;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLDataFactory;
import org.semanticweb.owlapi.model.OWLDataProperty;
import org.semanticweb.owlapi.model.OWLIndividual;
import org.semanticweb.owlapi.model.OWLObjectProperty;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;
import org.semanticweb.owlapi.model.OWLOntologyManager;

import org.semanticweb.HermiT.Reasoner.ReasonerFactory;
import iso.checker.StrictIso;


public class LazyRootChainer {

	StrictIso si;
	ReasonerFactory reasonerfac;
	OWLOntologyManager ontoman;
	static OWLDataFactory df;
	
	public LazyRootChainer(){
		this.ontoman = OWLManager.createOWLOntologyManager();
		this.reasonerfac = new ReasonerFactory();
		this.df = ontoman.getOWLDataFactory();
		this.si = new StrictIso(reasonerfac);
	}
	
	public Boolean rootLemmaChecker(Explanation<OWLAxiom> exp1, Explanation<OWLAxiom> exp2){
		Set<OWLAxiom> set1 = exp1.getAxioms();
		Set<OWLAxiom> set2 = exp2.getAxioms();
		OWLAxiom entToCheck = exp1.getEntailment();
		Set<OWLAxiom> drawSet = new HashSet<OWLAxiom>(set2);
		Set<OWLClass> classes = new HashSet<OWLClass>();
		Set<OWLDataProperty> dataproperty = new HashSet<OWLDataProperty>();
		Set<OWLObjectProperty> objproperty = new HashSet<OWLObjectProperty>();
		Set<OWLIndividual> ind = new HashSet<OWLIndividual>();
		for(OWLAxiom ax: set2)
		{
			classes.addAll(ax.getClassesInSignature());
			dataproperty.addAll(ax.getDataPropertiesInSignature());
			objproperty.addAll(ax.getObjectPropertiesInSignature());
			ind.addAll(ax.getAnonymousIndividuals());
			ind.addAll(ax.getIndividualsInSignature());
		}
		
		int diff = set2.size() - set1.size();
		Boolean foundCond = true;
		do{
			if(diff == 1)
			{
				for(OWLAxiom ax:set2)
				{
					drawSet.remove(ax);
					Set<OWLAxiom> checkSet = new HashSet<OWLAxiom>(drawSet);
					drawSet.add(ax);
					
					
					
				}
			}
		}while(foundCond);
		return true;
	}
		
	public Map<Integer,ArrayList<String>>  produceSizeMapping(File dir) throws OWLOntologyCreationException{
		Map<Integer,ArrayList<String>> sizeMap = new HashMap<Integer,ArrayList<String>>();
		String[] list = dir.list();
		for(String path:list)
		{
			File fl = new File(path);
			OWLOntology ont = ontoman.loadOntologyFromOntologyDocument(fl);
			Set<OWLAxiom> set = new HashSet<OWLAxiom>();
			for(OWLAxiom ax:ont.getAxioms())
			{
				if(ax.isLogicalAxiom())
				{
					set.add(ax);
				}
			}
			Integer size = set.size();
			ontoman.removeOntology(ont);
			if(sizeMap.isEmpty())
			{
				ArrayList<String> newList = new ArrayList<String>();
				newList.add(path);
				sizeMap.put(size,newList);
			}
			else if(sizeMap.containsKey(size))
			{
				sizeMap.get(size).add(path);
			}
			else
			{
				ArrayList<String> newList = new ArrayList<String>();
				newList.add(path);
				sizeMap.put(size,newList);
			}
		}
		
		return sizeMap;
	}
	
}
