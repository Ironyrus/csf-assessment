package vttp2022.csf.assessment.server.repositories;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Repository;
import org.springframework.web.multipart.MultipartFile;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.AmazonS3Exception;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.GetObjectRequest;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectInputStream;

import vttp2022.csf.assessment.imageFile;
import vttp2022.csf.assessment.servletContext;

@Repository
public class MapCache {

	@Autowired 
    AmazonS3 s3Client;

	// TODO Task 4
	// Use this method to retrieve the map
	// You can add any parameters (if any) and the return type 
	// DO NOT CHNAGE THE METHOD'S NAME
	public String getMap(ResponseEntity<byte[]> response, String name) throws IOException {
		
		// Implmementation in here
		byte[] image = response.getBody();
		Map<String, String> userData = new HashMap<>();
        
        userData.put("service", "restaurant");
        userData.put("uploadTime", (new Date().toString()));
		
		ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentLength(image.length);
        metadata.setContentType("image/png");
        metadata.setUserMetadata(userData);

		String key = name;
		InputStream is = new ByteArrayInputStream(response.getBody());

		// https://ridhwan-bucket.sgp1.digitaloceanspaces.com/restaurantImages%2FRiviera%20Caterer
		String url = "https://ridhwan-bucket.sgp1.digitaloceanspaces.com/restaurantImages/" + key; 

		byte[] checkImage = checkMap(key);
		if(checkImage == null){
			PutObjectRequest put = new PutObjectRequest(
				"ridhwan-bucket", // bucket name
				"restaurantImages/%s".formatted(key), // myobjects/asd1s3af, myobjects/asd98dsd etc
				is,
				metadata);
	
				// Allow public access
				put = put.withCannedAcl(CannedAccessControlList.PublicRead);
	
				s3Client.putObject(put);
				// return response.getBody();
				return url;
		} else {
			System.out.println("returning from cache...");
			// return checkImage;
			return url;
		}
	}

	// You may add other methods to this class
	public byte[] checkMap(String key){
		 AmazonS3Exception e = null;
        try {
			key = "restaurantImages/" + key;
            GetObjectRequest getReq = new GetObjectRequest("ridhwan-bucket", key);
            S3Object result = s3Client.getObject(getReq);
            ObjectMetadata metadata = result.getObjectMetadata();
            Map<String, String> userData = metadata.getUserMetadata();
            System.out.println(result.getKey());
            try(S3ObjectInputStream is = result.getObjectContent()){
                byte[] buffer = is.readAllBytes();
				System.out.println("Returning from Amazon...");
                return buffer;
            } catch (AmazonS3Exception ex) {
                // TODO: handle exception
                e = ex;
                System.out.println(ex.getErrorMessage());
            }
        } catch (Exception ex2) {
                System.out.println(ex2.getMessage());
        }
		return null;
	}
}
