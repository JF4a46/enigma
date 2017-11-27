package enigma;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
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

	public EnigmaInterface() {
		setUI();
		setDataUI();
	}

	private void setUI() {
		setLayout(null);
		String[] buttonNames = { "Configurer Rotors", "Encrypter", "Etape Suivante", "Decrypter" };
		String[] names = { "Réflecteur", "Roteur 3", "Roteur 2", "Roteur 1" };
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
			cle[i].setBounds(340 + i * 50, 400, 30, 25);
			add(cle[i]);
		}

		for (int i = 0; i < reflecteur.length; i++) {
			reflecteur[i] = new JTextArea();
			reflecteur[i].setBounds(100 + 30 * i, 50, 20, 20);
			reflecteur[i].setEditable(false);
			add(reflecteur[i]);
		}

		getContentPane().setBackground(Color.GRAY);
		etiquettes[4] = new JLabel("Clé");
		etiquettes[4].setBounds(250, 400, 80, 25);
		add(etiquettes[4]);
		zoneClaire.setBounds(100, 450, 300, 100);
		add(zoneClaire);
		zoneChiffre.setBounds(100, 630, 300, 100);
		add(zoneChiffre);

		for (int i = 0; i < roteurs.length; i++) {
			for (int j = 0; j < roteurs[i].length; j++) {
				roteurs[i][j] = new JTextArea();
				roteurs[i][j].setEditable(false);
				roteurs[i][j].setBounds(100 + 30 * j, 35 * i + 100, 20, 20);
				add(roteurs[i][j]);
			}
		}
		char[] alphabet = enig.getAlphabet();
		for (int i = 0; i < alphabet.length; i++) {
			this.alphabet[i] = new JTextArea("" + alphabet[i]);
			this.alphabet[i].setBounds(100 + 30 * i, 310, 20, 20);
			add(this.alphabet[i]);
		}

		setSize(900, 750);
		setVisible(true);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
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

	public static void main(String[] args) {
		EnigmaInterface app = new EnigmaInterface();
	}

	public void actionPerformed(ActionEvent e) {

		if (e.getSource() == interfaces[0]) {
			parseKey();

		} else if (e.getSource() == interfaces[1]) {

		} else if (e.getSource() == interfaces[2]) {
			etapeSuivante();
		} else if (e.getSource() == interfaces[3]) {
		}
	}

	private void etapeSuivante() {
		enig.rotorNormalMovement();
		setDataUI();
	}

	private void parseKey() {
		enig.initRotors();
		int[] tabOrder = new int[3];
		boolean[] sensD = new boolean[3];
		int[] initOffset = new int[3];
		int compteur = 0;

		for (int i = 0; i < cle.length; i += 3) {
			tabOrder[compteur] = Integer.parseInt(cle[i].getText());

			if (cle[i + 1].getText().charAt(0) == 'd' || cle[i + 1].getText().charAt(0) == 'D') {
				sensD[compteur] = true;
			} else if (cle[i + 1].getText().charAt(0) == 'g' || cle[i + 1].getText().charAt(0) == 'G') {
				sensD[compteur] = false;
			} else
				System.err.println("Erreur direction rotation");
			initOffset[compteur] = Integer.parseInt(cle[i + 2].getText());
			compteur++;
		}
		if (tabOrder[0] == tabOrder[1] || tabOrder[1] == tabOrder[2] || tabOrder[0] == tabOrder[2])
			System.err.println("Erreur rotors");
		enig.setOrder(tabOrder);
		enig.setClockwise(sensD);
		for (int i = 0; i < initOffset.length; i++) {
			int selector = enig.intToRotorNumber(tabOrder[i]);
			if (initOffset[i] > 0) {
				enig.rotateTabRight(selector, initOffset[i]);
			} else if (initOffset[i] < 0) {
				enig.rotateTabLeft(selector, initOffset[i]);
			}
		}
		setDataUI();
	}
}
