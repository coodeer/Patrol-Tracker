package ar.com.globallogic.promocion.autocarga;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;
import java.util.concurrent.CountDownLatch;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;

import ar.com.globallogic.promocion.mongo.model.Position;
import ar.com.globallogic.promocion.mongo.model.Trackeable;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

public class CargaAutomatica {

	
	@SuppressWarnings("unchecked")
	public static void main(String [] args) throws ClientProtocolException, IOException, InterruptedException	 {

		DefaultHttpClient client = new DefaultHttpClient();


		ObjectMapper mapper = new ObjectMapper();

		HttpGet requestTrackeable = new HttpGet(
				"http://promociongl.herokuapp.com/trackeable");

		String result = null;
		String output = null;
		HttpResponse response = client.execute(requestTrackeable);
		BufferedReader br = new BufferedReader(new InputStreamReader(
				(response.getEntity().getContent())));
		while ((output = br.readLine()) != null) {
			result = output;
		}
		requestTrackeable.releaseConnection();
		requestTrackeable.reset();

		List<Trackeable> a = (List<Trackeable>) mapper.readValue(result,
				new TypeReference<List<Trackeable>>() {
				});
		CountDownLatch countDownLatch = new CountDownLatch(a.size());
		for (Trackeable trackeable : a) {
			Hilo hilo = new Hilo(trackeable);
			hilo.start();
		}

		countDownLatch.await();
	}


}
 class Hilo extends Thread {
	
	private Trackeable trackeable;
	
	public Hilo(Trackeable trackeable) {
		super();
		this.trackeable = trackeable;
	}
	
	@Override
	public void run() {
		
		ObjectMapper mapper = new ObjectMapper();
		DefaultHttpClient client = new DefaultHttpClient();
		while (true) {
			HttpPost informPosition = new HttpPost(
					"http://positiontracker.herokuapp.com/history/"
					// "http://localhost:8080/PositionTracker/history/"
					+ trackeable.getId());
			
			boolean error = false;
			
			if (!error) {
				
				Double sumar = Math.random() * 10;
				boolean restar = (Math.random() * 10) > 5;
				
				sumar = sumar / 100000;
				
				if (restar) {
					sumar *= -1;
				}
				
				Double latitude = trackeable.getCurrentPosition()
						.getLatitude();
				Double longitude = trackeable.getCurrentPosition()
						.getLongitude();
				
				Position position = new Position(latitude + sumar,
						longitude + sumar);
				
				trackeable.setCurrentPosition(position);
				
				try {
					String positionString = mapper
							.writeValueAsString(position);
					informPosition.setEntity(new StringEntity(
							positionString, ContentType.APPLICATION_JSON));
					client.execute(informPosition);
				} catch (ClientProtocolException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				informPosition.releaseConnection();
				informPosition.reset();
			}
			
		}
		
	}
	
}
