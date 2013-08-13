package es.udc.cartolab.gvsig.epanet.structures;

import com.hardcode.gdbms.driver.exceptions.ReadDriverException;
import com.hardcode.gdbms.engine.values.IntValue;
import com.hardcode.gdbms.engine.values.NumericValue;
import com.iver.cit.gvsig.fmap.core.IFeature;
import com.iver.cit.gvsig.fmap.layers.FLyrVect;
import com.vividsolutions.jts.geom.Coordinate;

import es.udc.cartolab.gvsig.epanet.config.Preferences;
import es.udc.cartolab.gvsig.epanet.config.TankFieldNames;
import es.udc.cartolab.gvsig.epanet.exceptions.ExternalError;
import es.udc.cartolab.gvsig.epanet.network.IDCreator;
import es.udc.cartolab.gvsig.epanet.network.NetworkBuilder;

public class TankLayer extends NodeLayer {

    private int elevationIdx;
    private int initLevelIdx;
    private int minLevelIdx;
    private int maxLevelIdx;
    private int diameterIdx;
    private int pressureIdx;
    private int headIdx;
    private int demandIdx;

    public TankLayer(FLyrVect layer) {
	super(layer);
    }

    @Override
    protected NodeWrapper processSpecific(IFeature iFeature, NetworkBuilder nb) {
	TankWrapper tank = new TankWrapper(iFeature);

	String id = IDCreator.addNode(iFeature.getID());
	Coordinate coordinate = iFeature.getGeometry().toJTSGeometry()
		.getCoordinate();
	NumericValue elevation = (NumericValue) iFeature
		.getAttribute(elevationIdx);
	IntValue initLevel = (IntValue) iFeature.getAttribute(initLevelIdx);
	IntValue minLevel = (IntValue) iFeature.getAttribute(minLevelIdx);
	IntValue maxLevel = (IntValue) iFeature.getAttribute(maxLevelIdx);
	NumericValue diameter = (NumericValue) iFeature
		.getAttribute(diameterIdx);
	tank.createTank(id, coordinate.x, coordinate.y,
		elevation.doubleValue(), initLevel.intValue(),
		minLevel.intValue(), maxLevel.intValue(),
		diameter.doubleValue());
	nb.addTank(tank);
	return tank;
    }

    @Override
    protected int[] getIndexes() {
	TankFieldNames fieldNames = Preferences.getTankFieldNames();

	try {
	    elevationIdx = getFieldIdx(fieldNames.getElevation());
	    initLevelIdx = getFieldIdx(fieldNames.getInitLevel());
	    minLevelIdx = getFieldIdx(fieldNames.getMinLevel());
	    maxLevelIdx = getFieldIdx(fieldNames.getMaxLevel());
	    diameterIdx = getFieldIdx(fieldNames.getDiameter());
	    pressureIdx = getFieldIdx(fieldNames.getPressure());
	    headIdx = getFieldIdx(fieldNames.getHead());
	    demandIdx = getFieldIdx(fieldNames.getDemand());
	} catch (ReadDriverException e) {
	    throw new ExternalError(e);
	}

	return new int[] { pressureIdx, headIdx, demandIdx };

    }

}
