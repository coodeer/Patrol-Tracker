package ar.com.globallogic.promocion.mongo.model;



public class Gendarme extends Trackeable{

	private Vehiculo assignedVehicle;
	
	public String getType() {
		return "gendarme";
	}

	
	public Boolean getIsVehicle() {
		return false;
	}

	public Vehiculo getAssignedVehicle() {
		return assignedVehicle;
	}

	public void setAssignedVehicle(Vehiculo assignedVehicle) {
		this.assignedVehicle = assignedVehicle;
	}

}
