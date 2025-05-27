import java.util.*;

public class Graph {

	private ArrayList<Node> nodeList;
	private ArrayList<Edge> edgeList;
	private ArrayList<Node> tour;
	private int mstCost; // Added Attribute DelivC
	// DelivC: Could add an attribute here to hold the MST if you want. Maybe the edges of it.
	
	public Graph() {
		nodeList = new ArrayList<Node>();
		edgeList = new ArrayList<Edge>();
		tour = new ArrayList<Node>();
	}
	
	public ArrayList<Node> getNodeList() {
		return nodeList;
	}
	
	public ArrayList<Edge> getEdgeList() {
		return edgeList;
	}
	
	// Added Method DelivD
	
	public ArrayList<Node> getTour(){
		return this.tour;
	}
	
	// Added Method DelivC
	
	public int getMstCost() {
		return mstCost;
	}
	
	public void addNode(Node n) {
		nodeList.add(n);
	}
	
	public void addEdge(Edge e) {
		edgeList.add(e);
	}
	
	// Added Method DelivD
	
	public void addTour(Node newNode) {
		this.tour.add(newNode);
	}
	
	// Added Method DelivC
	
	public void setMstCost(int mstCost) {
		this.mstCost = mstCost;
	}
	
	// Added Method DelivA | Unused
	
	// Order the graphs nodes from greatest to least indegree using selection sort.
	public void orderGreatestToLeastIndegree() {
		
		// Grab the graphs list and assign it to a variable for easier use.
		ArrayList<Node> myList = this.getNodeList();
		
		// Starting at the beginning of the list, look for the largest node.
	    for ( int j=0; j<myList.size()-1; j++ )
	    {
	    	// Define the first value as the max for starting purposes.
	      Node max = myList.get(j);
	      
	      /* After defining the first value as the max, search all subsequent nodes
	       * for a larger value...
	       */
	      for ( int k=j+1; k<myList.size(); k++ ) {
	    	// Get the current iterations node.
	    	Node temp = myList.get(k);
	    	
	    	/*  Check if current iterations node has a larger indegree. If so,
	    	 * make it the max.
	    	 */
	        if (temp.getIndegree() > max.getIndegree()) {
	        	max = temp;
	        }
	        
	        /* If indegrees are the same, check the outdegrees. If current iteration
	         * has a larger outdegree, make it the max.
	         */
	        else if (temp.getIndegree() == max.getIndegree() && temp.getOutdegree() > max.getOutdegree()) {
	        	max = temp;
	        }
	        
	        /* If both indegree and outdegree are the same, order by abbreviation.
	         */
	        else if (temp.getIndegree() == max.getIndegree() && temp.getOutdegree() 
	        		== max.getOutdegree() && temp.getAbbrev().compareToIgnoreCase(max.getAbbrev()) < 0) {
	        	max = temp;
	        }
	        
	      }
	      
	      /* Swap the values. j being the current main loop iteration and max
	       * being the largest subsequent node.
	       */
	      Node temp = myList.get(j);
	      myList.set(myList.indexOf(max), temp);
	      myList.set(j, max);
	      
	    }
		
	}
	
	// Added Method DelivA | Unused
	
	/* Order the graphs nodes from greatest to least outdegree using selection sort.
	 * Functionally the same as previous method, but with a focus on outdegree.
	 */
	public void orderGreatestToLeastOutdegree() {
		
		ArrayList<Node> myList = this.getNodeList();
		
	    for ( int j=0; j<myList.size()-1; j++ )
	    {

	      Node max = myList.get(j);
	      
	      for ( int k=j+1; k<myList.size(); k++ ) {
	    	  
	    	Node temp = myList.get(k);
	    	
	        if (temp.getOutdegree() > max.getOutdegree()) {
	        	max = temp;
	        }
	        
	        else if (temp.getOutdegree() == max.getOutdegree() && temp.getIndegree() > max.getIndegree()) {
	        	max = temp;
	        }
	        
	        else if (temp.getOutdegree() == max.getOutdegree() && temp.getIndegree() 
	        		== max.getIndegree() && temp.getAbbrev().compareToIgnoreCase(max.getAbbrev()) < 0) {
	        	max = temp;
	        }
	        
	      }

	      Node temp = myList.get(j);
	      myList.set(myList.indexOf(max), temp);
	      myList.set(j, max);
	      
	    }	
	}
}