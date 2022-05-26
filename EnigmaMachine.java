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
	String leftShift;
	String middleShift;
	String rightShift;
	ArrayList<String> plugboardSettings;
	String input = "";
	String output = "";

	public EnigmaMachine(String[] inputSplit) {
		leftRotor = rotorConvertor(inputSplit[0]);
		middleRotor = rotorConvertor(inputSplit[1]);
		rightRotor = rotorConvertor(inputSplit[2]);
		leftShift = inputSplit[3];
		middleShift = inputSplit[4];
		rightShift = inputSplit[5] + 1;
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
		System.out.println(output);
		// Step 2
		output = Enigma.affineCipher(output.split(""), true, ALPHABET, rightRotor, (int) (rightShift.charAt(0)) - 64);
		System.out.println(output);
		output = Enigma.affineCipher(output.split(""), true, ALPHABET, middleRotor, (int) (middleShift.charAt(0) - 65));
		System.out.println(output);
		output = Enigma.affineCipher(output.split(""), true, ALPHABET, leftRotor, (int) (leftShift.charAt(0) - 65));

		// Step 3
		output = Enigma.affineCipher(output.split(""), true, ALPHABET, REFLECTOR, 0);
		// Step 4
		output = Enigma.affineCipher(output.split(""), true, leftRotor, ALPHABET, (int) (leftShift.charAt(0)) - 65);
		output = Enigma.affineCipher(output.split(""), true, middleRotor, ALPHABET, (int) (middleShift.charAt(0) - 65));
		output = Enigma.affineCipher(output.split(""), true, rightRotor, ALPHABET, (int) (rightShift.charAt(0) - 64));
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
}
