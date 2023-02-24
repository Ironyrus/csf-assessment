package vttp2022.csf.assessment.server.repositories;

import java.util.List;
import java.util.Optional;

import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import vttp2022.csf.assessment.server.models.Comment;
import vttp2022.csf.assessment.server.models.LatLng;
import vttp2022.csf.assessment.server.models.Restaurant;

@Repository
public class RestaurantRepository {

	@Autowired
	MongoTemplate mongoTemplate;

	// TODO Task 2
	// Use this method to retrive a list of cuisines from the restaurant collection
	// You can add any parameters (if any) and the return type 
	// DO NOT CHNAGE THE METHOD'S NAME
	// Write the Mongo native query above for this method
	// *** Mongo Query: ***
	// db.restaurants.find({})
	public List<Document> getCuisines() {
		// Implmementation in here
		Criteria c = Criteria.where("cuisine").exists(true);
		Query q = Query.query(c);
		List<Document> results = mongoTemplate.find(q, Document.class, "restaurants");
		return results;
	}

	// TODO Task 3
	// Use this method to retrive a all restaurants for a particular cuisine
	// You can add any parameters (if any) and the return type 
	// DO NOT CHNAGE THE METHOD'S NAME
	// Write the Mongo native query above for this method
	
	// db.restaurants.find({
	// 	cuisine: /America.*/
	// })
	public List<Document> getRestaurantsByCuisine(String cuisine) {
		// Implmementation in here
		Criteria c = Criteria.where("cuisine").regex(cuisine);
		Query q = Query.query(c);
		List<Document> results = mongoTemplate.find(q, Document.class, "restaurants");
		return results;
	}

	// TODO Task 4
	// Use this method to find a specific restaurant
	// You can add any parameters (if any) 
	// DO NOT CHNAGE THE METHOD'S NAME OR THE RETURN TYPE
	// Write the Mongo native query above for this method
	//  
	// db.restaurants.find({
	// 	name: 'Ajisen Ramen'
	// })
	public Optional<Restaurant> getRestaurant(String name) {
		// Implmementation in here
		Criteria c = Criteria.where("name").is(name);
		Query q = Query.query(c);
		List<Document> results = mongoTemplate.find(q, Document.class, "restaurants");
		Optional<Restaurant> out = Optional.empty();
		Restaurant restaurant = null;
		for (Document document : results) {
			restaurant = new Restaurant();
			restaurant.setName(document.getString("name"));
			restaurant.setRestaurantId(document.getString("restaurant_id"));
			restaurant.setCuisine(document.getString("cuisine"));

			Document address = document.get("address", Document.class);
			restaurant.setAddress(address.getString("building") + ", " + address.getString("street") + ", " + address.getString("zipcode") + ", " + document.getString("borough"));
			List<Float> coords = address.get("coord", List.class);
			float lat = Float.parseFloat(coords.get(1) + "");
			float longitude = Float.parseFloat(coords.get(0)+"");
			LatLng coords2 = new LatLng();
			coords2.setLatitude(lat);
			coords2.setLongitude(longitude);
			restaurant.setCoordinates(coords2);

			out = Optional.of(restaurant);
		}
		System.out.println(restaurant.getName());
		System.out.println(restaurant.getRestaurantId());
		System.out.println(restaurant.getCuisine());
		System.out.println(restaurant.getAddress());
		System.out.println(restaurant.getCoordinates().getLatitude());

		return out;
	}

	// TODO Task 5
	// Use this method to insert a comment into the restaurant database
	// DO NOT CHNAGE THE METHOD'S NAME OR THE RETURN TYPE
	// Write the Mongo native query above for this method
	//  
	// public void addComment(Comment comment) {
		// Implmementation in here
		
	// }
	
	// You may add other methods to this class

}
