package es.udc.cartolab.gvsig.epanet.structures.validations;

import es.udc.cartolab.gvsig.epanet.structures.JunctionWrapper;
import es.udc.cartolab.gvsig.epanet.structures.NodeWrapper;

public class NegativePressure implements NodeChecker {

    @Override
    public Warning check(NodeWrapper node) {
	if (node instanceof JunctionWrapper) {
	    if (node.getDemand() > 0) {
		if (node.getPressure() < 0) {
		    return new Warning(
			    "Aviso de cálculo hidráulico: Existen conexiones con demanda con presiones negativas");
		}
	    }
	}
	return null;
    }

}
