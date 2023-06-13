package main;

import es.udc.teis.model.*;
import java.nio.*;
import java.nio.file.Paths;
import java.io.*;
import java.util.*;
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

import org.w3c.dom.*;

public class mainC {


		private static final String PERSONAS_NS_DEFAULT_URI = "http://www.personas.com";
		private static final String PERSONAS_NS_ACTIVE_URI = "http://www.personas.com/active";

		private static final String PERSONA_TAG = "persona";
		private static final String PERSONAS_TAG = "persona";

		private static final String PERSONAS_DTD_FILE = "personas.dtd";
		
		
		private static final String PERSONA_NOMBRE_TAG = "nombre";
		private static final String PERSONA_DNI_TAG = "dni";
		private static final String PERSONA_EDAD_TAG = "edad";
		private static final String PERSONA_SALARIO_TAG = "salario";
		private static final String PERSONA_ATT_ID = "id";
		private static final String PERSONA_ATT_BORRADO = "borrado";

		private static final String PERSONAS_INPUT_FILE = Paths.get("src", "docs", "personas_ns.xml").toString();
		private static final String PERSONAS_OUTPUT_FILE = Paths.get("src", "docs", "miListaActivas.xml").toString();

		public static void main(String[] args) {
			//obtenemos lista de activas
			getActivasList();
		
			
			//ahora creamos el doumento con dicha lista
			
			buildXMLactivas();
			

		}
		
		
		private static ArrayList<Persona> getActivasList() {

			Persona persona = null;
			ArrayList<Persona> activas = new ArrayList<>();
			
			try {

				File inputFile = new File(PERSONAS_INPUT_FILE);

				DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
				dbFactory.setNamespaceAware(true);

				DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
				Document doc = dBuilder.parse(inputFile);

				System.out.println("Root element :" + doc.getDocumentElement().getNodeName());

			
				// activas:
				NodeList nListActivas = doc.getElementsByTagNameNS(PERSONAS_NS_ACTIVE_URI, PERSONA_TAG);
				System.out.println("----------------------------");
				
				//creamos lista
				activas = createActivasList(nListActivas);

				System.out.println("---------------------------------");
				System.out.println(activas.toString());

			} catch (Exception e) {
				e.printStackTrace();
				System.err.println("Ha ocurrido una exception: " + e.getMessage());
			}
			
			
			return activas;
		}

		private static ArrayList<Persona> createActivasList(NodeList nListActivas) {

		        double numero = 0;
		        int edad = 0;
		        int id = 0;
		        String nombre = "", dni = "";
		        float salario = 0;
		        Persona persona = null;
		        boolean borrado = false;
		        ArrayList<Persona> activas = new ArrayList<>();
		        
		        for (int i = 0; i < nListActivas.getLength(); i++) {
					Node nNode = nListActivas.item(i);

					if (nNode.getNodeType() == Node.ELEMENT_NODE) {
						Element eElement = (Element) nNode;

						id = Integer.parseInt(eElement.getAttribute(PERSONA_ATT_ID));
						borrado = Boolean.parseBoolean(eElement.getAttribute(PERSONA_ATT_BORRADO));

						nombre = eElement.getElementsByTagName(PERSONA_NOMBRE_TAG).item(0).getTextContent();

						edad = Integer.parseInt(eElement.getElementsByTagName(PERSONA_EDAD_TAG).item(0).getTextContent());
						dni = eElement.getElementsByTagName(PERSONA_DNI_TAG).item(0).getTextContent();
						salario = Float
								.parseFloat(eElement.getElementsByTagName(PERSONA_SALARIO_TAG).item(0).getTextContent());

						persona = new Persona(id, nombre, dni, edad, salario);
						persona.setBorrado(borrado);

						activas.add(persona);

					}
				}

		        return activas;
		    }

		
		private static void buildXMLactivas() {
			
			  try {

		            ArrayList<Persona> personas = getActivasList();

		            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		            DocumentBuilder builder = factory.newDocumentBuilder();

		            DOMImplementation implementation = builder.getDOMImplementation();
		            //Si el documento XML necesita un DOCTYPE:
		            DocumentType docType = implementation.createDocumentType(PERSONAS_TAG, null, PERSONAS_DTD_FILE);
		            //Crea un document con un elmento raiz
		            Document document = implementation.createDocument(null, PERSONAS_TAG, docType);

		            //Si no se necesita DOCTYPE se podría llamar a createDocument con el tercer parámetro a null
		            //Document document = implementation.createDocument(null, VERSIONES_TAG, docType);
		            
		            
		            //Obtenemos el elemento raíz
		            Element root = document.getDocumentElement();
		            
		            //Existe otra posibilidad para la creación de un document totalmente vacío, al que hay que añadirle un elemento raíz:
		            
//		            //Crear un nuevo documento XML VACÍO
//		            Document document = builder.newDocument();
//		            //Crear el nodo raíz y añadirlo al documento
//		            Element root = document.createElement(VERSIONES_TAG);
//		            document.appendChild(root);
		            
		            for (Persona persona : personas) {
		                //desde el document creamos un nuevo elemento
		                Element eVersion = document.createElement(PERSONA_TAG);
		                eVersion.setAttribute(PERSONA_ATT_ID, String.valueOf(persona.getId()));

		                addElementConTexto(document, eVersion, PERSONA_NOMBRE_TAG, persona.getNombre());
		                addElementConTexto(document, eVersion, PERSONA_DNI_TAG, String.valueOf(persona.getDni()));
		                addElementConTexto(document, eVersion, PERSONA_EDAD_TAG, String.valueOf(persona.getEdad()));
		                addElementConTexto(document, eVersion, PERSONA_SALARIO_TAG, String.valueOf(persona.getSalario()));

		                
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
		            Result destino = new StreamResult(PERSONAS_OUTPUT_FILE);
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





