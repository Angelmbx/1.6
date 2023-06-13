/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package es.udc.teis;

import es.udc.teis.model.Version;
import java.nio.file.Paths;
import java.util.ArrayList;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.w3c.dom.DOMImplementation;
import org.w3c.dom.Document;
import org.w3c.dom.DocumentType;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

/**
 *
 * @author maria
 */
public class EjemploDOMCreate {

    private static final String VERSIONES_TAG = "versiones";
    private static final String VERSION_TAG = "version";
    private static final String VERSION_NOMBRE_TAG = "nombre";
    private static final String VERSION_API_TAG = "api";
    private static final String VERSION_ATT_NUMERO = "numero";

    private static final String VERSIONES_OUTPUT_FILE = Paths.get("src", "docs", "versiones_output.xml").toString();
    private static final String VERSIONES_DTD_FILE = "versiones.dtd";

    public static void main(String[] args) {
        try {

            ArrayList<Version> versions = crearVersions();

            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();

            DOMImplementation implementation = builder.getDOMImplementation();
            //Si el documento XML necesita un DOCTYPE:
            DocumentType docType = implementation.createDocumentType(VERSIONES_TAG, null, VERSIONES_DTD_FILE);
            //Crea un document con un elmento raiz
            Document document = implementation.createDocument(null, VERSIONES_TAG, docType);

            //Si no se necesita DOCTYPE se podría llamar a createDocument con el tercer parámetro a null
            //Document document = implementation.createDocument(null, VERSIONES_TAG, docType);
            
            
            //Obtenemos el elemento raíz
            Element root = document.getDocumentElement();
            
            //Existe otra posibilidad para la creación de un document totalmente vacío, al que hay que añadirle un elemento raíz:
            
//            //Crear un nuevo documento XML VACÍO
//            Document document = builder.newDocument();
//            //Crear el nodo raíz y añadirlo al documento
//            Element root = document.createElement(VERSIONES_TAG);
//            document.appendChild(root);
            
            for (Version version : versions) {
                //desde el document creamos un nuevo elemento
                Element eVersion = document.createElement(VERSION_TAG);
                eVersion.setAttribute(VERSION_ATT_NUMERO, String.valueOf(version.getNumero()));

                addElementConTexto(document, eVersion, VERSION_NOMBRE_TAG, version.getNombre());
                addElementConTexto(document, eVersion, VERSION_API_TAG, String.valueOf(version.getApi()));

                root.appendChild(eVersion);
            }

            //Para generar un documento XML con un objeto Document
            //Generar el tranformador para obtener el documento XML en un fichero
            TransformerFactory fabricaTransformador = TransformerFactory.newInstance();
            //Espacios para indentar cada línea
            fabricaTransformador.setAttribute("indent-number", 4);
            Transformer transformador = fabricaTransformador.newTransformer();
            //Insertar saltos de línea al final de cada línea
            //https://docs.oracle.com/javase/8/docs/api/javax/xml/transform/OutputKeys.html
            transformador.setOutputProperty(OutputKeys.INDENT, "yes");
         
          
            
            //Si se quisiera añadir el <!DOCTYPE>:
           // transformador.setOutputProperty(OutputKeys.DOCTYPE_SYSTEM, docType.getSystemId());
            //El origen de la transformación es el document
            Source origen = new DOMSource(document);
            //El destino será un stream a un fichero 
            Result destino = new StreamResult(VERSIONES_OUTPUT_FILE);
            transformador.transform(origen, destino);
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
            System.err.println("Ha ocurrido una exception: " + e.getMessage());
        } catch (TransformerConfigurationException e) {
            e.printStackTrace();
            System.err.println("Ha ocurrido una exception: " + e.getMessage());
        } catch (TransformerException e) {
            e.printStackTrace();
            System.err.println("Ha ocurrido una exception: " + e.getMessage());
        }

    }

    private static ArrayList<Version> crearVersions() {
        ArrayList<Version> versions = new ArrayList<>();

        Version versionA = new Version(1, "nombreA", 2);

        Version versionB = new Version(1.5, "nombreB", 3);
        Version versionC = new Version(2, "nombreC", 4);
        versions.add(versionA);
        versions.add(versionB);
        versions.add(versionC);

        return versions;
    }

    private static void addElementConTexto(Document document, Node padre, String tag, String text) {
        //Creamos un nuevo nodo de tipo elemento desde document
        Node node = document.createElement(tag);
        //Creamos un nuevo nodo de tipo texto también desde document
        Node nodeTexto = document.createTextNode(text);
        //añadimos a un nodo padre el nodo elemento
        padre.appendChild(node);
        //Añadimos al nodo elemento su nodo hijo de tipo texto
        node.appendChild(nodeTexto);
    }

}
