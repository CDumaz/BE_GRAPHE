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
	
	
	
	protected ShortestPathData dataShortest[] = new ShortestPathData[5];
	protected ShortestPathData dataFastest[] = new ShortestPathData[3];
	
	@Before
	public void Construct() throws Exception{
	
	
	final Graph graphHG, graphB, graphPoly;

	final String graphHauteGaronne ="/Users/clemd/BE_GRAPHE/maps/haute-garonne.mapgr";
	final String graphBretagne ="/Users/clemd/BE_GRAPHE/maps/Bretagne.mapgr";
	final String graphPolynesie ="/Users/clemd/BE_GRAPHE/maps/french-polynesia.mapgr";

	
	try(GraphReader readerHG = new BinaryGraphReader(new DataInputStream(new BufferedInputStream(new FileInputStream(graphHauteGaronne)))))
	{
		graphHG=readerHG.read();
	}
		
	try(final GraphReader readerB = new BinaryGraphReader(new DataInputStream(new BufferedInputStream(new FileInputStream(graphBretagne)))))
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
	dataShortest[3] = new ShortestPathData(graphHG, graphHG.get(10991), graphHG.get(8920), carsAndLength);
	dataShortest[4] = new ShortestPathData(graphB, graphB.get(60277), graphB.get(8490), allRoadsLength);
	
	dataFastest[0] = new ShortestPathData(graphHG, graphHG.get(10991), graphHG.get(8920), allRoadsTime);
	dataFastest[1] = new ShortestPathData(graphHG, graphHG.get(10991), graphHG.get(10991), allRoadsTime);
	dataFastest[2] = new ShortestPathData(graphB, graphB.get(60277), graphB.get(8490), allRoadsLength);
	
	}
	
	@Test
	public void testcompareDB() {
		
		//chemin court HG longueur
		DijkstraAlgorithm dijkstra1 = new DijkstraAlgorithm(dataShortest[0]);
		ShortestPathSolution solutionD = dijkstra1.run();
		BellmanFordAlgorithm BF1 = new BellmanFordAlgorithm(dataShortest[0]);
		ShortestPathSolution solutionBF = BF1.run();
		
		//comparaison des coûts des solutions
		
		assertTrue(solutionD.getPath().isValid());
		assertTrue(solutionBF.getPath().isValid());
		assertEquals(solutionD.getPath().getLength(),solutionBF.getPath().getLength(),0.0001);
		
		//chemin court HG durée
		
		dijkstra1=new DijkstraAlgorithm(dataFastest[0]);
		solutionD=dijkstra1.run();
		BF1=new BellmanFordAlgorithm(dataFastest[0]);
		solutionBF=BF1.run();
		
		assertTrue(solutionD.getPath().isValid());
		assertTrue(solutionBF.getPath().isValid());
		assertEquals(solutionD.getPath().getMinimumTravelTime(),solutionBF.getPath().getMinimumTravelTime(),0.0001);
		
		//chemin long Bretagne longueur
		/*
		dijkstra1=new DijkstraAlgorithm(dataShortest[4]);
		solutionD=dijkstra1.run();
		BF1=new BellmanFordAlgorithm(dataShortest[4]);
		solutionBF=BF1.run();
		
		assertTrue(solutionD.getPath().isValid());
		assertTrue(solutionBF.getPath().isValid());
		assertEquals(solutionD.getPath().getLength(),solutionBF.getPath().getLength(),0.0001);
		*/
		//chemin long Bretagne durée
		
		dijkstra1=new DijkstraAlgorithm(dataFastest[2]);
		solutionD=dijkstra1.run();
		BF1=new BellmanFordAlgorithm(dataFastest[2]);
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
		
		
		//chemin inexistant (absence d'arc)
		
		dijkstra1=new DijkstraAlgorithm(dataShortest[2]);
		solutionD=dijkstra1.run();
		BF1=new BellmanFordAlgorithm(dataShortest[2]);
		solutionBF=BF1.run();
		
		assertEquals(solutionD.getPath(),null);
		assertEquals(solutionD.getStatus(),Status.INFEASIBLE);
		assertEquals(solutionBF.getPath(),null);
		assertEquals(solutionBF.getStatus(),Status.INFEASIBLE);
		
		//chemnin en voiture
		
		dijkstra1=new DijkstraAlgorithm(dataShortest[3]);
		solutionD=dijkstra1.run();
		BF1=new BellmanFordAlgorithm(dataShortest[3]);
		solutionBF=BF1.run();
		
		assertTrue(solutionD.getPath().isValid());
		assertTrue(solutionBF.getPath().isValid());
		assertEquals(solutionD.getPath().getLength(),solutionBF.getPath().getLength(),0.0001);
		
	}
	
	@Test
	public void TestCompareAB() {
		
		//longueur
		
		AStarAlgorithm AStar = new AStarAlgorithm(dataShortest[0]);
		ShortestPathSolution solutionA = AStar.run();
		BellmanFordAlgorithm BF1 = new BellmanFordAlgorithm(dataShortest[0]);
		ShortestPathSolution solutionBF = BF1.run();
		
		assertTrue(solutionA.getPath().isValid());
		assertTrue(solutionBF.getPath().isValid());
		assertEquals(solutionA.getPath().getLength(),solutionBF.getPath().getLength(),0.0001);
		
		//vitesse
		
		AStar=new AStarAlgorithm(dataFastest[0]);
		solutionA=AStar.run();
		BF1=new BellmanFordAlgorithm(dataFastest[0]);
		solutionBF=BF1.run();
		
		assertTrue(solutionA.getPath().isValid());
		assertTrue(solutionBF.getPath().isValid());
		assertEquals(solutionA.getPath().getMinimumTravelTime(),solutionBF.getPath().getMinimumTravelTime(),0.0001);
		
		//voiture
		
		AStar=new AStarAlgorithm(dataShortest[3]);
		solutionA=AStar.run();
		BF1=new BellmanFordAlgorithm(dataShortest[3]);
		solutionBF=BF1.run();
		
		assertTrue(solutionA.getPath().isValid());
		assertTrue(solutionBF.getPath().isValid());
		assertEquals(solutionA.getPath().getLength(),solutionBF.getPath().getLength(),0.0001);
		
		//chemin null HG (origine=end)
		
		AStar=new AStarAlgorithm(dataShortest[1]);
		solutionA=AStar.run();
		BF1=new BellmanFordAlgorithm(dataShortest[1]);
		solutionBF=BF1.run();
				
		assertEquals(solutionA.getPath(),null);
		assertEquals(solutionA.getStatus(),Status.INFEASIBLE);
		assertEquals(solutionBF.getPath(),null);
		assertEquals(solutionBF.getStatus(),Status.INFEASIBLE);
		
		//chemin inexistant
		
		AStar=new AStarAlgorithm(dataShortest[2]);
		solutionA=AStar.run();
		BF1=new BellmanFordAlgorithm(dataShortest[2]);
		solutionBF=BF1.run();
						
		assertEquals(solutionA.getPath(),null);
		assertEquals(solutionA.getStatus(),Status.INFEASIBLE);
		assertEquals(solutionBF.getPath(),null);
		assertEquals(solutionBF.getStatus(),Status.INFEASIBLE);
		
	}

}

