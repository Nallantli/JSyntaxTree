package window;

import javax.swing.*;
import javax.swing.filechooser.*;

import java.awt.*;
import java.awt.event.*;

import syntax.JSyntaxTree;

/**
 * Small GUI class so to offer a graphical way of selecting options
 */

public class GUI extends Frame {
	private TextField tfCount, ofn, on, fn, fs, sx, sy, lw;
	private Button btnReset, create;
	String file_path = null;
	String out_file = null;

	public GUI() {
		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				System.exit(0);
			}
		});
		setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));

		JPanel j1 = new JPanel();
		j1.setLayout(new GridLayout(9, 2));
		add(j1);

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
		sy = new TextField("200", 20);
		j1.add(sy);

		j1.add(new Label("Line Width"));
		lw = new TextField("3.0", 20);
		j1.add(lw);

		Checkbox inColor = new Checkbox("Colored");
		j1.add(inColor);
		Checkbox sScript = new Checkbox("Auto-Subscript");
		j1.add(sScript);

		create = new Button("BUILD");
		add(create);

		JPanel f = new JPanel();
		add(f);

		create.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent evt) {
				String[] args = { "-i", file_path, "-o", out_file + "/" + on.getText() + ".png", "-f", fn.getText(), "-fs",
						fs.getText(), "-sx", sx.getText(), "-sy", sy.getText(), "-l", lw.getText(),
						(inColor.getState() ? "-c" : ""), (sScript.getState() ? "-a" : "") };
				try {
					f.removeAll();
					JSyntaxTree.initialize(args, f);
				} catch (Exception e) {
					System.err.println(e);
				}
				pack();
			}
		});

		setTitle("Quick-and-dirty GUI");
		pack();
		setVisible(true);
		setResizable(false);
	}

	// The entry main method
	public static void main(String[] args) {
		new GUI(); // Let the constructor do the job
	}
}