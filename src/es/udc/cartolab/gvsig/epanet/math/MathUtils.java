package es.udc.cartolab.gvsig.epanet.math;

public class MathUtils {

    /**
     * Suppress default constructor for noninstantiability. AssertionError
     * avoids accidentally invoke the constructor within the class
     */
    private MathUtils() {
	throw new AssertionError();
    }

    public static double PRECISION = 0.01d;

    public static boolean compare(double x, double y) {
	return Math.abs(x - y) < PRECISION;
    }

    public static boolean inClosedInterval(double leftEndPoint, double value,
	    double rightEndPoint) {
	return value >= leftEndPoint && value <= rightEndPoint;
    }
}
