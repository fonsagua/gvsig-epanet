package es.udc.cartolab.gvsig.fonsagua.epanet;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;

public class ComparatorUtils {

    public static boolean rptComparator(File f1, File f2) throws Exception {
    
        FileInputStream fstream1 = new FileInputStream(f1);
        FileInputStream fstream2 = new FileInputStream(f2);
    
        DataInputStream in1 = new DataInputStream(fstream1);
        DataInputStream in2 = new DataInputStream(fstream2);
    
        BufferedReader br1 = new BufferedReader(new InputStreamReader(in1));
        BufferedReader br2 = new BufferedReader(new InputStreamReader(in2));
    
        String strLine1, strLine2;
        br1.readLine();
        br2.readLine();
        while ((strLine1 = br1.readLine()) != null
        	&& (strLine2 = br2.readLine()) != null) {
            if (strLine1.contains("Analysis begun")
        	    || strLine1.contains("Analysis ended")) {
        	continue;
            }
            if (!strLine1.equals(strLine2)) {
        	System.out.println(strLine1);
        	System.out.println(strLine2);
        	return false;
            }
        }
        return true;
    }

}
