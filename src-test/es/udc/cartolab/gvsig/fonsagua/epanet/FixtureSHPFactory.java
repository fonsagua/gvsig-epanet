package es.udc.cartolab.gvsig.fonsagua.epanet;

import java.io.File;

import com.hardcode.gdbms.engine.values.Value;
import com.hardcode.gdbms.engine.values.ValueFactory;
import com.iver.cit.gvsig.fmap.core.DefaultFeature;
import com.iver.cit.gvsig.fmap.core.FShape;
import com.iver.cit.gvsig.fmap.core.GeneralPathX;
import com.iver.cit.gvsig.fmap.core.IFeature;
import com.iver.cit.gvsig.fmap.core.IGeometry;
import com.iver.cit.gvsig.fmap.core.ShapeFactory;
import com.iver.cit.gvsig.fmap.drivers.FieldDescription;

import es.udc.cartolab.gvsig.shputils.FieldDescriptionFactory;
import es.udc.cartolab.gvsig.shputils.SHPFactory;

public class FixtureSHPFactory {

    protected static IFeature createJunctionFeature(double x, double y,
	    int elevation, int demand) {
	Value[] values = new Value[3];
	values[0] = ValueFactory.createValue(elevation);
	values[1] = ValueFactory.createValue(demand);
	values[2] = ValueFactory.createValue(0.0);
	IGeometry geom = ShapeFactory.createPoint2D(x, y);
	IFeature feat = new DefaultFeature(geom, values);
	return feat;
    }

    protected static void createJunctionShp(File file, IFeature[] features)
	    throws Exception {
	FieldDescriptionFactory fdFactory = new FieldDescriptionFactory();
	fdFactory.addInteger("elevation");
	fdFactory.addInteger("demand");
	fdFactory.addDouble("preassure");
	FieldDescription[] fieldsDesc = fdFactory.getFields();
	SHPFactory.createSHP(file, fieldsDesc, FShape.POINT, features);
    }

    protected static IFeature createReservoirFeature(double x, double y,
	    int totalHead) {
	Value[] values = new Value[1];
	values[0] = ValueFactory.createValue(totalHead);
	IGeometry geom = ShapeFactory.createPoint2D(x, y);
	IFeature feat = new DefaultFeature(geom, values);
	return feat;
    }

    protected static void createReservoirShp(File file, IFeature[] features)
	    throws Exception {
	FieldDescriptionFactory fdFactory = new FieldDescriptionFactory();
	fdFactory.addInteger("totalHead");
	FieldDescription[] fieldsDesc = fdFactory.getFields();
	SHPFactory.createSHP(file, fieldsDesc, FShape.POINT, features);
    }

    protected static void createPipeShp(File file, IFeature[] features)
	    throws Exception {
	FieldDescriptionFactory fdFactory = new FieldDescriptionFactory();
	fdFactory.addDouble("diameter");
	fdFactory.addDouble("roughness");
	FieldDescription[] fieldsDesc = fdFactory.getFields();
	SHPFactory.createSHP(file, fieldsDesc, FShape.LINE, features);
    }

    protected static IFeature createPipeFeature(IFeature startNode,
	    IFeature endNode, double diameter, double roughness) {
	Value[] values = new Value[2];
	values[0] = ValueFactory.createValue(diameter);
	values[1] = ValueFactory.createValue(roughness);

	GeneralPathX gpx = new GeneralPathX();
	gpx.append(startNode.getGeometry(), true);
	gpx.append(endNode.getGeometry(), true);
	IGeometry geom = ShapeFactory.createPolyline2D(gpx);

	IFeature feat = new DefaultFeature(geom, values);
	return feat;
    }

    public static IFeature createTankFeature(double x, double y, int elevation,
	    int initLevel, int minLevel, int maxLevel, double diameter) {
	Value[] values = new Value[5];
	values[0] = ValueFactory.createValue(elevation);
	values[1] = ValueFactory.createValue(initLevel);
	values[2] = ValueFactory.createValue(minLevel);
	values[3] = ValueFactory.createValue(maxLevel);
	values[4] = ValueFactory.createValue(diameter);
	IGeometry geom = ShapeFactory.createPoint2D(x, y);
	IFeature feat = new DefaultFeature(geom, values);
	return feat;
    }

    protected static void createTankShp(File file, IFeature[] features)
	    throws Exception {
	FieldDescriptionFactory fdFactory = new FieldDescriptionFactory();
	fdFactory.addInteger("elevation");
	fdFactory.addInteger("initlevel");
	fdFactory.addInteger("minlevel");
	fdFactory.addInteger("maxlevel");
	fdFactory.addDouble("diameter");
	FieldDescription[] fieldsDesc = fdFactory.getFields();
	SHPFactory.createSHP(file, fieldsDesc, FShape.POINT, features);
    }

    protected static IFeature createFCVFeature(double x, double y,
	    int elevation, double diameter, double flow) {
	Value[] values = new Value[3];
	values[0] = ValueFactory.createValue(elevation);
	values[1] = ValueFactory.createValue(diameter);
	values[2] = ValueFactory.createValue(flow);
	IGeometry geom = ShapeFactory.createPoint2D(x, y);
	IFeature feat = new DefaultFeature(geom, values);
	return feat;
    }

    public static void createFCVShp(File file, IFeature[] features)
	    throws Exception {
	FieldDescriptionFactory fdFactory = new FieldDescriptionFactory();
	fdFactory.addInteger("elevation");
	fdFactory.addDouble("diameter");
	fdFactory.addDouble("flow");
	FieldDescription[] fieldsDesc = fdFactory.getFields();
	SHPFactory.createSHP(file, fieldsDesc, FShape.POINT, features);
    }

    public static IFeature createPumpFeature(double x, double y,
	    double elevation, String type, String value) {
	Value[] values = new Value[3];
	values[0] = ValueFactory.createValue(elevation);
	values[1] = ValueFactory.createValue(type);
	values[2] = ValueFactory.createValue(value);
	IGeometry geom = ShapeFactory.createPoint2D(x, y);
	IFeature feat = new DefaultFeature(geom, values);
	return feat;
    }

    public static void createPumpShp(File file, IFeature[] features)
	    throws Exception {
	FieldDescriptionFactory fdFactory = new FieldDescriptionFactory();
	fdFactory.addInteger("elevation");
	fdFactory.addString("type");
	fdFactory.addString("value");
	FieldDescription[] fieldsDesc = fdFactory.getFields();
	SHPFactory.createSHP(file, fieldsDesc, FShape.POINT, features);
    }

}
