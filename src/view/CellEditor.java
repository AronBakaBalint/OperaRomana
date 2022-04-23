package view;

import java.awt.Font;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JTextArea;

public class CellEditor extends JFrame {
	private static final long serialVersionUID = 4921753682582036207L;
	private JTextArea subText;
	private JButton ok = new JButton("OK");

	public CellEditor(String text) {
		setTitle("Cell Editor");
		setSize(435, 280);
		setLocation(400, 300);
		setLayout(null);
		this.subText = new JTextArea(text);
		this.subText.setBounds(10, 20, 400, 150);
		this.subText.setFont(new Font("Arial", 0, 14));
		add(this.subText);
		this.ok.setBounds(180, 190, 60, 30);
		add(this.ok);
	}

	public JButton getButton() {
		return this.ok;
	}

	public String getText() {
		return this.subText.getText();
	}
}
