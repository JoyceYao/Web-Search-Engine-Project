# Web-Search-Engine-Project
A search result clustering system
<p>
The clustering process:
<p>
![cluster process](/img/cluster_process.png?raw=true)

<p>
What this system do:
<ol>(1) The user inputs search key words.</ol>
<ol>(2) The system requests a Google search, and parses the first 20 or 40 search results.</ol>
<ol>(3) The system calculates clusters using TF-IDF and bisecting K means.</ol>
<ol>(4) The system calculates a representive label for each cluster by querying wordNet and Wikipedia.</ol>
<ol>(5) The system shows the search result clusters and labels to the user.</ol>
<ol>(6) If the user clicks on the cluster name, the system shows the results for this particular cluster.</ol>
<ol>(7) If the user clicks on the search result URL, the systme opens the web page.</ol>
<p>
<p>
The cluster result:
![cluster result](/img/cluster_result.png)
