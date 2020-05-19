package org.insa.graphs.algorithm.utils;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.FileInputStream;

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

public class ShortestPathTest {
	
private ShortestPathData data;
private BellmanFordAlgorithm bellman = new BellmanFordAlgorithm(data);
private DijkstraAlgorithm dijkstra = new DijkstraAlgorithm(data);
private AStarAlgorithm astar = new AStarAlgorithm(data);

final String graphHauteGaronne="/Users/clemd/Downloads/haute-garonne.mapgr";
final String graphBelgium="/Users/clemd/Downloads/belgium.mapgr";

final GraphReader readerHG = new BinaryGraphReader(new DataInputStream(new BufferedInputStream(new FileInputStream(graphHauteGaronne))));
final GraphReader readerB = new BinaryGraphReader(new DataInputStream(new BufferedInputStream(new FileInputStream(graphBelgium))));
   



public ShortestPathTest(ShortestPathData data) {
        this.data=data;
    }

}
