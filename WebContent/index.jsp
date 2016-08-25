<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="project.*" %>
<%@ page import="result.*" %>
<%@ page import="java.util.List" %>
<%@ page import="java.util.Set" %>

<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=BIG5">
<link rel="stylesheet" href="css/styles.css">
<title>Search</title>
</head>
<body>
	<div id="spinner" class="spinner" style="display:none;">
    	<img id="img-spinner" src="img/gears.gif" alt="Loading"/>
	</div>
	 
    <%
    	System.out.println("loading index.jsp");
    
    	SearchResult result = (SearchResult)request.getSession().getAttribute("result");
    	String queryByClusterIdx = (String)request.getAttribute("queryByClusterIdx");
    	String query = (String)request.getAttribute("query");
    	Integer docNum = (Integer)request.getAttribute("docNum");
    %>

    <script language="JavaScript">
    	function showSpinner(){
    		document.getElementById("spinner").style = "display:block";
    	}
    	
    	function selectCluster(idx){
    		document.getElementById("clusterIdx").value = idx;
    		document.getElementById("queryForm").submit();
    	}
	</script>
	
	<div class="header">
		<div class="container">
			<h1 class="header-heading">Web Search Engine Project</h1>
		</div>
	</div>	
	<div class="searchbar">
		<div class="container">
			<form action="QueryServlet" method="post" id="queryForm">
				<table>
					<tr>
						<td>Query Text:</td>
						<td><input type="text" name="query" value="<%=query == null? "":query %>"/></td>
						<td rowspan=2><input type="submit" id="query" value="query" onclick="showSpinner()" /></td>
					</tr>
					<tr>
						<td>Document Number:</td>
						<td>
							<select name="docNum">
								<option value="20" <% if(docNum==null || docNum==20){%>selected<%}%> >20</option>
								<option value="40" <% if(docNum!=null && docNum==40){%>selected<%}%> >40</option>
							</select>
						</td>
					</tr>
				</table>
				<input type="hidden" name="clusterIdx" id="clusterIdx" value="-1"/>
    		</form>
		</div>
    </div>
	<div class="desp">
		<div class="container">
			<% 
				if(docNum != null){
					out.println("<div class=\"des\"><h4>Cluster result for <b>"+ query + "</b> in first "+ docNum +" web pages </h4></div>"); 
				}
			%>
		</div>
	</div>    
	<div class="content">
		<div class="container">
			<div class="manu" style="width:20%">
			<%
				if(result != null){
					List<String> labels = result.getLabels();
					List<Set<Integer>> clusters = result.getClusters();
					for(int i=0; i<labels.size(); i++){
						if(clusters.get(i).size() == 0){ continue; }
						out.println("<h4><a title=\"Show cluster pages\" href=\"#\" onclick=\"selectCluster("+ i +"); return false;\">" 
									+ labels.get(i) + "(" + clusters.get(i).size() + ")" + "</a></h4><br>");
					}
				}
			%>
			</div>
			<div class="main"> 
			<%
				if(result != null){
					List<SearchResultBlock> blocks = result.getBlocks();
					
					// show a particular cluster
					if(queryByClusterIdx != null){
						int clusterIdx = Integer.parseInt(queryByClusterIdx);
						Set<Integer> shownCluster = result.getClusters().get(clusterIdx);
						for(int idx: shownCluster){
							out.println(blocks.get(idx).getBlockContent() + "<br>");
						}
					}else{
						// show all results
						for(SearchResultBlock block : blocks){
							out.println(block.getBlockContent() + "<br>");
						}
					}
				}
			%>
			</div>
		</div>
    </div>
    
</body>
</html>