package com.graph.redis.lp.object;

import com.graph.redis.core.object.GraphEdgeElementType;
import com.graph.redis.core.object.RepositoryEdge;

public class LinkEdge extends RepositoryEdge {
	
	@GraphEdgeElementType ("fromNode")
	private Node origin;
	
	@GraphEdgeElementType ("toNode")
	private Node destination;
	
	@GraphEdgeElementType ("edge")
	private Link link;
	
	public Node getOrigin() {
		return origin;
	}
	public void setOrigin(Node origin) {
		this.origin = origin;
	}
	public Link getLink() {
		return link;
	}
	public void setLink(Link link) {
		this.link = link;
	}
	public Node getDestination() {
		return destination;
	}
	public void setDestination(Node destination) {
		this.destination = destination;
	}
}
