package com.digital_watermarking;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.Collection;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.http.Part;

/**
 * Servlet implementation class UploadFileHandler
 */
@WebServlet("/UploadFileHandler")
@MultipartConfig
public class UploadFileHandler extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private String tempPath;
	private String fileName = null;
	private String logoName = null;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public UploadFileHandler() {
		super();
		// TODO Auto-generated constructor stub
	}

	@Override
	public void init() throws ServletException {
		tempPath = getServletContext().getRealPath("/") + "temp";
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		for (Part part : request.getParts()) {
			Collection<String> headers = part.getHeaders("content-disposition");
			for (String header : headers) {
				for (String headerData : header.split(";")) {
					if (headerData.contains("filename")) {
						String tempName = headerData.split("=")[1];
						if (part.getName().equals("file_upload")) {
							fileName = tempName.substring(1, tempName.length() - 1);
							uploadTempFiles(part, fileName);
						} else if (part.getName().equals("logo_upload")) {
							logoName = tempName.substring(1, tempName.length() - 1);
							uploadTempFiles(part, logoName);
						}
					}
				}
			}
		}
		WatermarkHandler wmh = new WatermarkHandler();
		System.out.print("\n\nTest: "+ tempPath+"\n\nFilename: "+fileName);
		wmh.applyWatermark(tempPath + File.separator + fileName, tempPath + File.separator + logoName,request);
		String value=request.getParameter("input_hidden");
		if(value.equals("Watermarked") ){
			request.getRequestDispatcher("HomePage.html").forward(request, response);
		}
		else {
			response.sendRedirect("Download.html?watermarked=true");
		}	
	}

	private void uploadTempFiles(Part part, String fileName) {
		try {
			InputStream is = part.getInputStream();
			BufferedInputStream bis = new BufferedInputStream(is);
			FileOutputStream fos = new FileOutputStream(new File(tempPath + File.separator + fileName));
			int ch = 0;
			while ((ch = bis.read()) != -1) {
				fos.write(ch);
			}
			fos.flush();
			fos.close();
		} 
		catch (Exception e) {

		}
	}
}
