package com.navaidtracker.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class PdfEntry {
	private String noticeNumber;
	private String country;
	private String description;
	private String chartNumber;
	private String action;
	private String actionInfo;
	private String latitude;
	private String longitude;
}