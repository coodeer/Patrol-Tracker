package ar.com.globallogic.promocion.mongo.repository;

import java.util.List;

import ar.com.globallogic.promocion.mongo.model.ChanellInfo;
import ar.com.globallogic.promocion.mongo.model.Trackeable;
import ar.com.globallogic.promocion.mongo.model.Zone;

public interface TrackeableRepo {

	public Trackeable findById(String id);

	public List<Trackeable> findByZone(Zone zone);

	public Boolean isOutOfZone(String Id);

	public Boolean isOutOfZone(Trackeable trackeable);

	public List<Trackeable> findAll();

	public void save(Trackeable trackeable);

	public Trackeable update(Trackeable trackeable);

	public Trackeable delete(Trackeable trackeable);

	Boolean isOutOfZone(String id, Zone zone);

	List<Trackeable> getByTypeNear(String type, Double longitud, Double latitud);

	List<Trackeable> getByTypeInBox(String type, Double swlongitud,
			Double swlatitud, Double nelongitud, Double nelatitud);

	Trackeable getByIdInBox(String type, Double swlongitud, Double swlatitud,
			Double nelongitud, Double nelatitud);

	List<Trackeable> getByType(String type);

	void assignZone(String id, Zone zone);

	void saveChannel(ChanellInfo chanellInfo);
}
