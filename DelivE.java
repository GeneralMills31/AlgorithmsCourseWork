import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Random;

public class DelivE {

	private File inputFile;
	private File outputFile;
	private PrintWriter output;
	private Graph graph;

	//Constructor - DO NOT MODIFY
	public DelivE(File in, Graph gr) {
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
		
		// Calls the method that will do the work of deliverable E
		runDelivE();

		output.flush();
	}

	//*********************************************************************************
	//               This is where your work starts
	
	private void runDelivE() {
		
		ArrayList<Node> finalList = this.TSP(this.graph.getNodeList());
		System.out.println("\nFinal Tour: " + this.displayTSP(finalList) + "\nFinal Cost: " + this.getTSPCost(finalList));
		
	}
	
	/* Method that takes the original order of the cities and applies a greedy algorithm (nearest neighbor)
	 * to it. The method takes a random start. This makes it so the algorithms output differs between runs
	 * and allows the improvement method to get different results.
	 */
	public ArrayList<Node> initialTour(ArrayList<Node> inputList) {
		
		// Initialize the output tour.
		ArrayList<Node> greedyTour = new ArrayList<Node>();
		
		// Initialize the nodes.
		for (Node n : inputList) {
			n.setColor("white");
		}
		
		// Use Random to get a random start node.
		Random random = new Random();
		Node startNode = inputList.get(random.nextInt(inputList.size()));
		
		// Set the rolling node to the start node.
		Node currentNode = startNode;
		// Loop until no white edge is available.
		while (!currentNode.getColor().equalsIgnoreCase("black")) {
			// Set currentNode to black and add it to the tour.
			currentNode.setColor("black");
			greedyTour.add(currentNode);
			// Sort currentNodes outgoing edges.
			Collections.sort(currentNode.getOutgoingEdges(), new EdgeDistanceComparer());
			// Get the lowest distance and valid path addition.
			for (Edge e : currentNode.getOutgoingEdges()) {
				Node temp = e.getHead();
				if (!temp.getName().equalsIgnoreCase(startNode.getName()) && temp.getColor().equalsIgnoreCase("white")) {
					currentNode = temp;
					break;
				}
			}
		}
		// Add the start node to the end (return).
		greedyTour.add(startNode);
		return greedyTour;
	}
	
	/* Method to improve a given tour. Here it will be used to improve the tours
	 * provided by the greedy algorithm. The method, essentially, takes an edge
	 * and swaps it with its neighbors. If any of these swaps generate a lower cost,
	 * the changes will be kept 
	 */
	public ArrayList<Node> improveTour(ArrayList<Node> inputList) {
		
		// Initialize variables for the best and current tours.
		ArrayList<Node> bestTour = new ArrayList<Node>(inputList);
		ArrayList<Node> changedTour = new ArrayList<Node>(inputList);
		
		// Try and except blocks to manage the file.
		try {
			// Get file writer ready and write the first greedy tour to the file.
			FileWriter fileWriter = new FileWriter("AttemptedImprovements.txt", true);
			fileWriter.write(this.displayTSP(bestTour));
			
			// Initialize variable to control while loop.
			boolean keepGoing = true;
			/* Grab an edge and attempt to swap it with its neighbors. If any
			 * of these swaps result in a better tour cost, keep the changes and
			 * set the control variable to true so it will attempt to improve the
			 * result again. If no improvements are made the loop will end and
			 * the best tour will be returned.
			 */
			while (keepGoing == true) {
				keepGoing = false;
				// Start at 1 to avoid start node. This is the main edge to try swapping.
				for (int i = 1; i < changedTour.size()-2; i++) {
					// These are the neighbors that will be swapped with the above edge.
					for (int j = i+1; j < changedTour.size()-1; j++) {
						// Swap the edges.
						Node temp = changedTour.get(i);
						changedTour.set(i, changedTour.get(j));
						changedTour.set(j, temp);
						// Write the improvement attempt to the file.
						fileWriter.write("\n" + this.displayTSP(changedTour));
						/* Calculate distance of new tour and compare it to the best known.
						 * If better, set the best known to that tour and set the control variable
						 * to true so it will attempt to improve the tour again. If no improvements are
						 * made the loop will end.
						 */
						int newDistance = this.getTSPCost(changedTour);
						if (newDistance < this.getTSPCost(bestTour)) {
							bestTour = new ArrayList<Node>(changedTour);
							// If a swap is made and kept keep the loops going.
							keepGoing = true;
						}
						// If the changes were not an improvement, reset the tour.
						else {
							changedTour = new ArrayList<Node>(bestTour);
						}
					}
				}
			}
			// Close the file writer.
			fileWriter.close();
		}
		catch (IOException ex) {
		      System.out.println("An error occured with the file!");
		      ex.printStackTrace();
		}
		return bestTour;
	}
	
	public ArrayList<Node> TSP(ArrayList<Node> inputList) {
		
		// Initialize the variables that represent the final tour and its cost.
		ArrayList<Node> finalTour = new ArrayList<Node>(inputList);
		int finalCost = Integer.MAX_VALUE;
		
		/* Calculate the number of improvement attempts. Will do 10% of the size of the
		 * city list. Seems reasonable and sources online said 10% is a good size.
		 */
		int numAttempts = (int) (inputList.size() * 0.10);
		int count = 0;
		
		/* Each attempt will take the original order of the cities, pick a random node for the start node,
		 * and create a tour using a greedy algorithm (nearest neighbor). It will then take the resulting tour from
		 * the greedy algorithm and try to improve it using a 2-Opt algorithm. The actual final tour will be best tour
		 * from all these attempts following this process.
		 */
		while (count <= numAttempts) {
			// Send the original cities order into the greedy algorithm that will choose a random start node.
			ArrayList<Node> startTour = this.initialTour(inputList);
			// If this is the very first attempt, display our starting tour.
			if (count == 0) {
				System.out.println("Start Tour: " + this.displayTSP(startTour) + "\nStart Tour Cost: " + this.getTSPCost(startTour));
			}
			// Send the resulting tour from the greedy algorithm into the improvement method.
			ArrayList<Node> improvedTour = this.improveTour(startTour);
			/* Calculate the cost of the current improved tour and compare it to the cost of the current
			 * best known tour. If the improved tour is better than our current best, set out best tour
			 * to the improved tour and update the cost. Display this improvement to the user.
			 */
			int currentTourCost = this.getTSPCost(improvedTour);
			if (currentTourCost < finalCost) {
				System.out.println("\nImproved Tour: " + this.displayTSP(improvedTour) + "\nCost: " + currentTourCost);
				finalTour = new ArrayList<Node>(improvedTour);
				finalCost = currentTourCost;
			}
			// Iterate the counter.
			count++;
		}
		// Return the best tour.
		return finalTour;
	}
	
	// Method to display a given tour.
	public String displayTSP(ArrayList<Node> inputList) {
		String output = "";
		for (int i = 0; i < inputList.size()-1; i++) {
			output = output + inputList.get(i).getAbbrev() + "->";
		}
		output = output + inputList.get(inputList.size()-1).getAbbrev();
		return output;
	}
	
	// Method to calculate and return the cost of a given tour.
	public int getTSPCost(ArrayList<Node> tour) {
		int tourCost = 0;
		for ( int i = 0; i < tour.size() - 1; i++) {
			Node node1 = tour.get(i);
			Node node2 = tour.get(i + 1);
			for (Edge e : node1.getOutgoingEdges()) {
				Node temp = e.getHead();
				if (temp.getName().equalsIgnoreCase(node2.getName())) {
					tourCost += e.getDistance();
					break;
				}
			}
		}
		return tourCost;
	}
	
	// Method used to sort edges based on their distance value.
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
}

// ------------------------------------------------------------------------------------------------------------------
// OLD ATTEMPT / OLD CODE
//public ArrayList<Node> improveTSP(ArrayList<Node> initialList) {
//
//// Save the best attempt and currentAttempt. Time: O(n^2)
//ArrayList<Node> currentBest = new ArrayList<Node>(initialList);
//int currentBestDistance = this.getTSPCost(currentBest);
//ArrayList<Node> currentAttempt = new ArrayList<Node>(initialList);
//int currentAttemptDistance;
//// Store the bad nodes.
//ArrayList<Node> badNodes = new ArrayList<Node>();
//
//// Try to improve the trip.
//while (currentBestDistance > 3000) {
//	// Initialize the "worst" node.
//	Node worstNode = this.getWorstNode(currentAttempt, badNodes);
//	Node currentNeighbor = currentAttempt.get(currentAttempt.indexOf(worstNode) + 1);
//	Node bestNeighbor = this.getBestNeighbor(worstNode);
//	
//	if (currentNeighbor.getName().equalsIgnoreCase(bestNeighbor.getName())) {
//		// Add to file.
//		// Add worst node to bad nodes list so it is not used later.
//		badNodes.add(worstNode);
//		// Reset the current attempt.
//		currentAttempt = new ArrayList<Node>(currentBest);
//		System.out.println("Reset Distance: " + this.getTSPCost(currentAttempt));
//		System.out.println("Degraded\n");
//		continue;
//	}
//	
//	// Debugging
//	System.out.println("\n" + worstNode.getName());
//	System.out.println(currentNeighbor.getName());
//	System.out.println(bestNeighbor.getName());
//	System.out.println(this.getTSPCost(currentAttempt));
//	
//	// Swap the worst and second worst nodes.
//	currentAttempt.set(currentAttempt.indexOf(currentNeighbor), bestNeighbor);
//	currentAttempt.set(currentAttempt.indexOf(bestNeighbor), currentNeighbor);
//	
//	// Get new list data.
//	currentAttemptDistance = this.getTSPCost(currentAttempt);
//	
//	// Debugging
//	System.out.println(this.getTSPCost(currentAttempt));
//	
//	// Check new list.
//	if (currentAttemptDistance < currentBestDistance) {
//		currentBest = new ArrayList<Node>(currentAttempt);
//		currentBestDistance = currentAttemptDistance;
//		// Display the improved tour.
//		System.out.println("Improved Tour: " + this.displayTSP(currentBest) + "\nCost : " + currentBestDistance);
//		// If an improvement is made, empty the bad nodes list.
//		badNodes.clear();
//	}
//	else if (currentAttemptDistance >= currentBestDistance) {
//		// Add to file.
//		// Add worst node to bad nodes list so it is not used later.
//		badNodes.add(worstNode);
//		// Reset the current attempt.
//		currentAttempt = new ArrayList<Node>(currentBest);
//		System.out.println("Reset Distance: " + this.getTSPCost(currentAttempt));
//		System.out.println("Degraded\n");
//	}
//}
//return currentBest;
//}
//public Node getBestNeighbor(Node node) {
//	
//	Node bestNeighbor = null;
//	int bestNeighborDistance = Integer.MAX_VALUE;
//	for (Edge e : node.getOutgoingEdges()) {
//		if (e.getDistance() < bestNeighborDistance) {
//			bestNeighbor = e.getHead();
//			bestNeighborDistance = e.getDistance();
//		}
//	}
//	return bestNeighbor;
//}
//
//public Node getWorstNode(ArrayList<Node> list, ArrayList<Node> badNodes) {
//	Node worstNode = null;
//	int worstDistance = Integer.MIN_VALUE;
//	// Get the "worst" node. Time: O(n^2)
//	for (int i = 0; i < list.size()-2; i++) {
//		Node tempOne = list.get(i);
//		Node tempTwo = list.get(i+1);
//		Edge edgeBetween = this.getEdgeBetween(tempOne, tempTwo);
//		if (!badNodes.contains(tempOne) && edgeBetween.getDistance() > worstDistance) {
//			worstDistance = edgeBetween.getDistance();
//			worstNode = tempOne;
//		}
//	}
//	return worstNode;
//}
//
//public Edge getEdgeBetween(Node nodeOne, Node nodeTwo) {
//	for (Edge e : nodeOne.getOutgoingEdges()) {
//		Node temp = e.getHead();
//		if (temp.getName().equalsIgnoreCase(nodeTwo.getName())) {
//			return e;
//		}
//	}
//	return null;
//}

/* OLD NOTES:
 * 
 * Can we use some of the information discussed in CSP2? Greedy descent, randomness, keep going function, etc.
 * 
 * How should I call this method? Static or instance method?
 * Should I store the data? Or should I just have it run, calculate and display path, and
 * only store a path (current one or smallest one) once we "end" it?
 * 
 * Have variable for current smallest path, previous path, and current path? How else will it
 * work on previous data to make alterations?
 * 
 * Some form of greedy algorithm that makes swaps and changes that seem to improve the path? Keep
 * changes that do. Similar to Greedy Descent? Make alterations on previous run or current smallest?
 * 
 * Put the whole thing in a while loop (keep_going type of thing) and it keeps running until keep_going says to
 * stop. Once stopped, it exits the while loop and sets the current smallest known path as the tour? Within the loop,
 * it displays the data and makes alterations? Should we store the previous run then?
 * 
 * REFACTOR THE CODE/BREAK IT DOWN. Make it easier to manage and deal with.
 * Move the loops that grab the two worst nodes into their own method.
 * Other option is to just take the worst node and make its neighbor the head node from its best distance.
 * So, essentially greedy descent.
 * 
 * What happens if the worst node is the one at the end returning?
 * Simply change index values of loops searching for worst node to not account for the last edge?
 * If degraded, should we keep the changes?
 * Save worst node so we do not grab it a second time if it sucks.
 * 
 * Picking a random start node, then running greedy on it, then improving the solution. Solid option!
 * Random to generate number from list size, then use that as start node and then run the greedy algorithm on it,
 * then improve that specific version of the algorithm.
 * 
 * How to improve the improvement?
 * 
 */