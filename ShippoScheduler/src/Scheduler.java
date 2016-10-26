import java.util.Scanner;

public class Scheduler {
	public String startSchedulerMessage(){
		String schedulerMessage = "1. Enter 1 to list all future appointments \n"
				+ "2. Enter 2 to schedule an appointment \n3. Enter 3 to exit the scheduler"; 
		return schedulerMessage; 
	}
	
	public String addEventMessage(){
		String addEventMessage = "Please enter desired time (in Military Time!) and appointment type\n"
				+ "Example: '13:00, Shampoo & Cut\n'"
				+ "Example: '14:00, Haircut'"; 
		
		return addEventMessage; 
	}
	
	public void startScheduler(String[] args){
		try{
			while(true){
				System.out.println(startSchedulerMessage());
				Scanner scan = new Scanner(System.in);
				String s = scan.next();				
				
				while(!s.equals("1") && !s.equals("2") && !s.equals("3")){
					System.out.println(startSchedulerMessage());
					scan.nextLine();
				}
				
				if(s.equals("1")){
					System.out.println("You entered a 1");
					scan.nextLine();
				}else if(s.equals("2")){
					
				}else if(s.equals("3")){
					System.exit(0);
				}
			}
		
			
		}catch(Exception ex){
			ex.printStackTrace();
			System.out.println(ex);
		}
		
	}
	
	/*public StartEndTime addEvent(String[] args){
		System.out.println("hi");
		System.out.println(addEventMessage());
		
		//sample - remove later 
		StartEndTime SET = new StartEndTime("hello", "hello"); 
		return SET; 
		
	}*/
	
	public static void main (String[] args) throws Exception{
		Scheduler scheduler = new Scheduler(); 
		scheduler.startScheduler(args); 
	}
}
