package enigma;

import java.util.ArrayList;

public class Enigma {

	private ArrayList<Character> alphabet = new ArrayList<Character>();
	private int[][] rotors = new int[7][26];
	private int[] path = new int[8];
	private int[] order = new int[3];
	// true == right
	private boolean[] clockwise = new boolean[3];
	private int mouvementCount = 0;
	private int rotorCount = 0;
	private boolean keyInitialized = false;

	public void setOrder(int[] order) {
		this.order = order;
	}

	public void keyInit() {
		keyInitialized = true;
	}

	public void setClockwise(boolean[] clockwise) {
		this.clockwise = clockwise;
	}

	public Enigma() {
		initRotors();
	}

	public int mod(int num, int mod) {
		int temp;
		if (num >= 0)
			return num % mod;
		else {
			temp = num % mod;
			return temp + mod;
		}
	}

	public int[] getPath() {
		return path;
	}

	public ArrayList<Character> getAlphabet() {
		return alphabet;
	}

	public int[][] getRotors() {
		return rotors;
	}

	public String encrypt(char car) {

		int cel = alphabet.indexOf(car);
		path[0] = cel;
		path[1] = cel;
		cel = mod(rotors[0][cel] + cel, 26);
		path[2] = cel;
		cel = mod(rotors[2][cel] + cel, 26);
		path[3] = cel;
		cel = mod(rotors[4][cel] + cel, 26);
		path[4] = cel;
		cel = mod(rotors[6][cel] + cel, 26);
		path[5] = cel;
		cel = mod(rotors[5][cel] + cel, 26);
		path[6] = cel;
		cel = mod(rotors[3][cel] + cel, 26);
		path[7] = cel;
		cel = mod(rotors[1][cel] + cel, 26);
		
		return "" + (char) (cel + 97);
	}

	
	
	public int intToRotorNumber(int index) {
		int selector;
		switch (index) {
		case 1:
			selector = 0;
			break;
		case 2:
			selector = 2;
			break;
		case 3:
			selector = 4;
			break;
		default:
			selector = 0;
			System.err.println("Erreur de selection de roteur");
			break;
		}

		return selector;
	}

	public void rotorNormalMovement() {

		if (keyInitialized) {
			if (mouvementCount >= 26) {
				mouvementCount = 0;
				rotorCount++;
			}
			if (rotorCount >= 3) {
				rotorCount = 0;
			}
			int rotorNumber = order[rotorCount];
			if (clockwise[rotorCount]) {
				rotateTabRight(intToRotorNumber(rotorNumber), 1);
			} else
				rotateTabLeft(intToRotorNumber(rotorNumber), 1);
			mouvementCount++;
		}
	}

	public void rotateTabRight(int rotorSelect, int offset) {
		for (int g = 0; g < 2; g++) {
			int[] tab = rotors[rotorSelect + g];
			for (int j = 0; j < offset; j++) {
				int temp = tab[tab.length - 1];
				int i;
				for (i = tab.length - 2; i >= 0; i--) {
					tab[i + 1] = tab[i];
				}
				tab[0] = temp;
			}
		}
	}

	public void rotateTabLeft(int rotorSelect, int offset) {
		for (int g = 0; g < 2; g++) {
			int[] tab = rotors[rotorSelect + g];
			for (int j = 0; j < offset; j++) {
				int temp = tab[0];
				int i;
				for (i = 0; i < tab.length - 1; i++) {
					tab[i] = tab[i + 1];
				}
				tab[i] = temp;
			}
		}
	}

	public void initRotors() {

		int[] wheelOneFirst = { 10, 21, 5, -17, -21, -4, 12, 16, 6, -3, 7, -7, 4, 2, 5, -7, -11, -17, -9, -6, -9, -19,
				2, -3, -21, -4 };
		int[] wheelOneSecond = { 17, 4, 19, 21, 7, 11, 3, -5, 7, 9, -10, 9, 17, 6, -6, -2, -4, -7, -12, -5, 3, 4, -21,
				-16, -2, -21 };
		int[] wheelTwoFirst = { 3, 17, 22, 18, 16, 7, 5, 1, -7, 16, -3, 8, 2, 9, 2, -5, -1, -13, -12, -17, -11, -4, 1,
				-10, -19, -25 };
		int[] wheelTwoSecond = { 25, 7, 17, -3, 13, 19, 12, 3, -1, 11, 5, -5, -7, 10, -2, 1, -2, 4, -17, -8, -16, -18,
				-9, -1, -22, -16 };
		int[] wheelThreeFirst = { 1, 16, 5, 17, 20, 8, -2, 2, 14, 6, 2, -5, -12, -10, 9, 10, 5, -9, 1, -14, -2, -10, -6,
				13, -10, -23 };
		int[] wheelThreeSecond = { 12, -1, 23, 10, 2, 14, 5, -5, 9, -2, -13, 10, -2, -8, 10, -6, 6, -16, 2, -1, -17, -5,
				-14, -9, -20, -10 };

		int[] reflector = new int[26];
		for (int i = 0, j = 25; i < reflector.length; i++, j -= 2) {
			reflector[i] = j;
			char letter = (char) (i + 97);
			alphabet.add(letter);
			mouvementCount = 0;
			rotorCount = 0;
			rotors[0] = wheelOneFirst;
			rotors[1] = wheelOneSecond;
			rotors[2] = wheelTwoFirst;
			rotors[3] = wheelTwoSecond;
			rotors[4] = wheelThreeFirst;
			rotors[5] = wheelThreeSecond;
			rotors[6] = reflector;

		}
	}

}
