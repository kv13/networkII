import java.io.*;
import java.net.*;
import java.util.*;



public class ECHO{
	public static void main(String[] args)throws Exception{
		byte[] addressArray={(byte)155,(byte)207,18,(byte)208};
		int Client_listening_port=48030;
		int Server_listening_port=38030;
		String Echo_request_code="E9134";
		InetAddress serverAddress=InetAddress.getByAddress(addressArray);
		DatagramSocket sendSocket=new DatagramSocket();
		DatagramSocket receiveSocket=new DatagramSocket(Client_listening_port);
		receiveSocket.setSoTimeout(10000);
		byte[] receiveBuffer=new byte[4096];
		 System.out.println("Choose one of the following modes by typing the appropriate number: 1. Receive packets with delay, 2. Receive packets without delay"); 
		   Scanner userInput = new Scanner(System.in);
		   String echo_preference = userInput.nextLine();
		   int echo_user_preference = Integer.parseInt(echo_preference);
		   
		    byte[] buf=(Echo_request_code+"\r").getBytes();
		 
		    if(echo_preference=="2"){
		          buf=("E0000"+"\r").getBytes();
		    }

		    DatagramPacket sendPacket = new DatagramPacket(buf, buf.length, serverAddress, Server_listening_port);
		    DatagramPacket receivePacket = new DatagramPacket(receiveBuffer,receiveBuffer.length);
		    sendSocket.send(sendPacket);

        	BufferedWriter bw = null;
        	FileWriter fw = null;
        	long response_duration;

        	try {
        		fw = new FileWriter("Echo_session.txt",true);
        		bw = new BufferedWriter(fw);
        	} catch (IOException e) {
        		e.printStackTrace();
	        }

        	long beginning = System.currentTimeMillis();
        	long startTime, endTime;
        	while ((System.currentTimeMillis()-beginning)< 5 * 60 * 1000){  //gia 4 lepta 4 * 60 * 1000
        		System.out.println("Received packet from Server");
        		startTime=System.currentTimeMillis();
        		sendSocket.send(sendPacket);
        		receiveSocket.receive(receivePacket);
        		endTime=System.currentTimeMillis(); //end of measurement
        		response_duration=endTime-startTime;//response duration
        		String message = new String(receiveBuffer,0,receivePacket.getLength());
        		String responseLine=String.valueOf(response_duration);
	            message= message +" "+ responseLine +"\r\n";
	            bw.write(message);
        	}
	
        	try {
        		if (bw != null) bw.close();
        		if (fw != null) fw.close();
        	} catch (IOException ex) {
        		ex.printStackTrace();
        	}
        	bw.close();
        	fw.close();
        	System.out.println("FINISHHHHH");
	}
}