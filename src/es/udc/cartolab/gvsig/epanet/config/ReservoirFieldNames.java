package es.udc.cartolab.gvsig.epanet.config;

public class ReservoirFieldNames {
    private String totalHead;
    private String pressure;
    private String head;
    private String demand;

    public ReservoirFieldNames() {
	totalHead = "totalhead";
	pressure = "pressure";
	head = "head";
	demand = "demand";
    }

    public String getTotalHead() {
	return totalHead;
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

    public void setTotalHead(String totalHead) {
	this.totalHead = totalHead;
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
