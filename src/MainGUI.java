import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JLabel;

import java.awt.BorderLayout;
import javax.swing.JPanel;
import javax.swing.JMenuBar;
import javax.swing.JOptionPane;
import javax.swing.JButton;
import javax.swing.JFileChooser;

import java.awt.Choice;
import javax.swing.JTextPane;
import javax.swing.JSeparator;
import java.awt.SystemColor;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Dictionary;
import java.util.Hashtable;
import java.util.LinkedList;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

import javax.swing.JTextArea;
import javax.swing.JScrollPane;
import java.awt.FlowLayout;
import java.awt.Insets;
import java.awt.ComponentOrientation;
import javax.swing.SwingConstants;
import javax.swing.JLayeredPane;
import javax.swing.Box;
import javax.swing.JSplitPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.JSlider;
import javax.swing.event.ChangeListener;
import javax.swing.event.ChangeEvent;

//Maintain ready list in GUI

public class MainGUI {

	private JFrame frmCpuSchedulingSimulator;
	LogArea logWindow = new LogArea();
	FinishedArea finishedWindow = new FinishedArea();
	File file;
	FileReader fr;
	Thread worker;
	String sourceFile;
	String chosenAlgorithm = "First Come First Serve";
	JTextArea readyArea;
	JTextArea waitingArea;
	JTextArea processList;
	JTextPane textPane = new JTextPane();
	JButton btnNewButton;
	private Choice choice;
	FirstComeFirstServe FCFS;
	PriorityScheduling PS;
	ShortestJobFirst SJF;
	RoundRobin RR;

	LinkedList<Process> newProcess = new LinkedList<Process>();
	Process process;
	String name;
	// LinkedList<Integer> CPUburst = new LinkedList<Integer>(), IOburst = new
	// LinkedList<Integer>();
	// LinkedList<Integer> CPUburst2 = new LinkedList<Integer>(), IOburst2 = new
	// LinkedList<Integer>();
	int PID, priority, arrivalTime;
	Integer sliderValue = 1;
	long systemWaitTime = 1000;
	private JTextArea cpuTextArea;
	private JTextArea ioTextArea;
	AtomicBoolean isSimulating = new AtomicBoolean();
	AtomicBoolean stopSimulating = new AtomicBoolean();
	AtomicBoolean isPaused = new AtomicBoolean();
	private JTextField systemTimeTextField;
	private JTextField currentCPUBurstTextField;
	private JTextField currentIOBurstTextField;
	private JButton startSchedulingbtn;
	private JTextField sliderTextField;
	private JTextField avgTurnaroundTimeTextField;
	private JTextField avgWaitTimeTextField;
	private JTextField avgThroughputTextField;
	private JTextField QuantumTimeTextField;
	int quantumTime;

	/**
	 * Methods
	 * 
	 * @return
	 */

	// adds the specific process in question
	public void addSpecificProcess(LinkedList<Integer> CPU, LinkedList<Integer> IO) {
		// CPUburst2 = CPU;
		// IOburst2 = IO;
		process = new Process(PID, name, arrivalTime, priority, CPU, IO);
		newProcess.add(process);
		System.out.println("Added process: " + process.toString());
	}

	// adds entire process list from the file
	public void addProcess() {
		// reading file
		file = new File(sourceFile);
		try {
			Scanner sc = new Scanner(new FileReader(file));

			// assigning inital process list
			while (sc.hasNextLine()) {
				PID = sc.nextInt();
				name = sc.next();
				arrivalTime = sc.nextInt();
				priority = sc.nextInt();
				LinkedList<Integer> CPUburst = new LinkedList<Integer>(), IOburst = new LinkedList<Integer>();
				System.out
						.println("before inner while loop within addProcess. Name: " + name + " Priority: " + priority);
				while (sc.hasNextInt()) {

					CPUburst.addLast(sc.nextInt());
					System.out.println("added to CPU burst" + CPUburst);
					if (sc.hasNext("end")) {
						sc.nextLine();
						break;
					}
					if (sc.hasNext()) {

						IOburst.addLast(sc.nextInt());
						System.out.println("added to IO burst" + IOburst);
						if (sc.hasNext("end")) {
							sc.nextLine();
							break;
						}

					}

				}
				System.out.println("after inner while loop within addProcess");
				processList.append("PID: " + PID + "| Name: " + name + "| Arrival Time: " + arrivalTime + "| Priority: "
						+ priority + "| CPU Burst: " + CPUburst + "| IO Burst: " + IOburst + "\n");
				processList.append(
						"------------------------------------------------------------------------------------------------------------------\n");
				addSpecificProcess(CPUburst, IOburst);
				for (int i = 0; i <= newProcess.size() - 1; i++) {
					Process p = newProcess.get(i);
					System.out.println("Check to ensure it's actually in queue: " + p.toString());
				}

				// CPUburst.clear();
				// IOburst.clear();

			}
			System.out.println("finished adding new processes");
			sc.close();
		} catch (FileNotFoundException ex) {
			ex.getLocalizedMessage();
		}

	}

	//utility methods for Queue display
	public void updateThroughputTextField(Double avgThrough) {
		String s = avgThrough.toString();
		avgThroughputTextField.setText(s);	
	}
	
	public void updateAvgWaitTimeTextField(Double avgWait) {
		String s = avgWait.toString();
		avgWaitTimeTextField.setText(s);	
		
	}
	
	public void updateAvgTurnAroundTimeTextField(Double avgTT) {
		String s = avgTT.toString();
		avgTurnaroundTimeTextField.setText(s);	
	}
	
	public void clearProcessList() {
		this.processList.setText(null);
	}
	
	public void clearReadyTextArea() {
		this.readyArea.setText(null);
	}

	public void addToReadyTextArea(String string) {
		this.readyArea.append(string + "\n");
	}
	
	public void clearWaitingTextArea() {
		this.waitingArea.setText(null);
	}
	
	public void addToWaitingTextArea(String string) {
		this.waitingArea.append(string + "\n");
	}
	
	//utility methods for CPU display
	public void addToCPUTextArea(String string) {
		this.cpuTextArea.append(string + "\n");
	}
	
	public void clearCPUTextArea() {
		this.cpuTextArea.setText(null);
	}

	public void setCPUBurstTextArea(int burst) {
		String s = Integer.toString(burst);
		this.currentCPUBurstTextField.setText(s);
	}
	
	public void clearCPUBurstTextArea() {
		this.currentCPUBurstTextField.setText(null);
	}
	
	//utility methods for IO display
	public void addToIOTextArea(String string) {
		this.ioTextArea.append(string + "\n");
	}
	
	public void clearIOTextArea() {
		this.ioTextArea.setText(null);
	}

	public void setIOBurstTextArea(int burst) {
		String s = Integer.toString(burst);
		this.currentIOBurstTextField.setText(s);
	}
	
	public void clearIOBurstTextArea() {
		this.currentIOBurstTextField.setText(null);
	}
	
	//utility method for system time display
	public void setSystemTimeTextArea(int time) {
		String s = Integer.toString(time);
		this.systemTimeTextField.setText(s);
	}

	public String getFile() {
		JFileChooser chooser = new JFileChooser();
		int returnVal = chooser.showOpenDialog(btnNewButton);
		if (returnVal == JFileChooser.APPROVE_OPTION) {
			file = chooser.getSelectedFile();
			textPane.setText(file.getName());
			sourceFile = file.getPath();
			chooser.setCurrentDirectory(file.getParentFile());
			return file.getPath();
		} else {
				
		}
		return "No file selected";

	}


	public void startScheduling() {
		logWindow.clearLog();
		if (chosenAlgorithm == "First Come First Serve") {
			System.out.println("User selected " + chosenAlgorithm + ".");
			/**
			 * Launch LogArea the application.
			 */
			EventQueue.invokeLater(new Runnable() {
				public void run() {
					try {
						 logWindow = new LogArea();
						 finishedWindow = new FinishedArea();
						
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			});
			
			// need to pass through "this" GUI if you want to have another class edit it.
			 FCFS = new FirstComeFirstServe(this, this.logWindow, this.finishedWindow );
			 worker = new Thread(FCFS);
			isSimulating.set(true);
			worker.start();
			
			

		} else if (chosenAlgorithm == "Shortest Job First") {
			System.out.println("User selected " + chosenAlgorithm + ".");
			/**
			 * Launch LogArea the application.
			 */
			EventQueue.invokeLater(new Runnable() {
				public void run() {
					try {
						 logWindow = new LogArea();
						 finishedWindow = new FinishedArea();

					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			});
			
			// need to pass through "this" GUI if you want to have another class edit it.
			SJF = new ShortestJobFirst(this, this.logWindow, this.finishedWindow );
			worker = new Thread(SJF);
			isSimulating.set(true);
			worker.start();
		} else if (chosenAlgorithm == "Priority Scheduling") {
			System.out.println("User selected " + chosenAlgorithm + ".");
			/**
			 * Launch LogArea the application.
			 */
			EventQueue.invokeLater(new Runnable() {
				public void run() {
					try {
						 logWindow = new LogArea();
						 finishedWindow = new FinishedArea();
						
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			});
			
			// need to pass through "this" GUI if you want to have another class edit it.
			PS = new PriorityScheduling(this, this.logWindow, this.finishedWindow );
			worker = new Thread(PS);
			isSimulating.set(true);
			worker.start();
		}else if (chosenAlgorithm == "Round Robin") {
			System.out.println("User selected " + chosenAlgorithm + ".");
			/**
			 * Launch LogArea the application.
			 */
			EventQueue.invokeLater(new Runnable() {
				public void run() {
					try {
						 logWindow = new LogArea();
						 finishedWindow = new FinishedArea();
						
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			});
			
			// need to pass through "this" GUI if you want to have another class edit it.
			quantumTime = Integer.parseInt(QuantumTimeTextField.getText());
			RR = new RoundRobin(this, this.logWindow, this.finishedWindow );
			worker = new Thread(RR);
			isSimulating.set(true);
			worker.start();
		}
		startSchedulingbtn.setText("Pause Scheduling");
		stopSimulating.set(false);
		isPaused.set(false);
		

	}
	public void resumeScheduling() {
		isPaused.set(false);
		startSchedulingbtn.setText("Pause Scheduling");
	}
	public void pauseScheduling() {
		isPaused.set(true);
		
		
		startSchedulingbtn.setText("Resume Scheduling");
	}

	public void stopScheduling() {
		isSimulating.set(false);
		clearProcessList();
		clearReadyTextArea();
		clearWaitingTextArea();
		clearCPUTextArea();
		clearIOTextArea();
		newProcess.clear();	
		System.exit(0);
		
		startSchedulingbtn.setText("Start Scheduling");
		stopSimulating.set(true);
		System.out.println("Stopping Simulation.....");
	}

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					MainGUI window = new MainGUI();
					window.frmCpuSchedulingSimulator.setVisible(true);
					
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public MainGUI() {
		initialize();
		logWindow.frmLog.setLocation(900, 100);
		logWindow.frmLog.setVisible(true);
		finishedWindow.frmLog.setLocation(900, 454);
		finishedWindow.frmLog.setVisible(true);
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frmCpuSchedulingSimulator = new JFrame();
		frmCpuSchedulingSimulator.setTitle("CPU Scheduling Simulator");
		frmCpuSchedulingSimulator.setBounds(100, 100, 900, 540);
		frmCpuSchedulingSimulator.setLocation(0, 100);
		frmCpuSchedulingSimulator.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frmCpuSchedulingSimulator.getContentPane().setLayout(new BorderLayout(0, 0));

		JPanel panel = new JPanel();
		frmCpuSchedulingSimulator.getContentPane().add(panel, BorderLayout.NORTH);
		panel.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
		
				JLabel systemTime = new JLabel("System Time:");
				panel.add(systemTime);
				systemTime.setLabelFor(systemTimeTextField);
				
						systemTimeTextField = new JTextField();
						systemTimeTextField.setEditable(false);
						systemTimeTextField.setText("0");
						panel.add(systemTimeTextField);
						systemTimeTextField.setColumns(2);

		JButton addProcessButton = new JButton("New Process");
		addProcessButton.setName("");
		panel.add(addProcessButton);

		startSchedulingbtn = new JButton("Start Scheduling");
		panel.add(startSchedulingbtn);

		JButton stopSchedulingbtn = new JButton("Stop Scheduling");
		panel.add(stopSchedulingbtn);
		
		JLabel adjustSpeedLabel = new JLabel("Adjust Speed:");
		panel.add(adjustSpeedLabel);
		
		Dictionary<Integer, JLabel> dict = new Hashtable<Integer, JLabel>();
		dict.put(0, new JLabel("Faster"));
		//dict.put(1, new JLabel("Fast"));
		//dict.put(4, new JLabel("Slow"));
		dict.put(5, new JLabel("Slower"));

		JSlider slider = new JSlider(0,5,1);
		slider.setMajorTickSpacing(1);
		slider.setPaintTicks(true);
		//slider.createStandardLabels(1);
		slider.setLabelTable(dict);
		slider.setPaintLabels(true);
		slider.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				if(slider.getValue()==0) {
					systemWaitTime = slider.getValue()+400;
				}else {
				systemWaitTime = slider.getValue()*1000;
				}
				sliderValue = slider.getValue();
				String s = sliderValue.toString();
				sliderTextField.setText(s);
			}
		});
		panel.add(slider);
		
		sliderTextField = new JTextField();
		sliderTextField.setEditable(false);
		sliderTextField.setText("1");
		panel.add(sliderTextField);
		sliderTextField.setColumns(2);
		startSchedulingbtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(startSchedulingbtn.getText() == "Start Scheduling") {
					startScheduling();
				}else if(startSchedulingbtn.getText() == "Resume Scheduling") {
						resumeScheduling();

				}else if(startSchedulingbtn.getText() == "Pause Scheduling") {
					pauseScheduling();
				}
			}
		});
		stopSchedulingbtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				stopScheduling();
			}
		});

		Insets marginInsets = new Insets(3, 3, 3, 3);
		processList = new JTextArea(10, 10);
		processList.setEditable(false);
		processList.setMargin(marginInsets);
		JScrollPane processScroll = new JScrollPane(processList, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
				JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		frmCpuSchedulingSimulator.getContentPane().add(processScroll, BorderLayout.WEST);

		JPanel panel_1 = new JPanel();
		processScroll.setColumnHeaderView(panel_1);

		JLabel processListLabel = new JLabel("Process List:");
		panel_1.add(processListLabel);

		JSplitPane splitPane = new JSplitPane();
		splitPane.setResizeWeight(0.5);
		splitPane.setDividerLocation(-5);
		splitPane.setDividerSize(7);
		frmCpuSchedulingSimulator.getContentPane().add(splitPane, BorderLayout.CENTER);

		JScrollPane scrollPane = new JScrollPane();
		splitPane.setLeftComponent(scrollPane);

		readyArea = new JTextArea(10, 10);
		readyArea.setEditable(false);
		scrollPane.setViewportView(readyArea);

		JPanel panel_2 = new JPanel();
		scrollPane.setColumnHeaderView(panel_2);

		JLabel lblNewLabel = new JLabel("Ready Queue:");
		panel_2.add(lblNewLabel);

		JScrollPane scrollPane_1 = new JScrollPane();
		splitPane.setRightComponent(scrollPane_1);

		JPanel panel_3 = new JPanel();
		scrollPane_1.setColumnHeaderView(panel_3);

		JLabel lblNewLabel_1 = new JLabel("Waiting Queue:");
		panel_3.add(lblNewLabel_1);

		waitingArea = new JTextArea();
		waitingArea.setColumns(10);
		waitingArea.setRows(10);
		scrollPane_1.setViewportView(waitingArea);
		
		JPanel panel_6 = new JPanel();
		frmCpuSchedulingSimulator.getContentPane().add(panel_6, BorderLayout.SOUTH);
		
		JLabel avgTurnAroundTimeLabel = new JLabel("Avg Turnaround Time:");
		panel_6.add(avgTurnAroundTimeLabel);
		
		avgTurnaroundTimeTextField = new JTextField();
		avgTurnAroundTimeLabel.setLabelFor(avgTurnaroundTimeTextField);
		avgTurnaroundTimeTextField.setEditable(false);
		panel_6.add(avgTurnaroundTimeTextField);
		avgTurnaroundTimeTextField.setColumns(3);
		
		JLabel avgWaitTimeLabel = new JLabel("Avg Wait Time:");
		panel_6.add(avgWaitTimeLabel);
		
		avgWaitTimeTextField = new JTextField();
		avgWaitTimeLabel.setLabelFor(avgWaitTimeTextField);
		avgWaitTimeTextField.setEditable(false);
		panel_6.add(avgWaitTimeTextField);
		avgWaitTimeTextField.setColumns(3);
		
		JLabel throughputLabel = new JLabel("Throughput:");
		panel_6.add(throughputLabel);
		
		avgThroughputTextField = new JTextField();
		avgThroughputTextField.setEditable(false);
		panel_6.add(avgThroughputTextField);
		avgThroughputTextField.setColumns(3);
		
		JLabel quantumTimeLabel = new JLabel("Quantum Time: ");
		panel_6.add(quantumTimeLabel);
		
		QuantumTimeTextField = new JTextField();
		quantumTimeLabel.setLabelFor(QuantumTimeTextField);
		QuantumTimeTextField.setEditable(true);
		panel_6.add(QuantumTimeTextField);
		QuantumTimeTextField.setColumns(3);

		JSplitPane splitPane_1 = new JSplitPane();
		frmCpuSchedulingSimulator.getContentPane().add(splitPane_1, BorderLayout.EAST);

		JScrollPane scrollPane_2 = new JScrollPane();
		splitPane_1.setLeftComponent(scrollPane_2);

		cpuTextArea = new JTextArea(10, 10);
		scrollPane_2.setViewportView(cpuTextArea);

		JPanel panel_4 = new JPanel();
		scrollPane_2.setColumnHeaderView(panel_4);

		JLabel lblNewLabel_2 = new JLabel("CPU Usage:");
		panel_4.add(lblNewLabel_2);
		
		currentCPUBurstTextField = new JTextField();
		panel_4.add(currentCPUBurstTextField);
		currentCPUBurstTextField.setColumns(2);

		JScrollPane scrollPane_3 = new JScrollPane();
		splitPane_1.setRightComponent(scrollPane_3);

		ioTextArea = new JTextArea(10, 10);
		scrollPane_3.setViewportView(ioTextArea);

		JPanel panel_5 = new JPanel();
		scrollPane_3.setColumnHeaderView(panel_5);

		JLabel lblNewLabel_3 = new JLabel("I/O");
		panel_5.add(lblNewLabel_3);
		
		currentIOBurstTextField = new JTextField();
		currentIOBurstTextField.setEditable(false);
		panel_5.add(currentIOBurstTextField);
		currentIOBurstTextField.setColumns(2);
		addProcessButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				addProcess();
			}
		});

		JMenuBar menuBar = new JMenuBar();
		menuBar.setBackground(SystemColor.menu);
		menuBar.setForeground(SystemColor.menu);
		frmCpuSchedulingSimulator.setJMenuBar(menuBar);

		JLabel sourceFile = new JLabel("Source File: ");
		menuBar.add(sourceFile);

		textPane.setFocusable(false);
		textPane.setEditable(false);
		menuBar.add(textPane);

		btnNewButton = new JButton("Load FIle");
		menuBar.add(btnNewButton);
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				getFile();
				System.out.println("Got file");
			}
		});

		JSeparator separator = new JSeparator();
		menuBar.add(separator);

		JLabel algorithmLabel = new JLabel("Selected Algorithm: ");
		menuBar.add(algorithmLabel);

		choice = new Choice();
		choice.setForeground(SystemColor.menuText);
		choice.add("First Come First Serve");
		choice.add("Shortest Job First");
		choice.add("Priority Scheduling");
		choice.add("Round Robin");
		choice.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e) {

				chosenAlgorithm = choice.getSelectedItem();

			}
		});
		menuBar.add(choice);

	}
}
