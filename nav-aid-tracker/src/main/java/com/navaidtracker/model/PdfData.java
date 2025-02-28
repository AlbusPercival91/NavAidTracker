package com.navaidtracker.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PdfData {
	private String noticeToMarinersNumber;
	private String chartNumber;
	private String action;
	private String actionInfo;
	private String latLong;

	@Override
	public String toString() {
		return "PdfData{" + "noticeToMarinersNumber='" + noticeToMarinersNumber + '\'' + ", chartNumber='" + chartNumber
				+ '\'' + ", action='" + action + '\'' + ", actionInfo='" + actionInfo + '\'' + ", latLong='" + latLong
				+ '\'' + '}';
	}
}