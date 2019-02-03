package window;

import java.awt.Button;
import java.awt.Checkbox;
import java.awt.Color;
import java.awt.Font;
import java.awt.Frame;
import java.awt.GridLayout;
import java.awt.Label;
import java.awt.TextField;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.nio.charset.StandardCharsets;

import javax.swing.BoxLayout;
import javax.swing.JFileChooser;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.ScrollPaneConstants;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.undo.UndoManager;

import syntax.JSyntaxTree;

/**
 * Small GUI class so to offer a graphical way of selecting options
 */

public class GUI extends Frame implements ActionListener {
	private TextField tfCount, ofn, on, fn, fs, sx, sy, lw;
	private JTextArea main_box;
	private Button btnReset, create;
	String file_path = null;
	String out_file = "./";
	JMenuItem undo, redo;
	UndoManager undoManager;

	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == undo)
			undoManager.undo();
		if (e.getSource() == redo)
			undoManager.redo();
	}

	public GUI() {
		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				System.exit(0);
			}
		});

		setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
		JPanel full_win = new JPanel();
		full_win.setLayout(new GridLayout(1, 2));
		add(full_win);
		JPanel mainj = new JPanel();
		full_win.add(mainj);

		JPanel menuHolder = new JPanel();
		menuHolder.setLayout(new BoxLayout(menuHolder, BoxLayout.PAGE_AXIS));
		main_box = new JTextArea(0, 20);
		undoManager = new UndoManager();
		main_box.getDocument().addUndoableEditListener(undoManager);

		JScrollPane scroll = new JScrollPane(main_box);
		scroll.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);

		JMenuBar mb = new JMenuBar();
		undo = new JMenuItem("Undo");
		redo = new JMenuItem("Redo");
		undo.addActionListener(this);
		redo.addActionListener(this);
		mb.add(undo);
		mb.add(redo);
		mb.setMaximumSize(mb.getPreferredSize());

		main_box.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 12));
		main_box.setTabSize(4);

		menuHolder.add(mb);
		menuHolder.add(scroll);
		full_win.add(menuHolder);

		mainj.setLayout(new BoxLayout(mainj, BoxLayout.PAGE_AXIS));

		JPanel j1 = new JPanel();
		j1.setLayout(new GridLayout(9, 2));
		mainj.add(j1);

		tfCount = new TextField("Select a file", 20);
		tfCount.setEditable(false);
		j1.add(tfCount);
		btnReset = new Button("Choose Source");
		j1.add(btnReset);
		btnReset.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent evt) {
				JFileChooser chooser = new JFileChooser();
				FileNameExtensionFilter filter = new FileNameExtensionFilter("TXT Files", "txt");
				chooser.setFileFilter(filter);
				int returnVal = chooser.showOpenDialog(null);
				if (returnVal == JFileChooser.APPROVE_OPTION) {
					tfCount.setText(chooser.getSelectedFile().getAbsolutePath());
					file_path = chooser.getSelectedFile().getAbsolutePath();

					String total = "";

					try {
						InputStream bytes = new FileInputStream(file_path);
						Reader chars = new InputStreamReader(bytes, StandardCharsets.UTF_8);
						BufferedReader br = new BufferedReader(chars);
						String line;
						while ((line = br.readLine()) != null)
							total = total.concat(line) + "\n";
						br.close();
					} catch (IOException e) {
						System.err.println("Error openning file: " + file_path);
					}

					main_box.setText(total);
				}
			}
		});

		ofn = new TextField("Navigate to a folder", 20);
		ofn.setEditable(false);
		j1.add(ofn);
		btnReset = new Button("Choose Output Folder");
		j1.add(btnReset);
		btnReset.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent evt) {
				JFileChooser chooser = new JFileChooser();
				chooser.setCurrentDirectory(new java.io.File("."));
				chooser.setDialogTitle("Choose a folder...");
				chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
				chooser.setAcceptAllFileFilterUsed(false);
				if (chooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
					out_file = chooser.getSelectedFile().getAbsolutePath();
					ofn.setText(out_file);
				} else {
					System.out.println("No Selection ");
				}
			}
		});

		j1.add(new Label("Output Filename"));
		on = new TextField("OUTPUT", 20);
		j1.add(on);

		j1.add(new Label("Font Name"));
		fn = new TextField("Doulos SIL", 20);
		j1.add(fn);

		j1.add(new Label("Font Size"));
		fs = new TextField("48", 20);
		j1.add(fs);

		j1.add(new Label("Spacing X"));
		sx = new TextField("50", 20);
		j1.add(sx);

		j1.add(new Label("Spacing Y"));
		sy = new TextField("150", 20);
		j1.add(sy);

		j1.add(new Label("Line Width"));
		lw = new TextField("3.0", 20);
		j1.add(lw);

		Checkbox inColor = new Checkbox("Colored");
		j1.add(inColor);
		Checkbox sScript = new Checkbox("Auto-Subscript");
		j1.add(sScript);

		create = new Button("BUILD");
		mainj.add(create);

		JPanel f = new JPanel();
		add(f);

		create.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent evt) {
				try {
					BufferedWriter bw = new BufferedWriter(
							new OutputStreamWriter(new FileOutputStream("TEMP.txt"), StandardCharsets.UTF_8));
					bw.write(main_box.getText());
					bw.close();
				} catch (IOException e) {
					System.err.println(e);
				}
				String[] args = { "-i", "TEMP.txt", "-o", out_file + "/" + on.getText() + ".png", "-f", fn.getText(),
						"-fs", fs.getText(), "-sx", sx.getText(), "-sy", sy.getText(), "-l", lw.getText(),
						(inColor.getState() ? "-c" : ""), (sScript.getState() ? "-a" : "") };
				try {
					f.removeAll();
					JSyntaxTree.initialize(args, f);
				} catch (Exception e) {
					System.err.println(e);
				}
				f.repaint();
				f.setSize(f.getPreferredSize());
				f.setBackground(Color.white);
				pack();
			}
		});

		setTitle("Quick-and-dirty GUI");
		scroll.setPreferredSize(scroll.getSize());
		pack();
		setVisible(true);
		setResizable(false);
	}

	// The entry main method
	public static void main(String[] args) {
		new GUI(); // Let the constructor do the job
	}
}