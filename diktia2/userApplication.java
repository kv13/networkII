
import java.net.*;


import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.SourceDataLine;

import java.io.*;



/*s=server socket
  p=server packet
  r=client socket
  q=client packet*/
public class userApplication {

  public static void main(String[] args) throws IOException, LineUnavailableException{
    
      
    //echo(5);
      
    //pic(); 
      
    //sound1(); 
      
    sound2();
       
    //copter();
      
    //obd(5); 
      
  }
public static void echo(int min) throws IOException{
    
    int numofpack=0,numofpacks=0, count=0, faults=0 ;
    long  timereceived, start=0, i=0, runtime=min*60*1000;
    long trans[]= new long[400000];
    
    PrintStream transmission = null;
    PrintStream timepassed = null;
    
    
    FileWriter echofile = new FileWriter("echoPackets.csv");
    BufferedWriter output = new BufferedWriter(echofile);
    transmission = new PrintStream(new FileOutputStream("echoResponseTime.csv"));
    timepassed = new PrintStream(new FileOutputStream("RealTime.csv"));
    
    DatagramSocket s = new DatagramSocket();
    String packetInfo ="Ε9134";
    
    byte[] txbuffer = packetInfo.getBytes();
    int serverPort = 38030;
    
    
    
    InetAddress hostAddress = InetAddress.getByName("ithaki.eng.auth.gr");
    DatagramPacket p = new DatagramPacket(txbuffer,txbuffer.length, hostAddress,serverPort);
    s.send(p);
    
    
    int clientPort = 48030;
    DatagramSocket r = new DatagramSocket(clientPort);
    r.setSoTimeout(3000);
    byte[] rxbuffer = new byte[2048];
    DatagramPacket q = new DatagramPacket(rxbuffer,rxbuffer.length);
    
    
    start=System.currentTimeMillis();
    i=start;
     while(start-i<=runtime){
      try {
        
        r.receive(q);
        timereceived=System.currentTimeMillis();
        numofpack=numofpack+1;
        numofpacks+=1;
        trans[count++]=timereceived-i;
        String message = new String(rxbuffer,0,q.getLength());
        System.out.println(message);
        output.write(message,18,8);
        output.newLine();
        transmission.println(timereceived-i);
        timepassed.println((timereceived-start));
      }catch (Exception x) {
        System.out.println(x);
        faults+=1;
        break;
      }
      
        
        s.send(p);
        
        i=System.currentTimeMillis();
      
      
    }
     output.close();
     System.out.print("Number of Packages Received ");
     System.out.println(numofpacks);
     System.out.print("Number of faults Packages ");
     System.out.println(faults);
     transmission.close();
     timepassed.close();
     echofile.close();
    
     
    
      
    s.close();
    r.close();
    output.close();
    

  
  }

  public static void pic() {
    try {
      DatagramSocket s = new DatagramSocket();
      String packetInfo ="M8001CAM=PTZ";
      byte[] txbuffer = packetInfo.getBytes();
      int serverPort = 38018;
      
      try {
        InetAddress hostAddress = InetAddress.getByName("ithaki.eng.auth.gr");
        DatagramPacket p = new DatagramPacket(txbuffer,txbuffer.length, hostAddress,serverPort);
        s.send(p);
      }catch(Exception b){
        System.out.println(b);
      }
      
      int clientPort = 48018;
      DatagramSocket r = new DatagramSocket(clientPort);
      r.setSoTimeout(3000);
      byte[] rxbuffer = new byte[2048];
      DatagramPacket q = new DatagramPacket(rxbuffer,rxbuffer.length);
      
      File myFile;
      myFile = new File("image.jpg");
      FileOutputStream fout=null;
      
      try {
        fout = new FileOutputStream(myFile, false);
      }catch(Exception e) {
        System.out.println(e);
      }
        
          
      for(;;) { 
        try {
          r.receive(q);
          
          fout.write(rxbuffer,0,128);
          
        }catch(Exception e) {
          System.out.println(e);
          break;
        }
      }
      try {
        fout.flush();
        fout.close();
      }catch(Exception e) {
        System.out.println(e);
      }
      s.close();
      r.close();  
    }catch(SocketException a) {
      System.out.println(a);
    }
    
  }
    
  public static void sound1() throws IOException, LineUnavailableException{
    
    
    DatagramSocket s = new DatagramSocket();
    String packetInfo ="A4217T960"; //Y=T or F
    byte[] txbuffer = packetInfo.getBytes();
    int serverPort = 38018;
  
    InetAddress hostAddress = InetAddress.getByName("ithaki.eng.auth.gr");
    DatagramPacket p = new DatagramPacket(txbuffer,txbuffer.length, hostAddress,serverPort);
    s.send(p);
    
  
    int clientPort = 48018;
    DatagramSocket r = new DatagramSocket(clientPort);
    r.setSoTimeout(1000);
  
    AudioFormat FAudio = new AudioFormat(8000, 8, 1, true, false);
  
    byte[] rxbuffer = new byte[128];
    DatagramPacket q = new DatagramPacket(rxbuffer,rxbuffer.length);
    
    byte[] receivedSound=new byte[128*960];
    int[] samples=new int[256*960];
    int[] clip=new int[256*960];
    try {
    SourceDataLine file= AudioSystem.getSourceDataLine(FAudio); 
    
    int j=0;
    for(;;) {
        try {
          r.receive(q);         
          for(int i=0; i<128; i++) {  
            receivedSound[j*128+i]=rxbuffer[i]; 
          }
          j++;
        }catch(Exception e) {
          break;
        }
        
    }
    file.open(FAudio, 256*960); 
    
    for(int i=0; i<128*960; i++) {
      int k= (int)receivedSound[i];
      samples[2*i]=(((k>>4)&15)-8);
      samples[2*i+1]=((k&15)-8);
    }
    
    byte[] audioOut=new byte[256*960];
    
    for(int i=1; i<256*960; i++) {
      clip[i]=samples[i];
      audioOut[0]=(byte)(2*clip[0]);
    }
      for(int i=1; i<256*960; i++) {
        clip[i]=2*clip[i]+audioOut[i-1];
        audioOut[i]=(byte)clip[i];
      }
      PrintWriter Samples = new PrintWriter("SamplesDPCM.txt");
      PrintWriter SamplesDiffs = new PrintWriter("DiffsDPCM.txt");
      for(int i=0; i<256*960; i++){
        Samples.println(audioOut[i]);
        SamplesDiffs.println(samples[i]);
        }
    
    file.start();
    file.write(audioOut, 0, 256*960);
    file.stop();
    file.close();
    Samples.close();
    SamplesDiffs.close();
    }catch(Exception z) {
      System.out.println(z);
    }
    
  
  
  
  r.close();
  s.close();

  
}

  
  public static void sound2() throws IOException, LineUnavailableException {
    int cnt=0;
    int lsb = 0;
    int msb = 0;
    int[] mean = new int[960];
    int[] step = new int[960];
  

    
      DatagramSocket s = new DatagramSocket();
      String packetInfo ="A6377AQT960"; //Y=T or F
      byte[] txbuffer = packetInfo.getBytes();
      int serverPort = 38030;
      
      InetAddress hostAddress = InetAddress.getByName("ithaki.eng.auth.gr");
      DatagramPacket p = new DatagramPacket(txbuffer,txbuffer.length, hostAddress,serverPort);
      s.send(p);
      
      PrintWriter means = new PrintWriter("means.txt");
      PrintWriter steps = new PrintWriter("steps.txt");
    
      int clientPort = 48030;
      DatagramSocket r = new DatagramSocket(clientPort);
      r.setSoTimeout(1000);
    
      AudioFormat FAudio = new AudioFormat(8000, 16, 1, true, false);
    
      byte[] rxbuffer = new byte[132];
      DatagramPacket q = new DatagramPacket(rxbuffer,rxbuffer.length);
      
      byte[] receivedSound=new byte[132*960];
      int[] samples=new int[264*960];
      int[] clip=new int[264*960];
      
      SourceDataLine file= AudioSystem.getSourceDataLine(FAudio); 
      
      int j=0;
      for(;;) {
        try { 
            r.receive(q);         
            for(int i=0; i<132; i++) {  
              receivedSound[j*132+i]=rxbuffer[i]; 
            }
            j++;
        }catch(Exception a) {
          System.out.println(a);
          break;
        } 
      }
      file.open(FAudio, 264*960); 
      
      for (int i=0; i<960; i++) {
        lsb = (int)receivedSound[132*i];
        msb = (int)receivedSound[132*i + 1];
        mean[i] = (256 * msb) + (lsb & 0x00FF);
        lsb = (int)receivedSound[132*i + 2];
        msb = (int)receivedSound[132*i + 3];
        step[i] = (256 * (msb & 0x00FF)) + (lsb & 0x00FF);
        }
      
      for(int i=0; i<960; i++) {
        means.println(mean[i]);
        steps.println(step[i]);
        }
      
      for (int i=0; i<960; i++) {
        for(j=4; j<132; j++) {
        int k = (int)receivedSound[i*132 + j];
        samples[2*cnt] = (((k >> 4) & 15) - 8)*step[i];
        samples[2*cnt + 1] = ((k & 15) - 8)*step[i];
        cnt++;
        }
        }
      
        byte[] audioOut=new byte[512*960];
        
        for(int i=1; i<256*960; i++) {
          clip[i] = samples[i];
        }
        
        for(int i=0; i<960; i++) {
          for(j=0; j<256; j++) {
          if(i==0 && j==0) continue;
          clip[i*256 + j] = clip[i*256+j] + clip[i*256+j-1];
          }
          }
        
        for(int i=0; i<256*960; i++){
          audioOut[2*i] = (byte)(clip[i] & 0xFF);
          audioOut[2*i + 1] = (byte)((clip[i] >> 8) & 0xFF);
          }
          
        PrintWriter Samples = new PrintWriter("SamplesAQDPCM.txt");
        PrintWriter SamplesDiffs = new PrintWriter("DiffsAQDPCM.txt");
        for(int i=0; i<256*960; i++){
          Samples.println(audioOut[i]);
          SamplesDiffs.println(samples[i]);
          }
      
      file.start();
      file.write(audioOut, 0, audioOut.length);
      file.stop();
      file.close();
      Samples.close();
      SamplesDiffs.close();
      means.close();
      steps.close();
      
    
    
    
    r.close();
    s.close();
  
    
  }

public static void copter() throws IOException {  
  int portNumber = 38048;
  byte[] addressArray = { (byte)155, (byte)207, 18, (byte)208 };
  InetAddress hostAddress = InetAddress.getByAddress(addressArray);
  PrintWriter copter = new PrintWriter("copter.txt");
  Socket send = new Socket(hostAddress, portNumber);
  send.setSoTimeout(1000);
  InputStream in = send.getInputStream();
  OutputStream out = send.getOutputStream();
  for(;;) {
  try {
  out.write("AUTO FLIGHTLEVEL4050 LMOTOR=135 RMOTOR=135 PILOT\r\n".getBytes());
  int k = in.read();
  System.out.print((char)k);
  copter.print((char) k);
  } catch(Exception x) {
  System.out.println(x);
  break;
  }
  }
  send.close();
  copter.close();
  }


public static void obd(int min) throws IOException{
  
  int clientPort = 38016; 
  DatagramSocket r = new DatagramSocket(clientPort); //=rxsoc 
  
  DatagramSocket s = new DatagramSocket();  //=txsoc
  byte[] rxbuffer = new byte[2048];
  DatagramPacket q = new DatagramPacket(rxbuffer,rxbuffer.length); //=rpack
  int serverPort = 48016, c=0;
  
  String packetInfo="V9515";
  
  
  String str="";
  
  
  long start=System.currentTimeMillis();
  while(System.currentTimeMillis()-start<(min*60000)+20){
    
    
    
    for(c=0;c<6;c++){
      
            
      switch(c) {
      
        case 0: str= packetInfo+"OBD=01 1F"; 
        

        break;  //run time
          


        case 1: str= packetInfo+"OBD=01 0F"; break; //Air Temp
        case 2: str= packetInfo+"OBD=01 11"; break; //Throttle
        case 3: str= packetInfo+"OBD=01 0C"; break; //RPM
        case 4: str= packetInfo+"OBD=01 0D"; break; //Speed
        case 5: str= packetInfo+"OBD=01 05"; break; //Coolant Temp
      } 
      
      byte[] txbuffer = str.getBytes();
      
      
      InetAddress hostAddress = InetAddress.getByName("ithaki.eng.auth.gr");
      DatagramPacket p = new DatagramPacket(txbuffer,txbuffer.length, hostAddress,serverPort);
      s.send(p);
  
      
        System.out.println("edw paei");
        r.receive(q);
        System.out.println("edw uphrxe thema");



    int value;
    String hexData1 = new String(rxbuffer,6,2);
    int decData1 = Integer.parseInt(hexData1,16);
    String hexData2 = "";
    int decData2=0;
    if(q.getLength()>9){
      hexData2 = new String(rxbuffer,9,2);
      decData2 = Integer.parseInt(hexData2,16);
    }
    
    switch(c){
      case 0: value=256*decData1+decData2;      

          System.out.println("\nruntime: "+value); 
          PrintWriter runtime = new PrintWriter("runtime.csv");
          runtime.println(value);
          break;                  

      case 1: value=decData1-40;
          System.out.println("air temp: "+value); 
          PrintWriter air_temp = new PrintWriter("air_temp.csv");
          air_temp.println(value);
          break;
      case 2: value=decData1*100/255;
          System.out.println("throttle: "+value); 
          PrintWriter throttle = new PrintWriter("throttle.csv");
          throttle.println(value);
          break;
      case 3: value=((decData1*256)+decData2)/4;
          System.out.println("RPM: "+value); 
          PrintWriter RPM = new PrintWriter("RPM.csv");
          RPM.println(value);
          break;
      case 4: value=decData1;
          System.out.println("Speed: "+value); 
          PrintWriter speed = new PrintWriter("speed.csv");
          speed.println(value);
          break;
      case 5: value=decData1-40;
          System.out.println("coolant temp: "+value); 
          PrintWriter coolant_temp = new PrintWriter("coolant_temp.csv");
          coolant_temp.println(value);
          break;
    } 
  }
    r.close();
      s.close();
  } 
}

}