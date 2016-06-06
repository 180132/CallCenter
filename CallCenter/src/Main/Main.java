package Main;

import ActivitiRestApi.RestApi;
import AutomaticService.AutoAnswering;

public class Main {

	 public static void main(String[] args) throws Exception {
		 //AutoAnswering autoAnswering = new AutoAnswering();
		 RestApi restApi = new RestApi("http://localhost:8080/activiti-rest/service/repository");
	 }	 
}
