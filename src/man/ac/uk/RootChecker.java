package man.ac.uk;
import java.util.Set;

import org.semanticweb.owl.explanation.api.Explanation;
import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.model.OWLAxiom;
import org.semanticweb.owlapi.model.OWLOntologyManager;


public class RootChecker {
	
	
	public RootChecker(){
	}
	
	public boolean compareExplanations(Explanation<OWLAxiom> exp1, Explanation<OWLAxiom> exp2){
		
		//Automatic dismissal conditions, same number of axioms, different axiom types (later, fuck it!)
		Set<OWLAxiom> set1 = exp1.getAxioms();
		Set<OWLAxiom> set2 = exp2.getAxioms();
		if(set1.size() == set2.size())
		{		
			return false;
		}
		else
		{
			return set2.containsAll(set1);
		}
	}
}
