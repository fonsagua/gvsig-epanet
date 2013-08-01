package es.udc.cartolab.gvsig.epanet.config;

public class LayerNames {

    private String junctions;
    private String pipes;
    private String pumps;
    private String reservoirs;
    private String tanks;
    private String valves;

    public LayerNames() {
	junctions = "junctions";
	pipes = "pipes";
	pumps = "pumps";
	reservoirs = "reservoirs";
	tanks = "tanks";
	valves = "valves";
    }

    public String getJunctions() {
	return junctions;
    }

    public String getPipes() {
	return pipes;
    }

    public String getPumps() {
	return pumps;
    }

    public String getReservoirs() {
	return reservoirs;
    }

    public String getTanks() {
	return tanks;
    }

    public String getValves() {
	return valves;
    }

    public void setJunctions(String junctions) {
	this.junctions = junctions;
    }

    public void setPipes(String pipes) {
	this.pipes = pipes;
    }

    public void setPumps(String pumps) {
	this.pumps = pumps;
    }

    public void setReservoirs(String reservoirs) {
	this.reservoirs = reservoirs;
    }

    public void setTanks(String tanks) {
	this.tanks = tanks;
    }

    public void setValves(String valves) {
	this.valves = valves;
    }

}
