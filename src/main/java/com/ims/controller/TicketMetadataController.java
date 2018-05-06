package com.ims.controller;

import java.util.List;

import org.omg.CORBA.portable.ApplicationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ims.entity.TicketMetadata;
import com.ims.service.TicketMetadataService;

@RestController
@RequestMapping("/ticketmetata")
public class TicketMetadataController {
	
	@Autowired
	TicketMetadataService ticketMetadataService;

    @PostMapping
    public List<TicketMetadata> createMetadata(@RequestBody List<TicketMetadata> ticketMetadata) {
        return ticketMetadataService.create(ticketMetadata);
    }

    @PutMapping
    public List<TicketMetadata> updateMetadata(@RequestBody List<TicketMetadata> ticketMetadata) {
        return ticketMetadataService.update(ticketMetadata);
    }

    @DeleteMapping(value = "/{metadata}")
    public ResponseEntity<Void> deleteMetadata(@PathVariable String metadata) throws ApplicationException {
    	ticketMetadataService.delete(metadata);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping(value = "/{metadata}")
    public TicketMetadata findMetadataById(@PathVariable String metadata) throws ApplicationException {
        return ticketMetadataService.findById(metadata);
    }

    @GetMapping
    public List<TicketMetadata> findAll() {
        return ticketMetadataService.findAll();
    }
    
    @PostMapping(value = "/getData")
    public List<TicketMetadata> findBySystemNameAndCustomer(@RequestBody TicketMetadata ticketMetadata) {
        return ticketMetadataService.findBySystemNameAndCustomer(ticketMetadata.getSystemName(), ticketMetadata.getCustomer());
    }
    
    @PostMapping(value = "/getDataBySystemName")
    public List<TicketMetadata> findBySystemName(@RequestBody TicketMetadata ticketMetadata) {
        return ticketMetadataService.findBySystemName(ticketMetadata.getSystemName());
    }
    
    @PostMapping(value = "/getDataByCustomerName")
    public List<TicketMetadata> findByCustomer(@RequestBody TicketMetadata ticketMetadata) {
        return ticketMetadataService.findByCustomer(ticketMetadata.getCustomer());
    }
	
}
