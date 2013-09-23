package es.udc.cartolab.gvsig.epanet.exceptions;

//6xx code errors, is for errors that are not considered in epanet
public enum ErrorCode {
    FLOATING_PIPE(601), OVERLAPPED_NODES(602), ILLEGAL_VALUE(202);

    private int code;

    ErrorCode(int code) {
	this.code = code;
    }
}
