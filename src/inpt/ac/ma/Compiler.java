package inpt.ac.ma;

import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.SystemColor;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.imageio.ImageIO;
import javax.swing.GroupLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JToolBar;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.WindowConstants;
import javax.swing.filechooser.FileNameExtensionFilter;

@SuppressWarnings("serial")
public class Compiler extends JFrame {

	private JFileChooser openFileChooser;
	private JFileChooser dialogSave;
	private JScrollPane scrollPane;
	private JToolBar toolBar;
	private JButton openFileButton;
	private JButton saveButton;
	private JPanel panel;
	private JLabel label1;

	File currentFile = null;
	int fontSize = 12;
	private JButton runProgramButton;
	private JLabel lblNewLabel;
	private JTextArea editingText;
	private JScrollPane scrollPane_1;
	private JTextArea output;

	// constructor
	public Compiler() {

		initiateElements();

		this.setLocationRelativeTo(null);
		this.addWindowListener(new WindowAdapter() {

			public void WindowClosing(WindowEvent event) {
				super.windowClosing(event);
				int option = JOptionPane.showConfirmDialog(rootPane, "Do you want to save the changes ?", "Confirm",
						JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
				if (option == JOptionPane.YES_OPTION) {
					saveChanges();
				}
			}
		});

		try {
			BufferedImage imageURL = ImageIO.read(Compiler.class.getResource("amarilla.png"));
			this.setIconImage(imageURL);
		} catch (IOException e) {
			String message = Compiler.class.getName();
			Logger.getLogger(message).log(Level.SEVERE, null, e);
		}

	}

	public Compiler(File file) {

		initiateElements();
		this.setLocationRelativeTo(null);

		currentFile = file;
		openFileChooser.setFileFilter(new FileNameExtensionFilter("mrl file", "mrl", "mrl"));
		readFile(file);

	}

	@SuppressWarnings("resource")
	private void readFile(File file) {
		try {
			Scanner scanner = new Scanner(file);
			String text = "";
			while (scanner.hasNext()) {
				text += scanner.nextLine() + "\n";
			}
			editingText.setText(text);
		} catch (FileNotFoundException e) {
			Logger.getLogger(Compiler.class.getName()).log(Level.SEVERE, null, e);
		}

	}

	private void initiateElements() {

		openFileChooser = new JFileChooser();
		dialogSave = new JFileChooser();
		panel = new JPanel();
		new JScrollPane();

		dialogSave.setDialogType(JFileChooser.SAVE_DIALOG);
		dialogSave.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);

		setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		setTitle("Amarilla Syntax Compiler");
		GroupLayout layout = new GroupLayout(getContentPane());
		getContentPane().setLayout(layout);
		layout.setHorizontalGroup(
				layout.createParallelGroup(GroupLayout.Alignment.LEADING)
						.addComponent(panel, GroupLayout.Alignment.TRAILING,
								GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE,
								Short.MAX_VALUE));
		layout.setVerticalGroup(
				layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
						.addComponent(panel, GroupLayout.DEFAULT_SIZE,
								GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE));
		GridBagLayout gbl_pane = new GridBagLayout();
		gbl_pane.columnWidths = new int[] { 662, 0 };
		gbl_pane.rowHeights = new int[] { 91, 281, 266, 14, 0 };
		gbl_pane.columnWeights = new double[] { 1.0, Double.MIN_VALUE };
		gbl_pane.rowWeights = new double[] { 0.0, 0.0, 1.0, 0.0, Double.MIN_VALUE };
		panel.setLayout(gbl_pane);
		toolBar = new JToolBar();
		openFileButton = new JButton();
		saveButton = new JButton();

		toolBar.setBackground(SystemColor.control);
		toolBar.setRollover(true);

		openFileButton
				.setIcon(new ImageIcon(getClass().getResource("openNewFile.png")));
		openFileButton.setText("Open New File");
		openFileButton.setFocusable(false);
		openFileButton.setHorizontalTextPosition(SwingConstants.CENTER);
		openFileButton.setVerticalTextPosition(SwingConstants.BOTTOM);
		openFileButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				openFileButtonActionPerformed(e);
			}
		});

		lblNewLabel = new JLabel("Amarilla's Compiler");
		lblNewLabel.setIcon(
				new ImageIcon(Compiler.class.getResource("/inpt/ac/ma/resized.png")));
		toolBar.add(lblNewLabel);
		toolBar.add(openFileButton);

		saveButton.setIcon(new ImageIcon(getClass().getResource("saveFile.png")));
		saveButton.setText("Save File");
		saveButton.setFocusable(false);
		saveButton.setHorizontalTextPosition(SwingConstants.CENTER);
		saveButton.setVerticalTextPosition(SwingConstants.BOTTOM);
		saveButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				saveButtonActionPerformed(e);
			}
		});
		toolBar.add(saveButton);

		runProgramButton = new JButton("Compile Program");
		runProgramButton.setToolTipText("");
		runProgramButton.setVerticalTextPosition(SwingConstants.BOTTOM);
		runProgramButton.setHorizontalTextPosition(SwingConstants.CENTER);
		runProgramButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (currentFile != null) {
					String fileName = currentFile.getName();
					if (fileName.endsWith("mrl")) {
						System.out.println("File succesfully loaded!");
						try {
							BufferedReader in;
							in = new BufferedReader(new FileReader(currentFile));
							ArrayList<String> erreurs = new ArrayList<>();
							int numeroDeLigne = 1;
							/// hnaya fin wslt

							for (String ligne; (ligne = in.readLine()) != null;) {

								// s'il s'agit d'un commentaire on va negliger la ligne
								if (ligne.startsWith("//")) {
									numeroDeLigne++;
									continue;
								}

								// detection des erreurs syntaxiques
								Boolean resultat = Parentheses.checkValidity(ligne);

								// verifier que le programme commence par le mot cle
								if (!checkStartingKeyWord(numeroDeLigne, ligne)) {
									String erreurDebutProg = "Couldn't find the starting keyword at the first Line";
									erreurs.add(erreurDebutProg);
									break;
								}

								if (!resultat) {
									String erreur = "Syntax error a parenthesis expected at line number : "
											+ numeroDeLigne;
									erreurs.add(erreur);
								}

								if (!ligne.endsWith(";")) {
									String erreurPointVirgule = "syntax error \';\' expected to complete the instruction at line : "
											+ numeroDeLigne;
									erreurs.add(erreurPointVirgule);
								}

								if (!checkStartsWithKeyword(ligne)) {
									String erreurMotCle = "Syntax error the instruction should start with a keyword at line :"
											+ numeroDeLigne;
									erreurs.add(erreurMotCle);
								}

								if (checkStartsWithLibrary(ligne)) {
									if (!functionExistsInLibrary(ligne)) {
										String libraryerror = "Syntax error the function declared does not exist in this library at line :"
												+ numeroDeLigne;
										erreurs.add(libraryerror);
									}
								}

								numeroDeLigne++;

							}

							// affichage des erreurs
							if (erreurs != null) {
								output.setText("OUTPUT: \n");
								for (int j = 0; j < erreurs.size(); j++) {
									output.setText(output.getText() + "\n" + erreurs.get(j));
								}
								output.setText(output.getText() + "\n" + "Compilation done successfully");
							}
							in.close();

						} catch (FileNotFoundException e1) {
							e1.printStackTrace();
						} catch (IOException e2) {
							e2.printStackTrace();
						}
					} else {
						JOptionPane.showMessageDialog(openFileButton, "Please enter a file with the extension mrl");
					}
				} else {
					System.out.println("No file chosen");
				}
			}

			private boolean functionExistsInLibrary(String ligne) {
				String[] ligneSplitted = ligne.split("\\.", 5);
				String nomDeLaBibliotheque = ligneSplitted[0];
				String nouvelleLigne = ligneSplitted[1];
				if (nomDeLaBibliotheque.equals("Math")) {
					String[] fonctionsMath = { "arcoshp", "arccos", "arcsin", "arcsinhp", "arctan", "arctanhp", "cos",
							"coshp", "divEnt", "expo", "facto", "log", "log10", "puissance", "racineC2", "racineC3",
							"reste", "signCop", "sin", "sinhp", "tan", "tanhp", "valAbs", "valEnt" };
					for (int i = 0; i < fonctionsMath.length; i++) {
						if (nouvelleLigne.startsWith(fonctionsMath[i])) {
							return true;
						}
					}
					return false;
				}

				else if (nomDeLaBibliotheque.equals("IO")) {
					String[] fonctionsIO = { "closef", "open", "print", "readf", "return", "savef", "scanner",
							"writef" };
					for (int i = 0; i < fonctionsIO.length; i++) {
						if (nouvelleLigne.startsWith(fonctionsIO[i])) {
							return true;
						}
					}
					return false;
				}

				else if (nomDeLaBibliotheque.equals("IO")) {
					String[] fonctionsStrings = { "concatenate", "digitToASCII", "equal", "find", "indexOf",
							"isAlphabetic", "isDigit", "isEmpty", "isLower", "isUpper", "letterToASCII", "lowerCase",
							"occurence", "replace", "size", "startsWith", "upperCase" };
					for (int i = 0; i < fonctionsStrings.length; i++) {
						if (nouvelleLigne.startsWith(fonctionsStrings[i])) {
							return true;
						}
					}
					return false;
				}

				else if (nomDeLaBibliotheque.equals("IO")) {
					String[] fonctionsArrays = { "add", "clear", "contains", "delete", "deleteFirst", "deleteLast",
							"get", "indexOf", "isEmpty", "size" };
					for (int i = 0; i < fonctionsArrays.length; i++) {
						if (nouvelleLigne.startsWith(fonctionsArrays[i])) {
							return true;
						}
					}
					return false;
				}

				return false;
			}

			private boolean checkStartsWithLibrary(String ligne) {
				String[] bibliotheques = { "Math", "IO", "Strings", "Arrays" };
				for (int i = 0; i < bibliotheques.length; i++) {
					if (ligne.startsWith(bibliotheques[i])) {
						return true;
					}
				}
				return false;
			}

			private boolean checkStartsWithKeyword(String ligne) {
				String[] motsClesDuLangage = { "byte", "const", "double", "false", "long", "negat", "null", "posit",
						"short",
						"true", "bool", "char", "float", "int", "string", "void", "if", "else", "elif", "for", "while",
						"case", "default", "end", "end", "exit", "fun", "print", "return", "start", "test", "tab" };
				for (int i = 0; i < motsClesDuLangage.length; i++) {
					if (ligne.startsWith(motsClesDuLangage[i])) {
						return true;
					}
				}
				return false;
			}

			private boolean checkStartingKeyWord(int numeroDeLigne, String ligne) {
				if (numeroDeLigne == 1) {
					if (ligne.contains("start")) {
						return true;
					} else {
						return false;
					}
				}
				return true;
			}
		});
		runProgramButton
				.setIcon(new ImageIcon(Compiler.class.getResource("/inpt/ac/ma/RunProgram.png")));
		toolBar.add(runProgramButton);
		GridBagConstraints gbc_toolBar = new GridBagConstraints();
		gbc_toolBar.anchor = GridBagConstraints.NORTH;
		gbc_toolBar.fill = GridBagConstraints.HORIZONTAL;
		gbc_toolBar.insets = new Insets(0, 0, 5, 0);
		gbc_toolBar.gridx = 0;
		gbc_toolBar.gridy = 0;
		panel.add(toolBar, gbc_toolBar);
		scrollPane = new JScrollPane();

		editingText = new JTextArea();
		editingText.setFont(new Font("Times New Roman", Font.PLAIN, 14));
		editingText.setLineWrap(true);
		editingText.setBackground(new Color(245, 245, 220));
		editingText.setWrapStyleWord(true);
		editingText.setColumns(8);
		editingText.setRows(7);
		scrollPane.setViewportView(editingText);
		GridBagConstraints gbc_scrollPane = new GridBagConstraints();
		gbc_scrollPane.fill = GridBagConstraints.BOTH;
		gbc_scrollPane.insets = new Insets(0, 0, 5, 0);
		gbc_scrollPane.gridx = 0;
		gbc_scrollPane.gridy = 1;
		panel.add(scrollPane, gbc_scrollPane);

		scrollPane_1 = new JScrollPane();
		GridBagConstraints gbc_scrollPane_1 = new GridBagConstraints();
		gbc_scrollPane_1.insets = new Insets(0, 0, 5, 0);
		gbc_scrollPane_1.fill = GridBagConstraints.BOTH;
		gbc_scrollPane_1.gridx = 0;
		gbc_scrollPane_1.gridy = 2;
		panel.add(scrollPane_1, gbc_scrollPane_1);

		output = new JTextArea();
		output.setEditable(false);
		output.setFont(new Font("Monospaced", Font.BOLD, 15));
		output.setLineWrap(true);
		output.setColumns(20);
		output.setRows(8);
		scrollPane_1.setViewportView(output);
		label1 = new JLabel();

		label1.setHorizontalAlignment(SwingConstants.CENTER);
		label1.setText("Amarilla's Compiler");
		GridBagConstraints gbc_label1 = new GridBagConstraints();
		gbc_label1.anchor = GridBagConstraints.NORTH;
		gbc_label1.fill = GridBagConstraints.HORIZONTAL;
		gbc_label1.gridx = 0;
		gbc_label1.gridy = 3;
		panel.add(label1, gbc_label1);

		pack();

	}

	private void openFileButtonActionPerformed(ActionEvent e) {// GEN-FIRST:event_openFileButtonActionPerformed
		// Show File Open dialouge here
		int returnedValue = openFileChooser.showOpenDialog(rootPane);
		if (returnedValue == JFileChooser.APPROVE_OPTION) {
			if (currentFile != null) {
				// A file is opened and is being edited. Open the new file in new window
				Compiler newWindow = new Compiler(openFileChooser.getSelectedFile());
				newWindow.setVisible(true);
				newWindow.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
				newWindow.pack();
				return;
			}
			currentFile = openFileChooser.getSelectedFile();
			System.out.println("File chosen. File name = " + openFileChooser.getSelectedFile().getName());

			try {
				// Now read the contents of file
				Scanner scanner = new Scanner(new FileInputStream(currentFile));
				String readText = "";
				while (scanner.hasNext()) {
					readText += scanner.nextLine() + "\n";
				}
				editingText.setText(readText);
			} catch (FileNotFoundException ex) {
				Logger.getLogger(Compiler.class.getName()).log(Level.SEVERE, null, ex);
			}

		} else {
			System.out.println("No file selected");
		}
	}

	private void saveButtonActionPerformed(ActionEvent evt) {// GEN-FIRST:event_saveButtonActionPerformed
		// If we are editing a file opened, then we have to save the contents on the
		// same file, currentFile
		if (currentFile != null) {
			try {
				PrintWriter printWriter = new PrintWriter(currentFile);
				printWriter.write(editingText.getText());
				printWriter.close();
				JOptionPane.showMessageDialog(rootPane, "Changes saved to " + currentFile.getName(),
						"SUCCESSFULLY SAVED",
						JOptionPane.INFORMATION_MESSAGE);
			} catch (FileNotFoundException e) {
				Logger.getLogger(Compiler.class.getName()).log(Level.SEVERE, null, e);
			}
		} else {
			int returnedValue = dialogSave.showOpenDialog(rootPane);
			if (returnedValue == JFileChooser.APPROVE_OPTION) {
				// We got directory. Now needs file name
				String fileName = JOptionPane.showInputDialog("File Name", "Untitled.mrl");
				if (!fileName.contains(".mrl")) {
					fileName += ".mrl";
				}
				File file = new File(dialogSave.getSelectedFile() + "\\" + fileName);
				if (file.exists()) {
					JOptionPane.showMessageDialog(rootPane, "File Already Exist.", "Error", JOptionPane.ERROR_MESSAGE);
					return;
				} else {
					try {
						file.createNewFile();
						PrintWriter printWriter = new PrintWriter(file);
						printWriter.write(editingText.getText());
						printWriter.close();
						JOptionPane.showMessageDialog(rootPane, "Saved", "Done", JOptionPane.INFORMATION_MESSAGE);
					} catch (IOException e) {
						Logger.getLogger(Compiler.class.getName()).log(Level.SEVERE, null, e);
					}
				}
			} else {
				JOptionPane.showMessageDialog(rootPane, "Error Occured", "Cant Save", JOptionPane.ERROR_MESSAGE);
			}

		}

	}

	private void saveChanges() {
		try {
			PrintWriter printWriter = new PrintWriter(currentFile);
			printWriter.write(editingText.getText());
			printWriter.close();
//            JOptionPane.showMessageDialog(rootPane, "Saved", "Done", JOptionPane.INFORMATION_MESSAGE);
		} catch (FileNotFoundException e) {
			Logger.getLogger(Compiler.class.getName()).log(Level.SEVERE, null, e);
		}
	}

	public static void main(String args[]) {

		try {
			for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
				if ("Nimbus".equals(info.getName())) {
					UIManager.setLookAndFeel(info.getClassName());
					break;
				}
			}
		} catch (ClassNotFoundException e) {
			Logger.getLogger(Compiler.class.getName()).log(Level.SEVERE, null, e);
		} catch (InstantiationException e) {
			Logger.getLogger(Compiler.class.getName()).log(Level.SEVERE, null, e);
		} catch (IllegalAccessException e) {
			Logger.getLogger(Compiler.class.getName()).log(Level.SEVERE, null, e);
		} catch (UnsupportedLookAndFeelException e) {
			Logger.getLogger(Compiler.class.getName()).log(Level.SEVERE, null, e);
		}
		// </editor-fold>

		/* Create and editingText the form */
		EventQueue.invokeLater(new Runnable() {
			@Override
			public void run() {
				new Compiler().setVisible(true);
			}
		});
	}
}
