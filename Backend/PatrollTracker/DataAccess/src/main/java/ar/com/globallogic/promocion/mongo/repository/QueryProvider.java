package ar.com.globallogic.promocion.mongo.repository;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import ar.com.globallogic.promocion.mongo.model.Zone;
import ar.com.globallogic.promocion.mongo.query.ZoneQuery;
@Component
public class QueryProvider {

	@Autowired
	private Map<String, ZoneQuery> zoneMap;

	public QueryProvider(){
		super();
	}
	
	public ZoneQuery trackeaByZone(Zone zone) {
		return getZoneMap().get(zone.getQueryName());
	}

	public Map<String, ZoneQuery> getZoneMap() {
		return zoneMap;
	}

	public void setZoneMap(Map<String, ZoneQuery> zoneMap) {
		this.zoneMap = zoneMap;
	}


}
