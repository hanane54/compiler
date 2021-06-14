package inpt.ac.ma;

import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.border.EmptyBorder;
import javax.swing.filechooser.FileNameExtensionFilter;

@SuppressWarnings("serial")
public class LexicalAnalysis extends JFrame {

	private JPanel contentPane;
	private final JFileChooser openFileChooser;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {

		EventQueue.invokeLater(new Runnable() {
			@Override
			public void run() {
				try {
					LexicalAnalysis frame = new LexicalAnalysis();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public LexicalAnalysis() {

		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 841, 615);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);

		JButton openFileButton = new JButton("Open file...");
		openFileButton.setBackground(new Color(245, 255, 250));

		openFileButton.setBounds(224, 29, 127, 23);
		contentPane.add(openFileButton);

		JLabel messageLabel = new JLabel("");
		messageLabel.setForeground(Color.BLACK);
		messageLabel.setFont(new Font("Tahoma", Font.PLAIN, 13));
		messageLabel.setBackground(Color.PINK);
		messageLabel.setBounds(361, 29, 394, 24);
		contentPane.add(messageLabel);

		JTextArea txtOutput = new JTextArea();
		txtOutput.setWrapStyleWord(true);
		txtOutput.setBackground(new Color(240, 230, 140));
		txtOutput.setText("Output Texte :");
		txtOutput.setBounds(224, 66, 546, 416);
		contentPane.add(txtOutput);

		JLabel imgLabel = new JLabel("");
		Image img = new ImageIcon(this.getClass().getResource("/amarilla.png")).getImage();
		imgLabel.setIcon(new ImageIcon(img));
		imgLabel.setBounds(0, 0, 200, 173);
		contentPane.add(imgLabel);
		openFileChooser = new JFileChooser();
		openFileChooser.setCurrentDirectory(new File("C:\\temp"));
		openFileChooser.setFileFilter(new FileNameExtensionFilter("mrl file", "mrl"));
		openFileButton.addActionListener(new ActionListener() {
			@SuppressWarnings("resource")
			@Override
			public void actionPerformed(ActionEvent e) {
				int returnValue = openFileChooser.showOpenDialog(null);

				if (returnValue == JFileChooser.APPROVE_OPTION) {

					File selected = openFileChooser.getSelectedFile();
					String fileName = selected.getName();
					if (fileName.endsWith("mrl")) {
						messageLabel.setText("File successfully load!");
						try {
							BufferedReader in;
							in = new BufferedReader(new FileReader(selected));
							ArrayList<String> erreurs = new ArrayList<>();
							int numeroDeLigne = 1;

							for (String ligne; (ligne = in.readLine()) != null;) {

// txtOutput.setText(txtOutput.getText() + "\n" + ligne);

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

								numeroDeLigne++;
							}

							// afficer les erreurs
							if (erreurs != null) {
								for (int j = 0; j < erreurs.size(); j++) {
									txtOutput.setText(txtOutput.getText() + "\n" + erreurs.get(j));
								}
							}
							in.close();

						} catch (FileNotFoundException e1) {
							e1.printStackTrace();
						} catch (IOException e1) {
							e1.printStackTrace();
						}
					} else {
						JOptionPane.showMessageDialog(openFileButton, "Please enter a file with the extension mrl");
					}

				} else {
					messageLabel.setText("No file chosen");
				}
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

	}
}
