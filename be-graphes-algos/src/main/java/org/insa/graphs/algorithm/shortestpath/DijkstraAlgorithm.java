package org.insa.graphs.algorithm.shortestpath;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.insa.graphs.algorithm.AbstractSolution.Status;
import org.insa.graphs.algorithm.utils.BinaryHeap;
import org.insa.graphs.model.Arc;
import org.insa.graphs.model.Graph;

import org.insa.graphs.model.Node;
import org.insa.graphs.model.Path;



public class DijkstraAlgorithm extends ShortestPathAlgorithm {
	/*création du tas de labels*/
	BinaryHeap<Label> tas_label = new BinaryHeap<Label>();
	
    public DijkstraAlgorithm(ShortestPathData data) {
        super(data);
    }

    @Override
    protected ShortestPathSolution doRun() {
        final ShortestPathData data = getInputData();
        ShortestPathSolution solution = null;
        int nbtabin=0;
        int nbtabout=0;
        Graph graph =data.getGraph();
        List<Node>nodes=graph.getNodes();
        Label[] labels = new Label[graph.size()];
        
        /*on remplit le tableau de label et on ajoute au tas le premier label*/
        for(Node node : nodes) {
        	labels[node.getId()]=new Label(node,false,Float.POSITIVE_INFINITY,null);
        	if(node==data.getOrigin()) {
        		labels[node.getId()]=new Label(node,true,0,null);
        		tas_label.insert(new Label(node,true,0,null));
        		nbtabin++;
        	}
        }
        
        long StartTime = System.nanoTime();
        
        /*boucle principale : tant que des labels de sont pas marqués et que la solution n'a pas été trouvée*/
        while(tas_label.isEmpty()==false && labels[data.getDestination().getId()].isMarque()==false) {
        	
        	Label label_x=tas_label.deleteMin();
        	nbtabout++;
        	label_x.marquer();
        	
        	
        	/*System.out.println("coût label marqué :"+label_x.getCost()+"\n");*/
        	
        	Node noeud_courant=label_x.getNode();
        	
        	/*if(tas_label.isValid()==true) {
        		System.out.println("le tas est valide\n");
        	}
        	else {
        		System.out.println("le tas n'est pas valide\n");
        	}
        	System.out.println("nb de successeurs à chercher : "+label_x.getNode().getNumberOfSuccessors()+"\n");
        	*/
        	/*on cherche parmi tous les successeurs*/
        	for(Arc arc : noeud_courant.getSuccessors()) {
                Node noeud_destination = arc.getDestination();
                Label label_destination = labels[noeud_destination.getId()];
                double cout=data.getCost(arc)+label_x.getCost();
                if(label_destination.isMarque()==false) {
                	if (!data.isAllowed(arc)) {
                        continue;
                    }
                	/*on compare les couts*/
                	if(cout<label_destination.getCost()) {
                		/*soit le label est déjà dans le tas ->mettre à jour le cout ; soit il n'y est pas -> on l'ajoute*/
                		if(label_destination.getPere()==null) {
                			label_destination.setCost(label_x.getCost()+data.getCost(arc));
                			label_destination.setPere(arc);
                			tas_label.insert(label_destination);
                			//notifyNodeReached(label_destination.getNode());
                			nbtabin++;
                			
                		}
                		else {
                        	tas_label.remove(label_destination);
                        	
                        	label_destination.setCost(cout);
                        	label_destination.setPere(arc);
                        	tas_label.insert(label_destination);
                        	
                        	notifyNodeReached(label_destination.getNode());
                        	
                		}
                	}	
                }
        	}
        }
        
        long EndTime = System.nanoTime();
        
        long CalTime = EndTime-StartTime;
        System.out.println("temps de calcul : "+CalTime/1000000+"ms\n");
        System.out.println("nbtabin :"+nbtabin+"  nbtabout :"+nbtabout);
        
        
        Node noeud_desti = data.getDestination();
        Label label_desti = labels[noeud_desti.getId()];
        
        // Destination has no predecessor, the solution is infeasible...
        if (label_desti.getPere()== null) {
            solution = new ShortestPathSolution(data, Status.INFEASIBLE);
        }
        else {
            // The destination has been found, notify the observers.
            notifyDestinationReached(noeud_desti);
            
            // Create the path from the array of predecessors...
            ArrayList<Arc> arcs = new ArrayList<>();
            Arc arc = label_desti.getPere();
            
            while (arc != null) {
                arcs.add(arc);
                label_desti = labels[arc.getOrigin().getId()];
                arc = label_desti.getPere();
            }

            // Reverse the path...
            Collections.reverse(arcs);
            
            // Create the final solution.
            solution = new ShortestPathSolution(data, Status.OPTIMAL, new Path(graph, arcs));
            
            /*on vérifie que la solution est bien valide*/
            if(solution.getPath().isValid()) {
            	System.out.println("le chemin obtenu est bien valide\n");
            	float l =solution.getPath().getLength();
            	System.out.println("vérification de la longueur du chemin obtenu:"+l+"\n");	
            }
            else {
            	System.out.println("le chemin obtenu n'est pas valide\n");
            }
        }
    return solution;
    }
}