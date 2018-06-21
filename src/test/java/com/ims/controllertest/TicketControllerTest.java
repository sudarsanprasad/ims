package com.ims.controllertest;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import com.ims.ImsApplication;
import com.ims.entity.Ticket;
import com.ims.service.TicketService;

/*@RunWith(SpringRunner.class)
@SpringBootTest(classes = ImsApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc*/
public class TicketControllerTest {

	//@Autowired
	private MockMvc mockMvc;
	
	//@MockBean
	TicketService ticketService;
	
	@Before
	public void setUp(){
		ticketService = mock(TicketService.class);
	}
	
	@Test
	public void testGetAllTickets() throws IOException, Exception{
	/*	List<Ticket> ticketList=new ArrayList<>();
		Ticket ticket = new Ticket();
		ticketList.add(ticket);
		when(ticketService.getTicketData()).thenReturn(ticketList);
		this.mockMvc.perform(get("/ticket").contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)).andExpect(status().isOk());
*/	}
}
