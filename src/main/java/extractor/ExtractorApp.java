package extractor;

/**
 * http://www.java2s.com/Tutorial/Java/0320__Network/Getallhyperlinksfromawebpage.htm
 **/

import gnu.getopt.Getopt;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;

public class ExtractorApp {
	//
	// static String toDel = null;

	public void startExtractor(String path){
		// Initialize Crawler & Extractor
				Extractor extr = new Extractor();
				Storage saving = new Storage();
				Set<CrawledLink> visitedLinks = null;
				Set<String> linkStrings = new HashSet<String>();
				try {
					visitedLinks = saving.readJSON(path);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				
				System.out.println(visitedLinks.size());
				List<CrawledLink> test = new ArrayList<CrawledLink>(visitedLinks);
				
				
				for(int i = 0; i < test.size(); i++){
					String a = test.get(i).getLinkURL().toString();
					if (a.lastIndexOf("/") == a.length() - 1
							|| a.lastIndexOf("#") == a.length() - 1) {
						a = StringUtils.stripEnd(a, "/");
						a = StringUtils.stripEnd(a, "#");
					}
					
					test.get(i).setLinkURL(a);
				}

				
				System.out.println(new File(test.get(0).getLocalPath()).getParent()); 
				
				for(int i = 0; i < test.size(); i++){
					try {
						System.out.println("------------------------------------");
						System.out.println("Link No: " + i + "  Link Local: " + test.get(i).getLinkURL());
						String a = test.get(i).getLinkURL().toString();

						if (!linkStrings.contains(a)){
							test.set(i, extr.extractMeta(test.get(i)));
							if(test.get(i).getWordSet() != null || test.get(i).getWordSet().isEmpty()){
								System.out.println(test.get(i).getLinkURL());
							}
							linkStrings.add(a);
						}
						
						else{
							test.remove(i);
						}
						
						

					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
//					List<Link> links = new ArrayList<Link> (test.get(i).getListOfLinks());
//					System.out.println("Link No: " + i + "  -- Link: " + links.size());
				}
				
				saving.store2(test);
//				
//				 toDel = new File(test.get(0).getLocalPath()).getParent();
				 
	}

	public static void main(String args[]) {
		Getopt g = new Getopt("testprog", args, "c:");
		int a;
		String arg;

		String dir = "";

		// if(args.length == 4 && args[0].equals("-d") &&
		// Integer.parseInt(args[1]) > 0 && args[2].equals("-u")){
		// depth = Integer.parseInt(args[1]);
		// url = args[3];
		// }
		// else if(args.length == 4 && args[2].equals("-d") &&
		// Integer.parseInt(args[3]) > 0 && args[0].equals("-u")){
		// depth = Integer.parseInt(args[3]);
		// url = args[1];
		// }
		// else{
		// System.out.println("Usage: -d <depth> -u <url> ");
		// System.exit(0);
		// }

		while ((a = g.getopt()) != -1) {
			switch (a) {
			case 'c':
				arg = g.getOptarg();
				System.out.println("You picked " + (char) a + " with argument "
						+ ((arg != null) ? arg : "null"));
				if (arg != null)
					dir = arg;
				break;
			case '?':
				break; // getopt() already printed an error
			default:
				System.out.print("getopt() returned " + a + "\n");
			}
		}

		System.out.println("Inputted depth: " + dir);

		new ExtractorApp().startExtractor(dir);

		// Deleter.delLocalFolder(toDel);

	}

}