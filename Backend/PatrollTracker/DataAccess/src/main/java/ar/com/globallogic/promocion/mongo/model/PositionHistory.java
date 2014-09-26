package ar.com.globallogic.promocion.mongo.model;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.mongodb.core.mapping.Document;
@Document
public class PositionHistory {

	public PositionHistory(){
		super();
		setRecords(new ArrayList<PositionRecord>());
	}
	
	private String trackeable_id;
	private List<PositionRecord> records;


	public void addRecord(PositionRecord position){
		records.add(position);
	}

	public String getTrackeable_id() {
		return trackeable_id;
	}

	public void setTrackeable_id(String trackeable_id) {
		this.trackeable_id = trackeable_id;
	}

	public List<PositionRecord> getRecords() {
		return records;
	}

	public void setRecords(List<PositionRecord> records) {
		this.records = records;
	}
}
