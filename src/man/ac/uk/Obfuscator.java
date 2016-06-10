package man.ac.uk;

import java.lang.reflect.Array;
import java.util.HashSet;
import java.util.Set;

import org.semanticweb.owl.explanation.api.Explanation;
import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLAxiom;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLDataFactory;
import org.semanticweb.owlapi.model.OWLDataProperty;
import org.semanticweb.owlapi.model.OWLNamedIndividual;
import org.semanticweb.owlapi.model.OWLObjectProperty;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;
import org.semanticweb.owlapi.model.OWLOntologyManager;
import org.semanticweb.owlapi.model.AxiomType;
import org.semanticweb.owlapi.util.OWLEntityRenamer;


public class Obfuscator {
	OWLOntologyManager ontoman;
	static OWLDataFactory df;
	OWLEntityRenamer ontoRename;
	
	
	//Initialisation
	public Obfuscator() {
	
	ontoman = OWLManager.createOWLOntologyManager();
	df = ontoman.getOWLDataFactory();
	
	}
	
	public Explanation<OWLAxiom> obfuscateJustifcation(Explanation<OWLAxiom> exp){
		
		try {
			OWLOntology just = ontoman.createOntology(exp.getAxioms());
			ontoman.addAxiom(just, exp.getEntailment());
			Set<OWLOntology> justSet = new HashSet<OWLOntology>();
			justSet.add(just);
			OWLEntityRenamer ontoRename = new OWLEntityRenamer(ontoman,justSet);
			
			
			
		} catch (OWLOntologyCreationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		
		
		
		
		return exp;
	}
	
	//Entailment -> Entailment
	//For a given Entailment as an OWLAxiom will return it obfuscated.
	
	public void obfuscateEntailment(OWLAxiom ax){
		//Is the axiom a logical axiom?
		if(!ax.isLogicalAxiom())
		{
			System.out.println("Error: Entailment is not a logical axiom!");
			
		}
		
		
		//Checks number of Classes, Ind. Data and Obj Properties. If less then three
		//for any apply renaming
		
		OWLClass[] Classes = (OWLClass[]) ax.getClassesInSignature().toArray();
		OWLDataProperty[] DataProperties = (OWLDataProperty[]) ax.getDataPropertiesInSignature().toArray();
		OWLObjectProperty[] ObjectProperties = (OWLObjectProperty[]) ax.getObjectPropertiesInSignature().toArray();
		OWLNamedIndividual[] NamedIndividuals = (OWLNamedIndividual[]) ax.getIndividualsInSignature().toArray();
		
		
		if(Classes.length < 3  && !ax.getClassesInSignature().isEmpty())
		{
			if(Classes.length == 2)
			{
				OWLClass cl1 = Classes[0];
				OWLClass cl2 = Classes[1];
				ontoRename.changeIRI(cl1, IRI.create("http://cs.man.ac.uk/corpus/#A"));
				ontoRename.changeIRI(cl2, IRI.create("http://cs.man.ac.uk/corpus/#B"));
			}
			else
			{
				OWLClass cl = Classes[0];
				ontoRename.changeIRI(cl, IRI.create("http://cs.man.ac.uk/corpus/#A"));
			}
		}
		
		if(DataProperties.length < 3  && !ax.getDataPropertiesInSignature().isEmpty())
		{
			if(Classes.length == 2)
			{
				OWLClass cl1 = Classes[0];
				OWLClass cl2 = Classes[1];
				ontoRename.changeIRI(cl1, IRI.create("http://cs.man.ac.uk/corpus/#A"));
				ontoRename.changeIRI(cl2, IRI.create("http://cs.man.ac.uk/corpus/#B"));
			}
			else
			{
				OWLClass cl = Classes[0];
				ontoRename.changeIRI(cl, IRI.create("http://cs.man.ac.uk/corpus/#A"));
			}
		}
		
		if(ObjectProperties.length < 3  && !ax.getObjectPropertiesInSignature().isEmpty())
		{
			if(Classes.length == 2)
			{
				OWLClass cl1 = Classes[0];
				OWLClass cl2 = Classes[1];
				ontoRename.changeIRI(cl1, IRI.create("http://cs.man.ac.uk/corpus/#A"));
				ontoRename.changeIRI(cl2, IRI.create("http://cs.man.ac.uk/corpus/#B"));
			}
			else
			{
				OWLClass cl = Classes[0];
				ontoRename.changeIRI(cl, IRI.create("http://cs.man.ac.uk/corpus/#A"));
			}
		}
		
		if(NamedIndividuals.length < 3  && !ax.getIndividualsInSignature().isEmpty())
		{
			if(Classes.length == 2)
			{
				OWLClass cl1 = Classes[0];
				OWLClass cl2 = Classes[1];
				ontoRename.changeIRI(cl1, IRI.create("http://cs.man.ac.uk/corpus/#A"));
				ontoRename.changeIRI(cl2, IRI.create("http://cs.man.ac.uk/corpus/#B"));
			}
			else
			{
				OWLClass cl = Classes[0];
				ontoRename.changeIRI(cl, IRI.create("http://cs.man.ac.uk/corpus/#A"));
			}
		}
		
	
	}
		
	
	
	//ABoxAxiom -> ABoxAxiom
	//For a given ABox axiom will return obfuscated ABox axiom.
	
	public OWLAxiom obfuscateABoxAxiom (OWLAxiom ax){
		if(ax.isOfType(AxiomType.CLASS_ASSERTION))
		{
			return ax;
		}
		else if(ax.isOfType(AxiomType.DIFFERENT_INDIVIDUALS))
		{
			return ax;
		}
		else if(ax.isOfType(AxiomType.NEGATIVE_DATA_PROPERTY_ASSERTION))
		{
			return ax;
		}
		else if(ax.isOfType(AxiomType.DATA_PROPERTY_ASSERTION))
		{
			return ax;
		}
		else if(ax.isOfType(AxiomType.SAME_INDIVIDUAL))
		{
			return ax;
		}
		else if(ax.isOfType(AxiomType.OBJECT_PROPERTY_ASSERTION))
		{
			return ax;
		}
		else if(ax.isOfType(AxiomType.DATATYPE_DEFINITION))
		{
			return ax;
		}
		else 
		{
			//NegativeObjectPropertyAssertion
			return ax;
		}
	}
	
	
	
}
