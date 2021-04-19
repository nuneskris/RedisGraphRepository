package com.graph.redis.core.object;

import java.lang.reflect.Field;

public abstract class RepositoryEdge {
	public final RepositoryNode getEdgeElement(String nodeElementType) throws IllegalArgumentException, IllegalAccessException {
		for (Field f: this.getClass().getDeclaredFields()) {
			GraphEdgeElementType column = f.getAnnotation(GraphEdgeElementType.class);
			String columnValue = "";
			columnValue = column.value();
			if(columnValue.equals(nodeElementType)) {
				f.setAccessible(true);
				return  (RepositoryNode) f.get(this);
			}
		}
		return null;
	}
	
	public final String getEdgeElementFieldName(String nodeElementType) throws IllegalArgumentException, IllegalAccessException {
		for (Field f: this.getClass().getDeclaredFields()) {
			GraphEdgeElementType column = f.getAnnotation(GraphEdgeElementType.class);
			String columnValue = "";
			columnValue = column.value();
			if(columnValue.equals(nodeElementType)) {
				f.setAccessible(true);
				return f.getName();
			}
		}
		return null;
	}
}
