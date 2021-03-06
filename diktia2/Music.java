import java.net.*;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.io.*;
import java.util.*;
import java.lang.System;
import java.io.BufferedReader;
import java.util.Scanner;

import javax.sound.sampled.*;
import javax.sound.sampled.AudioFileFormat.Type;

public class Music {
	public static void main(String[] args)throws Exception{
		int Client_listening_port=48030;
		int Server_listening_port=38030;
		byte[] hostIP={(byte)155,(byte)207,18,(byte)208};
		InetAddress serverAddress=InetAddress.getByAddress(hostIP);
		String Sound_request_code="A6377";
		DatagramSocket sendSocket=new DatagramSocket();
		DatagramSocket receiveSocket=new DatagramSocket(Client_listening_port);
		receiveSocket.setSoTimeout(10000);
        byte[] receiveBuffer = new byte[4096];
		System.out.println("Choose 1 for DPCM 2 for AQ-DPCM\n");
		Scanner userInput=new Scanner(System.in);
		String echo_preference=userInput.nextLine();
		int preference=Integer.parseInt(echo_preference);
		int number_of_packets=999;//giati 999???
		int song;
		String song_number;
		if(preference==1){
			song=22;
			song_number=song < 10 ? "0" + song : song +"";
			System.out.println(song_number);
			try{
				playmusic(fetchDPCM(Sound_request_code +"L"+song_number+"T"+number_of_packets,number_of_packets , sendSocket,receiveSocket,Server_listening_port,serverAddress),8);
			}catch(LineUnavailableException e){
				e.printStackTrace();
			}
		}
		if(preference==2){
			song=15;
			song_number=song<10 ? "0"+song : song +"";
			try{
				playmusic( fetchaqdpcm( Sound_request_code + "AQL" + song_number + "F" + number_of_packets, number_of_packets, sendSocket, receiveSocket, Server_listening_port, serverAddress ), 16 );
			}catch (Exception e){
				e.printStackTrace();
			}
		}
	}
	/*public void playmusic(byte[] audio,int Q)throws LineUnavailableException{
		AudioInputStream source;
		AudioFormat linearPCM=new AudioFormat(8000,Q,1,true,false);
		SourceDataLine lineOut=AudioSystem.getSourceDataLine(linearPCM);
		lineOut.open(linearPCM,32000);
		lineOut.start();
		lineOut.write(audio,0,audio.length);
		lineOut.stop();
		lineOut.close();
	}
	public byte[] fetchDPCM(String code,int number_of_packets,DatagramSocket sendSocket ,DatagramSocket receiveSocket ,int Server_listening_port ,InetAddress serverAddress){
		byte[] rxbuffer ,txbuffer;
		DatagramPacket p,q;
		rxbuffer=new byte[128];
		q=new DatagramPacket(rxbuffer,rxbuffer.length);
		bytep[] txBuffer=code.getBytes();
		p=new DatagramPacket(txBuffer,txBuffer.length,serverAddress,Server_listening_port);
		byte[] buff=new byte[128*2];
		byte[] audio=new byte[number_of_packets*2*128];
		int counter=0;
		int nibble=0;
		try{
			PrintWriter pw=null;
			try{
				pw=new PrintWriter(new File("SessionMusicDPCM.csv"));
			}catch(FileNotFoundException e){
				e.printStackTrace();
			}
			StringBuilder sb=new StringBuilder();
			sb.append("timi diaforas x1");
			sb.append(",");
			sb.append("timi diaforas x2");
			sb.append(",");
			sb.append("timi deigmatos x1");
			sb.append(",");
			sb.append("timi deigmatos x2");
			ab.append("\n");
			sendSocket.send(p);
			for(int j=0;j<number_of_packets;j++){
				try{
					receiveSocket.receive(q);
					buff=q.getData();
					for(int i=0;i<128;i++){
						int X2=buff[i] & 0x0f;
						int X1=(buff[i] >> 4) & 0x0f;
						sb.append(X1);
						sb.append(",");
						sb.appned(X2);
						sb.append(",");
						X1-=8;
						X2-=8;
						if(X1>16 || X1<-16 || X2>16 || X2<-16){
							System.out.println("kati paei polu strava me ta prwta x");
						}
						X1+=nibble;
						if(X1>127)X1=127;
						if(X1<-128)X1=-128;
						X2+=X1;
						if(X2>127)X2=127;
						if(X2<-128)X2=-128;
						sb.append(X1);
						sb.append(",");
						sb.append(X2);
						sb.append(",");
						sb.append("\n");
						nibble=X2;
						byte x1=(byte)X1;
						byte x2=(byte)X2;
						audio[counter++]=x1;
						audio[counter++]=x2;
					}
				}catch(Exception x){
					break;
				}
			}
			pw.write(sb.toString());
			pw.close();
		}catch(Exception x){
			System.out.println("ERROR while fetching music");
			System.exit(1);
		}
		return audio;
	}*/

	public static void playmusic( byte[] audio, int Q ) throws LineUnavailableException{
        AudioInputStream source;
        AudioFormat linearPCM = new AudioFormat( 16000, Q, 1, true, false );
        SourceDataLine lineOut = AudioSystem.getSourceDataLine( linearPCM );
        lineOut.open( linearPCM, 32000 );
        lineOut.start();
        lineOut.write( audio, 0, audio.length );
        lineOut.stop();
        lineOut.close();
	}
	
	   public static byte[] fetchDPCM(String code, int number_of_packets, DatagramSocket sendSocket, DatagramSocket receiveSocket, int Server_listening_port, InetAddress serverAddress ){
		    byte[] rxbuffer, txbuffer;
	        DatagramPacket p, q;
	    	rxbuffer = new byte[ 128 ]; //packetsize
	        q = new DatagramPacket( rxbuffer, rxbuffer.length );
		    
	        byte[] txBuffer = code.getBytes();
	        p = new DatagramPacket( txBuffer, txBuffer.length, serverAddress, Server_listening_port ); 
	        byte[] buff = new byte[ 128 * 2 ];
	        byte[] audio = new byte[ number_of_packets * 2 * 128 ];
	        int counter = 0;
	        int nibble = 0;
	        try{
	        	PrintWriter pw = null;
		        try {
		        	pw = new PrintWriter(new File("SessionMusicDPCM.csv")); 
		        	} catch (FileNotFoundException e) {
		        	e.printStackTrace(); 
		        }

		        StringBuilder sb = new StringBuilder();
	            sb.append("timh diaforas x1");
	            sb.append(",");
	            sb.append("timh diaforas x2");
	            sb.append(",");
	            sb.append("timh deigmatos x1");
	            sb.append(",");
	            sb.append("timh deigmatos x2");
	            sb.append("\n");
	            
	            sendSocket.send( p );
	            for( int j = 0; j < number_of_packets; ++j ){
	                try{
	                    receiveSocket.receive(q );
	                    buff = q.getData();
	                    for( int i = 0; i < 128; i++ ){
	                        int X2 = buff[ i ] & 0x0f;
	                        int X1 = ( buff[ i ] >> 4 ) & 0x0f;
	                        sb.append(X1);
	                        sb.append(",");
	                        sb.append(X2);
	                        sb.append(",");
	                        
	                        X1 -= 8;  //afairw to 8
	                        X2 -= 8;
	                        
	                        if(X1>16 || X1<-16 || X2>16 || X2<-16 ){
	                        	System.out.println("kati paei strava  me ta prwta x");
	                        }

	                        X1 += nibble;
	                  
	                        if( X1 > 127 ){
	                            X1 = 127;
	                        }
	                        if( X1 < -128 ){
	                            X1 = -128;
	                        }
	                        X2 += X1;
	 
	                        if( X2 > 127 ){
	                            X2 = 127;
	                        }
	                        if( X2 < -128 ){
	                            X2 = -128;
	                        }
	                        
	                        sb.append(X1);
	                        sb.append(",");
	                        sb.append(X2);
	                        sb.append(",");
	                        sb.append("\n");
	                        
	                        nibble = X2;

	                        byte x1 = ( byte ) X1;
	                        byte x2 = ( byte ) X2;

	                        audio[ counter++ ] = x1;
	                        audio[ counter++ ] = x2;

	                    }
	                }
	                catch( Exception x ){
	                    break;
	                }
	            }
	            pw.write(sb.toString());
	 	        pw.close();
	        }
	        catch( Exception x ){
	            System.out.println( "Error while fetching music: " + x );
	        }
	       
	        return audio;
	    }
	    
	    public static byte[] fetchaqdpcm(String code, int number_of_packets, DatagramSocket sendSocket, DatagramSocket receiveSocket, int Server_listening_port, InetAddress serverAddress ){
		    byte[] rxbuffer, txbuffer;
	        DatagramPacket p, q;
	    	rxbuffer = new byte[ 128 ]; //packetsize
	        q = new DatagramPacket( rxbuffer, rxbuffer.length );

	        byte[] txBuffer = code.getBytes();
	        p = new DatagramPacket( txBuffer, txBuffer.length, serverAddress, Server_listening_port ); 
	        
	        byte[] buff = new byte[ 132 * 2 ];
	        byte[] audio = new byte[ number_of_packets * 4 * 128 ];
	        int counter = 0;
	        int nibble = 0;
	        try{
	        	PrintWriter pw = null;
		        try {
		        	pw = new PrintWriter(new File("SessionMusicAQDPCM.csv")); //apothikefsh xronwn apokrishs se ena arxio .csv
		        	} catch (FileNotFoundException e) {
		        	e.printStackTrace(); //se periptwsh tyxon sfalmatos me th dhmiourgia arxeioy mpainei
		        }

		        StringBuilder sb = new StringBuilder();
	            sb.append("m");
	            sb.append(",");
	            sb.append("b");
	            sb.append("\n");
	            sendSocket.send( p );
	            for( int j = 0; j < number_of_packets; ++j ){
	                try{
	                    receiveSocket.receive(q );
	                    buff = q.getData();

	                    byte[] bb = new byte[ 4 ];
	                    byte sign = (byte)( ( buff[ 1 ] & 0x80 ) != 0 ? 0xff : 0x00 );
	                    bb[ 3 ] = sign;
	                    bb[ 2 ] = sign;
	                    bb[ 1 ] = buff[ 1 ];
	                    bb[ 0 ] = buff[ 0 ];

	                    int m = ByteBuffer.wrap( bb ).order( ByteOrder.LITTLE_ENDIAN ).getInt();


	                    sign = (byte)( ( buff[ 3 ] & 0x80 ) != 0 ? 0xff : 0x00 );
	                    bb[ 3 ] = sign;
	                    bb[ 2 ] = sign;
	                    bb[ 1 ] = buff[ 3 ];
	                    bb[ 0 ] = buff[ 2 ];

	                    int b = ByteBuffer.wrap( bb ).order( ByteOrder.LITTLE_ENDIAN ).getInt();
	                    
	                    sb.append(m);
	                    sb.append(",");
	                    sb.append(b);
	                    sb.append("\n");
	                    
	                    for( int i = 4; i < 132; ++i ){
	                        int D1 = ( buff[ i ] >>> 4 ) & 0x0f;
	                        int D2 = buff[ i ] & 0x0f;

	                       /* sb.append(D1);
	                        sb.append(",");
	                        sb.append(D2);
	                        sb.append(",");
	                        */
	                        int d1 = D1 - 8;
	                        int d2 = D2 - 8;

	                        int delta1 = d1 * b;
	                        int delta2 = d2 * b;

	                        int X1 = delta1 + nibble;
	                        int X2 = delta2 + delta1;

	                        nibble = delta2;

	                        int x1 = X1 + m;
	                        int x2 = X2 + m;
	                        
	                        sb.append(x1);
	                        sb.append(",");
	                        sb.append(x2);
	                        sb.append(",");
	                        sb.append("\n");
	                        audio[ counter++ ] = ( byte ) ( x1 );
	                        audio[ counter++ ] = ( byte ) ( x1 / 256 > 127 ? 127 : x1 / 256 < -128 ? -128 : x1 / 256 );
	                        audio[ counter++ ] = ( byte ) ( x2 );
	                        audio[ counter++ ] = ( byte ) ( x2 / 256 > 127 ? 127 : x2 / 256 < -128 ? -128 : x2 / 256 );
	                    }

	                }
	                catch( Exception x ){
	                    break;
	                }
	            }
	            pw.write(sb.toString());
	 	        pw.close();
	        }
	        catch( Exception x ){
	            System.out.println( "Error while fetching AQ-DPCM: " + x );
	        }
	        return audio;
	    }
	}
