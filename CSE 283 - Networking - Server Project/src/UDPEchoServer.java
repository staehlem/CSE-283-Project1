import java.net.*;  // for DatagramSocket, DatagramPacket, and InetAddress
import java.io.*;   // for IOException

public class UDPEchoServer {

  private static final String MAGIC_STRING = "--------MagicStringCSE283Miami";
	
  private static final int ECHOMAX = 65535;  // Maximum size of echo datagram

  public static void main(String[] args) throws IOException {

    int servPort = Integer.parseInt(args[0]);

    DatagramSocket socket = new DatagramSocket(servPort);
    DatagramPacket packet = new DatagramPacket(new byte[ECHOMAX], ECHOMAX);
    //FileOutputStream fos = new FileOutputStream("test.txt");
    
    for (;;) {  // Run forever, receiving and echoing datagrams
      socket.receive(packet);     // Receive packet from client
      byte[] data = packet.getData();
      System.out.println("Handling client at " +
        packet.getAddress().getHostAddress() + " on port " + packet.getPort());
      
      String nameOfUDPFile = new String(data, 0, packet.getLength()); //first line should be file name
      
      System.out.println("Received " + nameOfUDPFile); //ensure correctly identified file name
      
      FileOutputStream fos = new FileOutputStream(nameOfUDPFile.trim()); //would be nameOf...   .trim()
      
      //while(packet.getLength() != 0) {
    	  System.out.println("Start reading from packet");
	      while(true) {
	    	  socket.receive(packet);
	    	  data = packet.getData();
	    	  String magicCheck = new String(data, 0, packet.getLength());
	    	  int index = magicCheck.indexOf(MAGIC_STRING);
	    	  if(index != -1) {
	    		  //fos.write(packet.getData(), 0, index);		// write to file, everything should now be content
	    		  fos.flush();
	    		  fos.close();
	    		  break;
	    	  }
	    	  fos.write(packet.getData(), 0, packet.getLength());
	    	  fos.flush();
	      }
	      //System.out.println("End reading from packet");
      //}
      //System.out.println("All packets read...");
      fos.close();
      //  Don't want these because I'm not sending to a socket
      //socket.send(packet);       // Send the same packet back to client
      //packet.setLength(ECHOMAX); // Reset length to avoid shrinking buffer
    }
    /* NOT REACHED */
  }
  
//  private String writeFromUDP() {
//	  String tmp;
//	  while(!(packet.getData().equals(MAGIC_STRING))) {
//    	  //fos.write(packet.getData());		// write to file, everything should now be content
//		  tmp += new String(packet.get)
//	  }
//  }
}