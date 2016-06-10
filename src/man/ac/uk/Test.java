package man.ac.uk;

import java.io.File;
import java.io.IOException;

import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.model.OWLDataFactory;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;
import org.semanticweb.owlapi.model.OWLOntologyManager;
import org.semanticweb.owlapi.model.OWLOntologyStorageException;


public final class Test {

	/**
	 * @param args
	 * @throws OWLOntologyCreationException 
	 * @throws IOException 
	 * @throws OWLOntologyStorageException 
	 */
	@SuppressWarnings({ "unchecked", "resource", "rawtypes" })
	public static void main(String[] args) throws OWLOntologyCreationException, IOException, OWLOntologyStorageException {
		
		// TODO Auto-generated method stub
		OWLOntologyManager ontoman = OWLManager.createOWLOntologyManager();
		OWLDataFactory df2 = ontoman.getOWLDataFactory();
		df2.getOWLThing();
		File file = new File("/home/michael/corpus/bco/just_5_hermit_IAO_0000109_BFO_0000040_1429882209644.owl");
		OWLOntology just = ontoman.loadOntologyFromOntologyDocument(file);
		Isosearch iso = new Isosearch();
		System.out.println(iso.entailSearch(just));
		
		//CorpusSort
		/**
		CorpusSorter cs = new CorpusSorter();
		File dir = new File("/home/michael/corpus/bco/");
		File dir2 = new File("/home/michael/ext2_equivalence_classes/bco2/");
		cs.eliminateRedundantJusts(dir, dir2);
		
		//Produce axiomlist and get .csv
		/**
		//AxiomSort as = new AxiomSort();
		File dir = new File("/home/michael/corpus/nemo");
		RootSorter rs = new RootSorter();
		CorpusSorter cs = new CorpusSorter();
		OWLOntologyManager ontoman = OWLManager.createOWLOntologyManager();
		List<String> stuff = rs.produceRootList(dir);
		Map<Explanation<OWLAxiom>,String> corpusRoot = new HashMap<Explanation<OWLAxiom>,String>();
		for(String s:stuff)
		{
			File fl = new File(s);
			OWLOntology onto = ontoman.loadOntologyFromOntologyDocument(fl);
			Set<OWLAxiom> just = new HashSet<OWLAxiom>();
			for(OWLAxiom ax:onto.getAxioms())
			{
				if(!ax.isAnnotationAxiom())
				{
					just.add(ax);
				}
			}
			Explanation<OWLAxiom> exp = new Explanation<OWLAxiom>(cs.entailSearch(onto),just);
			corpusRoot.put(exp, s);
		}
		Map<OWLAxiom,ArrayList<String>> axiomShareList = as.constructAxiomShareList(corpusRoot);
		File output = new File("/home/michael/corpus/sharelist_nemo_"+System.currentTimeMillis()+".csv");
		PrintWriter outStr2 = new PrintWriter(new BufferedWriter(new FileWriter(output, true)));
		//System.out.println(cleanedList);
		
		outStr2.println("axiom,j_containing_ax,number");
		for(OWLAxiom ax:axiomShareList.keySet())
		{
			String s = ax.toString();
			//System.out.println(s);
			String line = s.concat(",");
			for(String s2:axiomShareList.get(ax))
			{
				line = line + s2 + ";";
			}
			//System.out.println(line);
			line =  line + "," + axiomShareList.get(ax).size();
			outStr2.println(line);
		}
		outStr2.close();
		//Evaluate and get .csv list of equivalence class root and derived relations.
		/**
		RootSorter rs = new RootSorter();
		File dir = new File("/home/michael/corpus/bco");
		File dir2 = new File("/home/michael/ext2_with_datatype__equivalence_classes_2/bco");
		Map<String,ArrayList<String>> cpo = rs.produceCompletePartialOrder(dir);
		HashMap<String,ArrayList<String>> ecpo = new HashMap<String,ArrayList<String>>();
		Map<String,ArrayList<String>> eqClasses = new HashMap<String,ArrayList<String>>();
		OWLOntologyManager ontoman = OWLManager.createOWLOntologyManager();
		Isosearch is = new Isosearch();
		for(File fl:dir2.listFiles())
		{
			//String equv = fl.getPath().substring(fl.getPath().lastIndexOf("/") + 1) + ",";
			String path = fl.getPath().substring(fl.getPath().lastIndexOf("/") + 1);
			OWLOntology ont = ontoman.loadOntologyFromOntologyDocument(fl);
			Set<OWLAxiom> set = new HashSet<OWLAxiom>();
			for(OWLAxiom ax:ont.getAxioms())
			{
				if(!ax.isAnnotationAxiom())
				{
				set.add(ax);
				}
			}
			ArrayList<String> found  = is.dirSearch(new Explanation<OWLAxiom>(Isosearch.entailSearch(ont),set), dir);
			ontoman.removeOntology(ont);
			ArrayList<String> total = new ArrayList<String>();
			for(String f:found)
			{
				//equv = equv + f.substring(f.lastIndexOf("/")) + ";";
				total.add(f.substring(f.lastIndexOf("/") + 1));
			}
			eqClasses.put(path,total);
		}
		
		
		for(String root:cpo.keySet())
 		{
 			String eq1 = "";
 			for(String name:eqClasses.keySet())
 			{
 				if(eqClasses.get(name).contains(root) && eq1.equals(""))
 				{
 				System.out.println("Got name: " + name + " for root " + root);
 				eq1 = eq1 + name;
 				}
 			}
 			ArrayList<String> relations = new ArrayList<String>();
 			for(String derived:cpo.get(root))
 			{
 				for(String name:eqClasses.keySet())
 	 			{
 	 				if(eqClasses.get(name).contains(derived))
 	 				{
 	 				System.out.println("Got name: " + name + " for derived " + derived);
 	 				relations.add(name);
 	 				}
 	 			}
 			}
 			if(!eq1.equals(""))
 			{
 				if(!ecpo.containsKey(eq1))
 				{
 					System.out.println("Found a new equv class " + eq1); 
 					ecpo.put(eq1, relations);
 				}
 				else
 				{
 					ArrayList<String> update = new ArrayList<String>(ecpo.get(eq1));
 					for(String checkString:relations)
 					{
	 					if(!ecpo.get(eq1).contains(checkString))
	 					{
	 						System.out.println("Found a new relation " + checkString);
	 						update.add(checkString);
	 					}
	 					else{
	 						System.out.println("Relation already present " + checkString);
	 					}
 					}
 					ecpo.put(eq1, update);
 				}
 			}
 			else{
 				System.out.println("Skipped for " + root);
 			}
 			System.out.println("");
 		}
		//for(String s:ecpo.keySet()){
 		//System.out.println(s + " " + ecpo.get(s));
		//}
		
		Map<String,ArrayList<String>> cleanedList = rs.cleanList(ecpo);
		File file2 = new File("/home/michael/ext2_with_datatype__equivalence_classes_2/root_and_derived_analysis/root_and_derived_equ_classes_bco_"+ System.currentTimeMillis() + ".csv");
		PrintWriter outStr2 = new PrintWriter(new BufferedWriter(new FileWriter(file2, true)));
		//System.out.println(cleanedList);
		outStr2.println("name,derived");
		for(String s:cleanedList.keySet())
		{
			//System.out.println(s);
			String line = s.concat(",");
			for(String s2:cleanedList.get(s))
			{
				line = line + s2 + ";";
			}
			//System.out.println(line);
			outStr2.println(line);
		}
		outStr2.close();
		//Obfuscate
		/**
		OWLOntologyManager ontoman = OWLManager.createOWLOntologyManager();
		OWLDataFactory df = ontoman.getOWLDataFactory();
		File dir = new File("/home/michael/ext_equivalence_classes/overallwithoutbugs/");
		String dir2 = "/home/michael/obfuscated/";
		Isosearch is = new Isosearch();
		Isomatch iso = new Isomatch();
		File[] list = dir.listFiles();
		Integer i = 1;
		for(File fl:list)
		{
			OWLOntology ont1 = ontoman.loadOntologyFromOntologyDocument(fl);
			File fl2 = new File(dir2 + "j" + i.toString() + ".owl");
			fl2.canWrite();
			OWLOntology ont2 = is.sigRenamer(ont1, Isosearch.entailSearch(ont1));
			ontoman.saveOntology(ont2, IRI.create(fl2.toURI()));
			ontoman.removeOntology(ont2);
			ontoman.removeOntology(ont1);
			i++;
		}
		
		**/
		//Entailment matcher
		/**
		File fl5 = new File("/home/michael/justifications/errors/map_errors/good_map_1.owl");
		File fl6 = new File("/home/michael/justifications/errors/map_errors/good_map_2.owl");
		OWLReasonerFactory hermitfac = new ReasonerFactory();
		Ext2StrIso e2si = new Ext2StrIso(hermitfac);
		StrictIso si = new StrictIso(hermitfac);
		OWLDataFactory df = OWLManager.getOWLDataFactory();
		OWLOntologyManager ontman = OWLManager.createOWLOntologyManager();
		OWLOntology just3 = ontman.loadOntologyFromOntologyDocument(fl5);
		Set<OWLAxiom> set3 = new HashSet<OWLAxiom>();
		for(OWLAxiom ax:just3.getAxioms())
		{
			if(!ax.isAnnotationAxiom() && !ax.isOfType(AxiomType.DECLARATION)){
				set3.add(ax);
			}
		}
		Explanation<OWLAxiom> exp3 = new Explanation<OWLAxiom>(df.getOWLSubClassOfAxiom(df.getOWLClass(IRI.create("http://abstractnames.com/A")),df.getOWLClass(IRI.create("http://abstractnames.com/B"))),set3);
		ontman.removeOntology(just3);
		OWLOntology just4 = ontman.loadOntologyFromOntologyDocument(fl6);
		Set<OWLAxiom> set4 = new HashSet<OWLAxiom>();
		for(OWLAxiom ax:just4.getAxioms())
		{
			if(!ax.isAnnotationAxiom() && !ax.isOfType(AxiomType.DECLARATION)){
				set4.add(ax);
			}
		}
		Explanation<OWLAxiom> exp4 = new Explanation<OWLAxiom>(df.getOWLSubClassOfAxiom(df.getOWLClass(IRI.create("http://abstractnames.com#X")),df.getOWLClass(IRI.create("http://abstractnames.com#Y"))),set4);
		ontman.removeOntology(just4);
		AxiomTreeBuilder atb = new AxiomTreeBuilder();

        AxiomTreeNode et1 = atb.generateAxiomTree(exp3.getEntailment());
        AxiomTreeNode et2 = atb.generateAxiomTree(exp4.getEntailment());
        List<AxiomTreeNode> entailLeafNodes1 = new ArrayList<AxiomTreeNode>();
        for(AxiomTreeNode entatm:et1.getChildTrees())
        {
        	if(entatm.isLeaf())
        	{
        		entailLeafNodes1.add(entatm);
        	}
        }
        List<AxiomTreeNode> entailLeafNodes2 = new ArrayList<AxiomTreeNode>();
        for(AxiomTreeNode entatm:et2.getChildTrees())
        {
        	if(entatm.isLeaf())
        	{
        		entailLeafNodes2.add(entatm);
        	}
        }
        List<AxiomTreeMapping> candidates = e2si.getMappingCandidates(et1, et2);
        
        AxiomTreeNode jt1 = atb.generateExplanationTree(exp3);
        AxiomTreeNode jt2 = atb.generateExplanationTree(exp4);
        List<AxiomTreeMapping> candidates2 = e2si.getMappingCandidates(jt1, jt2);
        List<AxiomTreeMapping> generalCand = new ArrayList<AxiomTreeMapping>();
        for(AxiomTreeMapping atm:candidates2)
        {
        	atm.printMapping();
        	for(AxiomTreeNode atn:entailLeafNodes1)
        	{
        		System.out.println(atn);
        		System.out.println(atm.containsSource(atn));
        	}
        	System.out.println(atm.containsTarget(et1));
        	if(!atm.contains(et1, et2))
        	{
        		System.out.println("Mapping not containing targets");
        		for(AxiomTreeMapping atm2:candidates)
        		{
        			AxiomTreeMapping atm3 = atm.copy();
        			for(AxiomTreeNode node:atm2.getVarsForSource().keySet())
        			{
        				atm3.addMapping(node, atm2.getTarget(node));
        			}
        			generalCand.add(atm3);
        		}
        	}
        	else
        	{
        		System.out.println("Mapping contained targets");
	        	AxiomTreeMapping atm3 = atm.copy();
	        	generalCand.add(atm3);
        	}
        }
        for(AxiomTreeMapping atm:generalCand)
        {
        	atm.printMapping();
        }
        **/
		//Swapper test code
		/**
		File fl1 = new File("/home/michael/justifications/errors/map_errors/bad_map_1.owl");
		File fl2 = new File("/home/michael/justifications/errors/map_errors/bad_map_2.owl");
		File fl3 = new File("/home/michael/justifications/errors/map_errors/bad_map_3.owl");
		File fl4 = new File("/home/michael/justifications/errors/map_errors/bad_map_4.owl");
		File fl5 = new File("/home/michael/justifications/errors/map_errors/good_map_1.owl");
		File fl6 = new File("/home/michael/justifications/errors/map_errors/good_map_2.owl");
		OWLReasonerFactory hermitfac = new ReasonerFactory();
		Ext2StrIso e2si = new Ext2StrIso(hermitfac);
		StrictIso si = new StrictIso(hermitfac);
		OWLDataFactory df = OWLManager.getOWLDataFactory();
		OWLOntologyManager ontman = OWLManager.createOWLOntologyManager();
		OWLOntology just1 = ontman.loadOntologyFromOntologyDocument(fl1);
		Set<OWLAxiom> set = new HashSet<OWLAxiom>();
		for(OWLAxiom ax:just1.getAxioms())
		{
			if(!ax.isAnnotationAxiom() && !ax.isOfType(AxiomType.DECLARATION)){
				set.add(ax);
			}
		}
		Explanation<OWLAxiom> exp1 = new Explanation<OWLAxiom>(df.getOWLSubClassOfAxiom(df.getOWLClass(IRI.create("http://abstractname.com/A")),df.getOWLClass(IRI.create("http://abstractname.com/D"))),set);
		ontman.removeOntology(just1);
		OWLOntology just2 = ontman.loadOntologyFromOntologyDocument(fl2);
		Set<OWLAxiom> set2 = new HashSet<OWLAxiom>();
		for(OWLAxiom ax:just2.getAxioms())
		{
			if(!ax.isAnnotationAxiom() && !ax.isOfType(AxiomType.DECLARATION)){
				set2.add(ax);
			}
		}
		Explanation<OWLAxiom> exp2 = new Explanation<OWLAxiom>(df.getOWLSubClassOfAxiom(df.getOWLClass(IRI.create("http://abstractnames.com#X")),df.getOWLClass(IRI.create("http://abstractnames.com#K"))),set2);
		ontman.removeOntology(just2);
		OWLOntology just3 = ontman.loadOntologyFromOntologyDocument(fl3);
		Set<OWLAxiom> set3 = new HashSet<OWLAxiom>();
		for(OWLAxiom ax:just3.getAxioms())
		{
			if(!ax.isAnnotationAxiom() && !ax.isOfType(AxiomType.DECLARATION)){
				set3.add(ax);
			}
		}
		Explanation<OWLAxiom> exp3 = new Explanation<OWLAxiom>(df.getOWLSubClassOfAxiom(df.getOWLClass(IRI.create("http://abstractnames.com/G")),df.getOWLClass(IRI.create("http://abstractnames.com/H"))),set3);
		ontman.removeOntology(just3);
		OWLOntology just4 = ontman.loadOntologyFromOntologyDocument(fl4);
		Set<OWLAxiom> set4 = new HashSet<OWLAxiom>();
		for(OWLAxiom ax:just4.getAxioms())
		{
			if(!ax.isAnnotationAxiom() && !ax.isOfType(AxiomType.DECLARATION)){
				set4.add(ax);
			}
		}
		Explanation<OWLAxiom> exp4 = new Explanation<OWLAxiom>(df.getOWLSubClassOfAxiom(df.getOWLClass(IRI.create("http://abstractnames.com#B")),df.getOWLClass(IRI.create("http://abstractnames.com#C"))),set4);
		ontman.removeOntology(just4);
		OWLOntology just5 = ontman.loadOntologyFromOntologyDocument(fl5);
		Set<OWLAxiom> set5 = new HashSet<OWLAxiom>();
		for(OWLAxiom ax:just5.getAxioms())
		{
			if(!ax.isAnnotationAxiom() && !ax.isOfType(AxiomType.DECLARATION)){
				set5.add(ax);
			}
		}
		Explanation<OWLAxiom> exp5 = new Explanation<OWLAxiom>(df.getOWLSubClassOfAxiom(df.getOWLClass(IRI.create("http://abstractnames.com/A")),df.getOWLClass(IRI.create("http://abstractnames.com/B"))),set5);
		ontman.removeOntology(just5);
		OWLOntology just6 = ontman.loadOntologyFromOntologyDocument(fl6);
		Set<OWLAxiom> set6 = new HashSet<OWLAxiom>();
		for(OWLAxiom ax:just6.getAxioms())
		{
			if(!ax.isAnnotationAxiom() && !ax.isOfType(AxiomType.DECLARATION)){
				set6.add(ax);
			}
		}
		Explanation<OWLAxiom> exp6 = new Explanation<OWLAxiom>(df.getOWLSubClassOfAxiom(df.getOWLClass(IRI.create("http://abstractnames.com#X")),df.getOWLClass(IRI.create("http://abstractnames.com#Y"))),set6);
		ontman.removeOntology(just6);
		System.out.println(exp1);
		System.out.println(exp2);
		System.out.println(e2si.equivalent(exp1, exp2));
		System.out.println(exp3);
		System.out.println(exp4);
		System.out.println(e2si.equivalent(exp3, exp4));
		System.out.println(exp5);
		System.out.println(exp6);
		System.out.println(e2si.equivalent(exp5, exp6));
		System.out.println(si.equivalent(exp1, exp2));
		System.out.println(si.equivalent(exp3, exp4));
		System.out.println(si.equivalent(exp5, exp6));
		**/
		/**
		File fl1 = new File("/home/michael/Dropbox/Work/incosnsistent_equv/just_3_jfact_NEMO_3296000_NEMO_3948000_1429893361042.owl");
		File fl2 = new File("/home/michael/Dropbox/Work/incosnsistent_equv/just_3_jfact_NEMO_3948000_NEMO_3296000_1429893281937.owl");
		
		OWLReasonerFactory hermitfac = new ReasonerFactory();
		Ext2StrIso e2si = new Ext2StrIso(hermitfac);
		StrictIso si = new StrictIso(hermitfac);
		CorpusSorter cs = new CorpusSorter();
		OWLOntologyManager ontman = OWLManager.createOWLOntologyManager();
		OWLDataFactory df = OWLManager.getOWLDataFactory();
		/**
		OWLOntology just1 = ontman.loadOntologyFromOntologyDocument(fl1);
		Set<OWLAxiom> set = new HashSet<OWLAxiom>();
		for(OWLAxiom ax:just1.getAxioms())
		{
			if(!ax.isAnnotationAxiom()){
				set.add(ax);
			}
		}
		Explanation<OWLAxiom> exp1 = new Explanation<OWLAxiom>(cs.entailSearch(just1),set);
		ontman.removeOntology(just1);
		OWLOntology just2 = ontman.loadOntologyFromOntologyDocument(fl2);
		Set<OWLAxiom> set2 = new HashSet<OWLAxiom>();
		for(OWLAxiom ax:just2.getAxioms())
		{
			if(!ax.isAnnotationAxiom()){
				set2.add(ax);
			}
		}
		Explanation<OWLAxiom> exp2 = new Explanation<OWLAxiom>(cs.entailSearch(just2),set2);
		System.out.println(exp1);
		System.out.println(exp2);
		
		System.out.println(e2si.equivalent(exp1, exp2));
		**/
		//Spot test
		/**
		OWLSubClassOfAxiom ax1 = 
				df.getOWLSubClassOfAxiom(
						df.getOWLObjectSomeValuesFrom(
								df.getOWLObjectProperty(IRI.create("http://abstractnames.com/#R1")),df.getOWLClass(IRI.create("http://abstractnames.com/#A"))), 
						df.getOWLObjectSomeValuesFrom(
								df.getOWLObjectProperty(IRI.create("http://abstractnames.com/#R2")),df.getOWLClass(IRI.create("http://abstractnames.com/#B"))));
		OWLSubClassOfAxiom ax2 = 
				df.getOWLSubClassOfAxiom(
						df.getOWLObjectSomeValuesFrom(
								df.getOWLObjectProperty(IRI.create("http://abstractnames.com/#R2")),df.getOWLClass(IRI.create("http://abstractnames.com/#B"))), 
						df.getOWLObjectSomeValuesFrom(
								df.getOWLObjectProperty(IRI.create("http://abstractnames.com/#R3")),df.getOWLClass(IRI.create("http://abstractnames.com/#C"))));
		OWLSubClassOfAxiom ax3 = 
				df.getOWLSubClassOfAxiom(
						df.getOWLObjectSomeValuesFrom(
								df.getOWLObjectProperty(IRI.create("http://abstractnames.com/#R1")),df.getOWLClass(IRI.create("http://abstractnames.com/#A"))), 
						df.getOWLObjectSomeValuesFrom(
								df.getOWLObjectProperty(IRI.create("http://abstractnames.com/#R3")),df.getOWLClass(IRI.create("http://abstractnames.com/#C"))));
		OWLSubClassOfAxiom ax4 = 
				df.getOWLSubClassOfAxiom(
						df.getOWLObjectSomeValuesFrom(
								df.getOWLObjectProperty(IRI.create("http://abstractnames.com/#R4")),df.getOWLClass(IRI.create("http://abstractnames.com/#X"))), 
						df.getOWLObjectSomeValuesFrom(
								df.getOWLObjectProperty(IRI.create("http://abstractnames.com/#R5")),df.getOWLClass(IRI.create("http://abstractnames.com/#Y"))));
		OWLSubClassOfAxiom ax5 = 
				df.getOWLSubClassOfAxiom(
						df.getOWLObjectSomeValuesFrom(
								df.getOWLObjectProperty(IRI.create("http://abstractnames.com/#R5")),df.getOWLClass(IRI.create("http://abstractnames.com/#Y"))), 
						df.getOWLObjectSomeValuesFrom(
								df.getOWLObjectProperty(IRI.create("http://abstractnames.com/#R6")),df.getOWLClass(IRI.create("http://abstractnames.com/#Z"))));
		OWLSubClassOfAxiom ax6 = 
				df.getOWLSubClassOfAxiom(
						df.getOWLObjectSomeValuesFrom(
								df.getOWLObjectProperty(IRI.create("http://abstractnames.com/#R6")),df.getOWLClass(IRI.create("http://abstractnames.com/#Z"))), 
						df.getOWLObjectSomeValuesFrom(
								df.getOWLObjectProperty(IRI.create("http://abstractnames.com/#R4")),df.getOWLClass(IRI.create("http://abstractnames.com/#X"))));
		Set<OWLAxiom> set1 = new HashSet<OWLAxiom>();
		Set<OWLAxiom> set2 = new HashSet<OWLAxiom>();
		set1.add(ax1);
		set1.add(ax2);
		set2.add(ax4);
		set2.add(ax5);
		Explanation<OWLAxiom> exp1 = new Explanation<OWLAxiom>(ax3,set1);
		Explanation<OWLAxiom> exp2 = new Explanation<OWLAxiom>(ax6,set2);
		**/
		//System.out.println(e2si.equivalent(exp1, exp2));
		//System.out.println(si.equivalent(exp1, exp2));
	
		/**
		OWLSubClassOfAxiom ax1 = df.getOWLSubClassOfAxiom(df.getOWLClass(IRI.create("http://abstractnames.com/#A")), df.getOWLClass(IRI.create("http://abstractnames.com/#B")));
		OWLSubClassOfAxiom ax2 = df.getOWLSubClassOfAxiom(df.getOWLClass(IRI.create("http://abstractnames.com/#B")), df.getOWLClass(IRI.create("http://abstractnames.com/#C")));
		OWLSubClassOfAxiom ax3 = df.getOWLSubClassOfAxiom(df.getOWLClass(IRI.create("http://abstractnames.com/#A")), df.getOWLClass(IRI.create("http://abstractnames.com/#C")));
		OWLSubClassOfAxiom ax4 = df.getOWLSubClassOfAxiom(df.getOWLClass(IRI.create("http://abstractnames.com/#X")), df.getOWLClass(IRI.create("http://abstractnames.com/#Y")));
		OWLSubClassOfAxiom ax5 = df.getOWLSubClassOfAxiom(df.getOWLClass(IRI.create("http://abstractnames.com/#Y")), df.getOWLClass(IRI.create("http://abstractnames.com/#Z")));
		OWLSubClassOfAxiom ax6 = df.getOWLSubClassOfAxiom(df.getOWLClass(IRI.create("http://abstractnames.com/#X")), df.getOWLClass(IRI.create("http://abstractnames.com/#Z")));
		Set<OWLAxiom> set1 = new HashSet<OWLAxiom>();
		Set<OWLAxiom> set2 = new HashSet<OWLAxiom>();
		set1.add(ax1);
		set1.add(ax2);
		set2.add(ax4);
		set2.add(ax5);
		Explanation<OWLAxiom> exp1 = new Explanation<OWLAxiom>(ax3,set1);
		Explanation<OWLAxiom> exp2 = new Explanation<OWLAxiom>(ax6,set2);
		
		System.out.println(e2si.equivalent(exp1, exp2));
		System.out.println(si.equivalent(exp1, exp2));
		
		//RootSorterTest
		/**
		File dir = new File("/home/michael/justifications_for_isotest/stato/");
		//File just = new File("/home/michael/justifications_for_isotest/bco/IAO_0000109_BFO_0000040/hermit/just_5_hermit_IAO_0000109_BFO_0000040_1429882209644.owl");
		RootSorter rs = new RootSorter();
		ClassBasedEdgeFactory<JustificationVertex,DefaultEdge> ef = new ClassBasedEdgeFactory<JustificationVertex,DefaultEdge>(DefaultEdge.class);
		ClassBasedVertexFactory<JustificationVertex> vf = new ClassBasedVertexFactory<JustificationVertex>(JustificationVertex.class);
				
		SimpleDirectedGraph<JustificationVertex,DefaultEdge> rootDerivedJusts = 
				new SimpleDirectedGraph<JustificationVertex,DefaultEdge>(ef);
		ArrayList<String> roots = rs.produceRootList(dir);
		Map<String,ArrayList<String>> cpo = new HashMap<String,ArrayList<String>>();
		for(String path:roots)
		{
			File just = new File(path);
			cpo.putAll(rs.produceDerivedPartialOrdering(just, dir));			
		}
		Map<String,ArrayList<String>> cpo2 = rs.cleanList(cpo);
		for(String path: cpo2.keySet())
		{
			JustificationVertex vertex = vf.createVertex();
			vertex.path = path.substring(path.lastIndexOf("/") + 1);
			rootDerivedJusts.addVertex(vertex);
		}
		
		for(JustificationVertex jv:rootDerivedJusts.vertexSet())
		{
			for(String filePath:cpo2.keySet())
			{
				if(filePath.contains(jv.path))
				{
					for(String derivedFilePath:cpo2.get(filePath))
					{
						for(JustificationVertex jv2:rootDerivedJusts.vertexSet())
						{
							if(derivedFilePath.contains(jv2.path))
							{
								rootDerivedJusts.addEdge(jv, jv2);
							}
						}
					}
				}
			}
		}
		
		
				
		/**for(String path:cpo.keySet())
		{
			if(!cpo.get(path).isEmpty()){
				for(String derived:cpo.get(path))
				{
					
					
					Boolean cond = false;
					JustificationVertex searchResult = null;
					for(JustificationVertex vertex:rootDerivedJusts.vertexSet())
					{
						if(vertex.path.equals(derived.substring(derived.lastIndexOf("/"))))
						{
							cond = true;
							searchResult = vertex;
						}
					}
					if(cond)
					{
						rootDerivedJusts.addEdge(root, searchResult);
					}
					else
					{
						JustificationVertex der = vf.createVertex();
						der.path = derived.substring(path.lastIndexOf("/") + 1);
						rootDerivedJusts.addVertex(der);
						rootDerivedJusts.addEdge(root, der);
					}
				}
			}
		}
		**/
		/**
		IntegerNameProvider inp = new IntegerNameProvider();
		PathVertexProvider pvp = new PathVertexProvider();
		IntegerEdgeNameProvider ienp = new IntegerEdgeNameProvider();
		StringEdgeNameProvider senp = new StringEdgeNameProvider();
		GmlExporter<JustificationVertex,DefaultEdge> gme = new GmlExporter<JustificationVertex,DefaultEdge>(
				inp,
				pvp,
				ienp,
				senp);
		gme.setPrintLabels(GmlExporter.PRINT_VERTEX_LABELS);
		File dir2 = new File("/home/michael/rootandderived/");
		File save = new File("/home/michael/rootandderived/" +  "stato2_rdgraph" + ".gml");
		FileWriter fw = new FileWriter(save);
		BufferedWriter bw = new BufferedWriter(fw);
		gme.export(fw, rootDerivedJusts);
		**/
		//for(String roots:rs.produceRootList(dir))
		//{
			//System.out.println(roots);
		//}
		
		//rs.produceCompletePartialOrder(dir);
		/**Map<String,ArrayList<String>> dependcies = rs.produceDerivedPartialOrdering(just, dir); 
		
		for(String key:dependcies.keySet())
		{
			System.out.println("ROOT: " + key);
			System.out.println("");
			System.out.println("DEPENDENT JUSTIFICATIONS: ");
			for(String path:dependcies.get(key))
			{
				System.out.println(path);
			}
			System.out.println("");
		}
		**/
		/**
		Isosearch is = new Isosearch();
		
		OWLOntologyManager ontoman = OWLManager.createOWLOntologyManager();
		OWLOntology ont = ontoman.loadOntologyFromOntologyDocument(just);
		Set<OWLAxiom> set = new HashSet<OWLAxiom>();
		for(OWLAxiom ax:ont.getLogicalAxioms())
		{
			set.add(ax);
		}
		Explanation<OWLAxiom> exp = new Explanation<OWLAxiom>(Isosearch.entailSearch(ont),set);
		
		RootSorter rs = new RootSorter();
		System.out.println(rs.getDerivedJustifications(exp, dir));
		**/
		//System.out.println(rs.eliminateDuplicates(rs.getDerivedJustifications(exp, dir)));
		//Code to sort justs with
		/**
		Isomatch ism = new Isomatch();
		File dir = new File("/home/michael/corpus/nemo/");
		File dir2 = new File("/home/michael/ext2_with_datatype__equivalence_classes_2/nemo");
		ism.sortJusts(dir, dir2);
		**/
		/**
		Isosearch is = new Isosearch();
		Isomatch im = new Isomatch();
		File fl = new File("/home/michael/ext2_with_datatype_equivalence_classes/overall2/just_3_jfact_catalysis_entity_1429981283788.owl");
		OWLOntologyManager ontoman = OWLManager.createOWLOntologyManager();
		OWLOntology ont = ontoman.loadOntologyFromOntologyDocument(fl);
		Set<OWLAxiom> j = new HashSet<OWLAxiom>();
		for(OWLAxiom ax:ont.getAxioms())
		{
			if(!ax.isAnnotationAxiom())
			{
				j.add(ax);
			}
		}
		Explanation<OWLAxiom> exp = new Explanation<OWLAxiom>(Isosearch.entailSearch(ont),j);
		File dir2 = new File("/home/michael/corpus/overall");
		ArrayList<String> found = is.dirSearch(exp, dir2);
		for(String s:found)
		{
			System.out.println(s);
		}
		**/
		//Get equivalence class members output to CSV.
		//Also get overlaps in equivalence class, output to CSV.
		/**
		File dir = new File("/home/michael/ext2_with_datatype__equivalence_classes_2/overall");
		File dir2 = new File("/home/michael/corpus/overall");
		File file = new File("/home/michael/ext2_with_datatype__equivalence_classes_2/overall_equivalence_classes_" + System.currentTimeMillis() + ".csv");
		file.canWrite();
		PrintWriter outStr = new PrintWriter(new BufferedWriter(new FileWriter(file, true)));
		outStr.println("name,members");
		Isosearch is = new Isosearch();
		Isomatch ism = new Isomatch();
		OWLOntologyManager ontoman = OWLManager.createOWLOntologyManager();
		Map<String,ArrayList<String>> map = new HashMap<String,ArrayList<String>>();
		for(File fl:dir.listFiles())
		{
			String equv = fl.getPath().substring(fl.getPath().lastIndexOf("/")) + ",";
			String path = fl.getPath().substring(fl.getPath().lastIndexOf("/") + 1);
			OWLOntology ont = ontoman.loadOntologyFromOntologyDocument(fl);
			Set<OWLAxiom> set = new HashSet<OWLAxiom>();
			for(OWLAxiom ax:ont.getAxioms())
			{
				if(!ax.isAnnotationAxiom())
				{
				set.add(ax);
				}
			}
			ArrayList<String> found  = is.dirSearch(new Explanation<OWLAxiom>(Isosearch.entailSearch(ont),set), dir2);
			ontoman.removeOntology(ont);
			ArrayList<String> total = new ArrayList<String>();
			for(String f:found)
			{
				equv = equv + f.substring(f.lastIndexOf("/")) + ";";
				total.add(f.substring(f.lastIndexOf("/") + 1));
			}
			outStr.println(equv);
			map.put(path,total);
		}
		outStr.close();
		File file2 = new File("/home/michael/ext2_with_datatype__equivalence_classes_2/overlaps_"+ System.currentTimeMillis() + ".csv");
		PrintWriter outStr2 = new PrintWriter(new BufferedWriter(new FileWriter(file2, true)));
		outStr2.println("class1,class2,overlap");
		for(String s:map.keySet())
		{
			for(String s2:map.get(s))
			{
				for(String s3:map.keySet()){
					if(map.get(s3).contains(s2) && !s.equals(s3))
					{
						outStr2.println(s+ ","+ s3 + "," + s2);
					}
				}
			}
		}
		outStr2.close();
		**/
		//Get differences, output to CSV
		/**
		File dir = new File("/home/michael/corpus/nemo/");
		File file = new File("/home/michael/corpus/nemo_definitional_"+System.currentTimeMillis()+".csv");
		file.canWrite();
		PrintWriter outStr = new PrintWriter(new BufferedWriter(new FileWriter(file, true)));
		CorpusSorter cs = new CorpusSorter();
		DifferenceChecker dc = new DifferenceChecker();
		ArrayList<String> masterList = cs.corpusList(dir);
		OWLOntologyManager ontoman = OWLManager.createOWLOntologyManager();
		ArrayList<String> defintionalList = new ArrayList<String>();
		for(String name:masterList)
		{
			OWLOntology ont1 = ontoman.loadOntologyFromOntologyDocument(new File(name));
			OWLSubClassOfAxiom ax = cs.entailSearch(ont1);
			Set<OWLAxiom> set = new HashSet<OWLAxiom>();
			for(OWLAxiom ax2:ont1.getLogicalAxioms())
			{
				set.add(ax2);
			}
			Explanation<OWLAxiom> exp = new Explanation<OWLAxiom>(ax,set);
			if(dc.containsDefinitionalAxioms(exp, ax))
			{
				defintionalList.add(name.substring(name.lastIndexOf("/") + 1, name.length()) + "," + dc.findDifferences(exp, ax));
			}
			ontoman.removeOntology(ont1);
		}
		//StringBuilder sb = new StringBuilder();
		outStr.println("name,subclass_classes,supclass_classes,subclass_objprop,supclass_objprop,subclass_dataprop,supclass_dataprop,subclass_datatype,supclass_datatype,subclass_misc,supclass_misc,class_diff,objprop_diff,dataprop_diff,misc_diff");
		for(String s:defintionalList)
		{
			outStr.println(s);
		
		}
		outStr.close();
		**/
		//testing code
		/**
		OWLReasonerFactory hermitfac = new ReasonerFactory();
		Ext2StrIso e2si = new Ext2StrIso(hermitfac);
		
		File bad1 = new File("/home/michael/justifications/errors/baduniquedata.owl");
		File bad2 = new File("/home/michael/justifications/errors/goodunique2.owl");
		File bad3 = new File("/home/michael/justifications/errors/bad3.owl");
		File good1 = new File("/home/michael/justifications/errors/gooduniquedata.owl");
		File good2 = new File("/home/michael/justifications/errors/good2.owl");
		File good3 = new File("/home/michael/justifications/errors/good3.owl");
		OWLOntologyManager ontoman = OWLManager.createOWLOntologyManager();
		OWLDataFactory df = ontoman.getOWLDataFactory();
		OWLSubClassOfAxiom ax = df.getOWLSubClassOfAxiom(df.getOWLClass(IRI.create("http://abstractnames.com/A")), df.getOWLClass(IRI.create("http://abstractnames.com/B")));
		OWLSubClassOfAxiom ax2 = df.getOWLSubClassOfAxiom(df.getOWLClass(IRI.create("http://abstractnames.com/A1")), df.getOWLClass(IRI.create("http://abstractnames.com/B1")));
		OWLOntology b1 = ontoman.loadOntologyFromOntologyDocument(bad1);
		HashSet<OWLAxiom> bs1 = new HashSet<OWLAxiom>();
		for(OWLAxiom ax1:b1.getAxioms())
		{
			if(ax1.isLogicalAxiom())
			{
				bs1.add(ax1);
			}
		}
		System.out.println(b1.getDatatypesInSignature());
		Explanation<OWLAxiom> eb1 = new Explanation<OWLAxiom>(ax, bs1);
		ontoman.removeOntology(b1);
		OWLOntology b2 = ontoman.loadOntologyFromOntologyDocument(bad2);
		HashSet<OWLAxiom> bs2 = new HashSet<OWLAxiom>();
		for(OWLAxiom ax1:b2.getAxioms())
		{
			if(ax1.isLogicalAxiom())
			{
				bs2.add(ax1);
			}
		}
		Explanation<OWLAxiom> eb2 = new Explanation<OWLAxiom>(ax, bs2);	
		ontoman.removeOntology(b2);
		OWLOntology g1 = ontoman.loadOntologyFromOntologyDocument(good1);
		System.out.println(g1.getDatatypesInSignature());
		HashSet<OWLAxiom> gs1 = new HashSet<OWLAxiom>();
		for(OWLAxiom ax1:g1.getAxioms())
		{
			if(ax1.isLogicalAxiom())
			{
				gs1.add(ax1);
			}
		}
		Explanation<OWLAxiom> eg1 = new Explanation<OWLAxiom>(ax, gs1);	
		ontoman.removeOntology(g1);
		OWLOntology g2 = ontoman.loadOntologyFromOntologyDocument(good2);
		HashSet<OWLAxiom> gs2 = new HashSet<OWLAxiom>();
		for(OWLAxiom ax1:g2.getAxioms())
		{
			if(ax1.isLogicalAxiom())
			{
				gs2.add(ax1);
			}
		}
		Explanation<OWLAxiom> eg2 = new Explanation<OWLAxiom>(ax, gs2);
		Isosearch iso = new Isosearch();
		ontoman.removeOntology(g2);
		OWLOntology b3 = ontoman.loadOntologyFromOntologyDocument(bad3);
		HashSet<OWLAxiom> bs3 = new HashSet<OWLAxiom>();
		for(OWLAxiom ax1:b3.getAxioms())
		{
			if(ax1.isLogicalAxiom())
			{
				bs3.add(ax1);
			}
		}
		Explanation<OWLAxiom> eb3 = new Explanation<OWLAxiom>(ax, bs3);
		ontoman.removeOntology(b3);
		OWLOntology g3 = ontoman.loadOntologyFromOntologyDocument(good3);
		HashSet<OWLAxiom> gs3 = new HashSet<OWLAxiom>();
		for(OWLAxiom ax1:g3.getAxioms())
		{
			if(ax1.isLogicalAxiom())
			{
				gs3.add(ax1);
			}
		}
		Explanation<OWLAxiom> eg3 = new Explanation<OWLAxiom>(ax, gs3);
		ontoman.removeOntology(g3);
		System.out.println("bad1 and good1: " + e2si.equivalent(eb1, eg1));
		
		System.out.println("bad2 and good2: " + e2si.equivalent(eb2, eg1));
		System.out.println("bad3 and good3: " + e2si.equivalent(eb3, eg3));
		
		System.out.println("good1 and good2: " + e2si.equivalent(eg1,eg2));
		System.out.println("bad1 and bad2: " + e2si.equivalent(eb1, eb2));
		System.out.println(eb1);
		System.out.println(eg1);
		System.out.println(eb2);
		System.out.println(eg2);
		System.out.println(eb3);
		System.out.println(eg3);
		OWLOntology just1 = ontoman.loadOntologyFromOntologyDocument(new File("/home/michael/justifications/errors/just_3_jfact_NEMO_3296000_NEMO_5608000_1429893306145.owl"));
		OWLOntology just2 = ontoman.loadOntologyFromOntologyDocument(new File("/home/michael/justifications/errors/just_3_jfact_NEMO_3948000_NEMO_5608000_1429893372367.owl"));
		HashSet<OWLAxiom> set1 = new HashSet<OWLAxiom>();
		for(OWLAxiom ax1:just1.getAxioms())
		{
			if(ax1.isLogicalAxiom())
			{
				set1.add(ax1);
			}
		}
		HashSet<OWLAxiom> set2 = new HashSet<OWLAxiom>();
		for(OWLAxiom ax1:just2.getAxioms())
		{
			if(ax1.isLogicalAxiom())
			{
				set2.add(ax1);
			}
		}
		Explanation<OWLAxiom> ex1 = new Explanation<OWLAxiom>(Isosearch.entailSearch(just1),set1);
		Explanation<OWLAxiom> ex2 = new Explanation<OWLAxiom>(Isosearch.entailSearch(just2),set2);
		System.out.println("FUCKUP IS: "+ e2si.equivalent(ex1, ex2));
		
		DifferenceChecker dc = new DifferenceChecker();
		System.out.println(dc.containsDefinitionalAxioms(ex1, (OWLSubClassOfAxiom) ex1.getEntailment()));
		System.out.println(dc.findDifferences(ex1, (OWLSubClassOfAxiom) ex1.getEntailment()));
		/**
		OWLOntologyManager ontoman = OWLManager.createOWLOntologyManager();
		Isosearch iso = new Isosearch();
		OWLOntology just1 = ontoman.loadOntologyFromOntologyDocument(new File("/home/michael/justifications/errors/good_alt2.owl"));
		OWLOntology just2 = ontoman.loadOntologyFromOntologyDocument(new File("/home/michael/justifications/errors/bad_alt2.owl"));
		HashSet<OWLAxiom> set1 = new HashSet<OWLAxiom>();
		for(OWLAxiom ax1:just1.getAxioms())
		{
			if(ax1.isLogicalAxiom())
			{
				set1.add(ax1);
			}
		}
		HashSet<OWLAxiom> set2 = new HashSet<OWLAxiom>();
		for(OWLAxiom ax1:just2.getAxioms())
		{
			if(ax1.isLogicalAxiom())
			{
				set2.add(ax1);
			}
		}
		Explanation<OWLAxiom> ex1 = new Explanation<OWLAxiom>(Isosearch.entailSearch(just1),set1);
		Explanation<OWLAxiom> ex2 = new Explanation<OWLAxiom>(Isosearch.entailSearch(just2),set2);
		System.out.println(ex1);
		System.out.println(ex2);
		System.out.println("FUCKUP IS: "+ e2si.equivalent(ex1, ex2));
		**/
		
		/**
		File dir = new File("/home/michael/ext_equivalence_classes/nemo/just_3_jfact_NEMO_3296000_NEMO_5608000_1429893306145.owl");
		File dir2 = new File("/home/michael/ext2_equivalence_classes/nemo/");
		Isomatch ism = new Isomatch();
		Isosearch iso = new Isosearch();
		OWLOntologyManager ontoman = OWLManager.createOWLOntologyManager();
		OWLOntology ont = ontoman.loadOntologyFromOntologyDocument(dir);
		HashSet<OWLAxiom> set = new HashSet<OWLAxiom>();
		for(OWLAxiom ax:ont.getAxioms())
		{
			if(ax.isLogicalAxiom())
			{
				set.add(ax);
			}
		}
		Explanation<OWLAxiom> just = new Explanation<OWLAxiom>(Isosearch.entailSearch(ont),set);
		File temp = new File("/home/michael/ext2_equivalence_classes/nemo/weird.txt");
		temp.setWritable(true);
		PrintWriter outStr1 = new PrintWriter(new BufferedWriter(new FileWriter(temp, true)));
		BufferedReader in = new BufferedReader(new InputStreamReader(new FileInputStream(iso.dirSearch(just,dir2))));
		String aLine = null;
		while((aLine = in.readLine()) != null)
		{
			outStr1.println(aLine);
		}
		in.close();
		outStr1.close();
		**/
		/**
		File dir = new File("/home/michael/li_equivalence_classes/li_overall");
		File dir2 = new File("/home/michael/li_equivalence_classes/li_overall_obfs/");
		OWLOntologyManager ontoman = OWLManager.createOWLOntologyManager();
		File[] list = dir.listFiles();
		Isosearch iso = new Isosearch();
		int COUNT = 1;
		for(File fl:list)
		{
			File fl2 = new File(dir2.getAbsolutePath() + COUNT + ".owl");
			OWLOntology just = ontoman.loadOntologyFromOntologyDocument(fl);
			OWLOntology newOnt = iso.sigRenamer(just, Isosearch.entailSearch(just));
			ontoman.saveOntology(newOnt,IRI.create(fl2.toURI()));
			ontoman.removeOntology(just);
			ontoman.removeOntology(newOnt);
		}
		**/
		
		//List of isomorphic justs
		/**
		File dir = new File("/home/michael/ext_equivalence_classes/overall");
		File dir2 = new File("/home/michael/justifications_for_isotest/");
		OWLOntologyManager ontoman = OWLManager.createOWLOntologyManager();
		File[] list = dir.listFiles();
		Isosearch iso = new Isosearch();
		File output1 = new File("/home/michael/ext_equivalence_classes/dependent_candidates.txt");
		output1.setWritable(true);
		PrintWriter outStr1 = new PrintWriter(new BufferedWriter(new FileWriter(output1, true)));
	
		for(File fl:list)
		{
			OWLOntology ont = ontoman.loadOntologyFromOntologyDocument(fl);
			HashSet<OWLAxiom> set = new HashSet<OWLAxiom>();
			for(OWLAxiom ax:ont.getAxioms())
			{
				if(!ax.isAnnotated())
				{
					set.add(ax);
				}
			}
			Explanation<OWLAxiom> exp = new Explanation<OWLAxiom>(Isosearch.entailSearch(ont),set);
			String justName = fl.getAbsolutePath().substring(fl.getAbsolutePath().lastIndexOf("/"));
			File equvilenceClasses = File.createTempFile(justName.substring(0,justName.length() - 4) + "_equvilence_classes", ".txt", new File("/home/michael/ext_equivalence_classes/equivalencelists/"));
			equvilenceClasses.setWritable(true);
			PrintWriter outStr2 = new PrintWriter(new BufferedWriter(new FileWriter(equvilenceClasses, true)));
			BufferedReader in = new BufferedReader(new InputStreamReader(new FileInputStream(iso.dirSearch(exp,dir2))));
			String aLine = null;
			while((aLine = in.readLine()) != null)
			{
				outStr2.println(aLine);
			}
			in.close();
			outStr2.close();
			String aLine2 = null;
			BufferedReader in2 = new BufferedReader(new InputStreamReader(new FileInputStream(equvilenceClasses))); 
			while((aLine2 = in2.readLine()) != null)
			{
				outStr2.println(aLine);
			}
		}
		**/
		
		/**for(File fl:list)
		{
			OWLOntology ont = ontoman.loadOntologyFromOntologyDocument(fl);
			HashSet<OWLAxiom> set = new HashSet<OWLAxiom>();
			for(OWLAxiom ax:ont.getAxioms())
			{
				if(!ax.isAnnotated())
				{
					set.add(ax);
				}
			}
			Explanation<OWLAxiom> exp = new Explanation<OWLAxiom>(Isosearch.entailSearch(ont),set);
			String just = fl.getAbsolutePath().substring(fl.getAbsolutePath().lastIndexOf("/"));
			File output = new File("/home/michael/ext_equivalence_classes/equivalencelists/" + just + ".txt");
			output.setWritable(true);
			PrintWriter outStr = new PrintWriter(new BufferedWriter(new FileWriter(output, true)));
			BufferedReader in = new BufferedReader(new InputStreamReader(new FileInputStream(iso.dirSearch(exp,dir2))));
			String aLine = null;
			while((aLine = in.readLine()) != null)
			{
				outStr.println(aLine);
			}
			in.close();
			outStr.close();
			ontoman.removeOntology(ont);
		}
		/**
		File fl1 = new File("/home/michael/justifications_for_isotest/gro/Decrease_Occurrent/pellet/just_2_pellet_Decrease_Occurrent_1429966937147.owl");
		File fl2 = new File("/home/michael/justifications_for_isotest/obcs/OBCS_0000140_OBI_0200000/hermit/just_2_hermit_OBCS_0000140_OBI_0200000_1430065367113.owl");
		OWLOntologyManager ontoman = OWLManager.createOWLOntologyManager();
		OWLDataFactory df = ontoman.getOWLDataFactory();
		OWLSubClassOfAxiom ax1 = df.getOWLSubClassOfAxiom(df.getOWLClass(IRI.create("http://abstractednames.com/A")), df.getOWLClass(IRI.create("http://abstractednames.com/B")));
		OWLSubClassOfAxiom ax2 = df.getOWLSubClassOfAxiom(df.getOWLClass(IRI.create("http://abstractednames.com/B")),df.getOWLClass(IRI.create("http://abstractednames.com/C")));
		OWLSubClassOfAxiom ax3 = df.getOWLSubClassOfAxiom(df.getOWLClass(IRI.create("http://abstractednames.com/X")),df.getOWLClass(IRI.create("http://abstractednames.com/Y")));
		OWLSubClassOfAxiom ax4 = df.getOWLSubClassOfAxiom(df.getOWLClass(IRI.create("http://abstractednames.com/Y")), df.getOWLClass(IRI.create("http://abstractednames.com/Z")));
		OWLSubClassOfAxiom ax5 = df.getOWLSubClassOfAxiom(df.getOWLClass(IRI.create("http://abstractednames.com/X")), df.getOWLClass(IRI.create("http://abstractednames.com/Z")));
		OWLSubClassOfAxiom ax6 = df.getOWLSubClassOfAxiom(df.getOWLClass(IRI.create("http://abstractednames.com/A")), df.getOWLClass(IRI.create("http://abstractednames.com/C")));
		HashSet<OWLAxiom> set3 = new HashSet<OWLAxiom>();
		HashSet<OWLAxiom> set4 = new HashSet<OWLAxiom>();
		set3.add(ax1);
		set4.add(ax2);
		set3.add(ax3);
		set4.add(ax4);
		Explanation<OWLAxiom> exp3 = new Explanation<OWLAxiom>(ax3,set3);
		Explanation<OWLAxiom> exp4 = new Explanation<OWLAxiom>(ax1,set4);
		
		Isosearch iso = new Isosearch();
		
		OWLOntology just1 = ontoman.loadOntologyFromOntologyDocument(fl1);
		OWLOntology just2 = ontoman.loadOntologyFromOntologyDocument(fl2);
		ReasonerFactory hermitfac = new ReasonerFactory();
		OWLReasoner h1 = hermitfac.createReasoner(just1);
		OWLReasoner h2 = hermitfac.createReasoner(just2);
		System.out.println(h1.isEntailed(Isosearch.entailSearch(just1)));
		System.out.println(h2.isEntailed(Isosearch.entailSearch(just2)));
		HashSet<OWLAxiom> set = new HashSet<OWLAxiom>();
		HashSet<OWLAxiom> set2 = new HashSet<OWLAxiom>();
		for(OWLAxiom ax:just1.getAxioms())
		{
			if(!ax.isAnnotationAxiom() && !ax.isOfType(AxiomType.DECLARATION)){
			set.add(ax);
			}
		}
		for(OWLAxiom ax:just2.getAxioms())
		{
			if(!ax.isAnnotationAxiom() && !ax.isOfType(AxiomType.DECLARATION)){
			set2.add(ax);
			}
		}
		OWLSubClassOfAxiom ent = Isosearch.entailSearch(just1);
		OWLSubClassOfAxiom ent2 = Isosearch.entailSearch(just2);
		
		Explanation<OWLAxiom> exp = new Explanation<OWLAxiom>(ent,set);
		Explanation<OWLAxiom> exp2 = new Explanation<OWLAxiom>(ent2,set2);
		
		ontoman.removeOntology(just1);
		ontoman.removeOntology(just2);
		System.out.println(ent.getSuperClass());
		System.out.println(ent2.getSuperClass());
		StrictIso si = new StrictIso(hermitfac);
		ExtStrictIso esi = new ExtStrictIso(hermitfac);
		System.out.println("SI: "+ si.equivalent(exp,exp2));
		System.out.println("EXT SI: "+ esi.equivalent(exp, exp2));
		AxiomTreeBuilder atb = new AxiomTreeBuilder();

	    AxiomTreeNode et1 = atb.generateAxiomTree(exp3.getEntailment());
	    AxiomTreeNode et2 = atb.generateAxiomTree(exp4.getEntailment());

	    AxiomTreeNode jt1 = atb.generateExplanationTree(exp3);
	    AxiomTreeNode jt2 = atb.generateExplanationTree(exp4);
	    System.out.println("SIZE: " + (exp.getSize() == exp2.getSize()));
	    System.out.println(et1.renderTree());
	    System.out.println(jt1.renderTree());
	    System.out.println(et2.renderTree());
	    System.out.println(jt2.renderTree());
	    //@SuppressWarnings("rawtypes")
		/**
	    HashMap<AxiomType, Integer> map = new HashMap<AxiomType, Integer>();
        for (OWLAxiom axiom : exp.getAxioms()) {
            AxiomType t = axiom.getAxiomType();
            if (map.containsKey(t)) {
                int count = map.get(t);
                count++;
                map.put(t, count);
            } else {
                map.put(t, 1);
            }
        }
        HashMap<AxiomType, Integer> map2 = new HashMap<AxiomType, Integer>();
        for (OWLAxiom axiom : exp2.getAxioms()) {
            AxiomType t = axiom.getAxiomType();
            if (map2.containsKey(t)) {
                int count = map2.get(t);
                count++;
                map2.put(t, count);
            } else {
                map2.put(t, 1);
            }
        }
        **/
	    
	    //System.out.println("AXIOM TYPES: " + map.toString() + " " + map2.toString());
	    //System.out.println("MAPS SAME?: " + map.equals(map2));
		/**
	    System.out.println(jt1);
	    System.out.println(jt2);
	    java.util.List<AxiomTreeMapping> cand = esi.getMappingCandidates(jt1, jt2);
	    java.util.List<AxiomTreeMapping> head = esi.getMappingCandidates(et1, et2);
	    ArrayList<AxiomTreeMapping> candidates = new ArrayList<AxiomTreeMapping>();
	    EntailmentChecker ec = new EntailmentChecker(hermitfac);
	    //System.out.println(candidates);
	    for (AxiomTreeMapping c : cand) {
	     	c.printMapping();
		    for (AxiomTreeMapping c2 : head)
		    {
		    	//c2.printMapping();
		    	Set<AxiomTreeNode> keys = c2.getVarsForSource().keySet();
		    	for(AxiomTreeNode n:keys){
		    		if(!c.containsSource(n)){
		    			if(!c.containsTarget(c2.getTarget(n))){
		    				AxiomTreeMapping m = c.copy();
		    				m.addMapping(n, c2.getTarget(n));
		    				candidates.add(m);
		    			}
		    		}
		    	}
		    }
	    
	    //System.out.println(ec.isEntailed(et1, jt1, c.getVarsForSource()) && ec.isEntailed(et2, jt2, c.getVarsForTarget()));
	    }
	    System.out.println("Making candidates");
		for (AxiomTreeMapping c: candidates)
		{
			c.printMapping();
			for (AxiomTreeNode n : jt1.getChildTrees()) {
				System.out.println("Looking at initial nodes");
	            System.out.println(n);
	            for(int i=0;i< n.getChildCount();i++)
	            {
	            	System.out.println("Child: "+ n.getChild(i));
	            }
	            System.out.println("Now mapping");
	            AxiomTreeNode m = ec.copyWithMapping(n, c.getVarsForSource());
	            System.out.println(m);
	            for(int i=0;i< m.getChildCount();i++)
	            {
	            	System.out.println("Child: "+ m.getChild(i));
	            }
	        }
			System.out.println("");
			for (AxiomTreeNode n : jt2.getChildTrees()) {
				System.out.println("Looking at initial nodes");
	            System.out.println(n);
	            for(int i=0;i< n.getChildCount();i++)
	            {
	            	System.out.println("Child: "+n.getChild(i));
	            }
	            System.out.println("Now mapping");
	            AxiomTreeNode m = ec.copyWithMapping(n, c.getVarsForTarget());
	            System.out.println(m);
	            for(int i=0;i< m.getChildCount();i++)
	            {
	            	System.out.println("Child: "+ m.getChild(i));
	            }
	        }
			
			System.out.println(c.getVarsForSource());
			System.out.println(c.getVarsForTarget());
			System.out.println(ec.isEntailed(et1, jt1, c.getVarsForSource()) && ec.isEntailed(et2, jt2, c.getVarsForTarget()));
		}
		//JFrame frame = new JFrame("Manual Nodes");
		//Test for ext strict
		/**
		File fl1 = new File("/home/michael/justifications/obfuscateattempts/extbad/extbad1.owl");
		File fl2 = new File("/home/michael/justifications/obfuscateattempts/extbad/extbad2.owl");
		OWLOntologyManager ontoman = OWLManager.createOWLOntologyManager();
		OWLOntology just3 = ontoman.loadOntologyFromOntologyDocument(fl1);
		OWLDataFactory df = ontoman.getOWLDataFactory();
		OWLSubClassOfAxiom ax1 = df.getOWLSubClassOfAxiom(df.getOWLClass(IRI.create("http://abstractednames.com/A")), df.getOWLClass(IRI.create("http://abstractednames.com/B")));
		OWLSubClassOfAxiom ax2 = df.getOWLSubClassOfAxiom(df.getOWLClass(IRI.create("http://abstractednames.com/B")),df.getOWLClass(IRI.create("http://abstractednames.com/C")));
		OWLSubClassOfAxiom ax3 = df.getOWLSubClassOfAxiom(df.getOWLClass(IRI.create("http://abstractednames.com/X")),df.getOWLClass(IRI.create("http://abstractednames.com/Y")));
		OWLSubClassOfAxiom ax4 = df.getOWLSubClassOfAxiom(df.getOWLClass(IRI.create("http://abstractednames.com/Y")), df.getOWLClass(IRI.create("http://abstractednames.com/Z")));
		OWLSubClassOfAxiom ax5 = df.getOWLSubClassOfAxiom(df.getOWLClass(IRI.create("http://abstractednames.com/X")), df.getOWLClass(IRI.create("http://abstractednames.com/Z")));
		OWLSubClassOfAxiom ax6 = df.getOWLSubClassOfAxiom(df.getOWLClass(IRI.create("http://abstractednames.com/A")), df.getOWLClass(IRI.create("http://abstractednames.com/C")));
		HashSet<OWLAxiom> set = new HashSet<OWLAxiom>();
		HashSet<OWLAxiom> set2 = new HashSet<OWLAxiom>();
		HashSet<OWLAxiom> set3 = new HashSet<OWLAxiom>();
		HashSet<OWLAxiom> set4 = new HashSet<OWLAxiom>();
		set.add(ax1);
		set.add(ax2);
		set2.add(ax3);
		set2.add(ax4);
		OWLOntology just1 = ontoman.createOntology(set);
		OWLOntology just2 = ontoman.createOntology(set2);
		ReasonerFactory hermitfac = new ReasonerFactory();
		OWLReasoner h1 = hermitfac.createReasoner(just1);
		Isosearch iso = new Isosearch();
		//System.out.println(Isosearch.entailSearch(just1));
		System.out.println("For bad just 1, hermit says " + h1.isEntailed(ax3));
		
		for(OWLAxiom ax:just3.getLogicalAxioms())
		{
			if(!ax.isOfType(AxiomType.DECLARATION)){
			set3.add(ax);
			}
		}
		
		OWLOntology just4 = ontoman.loadOntologyFromOntologyDocument(fl2);
		//System.out.println(Isosearch.entailSearch(just2));
		OWLReasoner h2 = hermitfac.createReasoner(just2);
		OWLReasoner h3 = hermitfac.createReasoner(just3);
		OWLReasoner h4 = hermitfac.createReasoner(just4);
		for(OWLAxiom ax: just4.getLogicalAxioms())
		{
			if(!ax.isOfType(AxiomType.DECLARATION)){
			set4.add(ax);
			}
		}
		
		AxiomTreeBuilder atb = new AxiomTreeBuilder();
		Explanation<OWLAxiom> exp1 = new Explanation<OWLAxiom>(ax3,set);
		//AxiomTreeNode atn = atb.generateExplanationTree(exp1);
		//System.out.println(atn.renderTree());
		/**
		JTree tree = new JTree(atn);
	    JScrollPane scrollPane = new JScrollPane(tree);
	    frame.getContentPane().add(scrollPane, BorderLayout.CENTER);
	    frame.setSize(3000, 1500);
	    frame.setVisible(true);
		**/
		//Enumeration dfe = atn.depthFirstEnumeration();
		//while(dfe.hasMoreElements())
		//{
			//System.out.println(dfe.nextElement().toString());
		//}
		
		
		//Checks that explanations are isomorphic and produces mappings.
		//OWLSubClassOfAxiom ax1 = Isosearch.entailSearch(just1);
		//OWLSubClassOfAxiom ax2 = Isosearch.entailSearch(just2);
		
		//Explanation<OWLAxiom> exp1 = new Explanation<OWLAxiom>(ax1,set);
		/**
		Explanation<OWLAxiom> exp2 = new Explanation<OWLAxiom>(ax1,set2);
		Explanation<OWLAxiom> exp3 = new Explanation<OWLAxiom>(ax3,set3);
		Explanation<OWLAxiom> exp4 = new Explanation<OWLAxiom>(ax1,set4);
		Explanation<OWLAxiom> exp5 = new Explanation<OWLAxiom>(ax6,set);
		Explanation<OWLAxiom> exp6 = new Explanation<OWLAxiom>(ax5,set2);
		
		System.out.println("For bad just 2, hermit says " + h2.isEntailed(ax1));
		System.out.println("For bad just 3 and " + ax3 + ", hermit says " + h3.isEntailed(ax3));
		System.out.println("For bad just 4, hermit says " + h4.isEntailed(ax1));
		System.out.println(exp1);
		System.out.println(exp2);
		System.out.println(exp3);
		System.out.println(exp4);
		System.out.println(exp5);
		System.out.println(exp6);
		StrictIso si = new StrictIso(hermitfac);
		SubexIso subIso = new SubexIso(hermitfac);
		ExtStrictIso esi = new ExtStrictIso(hermitfac);
		
		System.out.println("Strict iso for 1 and 2: " + si.equivalent(exp1, exp2));
		System.out.println("Extended Strict iso for 1 and 2: " + esi.equivalent(exp1, exp2));
		System.out.println("Sub iso for 1 and 2: " + subIso.equivalent(exp1, exp2));
		System.out.println("Strict iso for 3 and 4: " + si.equivalent(exp3, exp4));
		System.out.println("Extended Strict iso for 3 and 4: " + esi.equivalent(exp3, exp4));
		System.out.println("Sub iso for 3 and 4: " + subIso.equivalent(exp3, exp4));
		System.out.println("Strict iso for 1 and 4: " + si.equivalent(exp1, exp4));
		System.out.println("Extended Strict iso for 1 and 4: " + esi.equivalent(exp1, exp4));
		System.out.println("Sub iso for 1 and 4: " + subIso.equivalent(exp1, exp4));
	    System.out.println("Strict iso for 5 and 6: " + si.equivalent(exp5, exp6));
	    System.out.println("Extended Strict iso for 5 and 6: " + esi.equivalent(exp5, exp6));
	    System.out.println("Strict iso for 1 and 5: " + si.equivalent(exp1,exp5));
	    System.out.println("Extended Strict iso for 1 and 5: " + si.equivalent(exp1, exp5));
		/**OWLOntology ob1 = iso.sigRenamer(just1, Isosearch.entailSearch(just1));
		OWLOntology ob2 = iso.sigRenamer(just2, Isosearch.entailSearch(just2));
		File fl3 = new File("/home/michael/justifications/obfuscateattempts/good.owl");
		File fl4 = new File("/home/michael/justifications/obfuscateattempts/ugly.owl");
		FileOutputStream fos1 = new FileOutputStream(fl3);
		FileOutputStream fos2 = new FileOutputStream(fl4);
		ontoman.saveOntology(ob1, fos1);
		ontoman.saveOntology(ob2, fos2);
		**/
	
		
		/**System.out.println("");
		for(OWLAxiom ax: ob1.getAxioms())
		{
			System.out.println(ax);
		}
		System.out.println("");
		for(OWLAxiom ax: ob2.getAxioms())
		{
			System.out.println(ax);}
		
		OWLClass A = df.getOWLClass(IRI.create("http://www.abstractednames.com/A"));
		OWLClass B = df.getOWLClass(IRI.create("http://www.abstractednames.com/B"));
		OWLSubClassOfAxiom sub1 = df.getOWLSubClassOfAxiom(A, B);
		Explanation<OWLAxiom> realJust = new Explanation<OWLAxiom>(sub1, ob1.getAxioms());
		Explanation<OWLAxiom> fakeJust = new Explanation<OWLAxiom>(sub1, ob2.getAxioms());
		**/
		/**
		AxiomTreeBuilder atb = new AxiomTreeBuilder();
		AxiomTreeNode et1 = atb.generateAxiomTree(Isosearch.entailSearch(just1));
	    AxiomTreeNode et2 = atb.generateAxiomTree(Isosearch.entailSearch(just2));
	    System.out.println(et1);
	    System.out.println(et2);
	    AxiomTreeNode jt1 = atb.generateExplanationTree(exp);
	    AxiomTreeNode jt2 = atb.generateExplanationTree(exp2);
	    System.out.println(exp.equals(exp2));
	    System.out.println(jt1);
	    System.out.println(jt2);
		java.util.List<AxiomTreeMapping> candidates = subIso.getMappingCandidates(jt1, jt2);
	    EntailmentChecker ec = new EntailmentChecker(hermitfac);
	    for (AxiomTreeMapping c : candidates) {
	      	c.printMapping();
	     System.out.println(ec.isEntailed(et1, jt1, c.getVarsForSource()) && ec.isEntailed(et2, jt2, c.getVarsForTarget()));
	     }
		
	}
		/**
		OWLReasonerFactory hermitfac = new ReasonerFactory();
		OWLOntologyManager ontoman = OWLManager.createOWLOntologyManager();
		OWLDataFactory df = ontoman.getOWLDataFactory();
		OWLClass A = df.getOWLClass(IRI.create("http://cs.man.ac.uk/ont#A"));
		OWLClass B = df.getOWLClass(IRI.create("http://cs.man.ac.uk/ont#B"));
		OWLClass C = df.getOWLClass(IRI.create("http://cs.man.ac.uk/ont#C"));
		OWLClass F = df.getOWLClass(IRI.create("http://cs.man.ac.uk/ont#F"));
		OWLSubClassOfAxiom sub1 = df.getOWLSubClassOfAxiom(A, B);
		OWLSubClassOfAxiom sub2 = df.getOWLSubClassOfAxiom(B, C);
		OWLSubClassOfAxiom realSub = df.getOWLSubClassOfAxiom(A, C);
		OWLSubClassOfAxiom fakeSub = df.getOWLSubClassOfAxiom(A, F);
		SubexIso subIso = new SubexIso(hermitfac);
		HashSet<OWLAxiom> just = new HashSet<OWLAxiom>();
		just.add(sub1);
		just.add(sub2);
		just.add(df.getOWLSubClassOfAxiom(B, A));
		HashSet<OWLAxiom> just2 = new HashSet(just);
		just2.add(df.getOWLSubClassOfAxiom(C, B));
		Explanation<OWLAxiom> realJust = new Explanation<OWLAxiom>(fakeSub, just2);
		Explanation<OWLAxiom> fakeJust = new Explanation<OWLAxiom>(fakeSub, just);
		System.out.println(subIso.equivalent(fakeJust, realJust));
		**/
		//AxiomTreeBuilder check
		/**
		OWLReasonerFactory hermitfac = new ReasonerFactory();
		SubexIso subiso = new SubexIso(hermitfac);
		File fl2 = new File("/home/michael/sb_equivalence_classes/nemo/just_2_jfact_NEMO_0890000_NEMO_7622000_1429893317957.owl");
		File fl1 = new File("/home/michael/equivalence_classes/nemo/just_2_hermit_NEMO_3296000_NEMO_5608000_1429893778733.owl");
		/**
		OWLOntologyManager ontoman = OWLManager.createOWLOntologyManager();
		OWLOntology just1 = ontoman.loadOntologyFromOntologyDocument(fl1);
		OWLOntology just2 = ontoman.loadOntologyFromOntologyDocument(fl2);
		Isosearch iso = new Isosearch();
		**/
		//AxiomTreeBuilder atb = new AxiomTreeBuilder();
		//HashSet<OWLAxiom> set1 = new HashSet();df.getOWLSubClassOfAxio
		//HashSet<OWLAxiom> set2 = new HashSet();
		/**
		for(OWLAxiom ax:just1.getLogicalAxioms())
		{
			set1.add(ax);
		}
		for(OWLAxiom ax:just2.getLogicalAxioms())
		{
		set2.add(ax);
		}
		Explanation<OWLAxiom> e1 = new Explanation<OWLAxiom>(Isosearch.entailSearch(just1),set1);
	    Explanation<OWLAxiom> e2 = new Explanation<OWLAxiom>(Isosearch.entailSearch(just2),set2);
	    **/
		//AxiomTreeNode et1 = atb.generateAxiomTree(fakeSub);
       // AxiomTreeNode et2 = atb.generateAxiomTree(fakeSub);
        //System.out.println(et1);
        //System.out.println(et2);
        //AxiomTreeNode jt1 = atb.generateExplanationTree(realJust);
        //AxiomTreeNode jt2 = atb.generateExplanationTree(fakeJust);
        //System.out.println(realJust.equals(fakeJust));
        //System.out.println(jt1);
        //System.out.println(jt2);
		/**
        java.util.List<AxiomTreeMapping> candidates = subIso.getMappingCandidates(jt1, jt2);
        EntailmentChecker ec = new EntailmentChecker(hermitfac);
        for (AxiomTreeMapping c : candidates) {
        	c.printMapping();
            System.out.println(ec.isEntailed(et1, jt1, c.getVarsForSource()) && ec.isEntailed(et2, jt2, c.getVarsForTarget()));
        }
        **/
		//reasoner checking
		/**
		OWLReasonerFactory hermitfac = new ReasonerFactory();
		File searchDir = new File("/home/michael/sb_equivalence_classes/sb_overall/");
		File[] searchDirFiles = searchDir.listFiles();
		OWLOntologyManager ontoman = OWLManager.createOWLOntologyManager();
		Isosearch iso = new Isosearch();
		for(File fl: searchDirFiles)
		{
			OWLOntology just = ontoman.loadOntologyFromOntologyDocument(fl);
			OWLReasoner hermit = hermitfac.createReasoner(just);
			OWLAxiom ax = Isosearch.entailSearch(just);
			System.out.println(fl.getPath() + ", " + ax + " without precomputation: " + hermit.isEntailed(ax));
			hermit.precomputeInferences(InferenceType.CLASS_HIERARCHY);
			System.out.println(fl.getPath() + ", " + ax + " with precomputation: " + hermit.isEntailed(ax));
			hermit.dispose();
			ontoman.removeOntology(just);
		}
		**/
		//This is a search method...
		/**
		File searchDir = new File("/home/michael/sb_equivalence_classes/sb_overall");
		File strDir = new File("/home/michael/justifications_for_isotest");
		OWLOntologyManager ontoman = OWLManager.createOWLOntologyManager();
		OWLReasonerFactory hermitfac = new ReasonerFactory();
		Isosearch iso = new Isosearch();
		File[] searchDirFiles = searchDir.listFiles();
		File searchResults = new File("/home/michael/li_equivalence_classes/searchresults3.txt");
		searchResults.canWrite();
		PrintWriter outStr = new PrintWriter(new BufferedWriter(new FileWriter(searchResults, true)));
		for(File fl:searchDirFiles)
		{
			OWLOntology just = ontoman.loadOntologyFromOntologyDocument(fl);
			OWLReasoner hermit = hermitfac.createReasoner(just);
			outStr.println(fl.getPath() + " " + hermit.isEntailed(Isosearch.entailSearch(just)));
			hermit.dispose();
			System.out.println(fl.getPath());
			HashSet<OWLAxiom> setJust = new HashSet<OWLAxiom>();
			for(OWLAxiom ax:just.getAxioms())
			{
				if(!ax.isAnnotationAxiom() && !ax.isOfType(AxiomType.DECLARATION))
				{
					setJust.add(ax);
				}
			}
			
			ontoman.removeOntology(just);
			Explanation<OWLAxiom> exp = new Explanation<OWLAxiom>(Isosearch.entailSearch(just),setJust);
			FileInputStream inStream2 = new FileInputStream(iso.dirSearch(exp, strDir));
			BufferedReader in2 = new BufferedReader(new InputStreamReader(inStream2));
			String aLine2 = null;
			System.out.println(in2.ready());
			while((aLine2 = in2.readLine()) != null)
			{
				File fl3 = new File(aLine2);
				OWLOntology check = ontoman.loadOntologyFromOntologyDocument(fl3);
				OWLReasoner r = hermitfac.createReasoner(check);
				outStr.println(aLine2 + " " + r.isEntailed(Isosearch.entailSearch(check)));
				r.dispose();
				ontoman.removeOntology(check);
			}
			in2.close();
     	}
		outStr.close();
		
		
		/**
		File jf = new File("/home/michael/justifications_for_isotest/stato/STATO_0000073_BFO_0000001/jfact/just_3_jfact_STATO_0000073_BFO_0000001_1430089500606.owl");
		OWLOntologyManager ontoman = OWLManager.createOWLOntologyManager();
		OWLOntology just = ontoman.loadOntologyFromOntologyDocument(jf);
		Isosearch is = new Isosearch();
		System.out.println(Isosearch.entailSearch(just));
		**/
		/**Isomatch isom = new Isomatch();
		isom.makeList(dir);
		File savedFile = File.createTempFile("justificationlist", ".txt", dir);
		FileInputStream inStream1 = new FileInputStream(isom.makeList(dir));
		FileOutputStream outStream = new FileOutputStream(savedFile,true);
		byte[] buffer = new byte[1024];
	    int length;
	    while ((length = inStream1.read(buffer)) > 0){
	    	outStream.write(buffer, 0, length);
	    }
	    if (inStream1 != null)inStream1.close();
	    if (outStream != null)outStream.close();
		
		
		
		/**File dir = new File("/home/michael/justifications_for_isotest/");
		Isosearch iso = new Isosearch();
		OWLOntologyManager ontoman = OWLManager.createOWLOntologyManager();
		OWLDataFactory df = ontoman.getOWLDataFactory();
		File file2 = new File("/home/michael/justifications_for_isotest/obi_bcgo/OBI_0200181_OBI_0200000/hermit/just_1_hermit_OBI_0200181_OBI_0200000_1430069967745.owl");
		OWLOntology just = ontoman.loadOntologyFromOntologyDocument(file2);
		Set axioms = just.getLogicalAxioms();
		System.out.println(axioms.size());
		OWLClass sub = df.getOWLClass(IRI.create("http://purl.obolibrary.org/obo/OBI_0200181"));
		OWLClass sup = df.getOWLClass(IRI.create("http://purl.obolibrary.org/obo/OBI_0200000"));
		OWLSubClassOfAxiom sb = df.getOWLSubClassOfAxiom(sub,sup);
		Explanation<OWLAxiom>  exp = new Explanation<OWLAxiom>(sb, axioms);
		File savedFile = File.createTempFile("isoSelfJustifications", ".txt", dir);
		FileInputStream inStream1 = new FileInputStream(iso.dirSearch(exp, dir));
		FileOutputStream outStream = new FileOutputStream(savedFile,true);
		byte[] buffer = new byte[1024];
	    int length;
	    while ((length = inStream1.read(buffer)) > 0){
	    	outStream.write(buffer, 0, length);
	    }
	    if (inStream1 != null)inStream1.close();
	    if (outStream != null)outStream.close();
	    
		/**
		File file = new File("/home/michael/justifications/bco/IAO_0000109_BFO_0000004/hermit/just_6_hermit_IAO_0000109_BFO_0000004_1429882211393.owl");
		OWLOntologyManager ontoman = OWLManager.createOWLOntologyManager();
		OWLDataFactory df = ontoman.getOWLDataFactory();
		OWLOntology just = ontoman.loadOntologyFromOntologyDocument(file);
		Set axioms = just.getLogicalAxioms();
		System.out.println(axioms.size());
		OWLClass sub = df.getOWLClass(IRI.create("http://purl.obolibrary.org/obo/IAO_0000109"));
		OWLClass sup = df.getOWLClass(IRI.create("http://purl.obolibrary.org/obo/BFO_0000004"));
		OWLSubClassOfAxiom sb = df.getOWLSubClassOfAxiom(sub,sup);
		Explanation<OWLAxiom>  exp = new Explanation<OWLAxiom>(sb, axioms);
		System.out.println(exp.getSize());
		
		
	
		/**
		OWLOntologyManager ontoman2 = OWLManager.createOWLOntologyManager();
		OWLDataFactory df2 = ontoman2.getOWLDataFactory();
		
		
		System.out.println(AxiomType.ABoxAxiomTypes);
		System.out.println(AxiomType.TBoxAxiomTypes);
		System.out.println(AxiomType.RBoxAxiomTypes);
		
		
		Map<OWLAxiom,OWLAxiom> ABoxAxiomTests = new HashMap<OWLAxiom,OWLAxiom>();
		
		//Raw elements for tests
		OWLClass A = df2.getOWLClass(IRI.create("http://abstractednames.com/A"));
		OWLClass B = df2.getOWLClass(IRI.create("http://abstractednames.com/B"));
		OWLClass C1 = df2.getOWLClass(IRI.create("http://abstractednames.com/C1"));
		OWLClass C2 = df2.getOWLClass(IRI.create("http://abstractednames.com/C2"));
		OWLClass C3 = df2.getOWLClass(IRI.create("http://abstractednames.com/C3"));
		OWLClass C4 = df2.getOWLClass(IRI.create("http://abstractednames.com/C4"));
		OWLNamedIndividual a = df2.getOWLNamedIndividual(IRI.create("http://abstractednames.com/a"));
		OWLNamedIndividual b = df2.getOWLNamedIndividual(IRI.create("http://abstractednames.com/b"));
		OWLNamedIndividual x1 = df2.getOWLNamedIndividual(IRI.create("http://abstractednames.com/x1"));
		OWLNamedIndividual x2 = df2.getOWLNamedIndividual(IRI.create("http://abstractednames.com/x2"));
		OWLNamedIndividual x3 = df2.getOWLNamedIndividual(IRI.create("http://abstractednames.com/x3"));
		OWLDataProperty E = df2.getOWLDataProperty(IRI.create("http://abstractednames.com/E"));
		OWLDataProperty F = df2.getOWLDataProperty(IRI.create("http://abstractednames.com/F"));
		OWLDataProperty D1 = df2.getOWLDataProperty(IRI.create("http://abstractednames.com/D1"));
		OWLDataProperty D2 = df2.getOWLDataProperty(IRI.create("http://abstractednames.com/D2"));
		OWLDataProperty D3 = df2.getOWLDataProperty(IRI.create("http://abstractednames.com/D3"));
		OWLObjectProperty R = df2.getOWLObjectProperty(IRI.create("http://abstractednames.com/R"));
		OWLObjectProperty S = df2.getOWLObjectProperty(IRI.create("http://abstractednames.com/S"));
		OWLObjectProperty P1 = df2.getOWLObjectProperty(IRI.create("http://abstractednames.com/P1"));
		OWLObjectProperty P2 = df2.getOWLObjectProperty(IRI.create("http://abstractednames.com/P2"));
		OWLObjectProperty P3 = df2.getOWLObjectProperty(IRI.create("http://abstractednames.com/P3"));
		
		
		//Test Examples and Answers
		//Class Assertions	
		ABoxAxiomTests.put(df2.getOWLClassAssertionAxiom(C1,x1),df2.getOWLClassAssertionAxiom(A,a));
		ABoxAxiomTests.put(df2.getOWLClassAssertionAxiom(df2.getOWLObjectIntersectionOf(C1,C2),x1),
				           df2.getOWLClassAssertionAxiom(df2.getOWLObjectIntersectionOf(A,B),a));
		ABoxAxiomTests.put(df2.getOWLClassAssertionAxiom(df2.getOWLObjectIntersectionOf(A,B,C1),x1),
						   df2.getOWLClassAssertionAxiom(df2.getOWLObjectIntersectionOf(C1,C2,C3),a));
		ABoxAxiomTests.put(df2.getOWLClassAssertionAxiom(df2.getOWLObjectSomeValuesFrom(P1,C1),x1),
				  		   df2.getOWLClassAssertionAxiom(df2.getOWLObjectSomeValuesFrom(R,A),a));
		ABoxAxiomTests.put(df2.getOWLClassAssertionAxiom(df2.getOWLObjectSomeValuesFrom(P1,df2.getOWLObjectIntersectionOf(C1,C2)),x1),
		  		   		   df2.getOWLClassAssertionAxiom(df2.getOWLObjectSomeValuesFrom(R,df2.getOWLObjectIntersectionOf(A,B)),a));
		ABoxAxiomTests.put(df2.getOWLClassAssertionAxiom(df2.getOWLObjectSomeValuesFrom(P1,df2.getOWLObjectIntersectionOf(A,B,C1)),x1),
		   			       df2.getOWLClassAssertionAxiom(df2.getOWLObjectSomeValuesFrom(R,df2.getOWLObjectIntersectionOf(C1,C2,C3)),a));
		ABoxAxiomTests.put(df2.getOWLClassAssertionAxiom(df2.getOWLObjectIntersectionOf(df2.getOWLObjectSomeValuesFrom(P1,C1),C2),x1),
				  		   df2.getOWLClassAssertionAxiom(df2.getOWLObjectIntersectionOf(df2.getOWLObjectSomeValuesFrom(R,A),B),a));
		ABoxAxiomTests.put(df2.getOWLClassAssertionAxiom(df2.getOWLObjectIntersectionOf(df2.getOWLObjectSomeValuesFrom(P1,C1),
																						df2.getOWLObjectSomeValuesFrom(P2,C2)),x1),
		  		   		   df2.getOWLClassAssertionAxiom(df2.getOWLObjectIntersectionOf(df2.getOWLObjectSomeValuesFrom(R,A),
		  		   				   														df2.getOWLObjectSomeValuesFrom(S,B)),a));
		ABoxAxiomTests.put(df2.getOWLClassAssertionAxiom(df2.getOWLObjectIntersectionOf(df2.getOWLObjectSomeValuesFrom(P1,
																						df2.getOWLObjectIntersectionOf(A,B)),
						   																df2.getOWLObjectSomeValuesFrom(P2,C1)),x1),
						   df2.getOWLClassAssertionAxiom(df2.getOWLObjectIntersectionOf(df2.getOWLObjectSomeValuesFrom(R,
								   														df2.getOWLObjectIntersectionOf(C1,C2)),
								   														df2.getOWLObjectSomeValuesFrom(S,C3)),a));
		ABoxAxiomTests.put(df2.getOWLClassAssertionAxiom(df2.getOWLObjectIntersectionOf(df2.getOWLObjectSomeValuesFrom(R,A),
																						df2.getOWLObjectSomeValuesFrom(S,B),
																						df2.getOWLObjectSomeValuesFrom(P1,C1)),x1),
						   df2.getOWLClassAssertionAxiom(df2.getOWLObjectIntersectionOf(df2.getOWLObjectSomeValuesFrom(P1,C1),
								   														df2.getOWLObjectSomeValuesFrom(P2,C2),
								   														df2.getOWLObjectSomeValuesFrom(P3,C3)),a));
		ABoxAxiomTests.put(df2.getOWLClassAssertionAxiom(df2.getOWLObjectIntersectionOf(df2.getOWLObjectSomeValuesFrom(R,C1),
																						df2.getOWLObjectSomeValuesFrom(S,C2),
																						df2.getOWLObjectSomeValuesFrom(P1,C2)),x1),
						   df2.getOWLClassAssertionAxiom(df2.getOWLObjectIntersectionOf(df2.getOWLObjectSomeValuesFrom(P1,A),
								   														df2.getOWLObjectSomeValuesFrom(P2,B),
								   														df2.getOWLObjectSomeValuesFrom(P3,B)),a));
		ABoxAxiomTests.put(df2.getOWLClassAssertionAxiom(df2.getOWLDataSomeValuesFrom(D1, df2.getIntegerOWLDatatype()), x1),
						   df2.getOWLClassAssertionAxiom(df2.getOWLDataSomeValuesFrom(E, df2.getIntegerOWLDatatype()), a));
		ABoxAxiomTests.put(df2.getOWLClassAssertionAxiom(
				df2.getOWLObjectIntersectionOf(df2.getOWLDataSomeValuesFrom(E, df2.getIntegerOWLDatatype()),
											   df2.getOWLDataSomeValuesFrom(F, df2.getIntegerOWLDatatype()),
											   df2.getOWLDataSomeValuesFrom(D1, df2.getIntegerOWLDatatype())), x1),
			               df2.getOWLClassAssertionAxiom(
				df2.getOWLObjectIntersectionOf(df2.getOWLDataSomeValuesFrom(D1, df2.getIntegerOWLDatatype()),
											   df2.getOWLDataSomeValuesFrom(D2, df2.getIntegerOWLDatatype()),
											   df2.getOWLDataSomeValuesFrom(D3, df2.getIntegerOWLDatatype())), a));
											   
		//Simple ABox Assertions
		
		//3 or more individuals
		Set<OWLIndividual> moreThen3IndividualsTest = new HashSet<OWLIndividual>();
		moreThen3IndividualsTest.add(a);
		moreThen3IndividualsTest.add(b);
		moreThen3IndividualsTest.add(x1);
		Set<OWLIndividual> moreThen3IndividualsAns = new HashSet<OWLIndividual>();
		moreThen3IndividualsAns.add(x1);
		moreThen3IndividualsAns.add(x2);
		moreThen3IndividualsAns.add(x3);
		
		
		ABoxAxiomTests.put(df2.getOWLSameIndividualAxiom(x1,x2),df2.getOWLSameIndividualAxiom(a,b));
		ABoxAxiomTests.put(df2.getOWLSameIndividualAxiom(moreThen3IndividualsTest),
				           df2.getOWLSameIndividualAxiom(moreThen3IndividualsAns));
		ABoxAxiomTests.put(df2.getOWLDifferentIndividualsAxiom(x1,x2),df2.getOWLDifferentIndividualsAxiom(a,b));
		ABoxAxiomTests.put(df2.getOWLDifferentIndividualsAxiom(moreThen3IndividualsTest),
						   df2.getOWLDifferentIndividualsAxiom(moreThen3IndividualsAns));
		
		ABoxAxiomTests.put(df2.getOWLObjectPropertyAssertionAxiom(P1, x1, x2),df2.getOWLObjectPropertyAssertionAxiom(R, a, b));
		ABoxAxiomTests.put(df2.getOWLNegativeObjectPropertyAssertionAxiom(P1, x1, x2),
						   df2.getOWLNegativeObjectPropertyAssertionAxiom(R, a, b));	
		ABoxAxiomTests.put(df2.getOWLDataPropertyAssertionAxiom(D1, x1, true),
				           df2.getOWLDataPropertyAssertionAxiom(E, a, true));
		ABoxAxiomTests.put(df2.getOWLNegativeDataPropertyAssertionAxiom(D1, x1, df2.getOWLLiteral(true)),
						   df2.getOWLNegativeDataPropertyAssertionAxiom(E, a, df2.getOWLLiteral(true)));	
		
		Map<OWLAxiom,OWLAxiom> TBoxAxiomTests = new HashMap<OWLAxiom, OWLAxiom>();
		
		
		
		//Class Expressions
		//SubClassOf
		TBoxAxiomTests.put(df2.getOWLSubClassOfAxiom(C1, C2), df2.getOWLSubClassOfAxiom(A, B));
		TBoxAxiomTests.put(df2.getOWLSubClassOfAxiom(C3,df2.getOWLObjectIntersectionOf(C4,B)), 
				           df2.getOWLSubClassOfAxiom(A,df2.getOWLObjectIntersectionOf(C1,C2)));
		TBoxAxiomTests.put(df2.getOWLSubClassOfAxiom(df2.getOWLObjectIntersectionOf(C4,C3),A), 
		           		   df2.getOWLSubClassOfAxiom(df2.getOWLObjectIntersectionOf(C1,C2),B));
		TBoxAxiomTests.put(df2.getOWLSubClassOfAxiom(df2.getOWLObjectIntersectionOf(A,C3),df2.getOWLObjectIntersectionOf(B,C2)), 
        		   		   df2.getOWLSubClassOfAxiom(df2.getOWLObjectIntersectionOf(C1,C2),df2.getOWLObjectIntersectionOf(C3,C4)));
		TBoxAxiomTests.put(df2.getOWLSubClassOfAxiom(
						   df2.getOWLObjectSomeValuesFrom(P1, C1),df2.getOWLObjectSomeValuesFrom(P2, C2)),
						   df2.getOWLSubClassOfAxiom(
						   df2.getOWLObjectSomeValuesFrom(R, A),df2.getOWLObjectSomeValuesFrom(S, B)));
		TBoxAxiomTests.put(df2.getOWLSubClassOfAxiom(
						   df2.getOWLObjectSomeValuesFrom(P1, df2.getOWLObjectIntersectionOf(A,C3)),B),
				           df2.getOWLSubClassOfAxiom(
				           df2.getOWLObjectSomeValuesFrom(R, df2.getOWLObjectIntersectionOf(C1,C2)),B));		
		TBoxAxiomTests.put(df2.getOWLSubClassOfAxiom(
				   		   df2.getOWLObjectIntersectionOf(df2.getOWLObjectSomeValuesFrom(R,A),df2.getOWLObjectSomeValuesFrom(S,C4)),C3),
		                   df2.getOWLSubClassOfAxiom(
		                   df2.getOWLObjectIntersectionOf(df2.getOWLObjectSomeValuesFrom(P1,C1),df2.getOWLObjectSomeValuesFrom(P2,C2)),B));
		//
		
		
		//Test Printout
		for(OWLAxiom ax: TBoxAxiomTests.keySet())
		{
			System.out.println("Test: " + ax);
			System.out.println("Answer: " + TBoxAxiomTests.get(ax));
		}
		/**
		File check = new File("/home/michael/justificationstest/gro/Decrease_Nothing/fact/just_2_fact_Decrease_Nothing_1429966937082.owl");
		Isosearch searcher = new Isosearch();
		OWLOntologyManager ontoman = OWLManager.createOWLOntologyManager();
		OWLOntology just = ontoman.loadOntologyFromOntologyDocument(check);
		searcher.autoAnnotate(check);
		searcher.sigRenamer(just, Isosearch.entailSearch(just));
		
		
		/**
		File anotateFile = new File("/home/michael/justificationstest/obcs/OBI_0001873_Nothing/pellet/just_2_pellet_OBI_0001873_Nothing_1430065363219.owl");
		Isosearch searcher = new Isosearch();
		OWLOntologyManager ontoman = OWLManager.createOWLOntologyManager();
		OWLOntology just = ontoman.loadOntologyFromOntologyDocument(anotateFile);
		System.out.println(Isosearch.entailSearch(just));
		System.out.println(searcher.sigRenamer(just, Isosearch.entailSearch(just)));
		**/
		
		//searcher.sigRenamer(just,Isosearch.entailSearch(just));
		//Takes the justification and finds the relevant annotations, returning the sub and superclasses.
		//File dir = new File("/home/michael/justificationstest");
		//File file = new File("/home/michael/justificationstest/bco/IAO_0000109_BFO_0000004/fact/just_6_fact_IAO_0000109_BFO_0000004_1429882208052.owl");
		//Isosearch searcher = new Isosearch();
		//searcher.autoAnnotateDir(dir);
		//OWLOntologyManager ontoman = OWLManager.createOWLOntologyManager();
		//OWLOntology just = ontoman.loadOntologyFromOntologyDocument(file);
		//OWLOntology abs = searcher.sigRenamer(just,Isosearch.entailSearch(just));
		//Set<OWLOntology> setOnt = new HashSet<OWLOntology>();
		//setOnt.add(abs);
		//OWLDataFactory dfac = ontoman.getOWLDataFactory();
		//OWLClass cl1 = dfac.getOWLClass(IRI.create("http://www.abstractednames.com/" + "A"));
		//OWLClass cl2 = dfac.getOWLClass(IRI.create("http://www.abstractednames.com/" + "B"));
		//OWLSubClassOfAxiom sb = dfac.getOWLSubClassOfAxiom(cl1, cl2);
		//Explanation<OWLAxiom> exp = new Explanation<OWLAxiom>(sb,abs.getAxioms());
		//File found = new File("/home/michael/justificationstest/isoFound.txt");
		//searcher.dirSearch(exp, dir).renameTo(found);
		
		
		//searcher.dirSearch(exp, dir);
		/**
		File file = new File("/home/michael/justifications/annotateattempts/protegeannotate.owl");
		OWLOntologyManager ontoman = OWLManager.createOWLOntologyManager();
		OWLOntology annojust = ontoman.loadOntologyFromOntologyDocument(file);
		OWLDataFactory df = ontoman.getOWLDataFactory();
		System.out.println(annojust.getAnnotationPropertiesInSignature());
		Set<OWLClass> anjuClass = annojust.getClassesInSignature();
		OWLClass subclass = null;
		OWLClass supclass = null;
		Boolean cond1 = false;
		Boolean cond2 = false;
		for(OWLClass cl:anjuClass)
		{
			Set<OWLAnnotationAssertionAxiom> anno = cl.getAnnotationAssertionAxioms(annojust);
			if(!anno.isEmpty())
			{
				for(OWLAnnotationAssertionAxiom aax : anno)
				{
					if(aax.getAnnotation().getProperty().toString().contains("supclass") && !cond1)
					{
							cond1 = true;
							supclass = cl;
					}
					else if(aax.getAnnotation().getProperty().toString().contains("subclass") && !cond2)
					{
							cond2 = true;
							subclass = cl;
					}
				}
				
			}
		}
		if(cond1 && cond2)
		{
			System.out.println("Subclass of relevant entailment is: " + subclass);
			System.out.println("Superclass of relevant entailment is: "+ supclass);
		}
		else 
		{
			System.out.println("The respective annotations for sub and superclass are missing from this justification.");
		}
		**/
		
		/**
		//THIS ADDS THE COMMENTS TO THE RESPECTIVE CLASSES
		
		File file = new File("/home/michael/justifications/bco/IAO_0000109_BFO_0000004/fact/just_6_fact_IAO_0000109_BFO_0000004_1429882208052.owl");
		OWLOntologyManager ontoman = OWLManager.createOWLOntologyManager();
		OWLOntology just = ontoman.loadOntologyFromOntologyDocument(file);
		OWLDataFactory df = ontoman.getOWLDataFactory();
		IRI sub = IRI.create("http://purl.obolibrary.org/obo/IAO_0000109");
		IRI sup = IRI.create("http://purl.obolibrary.org/obo/BFO_0000004");
		OWLAnnotation commentSub = df.getOWLAnnotation(df.getRDFSComment(),
                df.getOWLLiteral("subclass", "en"));
		OWLAnnotation commentSup = df.getOWLAnnotation(df.getRDFSComment(),
                df.getOWLLiteral("supclass", "en"));
		OWLAxiom ax1 = df.getOWLAnnotationAssertionAxiom(sub, commentSub);
		OWLAxiom ax2 = df.getOWLAnnotationAssertionAxiom(sup, commentSup);
		ontoman.applyChange(new AddAxiom(just,ax1));
		ontoman.applyChange(new AddAxiom(just,ax2));
		File newfile = new File("/home/michael/justifications/annotate2.owl");
		ontoman.saveOntology(just, IRI.create(newfile.toURI()));
		**/
		
		/**
		File file = new File("/home/michael/justifications/bco/IAO_0000109_BFO_0000004/fact/just_6_fact_IAO_0000109_BFO_0000004_1429882208052.owl");
		Isosearch search = new Isosearch();
		OWLOntology update1 = search.ontoman.loadOntologyFromOntologyDocument(file);
		System.out.println(update1.getAxioms());
		OWLOntology update2 = search.sigRenamer(update1, (search.entailSearch(update1)));
		System.out.println(update2.getAxioms());
		**/
		
		/**
		File file = new File("/home/michael/justifications/bco/IAO_0000109_BFO_0000004/fact/just_6_fact_IAO_0000109_BFO_0000004_1429882208052.owl");
		String path = file.toString();		
		OWLOntologyManager ontoman = OWLManager.createOWLOntologyManager();
		OWLOntology just = ontoman.loadOntologyFromOntologyDocument(file);
		Set<OWLClass> test = just.getClassesInSignature();
		ArrayList<String> classNames = new ArrayList<String>();
		for(OWLClass cl : test)
		{
			int nameBegin = cl.toString().lastIndexOf("/");
			classNames.add(cl.getIRI().toString().substring(nameBegin));
		}
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
					sub = sub + cls;
				}
				else
				{
					sup = sup + cls;
				}
			}
		}
		System.out.println(sub);
		System.out.println(sup);
		OWLClass subcl = null;
		OWLClass supcl = null;
		for(OWLClass cl: test)
		{
			if(cl.toString().contains(sub))
			{
				subcl = cl;
			}
			else if(cl.toString().contains(sup))
			{
				supcl = cl;
			}
		}
		System.out.println(subcl.toString());
		System.out.println(supcl.toString());
		
		
		
		
		
		IRI iri = IRI.create("http://abstractednames.com/");
		ontoman.setOntologyDocumentIRI(just, iri);
		//OWLOntology obsjust = ontoman.createOntology(iri);
		//OWLOntology obsjustattempt = ontoman.createOntology(just.getAxioms(), iri);
		//System.out.println(obsjustattempt.getAxioms());
		Set<OWLOntology> sont = new HashSet<OWLOntology>();
		sont.add(just);
		//sont.add(obsjust);
		OWLDataFactory df = ontoman.getOWLDataFactory();
		OWLClass cl1 = df.getOWLClass(IRI.create("http://purl.obolibrary.org/obo/IAO_0000109"));
		OWLClass cl2 = df.getOWLClass(IRI.create("http://purl.obolibrary.org/obo/BFO_0000004"));
		Set<OWLClass> sbclass = new HashSet<OWLClass>();
		sbclass.add(cl1);
		sbclass.add(cl2);
		OWLSubClassOfAxiom sb = df.getOWLSubClassOfAxiom(cl1, cl2);
		Set<OWLAxiom> ex1 = just.getAxioms();
		Explanation<OWLAxiom> exp1 = new Explanation<OWLAxiom>(sb,ex1);
		ReasonerFactory hermitfac = new ReasonerFactory();
		Configuration configuration=new Configuration();
		Set<OWLClass> justClass = just.getClassesInSignature();
		Set<OWLDataProperty> justDataProp = just.getDataPropertiesInSignature();
		Set<OWLObjectProperty> justObjProp = just.getObjectPropertiesInSignature();
		Set<OWLNamedIndividual> justInd = just.getIndividualsInSignature();
		justClass.removeAll(sbclass);
		int classCount = 1;
		int propCount = 1;
		int dataCount = 1;
		int indCount = 1;
		/**OWLEntityRenamer ontorename = new OWLEntityRenamer(ontoman,sont);
		ontoman.applyChanges(ontorename.changeIRI(sb.getSubClass().asOWLClass(), IRI.create("http://www.abstractednames.com/" + "A")));
		ontoman.applyChanges(ontorename.changeIRI(sb.getSuperClass().asOWLClass(), IRI.create("http://www.abstractednames.com/" + "B")));
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
		Set<OWLAxiom> ex2 = just.getAxioms();
		System.out.println(ex2);
		System.out.println(ex1);
		Explanation<OWLAxiom> exp2 = new Explanation<OWLAxiom>(sb,ex2);
		File newfile = new File("/home/michael/justifications/obfuscateattempts/obscure.owl");
		ontoman.saveOntology(just, IRI.create(newfile.toURI()));
		StrictIso sti = new StrictIso(hermitfac);
		System.out.println("Is the obfuscated justification strictly isomorphic to the original one?");
		if(sti.equivalent(exp1, exp2)){
			System.out.println("True");
			System.out.println("Saving...");
			//File newfile = new File("/home/michael/justifications/obfuscateattempts");
			//ontoman.saveOntology(just, IRI.create(newfile.toURI()));
		}
		else{
			System.out.println("False");
		}
		
		
		/**
        configuration.throwInconsistentOntologyException=false;
        System.out.println(hermitfac.getReasonerName());
        OWLReasoner reasoner = hermitfac.createReasoner(just, configuration);
        ExplanationGeneratorFactory<OWLAxiom> egf = ExplanationManager.createExplanationGeneratorFactory(hermitfac);
        ExplanationGenerator<OWLAxiom> eg1 = egf.createExplanationGenerator(just);
        Explanation<OWLAxiom> exp = new Explanation<OWLAxiom>(sb,ex);
        Set<Explanation<OWLAxiom>> set = eg1.getExplanations(sb, 1);
        StrictIso si = new StrictIso(hermitfac);
        LemmaIso li = new LemmaIso(hermitfac);
        Set<OWLEntity> entSig = sb.getSignature();
		Set<OWLEntity> justSig = new HashSet<OWLEntity>();
		Set<OWLOntology> ontSet = new HashSet<OWLOntology>();
		ontSet.add(just);
		for(OWLAxiom ax: ex)
		{
			justSig.addAll(ax.getSignature());
		}
        System.out.println(entSig);
        System.out.println(justSig);       
        Map justMap = new HashMap<String,String>();
        justSig.removeAll(entSig);
        int classCount = 1;
		int propCount = 1;
		int dataCount = 1;
		int indCount = 1;
        OWLEntityRenamer renamingtool = new OWLEntityRenamer(ontoman,ontSet);       
		for(OWLEntity sigPart:justSig)
		{
			if(sigPart.isOWLClass())
			{
				if(!justMap.containsKey(sigPart))
				{
				IRI iri = IRI.create("http://www.abstractednames.com/" + "C" + classCount);
				renamingtool.changeIRI(sigPart, iri);
				classCount++;
				}
			}
			else if(sigPart.isOWLDataProperty())
			{
				if(!justMap.containsKey(sigPart))
				{
					IRI iri = IRI.create("http://www.abstractednames.com/"+ "D" + dataCount);
					renamingtool.changeIRI(sigPart, iri);
					dataCount++;
				}
			}
			else if(sigPart.isOWLObjectProperty())
			{
				if(!justMap.containsKey(sigPart))
				{
					IRI iri = IRI.create("http://www.abstractednames.com/"+ "R" + propCount);
					renamingtool.changeIRI(sigPart, iri);
					propCount++;
				}
			}
			else if(sigPart.isOWLNamedIndividual())
			{
				if(!justMap.containsKey(sigPart))
				{
					IRI iri = IRI.create("http://www.abstractednames.com/"+ "a" + indCount);
					renamingtool.changeIRI(sigPart, iri);
					indCount++;
				}
			}
		}
		 System.out.println(justMap.toString());
        
        /*System.out.println("Justification:");
        for(OWLAxiom ax: ex)
		{
			System.out.println(ax);
		}
        System.out.println("Produced Justification:");
        for(Explanation<OWLAxiom> explain: set)
        {
    			System.out.println(explain);
    			System.out.println("Is produced exp a just?" + explain.isJustificationEntailment());
        }
        System.out.println("Entailment:");
        System.out.println(sb);
        System.out.println("Is exp a just? " + exp.isJustificationEntailment());
		*/
		/**File file2 = new File("/home/michael/Ontologies/lemmajust1.owl"); 
		File file3 = new File("/home/michael/Ontologies/lemmajust2.owl");
		OWLOntology just2 = ontoman.loadOntologyFromOntologyDocument(file2);
		Set<OWLAxiom> lex1 = just2.getAxioms();
		OWLClass cl3 = df.getOWLClass(IRI.create("http://www.semanticweb.org/michael/ontologies/2015/5/lemmajustification1#A"));
		OWLClass cl4 = df.getOWLClass(IRI.create("http://www.semanticweb.org/michael/ontologies/2015/5/lemmajustification1#D"));
		OWLSubClassOfAxiom sb2 = df.getOWLSubClassOfAxiom(cl3, cl4);
		Explanation<OWLAxiom> lexp1 = new Explanation<OWLAxiom>(sb2,lex1);
		
		ExplanationGenerator<OWLAxiom> eg2 = egf.createExplanationGenerator(just2);
		OWLOntology just3 = ontoman.loadOntologyFromOntologyDocument(file3);
		Set<OWLAxiom> lex2 = just3.getAxioms();
		OWLClass cl5 = df.getOWLClass(IRI.create("http://www.semanticweb.org/michael/ontologies/2015/5/lemmajustification2#A"));
		OWLClass cl6 = df.getOWLClass(IRI.create("http://www.semanticweb.org/michael/ontologies/2015/5/lemmajustification2#F"));
		OWLSubClassOfAxiom sb3 = df.getOWLSubClassOfAxiom(cl5, cl6);
		Explanation<OWLAxiom> lexp2 = new Explanation<OWLAxiom>(sb3,lex2);
		ExplanationGenerator<OWLAxiom> eg3 = egf.createExplanationGenerator(just3);
		
		System.out.println("Strict Isomorphism between justification 1 and 2. Expected answer no. Actual answer: " + si.equivalent(lexp1, lexp2));
		System.out.println("Lemma Isomorphism between justification 1 and 2. Expected answer yes. Actual answer: " + li.equivalent(lexp1, lexp2));
		
		/*
		StrictIso sti = new StrictIso(rf);
		LemmaIso li = new LemmaIso(rf);
		System.out.println("");
		System.out.println(sti.equivalent(exp, exp));
		System.out.println("Lemma Isomorphism:");
		System.out.println(li.equivalent(lexp1, lexp2));
		System.out.println("Strict Isomorphism:");
		System.out.println(sti.equivalent(lexp1, lexp2));
		*/
		}
}


