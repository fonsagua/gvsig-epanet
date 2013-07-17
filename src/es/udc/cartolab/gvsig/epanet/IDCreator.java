package es.udc.cartolab.gvsig.epanet;

import java.util.HashMap;
import java.util.Map;

public class IDCreator {

    private static int nodeID;
    private static int linkID;
    private static Map<String, String> nodeMap;
    private static Map<String, String> linkMap;

    public static void reset() {
	nodeID = 0;
	nodeMap = new HashMap<String, String>();

	linkID = 0;
	linkMap = new HashMap<String, String>();
    }

    public static String addNode(String featureID) {
	String id = String.valueOf(++nodeID);
	nodeMap.put(featureID, id);
	return id;
    }

    public static String addLink(String featureID) {
	String id = String.valueOf(++linkID);
	linkMap.put(featureID, id);
	return id;
    }

    public static String addValveNode(String featureID) {
	String id = String.valueOf(++nodeID);
	return id;
    }

    public static String addValveLink(String featureID) {
	String id = String.valueOf(++linkID);
	return id;
    }

    public static String addPumpNode(String featureID) {
	String id = String.valueOf(++nodeID);
	return id;
    }

    public static String addPumpLink(String featureID) {
	String id = String.valueOf(++linkID);
	return id;
    }
}
