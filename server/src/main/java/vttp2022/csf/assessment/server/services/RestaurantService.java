package vttp2022.csf.assessment.server.services;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import javax.print.attribute.standard.Media;

import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;

import com.amazonaws.Request;
import com.amazonaws.Response;

import vttp2022.csf.assessment.server.models.Comment;
import vttp2022.csf.assessment.server.models.Restaurant;
import vttp2022.csf.assessment.server.repositories.MapCache;
import vttp2022.csf.assessment.server.repositories.RestaurantRepository;

@Service
public class RestaurantService {

	@Autowired
	private RestaurantRepository restaurantRepo;

	@Autowired
	private MapCache mapCache;

	// TODO Task 2 
	// Use the following method to get a list of cuisines 
	// You can add any parameters (if any) and the return type 
	// DO NOT CHNAGE THE METHOD'S NAME
	public List<String> getCuisines() {
		// Implmementation in here
		List<Document> results = restaurantRepo.getCuisines();
		Set<String> out = new HashSet<String>();
		for (Document doc : results) {
			String temp = "";
			temp = (doc.getString("cuisine").replace("/", "_")).trim();
			out.add(temp);
		}
		List<String> out2 = new ArrayList(out);
		Collections.sort(out2);
		
		return out2;
	}

	// TODO Task 3 
	// Use the following method to get a list of restaurants by cuisine
	// You can add any parameters (if any) and the return type 
	// DO NOT CHNAGE THE METHOD'S NAME
	public List<String> getRestaurantsByCuisine(String cuisine) {
		// Implmementation in here
		List<Document> results = restaurantRepo.getRestaurantsByCuisine(cuisine);
		Set<String> out = new HashSet<String>();
		for (Document doc : results) {
			
			out.add(doc.getString("name"));
		}
		List<String> out2 = new ArrayList(out);
		Collections.sort(out2);
		return out2;
	}	

	// TODO Task 4
	// Use this method to find a specific restaurant
	// You can add any parameters (if any) 
	// DO NOT CHNAGE THE METHOD'S NAME OR THE RETURN TYPE
	public Restaurant getRestaurant(String name) {
		restaurantRepo.getRestaurant(name);
		// Implmementation in here
		Optional<Restaurant> out = restaurantRepo.getRestaurant(name);
		Restaurant restaurant = out.get();
		// RequestEntity<String> to make external http call
		String url = "http://map.chuklee.com" + "/map?lat=" + restaurant.getCoordinates().getLatitude() + "&lng=" + restaurant.getCoordinates().getLongitude();
		System.out.println(url);
		RequestEntity<Void> request = RequestEntity.get(url)
			.accept()
			.build();
		RestTemplate template = new RestTemplate();
		ResponseEntity<byte[]> response = null;

		String payload = "test";

		byte[] image = null;

		try {
			response = template.exchange(request, byte[].class);
			// payload = response.getBody();
		} catch (Exception e) {
			// TODO: handle exception
		}

		// Get Map and upload to S3
		if(response != null){
			try {
				// image = mapCache.getMap(response, restaurant.getName());
				String urlOut = mapCache.getMap(response, restaurant.getName());
				restaurant.setMapURL(url);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		// return ResponseEntity.ok().body(image);
		return restaurant;
	}

	// TODO Task 5
	// Use this method to insert a comment into the restaurant database
	// DO NOT CHNAGE THE METHOD'S NAME OR THE RETURN TYPE
	// public void addComment(Comment comment) {
		// Implmementation in here
		
	// }
	//
	// You may add other methods to this class
}
