package org.xor.utils;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;
/**An utility class, that provides useful file methods
 * 
 * @author LordOfBees
 * @version 1.00
 */
public final class FileTools {
	/**
	 * Indicate to the reader that this is the end of the file he has received
	 */
	private static final String FILE_END = "FSTOP";
	/**Add the specified file to the specified ZIP output stream
	 * 
	 * @param file is the file to add to the ZIP output stream
	 * @param out is the ZIP output stream to add the file to
	 */
	public static void addToZipFile(File file, ZipOutputStream out) {
		try {
			FileInputStream fis = new FileInputStream(file);	
			ZipEntry zipEntry = new ZipEntry(file.getName());
			out.putNextEntry(zipEntry);
			byte[] bytes = new byte[4096];
			int length;
			while (( length = fis.read(bytes) ) > 0) {
				out.write(bytes, 0, length);
			}
			out.closeEntry();
			fis.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**Read a file and send it over the output stream
     * <p>To get the file from the data sent, see {@link #saveFromInput(File, DataInputStream, int)}
     * @param out is the output stream over we will send the file data
     * @param file the file to be sent
     * @param bufferSize is the size of the byte buffer used
     */
    public static void sendFile(DataOutputStream out, File file, int bufferSize){
    	try {
			DataInputStream reader = new DataInputStream(new FileInputStream(file));
			byte[] buf = new byte[bufferSize];
			int read = 0;
			while((read = reader.read(buf)) > 0){
				out.write(buf, 0, read);
			}
			out.flush();
			out.writeUTF(FILE_END);
			reader.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
    }
    
    /**Save a file from the data received by the input stream
     * <p>To send the file data, see {@link #sendFile(DataOutputStream, File, int)}
     * @param file is the file where the data will be saved
     * @param stream is the input stream that send the data
     * @param bufferSize is the size of the byte buffer used
     */
    public static void saveFromInput(File file, DataInputStream stream, int bufferSize){
    	try {
        	if(!file.exists())
        		file.createNewFile();
			DataOutputStream writer = new DataOutputStream(new FileOutputStream(file));
			byte[] buffer = new byte[bufferSize];
			int bytesRead = 0;
			String line = "";
			while(( bytesRead = stream.read(buffer) ) > 0){
				if(( line = new String(buffer) ).contains(FILE_END)){
					writer.write(buffer, 0, line.indexOf(FILE_END));
					int from = Math.min(line.indexOf(FILE_END)+FILE_END.length()+1, bytesRead);
					writer.write(buffer, from, bytesRead-from);
					break;
				}
				writer.write(buffer, 0, bytesRead);
			}
			writer.close();
		}
    	catch (IOException e) {
			e.printStackTrace();
		}
    }
    
    /**Extract a resource file from its own jar and save it to the <b>file</b>
     * <p>NOTE : as said above it's a resource file and so on must be contained in your res directory
     * @param file is the file where the resource will be saved
     * @param resName is the file's name to extract
     */
    public static void extract(File file, String resName){
    	try {
        	if(!file.exists())
        		file.createNewFile();
        	DataInputStream stream = new DataInputStream(FileTools.class.getResourceAsStream("/res/"+resName));
			DataOutputStream writer = new DataOutputStream(new FileOutputStream(file));
			byte[] buf = new byte[4096];
			int read = 0;
			while((read = stream.read(buf)) != -1){
				writer.write(buf, 0, read);
			}
			stream.close();
			writer.close();
		}
    	catch (IOException e) {
			e.printStackTrace();
		}
    }
    /**
	 * Get the extension of a file without the point
	 * <p><i>see {@link #getExtensionPoint(File)}</i>
	 * @param file is the file to get the extension
     * @return the extension of the file
	 */  
	public static String getExtension(File file) {
	    String ext = getExtensionPoint(file);
	    return ext != null ? ext.substring(1) : null;
	}

	/**Get the extension of a file with the point
     * <p><i>see {@link #getExtension(File)}</i>
     * @param file is the file to get the extension
     * @return the extension of the file with the point or <b>null</b> if there is no point
     */
	public static String getExtensionPoint(File file) {
		String ext = null;
        String s = file.getName();
        if(!s.contains("."))
        	return null;
        int i = s.lastIndexOf('.');

        if (i > 0 &&  i < s.length() - 1) {
            ext = s.substring(i).toLowerCase();
        }
        return ext;
	}

}