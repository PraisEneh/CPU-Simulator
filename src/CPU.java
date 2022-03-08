import java.util.Stack;

public class CPU {
	
	Stack<Process> cpuStack = new Stack<Process>();
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
		if(cpuStack.isEmpty()) {
			cpuStack.add(p);
			currentBurst = cpuStack.get(0).CPUburstTime.get(0);
		}
			System.out.println("Current CPU stack: "+cpuStack);
			System.out.println("Current Burst: "+ currentBurst);
		
	}
	
	public void decrementCPUburst() {
		System.out.println("Current Burst before CPU decrement: "+currentBurst);
		currentBurst--;
		System.out.println("New Burst after CPU decrement: "+ currentBurst);
	  
	}
	
	public Process returnProcess() {
		
		return cpuStack.pop();
		
	}
	
	
	public CPU() {
		
	}
	
}
