package ar.com.globallogic.promocion.mongo.model;

import java.util.Date;

public class PositionRecord {

	private Date date;
	private Position postion;

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public Position getPostion() {
		return postion;
	}

	public void setPostion(Position postion) {
		this.postion = postion;
	}
}
