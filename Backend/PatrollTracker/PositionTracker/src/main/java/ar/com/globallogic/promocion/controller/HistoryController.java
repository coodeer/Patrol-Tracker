package ar.com.globallogic.promocion.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import ar.com.globallogic.promocion.commons.CommonsConstants;
import ar.com.globallogic.promocion.commons.Response;
import ar.com.globallogic.promocion.mongo.model.Position;
import ar.com.globallogic.promocion.mongo.model.PositionRecord;
import ar.com.globallogic.promocion.service.HistoryService;

/**
 * 
 * @author maxi
 * 
 */
@Controller
@RequestMapping(value = "/history")
public class HistoryController {

	@Autowired
	HistoryService service;

	/**
	 * registra la posicion de un trackeable
	 * 
	 * @param id
	 * @param record
	 * @return
	 */
	@RequestMapping(value = "/{id}", method = RequestMethod.POST)
	@ResponseBody
	public Response create(@PathVariable("id") String id,
			@RequestBody Position record) {

		service.addRecord(record, id);
		Response response = new Response();
		response.setCode(CommonsConstants.OK_CODE);
		response.setMessage(CommonsConstants.OK_MESSAGE);

		return response;
	}

	/**
	 * devuelve el historial de un trackeable con id dado
	 * @param id
	 * @return
	 */
	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
	@ResponseBody
	public List<PositionRecord> getHistory(@PathVariable("id") String id) {
		return service.list(id);

	}

}
