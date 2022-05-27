import java.util.ArrayList;

public class EnigmaMachine {
	public static final String REFLECTOR = "YRUHQSLDPXNGOKMIEBFZCWVJAT";
	public static final String ALPHABET = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
	public static final String ROTOR_ONE = "EKMFLGDQVZNTOWYHXUSPAIBRCJ";
	public static final String ROTOR_TWO = "AJDKSIRUXBLHWTMCQGZNPYFVOE";
	public static final String ROTOR_THREE = "BDFHJLCPRTXVZNYEIWGAKMUSQO";
	public static final String ROTOR_FOUR = "ESOVPZJAYQUIRHXLNFTGKDCMWB";
	public static final String ROTOR_FIVE = "VZBRGITYUPSDNHLXAWMJQOFECK";
	String leftRotor;
	String middleRotor;
	String rightRotor;
	char leftShift;
	char middleShift;
	char rightShift;
	ArrayList<String> plugboardSettings;
	String input = "";
	String output = "";
	boolean notched = false;
	boolean shifted = false;

	public EnigmaMachine(String[] inputSplit) {
		leftRotor = rotorConvertor(inputSplit[0]);
		middleRotor = rotorConvertor(inputSplit[1]);
		rightRotor = rotorConvertor(inputSplit[2]);
		leftShift = inputSplit[3].charAt(0);
		middleShift = inputSplit[4].charAt(0);
		rightShift = inputSplit[5].charAt(0);
		plugboardSettings = new ArrayList<>();
		for (int i = 6; i < inputSplit.length - 1; i++) {
			plugboardSettings.add(inputSplit[i]);
		}
		input = inputSplit[inputSplit.length - 1].toUpperCase();
		decode();
	}

	public String plugboardSwitch(String letter) {
		// switches letter if has plugboard
		for (String setting : plugboardSettings) {
			if (setting.contains(letter)) {
				if (setting.indexOf(letter) == 0) {
					letter = setting.charAt(1) + "";
				} else {
					letter = setting.charAt(0) + "";
				}
			}
		}
		return letter;
	}

	public void decode() {
		// Step 1
		for (int i = 0; i < input.length(); i++) {
			output += plugboardSwitch(input.charAt(i) + "");
		}
		// Step 2
		for (int i = 0; i < output.length(); i++) {
			if (notched) {
				middleShift = Enigma.caeserCipher((middleShift + "").split(""), true, 1).charAt(0);
				notched = false;
			}
			rightShift = Enigma.caeserCipher((rightShift + "").split(""), true, 1).charAt(0);
			checkNotch(rightRotor, rightShift);
			checkNotch(middleRotor, middleShift);
			String current = output.charAt(i) + "";
			current = Enigma.affineCipher(current.split(""), true, ALPHABET, rightRotor, (int) (rightShift - 65));
			current = Enigma.affineCipher(current.split(""), true, ALPHABET, middleRotor, (int) (middleShift - 65));
			current = Enigma.affineCipher(current.split(""), true, ALPHABET, leftRotor, (int) (leftShift - 65));
			// Step 3
			current = Enigma.affineCipher(current.split(""), true, ALPHABET, REFLECTOR, 0);
			// Step 4
			current = Enigma.affineCipher(current.split(""), true, leftRotor, ALPHABET, (int) (leftShift - 65));
			current = Enigma.affineCipher(current.split(""), true, middleRotor, ALPHABET, (int) (middleShift - 65));
			current = Enigma.affineCipher(current.split(""), true, rightRotor, ALPHABET, (int) (rightShift - 65));
			output = output.substring(0, i) + current + output.substring(i + 1);
		}
		// Step 5
		for (int i = 0; i < input.length(); i++) {
			output = output.substring(0, i) + plugboardSwitch(output.charAt(i) + "") + output.substring(i + 1);
		}
		System.out.println(output);

	}

	public String rotorConvertor(String str) {
		if (str.equals("I")) {
			return ROTOR_ONE;
		} else if (str.equals("II")) {
			return ROTOR_TWO;
		} else if (str.equals("III")) {
			return ROTOR_THREE;
		} else if (str.equals("IV")) {
			return ROTOR_FOUR;
		} else if (str.equals("V")) {
			return ROTOR_FIVE;
		} else {
			return str;
		}
	}

	public void checkNotch(String rotor, char shift) {
		boolean rotorOne = rotor.equals(ROTOR_ONE) && shift == 'R';
		boolean rotorTwo = rotor.equals(ROTOR_TWO) && shift == 'F';
		boolean rotorThree = rotor.equals(ROTOR_THREE) && shift == 'W';
		boolean rotorFour = rotor.equals(ROTOR_FOUR) && shift == 'K';
		boolean rotorFive = rotor.equals(ROTOR_FIVE) && shift == 'A';
		System.out.println("Left: " + leftShift + ", Middle: " + middleShift + ", Right: " + rightShift);
		if (rotorOne || rotorTwo || rotorThree || rotorFour || rotorFive) {
			if (rotor.equals(rightRotor)) {
				middleShift = Enigma.caeserCipher((middleShift + "").split(""), true, 1).charAt(0);
				notched = true;
			} else {
				if (!shifted) {
					leftShift = Enigma.caeserCipher((leftShift + "").split(""), true, 1).charAt(0);
					shifted = true;
				}
			}
		}
	}
}
