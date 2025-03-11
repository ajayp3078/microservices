package com.CitizenService.controller;

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

import com.CitizenService.entity.Citizen;
import com.CitizenService.repository.CitizenRepository;

@RestController
@RequestMapping("/citizen")
public class CitizenController {
	
	@Autowired
	private CitizenRepository citizenRepository;
	
	@GetMapping("/test")
	public String hello() {
		return "Running";
	}
	
	@GetMapping("/id/{id}")
	public ResponseEntity<List<Citizen>> getCitizensByVaccinationCenterId(@PathVariable Integer id){
		List<Citizen> listOfCitizen = citizenRepository.findByVaccinationCenterId(id);
		return new ResponseEntity<>(listOfCitizen, HttpStatus.OK); 
	}

	@PostMapping("/add")
	public ResponseEntity<Citizen> addCitizen(@RequestBody Citizen citizen){
		Citizen newCitizen = citizenRepository.save(citizen);
		return new ResponseEntity<>(newCitizen, HttpStatus.CREATED); 
	}
	
	

}
