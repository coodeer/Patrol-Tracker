package ar.com.globallogic.promocion.service;

import java.util.Iterator;
import java.util.List;

import org.apache.commons.collections.IteratorUtils;
import org.jongo.Jongo;
import org.jongo.MongoCollection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import ar.com.globallogic.promocion.commons.CommonsConstants;
import ar.com.globallogic.promocion.mongo.model.Zone;

@Component
public class ZoneService {

	@Autowired
	Jongo jongo;
	
	public Zone save(Zone zone) {
		MongoCollection collection = jongo.getCollection(CommonsConstants.ZONE_COLLECTION);
		collection.save(zone);
		return zone;
	}

	@SuppressWarnings("unchecked")
	public List<Zone> getAll() {
		MongoCollection collection = jongo.getCollection(CommonsConstants.ZONE_COLLECTION);
		Iterator<Zone> iterator = collection.find().as(Zone.class).iterator();
		
		return IteratorUtils.toList(iterator);
	}

}
