package project;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.StringTokenizer;

import edu.mit.jwi.IRAMDictionary;
import edu.mit.jwi.RAMDictionary;
import edu.mit.jwi.data.ILoadPolicy;
import edu.mit.jwi.item.IIndexWord;
import edu.mit.jwi.item.ISynset;
import edu.mit.jwi.item.ISynsetID;
import edu.mit.jwi.item.IWord;
import edu.mit.jwi.item.IWordID;
import edu.mit.jwi.item.POS;
import edu.mit.jwi.item.Pointer;

public class DocumentVectorUtil {

	Map<String, Integer> allWordsMap = new HashMap<>();
	Map<String, Map<String, Double>> wordCountMap = new HashMap<>();
	Map<String, Double> wordCountTotal = new HashMap<>();
	List<String> allWordsList = new ArrayList<>();
	int wordIdx = 0;
	double[][] vectors;
	double[] allDocs;
	Map<String, Double> allDocMap = new HashMap<>();
	Set<String> freqWords = null;
	Map<String, Double> idfMap = null;
	
	/**
	 * Calculate the document vectors
	 * @param urls	url list to the web pages
	 * @param contextPath	
	 * @param queryTerms
	 */
	public void createDocumentVector(List<String> urls, String[] queryTerms){
		freqWords = WordFrequencyUtil.buildFreqWords();
		
		for(String url: urls){
			String webContent = HtmlDocumentUtil.getWebContent(url);
			storeWordCountMap(url, webContent, freqWords, queryTerms);
		}
		
		idfMap = createIDF();
		
		vectors = new double[urls.size()][allWordsMap.size()];
		allDocs = new double[allWordsMap.size()];

        for(int i=0; i<urls.size(); i++){
        	String url = urls.get(i);
        	Map<String, Double> thisDoc = wordCountMap.get(url);
        	
        	for(int j=0; j<allWordsList.size(); j++){
        		String thisWord = allWordsList.get(j);
        		Double prevV = thisDoc.get(thisWord);
        		if(prevV == null){prevV=(double)0;}
        		vectors[i][j] = prevV*idfMap.get(thisWord);
        		allDocs[j] += prevV*idfMap.get(thisWord);
        	}	
        	normalizeVector(vectors[i]);

        }
        normalizeVector(allDocs);    	
	}
	
	/**
	 * Normalize the vector
	 * @param vector
	 */
	private void normalizeVector(double[] vector){
		double leng = getVectorLength(vector);
		for(int j=0; j<allWordsList.size(); j++){
			vector[j] /= leng; 
		}
	}
	
	/**
	 * Calculate the vector length
	 * @param vector
	 * @return vector length
	 */
	private double getVectorLength(double[] vector){
    	double length = 0;
    	for(int j=0; j<allWordsList.size(); j++){
    		length += Math.pow(vector[j], 2);
    	}
    	length = Math.pow(length, 0.5);
    	return length;
	}
	
	/**
	 * To build wordCountMap from web page content
	 * @param url	The URL of web page
	 * @param s		The text content of the web page
	 * @param freqWords	A set of frequency words
	 * @param queryTerms	query keywords
	 */
	private void storeWordCountMap(String url, String s, Set<String> freqWords, String[] queryTerms){
		Map<String, Double> thisWordCount = new HashMap<>();
		double total = 0;
		
		// To lowercase, and lemmatize it
		List<String> lemmaStr = LemmaUtil.getLemmaLowerCaseTokens(s);
		
		if(lemmaStr.size() == 0){
			lemmaStr.add("error");
		}
		
		// Add word count from the url
		// Or when the content is empty, add the keyword to avoid NAN problem 
		// in the future calculation
		for(String term: queryTerms){
			if(url.toLowerCase().indexOf(term) >= 0){
				lemmaStr.add(term);
			}
		}
		
		for(String word: lemmaStr){

			// Do not use this word if
			// (1) it is empty
			// (2) it doesn't start with a letter
			// (3) it appears very frequently
			// (4) it is not a word in wordNet
			if(word.isEmpty()){ continue; }
			if(!Character.isLetter(word.charAt(0))){ continue; }
			if(freqWords.contains(word)){ continue; }
			//if(!LemmaUtil.isWord(word)){ continue; }
			
			// If this is a new word, add it into allWordsMap and allWordsList
			if(!allWordsMap.containsKey(word)){
				allWordsMap.put(word, wordIdx++);
				allWordsList.add(word);
			}
			Double prevCount = thisWordCount.get(word);
			thisWordCount.put(word, prevCount==null? 1: prevCount+1);
			total++;
			Double prevMap = allDocMap.get(word);
			allDocMap.put(word, prevMap==null? 1:prevMap+1);
		}
		//System.out.println();
		//System.out.println("map size=" + thisWordCount.size());
		//System.out.println("map=" + thisWordCount);
		wordCountTotal.put(url, total);
		wordCountMap.put(url, thisWordCount);
	}
	
	/**
	 * Calculate a map of "Inverted Document Frequency"
	 * @return Map of IDF
	 */
	private Map<String, Double> createIDF(){
		Map<String, Double> docFreq = new HashMap<>();
		int totalDocNo = wordCountMap.size();

		for(String word :allWordsList){
			double count = 0;
			for(Map<String, Double> wordCountMap: wordCountMap.values()){
				if(wordCountMap.containsKey(word)){
					count++;
				}
			}
			docFreq.put(word, Math.log(totalDocNo/count));	
		}
		return docFreq;
	}
	
	
	public double[] getDocVector(String s){
		List<String> lemmaStr = LemmaUtil.getLemmaLowerCaseTokens(s);
		double[] vector = new double[allWordsList.size()];
		
		for(String word: lemmaStr){
			if(allWordsMap.containsKey(word)){
				vector[allWordsMap.get(word)]++;
			}
		}
		
    	for(int j=0; j<allWordsList.size(); j++){
    		String thisWord = allWordsList.get(j);
    		vector[j]*=idfMap.get(thisWord);
    	}
    	
    	normalizeVector(vector);
    	return vector;
	}
	
}
