package es.udc.cartolab.gvsig.epanet.structures;

import com.hardcode.gdbms.driver.exceptions.ReadDriverException;
import com.hardcode.gdbms.engine.values.NumericValue;
import com.iver.cit.gvsig.fmap.core.IFeature;
import com.iver.cit.gvsig.fmap.layers.FLyrVect;
import com.vividsolutions.jts.geom.Coordinate;

import es.udc.cartolab.gvsig.epanet.config.Preferences;
import es.udc.cartolab.gvsig.epanet.config.ReservoirFieldNames;
import es.udc.cartolab.gvsig.epanet.exceptions.ExternalError;
import es.udc.cartolab.gvsig.epanet.exceptions.InvalidNetworkError;
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
    protected NodeWrapper processSpecific(IFeature iFeature, NetworkBuilder nb)
	    throws InvalidNetworkError {
	ReservoirWrapper reservoir = new ReservoirWrapper(iFeature);
	Coordinate coordinate = iFeature.getGeometry().toJTSGeometry()
		.getCoordinate();
	NumericValue totalHead = getValue(iFeature, totalHeadIdx);
	String id = IDCreator.addNode(iFeature.getID());
	reservoir.createReservoir(id, coordinate.x, coordinate.y,
		totalHead.intValue());
	nb.addReservoir(reservoir);
	return reservoir;
    }

    @Override
    protected int[] getIndexes() {
	ReservoirFieldNames fieldNames = Preferences.getReservoirFieldNames();

	try {
	    totalHeadIdx = getFieldIdx(fieldNames.getTotalHead());
	    pressureIdx = getFieldIdx(fieldNames.getPressure());
	    headIdx = getFieldIdx(fieldNames.getHead());
	    demandIdx = getFieldIdx(fieldNames.getDemand());
	} catch (ReadDriverException e) {
	    throw new ExternalError(e);
	}

	return new int[] { pressureIdx, headIdx, demandIdx };

    }

}
