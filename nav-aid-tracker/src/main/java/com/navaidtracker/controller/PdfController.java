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
import java.io.File;
import java.io.IOException;
import java.util.List;

@Controller
public class PdfController {

    @Autowired
    private PdfService pdfService;

    // Show the upload page
    @GetMapping("/")
    public String uploadPage() {
        return "upload";  // This will return the upload.html page
    }

    // Handle file upload and PDF processing
    @PostMapping("/processPdf")
    public String processPdf(@RequestParam("file") MultipartFile file, Model model) {
        if (file.isEmpty()) {
            model.addAttribute("error", "Please select a file to upload.");
            return "upload"; // Return to upload form with error message
        }

        try {
            // Save file temporarily in the system's temp directory
            File tempFile = File.createTempFile("uploaded-", ".pdf");

            // Transfer the uploaded file to the temporary file
            file.transferTo(tempFile);

            // Process the file using the PdfService
            List<PdfData> pdfEntries = pdfService.extractData(tempFile.getAbsolutePath());
            model.addAttribute("entries", pdfEntries);  // Add processed data to the model

            // Delete the temporary file after processing
            tempFile.delete();

        } catch (IOException e) {
            model.addAttribute("error", "Error processing file: " + e.getMessage());
            return "upload"; // Return to upload form with error message
        }

        return "pdfresult";  // Return the pdfresult.html page with results
    }
}
