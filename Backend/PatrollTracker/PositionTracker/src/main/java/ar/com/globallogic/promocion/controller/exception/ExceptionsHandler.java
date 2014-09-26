package ar.com.globallogic.promocion.controller.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import ar.com.globallogic.promocion.commons.Response;
import ar.com.globallogic.promocion.commons.exception.OutOffZoneException;

@ControllerAdvice
public class ExceptionsHandler {	

	
	/**
	 * Handle the exception, and sent the correct message
	 * 
	 * @param exception - {@link OutOffZoneException}
	 */
	@ExceptionHandler(OutOffZoneException.class)
	@ResponseStatus(value = HttpStatus.CONFLICT)
	@ResponseBody
	public Response handle(final OutOffZoneException ex){
		
		Response response = new Response();
		response.setCode(ex.getCode());
		response.setMessage("Se encuentra fuera de de su zona asignada");
		return response;
	}
	
	
	
}