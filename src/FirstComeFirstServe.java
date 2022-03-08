import java.io.File;
import java.io.FileNotFoundException;
import java.util.LinkedList;
import java.util.Scanner;
import java.util.concurrent.atomic.AtomicBoolean;

public class FirstComeFirstServe implements Runnable {

	//display variables
	int avgTurnaroundTime;
	int avgWaitTime;
	int throughput;
	int systemTime = 0;
	
	//functioning variables
	int IOwaitTime;
	AtomicBoolean isRunning = new AtomicBoolean();
	private MainGUI GUI;
	private LogArea LogArea;
	private FinishedArea FinishedArea;
	public CPU cpu = new CPU();
	public IO io = new IO();

	LinkedList<Process> readyQueue = new LinkedList<Process>();
	LinkedList<Process> waitingQueue = new LinkedList<Process>();
	LinkedList<Process> finishedQueue = new LinkedList<Process>();


	/**
	 * Methods
	 * 
	 */

	/**
	 * Program waits for 1 second
	 * 
	 * @throws InterruptedException
	 */

	public void chill() {
		try {
			Thread.sleep(GUI.systemWaitTime);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			System.out.println("I can't chill");
		}
	}
	
	public void checkForPause() {
		while(GUI.isPaused.get()) {
			
		}
	}
	public void end() {
		if(isRunning.get()) {
			readyQueue.clear();
			waitingQueue.clear();
			io.ioStack.clear();
			cpu.cpuStack.clear();
			isRunning.set(false);
			GUI.clearProcessList();
			GUI.clearReadyTextArea();
			GUI.clearWaitingTextArea();
			GUI.clearCPUTextArea();
			GUI.clearIOTextArea();
			GUI.newProcess.clear();
			GUI.clearCPUBurstTextArea();
			GUI.clearIOBurstTextArea();
		}
	}
	
	public void incrementTime() {
		systemTime++;
		GUI.setSystemTimeTextArea(systemTime);
		System.out.println();
		System.out.println();
		System.out.println("Current System Time: "+systemTime);
	}

	/**
	 * Adds/Updates the ready Queue
	 * @param p
	 */

	public void addToReadyQueue() {
		for(int i = 0; i <= GUI.newProcess.size()-1; i++) {
			Process readyQueueProspect= GUI.newProcess.get(i);
			if(readyQueueProspect.getArrivalTime() == systemTime) {
				readyQueue.add(readyQueueProspect);
				LogArea.log("Process: "+readyQueueProspect.getName()+" was added to Ready Queue", systemTime);
			}else {
			}
			
		}

	}
	public void incrementReadyQueueWait() {
		for(int i=0; i<=readyQueue.size()-1; i++) {
			int CPUwait = readyQueue.get(i).getTotalCPUWaitTime()+1;
			readyQueue.get(i).setTotalCPUWaitTime(CPUwait);
		}	
	}
	public void incrementWaitingQueueWait() {
		for(int i=0; i<=waitingQueue.size()-1; i++) {
			int IOwait = waitingQueue.get(i).getTotalIOWaitTime()+1;
			waitingQueue.get(i).setTotalIOWaitTime(IOwait);
		}	
	}
	
	public void displayAvgThroughput() {
		double sum = 0;
		double avgThroughput;
		double numOfProcess = finishedQueue.size();
		for(int i=0;  i < finishedQueue.size(); i++) {
			sum += finishedQueue.get(i).getTotalCPUBurstTime();
			
			}
		
		avgThroughput= sum / numOfProcess;
		
		GUI.updateThroughputTextField(avgThroughput);
	}
	
	public void displayAvgWaitTime() {
		double sum = 0;
		double numOfProcess = finishedQueue.size();
		for(int i=0;  i < finishedQueue.size(); i++) {
			sum += finishedQueue.get(i).getTotalCPUWaitTime();
			
		}
		double avgWaitTime = sum / numOfProcess;
		GUI.updateAvgWaitTimeTextField(avgWaitTime);
	}
	public void displayAvgTurnaroundTime() {
		double sum = 0;
		double avgTT;
		double numOfProcess = finishedQueue.size();
		for(int i=0;  i < finishedQueue.size(); i++) {
			sum += finishedQueue.get(i).getTurnAroundTime();
			
			}
		
		avgTT= sum / numOfProcess;
		
		GUI.updateAvgTurnAroundTimeTextField(avgTT);
	}
	
	public void updateReadyQueueTextArea(){
		GUI.clearReadyTextArea();
		for(int i=0; i<=readyQueue.size()-1; i++) {
			if(readyQueue.contains(readyQueue.get(i))) {
				GUI.addToReadyTextArea(readyQueue.get(i).toString());
			}
		}
	}
	
	public void updateWaitingQueueTextArea(){
		GUI.clearWaitingTextArea();
		for(int i=0; i<=waitingQueue.size()-1; i++) {
			if(waitingQueue.contains(waitingQueue.get(i))) {
				GUI.addToWaitingTextArea(waitingQueue.get(i).toString());
			}
		}
		System.out.println("Current Waiting Q"+waitingQueue );
	}
	
		
	public void updateCPUStackTextArea() {
		GUI.clearCPUTextArea();
		for(int i=0; i<=cpu.cpuStack.size()-1; i++) {
			if(cpu.cpuStack.contains(cpu.cpuStack.get(i))) {
				GUI.addToCPUTextArea(cpu.cpuStack.get(i).name +", Burst: "+ cpu.cpuStack.get(i).CPUburstTime.get(i));
				updateCPUBurstTextArea();
			}
		}
		System.out.println("Current CPU Stack"+cpu.cpuStack );
	}
	
	public void updateCPUBurstTextArea() {
		GUI.setCPUBurstTextArea(cpu.currentBurst);

	}
	
	public void updateIOStackTextArea() {
		GUI.clearIOTextArea();
		for(int i=0; i<=io.ioStack.size()-1; i++) {
			if(io.ioStack.contains(io.ioStack.get(i))) {
				GUI.addToIOTextArea(io.ioStack.get(i).name +", Burst: "+ io.ioStack.get(i).IOburstTime.get(i));
				updateIOBurstTextArea();
			}
		}
		System.out.println("Current IO Stack"+io.ioStack );
	}
	
	public void updateIOBurstTextArea() {
		GUI.setIOBurstTextArea(io.currentBurst);

	}
	
	public void doCPUCycle() {
		
		if(cpu.cpuStack.isEmpty() && !readyQueue.isEmpty()) {
			cpu.addToStack(readyQueue.pop());
			LogArea.log("Process: "+cpu.cpuStack.get(0).getName()+" was added to CPU Stack", systemTime);

			updateCPUStackTextArea();
			
		}else if(!cpu.cpuStack.isEmpty()) {
			cpu.decrementCPUburst();
			updateCPUBurstTextArea();
		}
		
		
		if(cpu.currentBurst == 0 && !cpu.cpuStack.isEmpty()) {
			cpu.cpuStack.get(0).totalCPUWaitTime += cpu.cpuStack.get(0).CPUburstTime.get(0);
			cpu.cpuStack.get(0).finishedCPUburst.add(cpu.cpuStack.get(0).CPUburstTime.pop());
			GUI.clearCPUTextArea();
			//decide if it's going to the finished queue or waiting queue
			for(int i = 0; i <= cpu.cpuStack.size()-1; i++) {
				if(cpu.cpuStack.get(i).IOburstTime.isEmpty()) {
					LogArea.log("Process: "+cpu.cpuStack.get(0).getName()+" was added to Finished Queue", systemTime);
					cpu.cpuStack.get(0).setFinishTime(systemTime);
					if(!finishedQueue.contains(cpu.cpuStack.get(0))) {
						FinishedArea.addToFinishedTextArea(cpu.cpuStack.get(0).toString());
						finishedQueue.add(cpu.returnProcess());
						displayAvgTurnaroundTime();
						displayAvgWaitTime();
						displayAvgThroughput();
					}

				}else {
					LogArea.log("Process: "+cpu.cpuStack.get(0).getName()+" was added to Waiting Queue", systemTime);
					waitingQueue.add(cpu.returnProcess());

				}
			}
		}
		
	}
	
	
	
public void doIOCycle() {
		
		if(io.ioStack.isEmpty() && !waitingQueue.isEmpty()) {
			io.addToStack(waitingQueue.pop());
			LogArea.log("Process: "+io.ioStack.get(0).getName()+" was added to IO Stack", systemTime);
			updateIOStackTextArea();
			
		}else if(!io.ioStack.isEmpty()) {
			io.decrementIOburst();
			updateIOBurstTextArea();
		}
		
		
		if(io.currentBurst == 0 && !io.ioStack.isEmpty()) {
			io.ioStack.get(0).finishedIOburst.add(io.ioStack.get(0).IOburstTime.pop());
			GUI.clearIOTextArea();
			LogArea.log("Process: "+io.ioStack.get(0).getName()+" was added to Ready Queue from IO", systemTime);
			readyQueue.add(io.returnProcess());
		}
		
	}
	

	/**
	 * 
	 * @param p
	 */
	public void run() {

		while(GUI.isSimulating.get()) {
			isRunning.set(true);
			checkForPause();
			incrementTime();
			addToReadyQueue();
			updateReadyQueueTextArea();
			updateWaitingQueueTextArea();
			chill();
			doCPUCycle();
			incrementReadyQueueWait();
			updateReadyQueueTextArea();
			updateWaitingQueueTextArea();
			doIOCycle();
			incrementWaitingQueueWait();
			updateReadyQueueTextArea();
			updateWaitingQueueTextArea();
			chill();
			checkForPause();
				
			if(!GUI.isSimulating.get()) {LogArea.log("Simulation Ended", systemTime);
			end();
			break;}
			else if(readyQueue.isEmpty() && waitingQueue.isEmpty() && cpu.cpuStack.isEmpty() && io.ioStack.isEmpty()) {GUI.isSimulating.set(false);; LogArea.log("Simulation Ended", systemTime); systemTime = 0; break; }
			}
		
	}

	/**
	 * Constructors
	 * 
	 * @param p
	 */

	public FirstComeFirstServe(MainGUI main, LogArea Log, FinishedArea fin ) {
		this.GUI = main;
		this.LogArea = Log;
		this.FinishedArea = fin;

	}

}
