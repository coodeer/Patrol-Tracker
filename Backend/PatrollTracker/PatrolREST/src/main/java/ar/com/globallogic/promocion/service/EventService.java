package ar.com.globallogic.promocion.service;

import java.util.Iterator;
import java.util.List;

import org.apache.commons.collections.IteratorUtils;
import org.jongo.Jongo;
import org.jongo.MongoCollection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import ar.com.globallogic.promocion.commons.CommonsConstants;
import ar.com.globallogic.promocion.controller.TrackeableController;
import ar.com.globallogic.promocion.mongo.model.Event;

@Component
public class EventService {

	@Autowired
	Jongo jongo;
	private static Logger log = LoggerFactory
			.getLogger(EventService.class);
	@SuppressWarnings("unchecked")
	public List<Event> list() {
		log.info("obteniendo todas los eventos");
		MongoCollection collection = jongo
				.getCollection(CommonsConstants.EVENT_COLLECTION);
		Iterator<Event> iterator = collection.find().as(Event.class).iterator();
		return IteratorUtils.toList(iterator);
	}

	public List<Event> getByTrackeabkle(String id) {
		
		log.info("obteneindo los eventos para el  trackeable: {}",id);
		MongoCollection collection = jongo
				.getCollection(CommonsConstants.EVENT_COLLECTION);
		Iterator<Event> iterator = collection.find("{trackeable_id: #}", id)
				.as(Event.class).iterator();
		return IteratorUtils.toList(iterator);
	}

	public Event getById(String id) {
		log.info("obteneindo el evento con id {}", id);
		MongoCollection collection = jongo
				.getCollection(CommonsConstants.EVENT_COLLECTION);
		Event event= collection.findOne("{_id: #}", id).as(Event.class);
		return event;
	}
}
