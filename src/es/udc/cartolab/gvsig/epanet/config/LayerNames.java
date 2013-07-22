package es.udc.cartolab.gvsig.epanet.config;

public class LayerNames {

    private String junctions;
    private String pipes;
    private String pumps;
    private String reservoirs;
    private String tanks;
    private String valves;

    protected LayerNames() {
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

    protected void setJunctions(String junctions) {
	this.junctions = junctions;
    }

    protected void setPipes(String pipes) {
	this.pipes = pipes;
    }

    protected void setPumps(String pumps) {
	this.pumps = pumps;
    }

    protected void setReservoirs(String reservoirs) {
	this.reservoirs = reservoirs;
    }

    protected void setTanks(String tanks) {
	this.tanks = tanks;
    }

    protected void setValves(String valves) {
	this.valves = valves;
    }

}
