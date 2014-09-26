package ar.com.globallogic.promocion.mongo.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jongo.marshall.jackson.oid.Id;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class Zone {

	@Id
	private String id;
	private List<Position> points;
	private Double radius;
	private String Shape;
	private Boolean isCritic;

	@JsonIgnore
	private Map<String,String> querynameMap; 
	
	public Zone(){
		super();
		this.points = new ArrayList<Position>();
		querynameMap = new HashMap<String, String>();
		querynameMap.put("CIRCLE", "circleQuery");
		querynameMap.put("POLYGON", "polygonQuery");
		querynameMap.put("BOX", "boxQuery");
		
	}
	
	public Boolean getIsCritic() {
		return isCritic;
	}

	public void setIsCritic(Boolean isCritic) {
		this.isCritic = isCritic;
	}

	
	public String getShape() {
		return Shape;
	}
	/**
	 * Retorna el nombre del bean query que sabe manejarlo
	 * @return String
	 */

	@JsonIgnore
	public String getQueryName() {
		return querynameMap.get(this.Shape);
	}

	public void setShape(String shape) {
		Shape = shape;
	}

	public Double getRadius() {
		return radius;
	}

	public void setRadius(Double radius) {
		this.radius = radius;
	}

	public List<Position> getPoints() {
		return points;
	}

	public void setPoints(List<Position> points) {
		this.points = points;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}
}
