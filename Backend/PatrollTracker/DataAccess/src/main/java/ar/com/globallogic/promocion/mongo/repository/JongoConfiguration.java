package ar.com.globallogic.promocion.mongo.repository;

import java.net.UnknownHostException;

import org.jongo.Jongo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import ar.com.globallogic.promocion.commons.CommonsConstants;

import com.mongodb.DB;
import com.mongodb.MongoURI;

@Component
public class JongoConfiguration {

	@Autowired
	MongoURI mongoURI;
	
	Jongo jon;
	
	@Bean(name="jongo")
	public Jongo jongo() throws UnknownHostException{
		if(jon == null){
			DB db = mongoURI.connectDB();
			boolean authenticate = db.authenticate(mongoURI.getUsername(), mongoURI.getPassword());
			jon = new Jongo(db);
			jon.getCollection(CommonsConstants.TRACKEABLE_COLLECTION).ensureIndex("{'currentPosition':'2dsphere'}");
		}
		return jon;
	}
}
