import java.util.ArrayList;
import java.util.Random;

public class Player {
    static final int SIZE = 4;

    // Displays the results (black pegs and white pegs) to the player
    public static void showResults() {
        System.out.println("Black Peg(s): " + CodeBreakerGUI.bp);
        System.out.println("White Peg(s): " + CodeBreakerGUI.wp);
    }

    // Validates if the entered code contains valid colors
    public static boolean isValidCode(String code) {
        for (char c : code.toCharArray()) {
            boolean validation = false;
            switch (Character.toUpperCase(c)) {
                case 'R':
                case 'G':
                case 'B':
                case 'O':
                case 'Y':
                case 'P':
                    validation = true;
                    break;
                default:
                    return false;
            }
            if (!validation) {
                return false;
            }
        }
        return true;
    }

    // Checks for white and/or black pegs
    public static int[] compare(String realAns, String playerRsd) {
        // Create lists to store unmatched colors in the secret code and player's answer
        ArrayList<Character> unmatchedReal = new ArrayList<>();
        ArrayList<Character> unmatchedPlayer = new ArrayList<>();

        // Iterate through each position in the code
        for (int i = 0; i < SIZE; i++) {
            // Check if the color at the current position matches exactly
            if (realAns.charAt(i) == playerRsd.charAt(i)) {
                // Increment black pegs count if the colors match exactly
                CodeBreakerGUI.bp++;
            } else {
                // Add unmatched colors to respective lists for further comparison
                unmatchedReal.add(realAns.charAt(i));
                unmatchedPlayer.add(playerRsd.charAt(i));
            }
        }

        // Iterate through each unmatched color in the player's answer
        for (Character color : unmatchedPlayer) {
            // Check if the unmatched color is present in the secret code
            if (unmatchedReal.contains(color)) {
                // Increment white pegs count and remove the color from the unmatched list
            	CodeBreakerGUI.wp++;
                unmatchedReal.remove(color);
            }
        }

        // Return the BP and WP amounts as an array
        int[] pegs = { CodeBreakerGUI.bp, CodeBreakerGUI.wp };
        return pegs;
    }

    // Generates a random color code
    public static String randomColorGeneration(String[] VALID_CHARS) {
        Random rand = new Random();
        char[] randomColour = new char[SIZE];
        for (int i = 0; i < SIZE; i++) {
            int index = rand.nextInt(VALID_CHARS.length);
            randomColour[i] = VALID_CHARS[index].charAt(0);
        }
        return new String(randomColour);
    }
}
