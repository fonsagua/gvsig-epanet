package es.udc.cartolab.gvsig.epanet.config;

public class TankFieldNames {

    private String elevation;
    private String initLevel;
    private String minLevel;
    private String maxLevel;
    private String diameter;
    private String pressure;
    private String head;
    private String demand;

    protected TankFieldNames() {
	elevation = "elevation";
	initLevel = "inilevel";
	minLevel = "minlevel";
	maxLevel = "maxlevel";
	diameter = "diameter";
	pressure = "pressure";
	head = "head";
	demand = "demand";
    }

    public String getElevation() {
	return elevation;
    }

    public String getInitLevel() {
	return initLevel;
    }

    public String getMinLevel() {
	return minLevel;
    }

    public String getMaxLevel() {
	return maxLevel;
    }

    public String getDiameter() {
	return diameter;
    }

    public String getPressure() {
	return pressure;
    }

    public String getHead() {
	return head;
    }

    public String getDemand() {
	return demand;
    }

    protected void setElevation(String elevation) {
	this.elevation = elevation;
    }

    protected void setInitLevel(String initLevel) {
	this.initLevel = initLevel;
    }

    protected void setMinLevel(String minLevel) {
	this.minLevel = minLevel;
    }

    protected void setMaxLevel(String maxLevel) {
	this.maxLevel = maxLevel;
    }

    protected void setDiameter(String diameter) {
	this.diameter = diameter;
    }

    protected void setPressure(String pressure) {
	this.pressure = pressure;
    }

    protected void setHead(String head) {
	this.head = head;
    }

    protected void setDemand(String demand) {
	this.demand = demand;
    }

}
