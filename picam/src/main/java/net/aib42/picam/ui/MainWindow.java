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
	private JButton recordButton;
	private boolean recording;

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
		mainPanel.add(setupViewPanel());
		mainPanel.add(setupControls());

		return mainPanel;
	}

	private JPanel setupViewPanel() {
		JPanel viewPanel = new JPanel();
		viewPanel.setLayout(new BoxLayout(viewPanel, BoxLayout.LINE_AXIS));

		viewPanel.add(new LiveviewPanel(mainApp, "lv"));
		viewPanel.add(new LiveviewPanel(mainApp, "vc"));
//		viewPanel.add(new RecordingPanel(mainApp));

		return viewPanel;
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

	private JPanel setupControls() {
		JPanel controls = new JPanel();
		controls.setLayout(new BoxLayout(controls, BoxLayout.LINE_AXIS));

		JButton zoomIn = new JButton("Zoom +");
		zoomIn.setActionCommand("ZOOM-IN");
		zoomIn.addActionListener(this);
		controls.add(zoomIn);

		JButton zoomOut = new JButton("Zoom -");
		zoomOut.setActionCommand("ZOOM-OUT");
		zoomOut.addActionListener(this);
		controls.add(zoomOut);

		recordButton = new JButton();
		updateRecordButton();
		recordButton.setActionCommand("RECORD");
		recordButton.addActionListener(this);
		controls.add(recordButton);

		return controls;
	}

	private void updateRecordButton() {
		if (recording) {
//			recordButton.setText("\u23f9 STOP");
			recordButton.setText("\u25fc STOP");
		} else {
//			recordButton.setText("\u23fa RECORD");
			recordButton.setText("\u26ab RECORD");
		}
	}

	public void actionPerformed(ActionEvent e) {
		if (e.getActionCommand() == "SET-URL") {
			mainApp.setCameraUrl(urlTextField.getText());
		}

		else if (e.getActionCommand() == "ZOOM-IN") {
			mainApp.zoomIn();
		}

		else if (e.getActionCommand() == "ZOOM-OUT") {
			mainApp.zoomOut();
		}

		else if (e.getActionCommand() == "RECORD") {
			if (!recording) {
				mainApp.startRecording();
				recording = true;
			} else {
				mainApp.stopRecording();
				recording = false;
			}
			updateRecordButton();
		}
	}
}
