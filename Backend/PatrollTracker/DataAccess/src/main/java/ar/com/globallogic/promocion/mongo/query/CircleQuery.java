package ar.com.globallogic.promocion.mongo.query;

import java.util.Iterator;
import java.util.List;

import org.apache.commons.collections.IteratorUtils;
import org.jongo.Jongo;
import org.jongo.MongoCollection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import ar.com.globallogic.promocion.commons.CommonsConstants;
import ar.com.globallogic.promocion.mongo.model.Trackeable;
import ar.com.globallogic.promocion.mongo.model.Zone;

@Component
public class CircleQuery implements ZoneQuery {

	@Autowired
	Jongo jongo;


	@Value("${testing_context}")
	Boolean testContext;
	
	@SuppressWarnings("unchecked")
	@Override
	public List<Trackeable> execute(Zone zone) {


		MongoCollection collection = getTrackeableCollection();

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
		
		MongoCollection collection = getTrackeableCollection();
		
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

	private MongoCollection getTrackeableCollection() {
		if(testContext){
			return jongo.getCollection(CommonsConstants.TRACKEABLE_COLLECTION_TEST);			
		}else{			
			return jongo.getCollection(CommonsConstants.TRACKEABLE_COLLECTION);
		}
	}
}
