package es.udc.cartolab.gvsig.fonsagua.epanet;

import java.util.HashMap;
import java.util.Map;

public class IDCreator {

    private int nodeID;
    private int linkID;
    private Map<String, String> nodeMap;
    private Map<String, String> linkMap;

    public IDCreator() {
	nodeID = 0;
	nodeMap = new HashMap<String, String>();

	linkID = 0;
	linkMap = new HashMap<String, String>();
    }

    public String addNode(String featureID) {
	String id = String.valueOf(++nodeID);
	nodeMap.put(featureID, id);
	return id;
    }

    public String addLink(String featureID) {
	String id = String.valueOf(++linkID);
	linkMap.put(featureID, id);
	return id;
    }
}
