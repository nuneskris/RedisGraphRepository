package com.graph.redis.lp.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.graph.redis.lp.object.LinkEdge;
import com.graph.redis.lp.object.Node;
import com.graph.redis.lp.service.EdgeService;
import com.graph.redis.lp.service.NodeService;



@RestController
@RequestMapping("/api/graph")
public class NetworkGraphController {
	


	@Autowired
	NodeService nodeService;

	@Autowired
	EdgeService edgeService;

	@PostMapping(path = "/Link",consumes = "application/json", produces = "application/json")
	public ResponseEntity<String> postLeg (@RequestBody LinkEdge linkEdge)  {
		edgeService.create(linkEdge);
		return ResponseEntity.ok("");
	}
	
	@PostMapping(path = "/Links",consumes = "application/json", produces = "application/json")
	public ResponseEntity<String> postLegs (@RequestBody List<LinkEdge> linkEdges)  {
		edgeService.createAll(linkEdges);
		return ResponseEntity.ok("");
	}

	@GetMapping("/Links")
	public ResponseEntity<List<LinkEdge>> getLegs(@RequestParam(name = "query") String legEdgeString) throws JsonMappingException, JsonProcessingException {
		ObjectMapper objectMapper = new ObjectMapper();
		LinkEdge lpe = objectMapper.readValue(legEdgeString, LinkEdge.class);

		List<LinkEdge> ja = edgeService.getLegEdge(lpe);
		return ResponseEntity.accepted().body(ja);
	}
	

	@PostMapping(path = "/Node",consumes = "application/json", produces = "application/json")
	public ResponseEntity<String> postFacility (@RequestBody Node node)  {
		nodeService.create(node);
		return ResponseEntity.ok("");
	}

	@PostMapping(path = "/Nodes",consumes = "application/json", produces = "application/json")
	public ResponseEntity<String> postFacilities(@RequestBody List<Node> nodes)  {
		nodeService.createAll(nodes);
		return ResponseEntity.ok("Hello World!");
	}

	@GetMapping("/Node")
	public ResponseEntity<List<Node>> getAll() {
		List<Node> ja = nodeService.getAll();
		return ResponseEntity.accepted().body(ja);
	}

	@GetMapping("/ping")
	public ResponseEntity<String> getPing() {
		return ResponseEntity.accepted().body("I am awake");
	}
}