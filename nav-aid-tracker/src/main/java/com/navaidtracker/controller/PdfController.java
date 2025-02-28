package com.navaidtracker.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import com.navaidtracker.model.PdfData;
import com.navaidtracker.service.PdfService;
import lombok.extern.slf4j.Slf4j;
import java.io.File;
import java.io.IOException;
import java.util.List;

@Slf4j
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
			File tempFile = File.createTempFile("uploaded-", ".pdf");
			file.transferTo(tempFile);

			List<PdfData> pdfEntries = pdfService.extractData(tempFile.getAbsolutePath());
			log.info("Extracted entries: {}", pdfEntries);

			model.addAttribute("entries", pdfEntries);
			tempFile.delete();

		} catch (IOException e) {
			log.error("Error processing file: {}", e.getMessage(), e);
			model.addAttribute("error", "Error processing file: " + e.getMessage());
			return "upload";
		}

		return "pdfresult";
	}
}
