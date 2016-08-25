package servlet;

import java.io.File;
import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import project.Main;
import result.SearchResult;

/**
 * Servlet implementation class QueryServlet
 */
@WebServlet("/QueryServlet")
public class QueryServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public QueryServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		HttpSession session = request.getSession();
        String query = request.getParameter("query");
        String contextPath = getServletContext().getRealPath(File.separator); 
        String docNumStr = request.getParameter("docNum");
        int docNum = docNumStr == null? 20: Integer.parseInt(docNumStr);
        String clusterIdx = request.getParameter("clusterIdx");
        
        Main main = new Main();
        SearchResult result = null;
        
        if(!"-1".equals(clusterIdx)){
        	request.setAttribute("queryByClusterIdx", clusterIdx);
        	result = (SearchResult)session.getAttribute("result");
        }else{        
        	if (query != null) {
        		try {
        			result = main.doSeach(query, docNum, contextPath);
        		} catch (Exception e) {
        			// TODO Auto-generated catch block
        			e.printStackTrace();
        		}
        	}
        }
        
        session.setAttribute("result", result);
        request.setAttribute("query", query);
        request.setAttribute("docNum", docNum);
        request.getRequestDispatcher("/index.jsp").forward(request, response);
	}

}
