package com.graph.redis.lp.service;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.graph.redis.lp.object.LinkEdge;
import com.graph.redis.lp.repository.RedisGraphRepository;

@Service
public class EdgeService {
	
	@Autowired
	RedisGraphRepository repository;
	
	public void create(LinkEdge linkEdge) {
		repository.createEdge(linkEdge);
	}
	
	public List<LinkEdge> getLegEdge(LinkEdge linkEdge) {
		@SuppressWarnings("unchecked")
		ArrayList<LinkEdge> all = (ArrayList<LinkEdge>) repository.getEdgeByFilter(linkEdge);
		return (List<LinkEdge>) all;	
	}
	
	public void createAll(List<LinkEdge> linkEdges) {
		for(LinkEdge linkEdge : linkEdges) {
			repository.createEdge(linkEdge);
		}
	}

}
