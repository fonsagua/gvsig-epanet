package es.udc.cartolab.gvsig.epanet.structures.validations;

import java.util.Map;

import es.udc.cartolab.gvsig.epanet.structures.NodeWrapper;

public interface NodesChecker {

    public abstract Warning check(Map<String, NodeWrapper> nodes);

}