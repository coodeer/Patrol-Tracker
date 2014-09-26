package ar.com.globallogic.promocion.service;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import ar.com.globallogic.promocion.exception.UntrackeableException;
import ar.com.globallogic.promocion.mongo.model.ChanellInfo;
import ar.com.globallogic.promocion.mongo.model.Position;
import ar.com.globallogic.promocion.mongo.model.Trackeable;
import ar.com.globallogic.promocion.mongo.model.Zone;
import ar.com.globallogic.promocion.mongo.query.ZoneQuery;
import ar.com.globallogic.promocion.mongo.repository.QueryProvider;
import ar.com.globallogic.promocion.mongo.repository.TrackeableRepo;
import ar.com.globallogic.promocion.mongo.repository.zoneRepo;

@Component
public class TrackeableService {
	private static Logger log = LoggerFactory
			.getLogger(TrackeableService.class);
	@Autowired
	TrackeableRepo trackeableRepository;

	@Autowired
	@Qualifier("zoneRepository")
	zoneRepo zoneRepository;

	@Autowired
	QueryProvider queryProvider;

	public Trackeable getById(String id) {
		return trackeableRepository.findById(id);
	}

	public List<Trackeable> findInBox(String southWest, String northEast) {

		log.info("Parseando los parametros recibidos");
		String[] bottomLeft = southWest.split(",");
		String[] topRight = northEast.split(",");

		Position bottomLeftPosition = new Position();
		Position topRightPosition = new Position();

		bottomLeftPosition.setLongitude(Double.valueOf(bottomLeft[0]));
		bottomLeftPosition.setLatitude(Double.valueOf(bottomLeft[1]));

		topRightPosition.setLongitude(Double.valueOf(topRight[0]));
		topRightPosition.setLatitude(Double.valueOf(topRight[01]));

		List<Position> posiciones = new ArrayList<Position>();
		posiciones.add(bottomLeftPosition);
		posiciones.add(topRightPosition);
		Zone zone = new Zone();
		zone.setShape("BOX");
		zone.setPoints(posiciones);

		ZoneQuery box = queryProvider.trackeaByZone(zone);
		return box.execute(zone);
	}

	public void save(Trackeable trackeable) {
		trackeableRepository.save(trackeable);
	}

	public Trackeable assignZone(String idZone, String id) {

		Trackeable trackeable = trackeableRepository.findById(id);
		Zone zone = zoneRepository.findById(idZone);
		if (trackeable == null || zone == null) {
			throw new UntrackeableException();
		}

		trackeableRepository.assignZone(id, zone);

		return null;
	}

	public List<Trackeable> findAl() {
			return trackeableRepository.findAll();
		
	}

	public List<Trackeable> getByType(String type) {
		return trackeableRepository.getByType(type);
	}

	public List<Trackeable> getByTypeNear(String type, String location) {
		String[] split = location.split(",");
		Double longitud = Double.valueOf(split[0]);
		Double latitud = Double.valueOf(split[1]);
		return trackeableRepository.getByTypeNear(type, longitud, latitud);
	}

	public List<Trackeable> getByTypeInBox(String type, String sw, String ne) {
		// TODO Auto-generated method stub
		String[] swsplit = sw.split(",");
		Double swlongitud = Double.valueOf(swsplit[0]);
		Double swlatitud = Double.valueOf(swsplit[1]);
		
		String[] nesplit = ne.split(",");
		Double nelongitud = Double.valueOf(nesplit[0]);
		Double nelatitud = Double.valueOf(nesplit[1]);
		
		return trackeableRepository.getByTypeInBox(type, swlongitud, swlatitud, nelongitud, nelatitud);
	}

	public String saveChanell(ChanellInfo chanellInfo, String auth) {
		
		trackeableRepository.saveChannel(chanellInfo);
		return "ok";
	}



}
