package ar.com.globallogic.promocion.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import ar.com.globallogic.promocion.mongo.model.Zone;
import ar.com.globallogic.promocion.service.ZoneService;
/**
 * 
 * @author maxi
 *
 */
@Controller
@RequestMapping(value="/zone")
public class ZoneController {
	private static Logger log = LoggerFactory
			.getLogger(Zone.class);
	@Autowired
	ZoneService service;
	/**
	 * crea una zona
	 * @param zone
	 * @return
	 */
	@RequestMapping(method = RequestMethod.POST)
	@ResponseBody
	public Zone create(@RequestBody Zone zone) {
		log.info("creando una nueva zona");
		return service.save(zone);
	}
	/**
	 * devuelve todas las zonas
	 * @return
	 */
	@RequestMapping(method = RequestMethod.GET)
	@ResponseBody
	public List<Zone> getAll( ) {
		log.info("Obteniendo todas las zonas");
		return service.getAll();
	}
	
}
