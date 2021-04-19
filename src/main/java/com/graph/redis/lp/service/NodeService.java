package com.graph.redis.lp.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.graph.redis.lp.object.Node;
import com.graph.redis.lp.repository.RedisGraphRepository;

@Service
public class NodeService {
	
	@Autowired
	RedisGraphRepository repository;
	
	public void create(Node node) {
		repository.create(node);
	}

	public void createAll(List<Node> nodes) {
		repository.createAll(nodes);	
	}
	
	
	public List<Node> getAll() {
		@SuppressWarnings("unchecked")
		List<Node> all = (List<Node>) repository.getAll(new Node());
		return all;
	}
}	