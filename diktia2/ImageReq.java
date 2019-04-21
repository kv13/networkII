import java.io.*;
import java.net.*;
import java.util.*;


public class ImageReq{
	public static void main(String[] args)throws Exception{
		byte[] addressArray={(byte)155,(byte)207,18,(byte)208};
		int Client_listening_port=48030;
		int Server_listening_port=38030;
		String Image_request_code="M3821";
		InetAddress serverAddress=InetAddress.getByAddress(addressArray);
		DatagramSocket sendSocket=new DatagramSocket();
		DatagramSocket receiveSocket=new DatagramSocket(Client_listening_port);
		receiveSocket.setSoTimeout(10000);
		byte[] receiveBuffer=new byte[4096];
		/*DatagramSocket s=new DatagramSocket();
		String imageReq="M8317\r";
		byte[] txbuffer=imageReq.getBytes();
		int serverPort=38002;
		byte[] hostIP={(byte)155,(byte)207,18,(byte)208};
		InetAddress hostAddress=InetAddress.getByAddress(hostIP);
		DatagramPacket p=new DatagramPacket(txbuffer,txbuffer.length,hostAddress,serverPort);
		int clientPort=48002 ;
		DatagramSocket r=new DatagramSocket(clientPort);
		r.setSoTimeout(8000);
		byte[] rxbuffer=new byte[4096];
		DatagramPacket q=new DatagramPacket(rxbuffer,rxbuffer.length);
		List<Byte> received_packet=new ArrayList<Byte>();
		File image=new File("Image.jpg");
		FileOutputStream image_stream=null;
		int image_pointer=0;
		try{
			image_stream=new FileOutputStream(image);
		}catch (Exception x){
			System.out.println("Cannot open the file ");
			System.exit(1);
		}
		s.send(p);
		for(;;){
			try{
				r.receive(q);
				for(int i=0;i<q.getLength();i++){
					received_packet.add(q.getData()[i]);
				}
				image_pointer=image_pointer+q.getLength();
				if(received_packet.get(image_pointer-1)==(byte)217 && received_packet.get(image_pointer-2)==(byte)255){
					byte[] array=new byte[received_packet.size()];
					for(int i=0;i<received_packet.size();i++){
						array[i]=received_packet.get(i);
					}
					image_stream.write(array,0,received_packet.size());
					image_stream.flush();
					image_stream.close();
					break; 
				}
			}catch (Exception x){
				System.out.println(x);
			}
		}
		System.out.println("FINISHHHHH");*/
				String Image_request_code_final=Image_request_code+"\r";
		byte[] buf=Image_request_code_final.getBytes();
		DatagramPacket sendPacket = new DatagramPacket(buf, buf.length, serverAddress, Server_listening_port);
		DatagramPacket receivePacket = new DatagramPacket(receiveBuffer,receiveBuffer.length);
		try {
			sendSocket.send(sendPacket);
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		
		List<Byte> received_packet =new ArrayList<Byte>();
		File image = new File("ImageCAM1.jpeg");
		FileOutputStream image_stream;

		int image_pointer=0;
		byte[] end_image_delimiter = {(byte) 255,(byte) 217}; //image delimiter
		for (;;) {
			 try {
				 image_stream = new FileOutputStream(image);
				 receiveSocket.receive(receivePacket);
				 System.out.println("Received packet");
				 for (int i=0; i<receivePacket.getLength(); i++){
					 received_packet.add(receivePacket.getData()[i]);
				 }
				 image_pointer=image_pointer+receivePacket.getLength();
				 if(received_packet.get(image_pointer-1)==end_image_delimiter[1] && received_packet.get(image_pointer-2)==end_image_delimiter[0]){  //start of image delimeter
					 byte[] array=new byte[received_packet.size()];
					 for(int i=0;i<received_packet.size(); i++){
						 array[i]=received_packet.get(i);
					 }
					 image_stream.write(array, 0, received_packet.size());
					 image_stream.flush();
					 image_stream.close();
					 break;
				 }
			 } catch (Exception x) {
			 }
		}
		
		Image_request_code_final=Image_request_code+"CAM=PTZ"+"\r"; //edw vazw poy na stripsei h kamera
		buf=Image_request_code_final.getBytes();
		sendPacket = new DatagramPacket(buf, buf.length, serverAddress, Server_listening_port);
		try {
			sendSocket.send(sendPacket);
		} catch (IOException e) {
			e.printStackTrace();
		}
		received_packet=new ArrayList<Byte>();
		image = new File("ImageCAM2.jpeg");
		try {
			image_stream = new FileOutputStream(image);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		image_pointer=0;
		for (;;) {
			 try {
				 receiveSocket.receive(receivePacket);
				 System.out.println("Receive packet from Server");
				 for (int i=0; i<receivePacket.getLength(); i++){
					 received_packet.add(receivePacket.getData()[i]);
				 }
				 image_pointer=image_pointer+receivePacket.getLength();
				 if(received_packet.get(image_pointer-1)==end_image_delimiter[1] && received_packet.get(image_pointer-2)==end_image_delimiter[0]){
					 byte[] array=new byte[received_packet.size()];
					 for(int i=0;i<received_packet.size(); i++){
						 array[i]=received_packet.get(i);
					 }
					 image_stream.write(array, 0, received_packet.size());
					 image_stream.flush();
					 image_stream.close();
					 break;
				 }
			 } catch (Exception x) {
			 }
		}
	}
}