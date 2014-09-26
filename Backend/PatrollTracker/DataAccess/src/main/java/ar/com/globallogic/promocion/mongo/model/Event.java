package ar.com.globallogic.promocion.mongo.model;

import java.util.Date;

import org.jongo.marshall.jackson.oid.Id;

public class Event {

	@Id
	private String id;
	private String trackeable_id;
	private int code;
	private String value;
	private Date date;
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public int getCode() {
		return code;
	}
	public void setCode(int code) {
		this.code = code;
	}
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
	public Date getDate() {
		return date;
	}
	public void setDate(Date date) {
		this.date = date;
	}
	public String getTrackeable_id() {
		return trackeable_id;
	}
	public void setTrackeable_id(String trackeable_id) {
		this.trackeable_id = trackeable_id;
	}
}
