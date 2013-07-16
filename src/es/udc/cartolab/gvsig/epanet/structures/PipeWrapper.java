package es.udc.cartolab.gvsig.epanet.structures;

import org.addition.epanet.network.structures.Link;
import org.addition.epanet.network.structures.Link.LinkType;
import org.addition.epanet.network.structures.Link.StatType;

import com.iver.cit.gvsig.fmap.core.IFeature;

public class PipeWrapper extends LinkWrapper {

    // public PipeWrapper(String id, Node startNode, Node endNode, double len,
    // double diameter, double roughness) {
    // createPipe(id, startNode, endNode, len, diameter, roughness);
    // }

    public PipeWrapper(IFeature iFeature) {
	super(iFeature);
    }

    public PipeWrapper(String id, NodeWrapper startNode, NodeWrapper endNode,
	    double len, double diameter, double roughness) {
	super();
	createPipe(id, startNode, endNode, len, diameter, roughness);
    }

    protected void createPipe(String id, NodeWrapper startNode,
	    NodeWrapper endNode, double len, double diameter, double roughness) {
	// TODO: MinorLoss
	Link link = new Link();
	link.setId(id);
	link.setFirst(startNode.getNode());
	link.setSecond(endNode.getNode());
	link.setLenght(len);
	link.setDiameter(diameter);
	link.setRoughness(roughness);
	link.setStatus(StatType.OPEN);
	link.setType(LinkType.PIPE);

	setLink(link);

    }

}
