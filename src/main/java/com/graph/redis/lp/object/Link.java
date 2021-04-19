package com.graph.redis.lp.object;

import com.graph.redis.core.object.BaseGraphPart;

public class Link  extends BaseGraphPart{
	

	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}

	public String getDay() {
		return day;
	}
	public void setDay(String day) {
		this.day = day;
	}
	public String getFinalDestination() {
		return finalDestination;
	}
	public void setFinalDestination(String finalDestination) {
		this.finalDestination = finalDestination;
	}
	private String type;
	private String finalDestination;
	private String day;

}
