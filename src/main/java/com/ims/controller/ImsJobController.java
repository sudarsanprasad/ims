package com.ims.controller;

import static org.springframework.http.HttpStatus.CREATED;

import org.quartz.Trigger.TriggerState;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ims.jobs.JobDescriptor;
import com.ims.service.ImsJobService;

@RestController
@RequestMapping("/job")
public class ImsJobController {
	
	@Autowired
	private ImsJobService imsJobService;

	@PostMapping(path = "/groups/{group}/jobs")
	public ResponseEntity<JobDescriptor> createJob(@PathVariable String group, @RequestBody JobDescriptor descriptor) {
		return new ResponseEntity<>(imsJobService.createJob(group, descriptor), CREATED);
	}

	@PutMapping(path = "/groups/{group}/jobs/{name}")
	public ResponseEntity<Void> updateJob(@PathVariable String group, @PathVariable String name, @RequestBody JobDescriptor descriptor) {
		imsJobService.updateJob(group, name, descriptor);
		return ResponseEntity.noContent().build();
	}

	@DeleteMapping(path = "/groups/{group}/jobs/{name}")
	public ResponseEntity<Void> deleteJob(@PathVariable String group, @PathVariable String name) {
		imsJobService.deleteJob(group, name);
		return ResponseEntity.noContent().build();
	}

	@PatchMapping(path = "/groups/{group}/jobs/{name}/pause")
	public ResponseEntity<Void> pauseJob(@PathVariable String group, @PathVariable String name) {
		imsJobService.pauseJob(group, name);
		return ResponseEntity.noContent().build();
	}

	@PatchMapping(path = "/groups/{group}/jobs/{name}/resume")
	public ResponseEntity<Void> resumeJob(@PathVariable String group, @PathVariable String name) {
		imsJobService.resumeJob(group, name);
		return ResponseEntity.noContent().build();
	}
	
	@PatchMapping(path = "/{customerName}/forecast/job/build")
	public ResponseEntity<Void> triggerForecastModelScheduler(@PathVariable String customerName) {
		imsJobService.triggerForecastModelScheduler(customerName);
		return ResponseEntity.noContent().build();
	}
	
	@PatchMapping(path = "/groups/{group}/jobs/{name}/state")
	public TriggerState statusJob(@PathVariable String group, @PathVariable String name) {
		return imsJobService.statusJob(group, name);
		 
	}
	
}
