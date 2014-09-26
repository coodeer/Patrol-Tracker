package ar.com.globalogic.promocion.mongo.test;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.jongo.Jongo;
import org.jongo.MongoCollection;
import org.jongo.MongoCursor;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import ar.com.globallogic.promocion.mongo.model.Trackeable;
import ar.com.globallogic.promocion.mongo.repository.TrackeableRepository;
import ar.com.globallogic.promocion.service.TrackeableService;

import com.fasterxml.jackson.databind.ObjectMapper;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={"classpath:applicationContext.xml"})
public class CargaTest {

	@Autowired
	TrackeableService gendarmeService;

	@Autowired
	TrackeableRepository gendarmeRepository;
	
	@Autowired
	Jongo jongo;
	
	
	
	@Test
	public void test() throws  IOException {
		
		ObjectMapper mapper = new ObjectMapper();
		
		TrackeablesColl coll= mapper.readValue(new File("/opt/test/archivos/gendarme.json"), TrackeablesColl.class);

		List<Trackeable> gendarmes = coll.getTrackeables();
		
		
		MongoCollection trackeables = jongo.getCollection("trackeable");

        MongoCursor<Trackeable> mongoCursor = trackeables.find(
		"{currentPosition: {$geoWithin: { $centerSphere : [[ -58.429082,-34.586655 ],"+ 10/6371 +"]} }  }"			
				).as(Trackeable.class);

		
		trackeables.ensureIndex("{'currentPosition':'2dsphere'}");
		for (Trackeable trackeable : gendarmes) {
			gendarmeRepository.save(trackeable);
		}
		
		for (Trackeable gendarme : mongoCursor) {
			System.out.println(gendarme.getIsVehicle());
		}
	}

}
