package ActivitiRestApi;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map.Entry;



public class RestApi {
	private String basicUrl;
	private HashMap<String, String> properties = new HashMap<>();
	
	private enum MethodType {
		GET, POST, PUT, DELETE
	}
		
	public RestApi(String basicUrl) {
		this.basicUrl = basicUrl;
		this.properties.put("Authorization", "Basic a2VybWl0Omtlcm1pdA==");
		testRestMethods();
	}
	
	
	private void testRestMethods() {		
		try {
			testGetMethod(properties);
			//testPostMethod(properties);
			//testPutMethod(properties);	     
			//testDeleteMethod(properties);	        				
		} catch(IOException e) {
			e.printStackTrace();
		}
	}	
	
	private void testGetMethod(HashMap<String, String> properties) throws IOException {
		String jsonString = httpRequest(basicUrl + "/deployments", properties, MethodType.GET);
		System.out.println("PARAMETER: " + jsonString + "\n\n");
	}
	
	private void testPostMethod(HashMap<String, String> properties) throws IOException {
		System.out.println("Post\n" + httpRequest(basicUrl + "", properties, MethodType.POST)); //Dodawanie deploymentu.
	}//"?id=10&name=activiti-examples.bar&deploymentTime=2010-10-13T14:54:26.750+02:00&category=null&url=http://localhost:8081/service/repository/deployments/10&tenantId=10"
	
	private void testPutMethod(HashMap<String, String> properties) throws IOException {
		System.out.println("Put\n" + httpRequest(basicUrl + "/id2/sort", properties, MethodType.PUT));
	}
	
	private void testDeleteMethod(HashMap<String, String> properties) throws IOException {
		System.out.println("Delete\n" + httpRequest(basicUrl + "/20", properties, MethodType.DELETE)); //Usuwanie deployment'u o id równym 20.
	}
	
	private String httpRequest(String urlStr, HashMap<String, String> properties, MethodType methodType) throws IOException {		
		URL url = new URL(urlStr);
		HttpURLConnection httpConnection = (HttpURLConnection) url.openConnection();
		httpConnection.setRequestMethod(methodType.toString());
		for(Entry<String, String> entry : properties.entrySet()) {
			httpConnection.setRequestProperty(entry.getKey(), entry.getValue());
		}

		if(methodType == MethodType.POST) {
			httpConnection.setDoOutput(true);
			OutputStream os = httpConnection.getOutputStream();
			os.write("tenantId=myId".getBytes());
			os.flush();
			os.close();
		}
        System.out.println("Response Code: " + httpConnection.getResponseCode());
		
		BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(httpConnection.getInputStream()));
		StringBuilder strB = new StringBuilder();
		String line;
		while ((line = bufferedReader.readLine()) != null) {
			strB.append(line);
		}
		bufferedReader.close();

		httpConnection.disconnect();
		return strB.toString();
	}
}
