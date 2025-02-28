package com.navaidtracker.service;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.springframework.stereotype.Service;
import com.navaidtracker.model.PdfData;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class PdfService {

	public List<PdfData> extractData(String filePath) throws IOException {
	    File file = new File(filePath);
	    PDDocument document = PDDocument.load(file);
	    PDFTextStripper stripper = new PDFTextStripper();
	    String text = stripper.getText(document);
	    document.close();

	    log.info("Extracted text from PDF:\n{}", text);

	    // Split the text into entries – here we assume a newline followed by a 4-digit number starts an entry.
	    String[] entries = text.split("(?=\\n\\d{4}\\s)");

	    List<PdfData> pdfEntries = new ArrayList<>();

	    for (String entry : entries) {
	        log.info("Processing entry:\n{}", entry);

	        PdfData data = new PdfData();
	        data.setNoticeToMarinersNumber(extractNoticeToMarinersNumber(entry));
	        data.setChartNumber(extractChartNumber(entry));
	        data.setAction(extractAction(entry));

	        // For Amend/Insert, extract text up to the coordinates; for Delete we already set a fixed string.
	        data.setActionInfo(extractActionInfo(entry));

	        // Extract coordinates from the entire entry.
	        data.setLatLong(extractLatLong(entry));

	        log.info("Extracted data for entry:");
	        log.info("Notice to Mariners Number: {}", data.getNoticeToMarinersNumber());
	        log.info("Chart Number: {}", data.getChartNumber());
	        log.info("Action: {}", data.getAction());
	        log.info("Action Info: {}", data.getActionInfo());
	        log.info("Lat/Long: {}", data.getLatLong());

	        if (data.getNoticeToMarinersNumber() != null) {
	            pdfEntries.add(data);
	        }
	    }

	    log.info("Final extracted entries: {}", pdfEntries);
	    return pdfEntries;
	}


	// Helper method to extract Notice to Mariners Number (e.g., 1109, 1112)
	private String extractNoticeToMarinersNumber(String text) {
		Pattern pattern = Pattern.compile("^(\\d{4})"); // Matches 4-digit number at the start of the entry
		Matcher matcher = pattern.matcher(text.trim());
		if (matcher.find()) {
			return matcher.group(1);
		}
		return null;
	}

	// Helper method to extract Chart Number
	private String extractChartNumber(String text) {
		Pattern pattern = Pattern.compile("Chart\\s+(\\d+\\s*\\(.*?\\)|\\d+)"); // Matches "Chart 1429 (INT 1403)" or
																				// "Chart 1429"
		Matcher matcher = pattern.matcher(text);
		if (matcher.find()) {
			return matcher.group(1).trim();
		}
		return null;
	}

	// Helper method to extract Action
	private String extractAction(String text) {
		if (text.contains("Delete")) {
			return "Delete";
		} else if (text.contains("Amend")) {
			return "Amend";
		} else if (text.contains("Insert")) {
			return "Insert";
		}
		return null;
	}

	private String extractActionInfo(String text) {
		// For Delete, we use a fixed message.
		if (text.contains("Delete")) {
			return "Delete operation";
		} else if (text.contains("Amend")) {
			// Use a regex with a lookahead for a coordinate pattern.
			Pattern pattern = Pattern.compile("Amend\\s+(.*?)(?=\\d{1,3}°\\s*\\d{1,2}[´']·\\d+)", Pattern.DOTALL);
			Matcher matcher = pattern.matcher(text);
			if (matcher.find()) {
				return matcher.group(1).trim();
			}
		} else if (text.contains("Insert")) {
			Pattern pattern = Pattern.compile("Insert\\s+(.*?)(?=\\d{1,3}°\\s*\\d{1,2}[´']·\\d+)", Pattern.DOTALL);
			Matcher matcher = pattern.matcher(text);
			if (matcher.find()) {
				return matcher.group(1).trim();
			}
		}
		return null;
	}

	private String extractLatLong(String text) {
	    Pattern pattern = Pattern.compile("(\\d{1,3}°\\s*\\d{1,2}[´']·\\d+\\.?\\d*[NSEW]\\.,\\s*\\d{1,3}°\\s*\\d{1,2}[´']·\\d+\\.?\\d*[NSEW]\\.)");
	    Matcher matcher = pattern.matcher(text);
	    if (matcher.find()) {
	        return matcher.group(1).trim();
	    }
	    return null;
	}

}