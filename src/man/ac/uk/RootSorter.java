package man.ac.uk;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.semanticweb.owl.explanation.api.Explanation;
import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.model.OWLAxiom;
import org.semanticweb.owlapi.model.OWLLogicalAxiom;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;
import org.semanticweb.owlapi.model.OWLOntologyManager;

public class RootSorter {
	
	OWLOntologyManager ontoman;
	
	public RootSorter(){
		this.ontoman = OWLManager.createOWLOntologyManager();
	}
	
	public Map<String,ArrayList<String>> produceCompletePartialOrder(File dir) throws FileNotFoundException, OWLOntologyCreationException, IOException {
		ArrayList<String> roots = this.produceRootList(dir);
		Map<String,ArrayList<String>> cpo = new HashMap<String,ArrayList<String>>();
		for(String path:roots)
		{
			File just = new File(path);
			cpo.putAll(this.produceDerivedPartialOrdering(just, dir));			
		}
		return this.cleanList(cpo);
		
		//for(String path:cpo.keySet())
		//{
			//System.out.println("ROOT: " + path);
			//System.out.println("DERIVED JUSTIFICATIONS: ");
			//for(String derived:cpo.get(path))
			//{
				//System.out.println(derived);
			//}
			//System.out.println("");
		//}
	}
	
	public Boolean isSharedSet(Explanation<OWLAxiom> exp1,Explanation<OWLAxiom> exp2){
		return exp1.getAxioms().equals(exp2.getAxioms());
	}
	
	public ArrayList<String> findSharedJustifications(Explanation<OWLAxiom> exp, File dir) throws OWLOntologyCreationException{
		CorpusSorter cs = new CorpusSorter();
		ArrayList<String> list = cs.corpusList(dir);
		ArrayList<String> shareholders = new ArrayList<String>();
		for(String s:list){
			File fl = new File(s);
			OWLOntology just = ontoman.loadOntologyFromOntologyDocument(fl);
			Set<OWLAxiom> checkSet = new HashSet<OWLAxiom>();
			for(OWLAxiom ax:just.getLogicalAxioms())
			{
				checkSet.add(ax);
			}
			if(exp.getAxioms().equals(checkSet))
			{
				if(!exp.getEntailment().equals(cs.entailSearch(just)))
				{
					shareholders.add(s);
				}
			}
		}
		return shareholders;
	}
	
	
	public ArrayList<String> produceRootList(File dir) throws FileNotFoundException, IOException, OWLOntologyCreationException {
		
		CorpusSorter cs = new CorpusSorter();
		ArrayList<String> masterList = cs.corpusList(dir);
		ArrayList<String> roots = new ArrayList<String>(this.eliminateDuplicates(masterList));
		for(String path:this.eliminateDuplicates(masterList))
		{
			File fl = new File(path);
			OWLOntology ont1 = ontoman.loadOntologyFromOntologyDocument(fl);
			Set<OWLLogicalAxiom> set1 = ont1.getLogicalAxioms();
			ontoman.removeOntology(ont1);
			for(String path2:masterList)
			{
				if(!path.equals(path2))
				{
					File fl2 = new File(path2);
					OWLOntology ont2 = ontoman.loadOntologyFromOntologyDocument(fl2);
					Set<OWLLogicalAxiom> set2 = ont2.getLogicalAxioms();
					ontoman.removeOntology(ont2);
					if(set1.size() > set2.size())
					{
						if(set1.containsAll(set2))
						{
							roots.remove(path);
						}
					}
				}
			}
		}
		return roots;
	}

	public Map<String,ArrayList<String>> produceDerivedPartialOrdering(File just, File dir) throws OWLOntologyCreationException, IOException{
		Map<String,ArrayList<String>> po = new HashMap<String,ArrayList<String>>();
		OWLOntology ont = ontoman.loadOntologyFromOntologyDocument(just);
		Isosearch iso = new Isosearch();
		Set<OWLAxiom> set = new HashSet<OWLAxiom>();
		for(OWLAxiom ax:ont.getLogicalAxioms())
		{
			set.add(ax);
		}
		Explanation<OWLAxiom> exp = new Explanation<OWLAxiom>(iso.entailSearch(ont),set);
		ontoman.removeOntology(ont);
		ArrayList<String> overall = this.eliminateDuplicates(this.getDerivedJustifications(exp, dir));
		ArrayList<String> master = new ArrayList<String>(overall);
		for(String path:master)
		{
			File fl = new File(path);
			OWLOntology ont2 = ontoman.loadOntologyFromOntologyDocument(fl);
			Set<OWLAxiom> set2 = new HashSet<OWLAxiom>();
			for(OWLAxiom ax:ont2.getLogicalAxioms())
			{
				set2.add(ax);
			}
			Explanation<OWLAxiom> exp2 = new Explanation<OWLAxiom>(iso.entailSearch(ont2),set2);
			ontoman.removeOntology(ont2);
			ArrayList<String> mapping = this.eliminateDuplicates(this.getDerivedJustifications(exp2, dir));
			overall.removeAll(mapping);
			for(ArrayList<String> list:po.values())
			{
				if(list.containsAll(mapping))
				{
					list.removeAll(mapping);
				}
			}
			po.put(path, mapping);
		}
		po.put(just.getAbsolutePath(),overall);
		return po;
	}

	
	public ArrayList<String> getDerivedJustifications(Explanation<OWLAxiom> exp, File dir) throws IOException, OWLOntologyCreationException{
		ArrayList<String> derivedJusts = new ArrayList<String>();
		CorpusSorter cs = new CorpusSorter();
		ArrayList<String> masterList = cs.corpusList(dir);
		Set<OWLAxiom> masterSet = exp.getAxioms();
		
		//System.out.println(masterList);
		for(String path:masterList)
		{
			File fl = new File(path);
			OWLOntology ont = ontoman.loadOntologyFromOntologyDocument(fl);
			Set<OWLAxiom> checkSet = new HashSet<OWLAxiom>();
			for(OWLAxiom ax:ont.getAxioms())
			{
				if(ax.isLogicalAxiom())
				{
					checkSet.add(ax);
				}
			}
			
			if(masterSet.size() < checkSet.size())
			{
				if(checkSet.containsAll(masterSet))
				{
					derivedJusts.add(path);
				}
			}
			ontoman.removeOntology(ont);
		}		
		return derivedJusts;
	}
	
	public ArrayList<String> eliminateDuplicates(ArrayList<String> dir) throws OWLOntologyCreationException, IOException{
		ArrayList<String> reducedDir = new ArrayList<String>(dir);
		for(String fl:dir)
		{
			if(reducedDir.contains(fl))
			{
				File file = new File(fl);
				Set<OWLAxiom> set1 = new HashSet<OWLAxiom>();
				OWLOntology ont1 = ontoman.loadOntologyFromOntologyDocument(file);
				Isosearch iso = new Isosearch();
				for(OWLAxiom ax:ont1.getLogicalAxioms())
				{
					set1.add(ax);
				}
				Explanation<OWLAxiom> exp1 = new Explanation<OWLAxiom>(iso.entailSearch(ont1),set1);
				ontoman.removeOntology(ont1);
				for(String fl2:dir)
				{
					if(!fl2.equals(fl))
					{
						File file2 = new File(fl2);
						OWLOntology ont2 = ontoman.loadOntologyFromOntologyDocument(file2);
						Set<OWLAxiom> set2 = new HashSet<OWLAxiom>();
						for(OWLAxiom ax:ont2.getLogicalAxioms())
						{
							set2.add(ax);
						}
						Explanation<OWLAxiom> exp2 = new Explanation<OWLAxiom>(iso.entailSearch(ont2),set2);
						if(exp1.equals(exp2))
						{
							reducedDir.remove(fl2);
						}
						ontoman.removeOntology(ont2);
					}
				}
			}
		}
		return reducedDir;
	}
	
	public Map<String, ArrayList<String>> cleanList(Map<String, ArrayList<String>> po){
		Set<String> keys = po.keySet();
		for(String root:keys){
			ArrayList<String> oldList = po.get(root);
			ArrayList<String> newDerivedList = new ArrayList<String>(po.get(root));
			ArrayList<String> removals = new ArrayList<String>();
			for(String derived: po.get(root))
			{
				ArrayList<String> checkList = po.get(derived);
				if(!checkList.isEmpty())
				{
					for(String path:checkList)
					{
						if(oldList.contains(path))
						{
							removals.add(path);
						}
					}
					newDerivedList.removeAll(removals);
					po.put(root, newDerivedList);
				}
			}
			
		}
		Map<String, ArrayList<String>> sansDirInfo = new HashMap<String, ArrayList<String>>();
		for(String root:keys)
		{
			String name = root.substring(root.lastIndexOf("/") + 1);
			ArrayList<String> listSansDirInfo = new ArrayList<String>();
			for(String s:po.get(root))
			{
				listSansDirInfo.add(s.substring(s.lastIndexOf("/") + 1));
			}
			sansDirInfo.put(name, listSansDirInfo);
		}
		return sansDirInfo;
	}
	
}
