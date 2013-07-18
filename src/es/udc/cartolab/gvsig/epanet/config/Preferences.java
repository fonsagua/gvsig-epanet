package es.udc.cartolab.gvsig.epanet.config;

public class Preferences {

    public static JunctionFieldNames getJunctionFieldNames() {
	JunctionFieldNames names = new JunctionFieldNames();
	names.setElevation("elevation");
	names.setBaseDemand("basedemand");
	names.setPressure("pressure");
	names.setHead("head");
	names.setDemand("demand");

	return names;
    }

}
