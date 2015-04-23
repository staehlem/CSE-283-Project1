import java.io.*;
import java.util.zip.*;



//need to replace file input and output stream from the zip server with socket stuff
//hard code in proj2.bin in that file name spot line 31 in the zip server file
//client side is webserver.java, must write to file system and socket, so you send 
//the same bytes to the compression server as you do the file system
//inputstream reads and outputstream writes

//already have in and out stream and other
//use socket in and out stream instead of file input and output stream
//dest will say out 
//remember to flush buffer in correct order and combination multiple streams and buffers make this difficult
//
//

public class Zip {
   static final int BUFFER = 2048;
   public static void main (String argv[]) {
      try {
         String fileInput = argv[0];
         String fileOutput = argv[1];
		 
         BufferedInputStream origin = null;
		 
		 // Create a file output stream
         FileOutputStream dest = new 
           FileOutputStream(fileOutput);
		   
         ZipOutputStream out = new ZipOutputStream(new 
           BufferedOutputStream(dest));
		   
         //out.setMethod(ZipOutputStream.DEFLATED);
         byte data[] = new byte[BUFFER];
		 
         // get a list of files from current directory
          FileInputStream fi = new 
            FileInputStream(fileInput);
			
          origin = new 
            BufferedInputStream(fi, BUFFER);
			
          ZipEntry entry = new ZipEntry("proj2.bin");
          out.putNextEntry(entry);
          int count;
          while((count = origin.read(data, 0, 
            BUFFER)) != -1) {
             out.write(data, 0, count);
          }
         origin.close();
         out.close();
      } catch(Exception e) {
         e.printStackTrace();
      }
   }
} 