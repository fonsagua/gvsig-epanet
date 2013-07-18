package es.udc.cartolab.gvsig.epanet.config;

public class PumpFieldNames extends LinkFieldNames {

    private String elevation;
    private String value;

    protected PumpFieldNames() {
	elevation = "elevation";
	value = "value";
    }

    public String getElevation() {
	return elevation;
    }

    public String getValue() {
	return value;
    }

    protected void setElevation(String elevation) {
	this.elevation = elevation;
    }

    protected void setValue(String value) {
	this.value = value;
    }
}
