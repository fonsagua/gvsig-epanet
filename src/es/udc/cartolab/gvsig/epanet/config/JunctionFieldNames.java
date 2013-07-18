package es.udc.cartolab.gvsig.epanet.config;

public class JunctionFieldNames {

    private String elevation;
    private String baseDemand;
    private String pressure;
    private String head;
    private String demand;

    protected JunctionFieldNames() {
	elevation = "elevation";
	baseDemand = "basedemand";
	pressure = "pressure";
	head = "head";
	demand = "demand";
    }

    public String getElevation() {
	return elevation;
    }

    public String getBaseDemand() {
	return baseDemand;
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

    protected void setBaseDemand(String baseDemand) {
	this.baseDemand = baseDemand;
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
