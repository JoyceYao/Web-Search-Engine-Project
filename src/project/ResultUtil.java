package project;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import result.SearchResult;

public class ResultUtil {

	public static void checkDupLabelAndStoreResult(List<Set<Integer>> clusters, List<String> labels, SearchResult searchResult){
		// check whether there are duplicate labels,
		// if found, merge them into the same cluster
		Map<String, Set<Integer>> labelsMap = new HashMap<>();
		for(int i=0; i<labels.size(); i++){
			String thisLabel = labels.get(i);
			if(labelsMap.containsKey(thisLabel)){
				labelsMap.get(thisLabel).addAll(clusters.get(i));
			}else{
				labelsMap.put(thisLabel, clusters.get(i));
			}
		}
		
		// store the labels and clusters into searchResult
		Set<String> distinctLabels = labelsMap.keySet();
		for(String label: distinctLabels){
			searchResult.addLabel(label);
			searchResult.addCluster(labelsMap.get(label));
		}
	}
}
