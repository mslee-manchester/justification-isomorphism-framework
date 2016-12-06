package man.ac.uk;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.semanticweb.HermiT.Reasoner.ReasonerFactory;
import org.semanticweb.owl.explanation.api.Explanation;
import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.model.AxiomType;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLAnnotation;
import org.semanticweb.owlapi.model.OWLAnnotationProperty;
import org.semanticweb.owlapi.model.OWLAxiom;
import org.semanticweb.owlapi.model.OWLDataFactory;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;
import org.semanticweb.owlapi.model.OWLOntologyManager;
import org.semanticweb.owlapi.reasoner.OWLReasoner;
import org.semanticweb.owlapi.reasoner.OWLReasonerFactory;

import iso.checker.PutativeStrictIso;

public class Test {

	public Test() {
		// TODO Auto-generated constructor stub
	}

	public static void main(String[] args) throws OWLOntologyCreationException, IOException {
		// TODO Auto-generated method stub
		/**
		File dir = new File("/home/michael/experiments/ore/ax_swallow_result2/dis");
		File dir2= new File("/home/michael/experiments/ore/ax_swallow_result2/just");
		OWLOntologyManager ontoman = OWLManager.createOWLOntologyManager();
		OWLDataFactory df = ontoman.getOWLDataFactory();
		//OWLAnnotationProperty name = df.getOWLAnnotationProperty(IRI.create("http://owl.cs.manchester.ac.uk/reasoner_verification/vocabulary#generating_ontology"));
		Set<OWLAxiom> disset = new HashSet<OWLAxiom>();
		for(File o:dir.listFiles())
		{
			OWLOntology ont = ontoman.loadOntologyFromOntologyDocument(o);
			for(OWLAxiom ax:ont.getAxioms())
			{
				disset.add(ax.getAxiomWithoutAnnotations());
			}
			ontoman.removeOntology(ont);
		}
		File csv = new File("/home/michael/experiments/ore/ax_swallow_result2/axiom_swallow.csv");
		PrintWriter pw = new PrintWriter(new BufferedWriter(new FileWriter(csv)));
		pw.println("just,axiom_swallow");
		for(File j:dir2.listFiles())
		{
			Boolean cond = false;
			OWLOntology just = ontoman.loadOntologyFromOntologyDocument(j);
			for(OWLAxiom ax:just.getAxioms())
			{
				if(disset.contains(ax))
				{
					cond = true;
				}
			}
			pw.println(j.getAbsolutePath()+","+cond);
			ontoman.removeOntology(just);
		}
		pw.close();
		**/
		//Code to find if MORe ever disagrees with hermit.
		/**
		File dir = new File("/home/michael/experiments/ore/dis/sj_out/");
		OWLOntologyManager ontoman = OWLManager.createOWLOntologyManager();
		ArrayList<String> found = new ArrayList<>();
		for(File fl:dir.listFiles())
		{
			OWLOntology disagreement = ontoman.loadOntologyFromOntologyDocument(fl);
			ArrayList<String> f = new ArrayList<>();
			for(OWLAxiom ax:disagreement.getAxioms())
			{
				//Does hermit1, hermit 2 or more occured in axiom?
				Boolean cond1 = false;
				Boolean cond2 = false;
				Boolean cond3 = false;
				//Has at least one occured in axiom?
				Boolean cond4 = false;
				for(OWLAnnotation anno:ax.getAnnotations())
				{
					if(anno.toString().contains("owl_hermit-linux"))
					{
						cond1 = true;
						cond4 = true;
					}
					else if(anno.toString().contains("owl_hermit-owlapiv4"))
					{
						cond2 = true;
						cond4 = true;
					}
					else if(anno.toString().contains("MOReHermiT-linux"))
					{
						cond3 = true;
						cond4 = true;
					}
				}
				if((!(cond1 && cond3) || !(cond2 && cond3)) && cond4)
				{
					found.add(fl.getName() + "," + ax.getAxiomWithoutAnnotations().toString());
				}
			}
		ontoman.removeOntology(disagreement);
		}
		File csv = new File("/home/michael/experiments/ore/hermit_vs_more_diff_axsw.csv");
		PrintWriter pw = new PrintWriter(new BufferedWriter(new FileWriter(csv)));
		pw.println("ontology,entailment");
		for(String line:found)
		{
			pw.println(line);
		}
		pw.close();
		**/
		
		//Code to find if versions of the same reasoner (hermit) ever disagree or fail.

		File dir = new File("/home/michael/experiments/ore/dis/sj_out/");
		OWLOntologyManager ontoman = OWLManager.createOWLOntologyManager();
		ArrayList<String> found = new ArrayList<>();
		Set<String> ont = new HashSet<>();
		for(File fl:dir.listFiles())
		{
			OWLOntology disagreement = ontoman.loadOntologyFromOntologyDocument(fl);
			for(OWLAxiom ax:disagreement.getAxioms())
			{
				//Does hermit1 or hermit 2 occur in axiom?
				Boolean cond1 = false;
				Boolean cond2 = false;
				//Has at least one occured in axiom?
				Boolean cond3 = false;
				for(OWLAnnotation anno:ax.getAnnotations())
				{
					if(anno.toString().contains("owl_fact++"))
					{
						cond1 = true;
						cond3 = true;
					}
					else if(anno.toString().contains("chainsaw-linux"))
					{
						cond2 = true;
						cond3 = true;
					}
				}
				if(!(cond1 && cond2) && cond3)
				{
					found.add(fl.getName() + "," + ax.getAxiomWithoutAnnotations().toString());
					ont.add(fl.getName());
				}
			}
			ontoman.removeOntology(disagreement);
		}
		
		for(String s:ont)
		{
			System.out.println(s);
		}
		
		File csv = new File("/home/michael/experiments/ore/chainsaw_vs_fact_diff_axsw.csv");
		PrintWriter pw = new PrintWriter(new BufferedWriter(new FileWriter(csv)));
		pw.println("ontology,entailment");
		for(String line:found)
		{
			pw.println(line);
		}
		pw.close();
		/**
		File dir = new File("/home/michael/experiments/ore/ax_swallow_result2/non-axswallow_cases");
		File dir2 = new File("/home/michael/experiments/ore/ax_swallow_result2/non-axswallow_corpus");
		Isomatch ism = new Isomatch();
		ism.sortJusts(dir, dir2);
		/**
		File dir = new File("/home/michael/ore_inferred_ch/reorganized-results/ore2015-classification-dl-linux/disagreement/");
		Isomatch ism = new Isomatch();
		ArrayList<String> dir1List = ism.makeList(dir);
		OWLOntologyManager ontologyManager = OWLManager.createOWLOntologyManager();
		File csv = new File("/home/michael/experiments/ore/problem_files.csv");
		PrintWriter pw = new PrintWriter(new BufferedWriter(new FileWriter(csv)));
		pw.println("inf_ch,status");
		OWLReasonerFactory hermitfac = new ReasonerFactory();
		for(String s:dir1List)
		{
			File ch = new File(s);
			if(ch.length() != 0)
			{
				OWLOntology ont = ontologyManager.loadOntologyFromOntologyDocument(ch);
				if(!ont.isEmpty())
				{
					OWLReasoner r = hermitfac.createNonBufferingReasoner(ont);
					if(r.isConsistent())
					{
						pw.println(s + ",normal");
					}
					else
					{
						pw.println(s + ",inconsistent");
					}
					r.dispose();
				}
				else
				{
					pw.println(s + ",empty");
				}
				ontologyManager.removeOntology(ont);
			}
			else
			{
				pw.println(s + ",blank");
			}
		}
		pw.close();
		/**
		File dir = new File("/home/michael/experiments/iso_rerun/overall/");
		File dir2 = new File("/home/michael/corpus/overall/");
		File csv = new File("/home/michael/experiments/iso_rerun/data/equivalence_classes.csv");
		PrintWriter pw = new PrintWriter(new BufferedWriter(new FileWriter(csv)));
		OWLOntologyManager ontoman = OWLManager.createOWLOntologyManager();
		pw.println("equiv_class,members,size");
		Isomatch ism = new Isomatch();
		Isosearch iso = new Isosearch();
		CorpusSorter cs = new CorpusSorter();
		ArrayList<String> dir1List = ism.makeList(dir);
		for(String s:dir1List)
		{
			File ont = new File(s);
			OWLOntology onto = ontoman.loadOntologyFromOntologyDocument(ont);
			Set<OWLAxiom> set = new HashSet<OWLAxiom>();
			for(OWLAxiom ax:onto.getAxioms())
			{
				if(ax.isLogicalAxiom() && !ax.isOfType(AxiomType.DECLARATION))
				{
					set.add(ax);
				}
			}
			Explanation<OWLAxiom> exp = new Explanation<OWLAxiom>(cs.entailSearch(onto),set);
			ontoman.removeOntology(onto);
			ArrayList<String> found = iso.dirSearch(exp, dir2);
			String line = s + ",";
			for(String f:found)
			{
				line = line + f + ";";
			}
			line = line + "," + found.size();
			pw.println(line);
		}
		pw.close();
		/**
		Isomatch ism = new Isomatch();
		Isosearch iso = new Isosearch();
		CorpusSorter cs = new CorpusSorter();
		File dir = new File("/home/michael/corpus/overall/");
		File dir2 = new File("/home/michael/experiments/iso_rerun/overall/");
		File csv = new File("/home/michael/experiments/iso_rerun/data/timing_overall.csv");
		String ont = "overall";
		long start = System.nanoTime();
		ism.sortJusts(dir, dir2);
		long end = System.nanoTime();
		PrintWriter pw = new PrintWriter(new BufferedWriter(new FileWriter(csv)));
		//pw.println("ontology,time_to_sort");
		pw.println(ont + "," + (end - start));
		pw.close();
		/**
		ArrayList<String> dir1List = ism.makeList(dir);
		ArrayList<String> dir2List = ism.makeList(dir2);
		OWLOntologyManager ontoman = OWLManager.createOWLOntologyManager();
		Map<String,ArrayList<String>> equ = new HashMap<String,ArrayList<String>>();
		for(String s:dir1List)
		{
			File ont = new File(s);
			OWLOntology onto = ontoman.loadOntologyFromOntologyDocument(ont);
			Set<OWLAxiom> set = new HashSet<OWLAxiom>();
			for(OWLAxiom ax:onto.getAxioms())
			{
				if(ax.isLogicalAxiom() && !ax.isOfType(AxiomType.DECLARATION))
				{
					set.add(ax);
				}
			}
			Explanation<OWLAxiom> exp = new Explanation<OWLAxiom>(cs.entailSearch(onto),set);
			ArrayList<String> found = iso.dirSearch(exp, dir2);
			equ.put(s, found);
			ontoman.removeOntology(onto);
		}
		
		File csv = new File("/home/michael/experiments/iswc_repeat/iwsc_repeat_sept/corpus/matches.csv");
		File csv2 = new File("/home/michael/experiments/iswc_repeat/iwsc_repeat_sept/corpus/nomatches.csv");
		PrintWriter pw = new PrintWriter(new BufferedWriter(new FileWriter(csv)));
		PrintWriter pw2 = new PrintWriter(new BufferedWriter(new FileWriter(csv2)));
		pw.println("eq_in_orig,eq_in_rerun");
		pw2.println("no_match");
		for(String s:equ.keySet())
		{
			String line = s + ",";
			for(String f:equ.get(s))
			{
				line = line + f + ";";
			}
			if(equ.get(s).isEmpty())
			{
				pw2.println(s);
			}
			pw.println(line);
		}
		pw.close();
		pw2.close();
		/**
		File dir = new File("/home/michael/ore_dis_onto/");
		Map<String,ArrayList<String>> repeats = new HashMap<String,ArrayList<String>>();
		ArrayList<String> arraylist = new ArrayList<String>();
		for(File fl:dir.listFiles())
		{
			if(fl.toString().contains(".owl"))
			{
				arraylist.add(fl.toString());
			}
		}
		OWLOntologyManager ontoman = OWLManager.createOWLOntologyManager();
		OWLOntologyManager ontoman2 = OWLManager.createOWLOntologyManager();
		while(!arraylist.isEmpty())
		{
			File fl = new File(arraylist.get(0));
			String repeat = arraylist.get(0);
			ArrayList<String> found = new ArrayList<String>();
			arraylist.remove(0);
			OWLOntology check = ontoman.loadOntologyFromOntologyDocument(fl);
			for(String s:arraylist)
			{
				File fl2 = new File(s);
				OWLOntology ont = ontoman2.loadOntologyFromOntologyDocument(fl2);
				if(check.equals(ont))
				{
					found.add(s);
				}
				ontoman2.removeOntology(ont);
			}
			arraylist.removeAll(found);
			repeats.put(repeat, found);
			ontoman.removeOntology(check);
		}
		File csv = new File("/home/michael/experiments/ore/repeats/repeat_ontology.csv");
		PrintWriter pw = new PrintWriter(new BufferedWriter(new FileWriter(csv)));
		pw.println("name,repeats");
		for(String name:repeats.keySet())
		{	
			String found = "";
			for(String s:repeats.get(name))
			{
				found = found + s + ";";
			}
			pw.println(name + "," + found);
		}
		**/
		
		/**
		File fl1 = new File("/home/michael/experiments/iswc_repeat/iwsc_repeat_sept/justs/dikb/just_dikb.drug-interaction-knowledge-base-ontology.3.orig.owl.xml_jfact_463929380.owl");
		File fl2 = new File("/home/michael/experiments/iswc_repeat/iwsc_repeat_sept/justs/dikb/just_dikb.drug-interaction-knowledge-base-ontology.3.orig.owl.xml_jfact_767875311.owl");
		OWLReasonerFactory hermitfac = new ReasonerFactory();
		PutativeStrictIso e2si = new PutativeStrictIso(hermitfac);
		CorpusSorter cs = new CorpusSorter();
		OWLOntologyManager ontoman = OWLManager.createOWLOntologyManager();
		OWLOntology ont1 = ontoman.loadOntologyFromOntologyDocument(fl1);
		Set<OWLAxiom> set1 = new HashSet<OWLAxiom>();
		for(OWLAxiom ax:ont1.getAxioms())
		{
			if(ax.isLogicalAxiom())
			{
				if(!ax.isOfType(AxiomType.DECLARATION))
				{
					set1.add(ax);
				}
			}
		}
		Explanation<OWLAxiom> exp1 = new Explanation<OWLAxiom>(cs.entailSearch(ont1),set1);
		ontoman.removeOntology(ont1);
		OWLOntology ont2 = ontoman.loadOntologyFromOntologyDocument(fl1);
		Set<OWLAxiom> set2 = new HashSet<OWLAxiom>();
		for(OWLAxiom ax:ont2.getAxioms())
		{
			if(ax.isLogicalAxiom())
			{
				if(!ax.isOfType(AxiomType.DECLARATION))
				{
					set2.add(ax);
				}
			}
		}
		Explanation<OWLAxiom> exp2 = new Explanation<OWLAxiom>(cs.entailSearch(ont2),set2);
		System.out.println(e2si.equivalent(exp1, exp2));
		**/
		/**
		 * 
		 * 
		 * 
		File dir = new File(args[0]);
		File dir2 = new File(args[1]);
		File csv = new File(args[2]);
		OWLReasonerFactory hermitfac = new ReasonerFactory();
		ExplanationGeneratorFactory<OWLAxiom> genFac = ExplanationManager
		.createLaconicExplanationGeneratorFactory(hermitfac);
		OWLOntologyManager ontoman = OWLManager.createOWLOntologyManager();
		CorpusSorter cs = new CorpusSorter();
		for(File fl:dir.listFiles())
		{
			OWLOntology o1 = ontoman.loadOntologyFromOntologyDocument(fl);
			ExplanationGenerator<OWLAxiom> leg = genFac.createExplanationGenerator(o1);
			Set<Explanation<OWLAxiom>> found = leg.getExplanations(cs.entailSearch(o1));
			System.out.println(found);
		}
		**/
		/**
		File dir = new File(args[0]);
		File dir2 = new File(args[1]);
		File csv = new File(args[2]);
		OWLOntologyManager ontoman = OWLManager.createOWLOntologyManager();
		Set<OWLAxiom> checkSet = new HashSet<OWLAxiom>();
		for(File fl:dir.listFiles())
		{
			System.out.println(fl.getName());
			OWLOntology o1 = ontoman.loadOntologyFromOntologyDocument(fl);
			for(OWLAxiom ax:o1.getLogicalAxioms())
			{
				checkSet.add(ax.getAxiomWithoutAnnotations());
			}
			ontoman.removeOntology(o1);
		}
		ArrayList<String> list = new ArrayList<String>();
		for(File fl:dir2.listFiles())
		{
			OWLOntology o2 = ontoman.loadOntologyFromOntologyDocument(fl);
			Boolean cond = false;
			for(OWLAxiom ax:o2.getAxioms())
			{
				if(checkSet.contains(ax.getAxiomWithoutAnnotations()))
				{
					cond = true;
				}
			}
			if(!cond)
			{
				list.add(fl.getName());
			}
			ontoman.removeOntology(o2);
		}
		PrintWriter pw = new PrintWriter(new BufferedWriter(new FileWriter(csv)));
		pw.println("just_without_axiom_swallowing");
		for(String s:list)
		{
			pw.println(s);
		}
		**/
		
		/**
		
		CorpusSorter cs = new CorpusSorter();
		File dir = new File("/home/michael/experiments/iswc_repeat/iwsc_repeat_sept/justs/");
		File dir2 = new File("/home/michael/experiments/iswc_repeat/iwsc_repeat_sept/corpus/overall2/");
		Isomatch ism = new Isomatch();
		ism.sortJusts(dir, dir2);
	
		/** 
		OWLReasonerFactory hermitfac = new ReasonerFactory();
		Ext2StrIso e2si = new Ext2StrIso(hermitfac);
		CorpusSorter cs = new CorpusSorter();
		File dir = new File(args[0]);
		File dir2 = new File(args[1]);
		File csv = new File(args[2]);
		OWLOntologyManager ontoman = OWLManager.createOWLOntologyManager();
		OWLOntology just = ontoman.loadOntologyFromOntologyDocument(file);
		Set<OWLAxiom> set1 = new HashSet<OWLAxiom>();
		for(OWLAxiom ax:just.getAxioms())
		{
			if(ax.isLogicalAxiom())
			{
				set1.add(ax);
			}
		}
		OWLAxiom ent1 = cs.entailSearch(just);
		ontoman.removeOntology(just);
		OWLOntology just2 = ontoman.loadOntologyFromOntologyDocument(file2);
		Set<OWLAxiom> set2 = new HashSet<OWLAxiom>();
		for(OWLAxiom ax:just2.getAxioms())
		{
			if(ax.isLogicalAxiom())
			{
				set2.add(ax);
			}
		}
		OWLAxiom ent2 = cs.entailSearch(just2);
		ontoman.removeOntology(just2);
		Explanation<OWLAxiom> exp1 = new Explanation<OWLAxiom>(ent1,set1);
		Explanation<OWLAxiom> exp2 = new Explanation<OWLAxiom>(ent2,set2);
		
		/**
		OWLReasonerFactory hermitfac = new ReasonerFactory();
		Ext2StrIso e2si = new Ext2StrIso(hermitfac);
		CorpusSorter cs = new CorpusSorter();
		File dir = new File(args[0]);
		File dir2 = new File(args[1]);
		File csv = new File(args[2]);
		File missing_equv_csv = new File(args[3]);
		File no_equv_csv = new File(args[4]);
		PrintWriter pw = new PrintWriter(new BufferedWriter(new FileWriter(csv)));
		pw.println("equivalence_class,members_in_corpus");
		OWLOntologyManager ontoman = OWLManager.createOWLOntologyManager();
		ArrayList<String> matchedDir2Files = new ArrayList<String>();
		ArrayList<String> noMatchDir = new ArrayList<String>();
		ArrayList<String> noMatchDir2 = new ArrayList<String>();
		
		for(File fl:dir.listFiles())
		{
			String list = fl.getName() + ",";
			OWLOntology o1 = ontoman.loadOntologyFromOntologyDocument(fl);
			Set<OWLAxiom> set1 = new HashSet<OWLAxiom>();
			for(OWLAxiom ax:o1.getLogicalAxioms())
			{
				set1.add(ax);
			}
			System.out.println("ENTAIL S: " + fl.getName());
			Explanation<OWLAxiom> e1 = new Explanation<OWLAxiom>(cs.entailSearch(o1),set1);
			ontoman.removeOntology(o1);
			for(File fl2:dir2.listFiles())
			{
				OWLOntology o2 = ontoman.loadOntologyFromOntologyDocument(fl2);
				Set<OWLAxiom> set2 = new HashSet<OWLAxiom>();
				for(OWLAxiom ax:o2.getLogicalAxioms())
				{
					set2.add(ax);
				}
			
				System.out.println("ENTAIL S: " + fl2.getName());
				Explanation<OWLAxiom> e2 = new Explanation<OWLAxiom>(cs.entailSearch(o2),set2);
				ontoman.removeOntology(o2);
				if(e2si.equivalent(e1, e2))
				{
					list = list + fl2.getName() + ";";
					matchedDir2Files.add(fl2.getName());
				}
			}
			pw.println(list);
			if(list.equals(fl.getName() + ","))
			{
				noMatchDir.add(fl.getName());
			}
		}
		pw.flush();
		for(File fl2:dir2.listFiles())
		{
			if(!matchedDir2Files.contains(fl2.getName()))
			{
				noMatchDir2.add(fl2.getName());
			}
		}
		PrintWriter pw2 = new PrintWriter(new BufferedWriter(new FileWriter(missing_equv_csv)));
		for(String s:noMatchDir)
		{
			pw2.println(s);
		}
		pw2.flush();
		PrintWriter pw3 = new PrintWriter(new BufferedWriter(new FileWriter(no_equv_csv)));
		for(String s:noMatchDir)
		{
			pw3.println(s);
		}
		pw3.flush();
		**/
	}
	
}
