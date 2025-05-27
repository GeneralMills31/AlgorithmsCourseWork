import java.io.*;
import java.util.Collections;
import java.util.Comparator;

public class DelivA {

	private File inputFile;
	private File outputFile;
	private PrintWriter output;
	private Graph graph;

	//Constructor - DO NOT MODIFY
	public DelivA(File in, Graph gr) {
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
		
		// Calls the method that will do the work of deliverable A
		runDelivA();

		output.flush();
	}

	//*********************************************************************************
	//               This is where your work starts
	
	private void runDelivA() {
		
		// Two sorting algos and then two loops to print
		
		System.out.println("Indegree:");
		
		// Sort by indegree and display results.
		// Collections.sort() sorts by ascending order
		Collections.sort(this.graph.getNodeList(), new NodeComparerIndegree());
		Collections.reverse(this.graph.getNodeList());
		for (Node currentNode : this.graph.getNodeList()) {
			System.out.println("Node " + currentNode.getAbbrev() + " has indegree " + currentNode.getIndegree());
		}
		
		System.out.println("\nOutdegree:");
		
		// Sort by outdegree and display results.
		Collections.sort(this.graph.getNodeList(), new NodeComparerOutdegree());
		Collections.reverse(this.graph.getNodeList());
		for (Node currentNode : this.graph.getNodeList()) {
			System.out.println("Node " + currentNode.getAbbrev() + " has outdegree " + currentNode.getOutdegree());
		
		
		}
	}
	
	/* Lecture Suggestion/Can have more than one. Can do this for anything you want to compare against.
	 * This is can be better than comparable as comparable is more set in stone/Decides a natural order.
	 * This is used if you want to compare any number of things.
	 */
	class NodeComparerIndegree implements Comparator<Node>{

		@Override
		public int compare(Node node1, Node node2) {
			if (node1.getIndegree() > node2.getIndegree()) {
	        	return 1;
	        }
	        
	        else if (node1.getIndegree() == node2.getIndegree() && 
	        		node1.getOutdegree() > node2.getOutdegree()) {
	        	return 1;
	        }
	        
	        else if (node1.getIndegree() == node2.getIndegree() && 
	        		node1.getOutdegree() == node2.getOutdegree() && 
	        		node1.getAbbrev().compareToIgnoreCase(node2.getAbbrev()) < 0) {
	        	return 1;
	        }
			
	        else if (node1.getIndegree() == node2.getIndegree() && 
	        		node1.getOutdegree() == node2.getOutdegree() && 
	        		node1.getAbbrev().compareToIgnoreCase(node2.getAbbrev()) == 0) {
	        	return 0;
	        }
			
	        else {
	        	return -1;
	        }
		}
	}
	
	class NodeComparerOutdegree implements Comparator<Node>{

		@Override
		public int compare(Node node1, Node node2) {
			if (node1.getOutdegree() > node2.getOutdegree()) {
	        	return 1;
	        }
	        
	        else if (node1.getOutdegree() == node2.getOutdegree() && 
	        		node1.getIndegree() > node2.getIndegree()) {
	        	return 1;
	        }
	        
	        else if (node1.getOutdegree() == node2.getOutdegree() && 
	        		node1.getIndegree() == node2.getIndegree() && 
	        		node1.getAbbrev().compareToIgnoreCase(node2.getAbbrev()) < 0) {
	        	return 1;
	        }
			
	        else if (node1.getOutdegree() == node2.getOutdegree() && 
	        		node1.getIndegree() == node2.getIndegree() && 
	        		node1.getAbbrev().compareToIgnoreCase(node2.getAbbrev()) == 0) {
	        	return 0;
	        }
			
	        else {
	        	return -1;
	        }
		}
	}
}