package com.graph.redis.lp.object;
 
import com.graph.redis.core.object.BaseGraphPart;



public class Node extends BaseGraphPart{
	public String getNumber() {
		return number;
	}
	public void setNumber(String number) {
		this.number = number;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getRegn() {
		return regn;
	}
	public void setRegn(String regn) {
		this.regn = regn;
	}
	private String number;
	private String name;
	private String type;
	private String regn;

}
