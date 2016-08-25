package project;

import java.util.List;
import java.util.Properties;
import java.util.Set;

import result.SearchResult;
import ssltool.SSLTool;
import edu.ucla.sspace.clustering.Assignments;
import edu.ucla.sspace.matrix.ArrayMatrix;
import edu.ucla.sspace.matrix.Matrix;

public class Main {
	
	public static void main(String[] args){
		Main c = new Main();
		c.doSeach("google", 20, "D:\\HPS_HW\\HPS_workspace\\WSE_project\\WebContent");
	}
	
	public SearchResult doSeach(String query, int docNum, String contextPath){
		//System.out.println("doSearch in Main!!");
		SSLTool.trustAllContext();
		
		// Load the application properties
		PropertiesUtil.load(contextPath);
		
		// Do a Google query and store all the URLs
		SearchResult searchResult = GoogleSearchCrawler.getDataFromGoogle(query, docNum);
		
		System.out.println("test[2]");
		//System.out.println("test[2]");

		// Calculate the document vectors and change it into matrix
		DocumentVectorUtil docUtil = new DocumentVectorUtil();
		docUtil.createDocumentVector(searchResult.getUrls(), query.toLowerCase().split(" "));
		Matrix matrix = new ArrayMatrix(docUtil.vectors);
		
		// Do BiesctingKMean clustering with different number of centers (between minCenter and maxCenter)
		// Find the best clusters set
		int minCenter = Integer.parseInt(PropertiesUtil.getProperty("MIN_CENTER"));
		int maxCenter = Integer.parseInt(PropertiesUtil.getProperty("MAX_CENTER"));
		Assignments assignment = BisectingKMeansUtil.getBestClusterByNo(matrix, new Properties(), minCenter, maxCenter);
		List<Set<Integer>> clusters = assignment.clusters();
		
		// Find the best label for each cluster
		List<String> labels = ClusterLabelUtil.getAllLabels(clusters, docUtil);
		
		// Store the clusters and labels into the result object
		ResultUtil.checkDupLabelAndStoreResult(clusters, labels, searchResult);
		
		System.out.println(searchResult.getLabels());
		System.out.println(searchResult.getClusters());
		
		return searchResult;
	}
}
