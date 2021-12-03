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
        long startTime = System.nanoTime();
        pg.importData(input);
        long endTime   = System.nanoTime();
        long totalTime = endTime - startTime;
        System.out.print("import time: "+(totalTime/1000000) +"ms");
        switch(args[2]){
            case "pgdf":
                startTime = System.nanoTime();
                pg.export("", "output");
                endTime   = System.nanoTime();
                totalTime = endTime - startTime;
                System.out.print("export pgdf time: "+(totalTime/1000000) +"ms");
                break;
            case "ypg":
                startTime = System.nanoTime();
                pg.exportToYARSPG("", "output");
                endTime   = System.nanoTime();
                totalTime = endTime - startTime;
                System.out.print("export ypg time: "+(totalTime/1000000) +"ms");
                break;
            case "gml":
                startTime = System.nanoTime();
                pg.exportToGraphML("", "output");
                endTime   = System.nanoTime();
                totalTime = endTime - startTime;
                System.out.print("export gml time: "+(totalTime/1000000) +"ms");
                break;
            case "json":
                startTime = System.nanoTime();
                pg.exportToJSON("", "output");
                endTime   = System.nanoTime();
                totalTime = endTime - startTime;
                System.out.print("export json time: "+(totalTime/1000000) +"ms");
                break;
            case "all":
                startTime = System.nanoTime();
                pg.export("", "output");
                endTime   = System.nanoTime();
                totalTime = endTime - startTime;
                System.out.print("export pgdf time: "+(totalTime/1000000) +"ms");
                startTime = System.nanoTime();
                pg.exportToYARSPG("", "output");
                endTime   = System.nanoTime();
                totalTime = endTime - startTime;
                System.out.print("export ypg time: "+(totalTime/1000000) +"ms");
                startTime = System.nanoTime();
                pg.exportToGraphML("", "output");
                endTime   = System.nanoTime();
                totalTime = endTime - startTime;
                System.out.print("export gml time: "+(totalTime/1000000) +"ms");
                startTime = System.nanoTime();
                pg.exportToJSON("", "output");
                endTime   = System.nanoTime();
                totalTime = endTime - startTime;
                System.out.print("export json time: "+(totalTime/1000000) +"ms");
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
