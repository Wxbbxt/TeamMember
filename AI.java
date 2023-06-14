import java.util.ArrayList;
import java.util.Arrays;
import java.util.concurrent.atomic.AtomicInteger;

public class AI {
    public static final int SIZE = 4; // The size of the secret code
    public static final char[] VALID_CHARS = {'R', 'G', 'B', 'O', 'Y', 'P'}; // Valid characters for the secret code
    public static final int TRIES_LIMIT = 10; // Maximum number of tries allowed

    // Method to check if a color is valid
    public static boolean isValidColor(char color) {
        for (char c : VALID_CHARS) {
            if (c == color) {
                return true; // Color is valid
            }
        }
        return false; // Color is not valid
    }

    // Method to generate all possible codes
    public static ArrayList<String> generateAllCodes() {
        ArrayList<String> allCodes = new ArrayList<>();

        for (char c1 : VALID_CHARS) {
            for (char c2 : VALID_CHARS) {
                for (char c3 : VALID_CHARS) {
                    for (char c4 : VALID_CHARS) {
                        String code = String.valueOf(c1) + c2 + c3 + c4;
                        allCodes.add(code); // Add the code to the list of all codes
                    }
                }
            }
        }

        return allCodes; // Return the list of all codes
    }

    // Method to get the AI's next guess
    public static String getNextGuess(ArrayList<String> remainingCodes) {
        // If only one code remains, return it as the guess
        if (remainingCodes.size() == 1) {
            return remainingCodes.get(0);
        }

        String guess = "RRGB"; // Default initial guess
        if (remainingCodes.contains(guess)) {
            return guess; // If the default guess is still in the remaining codes, return it
        }

        // Calculate the best guess based on the remaining codes and their scores
        int minMaxScore = Integer.MAX_VALUE;
        String bestGuess = "";

        for (String code : remainingCodes) {
            int[] scoreCount = new int[14];

            for (String guessCode : remainingCodes) {
            	int score = getScore(code, guessCode);
            	scoreCount[score]++;
            }

            int maxScoreCount = Arrays.stream(scoreCount).max().getAsInt();

            if (maxScoreCount < minMaxScore) {
                minMaxScore = maxScoreCount;
                bestGuess = code;
            }
        }

        guess = bestGuess;
        return guess; // Return the AI's best guess
    }

    // Method to calculate the score for two codes
    public static int getScore(String code1, String code2) {
        int score = 0;

        for (int i = 0; i < SIZE; i++) {
            if (code1.charAt(i) == code2.charAt(i)) {
                score++; // Increment the score if the characters at the same position match
            }
        }

        return score; // Return the score
    }

    // Method to filter the remaining codes based on the guess and feedback
    public static ArrayList<String> filterCodes(ArrayList<String> codes, String guess, AtomicInteger[] feedback) {
        ArrayList<String> filteredCodes = new ArrayList<>();

        for (String code : codes) {
            int[] codeFeedback = getFeedback(code, guess);
            if (codeFeedback[0] == feedback[0].get() && codeFeedback[1] == feedback[1].get()) {
                filteredCodes.add(code); // Add the code to the filtered codes if the feedback matches
            }
        }

        return filteredCodes; // Return the filtered codes
    }

    // Method to calculate the feedback for a guess
    public static int[] getFeedback(String secretCode, String guess) {
        int blackPegs = 0; // Number of black pegs (correct color and position)
        int whitePegs = 0; // Number of white pegs (correct color but wrong position)
        boolean[] secretUsed = new boolean[SIZE];
        boolean[] guessUsed = new boolean[SIZE];

        // Check for black pegs
        for (int i = 0; i < SIZE; i++) {
            if (secretCode.charAt(i) == guess.charAt(i)) {
                blackPegs++; // Increment black peg count
                secretUsed[i] = true;
                guessUsed[i] = true;
            }
        }

        // Check for white pegs
        for (int i = 0; i < SIZE; i++) {
            if (!guessUsed[i]) {
                for (int j = 0; j < SIZE; j++) {
                    if (!secretUsed[j] && secretCode.charAt(j) == guess.charAt(i)) {
                        whitePegs++; // Increment white peg count
                        secretUsed[j] = true;
                        guessUsed[i] = true;
                        break;
                    }
                }
            }
        }

        return new int[]{blackPegs, whitePegs}; // Return the feedback array
    }
}
