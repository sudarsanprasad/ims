package com.ims.constant;

public enum SourceType {
	FTP("FTP"),
    API("API");
	
	private String description;

	SourceType(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
