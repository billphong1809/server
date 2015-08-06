package phong.android.com.view;

import java.io.Serializable;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

public class ServerViewChat extends JFrame implements Serializable {

	private JTextArea jtaServer;
	private JScrollPane scrollpane;
	private static final long serialVersionUID = 1L;

	public ServerViewChat() {
		initComponent();
	}
	public void initComponent() {
		jtaServer = new JTextArea();
		jtaServer.setEditable(false);
		scrollpane = new JScrollPane();
		scrollpane.setViewportView(jtaServer);
		setContentPane(scrollpane);
		setSize(550, 250);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setVisible(true);
		setTitle("Server Chat Mutithreading");
	}
	public void showMessage(String message) {
		jtaServer.append(message);
	}

}
