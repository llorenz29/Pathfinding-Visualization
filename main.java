
public class main implements Runnable{
	
	GUI gui = new GUI();

	public static void main(String[] args) {
		
		new Thread(new main()).start();

	}
	
	public void run() {
		
		while(true)
			gui.repaint();
		
	}

}


//if(i == 0 && j==2 || (i ==2 && j==0) || (i==2 && j==2) || (i==0 && j==0)) { //corners
//	gCons = 14;
//
//	if(array[current.getX()-1 +i][current.getY()-1 +j].getH() == 0)
//		array[current.getX()-1 +i][current.getY()-1 +j].setH(manhattanHeuristic(array[current.getX()-1 +i][current.getY()-1 +j],end));
//
//	array[current.getX()-1 +i][current.getY()-1 +j].setG(array[current.getX()-1 +i][current.getY()-1 +j].getG()+gCons);
//	
//	if(array[current.getX()-1 +i][current.getY()-1 +j].getF()<
//			array[current.getX()-1 +i][current.getY()-1 +j].getG() + array[current.getX()-1 +i][current.getY()-1 +j].getH())
//		continue;
//	else {
//		array[current.getX()-1 +i][current.getY()-1 +j].setF(array[current.getX()-1 +i][current.getY()-1 +j].getG() + array[current.getX()-1 +i][current.getY()-1 +j].getH());
//		array[current.getX()-1 +i][current.getY()-1 +j].setPrevious(current);
//	}
//	
//						
//	pq.add(array[current.getX()-1 +i][current.getY()-1 +j]);
//	array[current.getX()-1 +i][current.getY()-1 +j].putInQueue();
//	
//
//	
//	
//}else {
//	gCons = 10;
//	
//	if(array[current.getX()-1 +i][current.getY()-1 +j].getH() == 0)
//		array[current.getX()-1 +i][current.getY()-1 +j].setH(manhattanHeuristic(array[current.getX()-1 +i][current.getY()-1 +j],end));
//
//	array[current.getX()-1 +i][current.getY()-1 +j].setG(array[current.getX()-1 +i][current.getY()-1 +j].getG()+gCons);
//	
//	if(array[current.getX()-1 +i][current.getY()-1 +j].getF()<
//			array[current.getX()-1 +i][current.getY()-1 +j].getG() + array[current.getX()-1 +i][current.getY()-1 +j].getH())
//		continue;
//	else {
//		array[current.getX()-1 +i][current.getY()-1 +j].setF(array[current.getX()-1 +i][current.getY()-1 +j].getG() + array[current.getX()-1 +i][current.getY()-1 +j].getH());
//		array[current.getX()-1 +i][current.getY()-1 +j].setPrevious(current);
//	}
//	pq.add(array[current.getX()-1 +i][current.getY()-1 +j]);
//	array[current.getX()-1 +i][current.getY()-1 +j].putInQueue();
//}
//	
//
//
//}
//}