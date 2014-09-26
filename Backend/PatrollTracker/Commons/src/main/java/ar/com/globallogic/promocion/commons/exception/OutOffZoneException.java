package ar.com.globallogic.promocion.commons.exception;

import ar.com.globallogic.promocion.commons.CommonsConstants;

@SuppressWarnings("serial")
public class OutOffZoneException extends RuntimeException {

	public String getCode() {
		return CommonsConstants.OUT_ZONE_RESPONSE_CODE;
	}

}
