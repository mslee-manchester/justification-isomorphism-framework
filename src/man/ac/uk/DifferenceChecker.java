package man.ac.uk;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import org.semanticweb.HermiT.structural.OWLAxioms;
import org.semanticweb.owl.explanation.api.Explanation;
import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.io.OWLObjectRenderer;
import org.semanticweb.owlapi.model.AxiomType;
import org.semanticweb.owlapi.model.ClassExpressionType;
import org.semanticweb.owlapi.model.OWLAxiom;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLClassExpression;
import org.semanticweb.owlapi.model.OWLDataProperty;
import org.semanticweb.owlapi.model.OWLDatatype;
import org.semanticweb.owlapi.model.OWLEquivalentClassesAxiom;
import org.semanticweb.owlapi.model.OWLObjectProperty;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;
import org.semanticweb.owlapi.model.OWLOntologyManager;
import org.semanticweb.owlapi.model.OWLSubClassOfAxiom;

import uk.ac.manchester.cs.owl.owlapi.mansyntaxrenderer.ManchesterOWLSyntaxOWLObjectRendererImpl;
import uk.ac.manchester.cs.owlapi.dlsyntax.DLSyntaxObjectRenderer;

public class DifferenceChecker {
	OWLOntologyManager ontoman;
	Isosearch iso;
	OWLObjectRenderer ren = new ManchesterOWLSyntaxOWLObjectRendererImpl();
	
	public DifferenceChecker() throws OWLOntologyCreationException, IOException{
		OWLOntologyManager ontoman = OWLManager.createOWLOntologyManager();
		Isosearch iso = new Isosearch();
	}
	
	public Boolean containsDefinitionalAxioms(Explanation<OWLAxiom> exp1, OWLSubClassOfAxiom ax){
		Boolean cond1 = false;
		int i = 0;
		Set<OWLEquivalentClassesAxiom> equvSet = new HashSet<OWLEquivalentClassesAxiom>();
		for(OWLAxiom ax1:exp1.getAxioms())
		{
			if(ax1.isOfType(AxiomType.EQUIVALENT_CLASSES)){
				cond1 = true;
				equvSet.add((OWLEquivalentClassesAxiom) ax1);
			}
		}
		
		if(cond1){
			OWLClass subclass = ax.getSubClass().asOWLClass();
			OWLClass supClass = ax.getSuperClass().asOWLClass();
			Boolean cond2 = false;
			Boolean cond3 = false;
			for(OWLEquivalentClassesAxiom ax2:equvSet){
				for(OWLClass cl:ax2.getNamedClasses()){
					if(subclass.equals(cl) || subclass.isBottomEntity() || subclass.isTopEntity()){
						cond2 = true;
					}
						
					if(supClass.equals(cl) || subclass.isBottomEntity() || subclass.isTopEntity()){
							cond3 = true;
					}
				}
			}
			return (cond2 && cond3);
		}
		else{
			return false;
		}
	}
	
	public String findDifferences(Explanation<OWLAxiom> exp, OWLSubClassOfAxiom ax){
		OWLClass subclass = ax.getSubClass().asOWLClass();
		OWLClass supclass = ax.getSuperClass().asOWLClass();
		Set<OWLEquivalentClassesAxiom> definitions = new HashSet<OWLEquivalentClassesAxiom>();
		for(OWLAxiom ax1:exp.getAxioms())
		{
			if(ax1.getClassesInSignature().contains(subclass) || ax1.getClassesInSignature().contains(supclass))
			{
				if(ax1.isOfType(AxiomType.EQUIVALENT_CLASSES))
				{
					definitions.add((OWLEquivalentClassesAxiom) ax1);
				}
			}
		}
		Set<OWLEquivalentClassesAxiom> removals = new HashSet<OWLEquivalentClassesAxiom>();
		for(OWLEquivalentClassesAxiom eca:definitions)
		{
			if(!eca.containsOWLThing() && !eca.containsOWLNothing() && !eca.contains(supclass) && !eca.contains(subclass))
			{
				removals.add(eca);
			}
		}
		definitions.removeAll(removals);
		Set<OWLClass> supClasses = new HashSet<OWLClass>();
		Set<OWLClass> subClasses = new HashSet<OWLClass>();
		Set<OWLDataProperty> supDataProp = new HashSet<OWLDataProperty>();
		Set<OWLDataProperty> subDataProp = new HashSet<OWLDataProperty>();
		Set<OWLObjectProperty> supObjProp = new HashSet<OWLObjectProperty>();
		Set<OWLObjectProperty> subObjProp = new HashSet<OWLObjectProperty>();
		Set<OWLDatatype> subDatatype = new HashSet<OWLDatatype>();
		Set<OWLDatatype> supDatatype = new HashSet<OWLDatatype>();
		Set<OWLClassExpression> supClassEx = new HashSet<OWLClassExpression>();
		Set<OWLClassExpression> subClassEx = new HashSet<OWLClassExpression>();
		if(definitions.size() == 2){
			for(OWLEquivalentClassesAxiom eca:definitions)
			{
				if(eca.contains(supclass))
				{
					supClasses.addAll(eca.getClassesInSignature());
					supDataProp.addAll(eca.getDataPropertiesInSignature());
					supObjProp.addAll(eca.getObjectPropertiesInSignature());
					supDatatype.addAll(eca.getDatatypesInSignature());
					for(OWLClassExpression ce:eca.getNestedClassExpressions()){
						ClassExpressionType cet = ce.getClassExpressionType();
						if(		cet.toString().equals("DataSomeValuesFrom") ||
								cet.toString().equals("DataAllValuesFrom") ||
								cet.toString().equals("DataExactCardinality") ||
								cet.toString().equals("DataHasValue") ||
								cet.toString().equals("DataMaxCardinality") ||
								cet.toString().equals("DataMinCardinality")){
								supClassEx.add(ce);
						}
					}
				}
				
				if(eca.contains(subclass))
				{
					subClasses.addAll(eca.getClassesInSignature());
					subDataProp.addAll(eca.getDataPropertiesInSignature());
					subObjProp.addAll(eca.getObjectPropertiesInSignature());
					subDatatype.addAll(eca.getDatatypesInSignature());
					for(OWLClassExpression ce:eca.getNestedClassExpressions()){
						ClassExpressionType cet = ce.getClassExpressionType();
						if(		cet.toString().equals("DataSomeValuesFrom") ||
								cet.toString().equals("DataAllValuesFrom") ||
								cet.toString().equals("DataExactCardinality") ||
								cet.toString().equals("DataHasValue") ||
								cet.toString().equals("DataMaxCardinality") ||
								cet.toString().equals("DataMinCardinality")){
								subClassEx.add(ce);
						}
					}
				}
				
				
			}
			
			
			
			Set<OWLClass> diffSupClass = new HashSet<OWLClass>();
			Set<OWLClass> diffSubClass = new HashSet<OWLClass>();
			Set<OWLDataProperty> diffSupDataProp = new HashSet<OWLDataProperty>();
			Set<OWLDataProperty> diffSubDataProp = new HashSet<OWLDataProperty>();
			Set<OWLObjectProperty> diffSupObjProp = new HashSet<OWLObjectProperty>();
			Set<OWLObjectProperty> diffSubObjProp = new HashSet<OWLObjectProperty>();
			Set<OWLDatatype> diffSupDatatype = new HashSet<OWLDatatype>();
			Set<OWLDatatype> diffSubDatatype = new HashSet<OWLDatatype>();
			Set<OWLClassExpression> diffSupClassEx = new HashSet<OWLClassExpression>();
			Set<OWLClassExpression> diffSubClassEx = new HashSet<OWLClassExpression>();
			diffSupClass.addAll(supClasses);
			diffSupClass.removeAll(subClasses);
			diffSupClass.remove(supclass);
			diffSubClass.addAll(subClasses);
			diffSubClass.removeAll(supClasses);
			diffSubClass.remove(subclass);
			diffSupDataProp.addAll(supDataProp);
			diffSupDataProp.removeAll(subDataProp);
			diffSubDataProp.addAll(subDataProp);
			diffSubDataProp.removeAll(supDataProp);
			diffSubObjProp.addAll(subObjProp);
			diffSubObjProp.removeAll(supObjProp);
			diffSupObjProp.addAll(supObjProp);
			diffSupObjProp.removeAll(subObjProp);
			diffSupDatatype.addAll(supDatatype);
			diffSupDatatype.removeAll(subDatatype);
			diffSubDatatype.addAll(subDatatype);
			diffSubDatatype.removeAll(supDatatype);
			diffSupClassEx.addAll(supClassEx);
			diffSupClassEx.removeAll(subClassEx);
			diffSupClassEx.remove(supclass);
			diffSubClassEx.addAll(subClassEx);
			diffSubClassEx.removeAll(supClassEx);
			diffSubClassEx.remove(subclass);
			String differences = "";
			for(OWLClass cl:diffSupClass){
				differences = differences.concat(ren.render(cl) + ";");
			}
			for(OWLClass cl:diffSubClass){
				differences = differences.concat(ren.render(cl) + ";");
			}
			differences = differences.concat(",");
			for(OWLObjectProperty op:diffSupObjProp){
				differences =  differences.concat(ren.render(op) + ";");
			}
			for(OWLObjectProperty op:diffSubObjProp){
				differences = differences.concat(ren.render(op) + ";");
			}
			differences = differences.concat(",");
			for(OWLDataProperty dp:diffSupDataProp){
				differences = differences.concat(ren.render(dp) + ";");
			}
			for(OWLDataProperty dp:diffSubDataProp){
				differences = differences.concat(ren.render(dp) + ";");
			}
			differences = differences.concat(",");
			
			for(OWLDatatype dt:diffSupDatatype){
				differences = differences.concat(ren.render(dt) + ";");
			}
			for(OWLDatatype dt:diffSubDatatype){
				differences = differences.concat(ren.render(dt) + ";");
			}
			differences = differences.concat(",");
		
			for(OWLClassExpression ce:diffSupClassEx){
				differences = differences.concat(ren.render(ce) + ";");
			}
			for(OWLClassExpression ce:diffSupClassEx){
				differences = differences.concat(ren.render(ce) + ";");
			}
			return diffSupClass.size() + "," + diffSubClass.size() 
					+ "," + diffSupObjProp.size() +  "," + diffSubObjProp.size() +
					"," + diffSupDataProp.size() +  "," + diffSubDataProp.size() +
					"," + diffSupDatatype.size() + "," + diffSubDatatype.size() + 
					"," + diffSupClassEx.size() + "," + diffSubClassEx.size()+ ","+ differences;
		}
		return "";
	}
}

