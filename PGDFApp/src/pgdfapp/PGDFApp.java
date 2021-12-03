/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pgdfapp;

import PropertyGraphDF.PropertyGraph;
import java.io.IOException;
import java.sql.SQLException;

/**
 *
 * @author cacac
 */
public class PGDFApp {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws IOException, SQLException {
        
        if(args.length != 3){
            help();
            return;
        }
        String input = args[0];
        PropertyGraph pg = null;
        if(args[1].equals("memory")){
            pg = new PropertyGraph("memory");
        }
        else if(args[1].equals("disk")){
            pg = new PropertyGraph("disk");
        }
        else{
            System.out.println("Storage type can only be 'memory' or 'disk'.");
            return;
        }
        pg.importData(input);
        switch(args[2]){
            case "pgdf":
                pg.export("", "output");
                break;
            case "ypg":
                pg.exportToYARSPG("","output");
                break;
            case "gml":
                pg.exportToGraphML("", "output");
                break;
            case "json":
                pg.exportToJSON("", "output");
                break;
            case "all":
                pg.export("", "output");
                pg.exportToYARSPG("","output");
                pg.exportToGraphML("", "output");
                pg.exportToJSON("", "output");
                break;
            default:
                System.out.println("Output type can only be 'pgdf', 'gml', 'ypg' or 'json'.");
                break;
        }        
    }
       
    public static void help(){
        System.out.println("Execute instruction and arguments:");
        System.out.println("java -jar <input> <storage> <output>");
        System.out.println("input: path to the input file. it must be a .pgdf file.");
        System.out.println("storage: indicate the storage type to use (memory or disk)");
        System.out.println("output: indicate the format to the output file. The only available formats are pgdf, gml, "
                + "json and ypg. If you want to export to all of them, use 'all' as an output. (just used on test)");
    }
}
