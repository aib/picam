package net.aib42.picam.ui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextField;

import net.aib42.picam.MainApp;

public class MainWindow implements ActionListener {
	private MainApp mainApp;

	private JTextField urlTextField;

	public MainWindow(MainApp mainApp) {
		this.mainApp = mainApp;

		JFrame frame = new JFrame("picam v" + mainApp.getVersionString());
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		frame.getContentPane().add(setupMainPanel());

		frame.pack();
		frame.setVisible(true);
	}

	private JPanel setupMainPanel() {
		JPanel mainPanel = new JPanel();
		mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.PAGE_AXIS));

		mainPanel.add(setupUrlPanel());
		mainPanel.add(new LiveviewPanel(mainApp, "lv"));

		return mainPanel;
	}

	private JPanel setupUrlPanel() {
		JPanel urlPanel = new JPanel();
		urlPanel.setLayout(new BoxLayout(urlPanel, BoxLayout.LINE_AXIS));

		urlTextField = new JTextField(mainApp.getCameraUrl());
		urlPanel.add(urlTextField);

		JButton urlSetButton = new JButton("Set");
		urlSetButton.setActionCommand("SET-URL");
		urlSetButton.addActionListener(this);
		urlPanel.add(urlSetButton);

		return urlPanel;
	}

	public void actionPerformed(ActionEvent e) {
		if (e.getActionCommand() == "SET-URL") {
			mainApp.setCameraUrl(urlTextField.getText());
		}
	}
}
