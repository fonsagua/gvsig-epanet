package es.udc.cartolab.gvsig.epanet.config;

public class ValveFieldNames extends LinkFieldNames {

    private String elevation;
    private String diameter;
    private String setting;

    public ValveFieldNames() {
	elevation = "elevation";
	diameter = "diameter";
	setting = "setting";
    }

    public String getElevation() {
	return elevation;
    }

    public String getDiameter() {
	return diameter;
    }

    public String getSetting() {
	return setting;
    }

    public void setElevation(String elevation) {
	this.elevation = elevation;
    }

    public void setDiameter(String diameter) {
	this.diameter = diameter;
    }

    public void setSetting(String setting) {
	this.setting = setting;
    }

}
