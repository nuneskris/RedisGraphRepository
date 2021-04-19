package com.graph.redis.lp.repository;

import java.util.List;

import com.fasterxml.jackson.databind.node.ObjectNode;
import com.graph.redis.core.object.RepositoryEdge;
import com.graph.redis.core.object.RepositoryNode;

public interface ObjectRepository {

	public List<? extends RepositoryNode> getAll(RepositoryNode repositoryNode);

	public void create(RepositoryNode repositoryNode);
	
	public void createAll(List<? extends RepositoryNode> repositoryNodes);


	public void createEdges(List<? extends RepositoryEdge> repositoryEdges);

	void createEdge(RepositoryEdge repositoryEdge);

	List<? extends RepositoryEdge> getEdgeByFilter(RepositoryEdge repositoryEdge);
	
	

}
