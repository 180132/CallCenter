package AutomaticService;

import java.util.ArrayList;
import java.util.Dictionary;
import java.util.Hashtable;

import com.skype.Call;
import com.skype.ChatMessage;
import com.skype.ChatMessageAdapter;
import com.skype.Skype;
import com.skype.SkypeException;
import com.skype.User;
import com.skype.User.Status;

public class AutoAnswering {

	private Dictionary<String, String> Clients = new Hashtable<String, String>();
	
	//lista z konsultantami
	private static ArrayList<Consultant> consultants = new ArrayList<Consultant>();
	
	public AutoAnswering() throws Exception {
		 
		initConsultants();//wype³nij listê konsultantami
		//chooseConsultant();
			Skype.setDaemon(false);
			//Skype callCenter = new Skype("","");
	        System.out.println("Start Auto Answering ...");
	        System.out.println(Skype.getProfile().getId());
	       
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
	 
	 private String Answer(String user, String message){
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
					 callToConsultant(user);
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
	 
	 //rozpoczyna nowa rozmowe i przekierowuje ja do konsultanta
	 //jesli nie ma wolnych konsultantow zwraca false
	 private static boolean callToConsultant(String userId){
		 try {
			
			Consultant k = chooseConsultant();
			if(k!=null) {
				Call c = Skype.call(userId);
				c.transferTo(k.id);
			}
			else{
				return false;
			}
		} catch (SkypeException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		 return true;
	 }
	 
	 //wyberz 1 konsultanta, ktory najdluzej czeka i jest aktywny, inaczej zwroc null
	 private static Consultant chooseConsultant(){
		 
		 //lista aktywnych konsultantów
		 ArrayList<Consultant> active = new ArrayList<Consultant>();
		 for(Consultant it:consultants) {
			 User u;
			try {
				u = Skype.getUser(it.id);
				if(u.getStatus()==Status.ONLINE) active.add(it);
			} catch (SkypeException e) {
				e.printStackTrace();
			}
		 }
		 active.sort((o1, o2) -> o1.waitingTime.compareTo(o2.waitingTime));
		 for(Consultant it:active)System.out.println(it.id + " "+it.waitingTime);

		 return active.size()!=0 ? active.get(0) : null;
	 }
	 
	 //wype³nianie listy konsultantami
	 private static void initConsultants(){
		 consultants.add(new Consultant("konsultant1.miasi"));
		 consultants.add(new Consultant("konsultant2.miasi"));
		 consultants.add(new Consultant("konsultant3.miasi"));
		 consultants.add(new Consultant("konsultant4.miasi"));
		 consultants.add(new Consultant("konsultant5.miasi"));
	 }
}
