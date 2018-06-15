package com.ims.constant;

public enum FileType {
	CSV("csv"),
	XLX("xlx"),
    XLSX("xlsx"),
    XLS("xls");
	
	private String description;

	FileType(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
