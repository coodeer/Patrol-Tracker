package ar.com.globallogic.promocion.service;

import org.jongo.marshall.jackson.oid.Id;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_NULL)
public class Notification {
	@Id
	private String id;
	private String event_id;
	private String description;
	private String trackeable_id;
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getEvent_id() {
		return event_id;
	}
	public void setEvent_id(String event_id) {
		this.event_id = event_id;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getTrackeable_id() {
		return trackeable_id;
	}
	public void setTrackeable_id(String trackeable_id) {
		this.trackeable_id = trackeable_id;
	}


}
