package es.udc.cartolab.gvsig.epanet.structures.validations;

import es.udc.cartolab.gvsig.epanet.structures.JunctionWrapper;
import es.udc.cartolab.gvsig.epanet.structures.NodeWrapper;

public class SourcePressure implements NodeChecker {

    @Override
    public Warning check(NodeWrapper node) {
	// TODO: Not a really good form to check if the node is a Source
	if ((node instanceof JunctionWrapper) && (node.getDemand() < 0)) {
	    final double pressure = node.getPressure();
	    if (pressure > 0) {
		return new Warning(
			"Hay fuentes donde la energía no es suficiente");
	    } else if (pressure < -2) {
		return new Warning("Sobra energía en una fuente");
	    }
	}
	return null;
    }
}
