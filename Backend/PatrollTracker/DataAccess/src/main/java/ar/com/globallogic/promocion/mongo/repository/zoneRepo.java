package ar.com.globallogic.promocion.mongo.repository;

import ar.com.globallogic.promocion.mongo.model.Zone;

public interface zoneRepo {

	void save(Zone zone);
	Zone findById(String id);
}
