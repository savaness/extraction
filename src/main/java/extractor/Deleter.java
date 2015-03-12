package extractor;

import java.io.File;

	import org.apache.commons.codec.binary.StringUtils;

	public class Deleter {

	    public static void delLocalFolder(String filePath) {
	        Boolean folderDelete = false;

	        //INPUT PATH/FOLDER NAME HERE TO DELETE PERSISTENT FOLDERS!!!!!
	        File folder = new File(filePath);
	        folderDelete = deleteDirectory(folder);
	        System.out.println("Local Files Deleted: " + folderDelete);
	    }
	    
	    private static boolean deleteDirectory(File path) {
	        if (path.exists()) {
	            File[] files = path.listFiles();
	            for (int i = 0; i < files.length; i++) {
	                if (files[i].isDirectory()) {
	                    deleteDirectory(files[i]);
	                } else {
	                    files[i].delete();
	                }
	            }
	        }
	        return (path.delete());
	    }
	}

