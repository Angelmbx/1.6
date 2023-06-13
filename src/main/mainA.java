package main;


import es.udc.teis.model.*;

import java.nio.*;
import java.nio.file.Paths;
import java.io.*;
import java.util.*;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.*;


public class mainA {

	private static final String PERSONA_TAG = "persona";
    private static final String PERSONA_NOMBRE_TAG = "nombre";
    private static final String PERSONA_DNI_TAG = "dni";
    private static final String PERSONA_EDAD_TAG = "edad";
    private static final String PERSONA_SALARIO_TAG = "salario";
    private static final String PERSONA_ATT_ID = "id";
    private static final String PERSONA_ATT_BORRADO = "borrado";
	
    private static final String PERSONAS_INPUT_FILE = Paths.get("src", "docs", "personas.xml").toString();
	
	public static void main(String[] args) {
	
	String nombre;
	String dni;
	int edad =0;
	float salario = 0f;
	int id = 0;
	boolean borrado=false;
	
	Persona persona = null;
	ArrayList<Persona> personas = new ArrayList<>();
	
		
	try {
        File inputFile = new File(PERSONAS_INPUT_FILE);

        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
        Document doc = dBuilder.parse(inputFile);
		
        
        System.out.println("Root element :"
                + doc.getDocumentElement().getNodeName());

   
        NodeList nList = doc.getElementsByTagName(PERSONA_TAG);

        System.out.println("----------------------------");
        for (int i = 0; i < nList.getLength(); i++) {
            Node nNode = nList.item(i);

            if (nNode.getNodeType() == Node.ELEMENT_NODE) {
                Element eElement = (Element) nNode;
                
                id = Integer.parseInt(eElement.getAttribute(PERSONA_ATT_ID));
                borrado = Boolean.parseBoolean(eElement.getAttribute(PERSONA_ATT_BORRADO));
                
                
                nombre = eElement.getElementsByTagName(PERSONA_NOMBRE_TAG).item(0).getTextContent();
             
                edad = Integer.parseInt(eElement.getElementsByTagName(PERSONA_EDAD_TAG).item(0).getTextContent());
                dni = eElement.getElementsByTagName(PERSONA_DNI_TAG).item(0).getTextContent();
                salario = Float.parseFloat(eElement.getElementsByTagName(PERSONA_SALARIO_TAG).item(0).getTextContent());
                
                
                persona = new Persona(id, nombre, dni, edad, salario);
                persona.setBorrado(borrado);

                personas.add(persona);
                
            }
        }

     
       System.out.println(personas.toString());
		
		
	} catch (Exception e) {
        e.printStackTrace();
        System.err.println("Ha ocurrido una exception: " + e.getMessage());
    }
		
		
		
	}

}
