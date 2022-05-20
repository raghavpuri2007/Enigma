//Raghav Puri
//Ap Computer Science A
//Enigma

import java.util.Arrays;
import java.util.Scanner;

public class Enigma {
	public static final String ALPHABET = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
	public static final String ROTOR_ONE = "EKMFLGDQVZNTOWYHXUSPAIBRCJ";
	public static final String ROTOR_TWO = "AJDKSIRUXBLHWTMCQGZNPYFVOE";
	public static final String ROTOR_THREE = "BDFHJLCPRTXVZNYEIWGAKMUSQO";

	public static void main(String[] args) {
		processCommand();
	}

	public static void processCommand() {
		boolean quit = false;
		while (!quit) {
			boolean forward = false;
			Scanner userInput = new Scanner(System.in);
			String result = "";
			System.out.print("Input: ");
			String input = userInput.nextLine();
			if (input.charAt(0) == '>') {
				forward = true;
			}
			String[] splitted = input.substring(3).split("");
			// input checker
			if (input.charAt(1) == 'C') {
				int increment = 1;
				if (Character.isDigit(input.charAt(2))) {
					increment = Integer.parseInt("" + input.charAt(2));
					splitted = Arrays.copyOfRange(splitted, 1, splitted.length);
				}
				result = caeserCipher(splitted, forward, increment);
			} else if (input.charAt(1) == 'A') {
				result = affineCipher(splitted, forward, ALPHABET, ROTOR_ONE);
			} else if (input.charAt(1) == 'R') {
				result = rotorCipher(splitted, forward);
			} else if (input.equalsIgnoreCase("help")) {
				result = "help menu";
			} else if (input.equalsIgnoreCase("quit")) {
				result = "Thank you for using Enigma. Have a good day!";
				quit = true;
			} else {
				result = "I don't understand, please enter valid input";
			}
			System.out.println(result);
		}
	}

	public static String caeserCipher(String[] input, boolean forward, int increment) {
		// checks if the input is an advanced caesar cipher
		String result = "";
		for (String s : input) {
			char letter = s.toUpperCase().charAt(0);
			if (s.equals(" ")) {
				result += " ";
			} else if (!Character.isLetter(letter)) {
				return s + " is not a letter. Please enter valid input";
			} else {
				if (forward) {
					if (letter + increment > 90) {
						result += (char) (64 + increment - (90 - letter));
					} else {
						result += (char) (letter + increment);
					}
				} else {
					if (letter - increment < 65) {
						result += (char) (91 - increment - (65 - letter));
					} else {
						result += (char) (letter - increment);
					}
				}
			}
		}
		return result;
	}

	public static String rotorCipher(String[] input, boolean forward) {
		if (forward) {
			String cipher1 = affineCipher(input, forward, ALPHABET, ROTOR_ONE);
			String cipher2 = affineCipher(cipher1.split(""), forward, ALPHABET, ROTOR_TWO);
			return affineCipher(cipher2.split(""), forward, ALPHABET, ROTOR_THREE);
		} else {
			String cipher1 = affineCipher(input, forward, ALPHABET, ROTOR_THREE);
			String cipher2 = affineCipher(cipher1.split(""), forward, ALPHABET, ROTOR_TWO);
			return affineCipher(cipher2.split(""), forward, ALPHABET, ROTOR_ONE);
		}
	}

	public static String affineCipher(String[] input, boolean forward, String rotorIn, String rotorOut) {
		String result = "";
		int index = 0;
		for (String s : input) {
			if (s.equals(" ")) {
				result += " ";
			} else {
				if (forward) {
					// in
					index = rotorIn.indexOf(s);
					result += rotorOut.charAt(index);
				} else {
					// out
					index = rotorOut.indexOf(s);
					result += rotorIn.charAt(index);
				}
			}
		}
		return result;
	}
}
