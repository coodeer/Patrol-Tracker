package ar.com.globallogic.promocion.mongo.repository;

import java.util.Iterator;
import java.util.List;

import org.apache.commons.collections.IteratorUtils;
import org.jongo.Jongo;
import org.jongo.MongoCollection;
import org.jongo.MongoCursor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import ar.com.globallogic.promocion.commons.CommonsConstants;
import ar.com.globallogic.promocion.exception.UpdateException;
import ar.com.globallogic.promocion.mongo.model.ChanellInfo;
import ar.com.globallogic.promocion.mongo.model.Gendarme;
import ar.com.globallogic.promocion.mongo.model.Trackeable;
import ar.com.globallogic.promocion.mongo.model.Zone;
import ar.com.globallogic.promocion.mongo.query.ZoneQuery;

import com.mongodb.WriteResult;

@Component
public class TrackeableRepository implements TrackeableRepo {

	@Autowired
	Jongo jongo;

	@Autowired
	QueryProvider queryProvider;

	private MongoCollection collection;

	public Trackeable findById(String id) {
		Gendarme gendarme = jongo.getCollection("trackeable")
				.findOne("{_id : # }", id).as(Gendarme.class);
		return gendarme;
	}

	public List<Trackeable> findByZone(Zone zone) {
		ZoneQuery trackeaByZone = queryProvider.trackeaByZone(zone);

		return (List<Trackeable>) trackeaByZone.execute(zone);
	}

	public Boolean isOutOfZone(String id) {
		collection = jongo
				.getCollection(CommonsConstants.TRACKEABLE_COLLECTION);
		Trackeable trackeable = collection.findOne("{ _id : # }", id).as(
				Trackeable.class);
		return isOutOfZone(trackeable);

	}

	public Boolean isOutOfZone(Trackeable trackeable) {

		Boolean result = false;
		if (trackeable.getAssignedZone() != null) {
			ZoneQuery zoneQuery = queryProvider.getZoneMap().get(
					trackeable.getAssignedZone().getQueryName());
			result = zoneQuery.ifOutOfZone(trackeable.getAssignedZone(),
					trackeable.getId());
		}
		return result;

	}

	public Boolean isOutOfZone(String id, Zone zone) {
		Boolean result = false;
		ZoneQuery zoneQuery = queryProvider.getZoneMap().get(
				zone.getQueryName());
		result = zoneQuery.ifOutOfZone(zone, id);
		return result;
	}

	public List<Trackeable> findAll() {
		MongoCursor<Trackeable> mongocursor = jongo.getCollection("trackeable")
				.find().as(Trackeable.class);
		List<Trackeable> list = IteratorUtils.toList(mongocursor.iterator());
		return list;
	}

	public void save(Trackeable trackeable) {
		jongo.getCollection(CommonsConstants.TRACKEABLE_COLLECTION).save(
				trackeable);
	}

	public void assignZone(String id, Zone zone) {

		MongoCollection collection = jongo
				.getCollection(CommonsConstants.TRACKEABLE_COLLECTION);
		collection.update("{ _id : # }", id).with(
				"{ $set: {assignedZone: # } }", zone);
	}

	public Trackeable update(Trackeable trackeable) {

		if (trackeable.getId() == null) {
			throw new UpdateException("Objeto sin ID");
		}

		MongoCollection collection = jongo
				.getCollection(CommonsConstants.TRACKEABLE_COLLECTION);

		WriteResult writeResult = collection.update("{_id :#}",
				trackeable.getId()).with(trackeable);

		if (!writeResult.isUpdateOfExisting()) {
			throw new UpdateException("No se pudo hacer update ");
		}
		return trackeable;
	}


	@SuppressWarnings("unchecked")
	public List<Trackeable> getByType(String type) {
		MongoCollection collection = jongo
				.getCollection(CommonsConstants.TRACKEABLE_COLLECTION);
		Iterator<Trackeable> iterator = collection.find("{type: #}", type)
				.as(Trackeable.class).iterator();
		return IteratorUtils.toList(iterator);
	}

	@SuppressWarnings("unchecked")
	public List<Trackeable> getByTypeNear(String type, Double longitud,
			Double latitud) {
		String query = "{ type: #, location:{$nearSphere:{[#,#]}}}";
		MongoCollection collection = jongo
				.getCollection(CommonsConstants.TRACKEABLE_COLLECTION);
		Iterator<Trackeable> iterator = collection
				.find(query, type, longitud, latitud).as(Trackeable.class)
				.iterator();
		return IteratorUtils.toList(iterator);
	}

	@SuppressWarnings("unchecked")
	public List<Trackeable> getByTypeInBox(String type, Double swlongitud,
			Double swlatitud, Double nelongitud, Double nelatitud) {
		String query = "{ type: # , currentPosition : { $geoWithin : { $box : [ [ #,# ] , [ #,# ] ] } } }";

		MongoCollection collection = jongo
				.getCollection(CommonsConstants.TRACKEABLE_COLLECTION);
		Iterator<Trackeable> iterator = collection
				.find(query, type, swlongitud, swlatitud, nelongitud, nelatitud)
				.as(Trackeable.class).iterator();
		return IteratorUtils.toList(iterator);
	}

	public Trackeable getByIdInBox(String id, Double swlongitud,
			Double swlatitud, Double nelongitud, Double nelatitud) {
		String query = "{ _id: # , currentPosition : { $geoWithin : { $box : [ [ #,# ] , [ #,# ] ] } } }";

		MongoCollection collection = jongo
				.getCollection(CommonsConstants.TRACKEABLE_COLLECTION);
		Trackeable iterator = collection.findOne(query, id, swlongitud,
				swlatitud, nelongitud, nelatitud).as(Trackeable.class);
		return iterator;
	}

	@Override
	public Trackeable delete(Trackeable trackeable) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void saveChannel(ChanellInfo chanellInfo) {
		MongoCollection collection2 = jongo.getCollection(CommonsConstants.CHANELL_COLLECTION);
		ChanellInfo chanel = collection2.findOne().as(ChanellInfo.class);
		if(chanel == null){
			collection2.save(chanellInfo);
		}else{
			collection2.update("{_id : #}",chanel.getId()).with("{northEast:#, southWest:#}",chanellInfo.getNorthEast(), chanellInfo.getSouthWest());
		}
	}

}