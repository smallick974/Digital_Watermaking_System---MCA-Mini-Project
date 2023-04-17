package com.digital_watermarking;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * Servlet implementation class DownloadFileHandler
 */
@WebServlet("/DownloadFileHandler")
public class DownloadFileHandler extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public DownloadFileHandler() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		
		//PrintWriter out = response.getWriter();
		HttpSession session = request.getSession();
		String downloadPath=(String) session.getAttribute("file-name");
		System.out.println(downloadPath);
        //read file from server
		File downloadFile=new File(downloadPath);
		FileInputStream downloadFileInputStream = new FileInputStream(downloadFile);

        ServletContext context = getServletContext();
        String mimeType = context.getMimeType(downloadPath);

        response.setContentType(mimeType);
        response.setContentLength((int) downloadFile.length());
        response.setHeader("Content-Disposition", "inline; filename=\""+ downloadFile.getName() + "\"");
            
        OutputStream downloadFileoutStream = response.getOutputStream();
        
        int bytesRead = -1;
        byte[] buffer = new byte[4096];
        while ((bytesRead = downloadFileInputStream.read(buffer)) != -1) {
        	downloadFileoutStream.write(buffer, 0, bytesRead);
        }
        
        downloadFileInputStream.close();
        downloadFileoutStream.close();
	}
		
	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
