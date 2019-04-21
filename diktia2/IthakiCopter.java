import java.util.*;
import java.net.*;
import java.io.*;



public class IthakiCopter{
	public static void main(String[] args)throws Exception{
		byte[] addressArray={(byte)155,(byte)207,18,(byte)208};
		String Ithakiopter_request_code="Q2375";
		int Client_listening_port=48030;
		int Server_listening_port=38030;
		InetAddress serverAddress=InetAddress.getByAddress(addressArray);
		DatagramSocket sendSocket=new DatagramSocket();
		DatagramSocket receiveSocket=new DatagramSocket(Client_listening_port);
		receiveSocket.setSoTimeout(10000);
		byte[] receiveBuffer=new byte[4096];
		System.out.println("WELCOME TO ITHAKI COPTER\n");
		Scanner userInput=new Scanner(System.in);
		System.out.println("Give the flight level the left motor level and the right motor level");
		String input_flight_level=userInput.nextLine();
		int desirable_flight_level=Integer.parseInt(input_flight_level);
		String input_lmotor=userInput.nextLine();
		int desirable_lmotor=Integer.parseInt(input_lmotor);
		String input_rmotor=userInput.nextLine();
		int desirable_rmotor=Integer.parseInt(input_rmotor);
		PrintWriter pw=null;
	        try {
	        	pw = new PrintWriter(new File("Ithakicopter.csv")); //apothikefsh xronwn apokrishs se ena arxio .csv
	        	} catch (FileNotFoundException e) {
	        	e.printStackTrace(); //se periptwsh tyxon sfalmatos me th dhmiourgia arxeioy mpainei
	       }
	        
	       StringBuilder sb = new StringBuilder();

	       sb.append("id_of_measurement"); 
          sb.append(",");
          sb.append("desirable_flight_level");  
          sb.append(",");
          sb.append("desirable_lmotor");  //LMOTOR
          sb.append(",");
          sb.append("desirable_rmotor");  //RMOTOR
          sb.append(",");
          sb.append("lmotor_value");  //LMOTOR
          sb.append(",");
          sb.append("rmotor_value");  //RMOTOR
          sb.append(",");
          sb.append("altitude");  //ALTITUDE
          sb.append(",");
          sb.append("temperature");  //TEMPERATURE
          sb.append(",");
          sb.append("pressure");    //PRESSURE
          sb.append("\n");
	
	        
	        try {
	            Socket sock = new Socket(serverAddress,38048); //38048
	            DataOutputStream ithakicopter_stream = new DataOutputStream(sock.getOutputStream());
	            BufferedReader in = new BufferedReader(new InputStreamReader(sock.getInputStream()));
	            BufferedReader stdIn =new BufferedReader(new InputStreamReader(System.in));
	           
	            ithakicopter_stream.writeBytes("GET /index.html HTTP/1.0\r\n\r\n");

	            long startTime = System.currentTimeMillis();
	            int i=-1;
	            
	            while ((System.currentTimeMillis()-startTime)< 4 * 60 * 1000){  //gia 4 lepta 240*1000
	                String message = in.readLine();
	                System.out.println(message);
	                int index = message.indexOf("ITHAKICOPTER");
	                ithakicopter_stream.writeBytes("AUTO FLIGHTLEVEL="+desirable_flight_level+" LMOTOR="+desirable_lmotor+" RMOTOR="+desirable_rmotor+" PILOT \r\n");
	             
	                if(index>=0){	                
	                i++;	

	                if(i>0){
	                sb.append(i); 
		            sb.append(",");
	                String[] Splitted = message.split(" |=");
	                sb.append(desirable_flight_level);  //FLIGHT LEVEL
	                sb.append(",");
	                sb.append(desirable_lmotor);  //LMOTOR
	                sb.append(",");
	                sb.append(desirable_rmotor);  //RMOTOR
	                sb.append(",");
	                int lmotor = Integer.parseInt(Splitted[2]);
	                sb.append(lmotor);  //LMOTOR
	                sb.append(",");
	                int rmotor = Integer.parseInt(Splitted[4]);
	                sb.append(rmotor);  //RMOTOR
	                sb.append(",");
	                int altitude = Integer.parseInt(Splitted[6]);
	                sb.append(altitude);  //ALTITUDE
	                sb.append(",");
	                float temperature = Float.parseFloat(Splitted[8]); 
	                sb.append(temperature);  //TEMPERATURE
	                sb.append(",");
	                float pressure = Float.parseFloat(Splitted[10]);
	                sb.append(pressure);    //PRESSURE
	                sb.append("\n");
	                
	          
	                if(altitude>desirable_flight_level){
	                	--desirable_lmotor;
		                --desirable_rmotor;
	                }else{
	                	 ++desirable_lmotor;
	 	                 ++desirable_rmotor;
	                }
	                
	                }
	                }
	            }
	            
	         }catch (Exception e){}
	        pw.write(sb.toString());
	        pw.close();
	}

}