import java.util.LinkedList;

public class Process {
	int PID;
	String name;
	int priority;
	LinkedList<Integer> CPUburstTime  = new LinkedList<Integer>();
	LinkedList<Integer> IOburstTime = new LinkedList<Integer>();
	LinkedList<Integer> finishedCPUburst = new LinkedList<Integer>();
	LinkedList<Integer> finishedIOburst = new LinkedList<Integer>();
	
	int arrivalTime;
	int finishTime;
	int totalCPUWaitTime = 0;
	int totalIOWaitTime = 0;
	double turnAroundTime;
	double totalCPUBurstTime;
	
	//Cpu burst list and IO burst list
	public Process(int PID, String name, int arrivalTime, int priority, LinkedList<Integer> CPUburstTime, LinkedList<Integer> IOburstTime) {
		this.PID = PID;
		this.name = name;
		this.arrivalTime = arrivalTime;
		this.priority = priority;
		this.CPUburstTime = CPUburstTime;
		this.IOburstTime = IOburstTime;
		this.finishTime = 0;
		//this.turnAroundTime = turnAroundTime;

		for(int i = 0; i<= this.CPUburstTime.size()-1; i++) {
			this.totalCPUBurstTime += this.CPUburstTime.get(i);
		}

			
		
	}
	
	public String toString() {
		return "PID: "+PID+"| Name: "+name+"| Arrival Time: "+arrivalTime+"| Priority Level: "+priority+ "| CPU Burst: "+CPUburstTime.toString()+
				"| IO Burst: "+IOburstTime.toString()+"| CPU Wait: "+totalCPUWaitTime+"| IO Wait: "+totalIOWaitTime+"| Turnaround Time: "+turnAroundTime+"| Total CPU Burst Time: "+totalCPUBurstTime;
	} 
	
	//getters
	/*public int getTurnAroundTime() {
		return this.turnAroundTime;
	}*/
	public int getPID() {
		return this.PID;
	}
	public String getName(){
		
		return this.name;
	}
	public int getPriority(){
		return this.priority;
	}
	/*public int[] getCPUBurstTime(int i) {
		CPUburstTime	
	}
	public double getIOBurstTime() {
		return this.IOburstTime;
	}*/
	public double getArrivalTime() {
		return this.arrivalTime;	
	}
	public double getFinishTime() {
		return this.finishTime;
	}
	public double getTurnAroundTime() {
		return this.finishTime;
	}
	public int getTotalCPUWaitTime() {
		return this.totalCPUWaitTime;
	}
	public int getTotalIOWaitTime() {
		return this.totalIOWaitTime;
	}
	public double getTotalCPUBurstTime() {
		return this.totalCPUBurstTime;
	}
	
	//setters
	/*public void setTurnAroundTime(int turnAroundTime) {
		this.turnAroundTime = turnAroundTime;
	}*/
	public void setPID(int PID) {
		this.PID = PID;
	}
	public void setName(String name) {
		this.name = name;
	}
	public void setPriority(int priority) {
		if(priority <10 && priority <=0) {
			this.priority = priority;
		}
		else {
			System.out.println("Error: Invalid Priority Listing");
		}
	}
	/*public void setCPUbursttime(double CPUburstTime) {
		this.CPUburstTime = CPUburstTime;
	}
	public void setIObursttime(double IOburstTime) {
		this.IOburstTime = IOburstTime;
	}*/
	public void setArrivalTime(int arrivalTime) {
		this.arrivalTime = arrivalTime;
	}
	public void setFinishTime(int finishTime) {
		this.finishTime = finishTime;
	}
	public void setTurnAroundTime(double time) {
		this.turnAroundTime = time;
	}
	public void setTotalCPUWaitTime(int time) {
		this.totalCPUWaitTime = time;
	}
	public void setTotalIOWaitTime(int time) {
		this.totalIOWaitTime = time;
	}
	
}
