package es.udc.cartolab.gvsig.epanet.structures;

import com.hardcode.gdbms.driver.exceptions.ReadDriverException;
import com.hardcode.gdbms.engine.values.NumericValue;
import com.iver.cit.gvsig.fmap.core.IFeature;
import com.iver.cit.gvsig.fmap.layers.FLyrVect;
import com.vividsolutions.jts.geom.Coordinate;

import es.udc.cartolab.gvsig.epanet.config.JunctionFieldNames;
import es.udc.cartolab.gvsig.epanet.config.Preferences;
import es.udc.cartolab.gvsig.epanet.exceptions.ExternalError;
import es.udc.cartolab.gvsig.epanet.exceptions.InvalidNetworkError;
import es.udc.cartolab.gvsig.epanet.network.IDCreator;
import es.udc.cartolab.gvsig.epanet.network.NetworkBuilder;

public class JunctionLayer extends NodeLayer {

    private int elevationIdx;
    private int bdemandIdx;
    private int demandIdx;
    private int headIdx;
    private int pressureIdx;

    public JunctionLayer(FLyrVect layer) {
	super(layer);
    }

    @Override
    protected NodeWrapper processSpecific(IFeature iFeature, NetworkBuilder nb)
	    throws InvalidNetworkError {
	JunctionWrapper junction = new JunctionWrapper(iFeature);
	Coordinate coordinate = iFeature.getGeometry().toJTSGeometry()
		.getCoordinate();

	NumericValue elevation = getValue(iFeature, elevationIdx);
	NumericValue bdemand = getValue(iFeature, bdemandIdx);
	String id = IDCreator.addNode(iFeature.getID());
	junction.createJunction(id, coordinate.x, coordinate.y,
		elevation.doubleValue(), bdemand.doubleValue());
	nb.addJunction(junction);
	return junction;
    }

    @Override
    protected int[] getIndexes() {
	JunctionFieldNames fieldNames = Preferences.getJunctionFieldNames();
	try {
	    elevationIdx = getFieldIdx(fieldNames.getElevation());
	    bdemandIdx = getFieldIdx(fieldNames.getBaseDemand());
	    pressureIdx = getFieldIdx(fieldNames.getPressure());
	    headIdx = getFieldIdx(fieldNames.getHead());
	    demandIdx = getFieldIdx(fieldNames.getDemand());
	} catch (ReadDriverException e) {
	    throw new ExternalError(e);
	}
	return new int[] { pressureIdx, headIdx, demandIdx };

    }

}
