package extractor;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.UUID;

import org.apache.commons.io.FilenameUtils;
import org.apache.tika.exception.TikaException;
import org.apache.tika.metadata.Metadata;
import org.apache.tika.parser.AutoDetectParser;
import org.apache.tika.parser.ParseContext;
import org.apache.tika.parser.Parser;
import org.apache.tika.sax.BodyContentHandler;
import org.xml.sax.ContentHandler;
import org.xml.sax.SAXException;

public class Extractor {

	// public static void main(String args[]) throws IOException, SAXException,
	// TikaException {
	// Extractor ex = new Extractor();
	//
	// ex.parseExample("http://www.google.com");
	//
	// }

	Set<String> metaNeeded = new HashSet<String>();

	public void fillMetaNeeded() {
		metaNeeded.add("Description");
		metaNeeded.add("title");
		metaNeeded.add("Author");
		metaNeeded.add("analytics-track");
		metaNeeded.add("Device Mfg Description");
		metaNeeded.add("Device Model Description");
		metaNeeded.add("Device manufacturer");
		metaNeeded.add("Copyright");
		metaNeeded.add("tEXt tEXtEntry");

	}

	public Map<String, String> extractMeta(String filepath) {

		Map<String, String> metaDataMap = new HashMap<String, String>();

		Parser parser = new AutoDetectParser();
		ContentHandler bodyCH = new BodyContentHandler();
		Metadata metadata = new Metadata();
		FileInputStream input = null;
		try {
			input = new FileInputStream(filepath);
		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
		}

		try {
			try {
				parser.parse(input, bodyCH, metadata, new ParseContext());
			} catch (SAXException e) {
				e.printStackTrace();
			} catch (TikaException e) {
				e.printStackTrace();
			}
			System.out.println("------------------------------------");
			System.out.println("Number of Metadata Tags: " + metadata.size());

			for (String eachName : metadata.names()) {
				System.out.println(eachName + ": " + metadata.get(eachName));
				metaDataMap.put(eachName, metadata.get(eachName));
			}

			System.out.println("------------------------------------");

			input.close();

		} catch (IOException e) {
			e.printStackTrace();
		}

		return metaDataMap;
	}


	public String downloadFiles(String fileUrl) throws IOException {
		/**
		 * Source:
		 * http://stackoverflow.com/questions/17101276/java-download-all-
		 * files-and-folders-in-a-directory
		 * http://stackoverflow.com/questions/3024002
		 * /how-to-create-a-folder-in-java
		 * http://stackoverflow.com/questions/9658297
		 * /java-how-to-create-a-file-in-a-directory-using-relative-path
		 * http://www
		 * .java2s.com/Tutorial/Java/0180__File/Removefileordirectory.htm
		 * http://
		 * stackoverflow.com/questions/4875064/jsoup-how-to-get-an-images-
		 * absolute-url
		 * http://www.avajava.com/tutorials/lessons/how-do-i-save-an
		 * -image-from-a-url-to-a-file.html
		 * http://stackoverflow.com/questions/3987921
		 * /not-able-to-delete-the-directory-through-java
		 * 
		 */

		String[] folders = fileUrl.split("/");

		File folder = new File("files");
		try {
			if (folder.mkdirs()) {
				System.out.println("Directory Created");
			} else {
				System.out.println("Directory exists");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		String file = saveFiles(fileUrl, folders, folder);

		System.out.println("File Saved: " + file);
		return file;
	}

	public String saveFiles(String fileUrl, String[] folders, File folder) {

		URL fileURL2;
		String folderName = "";
		try {
			fileURL2 = new URL(fileUrl);
			InputStream is = fileURL2.openStream();
			folderName = folder + "/" + UUID.randomUUID(); // + "." +
															// folders[folders.length-1].split("\\.")[1].replace("?",
															// "");
			System.out.println(folderName);
			// OutputStream os = new FileOutputStream(folderName);
			OutputStream os = new FileOutputStream(Paths.get(folderName)
					.toString());

			byte[] b = new byte[2048];
			int length;

			while ((length = is.read(b)) != -1) {
				os.write(b, 0, length);
			}

			is.close();
			os.close();
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return folderName;
	}

	static public boolean deleteDirectory(File path) {
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

	public CrawledLink extractMeta(CrawledLink crawledLink) {

		Map<String, String> metaDataMap = new HashMap<String, String>();

		Parser parser = new AutoDetectParser();
		ContentHandler bodyCH = new BodyContentHandler();
		Metadata metadata = new Metadata();
		FileInputStream input = null;
		try {
			input = new FileInputStream(crawledLink.getLocalPath());
		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
		}

		try {
			try {
				parser.parse(input, bodyCH, metadata, new ParseContext());
			} catch (SAXException e) {
				e.printStackTrace();
			} catch (TikaException e) {
				e.printStackTrace();
			}
			System.out.println("Number of Metadata Tags: " + metadata.size());

			for (String eachName : metadata.names()) {
				System.out.println(eachName + ": " + metadata.get(eachName));
				metaDataMap.put(eachName, metadata.get(eachName));
			}

			System.out.println("------------------------------------");

			input.close();

		} catch (IOException e) {
			e.printStackTrace();
		}

		crawledLink.setMetadata(metaDataMap);

		String[] wordArray = bodyCH.toString().toLowerCase().split("\\s+");

		for (int i = 0; i < wordArray.length; i++) {
			wordArray[i] = wordArray[i].replaceAll("[^a-zA-Z]", "")
					.toLowerCase();
			;
		}

		// System.out.println("ARRAY SIZE: " + wordArray.length);

		Set<String> wordSet = new HashSet<String>();
		wordSet.addAll(Arrays.asList(wordArray));
		
		Set<String> metaSet = new HashSet<String>();

		for (Entry<String, String> entry : metaDataMap.entrySet()) {
			for (String metaKey : metaNeeded)
				if (entry.getKey() == metaKey) {
					String[] metaArray = entry.getValue().toLowerCase()
							.split("\\s+");
					for (int i = 0; i < metaArray.length; i++) {
						metaArray[i] = metaArray[i].replaceAll("[^a-zA-Z0-9]",
								"").toLowerCase();
						;
					}
					metaSet.addAll(Arrays.asList(metaArray));
				}
		}
		
		wordSet.addAll(metaSet);

		// System.out.println("SET SIZE: " + wordSet.size());
		
		

		    
		String[] filename = FilenameUtils.getBaseName(crawledLink.getLinkURL()).toLowerCase()
				.split("\\s+");
		for (int i = 0; i < filename.length; i++) {
			filename[i] = filename[i].replaceAll("[^a-zA-Z0-9]",
					"").toLowerCase();
			;
		}
		
		wordSet.addAll(Arrays.asList(filename));
		

		Map<String, Integer> wordMap = new HashMap<String, Integer>();
		
		
		for (String word: wordSet){
			wordMap.put(word, 0);

			for (String s : wordArray) {
				int value = wordMap.get(word);
				if (word.equalsIgnoreCase(s)) {
					wordMap.put(word, ++value);
				}
			}
			for (String s : metaSet) {
				int value = wordMap.get(word);
				if (word.equalsIgnoreCase(s)) {
					wordMap.put(word, ++value);
				}
			}
			for (String s : filename) {
				int value = wordMap.get(word);
				if (word.equalsIgnoreCase(s)) {
					wordMap.put(word, ++value);
				}
			}
			
		}
		
		
		
		
		
		

		crawledLink.setWordSet(wordSet);
		crawledLink.setWordMap(wordMap);

		return crawledLink;

	}

}