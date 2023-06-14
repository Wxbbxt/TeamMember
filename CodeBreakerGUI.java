/**
 * @authors: Wen-Yao-Zhang: (1-1810); Tommy Lyu: () 
 * @CourseCode: ICS4U1-03 
 * @DateOfModification: 6/12/2023
 * 
 * @PROPOSE: Create a working Code-Breaker/Mastermind game GUI that contains menus and sections:
 * Login Menu: Login/Play as Guesting (+Settings)
 * Main Menu: CODEBREAKER(Player vs AI) / AI(AI vs User) / LeaderBoards
 * Player: User tries to guess the randomly generated code within 10 tries
 * AI: AI tries to guess player generated code within 10 tries using Knuth's Algorithm
 * 
 * @ASSUMPTIONS: 
 * All input should mouse input
 * Music cannot be added = no sound slider in settings
 * Known Bugs: N/A
 * 
 * @REQUIREMENTS:
 * Create a user friend interface that displays Mastermind/Code-Breaker
 * AI: AI tries to guess user generated code within 10 attempts using Knuths Algorithm
 * PLAYER: Player tries to guess AI generated code within 10 tries
 * 
 * @TOBEADDED: 
 * The button to change the background in settings is yet to be added
 * Complete Proper Commenting
 */


import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.Timer;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.text.JTextComponent;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.Graphics;
import java.awt.GraphicsEnvironment;
import java.awt.GridLayout;
import java.awt.Image;

import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.FilteredImageSource;
import java.awt.image.ImageFilter;
import java.awt.image.RGBImageFilter;
import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class CodeBreakerGUI {
    JFrame window; // Create new frame
    JPanel panel; // Creates a new global panel
    JLabel codeBreaker; // Global label for the welcomeText, i.e., logo: CODEBREAKER
    JLabel logoLabel; // Declare logoLabel as an instance variable
    Font titleFont; // Title Font
    Font pixelFont; // Font size 20
    Font pixelFont2; // Font size 10
    Font pixelFont3; // Font size 30
    Font pixelFont4; // Font size 15
    CustomButton aiButton; // Declaration of a custom button object named aiButton 
    CustomButton codeBreakerButton; // Declaration of a custom button object named codeBreakerButton 
    CustomButton customButton; // Declaration of a custom button object named customButton 

    static final int TRIES_LIMIT = 10; // Maximum number of tries allowed
    static final int SIZE = 4; // Maximum Code Length
    static String VALID_CHARS[] = {"G", "R", "B", "Y", "O", "P"}; // All valid colors
    static String playerRsd; // Player response(d) 
    static String currentUser; // Current User active
    static int bp = 0; // Black pegs
    static int wp = 0; // White pegs
    static int streak = 0; // Player code break streak
    static String realAns; // The real answer
    boolean loggedIn; // Declaration of a boolean variable named loggedIn

    static ImageIcon logoIcon = new ImageIcon("Icon_Logo.png"); // Updated variable name
    private String[] args; // Store the args array as an instance variable
    private ArrayList<String> remainingCodes; // Declare a private ArrayList variable named 'remainingCodes' to store the remaining possible codes
    private Timer timer; // Timer object for scheduling timed events
    private JLabel label; // JLabel object to display text or an image
    private long startTime = 0;
    private boolean isTimerRunning = false; // Used to track if the timer is running
    AtomicLong totalElapsedTime = new AtomicLong(0);
    
    public static void main(String[] args) {
    	new CodeBreakerGUI(args); // Create a new instance of the CodeBreakerGUI class with the given arguments
    }

    public CodeBreakerGUI(String[] args) {
        this.args = args; // Assign the args array to the instance variable

        try {
            // Load the pixelFont font in the project folder 
            pixelFont = Font.createFont(Font.TRUETYPE_FONT, new File("VCR_OSD_MONO_1.001.ttf")).deriveFont(20f); // Size 20
            GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
            ge.registerFont(Font.createFont(Font.TRUETYPE_FONT, new File("VCR_OSD_MONO_1.001.ttf")));
        } catch (IOException | FontFormatException e) {
            e.printStackTrace();
        }
        
        try {
            // Load the pixelFont2 font in the project folder
            pixelFont2 = Font.createFont(Font.TRUETYPE_FONT, new File("VCR_OSD_MONO_1.001.ttf")).deriveFont(10f); // Size 10
            GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
            ge.registerFont(Font.createFont(Font.TRUETYPE_FONT, new File("VCR_OSD_MONO_1.001.ttf")));
        } catch (IOException | FontFormatException e) {
            e.printStackTrace();
        }
        
        try {
            // Load the pixelFont3 font in the project folder
            pixelFont3 = Font.createFont(Font.TRUETYPE_FONT, new File("VCR_OSD_MONO_1.001.ttf")).deriveFont(30f); // Size 30
            GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
            ge.registerFont(Font.createFont(Font.TRUETYPE_FONT, new File("VCR_OSD_MONO_1.001.ttf")));
        } catch (IOException | FontFormatException e) {
            e.printStackTrace();
        }
        
        try {
            // Load the pixelFont2 font in the project folder
            pixelFont4 = Font.createFont(Font.TRUETYPE_FONT, new File("VCR_OSD_MONO_1.001.ttf")).deriveFont(15f); // Size 10
            GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
            ge.registerFont(Font.createFont(Font.TRUETYPE_FONT, new File("VCR_OSD_MONO_1.001.ttf")));
        } catch (IOException | FontFormatException e) {
            e.printStackTrace();
        }
        
        try {
            // Load the title font in the project folder
            titleFont = Font.createFont(Font.TRUETYPE_FONT, new File("Title.ttf")).deriveFont(40f); // Size 40
            GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
            ge.registerFont(Font.createFont(Font.TRUETYPE_FONT, new File("Title.ttf")));
        } catch (IOException | FontFormatException e) {
            e.printStackTrace();
        }
        
        window = new JFrame("Code Breaker"); // Create a new JFrame window with the title "Code Breaker"
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setSize(700, 950); // Set Size of window
        window.setIconImage(logoIcon.getImage()); // Set the icon image for the JFrame

        window.setResizable(false);

        panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS)); // Set the layout manager for the panel
        panel.setOpaque(false); // Set panel background to transparent

        logoLabel = new JLabel(); // Initialize logoLabel
        ImageIcon logoIcon = new ImageIcon("Icon_Logo.png"); // Updated variable name
        Image logoImage = logoIcon.getImage().getScaledInstance(200, 200, java.awt.Image.SCALE_SMOOTH); // Scale the logo image

        // Create a color filter to change the logo to white
        ImageFilter colorFilter = new RGBImageFilter() {
            @Override
            public int filterRGB(int x, int y, int rgb) {
                // Set the RGB channels to white (255) while preserving the alpha channel
                return (rgb & 0xFF000000) | 0x00FFFFFF;
            }
        };

        // Apply the color filter to the logo image
        Image whiteLogoImage = Toolkit.getDefaultToolkit().createImage(new FilteredImageSource(logoImage.getSource(), colorFilter));
        whiteLogoImage = whiteLogoImage.getScaledInstance(200, 200, java.awt.Image.SCALE_SMOOTH);

        // Set the white logo image to the logo label
        logoLabel.setIcon(new ImageIcon(whiteLogoImage));
        logoLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.add(logoLabel); // Add the logo label to the panel

        panel.add(Box.createVerticalStrut(15)); // Used to adjust spacing (15)
        codeBreakerButton = new CustomButton("Code Breaker (User vs AI)", 50, 50); // Create a custom button for the code breaker mode, size adjustable
        codeBreakerButton.setAlignmentX(Component.CENTER_ALIGNMENT); // Center the button

        codeBreakerButton.setOpacity(0.5f); // Set the opacity to half-transparent
        
        codeBreakerButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
            	showPlayerSection(); // Call the showPlayerSection() method when the button is clicked
            }
        });

        panel.add(codeBreakerButton); 

        panel.add(Box.createVerticalStrut(7)); // Used to adjust spacing (7)

        aiButton = new CustomButton("AI (AI vs User)", 50, 50); // Declaration of a new custom button for the AI mode, size adjustable
        aiButton.setAlignmentX(Component.CENTER_ALIGNMENT); // Center the button

        aiButton.setOpacity(0.5f); // Set the opacity to half-transparent

        aiButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                showAISection(); // Call the showAISection() method when the button is clicked
            }
        });

        // Create a new JPanel for the content
        // Override annotation provides a compile time check; used to catch errors
        JPanel contentPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                // Paint the GIF image as the background
                g.drawImage(new ImageIcon("background.gif").getImage(), 0, 0, getWidth(), getHeight(), this);
            }
        };
        contentPanel.setLayout(new BorderLayout());
        contentPanel.setOpaque(false); // Set panel background to transparent

        // Add vertical strut to shift components up
        contentPanel.add(Box.createVerticalStrut(50), BorderLayout.NORTH);

        // Add the panel to the content panel
        contentPanel.add(panel, BorderLayout.CENTER);

        // Add vertical glue to push the panel to the top and center horizontally
        contentPanel.add(Box.createVerticalGlue(), BorderLayout.SOUTH);

        // Add vertical strut to shift components up
        contentPanel.add(Box.createVerticalStrut(50), BorderLayout.SOUTH);

        // Set the content pane of the window to the content panel
        window.setContentPane(contentPanel);
        
        loginMenu(); // Start program; calls loginMenu() method and brings you into the first section of the program
        
        window.setVisible(true);
    }

    private void showPlayerSection() {
    	// Remove the existing components from the panel
        panel.removeAll();
        AtomicBoolean success = new AtomicBoolean(false); // Checks if the player succeeded in cracking the code
        AtomicBoolean triesEXCEEDED = new AtomicBoolean(false); // Checks if the player exceeded the amount of tries
        AtomicInteger TRIES = new AtomicInteger(1); // Number of tries made by the AI
        remainingCodes = AI.generateAllCodes(); // List of all possible codes
        ArrayList<String> hintStringList = new ArrayList<>(); // Create an ArrayList to store hint strings
        ArrayList<Color> hintStringColor = new ArrayList<>(); // Create an ArrayList to store hint colors
        JButton submitButton = null; // Initialize the submit button
        
        // Create and configure the 'PLAYER' title
        JLabel aiLabel = new JLabel("PLAYER");
        aiLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        aiLabel.setForeground(Color.WHITE);
        aiLabel.setFont(titleFont);
        panel.add(aiLabel);

        // Add spacing between AI label and the main panel
        panel.add(Box.createVerticalStrut(15));

        // Create the main panel for the AI section
        JPanel aiPanel = new JPanel();
        // Set the layout for aiPanel as a GridLayout
        // The GridLayout will arrange components in a grid with the specified number of rows and columns
        // In this case, it creates a grid with 1 row and 2 columns
        // The third parameter, 11, specifies the horizontal gap between components
        // The fourth parameter, 0, specifies the vertical gap between components
        aiPanel.setLayout(new GridLayout(1, 2, 11, 0));
        aiPanel.setOpaque(false); // Set the opaque property of aiPanel to false

        // Create the left panel with the grid layout
        JPanel leftPanel = new JPanel();
        leftPanel.setLayout(new GridLayout(11, 4, 5, 5)); // Rows: 11; Columns: 4; Horizontal Gap: 5; Vertical Gap: 5
        // Create an empty border for the leftPanel
        // BorderFactory.createEmptyBorder() creates a border with no visible lines or insets
        // The parameters specify the size of the empty border on each side (top, left, bottom, right)
    	leftPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
        leftPanel.setBackground(new Color(0, 0, 0, 150)); // Transparent black color with alpha value 150
        
        // Components represent the left panel of the game
        CustomComponent[] components = new CustomComponent[44]; // Set the amount of components as 44

        Color[] bottomRowColors = new Color[4]; 
        bottomRowColors[0] = Color.RED;
        bottomRowColors[1] = Color.RED;
        bottomRowColors[2] = Color.GREEN;
        bottomRowColors[3] = Color.GREEN;

        // Add components to the left panel
        for (AtomicInteger i = new AtomicInteger(0); i.get() < 44; i.incrementAndGet()) { // AtomicIntegers used to ensure that the data shared across all threads are accurate
            int currentIndex = i.get(); // Get the current value of i
            
            components[currentIndex] = new CustomComponent(35); // Change the size of the right component circles
            components[currentIndex].setOpaque(false); // Remove background color

            // Check if the current component is in the bottom row
            if (currentIndex >= 40) {
                components[currentIndex].setColor(bottomRowColors[currentIndex - 40]); // Set the color of the bottom row circles
                components[currentIndex].setCanChangeColor(true); // Allow color change only if it is the bottom row
                components[currentIndex].addMouseListener(new MouseAdapter() {
                	// Override annotation provides a compile time check; used to catch errors
                    @Override
                    public void mouseClicked(MouseEvent e) { // Checks if the user clicked 'that' specific index
                        components[currentIndex].changeColorSequence(); // Call the changeColorSequence() method with the current index
                        
                        // Update the bottom row colors
                        if (currentIndex >= 40) {
                            bottomRowColors[currentIndex - 40] = components[currentIndex].getColor(); // Update the corresponding color in bottomRowColors array
                        }
                        
                        // Convert bottomRowColors array to string
                        StringBuilder colorsStringBuilder = new StringBuilder();
                        for (int i = 0; i < bottomRowColors.length; i++) {
                            colorsStringBuilder.append(convertColorToString(bottomRowColors[i]));
                            if (i < bottomRowColors.length - 1) {
                                colorsStringBuilder.append("");
                            }
                        }
                        String bottomRowColorsString = colorsStringBuilder.toString();
                        
                        playerRsd = bottomRowColorsString; // Set the player response(d) to colors of the bottom row
                    }
                });
            } else {
                components[currentIndex].setCanChangeColor(false); // Disable color change for other rows
            }

            leftPanel.add(components[currentIndex]); 
        }

        aiPanel.add(leftPanel); // Add the left panel to the AI panel

        // Create the right panel with the grid layout
        JPanel rightPanel = new JPanel();
        rightPanel.setLayout(new GridLayout(11, 4, 0, 0)); // Adjusted to have 4 columns and 10 rows
        rightPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        rightPanel.setBackground(new Color(0, 0, 0, 150)); // Transparent black color with alpha value 150

        CustomComponent[][] rightComponents = new CustomComponent[11][4]; // Adjusted to have 11 rows and 4 columns
        for (int i = 0; i < 11; i++) { // Adjusted the loop limit to 11
            for (int j = 0; j < 4; j++) {
                rightComponents[i][j] = new CustomComponent(20);
                rightComponents[i][j].setOpaque(false); // Remove background color
                rightComponents[i][j].setCanChangeColor(false); // Allow color change only if it is the bottom row
                rightPanel.add(rightComponents[i][j]);
            }
        }
        
        // Label that displays the guesses and current streak
        JLabel attemptTries = new JLabel("Guesses: "+(TRIES.get()-1)+" Streak: "+streak); 
        attemptTries.setAlignmentX(Component.CENTER_ALIGNMENT);
    	attemptTries.setForeground(Color.WHITE);
    	attemptTries.setFont(pixelFont);
        panel.add(attemptTries);
        
        // Add a "submit" button
        submitButton = new CustomButton("Submit", 50, 50); // Set the name and size of the button buttom
        
        // Generate random code
        realAns = Player.randomColorGeneration(VALID_CHARS);
        
        // Convert bottomRowColors to a String
        StringBuilder colorsStringBuilder = new StringBuilder();
        for (int i = 0; i < bottomRowColors.length; i++) {
            colorsStringBuilder.append(convertColorToString(bottomRowColors[i]));
            if (i < bottomRowColors.length - 1) {
                colorsStringBuilder.append("");
            }
        }
        String bottomRowColorsString = colorsStringBuilder.toString();
        
        playerRsd = bottomRowColorsString; // Set the String value of player response(d) to the bottom row colors
        
        submitButton.setAlignmentX(Component.CENTER_ALIGNMENT); // Center the submit button
        submitButton.addMouseListener(new MouseAdapter() {
        	// Override annotation provides a compile time check; used to catch errors
            @Override
            public void mouseClicked(MouseEvent e) {
                if (TRIES.get() < TRIES_LIMIT) { // Limits the maximum number of attempts allowed
                	
                	// Continuously obtains the colors of the bottom row which is stores in an array
                	int tryCount = TRIES.get(); 
                	if (tryCount >= 1 && tryCount <= TRIES_LIMIT) {
                	    int startIndex = (tryCount - 1) * 4; // Calculate the starting index for updating colors
                	    for (int i = 0; i < SIZE; i++) {
                	        components[startIndex + i].setColor(bottomRowColors[i]); 
                	        components[startIndex + i].repaint(); 
                	    }
                	}
                	
                    // Repaint the panel to update the UI
                    panel.revalidate();
                    panel.repaint();
                    
                    // Compare the player's guess with the real answer and store the result in an int array
                    // Calls the player class
                    int[] pegs = Player.compare(realAns, playerRsd);
                    bp = pegs[0]; // Sets the amount of black pegs to the first index of pegs obtained from the player class
                    wp = pegs[1]; // Sets the amount of white pegs to the second index of pegs obtained from the player class
                    
                    // Winning Condition
                    if (bp == SIZE) { 
                    	success.set(true);
                    	streak++;
                    	int lastStreak = streak;
                    	playerResults(TRIES, triesEXCEEDED, success, lastStreak); // Calls the playerResults() method
                    
                    // If the player doens't win on that attempt:
                    } else {
                    	
                    	// Obtains a list of hints
                        hintStringList.clear(); // Clear the list before adding new hints
                        for (int i = 0; i < bp; i++) {
                            hintStringList.add("B"); // Adds the amount of black pegs into the hintString ArrayList
                        }
                        for (int i = 0; i < wp; i++) {
                            hintStringList.add("W"); // Adds the amount of white pegs into the hintString ArrayList
                        }
                        for (int i = hintStringList.size(); i < SIZE; i++) {
                            hintStringList.add("_"); // Adds the amount of null pegs into the hintString ArrayList
                        }
                        hintStringColor.clear(); // Clear the list before adding new colors
                        for (int i = 0; i < 4; i++) {
                            hintStringColor.add(getGuessColorString(hintStringList.get(i)));  // Converts the Strings to colors and stores them in a color ArrayList
                        }
                        
                        // Adds the changed colors to the top of the frame and moves down (hence why, -1)
                        if (tryCount >= 1 && tryCount <= 10) {
                            int rowIndex = tryCount - 1;
                            for (int j = 0; j < 4; j++) {
                                rightComponents[rowIndex][j].setColor(hintStringColor.get(j));
                            }
                        }
                    }
                    
                    // Reset the peg values
                    bp = 0; 
                    wp = 0;
                    
                    // Increment the amount of tries
                    TRIES.incrementAndGet();
                    // Update the text that displays the guesses and streaks
                    attemptTries.setText("Guesses: "+(TRIES.get()-1)+" Streak: "+streak); 

                } else {
                    // AI exceeds tries (10) and fails
                    triesEXCEEDED.set(true); // Sets the tries boolean to true
                    int lastStreak = streak; // Obtain the value for streak before resetting it
                    streak = 0; // Reset the streak
                    playerResults(TRIES, triesEXCEEDED, success, lastStreak); // Calls on the playerResults() method after failure
                }
            }
        });

        aiPanel.add(rightPanel); // Add the right panel to the AI panel

        panel.add(aiPanel); // Add the AI panel to the main panel

        panel.add(Box.createVerticalStrut(7)); // Used to adjust spacing
        panel.add(submitButton); 
        panel.add(Box.createVerticalStrut(7)); // Used to adjust spacing
        
        // Create a new JButton for the "Back to Main Menu" option
        JButton backButton = new CustomButton("Back to Main Menu", 50, 50);
        backButton.setAlignmentX(Component.CENTER_ALIGNMENT); // Align the back button to the center
        backButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                showMainMenu(); // Call the showMainMenu() method when the button is clicked
            }
        });
        panel.add(backButton); 

        // Checks if the user is logged in 
        // If they are logged in, display the current user
        if (loggedIn) {
            // Create and display a Label for the current user
            JLabel currentUserLabel = new JLabel("Current User: " + currentUser);
            currentUserLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
            currentUserLabel.setForeground(Color.WHITE);
            currentUserLabel.setFont(pixelFont2);
            panel.add(currentUserLabel);
        } else {
            // Create and display a Label for the guest
            JLabel guestLabel = new JLabel("Current User: Guest");
            guestLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
            guestLabel.setForeground(Color.WHITE);
            guestLabel.setFont(pixelFont2);
            panel.add(Box.createVerticalStrut(7));
            panel.add(guestLabel);
        }

        // Repaint the panel
        panel.revalidate();
        panel.repaint();
    }

    // AI section for AI button
    private void showAISection() {
        // Remove the existing components from the panel
        panel.removeAll();
        
        AtomicBoolean error = new AtomicBoolean(false); // Create an AtomicBoolean that checks if there are errors in the code
        AtomicBoolean success = new AtomicBoolean(false); // Checks if the AI correctly cracked the code
        AtomicBoolean triesEXCEEDED = new AtomicBoolean(false); // Checks if the AI exceeded the amount of tries
        AtomicInteger TRIES = new AtomicInteger(1); // Number of tries made by the AI
        AtomicBoolean isCodeCracked = new AtomicBoolean(false); // Flag to track if the code is cracked
        remainingCodes = AI.generateAllCodes(); // List of all possible codes
        ArrayList<String> colorStringList = new ArrayList<>(Arrays.asList("_", "_", "_", "_"));  // Create a new color ArrayList that sets the default colors as null
        JButton submitButton = null; // Initialize the submitButton as null
        
        // Create and configure the AI label
        JLabel aiLabel = new JLabel("AI");
        aiLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        aiLabel.setForeground(Color.WHITE);
        aiLabel.setFont(titleFont);
        panel.add(aiLabel);

        // Add spacing between AI label and the main panel
        panel.add(Box.createVerticalStrut(15));

        // Create the main panel for the AI section
        JPanel aiPanel = new JPanel();
        aiPanel.setLayout(new GridLayout(1, 2, 11, 0));
        aiPanel.setOpaque(false);

        // Create the left panel with the grid layout
        JPanel leftPanel = new JPanel();
        leftPanel.setLayout(new GridLayout(11, 4, 5, 5));
        leftPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
        leftPanel.setBackground(new Color(0, 0, 0, 150)); // Transparent black color with alpha value 150
        CustomComponent[] components = new CustomComponent[44];

        // Set the first guess as 'RRGB'
        Color[] bottomRowColors = new Color[4];
        bottomRowColors[0] = Color.RED;
        bottomRowColors[1] = Color.RED;
        bottomRowColors[2] = Color.GREEN;
        bottomRowColors[3] = Color.BLUE;

        // Add components to the left panel
        for (int i = 0; i < 44; i++) {
        	components[i] = new CustomComponent(35); // Change the size of the right component circles
        	components[i].setOpaque(false); // Remove background color
        	components[i].setCanChangeColor(false); // Enable color change
        	leftPanel.add(components[i]);
        	
        	// Check if the current component is in the bottom row
        	if (i >= 40) {
        		components[i].setColor(bottomRowColors[i - 40]); // Set the color of the bottom row circles
        	}
        }

        aiPanel.add(leftPanel); // Add the left panel to the AI panel

        // Create the right panel with the grid layout
        JPanel rightPanel = new JPanel();
        rightPanel.setLayout(new GridLayout(11, 4, 0, 0)); // Adjusted to have 4 columns and 10 rows
        rightPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        rightPanel.setBackground(new Color(0, 0, 0, 150)); // Transparent black color with alpha value 150

        CustomComponent[][] rightComponents = new CustomComponent[11][4]; // Adjusted to have 11 rows and 4 columns
        for (int i = 0; i < 11; i++) { // Adjusted the loop limit to 11
            for (int j = 0; j < 4; j++) {
                rightComponents[i][j] = new CustomComponent(20); // Set the size of the circles
                rightComponents[i][j].setOpaque(false); // Remove background color
                if (i == 10) {
                    rightComponents[i][j].setCanChangeColor(true); // Allow color change only if it is the bottom row
                    rightComponents[i][j].addMouseListener(new MouseAdapter(){
                        @Override
                        public void mouseClicked(MouseEvent e) {
                            if (!isCodeCracked.get()) {
                                // Store the color of the bottom row
                            	colorStringList.clear(); // Clear the array before adding new colors
                                for (int k = 0; k < 4; k++) {
                                	// Get the colors of the bottom row and stores them into an array
                                    bottomRowColors[k] = rightComponents[10][k].getColor(); 
                                    String colorString = convertColorToString(bottomRowColors[k]);
                                    colorStringList.add(colorString); // Add the color to the String Array
                                }
                            }
                        }
                    });
                }
                else {
                    rightComponents[i][j].setCanChangeColor(false);
                }
                rightPanel.add(rightComponents[i][j]);
            }
        }
        
        // Create a label that displays the amount of guesses
        JLabel attemptTries = new JLabel("Guesses: "+TRIES.get());
        attemptTries.setAlignmentX(Component.CENTER_ALIGNMENT);
    	attemptTries.setForeground(Color.WHITE);
    	attemptTries.setFont(pixelFont);
        panel.add(attemptTries);
        
        // Add a "submit" button
        submitButton = new CustomButton("Submit", 50, 50);
        submitButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        submitButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (TRIES.get() < 11) {
    		        
    		        // Repaint the panel to update the UI
    		        panel.revalidate();
    		        panel.repaint();
                	
                	String guess = AI.getNextGuess(remainingCodes); // Get the AI's next guess
                	char[] guessChars = guess.toCharArray();
                	for (int i = 0; i < guessChars.length; i++) {
                        guessChars[i] = Character.toUpperCase(guessChars[i]);
                    }
                	for (int i = 0; i < 4; i++) {
                		try {
                		    if (guessChars.length > 0) {
                		        for (int j = 0; j < 4; j++) {
                		            bottomRowColors[j] = getGuessColor(guessChars[j]);
                		            components[40 + j].setColor(bottomRowColors[j]);
                		            
                		            // Visually stores the colors of the guessed colors starting from the top to bottom
                		            int index = (TRIES.get() - 1) * 4;
                		            for (int k = 0; k < 4; k++) {
                		                bottomRowColors[k] = getGuessColor(guessChars[k]);
                		                components[40 + k].setColor(bottomRowColors[k]);
                		                components[index + k].setColor(bottomRowColors[k]);
                		            }
                		        }
                		    } else {
                		    	error.set(true);
                		    	// Calls the aiResults() method that displays an error message when there are no possible solutions to the hints provided
                		    	aiResults(error, success, isCodeCracked, TRIES); 
                		        
                		        // Repaint the panel to update the UI
                		        panel.revalidate();
                		        panel.repaint();
                		    }
                		} catch (Exception ex) {
                		    // Handle any other exceptions that might occur
                		    System.out.println("An error occurred: " + ex.getMessage());
                		}
                    }

                    AtomicInteger blackPegs = new AtomicInteger(0);
                    AtomicInteger whitePegs = new AtomicInteger(0);
                    for (int i = 0; i < 4; i++) {
                        if (colorStringList.get(i).contains("B")) {
                            blackPegs.incrementAndGet(); 
                        } else if (colorStringList.get(i).contains("W")) {
                            whitePegs.incrementAndGet();
                        }
                    }
                    
                    // Store the colors of black pegs
                    for (int i = 0; i < blackPegs.get(); i++) {
                        rightComponents[TRIES.get() - 1][i].setColor(Color.BLACK);
                    }

                    // Store the colors of white pegs
                    for (int i = 0; i < whitePegs.get(); i++) {
                        rightComponents[TRIES.get() - 1][blackPegs.get() + i].setColor(Color.WHITE);
                    }

                    AtomicInteger feedback[] = { blackPegs, whitePegs };

                    // Check if the code is cracked
                    if (feedback[0].get() == AI.SIZE) {
                    	// Handle the case when guessChars is empty
        		    	// Create and configure the AI label
                    	success.set(true);
                    	aiResults(error, success, isCodeCracked, TRIES);
                        isCodeCracked.set(true); // The code is cracked, set the flag to true
                    } else {
                        remainingCodes = AI.filterCodes(remainingCodes, guess, feedback); // Filter the remaining codes based on the feedback
                        guess = AI.getNextGuess(remainingCodes); // Get the AI's next guess
                        guessChars = guess.toCharArray();
                    	for (int i = 0; i < guessChars.length; i++) {
                            guessChars[i] = Character.toUpperCase(guessChars[i]);
                        }
                    	for (int i = 0; i < 4; i++) {
                    		try {
                    		    if (guessChars.length > 0) {
                    		        for (int j = 0; j < 4; j++) {
                    		            bottomRowColors[j] = getGuessColor(guessChars[j]);
                    		            components[40 + j].setColor(bottomRowColors[j]);
                    		        }
                    		    } else {
                    		    	// Handle the case when guessChars is empty
                    		    	// Create and configure the AI label
                    		    	error.set(true);
                    		    	aiResults(error, success, isCodeCracked, whitePegs);
                    		        
                    		        // Repaint the panel to update the UI
                    		        panel.revalidate();
                    		        panel.repaint();
                    		    }
                    		} catch (Exception ex) {
                    		    // Handle any other exceptions that might occur
                    		    System.out.println("An error occurred: " + ex.getMessage());
                    		}
                        }
                    }
                    TRIES.incrementAndGet();
                    attemptTries.setText("Guesses: " + TRIES.get());

                } else {
                    // AI exceeds tries (10) and fails
                    triesEXCEEDED.set(true);
                    aiResults(success, error, triesEXCEEDED, TRIES);
                }
            }
        });

        aiPanel.add(rightPanel); // Add the right panel to the AI panel

        panel.add(aiPanel); // Add the AI panel to the main panel

        // Add spacing between the AI panel and the "Back" button
        panel.add(Box.createVerticalStrut(7));
        panel.add(submitButton);
        panel.add(Box.createVerticalStrut(7));
        
        // Add a "Back" button
        JButton backButton = new CustomButton("Back to Main Menu", 50, 50);
        backButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        backButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                showMainMenu();
            }
        });
        panel.add(backButton);

        if (loggedIn) {
            // Create and display a Label for the current user
            JLabel currentUserLabel = new JLabel("Current User: " + currentUser);
            currentUserLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
            currentUserLabel.setForeground(Color.WHITE);
            currentUserLabel.setFont(pixelFont2);
            panel.add(currentUserLabel);
        } else {
            // Create and display a Label for the current user
            JLabel guestLabel = new JLabel("Current User: Guest");
            guestLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
            guestLabel.setForeground(Color.WHITE);
            guestLabel.setFont(pixelFont2);
            panel.add(Box.createVerticalStrut(7));
            panel.add(guestLabel);
        }

        // Repaint the panel
        panel.revalidate();
        panel.repaint();
    }
    
    // New Mode Section 
    private void newModeSection() {
        // Remove the existing components from the panel
        panel.removeAll();
        
        AtomicBoolean error = new AtomicBoolean(false); // Create an AtomicBoolean that checks if there are errors in the code
        AtomicBoolean success = new AtomicBoolean(false); // Checks if the AI correctly cracked the code
        AtomicBoolean triesEXCEEDED = new AtomicBoolean(false); // Checks if the AI exceeded the amount of tries
        AtomicInteger TRIES = new AtomicInteger(1); // Number of tries made by the AI
        AtomicBoolean isCodeCracked = new AtomicBoolean(false); // Flag to track if the code is cracked
        remainingCodes = AI.generateAllCodes(); // List of all possible codes
        ArrayList<String> colorStringList = new ArrayList<>(Arrays.asList("_", "_", "_", "_"));  // Create a new color ArrayList that sets the default colors as null
        JButton submitButton = null; // Initialize the submitButton as null
        ArrayList<Color> charRealGuessColors = new ArrayList<>(); // Create an array list to hold randomly generated code
        char[] charColors = new char[SIZE];
        // Create and configure the AI label
        JLabel competativeLabel = new JLabel("Competative AI");
        competativeLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        competativeLabel.setForeground(Color.WHITE);
        competativeLabel.setFont(titleFont);
        panel.add(competativeLabel);

        // Add spacing between AI label and the main panel
        panel.add(Box.createVerticalStrut(15));
        
        // Create the main panel for the AI section
        JPanel aiPanel = new JPanel();
        aiPanel.setLayout(new GridLayout(1, 2, 11, 0));
        aiPanel.setOpaque(false);

        // Create the left panel with the grid layout
        JPanel leftPanel = new JPanel();
        leftPanel.setLayout(new GridLayout(11, 4, 5, 5));
        leftPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
        leftPanel.setBackground(new Color(0, 0, 0, 150)); // Transparent black color with alpha value 150
        CustomComponent[] components = new CustomComponent[44];
        
        // Generate random code
        realAns = Player.randomColorGeneration(VALID_CHARS);
        
        // Store every character of realAns into a character array
        for (int i = 0; i < SIZE; i++) {
            charColors[i] = realAns.charAt(i);
        }
        
        // Store the converted character array into a color array
        for (int i = 0; i < SIZE; i++) {
        	charRealGuessColors.add(getGuessColor(realAns.charAt(i)));
        }
        
     	// Set code to match under the first guess
        Color[] codeToMatch = new Color[4];
        for (int i = 0; i < realAns.length(); i++) {
        	codeToMatch[i] = charRealGuessColors.get(i);
        }

        
        // Set the first guess as 'RRGB'
        Color[] bottomRowColors = new Color[4];
        bottomRowColors[0] = Color.RED;
        bottomRowColors[1] = Color.RED;
        bottomRowColors[2] = Color.GREEN;
        bottomRowColors[3] = Color.BLUE;

        // Add components to the left panel
        for (int i = 0; i < 44; i++) {
        	components[i] = new CustomComponent(35); // Change the size of the right component circles
        	components[i].setOpaque(false); // Remove background color
        	components[i].setCanChangeColor(false); // Enable color change
        	leftPanel.add(components[i]);
        	
        	// Check if the current component is in the bottom row
        	if (i >= 40) {
        		components[i].setColor(bottomRowColors[i - 40]); // Set the color of the bottom row circles
        	}
        }
        
        // Add codeToMatch under the bottom rows
        for (int i = 0; i < 4; i++) {
            components[36 + i].setColor(codeToMatch[i]);
        }

        aiPanel.add(leftPanel); // Add the left panel to the AI panel

        // Create the right panel with the grid layout
        JPanel rightPanel = new JPanel();
        rightPanel.setLayout(new GridLayout(11, 4, 0, 0)); // Adjusted to have 4 columns and 10 rows
        rightPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        rightPanel.setBackground(new Color(0, 0, 0, 150)); // Transparent black color with alpha value 150

        CustomComponent[][] rightComponents = new CustomComponent[11][4]; // Adjusted to have 11 rows and 4 columns
        for (int i = 0; i < 11; i++) { // Adjusted the loop limit to 11
            for (int j = 0; j < 4; j++) {
                rightComponents[i][j] = new CustomComponent(20); // Set the size of the circles
                rightComponents[i][j].setOpaque(false); // Remove background color
                if (i == 10) {
                    rightComponents[i][j].setCanChangeColor(true); // Allow color change only if it is the bottom row
                    rightComponents[i][j].addMouseListener(new MouseAdapter(){
                        @Override
                        public void mouseClicked(MouseEvent e) {
                            if (!isCodeCracked.get()) {
                                // Store the color of the bottom row
                            	colorStringList.clear(); // Clear the array before adding new colors
                                for (int k = 0; k < 4; k++) {
                                	// Get the colors of the bottom row and stores them into an array
                                    bottomRowColors[k] = rightComponents[10][k].getColor(); 
                                    String colorString = convertColorToString(bottomRowColors[k]);
                                    colorStringList.add(colorString); // Add the color to the String Array
                                }
                            }
                        }
                    });
                }
                else {
                    rightComponents[i][j].setCanChangeColor(false);
                }
                rightPanel.add(rightComponents[i][j]);
            }
        }
        
        // Create a label that displays the amount of guesses
        JLabel attemptTries = new JLabel("Guesses: "+TRIES.get());
        attemptTries.setAlignmentX(Component.CENTER_ALIGNMENT);
    	attemptTries.setForeground(Color.WHITE);
    	attemptTries.setFont(pixelFont);
        panel.add(attemptTries);
        
    	// Create a label for the timer
        JLabel timerLabel = new JLabel("Timer: 00:00:000");
        timerLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        timerLabel.setForeground(Color.WHITE);
        timerLabel.setFont(pixelFont);
        panel.add(timerLabel);
        
        // Add a "submit" button
        submitButton = new CustomButton("Submit", 50, 50);
        submitButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        submitButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (TRIES.get() < 11) {

                	if (!isTimerRunning) { // Check if the timer is not already running
                        // Start the timer on the first guess
                        startTime = System.currentTimeMillis();
                        timer = new Timer(1000, new ActionListener() {
                            @Override
                            public void actionPerformed(ActionEvent e) {
                                long elapsedTime = System.currentTimeMillis() - startTime;
                                totalElapsedTime.getAndSet(elapsedTime);
                                long minutes = TimeUnit.MILLISECONDS.toMinutes(elapsedTime);
                                long seconds = TimeUnit.MILLISECONDS.toSeconds(elapsedTime) % 60;
                                long milliseconds = elapsedTime % 1000; // Calculate milliseconds
                                String timeString = String.format("Timer: %02d:%02d:%03d", minutes, seconds, milliseconds);
                                timerLabel.setText(timeString);
                                // Update the timer display (e.g., label) with the elapsed time
                                // Here you can convert milliseconds to minutes:seconds format
                            }
                        });
                        timer.start(); // Start the timer
                        
                        // Set the isTimerRunning boolean to true
                        isTimerRunning = true;
                    }
                	
    		        // Repaint the panel to update the UI
    		        panel.revalidate();
    		        panel.repaint();
                	
                	String guess = AI.getNextGuess(remainingCodes); // Get the AI's next guess
                	char[] guessChars = guess.toCharArray();
                	for (int i = 0; i < guessChars.length; i++) {
                        guessChars[i] = Character.toUpperCase(guessChars[i]);
                    }
                	for (int i = 0; i < 4; i++) {
                		try {
                		    if (guessChars.length > 0) {
                		        for (int j = 0; j < 4; j++) {
                		            bottomRowColors[j] = getGuessColor(guessChars[j]);
                		            components[40 + j].setColor(bottomRowColors[j]);
                		            
                		            // Visually stores the colors of the guessed colors starting from the top to bottom
                		            int index = (TRIES.get() - 1) * 4;
                		            for (int k = 0; k < 4; k++) {
                		                bottomRowColors[k] = getGuessColor(guessChars[k]);
                		                components[40 + k].setColor(bottomRowColors[k]);
                		                components[index + k].setColor(bottomRowColors[k]);
                		            }
                		        }
                		    } else {
                		    	error.set(true);
                		    	// Stop the timer
                		    	timer.stop();
                		    	isTimerRunning = false;
                		    	
                		    	// Calls the aiResults() method that displays an error message when there are no possible solutions to the hints provided
                		    	Boolean validWin = true;
                		    	newModeResults(error, success, isCodeCracked, TRIES, validWin); 
                		        
                		        // Repaint the panel to update the UI
                		        panel.revalidate();
                		        panel.repaint();
                		    }
                		} catch (Exception ex) {
                		    // Handle any other exceptions that might occur
                		    System.out.println("An error occurred: " + ex.getMessage());
                		}
                    }

                    AtomicInteger blackPegs = new AtomicInteger(0);
                    AtomicInteger whitePegs = new AtomicInteger(0);
                    for (int i = 0; i < 4; i++) {
                        if (colorStringList.get(i).contains("B")) {
                            blackPegs.incrementAndGet(); 
                        } else if (colorStringList.get(i).contains("W")) {
                            whitePegs.incrementAndGet();
                        }
                    }
                    
                    // Store the colors of black pegs
                    for (int i = 0; i < blackPegs.get(); i++) {
                        rightComponents[TRIES.get() - 1][i].setColor(Color.BLACK);
                    }

                    // Store the colors of white pegs
                    for (int i = 0; i < whitePegs.get(); i++) {
                        rightComponents[TRIES.get() - 1][blackPegs.get() + i].setColor(Color.WHITE);
                    }

                    AtomicInteger feedback[] = { blackPegs, whitePegs };

                    // Check if the code is cracked
                    if (feedback[0].get() == AI.SIZE) {
                    	
                    	// Stop the timer
        		    	timer.stop();
        		    	isTimerRunning = false;
                    	// Handle the case when guessChars is empty
        		    	// Create and configure the AI label
                    	success.set(true);
                    	boolean validWin = true; // Checks if the user won the new mode correctly

                    	for (int i = 0; i < SIZE; i++) {
                    	    if (bottomRowColors[i] != charRealGuessColors.get(i)) {
                    	        validWin = false;
                    	        break;
                    	    }
                    	}
                    	
                    	newModeResults(error, success, isCodeCracked, TRIES, validWin);
                        isCodeCracked.set(true); // The code is cracked, set the flag to true
                    } else {
                        remainingCodes = AI.filterCodes(remainingCodes, guess, feedback); // Filter the remaining codes based on the feedback
                        guess = AI.getNextGuess(remainingCodes); // Get the AI's next guess
                        guessChars = guess.toCharArray();
                    	for (int i = 0; i < guessChars.length; i++) {
                            guessChars[i] = Character.toUpperCase(guessChars[i]);
                        }
                    	for (int i = 0; i < 4; i++) {
                    		try {
                    		    if (guessChars.length > 0) {
                    		        for (int j = 0; j < 4; j++) {
                    		            bottomRowColors[j] = getGuessColor(guessChars[j]);
                    		            components[40 + j].setColor(bottomRowColors[j]);
                    		        }
                    		    } else {
                    		    	//Stop the Timer
                    		    	timer.stop();
                    		    	isTimerRunning = false;
                    		    	// Handle the case when guessChars is empty
                    		    	// Create and configure the AI label
                    		    	error.set(true);
                    		    	Boolean validWin = true;
                    		    	newModeResults(error, success, isCodeCracked, TRIES, validWin); 
                    		        
                    		        // Repaint the panel to update the UI
                    		        panel.revalidate();
                    		        panel.repaint();
                    		    }
                    		} catch (Exception ex) {
                    		    // Handle any other exceptions that might occur
                    		    System.out.println("An error occurred: " + ex.getMessage());
                    		}
                        }
                    }
                    TRIES.incrementAndGet();
                    attemptTries.setText("Guesses: " + TRIES.get());

                } else {
                	// Stop the timer
    		    	timer.stop();
    		    	isTimerRunning = false;
                    // AI exceeds tries (10) and fails
    		    	Boolean validWin = true;
    		    	newModeResults(error, success, isCodeCracked, TRIES, validWin); 
                }
            }
        });

        aiPanel.add(rightPanel); // Add the right panel to the AI panel

        panel.add(aiPanel); // Add the AI panel to the main panel

        // Add spacing between the AI panel and the "Back" button
        panel.add(Box.createVerticalStrut(7));
        panel.add(submitButton);
        panel.add(Box.createVerticalStrut(7));
        
        // Add a "Back" button
        JButton backButton = new CustomButton("Back to Main Menu", 50, 50);
        backButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        backButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
            	showMainMenu();
            }
        });
        panel.add(backButton);

        if (loggedIn) {
            // Create and display a Label for the current user
            JLabel currentUserLabel = new JLabel("Current User: " + currentUser);
            currentUserLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
            currentUserLabel.setForeground(Color.WHITE);
            currentUserLabel.setFont(pixelFont2);
            panel.add(currentUserLabel);
        } else {
            // Create and display a Label for the current user
            JLabel guestLabel = new JLabel("Current User: Guest");
            guestLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
            guestLabel.setForeground(Color.WHITE);
            guestLabel.setFont(pixelFont2);
            panel.add(Box.createVerticalStrut(7));
            panel.add(guestLabel);
        }

        // Repaint the panel
        panel.revalidate();
        panel.repaint();
    }
    
    // Converts the characters into valid colors
    private Color getGuessColor(char guessChar) {
        switch (guessChar) {
            case 'R':
                return Color.RED;
            case 'G':
                return Color.GREEN;
            case 'B':
                return Color.BLUE;
            case 'Y':
                return Color.YELLOW;
            case 'O':
                return Color.ORANGE;
            case 'P':
                return Color.PINK;
            case 'K':
                return Color.BLACK;
            case 'W':
                return Color.WHITE;
            case '_':
                return Color.LIGHT_GRAY;
            default:
                return null;
        }
    }
    
    // Converts the characters to hints 'B, W, and HINTS'
    private Color getGuessColorString(String guessString) {
        switch (guessString) {
            case "_":
                return Color.LIGHT_GRAY;
            case "B":
                return Color.BLACK;
            case "W":
                return Color.WHITE;
            default:
                return null;
        }
    }

    // Converts the Strings into colors
    private String convertColorToString(Color color) {
        if (color.equals(Color.RED)) {
            return "R";
        } else if (color.equals(Color.GREEN)) {
            return "G";
        } else if (color.equals(Color.BLUE)) {
            return "B";
        } else if (color.equals(Color.YELLOW)) {
            return "Y";
        } else if (color.equals(Color.ORANGE)) {
            return "O";
        } else if (color.equals(Color.PINK)) {
            return "P";
        } else if (color.equals(Color.BLACK)) {
            return "B";
        } else if (color.equals(Color.WHITE)) {
            return "W";
        } else if (color.equals(Color.LIGHT_GRAY)) {
            return "_";
        } else {
            return "K";
        }
    }
    
    // Register Menu 
    private void showLoginMenu() {
        // Remove the existing components from the panel
        panel.removeAll();

        // Create a vertical Box layout for the main panel
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        // Create a panel for the AI label
        JPanel aiPanel = new JPanel();
        aiPanel.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Set the panel as non-opaque
        aiPanel.setOpaque(false);

        // Create and configure the AI label
        JLabel aiLabel = new JLabel("LOGIN");
        aiLabel.setForeground(Color.WHITE);
        aiLabel.setFont(titleFont);

        // Add the AI label to the AI panel
        aiPanel.add(aiLabel);

        // Add spacing
        panel.add(Box.createVerticalStrut(15));

        // Add the AI panel to the main panel
        panel.add(aiPanel);

        // Create a panel for the labels and text fields
        JPanel loginPanel = new JPanel();
        loginPanel.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Set the panel as non-opaque
        loginPanel.setOpaque(false);

        // Set the background color with transparency
        Color transparentBlack = new Color(0, 0, 0, 200); // Adjust the alpha value as needed
        loginPanel.setBackground(transparentBlack);
        loginPanel.setLayout(new BoxLayout(loginPanel, BoxLayout.Y_AXIS));  // Use BoxLayout for vertical alignment

        // Create and configure the Username label and text field
        JLabel userNameLabel = new JLabel();
        userNameLabel.setForeground(Color.WHITE);
        userNameLabel.setHorizontalAlignment(SwingConstants.CENTER);
        userNameLabel.setFont(pixelFont);
        CustomTextField usernameField = new CustomTextField(13);
        usernameField.setAlignmentX(Component.CENTER_ALIGNMENT);
        setPlaceholderText(usernameField, "Enter username");

        // Create a panel to contain the username field
        JPanel usernamePanel = new JPanel();
        usernamePanel.setLayout(new FlowLayout(FlowLayout.CENTER));
        usernamePanel.setOpaque(false);
        usernamePanel.add(usernameField);

        // Set the preferred size of the username panel
        usernamePanel.setPreferredSize(new Dimension(150, 30)); // Adjust the size as needed

        // Create and configure the Password label and text field
        JLabel passWordLabel = new JLabel();
        passWordLabel.setForeground(Color.WHITE);
        passWordLabel.setHorizontalAlignment(SwingConstants.CENTER);
        passWordLabel.setFont(pixelFont);
        CustomTextField passwordField = new CustomTextField(13);
        passwordField.setAlignmentX(Component.CENTER_ALIGNMENT);
        setPlaceholderText(passwordField, "Enter password");

        // Create a panel to contain the password field
        JPanel passwordPanel = new JPanel();
        passwordPanel.setLayout(new FlowLayout(FlowLayout.CENTER));
        passwordPanel.setOpaque(false);
        passwordPanel.add(passwordField);

        // Set the preferred size of the password panel
        passwordPanel.setPreferredSize(new Dimension(150, 30)); // Adjust the size as needed

        // Add the labels and text fields to the login panel
        loginPanel.add(userNameLabel);
        loginPanel.add(usernamePanel);
        loginPanel.add(passWordLabel);
        loginPanel.add(passwordPanel);

        // Add spacing
        loginPanel.add(Box.createVerticalStrut(10));

        // Create a panel for the error message
        JPanel errorPanel = new JPanel();
        errorPanel.setOpaque(false);

        // Create the error label with initial visibility set to false
        JLabel errorLabel = new JLabel("LOG-IN FAILED");
        errorLabel.setFont(pixelFont);
        errorLabel.setForeground(Color.RED);
        errorLabel.setVisible(false);

        // Add the error label to the error panel
        errorPanel.add(errorLabel);

        // Add the error panel to the login panel
        loginPanel.add(errorPanel);

        // Add spacing
        loginPanel.add(Box.createVerticalStrut(10));

        // Add a "Login" button
        JButton loginButton = new CustomButton("Login", 50, 50);
        loginButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        ((CustomButton) loginButton).setOpacity(0.5f);
        loginButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // Get the entered username and password
                String enteredUsername = usernameField.getText();
                String enteredPassword = passwordField.getText();

                // Check if the username and password exist in the loginRecords file
                if (enteredUsername.length() == 0 || enteredPassword.length() == 0) {
                    // Invalid! Please enter a username and password
                    errorLabel.setVisible(true);  // Show the error label

                } else if (checkCredentials(enteredUsername, enteredPassword)) {
                    // Login successful
                    showMainMenu();
                } else {
                    // Login failed
                	errorLabel.setVisible(true);  // Show the error label
                }
            }
        });

        // Add the login panel to the main panel
        panel.add(loginPanel);

        // Add the login button to the main panel
        panel.add(loginButton);

        // Add spacing
        panel.add(Box.createVerticalStrut(15));

        // Update the UI
        panel.revalidate();
        panel.repaint();
        
        JButton registerButton = new CustomButton("Register", 50, 50);
        registerButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        registerButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                showRegisterMenu();
            }
        });

        // Add the "Login" and "register" button to the main panel
        panel.add(loginButton);
        panel.add(Box.createVerticalStrut(270));
        panel.add(registerButton);

        // Add spacing
        panel.add(Box.createVerticalStrut(15));

        // Add a "Back" button
        JButton backButton = new CustomButton("Back to Login Menu", 50, 50);
        backButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        backButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                loginMenu();
            }
        });

        // Add the "Back" button to the main panel
        panel.add(backButton);

        // Repaint the panel
        panel.revalidate();
        panel.repaint();
    }
    
    // Login and Password textfield placeholders
    private void setPlaceholderText(JTextComponent textComponent, String placeholder) {
        // Set initial placeholder text and color
        textComponent.setForeground(Color.GRAY);
        textComponent.setText(placeholder);

        // Add a FocusListener to handle focus events
        textComponent.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {
                // When the component gains focus, clear the placeholder text if present
                if (textComponent.getText().equals(placeholder)) {
                    textComponent.setText("");
                    textComponent.setForeground(Color.BLACK);
                }
            }

            @Override
            public void focusLost(FocusEvent e) {
                // When the component loses focus and is empty, restore the placeholder text
                if (textComponent.getText().isEmpty()) {
                    textComponent.setForeground(Color.GRAY);
                    textComponent.setText(placeholder);
                }
            }
        });
    }

    // Settings Menu
    private void showSettingsMenu() {
        // Remove the existing components from the panel
        panel.removeAll();

        // Add spacing
        panel.add(Box.createVerticalStrut(15));

        // Create and configure the AI label
        JLabel aiLabel = new JLabel("SETTINGS");
        aiLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        aiLabel.setForeground(Color.WHITE);
        aiLabel.setFont(titleFont);
        panel.add(aiLabel);

        panel.add(Box.createVerticalStrut(30));
        JButton changeBackgroundButton1 = new CustomButton("River Cyberpunk", 50, 50);
        changeBackgroundButton1.setAlignmentX(Component.CENTER_ALIGNMENT);
        changeBackgroundButton1.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // Call a method to change the background
            	JPanel contentPanel = new JPanel() {
            	    @Override
            	    protected void paintComponent(Graphics g) {
            	        super.paintComponent(g);
            	        // Paint the GIF image as the background
            	        g.drawImage(new ImageIcon("background.gif").getImage(), 0, 0, getWidth(), getHeight(), this);
            	    }
            	};
            	contentPanel.setLayout(new BorderLayout());
                contentPanel.setOpaque(false); // Set panel background to transparent

                // Add vertical strut to shift components up
                contentPanel.add(Box.createVerticalStrut(50), BorderLayout.NORTH);

                // Add the panel to the content panel
                contentPanel.add(panel, BorderLayout.CENTER);

                // Add vertical glue to push the panel to the top and center horizontally
                contentPanel.add(Box.createVerticalGlue(), BorderLayout.SOUTH);

                // Add vertical strut to shift components up
                contentPanel.add(Box.createVerticalStrut(50), BorderLayout.SOUTH);
            	
            	window.setContentPane(contentPanel);
            	window.validate(); // This triggers a layout update
            	window.repaint();  // This triggers a repaint of the frame
            }
        });
        panel.add(Box.createVerticalStrut(7));
        panel.add(changeBackgroundButton1);
        
        JButton changeBackgroundButton2 = new CustomButton("Cherry Blossom", 50, 50);
        changeBackgroundButton2.setAlignmentX(Component.CENTER_ALIGNMENT);
        changeBackgroundButton2.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // Call a method to change the background
            	JPanel contentPanel = new JPanel() {
            	    @Override
            	    protected void paintComponent(Graphics g) {
            	        super.paintComponent(g);
            	        // Paint the GIF image as the background
            	        g.drawImage(new ImageIcon("background2.gif").getImage(), 0, 0, getWidth(), getHeight(), this);
            	    }
            	};
            	contentPanel.setLayout(new BorderLayout());
                contentPanel.setOpaque(false); // Set panel background to transparent

                // Add vertical strut to shift components up
                contentPanel.add(Box.createVerticalStrut(50), BorderLayout.NORTH);

                // Add the panel to the content panel
                contentPanel.add(panel, BorderLayout.CENTER);

                // Add vertical glue to push the panel to the top and center horizontally
                contentPanel.add(Box.createVerticalGlue(), BorderLayout.SOUTH);

                // Add vertical strut to shift components up
                contentPanel.add(Box.createVerticalStrut(50), BorderLayout.SOUTH);
            	
            	window.setContentPane(contentPanel);
            	window.validate(); // This triggers a layout update
            	window.repaint();  // This triggers a repaint of the frame
            }
        });
        panel.add(Box.createVerticalStrut(7));
        panel.add(changeBackgroundButton2);
        
        // Add a "Back" button
        JButton backButton = new CustomButton("Back to Main Menu", 50, 50);
        panel.add(Box.createVerticalStrut(225)); // Adds gap to push main menu button to the bottom
        backButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        backButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
            	showMainMenu();
            }
        });
        panel.add(Box.createVerticalStrut(15));
        panel.add(backButton);
        
        if(loggedIn) {
	        // Create and display a Label for current user
	        JLabel currentUserLabel = new JLabel("Current User: "+currentUser);
	        currentUserLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
	        currentUserLabel.setForeground(Color.WHITE);
	        currentUserLabel.setFont(pixelFont2);
	        panel.add(currentUserLabel);
        } else {
        	// Create and display the guess user
	        JLabel guestLabel = new JLabel("Current User: Guest");
	        guestLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
	        guestLabel.setForeground(Color.WHITE);
	        guestLabel.setFont(pixelFont2);
	        panel.add(Box.createVerticalStrut(7));
	        panel.add(guestLabel);
        }

        // Repaint the panel
        panel.revalidate();
        panel.repaint();
    }
    
    // The Settings Menu for the Login menu 
    private void showSettingsMenuLogin() {
        // Remove the existing components from the panel
        panel.removeAll();

        // Add spacing
        panel.add(Box.createVerticalStrut(15));

        // Create and configure the AI label
        JLabel settingsLabel = new JLabel("SETTINGS");
        settingsLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        settingsLabel.setForeground(Color.WHITE);
        settingsLabel.setFont(titleFont);
        panel.add(settingsLabel);
        
        panel.add(Box.createVerticalStrut(30));
        JButton changeBackgroundButton1 = new CustomButton("River Cyberpunk", 50, 50);
        changeBackgroundButton1.setAlignmentX(Component.CENTER_ALIGNMENT);
        changeBackgroundButton1.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // Call a method to change the background
            	JPanel contentPanel = new JPanel() {
            	    @Override
            	    protected void paintComponent(Graphics g) {
            	        super.paintComponent(g);
            	        // Paint the GIF image as the background
            	        g.drawImage(new ImageIcon("background.gif").getImage(), 0, 0, getWidth(), getHeight(), this);
            	    }
            	};
            	contentPanel.setLayout(new BorderLayout());
                contentPanel.setOpaque(false); // Set panel background to transparent

                // Add vertical strut to shift components up
                contentPanel.add(Box.createVerticalStrut(50), BorderLayout.NORTH);

                // Add the panel to the content panel
                contentPanel.add(panel, BorderLayout.CENTER);

                // Add vertical glue to push the panel to the top and center horizontally
                contentPanel.add(Box.createVerticalGlue(), BorderLayout.SOUTH);

                // Add vertical strut to shift components up
                contentPanel.add(Box.createVerticalStrut(50), BorderLayout.SOUTH);
            	
            	window.setContentPane(contentPanel);
            	window.validate(); // This triggers a layout update
            	window.repaint();  // This triggers a repaint of the frame
            }
        });
        panel.add(Box.createVerticalStrut(7));
        panel.add(changeBackgroundButton1);
        
        JButton changeBackgroundButton2 = new CustomButton("Cherry Blossom", 50, 50);
        changeBackgroundButton2.setAlignmentX(Component.CENTER_ALIGNMENT);
        changeBackgroundButton2.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // Call a method to change the background
            	JPanel contentPanel = new JPanel() {
            	    @Override
            	    protected void paintComponent(Graphics g) {
            	        super.paintComponent(g);
            	        // Paint the GIF image as the background
            	        g.drawImage(new ImageIcon("background2.gif").getImage(), 0, 0, getWidth(), getHeight(), this);
            	    }
            	};
            	contentPanel.setLayout(new BorderLayout());
                contentPanel.setOpaque(false); // Set panel background to transparent

                // Add vertical strut to shift components up
                contentPanel.add(Box.createVerticalStrut(50), BorderLayout.NORTH);

                // Add the panel to the content panel
                contentPanel.add(panel, BorderLayout.CENTER);

                // Add vertical glue to push the panel to the top and center horizontally
                contentPanel.add(Box.createVerticalGlue(), BorderLayout.SOUTH);

                // Add vertical strut to shift components up
                contentPanel.add(Box.createVerticalStrut(50), BorderLayout.SOUTH);
            	
            	window.setContentPane(contentPanel);
            	window.validate(); // This triggers a layout update
            	window.repaint();  // This triggers a repaint of the frame
            }
        });
        panel.add(Box.createVerticalStrut(7));
        panel.add(changeBackgroundButton2);

        // Add a "Back" button
        JButton backButton = new CustomButton("Back to Login Menu", 50, 50);
        panel.add(Box.createVerticalStrut(225)); // Adds gap to push main menu button to the bottom
        backButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        backButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
            	loginMenu();
            }
        });
        panel.add(Box.createVerticalStrut(15));
        panel.add(backButton);

        // Create and display a Label for current user
        JLabel guestLabel = new JLabel("Current User: UKNOWN");
        guestLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        guestLabel.setForeground(Color.WHITE);
        guestLabel.setFont(pixelFont2);
        panel.add(Box.createVerticalStrut(7));
        panel.add(guestLabel);

        // Repaint the panel
        panel.revalidate();
        panel.repaint();
    }
    
    // Show the results for the AI mode
    private void aiResults(AtomicBoolean error, AtomicBoolean success, AtomicBoolean triesEXCEEDED, AtomicInteger TRIES) {
        // Remove the existing components from the panel
        panel.removeAll();

        // Add spacing
        panel.add(Box.createVerticalStrut(15));
        
        boolean booleanError = error.get();
        boolean successError = success.get();
        boolean exceededTries = triesEXCEEDED.get();
        
        // Create and configure the AI label
        JLabel aiLabel = new JLabel("RESULTS");
        aiLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        aiLabel.setForeground(Color.WHITE);
        aiLabel.setFont(titleFont);
        panel.add(aiLabel);
        
        if (booleanError) {
        	// Show Results
        	panel.add(Box.createVerticalStrut(200));
	        JLabel errorEmpty = new JLabel("An Error has Occured!");
	        errorEmpty.setAlignmentX(Component.CENTER_ALIGNMENT);
	        errorEmpty.setForeground(Color.RED);
	        errorEmpty.setFont(titleFont);
	        panel.add(errorEmpty);
        }
        else if (successError){
	        // Show Results
	        panel.add(Box.createVerticalStrut(200));
	        JLabel aiCrackedCodeLabel = new JLabel("AI cracked your Code!");
	        aiCrackedCodeLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
	        aiCrackedCodeLabel.setForeground(Color.GREEN);
	        aiCrackedCodeLabel.setFont(titleFont);
	        panel.add(aiCrackedCodeLabel);
	        
	        JLabel attemptTries = new JLabel("Guesses: "+TRIES.get());
	        attemptTries.setAlignmentX(Component.CENTER_ALIGNMENT);
	    	attemptTries.setForeground(Color.green);
	    	attemptTries.setFont(pixelFont3);
	        panel.add(attemptTries);
        }
        else if (exceededTries) {
        	// Show Results
	        panel.add(Box.createVerticalStrut(200));
	        JLabel aiFailed = new JLabel("The AI was unable to crack your code");
	        aiFailed.setAlignmentX(Component.CENTER_ALIGNMENT);
	        aiFailed.setForeground(Color.WHITE);
	        aiFailed.setFont(titleFont);
	        panel.add(aiFailed);
        }
        
        // Repaint the panel to update the UI
        panel.revalidate();
        panel.repaint();
        
        // Add a "Continue" button
        JButton continueButton = new CustomButton("Continue?", 50, 50);
        panel.add(Box.createVerticalStrut(7)); // Adds gap to push main menu button to the bottom
        continueButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        continueButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
            	showAISection();
            }
        });
        
        // Add a "Back" button
        JButton backButton = new CustomButton("Back to Main Menu", 50, 50);
        panel.add(Box.createVerticalStrut(7)); // Adds gap to push main menu button to the bottom
        backButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        backButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
            	showMainMenu();
            }
        });
        panel.add(Box.createVerticalStrut(225));
        panel.add(continueButton);
        panel.add(Box.createVerticalStrut(7));
        panel.add(backButton);
        
        if(loggedIn) {
	        // Create and display a Label for current user
	        JLabel currentUserLabel = new JLabel("Current User: "+currentUser);
	        currentUserLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
	        currentUserLabel.setForeground(Color.WHITE);
	        currentUserLabel.setFont(pixelFont2);
	        panel.add(currentUserLabel);
        } else {
        	// Create and display a Label for current user
	        JLabel guestLabel = new JLabel("Current User: Guest");
	        guestLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
	        guestLabel.setForeground(Color.WHITE);
	        guestLabel.setFont(pixelFont2);
	        panel.add(Box.createVerticalStrut(7));
	        panel.add(guestLabel);
        }

        // Repaint the panel
        panel.revalidate();
        panel.repaint();
    }
    
    // Show the results for the new mode
    private void newModeResults(AtomicBoolean error, AtomicBoolean success, AtomicBoolean triesEXCEEDED, AtomicInteger TRIES, boolean validWin) {
        // Remove the existing components from the panel
        panel.removeAll();

        // Add spacing
        panel.add(Box.createVerticalStrut(15));
        
        boolean booleanError = error.get();
        boolean successError = success.get();
        boolean exceededTries = triesEXCEEDED.get();
        
        // Create and configure the AI label
        JLabel aiLabel = new JLabel("RESULTS");
        aiLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        aiLabel.setForeground(Color.WHITE);
        aiLabel.setFont(titleFont);
        panel.add(aiLabel);
        
        if (booleanError) {
        	// Show Results
        	panel.add(Box.createVerticalStrut(200));
	        JLabel errorEmpty = new JLabel("An Error has Occured!");
	        errorEmpty.setAlignmentX(Component.CENTER_ALIGNMENT);
	        errorEmpty.setForeground(Color.RED);
	        errorEmpty.setFont(titleFont);
	        panel.add(errorEmpty);
        }
        else if (!validWin) {
        	// Show Results
	        panel.add(Box.createVerticalStrut(200));
	        JLabel invalidWin = new JLabel("Invalid Win!");
	        invalidWin.setAlignmentX(Component.CENTER_ALIGNMENT);
	        invalidWin.setForeground(Color.RED);
	        invalidWin.setFont(titleFont);
	        panel.add(invalidWin);
        }
        else if (successError){
	        // Show Results
	        panel.add(Box.createVerticalStrut(200));
	        JLabel aiCrackedCodeLabel = new JLabel("AI cracked your Code!");
	        aiCrackedCodeLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
	        aiCrackedCodeLabel.setForeground(Color.GREEN);
	        aiCrackedCodeLabel.setFont(titleFont);
	        panel.add(aiCrackedCodeLabel);
	        
	        JLabel attemptTries = new JLabel("Guesses: " + TRIES.get() + " Time: " + totalElapsedTime.get());
	        attemptTries.setAlignmentX(Component.CENTER_ALIGNMENT);
	    	attemptTries.setForeground(Color.green);
	    	attemptTries.setFont(pixelFont3);
	        panel.add(attemptTries);
        }
        else if (exceededTries) {
        	// Show Results
	        panel.add(Box.createVerticalStrut(200));
	        JLabel aiFailed = new JLabel("The AI was unable to crack your code");
	        aiFailed.setAlignmentX(Component.CENTER_ALIGNMENT);
	        aiFailed.setForeground(Color.WHITE);
	        aiFailed.setFont(titleFont);
	        panel.add(aiFailed);
        }
        
        // Repaint the panel to update the UI
        panel.revalidate();
        panel.repaint();
        
        // Add a "Continue" button
        JButton continueButton = new CustomButton("Continue?", 50, 50);
        panel.add(Box.createVerticalStrut(7)); // Adds gap to push main menu button to the bottom
        continueButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        continueButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
            	newModeSection();
            }
        });
        
        // Add a "Back" button
        JButton backButton = new CustomButton("Back to Main Menu", 50, 50);
        panel.add(Box.createVerticalStrut(7)); // Adds gap to push main menu button to the bottom
        backButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        backButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
            	showMainMenu();
            }
        });
        panel.add(Box.createVerticalStrut(225));
        panel.add(continueButton);
        panel.add(Box.createVerticalStrut(7));
        panel.add(backButton);
        
        if(loggedIn) {
	        // Create and display a Label for current user
	        JLabel currentUserLabel = new JLabel("Current User: "+currentUser);
	        currentUserLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
	        currentUserLabel.setForeground(Color.WHITE);
	        currentUserLabel.setFont(pixelFont2);
	        panel.add(currentUserLabel);
	        addLeaderboard2(currentUser, totalElapsedTime.get());
        } else {
        	// Create and display a Label for current user
	        JLabel guestLabel = new JLabel("Current User: Guest");
	        guestLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
	        guestLabel.setForeground(Color.WHITE);
	        guestLabel.setFont(pixelFont2);
	        panel.add(Box.createVerticalStrut(7));
	        panel.add(guestLabel);
	        String guestUser = "Guest";
	        addLeaderboard2(guestUser, totalElapsedTime.get());
        }

        // Repaint the panel
        panel.revalidate();
        panel.repaint();
    }
    
    // Shows the leader board 
    private void addLeaderboard(String currentUser, int lastStreak) {
        // Read the existing leaderboard file
        ArrayList<String> lines = new ArrayList<>();
        try (Scanner scanner = new Scanner(new File("leaderboards.txt"))) {
            while (scanner.hasNextLine()) {
                lines.add(scanner.nextLine());
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            // Handle file reading error
            return;
        }

        // Update the scores for the current user
        boolean userExists = false;
        for (int i = 0; i < lines.size(); i++) {
            String[] parts = lines.get(i).split(",");
            if (parts.length == 2 && parts[0].equals(currentUser)) {
                parts[1] = String.valueOf(lastStreak);
                lines.set(i, String.join(",", parts));
                userExists = true;
                break;
            }
        }

        // If the user doesn't exist, add a new entry
        if (!userExists) {
            lines.add(currentUser + "," + lastStreak);
        }

        // Sort the lines in descending order based on the scores
        Collections.sort(lines, new Comparator<String>() {
            @Override
            public int compare(String line1, String line2) {
                int score1 = Integer.parseInt(line1.split(",")[1]);
                int score2 = Integer.parseInt(line2.split(",")[1]);
                return Integer.compare(score2, score1);
            }
        });

        // Write the updated leaderboard back to the file
        try (PrintWriter writer = new PrintWriter(new FileWriter("leaderboards.txt"))) {
            for (String line : lines) {
                writer.println(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
            // Handle file writing error
        }
    }

 // Shows the leader board 
    private void addLeaderboard2(String currentUser, long totalElapsedTime) {
        // Read the existing leaderboard file
        ArrayList<String> lines = new ArrayList<>();
        try (Scanner scanner = new Scanner(new File("leaderboards2.txt"))) {
            while (scanner.hasNextLine()) {
                lines.add(scanner.nextLine());
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            // Handle file reading error
            return;
        }

        // Update the scores for the current user
        boolean userExists = false;
        for (int i = 0; i < lines.size(); i++) {
            String[] parts = lines.get(i).split(",");
            if (parts.length == 2 && parts[0].equals(currentUser)) {
                parts[1] = String.valueOf(totalElapsedTime);
                lines.set(i, String.join(",", parts));
                userExists = true;
                break;
            }
        }

        // If the user doesn't exist, add a new entry
        if (!userExists) {
            lines.add(currentUser + "," + totalElapsedTime);
        }

        // Sort the lines in descending order based on the scores
        Collections.sort(lines, new Comparator<String>() {
            @Override
            public int compare(String line1, String line2) {
                int score1o = Integer.parseInt(line1.split(",")[1]);
                int score2o = Integer.parseInt(line2.split(",")[1]);
                return Integer.compare(score1o, score2o);
            }
        });

        // Write the updated leaderboard back to the file
        try (PrintWriter writer = new PrintWriter(new FileWriter("leaderboards2.txt"))) {
            for (String line : lines) {
                writer.println(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
            // Handle file writing error
        }
    }
    
    // Shows the results for the player mode
    private void playerResults(AtomicInteger TRIES, AtomicBoolean triesEXCEEDED, AtomicBoolean success, int lastStreak) {
        // Remove the existing components from the panel
        panel.removeAll();

        // Add spacing
        panel.add(Box.createVerticalStrut(15));
        
        boolean exceededTries = triesEXCEEDED.get();
        boolean playerWin = success.get();
     
        // Create and configure the AI label
        JLabel aiLabel = new JLabel("RESULTS");
        aiLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        aiLabel.setForeground(Color.WHITE);
        aiLabel.setFont(titleFont);
        panel.add(aiLabel);
        
        if (playerWin) {
	        // Show Results
	        panel.add(Box.createVerticalStrut(165));
	        JLabel codeCracked = new JLabel("YOU CRACKED THE CODE");
	        codeCracked.setAlignmentX(Component.CENTER_ALIGNMENT);
	        codeCracked.setForeground(Color.GREEN);
	        codeCracked.setFont(titleFont);
	        panel.add(codeCracked);
	        
	        JLabel attemptTries1 = new JLabel("Guesses: "+TRIES.get());
	        attemptTries1.setAlignmentX(Component.CENTER_ALIGNMENT);
	    	attemptTries1.setForeground(Color.white);
	    	attemptTries1.setFont(pixelFont3);
	        panel.add(attemptTries1); 
	        
	        JLabel playerStreak = new JLabel("Streak: "+streak);
	        playerStreak.setAlignmentX(Component.CENTER_ALIGNMENT);
	        playerStreak.setForeground(Color.white);
	        playerStreak.setFont(pixelFont3);
	        panel.add(playerStreak);
        }
        else if (exceededTries) {
            panel.add(Box.createVerticalStrut(165));
            JLabel codeCracked = new JLabel("YOU FAILED...");
            codeCracked.setAlignmentX(Component.CENTER_ALIGNMENT);
            codeCracked.setForeground(Color.RED);
            codeCracked.setFont(titleFont);
            panel.add(codeCracked);

            Color[] realAnsColors = new Color[realAns.length()];

            JPanel answerPanel = new JPanel();
            answerPanel.setLayout(new GridLayout(1, 4, 5, 5));
            answerPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
            answerPanel.setBackground(new Color(0, 0, 0, 100)); // Transparent black color with alpha value 100

            for (int i = 0; i < realAns.length(); i++) {
                char c = realAns.charAt(i);
                realAnsColors[i] = getGuessColor(c);

                // Create a CustomComponent for each character with the corresponding color
                CustomComponent customComponent = new CustomComponent();
                customComponent.setColor(realAnsColors[i]);

                // Add the CustomComponent to the answerPanel
                answerPanel.add(customComponent);
            }
            panel.add(answerPanel); // Add the answerPanel to the main panel
	        
	        JLabel attemptTries = new JLabel("Too many tries...");
	        attemptTries.setAlignmentX(Component.CENTER_ALIGNMENT);
	    	attemptTries.setForeground(Color.white);
	    	attemptTries.setFont(pixelFont3);
	        panel.add(attemptTries);
	        
	        JLabel playerStreak2 = new JLabel("Streak: "+lastStreak);
	        playerStreak2.setAlignmentX(Component.CENTER_ALIGNMENT);
	        playerStreak2.setForeground(Color.white);
	        playerStreak2.setFont(pixelFont3);
	        panel.add(playerStreak2);
        }
        
        // Repaint the panel to update the UI
        panel.revalidate();
        panel.repaint();
        
        // Add a "Continue" button
        JButton continueButton = new CustomButton("Continue?", 50, 50);
        panel.add(Box.createVerticalStrut(7)); // Adds gap to push main menu button to the bottom
        continueButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        continueButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
            	showPlayerSection();
            }
        });
        
        // Add a "Back" button
        JButton backButton = new CustomButton("Back to Main Menu", 50, 50);
        panel.add(Box.createVerticalStrut(7)); // Adds gap to push main menu button to the bottom
        backButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        backButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
            	showMainMenu();
            }
        });
        panel.add(Box.createVerticalStrut(225));
        panel.add(continueButton);
        panel.add(Box.createVerticalStrut(7));
        panel.add(backButton);
        
        if(loggedIn) {
	        // Create and display a Label for current user
	        JLabel currentUserLabel = new JLabel("Current User: "+currentUser);
	        currentUserLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
	        currentUserLabel.setForeground(Color.WHITE);
	        currentUserLabel.setFont(pixelFont2);
	        panel.add(currentUserLabel);
	        addLeaderboard(currentUser, lastStreak);
        } else {
        	// Create and display a Label for current user
	        JLabel guestLabel = new JLabel("Current User: Guest");
	        guestLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
	        guestLabel.setForeground(Color.WHITE);
	        guestLabel.setFont(pixelFont2);
	        panel.add(Box.createVerticalStrut(7));
	        panel.add(guestLabel);
	        String guestUser = "Guest";
	        addLeaderboard(guestUser, lastStreak);
        }

        // Repaint the panel
        panel.revalidate();
        panel.repaint();
    }
    
    // Leaderboards Menu
    private void showLeaderboardsMenu() {
        // Remove the existing components from the panel
        panel.removeAll();

        // Add spacing
        panel.add(Box.createVerticalStrut(15));

        // Read the leaderboard file
        ArrayList<String> lines = new ArrayList<>();
        try (Scanner scanner = new Scanner(new File("leaderboards.txt"))) {
            while (scanner.hasNextLine()) {
                lines.add(scanner.nextLine());
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            // Handle file reading error
            return;
        }

        // Extract names and scores into separate arrays
        ArrayList<String> names = new ArrayList<>();
        ArrayList<Integer> scores = new ArrayList<>();
        int count = Math.min(lines.size(), 10); // Take a maximum of 10 candidates
        for (int i = 0; i < count; i++) {
            String[] parts = lines.get(i).split(",");
            if (parts.length == 2) {
                String name = parts[0];
                int score = Integer.parseInt(parts[1]);
                names.add(name);
                scores.add(score);
            }
        }
        
        
        // Create and configure the AI label
        JLabel aiLabel = new JLabel("LEADERBOARD");
        aiLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        aiLabel.setForeground(Color.YELLOW);
        aiLabel.setFont(titleFont);
        panel.add(aiLabel);
        panel.add(Box.createVerticalStrut(25));
        
        // Display the leaderboard entries
        for (int i = 0; i < names.size(); i++) {
            String name = names.get(i);
            int score = scores.get(i);
            panel.add(Box.createVerticalStrut(20));
            JLabel entryLabel = new JLabel((i + 1) + ". " + name + " - " + score);
            entryLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
            entryLabel.setForeground(Color.WHITE);
            entryLabel.setFont(pixelFont3);
            panel.add(Box.createVerticalStrut(7)); // Adds gap between entries
            panel.add(entryLabel);
        }

        // Add a "Back" button
        JButton backButton = new CustomButton("Back to Main Menu", 50, 50);
        panel.add(Box.createVerticalStrut(10)); // Adds gap to push main menu button to the bottom
        backButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        backButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
            	showMainMenu();
            }
        });
        panel.add(Box.createVerticalStrut(15));
        panel.add(backButton);
        
        if(loggedIn) {
	        // Create and display a Label for current user
	        JLabel currentUserLabel = new JLabel("Current User: "+currentUser);
	        currentUserLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
	        currentUserLabel.setForeground(Color.WHITE);
	        currentUserLabel.setFont(pixelFont2);
	        panel.add(currentUserLabel);
        } else {
        	// Create and display a Label for current user
	        JLabel guestLabel = new JLabel("Current User: Guest");
	        guestLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
	        guestLabel.setForeground(Color.WHITE);
	        guestLabel.setFont(pixelFont2);
	        panel.add(Box.createVerticalStrut(7));
	        panel.add(guestLabel);
        }

        // Repaint the panel
        panel.revalidate();
        panel.repaint();
    }
    
    // Competative Leaderboards Menu
    private void showLeaderboardsMenu2() {
        // Remove the existing components from the panel
        panel.removeAll();

        // Add spacing
        panel.add(Box.createVerticalStrut(15));

        // Read the leaderboard file
        ArrayList<String> lines = new ArrayList<>();
        try (Scanner scanner = new Scanner(new File("leaderboards2.txt"))) {
            while (scanner.hasNextLine()) {
                lines.add(scanner.nextLine());
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            // Handle file reading error
            return;
        }

        // Extract names and scores into separate arrays
        ArrayList<String> names = new ArrayList<>();
        ArrayList<Integer> scores = new ArrayList<>();
        int count = Math.min(lines.size(), 10); // Take a maximum of 10 candidates
        for (int i = 0; i < count; i++) {
            String[] parts = lines.get(i).split(",");
            if (parts.length == 2) {
                String name = parts[0];
                int score = Integer.parseInt(parts[1]);
                names.add(name);
                scores.add(score);
            }
        }
        
        
        // Create and configure the AI label
        JLabel aiLabel = new JLabel("LEADERBOARD");
        aiLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        aiLabel.setForeground(Color.YELLOW);
        aiLabel.setFont(titleFont);
        panel.add(aiLabel);
        panel.add(Box.createVerticalStrut(25));
        
        // Display the leaderboard entries
        for (int i = 0; i < names.size(); i++) {
            String name = names.get(i);
            int score = scores.get(i);
            panel.add(Box.createVerticalStrut(20));
            JLabel entryLabel = new JLabel((i + 1) + ". " + name + " - " + score);
            entryLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
            entryLabel.setForeground(Color.WHITE);
            entryLabel.setFont(pixelFont3);
            panel.add(Box.createVerticalStrut(7)); // Adds gap between entries
            panel.add(entryLabel);
        }

        // Add a "Back" button
        JButton backButton = new CustomButton("Back to Main Menu", 50, 50);
        panel.add(Box.createVerticalStrut(10)); // Adds gap to push main menu button to the bottom
        backButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        backButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
            	showMainMenu();
            }
        });
        panel.add(Box.createVerticalStrut(15));
        panel.add(backButton);
        
        if(loggedIn) {
	        // Create and display a Label for current user
	        JLabel currentUserLabel = new JLabel("Current User: "+currentUser);
	        currentUserLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
	        currentUserLabel.setForeground(Color.WHITE);
	        currentUserLabel.setFont(pixelFont2);
	        panel.add(currentUserLabel);
        } else {
        	// Create and display a Label for current user
	        JLabel guestLabel = new JLabel("Current User: Guest");
	        guestLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
	        guestLabel.setForeground(Color.WHITE);
	        guestLabel.setFont(pixelFont2);
	        panel.add(Box.createVerticalStrut(7));
	        panel.add(guestLabel);
        }

        // Repaint the panel
        panel.revalidate();
        panel.repaint();
    }
    
    // Register Menu
    private void showRegisterMenu() {
    	// Remove the existing components from the panel
        panel.removeAll();

        // Create a vertical Box layout for the main panel
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        // Create a panel for the AI label
        JPanel mainRegisterPanel = new JPanel();
        mainRegisterPanel.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Set the panel as non-opaque
        mainRegisterPanel.setOpaque(false);

        // Create and configure the AI label
        JLabel registerLabel = new JLabel("REGISTER");
        registerLabel.setForeground(Color.WHITE);
        registerLabel.setFont(titleFont);

        // Add the AI label to the AI panel
        mainRegisterPanel.add(registerLabel);

        // Add spacing
        panel.add(Box.createVerticalStrut(15));

        // Add the AI panel to the main panel
        panel.add(mainRegisterPanel);

        // Create a panel for the labels and text fields
        JPanel registerPanel = new JPanel();
        registerPanel.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Set the panel as non-opaque
        registerPanel.setOpaque(false);

        // Set the background color with transparency
        Color transparentBlack = new Color(0, 0, 0, 200); // Adjust the alpha value as needed
        registerPanel.setBackground(transparentBlack);
        registerPanel.setLayout(new BoxLayout(registerPanel, BoxLayout.Y_AXIS));  // Use BoxLayout for vertical alignment

        CustomTextField newUsernameField = new CustomTextField(13);
        newUsernameField.setAlignmentX(Component.CENTER_ALIGNMENT);
        setPlaceholderText(newUsernameField, "Enter new username");

        // Create a panel to contain the username field
        JPanel usernamePanel = new JPanel();
        usernamePanel.setLayout(new FlowLayout(FlowLayout.CENTER));
        usernamePanel.setOpaque(false);
        usernamePanel.add(newUsernameField);

        // Set the preferred size of the username panel
        usernamePanel.setPreferredSize(new Dimension(150, 30)); // Adjust the size as needed

        CustomTextField newPasswordField = new CustomTextField(13);
        newPasswordField.setAlignmentX(Component.CENTER_ALIGNMENT);
        setPlaceholderText(newPasswordField, "Enter new password");

        // Create a panel to contain the password field
        JPanel passwordPanel = new JPanel();
        passwordPanel.setLayout(new FlowLayout(FlowLayout.CENTER));
        passwordPanel.setOpaque(false);
        passwordPanel.add(newPasswordField);

        // Set the preferred size of the password panel
        passwordPanel.setPreferredSize(new Dimension(150, 30)); // Adjust the size as needed

        registerPanel.add(usernamePanel);
        registerPanel.add(passwordPanel);

        // Add spacing
        registerPanel.add(Box.createVerticalStrut(10));

        // Create a panel for the error message
        JPanel displaysPanel = new JPanel();
        displaysPanel.setOpaque(false);

        // Create the error label with initial visibility set to false
        JLabel errorLabel2 = new JLabel("REGISTER FAILED");
        errorLabel2.setFont(pixelFont);
        errorLabel2.setForeground(Color.RED);
        errorLabel2.setVisible(false);
        
        // Create the error label with initial visibility set to false
        JLabel sucessfullyRegistered = new JLabel("SUCCESSFULLY REGISTERED!");
        sucessfullyRegistered.setFont(pixelFont);
        sucessfullyRegistered.setForeground(Color.GREEN);
        sucessfullyRegistered.setVisible(false);
        
        // Create the error label with initial visibility set to false
        JLabel returnStatement = new JLabel("Please go back to the login menu");
        returnStatement.setFont(pixelFont);
        returnStatement.setForeground(Color.WHITE);
        returnStatement.setVisible(false);

        // Add all display text to the displayPanel
        displaysPanel.add(errorLabel2);
        displaysPanel.add(sucessfullyRegistered);
        displaysPanel.add(returnStatement);

        // Add the error panel to the login panel
        registerPanel.add(displaysPanel);

        // Add spacing
        registerPanel.add(Box.createVerticalStrut(10));

        // Add a "Login" button
        JButton registerButton = new CustomButton("Register", 50, 50);
        registerButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        ((CustomButton) registerButton).setOpacity(0.5f);
        registerButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // Get the entered username and password
                String newUsername = newUsernameField.getText();
                String newPassword = newPasswordField.getText();

                // Check if the username and password exist in the loginRecords file

                if (newUsername.equals("Enter new username") || newPassword.equals("Enter new password")) {
                	// Invalid! Please enter a username and password
                	errorLabel2.setVisible(true);  // Show the error label
                } else if (checkCredentials(newUsername, newPassword)){
                	errorLabel2.setVisible(true);  // Show the error label
                } else if (addCredentials(newUsername, newPassword)) {
                	// Registration successful
                	errorLabel2.setVisible(false);
                	sucessfullyRegistered.setVisible(true);
                	returnStatement.setVisible(true);
                } else {
                	// Registration failed
                	errorLabel2.setVisible(true);  // Show the error label
                }
            }
        });

        // Add the login panel to the main panel
        panel.add(registerPanel);

        // Add the login button to the main panel
        panel.add(registerButton);

        // Add spacing
        panel.add(Box.createVerticalStrut(15));

        // Update the UI
        panel.revalidate();
        panel.repaint();

        // Add the "Login" and "register" button to the main panel
        panel.add(registerButton);
        panel.add(Box.createVerticalStrut(270));

        // Add a "Back" button
        JButton backButton = new CustomButton("Back to Login Menu", 50, 50);
        backButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        backButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                loginMenu();
            }
        });

        // Add the "Back" button to the main panel
        panel.add(backButton);

        // Repaint the panel
        panel.revalidate();
        panel.repaint();
    }
    
    // Method to add a new username and password to the loginRecords file
	private boolean addCredentials(String username, String password) {
	    try (BufferedWriter writer = new BufferedWriter(new FileWriter("loginRecords.txt", true))) {
	        writer.write(username + "," + password);
	        writer.newLine();
	        return true; // Credentials added successfully
	    } catch (IOException e) {
	        e.printStackTrace();
	        // Handle file writing error
	        return false; // Failed to add credentials
	    }
	}
    
    // Main Menu
    private void showMainMenu() {
        // Remove the existing components from the panel
        panel.removeAll();

        // Create and configure the CodeBreaker label
        JLabel codeBreakerLabel = new JLabel("CodeBreaker");
        codeBreakerLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        codeBreakerLabel.setForeground(Color.WHITE);
        codeBreakerLabel.setFont(titleFont);

        // Add the logo label and the CodeBreaker label to the panel
        panel.add(logoLabel);
        panel.add(Box.createVerticalStrut(15));
        panel.add(codeBreakerLabel);
        panel.add(Box.createVerticalStrut(15));
        panel.add(codeBreakerButton);
        panel.add(Box.createVerticalStrut(7));
        panel.add(aiButton);
        panel.add(Box.createVerticalStrut(7));

        // Add the leaderboards button
        JButton leaderboardsButton = new CustomButton("Leaderboard", 50, 50);
        leaderboardsButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.add(leaderboardsButton);
        leaderboardsButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
            	showLeaderboardsMenu(); 
            }
        });
        panel.add(Box.createVerticalStrut(7));

        // Add the play as a guest button
        JButton settingsButton = new CustomButton("Settings", 50, 50);
        settingsButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        settingsButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
            	showSettingsMenu(); 
            }
        });
        panel.add(settingsButton);
        
        // Add the newMode button
        panel.add(Box.createVerticalStrut(30));
        JButton newMode = new CustomButton("NEW MODE", 50, 50);
        ((CustomButton) newMode).setOpacity(0.76f);
        newMode.setAlignmentX(Component.CENTER_ALIGNMENT);
        newMode.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
            	newModeSection(); 
            }
        });
        panel.add(newMode);
        
        // Add the leaderboard button
        panel.add(Box.createVerticalStrut(7));
        JButton compLeaderboard = new CustomButton("Competative Leaderboard", 50, 50);
        ((CustomButton) compLeaderboard).setOpacity(0.76f);
        compLeaderboard.setAlignmentX(Component.CENTER_ALIGNMENT);
        compLeaderboard.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
            	showLeaderboardsMenu2(); 
            }
        });
        panel.add(compLeaderboard);
        
        if(loggedIn) {
	        
	        JButton showloginMenu = new CustomButton("Log Out", 50, 50);
	        showloginMenu.setAlignmentX(Component.CENTER_ALIGNMENT);
	        showloginMenu.addActionListener(new ActionListener() {
	            public void actionPerformed(ActionEvent e) {
	            	loginMenu(); 
	            	loggedIn = false;
	            }
	        });
	        
	     // Create and display a Label for current user
	        JLabel currentUserLabel = new JLabel("Current User: "+currentUser);
	        currentUserLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
	        currentUserLabel.setForeground(Color.WHITE);
	        currentUserLabel.setFont(pixelFont2);
	        
	        panel.add(Box.createVerticalStrut(50));
	        panel.add(showloginMenu); 
	        panel.add(Box.createVerticalStrut(7));
	        panel.add(currentUserLabel);
	        addLeaderboard2(currentUser, totalElapsedTime.get());
	        
        } else {
        	
        	panel.add(Box.createVerticalStrut(50));
	        JButton loginMenu = new CustomButton("Return to Login Menu", 50, 50);
	        loginMenu.setAlignmentX(Component.CENTER_ALIGNMENT);
	        loginMenu.addActionListener(new ActionListener() {
	            public void actionPerformed(ActionEvent e) {
	            	loginMenu(); 
	            }
	        });
	        
	        panel.add(loginMenu); // Adds the button to Return to Login Menu
        	// Create and display a Label for current user
	        JLabel guestLabel = new JLabel("Current User: Guest");
	        guestLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
	        guestLabel.setForeground(Color.WHITE);
	        guestLabel.setFont(pixelFont2);
	        panel.add(Box.createVerticalStrut(7));
	        panel.add(guestLabel);
        }

        // Repaint the panel
        panel.revalidate();
        panel.repaint();
    }
    // Show the How to Play menu
    private void howToPlayMenu(){
    	// Remove the existing components from the panel
        panel.removeAll();
    	
    	// Create and configure a How to Play label
        JLabel howToPlayLabel = new JLabel("How to Play");
        howToPlayLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        howToPlayLabel.setForeground(Color.YELLOW);
        howToPlayLabel.setFont(titleFont);
        panel.add(howToPlayLabel);
        
        // Add how to play text description
        panel.add(Box.createVerticalStrut(20));
        JLabel setUpLabel = new JLabel("Set Up:");
        setUpLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        setUpLabel.setForeground(Color.WHITE);
        setUpLabel.setFont(pixelFont3);
        panel.add(setUpLabel);
        
        // Add how to play text description
        panel.add(Box.createVerticalStrut(7));
        JLabel setUpText = new JLabel("<html><div style='margin-left: 30px;'>The game consists of 11 rows and 4 columns. The code maker"
        		+ "creates a secret combination of 4 colored pegs (RED; BLUE, GREEN, ORANGE, YELLOW, PINK). In this version of the game,"
        		+ "there is a mode where the AI will be generating a random code and the player will be guessing.</div></html>");
        setUpText.setAlignmentX(Component.CENTER_ALIGNMENT);
        setUpText.setForeground(Color.WHITE);
        setUpText.setFont(pixelFont4);
        panel.add(setUpText);
        
        // Add guessing description
        panel.add(Box.createVerticalStrut(30));
        JLabel guessingLabel = new JLabel("Guessing:");
        guessingLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        guessingLabel.setForeground(Color.WHITE);
        guessingLabel.setFont(pixelFont3);
        panel.add(guessingLabel);
        
        // Add guessing text description
        panel.add(Box.createVerticalStrut(7));
        JLabel guessingText = new JLabel("<html><div style='margin-left: 30px;'>Typically, in the normal version of this game the player will"
        		+ "be trying to guess another player's code. However, in this version of the game, there is an AI mode where the AI will be trying"
        		+ "to guess the player's code using an algorithm. The player will be providing hints for the AI. </div></html>");
        guessingText.setAlignmentX(Component.CENTER_ALIGNMENT);
        guessingText.setForeground(Color.WHITE);
        guessingText.setFont(pixelFont4);
        panel.add(guessingText);
        
        // Add feedback description
        panel.add(Box.createVerticalStrut(30));
        JLabel feedBackLabel = new JLabel("FeedBack:");
        feedBackLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        feedBackLabel.setForeground(Color.WHITE);
        feedBackLabel.setFont(pixelFont3);
        panel.add(feedBackLabel);
        
        // Add feedback text description
        panel.add(Box.createVerticalStrut(7));
        JLabel feedBackText = new JLabel("<html><div style='margin-left: 30px;'>The code maker will be providing feed back to the player to aid them in "
        		+ "cracking the code. A feedback of black peg(s) means that the code breaker guessed the right color in the right position. A feedback of "
        		+ "white peg(s) means that the code breaker correctly guessed the right color in the wrong postion. No hints means that the code breaker"
        		+ "did not correctly guess any colors. Note that the code maker will always only provide black hints before white hints.</div></html>");
        feedBackText.setAlignmentX(Component.CENTER_ALIGNMENT);
        feedBackText.setForeground(Color.WHITE);
        feedBackText.setFont(pixelFont4);
        panel.add(feedBackText);
        
        // Add winning and losing description
        panel.add(Box.createVerticalStrut(30));
        JLabel winningLosingLabel = new JLabel("Winning and Losing:");
        winningLosingLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        winningLosingLabel.setForeground(Color.WHITE);
        winningLosingLabel.setFont(pixelFont3);
        panel.add(winningLosingLabel);
        
        // Add winning and losing text description
        panel.add(Box.createVerticalStrut(7));
        JLabel winningLosingText = new JLabel("<html><div style='margin-left: 30px;'>In this game, the code breaker has 10 maximum allowed attempts to guess"
        		+ "the code before they code and the code maker wins. However, if the code breaker manages to guess the 4 digit code within 10 attemps, the code"
        		+ "breaker win and the code maker loses.</div></html>");
        winningLosingText.setAlignmentX(Component.CENTER_ALIGNMENT);
        winningLosingText.setForeground(Color.WHITE);
        winningLosingText.setFont(pixelFont4);
        panel.add(winningLosingText);
        
        // Add a button to return to the login menu
        panel.add(Box.createVerticalStrut(50));
        JButton LoginMenuButton = new CustomButton("Return to Login Menu", 50, 50);
        LoginMenuButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        LoginMenuButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
            	loginMenu(); 
            }
        });
        panel.add(LoginMenuButton);
        
        // Repaint the panel
        panel.revalidate();
        panel.repaint();
    }
    // Add an about button
    private void aboutButton() {
    	// Remove all the existing components from the panel
    	panel.removeAll();
    	
    	// Create and configure the about label
        JLabel aboutLabel = new JLabel("ABOUT");
        aboutLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        aboutLabel.setForeground(Color.YELLOW);
        aboutLabel.setFont(titleFont);
        panel.add(aboutLabel);
        
        // Add Program Name
        panel.add(Box.createVerticalStrut(10));
        JLabel codeBreakerLabel = new JLabel("CodeBreaker:");
        codeBreakerLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        codeBreakerLabel.setForeground(Color.YELLOW);
        codeBreakerLabel.setFont(pixelFont3);
        panel.add(codeBreakerLabel);
        
        // Add purpose label
        panel.add(Box.createVerticalStrut(30));
        JLabel purposeLabel = new JLabel("Purpose:");
        purposeLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        purposeLabel.setForeground(Color.WHITE);
        purposeLabel.setFont(pixelFont3);
        panel.add(purposeLabel);
        
        // Add purpose text description
        panel.add(Box.createVerticalStrut(7));
        JLabel purposeText = new JLabel("<html><div style='margin-left: 30px;'>Demonstrate understanding in:"
        		+ "<br> 	- Project Management stages and documentation"
        		+ "<br> 	- Writing algorithms"
        		+ "<br> 	- Working with data structures: arrays, ArrayLists, 2D arrays"
        		+ "<br> 	- Concepts of: Methods, Array processing, Problem-solving, Searching and File-handling"
        		+ "<br> 	- Writing programs that follows the games’ logic"
        		+ "<br> 	- Graphical User Interface and Artificial Intelligence"
        		+ "<br><br>Creating this game helps develop creativity skills, learning and devlopment skills, and"
        		+ "it provides a sense of self-satisfaction.</div></html>");
        purposeText.setAlignmentX(Component.CENTER_ALIGNMENT);
        purposeText.setForeground(Color.WHITE);
        purposeText.setFont(pixelFont4);
        panel.add(purposeText);
        
        // Add acknowledgement label
        panel.add(Box.createVerticalStrut(30));
        JLabel acknowledgementLabel = new JLabel("Acknowledgement:");
        acknowledgementLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        acknowledgementLabel.setForeground(Color.WHITE);
        acknowledgementLabel.setFont(pixelFont3);
        panel.add(acknowledgementLabel);
        
        // Add acknowledgement text description
        panel.add(Box.createVerticalStrut(7));
        JLabel acknowledgementText = new JLabel("<html><div style='margin-left: 30px;'>"
        		+ "<br> 	- Mr.A "
        		+ "<br> 	- Tommy Lyu"
        		+ "<br> 	- Wen Yao Zhang</div></html>");
        acknowledgementText.setAlignmentX(Component.CENTER_ALIGNMENT);
        acknowledgementText.setForeground(Color.WHITE);
        acknowledgementText.setFont(pixelFont4);
        panel.add(acknowledgementText);
        
        // Add contact information label
        panel.add(Box.createVerticalStrut(30));
        JLabel contactInformationLabel = new JLabel("Contact Information:");
        contactInformationLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        contactInformationLabel.setForeground(Color.WHITE);
        contactInformationLabel.setFont(pixelFont3);
        panel.add(contactInformationLabel);
        
        // Add contact information text description
        panel.add(Box.createVerticalStrut(7));
        JLabel contactInformationText = new JLabel("<html><div style='margin-left: 30px;'>"
        		+ "<br>Gmail:"
        		+ "<br> 	- ???@Gmail.com"
        		+ "<br> 	- ???@Gmail.com"
        		+ "<br><br>Phone Number:"
        		+ "<br> 	- ???-???-????"
        		+ "<br> 	- ???-???-????"
        		+ "<br><br>VERSION: 1.0.0</div></html>");
        contactInformationText.setAlignmentX(Component.CENTER_ALIGNMENT);
        contactInformationText.setForeground(Color.WHITE);
        contactInformationText.setFont(pixelFont4);
        panel.add(contactInformationText);
        
        // Add a button to return to the login menu
        panel.add(Box.createVerticalStrut(30));
        JButton LoginMenuButton = new CustomButton("Return to Login Menu", 50, 50);
        LoginMenuButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        LoginMenuButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
            	loginMenu(); 
            }
        });
        panel.add(LoginMenuButton);
        
        // Repaint the panel
        panel.revalidate();
        panel.repaint();
    }
    
    // Show the login menu
    private void loginMenu() {
        // Remove the existing components from the panel
        panel.removeAll();

        // Create and configure the login menu label
        JLabel codeBreakerLabel = new JLabel("LOGIN MENU");
        codeBreakerLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        codeBreakerLabel.setForeground(Color.WHITE);
        codeBreakerLabel.setFont(titleFont);
        panel.add(codeBreakerLabel);

        // Login button
        panel.add(Box.createVerticalStrut(250));
        JButton LoginButton = new CustomButton("Login", 50, 50);
        LoginButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        ((CustomButton) LoginButton).setOpacity(0.5f); // Set the opacity to half-transparent
        LoginButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
            	showLoginMenu();
            }
        });
        panel.add(LoginButton);
            
        // Play as Guest button
        panel.add(Box.createVerticalStrut(7));
        JButton playGuestButton = new CustomButton("Play as Guest", 50, 50);
        playGuestButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        ((CustomButton) playGuestButton).setOpacity(0.5f); // Set the opacity to half-transparent
        playGuestButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                showMainMenu();
            }
        });
        panel.add(playGuestButton);
        
        // How to play button
        panel.add(Box.createVerticalStrut(7));
        JButton howToPlayButton = new CustomButton("How to Play", 50, 50);
        howToPlayButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        howToPlayButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                howToPlayMenu();
            }
        });
        panel.add(howToPlayButton);
        
        // Add and about button
        panel.add(Box.createVerticalStrut(7));
        JButton aboutButton = new CustomButton("About", 50, 50);
        aboutButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        aboutButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                aboutButton();
            }
        });
        panel.add(aboutButton);
        
        // Add the play as a guest button
        JButton settingsButton = new CustomButton("Settings", 50, 50);
        settingsButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        settingsButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
            	showSettingsMenuLogin(); 
            }
        });
        
        panel.add(Box.createVerticalStrut(200));
        panel.add(settingsButton);

        // Repaint the panel
        panel.revalidate();
        panel.repaint();
    }
    
    // Check if the user info matches the loginRecords
    private boolean checkCredentials(String enteredUsername, String enteredPassword) {
        try {
            // Read the loginRecords file
            File loginRecordsFile = new File("loginRecords.txt");
            Scanner scanner = new Scanner(loginRecordsFile);

            // Check each line in the file for matching username and password
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                String[] parts = line.split(",");
                String username = parts[0];
                String password = parts[1];

                if (username.equals(enteredUsername) && password.equals(enteredPassword)) {
                    scanner.close();
                    currentUser = username;
                    loggedIn = true;
                    return true; // Credentials match
                }
            }

            scanner.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        return false; // Credentials not found or do not match
    }
    
    // Customize the buttons size
    private class CustomButton extends JButton {
        private float opacity;

        public CustomButton(String text, int width, int height) {
            super(text);
            setSize(width, height);
            setOpaque(false);
            setContentAreaFilled(false);
            setFocusPainted(false);
            setForeground(Color.WHITE);
            setFont(pixelFont);
        }

        public void setOpacity(float opacity) {
            this.opacity = opacity;
        }

        @Override
        protected void paintComponent(Graphics g) {
            g.setColor(new Color(255, 255, 255, (int) (opacity * 255)));
            g.fillRect(0, 0, getWidth(), getHeight());
            super.paintComponent(g);
        }
    }
}