package com.ims.dto;

import java.util.List;

import lombok.Data;

@Data
public class ResultDto {
	private String need_tool;
	private String text;
	private List<TopicDto> topics;

}
