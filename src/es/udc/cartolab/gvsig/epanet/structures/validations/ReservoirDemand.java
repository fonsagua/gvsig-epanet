package es.udc.cartolab.gvsig.epanet.structures.validations;

import es.udc.cartolab.gvsig.epanet.structures.NodeWrapper;
import es.udc.cartolab.gvsig.epanet.structures.ReservoirWrapper;

public class ReservoirDemand implements NodeChecker {
    @Override
    public Warning check(NodeWrapper node) {
	if (node instanceof ReservoirWrapper) {
	    if (node.getDemand() > 0) {
		return new Warning(
			"Aviso de cálculo hidráulico: Algún embalse del sistema actúa como recpetor del agua y no como emisor");
	    }
	}
	return null;
    }
}
