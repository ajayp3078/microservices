package com.VaccinationCenter.Controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.VaccinationCenter.Entity.VaccinationCenter;
import com.VaccinationCenter.Model.Citizen;
import com.VaccinationCenter.Model.RequiredResponse;
import com.VaccinationCenter.Repository.CenterRepo;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;

@RestController
@RequestMapping("/vaccinationcenter")
public class VaccinationCenterController {
	
	private static final String USER_SERVICE = "userService";
	
	@Autowired
	private CenterRepo centerRepo;
	
	@Autowired
	private RestTemplate restTemplate;


	@PostMapping("/add")
	public ResponseEntity<VaccinationCenter> addCitizen(@RequestBody VaccinationCenter vaccinationCenter){
		VaccinationCenter newCenter = centerRepo.save(vaccinationCenter);
		return new ResponseEntity<>(newCenter, HttpStatus.CREATED); 
		
	}
	
	@GetMapping("/id/{id}")
	@CircuitBreaker(name = USER_SERVICE, fallbackMethod = "handleCitizenDownTime")
	public ResponseEntity<RequiredResponse> getAllCitizensByVCId(@PathVariable Integer id){
		RequiredResponse requiredResponse = new RequiredResponse();
		VaccinationCenter center = centerRepo.findById(id).get();
		requiredResponse.setCenter(center);
		List<Citizen> listOfCitizens = restTemplate.getForObject("http://CITIZEN-SERVICE/citizen/id/"+id,List.class);
		requiredResponse.setCitizens(listOfCitizens);
		return new ResponseEntity<RequiredResponse>(requiredResponse,HttpStatus.OK);
	}
	
	public ResponseEntity<RequiredResponse> handleCitizenDownTime(Integer id, Exception e){
		RequiredResponse requiredResponse = new RequiredResponse();
		VaccinationCenter center = centerRepo.findById(id).get();
		requiredResponse.setCenter(center);
		return new ResponseEntity<RequiredResponse>(requiredResponse,HttpStatus.OK);
	}
	
}
