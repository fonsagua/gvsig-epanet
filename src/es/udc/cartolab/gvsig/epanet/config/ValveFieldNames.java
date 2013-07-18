package es.udc.cartolab.gvsig.epanet.config;

public class ValveFieldNames extends LinkFieldNames {

    private String elevation;
    private String diameter;

    protected ValveFieldNames() {
	elevation = "elevation";
	diameter = "diameter";
    }

    public String getElevation() {
	return elevation;
    }

    public String getDiameter() {
	return diameter;
    }

    protected void setElevation(String elevation) {
	this.elevation = elevation;
    }

    protected void setDiameter(String diameter) {
	this.diameter = diameter;
    }
}
