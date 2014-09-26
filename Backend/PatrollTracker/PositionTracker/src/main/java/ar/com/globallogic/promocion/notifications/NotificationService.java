package ar.com.globallogic.promocion.notifications;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.pubnub.api.Callback;
import com.pubnub.api.Pubnub;
import com.pubnub.api.PubnubError;
import com.pubnub.api.PubnubException;

@Component
public class NotificationService {
	
	
	Pubnub pubnub;
	private String positionsChannel;
	private String notifChannel;
	
	@Autowired
	public NotificationService(
			@Value("${pubnub.subkey}") String subkey,
			@Value("${pubnub.pubkey}")String pubkey,
			@Value("${pubnub.notifChannel}")String notificationChannel,
			@Value("${pubnub.positionsChannel}")String positionsChannel
			) {
		super();
		this.pubnub = new Pubnub( pubkey,subkey);
		this.positionsChannel = positionsChannel;
		this.notifChannel = notificationChannel;
		suscribe();
	
	}
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
	
	public void publish(JSONObject message){
	
			 Callback callback = new Callback() {
			   public void successCallback(String channel, Object response) {
			     System.out.println(response.toString());
			   }
			   public void errorCallback(String channel, PubnubError error) {
			   System.out.println(error.toString());
			   }
			 };
			
			 pubnub.publish(notifChannel, message  , callback);
	}

	public void publishPosition(JSONObject message){
		
		Callback callback = new Callback() {
			public void successCallback(String channel, Object response) {
				System.out.println(response.toString());
			}
			public void errorCallback(String channel, PubnubError error) {
				System.out.println(error.toString());
			}
		};
		
		pubnub.publish(positionsChannel, message  , callback);
	}
}
