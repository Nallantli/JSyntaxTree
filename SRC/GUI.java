import javax.swing.*;
import javax.swing.filechooser.*;

import java.awt.*;
import java.awt.event.*;
 
public class GUI extends Frame {
   private TextField tfCount, ofn, fn, fs, sx, sy;
   private Button btnReset, create;
   String file_path = null;
 
   public GUI () {
		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {System.exit(0);}
		});
      setLayout(new GridLayout(7, 2));
      tfCount = new TextField("Select a file", 100);
      tfCount.setEditable(false);
	  add(tfCount);
      btnReset = new Button("Choose Source");
      add(btnReset);
      btnReset.addActionListener(new ActionListener() {
         @Override
         public void actionPerformed(ActionEvent evt) {
			JFileChooser chooser = new JFileChooser();
			FileNameExtensionFilter filter = new FileNameExtensionFilter(
					"TXT Files", "txt");
			chooser.setFileFilter(filter);
			int returnVal = chooser.showOpenDialog(null);
			if(returnVal == JFileChooser.APPROVE_OPTION) {
				tfCount.setText(chooser.getSelectedFile().getAbsolutePath());
				file_path = chooser.getSelectedFile().getAbsolutePath();
			}
         }
	  });	  
	  
	  add(new Label("Output File Name"));
      ofn = new TextField("OUTPUT.png", 50);
	  add(ofn);

	  add(new Label("Font Name"));
	  fn = new TextField("Doulos SIL", 50);
	  add(fn);

	  add(new Label("Font Size"));
	  fs = new TextField("48", 50);
	  add(fs);

	  add(new Label("Spacing X"));
	  sx = new TextField("50", 50);
	  add(sx);

	  add(new Label("Spacing Y"));
	  sy = new TextField("200", 50);
	  add(sy);

	  create = new Button("BUILD");
      add(create);
      create.addActionListener(new ActionListener() {
         @Override
         public void actionPerformed(ActionEvent evt) {
			 String[] args = { 
				 "-i", file_path, 
				 "-o", ofn.getText(), 
				 "-f", fn.getText(), 
				 "-fs", fs.getText(), 
				 "-sx", sx.getText(), 
				 "-sy", sy.getText()
			};
			JSyntaxTree.initialize(args);
         }
	  });	 
 
      setTitle("Quick-and-dirty GUI");
      setSize(400, 200);
      setVisible(true);
   }
 
   // The entry main method
   public static void main(String[] args) {
      new GUI();  // Let the constructor do the job
   }
}