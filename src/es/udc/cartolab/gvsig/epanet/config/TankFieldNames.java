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

    public TankFieldNames() {
	elevation = "elevation";
	initLevel = "initlevel";
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

    public void setElevation(String elevation) {
	this.elevation = elevation;
    }

    public void setInitLevel(String initLevel) {
	this.initLevel = initLevel;
    }

    public void setMinLevel(String minLevel) {
	this.minLevel = minLevel;
    }

    public void setMaxLevel(String maxLevel) {
	this.maxLevel = maxLevel;
    }

    public void setDiameter(String diameter) {
	this.diameter = diameter;
    }

    public void setPressure(String pressure) {
	this.pressure = pressure;
    }

    public void setHead(String head) {
	this.head = head;
    }

    public void setDemand(String demand) {
	this.demand = demand;
    }

}
