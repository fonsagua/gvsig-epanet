package es.udc.cartolab.gvsig.epanet.structures;

import com.hardcode.gdbms.driver.exceptions.ReadDriverException;
import com.hardcode.gdbms.engine.values.DoubleValue;
import com.iver.cit.gvsig.fmap.core.IFeature;
import com.iver.cit.gvsig.fmap.layers.FLyrVect;
import com.iver.cit.gvsig.fmap.layers.SelectableDataSource;
import com.vividsolutions.jts.geom.Coordinate;

import es.udc.cartolab.gvsig.epanet.config.JunctionFieldNames;
import es.udc.cartolab.gvsig.epanet.config.Preferences;
import es.udc.cartolab.gvsig.epanet.exceptions.ExternalError;
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
    public NodeWrapper processFeature(IFeature iFeature, NetworkBuilder nb) {
	JunctionWrapper junction = new JunctionWrapper(iFeature);
	Coordinate coordinate = iFeature.getGeometry().toJTSGeometry()
		.getCoordinate();
	DoubleValue elevation = (DoubleValue) iFeature
		.getAttribute(elevationIdx);
	DoubleValue bdemand = (DoubleValue) iFeature.getAttribute(bdemandIdx);
	String id = IDCreator.addNode(iFeature.getID());
	junction.createJunction(id, coordinate.x, coordinate.y,
		elevation.doubleValue(), bdemand.doubleValue());
	nb.addJunction(junction);
	return junction;
    }

    @Override
    protected int[] getIndexes() {
	JunctionFieldNames fieldNames = Preferences.getJunctionFieldNames();

	SelectableDataSource recordset;
	try {
	    recordset = layer.getRecordset();
	    elevationIdx = recordset.getFieldIndexByName(fieldNames
		    .getElevation());
	    bdemandIdx = recordset.getFieldIndexByName(fieldNames
		    .getBaseDemand());
	    pressureIdx = recordset.getFieldIndexByName(fieldNames
		    .getPressure());
	    headIdx = recordset.getFieldIndexByName(fieldNames.getHead());
	    demandIdx = recordset.getFieldIndexByName(fieldNames.getDemand());
	} catch (ReadDriverException e) {
	    throw new ExternalError(e);
	}

	throwIfFieldNotFound(elevationIdx, fieldNames.getElevation());
	throwIfFieldNotFound(bdemandIdx, fieldNames.getBaseDemand());
	throwIfFieldNotFound(pressureIdx, fieldNames.getPressure());
	throwIfFieldNotFound(headIdx, fieldNames.getHead());
	throwIfFieldNotFound(demandIdx, fieldNames.getDemand());

	return new int[] { pressureIdx, headIdx, demandIdx };

    }

}
