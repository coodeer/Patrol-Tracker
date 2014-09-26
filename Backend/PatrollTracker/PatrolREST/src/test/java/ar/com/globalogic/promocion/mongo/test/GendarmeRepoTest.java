package ar.com.globalogic.promocion.mongo.test;

import static org.junit.Assert.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import ar.com.globallogic.promocion.mongo.model.Gendarme;
import ar.com.globallogic.promocion.mongo.model.Position;
import ar.com.globallogic.promocion.mongo.model.Trackeable;
import ar.com.globallogic.promocion.mongo.model.Zone;
import ar.com.globallogic.promocion.mongo.repository.TrackeableRepository;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={"classpath:applicationContext.xml"})
public class GendarmeRepoTest {

	@Autowired
	TrackeableRepository gendarmeRepository;
	@Test
	public void test() throws IOException {

		//Gendarme gendarme = gendarmeRepository.findById("ObjectId('5414a4675450af8456237d5e')");
		List<Trackeable> gendarmes = gendarmeRepository.findAll();
			assertNotNull(gendarmes);
		for (Trackeable gendarme : gendarmes) {
			System.out.println(gendarme.getId());
		}	
		
		Gendarme g1 = (Gendarme) gendarmeRepository.findById("5417672a54502d734a181aab");
		
		System.out.println(g1.getId());
		
	}

	@Test
	public void testZone() throws IOException {
		
		Zone zone = new Zone();
		zone.setIsCritic(false);
		List<Position> points = new ArrayList<Position>();
		Position p = new Position();
		Position p2 = new Position();
		//bottom  left
		p.setLongitude(-66.89117792663569);
		p.setLatitude(-29.441704091449594);
		//top right
		p2.setLongitude(-66.778019);
		p2.setLatitude(-29.390423);
		points.add(p);
		points.add(p2);
		
		zone.setPoints(points);
		zone.setRadius(50D);
		zone.setShape("BOX");
		
		List<Trackeable> findByZone = gendarmeRepository.findByZone(zone);
		
		for (Trackeable trackeable : findByZone) {
			System.err.println(trackeable.getIsVehicle());
		}
	}

}
