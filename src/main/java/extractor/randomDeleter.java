package extractor;


import java.io.File;

public class randomDeleter {

	public static void main(String args[]) {
		Boolean folderDelete = false;
		
		//INPUT PATH/FOLDER NAME HERE TO DELETE PERSISTENT FOLDERS!!!!!
		File folder = new File("none");
		folderDelete = new randomDeleter().deleteDirectory(folder);
		System.out.println("Folder Deleted: " + folderDelete);
	}
	
	boolean deleteDirectory(File path) {
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