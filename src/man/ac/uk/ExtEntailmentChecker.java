package man.ac.uk;

import org.semanticweb.owl.explanation.api.Explanation;
import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.model.OWLAxiom;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;
import org.semanticweb.owlapi.model.OWLOntologyManager;
import org.semanticweb.owlapi.reasoner.OWLReasoner;
import org.semanticweb.owlapi.reasoner.OWLReasonerFactory;

import iso.axiomtree.AxiomTreeBuilder;
import iso.axiomtree.AxiomTreeMapping;
import iso.axiomtree.AxiomTreeNode;

import java.security.KeyStore.Entry;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Created by
 * User: SB
 * Date: 18/02/2013
 * Time: 21:10
 *
 */


public class ExtEntailmentChecker {

    OWLReasonerFactory rf;

    public ExtEntailmentChecker(OWLReasonerFactory rf) {
        this.rf = rf;
    }

    public boolean isSameExplanation(Explanation<OWLAxiom> exp1, AxiomTreeNode e1, AxiomTreeNode j1, Map vars1,
    								 Explanation<OWLAxiom> exp2, AxiomTreeNode e2, AxiomTreeNode j2, Map vars2) {
    	Set<OWLAxiom> axioms = new HashSet<OWLAxiom>();
        for (AxiomTreeNode c : j1.getChildTrees()) {
            OWLAxiom ax = convertToAxiom(c, vars1);
            //System.out.println("Looking at converted node: " + c.toString());
            //System.out.println("Converted node children: " + c.getChildTrees());
            if (ax == null) {
                return false;
            }
            axioms.add(ax); 
        }
        OWLAxiom entailment = convertToAxiom(e1, vars1);
        Explanation<OWLAxiom> compExp = new Explanation<OWLAxiom>(entailment,axioms);
        Set<OWLAxiom> axioms2 = new HashSet<OWLAxiom>();
        for (AxiomTreeNode c : j2.getChildTrees()) {
            OWLAxiom ax = convertToAxiom(c, vars2);
            //System.out.println("Looking at converted node: " + c.toString());
            //System.out.println("Converted node children: " + c.getChildTrees());
            if (ax == null) {
                return false;
            }
            axioms2.add(ax); 
        }
        OWLAxiom entailment2 = convertToAxiom(e2, vars2);
        Explanation<OWLAxiom> compExp2 = new Explanation<OWLAxiom>(entailment2,axioms2);
        //System.out.println(compExp);
        //System.out.println(compExp2);
        
        
        AxiomTreeBuilder atb = new AxiomTreeBuilder();
        if(entailment != null && compExp.equals(compExp2)){
        	AxiomTreeNode et = atb.generateAxiomTree(entailment);
        	AxiomTreeNode jt = atb.generateExplanationTree(compExp);
        	//System.out.println(et);
        	//System.out.println(jt.renderTree());
        	Map reverse = new HashMap();
        	for(Object o:vars1.values())
        	{
	        	for(Object o2:vars1.keySet())
	        	{
	        		if(vars1.get(o2).equals(o))
	        		{
	        			reverse.put(o, o2);
	        		}
	        	}
        	}
        	Map reverse2 = new HashMap();
        	for(Object o:vars2.values())
        	{
	        	for(Object o2:vars2.keySet())
	        	{
	        		if(vars2.get(o2).equals(o))
	        		{
	        			reverse2.put(o, o2);
	        		}
	        	}
        	}
        	//System.out.println(reverse);
        	//System.out.println(reverse2);
        	Set<OWLAxiom> axioms3 = new HashSet<OWLAxiom>();
            for (AxiomTreeNode c : j1.getChildTrees()) {
                OWLAxiom ax = convertToAxiom(c, reverse);
                //System.out.println("Looking at converted node: " + c.toString());
                //System.out.println("Converted node children: " + c.getChildTrees());
                if (ax == null) {
                    return false;
                }
                //System.out.println(ax);
                
                axioms3.add(ax); 
            }
            OWLAxiom entailment3 = convertToAxiom(e1, reverse);
            Explanation<OWLAxiom> checkExp1 = new Explanation<OWLAxiom>(entailment3,axioms3);
        	//System.out.println(checkExp1);
        	//System.out.println(exp1.equals(checkExp1));
        	if(exp1.equals(checkExp1))
        	{
        		Set<OWLAxiom> axioms4 = new HashSet<OWLAxiom>();
                for (AxiomTreeNode c : j2.getChildTrees()) {
                    OWLAxiom ax = convertToAxiom(c, reverse2);
                    //System.out.println("Looking at converted node: " + c.toString());
                    //System.out.println("Converted node children: " + c.getChildTrees());
                    if (ax == null) {
                        return false;
                    }
                    //System.out.println(ax);
                    
                    axioms4.add(ax); 
                }
                OWLAxiom entailment4 = convertToAxiom(e2, reverse2);
                Explanation<OWLAxiom> checkExp2 = new Explanation<OWLAxiom>(entailment4,axioms4);
                //System.out.println(checkExp2.getEntailment());
                //System.out.println(checkExp2.getAxioms());
                //System.out.println(exp2.equals(checkExp2));
                return exp2.equals(checkExp2);
        	}
        	else
        	{
        		return false;
        	}
        }
        else{
        	return false;
        }
    }
    
    public boolean isEntailed(AxiomTreeNode e, AxiomTreeNode j, Map vars) {
        OWLOntology ontology = getJustificationOntology(j, vars);
        OWLAxiom entailment = convertToAxiom(e, vars);
        if (ontology == null || entailment == null) {
            return false;
        }

        OWLReasoner r = rf.createReasoner(ontology);
        boolean entailed = r.isEntailed(entailment);
        return entailed;
    }

    private OWLAxiom convertToAxiom(AxiomTreeNode t, Map vars) {
        AxiomTreeNode copy = copyWithMapping(t, vars);
        OWLAxiom axiom = copy.asOWLAxiom();
        //System.out.println(t.getLabel());
        return axiom;
    }

    private OWLOntology getJustificationOntology(AxiomTreeNode j, Map vars) {
        OWLOntologyManager manager = OWLManager.createOWLOntologyManager();
        Set<OWLAxiom> axioms = new HashSet<OWLAxiom>();
        for (AxiomTreeNode c : j.getChildTrees()) {
            OWLAxiom ax = convertToAxiom(c, vars);
            if (ax == null) {
                return null;
            }
            axioms.add(ax);
        }
        OWLOntology ontology = null;
        try {
            ontology = manager.createOntology(axioms);
        } catch (OWLOntologyCreationException e) {
            e.printStackTrace();
        }
        return ontology;

    }


    /**
     * Create a copy of a tree, but use mapping instead
     * @return a copy of the tree using the mapping
     */
    public AxiomTreeNode copyWithMapping(AxiomTreeNode tree, Map vars) {
        try {
            AxiomTreeNode result = null;
            return copyWithMapping(tree, result, vars);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    /**
     * method used by deepCopyWithMapping
     * @param node
     * @param result
     * @return
     * @throws CloneNotSupportedException
     */
    private AxiomTreeNode copyWithMapping(AxiomTreeNode node, AxiomTreeNode result, Map vars) throws CloneNotSupportedException {
    	
        Object var = vars.get(node);
        //System.out.println("Deep copy on node: " + node.getLabel());
        // if the root is null at the beginning, copy the root of teh node tree
        if (result == null) {
            if (var != null) {
                result = new AxiomTreeNode(var);
            } else {
                result = new AxiomTreeNode(node.getLabel());

            }
        }
        // for each child node of the node, add a copy to the result tree
        for (AxiomTreeNode child : node.getChildTrees()) {

            Object cv = vars.get(child);
            AxiomTreeNode childCopy;
            if (cv != null) {
                childCopy = new AxiomTreeNode(cv);
            } else {
                childCopy = new AxiomTreeNode(child.getLabel());

            }
            result.add(childCopy);

            // then recurse to add the children of the child nodes
            copyWithMapping(child, childCopy, vars);
        }
        return result;
    }

}