package project;

import java.util.ArrayList;
import java.util.List;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import result.SearchResult;
import result.SearchResultBlock;

public class GoogleSearchCrawler {
	
	public static SearchResult getDataFromGoogle(String query, int num) {
		
		SearchResult result = new SearchResult();
		
		// Send query to google, use num+5 to make sure the parse result
		// will be more than require number
		List<String> urls = new ArrayList<String>();
		String request = "https://www.google.com/search?q=" + query + "&num="+(num+5);
		System.out.println("Sending request..." + request);
		
		Document doc = HtmlDocumentUtil.getDocument(request);

		// get all links
		Elements blocks = doc.select(".g");
		System.out.println("blocks="+blocks.size());
				
		for(int i=0; i<blocks.size(); i++){
			Elements links = blocks.get(i).select(".r");
			Element link = null;
			if(links != null && links.size() > 0){
				link = links.get(0);
			}else{
				continue;
			}
			
			String tmp = link.children().first().attr("href");
			int start = tmp.indexOf("http");
			if (start == -1){ 
				continue;
			}

			String url = tmp;
			System.out.println("url=" + url);
			urls.add(url);
			result.addBlock(storeResultBlock(url, blocks.get(i)));
			if(urls.size()==num){ break; }
		}
		
		result.setUrls(urls);
		return result;
	}
	
	private static SearchResultBlock storeResultBlock(String url, Element block){
		Elements title = block.getElementsByTag("h3");
		Elements cites = block.getElementsByTag("cite");
		Elements description = block.select(".st");
		
		StringBuilder newDiv = new StringBuilder();
		newDiv.append("<div class=\"block\">");
		newDiv.append("<h3 class=\"r\"><a href=\""+url+"\">"+title.get(0).select("a").html()+"</a></h3>");
		newDiv.append("<div class=\"s\"><div class=\"kv\" style=\"margin-bottom:2px\">");
		newDiv.append(cites.toString());
		newDiv.append("</div></div>");
		newDiv.append(description.toString());
		newDiv.append("</div>");
		
		//System.out.println(newDiv.toString());
		
		return new SearchResultBlock(url, newDiv.toString());
	}

}