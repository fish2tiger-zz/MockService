package test;

import static org.junit.Assert.*;

import java.util.Map;

import org.eclipse.jetty.client.ContentExchange;
import org.eclipse.jetty.client.HttpClient;
import org.eclipse.jetty.client.HttpExchange;
import org.eclipse.jetty.io.ByteArrayBuffer;
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
	Map<String,String> files = MockServerSimple.loadResource();
	@Before
	public void startServer(){
		mockServer = startClientAndServer(port);
		MockServerSimple.configserver();
		System.out.println("bofore");
		try{
			httpClient.start();
		}
		catch(Exception e){
			System.out.println("error creating httpclient");
		}
	}
	@Test
	public void isStage1(){
		try{
			ContentExchange contentExchange = new ContentExchange(true);
	        contentExchange.setMethod("POST");
	        contentExchange.setURL("http://localhost" + ":" + port + "/orders/123");
	        httpClient.send(contentExchange);
	        if (contentExchange.waitForDone() == HttpExchange.STATUS_COMPLETED) {
	        	String res = contentExchange.getResponseContent();
                assertEquals(files.get("order-1.json"), res);
            }
	        else {
                throw new RuntimeException("Exception making request to retrieve all books");
            }
		}
		catch(Exception e){
			System.out.println("exception in processing the request.");
		}
		try{
			ContentExchange contentExchange = new ContentExchange(true);
	        contentExchange.setMethod("POST");
	        contentExchange.setURL("http://localhost" + ":" + port + "/orders/123");
	        contentExchange.setRequestContentType("application/json; charset=UTF-8");;
	        contentExchange.setRequestContent(new ByteArrayBuffer(files.get("login.json")));
	        httpClient.send(contentExchange);
	        ;
	        if (contentExchange.waitForDone() == HttpExchange.STATUS_COMPLETED) {
	        	String res = contentExchange.getResponseContent();
                assertEquals(files.get("order-2.json"), res);
            } else {
                throw new RuntimeException("Exception making request to retrieve all books");
            }
		}
		catch(Exception e){
			System.out.println("exception in processing the request.");
		}
		try{
			ContentExchange contentExchange = new ContentExchange(true);
	        contentExchange.setMethod("POST");
	        contentExchange.setURL("http://localhost" + ":" + port + "/orders/123");
	        httpClient.start();
	        httpClient.send(contentExchange);
	        if (contentExchange.waitForDone() == HttpExchange.STATUS_COMPLETED) {
	        	String res = contentExchange.getResponseContent();
	        	assertEquals(files.get("order-3.json"), res);
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
		System.out.println("after");
	}

}