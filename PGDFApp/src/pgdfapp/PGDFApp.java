/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pgdfapp;

import PropertyGraphDF.PropertyGraph;
import java.io.IOException;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author cacac
 */
public class PGDFApp {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        
        if(args.length == 0){
            help();
            return;
        }
        PropertyGraph pg = null;
        String sourceFile = String.valueOf(args[0]);
        try{
            if(args[1].equals("-m")){
                System.out.println("Creando grafo en memoria.");
                pg = new PropertyGraph("memory");
            }
            else if(args[1].equals("-d")){
                System.out.println("Creando grafo en disco.");
                pg = new PropertyGraph("disk");
            }
            else{
                System.out.println("Falta tipo de almacenamiento (-m o -d).");
                return;
            }
            
            if(args.length > 2){
                if(args[2].equals("-e")){                    
                    String format = String.valueOf(args[3]);
                    if(format.equals("pgdf") || format.equals("gml") || format.equals("json") || format.equals("ypg")){
                        String directory = String.valueOf(args[4]);
                        System.out.println("Importando datos desde el archivo ingresado...");
                        long startTime = System.nanoTime();
                        pg.importData(sourceFile);
                        System.out.println("Exportando los datos al archivo ."+format);
                        if(directory.equals(" ")){
                            directory = "";
                        }
                        switch(format){
                            case "pgdf":
                                pg.export(directory, "PGDFApp");
                                break;                                
                            case "gml":
                                pg.exportToGraphML(directory, "PGDFApp");
                                break;
                            case "ypg":
                                pg.exportToYARSPG(directory, "PGDFApp");
                                break;
                            case "json":
                                pg.exportToJSON(directory, "PGDFApp");
                                break;
                            default: 
                                break;
                        }
                        long endTime   = System.nanoTime();
                        long totalTime = endTime - startTime;
                        System.out.println("Tiempo utilizado: " + (totalTime/1000000) +" ms");
                    }
                    else{
                        System.out.println("Formato de exportacion no válido.");
                    }
                }
                else{
                    System.out.println("Parametro ingresado no válido.");
                }
            }
        }
        catch(IndexOutOfBoundsException e){
            System.out.println("ERROR: Faltan parametros.");
            return;
        } catch (IOException ex) {
            Logger.getLogger(PGDFApp.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException ex) {
            Logger.getLogger(PGDFApp.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
       
    public static void help(){
        System.out.println("PGDFApp es una aplicacion por linea de comandos que permite el manejo de archivos .pgdf, principalmente para la transformacion a otros formatos.");
        System.out.println("Ademas, esta aplicacion puede utilizarse para verificar si un archivo .pgdf correponde al formato utilizado\n");        
        
        System.out.println("Entradas oblgatorias:");
        System.out.println("Example.pgdf (el directorio del archivo .pgdf a utilizar como entrada)");
        System.out.println("-m o -d (indica el tipo de almacenamiento de los datos para el grafo con propiedades)");
        System.out.println("Prefiera utilizar -d para archivos de gran tamaño o para grafos con muchas propiedades");
        
        System.out.println("Comandos opcionales:");
        System.out.println("    Para exportar...");
        System.out.println("    -e (formato) (directorio archivo generado)");
        System.out.println("    Formatos Disponibles: pgdf (formato por defecto, en caso de no indicar uno), gml (graphML), ypg (YARS-PG) y json (formato JSON aceptable por Neo4J)");
       
    }
}
