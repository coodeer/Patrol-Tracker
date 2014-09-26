package ar.com.globallogic.promocion.mongo.query;

public interface GenQuery<T, K>{

	T execute(K parmeter);
	
	String getFindQuery(); 
	
}
