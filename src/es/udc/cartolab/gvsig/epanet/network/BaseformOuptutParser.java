package es.udc.cartolab.gvsig.epanet.network;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

import es.udc.cartolab.gvsig.epanet.structures.JunctionWrapper;
import es.udc.cartolab.gvsig.epanet.structures.LinkWrapper;
import es.udc.cartolab.gvsig.epanet.structures.NodeWrapper;
import es.udc.cartolab.gvsig.epanet.structures.PipeWrapper;

/**
 * Class for parse the output files (.nodes.out, and .links.out) produced by
 * baseform
 * 
 */
public class BaseformOuptutParser {

    private Map<String, NodeWrapper> nodes = new HashMap<String, NodeWrapper>();
    private Map<String, LinkWrapper> links = new HashMap<String, LinkWrapper>();

    /**
     * 
     * @param nodesPath
     *            path to the output nodes file
     * @param linksPath
     *            path to the output links file
     */
    public BaseformOuptutParser(String nodesPath, String linksPath) {
	File nodesFile = new File(nodesPath);
	if (nodesFile.canRead()) {
	    readNodes(nodesFile);
	}
	File linksFile = new File(linksPath);
	if (linksFile.canRead()) {
	    readLinks(linksFile);
	}
    }

    private void readNodes(File nodesFile) {
	try {
	    FileInputStream fstream = new FileInputStream(nodesFile);
	    DataInputStream in = new DataInputStream(fstream);
	    BufferedReader br = new BufferedReader(new InputStreamReader(in));
	    String nodeLine;
	    // We skip the headers
	    br.readLine();
	    br.readLine();
	    while ((nodeLine = br.readLine()) != null) {
		parseNode(nodeLine);
	    }
	} catch (Exception e) {
	    e.printStackTrace();
	}
    }

    private void parseNode(String nodeLine) {
	String[] columns = nodeLine.replace(",", ".").split("\t");
	if (columns.length >= 7) {
	    NodeWrapper node = new JunctionWrapper(columns[0], 0, 0,
		    Double.parseDouble(columns[2]),
		    Double.parseDouble(columns[3]));
	    node.setPressure(Double.parseDouble(columns[4]));
	    node.setHead(Double.parseDouble(columns[5]));
	    node.setDemand(Double.parseDouble(columns[6]));
	    nodes.put(node.getId(), node);
	}
    }

    private void readLinks(File linksFile) {
	try {
	    FileInputStream fstream = new FileInputStream(linksFile);
	    DataInputStream in = new DataInputStream(fstream);
	    BufferedReader br = new BufferedReader(new InputStreamReader(in));
	    String linkLine;
	    // We skip the headers
	    br.readLine();
	    br.readLine();
	    while ((linkLine = br.readLine()) != null) {
		parseLink(linkLine);
	    }
	} catch (Exception e) {
	    e.printStackTrace();
	}
    }

    private void parseLink(String linkLine) {
	String[] columns = linkLine.replace(",", ".").split("\t");
	NodeWrapper mockNode = new JunctionWrapper("0", 0, 0, 0, 0);
	if (columns.length >= 9) {
	    LinkWrapper link = new PipeWrapper(columns[0], mockNode, mockNode,
		    Double.parseDouble(columns[2]),
		    Double.parseDouble(columns[3]),
		    Double.parseDouble(columns[4]));
	    link.setFlow(Double.parseDouble(columns[5]));
	    link.setVelocity(Double.parseDouble(columns[6]));
	    link.setUnitHeadLoss(Double.parseDouble(columns[7]));
	    link.setFrictionFactor(Double.parseDouble(columns[8]));
	    links.put(link.getId(), link);
	}
    }

    public Map<String, NodeWrapper> getNodes() {
	return nodes;
    }

    public Map<String, LinkWrapper> getLinks() {
	return links;
    }

}
