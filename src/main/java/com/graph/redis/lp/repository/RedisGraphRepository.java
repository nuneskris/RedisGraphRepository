package com.graph.redis.lp.repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.graph.redis.core.object.Constants;
import com.graph.redis.core.object.RepositoryEdge;
import com.graph.redis.core.object.RepositoryNode;
import com.redislabs.redisgraph.Record;
import com.redislabs.redisgraph.ResultSet;
import com.redislabs.redisgraph.graph_entities.Edge;
import com.redislabs.redisgraph.graph_entities.Node;
import com.redislabs.redisgraph.impl.api.RedisGraph;

import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;
import redis.clients.jedis.Protocol;

@Component
public class RedisGraphRepository implements ObjectRepository{
	
	 private static final Logger LOGGER=LoggerFactory.getLogger(RedisGraphRepository.class);

	private RedisGraph graph;

	private static final String graphName = "NetworkGraph";

	public RedisGraphRepository() {
		JedisPool pool = new JedisPool(new JedisPoolConfig(), "redis-13991.c60.us-west-1-2.ec2.cloud.redislabs.com", 13991, Protocol.DEFAULT_TIMEOUT, "g0ZKzWw07XIFccRjcRrEOmlW23ZeHuPu");
		this.graph = new RedisGraph(pool);
	}

	@Override
	public void create(RepositoryNode repositoryNode) {

		String query = " CREATE ";
		query += "(:";
		query += repositoryNode.getClass().getSimpleName();
		query +=  " " ;
		query += repositoryNode.getCypherCreateString() + " )";
		LOGGER.info(query);
		graph.query(graphName,query); 
	}


	private String buildEdgeString(RepositoryNode repositoryEdgeNode, RepositoryNode origin, RepositoryNode destiination) {
		String query = "";
		query += " MATCH ";
		query += "(f:" + origin.getClass().getSimpleName() + ")";
		query += ",";
		query += "(t:" + destiination.getClass().getSimpleName() + ") ";

		query += " WHERE ";
		query += origin.getCypherFilterString("f");
		query += " AND ";
		query += destiination.getCypherFilterString("t");

		query += " CREATE ";
		query += "(f)-[rel:" + repositoryEdgeNode.getClass().getSimpleName() + " " ;
		query += repositoryEdgeNode.getCypherCreateString();
		query += "]->(t)";	
		LOGGER.info(query);
		return query;
	}


	@Override
	public void createEdge(RepositoryEdge repositoryEdge) {
		try {
			String query =  buildEdgeString(repositoryEdge.getEdgeElement(Constants.EDGE_NODE), 
					repositoryEdge.getEdgeElement(Constants.FROM_NODE), repositoryEdge.getEdgeElement(Constants.TO_NODE));
			graph.query(graphName,query);
		} catch (IllegalArgumentException | IllegalAccessException | SecurityException e) {
			LOGGER.error(graphName, e);
		} 
	}

	@Override
	public List<? extends RepositoryEdge> getEdgeByFilter(RepositoryEdge repositoryEdge){
		ArrayList<RepositoryEdge> ja = new ArrayList<RepositoryEdge>();
		try {
			RepositoryNode fromNode = repositoryEdge.getEdgeElement(Constants.FROM_NODE);
			RepositoryNode toNode = repositoryEdge.getEdgeElement(Constants.TO_NODE);
			RepositoryNode edgeObject   = repositoryEdge.getEdgeElement(Constants.EDGE_NODE);
			String query = "";
			query += " MATCH ";
			query += "(from:" + fromNode.getClass().getSimpleName() + " ";
			query += fromNode.getCypherCreateString() + ")";

			query += "-[";
			query += "rel:" + edgeObject.getClass().getSimpleName() + " ";
			query += edgeObject.getCypherCreateString() + " ";
			query += "]->";

			query += "(to:" + toNode.getClass().getSimpleName() + " ";
			query += toNode.getCypherCreateString() + ")"+ " ";
			query += "RETURN from,to,rel";

			LOGGER.info(query);

			ResultSet resultSet = graph.query(graphName,query);

			ObjectMapper mapper = new ObjectMapper();
			
			while(resultSet.hasNext()) {
				Record record = resultSet.next();
				Node node = record.getValue(0);

				Set<String> propertyNames = node.getEntityPropertyNames();
				ObjectNode from = mapper.createObjectNode();
				for(String property : propertyNames) {
					from.put(property, (String)node.getProperty(property).getValue());
				}

				node = record.getValue(1);
				propertyNames = node.getEntityPropertyNames();
				ObjectNode to = mapper.createObjectNode();
				for(String property : propertyNames) {
					to.put(property, (String)node.getProperty(property).getValue());
				}

				Edge edge = record.getValue(2);
				propertyNames = edge.getEntityPropertyNames();
				ObjectNode rel = mapper.createObjectNode();
				for(String property : propertyNames) {
					rel.put(property, (String)edge.getProperty(property).getValue());
				}
				
				

				ObjectNode jo = mapper.createObjectNode();
				jo.set(repositoryEdge.getEdgeElementFieldName(Constants.FROM_NODE), from);
				jo.set(repositoryEdge.getEdgeElementFieldName(Constants.TO_NODE), to);
				jo.set(repositoryEdge.getEdgeElementFieldName(Constants.EDGE_NODE), rel);

				RepositoryEdge edgeRE = mapper.treeToValue(jo, repositoryEdge.getClass());
				
				

				ja.add(edgeRE);
			}
			} catch (JsonProcessingException | IllegalArgumentException | IllegalAccessException e) {
				// TODO Auto-generated catch block
				LOGGER.error(graphName, e);
			}
		return ja;
	}

	@Override
	public List<? extends RepositoryNode> getAll(RepositoryNode repositoryNode) {
		String query = "MATCH ";
		query += "(get: " + repositoryNode.getClass().getSimpleName() + ")";

		String filter = repositoryNode.getCypherFilterString("get");
		if(!filter.trim().isEmpty()) {
			query += " WHERE ";
			query += filter;
		}

		query +=  " RETURN ";
		query += repositoryNode.getCypherGetAllString();

		ResultSet resultSet = graph.query(graphName,query);
		ObjectMapper mapper = new ObjectMapper();
		List<RepositoryNode> ja = new ArrayList<RepositoryNode>();
		while(resultSet.hasNext()) {
			Record record = resultSet.next();
			ObjectNode jo = mapper.createObjectNode();
			for(String key : record.keys()) {
				jo.put(key, (String)record.getValue(key)); 
			}
			try {
				RepositoryNode object = mapper.treeToValue(jo, repositoryNode.getClass());
				ja.add(object);
			} catch (JsonProcessingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return ja;
	}



	@Override
	public void createAll(List<? extends RepositoryNode> repositoryNodes) {
		String query = "";
		for(RepositoryNode repositoryNode : repositoryNodes) {
			query += "CREATE ";
			query += "(:";
			query += repositoryNode.getClass().getSimpleName();
			query +=  " " ;
			query += repositoryNode.getCypherCreateString() + " )";
		}
		graph.query(graphName, query); 
	}

	@Override
	public void createEdges(List<? extends RepositoryEdge> repositoryEdges) {
		// TODO Auto-generated method stub

	}

}
