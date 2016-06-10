package man.ac.uk;

import java.awt.List;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import org.semanticweb.owl.explanation.api.Explanation;
import org.semanticweb.owlapi.model.OWLAxiom;

public class AxiomSort {
	
	public AxiomSort(){	
	}
	
	public Set<OWLAxiom> AxiomList(Set<Explanation<OWLAxiom>> corpus){
		return null;
	}
	
	public Set<OWLAxiom> EntailmentList(Set<Explanation<OWLAxiom>> corpus){
		return null;
	}
	
	public Map<OWLAxiom,ArrayList<String>> constructAxiomShareList(Map<Explanation<OWLAxiom>,String> corpusList){
		ArrayList<Explanation<OWLAxiom>> remainingExplanations = new ArrayList<Explanation<OWLAxiom>>(corpusList.keySet());
		Map<OWLAxiom,ArrayList<String>> axShList = new HashMap<OWLAxiom,ArrayList<String>>();
		while(!remainingExplanations.isEmpty())
		{
			Explanation<OWLAxiom> checkExp1 = remainingExplanations.get(0);			
			for(OWLAxiom ax:remainingExplanations.get(0).getAxioms())
			{
				if(!axShList.containsKey(ax))
				{
					//System.out.println(ax);
					ArrayList<String> containList = new ArrayList<String>();
					Map<Explanation<OWLAxiom>,Explanation<OWLAxiom>> removeMap = new HashMap<Explanation<OWLAxiom>,Explanation<OWLAxiom>>();
					for(Explanation<OWLAxiom> checkExp2:corpusList.keySet())
					{
						if(checkExp2.getAxioms().contains(ax))
						{
							String sharerName = corpusList.get(checkExp2);
							//System.out.println("SHARE: " + sharerName);
							containList.add(sharerName);
							Set<OWLAxiom> newSet = new HashSet<OWLAxiom>(checkExp2.getAxioms());
							//System.out.println(newSet);
							newSet.remove(ax);
							Explanation<OWLAxiom> newExp = new Explanation<OWLAxiom>(checkExp2.getEntailment(),newSet);
							removeMap.put(checkExp2, newExp);
						}
					}
					axShList.put(ax, containList);
					//Replaces explanations with shortened ones
					for(Explanation<OWLAxiom> alterExp:removeMap.keySet())
					{
						remainingExplanations.remove(alterExp);
						remainingExplanations.add(removeMap.get(alterExp));
					}
				}
			}
			remainingExplanations.remove(checkExp1);
			//Removes any empty explanations
			ArrayList<Explanation<OWLAxiom>> removeExpList = new ArrayList<Explanation<OWLAxiom>>();
			for(Explanation exp:remainingExplanations)
			{
				if(exp.getAxioms().isEmpty())
				{
					removeExpList.add(exp);
				}
			}
			remainingExplanations.removeAll(removeExpList);
		}
		return axShList;
	}
	
	
}
