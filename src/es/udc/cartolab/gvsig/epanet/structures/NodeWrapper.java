package es.udc.cartolab.gvsig.epanet.structures;

import org.addition.epanet.network.structures.Node;

import com.iver.cit.gvsig.fmap.core.IFeature;

public abstract class NodeWrapper extends StructureFeature {

    private double demand;
    private double head;
    private double pressure;

    private Node node;

    public NodeWrapper(IFeature feature) {
	super(feature);
    }

    public NodeWrapper() {
	super(null);
    }

    public double getDemand() {
	return demand;
    }

    public void setDemand(double demand) {
	this.demand = demand;
    }

    public double getHead() {
	return head;
    }

    public void setHead(double head) {
	this.head = head;
    }

    public double getPressure() {
	return pressure;
    }

    public void setPressure(double pressure) {
	this.pressure = pressure;
    }

    public Node getNode() {
	return node;
    }

    protected void setNode(Node node) {
	this.node = node;
    }

    public String getId() {
	return node.getId();
    }

    public double getX() {
	return node.getPosition().getX();
    }

    public double getY() {
	return node.getPosition().getY();
    }

    public void cloneResults(NodeWrapper results) {
	setPressure(results.getPressure());
	setHead(results.getHead());
	setDemand(results.getDemand());
    }
}
