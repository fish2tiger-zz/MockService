package com.ericsson.pct;
import static org.mockserver.integration.ClientAndServer.startClientAndServer;
import static org.mockserver.matchers.Times.once;
import static org.mockserver.matchers.Times.exactly;
import static org.mockserver.model.HttpRequest.request;
import static org.mockserver.model.HttpResponse.response;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;




import org.apache.commons.io.IOUtils;
import org.mockserver.client.server.MockServerClient;
import org.mockserver.integration.ClientAndServer;
import org.mockserver.model.Delay;
import org.mockserver.model.Header;

import static java.util.concurrent.TimeUnit.SECONDS;


             
public class MockServerSimple {
	
	static public String file2string(File file){
		StringBuffer sb = new StringBuffer();
		try{
			FileInputStream is = new FileInputStream(file);
			try { 
                Reader r = new InputStreamReader(is, "UTF-8"); 
                int length = 0; 
                for (char[] c = new char[1024]; (length = r.read(c)) != -1;) { 
                        sb.append(c, 0, length); 
                } 
                r.close(); 
	        } 
			catch (UnsupportedEncodingException e) { 
	                e.printStackTrace(); 
	        } 
			catch (FileNotFoundException e) { 
	                e.printStackTrace(); 
	        } 
			catch (IOException e) { 
	                e.printStackTrace(); 
	        } 
        return sb.toString(); 
		}
		catch(FileNotFoundException ex){
			System.out.println("File not found in there...");
		}
		return sb.toString();
	}
	static public Map<String,String> loadResource() throws IOException{
		Map<String,String> pair = new HashMap<String,String>();

		InputStream is1 = ClassLoader.getSystemResourceAsStream("login.json");
		pair.put("login.json", IOUtils.toString(is1,"UTF-8"));
		is1 = ClassLoader.getSystemResourceAsStream("order-1.json");
		pair.put("order-1.json",  IOUtils.toString(is1,"UTF-8"));
		is1 = ClassLoader.getSystemResourceAsStream("order-2.json");
		pair.put("order-2.json",  IOUtils.toString(is1,"UTF-8"));
		is1 = ClassLoader.getSystemResourceAsStream("order-3.json");
		pair.put("order-3.json",  IOUtils.toString(is1,"UTF-8"));
		
		return pair;
	}
	static void startMockServer(){
		ClientAndServer mockServer = startClientAndServer(8080);
	}
	static public void configserver() throws IOException {
		Map<String,String>  resources = loadResource();
		
		MockServerClient mockserverclient = new MockServerClient("127.0.0.1",8080);
		
		mockserverclient
				.when(
					request()
					.withMethod("POST")
					.withPath("/orders/")
					.withBody(resources.get("login.json")),
					once()
				)
				.respond(
						response().withStatusCode(200).withHeaders(
                               new Header("Content-Type", "application/json; charset=utf-8")
                        )
                        .withBody(resources.get("order-1.json"))
                        .withDelay(new Delay(SECONDS, 1))
				);
		 
		mockserverclient
			.when(
				request()
				.withMethod("GET")
				.withPath("/orders/[0-9]*"),
				exactly(2)
			)
			.respond(
				response().withStatusCode(200).withHeader(
						new Header("Content-type","application/json;charset=utf-8")
				)
				.withBody(resources.get("order-1.json"))
				.withDelay(new Delay(SECONDS, 5))
			);
		
		mockserverclient
		.when(
			request()
			.withMethod("GET")
			.withPath("/orders/[0-9]*"),
			exactly(2)
		)
		.respond(
			response().withStatusCode(200).withHeader(
					new Header("Content-type","application/json;charset=utf-8")
			)
			.withBody(resources.get("order-3.json"))
			.withDelay(new Delay(SECONDS, 5))
		);
		mockserverclient.dumpToLog();
	 
	}

	static public void main(String[] args) throws IOException {
		startMockServer();
		configserver();
	}

}
