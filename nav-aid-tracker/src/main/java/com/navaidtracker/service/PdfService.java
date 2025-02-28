package com.navaidtracker.service;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.springframework.stereotype.Service;
import com.navaidtracker.model.PdfEntry;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class PdfService {

	public List<PdfEntry> extractData(String filePath) {
		List<PdfEntry> entries = new ArrayList<>();

		try {
			File file = new File(filePath);
			PDDocument document = PDDocument.load(file);
			PDFTextStripper stripper = new PDFTextStripper();
			String text = stripper.getText(document);
			document.close();

			// Regex Patterns
			Pattern pattern = Pattern.compile(
					"(\\d{4})\\s.*?-\\s(.*?)\\s-\\s(.*?)\\.\\nSource:.*?\\nChart\\s+(\\d+) .*?\\n(\\w+)\\s+(.*?)\\s+(\\d{2}°\\s\\d{2}´·\\d+\\w),\\s(\\d{2}°\\s\\d{2}´·\\d+\\w)");

			Matcher matcher = pattern.matcher(text);

			while (matcher.find()) {
				PdfEntry entry = new PdfEntry(matcher.group(1), // Notice Number
						matcher.group(2), // Country
						matcher.group(3), // Description
						matcher.group(4), // Chart Number
						matcher.group(5), // Action (Delete, Amend, etc.)
						matcher.group(6), // Action Info
						matcher.group(7), // Latitude
						matcher.group(8) // Longitude
				);
				entries.add(entry);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

		return entries;
	}
}
