package ar.com.globalogic.promocion.mongo.test;

import static org.junit.Assert.assertNotNull;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.jongo.Jongo;
import org.jongo.MongoCollection;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import ar.com.globallogic.promocion.commons.CommonsConstants;
import ar.com.globallogic.promocion.mongo.model.Gendarme;
import ar.com.globallogic.promocion.mongo.model.Position;
import ar.com.globallogic.promocion.mongo.model.Trackeable;
import ar.com.globallogic.promocion.mongo.model.Zone;
import ar.com.globallogic.promocion.mongo.repository.TrackeableRepository;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={"classpath:patrol-test-applicationContext.xml"})
public class GendarmeRepoTest {

	@Autowired
	TrackeableRepository trackeableREpository;
	
	@Autowired
	Jongo jongo;
	
	@Test
	public void test() throws IOException {

		
		Trackeable trackeable = new Gendarme();
		
		trackeable.setName("pepito");
		trackeable.setCurrentPosition(new Position(-34.3216,-57.1654));
		
		trackeableREpository.save(trackeable);
		
		List<Trackeable> trackeables = trackeableREpository.findAll();
		assertNotNull(trackeables);
		String id = trackeables.get(0).getId();
		
		Gendarme g1 = (Gendarme) trackeableREpository.findById(id);
		
		assertNotNull(g1);
		
		MongoCollection collection = jongo.getCollection(CommonsConstants.TRACKEABLE_COLLECTION_TEST);
		collection.drop();
		
		System.out.println(g1.getId());
		
	}

	@Test
	public void testZone() throws IOException {
		
		
Trackeable trackeable = new Gendarme();
		
		trackeable.setName("pepito");
		trackeable.setCurrentPosition(new Position(-34.3216,-57.1654));
		
		trackeableREpository.save(trackeable);
		
		
		Zone zone = new Zone();
		zone.setIsCritic(false);
		List<Position> points = new ArrayList<Position>();
		Position p = new Position();
		p.setLongitude(-57.1654);
		p.setLatitude(-34.3216);
		points.add(p);
		
		zone.setRadius(500D);
		zone.setPoints(points);
		zone.setRadius(50D);
		zone.setShape(CommonsConstants.CIRCLE);
		
		List<Trackeable> findByZone = trackeableREpository.findByZone(zone);
		
		assertNotNull(findByZone.get(0));
		
		MongoCollection collection = jongo.getCollection(CommonsConstants.TRACKEABLE_COLLECTION_TEST);
		collection.drop();
	}

}
