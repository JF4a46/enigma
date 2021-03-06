package enigma;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

public class EnigmaInterface extends JFrame implements ActionListener {

	Enigma enig = new Enigma();
	JTextArea[][] roteurs = new JTextArea[6][26];
	JTextArea[] reflecteur = new JTextArea[26];
	JTextArea[] alphabet = new JTextArea[26];
	JTextField[] cle = new JTextField[9];
	JTextArea zoneClaire = new JTextArea();
	JTextArea zoneChiffre = new JTextArea();
	JButton[] interfaces = new JButton[4];
	JLabel mode = new JLabel("Mode : Aucun");
	String messageClear;
	String messageEnc;
	boolean encryption;
	boolean first = true;
	boolean go = false;
	int pointeurStr = 0;
	int limitStr;
	Font normal = new Font("timesnews", Font.PLAIN, 12);
	Font special = new Font("timesnews", Font.BOLD, 14);

	public EnigmaInterface() {
		setUI();
		setDataUI();
	}

	private void setUI() {
		mode.setBounds(70, 400, 120, 30);
		mode.setFont(new Font("TimesRoman", Font.PLAIN, 18));
		add(mode);
		setLayout(null);
		String[] buttonNames = { "Configurer Rotors", "Encrypter", "Etape Suivante", "Decrypter" };
		String[] names = { "Reflecteur", "Roteur 3", "Roteur 2", "Roteur 1" };
		JLabel[] etiquettes = new JLabel[5];

		for (int i = 0; i < etiquettes.length - 1; i++) {
			etiquettes[i] = new JLabel(names[i]);
			etiquettes[i].setBounds(15, 66 * i + 50, 80, 25);
			add(etiquettes[i]);
		}

		for (int i = 0; i < interfaces.length; i++) {
			interfaces[i] = new JButton(buttonNames[i]);
			interfaces[i].setBounds(200 * i + 55, 560, 180, 40);
			interfaces[i].addActionListener(this);
			add(interfaces[i]);
		}

		for (int i = 0; i < cle.length; i++) {
			cle[i] = new JTextField();
			cle[i].setBounds(340 + i * 53, 400, 30, 25);
			add(cle[i]);
		}

		for (int i = 0; i < reflecteur.length; i++) {
			reflecteur[i] = new JTextArea();
			reflecteur[i].setBounds(100 + 30 * i, 50, 20, 20);
			reflecteur[i].setEditable(false);
			add(reflecteur[i]);
		}

		getContentPane().setBackground(Color.GRAY);
		etiquettes[4] = new JLabel("Cle");
		etiquettes[4].setBounds(250, 400, 80, 25);
		add(etiquettes[4]);
		zoneClaire.setBounds(100, 470, 550, 75);
		add(zoneClaire);
		zoneChiffre.setBounds(100, 610, 550, 75);
		add(zoneChiffre);
		JLabel indic = new JLabel("(                       ),(                       ),(                       )");
		indic.setBounds(333, 370,600, 80);
		indic.setFont(new Font("timesnews", Font.BOLD, 22));
		add(indic);
		
		
		for (int i = 0; i < roteurs.length; i++) {
			for (int j = 0; j < roteurs[i].length; j++) {
				roteurs[i][j] = new JTextArea();
				roteurs[i][j].setEditable(false);
				roteurs[i][j].setBounds(100 + 30 * j, 35 * i + 100, 20, 20);
				add(roteurs[i][j]);
			}
		}

		for (int i = 0; i < alphabet.length; i++) {
			this.alphabet[i] = new JTextArea("" + (char) (i + 97));
			this.alphabet[i].setBounds(100 + 30 * i, 310, 20, 20);
			this.alphabet[i].setEditable(false);
			add(this.alphabet[i]);
		}

		setSize(900, 750);
		setVisible(true);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		roteurs[3][0].setForeground(Color.BLUE);
	}

	private void setDataUI() {
		int[][] rotors = enig.getRotors();
		int compte = 0;

		for (int i = rotors.length - 2; i >= 0; i--) {
			for (int j = 0; j < rotors[i].length; j++) {
				roteurs[i][j].setText("" + rotors[compte][j]);
			}
			compte++;
		}

		for (int i = 0; i < rotors[6].length; i++) {
			reflecteur[i].setText("" + rotors[6][i]);
		}

	}

	private void resetColor() {
		for (int i = 0; i < roteurs.length; i++) {
			for (int j = 0; j < roteurs[i].length; j++) {
				roteurs[i][j].setFont(normal);
				roteurs[i][j].setForeground(Color.BLACK);
			}
		}
		for (int i = 0; i < alphabet.length; i++) {
			alphabet[i].setFont(normal);
			alphabet[i].setForeground(Color.BLACK);
			reflecteur[i].setFont(normal);
			reflecteur[i].setForeground(Color.BLACK);
		}

	}

	public static void main(String[] args) {
		EnigmaInterface app = new EnigmaInterface();
	}

	public void actionPerformed(ActionEvent e) {

		if (e.getSource() == interfaces[0]) {
			parseKey();
			enig.keyInit();
		} else if (e.getSource() == interfaces[1]) {
			encrypt();
		} else if (e.getSource() == interfaces[2]) {
			if (go)
				etapeSuivante();
		} else if (e.getSource() == interfaces[3]) {
			decrypt();
		}
	}

	private void decrypt() {

		mode.setText("Mode : Decrypt");
		mode.setForeground(Color.GREEN);
		zoneClaire.setText("");
		parseKey();
		encryption = false;
		pointeurStr = 0;
		setDataUI();

	}

	private void encrypt() {

		mode.setText("Mode : Encrypt");
		mode.setForeground(Color.RED);
		zoneChiffre.setText("");
		parseKey();
		encryption = true;
		pointeurStr = 0;
		setDataUI();

	}

	private void setColor() {
		int[] path = enig.getPath();

		alphabet[path[0]].setForeground(Color.RED);
		alphabet[path[0]].setFont(special);
		roteurs[5][path[1]].setForeground(Color.RED);
		roteurs[5][path[1]].setFont(special);
		roteurs[3][path[2]].setForeground(Color.RED);
		roteurs[3][path[2]].setFont(special);
		roteurs[1][path[3]].setForeground(Color.RED);
		roteurs[1][path[3]].setFont(special);
		reflecteur[path[4]].setForeground(Color.RED);
		reflecteur[path[4]].setFont(special);
		roteurs[0][path[5]].setForeground(Color.BLUE);
		roteurs[0][path[5]].setFont(special);
		roteurs[2][path[6]].setForeground(Color.BLUE);
		roteurs[2][path[6]].setFont(special);
		roteurs[4][path[7]].setForeground(Color.BLUE);
		roteurs[4][path[7]].setFont(special);
		alphabet[path[8]].setFont(special);
		alphabet[path[8]].setForeground(Color.BLUE);
		
	}

	private void etapeSuivante() {
		setDataUI();
		resetColor();
		if (first)
			removeProblems();

		if (encryption) {
			if (pointeurStr < zoneClaire.getText().length())
				zoneChiffre.setText(zoneChiffre.getText() + enig.encrypt(zoneClaire.getText().charAt(pointeurStr)));
			else {
				avertissement();
				return;
			}
		} else {
			if (pointeurStr < zoneChiffre.getText().length())
				zoneClaire.setText(zoneClaire.getText() + enig.encrypt(zoneChiffre.getText().charAt(pointeurStr)));
			else {
				avertissement();
				return;
			}
		}
		pointeurStr++;
		enig.rotorNormalMovement();
		setColor();
	}

	private void avertissement() {
		JOptionPane.showMessageDialog(null, "Fin message");
	}

	private void removeProblems() {
		first = false;
		zoneChiffre.setText(zoneChiffre.getText().replaceAll(" ", ""));
		zoneChiffre.setText(zoneChiffre.getText().toLowerCase());
		zoneClaire.setText(zoneClaire.getText().replaceAll(" ", ""));
		zoneClaire.setText(zoneClaire.getText().toLowerCase());
	}

	private void parseKey() {
		enig.initRotors();
		int[] tabOrder = new int[3];
		boolean[] sensD = new boolean[3];
		int[] initOffset = new int[3];
		int compteur = 0;

		for (int i = 0; i < cle.length; i += 3) {
			try {
				tabOrder[compteur] = Integer.parseInt(cle[i].getText());
			} catch (NumberFormatException e) {
				JOptionPane.showMessageDialog(null, "Donnees non compatibles");
				return;
			}

			if (cle[i + 1].getText().charAt(0) == 'd' || cle[i + 1].getText().charAt(0) == 'D') {
				sensD[compteur] = true;
			} else if (cle[i + 1].getText().charAt(0) == 'g' || cle[i + 1].getText().charAt(0) == 'G') {
				sensD[compteur] = false;
			} else {
				JOptionPane.showMessageDialog(null, "Erreur direction rotation, corriger svp");
				return;
			}
			try {
				initOffset[compteur] = Integer.parseInt(cle[i + 2].getText());
			} catch (NumberFormatException e) {
				JOptionPane.showMessageDialog(null, "Donnees non compatibles");
				return;
			}

			compteur++;
		}
		if (tabOrder[0] == tabOrder[1] || tabOrder[1] == tabOrder[2] || tabOrder[0] == tabOrder[2]) {
			JOptionPane.showMessageDialog(null, "Erreur rotors");
			return;
		}
		enig.setOrder(tabOrder);
		enig.setClockwise(sensD);
		for (int i = 0; i < initOffset.length; i++) {
			int selector = enig.intToRotorNumber(tabOrder[i]);
			if (initOffset[i] > 0) {
				enig.rotateTabRight(selector, initOffset[i]);
			} else if (initOffset[i] < 0) {
				enig.rotateTabLeft(selector, Math.abs(initOffset[i]));
			}
		}
		go = true;
		setDataUI();
	}
}
