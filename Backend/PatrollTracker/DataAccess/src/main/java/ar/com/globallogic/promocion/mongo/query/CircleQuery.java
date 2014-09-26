package ar.com.globallogic.promocion.mongo.query;

import java.util.Iterator;
import java.util.List;

import org.apache.commons.collections.IteratorUtils;
import org.jongo.Jongo;
import org.jongo.MongoCollection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import ar.com.globallogic.promocion.commons.CommonsConstants;
import ar.com.globallogic.promocion.mongo.model.Trackeable;
import ar.com.globallogic.promocion.mongo.model.Zone;

@Component
public class CircleQuery implements ZoneQuery {

	@Autowired
	Jongo jongo;

	@SuppressWarnings("unchecked")
	@Override
	public List<Trackeable> execute(Zone zone) {


		MongoCollection collection = jongo
				.getCollection(CommonsConstants.TRACKEABLE_COLLECTION);

		Iterator<Trackeable> iterator = collection
				.find(getFindQuery(), zone.getPoints().get(0).getLongitude(),
						zone.getPoints().get(0).getLatitude(),
						zone.getRadius() / 6371).as(Trackeable.class)
				.iterator();

		return IteratorUtils.toList(iterator);
	}

	@Override
	public Boolean ifOutOfZone(Zone zone, String id) {
		
		String query = "{ _id : # , currentPosition: {$geoWithin: { $centerSphere : [[ #,#],#]}}}";
		
		MongoCollection collection = jongo.getCollection(CommonsConstants.TRACKEABLE_COLLECTION);
		
		 Trackeable trackeable = collection.findOne(query,
				 		id , 
				 		zone.getPoints().get(0).getLongitude(),
						zone.getPoints().get(0).getLatitude(),
						zone.getRadius() / 6371)
		.as(Trackeable.class);
		
		
		return trackeable == null;
	
	}

	public String getFindQuery() {
		return "{ currentPosition: {$geoWithin: { $centerSphere : [[ #,#],#]}}}";
	}

}
