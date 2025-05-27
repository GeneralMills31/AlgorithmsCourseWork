//import java.util.*;

// Edge between two nodes
public class Edge {
	
	private int distance;
	private Node tail;
	private Node head;
	private String edgeType; // Added Attribute DelivB
	
	public Edge(Node tailNode, Node headNode, int dist) {
		distance = dist;
		tail = tailNode;
		head = headNode;
		// Should I add edgeType here? Or allow default value to be used?
	}
	
	public Node getTail() {
		return tail;
	}
	
	public Node getHead() {
		return head;
	}
	
	public int getDistance() {
		return distance;
	}
	
	// Added Method DelivB
	
	public String getEdgeType() {
		return this.edgeType;
	}
	public void setTail(Node newTail) {
		tail = newTail;
	}
	
	public void setHead(Node newHead) {
		head = newHead;
	}
	
	public void setDistance(int dist) {
		distance = dist;
	}
	
	// Added Method DelivB
	
	public void setEdgeType(String edgeType) {
		this.edgeType = edgeType;
	}
}
