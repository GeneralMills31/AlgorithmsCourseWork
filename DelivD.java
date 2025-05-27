import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class DelivD {

	private File inputFile;
	private File outputFile;
	private PrintWriter output;
	private Graph graph;

	//Constructor - DO NOT MODIFY
	public DelivD(File in, Graph gr) {
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
		
		// Calls the method that will do the work of deliverable D
		runDelivD();

		output.flush();
	}

	//*********************************************************************************
	//               This is where your work starts
	
	private void runDelivD() {
		
		// Sort node list
		Collections.sort(this.graph.getNodeList(), new NodeValComparer());
		// Get the starting index for the algorithm and send it through.
		int startIndex = this.graph.getNodeList().size()-1;
		int distance = this.b(startIndex, startIndex);
		// Display the tour distance.
		System.out.println(distance);
		// Display the tour path.
		this.getTour();
	}
	
	public int b(int i, int j) {
		
		// If i = 0 & j = 1 | Base case
		if (i == 0 && j == 1) {
			this.graph.getNodeList().get(j).setPredecessor(this.graph.getNodeList().get(i));
			return this.biteDistance(this.graph.getNodeList().get(i), this.graph.getNodeList().get(j));
		}
		// If i < j - 1
		else if (i < j - 1) {
			this.graph.getNodeList().get(j).setPredecessor(this.graph.getNodeList().get(j - 1));
			return b(i, j - 1) + this.biteDistance(this.graph.getNodeList().get(j-1), this.graph.getNodeList().get(j));
		}
		// if (i == j - 1)
		else {
			int min = Integer.MAX_VALUE;
			int kMin = 0;
			for (int k = 0; k < j - 1; k++) {
				int temp = b(k,i) + this.biteDistance(this.graph.getNodeList().get(k), this.graph.getNodeList().get(j));
				if (temp < min) {
					min = temp;
					kMin = k;
				}
			}
			this.graph.getNodeList().get(j).setPredecessor(this.graph.getNodeList().get(kMin));
			return min;
		}
	}
	
	// Node comparator that compares based on value.
	class NodeValComparer implements Comparator<Node>{

		@Override
		public int compare(Node node1, Node node2) {
			if (Double.parseDouble(node1.getValue()) > Double.parseDouble(node2.getValue())) {
				return 1;
			}
			else if (Double.parseDouble(node1.getValue()) < Double.parseDouble(node2.getValue())) {
				return -1;
			}
			else {
				return 0;
			}
		}	
	}
	
	/* Distance method that gets the distance between two nodes 
	 * using the edge's distance that connects them.
	 */
	public int biteDistance(Node node1, Node node2) {
		for (Edge e : node2.getOutgoingEdges()) {
			Node head = e.getHead();
			if (head.getName().equalsIgnoreCase(node1.getName())) {
				// Code I am using the test the path
//				node1.setPredecessor(node2);
				return e.getDistance();
			}
		}
		return -1;
	}
	
	/* Method to display the tour. Start at the end node and loop until we have no
	 * predecessors; mark nodes as top as we go. Once that is reached, loop over the remaining
	 * nodes (bottom) and display them. The result should be the tour.
	 */
	public void getTour() {
		ArrayList<Node> nodeList = this.graph.getNodeList();
		Node currentNode = nodeList.get(nodeList.size() - 1);
		Node endNode = this.graph.getNodeList().get(0);
		while (!currentNode.getName().equalsIgnoreCase(endNode.getName())) {
			System.out.print(currentNode.getAbbrev() + " -> ");
			currentNode.setTop(true);
			currentNode = currentNode.getPredecessor();
		}
		
		for (Node n : nodeList) {
			if (n.getTop() == false) {
				System.out.print(n.getAbbrev() + " -> ");
			}
		}
		System.out.print(this.graph.getNodeList().get(this.graph.getNodeList().size()-1).getAbbrev());
	}
}