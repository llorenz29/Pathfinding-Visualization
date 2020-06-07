
public class Node implements Comparable<Node>{
	
	private int x;
	private int y;
	private int g = 0;
	private int h = 0;
	private int f= -1;
	
	private int currentCost;
	
	private boolean isWall = false;
	private boolean isStart = false;
	private boolean isEnd = false;
	private boolean checked = false;
	private boolean inQueue = false;
	private boolean isQuickest = false;
	private Node previous;
	
	
	public Node(int x,int y) {
		this.x =x;
		this.y = y;
		
	}
	
	/*
	 * Gets and returns the x value of the node (array[x][])
	 */
	public int getX()
	{
		return this.x;
	}
	
	/*
	 * Gets and returns the y value of the node(array[][y])
	 */
	public int getY()
	{
		return this.y;
	}
	
	public void setF(int f)
	{
		this.f = f;
	}
	
	public int getF()
	{
		return this.f;
	}
	
	
	
	public void setG(int g)
	{
		this.g =g;
	}
	
	public int getG()
	{
		return this.g;
	}
	
	
	
	public void setH(int h)
	{
		this.h = h;
	}
	
	public int getH()
	{
		return this.h;
	}
	
	
	public void setCurrentCost(int c)
	{
		this.currentCost = c;
	}
	
	public int getCurrentCost()
	{
		return this.currentCost;
	}
	
	/*
	 * Sets this node equal to the starting node
	 */
	public void setStart()
	{
		this.isStart = true;
	}
	
	/*
	 * Sets this node equal to the ending node
	 */
	public void setEnd()
	{
		this.isEnd = true;
	}
	
	public void removeStart()
	{
		this.isStart = false;
	}
	
	public void removeEnd()
	{
		this.isEnd = false;
	}
	
	/*
	 * Sets this node equal to a wall
	 */
	public void setWall()
	{
		this.isWall = true;
	}
	
	/*
	 * If this node was a wall, this command sets isWall to false
	 */
	public void deleteWall()
	{
		this.isWall = false;
	}
	
	/*
	 * Checks node, used in bfs
	 */
	public void check()
	{
		this.checked =true;
	}
	
	/*
	 * Is true if the node is in the queue if the bfs
	 */
	public void putInQueue()
	{
		this.inQueue = true;
	}
	
	/*
	 * Is false if the node is not in the queue  of the bfs
	 */
	public void takeOutOfQueue()
	{
		this.inQueue = false;
	}
	
	/*
	 * Checks the boolean to see if this node is in the queue
	 */
	public boolean isInQueue()
	{
		return this.inQueue;
	}
	
	
	/*
	 * Checks to see if this node is the starting node
	 */
	public boolean isStart()
	{
		if(this.isStart == true)
			return true;
		return false;
	}
	
	/*
	 * Checks to see if this node is the ending node
	 */
	public boolean isEnd()
	{
		if (this.isEnd == true)
			return true;
		return false;
	}
	
	/*
	 * Checks to see if this node is a wall node
	 */
	public boolean isWall()
	{
		if(this.isWall == true)
			return true;
		return false;
	}
	
	/*
	 * Checks to see if the node has already been searched
	 */
	public boolean isChecked()
	{
		if(this.checked == true)
			return true;
		return false;

	}
	
	/*
	 * Is true if the node is part of the quickest path from the start node to the end node
	 */
	public boolean isQuickestPath()
	{
		return this.isQuickest;
	}
	
	/*
	 * Sets this node to the quickest path
	 */
	public void setQuickest()
	{
		this.isQuickest = true;
	}

	
	
	public void setPrevious(Node n)
	{
		this.previous = n;
	}
	
	public void removePrevious()
	{
		this.previous = null;
	}
	
	public Node getPrevious()
	{
		return this.previous;
	}
	
	public void reset()
	{
		this.checked = false;
		this.isQuickest = false;
		this.inQueue = false;
		this.previous = null;
	}

	@Override
	public int compareTo(Node o) {
		// TODO Auto-generated method stub
		if(this.h>o.h)
			return 1;
		else if(this.h<o.h)
			return -1;
		else{
			return 0;
		}
	}

	


}
