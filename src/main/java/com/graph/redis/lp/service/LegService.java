package com.graph.redis.lp.service;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.graph.redis.lp.object.Node;
import com.graph.redis.lp.object.LinkEdge;
import com.graph.redis.lp.repository.RedisGraphRepository;

@Service
public class LegService {
	
	@Autowired
	RedisGraphRepository repository;
	
	public void create(LinkEdge linkEdge) {
		repository.createEdge(linkEdge);
	}
	
	public List<LinkEdge> getLegEdge(LinkEdge linkEdge) {
		ArrayList<LinkEdge> all = (ArrayList<LinkEdge>) repository.getEdgeByFilter(linkEdge);
		ObjectMapper mapper = new ObjectMapper();
		try {
			System.out.println(mapper.writeValueAsString(all));
		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return (List<LinkEdge>) all;	
	}
	
	public void createAll(List<LinkEdge> linkEdges) {
		for(LinkEdge linkEdge : linkEdges) {
			repository.createEdge(linkEdge);
		}
	}

}
