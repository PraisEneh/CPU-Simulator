import java.util.Stack;

public class IO {
	
	Stack<Process> ioStack = new Stack<Process>();
	int currentBurst = -3;
	
	/**
	 * 
	 * @param p - accepts a process p
	 * 
	 * Action: adds to stack if stack is empty
	 * then, sets the first and only process' CPU burst to the current burst
	 * If it's not empty decrement the CPU burst
	 */
	public void addToStack(Process p) {
		if(ioStack.isEmpty()) {
			ioStack.add(p);
			currentBurst = ioStack.get(0).IOburstTime.get(0);
		}
			System.out.println("Current IO stack: "+ioStack);
			System.out.println("Current Burst: "+ currentBurst);
		
	}
	
	public void decrementIOburst() {
		System.out.println("Current Burst before IO decrement: "+currentBurst);
		currentBurst--;
		System.out.println("New Burst after IO decrement: "+ currentBurst);
	  
	}
	
	public Process returnProcess() {
		
		return ioStack.pop();
		
	}
	
	
	public IO() {
		
	}
	
}
