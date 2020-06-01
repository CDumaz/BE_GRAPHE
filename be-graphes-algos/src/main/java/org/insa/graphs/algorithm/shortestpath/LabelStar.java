package org.insa.graphs.algorithm.shortestpath;

import org.insa.graphs.algorithm.AbstractInputData;
import org.insa.graphs.model.Arc;

import org.insa.graphs.model.Node;


public class LabelStar extends Label {
	
	private double cout_estime;
	
	private Node node_dest;
	
	private ShortestPathData data ;
	
	public LabelStar(Node s,boolean m,float c,Arc pere,Node node_dest,ShortestPathData data) {
		super(s,m,c,pere);
		this.node_dest=node_dest;
		this.data=data;
	}
	
	public double getEstimCost() {
		return this.cout_estime;
	}
	
	public void setEstimCost(double c) {
		this.cout_estime=c;
	}
	
	public double getTotalCost() {
		if(data.getMode()==AbstractInputData.Mode.LENGTH) {
			return this.getCost()+this.getNode().getPoint().distanceTo(this.node_dest.getPoint());
		}
		else {
			return this.getCost()+this.getNode().getPoint().distanceTo(this.node_dest.getPoint())/(this.data.getGraph().getGraphInformation().getMaximumSpeed()/3.6);
		}
	}
	
	/*public int compareTo(LabelStar other) {
		int result;
        result=Double.compare(getTotalCost(), other.getTotalCost());
        if(result==0) {
        	result=Double.compare(getEstimCost(), other.getEstimCost());
        }
        return result;
        */
    

}