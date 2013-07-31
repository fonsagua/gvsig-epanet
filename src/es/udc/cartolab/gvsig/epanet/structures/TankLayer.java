package es.udc.cartolab.gvsig.epanet.structures;

import com.hardcode.gdbms.driver.exceptions.ReadDriverException;
import com.hardcode.gdbms.engine.values.IntValue;
import com.hardcode.gdbms.engine.values.NumericValue;
import com.iver.cit.gvsig.fmap.core.IFeature;
import com.iver.cit.gvsig.fmap.layers.FLyrVect;
import com.iver.cit.gvsig.fmap.layers.SelectableDataSource;
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

	SelectableDataSource recordset;
	try {
	    recordset = layer.getRecordset();
	    elevationIdx = recordset.getFieldIndexByName(fieldNames
		    .getElevation());
	    initLevelIdx = recordset.getFieldIndexByName(fieldNames
		    .getInitLevel());
	    minLevelIdx = recordset.getFieldIndexByName(fieldNames
		    .getMinLevel());
	    maxLevelIdx = recordset.getFieldIndexByName(fieldNames
		    .getMaxLevel());
	    diameterIdx = recordset.getFieldIndexByName(fieldNames
		    .getDiameter());
	    pressureIdx = recordset.getFieldIndexByName(fieldNames
		    .getPressure());
	    headIdx = recordset.getFieldIndexByName(fieldNames.getHead());
	    demandIdx = recordset.getFieldIndexByName(fieldNames.getDemand());
	} catch (ReadDriverException e) {
	    throw new ExternalError(e);
	}

	throwIfFieldNotFound(elevationIdx, fieldNames.getElevation());
	throwIfFieldNotFound(initLevelIdx, fieldNames.getInitLevel());
	throwIfFieldNotFound(minLevelIdx, fieldNames.getMinLevel());
	throwIfFieldNotFound(maxLevelIdx, fieldNames.getMaxLevel());
	throwIfFieldNotFound(diameterIdx, fieldNames.getDiameter());
	throwIfFieldNotFound(pressureIdx, fieldNames.getPressure());
	throwIfFieldNotFound(headIdx, fieldNames.getHead());
	throwIfFieldNotFound(demandIdx, fieldNames.getDemand());
	return new int[] { pressureIdx, headIdx, demandIdx };

    }

}
