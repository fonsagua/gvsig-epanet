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

    public static TankFieldNames getTankFieldNames() {
	TankFieldNames names = new TankFieldNames();
	names.setElevation("elevation");
	names.setInitLevel("initlevel");
	names.setMinLevel("minlevel");
	names.setMaxLevel("maxlevel");
	names.setDiameter("diameter");
	names.setPressure("pressure");
	names.setHead("head");
	names.setDemand("demand");
	return names;
    }

    public static ReservoirFieldNames getReservoirFieldNames() {
	ReservoirFieldNames names = new ReservoirFieldNames();
	names.setTotalHead("totalhead");
	names.setPressure("pressure");
	names.setHead("head");
	names.setDemand("demand");
	return names;
    }

    public static PipeFieldNames getPipeFieldNames() {
	PipeFieldNames names = new PipeFieldNames();
	names.setDiameter("diameter");
	names.setRoughness("roughness");
	names.setFlow("flow");
	names.setVelocity("velocity");
	names.setUnitHeadLoss("uhloss");
	names.setFrictionFactor("fricfactor");
	return names;
    }

    public static PumpFieldNames getPumpFieldNames() {
	PumpFieldNames names = new PumpFieldNames();
	names.setElevation("elevation");
	names.setValue("value");
	names.setFlow("flow");
	names.setVelocity("velocity");
	names.setUnitHeadLoss("uhloss");
	names.setFrictionFactor("fricfactor");
	return names;
    }

    public static ValveFieldNames getValveFieldNames() {
	ValveFieldNames names = new ValveFieldNames();
	names.setElevation("elevation");
	names.setDiameter("diameter");
	names.setFlow("flow");
	names.setVelocity("velocity");
	names.setUnitHeadLoss("uhloss");
	names.setFrictionFactor("fricfactor");
	return names;
    }

}
