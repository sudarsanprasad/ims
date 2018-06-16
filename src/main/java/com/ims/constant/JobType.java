package com.ims.constant;

public enum JobType {
	FORECAST("forecast"),
	KR("kr");
	
	private String description;

	JobType(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
