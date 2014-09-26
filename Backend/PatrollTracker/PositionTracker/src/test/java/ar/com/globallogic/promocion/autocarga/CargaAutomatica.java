package ar.com.globallogic.promocion.autocarga;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.jongo.Jongo;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import ar.com.globallogic.promocion.mongo.model.Position;
import ar.com.globallogic.promocion.mongo.model.Trackeable;
import ar.com.globallogic.promocion.service.HistoryService;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:applicationContext.xml")
public class CargaAutomatica {

	@Autowired
	Jongo jongo;

	@Autowired
	HistoryService service;

	/*
	 * Este Test sirve para hacer la simulacion correrlo solo para simular
	 */
	@Test
	@SuppressWarnings("unchecked")
	public void test() throws JsonParseException, JsonMappingException, IOException{

		DefaultHttpClient client = new DefaultHttpClient();

		String id = "54221fc5e4b0827bb8298c7d";

		ObjectMapper mapper = new ObjectMapper();

		HttpGet requestTrackeable = new HttpGet(
				"http://promociongl.herokuapp.com/trackeable");

		HttpGet request = new HttpGet(
				"http://positiontracker.herokuapp.com/history/" + id);

		String result = null;
		String output = null;
		HttpResponse response = client.execute(requestTrackeable);
		BufferedReader br = new BufferedReader(new InputStreamReader(
				(response.getEntity().getContent())));
		while ((output = br.readLine()) != null) {
			result = output;
		}

		List<Trackeable> a = (List<Trackeable>) mapper.readValue(result,
				new TypeReference<List<Trackeable>>() {
				});
		Double latitude = null;
		Double longitude = null;

		while (true) {
			for (Trackeable trackeable : a) {

				client = new DefaultHttpClient();
				HttpGet getTRackeable = new HttpGet(
						"http://promociongl.herokuapp.com/trackeable/"
								+ trackeable.getId());
				HttpPost informPosition = new HttpPost(
						"http://positiontracker.herokuapp.com/history/"
								+ trackeable.getId());

				try {
					response = client.execute(getTRackeable);
				} catch (ClientProtocolException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}

				boolean error = false;

				Trackeable readValue = null;
				try{
				
				br = new BufferedReader(new InputStreamReader(
						(response.getEntity().getContent())));
				while ((output = br.readLine()) != null) {
					result = output;
				}
					readValue = mapper.readValue(result,	Trackeable.class);
				}catch(IOException e){
					 error = true;
				}

				if(!error){
					
					Double sumar = Math.random() * 10;
					boolean restar = (Math.random() * 10) > 5;
					
					sumar = sumar / 100000;
					
					if (restar) {
						sumar *= -1;
					}
					
					latitude = readValue.getCurrentPosition().getLatitude();
					longitude = readValue.getCurrentPosition().getLongitude();
					
					Position position = new Position(latitude + sumar, longitude
							+ sumar);
					
					
					
					try {
						String positionString = mapper.writeValueAsString(position);
						informPosition.setEntity(new StringEntity(positionString,
								ContentType.APPLICATION_JSON));
						client.execute(informPosition);
					} catch (ClientProtocolException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					getTRackeable.releaseConnection();
					informPosition.releaseConnection();
					request.releaseConnection();
				}

			}
		}

	}

}
