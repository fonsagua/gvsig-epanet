package es.udc.cartolab.gvsig.epanet;

public class LinkWrapper {

    private double flow;
    private double velocity;
    private double unitHeadLoss;
    private double frictionFactor;

    public double getFlow() {
	return flow;
    }

    public void setFlow(double flow) {
	this.flow = flow;
    }

    public double getVelocity() {
	return velocity;
    }

    public void setVelocity(double velocity) {
	this.velocity = velocity;
    }

    public double getUnitHeadLoss() {
	return unitHeadLoss;
    }

    public void setUnitHeadLoss(double unitheadloss) {
	this.unitHeadLoss = unitheadloss;
    }

    public double getFrictionFactor() {
	return frictionFactor;
    }

    public void setFrictionFactor(double frictionFactor) {
	this.frictionFactor = frictionFactor;
    }

}
