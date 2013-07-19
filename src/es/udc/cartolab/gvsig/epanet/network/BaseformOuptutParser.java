package es.udc.cartolab.gvsig.epanet.network;

import java.util.Map;

import es.udc.cartolab.gvsig.epanet.structures.LinkWrapper;
import es.udc.cartolab.gvsig.epanet.structures.NodeWrapper;

/**
 * Class for parse the output files (.nodes.out, and .links.out) produced by
 * baseform
 * 
 */
public class BaseformOuptutParser {

    /**
     * 
     * @param nodesPath
     *            path to the output nodes file
     * @param linksPath
     *            path to the output links file
     */
    public BaseformOuptutParser(String nodesPath, String linksPath) {

    }

    public Map<String, NodeWrapper> getNodes() {
	return null;
    }

    public Map<String, LinkWrapper> getLinks() {
	return null;
    }

}
