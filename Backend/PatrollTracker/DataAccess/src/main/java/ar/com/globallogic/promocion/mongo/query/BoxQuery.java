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
public class BoxQuery implements ZoneQuery {

	@Autowired
	Jongo jongo;

	@SuppressWarnings("unchecked")
	@Override
	public List<Trackeable> execute(Zone zone) {


		MongoCollection collection = jongo
				.getCollection(CommonsConstants.TRACKEABLE_COLLECTION);

		Iterator<Trackeable> iterator = collection
				.find(getFindQuery(), 
						zone.getPoints().get(0).getLongitude(),
						zone.getPoints().get(0).getLatitude(),
						zone.getPoints().get(1).getLongitude(),
						zone.getPoints().get(1).getLatitude())
				.as(Trackeable.class).iterator();

		return IteratorUtils.toList(iterator);
	}

	public String getFindQuery() {
		return  "{ currentPosition : { $geoWithin : { $box : [ [ #,# ] , [ #,# ] ] } } }";
	}

	@Override
	public Boolean ifOutOfZone(Zone zone, String id) {
		
		MongoCollection collection = jongo
				.getCollection(CommonsConstants.TRACKEABLE_COLLECTION);

		String query="{ _id : # , currentPosition : { $geoWithin : { $box : [ [ #,# ] , [ #,# ] ] } } }";
		 Trackeable trackeable = collection
				.findOne(query,id, 
						zone.getPoints().get(0).getLongitude(),
						zone.getPoints().get(0).getLatitude(),
						zone.getPoints().get(1).getLongitude(),
						zone.getPoints().get(1).getLatitude())
				.as(Trackeable.class);
		 return trackeable == null;
	}


}
