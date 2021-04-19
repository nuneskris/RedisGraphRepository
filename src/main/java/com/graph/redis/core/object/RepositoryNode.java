package com.graph.redis.core.object;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;


public abstract class RepositoryNode {
	@JsonIgnore
	private String cypherCreateString;
	
	@JsonIgnore
	private String propertyNames;
	
	@JsonIgnore
	private String cypherGetAllString;
	
	public final String getCypherCreateString() {
		return getObjectString();
	}
	
	public final String getCypherGetAllString() {
		return buildCypherGetAllString();
	}

	public final List<String> getPropertyNames(){
		Class<? extends Object> c1 = this.getClass();
		Field[] fields = c1.getDeclaredFields();
		List<String> properties = new ArrayList<String>();
		try {
			for (int i = 0; i < fields.length; i++) {
				String name = fields[i].getName();
				properties.add(name);

			}
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		}
		return properties;
	}
	
	
	
	private String buildCypherGetAllString() {
		String cypherString = "";
		for(String propName : this.getPropertyNames()) {
			cypherString += " get."+ propName +  " as " + propName ;
			cypherString += ",";
		}
		cypherString = cypherString.replaceAll(",$"," ");
		return cypherString;
	}
	
	
	
	private String getObjectString() {
		Class<? extends Object> c1 = this.getClass();
		String os = "{";
		Field[] fields = c1.getDeclaredFields();
		try {
			for (int i = 0; i < fields.length; i++) {
				String name = fields[i].getName();
				fields[i].setAccessible(true);
				Object value;
				value = fields[i].get(this);
				if(value != null) {
					os += name + ":'" + value + "'";    
					os += ",";    
				}
			}
			os = os.replaceAll(",$"," ");
		} catch (IllegalArgumentException | IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		os += "}";
		return os;
	}
	
	private String buildCypherFilterString(String ns) {
		Class<? extends Object> c1 = this.getClass();
		String os = "";
		Field[] fields = c1.getDeclaredFields();
		try {
			for (int i = 0; i < fields.length; i++) {
				String name = fields[i].getName();
				fields[i].setAccessible(true);
				Object value;
				value = fields[i].get(this);
				if(value != null) {
					os += ns+"."+name + " = '" + value + "'";
					os += " AND ";    
				}
				os = os.replaceAll(" AND $"," ");
			}
		} catch (IllegalArgumentException | IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println(os);
		return os;
	}

	public final String getCypherFilterString(String ns) {
		return buildCypherFilterString(ns);
	}
	
}
