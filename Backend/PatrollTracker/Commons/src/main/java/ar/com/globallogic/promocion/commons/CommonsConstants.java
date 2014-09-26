package ar.com.globallogic.promocion.commons;

public class CommonsConstants {

	public static final String TRACKEABLE_COLLECTION = "trackeable";
	public static final String HISTORY_COLLECTION = "history";
	public static final String ZONE_COLLECTION = "zone";
	public static String GENDARME_CLASS="ar.com.globallogic.promocion.mongo.model.Gendarme";
	public static String VEHICLE_CLASS="ar.com.globallogic.promocion.mongo.model.Vehiculo";
	
	public static final String OK_CODE = "200";
	public static final String OK_MESSAGE = "ok";
	
	public static final int VEHICLE_OUT_OF_ZONE_CODE = 1001;
	public static final String VEHICLE_OUT_OF_ZONE_MESSAGE = "Este vehiculo se encuentra fuera de su zona asignada";

	public static final int VEHICLE_BACK_TO_ZONE_CODE = 1002;
	public static final String VEHICLE_BACK_TO_ZONE_MESSAGE = "Este vehiculo volvio a su zona asignada";
	
	public static final int GENDARME_OUT_OF_ZONE_CODE = 2001;
	public static final String GENDARME_OUT_OF_ZONE_MESSAGE = "EL GENDARME ABANDONO SU PUESTO";

	public static final int GENDARME_BACK_TO_ZONE_CODE = 2002;
	public static final String GENDARME_BACK_TO_ZONE_MESSAGE = "EL GENDARME RETOMO SU PUESTO";
	public static final String EVENT_COLLECTION = "event";

	public static final String OUT_ZONE_RESPONSE_CODE = "1002";
	public static final String BACK_ZONE_RESPONSE_CODE = "1200";
	public static final String CHANELL_COLLECTION = "chanel";
	public static final String AUTHORIZATION = "Authorization";
	
}
