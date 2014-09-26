package ar.com.globallogic.promocion.mongo.model;

import java.util.List;

import org.jongo.marshall.jackson.oid.Id;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

@JsonTypeInfo(use=JsonTypeInfo.Id.NAME, 
include=JsonTypeInfo.As.PROPERTY,property="type"
)
@JsonSubTypes({
	@JsonSubTypes.Type(value = Gendarme.class, name = "gendarme"),
	@JsonSubTypes.Type(value = Auto.class, name = "auto"),
	@JsonSubTypes.Type(value = Camioneta.class, name = "camioneta")

})
@JsonInclude(Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public abstract class Trackeable {

	@Id
	private String id;
	private String name;
	private Zone assignedZone;

	private Position currentPosition;
	private List<Events> lastEvents;
	private Boolean redFlag;
	
	
	public abstract Boolean getIsVehicle();
	public abstract String getType();

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Zone getAssignedZone() {
		return assignedZone;
	}

	public void setAssignedZone(Zone assignedZone) {
		this.assignedZone = assignedZone;
	}

	public Position getCurrentPosition() {
		return currentPosition;
	}

	public void setCurrentPosition(Position currentPosition) {
		this.currentPosition = currentPosition;
	}

	public List<Events> getLastEvents() {
		return lastEvents;
	}

	public void setLastEvents(List<Events> lastEvents) {
		this.lastEvents = lastEvents;
	}

	public Boolean getRedFlag() {
		return redFlag;
	}

	public void setRedFlag(Boolean redFlag) {
		this.redFlag = redFlag;
	}


}