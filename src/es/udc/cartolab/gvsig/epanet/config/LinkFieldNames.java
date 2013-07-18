package es.udc.cartolab.gvsig.epanet.config;

public class LinkFieldNames {

    private String flow;
    private String velocity;
    private String unitHeadLoss;
    private String frictionFactor;

    protected LinkFieldNames() {
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

    protected void setFlow(String flow) {
	this.flow = flow;
    }

    protected void setVelocity(String velocity) {
	this.velocity = velocity;
    }

    protected void setUnitHeadLoss(String unitHeadLoss) {
	this.unitHeadLoss = unitHeadLoss;
    }

    protected void setFrictionFactor(String frictionFactor) {
	this.frictionFactor = frictionFactor;
    }

}
