package es.udc.cartolab.gvsig.epanet.config;

public class PumpFieldNames extends LinkFieldNames {

    private String elevation;
    private String value;

    public PumpFieldNames() {
	elevation = "elevation";
	value = "value";
    }

    public String getElevation() {
	return elevation;
    }

    public String getValue() {
	return value;
    }

    public void setElevation(String elevation) {
	this.elevation = elevation;
    }

    public void setValue(String value) {
	this.value = value;
    }
}
