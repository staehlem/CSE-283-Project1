import java.net.*; // for Socket, ServerSocket, and InetAddress
import java.io.*; // for IOException and Input/OutputStream
import java.nio.file.*;
import java.nio.charset.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

//need to replace file input and output stream from the zip server with socket stuff
//hard code in proj2.bin in that file name spot line 31 in the zip server file
//client side is webserver.java, must write to file system and socket, so you send 
//the same bytes to the compression server as you do the file system
//inputstream reads and outputstream writes

//already have in and out stream and oher
//use socket in and out stream instead of file input and output stream
//dest will say out 
//remember to flush buffer in correct order and combination multiple streams and buffers make this difficult
//
//
/**
 * Here is an example for InputStreamReader
 * 
 * BufferedReader br = new BufferedReader(new InputStreamReader(is,
 * "US-ASCII"));
 * 
 * Here is an example when reading string objects from buffers
 * 
 * String msg = new String(buffer, total, rcv, "US-ASCII");
 * 
 * If you do not explicitly set the encoding, then your images will get cut off
 * in the VM.
 * 
 * @author Erich
 *
 */

// everytime someone connects it makes input and output
//

public class CompressionServer {

	private static final int BUFSIZE = 1024; // Size of receive buffer
	private static final int ECHOMAX = 65535; // Maximum size of echo datagram
	private static final String MAGIC_STRING = "--------MagicStringCSE283Miami";


	public static void main(String[] args) throws IOException {

		int servPort = 5000;

		InetAddress serverAddress = InetAddress.getByName("localhost"); // Server
																		// address
		// File name must update to account for multiple files that can be sent in unison, pretty sure this sholdn't go here
//		String filename = "text.txt";
//		byte[] buf = filename.getBytes();

		// these are the udp sockets
		DatagramSocket socket = new DatagramSocket(servPort);
		DatagramPacket packet = new DatagramPacket(new byte[ECHOMAX], ECHOMAX);
		
		int recvMsgSize; // Size of received message
		byte[] buffer = new byte[BUFSIZE]; // Receive buffer

		for (;;) { // Run forever, accepting and servicing connections
			// Socket clntSock = servSock.accept(); // Get client connection
			System.out.println("I made it to line 66, im in the loop");
			/**
			 * socket.receive(packet);
			 * stuff stuff stuff stuff more stuff
			 */
			socket.receive(packet);     // Receive *FIRST* packet from client
		    byte[] data = packet.getData();
			
		    String nameOfUDPFile = new String(data, 0, packet.getLength()); //*FIRST PACKET* should be file name
		      
		      System.out.println("Received " + nameOfUDPFile); //ensure correctly identified file name, if this part works we know that we have implemented the sending and receiving at least somewhat correctly
		      
		      FileOutputStream fos = new FileOutputStream(nameOfUDPFile.trim()); //would be nameOf...   .trim()
		      FileOutputStream dest = new FileOutputStream(nameOfUDPFile.trim()+".zip");	//the thing that goes in the parenthesis is the name of the file I believe.
		      ZipOutputStream out = new ZipOutputStream(new BufferedOutputStream(dest));	// so now I have a zip output stream to write to the file system with and I think dest tell it where I want it
		      
		      // Create some sort of zip entry thing that I guess names the file which is what I thought the output stream did but I guess not
		      ZipEntry entry = new ZipEntry(nameOfUDPFile.trim()+".zip");
	          out.putNextEntry(entry);
	          
	          
		      //while(packet.getLength() != 0) {
		    	  System.out.println("Start reading from packet");
			      while(true) {
			    	  socket.receive(packet);
			    	  data = packet.getData();
			    	  String magicCheck = new String(data, 0, packet.getLength());
			    	  int index = magicCheck.indexOf(MAGIC_STRING);
			    	  if(index != -1) {
			    		  fos.flush();
				    	  out.close();
			    		  fos.close();
			    		  break;
			    		 
			    	  }
			    	  //fos.write(packet.getData(), 0, index);		// write to file, everything should now be content
		    		  fos.write(packet.getData(), 0, packet.getLength());
		    		  out.write(packet.getData(), 0, packet.getLength());
			    	  fos.flush();	//It is possible that I may not need this in my code, as I am already writing so then what is the point of flushing...?
			    	  
			      }
			      System.out.println("End reading from packet");
		      //}
		      //System.out.println("All packets read...");
		      fos.close();
			
		      
		      
		      
		      
			
//			String nameOfFileThatWeWantToUse = "";
//			FileOutputStream fos = new FileOutputStream(nameOfFileThatWeWantToUse);
//			
//			int startOfFileIndex = 0;	// start where we find the file name
//			int endOfFileIndex = 0;  // end where we find the magic string?
//			fos.write(buffer, startOfFileIndex, endOfFileIndex); // use file out stream to write to file first
			
			
			
			
			
			
			
			
			
			
			
			
			
			
			
			
			
			
			
			
			

			/*
			 * need to accept data from datagramsocket
			 */

			// System.out.println("Handling client at " +
			// clntSock.getInetAddress().getHostAddress() + " on port " +
			// clntSock.getPort());

			// InputStream inSock = clntSock.getInputStream();
			// OutputStream outSock = clntSock.getOutputStream();

			/**
			 * 
			 * now that we have this------------- read from zip input stream
			 * 
			 */

			// Receive until client closes connection, indicated by -1 return
			//BufferedInputStream origin = null;

			// // Create a file output stream
			// FileOutputStream dest = new
			// FileOutputStream(fileOutput);
			//

			// any output stream will work so you have the
//			ZipOutputStream outZip = new ZipOutputStream(
//					new BufferedOutputStream(outSock));

			// out.setMethod(ZipOutputStream.DEFLATED);
			//byte data[] = new byte[BUFSIZE];

			// // get a list of files from current directory
			// FileInputStream fi = new
			// FileInputStream(fileInput);
			//
//			origin = new BufferedInputStream(inSock, BUFSIZE);
//
//			ZipEntry entry = new ZipEntry("proj2.bin");		// this must change to be the name of the file.whateverItIs.zip
//			outZip.putNextEntry(entry);
//			int count;
//			while ((count = origin.read(data, 0, BUFSIZE)) != -1) {
//				outZip.write(data, 0, count);
//				outZip.flush();
//				// outSock.write(arg0);
//				// :)
//				// outSock.flush();
//				// flush zipoutstream and flush your socket outputstream
//				// need to flush because the system waits for the buffer to be
//				// filled
//
//			}
//
//			outZip.close();
//			clntSock.close(); // Close the socket. We are done with this client!
			// servSock.close();
		}
		/* NOT REACHED */
	}
}