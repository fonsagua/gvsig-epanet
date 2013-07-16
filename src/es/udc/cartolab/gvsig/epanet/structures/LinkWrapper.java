package es.udc.cartolab.gvsig.epanet.structures;

import org.addition.epanet.network.structures.Link;

import com.iver.cit.gvsig.fmap.core.IFeature;

public class LinkWrapper {

    private double flow;
    private double velocity;
    private double unitHeadLoss;
    private double frictionFactor;

    private Link link;
    private IFeature feature;

    public LinkWrapper(IFeature feature) {
	this.feature = feature;
    }

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

    public Link getLink() {
	return this.link;
    }

    protected void setLink(Link link) {
	this.link = link;
    }

    public String getId() {
	return link.getId();
    }

}
