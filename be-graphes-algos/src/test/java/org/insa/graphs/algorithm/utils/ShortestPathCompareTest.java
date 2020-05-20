package org.insa.graphs.algorithm.utils;

import java.awt.List;
import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import org.insa.graphs.algorithm.AbstractSolution.Status;
import org.insa.graphs.algorithm.ArcInspector;
import org.insa.graphs.algorithm.ArcInspectorFactory;
import org.insa.graphs.algorithm.shortestpath.*;
import org.insa.graphs.model.*;
import org.insa.graphs.model.io.*;

import org.junit.Assume;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameter;
import org.junit.runners.Parameterized.Parameters;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;

public class ShortestPathCompareTest {
	
	
	
	protected ShortestPathData dataShortest[] = new ShortestPathData[3];
	protected ShortestPathData dataFastest[] = new ShortestPathData[3];
	
	@Before
	public void Construct() throws Exception{
	
	
	final Graph graphHG, graphB, graphPoly;

	final String graphHauteGaronne ="/Users/clemd/BE_GRAPHE/maps/haute-garonne.mapgr";
	final String graphBelgium ="/Users/clemd/BE_GRAPHE/maps/belgium.mapgr";
	final String graphPolynesie ="/Users/clemd/BE_GRAPHE/maps/french-polynesia.mapgr";

	
	try(GraphReader readerHG = new BinaryGraphReader(new DataInputStream(new BufferedInputStream(new FileInputStream(graphHauteGaronne)))))
	{
		graphHG=readerHG.read();
	}
		
	try(final GraphReader readerB = new BinaryGraphReader(new DataInputStream(new BufferedInputStream(new FileInputStream(graphBelgium)))))
	{
		graphB=readerB.read();
	}
	
	try(GraphReader readerPoly = new BinaryGraphReader(new DataInputStream(new BufferedInputStream(new FileInputStream(graphPolynesie)))))
	{
		graphPoly=readerPoly.read();
	}
	
	final ArcInspector allRoadsLength=ArcInspectorFactory.getAllFilters().get(0);
	final ArcInspector carsAndLength=ArcInspectorFactory.getAllFilters().get(1);
	final ArcInspector allRoadsTime=ArcInspectorFactory.getAllFilters().get(2);

	dataShortest[0] = new ShortestPathData(graphHG, graphHG.get(10991), graphHG.get(8920), allRoadsLength);
	dataShortest[1] = new ShortestPathData(graphHG, graphHG.get(10991), graphHG.get(10991), allRoadsLength);
	dataShortest[2] = new ShortestPathData(graphPoly, graphPoly.get(1906), graphPoly.get(13993), allRoadsLength);
	
	dataFastest[0] = new ShortestPathData(graphHG, graphHG.get(10991), graphHG.get(8920), allRoadsTime);
	dataFastest[1] = new ShortestPathData(graphHG, graphHG.get(10991), graphHG.get(10991), allRoadsTime);
	dataFastest[2] = new ShortestPathData(graphHG, graphHG.get(10991), graphHG.get(8920), allRoadsTime);
	
	}
	
	@Test
	public void testcompareDB() {
		
		//chemin classique long HG
		DijkstraAlgorithm dijkstra1 = new DijkstraAlgorithm(dataShortest[0]);
		ShortestPathSolution solutionD = dijkstra1.run();
		BellmanFordAlgorithm BF1 = new BellmanFordAlgorithm(dataShortest[0]);
		ShortestPathSolution solutionBF = BF1.run();
		
		//comparaison des coûts des solutions
		
		assertTrue(solutionD.getPath().isValid());
		assertTrue(solutionBF.getPath().isValid());
		assertEquals(solutionD.getPath().getLength(),solutionBF.getPath().getLength(),0.0001);
		
		//chemin classique avec la durée
		
		dijkstra1=new DijkstraAlgorithm(dataFastest[0]);
		solutionD=dijkstra1.run();
		BF1=new BellmanFordAlgorithm(dataFastest[0]);
		solutionBF=BF1.run();
		
		assertTrue(solutionD.getPath().isValid());
		assertTrue(solutionBF.getPath().isValid());
		assertEquals(solutionD.getPath().getMinimumTravelTime(),solutionBF.getPath().getMinimumTravelTime(),0.0001);
		
		//chemin null HG (origine=end)
		
		dijkstra1=new DijkstraAlgorithm(dataShortest[1]);
		solutionD=dijkstra1.run();
		BF1=new BellmanFordAlgorithm(dataShortest[1]);
		solutionBF=BF1.run();
		
		assertEquals(solutionD.getPath(),null);
		assertEquals(solutionD.getStatus(),Status.INFEASIBLE);
		assertEquals(solutionBF.getPath(),null);
		assertEquals(solutionBF.getStatus(),Status.INFEASIBLE);
		
		
		//chemin inexistant
		
		dijkstra1=new DijkstraAlgorithm(dataShortest[2]);
		solutionD=dijkstra1.run();
		BF1=new BellmanFordAlgorithm(dataShortest[2]);
		solutionBF=BF1.run();
		
		assertEquals(solutionD.getPath(),null);
		assertEquals(solutionD.getStatus(),Status.INFEASIBLE);
		assertEquals(solutionBF.getPath(),null);
		assertEquals(solutionBF.getStatus(),Status.INFEASIBLE);
		
		
		
	}

}
