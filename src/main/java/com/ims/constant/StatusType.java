package com.ims.constant;

public enum StatusType {
	OPEN("OPEN"),
	INPROGRESS("IN PROGRESS"),
    COMPLETED("COMPLETED"),
    ABORTED("ABORTED");
	
	private String description;

	StatusType(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
