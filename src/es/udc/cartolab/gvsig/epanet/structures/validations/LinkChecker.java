package es.udc.cartolab.gvsig.epanet.structures.validations;

import es.udc.cartolab.gvsig.epanet.structures.LinkWrapper;

public interface LinkChecker {

    public abstract Warning check(LinkWrapper link);

}
