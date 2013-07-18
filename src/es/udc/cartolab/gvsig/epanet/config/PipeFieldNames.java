package es.udc.cartolab.gvsig.epanet.config;

public class PipeFieldNames extends LinkFieldNames {

    private String diameter;
    private String roughness;

    protected PipeFieldNames() {
	diameter = "diameter";
	roughness = "roughness";
    }

    public String getDiameter() {
	return diameter;
    }

    public String getRoughness() {
	return roughness;
    }

    protected void setDiameter(String diameter) {
	this.diameter = diameter;
    }

    protected void setRoughness(String roughness) {
	this.roughness = roughness;
    }
}
