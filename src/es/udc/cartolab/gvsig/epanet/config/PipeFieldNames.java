package es.udc.cartolab.gvsig.epanet.config;

public class PipeFieldNames extends LinkFieldNames {

    private String diameter;
    private String roughness;

    public PipeFieldNames() {
	diameter = "diameter";
	roughness = "roughness";
    }

    public String getDiameter() {
	return diameter;
    }

    public String getRoughness() {
	return roughness;
    }

    public void setDiameter(String diameter) {
	this.diameter = diameter;
    }

    public void setRoughness(String roughness) {
	this.roughness = roughness;
    }
}
