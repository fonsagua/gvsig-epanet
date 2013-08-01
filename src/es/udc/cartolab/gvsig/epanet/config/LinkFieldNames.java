package es.udc.cartolab.gvsig.epanet.config;

public class LinkFieldNames {

    private String flow;
    private String velocity;
    private String unitHeadLoss;
    private String frictionFactor;

    public LinkFieldNames() {
	flow = "flow";
	velocity = "velocity";
	unitHeadLoss = "uhloss";
	frictionFactor = "fricfactor";
    }

    public String getFlow() {
	return flow;
    }

    public String getVelocity() {
	return velocity;
    }

    public String getUnitHeadLoss() {
	return unitHeadLoss;
    }

    public String getFrictionFactor() {
	return frictionFactor;
    }

    public void setFlow(String flow) {
	this.flow = flow;
    }

    public void setVelocity(String velocity) {
	this.velocity = velocity;
    }

    public void setUnitHeadLoss(String unitHeadLoss) {
	this.unitHeadLoss = unitHeadLoss;
    }

    public void setFrictionFactor(String frictionFactor) {
	this.frictionFactor = frictionFactor;
    }

}
