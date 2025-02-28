package com.navaidtracker.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import com.navaidtracker.model.PdfEntry;
import com.navaidtracker.service.PdfService;
import java.io.File;
import java.io.IOException;
import java.util.List;

@Controller
public class PdfController {

	@Autowired
	private PdfService pdfService;

	@GetMapping("/")
	public String uploadPage() {
		return "upload";
	}

	@PostMapping("/processPdf")
	public String processPdf(@RequestParam("file") MultipartFile file, Model model) {
		if (file.isEmpty()) {
			model.addAttribute("error", "Please select a file to upload.");
			return "upload";
		}

		try {
			// Save file temporarily
			File tempFile = File.createTempFile("uploaded-", ".pdf");
			file.transferTo(tempFile);

			// Process the file
			List<PdfEntry> entries = pdfService.extractData(tempFile.getAbsolutePath());
			model.addAttribute("entries", entries);

			// Delete temp file after processing
			tempFile.delete();

		} catch (IOException e) {
			model.addAttribute("error", "Error processing file: " + e.getMessage());
			return "upload";
		}

		return "pdfresult";
	}
}
