import java.net.*;
import java.io.*;
import java.util.*;



public class OBD2EXT{
	public static void main(String[] args)throws Exception{
		String OBDII_request_code="V6584";
		int Client_listening_port=48030;
		int Server_listening_port=38030;
		byte[] addressArray={(byte)155,(byte)207,18,(byte)208};
		InetAddress serverAddress=InetAddress.getByAddress(addressArray);
		DatagramSocket sendSocket=new DatagramSocket();
		DatagramSocket receiveSocket=new DatagramSocket(Client_listening_port);
		receiveSocket.setSoTimeout(10000);
		byte[] receiveBuffer=new byte[4096];
		PrintWriter pw=null;
		try{
			pw=new PrintWriter(new File("OBDextension.csv"));
		}catch(FileNotFoundException e){
			e.printStackTrace();
		}
		String delimeter=" ";
		String Mode="01";
		StringBuilder sb=new StringBuilder();
		int input_PID_position;
		boolean found_PID=false;
        String[][] mode01_Pids_Descriptions = {{"04", "Calculated engine load"}, {"05", "Engine coolant temperature"},{"06", "Short term fuel trim�Bank 1"}, {"07", "Long term fuel trim�Bank 1"},{"08", "Short term fuel trim�Bank 2"}, {"09", "Long term fuel trim�Bank 2"},{"0A", "Fuel pressure (gauge pressure)"}, {"0B", "Intake manifold absolute pressure"},{"0C", "Engine RPM"}, {"0D", "Vehicle speed"},{"0E", "Timing advance"}, {"0F", "Intake air temperature"},{"10", "MAF air flow rate"}, {"11", "Throttle position"}, {"2C", "Commanded EGR"},{"2D", "EGR Error"}, {"2E", "Commanded evaporative purge"},{"2F", "Fuel Tank Level Input"},{"31", "Distance traveled since codes cleared"}, {"5B", "Hybrid battery pack remaining life"}};
	    System.out.println("Welcome to OBI Data Handler");
	    Scanner userInput=new Scanner(System.in);
	    System.out.println("Select one PID of Mode01 that you want data for");
	    String input_PID=userInput.nextLine();
	    System.out.println("Select duration of data to be received in minutes");
	    String input_minutes=userInput.nextLine();
	    int minutes=Integer.parseInt(input_minutes);
	    try {
	            Socket sock = new Socket(serverAddress,29078); 
	            DataOutputStream obd_stream = new DataOutputStream(sock.getOutputStream());
	            BufferedReader in = new BufferedReader(new InputStreamReader(sock.getInputStream()));
	            BufferedReader stdIn =new BufferedReader(new InputStreamReader(System.in));

	            for(int row = 0; row < mode01_Pids_Descriptions.length; row++){
	            	if( input_PID.equals(mode01_Pids_Descriptions[row][0])){
	                	input_PID_position = row;
	                	found_PID =true;
	                	sb.append(mode01_Pids_Descriptions[input_PID_position][1]);  
	     	            sb.append("\n");
	                	break;
	                }
	            } 
	            
	            if(found_PID == false){
	            	 System.out.println("Plhktrologhsate lathos PID");
	            }
	            
	            long startTime = System.currentTimeMillis();
	            while ((System.currentTimeMillis()-startTime)< minutes * 1000 && found_PID == true){ //*1000 gia na ginoyn ta lepta msec
	            	obd_stream.writeBytes(Mode+" "+input_PID + "\r");  
		            String  message = in.readLine();
		            String[] received_data = message.split(delimeter);
		            System.out.println(message);
	             
	                switch (input_PID) {
	                    case "04":  int x=Integer.parseInt(received_data[2], 16);
								  int result= (100*x)/255;
								  sb.append(result);  
				     	          sb.append("\n");
	                              break;
	                    case "05":  x=Integer.parseInt(received_data[2], 16);
						         result= x-40 ;
						         sb.append(result);  
				     	          sb.append("\n");
						         break;
	                    case "06":  x=Integer.parseInt(received_data[2], 16);
						  		result= (100*x)/128 ;
						  		sb.append(result);  
				     	          sb.append("\n");
						  		break;
	                    case "07":  x=Integer.parseInt(received_data[2], 16);
				  				result= (100*x)/128 ;
				  				sb.append(result);  
				     	          sb.append("\n");
				  				break;
	                    case "08": x=Integer.parseInt(received_data[2], 16);
				  				result= (100*x)/128 ;
				  				sb.append(result);  
				     	          sb.append("\n");
				  				break;
	                    case "09": x=Integer.parseInt(received_data[2], 16);
				  				result= (100*x)/128 ;
				  				sb.append(result);  
				     	          sb.append("\n");
				  				break;
	                    case "0A":  x=Integer.parseInt(received_data[2], 16);
		  						result= 3*x;
		  						sb.append(result);  
				     	          sb.append("\n");
		  						break;
	                    case "0B":  x=Integer.parseInt(received_data[2], 16);
  								result= x;
  								sb.append(result);  
				     	          sb.append("\n");
  								break;
	                    case "0C":  x=Integer.parseInt(received_data[2], 16);
								int y=Integer.parseInt(received_data[3], 16);
								result =(256*x + y)/4;
								sb.append(result);  
				     	          sb.append("\n");
								break;
	                    case "0D":  x=Integer.parseInt(received_data[2], 16);
								result= x;
								sb.append(result);  
				     	          sb.append("\n");
								break;
	                    case "0E":  x=Integer.parseInt(received_data[2], 16);
							      result= (x/2)-64;
							      sb.append(result);  
				     	          sb.append("\n");
							      break;
	                    case "0F": x=Integer.parseInt(received_data[2], 16);
					      		result= x-40;
					      		sb.append(result);  
				     	          sb.append("\n");
					      		break;
	                    case "10":   x=Integer.parseInt(received_data[2], 16);
								  y=Integer.parseInt(received_data[3], 16);
								  result =(256*x + y)/100;
								  sb.append(result);  
				     	          sb.append("\n");
								  break;
	                    case "11":   x=Integer.parseInt(received_data[2], 16);
			      					result= (100*x)/255;
			      					break;
	                    case "2C":  x=Integer.parseInt(received_data[2], 16);
      					          result= (100*x)/255;
      					        sb.append(result);  
				     	          sb.append("\n");
      					          break;
	                    case "2D":  x=Integer.parseInt(received_data[2], 16);
				          			result= (100*x)/128-100;
				          			sb.append(result);  
					     	          sb.append("\n");
				          			break;
	                    case "2E":   x=Integer.parseInt(received_data[2], 16);
	          						result= (100*x)/255;
	          						sb.append(result);  
					     	          sb.append("\n");
	          						break;
	                    case "2F":  x=Integer.parseInt(received_data[2], 16);
  								  result= (100*x)/255;
  								sb.append(result);  
				     	          sb.append("\n");
  								  break;
	                    case "31":  x=Integer.parseInt(received_data[2], 16);
	                    			y=Integer.parseInt(received_data[3], 16);
	                    			result =256*x + y;
	                    			sb.append(result);  
					     	          sb.append("\n");
	                    			break;
	                    case "5B":  x=Integer.parseInt(received_data[2], 16);
	                    			result= (100*x)/255;
	                    			sb.append(result);  
					     	          sb.append("\n");
	                    			break;        		
	                }  
	            
	            }
     
	         }catch (Exception e){}
	         pw.write(sb.toString());
	         pw.close();
	}
}