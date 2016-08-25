package project;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import edu.ucla.sspace.common.Similarity;

public class WikiLabelUtil {
	
	private static Map<String, double[]> prevQueries = new HashMap<>();
	
	private static String wikiHome = PropertiesUtil.getProperty("WIKI_HOME");
	
	private static String querySession = "";

	/**
	 * Find a best label for this cluster
	 * Use the candidate word to query the wiki website,
	 * and compare the similarity of wiki web page and cluster center
	 * @param center	The center of this cluster
	 * @param importantWords	Candidate labels for this cluster
	 * @return	The most possible label of this cluster
	 */
	public static String compareLabelWithWiki(double[] center, List<String> importantWords, DocumentVectorUtil docUtil){
		// If this is another query, clear the cache map
		String thisQuerySession = docUtil.allWordsList.size()+"_"+docUtil.vectors.length;
		if(!querySession.equals(thisQuerySession)){
			prevQueries.clear();
			querySession = thisQuerySession;
		}
		
		double maxSimilarity = Integer.MIN_VALUE;
		String bestLabel = "";
		
		for(String importantWord: importantWords){
			
			double[] thisvector = getWikiVectorByWord(importantWord, docUtil);
			
			//System.out.println("center length=" + center.length + " thisvector length=" + thisvector);
			
			double similarity = Similarity.cosineSimilarity(center, thisvector);
			//System.out.println("similarity="+similarity);
			if(similarity > maxSimilarity){
				maxSimilarity = similarity;
				bestLabel = importantWord;
			}
		}
		return bestLabel;
	}
	
	/**
	 * Create a vector from wiki web page
	 * @param word	The query word
	 * @return	a vector of of wiki page
	 */
	private static double[] getWikiVectorByWord(String word, DocumentVectorUtil docUtil){
		// If we have query this word before,
		// use the cached vector
		if(prevQueries.containsKey(word)){
			return prevQueries.get(word);
		}
		
		String url = wikiHome+word;
		String webContent = HtmlDocumentUtil.getWebContent(url);
		double[] vector = docUtil.getDocVector(webContent);
		prevQueries.put(word, vector);
		return vector;
	}
}
