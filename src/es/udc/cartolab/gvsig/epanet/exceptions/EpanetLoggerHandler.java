package es.udc.cartolab.gvsig.epanet.exceptions;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Handler;
import java.util.logging.LogRecord;

import org.addition.epanet.util.ENException;

/**
 * Baseform logs some exceptions, in the logger provider by the user code, and
 * throws others. It not seems to exist a criteria about when a exception is
 * thrown and when is logged. The intend of this class is to be added to the
 * Logger passed to baseform to record those exceptions that baseform logs.
 * Then, when the flow returns to the client code, a helper method
 * throwException will thrown the first ENExceptions recorded by this handler if
 * any, or the first not ENException recorded
 * 
 */
public class EpanetLoggerHandler extends Handler {

    private List<ENException> epanetExceptions = new ArrayList<ENException>();
    private List<Throwable> otherExceptions = new ArrayList<Throwable>();

    @Override
    public void publish(LogRecord record) {
	// TODO: Not sure if this getThrown will work for all situations. This
	// should be heavily tested
	if (record.getThrown() instanceof ENException) {
	    epanetExceptions.add((ENException) record.getThrown());
	} else {
	    otherExceptions.add(record.getThrown());
	}
    }

    @Override
    public void flush() {
    }

    @Override
    public void close() throws SecurityException {
    }

    public boolean hasExceptions() {
	return (!epanetExceptions.isEmpty()) || (!otherExceptions.isEmpty());
    }

    public void throwException() throws ENException {
	if (!epanetExceptions.isEmpty()) {
	    final ENException enException = epanetExceptions.get(0);
	    resetStack();
	    throw enException;
	} else if (!otherExceptions.isEmpty()) {
	    final Throwable oException = otherExceptions.get(0);
	    resetStack();
	    throw new RuntimeException(oException);
	}
    }

    private void resetStack() {
	epanetExceptions.clear();
	epanetExceptions = new ArrayList<ENException>();
	otherExceptions.clear();
	otherExceptions = new ArrayList<Throwable>();
    }

}