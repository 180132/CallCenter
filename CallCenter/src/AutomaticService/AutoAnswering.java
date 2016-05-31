package AutomaticService;

import java.util.Dictionary;
import java.util.Hashtable;

import com.skype.ChatMessage;
import com.skype.ChatMessageAdapter;
import com.skype.Skype;
import com.skype.SkypeException;
import com.skype.User;

public class AutoAnswering {

	static Dictionary<String,String> Clients = new Hashtable<String,String>();
	
	 public static void main(String[] args) throws Exception {
		 
	        System.out.println("Start Auto Answering ...");

	        Skype.setDaemon(false);
	         
	        Skype.addChatMessageListener(new ChatMessageAdapter() {
	            public void chatMessageReceived(ChatMessage received)
	                    throws SkypeException {
	                if (received.getType().equals(ChatMessage.Type.SAID)) {
	                	
	                    User sender =received.getSender();    
	                     
	                    System.out.println(sender.getId() +" say:");
	                    System.out.println(" "+received.getContent() );

	                    String answer = Answer(sender.getId(), received.getContent());
	                    
	                    received.getSender().send(answer);
	                }
	            }
	        });
	         
	        System.out.println("Auto Answering started!");
	    }
	 
	 private static String Answer(String user, String message){
		 if(Clients.get(user)==null){
			 Clients.put(user, "state0");
		 	return "Hello, this is automatic Call Center!\n\n"
		 			 + "Choose what you want to do:\n" 
			         + "press 1 to log in\n"
			         + "press 2 to connect to an agent\n"
			         + "press 3 to hear options again\n";
		 }
		 else{
			 if(Clients.get(user)=="state0"){
				 if(message.equals("1")){
					 Clients.put(user, "state1");//klient przechodzi do innego taska
					 return "Loging in ...";
				 }
				 if(message.equals("2")){
					 Clients.put(user, "state2");//klient przechodzi do innego taska
					 return "Calling ...";
				 }
				 else{
					 return "Choose what you want to do:\n" 
					         + "press 1 to log in\n"
					         + "press 2 to connect to an agent\n"
					         + "press 3 to hear options again\n";
				 }
			 }
			 else
				 return "Error - You should be eighter calling or loging in";
		 }
	 }
}
