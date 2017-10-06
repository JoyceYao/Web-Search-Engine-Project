# Web-Search-Engine-Project
  A search result clustering system

## The clustering process:
![cluster process](/img/cluster_process.png?raw=true)


## What this system do:
1. The user inputs search key words.
2. The system requests a Google search, and parses the first 20 or 40 search results.
3. The system calculates clusters using TF-IDF and bisecting K means.
4. The system calculates a representive label for each cluster by querying wordNet and Wikipedia.
5. The system shows the search result clusters and labels to the user.
6. If the user clicks on the cluster name, the system shows the results for this particular cluster.
7. If the user clicks on the search result URL, the system opens the web page.


## The cluster result:
![cluster result](/img/cluster_result.png?raw=true)
