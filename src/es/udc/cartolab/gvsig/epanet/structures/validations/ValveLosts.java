package es.udc.cartolab.gvsig.epanet.structures.validations;

import es.udc.cartolab.gvsig.epanet.math.MathUtils;
import es.udc.cartolab.gvsig.epanet.structures.LinkWrapper;
import es.udc.cartolab.gvsig.epanet.structures.ValveWrapper;

public class ValveLosts implements LinkChecker {

    @Override
    public Warning check(LinkWrapper link) {
	if (link instanceof ValveWrapper) {
	    if (MathUtils.compare(link.getUnitHeadLoss(), 0)) {
		return new Warning(
			"Aviso de cálculo hidráulico: La energía del sistema en área donde se ha ubicado una válvula no es sufieciente para aportar caudal");
	    }
	}
	return null;
    }

}
