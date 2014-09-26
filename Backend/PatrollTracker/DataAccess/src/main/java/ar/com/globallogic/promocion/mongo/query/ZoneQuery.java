package ar.com.globallogic.promocion.mongo.query;

import java.util.List;

import ar.com.globallogic.promocion.mongo.model.Trackeable;
import ar.com.globallogic.promocion.mongo.model.Zone;

public interface ZoneQuery extends GenQuery< List<Trackeable>,Zone> {

	public abstract List<Trackeable> execute(Zone zone);
	public abstract Boolean ifOutOfZone(Zone zone, String id);
}
