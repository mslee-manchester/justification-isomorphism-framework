package man.ac.uk;

import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.model.AxiomType;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLDatatype;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;
import org.semanticweb.owlapi.model.OWLOntologyManager;
import org.semanticweb.owlapi.model.OWLSubClassOfAxiom;

import iso.axiomtree.AxiomTreeBuilder;
import iso.axiomtree.AxiomTreeMapping;
import iso.axiomtree.AxiomTreeNode;
import iso.axiomtree.EntailmentChecker;
import iso.checker.AbstractIsoChecker;
import iso.checker.IsoUtil;

import org.semanticweb.owl.explanation.api.Explanation;
import org.semanticweb.owlapi.model.OWLAxiom;
import org.semanticweb.owlapi.model.OWLObject;
import org.semanticweb.owlapi.reasoner.OWLReasoner;
import org.semanticweb.owlapi.reasoner.OWLReasonerFactory;
import org.semanticweb.owlapi.vocab.OWL2Datatype;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;


/**
 * Created by
 * User: SB
 * Date: 29/11/2012
 * Time: 20:57
 *
 */


public class Ext2StrIso  {

	OWLReasonerFactory rf;	
    public Ext2StrIso(OWLReasonerFactory rf2) {
        this.rf = rf2;
    }

    public boolean equivalent(Explanation<OWLAxiom> e1, Explanation<OWLAxiom> e2)  throws OWLOntologyCreationException {
        // true if explanations are equivalent
    	//System.out.println(e1);
    	//System.out.println(e2);
        if (e1.equals(e2)) {
            return true;
        }

        // check if they have the same size, same entailment types, same numbers of axiom types
        IsoUtil pre = new IsoUtil();
        if (!pre.passesPretest(e1, e2)) {
        	//System.out.println("NOT THE SAME!");
            return false;
        }
        
        //check the current datatypes used in the justifications. If they don't match, reject this.
        HashSet<OWLDatatype> dataTypes1 = new HashSet<OWLDatatype>();
        HashSet<OWLDatatype> dataTypes2 = new HashSet<OWLDatatype>();
        
        for(OWLAxiom ax:e1.getAxioms())
        {
        	 for(OWLDatatype dt:ax.getDatatypesInSignature())
             {
             	if(dt.isBuiltIn())
             	{
             		dataTypes1.add(dt);
             	}
             }
        }
        for(OWLAxiom ax:e2.getAxioms())
        {
        	 for(OWLDatatype dt:ax.getDatatypesInSignature())
             {
             	if(dt.isBuiltIn())
             	{
             		dataTypes2.add(dt);
             	}
             }
        }      
        
        if(!dataTypes1.equals(dataTypes2))
        {
        	return false;
        }
       
        //check to see if both justifications are both real or fake w.r.t. reasoner
        /**
        OWLOntologyManager ontoman = OWLManager.createOWLOntologyManager();
        Boolean E1ENT = null;
        OWLReasoner j1 = rf.createReasoner(ontoman.createOntology(e1.getAxioms()));
		E1ENT = j1.isEntailed(e1.getEntailment());
		j1.flush();
		OWLReasoner j2;
	    Boolean E2ENT = null;
	    j2 = rf.createReasoner(ontoman.createOntology(e2.getAxioms()));
		E2ENT = j2.isEntailed(e2.getEntailment());
		j2.flush();
		System.out.println(e1.getEntailment().toString() + ": " + E1ENT + " " + e2.getEntailment().toString() + ": " + E2ENT);
		System.out.println(!((E1ENT && E2ENT) || (!E1ENT && !E2ENT)));
		if(!((E1ENT && E2ENT) || (!E1ENT && !E2ENT))) {
		System.out.println("Entailment check failed");
	    return false;	
		}
		**/
		// otherwise do the full check	
        AxiomTreeBuilder atb = new AxiomTreeBuilder();

        AxiomTreeNode et1 = atb.generateAxiomTree(e1.getEntailment());
        AxiomTreeNode et2 = atb.generateAxiomTree(e2.getEntailment());
        
        AxiomTreeNode jt1 = atb.generateExplanationTree(e1);
        AxiomTreeNode jt2 = atb.generateExplanationTree(e2);
        
        //for(AxiomTreeMapping atm:getMappingCandidates(et1,et2))
        //{
        	//atm.printMapping();
        //}
       
        List<AxiomTreeMapping> candidates = getMappingCandidates(jt1, jt2);
        //System.out.println(candidates.size());
        /**
       for(AxiomTreeMapping c : candidates) {
    	  System.out.println(c.getVarsForTarget());
    	  System.out.println(c.getVarsForSource());
    	  System.out.println(isCorrectMapping(e1, et1, jt1, c.getVarsForSource(), e2, et2, jt2, c.getVarsForTarget()));
       }
       **/
        
        
       for (AxiomTreeMapping c : candidates) {
       //System.out.println("\n--------------- MAPPING ---------------\n");
          if (isCorrectMapping(e1, et1, jt1, c.getVarsForSource(), e2, et2, jt2, c.getVarsForTarget())) {
              //c.printMapping();
              //System.out.println("TRUE MAPPING:");
              //System.out.println(c.getVarsForTarget());
              //System.out.println(c.getVarsForSource());
        	  return true;
           }
        }
       
        //for(AxiomTreeMapping atm:candidates)
        //{
        	//atm.printMapping();
        //}
        return false;
    }

    /**
     * main method that returns yes or no if the trees are/aren't equivalent
     * @param t1 first tree
     * @param t2 second tree
     * @return
     */
    public List<AxiomTreeMapping> getMappingCandidates(AxiomTreeNode t1, AxiomTreeNode t2) {
        if (t1 != null && t2 != null) {
            // main call to match the trees
            List<AxiomTreeMapping> mappingCandidates = match(t1, t2, new AxiomTreeMapping());
            return mappingCandidates;
        }
        return Collections.emptyList();
    }

    private boolean isCorrectMapping(Explanation<OWLAxiom> exp1, AxiomTreeNode et1, AxiomTreeNode jt1, Map vars1, Explanation<OWLAxiom> exp2, AxiomTreeNode et2, AxiomTreeNode jt2, Map vars2) {																			
        //EntailmentChecker checker = new EntailmentChecker(rf);
//        System.out.println(vars);
        //if(checker.isEntailed(et, jt, vars) && entval){
        	//return true;
        //}
        //else if(!checker.isEntailed(et, jt, vars) && !entval)
        //{
        	//return true;
        //}
        //else
        //{
        	//return false;
        //}
    	ExtEntailmentChecker checker = new ExtEntailmentChecker(rf);
    	return checker.isSameExplanation(exp1, et1, jt1, vars1, exp2, et2, jt2, vars2);
    }

    private List<AxiomTreeMapping> match(AxiomTreeNode t1, AxiomTreeNode t2, AxiomTreeMapping mapping) {
    	//System.out.println("Currently attempting to match: " + t1.toString() + " " + t2.toString());
    	//System.out.println("Current state of mapping: ");
    	//mapping.printMapping();
    	//System.out.println("-------------------------");
    	//System.out.println("");
    	//System.out.println(mapping.getVarsForSource());
    	//System.out.println(mapping.getVarsForTarget());
        // if the current mapping already contains the two root notes, continue with children
        if (mapping.contains(t1, t2)) {
        	
            return matchChildren(t1, t2, mapping);
        }

        // else if the root nodes match and we haven't yet found a mapping for the node in t2
        if (!mapping.containsTarget(t2) && isMatch(t1, t2, mapping)) {
        	
            // copy the mapping and add the two nodes to the existing mapping
            mapping = mapping.copy();
            
            if (t1.isLeaf() && t2.isLeaf()) {
            		mapping.addMapping(t1, t2);
            		//System.out.println("LEAF MAP");
            		//System.out.println("Current state of mapping: ");
            		//mapping.printMapping();
            		//System.out.println("-------------------------");
                	//System.out.println("");
            		//System.out.println("SHARED: " + t1.getParent().toString());              
	                //ystem.out.println("Leaf to leaf map: " + t1.toString() + " " + t2.toString());          	
            } 

            // then continue with the child nodes
            return matchChildren(t1, t2, mapping);
        }

        // otherwise if the nodes don't match, return an empty list of mappings
        //System.out.println("Childe nodes don't match.");
        return Collections.emptyList();
    }


    private List<AxiomTreeMapping> matchChildren(AxiomTreeNode t1, AxiomTreeNode t2, AxiomTreeMapping mapping) {

        return matchNodeLists(getChildNodeList(t1), getChildNodeList(t2), mapping);
    }

    /**
     * matches lists of child nodes of a tree - shortcut for looping over child nodes
     * @param t1s
     * @param t2s
     * @param mapping
     * @return
     */
    private List<AxiomTreeMapping> matchNodeLists(List<AxiomTreeNode> t1s, List<AxiomTreeNode> t2s, AxiomTreeMapping mapping) {

        // no more child nodes left
        if (t1s.isEmpty()) {
            return Collections.singletonList(mapping);
        }

        List<AxiomTreeMapping> matches = new ArrayList<AxiomTreeMapping>();

        // get the first child node in the list
        AxiomTreeNode t1 = t1s.get(0);
        List<AxiomTreeNode> t1sReduced = reduceNodeList(t1s, 0);
        //System.out.println("FIRST CHILD NODES: " + t1sReduced);
        // loop over child nodes in t2s
        for (int i = 0; i < t2s.size(); i++) {
            AxiomTreeNode t2 = t2s.get(i);
            List<AxiomTreeNode> t2sReduced = reduceNodeList(t2s, i);
            //System.out.println("SECOND CHILD NODES: " + t2sReduced);
            // try and match the first element in t1s and some element in t2
            for (AxiomTreeMapping match : match(t1, t2, mapping)) {
                matches.addAll(matchNodeLists(t1sReduced, t2sReduced, match));
            }
        }
        
        return matches;
    }


    /**
     * checks whether two nodes are compatible wrt the current mapping
     * @param t1
     * @param t2
     * @param mapping
     * @return
     */
    private boolean isMatch(AxiomTreeNode t1, AxiomTreeNode t2, AxiomTreeMapping mapping) {

        boolean match;
        //System.out.println("MATCHING: " + t1 + " " + t2);
        //System.out.println("t1: " + t1);
        //System.out.println("t2: " + t2);
        // nodes must have the same number of children
        if (t1.getChildCount() != t2.getChildCount()) {
            match = false;
            //System.out.println("DIFFERENT CHILD COUNT");
            //System.out.println("");
            return match;
        }

        // string matching for operators
        if (isOperator(t1) && isOperator(t2)) {
            match = t1.sameLabel(t2);
           // System.out.println("OPERATORS. Label matching is: "+ match);
           // System.out.println("");
            return match;
        } else {
            // else check whether the nodes violate an existing mapping
            if (compatibleNodeTypes(t1, t2) && !mapping.violatesStrictMapping(t1, t2)) {
            	//System.out.println("AT LEAST ONE NON OPERATOR. Compatible.");
            	//System.out.println("");
                return true;
            }
        }
        //System.out.println("Fails all tests.");
        //System.out.println("");
        return false;

    }

    private boolean compatibleNodeTypes(AxiomTreeNode t1, AxiomTreeNode t2) {
    	//System.out.println("t1 NODE TYPE: "+ t1.getNodeType());
    	//System.out.println("t2 NODE TYPE: "+ t2.getNodeType());
    	//System.out.println("t1 LABEL: "+ t1.getLabel());
    	//System.out.println("t2 LABEL: "+ t2.getLabel());
        if (!t1.getNodeType().equals(t2.getNodeType())) {
            return false;
        }
        
        switch (t1.getNodeType()) {
            case CARD:
                int label1 = (Integer) t1.getLabel();
                int label2 = (Integer) t2.getLabel();
                return (label1 == label2);
                	
            case OWLOBJECT:
                OWLObject o1 = (OWLObject) t1.getLabel();
                OWLObject o2 = (OWLObject) t2.getLabel();
                //System.out.println(o1.getClass());
                //System.out.println(o2.getClass());
                //Check for datatypes - then check datatype to see match. Else, return true if compatible.
                if(o1.getClass() == o2.getClass())
                {
                	if(!o1.getDatatypesInSignature().isEmpty())
                	{
                		//Need to check if built in first. First check is convenience, datatypes should match
                		//If one is built in, so should other. If neither is built in, this is also a viable match
                		//If not equal and at least one is built in, its not a match.
                		OWLDatatype dt1 = (OWLDatatype) o1.getDatatypesInSignature().toArray()[0];
            			OWLDatatype dt2 = (OWLDatatype) o2.getDatatypesInSignature().toArray()[0];
            			//System.out.println("DT1: " + dt1);
            			//System.out.println("DT2: " + dt2);
                		if(dt1.equals(dt2) && dt1.isBuiltIn())
                		{	
                			//System.out.println("Standard state");
                			//System.out.println(o1.equals(o2));
                			return o1.equals(o2);
                		}
                		else if(!dt1.isBuiltIn() && !dt2.isBuiltIn())
                		{
                			//System.out.println("Unique state");
                			return true;
                  		}
                		else
                		{
                			//System.out.println("Rejection state");
                			return false;
                		}
    
                	}
                	else
                	{
                		return true;
                	}
                }
                else
                {
                	return false;
                }
        }
        return false;
    }

    public boolean isOperator(AxiomTreeNode n) {
        switch (n.getNodeType()) {
            case AXIOMTYPE:
                return true;
            case EXPRESSIONTYPE:
                return true;
            case INVERSEOF:
                return true;
            case ROOT:
                return true;
        }
        return false;
    }

    /**
     * parses child nodes of a tree node into a simple list
     * @param t the tree node
     * @return the list of children
     */
    private List<AxiomTreeNode> getChildNodeList(AxiomTreeNode t) {

        List<AxiomTreeNode> list = new ArrayList<AxiomTreeNode>();

        for (int i = 0; i < t.getChildCount(); i++) {

            list.add((AxiomTreeNode) t.getChildAt(i));
        }

        return list;
    }


    /**
     * removes an element from a list
     * @param list   the list
     * @param remove the element to be removed
     * @return a copy of the list without the element
     */
    private List<AxiomTreeNode> reduceNodeList(List<AxiomTreeNode> list, int remove) {

        List<AxiomTreeNode> reduced = new ArrayList<AxiomTreeNode>(list);

        reduced.remove(remove);

        return reduced;
    }


}
