package es.udc.cartolab.gvsig.fonsagua.epanet.exceptions;

@SuppressWarnings("serial")
/**
 * Unrecovery exceptions related to the system and not with the application like I/O and son on
 *
 */
public class ExternalError extends RuntimeException {

    public ExternalError(Throwable cause) {
	super(cause);
    }
}
