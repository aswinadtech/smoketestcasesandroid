package twc.Automation.utils;

import java.io.File;
import java.io.IOException;

public class DeleteFiles {
	public static void deleteFiles(File file) {
	    if (file.isDirectory())
	        for (File f : file.listFiles())
	            deleteFiles(f);
	    else
	        file.delete();
	}
	
	public void deleteFiles2(File folder) throws IOException {
	    File[] files = folder.listFiles();
	     for(File file: files){
	            if(file.isFile()){
	                String fileName = file.getName();
	                boolean del= file.delete();
	                System.out.println(fileName + " : got deleted ? " + del);
	            }else if(file.isDirectory()) {
	                deleteFiles(file);
	            }
	        }
	    }
}
