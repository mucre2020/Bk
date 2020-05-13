package com.example.api.controller;

import java.io.IOException;
import java.util.List;

import javax.validation.Valid;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.example.api.dao.TripsDao;
import com.example.api.model.Trips;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

@RestController
@RequestMapping("/api")
public class TripsController {
	OkHttpClient client = new OkHttpClient();
	@Autowired
	TripsDao tripDao;
	/* to save */
	@PostMapping("/trips")
	public Trips create(@Valid @RequestBody Trips trip) {
		
		
			float response = this.distFrom(trip.getDepartureLat(), trip.getDepartureLong(), trip.getDestinationLat(), trip.getDestinationLong());
			System.out.println(response);
	         trip.setDistance(response/1000);
	         trip.setDueAmount(response*300);//business logic
	         
	
		return tripDao.save(trip);
	}
	
	/*get all */
	@GetMapping("/trips")
	public List<Trips> getAll(){
		return tripDao.findAll();
	}
	
	/*get by id*/
	@GetMapping("/trips/{id}")
	public ResponseEntity<Trips> getById(@PathVariable(value="id") Long id){
		Trips trip=tripDao.findOne(id);
		if(trip==null) {
			return ResponseEntity.notFound().build();
		}
		return ResponseEntity.ok().body(trip);
	}
	
	
	/*update a user by id*/
	@PutMapping("/trips/{id}")
	public ResponseEntity<Trips> update(@PathVariable(value="id") Long id,@Valid @RequestBody Trips details){	
		Trips trip=tripDao.findOne(id);
		if(trip==null) {
			return ResponseEntity.notFound().build();
		}
		
		//all attributes
		
//		trip.setRiderId(details.getRiderId());
//		trip.setDriverId(details.getDriverId());
		trip.setDestinationLat(details.getDestinationLat());
		trip.setDestinationLong(details.getDestinationLong());
		trip.setDepartureLong(details.getDestinationLong());
		trip.setDepartureLat(details.getDepartureLat());
		trip.setStatus(details.getStatus());
		trip.setDueAmount(details.getDueAmount());
		trip.setPendingAmount(details.getPendingAmount());
		trip.setDistance(details.getDistance());
		Trips update=tripDao.save(trip);
		return ResponseEntity.ok().body(update);		
	}
	
	
	
	/* Delete a User */
	@DeleteMapping("/trips/{id}")
	public ResponseEntity<Trips> delete(@PathVariable(value="id") Long id){
		Trips trip =tripDao.findOne(id);
		if(trip == null) {
			return ResponseEntity.notFound().build();
		}
		tripDao.delete(trip);
		return ResponseEntity.ok().build();
	}
	
	//google matrix api
	
	public String calculate(double depLat,double depLong,double destLat,double dstLong) throws IOException {
		String url = "https://maps.googleapis.com/maps/api/distancematrix/json?origins="+depLat+","+depLong+"&destinations="+destLat+","+depLong+"&key=AIzaSyDUYbTR-3PDWPhgxjENs4yf35g2eHc641s";
		Request request = new Request.Builder().url(url).build();

		Response response = client.newCall(request).execute();
		return response.body().string();
	}
	
	
	public  float distFrom(float lat1, float lng1, float lat2, float lng2) {
	    double earthRadius = 6371000; //meters
	    double dLat = Math.toRadians(lat2-lat1);
	    double dLng = Math.toRadians(lng2-lng1);
	    double a = Math.sin(dLat/2) * Math.sin(dLat/2) +
	               Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) *
	               Math.sin(dLng/2) * Math.sin(dLng/2);
	    double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a));
	    float dist = (float) (earthRadius * c);

	    return dist;
	    }

	
}
