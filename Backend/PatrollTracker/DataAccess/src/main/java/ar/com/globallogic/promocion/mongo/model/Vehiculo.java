package ar.com.globallogic.promocion.mongo.model;


public abstract class Vehiculo extends Trackeable{

	private Assignment assignments;

	public Boolean getIsVehicle() {		
		return true;
	}
	public Assignment getAssignments() {
		return assignments;
	}

	public void setAssignments(Assignment assignments) {
		this.assignments = assignments;
	}

}
