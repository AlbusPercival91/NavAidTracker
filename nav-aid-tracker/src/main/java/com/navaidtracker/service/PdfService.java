package com.navaidtracker.service;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.springframework.stereotype.Service;
import com.navaidtracker.model.PdfData;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
public class PdfService {

	// Extract data from the PDF
	public List<PdfData> extractData(String filePath) throws IOException {
		// Open the PDF file
		File file = new File(filePath);
		PDDocument document = PDDocument.load(file);

		// Create a PDFTextStripper to extract text from the PDF
		PDFTextStripper stripper = new PDFTextStripper();

		// Extract the text from the PDF
		String text = stripper.getText(document);
		document.close(); // Always close the document when done

		// Process the extracted text and extract relevant data
		List<PdfData> pdfEntries = new ArrayList<>();
		PdfData data = new PdfData();

		data.setNoticeToMarinersNumber(extractNoticeToMarinersNumber(text));
		data.setChartNumber(extractChartNumber(text));
		data.setAction(extractAction(text));
		data.setActionInfo(extractActionInfo(text));
		data.setLatLong(extractLatLong(text));

		pdfEntries.add(data); // Add the processed data to the list

		return pdfEntries; // Return the list of processed data
	}

	// Helper methods to extract specific data using regex or custom logic
	private String extractNoticeToMarinersNumber(String text) {
		// Your logic to extract the "Notice to Mariners Number" from the text
		return "Extracted Number"; // Placeholder
	}

	private String extractChartNumber(String text) {
		// Your logic to extract the "Chart Number" from the text
		return "Extracted Chart Number"; // Placeholder
	}

	private String extractAction(String text) {
		// Your logic to extract the "Action" (Amend, Insert, etc.) from the text
		return "Extracted Action"; // Placeholder
	}

	private String extractActionInfo(String text) {
		// Your logic to extract the "Action Info" from the text
		return "Extracted Action Info"; // Placeholder
	}

	private String extractLatLong(String text) {
		// Your logic to extract the "Lat/Long" from the text
		return "Extracted Lat/Long"; // Placeholder
	}
}
