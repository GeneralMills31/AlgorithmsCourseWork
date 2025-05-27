import java.util.*;

// A node of a graph for the Spring 2018 ICS 340 program

public class Node {

	private String name;
	private String value;  // The value of the Node which was stored in the value column
	private String abbrev;  // The abbreviation for the Node
	private String color;	// Added Attribute DelivB
	private Node predecessor; // Added Attribute DelivB
	private int discoveryTime, finishTime; // Added Attributes DelivB
	private int key; // Added Attribute DelivC
	private boolean top; // Added Attribute DelivD
	private ArrayList<Edge> outgoingEdges;  
	private ArrayList<Edge> incomingEdges;
	
	public Node(String abbreviation) {
		abbrev = abbreviation;
		value = null;
		name = null;
		outgoingEdges = new ArrayList<Edge>();
		incomingEdges = new ArrayList<Edge>();
		// Should I add color, predecessor, discoveryTime, and finishTime here?
		// Or allow default values to be used?
	}
	
	public String getAbbrev() {
		return abbrev;
	}
	
	public String getName() {
		return name;
	}
	
	public String getValue() {
		return value;
	}
	
	// Added Method DelivB
	
	public String getColor() {
		return this.color;
	}
	
	// Added Method DelivB
	
	public Node getPredecessor() {
		return this.predecessor;
	}
	
	// Added Method DelivB
	
	public int getFinishTime() {
		return this.finishTime;
	}
	
	// Added Method DelivB
	
	public int getDiscoveryTime() {
		return this.discoveryTime;
	}
	
	public ArrayList<Edge> getOutgoingEdges() {
		return outgoingEdges;
	}
	
	public ArrayList<Edge> getIncomingEdges() {
		return incomingEdges;
	}
	
	// Added Method DelivA
	
	public int getIndegree() {
		return this.getIncomingEdges().size();
	}
	
	// Added Method DelivA
	
	public int getOutdegree() {
		return this.getOutgoingEdges().size();
	}
	
	// Added Method DelivC
	
	public int getKey() {
		return this.key;
	}
	
	// Added Method DelivD
	
	public boolean getTop() {
		return this.top;
	}
	
	public void setAbbrev(String abbreviation) {
		abbrev = abbreviation;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public void setValue(String value) {
		this.value = value;
	}
	
	// Added Method DelivB
	
	public void setColor(String newColor) {
		this.color = newColor;
	}
	
	// Added Method DelivB
	
	public void setPredecessor(Node newPredecessor) {
		this.predecessor = newPredecessor;
	}
	
	// Added Method DelivB
	
	public void setDiscoveryTime(int newTime) {
		this.discoveryTime = newTime;
	}
	
	// Added Method DelivB
	
	public void setFinishTime(int newTime) {
		this.finishTime = newTime;
	}
	
	// Added Method DelivC
	
	public void setKey(int key) {
		this.key = key;
	}
	
	// Added MEthod DelivD
	
	public void setTop(boolean bool) {
		this.top = bool;
	}
	
	public void addOutgoingEdge(Edge e) {
		outgoingEdges.add(e);
	}
	
	public void addIncomingEdge(Edge e) {
		incomingEdges.add(e);
	}
}

