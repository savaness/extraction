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
import java.util.Set;
import java.util.UUID;

import org.apache.tika.exception.TikaException;
import org.apache.tika.metadata.Metadata;
import org.apache.tika.parser.AutoDetectParser;
import org.apache.tika.parser.ParseContext;
import org.apache.tika.parser.Parser;
import org.apache.tika.parser.html.HtmlParser;
import org.apache.tika.sax.BodyContentHandler;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.xml.sax.ContentHandler;
import org.xml.sax.SAXException;

import com.uwyn.jhighlight.tools.FileUtils;

public class Extractor {

//	public static void main(String args[]) throws IOException, SAXException, TikaException {
//		Extractor ex = new Extractor();
//		
//		ex.parseExample("http://www.google.com");
//
//	}
	
	
	public Map<String,String> extractMeta(String filepath) {
		
		Map<String,String> metaDataMap = new HashMap<String,String>();

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
	

	public Map<String,String> parseExample(String url) {
		/**
		 * Sources:
		 * http://chrisjordan.ca/post/15219674437/parsing-html-with-apache-tika
		 * Source: http://www.infoq.com/news/2011/12/tika-10 Source:
		 * http://www.hascode
		 * .com/2012/12/content-detection-metadata-and-content-
		 * extraction-with-apache-tika/#Tutorial_Sources
		 * http://www.tutorialspoint.com/tika/tika_metadata_extraction.htm
		 * http:/
		 * /stackoverflow.com/questions/6713927/extract-the-contenttext-of-
		 * a-url-using-tika
		 * http://www.ibm.com/developerworks/opensource/tutorials
		 * /os-apache-tika/
		 * http://stackoverflow.com/questions/5429814/how-can-i-
		 * use-the-html-parser-with-apache-tika-in-java-to-extract-all-html-tags
		 * http
		 * ://www.javaprogrammingforums.com/whats-wrong-my-code/34932-parse-any
		 * -file-using-auto-detect-parser-apache-tika-library.html
		 * 
		 */
		URL inputURL = null;
		Map<String, String> metaDataMap = new HashMap<String, String>();

		try {
			inputURL = new URL(url);
		} catch (MalformedURLException e1) {
			e1.printStackTrace();
		}
		InputStream input = null;
		try {
			input = inputURL.openStream();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		Parser parser = new HtmlParser();
		Metadata metaD = new Metadata();
		ContentHandler bodyCH = new BodyContentHandler();

		try {
			try {
				parser.parse(input, bodyCH, metaD, new ParseContext());
			} catch (SAXException e) {
				e.printStackTrace();
			} catch (TikaException e) {
				e.printStackTrace();
			}

			System.out.println("Number of Metadata Tags: " + metaD.size());

			for (String eachName : metaD.names()) {
				System.out.println(eachName + ": " + metaD.get(eachName));
				metaDataMap.put(eachName, metaD.get(eachName));
			}

			input.close();

		} catch (IOException e) {
			e.printStackTrace();
		}

		return metaDataMap;
	}
	
	public String downloadFiles(String fileUrl) throws IOException {
		/**
		 * Source:
		 * http://stackoverflow.com/questions/17101276/java-download-all-files-and-folders-in-a-directory
		 * http://stackoverflow.com/questions/3024002/how-to-create-a-folder-in-java
		 * http://stackoverflow.com/questions/9658297/java-how-to-create-a-file-in-a-directory-using-relative-path
		 * http://www.java2s.com/Tutorial/Java/0180__File/Removefileordirectory.htm
		 * http://stackoverflow.com/questions/4875064/jsoup-how-to-get-an-images-absolute-url
		 * http://www.avajava.com/tutorials/lessons/how-do-i-save-an-image-from-a-url-to-a-file.html
		 * http://stackoverflow.com/questions/3987921/not-able-to-delete-the-directory-through-java
		 * 
		 */

		String[] folders = fileUrl.split("/");

		File folder = new File("files");
		try{
			if(folder.mkdirs()) { 
				System.out.println("Directory Created");
			} else {
				System.out.println("Directory exists");
			}
		} catch(Exception e){
			e.printStackTrace();
		}
		
		
		String file = saveFiles(fileUrl, folders, folder);
		
		System.out.println("File Saved: " + file);
		return file;
	}
	
	public String saveFiles(String fileUrl, String [] folders, File folder){
		
    	URL fileURL2;
    	String folderName = "";
		try {
			fileURL2 = new URL(fileUrl);
	    	InputStream is = fileURL2.openStream();
	    	folderName = folder + "/" + UUID.randomUUID(); //+ "." + folders[folders.length-1].split("\\.")[1].replace("?", "");
	    	System.out.println(folderName);
			//OutputStream os = new FileOutputStream(folderName);
	    	OutputStream os = new FileOutputStream(Paths.get(folderName).toString());
	    	
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

		Map<String,String> metaDataMap = new HashMap<String,String>();

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
		
		crawledLink.setMetadata(metaDataMap);
		
		String[] wordArray = bodyCH.toString().toLowerCase().replaceAll("(^\\s+|\\s+$)", "").split("\\s+");
		
		for(int i = 0; i < wordArray.length; i++){
			if (wordArray[i].contains(".")){
				wordArray[i] = wordArray[i].substring(0, wordArray[i].lastIndexOf(".")); 
			}
			
		}
		
//		System.out.println("ARRAY SIZE: " + wordArray.length);

		Set<String> wordSet = new HashSet<String>();
		wordSet.addAll(Arrays.asList(wordArray));
		
//		System.out.println("SET SIZE: " + wordSet.size());

		Map<String, Integer> wordMap = new HashMap<String, Integer>();

		
		for (String word : wordSet) {
			wordMap.put(word, 0);
			
			for (String s : wordArray) {
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