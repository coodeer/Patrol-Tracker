package ar.com.globallogic.promocion.mongo.model;

import org.jongo.marshall.jackson.oid.Id;

public class ChanellInfo {
	private Position southWest;
	private Position northEast;
	@Id
	private String id;
	
	public ChanellInfo() {
		super();
	}
	
	
	public Position getNorthEast() {
		return northEast;
	}

	public void setNorthEast(Position northEast) {
		this.northEast = northEast;
	}

	public Position getSouthWest() {
		return southWest;
	}

	public void setSouthWest(Position southWest) {
		this.southWest = southWest;
	}


	public String getId() {
		return id;
	}


	public void setId(String id) {
		this.id = id;
	}



}
