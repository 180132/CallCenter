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
	private HashMap<String, String> defaultProperties = new HashMap<>();
	
	private enum MethodType {
		GET, POST, PUT, DELETE
	}
		
	public RestApi(String basicUrl) {
		this.basicUrl = basicUrl;
		this.defaultProperties.put("Authorization", "Basic a2VybWl0Omtlcm1pdA==");
	}	
	
	public String listOfProcessDefinitions() {
		try {
			return httpRequest(basicUrl + "/repository/process-definitions", defaultProperties, MethodType.GET);
		} catch(IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public String getProcessDefinition(String processDefinitionId) {
		try {
			return httpRequest(basicUrl + "/repository/process-definitions/" + processDefinitionId, defaultProperties, MethodType.GET);
		} catch(IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public String listOfProcessInstances() {
		try {
			return httpRequest(basicUrl + "/runtime/process-instances", defaultProperties, MethodType.GET);
		} catch(IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public String getProcessInstance(String processInstanceId) {
		try {
			return httpRequest(basicUrl + "/runtime/process-instances/" + processInstanceId, defaultProperties, MethodType.GET);
		} catch(IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public void activateProcessDefinition(String processDefinitionId) {
		try {
			HashMap<String, String> body = new HashMap<>(defaultProperties);
			body.put("action", "activate");
			body.put("includeProcessInstances", "true");
			body.put("date", null);
			httpRequest(basicUrl + "/repository/process-definitions/" + processDefinitionId, body, MethodType.PUT);			
		} catch(IOException e) {
			e.printStackTrace();
		}
	}
	
	public void activateOrSuspendProcessInstance(String processInstanceId, boolean activate) {
		try {
			HashMap<String, String> body = new HashMap<>(defaultProperties);
			if(activate) {
				body.put("action", "activate");
			} else {
				body.put("action", "suspend");
			}
			httpRequest(basicUrl + "/runtime/process-instances/" + processInstanceId, body, MethodType.PUT);			
		} catch(IOException e) {
			e.printStackTrace();
		}
	}
	
	public void startProcessInstance(String processDefinitionId, String businessKey) {
		try {
			HashMap<String, String> body = new HashMap<>(defaultProperties);
			body.put("processDefinitionId", processDefinitionId);
			body.put("businessKey", businessKey);
			body.put("variables", null);
			httpRequest(basicUrl + "/runtime/process-instances", body, MethodType.POST);			
		} catch(IOException e) {
			e.printStackTrace();
		}
	}
	
	public String getTask(String taskId) {
		try {
			return httpRequest(basicUrl + "/runtime/tasks/" + taskId, defaultProperties, MethodType.GET);
		} catch(IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public String getTasks() {
		try {
			return httpRequest(basicUrl + "/runtime/tasks", defaultProperties, MethodType.GET);
		} catch(IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	private String httpRequest(String urlStr, HashMap<String, String> body, MethodType methodType) throws IOException {		
		URL url = new URL(urlStr);
		HttpURLConnection httpConnection = (HttpURLConnection) url.openConnection();
		httpConnection.setRequestMethod(methodType.toString());
		for(Entry<String, String> entry : body.entrySet()) {
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
