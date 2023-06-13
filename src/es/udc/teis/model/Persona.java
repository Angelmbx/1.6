package es.udc.teis.model;

public class Persona {

	private int id;
	private boolean borrado=false;
	private String nombre;
	private String dni;
	private int edad;
	private float salario;
	
	
	public Persona () {
	
	}
	
	public Persona(int id, String nombre, String dni, int edad, float salario) {
		super();
		this.id = id;
		this.nombre = nombre;
		this.dni = dni;
		this.edad = edad;
		this.salario = salario;
	}

	
	
	
	
	public boolean isBorrado() {
		return borrado;
	}

	public void setBorrado(boolean borrado) {
		this.borrado = borrado;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getDni() {
		return dni;
	}

	public void setDni(String dni) {
		this.dni = dni;
	}

	public int getEdad() {
		return edad;
	}

	public void setEdad(int edad) {
		this.edad = edad;
	}

	public float getSalario() {
		return salario;
	}

	public void setSalario(float salario) {
		this.salario = salario;
	}

	@Override
	public String toString() {
		return "Persona [id=" + id + ", borrado=" + borrado + ", nombre=" + nombre + ", dni=" + dni + ", edad=" + edad
				+ ", salario=" + salario + "]";
	}
	
	
	
	
	
	
}
