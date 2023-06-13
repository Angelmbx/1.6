package main;

import es.udc.teis.model.*;

import java.nio.*;
import java.nio.file.Paths;
import java.io.*;
import java.util.*;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.*;

public class mainB {

	private static final String PERSONAS_NS_DEFAULT_URI = "http://www.personas.com";
	private static final String PERSONAS_NS_ACTIVE_URI = "http://www.personas.com/active";

	private static final String PERSONA_TAG = "persona";

	private static final String PERSONA_NOMBRE_TAG = "nombre";
	private static final String PERSONA_DNI_TAG = "dni";
	private static final String PERSONA_EDAD_TAG = "edad";
	private static final String PERSONA_SALARIO_TAG = "salario";
	private static final String PERSONA_ATT_ID = "id";
	private static final String PERSONA_ATT_BORRADO = "borrado";

	private static final String PERSONAS_INPUT_FILE = Paths.get("src", "docs", "personas_ns.xml").toString();

	public static void main(String[] args) {

		String nombre;
		String dni;
		int edad = 0;
		float salario = 0f;
		int id = 0;
		boolean borrado = false;

		Persona persona = null;
		ArrayList<Persona> noActivas = new ArrayList<>();
		ArrayList<Persona> activas = new ArrayList<>();

		try {

			File inputFile = new File(PERSONAS_INPUT_FILE);

			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			dbFactory.setNamespaceAware(true);

			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			Document doc = dBuilder.parse(inputFile);

			System.out.println("Root element :" + doc.getDocumentElement().getNodeName());

			// no activas:
			NodeList nListNoActivas = doc.getElementsByTagName(PERSONA_TAG);

			System.out.println("----------------------------");
			for (int i = 0; i < nListNoActivas.getLength(); i++) {
				Node nNode = nListNoActivas.item(i);

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

					noActivas.add(persona);

				}
			}

			// activas:
			NodeList nListActivas = doc.getElementsByTagNameNS(PERSONAS_NS_ACTIVE_URI, PERSONA_TAG);
			System.out.println("----------------------------");
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

			System.out.println("Listado de personas no activas: ");
			System.out.println(noActivas.toString());
			System.out.println("---------------------------------");
			System.out.println(activas.toString());

		} catch (Exception e) {
			e.printStackTrace();
			System.err.println("Ha ocurrido una exception: " + e.getMessage());
		}

	}

}
