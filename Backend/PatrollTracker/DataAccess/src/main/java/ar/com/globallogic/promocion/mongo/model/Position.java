package ar.com.globallogic.promocion.mongo.model;

public class Position {

	private Double longitude;
	private Double latitude;
	public Position(double latitude, double longitude) {
		super();
		this.latitude = latitude;
		this.longitude= longitude;
	}
	public Position() {
		super();
	}
	public Double getLongitude() {
		return longitude;
	}
	public void setLongitude(Double longitude) {
		this.longitude = longitude;
	}
	
	public Double getLatitude() {
		return latitude;
	}
	public void setLatitude(Double latitude) {
		this.latitude = latitude;
	}

}
