package es.udc.cartolab.gvsig.epanet.structures.validations;

import com.iver.cit.gvsig.fmap.layers.FLyrVect;

import es.udc.cartolab.gvsig.epanet.exceptions.InvalidNetworkError;

public interface LayerChecker {
    
    public abstract Warning check(FLyrVect layer) throws InvalidNetworkError;

}
