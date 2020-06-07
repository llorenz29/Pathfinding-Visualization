import javax.swing.*;
import javax.swing.Timer;
import javax.swing.border.Border;
import java.util.Queue;
import java.util.concurrent.TimeUnit;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.*;





public class GUI extends JFrame implements ActionListener{
	
	public int spacing = 1;
	
	
	//for the selection boxes
	public int boxDistance = 100;
	public int boxWidth = 100;
	public int boxHeight = 40;
	
	//regarding interaction
	public int mouseX;
	public int mouseY;
	public int boxX;
	public int boxY;
	
	//for selection boxes
	public boolean drawWalls = false;
	public boolean deleteWalls = false;
	public boolean run = false;
	public boolean step1 = true;
	public boolean done = false;
	public boolean found = false;
	
	//determining what search to use
	public boolean useBFS = false;
	public boolean useDFS = false;
	public boolean useAStar = false;
	public boolean useGreedy = false;
	
	//for reseting the grid
	public boolean resetGrid = false;
	public boolean clearWalls = false;
	
	//size of the grid
	public int boxSize = 20;
	public int gridWidth = 60;
	public int gridHeight = 36;
	public boolean changeSize = false;
	public boolean isSmall =false;
	
	//for moving the starting and ending nodes
	public boolean moveStart = false;
	public boolean moveEnd = false;
	public boolean startMoved = false;
	public boolean endMoved = false;
	
	//array of nodes (graph)
	public Node[][] array = new Node[gridWidth][gridHeight];
	
	
	//timer
	public Timer t= new Timer(1000,this);
	
	//Queue for BFS and Stack for DFS
	public Queue<Node> q = new LinkedList<Node>();
	public Stack<Node> s = new Stack<Node>();
	
	public PriorityQueue<Node> pq = new PriorityQueue<Node>();
	public PriorityQueue<Node> closedPQ = new PriorityQueue<Node>();
	
	
	
	//start and end nodes (green and red)
	public Node start;
	public Node end;
	
	

	
	
	
	public GUI() {
		
		this.setTitle("Pathfinding using BFS/DFS");
		this.setSize(1200, 800);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setVisible(true);
		this.setResizable(false); //can not resize
		JButton button = new JButton();
		this.add(button);
		button.setVisible(true);
		
		/*
		 * Initializes nodes
		 */
		for (int i =0; i<gridWidth; i++)
			for(int j =0; j<gridHeight; j++) {
				array[i][j] = new Node(i,j);
				
			}
		
		Grid Grid = new Grid();
		this.setContentPane(Grid);
		Grid.setBorder(BorderFactory.createEmptyBorder());
		
		move move = new move();
		this.addMouseMotionListener(move);
		
		click click = new click();
		this.addMouseListener(click);
		
		
		
		
		
		
		
	}
	
	
	
	public class Grid extends JPanel  implements ActionListener{
		
		
		
		
		public void paintComponent(Graphics g) {
			
			
			paintWindow(g);
			
			
			if(changeSize) {
				if(isSmall) {
					boxSize /= 2;
					gridWidth *= 2;
					gridHeight *= 2;
					isSmall = false;
					
					
				}else {
					boxSize *= 2;
					gridWidth /= 2;
					gridHeight /= 2;
					isSmall = true;
					
				}
				changeSize = false;
				
			}
				
			
			if(clearWalls) {
				for(int i = 0; i<gridWidth; i++) {
					for(int j = 0; j<gridHeight; j++) {
						array[i][j].deleteWall();
					}
					}
				clearWalls = false;
			}
			//resets all elements of the grid
			if(resetGrid) {
				for(int i = 0; i<gridWidth; i++) {
					for(int j = 0; j<gridHeight; j++) {
						array[i][j].reset();
						done = false;
						found = false;
						run = false;
						q.clear();
						s.clear();
						pq.clear();
						closedPQ.clear();
						step1 = true;
						resetGrid = false;
					}
				}
				
						
			}
				
			
			if(run) {
				if(step1) { //for both BFS and DFS, adds the starting node into the queue/stack
					q.add(start);
					s.push(start);
					updateGHFVals();
					if(useGreedy) //adds start to the priority queue
						pq.add(start);
					else if(useAStar) //adds start to closed list
						closedPQ.add(start);
					
					
					step1 = false;
				}else if(done) {
					t.stop();
					run = false;
				}else {
				t.start();
				if(useBFS)
					bfs();
				else if(useDFS)
					dfs();
				else if(useAStar)
					aStar();
				else if(useGreedy)
					greedyBFS();
			}
			}
			
					
			}

		@Override
		public void actionPerformed(ActionEvent e) {
			// TODO Auto-generated method stub
			
		}
		
		
		
			
		}
	
	public class move implements MouseMotionListener{

		@Override
		public void mouseDragged(MouseEvent e) {
			
			updateSquares(e);			
	
		}

		@Override
		public void mouseMoved(MouseEvent e) {
			
			
		}
		
		
	}
		
	
	public class click implements MouseListener{
		

		@Override
		public void mouseClicked(MouseEvent e) {
			
			checkBoxes(e);
					
			
		}

		@Override
		public void mousePressed(MouseEvent e) {
			// TODO Auto-generated method stub
		}

		@Override
		public void mouseReleased(MouseEvent e) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void mouseEntered(MouseEvent e) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void mouseExited(MouseEvent e) {
			// TODO Auto-generated method stub
			
		}
	}
	
	
	
	public int getBoxX(int x) {
		return Math.floorDiv(x, boxSize);
	}
	public int getBoxY(int y) {
		return Math.floorDiv(y-80, boxSize);
	}
	
	/*
	 * Draws the boxes seen above the grid, and updates the colors if they are clicked/dragged across
	 */
	public void drawBoxes(Graphics g) {
		g.setColor(Color.white);
		//Draws the "Create Walls" box
		if(drawWalls)
			g.setColor(Color.green);
		
		g.drawRect(10, 10, boxWidth, boxHeight);
		g.drawString("Create Walls", 20, 35);
		
		g.setColor(Color.white);
		if(deleteWalls)
			g.setColor(Color.green);
		//Draws the "Delete Walls" Box
		g.drawRect(10 + boxDistance, 10, boxWidth, boxHeight);
		g.drawString("Delete Walls", 20+boxDistance, 35);
		
		g.setColor(Color.white);
		if(run)
			g.setColor(Color.green);
		//Draws the "Run" box
		g.drawRect(10 + 2*boxDistance, 10, boxWidth, boxHeight);
		g.drawString("Run", 20+2*boxDistance, 35);
		
		//Draws the "Use BFS" box
		g.setColor(Color.white);
		if(useBFS)
			g.setColor(Color.green);
		g.drawRect(10 + 3*boxDistance, 10, boxWidth, boxHeight);
		g.drawString("Use BFS" , 20+3*boxDistance, 35);
		
		
		//Draws the "Use DFS" box
		g.setColor(Color.white);
		if(useDFS)
			g.setColor(Color.green);
		g.drawRect(10 + 4*boxDistance, 10, boxWidth, boxHeight);
		g.drawString("Use DFS" , 20+4*boxDistance, 35);
		
		//Draws the "Use A*" box
		g.setColor(Color.white);
		if(useAStar)
			g.setColor(Color.green);
		g.drawRect(10 + 5*boxDistance, 10, boxWidth, boxHeight);
		g.drawString("Use A*", 20+5*boxDistance, 35);
		
		//Draws the "Move Start" Box
		g.setColor(Color.white);
		if(useGreedy)
			g.setColor(Color.green);
		g.drawRect(10+6*boxDistance, 10, boxWidth, boxHeight);
		g.drawString("Greedy BFS", 20+6*boxDistance, 35);
		
		
		//Draws the "Move End" Box
		g.setColor(Color.white);
		if(moveStart)
			g.setColor(Color.green);
		g.drawRect(10+7*boxDistance, 10, boxWidth, boxHeight);
		g.drawString("Move Start", 20+7*boxDistance, 35);
		
		
		//Draws the "Move End" Box
		g.setColor(Color.white);
		if(moveEnd)
			g.setColor(Color.green);
		g.drawRect(10+8*boxDistance, 10, boxWidth, boxHeight);
		g.drawString("Move End", 20+8*boxDistance, 35);
		
		g.setColor(Color.white);
		if(clearWalls)
			g.setColor(Color.green);
		g.drawRect(10+9*boxDistance, 10, boxWidth, boxHeight);
		g.drawString("Clear Walls", 20+9*boxDistance, 35);
		
//		g.setColor(Color.white);
////		if()
//		g.drawRect(10+8*boxDistance, 10, boxWidth, boxHeight);
//		g.drawString("Add Trees", 20+8*boxDistance, 35);
//		
//		g.setColor(Color.white);
////		if()
//		g.drawRect(10+9*boxDistance, 10, boxWidth, boxHeight);
//		g.drawString("Delete Trees", 20+9*boxDistance, 35);
		
		
		g.setColor(Color.white);
		if(changeSize)
			g.setColor(Color.green);
		g.drawRect(10+10*boxDistance, 10, boxWidth, boxHeight);
		g.drawString("Change Size", 20+10*boxDistance, 35);
		
		//Draws the "Reset Button"
		g.setColor(Color.white);
		if(resetGrid)
			g.setColor(Color.green);
		g.drawRect(10+11*boxDistance, 10, boxWidth, boxHeight);
		g.drawString("Reset", 20+11*boxDistance, 35);
		
		
		
	}
	
	/*
	 * Checks to see if one of the boxes  have been clicked, and performs actions
	 * drawWalls: allows the user to draw walls and set the nodes in those boxes to walls
	 * deleteWalls: allows the user to delete walls and the wall nodes
	 * run:
	 */
	public void checkBoxes(MouseEvent e) {
		//selects the drawWalls box
		if(e.getX()>=10 && e.getX()<=110 && e.getY()>= 10 && e.getY()<=70) {
			deleteWalls =false;
			drawWalls = true;
			run = false;
			moveStart = false;
			moveEnd =false;
			clearWalls = false;
			
		}
		//selects the deleteWalls box
		else if(e.getX()>= 10 + boxDistance && e.getX()<= 10+boxDistance + boxWidth && e.getY()>= 10 && e.getY()<=70) {
			deleteWalls = true;
			drawWalls = false;
			run =false;
			moveStart = false;
			moveEnd =false;
			clearWalls = false;
		}
		//Selects the run button
		else if(e.getX()>= 10 + 2*boxDistance && e.getX()<= 10 + 2*boxDistance + boxWidth && e.getY()>= 10 && e.getY()<=70) {
			
			run = true;
			deleteWalls = false;
			drawWalls = false;
		}
		//selects BFS button
		else if(e.getX() >= 10 + 3*boxDistance && e.getX()<= 10 + 3*boxDistance + boxWidth && e.getY()>= 10 && e.getY()<=70) {
			
			deleteWalls=false;
			drawWalls =false;
			useDFS = false;
			useBFS = true;
			useAStar = false;
			useGreedy = false;
			clearWalls = false;
			
		}
		//selects DFS button
		else if(e.getX() >= 10 + 4*boxDistance && e.getX()<= 10 + 4*boxDistance + boxWidth && e.getY()>= 10 && e.getY()<=70) {
			
			deleteWalls=false;
			drawWalls =false;
			useDFS = true;
			useBFS = false;
			useAStar = false;
			useGreedy = false;
			clearWalls = false;
		}
		//selects the A* button
		else if(e.getX() >= 10 + 5*boxDistance && e.getX()<= 10 + 5*boxDistance + boxWidth && e.getY()>= 10 && e.getY()<=70) {
							
			deleteWalls=false;
			drawWalls =false;
			useDFS = false;
			useBFS = false;	
			useAStar = true;
			useGreedy = false;
			clearWalls = false;
		}
		
		//selects the Greedy BFS button
		else if(e.getX() >= 10 + 6*boxDistance && e.getX()<= 10 + 6*boxDistance + boxWidth && e.getY()>= 10 && e.getY()<=70) {					
			deleteWalls=false;
			drawWalls =false;
			useDFS = false;
			useBFS = false;	
			useAStar = false;
			useGreedy = true;
			moveStart = false;
			moveEnd = false;
			clearWalls = false;
		}
		
		//selects the Move Start button
		else if(e.getX() >= 10 + 7*boxDistance && e.getX()<= 10 + 7*boxDistance + boxWidth && e.getY()>= 10 && e.getY()<=70) {					
			deleteWalls=false;
			drawWalls =false;
			useDFS = false;
			useBFS = false;	
			useAStar = false;
			moveStart = true;
			moveEnd = false;
			clearWalls = false;
		}
	
		//selects the Move End button
		else if(e.getX() >= 10 + 8*boxDistance && e.getX()<= 10 + 8*boxDistance + boxWidth && e.getY()>= 10 && e.getY()<=70) {					
			deleteWalls=false;
			drawWalls =false;
			useDFS = false;
			useBFS = false;	
			useAStar = false;
			moveStart = false;
			moveEnd = true;
			clearWalls = false;
		}
		
		else if(e.getX() >= 10 + 9*boxDistance && e.getX()<= 10 + 9*boxDistance + boxWidth && e.getY()>= 10 && e.getY()<=70) {					
			deleteWalls=false;
			drawWalls =false;
			useDFS = false;
			useBFS = false;	
			useAStar = false;
			moveStart = false;
			moveEnd = false;
			clearWalls = true;
		}
		
		
		
		//selects Change Size button
		else if(e.getX() >= 10 + 10*boxDistance && e.getX()<= 10 + 10*boxDistance + boxWidth && e.getY()>= 10 && e.getY()<=70) {
			
			deleteWalls = false;
			drawWalls = false;
			useDFS = false;
			useBFS = false;
			useAStar = false;
			startMoved = false;
			endMoved = false;
			clearWalls = false;
			//clears the moved starts and end nodes
			deleteAllStarts();
			deleteAllEnds();
			
			
			if(changeSize)
				changeSize = false;
			else if(changeSize == false)
				changeSize = true;

			
		}
		
	
		//selects reset button
		else if(e.getX() >= 10 + 11*boxDistance && e.getX()<= 10 + 11*boxDistance + boxWidth && e.getY()>= 10 && e.getY()<=70) {
			deleteWalls = false;
			drawWalls = false;
			useDFS = false;
			useBFS = false;
			useAStar = false;
			useGreedy = false;
			
			moveStart = false;
			moveEnd = false;
			
			resetGrid = true;
			
			
			//resets the starting and ending nodes
			startMoved = false;
			endMoved = false;
			//clears the moved starts and end nodes
			deleteAllStarts();
			deleteAllEnds();
		}
		
		
		
	}
	
	public void updateSquares(MouseEvent e) {
		// if the draw walls button has been selected
		if(drawWalls) {
			//sets the node to a wall
			if(e.getY() >= 60)
				array[getBoxX(e.getX())][getBoxY(e.getY())].setWall();
			}
		//if the delete walls button has been selected
		if (deleteWalls) 
			if(e.getY() >=  60)
				array[getBoxX(e.getX())][getBoxY(e.getY())].deleteWall();;
	
		//moves starting node
		if(moveStart) {
			if(e.getY() >= 60)
				deleteAllStarts();
				startMoved = true; //gets rid of OG starting position
				array[getBoxX(e.getX())][getBoxY(e.getY())].setStart();
			}
		
		//moves ending node
		if(moveEnd) {
			if(e.getY() >= 60)
				deleteAllEnds();
				endMoved = true; //gets rid of OG starting position
				array[getBoxX(e.getX())][getBoxY(e.getY())].setEnd();
			}
		}

		
	
		
	
	
	/*
	 * ************
	 * 
	 * BFS (Essentially Dijkstra's because all edge weights are equal)
	 * 
	 * ************
	 */
	public void bfs() {
			
			
			if(q.isEmpty()) {
				run = false;
				done = true;
				return;
			}
			
			//removes the first node from the queue
			Node current = q.remove();

			
			
			if (current.isChecked() != true) {
				
				if(current.equals(end)) {
					//Backtracks and finds the quickest path 
					while(current.isStart() != true) {
						current.setQuickest();
						current = current.getPrevious();
					}
					
					run = false;
					done = true;
					found = true;
				}

				current.check();
				current.takeOutOfQueue();
				
				
				if(current.getX()+1 >=0 && current.getX()+1<gridWidth)
					if(array[current.getX()+1][current.getY()].isChecked() != true && array[current.getX()+1][current.getY()].isWall() !=true) {
						q.add(array[current.getX()+1][current.getY()]);
						array[current.getX()+1][current.getY()].putInQueue();
						array[current.getX()+1][current.getY()].setPrevious(current);
					}
				
				if(current.getX()-1 >=0 && current.getX()-1<gridWidth)
					if (array[current.getX()-1][current.getY()].isChecked() != true && array[current.getX()-1][current.getY()].isWall() !=true) {
						q.add(array[current.getX()-1][current.getY()]);
						array[current.getX()-1][current.getY()].putInQueue();
						array[current.getX()-1][current.getY()].setPrevious(current);
					}
						
				if(current.getY()+1 >=0 && current.getY()+1<gridHeight && array[current.getX()][current.getY()+1].isWall() !=true)
					if(array[current.getX()][current.getY()+1].isChecked() != true) {
						q.add(array[current.getX()][current.getY()+1]);
						array[current.getX()][current.getY()+1].putInQueue();
						array[current.getX()][current.getY()+1].setPrevious(current);
					}
				
				if(current.getY()-1 >=0 && current.getY()-1<gridHeight && array[current.getX()][current.getY()-1].isWall() !=true)
					if (array[current.getX()][current.getY()-1].isChecked() != true) {
						q.add(array[current.getX()][current.getY()-1]);
						array[current.getX()][current.getY()-1].putInQueue();
						array[current.getX()][current.getY()-1].setPrevious(current);
					}
			}
			
			
			
		}

	/*
	 * ************
	 * 
	 * DFS
	 * 
	 * ************
	 */
	public void dfs() {
		
		Node current = s.pop();
		
		if(current.isChecked() != true) { // if the node has not already been checked
			
			if(current.equals(end)) {
				//Backtracks and finds the path taken by DFS (NOT THE QUICKEST PATH)
				while(current.isStart() != true) {
					current.setQuickest();
					current = current.getPrevious();
				}
				
				run = false;
				done = true;
			}
			
			current.check();
			
			if(current.isInQueue() == true)
				current.takeOutOfQueue();
			
			if(current.getX()+1 >=0 && current.getX()+1<gridWidth)
				if(array[current.getX()+1][current.getY()].isChecked() != true && array[current.getX()+1][current.getY()].isWall() !=true) {
					//pushes the element onto the stack
					s.push(array[current.getX()+1][current.getY()]);
					
					// doesnt actually put in queue, rather in stack (for coloring purposes)
					array[current.getX()+1][current.getY()].putInQueue(); 
					array[current.getX()+1][current.getY()].setPrevious(current);
				}
			
			if(current.getX()-1 >=0 && current.getX()-1<gridWidth)
				if (array[current.getX()-1][current.getY()].isChecked() != true && array[current.getX()-1][current.getY()].isWall() !=true) {
					s.push(array[current.getX()-1][current.getY()]);
					array[current.getX()-1][current.getY()].putInQueue();
					array[current.getX()-1][current.getY()].setPrevious(current);
				}
					
			if(current.getY()+1 >=0 && current.getY()+1<gridHeight && array[current.getX()][current.getY()+1].isWall() !=true)
				if(array[current.getX()][current.getY()+1].isChecked() != true) {
					s.push(array[current.getX()][current.getY()+1]);
					array[current.getX()][current.getY()+1].putInQueue();
					array[current.getX()][current.getY()+1].setPrevious(current);
				}
			
			if(current.getY()-1 >=0 && current.getY()-1<gridHeight && array[current.getX()][current.getY()-1].isWall() !=true)
				if (array[current.getX()][current.getY()-1].isChecked() != true) {
					s.push(array[current.getX()][current.getY()-1]);
					array[current.getX()][current.getY()-1].putInQueue();
					array[current.getX()][current.getY()-1].setPrevious(current);
				}
		}
			
			
		}
		
		
	public void aStar() {
		
		/*
		 * Start by adding starting block to closed list
		 * 
		 * Add all adjacent to open list and calculate fgh costs
		 * Specify the parent is the starting point
		 * 
		 * The check for all nodes in open list which one has the lowest f score
		 * then add that node to the closed list/remove from open list
		 * then check adjacent tiles already  on  the open list
		 * check to see if g score is improved by going through current node
		 * 
		 * then look back in open list for node with lowest f score
		 * 
		 * 
		 * 
		 * 
		 * 
		 */
		
		
		Node current;
		
		if(pq.isEmpty())
			current = start;
		else { //removes the element from the open list and adds it to the closed list
			
			current = pq.remove();
			closedPQ.add(current);
		}
		
		
		if(current.equals(end)) {
			
			while(current.isStart() != true) {
				

				
				
				current.setQuickest();
				current = current.getPrevious();
			}
			run = false;
			done = true;
			found = true;
			
		}
		
		//for visual coloring purposes
		current.check();
		if(current.isInQueue() == true)
			current.takeOutOfQueue();
		
		
		
		
		
		//add all adjacent nodes to the open list
		for(int i = 0; i<3; i++) {
			for(int j = 0; j<3; j++) {
				
				if(i == 1 && j ==1)
					continue;
				
				//checks for nodes that have not been checked at all
				if(current.getX()-1 + i >-1 && current.getX()-1 + i <gridWidth
					&& current.getY()-1 + j >-1 && current.getY()-1 +j <gridHeight) {
				if(pq.contains(array[current.getX()-1 +i][current.getY()-1 +j]) != true 
					&& closedPQ.contains(array[current.getX()-1 +i][current.getY()-1 +j]) != true 
					&& array[current.getX()-1 +i][current.getY()-1 +j].isWall() == false ) {
					
					
					
					array[current.getX()-1 +i][current.getY()-1 +j].putInQueue();
					
					//adds the node to the open list, sets the parent of the children equal to the current node
					pq.add(array[current.getX()-1 +i][current.getY()-1 +j]);
					if(array[current.getX()-1 +i][current.getY()-1 +j].getPrevious() == null)
						array[current.getX()-1 +i][current.getY()-1 +j].setPrevious(array[current.getX()][current.getY()]);

					
					//update the GHF Values
					
					//h value, between node and the end
					
					
					array[current.getX()-1 +i][current.getY()-1 +j].setH(diagonalHeuristic(array[current.getX()-1 +i][current.getY()-1 +j],end));
					
					//g value
					//if the g value has been set and it is currently larger than the alternative 
					if(array[current.getX()-1 +i][current.getY()-1 +j].getG() == -1)
						array[current.getX()-1 +i][current.getY()-1 +j].setG(array[current.getX()-1 +i][current.getY()-1 +j].getG() + getG(current,array[current.getX()-1 +i][current.getY()-1 +j]));
				
					else if(array[current.getX()-1 +i][current.getY()-1 +j].getG() != -1 
						&& diagonalHeuristic(array[current.getX()-1 +i][current.getY()-1 +j],end)
						<array[current.getX()-1 +i][current.getY()-1 +j].getG())
					array[current.getX()-1 +i][current.getY()-1 +j].setG(current.getG() + getG(current,array[current.getX()-1 +i][current.getY()-1 +j]));
					//above thing is off
						
					//f value
					array[current.getX()-1 +i][current.getY()-1 +j].setF(array[current.getX()-1 +i][current.getY()-1 +j].getG() + array[current.getX()-1 +i][current.getY()-1 +j].getH());
					
					//adds to the open list
					
					
					pq.add(array[current.getX()-1 +i][current.getY()-1 +j]);
					
					
				}	
				//if the node is in the open list and within bounds
				}
				if(current.getX()-1 + i >-1 && current.getX()-1 + i <gridWidth
				&& current.getY()-1 + j >-1 && current.getY()-1 +j <gridHeight) {
				if(closedPQ.contains(array[current.getX()-1 +i][current.getY()-1 +j]) != true 
						&& array[current.getX()-1 +i][current.getY()-1 +j].isWall() == false
						) {
					/*
					 * check their values
					 */
					
					//if the f  value has not been set
					if(array[current.getX()-1 +i][current.getY()-1 +j].getF() == -1) {
						array[current.getX()-1 +i][current.getY()-1 +j].setH(diagonalHeuristic(array[current.getX()-1 +i][current.getY()-1 +j], end));
						array[current.getX()-1 +i][current.getY()-1 +j].setG(current.getG() +getG(array[current.getX()][current.getY()], array[current.getX()-1 +i][current.getY()-1 +j]));
						array[current.getX()-1 +i][current.getY()-1 +j].setF(array[current.getX()-1 +i][current.getY()-1 +j].getG() + array[current.getX()-1 +i][current.getY()-1 +j].getH());
						array[current.getX()-1 +i][current.getY()-1 +j].setPrevious(current);
						
					}//the f value has been set
					else {
						if(array[current.getX()-1 +i][current.getY()-1 +j].getF()> diagonalHeuristic(array[current.getX()-1 +i][current.getY()-1 +j],end) + getG(array[current.getX()][current.getY()],array[current.getX()-1 +i][current.getY()-1 +j])) {
							array[current.getX()-1 +i][current.getY()-1 +j].removePrevious();
							array[current.getX()-1 +i][current.getY()-1 +j].setF(diagonalHeuristic(array[current.getX()-1 +i][current.getY()-1 +j],end) + getG(array[current.getX()][current.getY()],array[current.getX()-1 +i][current.getY()-1 +j]));
							array[current.getX()-1 +i][current.getY()-1 +j].setPrevious(current);
					}
						
					}
				}
					
					
					
				}
					
			}
			
		}
		
		
		
		
		
		
	}
	
	public void greedyBFS() {
		
	
		
		
		
		if(pq.isEmpty()) {
			run = false;
			done = true;
			return;
		}
		
		start.setCurrentCost(0);
		
		Node current = pq.remove();
		
		if(current.equals(end)) {
			
			while(current.isStart() != true) {
				current.setQuickest();
				current = current.getPrevious();
			}
			run = false;
			done = true;
			found = true;
			
		}
		
		current.check();
		
		if(current.isInQueue() == true)
			current.takeOutOfQueue();
		
		
		
		
		//adds to priority queue
		if(current.getX()+1 >=0 && current.getX()+1<gridWidth)
			if(array[current.getX()+1][current.getY()].isWall() !=true) {
				if(array[current.getX()+1][current.getY()].getF() == -1) { //if the f value has not been set
				//sets the f value (g+h)
				array[current.getX()+1][current.getY()].setF(array[current.getX()+1][current.getY()].getG() + array[current.getX()+1][current.getY()].getH());
				pq.add(array[current.getX()+1][current.getY()]);
				array[current.getX()+1][current.getY()].setPrevious(current);
				array[current.getX()+1][current.getY()].putInQueue();
				
				}else {//the node has  already been visited and has had an f value set
					if(array[current.getX()+1][current.getY()].getF()< array[current.getX()+1][current.getY()].getG() + array[current.getX()+1][current.getY()].getH()) {
						//if the node has a lower f value than the current possible path
						pq.add(array[current.getX()+1][current.getY()]);
						array[current.getX()+1][current.getY()].setF(array[current.getX()+1][current.getY()].getG() + array[current.getX()+1][current.getY()].getH());
						array[current.getX()+1][current.getY()].setPrevious(current);
						array[current.getX()+1][current.getY()].putInQueue();
					}
						
				}
					
				
			}
				
		if(current.getX()-1 >=0 && current.getX()-1<gridWidth)
			if(array[current.getX()-1][current.getY()].isWall() !=true) {
				if(array[current.getX()-1][current.getY()].getF() == -1) { //if the f value has not been set
				//sets the f value (g+h)
				array[current.getX()-1][current.getY()].setF(array[current.getX()-1][current.getY()].getG() + array[current.getX()-1][current.getY()].getH());
				pq.add(array[current.getX()-1][current.getY()]);
				array[current.getX()-1][current.getY()].setPrevious(current);
				array[current.getX()-1][current.getY()].putInQueue();
				}else {//the node has  already been visited and has had an f value set
					if(array[current.getX()-1][current.getY()].getF()< array[current.getX()-1][current.getY()].getG() + array[current.getX()-1][current.getY()].getH()) {
						//if the node has a lower f value than the current possible path
						pq.add(array[current.getX()-1][current.getY()]);
						array[current.getX()-1][current.getY()].setF(array[current.getX()-1][current.getY()].getG() + array[current.getX()-1][current.getY()].getH());
						array[current.getX()-1][current.getY()].setPrevious(current);
						array[current.getX()-1][current.getY()].putInQueue();
					}
						
				}
					
				
			}
			
		
		if(current.getY()+1 >=0 && current.getY()+1<gridHeight && array[current.getX()][current.getY()+1].isWall() !=true){
			if(array[current.getX()][current.getY()+1].isWall() !=true) {
				if(array[current.getX()][current.getY()+1].getF() == -1) { //if the f value has not been set
				//sets the f value (g+h)
				array[current.getX()][current.getY()+1].setF(array[current.getX()][current.getY()+1].getG() + array[current.getX()][current.getY()+1].getH());
				pq.add(array[current.getX()][current.getY()+1]);
				array[current.getX()][current.getY()+1].setPrevious(current);
				array[current.getX()][current.getY()+1].putInQueue();
				}else {//the node has  already been visited and has had an f value set
					if(array[current.getX()][current.getY()+1].getF()< array[current.getX()][current.getY()+1].getG() + array[current.getX()][current.getY()+1].getH()) {
						//if the node has a lower f value than the current possible path
						pq.add(array[current.getX()][current.getY()+1]);
						array[current.getX()][current.getY()+1].setF(array[current.getX()][current.getY()+1].getG() + array[current.getX()][current.getY()+1].getH());
						array[current.getX()][current.getY()+1].setPrevious(current);
						array[current.getX()][current.getY()+1].putInQueue();
					}
						
				}
					
				
			}
			}
				
		if(current.getY()-1 >=0 && current.getY()-1<gridHeight && array[current.getX()][current.getY()-1].isWall() !=true) {
			if(array[current.getX()][current.getY()-1].isWall() !=true) {
				if(array[current.getX()][current.getY()-1].getF() == -1) { //if the f value has not been set
				//sets the f value (g+h)
				array[current.getX()][current.getY()-1].setF(array[current.getX()][current.getY()-1].getG() + array[current.getX()][current.getY()-1].getH());
				pq.add(array[current.getX()][current.getY()-1]);
				array[current.getX()][current.getY()-1].setPrevious(current);
				array[current.getX()][current.getY()-1].putInQueue();
				}else {//the node has  already been visited and has had an f value set
					if(array[current.getX()][current.getY()-1].getF()< array[current.getX()][current.getY()-1].getG() + array[current.getX()][current.getY()-1].getH()) {
						//if the node has a lower f value than the current possible path
						pq.add(array[current.getX()][current.getY()-1]);
						array[current.getX()][current.getY()-1].setF(array[current.getX()][current.getY()-1].getG() + array[current.getX()][current.getY()-1].getH());
						array[current.getX()][current.getY()-1].setPrevious(current);
						array[current.getX()][current.getY()-1].putInQueue();
					}
						
				}
					
				
			}
		}
		
		
	
		
		
		
			
		
		

		
	}
		
	

	
	public void paintWindow(Graphics g) {
		g.setColor(Color.black);
		g.fillRect(0, 0, 1200, 800);
		
		
		for (int i =0; i<gridWidth; i++) { //creates the grid
			for (int j=0; j<gridHeight; j++) {
				
				g.setColor(Color.white);
				
				//if the user has not chosen to move the start
				if(startMoved == false) {
				if(isSmall == false) { // if the graph is currently large scale
					array[5][8].removeStart(); // gets rid of multiple starting and ending nodes
					
				//sets the start node
				if(i == 10 && j ==16) { //sets the starting node
					array[i][j].setStart();
					start = array[i][j];
				}
				
				
				
				}else { // the graph is currently in small scale
					array[10][16].removeStart(); // gets rid of multiple starting and ending nodes
					
					if(i == 5 && j ==8) { //sets the starting node
						g.setColor(Color.green);
						array[i][j].setStart();
						start = array[i][j];
					}
					
				}
				
				}
				//if the user has not chosen to move the end
				if(endMoved == false) {
					if(isSmall == false) { //if the graph is small
						array[24][8].removeEnd();
						//sets the end node
						if(i == 48 && j == 16) {
							
							array[i][j].setEnd();
							end = array[i][j];
						}
					}else {
						array[48][16].removeEnd();
						//sets the end node
						if(i == 24 && j == 8) {
							g.setColor(Color.red);
							array[i][j].setEnd();
							end = array[i][j];
						}
					}
					
				}
					
				//paints the start green
				if(array[i][j].isStart()) {
					g.setColor(Color.green);
					start = array[i][j];
				}
				//paints the end red
				if(array[i][j].isEnd()) {
					g.setColor(Color.red);
					end = array[i][j];
				}
				
				
				
				
				if(array[i][j].isChecked() == true && array[i][j].isStart() != true && array[i][j].isEnd()!= true)
					g.setColor(Color.CYAN);
				if(array[i][j].isInQueue() == true && array[i][j].isStart() != true && array[i][j].isEnd()!= true)
					g.setColor(Color.BLUE);
				if(array[i][j].isQuickestPath() && array[i][j].isStart() != true && array[i][j].isEnd()!= true)
					g.setColor(Color.yellow);
		
					
				//draws the walls
				if(array[i][j].isWall() && array[i][j].isStart() == false && array[i][j].isEnd() ==  false)
					g.setColor(Color.black);
				

				g.fillRect(spacing + i*boxSize , spacing + j*boxSize + 60, boxSize-2*spacing, boxSize-2*spacing);	
				
			}
		}
		
	
		drawBoxes(g);
		

		
	}
	
	
	
    public void actionPerformed(ActionEvent evt) {
        		
        	System.out.println("Running");
        	
        	if(done) {
        		t.stop();
        		//if there is a path from the start node to the end node
        		if(found)
        			System.out.println("End Node Found!");
       		}

        		
            	
        	
        }
    
    
    //deletes all occurences of the start node to prevent duplicates
    public void deleteAllStarts() {
    	for(int i = 0; i<gridWidth;i++)
    		for(int j = 0; j<gridHeight; j++)
    			array[i][j].removeStart();
    }
   
    //deletes all occurences of the end node to prevent duplicates
    public void deleteAllEnds() {
    	for(int i = 0; i<gridWidth;i++)
    		for(int j = 0; j<gridHeight; j++)
    			array[i][j].removeEnd();
    }
    
    
    //gets and sets the g and h values for all nodes in the graph, based on the positions of the starting and ending nodes
    public void updateGHFVals() {
    	for(int i = 0; i<gridWidth; i++) {
    		for(int j = 0; j<gridHeight; j++) {
    			int Gval = diagonalHeuristic(array[i][j],start);
    			array[i][j].setG(Gval);
    			int Hval = diagonalHeuristic(array[i][j],end);
    			array[i][j].setH(Hval);
    			
    			
    		}
    		
    	}
    			
    }
    
    
    //calculates the distance from the current node to the ending node, without using diagonals
    public int diagonalHeuristic(Node n, Node end) {
    	int[] coord = {n.getX(),n.getY()};
    	int[] endCoord = {end.getX(),end.getY()};
    	
    	int dx = Math.abs(coord[0]-endCoord[0]);
    	int dy = Math.abs(coord[1]-endCoord[1]);
    	//times 10
    	return(10 *(dx +dy) + (14 -2 * 10) * Math.min(dx, dy));
    	
    }
    
    public int getG(Node current, Node next){
    	
    	int[] coord = {current.getX(), current.getY()};
    	int[] nextNode = {next.getX(), next.getY()};
    	
    	//if they are on the same x/y line
    	if(next.getX() == current.getX() || next.getY() == current.getY())
    		return 10;
    	else
    		return 14;
    	
    	
    	
    	
    }
    

    
	}


