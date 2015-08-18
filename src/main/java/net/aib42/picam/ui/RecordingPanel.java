package net.aib42.picam.ui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BoxLayout;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.ListSelectionModel;

import net.aib42.picam.MainApp;

@SuppressWarnings("serial")
public class RecordingPanel extends JPanel implements ActionListener {
	public class Recording {
		@Override
		public String toString() {
			return "FIXME"; //FIXME
		}
	}

	private MainApp mainApp;
	private DefaultListModel<Recording> recordings;
	private JList<Recording> recordingsList;

	public RecordingPanel(MainApp mainApp) {
		this.mainApp = mainApp;

		BoxLayout layout = new BoxLayout(this, BoxLayout.PAGE_AXIS);
		this.setLayout(layout);

		recordings = new DefaultListModel<Recording>();
		recordingsList = new JList<Recording>(recordings);
		recordingsList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		this.add(recordingsList);

		JPanel buttons = new JPanel();
		buttons.setLayout(new BoxLayout(buttons, BoxLayout.LINE_AXIS));

		JButton refresh = new JButton("Refresh");
		refresh.setActionCommand("REFRESH");
		refresh.addActionListener(this);
		buttons.add(refresh);

		JButton play = new JButton("Play");
		play.setActionCommand("PLAY");
		play.addActionListener(this);
		buttons.add(play);

		this.add(buttons);

		//FIXME
		for (int i=0; i<10; ++i) {
			recordings.addElement(new Recording());
		}
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getActionCommand() == "REFRESH") {
			//TODO
//			mainApp.getContentList();
		}

		else if (e.getActionCommand() == "PLAY") {
			int index = recordingsList.getSelectedIndex();
			if (index != -1) {
				Recording r = recordings.get(index);
				System.out.println(r); //TODO
			}
		}
	}
}
