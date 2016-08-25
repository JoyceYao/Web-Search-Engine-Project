package result;

public class SearchResultBlock {

	String url;
	String blockContent;
	
	public SearchResultBlock(String url, String blockContent){
		this.url = url;
		this.blockContent = blockContent;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getBlockContent() {
		return blockContent;
	}

	public void setBlockContent(String blockContent) {
		this.blockContent = blockContent;
	}
	
}
