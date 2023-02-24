package vttp2022.csf.assessment.server.controllers;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import jakarta.json.Json;
import jakarta.json.JsonArray;
import jakarta.json.JsonArrayBuilder;
import jakarta.json.JsonObject;
import jakarta.json.JsonObjectBuilder;
import vttp2022.csf.assessment.server.models.Comment;
import vttp2022.csf.assessment.server.models.Restaurant;
import vttp2022.csf.assessment.server.services.RestaurantService;

@Controller
public class RestaurantController {
    
    @Autowired
    RestaurantService service;

    @GetMapping(path="/api/cuisines", produces = MediaType.APPLICATION_JSON_VALUE)
    @CrossOrigin(origins="*")
    public ResponseEntity<String> getCuisines() {
        List<String> results = service.getCuisines();
        JsonArrayBuilder arrBuilder = Json.createArrayBuilder();
        
        for (String item : results) {
            JsonObjectBuilder objBuilder = Json.createObjectBuilder().add("cuisine", item);
            JsonObject out = objBuilder.build();
            arrBuilder.add(out);
        }
        JsonArray arrOut = arrBuilder.build();
        JsonObject jOut = Json.createObjectBuilder().add("test", "mic check").build();

        return ResponseEntity.ok().body(arrOut.toString());
    }

    @GetMapping(path="/api/{cuisine}/restaurants", produces = MediaType.APPLICATION_JSON_VALUE)
    @CrossOrigin(origins="*")
    public ResponseEntity<String> getRestaurantsByCuisine(@PathVariable String cuisine) {
        List<String> results = service.getRestaurantsByCuisine(cuisine);
        JsonArrayBuilder arrBuilder = Json.createArrayBuilder();
        
        for (String item : results) {
            JsonObjectBuilder objBuilder = Json.createObjectBuilder().add("name", item);
            JsonObject out = objBuilder.build();
            arrBuilder.add(out);
        }
        JsonArray arrOut = arrBuilder.build();

        return ResponseEntity.ok().body(arrOut.toString());
    }

    @GetMapping(path="/api/restaurants/{name}",  produces = MediaType.APPLICATION_JSON_VALUE)
    @CrossOrigin(origins="*")
    public ResponseEntity<String> getRestaurantById(@PathVariable String name) throws IOException {
        
        // ResponseEntity<byte[]> out = service.getRestaurant(name);
        // InputStream target = new ByteArrayInputStream(out.getBody());
        // byte[] image = target.readAllBytes();

        Restaurant restaurant = service.getRestaurant(name);

        List<Float> arr = new ArrayList<>();
        arr.add(restaurant.getCoordinates().getLatitude());
        arr.add(restaurant.getCoordinates().getLongitude());

        JsonObjectBuilder jOut = Json.createObjectBuilder()
            .add("restaurant_id", restaurant.getRestaurantId())
            .add("name", restaurant.getName())
            .add("cuisine", restaurant.getCuisine())
            .add("address", restaurant.getAddress())
            .add("coordinates", arr.toString())
            .add("mapUrl", restaurant.getMapURL());
        JsonObject Out = jOut.build();
        return ResponseEntity.ok().body(Out.toString());
    }

    @PostMapping(path="api/comments",consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @CrossOrigin(origins="*")
    public ResponseEntity<String> postComments(@RequestBody Comment comment) {
        
        System.out.println(comment.getName());
        System.out.println(comment.getRating());
        System.out.println(comment.getRestaurantId());
        System.out.println(comment.getText());

        JsonObject jOut = Json.createObjectBuilder().add("message", "comment posted").build();

        return ResponseEntity.ok().body(jOut.toString());
    }
}