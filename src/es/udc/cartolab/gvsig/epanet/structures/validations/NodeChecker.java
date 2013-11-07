package es.udc.cartolab.gvsig.epanet.structures.validations;

import es.udc.cartolab.gvsig.epanet.structures.NodeWrapper;

public interface NodeChecker {

    public abstract Warning check(NodeWrapper node);

}