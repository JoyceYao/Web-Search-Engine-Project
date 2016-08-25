package result;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class SearchResult {
	
	private List<Set<Integer>> clusters = new ArrayList<>();
	
	private List<String> urls = new ArrayList<>();
	
	private List<SearchResultBlock> blocks = new ArrayList<>();
	
	private List<String> labels = new ArrayList<>();

	public List<Set<Integer>> getClusters() {
		return clusters;
	}

	public void setClusters(List<Set<Integer>> clusters) {
		this.clusters = clusters;
	}

	public List<String> getUrls() {
		return urls;
	}

	public void setUrls(List<String> urls) {
		this.urls = urls;
	}

	public List<SearchResultBlock> getBlocks() {
		return blocks;
	}

	public void setBlocks(List<SearchResultBlock> blocks) {
		this.blocks = blocks;
	}
	
	public void addBlock(SearchResultBlock block){
		blocks.add(block);
	}

	public List<String> getLabels() {
		return labels;
	}

	public void setLabels(List<String> labels) {
		this.labels = labels;
	}
	
	public void addLabel(String label){
		labels.add(label);
	}
	
	public void addCluster(Set<Integer> cluster){
		clusters.add(cluster);
	}

}
