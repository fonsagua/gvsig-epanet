package es.udc.cartolab.gvsig.epanet.structures;

import com.hardcode.gdbms.driver.exceptions.ReadDriverException;
import com.hardcode.gdbms.engine.values.IntValue;
import com.iver.cit.gvsig.fmap.core.IFeature;
import com.iver.cit.gvsig.fmap.layers.FLyrVect;
import com.iver.cit.gvsig.fmap.layers.SelectableDataSource;
import com.vividsolutions.jts.geom.Coordinate;

import es.udc.cartolab.gvsig.epanet.config.Preferences;
import es.udc.cartolab.gvsig.epanet.config.ReservoirFieldNames;
import es.udc.cartolab.gvsig.epanet.exceptions.ExternalError;
import es.udc.cartolab.gvsig.epanet.network.IDCreator;
import es.udc.cartolab.gvsig.epanet.network.NetworkBuilder;

public class ReservoirLayer extends NodeLayer {

    private int totalHeadIdx;
    private int pressureIdx;
    private int headIdx;
    private int demandIdx;

    public ReservoirLayer(FLyrVect layer) {
	super(layer);
    }

    @Override
    protected NodeWrapper processSpecific(IFeature iFeature, NetworkBuilder nb) {
	ReservoirWrapper reservoir = new ReservoirWrapper(iFeature);
	Coordinate coordinate = iFeature.getGeometry().toJTSGeometry()
		.getCoordinate();
	IntValue totalHead = (IntValue) iFeature.getAttribute(totalHeadIdx);
	String id = IDCreator.addNode(iFeature.getID());
	reservoir.createReservoir(id, coordinate.x, coordinate.y,
		totalHead.intValue());
	nb.addReservoir(reservoir);
	return reservoir;
    }

    @Override
    protected int[] getIndexes() {
	ReservoirFieldNames fieldNames = Preferences.getReservoirFieldNames();

	SelectableDataSource recordset;
	try {
	    recordset = layer.getRecordset();
	    totalHeadIdx = recordset.getFieldIndexByName(fieldNames
		    .getTotalHead());
	    pressureIdx = recordset.getFieldIndexByName(fieldNames
		    .getPressure());
	    headIdx = recordset.getFieldIndexByName(fieldNames.getHead());
	    demandIdx = recordset.getFieldIndexByName(fieldNames.getDemand());
	} catch (ReadDriverException e) {
	    throw new ExternalError(e);
	}

	throwIfFieldNotFound(totalHeadIdx, fieldNames.getTotalHead());
	throwIfFieldNotFound(pressureIdx, fieldNames.getPressure());
	throwIfFieldNotFound(headIdx, fieldNames.getHead());
	throwIfFieldNotFound(demandIdx, fieldNames.getDemand());

	return new int[] { pressureIdx, headIdx, demandIdx };

    }

}
