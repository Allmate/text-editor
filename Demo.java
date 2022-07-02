package Java_Pack;

import javax.swing.JFrame;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.EventQueue;
import javax.swing.JTabbedPane;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JMenu;
import java.io.FileWriter;
import java.io.IOException;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.awt.FileDialog;
import javax.swing.JOptionPane;
import javax.swing.JButton;
import javax.swing.JTextField;
import javax.swing.JViewport;
import javax.swing.ScrollPaneConstants;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultHighlighter;
import javax.swing.text.Document;
import javax.swing.text.Highlighter;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import javax.swing.JLabel;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.Icon;
import java.awt.Image;
import javax.swing.JScrollPane;

public class Demo extends JFrame{

	private static final long serialVersionUID = 1L;
	
	MyHighLightPainter myHighLightPainter = new MyHighLightPainter(Color.YELLOW);
	
	private JMenuBar menuBar;
	private JMenu fileMenu;
	private JMenu editMenu;
	
	private JMenuItem newMenuItem;
	private JMenuItem openMenuItem;
	private JMenuItem saveMenuItem;
	private JMenuItem exitMenuItem;
	
	private JPanel panel;
	private FileDialog fileDialog;
	private JTabbedPane tabbedPane;
	private JButton searchbtn;
	private JTextField textfield;
	private JScrollPane scrollPane;
	private int counter;
	
	String filename = null;
	String filepath = null;
	String absolutefilename = null;
	
	public Demo() {
		initUI();
	}

private JPanel getTitlePanel(final String panelTitle, final JTabbedPane tabbedPane,final JPanel panel){
	JPanel titlePanel = new JPanel();
	titlePanel.setOpaque(false);
	titlePanel.setLayout(new GridBagLayout());
	
	JLabel label = new JLabel();
	label.setText(panelTitle);
	label.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 10));
	
	Icon exitIcon = new ImageIcon(new ImageIcon("src/Images/close.png").getImage().getScaledInstance(7,7,Image.SCALE_DEFAULT));
	JButton closeBtn = new JButton(exitIcon);
	closeBtn.setBackground(null);
	closeBtn.setBorder(null);
	closeBtn.setToolTipText("close the tab");
	
	GridBagConstraints gbc = new GridBagConstraints();
	gbc.gridx = 0;
	gbc.gridy = 0;
	gbc.weightx = 1;
	titlePanel.add(label);
	
	gbc.gridx++;
	gbc.weightx = 0;
	titlePanel.add(closeBtn);

	closeBtn.addActionListener(new java.awt.event.ActionListener(){
		@Override
		public void actionPerformed(java.awt.event.ActionEvent e){
			tabbedPane.remove(panel);
		}
});

	titlePanel.add(closeBtn);
	return titlePanel;
}
	
	private void initUI() {
		setTitle("NotePad");
		setSize(new Dimension(600,400));
		setLocationRelativeTo(null);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setResizable(false);
				
		tabbedPane = new JTabbedPane();
		add(tabbedPane);
		setJMenuBar(createJMenuBar());
		createSearchPanel();
		
		newMenuItem.addActionListener(event -> newMenuItemAction(event));
		saveMenuItem.addActionListener(event -> saveMenuItemAction(event));
		openMenuItem.addActionListener(event -> openMenuItemAction(event));
		exitMenuItem.addActionListener(event -> System.exit(0));		
		
		searchbtn.addActionListener(event -> searchAction(event));
	}
	
	private JMenuBar createJMenuBar() {

		menuBar = new JMenuBar();
		fileMenu = new JMenu("File");
		editMenu = new JMenu("Edit");
		
		newMenuItem = new JMenuItem("New");
		openMenuItem = new JMenuItem("Open");
		saveMenuItem = new JMenuItem("Save");
		exitMenuItem = new JMenuItem("Exit");
		exitMenuItem.setToolTipText("Exit the application");
		
		menuBar.add(fileMenu);
		menuBar.add(editMenu);
		
		fileMenu.add(newMenuItem);
		fileMenu.add(openMenuItem);
		fileMenu.add(saveMenuItem);
		fileMenu.addSeparator();
		fileMenu.add(exitMenuItem);
				
		return menuBar;
	}
	
	
private void newMenuItemAction(java.awt.event.ActionEvent evet) {

		panel = new JPanel();
		counter += 1;
		JTextArea textArea = createTextArea();
		scrollPane = 
				new JScrollPane(textArea, ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS, ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		scrollPane.setPreferredSize(new Dimension(580,380));
		panel.add(scrollPane);
		tabbedPane.add(panel);
		tabbedPane.setTabComponentAt(tabbedPane.indexOfComponent(panel), getTitlePanel("Tab" + Integer.toString(counter), tabbedPane, panel));
	}
	
	
	private void openMenuItemAction(java.awt.event.ActionEvent evet) {
		
		StringBuilder sb = null;
		
		fileDialog = new FileDialog(Demo.this, "Open File", FileDialog.LOAD);
		fileDialog.setVisible(true);
		
		if(fileDialog.getFile() != null) {
			filename = fileDialog.getFile();
			filepath = fileDialog.getDirectory();
		}
		try {
			BufferedReader bf = new BufferedReader(new FileReader(filepath + filename));
			sb = new StringBuilder();
			String line = null;
			
			while((line = bf.readLine()) != null) {
				sb.append(line + "\n");
			}	
			bf.close();

		}catch(IOException e) {
			System.err.println("Error!!");
		}
		
		panel = new JPanel();
		counter += 1;
		JTextArea textArea = createTextArea();
		textArea.setLineWrap(false);
		textArea.setWrapStyleWord(false);
		textArea.setText(sb.toString());
		scrollPane = 
				new JScrollPane(textArea, ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS, ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		scrollPane.setPreferredSize(new Dimension(580,380));
		panel.add(scrollPane);
		tabbedPane.add(panel);
		tabbedPane.setTabComponentAt(tabbedPane.indexOfComponent(panel), getTitlePanel(filename, tabbedPane, panel));
	}
	
	
	private void saveMenuItemAction(java.awt.event.ActionEvent evet){
		
		JPanel panel = (JPanel)tabbedPane.getSelectedComponent();
		JScrollPane scrollPane = (JScrollPane)(panel.getComponent(0));
		JViewport viewPort = (JViewport) scrollPane.getComponent(0);
		JTextArea textArea = (JTextArea) viewPort.getComponent(0);
		
		String data = textArea.getText();
		
		fileDialog = new FileDialog(Demo.this, "Save file", FileDialog.SAVE);
		fileDialog.setVisible(true);
			
		if(fileDialog.getFile() != null) {
			filename = fileDialog.getFile();
			filepath = fileDialog.getDirectory();
		}
			
		File file = new File(filepath + filename);
		try {
		file.createNewFile();
		}catch(IOException e) {
			System.err.println("Same File name exists.");
		}
		
		FileWriter writer;
		try {
			writer = new FileWriter(file.getAbsoluteFile());
			writer.write(data);
			writer.close();
			JOptionPane.showMessageDialog(Demo.this, "File saved successfully.", "File Save", JOptionPane.INFORMATION_MESSAGE);
			tabbedPane.setTabComponentAt(tabbedPane.indexOfComponent(panel), getTitlePanel(filename, tabbedPane, panel));
			
		} catch (IOException e) {
			System.err.println("Error occurrence.");
		}
	}
	
		
	private JTextArea createTextArea() {
		JTextArea textArea = new JTextArea();
		textArea.setLineWrap(true);
		textArea.setWrapStyleWord(true);
		return textArea;
	}
	
	
	private void createSearchPanel() {

		JPanel searchPanel = new JPanel();
		searchbtn = new JButton("Search");
		textfield = new JTextField(20);
		
		searchPanel.add(textfield);
		searchPanel.add(searchbtn);
		add(searchPanel, BorderLayout.SOUTH);
	}
	
	private void searchAction(java.awt.event.ActionEvent evet){
		String data = textfield.getText();
		JPanel panel = (JPanel)tabbedPane.getSelectedComponent();
		JViewport viewPort = (JViewport) scrollPane.getComponent(0);
		JTextArea textArea = (JTextArea) viewPort.getComponent(0);
		
		try {
			highlight(textArea,data);
		} catch (BadLocationException e) {
			e.printStackTrace();
		}
	}
	
	class MyHighLightPainter extends DefaultHighlighter.DefaultHighlightPainter{

		public MyHighLightPainter(Color color) {
			super(color);
		}
		
	}
	
	private void highlight(javax.swing.text.JTextComponent textComp, String pattern) throws BadLocationException {
		removeHighlights(textComp);
		
		Highlighter hilite = textComp.getHighlighter();
		Document doc = textComp.getDocument();
		String text = doc.getText(0, doc.getLength());
		int pos = 0;
		
		while((pos = text.indexOf(pattern, pos)) >= 0) {
			hilite.addHighlight(pos, pos + pattern.length(), myHighLightPainter);
			pos += pattern.length();
		}
	}
	
	private void removeHighlights(javax.swing.text.JTextComponent textComp) throws BadLocationException {
		Highlighter hilite = textComp.getHighlighter();
		Highlighter.Highlight [] hilites = hilite.getHighlights();
		
		for(int i = 0; i < hilites.length; i++) {
			if(hilites[i].getPainter() instanceof MyHighLightPainter) {
				hilite.removeHighlight(hilites[i]);
			}
		}
	}
	
	public static void main(String [] args) {
		EventQueue.invokeLater(() -> {
			new Demo().setVisible(true);
		});
	}
}