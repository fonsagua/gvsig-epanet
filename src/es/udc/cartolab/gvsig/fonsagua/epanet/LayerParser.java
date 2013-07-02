package es.udc.cartolab.gvsig.fonsagua.epanet;

import java.io.File;

import org.addition.epanet.util.ENException;

import com.hardcode.gdbms.engine.values.DoubleValue;
import com.hardcode.gdbms.engine.values.IntValue;
import com.iver.cit.gvsig.fmap.core.IFeature;
import com.iver.cit.gvsig.fmap.layers.FLyrVect;
import com.iver.cit.gvsig.fmap.layers.ReadableVectorial;
import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Geometry;

import es.udc.cartolab.gvsig.shputils.SHPFactory;

public class LayerParser {

    private NetworkBuilder nb;
    private IDCreator idCreator;

    public LayerParser() {
	idCreator = new IDCreator();
	nb = new NetworkBuilder();
    }

    public void addPipes(File file) throws Exception {
	FLyrVect layer = SHPFactory.getFLyrVectFromSHP(file);
	ReadableVectorial readableVectorial = layer.getSource();

	for (int i = 0; i < readableVectorial.getShapeCount(); i++) {
	    IFeature iFeature = readableVectorial.getFeature(i);
	    String id = idCreator.addLink(iFeature.getID());
	    Geometry geom = iFeature.getGeometry().toJTSGeometry();
	    Coordinate[] coordinates = geom.getCoordinates();
	    // FIXME
	    DoubleValue diameter = (DoubleValue) iFeature.getAttribute(0);
	    DoubleValue roughness = (DoubleValue) iFeature.getAttribute(1);
	    nb.getPipe(id, "1", "2", geom.getLength(), diameter.intValue(),
		    roughness.doubleValue());
	}
    }

    public void addJunctions(File file) throws Exception {
	FLyrVect layer = SHPFactory.getFLyrVectFromSHP(file);
	ReadableVectorial readableVectorial = layer.getSource();

	for (int i = 0; i < readableVectorial.getShapeCount(); i++) {
	    IFeature iFeature = readableVectorial.getFeature(i);
	    String id = idCreator.addNode(iFeature.getID());
	    Coordinate coordinate = iFeature.getGeometry().toJTSGeometry()
		    .getCoordinate();
	    IntValue elevation = (IntValue) iFeature.getAttribute(0);
	    IntValue demand = (IntValue) iFeature.getAttribute(1);
	    nb.getNode(id, coordinate.x, coordinate.y, elevation.intValue(),
		    demand.intValue());
	}
    }

    public void addReservoirs(File file) throws Exception {
	FLyrVect layer = SHPFactory.getFLyrVectFromSHP(file);
	ReadableVectorial readableVectorial = layer.getSource();
	for (int i = 0; i < readableVectorial.getShapeCount(); i++) {
	    IFeature iFeature = readableVectorial.getFeature(i);
	    String id = idCreator.addNode(iFeature.getID());
	    Coordinate coordinate = iFeature.getGeometry().toJTSGeometry()
		    .getCoordinate();
	    IntValue totalHead = (IntValue) iFeature.getAttribute(0);
	    nb.getReservoir(id, coordinate.x, coordinate.y,
		    totalHead.intValue());
	}
    }

    public void createInpFile(File inp) throws ENException {
	nb.createInpFile(inp);
    }

}
