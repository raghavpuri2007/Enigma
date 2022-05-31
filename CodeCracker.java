public class CodeCracker {
	public static String encrypted = "KJKRVCZFIQDUDHSBCGIKVJQEMBUXWBGOBEKMQIXFODK";

	// String is 43 characters
	public static void main(String[] args) throws InterruptedException {
		String s = "COMPUTER";
		boolean rotorgoing = true;
		boolean shiftgoing = true;
		String plugboard = "AA BB CC DD EE FF GG HH II JJ";
		String[] rotors = { "I", "II", "III" };
		String[] firstRotors = { "I", "II", "III" };
		String[] rotorShift = { "A", "A", "Z" };
		int advance = 0;
		int iteration = 1;
		while (rotorgoing) {
			while (shiftgoing) {
				String input = String.format("%s %s %s %s %s %s %s %s", rotors[0], rotors[1], rotors[2], rotorShift[0],
						rotorShift[1], rotorShift[2], plugboard, encrypted);

				EnigmaMachine e = new EnigmaMachine(input.split(" "));
				String output = e.getOutput();
				if (output.contains("PUTER")) {
					System.out.println(input);
					System.out.println(iteration + ": " + output);
					System.out.println();
					iteration++;
				}
				if (!shiftAdvance(rotorShift)) {
					shiftgoing = false;
				}
			}
			rotorAdvance(rotors, advance);
			advance++;
			if (advance == 6) {
				if (rotors[0].equals("III") && rotors[1].equals("IV") && rotors[2].equals("V")) {
					rotorgoing = false;
				} else {
					advance = 0;
					rotors[0] = firstRotors[0];
					rotors[1] = firstRotors[1];
					rotors[2] = firstRotors[2];
					rotorIncrement(rotors);
					firstRotors[0] = rotors[0];
					firstRotors[1] = rotors[1];
					firstRotors[2] = rotors[2];
				}
			}
			shiftgoing = true;
			rotorShift[0] = "A";
			rotorShift[1] = "A";
			rotorShift[2] = "Z";
		}
	}

	public static void rotorAdvance(String[] rotors, int advancement) {
		String temp = "";
		if (advancement % 2 == 0) {
			temp = rotors[2];
			rotors[2] = rotors[1];
			rotors[1] = temp;
		} else if (advancement % 2 == 1) {
			temp = rotors[2];
			rotors[2] = rotors[0];
			rotors[0] = temp;
		}
	}

	public static void rotorIncrement(String[] rotors) {
		if (rotors[2].equals("III")) {
			rotors[2] = "IV";
		} else if (rotors[2].equals("IV")) {
			rotors[2] = "V";
		} else if (rotors[1].equals("II")) {
			rotors[1] = "III";
			rotors[2] = "IV";
		} else if (rotors[1].equals("III")) {
			rotors[1] = "IV";
		} else if (rotors[1].equals("IV") && rotors[0].equals("I")) {
			rotors[0] = "II";
			rotors[1] = "III";
			rotors[2] = "IV";
		} else {
			rotors[0] = "III";
		}
	}

	public static boolean shiftAdvance(String[] rotorShift) {
		if (rotorShift[0].equals("Z")) {
			return false;
		}
		if (rotorShift[1].equals("Z")) {
			rotorShift[0] = Enigma.caeserCipher(rotorShift[0].split(" "), true, 1);
			rotorShift[1] = Enigma.caeserCipher(rotorShift[1].split(" "), true, 1);
		}
		if (rotorShift[2].equals("Y")) {
			rotorShift[1] = Enigma.caeserCipher(rotorShift[1].split(" "), true, 1);
		}
		rotorShift[2] = Enigma.caeserCipher(rotorShift[2].split(" "), true, 1);
		return true;
	}
}
