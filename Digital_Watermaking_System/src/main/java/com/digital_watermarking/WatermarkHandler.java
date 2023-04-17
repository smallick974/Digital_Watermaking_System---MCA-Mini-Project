package com.digital_watermarking;
import java.io.FileOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;

public class WatermarkHandler {

	public WatermarkHandler() {
		// TODO Auto-generated constructor stub
	}

	public void applyWatermark(String filePath, String logoPath,HttpServletRequest request) {
		try {
			String baseURL = filePath.substring(0, filePath.lastIndexOf("\\") + 1);
			String tempFileName = filePath.substring(filePath.lastIndexOf("\\"));
			String fileName = baseURL + tempFileName.substring(1, tempFileName.lastIndexOf(".")) + "_watermarked"+ tempFileName.substring(tempFileName.lastIndexOf("."));
			 HttpSession session = request.getSession();
			 session.setAttribute("file-name",fileName);
			 System.out.print("Filename111: "+fileName);
			PdfReader reader = new PdfReader(filePath);
			PdfStamper stamper = new PdfStamper(reader, new FileOutputStream(fileName)); 
			
			Image img = Image.getInstance(logoPath);
			float w = img.getScaledWidth();
			float h = img.getScaledHeight();
			
			PdfContentByte over; 
			Rectangle pagesize;
			float x, y; 
			int n =reader.getNumberOfPages();
			
			for (int i = 1; i<= n; i++) {
			  // get page size and position 
				pagesize = reader.getPageSizeWithRotation(i); 
				x = (pagesize.getLeft() + pagesize.getRight()) / 2;
				y = (pagesize.getTop() + pagesize.getBottom()) / 2; over = stamper.getOverContent(i);
			   over.saveState();
			   
			 // set transparency 
			   PdfGState state = new PdfGState();
			  state.setFillOpacity(0.4f); over.setGState(state);
			  
			 // add watermark text and image 
			 over.addImage(img, w, 0, 0, h, x - (w / 2), y- (h / 2));
			 over.restoreState(); 
			 } 
			stamper.close(); 
			reader.close();
    	  //  out.close();
		} 
		catch (Exception e) {
			System.out.print("Exception: "+e);
		}
	}
}
