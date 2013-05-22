package at.ac.tuwien.infosys.www.pixy.analysis.interprocedural.functional;

import at.ac.tuwien.infosys.www.pixy.analysis.LatticeElement;
import at.ac.tuwien.infosys.www.pixy.analysis.TransferFunction;
import at.ac.tuwien.infosys.www.pixy.analysis.interprocedural.Context;
import at.ac.tuwien.infosys.www.pixy.analysis.interprocedural.InterproceduralAnalysisNode;
import at.ac.tuwien.infosys.www.pixy.conversion.cfgnodes.AbstractCfgNode;
import at.ac.tuwien.infosys.www.pixy.conversion.cfgnodes.Call;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * An AnalysisNode holds analysis-specific information for a certain CFGNode.
 *
 * @author Nenad Jovanovic <enji@seclab.tuwien.ac.at>
 */
public class FunctionalAnalysisNode extends InterproceduralAnalysisNode {
    // mapping input LatticeElement -> Set of context LatticeElements;
    // only needed for call nodes
    Map<LatticeElement, Set<FunctionalContext>> reversePhi;

// *********************************************************************************
// CONSTRUCTORS ********************************************************************
// *********************************************************************************

    public FunctionalAnalysisNode(AbstractCfgNode node, TransferFunction tf) {
        super(tf);
        // maintain reverse mapping for call nodes
        if (node instanceof Call) {
            this.reversePhi = new HashMap<>();
        } else {
            this.reversePhi = null;
        }
    }

// *********************************************************************************
// GET *****************************************************************************
// *********************************************************************************

    // returns a set of contexts that are mapped to the given value
    Set<FunctionalContext> getReversePhiContexts(LatticeElement value) {
        return this.reversePhi.get(value);
    }

// *********************************************************************************
// SET *****************************************************************************
// *********************************************************************************

    // sets the PHI value for the given context
    protected void setPhiValue(Context contextX, LatticeElement value) {

        FunctionalContext context = (FunctionalContext) contextX;

        super.setPhiValue(context, value);

        // maintain reverse mapping, if needed
        if (this.reversePhi != null) {
            Set<FunctionalContext> contextSet = this.reversePhi.get(value);
            if (contextSet == null) {
                contextSet = new HashSet<>();
                this.reversePhi.put(value, contextSet);
            }
            contextSet.add(context);
        }
    }
}