import java.awt.EventQueue;

import javax.swing.JFrame;
import java.awt.BorderLayout;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.text.DefaultCaret;
import javax.swing.JLabel;

public class LogArea {

	public JFrame frmLog;
	public JTextArea logArea;
	
	
	
	
	/**
	 * Methods
	 */
	
	public void log(String action, int systemTime) {
		
		logArea.append(action+" at system time "+systemTime+"\n");
		logArea.append("\n");
	}
	public void clearLog() {
		logArea.setText(null);
	}
	
	public void restart() {
		frmLog.getContentPane().invalidate();
		frmLog.getContentPane().validate();
		frmLog.getContentPane().repaint();
		
	}
	 
	 
	 

	/**
	 * Create the application.
	 */
	public LogArea() {
		initialize();
		
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frmLog = new JFrame();
		frmLog.setTitle("Log Window");
		frmLog.setBounds(100, 100, 450, 353);
		frmLog.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frmLog.getContentPane().setLayout(new BorderLayout(0, 0));
		
		JPanel panel = new JPanel();
		frmLog.getContentPane().add(panel, BorderLayout.CENTER);
		
		JScrollPane scrollPane = new JScrollPane();
		panel.add(scrollPane);
		
		logArea = new JTextArea();
		logArea.setEditable(false);
		logArea.setColumns(35);
		logArea.setRows(15);
		scrollPane.setViewportView(logArea);
		DefaultCaret caret = (DefaultCaret)logArea.getCaret();
		caret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);
		
		JPanel panel_1 = new JPanel();
		scrollPane.setColumnHeaderView(panel_1);
		
		JLabel lblNewLabel = new JLabel("Log");
		panel_1.add(lblNewLabel);
	}

}
