import java.io.*;
import java.util.Collections;
import java.util.Comparator;

public class DelivB {

	private File inputFile;
	private File outputFile;
	private PrintWriter output;
	private Graph graph;
	private int time;	// Added Variable

	//Constructor - DO NOT MODIFY
	public DelivB(File in, Graph gr) {
		inputFile = in;
		graph = gr;

		// Set up for writing to a file
		try {
			// Use input file name to create output file in the same location
			String inputFileName = inputFile.toString();
			String outputFileName = inputFileName.substring(0, inputFileName.length() - 4).concat("_out.txt");
			outputFile = new File(outputFileName);

			// A Printwriter is an object that can write to a file
			output = new PrintWriter(outputFile);
		} catch (Exception x) {
			System.err.format("Exception: %s%n", x);
			System.exit(0);
		}
		
		// Calls the method that will do the work of deliverable B
		runDelivB();

		output.flush();
	}

	//*********************************************************************************
	//               This is where your work starts
	
	private void runDelivB() {
		
		this.DFS();
		
		System.out.println("DFS of Graph\n\nNode\tDisc\tFinish");
		/* Sort by abbreviation. This is to correct the order after placing the S value in front
		 * and match the output of the given files.
		 */
		
		Collections.sort(this.graph.getNodeList(), new NodeComparer());
		for (Node u : this.graph.getNodeList()) {
			System.out.println(u.getAbbrev() + "\t" + u.getDiscoveryTime() + "\t" + u.getFinishTime());
		}
		
		// Determine the edge types of the graphs edges after running DFS.
		this.determineEdgeType();
		System.out.println("\nEdge Classification:\n\nEdge\tType");
		for (Edge u : this.graph.getEdgeList()) {
				System.out.println(u.getTail().getAbbrev() + "->" + u.getHead().getAbbrev() + "\t" + u.getEdgeType());
		}
	}

	public void DFS() {
		
		/* Place the source node at the front of the node list using the comparator
		 * defined below.
		 */
		Collections.sort(this.graph.getNodeList(), new StartNodeComparer());
		
		// Prepare the nodes for DFS by setting their color to white and their predecessors to null.
		for (Node u : this.graph.getNodeList()) {
			u.setColor("white");
			u.setPredecessor(null);
		}
		
		// Set the global time value to 0.
		time = 0;
		
		/* Starting at the source, loop through the nodes. Any node with a color of white
		 * send through DFS_VISIT.
		 */
		for (Node u : this.graph.getNodeList()) {
			if (u.getColor().equalsIgnoreCase("white")) {
				this.DFS_VISIT(u);
			}
		}
	}
	
	public void DFS_VISIT(Node u){
		/* Once a node has been passed through DFS_VISIT, increment the 
		 * global time value and use it to set the nodes discovery time
		 * and then set their color to gray.
		 */
		time++;
		u.setDiscoveryTime(time);
		u.setColor("gray");
		
		/* Sort the current nodes outgoing edges based on distance.
		 * This will ensure the loop grabs the correct order of edge.
		 */
		Collections.sort(u.getOutgoingEdges(), new EdgeDistanceComparer());
		for (Edge e : u.getOutgoingEdges()) {
			// Grab the head of the current edge.
			Node v = e.getHead();
			/* If the edge is white, set its predecessor, define it as a tree edge,
			 * and send it through DFS_VISIT.
			 */
			if (v.getColor().equalsIgnoreCase("white")) {
				v.setPredecessor(u);
				e.setEdgeType("Tree");
				this.DFS_VISIT(v);
			}
		}
		/* After going through the nodes outgoing edges, increment the time value, 
		 * set the nodes finish time, and set its color to black.
		 */
		time++;
		u.setFinishTime(time);
		u.setColor("black");
	}
	
	/* Method to determine the edge type of all the edges in the graphs list.
	 * Tree nodes are defined in the running of DFS.
	 */
	public void determineEdgeType() {
		
		for (Edge e : this.graph.getEdgeList()) {
			// If the edge is not a tree edge, calculate its type.
			if (e.getEdgeType() == null) {
				Node u = e.getTail();
				Node v = e.getHead();
				if (u.getDiscoveryTime() < v.getDiscoveryTime() && u.getFinishTime() > v.getFinishTime()) {
					e.setEdgeType("Forward");
				}
				else if (u.getDiscoveryTime() > v.getDiscoveryTime() && u.getFinishTime() < v.getFinishTime()) {
					e.setEdgeType("Back");
				}
				else {
					e.setEdgeType("Cross");
				}
			}
		}
	}
	
	/* Method used to sort edges based on their distance value. This method is used in
	 * DFS_VISIT and helps ensure the correct order of outgoing edge is used.
	 */
	class EdgeDistanceComparer implements Comparator<Edge>{

		@Override
		public int compare(Edge edge1, Edge edge2) {
			if (edge1.getDistance() > edge2.getDistance()) {
	        	return 1;
	        }
			
			else if (edge1.getDistance() < edge2.getDistance()) {
	        	return -1;
	        }
			
			// If the two edges have the same distance value, sort by abbreviation.
			else if (edge1.getDistance() == edge2.getDistance()) {
				if (edge1.getHead().getAbbrev().compareToIgnoreCase(edge2.getHead().getAbbrev()) > 0) {
					return 1;
				}
				else {
					return -1;
				}
			}
	        else {
	        	return 0;
	        }
		}
	}
	
	/* Method used to sort nodes based on abbreviation. This method is used in the
	 * main method to sort the nodes back into order after running DFS and match the
	 * output specified in the given files.
	 */
	class NodeComparer implements Comparator<Node>{

		@Override
		public int compare(Node node1, Node node2) {
			
			if (node1.getAbbrev().compareToIgnoreCase(node2.getAbbrev()) > 0) {
				return 1;
			}
			
			else if (node1.getAbbrev().compareToIgnoreCase(node2.getAbbrev()) < 0) {
				return -1;
			}
			
			else {
				return 0;
			}
		}
	}
	
	/* Method used to place the source node at the front of the node list
	 * as well as sort the remaining nodes based on abbreviation. This is
	 * to ensure the DFS starts at the source, but will then grab the following
	 * values based on their abbreviation.
	 */
	class StartNodeComparer implements Comparator<Node>{

		@Override
		public int compare(Node node1, Node node2) {
			
			if (node1.getValue().equalsIgnoreCase("S")) {
				return -1;
			}
			
			else if (node2.getValue().equalsIgnoreCase("S")) {
				return 1;
			}
			
			else if (node1.getAbbrev().compareToIgnoreCase(node2.getAbbrev()) > 0) {
				return 1;
			}
			
			else if (node1.getAbbrev().compareToIgnoreCase(node2.getAbbrev()) < 0) {
				return -1;
			}
			
			else {
				return 0;
			}
		}
	}
}