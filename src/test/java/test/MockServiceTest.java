package test;

import static org.junit.Assert.*;

import org.eclipse.jetty.client.ContentExchange;
import org.eclipse.jetty.client.HttpClient;
import org.eclipse.jetty.client.HttpExchange;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockserver.integration.ClientAndProxy;
import org.mockserver.integration.ClientAndServer;
import org.mockserver.model.Parameter;

import com.ericsson.pct.MockServerSimple;

import static org.mockserver.integration.ClientAndProxy.startClientAndProxy;
import static org.mockserver.integration.ClientAndServer.startClientAndServer;
import static org.mockserver.model.HttpRequest.request;
import static org.mockserver.matchers.Times.exactly;
import static org.mockserver.model.HttpForward.forward;
import static org.mockserver.model.HttpForward.Scheme.HTTP;

public class MockServiceTest{
	private ClientAndServer mockServer;
	private String host = "127.0.0.1";
	private int port = 8080;
	private HttpClient httpClient = new HttpClient();
	@Before
	public void startServer(){
		System.out.println("hello");
		mockServer = startClientAndServer(port);
		System.out.println("hello");
		MockServerSimple.configserver();
	}
	@Test
	public void isStage1(){
		try{
			
			ContentExchange contentExchange = new ContentExchange(true);
	        contentExchange.setMethod("GET");
	        contentExchange.setURL("http://localhost" + ":" + port + "/orders/123");
	        httpClient.send(contentExchange);
	        if (contentExchange.waitForDone() == HttpExchange.STATUS_COMPLETED) {
	        	System.out.println(contentExchange.getResponseContent());
                assertEquals("", "");
                System.out.println("after");
            }
	        else {
                throw new RuntimeException("Exception making request to retrieve all books");
            }
		}
		catch(Exception e){
			System.out.println("exception in processing the request.");
		}
		 
	}
	@Test
	public void isStage2(){
		try{
			ContentExchange contentExchange = new ContentExchange(true);
	        contentExchange.setMethod("GET");
	        contentExchange.setURL("http://localhost" + ":" + port + "/orders/123");
	        httpClient.send(contentExchange);
	        if (contentExchange.waitForDone() == HttpExchange.STATUS_COMPLETED) {
                assertEquals(contentExchange.getResponseContent(), "");
            } else {
                throw new RuntimeException("Exception making request to retrieve all books");
            }
		}
		catch(Exception e){
			System.out.println("exception in processing the request.");
		}
	}
	@Test
	public void isStage3(){
		try{
			ContentExchange contentExchange = new ContentExchange(true);
	        contentExchange.setMethod("GET");
	        contentExchange.setURL("http://localhost" + ":" + port + "/orders/123");
	        httpClient.send(contentExchange);
	        if (contentExchange.waitForDone() == HttpExchange.STATUS_COMPLETED) {
                assertEquals(contentExchange.getResponseContent(), "");
            } else {
                throw new RuntimeException("Exception making request to retrieve all books");
            }
		}
		catch(Exception e){
			System.out.println("exception in processing the request.");
		}
	}
	@After
	public void stopServer(){
		mockServer.stop();
	}

}