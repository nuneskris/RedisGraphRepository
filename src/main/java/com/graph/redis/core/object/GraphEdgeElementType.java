package com.graph.redis.core.object;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface GraphEdgeElementType {
	String value();
}