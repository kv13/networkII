import java.io.*;
import java.net.*;
import java.util.*;



public class OBD2{
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
			pw=new PrintWriter(new File("SessionOBD.csv"));
		}catch(Exception e){
			e.printStackTrace();
		}
		int i=0;
		String delimeter=" ";
		StringBuilder sb=new StringBuilder();
		sb.append("id");
		sb.append(",");
		sb.append("engine run time");
		sb.append(",");
		sb.append("intake_air_temperature");
		sb.append(",");
		sb.append("throttle_positio");
		sb.append(",");
		sb.append("engine_rpm");
		sb.append(",");
		sb.append("vehicle_speed");
		sb.append(",");
		sb.append("coolant_temperature");
		sb.append("\n");
		try{
			Socket sock=new Socket(serverAddress,29078);
			DataOutputStream obd_stream=new DataOutputStream(sock.getOutputStream());
			BufferedReader in=new BufferedReader(new InputStreamReader(sock.getInputStream()));
			BufferedReader stdIn =new BufferedReader(new InputStreamReader(System.in));
			long startTime=System.currentTimeMillis();
			while(System.currentTimeMillis()-startTime<4*60*1000){
				i++;
				sb.append(i);
				sb.append(",");

					 //1o
	                obd_stream.writeBytes("01"+" "+ "1F"+ "\r"); //OBDII
	                String message = in.readLine();	                
	                String[] received_data = message.split(delimeter);
					int x=Integer.parseInt(received_data[2], 16);
					int y=Integer.parseInt(received_data[3], 16);
					int engine_run_time=255*x + y;
					sb.append(engine_run_time);
					sb.append(",");
					System.out.println(engine_run_time);
					
	               //2o
	                obd_stream.writeBytes("01"+" "+ "0F"+ "\r");
	                message = in.readLine();
	                received_data = message.split(delimeter);
					x=Integer.parseInt(received_data[2], 16);
					int intake_air_temperature=x-40;
					sb.append(intake_air_temperature);
					sb.append(",");
					System.out.println(intake_air_temperature);
					
	              //3o
	              obd_stream.writeBytes("01"+" "+ "11"+ "\r");
	              message = in.readLine();
	                received_data = message.split(delimeter);
					x=Integer.parseInt(received_data[2], 16);
					int throttle_position=x*100/255;
					sb.append(throttle_position);
					sb.append(",");
					System.out.println(throttle_position);
					
	              //4o
	              obd_stream.writeBytes( "01"+" "+ "0C"+ "\r");
	              message = in.readLine();
	                received_data = message.split(delimeter);
					x=Integer.parseInt(received_data[2], 16);
					y=Integer.parseInt(received_data[3], 16);
					int engine_rpm=(256*x + y)/4;
					sb.append(engine_rpm);
					sb.append(",");
					System.out.println(engine_rpm);
					
	              //5o
	              obd_stream.writeBytes( "01"+" "+ "0D"+ "\r");
	              message = in.readLine();
	                received_data = message.split(delimeter);
					x=Integer.parseInt(received_data[2], 16);
					int vehicle_speed=x;
					sb.append(vehicle_speed);
					sb.append(",");
					System.out.println(vehicle_speed);
					
	              //6o
	              obd_stream.writeBytes("01"+" "+ "05"+ "\r");  
	              message = in.readLine();
	                received_data = message.split(delimeter);
					x=Integer.parseInt(received_data[2], 16);
					int coolant_temperature=x-40;
					sb.append(coolant_temperature);
					sb.append("\n");
					System.out.println(coolant_temperature);

			}
		}catch (Exception e){
			System.out.println("Exception Occured");
			System.exit(1);
		}
		pw.write(sb.toString());
		pw.close();
	}
}