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
			"Aviso de c�lculo hidr�ulico: La energ�a del sistema en �rea donde se ha ubicado una v�lvula no es sufieciente para aportar caudal");
	    }
	}
	return null;
    }

}
