package ar.com.globallogic.promocion.autocarga;

import java.util.Date;

import org.jongo.Jongo;
import org.jongo.MongoCollection;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import ar.com.globallogic.promocion.commons.CommonsConstants;
import ar.com.globallogic.promocion.mongo.model.Position;
import ar.com.globallogic.promocion.mongo.model.PositionRecord;
import ar.com.globallogic.promocion.mongo.model.Trackeable;
import ar.com.globallogic.promocion.service.HistoryService;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:applicationContext.xml")
public class CargaAutomatica {

	@Autowired
	Jongo jongo;
	
	@Autowired
	HistoryService service;

	@Test
	public void test() {

		MongoCollection collection = jongo
				.getCollection(CommonsConstants.TRACKEABLE_COLLECTION);

		String id = "54221fc5e4b0827bb8298c7d";
		Trackeable trackeable = collection.findOne("{_id : #}",
				id).as(Trackeable.class);

		Double latitude = null;
		Double longitude= null;
		while (true) {
			Double sumar = Math.random() * 10;
			boolean restar= (Math.random() * 10)>5;
			
			sumar = sumar/100000;

			if(restar){
				sumar *=-1;
			}
					
			latitude =	trackeable.getCurrentPosition().getLatitude();
			longitude =	trackeable.getCurrentPosition().getLongitude();
			
			longitude+=sumar;
			latitude+=sumar;
			Position record = new Position(latitude, longitude);
			
			service.addRecord(record, id);
		}

	}

}
