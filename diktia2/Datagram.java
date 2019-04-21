import java.io.*;
import java.net.*;


public class Datagram{
	public static void main(String[] args)throws Exception{
		DatagramSocket s= new DatagramSocket();
		String packetInfo="E7824T00\r";
		String cheat="E0000\r";
		byte[] txbuffer1=cheat.getBytes();
		byte[] txbuffer=packetInfo.getBytes();
		int serverPort=38001;
		byte[] hostIP={(byte)155,(byte)207,18,(byte)208};
		InetAddress hostAddress=InetAddress.getByAddress(hostIP);
		DatagramPacket p1=new DatagramPacket(txbuffer1,txbuffer1.length,hostAddress,serverPort);
		DatagramPacket p=new DatagramPacket(txbuffer,txbuffer.length,hostAddress,serverPort);
		int clientPort=48001;
		DatagramSocket r=new DatagramSocket(clientPort);
		r.setSoTimeout(8000);
		byte[] rxbuffer=new byte[2048];
		DatagramPacket q=new DatagramPacket(rxbuffer,rxbuffer.length);
		String message;
		message=new String(rxbuffer,0,q.getLength());
		System.out.println(message);
		for(;;){
			try{
				//s.send(p1);
				s.send(p);
				r.receive(q);
				message=new String(rxbuffer,0,q.getLength());
				System.out.println(message);
			}catch(Exception x){
				System.out.println(x);
			}
		}
	}
}