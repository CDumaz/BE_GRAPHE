package org.insa.graphs.algorithm.shortestpath;

import org.insa.graphs.model.Arc;
import org.insa.graphs.model.Node;

public class Label implements Comparable<Label> {
	
	public Node sommet_courant;
	
	public boolean marque;
	
	public double cout;
	
	public Arc pere;
	
	public Label(Node s,boolean m,float c,Arc pere) {
		this.sommet_courant=s;
		this.marque=m;
		this.cout=c;
		this.pere=pere;
	}
	
	public double getCost() {
		return this.cout;
	}
	
	public double getTotalCost() {
		return this.cout;
	}
	
	public Arc getPere() {
		return this.pere;
	}
	
	public boolean isMarque() {
		return this.marque;
	}
	
	public int compareTo(Label other) {
		int result;
        result=Double.compare(getTotalCost(), other.getTotalCost());
        return result;
        
    }
	
	public void marquer() {
        this.marque = true;
    }
	
	public Node getNode() {
		return this.sommet_courant;
	}
	
	public void setCost(double c) {
		this.cout=c;
	}
	
	public void setPere(Arc arc) {
		this.pere=arc;
	}
	
	
	
	
	
}