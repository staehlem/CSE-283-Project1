import java.io.*;
import java.net.*;
import java.security.KeyStore;
import java.util.*;

import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLServerSocket;
import javax.net.ssl.SSLServerSocketFactory;
import javax.net.ssl.SSLSocket;

/**
 * 
 *  -------------AN IMPORTANT MSG FOR THE USER---------------------------------
 * 
 * The name you input in the text box that appears will be ignored by the current program.
 * In order to properly access your zipped file you must simply look for the exact name of 
 * the file you choose to upload and make sure it is the one with ".zip" at the end.
 * 
 * That is your zipped file, simply rename if applicable and treat it as any other zip file 
 * 
 * 
 * @author Erich M Staehling
 *
 */

//need to replace file input and output stream from the zip server with socket stuff
//hard code in proj2.bin in that file name spot line 31 in the zip server file
//client side is webserver.java, must write to file system and socket, so you send 
//the same bytes to the compression server as you do the file system
//inputstream reads and outputstream writes
//create sockect, write bytes to socket, read bytes from socket, write compressed bytes to socket system


final class HttpRequest implements Runnable {
	final static int BUF_SIZE = 1024000;
	final static String CRLF = "\r\n";
	private static final int TIMEOUT = 3000;   // Resend timeout (milliseconds)
	private static final String MAGIC_STRING = "--------MagicStringCSE283Miami";


	byte[] buffer;
	Socket socket;
	

	// Constructor
	public HttpRequest(Socket socket) throws Exception {
		this.socket = socket;
		buffer = new byte[BUF_SIZE];
	}

	// Implement the run() method of the Runnable interface.
	public void run() {
		try {
			processRequest();
		} catch (Exception e) {
			System.out.println(e);
		} 
	}

	private int getContent() throws Exception {
		int total = 0, rcv = 0;
		
		while (rcv != -1) {
			rcv = socket.getInputStream().read(buffer, total,
				BUF_SIZE - total - 1);
			String msg = new String(buffer, total, rcv, "US-ASCII");
			System.out.println(msg);
			total += rcv;
			
			// Only loop if it is not a GET message and have not reached
			// end of POST message, Upload+CRLF represents end of request
			if (msg.startsWith("GET") || msg.indexOf("Upload"+CRLF) != -1) {
				System.out.println("EXITING");
				break;
			}
		}
		// returns the total bytes in the buffer
		return total;
	}

	private void processRequest() throws Exception {

		int total = getContent();

		// Get a reference to the socket's input and output streams.
		// InputStream is = socket.getInputStream();
		InputStream is = new ByteArrayInputStream(buffer, 0, total);   // this makes buffer into a byte array
		DataOutputStream os = new DataOutputStream(socket.getOutputStream());
		

		// Set up input stream filters.
		// BufferedReader br = new BufferedReader(new InputStreamReader(is));
		BufferedReader br = new BufferedReader(new InputStreamReader(is, "US-ASCII"));     //we have this so we can use readline, now we can read stuff

		// Get the request line of the HTTP request message.
		String requestLine = br.readLine();

		// Extract the filename from the request line.
		StringTokenizer tokens = new StringTokenizer(requestLine);
		String method = tokens.nextToken(); // skip over the method, which
											// should be "GET"
		
		//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
		/**
		 * This is where the code begins and where I need to do all the work. This needs to sense the post then get the content-length which it does, and the  
		 * new file name, which it doesn't yet, then it needs t find a way to get the content of the file that I actually want in byte form as they will not 
		 * always be strings. It also needs to save the info it gets to a file with the new file name and then send that to a compression server as well.
		 * 
		 * 
		 */
		//Do different things if the request is a POST
		if(method.length() > 3) { //if not GET which is length 3 it must be POST
			//int i;
			
			//is.reset();
			
			//lets get the filename here ***********
			//make a for loop like the one below and use br.reset, 
			//though doing that might make you 'up' the other #s in the loop
			//for(i = 0, i )
			
			/**
			 * read line until first blank line, then the second blank line, the ------WebKitformat
			   to get the file name read lines until the line above the blank line above the file name then 
			   use begining and end index in byte value for the buffer (which has everything) *** out.write(buffer, begin, end-begin)
			   how to get the amount of bytes read: when you read line you want to keep track of how many chars in the line (so the length of the line) + CRLF as each char counts as one byte in 'buffer'

			   when you read line it strips out the carriage return line feed, so length of line in these blank 
			   each char of string is one byte 
			   int idx = line.length() + CRLF.length();		idx stands for index and you must add CRLF because readline takes it off, this allows you to keep track of bytes read
			   buffered reader takes bytes into strings, do not multiply by 2 unless it is in UTF-8
			   begin starts after the first blank line
			   
			 */
			
			//FileOutputStream fos = new FileOutputStream();  // this is where you add the new file name the one at the bottom of the file

			
			
	// there is a second out that is supposed to send to the server for part C, look up 
	// the TCP echo client for how to do this
			
			
			
			
			int currIndex = requestLine.length() + CRLF.length();
			int beginIndexOfContent = 0;
			int endIndexOfContent = 0;
			String line = br.readLine();
			String newFileName = null;
			currIndex += line.length() + CRLF.length();
			
			while(line.length() != 0) {
				line = br.readLine();
				currIndex += line.length() + CRLF.length();
			}
//			System.out.println();
//			System.out.println();
//			System.out.println(currIndex);
			
			line = br.readLine();
			currIndex += line.length() + CRLF.length();
			while(line.length() != 0) {
				line = br.readLine();
				currIndex += line.length() + CRLF.length();
			}
			
//			System.out.println();
//			System.out.println();
//			System.out.println(currIndex);
			
			beginIndexOfContent = currIndex;// + CRLF.length();
			//System.out.println("*************************************************************begining index of the content we want: " + beginIndexOfContent);
			line = br.readLine();
			while(!(line.startsWith("------WebKitFormBoundary"))) {
				currIndex += line.length() + CRLF.length();
				line = br.readLine();
				System.out.println();
				System.out.println(line);
				System.out.println();
			}
			
			endIndexOfContent = currIndex;
			System.out.println("*************************************************************ending index of the content we want: " + endIndexOfContent);
			
			
//			while(!(line.endsWith("name=\"destination\""))) {
//				line = br.readLine();
//			}
//			
//			while(!(line.endsWith("name=\"destination\""))) {
//				line = br.readLine();
//			}
//			br.readLine();
//			newFileName = br.readLine();
			br.readLine();
			br.readLine();
			newFileName = br.readLine();
			
			System.out.println("****************OUR NEW FILE NAME IS " + newFileName);
			
//			System.out.println();
//			System.out.println();
//			System.out.println(newFileName);
//			
//			System.out.println();
//			System.out.println();
//			System.out.println(beginIndexOfContent);
//			
//			System.out.println();
//			System.out.println();      '
			
			
//			System.out.println(endIndexOfContent);
			
			
			// The two lines below were used for project 2
			//FileOutputStream fos = new FileOutputStream(newFileName + ".zip");
			//Socket socketC = new Socket("127.0.0.1", 5000);
			//DatagramSocket UDPSocket = new DatagramSocket();
			
//			if ((args.length < 2) || (args.length > 3))  // Test for correct # of args
//			      throw new IllegalArgumentException("Parameter(s): <Server> <Word> [<Port>]");

			    InetAddress serverAddress = InetAddress.getByName("localhost");  // Server address
			    // Convert input String to bytes using the default character encoding
			    byte[] bytesToSend = newFileName.getBytes();	// this is the file name which is found the same way that it was in project 2

			    //int servPort = (args.length == 3) ? Integer.parseInt(args[2]) : 7;

			    DatagramSocket socket = new DatagramSocket();

			    socket.setSoTimeout(TIMEOUT);  // Maximum receive blocking time (milliseconds)
			
			DatagramPacket sendPacket = new DatagramPacket(bytesToSend,  // Sending packet
			        bytesToSend.length, serverAddress, 5000);

			//sendPacket. do someting with this
			
			//while(true) {
				
			
				byte[] FileNameBuf = newFileName.getBytes();
				sendPacket.setData(FileNameBuf, 0, FileNameBuf.length); // offset here should always be 0 since it is at the start of the file to be sent...  Do you use the buffer that I just created??
				// 
				// create file, look at zip.java, 
				// 
				// filename1
				// filename+".zip"
				// 
				// 
				System.out.println("******************************************************** file name length: " + newFileName.length());
				System.out.println("these should be the same ?");
				System.out.println("******************************************************** file name BUFFER length: " + FileNameBuf.length);

				socket.send(sendPacket);
				int sendLength = 0;
				int offset = beginIndexOfContent; // if we must send the file name first above then it should always be that the offset starts at the length of the file name... Do you use the buffer that I just created??
				System.out.println("############################################### offset length: " + offset);
				int amountToBeSent = endIndexOfContent - beginIndexOfContent;
				int amountSent = 0;
				System.out.println("the amount to be sent: " + amountToBeSent);
				// if my offset plus 1024 is going to be a problem you must do something, must check before the issue occurs, must predict issues in the code and deal with them
				while(true) {	//true
								//!(line.startsWith("------WebKitFormBoundary"))
					if(amountSent + 1024 >= amountToBeSent) {
						sendLength = amountToBeSent - amountSent;
						System.out.println("I changed stuff at line 249");
						System.out.println(sendLength);
						sendPacket.setData(buffer, offset, sendLength);
						//sendPacket.setData(buffer, offset, sendLength);
						amountSent += sendLength;
					} 
					else {
						sendLength = 1024;
						sendPacket.setData(buffer, offset, sendLength);  // - 2*beginIndexOfContent);
						amountSent += sendLength;
					}
					
					//sendPacket.setData(buffer, offset, sendLength - beginIndexOfContent);
					
					
					
					socket.send(sendPacket);
					System.out.println("take a 50 nap");
					Thread.sleep(50);
					System.out.println("wake up!");
					offset += sendLength;
					//sendLength = 0;
					
					if(amountSent >= amountToBeSent) {
						System.out.println("more things occured at line 259");
						System.out.println("SENT: " + amountSent);
						System.out.println("TO BE SENT: " + amountToBeSent);
						System.out.println("offset value at line 272: " + offset);
						System.out.println("amount to be sent " + amountToBeSent);
						break; 
					}
				}
				System.out.println("a file has completed sending...");
				
				//Here we send the magic string and live happily ever after 
				byte[] magicStringAsData = MAGIC_STRING.getBytes();
				sendPacket.setData(magicStringAsData, 0, magicStringAsData.length);
				socket.send(sendPacket);    //We need to make send packet have the magic string in this case so we know where to stop
				
				
				
			
			//}
			
			
			
			
			//Input and output stream for sockets for project 2
			//InputStream inSock = socketC.getInputStream();
			//OutputStream outSock = socketC.getOutputStream();
			
			
			//this doesn't want to write, cause UDP 
			//socket.write(buffer, beginIndexOfContent, endIndexOfContent-beginIndexOfContent);
			
			//only TCP sockets need to be flushed
			//outSock.flush();   
			//socketC.shutdownOutput();
			
			
			
			
			//int bufferSizeForPartC = 1024;
			
			
			//byte[] buffer = new byte[bufferSizeForPartC];
//			int countForPartC = 0;
//			// Copy requested file into the socket's output stream.
//			while ((countForPartC = inSock.read(buffer)) != -1) {
//				fos.write(buffer, 0, countForPartC);
//				fos.flush();
//			}
			
			//fos.write(buffer, beginIndexOfContent, endIndexOfContent-beginIndexOfContent);
//			fos.close();
//			socketC.close();
			
			
			
			
			String statusLine = "HTTP/1.0 200 OK" + CRLF;
			String contentTypeLine = "Content-Type: text/html" + CRLF;
			String entityBody = "<HTML>" + "<HEAD><TITLE>Uploaded Successfully</TITLE></HEAD>"
					+ "<BODY>Uploaded Successfully</BODY></HTML>";
			
			// Send the status line.
			os.writeBytes(statusLine);

			// Send the content type line.
			os.writeBytes(contentTypeLine);

			// Send a blank line to indicate the end of the header lines.
			os.writeBytes(CRLF);
			
			//entity body
			os.writeBytes(entityBody);
			
			
			
			
			
			
			// the index in the string is the index in the buffer times 2 * NO LONGER TRUE *
			//System.out.println("I made it to this point!!");
			//BufferedWriter writer = new BufferedWriter(new FileWriter("TextFileOutput.txt"));
//			int contentLength = 0;
//			for(i = 0; i < 13; i++) {
//				System.out.println("*****************************************************************");
//				if(i == 2) {
//					String[] str = br.readLine().split(" ");
//					contentLength = Integer.parseInt(str[1]);
//					System.out.println(contentLength);
//				}
//				System.out.println("outside the if statement");
//				System.out.println(br.readLine());
//			}
//			
//			
//			br.readLine();
//			String line = br.readLine();
//			String entityBodyContentForTxtFiles = null;
//			//24 chars so 23 in index form
//			while(!(line.startsWith("------WebKitFormBoundary")))	{
//				//System.out.println("I made it to the part where i am 'not' at the webkit thing");
//				System.out.println(line);
//				entityBodyContentForTxtFiles += line + "\n";
//				//os.write(b);
//				line = br.readLine();
//			}
//			
//			//this has a null in it for some unknown reason
//			System.out.println("###################################################");
//			System.out.println(entityBodyContentForTxtFiles);
//			writer.write(entityBodyContentForTxtFiles);
//			System.out.println("###################################################");
		} else {		
//		
//		File enteredFileName = new File("test.txt");
//		FileOutputStream fos = new FileOutputStream(enteredFileName);
		
		
		//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
		
		
		String fileName = tokens.nextToken();

		// Prepend a "." so that file request is within the current directory.
		fileName = "." + fileName;

		// Open the requested file.
		FileInputStream fis = null;
		boolean fileExists = true;
		try {
			fis = new FileInputStream(fileName);
		} catch (FileNotFoundException e) {
			fileExists = false;
		}
		
		// Construct the response message.
		String statusLine = null;
		String contentTypeLine = null;
		String entityBody = null;
		if (fileExists) {
			statusLine = "HTTP/1.0 200 OK" + CRLF;
			contentTypeLine = "Content-Type: " + contentType(fileName) + CRLF;
		} else {
			statusLine = "HTTP/1.0 404 Not Found" + CRLF;
			contentTypeLine = "Content-Type: text/html" + CRLF;
			entityBody = "<HTML>" + "<HEAD><TITLE>Not Found</TITLE></HEAD>"
					+ "<BODY>Not Found</BODY></HTML>";
		}
		// Send the status line.
		os.writeBytes(statusLine);

		// Send the content type line.
		os.writeBytes(contentTypeLine);

		// Send a blank line to indicate the end of the header lines.
		os.writeBytes(CRLF);

		// Send the entity body.
		if (fileExists) {
			sendBytes(fis, os);
			fis.close();
		} else {
			os.writeBytes(entityBody);
		}
		
		
		
		}
		
		// Close streams and socket.
		os.close();
		br.close();
		socket.close();
	}

	private static void sendBytes(FileInputStream fis, OutputStream os)
			throws Exception {
		// Construct a 1K buffer to hold bytes on their way to the socket.
		byte[] buffer = new byte[1024];
		int bytes = 0;

		// Copy requested file into the socket's output stream.
		while ((bytes = fis.read(buffer)) != -1) {
			os.write(buffer, 0, bytes);
		}
	}

	private static String contentType(String fileName) {
		if (fileName.endsWith(".htm") || fileName.endsWith(".html")) {
			return "text/html";
		}
		if (fileName.endsWith(".png")) {
			return "image/png";
		}
		if (fileName.endsWith(".pdf")) {
			return "application/pdf";
		}
		if (fileName.endsWith(".zip")) {
			return "application/zip";
		}
		if (fileName.endsWith(".jpeg")) {
			return "image/jpeg";
		}
		return "application/octet-stream";
	}
}

public final class WebServer {
	public static void main(String argv[]) throws Exception {
		
		
		
		// Get the port number from the command line.
		int port = Integer.parseInt(argv[0]);

		// Establish the listen socket.
		//ServerSocket socket = new ServerSocket(port);


		String ksName = "keystore.jks";
		char ksPass[] = "password".toCharArray();
		char ctPass[] = "password".toCharArray();

		KeyStore ks = KeyStore.getInstance("JKS");
		ks.load(new FileInputStream(ksName), ksPass);
		KeyManagerFactory kmf = 
				KeyManagerFactory.getInstance("SunX509");
		kmf.init(ks, ctPass);
		SSLContext sc = SSLContext.getInstance("SSL");
		sc.init(kmf.getKeyManagers(), null, null);
		SSLServerSocketFactory ssf = sc.getServerSocketFactory();
		SSLServerSocket s = (SSLServerSocket) ssf.createServerSocket(port);
		//printServerSocketInfo(s);
		//SSLSocket c = (SSLSocket) s.accept();




		// Process HTTP service requests in an infinite loop.
		while (true) {
			// Listen for a TCP connection request.
			//Socket connection = socket.accept();
			SSLSocket c = (SSLSocket) s.accept();

			// Construct an object to process the HTTP request message.
			HttpRequest request = new HttpRequest(c);

			// Create a new thread to process the request.
			Thread thread = new Thread(request);

			// Start the thread.
			thread.start();
		}
	}
}
