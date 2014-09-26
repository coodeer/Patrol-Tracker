package ar.com.globallogic.promocion.mongo.model;

public class Assignment {

	private Reference titular;
	private Reference crew;

	public Reference getCrew() {
		return crew;
	}

	public void setCrew(Reference crew) {
		this.crew = crew;
	}

	public Reference getTitular() {
		return titular;
	}

	public void setTitular(Reference titular) {
		this.titular = titular;
	}

}
