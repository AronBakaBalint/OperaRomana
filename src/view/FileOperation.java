package view;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Frame;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.SpinnerNumberModel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.MouseInputAdapter;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumnModel;
import javax.swing.text.BadLocationException;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.Style;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;

import language.Language;
import model.FillTable;
import model.SubGroup;

public class FileOperation extends javax.swing.JFrame implements java.awt.event.ItemListener {
	private static final long serialVersionUID = 1L;
	private JTextField filename1 = new JTextField("\t                                                     ");
	private JTextField filename2 = new JTextField("\t                                                     ");
	private JTextField longestLineVal1 = new JTextField("        ");
	private JTextField longestLineVal2 = new JTextField("        ");

	private JLabel dist1 = new JLabel();
	private JLabel dist2 = new JLabel();
	private JLabel file1 = new JLabel();
	private JLabel file2 = new JLabel();
	private JLabel longestLine1 = new JLabel();
	private JLabel longestLine2 = new JLabel();
	private JLabel text1Color = new JLabel();
	private JLabel text2Color = new JLabel();
	private JLabel fontSize = new JLabel("font size: ");
	private JLabel fontType = new JLabel("font type: ");

	private JComboBox<String> fontList;

	private JComboBox<String> lang1ColorList;
	private JComboBox<String> lang2ColorList;
	private JComboBox<String> languageSelector;
	private JSpinner distance1 = new JSpinner();
	private JSpinner distance2 = new JSpinner();
	private JSpinner fSize = new JSpinner();

	private JButton minimize = new JButton("Minimize");
	private JButton exit = new JButton("Exit");
	private JButton ok = new JButton();
	private JButton save1 = new JButton();
	private JButton save2 = new JButton();
	private JButton browse1 = new JButton();
	private JButton browse2 = new JButton();
	private JButton search = new JButton();

	public JTable subtitleDisplay;
	private JScrollPane scrollPane;
	private JScrollPane pane;
	private JPanel p6 = new JPanel();
	private JTextPane ta = new JTextPane();

	private Color lang1Color;
	private Color lang2Color;
	private int currentDisplayedRow = 0;
	private int nextSelectedRow = 0;
	private int dim;
	private int k;
	private int jumper = 5;
	private int editorRow;
	private int editorColumn;
	private WordSearcher ws;
	private CellEditor ce;
	private boolean isVisibleFullscreen = false;
	private boolean scrollEnabled = false;
	private StyledDocument doc;
	public List<Integer> indexList = new ArrayList<>();

	public FileOperation(int elt1, int elt2, String defaultTextType, int defaultFontSize, String lang1Color,
			String lang2Color, String oldFile1, String oldFile2) {

		initializeButtons();
		initializeLabels();
		
		this.lang1Color = getColor(lang1Color);
		this.lang2Color = getColor(lang2Color);
		setDefaultCloseOperation(3);
		FlowLayout layout = new FlowLayout();
		layout.setVgap(0);
		setLayout(layout);

		if ((oldFile1 != null) && (oldFile2 != null)) {
			filename1.setText(oldFile1);
			filename2.setText(oldFile2);

			file1.setText(cut(oldFile1.substring(oldFile1.lastIndexOf("\\") + 1)));
			file2.setText(cut(oldFile2.substring(oldFile2.lastIndexOf("\\") + 1)));
		}

		String[] fonts = GraphicsEnvironment.getLocalGraphicsEnvironment().getAvailableFontFamilyNames();

		longestLineVal1.setEditable(false);
		longestLineVal2.setEditable(false);
		addClickListener(longestLineVal1);
		addClickListener(longestLineVal2);

		fontList = new JComboBox<String>(fonts);

		addP0(defaultTextType, defaultFontSize);

		String[] languages = Language.languages.keySet().toArray(new String[Language.languages.keySet().size()]);
		languageSelector = new JComboBox<String>(languages);
		addP1();

		SpinnerNumberModel m_numberSpinnerModel = new SpinnerNumberModel(elt1, 0, 100, 1);
		distance1 = new JSpinner(m_numberSpinnerModel);

		m_numberSpinnerModel = new SpinnerNumberModel(elt2, 0, 100, 1);
		distance2 = new JSpinner(m_numberSpinnerModel);
		fontList.setSelectedItem(defaultTextType);

		distance1.addChangeListener(getFontListener());
		distance2.addChangeListener(getFontListener());

		m_numberSpinnerModel = new SpinnerNumberModel(defaultFontSize, 0, 100, 1);
		fSize = new JSpinner(m_numberSpinnerModel);

		ChangeListener listenerFontSize = new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				try {
					ta.setFont(new Font(fontList.getSelectedItem().toString(), 0, Integer.parseInt(fSize.getValue().toString())));
					saveData();
				} catch (Exception ex) {
					// ignore
				}
			}
		};

		fSize.addChangeListener(listenerFontSize);

		fontList.addItemListener(this);
		fontList.setBackground(Color.WHITE);

		addP2();

		String[] colors = getColorList();
		colors = selectedColor(colors, lang1Color);
		lang1ColorList = new JComboBox<String>(colors);
		colors = selectedColor(colors, lang2Color);
		lang2ColorList = new JComboBox<String>(colors);
		lang1ColorList.addActionListener(new LanguageColor());
		lang2ColorList.addActionListener(new LanguageColor());

		addP3();

		String[] header = { Language.translate("nr."), Language.translate("language") + 1,
				Language.translate("language") + 2 };
		String[][] subtitle = new String[0][3];
		subtitleDisplay = new JTable(subtitle, header);
		subtitleDisplay.setPreferredScrollableViewportSize(new Dimension(780, 400));
		subtitleDisplay.setFillsViewportHeight(true);
		subtitleDisplay.getColumnModel().getColumn(0).setMaxWidth(40);
		scrollPane = new JScrollPane(subtitleDisplay);
		p6.add(scrollPane);
		add(p6);
		
		setExtendedState(JFrame.MAXIMIZED_BOTH); 
		setUndecorated(true);
		GraphicsDevice gd = getGraphicsConfiguration().getDevice();
		gd.setFullScreenWindow(this);
		validate();
		setAlwaysOnTop(true);
	}

	private String[] getColorList() {
		String[] colors = { "White", "Black", "Green", "Yellow", "Red", "Blue", "Cyan", "Dark Gray", "Gray",
				"Light Gray", "Magenta", "Orange", "Pink" };
		return colors;
	}

	void initializeButtons() {
		save1.setText(Language.translate("save") + 1);
		save2.setText(Language.translate("save") + 2);
		ok.setText(Language.translate("load"));
		browse1.setText(Language.translate("language") + 1);
		browse2.setText(Language.translate("language") + 2);
		search.setText(Language.translate("search"));
	}

	void initializeLabels() {
		dist1.setText(Language.translate("offset") + " " + Language.translate("language") + " 1");
		dist2.setText(Language.translate("offset") + " " + Language.translate("language") + " 2");

		if (Language.translate("noFileChosen").equals(file1.getText()) || file1.getText().isEmpty()) {
			file1.setText(Language.translate("noFileChosen"));
		}
		if (Language.translate("noFileChosen").equals(file2.getText()) || file2.getText().isEmpty()) {
			file2.setText(Language.translate("noFileChosen"));
		}
		longestLine1.setText(Language.translate("longest line language") + 1);
		longestLine2.setText(Language.translate("longest line language") + 2);
		text1Color.setText(Language.translate("color") + " " + Language.translate("language") + 1);
		text2Color.setText(Language.translate("color") + " " + Language.translate("language") + 2);
	}

	class OpenL1 implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			JFileChooser c = new JFileChooser();
			try {
				if (!file2.getText().trim().equals(Language.translate("noFileChosen"))) {
					File f = new File(new File(filename2.getText().substring(0, filename2.getText().lastIndexOf("\\")))
							.getCanonicalPath());
					c.setCurrentDirectory(f);
				} else {
					File f = new File(new File(".").getCanonicalPath());
					c.setCurrentDirectory(f);
				}
			} catch (IOException e1) {
				e1.printStackTrace();
			}

			int rVal = c.showOpenDialog(FileOperation.this);
			if (rVal == 0) {
				filename1.setText(c.getSelectedFile().getPath());
				file1.setText(FileOperation.this.cut(c.getSelectedFile().getName()));
			}
		}
	}

	class OpenL2 implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			JFileChooser c = new JFileChooser();
			try {
				if (!file1.getText().trim().equals(Language.translate("noFileChosen"))) {
					File f = new File(new File(filename1.getText().substring(0, filename1.getText().lastIndexOf("\\")))
							.getCanonicalPath());
					c.setCurrentDirectory(f);
				} else {
					File f = new File(new File(".").getCanonicalPath());
					c.setCurrentDirectory(f);
				}
			} catch (IOException e1) {
				e1.printStackTrace();
			}

			int rVal = c.showOpenDialog(FileOperation.this);
			if (rVal == 0) {
				filename2.setText(c.getSelectedFile().getPath());
				file2.setText(FileOperation.this.cut(c.getSelectedFile().getName()));
			}
		}
	}

	class Ready implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			if ((file1.getText().trim().equals(Language.translate("noFileChosen")))
					&& (file2.getText().trim().equals(Language.translate("noFileChosen")))) {
				JOptionPane.showMessageDialog(FileOperation.this, Language.translate("selectFile") + "!");
				return;
			}
			dim = FillTable.nrOfRows(filename1.getText());
			FillTable ft = new FillTable(dim);

			if (filename1.getText().trim().length() > 0) {
				ft.fillColumn1(filename1.getText());
			} else {
				ft.fillColumn1(filename2.getText());
			}
			if (filename2.getText().trim().length() > 0) {
				ft.fillColumn2(filename2.getText());
			} else {
				ft.fillColumn2(filename1.getText());
			}
			String[] header = { Language.translate("nr."), Language.translate("language") + 1,
					Language.translate("language") + 2 };
			SubGroup[][] subtitle = ft.getTable();
			subtitleDisplay = new JTable(subtitle, header);

			subtitleDisplay.setRowHeight(40);
			subtitleDisplay.setPreferredScrollableViewportSize(new Dimension(780, 400));
			subtitleDisplay.setFillsViewportHeight(true);
			subtitleDisplay.getColumnModel().getColumn(0).setMaxWidth(40);
			subtitleDisplay.getColumnModel().getColumn(1).setMaxWidth(470);
			subtitleDisplay.getColumnModel().getColumn(2).setMaxWidth(470);

			scrollPane = new JScrollPane(subtitleDisplay);
			scrollPane.getVerticalScrollBar().addMouseListener(new TestScrollPane());
			TableCellRenderer renderers = subtitleDisplay.getDefaultRenderer(String.class);
			((JLabel) renderers).setHorizontalAlignment(0);

			p6.removeAll();
			p6.add(pane);
			p6.add(scrollPane);
			p6.revalidate();
			p6.repaint();

			longestLineVal1.setText(ft.maxIndex1() + 1 + "");
			longestLineVal2.setText(ft.maxIndex2() + 1 + "");
			subtitleDisplay.addKeyListener(new Keychecker());
			subtitleDisplay.setDefaultEditor(Object.class, null);
			subtitleDisplay.addMouseListener(new MouseAdapter() {
				public void mouseClicked(MouseEvent evt) {
					if (evt.getClickCount() == 2) {
						nextSelectedRow = subtitleDisplay.rowAtPoint(evt.getPoint());
						scrollEnabled = true;
					}
					if (evt.getModifiers() == 4) {
						editorRow = subtitleDisplay.rowAtPoint(evt.getPoint());
						editorColumn = subtitleDisplay.columnAtPoint(evt.getPoint());
						String s = subtitleDisplay.getValueAt(editorRow, editorColumn).toString();
						s = s.replace("<html>", "").replace("<br>", "\n");

						GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
						GraphicsDevice[] gd = ge.getScreenDevices();
						for (int i = 0; i < gd.length; i++) {
							if (gd[i].equals(getGraphicsConfiguration().getDevice())) {
								ce = new CellEditor(s);
								ce.setLocation((int) gd[i].getDefaultConfiguration().getBounds().getCenterX() + 50, getY() + 300);
								ce.setVisible(true);
								ce.getButton().addActionListener(new Editor());
							}

						}
					}
				}
			});
			subtitleDisplay.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
				private static final long serialVersionUID = 1L;

				public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected,
						boolean hasFocus, int row, int column) {
					Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
					setBorder(noFocusBorder);
					if ((row == currentDisplayedRow) && (currentDisplayedRow != nextSelectedRow)) {
						c.setBackground(Color.green);
					} else if (row == nextSelectedRow) {
						c.setBackground(Color.yellow);
					} else
						c.setBackground(Color.white);
					subtitleDisplay.repaint();
					if (!scrollEnabled) {
						subtitleDisplay
								.scrollRectToVisible(subtitleDisplay.getCellRect(nextSelectedRow + jumper, 0, true));
					}
					return c;
				}
			});

			writeToLastmodified(filename1.getText() + "\t" + filename2.getText());
		}
	}

	class TestScrollPane extends MouseInputAdapter {
		public void mousePressed(MouseEvent arg0) {
			scrollEnabled = true;
		}
	}

	class LanguageColor implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			if (e.getSource().equals(lang1ColorList)) {
				String s = lang1ColorList.getSelectedItem().toString();
				lang1Color = FileOperation.this.getColor(s);
			} else {
				String s = lang2ColorList.getSelectedItem().toString();
				lang2Color = FileOperation.this.getColor(s);
			}
			if(isVisibleFullscreen) {
				updateText();
			}
			saveData();
		}
	}

	class LanguageSelector implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			boolean noFile1 = false;
			if (Language.translate("noFileChosen").equals(file1.getText())) {
				noFile1 = true;
			}
			boolean noFile2 = false;
			if (Language.translate("noFileChosen").equals(file1.getText())) {
				noFile2 = true;
			}
			Language.currentLanguage = languageSelector.getSelectedItem().toString();
			initializeButtons();
			initializeLabels();
			if (noFile1) {
				file1.setText(Language.translate("noFileChosen"));
			}
			if (noFile2) {
				file2.setText(Language.translate("noFileChosen"));
			}
			String[] newHeader = { Language.translate("nr."), Language.translate("language") + 1,
					Language.translate("language") + 2 };
			try {
				JTableHeader th = subtitleDisplay.getTableHeader();
				TableColumnModel tcm = th.getColumnModel();
				tcm.getColumn(0).setHeaderValue(newHeader[0]);
				tcm.getColumn(1).setHeaderValue(newHeader[1]);
				tcm.getColumn(2).setHeaderValue(newHeader[2]);
				th.repaint();
			} catch (Exception ex) {
				// escape
			}

			try {
				saveData();
			} catch (Exception ex) {
				// ignore
			}
		}
	}

	class DataSaver1 implements ActionListener {
		public void actionPerformed(ActionEvent arg0) {
			JFileChooser c = new JFileChooser();
			try {
				File f = new File(new File(".").getCanonicalPath());
				c.setCurrentDirectory(f);
				String saver = "";
				int rVal = c.showOpenDialog(FileOperation.this);
				if (rVal == 0) {
					saver = c.getSelectedFile().getPath();
					BufferedWriter writer = new BufferedWriter(
							new OutputStreamWriter(new FileOutputStream(saver), "UTF8"));
					for (int i = 0; i < subtitleDisplay.getRowCount(); i++) {
						String s1 = subtitleDisplay.getValueAt(i, 1).toString();
						String[] lines = s1.split("<br>");
						writer.append(toTxtForm(lines[0]));
						writer.newLine();
						if (lines.length > 1) {
							writer.append(toTxtForm(lines[1]));
							writer.newLine();
						}
						writer.newLine();
					}
					writer.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	class DataSaver2 implements ActionListener {
		public void actionPerformed(ActionEvent arg0) {
			JFileChooser c = new JFileChooser();
			try {
				File f = new File(new File(".").getCanonicalPath());
				c.setCurrentDirectory(f);
				String saver = "";
				int rVal = c.showOpenDialog(FileOperation.this);
				if (rVal == 0) {
					saver = c.getSelectedFile().getPath();
					BufferedWriter writer = new BufferedWriter(
							new OutputStreamWriter(new FileOutputStream(saver), "UTF8"));
					for (int i = 0; i < subtitleDisplay.getRowCount(); i++) {
						String s1 = subtitleDisplay.getValueAt(i, 2).toString();
						String[] lines = s1.split("<br>");
						writer.append(toTxtForm(lines[0]));
						writer.newLine();
						if (lines.length > 1) {
							writer.append(toTxtForm(lines[1]));
							writer.newLine();
						}
						writer.newLine();
					}
					writer.close();
				}
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}
	}

	class Searcher implements ActionListener {
		public void actionPerformed(ActionEvent arg0) {
			ws = new WordSearcher();
			GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
			GraphicsDevice[] gd = ge.getScreenDevices();
			for (int i = 0; i < gd.length; i++) {
				if (gd[i].equals(getGraphicsConfiguration().getDevice())) {
					ws.setLocation((int) gd[i].getDefaultConfiguration().getBounds().getCenterX(), getY());
					ws.setVisible(true);
					ws.getButton().addActionListener(new Confirm());
				}
			}
			k = -1;
			ws.getButton().addActionListener(new Confirm());
			ws.getNext().addActionListener(new FindWord());
		}
	}

	class Confirm implements ActionListener {
		public void actionPerformed(ActionEvent arg0) {
			scrollEnabled = false;
			indexList = new ArrayList<>();
			k = -1;
			for (int i = 0; i < subtitleDisplay.getRowCount(); i++) {
				if (subtitleDisplay.getValueAt(i, 1).toString().contains(ws.getText().trim())) {
					indexList.add(Integer.valueOf(i));
				}
				if (subtitleDisplay.getValueAt(i, 2).toString().contains(ws.getText().trim())) {
					indexList.add(Integer.valueOf(i));
				}
			}
			if (indexList.size() > 0)
				nextSelectedRow = 0;
		}
	}

	class FindWord implements ActionListener {
		public void actionPerformed(ActionEvent arg0) {
			try {
				nextSelectedRow = ((Integer) indexList.get(++k)).intValue();
			} catch (Exception e) {
				nextSelectedRow = 0;
				JOptionPane.showMessageDialog(FileOperation.this, Language.translate("noMoreResults") + "!");
			}
		}
	}

	class Editor implements ActionListener {
		public void actionPerformed(ActionEvent arg0) {
			String s = ce.getText();
			String[] splitted = s.split("\n");
			SubGroup sg = new SubGroup(splitted[0]);
			if (splitted.length > 1)
				sg.add(splitted[1]);
			ce.setVisible(false);
			subtitleDisplay.setValueAt(sg, editorRow, editorColumn);
		}
	}

	class Keychecker extends java.awt.event.KeyAdapter {
		public void keyPressed(KeyEvent e) {
			int keyCode = e.getKeyCode();
			switch (keyCode) {
			case 32:
				scrollEnabled = false;
				if (isVisibleFullscreen) {
					ta.setText("");
					isVisibleFullscreen = false;
					currentDisplayedRow = nextSelectedRow;
				} else {
					currentDisplayedRow = nextSelectedRow;
					nextSelectedRow += 1;
					isVisibleFullscreen = true;
					updateText();
				}
				break;
			case 40:
				if (nextSelectedRow < dim - 1)
					nextSelectedRow += 1;
				if (!isVisibleFullscreen)
					currentDisplayedRow = nextSelectedRow;
				jumper = 5;
				break;
			case 38:
				if (nextSelectedRow > 0)
					nextSelectedRow -= 1;
				if (!isVisibleFullscreen)
					currentDisplayedRow = nextSelectedRow;
				jumper = 5;
			}
		}
	}

	private void write(int offset, String s, Color color) {
		try {
			Style style = ta.addStyle(s, null);
			StyleConstants.setForeground(style, color);
			doc.insertString(offset, s, style);
		} catch (BadLocationException e) {
			e.printStackTrace();
		}
	}

	private void writeToTa(String sg1, String sg2) {
		doc = ta.getStyledDocument();
		SimpleAttributeSet center = new SimpleAttributeSet();
		StyleConstants.setAlignment(center, 1);

		ta.setText("");
		if (!lang1Color.equals(Color.black)) {
			for(int i = 1; i <= Integer.parseInt(distance1.getValue().toString()); i++) {
				write(0, "\n", lang1Color);
			}
			write(doc.getLength(), sg1, lang1Color);
		}

		if (!sg1.endsWith("\n")) {
			write(doc.getLength(), "\n", lang1Color);
		}
		
		if (!lang2Color.equals(Color.black)) {
			for(int i = 1; i <= Integer.parseInt(distance2.getValue().toString()); i++) {
				write(doc.getLength(), "\n", lang2Color);
			}
			write(doc.getLength(), sg2, lang2Color);
		}
	}

	private void addClickListener(JTextField longestLineVal) {
		longestLineVal.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				scrollEnabled = false;
				if (Integer.parseInt(longestLineVal.getText()) < currentDisplayedRow) {
					jumper = -4;
				} else {
					jumper = 5;
				}
				nextSelectedRow = (Integer.parseInt(longestLineVal.getText()) - 1);
				currentDisplayedRow = nextSelectedRow;
			}
		});
	}

	private String cut(String s) {
		return s.length() < 20 ? s : s.substring(0, 7) + "..." + s.substring(s.length() - 13, s.length());
	}

	private void addP0(String fontType, int fontSize) {
		JPanel p0 = new JPanel();
		p0.setBackground(Color.BLACK);
		ta.setFont(new Font(fontType, 0, fontSize));
		ta.setEditable(false);
		ta.setBackground(Color.BLACK);
		ta.setForeground(Color.WHITE);
		ta.setPreferredSize(new Dimension(5000, 460));

		doc = ta.getStyledDocument();
		SimpleAttributeSet center = new SimpleAttributeSet();
		StyleConstants.setAlignment(center, 1);
		doc.setParagraphAttributes(0, doc.getLength(), center, false);

		pane = new JScrollPane(ta, 21, 31);
		p0.add(ta);
		add(p0);
	}

	private void addP1() {
		JPanel p1 = new JPanel();
		p1.add(browse1);
		p1.add(file1);
		p1.add(browse2);
		p1.add(file2);
		p1.add(ok);
//		p1.add(search);
		p1.add(languageSelector);
		p1.add(minimize);
		p1.add(exit);
		p1.setPreferredSize(new Dimension(10000, 40));
		search.addActionListener(new Searcher());
		save1.addActionListener(new DataSaver1());
		save2.addActionListener(new DataSaver2());
		browse1.addActionListener(new OpenL1());
		browse2.addActionListener(new OpenL2());
		exit.setBackground(Color.RED);
		exit.setForeground(Color.WHITE);
		exit.addActionListener(new Quit());
		minimize.addActionListener(new Minimizer());
		languageSelector.addActionListener(new LanguageSelector());
		languageSelector.setSelectedItem(Language.currentLanguage);
		add(p1);
	}

	private void addP2() {
		JPanel p2 = new JPanel();
		p2.add(fontType);
		p2.add(fontList);
		p2.add(fontSize);
		p2.add(fSize);
		p2.add(longestLine1);
		p2.add(longestLineVal1);
		p2.add(longestLine2);
		p2.add(longestLineVal2);
		p2.setPreferredSize(new Dimension(10000, 40));
		ok.addActionListener(new Ready());
		add(p2);
	}

	private void addP3() {
		JPanel p3 = new JPanel();
		p3.add(dist1);
		p3.add(distance1);
		p3.add(dist2);
		p3.add(distance2);
		p3.add(text1Color);
		p3.add(lang1ColorList);
		p3.add(text2Color);
		p3.add(lang2ColorList);
		p3.add(save1);
		p3.add(save2);
		p3.setPreferredSize(new Dimension(10000, 40));
		add(p3);
	}

	public void itemStateChanged(ItemEvent e) {
		String s = (String) e.getItem();
		ta.setFont(new Font(s, 0, Integer.parseInt(fSize.getValue().toString())));
		saveData();
	}

	public static void writeToFile(String s) {
		try (BufferedWriter writer = new BufferedWriter(new FileWriter("input.txt"))) {
			writer.write(s);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void writeToLastmodified(String s) {
		try (BufferedWriter writer = new BufferedWriter(new FileWriter("last.txt"))) {
			writer.write(s);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	class Quit implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			System.exit(0);
		}
		
	}
	
	class Minimizer implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			setState(Frame.ICONIFIED);
		}
		
	}

	private ChangeListener getFontListener() {
		ChangeListener listener1 = new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				if (isVisibleFullscreen) {
					updateText();
				}
				saveData();
			}
		};
		return listener1;
	}

	private Color getColor(String s) {
		Map<String, Color> colorList = new HashMap<>();
		colorList.put("white", Color.white);
		colorList.put("black", Color.black);
		colorList.put("yellow", Color.yellow);
		colorList.put("orange", Color.orange);
		colorList.put("blue", Color.blue);
		colorList.put("red", Color.red);
		colorList.put("pink", Color.pink);
		colorList.put("cyan", Color.cyan);
		colorList.put("gray", Color.gray);
		colorList.put("magenta", Color.magenta);
		colorList.put("light gray", Color.lightGray);
		colorList.put("dark gray", Color.darkGray);
		colorList.put("green", Color .green);
		return (Color) colorList.get(s.toLowerCase());
	}

	public String toTxtForm(String s) {
		return s.replace("<html>", "").replace("<br>", "").replace("\n", "");
	}

	private void saveData() {
		writeToFile((distance1.getValue().toString() + "\t" + distance2.getValue().toString() + "\t"
				+ fontList.getSelectedItem().toString() + "\t" + fSize.getValue().toString() + "\t"
				+ lang1ColorList.getSelectedItem().toString() + "\t" + lang2ColorList.getSelectedItem().toString()
				+ "\t" + languageSelector.getSelectedItem().toString()).replace("Română", "RO"));
	}

	private String[] selectedColor(String[] colors, String setColor) {
		for (int i = 0; i < colors.length; i++) {
			if (colors[i].equals(setColor)) {
				String auxColor = colors[0];
				colors[0] = colors[i];
				colors[i] = auxColor;
			}
		}
		return colors;
	}
	
	private void updateText() {
		String s1 = subtitleDisplay.getValueAt(currentDisplayedRow, 1).toString();
		String s2 = subtitleDisplay.getValueAt(currentDisplayedRow, 2).toString();
		s1 = SubGroup.transformToPrintableForm(s1);
		s2 = SubGroup.transformToPrintableForm(s2);
		writeToTa(s1, s2);
	}
}
