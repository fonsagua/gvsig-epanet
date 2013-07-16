package es.udc.cartolab.gvsig.epanet.structures;

import org.addition.epanet.network.structures.Node;

public abstract class NodeWrapper {

    private double demand;
    private double head;
    private double pressure;

    private Node node;

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

}
