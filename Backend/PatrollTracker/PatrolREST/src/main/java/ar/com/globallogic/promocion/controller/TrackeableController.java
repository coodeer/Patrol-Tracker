package ar.com.globallogic.promocion.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import ar.com.globallogic.promocion.commons.CommonsConstants;
import ar.com.globallogic.promocion.mongo.model.ChanellInfo;
import ar.com.globallogic.promocion.mongo.model.Trackeable;
import ar.com.globallogic.promocion.service.TrackeableService;

@Controller
@RequestMapping(value = "/trackeable")
public class TrackeableController {
	private static Logger log = LoggerFactory
			.getLogger(TrackeableController.class);

	@Autowired
	TrackeableService service;

	@RequestMapping(method = RequestMethod.POST)
	@ResponseBody
	public String create(@RequestBody Trackeable trackeable) {
		log.info("creando un nuevo Trackeable: {}", trackeable.getType());
		service.save(trackeable);
		return "ok";
	}

	@RequestMapping(method = RequestMethod.GET)
	@ResponseBody
	public List<Trackeable> getAll() {
		log.info("obteniendo todos los trackeables");
		return service.findAl();
	}

	@RequestMapping(value = "/type/{type}", method = RequestMethod.GET)
	@ResponseBody
	public List<Trackeable> getByType(@PathVariable("type") String type) {
		log.info("obteniendo todos los trackeables de tipo: {}", type);
		return service.getByType(type);
	}

	@RequestMapping(value = "/channel", method = RequestMethod.PUT)
	@ResponseBody
	public String setChanelPosition(@RequestBody ChanellInfo chanellInfo,
			HttpServletRequest request) {
		HttpServletRequestWrapper wrapper = new HttpServletRequestWrapper(
				request);

		log.info("registrando un viewport");
		String auth = wrapper.getHeader(CommonsConstants.AUTHORIZATION);
		return service.saveChanell(chanellInfo, auth);
	}

	@RequestMapping(value = "/type/{type}/{location}", method = RequestMethod.GET)
	@ResponseBody
	public List<Trackeable> getByTypeNear(@PathVariable("type") String type,
			@PathVariable("location") String location) {
		log.info("Obteniendo trackeables cercanos a una posicion");
		return service.getByTypeNear(type, location);
	}

	@RequestMapping(value = "/type/{type}/box/{sw:.+}/{ne:.+}", method = RequestMethod.GET)
	@ResponseBody
	public List<Trackeable> getByTypeInBox(@PathVariable("type") String type,
			@PathVariable("sw") String sw, @PathVariable("ne") String ne) {
		log.info("Obteniendo trackeables de tipo {} cercanos a una posicion", type);

		return service.getByTypeInBox(type, sw, ne);
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
	@ResponseBody
	public Trackeable getById(@PathVariable("id") String id) {
		log.info("Obteniendo trackeables con id: {} ", id);

		return service.getById(id);
	}

	@RequestMapping(value = "/{id}/zone/{idZone}", method = RequestMethod.PUT, consumes = "application/json")
	@ResponseBody
	public Trackeable assignZone(@PathVariable("idZone") String idZone,
			@PathVariable("id") String id) {
		log.info("Asignando la zona con id{} al trackeable con id: {} ", idZone, id);

		return service.assignZone(idZone, id);
	}

	@RequestMapping(value = "/{southWest:.+}/{northEast:.+}", method = RequestMethod.GET)
	@ResponseBody
	public List<Trackeable> getBySquare(
			@PathVariable("southWest") String southWest,
			@PathVariable("northEast") String northEast) {
		log.info("Obteniendo trackeables en el viewport: southWest: {} northEast {} ", southWest, northEast);
		return service.findInBox(southWest, northEast);
	}
}
