package ar.com.globallogic.promocion.mongo.repository;

import org.jongo.Jongo;
import org.jongo.MongoCollection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import ar.com.globallogic.promocion.commons.CommonsConstants;
import ar.com.globallogic.promocion.mongo.model.Zone;

@Component
public class ZoneRepository implements zoneRepo {

	@Autowired
	Jongo jongo;

	@Override
	public void save(Zone zone) {
		jongo.getCollection(CommonsConstants.ZONE_COLLECTION).save(zone);
	}

	@Override
	public Zone findById(String id) {
		MongoCollection collection = jongo
				.getCollection(CommonsConstants.ZONE_COLLECTION);
		return collection.findOne("{_id:#}", id).as(Zone.class);
	}

}
