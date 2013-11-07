package es.udc.cartolab.gvsig.epanet.config;

/**
 * There are some options more related to fonsagua that to epanet, that must be
 * handled directly by gvsig-epanet to avoid circular dependencies between the
 * plugin
 * 
 * I try to keep those external parameters in this class to handle it in a
 * propper way when we get time for it
 * 
 */
public class FonsaguaAlternative {
    public static String code = "";
    public static String alternativePK = "cod_alternativa";
}
