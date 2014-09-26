package ar.com.globallogic.promocion.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import ar.com.globallogic.promocion.mongo.model.Event;
import ar.com.globallogic.promocion.service.EventService;

@Controller
@RequestMapping(value="/event")
public class EventsController {

	private static Logger log = LoggerFactory.getLogger(EventsController.class);

	@Autowired
	EventService service;
	

	@RequestMapping(method = RequestMethod.GET)
	@ResponseBody
	public List<Event> getAll( ) {
		log.info("Obteniendo todos los eventos");
		return service.list();
	}
	@RequestMapping(value="{id} ",method = RequestMethod.GET)
	@ResponseBody
	public List<Event> getByTrackeable(@PathVariable("id") String id ) {
		log.info("Atendiendo peticion /event/id");
		log.debug("Atendiendo peticion /event/{}", id);
		return service.getByTrackeabkle(id);
	}
	@RequestMapping(value="/event/{id} ",method = RequestMethod.GET)
	@ResponseBody
	public Event getById(@PathVariable("id") String id ) {
		log.info("atendiendo peticion /event/event/{id}");
		log.debug("atendiendo peticion /event/event/{id}",id);
		return service.getById(id);
	}
	
}
