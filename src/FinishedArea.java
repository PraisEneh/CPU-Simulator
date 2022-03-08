import java.awt.EventQueue;

import javax.swing.JFrame;
import java.awt.BorderLayout;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.text.DefaultCaret;
import javax.swing.JLabel;

public class FinishedArea {

	public JFrame frmLog;
	public JTextArea finishedArea;
	
	
	
	
	/**
	 * Methods
	 */
	
	public void log(String action, int systemTime) {
		
		finishedArea.append(action+" at system time "+systemTime+"\n");
		finishedArea.append("\n");
	}
	public void log(String action) {
		
		finishedArea.append(action+"\n");
		finishedArea.append("\n");
	}
	
	public void clearFinishedTextArea() {
		this.finishedArea.setText(null);
	}

	public void addToFinishedTextArea(String string) {
		this.finishedArea.append(string + "\n");
	}
	 
	 
	 

	/**
	 * Create the application.
	 */
	public FinishedArea() {
		initialize();
		
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frmLog = new JFrame();
		frmLog.setTitle("Finished Queue");
		frmLog.setBounds(100, 100, 450, 353);
		frmLog.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frmLog.getContentPane().setLayout(new BorderLayout(0, 0));
		
		JPanel panel = new JPanel();
		frmLog.getContentPane().add(panel, BorderLayout.CENTER);
		
		JScrollPane scrollPane = new JScrollPane();
		panel.add(scrollPane);
		
		finishedArea = new JTextArea();
		finishedArea.setEditable(false);
		finishedArea.setColumns(35);
		finishedArea.setRows(15);
		scrollPane.setViewportView(finishedArea);
		DefaultCaret caret = (DefaultCaret)finishedArea.getCaret();
		caret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);
		
		JPanel panel_1 = new JPanel();
		scrollPane.setColumnHeaderView(panel_1);
		
		JLabel lblNewLabel = new JLabel("Finished Processes");
		panel_1.add(lblNewLabel);
	}

}
