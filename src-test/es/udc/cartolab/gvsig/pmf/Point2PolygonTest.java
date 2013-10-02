package es.udc.cartolab.gvsig.pmf;

import java.io.File;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import com.hardcode.gdbms.engine.values.Value;
import com.hardcode.gdbms.engine.values.ValueFactory;
import com.iver.cit.gvsig.fmap.core.DefaultFeature;
import com.iver.cit.gvsig.fmap.core.FShape;
import com.iver.cit.gvsig.fmap.core.IFeature;
import com.iver.cit.gvsig.fmap.core.IGeometry;
import com.iver.cit.gvsig.fmap.core.ShapeFactory;
import com.iver.cit.gvsig.fmap.core.v02.FConverter;
import com.iver.cit.gvsig.fmap.drivers.FieldDescription;
import com.iver.cit.gvsig.fmap.layers.FLyrVect;
import com.iver.cit.gvsig.fmap.layers.ReadableVectorial;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.GeometryCollection;
import com.vividsolutions.jts.geom.GeometryFactory;

import es.udc.cartolab.gvsig.epanet.utils.TestProperties;
import es.udc.cartolab.gvsig.shputils.Drivers;
import es.udc.cartolab.gvsig.shputils.FieldDescriptionFactory;
import es.udc.cartolab.gvsig.shputils.SHPFactory;

public class Point2PolygonTest {

    @Rule
    public TemporaryFolder temp = new TemporaryFolder();

    @BeforeClass
    public static void setUpBeforeClass() throws Exception {
	Drivers.initgvSIGDrivers(TestProperties.driversPath);
    }

    public IFeature createFeature(double x, double y, int id) {
	Value[] values = new Value[1];
	values[0] = ValueFactory.createValue(id);
	IGeometry geom = ShapeFactory.createPoint2D(x, y);
	IFeature feat = new DefaultFeature(geom, values);
	return feat;
    }

    public IFeature createFeature(IGeometry geom, int id) {
	Value[] values = new Value[1];
	values[0] = ValueFactory.createValue(id);
	IFeature feat = new DefaultFeature(geom, values);
	return feat;
    }

    @Before
    public void setUp() throws Exception {

    }

    @Test
    public void test() throws Exception {
	FieldDescriptionFactory fdFactory = new FieldDescriptionFactory();
	fdFactory.addInteger("id");
	FieldDescription[] fieldsDesc = fdFactory.getFields();

	File file = temp.newFile("points.shp");
	final IFeature f1 = createFeature(100, 100, 1);
	final IFeature f2 = createFeature(100, -100, 1);
	final IFeature f3 = createFeature(-100, 100, 1);
	final IFeature f4 = createFeature(-100, -100, 1);
	final IFeature f5 = createFeature(50, 50, 1);

	SHPFactory.createSHP(file, fieldsDesc, FShape.POINT, new IFeature[] {
		f1, f2, f3, f4, f5 });

	FLyrVect points = SHPFactory.getFLyrVectFromSHP(file);

	ReadableVectorial readableVectorial = points.getSource();
	Geometry[] geometries = new Geometry[readableVectorial.getShapeCount()];

	for (int i = 0; i < readableVectorial.getShapeCount(); i++) {
	    IFeature iFeature = readableVectorial.getFeature(i);
	    IGeometry gvGeom = iFeature.getGeometry();
	    geometries[i] = gvGeom.toJTSGeometry();
	}

	GeometryFactory geometryFactory = new GeometryFactory();
	GeometryCollection createGeometryCollection = geometryFactory
		.createGeometryCollection(geometries);

	Geometry convexHull = createGeometryCollection.convexHull();

	IGeometry jts_to_igeometry = FConverter.jts_to_igeometry(convexHull);

	addToShp(jts_to_igeometry);

	System.out.println("Done!");
    }

    private void addToShp(IGeometry jts_to_igeometry) throws Exception {
	FieldDescriptionFactory fdFactory = new FieldDescriptionFactory();
	fdFactory.addInteger("id");
	FieldDescription[] fieldsDesc = fdFactory.getFields();
	File file = temp.newFile("polygon.shp");

	SHPFactory.createSHP(file, fieldsDesc, FShape.POLYGON,
		new IFeature[] { createFeature(jts_to_igeometry, 1) });
    }
}
