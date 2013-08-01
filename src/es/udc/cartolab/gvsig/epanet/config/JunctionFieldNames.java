package es.udc.cartolab.gvsig.epanet.config;

public class JunctionFieldNames {

    private String elevation;
    private String baseDemand;
    private String pressure;
    private String head;
    private String demand;

    public JunctionFieldNames() {
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

    public void setElevation(String elevation) {
	this.elevation = elevation;
    }

    public void setBaseDemand(String baseDemand) {
	this.baseDemand = baseDemand;
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
