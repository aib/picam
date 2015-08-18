package net.aib42.picam.ui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BoxLayout;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JList;
import javax.swing.JPanel;

import net.aib42.picam.MainApp;

@SuppressWarnings("serial")
public class RecordingPanel extends JPanel implements ActionListener {
	private MainApp mainApp;

	public RecordingPanel(MainApp mainApp) {
		this.mainApp = mainApp;

		BoxLayout layout = new BoxLayout(this, BoxLayout.PAGE_AXIS);
		this.setLayout(layout);

		DefaultListModel<String> recordings = new DefaultListModel<String>();
		JList<String> recordingsList = new JList<String>(recordings);
		this.add(recordingsList);

		JPanel buttons = new JPanel();
		buttons.setLayout(new BoxLayout(buttons, BoxLayout.LINE_AXIS));

		JButton refresh = new JButton("Refresh");
		refresh.setActionCommand("REFRESH");
		refresh.addActionListener(this);
		buttons.add(refresh);

		JButton play = new JButton("Play");
		refresh.setActionCommand("PLAY");
		refresh.addActionListener(this);
		buttons.add(play);

		this.add(buttons);

		//FIXME
		for (int i=0; i<10; ++i) {
			recordings.addElement(String.format("%04d", i));
		}
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getActionCommand() == "REFRESH") {
			//TODO
		}

		else if (e.getActionCommand() == "PLAY") {
			//TODO
		}
	}
}
