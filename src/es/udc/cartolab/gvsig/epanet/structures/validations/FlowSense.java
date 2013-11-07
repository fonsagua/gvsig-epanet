package es.udc.cartolab.gvsig.epanet.structures.validations;

import es.udc.cartolab.gvsig.epanet.structures.LinkWrapper;
import es.udc.cartolab.gvsig.epanet.structures.PipeWrapper;
import es.udc.cartolab.gvsig.epanet.structures.PumpWrapper;

public class FlowSense implements LinkChecker {

    @Override
    public Warning check(LinkWrapper link) {
	if ((link instanceof PipeWrapper) || (link instanceof PumpWrapper)) {
	    if (link.getFlow() < 0) {
		return new Warning(
			"Aviso de cálculo hidráulico: Existen tuberías donde el agua discurre en sentido contrario al deseado");
	    }
	}
	return null;
    }

}
