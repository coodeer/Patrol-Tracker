package ar.com.globalogic.promocion.controller.test;

import static org.junit.Assert.*;

import org.junit.Test;

import com.pubnub.api.Callback;
import com.pubnub.api.Pubnub;
import com.pubnub.api.PubnubError;
import com.pubnub.api.PubnubException;

public class DatapushTest {
	Pubnub pubnub = new Pubnub( "pub-c-9240b235-1cbf-4c7f-b5b6-70227d80e339","sub-c-fe191c08-4426-11e4-b78c-02ee2ddab7fe");
	private void suscribe(){
		try {
			   pubnub.subscribe("patrol", new Callback() {
			 
			       @Override
			       public void connectCallback(String channel, Object message) {
			           System.out.println("SUBSCRIBE : CONNECT on channel:" + channel
			                      + " : " + message.getClass() + " : "
			                      + message.toString());
			       }
			 
			       @Override
			       public void disconnectCallback(String channel, Object message) {
			           System.out.println("SUBSCRIBE : DISCONNECT on channel:" + channel
			                      + " : " + message.getClass() + " : "
			                      + message.toString());
			       }
			 
			       public void reconnectCallback(String channel, Object message) {
			           System.out.println("SUBSCRIBE : RECONNECT on channel:" + channel
			                      + " : " + message.getClass() + " : "
			                      + message.toString());
			       }
			 
			       @Override
			       public void successCallback(String channel, Object message) {
			           System.out.println("SUBSCRIBE : " + channel + " : "
			                      + message.getClass() + " : " + message.toString());
			       }
			 
			       @Override
			       public void errorCallback(String channel, PubnubError error) {
			           System.out.println("SUBSCRIBE : ERROR on channel " + channel
			                      + " : " + error.toString());
			       }
			     }
			   );
			 } catch (PubnubException e) {
			   System.out.println(e.toString());
			 }
	}
	
	private void publish(String message){
	
			 Callback callback = new Callback() {
			   public void successCallback(String channel, Object response) {
			     System.out.println(response.toString());
			   }
			   public void errorCallback(String channel, PubnubError error) {
			   System.out.println(error.toString());
			   }
			 };
			 pubnub.publish("patrol", message , callback);
	}
	@Test
	public void test() {
		suscribe();
		publish("holamundo");
	}

}
