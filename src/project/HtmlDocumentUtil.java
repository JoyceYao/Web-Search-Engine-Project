package project;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.Connection.Response;
import org.jsoup.nodes.Document;

public class HtmlDocumentUtil {

	public static Document getDocument(String url){
		Connection con = null;
		Document doc = null;
		int retry = 5;
		
		while(doc == null && retry > 0){
			try{
				con = Jsoup.connect(url).userAgent("Mozilla/5.0 (Windows NT 5.1; en) AppleWebKit/535.12 (KHTML, like Gecko) Chrome/22.0.1229.79 Safari/535.1");
				con.timeout(50000).ignoreHttpErrors(true).followRedirects(true);
				Response resp = con.execute();
				System.out.println("url=" + url +" state="+resp.statusCode());
				
				if (resp.statusCode() == 200) {
					doc = con.get();
				}
				retry--;
			} catch (Exception e){
				e.printStackTrace();
				retry--;
			}
		}
		return doc;
	}
	
	private static String replaceHtmlTag(String input){
		String content = input.replaceAll("(?i)<[^>]*>", " ").replaceAll("\\s+", " ").trim();
		content = content.replaceAll("[^A-Za-z0-9 ]", "");
		return content;
	}
  
	public static String getWebContent(String url){
		Document doc = getDocument(url);
		//System.out.println(doc == null? "doc is empty": doc.text());
		return doc == null? "": doc.text(); //replaceHtmlTag(doc.text());
	}
}
