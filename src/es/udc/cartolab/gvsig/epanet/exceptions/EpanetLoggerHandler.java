package es.udc.cartolab.gvsig.epanet.exceptions;

import java.util.ArrayList;
import java.util.logging.Handler;
import java.util.logging.LogRecord;

public class EpanetLoggerHandler extends Handler {

    private ArrayList<Throwable> stack = new ArrayList<Throwable>();

    public ArrayList<Throwable> getStack() {
	return stack;
    }

    @Override
    public void publish(LogRecord record) {
	stack.add(record.getThrown());

    }

    @Override
    public void flush() {
    }

    @Override
    public void close() throws SecurityException {
    }

}