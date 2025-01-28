import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Collections;

public class Color_Matching_Game extends JFrame implements MouseListener 
{
	// Variables to store game state and settings
	private static final long serialVersionUID = 1L; // Unique identifier for serialization
	private static ArrayList<Integer> storeScore = new ArrayList<>(); // Stores scores of all rounds played
	private static ArrayList<Color> colors = new ArrayList<>(); // Stores a list of colors for the game
	private static ArrayList<Color> pairs = new ArrayList<>(); // Stores pairs of colors for the game
	private static int boxesRemaining = 28; // Number of boxes remaining to be matched
	private static int numTurns = 0; // Number of turns taken in the current game
	private static int numGames = 0; // Number of games played (0 indicates no games played yet)
	private static int firstRow = -1; // Row of the first clicked box
	private static int firstCol = -1; // Column of the first clicked box
	private static int secondRow = -1; // Row of the second clicked box
	private static int secondCol = -1; // Column of the second clicked box.........
	private static int currentScore = 0; // Current score of the game
	private static int highScore = 0; // Highest score achieved in any game
	private static boolean waitingForSecondClick = false; // Flag to indicate if waiting for the second click in a turn
	private static boolean isPaused = false; // Flag to indicate if the game is paused
	
	// Main method to launch the game
    public static void main(String[] args) {mainMenu();}
	
    // Constructor & Initialization to set up the game window
	Color_Matching_Game() 
    {
    	super("Color Matching Game");
        setUndecorated(true);  
        setSize(900, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
        addMouseListener(this);
    }
    
    // Method to initialize the game
    private void initializeGame() 
    {
    	// Add colors to the list 
    	Collections.addAll(colors, Color.RED, Color.BLUE, Color.GREEN, Color.YELLOW, Color.ORANGE, new Color(128, 0, 128), Color.PINK, new Color(139, 69, 19), Color.BLACK, Color.WHITE, Color.CYAN, new Color(0, 128, 128), Color.DARK_GRAY, Color.GRAY);

    	// Duplicate colors to create pairs and shuffle them
        pairs.addAll(colors);
        pairs.addAll(colors);
        
        Collections.shuffle(pairs);
        
        // Draw the initial game board
        redrawBoard();
    }

    // Method to clear and redraw the game board
    private void redrawBoard() 
    {
    	Graphics g = getGraphics();
        g.clearRect(0, 0, getWidth(), getHeight()); 
        paint(g); 
    }
    
    // Method to reset the game state and redraw the board
    private void resetGame()
    {
    	boxesRemaining = 28;
        numTurns = 0;
        currentScore = 0;
        firstRow = -1;
        firstCol = -1;
        secondRow = -1;
        secondCol = -1;
        waitingForSecondClick = false;
        isPaused = false;
        
        // Re-shuffling board for next round
        Collections.shuffle(pairs);
        redrawBoard();
    }

    // Drawing & Display Methods to make game interface
    public void paint(Graphics g) 
    {	
    	super.paint(g);
    	
    	// Draw Background
        Color lightGray = new Color(235, 235, 235);
        g.setColor(lightGray);
        g.fillRect(0, 0, 900, 600);
        
        // Draw Header
        Color lightBlue = new Color(135, 206, 235);
        g.setColor(lightBlue);
        g.fillRect(0, 85, 900, 5);

        g.setColor(Color.WHITE);
        g.fillRect(0, 0, 900, 85);
        
        // Draw Title
        Font titleFont = new Font("Arial", Font.BOLD, 30);
        g.setFont(titleFont);
        
        Color darkPink = new Color(255, 192, 203);
        g.setColor(darkPink);
        g.drawString("Matching Card Game", 280, 68);
        
        // Display Number of Rounds
        displayRounds(g);
        
        // Draw the game board
        drawBoard(g);
    }
    
    // Method to display the number of rounds
    private void displayRounds(Graphics g) 
    {
    	Font titleFont = new Font("Arial", Font.BOLD, 30);
        g.setFont(titleFont);
        
        // Draw background for the rounds display
        Color lightGray = new Color(235, 235, 235);
        g.setColor(lightGray);
        g.fillRect(280, 100, 340, 35);
        
        // Draw the text
        Color darkPink = new Color(255, 192, 203);
        g.setColor(darkPink);
        g.drawString("Number of Rounds: " + numTurns, 280, 127);
    }
    
    // Method to draw board of boxes
    private void drawBoard(Graphics g) 
    {
    	Color lightGreen = new Color(191, 227, 131);
        int height = 145;
        
        // Draw boxes in a grid
        for (int i = 25; i < 350; i += 100) 
        {
            for (int j = 100; j < 800; j += 100)
                drawBox(g, j, height, 85, 85, lightGreen);
            height += 100;
        }
    }
    
    // Method to draw an individual box
    private void drawBox(Graphics g, int x, int y, int width, int height, Color color) 
    {
    	g.setColor(color);
        g.fillRect(x, y, width, height);
    }
    
    // Method to change the color of a box
    public void changeBoxColor(Graphics g, int row, int column, Color color) 
    {
    	int x = column * 100 + 100;
        int y = row * 100 + 145;
        drawBox(g, x, y, 85, 85, color);
    }

    // Mouse event handlers (not used)
    public void mousePressed(MouseEvent e) {}

    public void mouseReleased(MouseEvent e) {}

    public void mouseEntered(MouseEvent e) {}

    public void mouseExited(MouseEvent e) {}
    
    // Mouse click event handler
    public void mouseClicked(MouseEvent e) 
    {
        // If the game is paused, no need to run the method 
        if (isPaused) 
            return;
        
        // Get the coordinates of the click
        Point clickPoint = e.getPoint(); 
        int relativeX = clickPoint.x;
        int relativeY = clickPoint.y;
        
        // Check if the click is within the game board area
        if (relativeX >= 100 && relativeX < 800 && relativeY >= 145 && relativeY < 545) 
        {
            // Calculate the row and column of the clicked box
            int row = (relativeY - 145) / 100;
            int col = (relativeX - 100) / 100;
            
            // Get the color of the clicked box
            Color clickedColor = getColorAt(relativeX, relativeY);
            Color lightGreen = new Color(191, 227, 131);
            
            // Handle first and second clicks for matching logic
            if (clickedColor.equals(lightGreen)) 
            {
                // First click
                if (!waitingForSecondClick) 
                {
                    firstRow = row;
                    firstCol = col;
                    changeBoxColor(getGraphics(), row, col, getBoxColor(row, col));
                    waitingForSecondClick = true;
                } 
                // Second click
                else 
                {
                    secondRow = row;
                    secondCol = col;
                    changeBoxColor(getGraphics(), row, col, getBoxColor(row, col));
                    waitingForSecondClick = false;
                    numTurns++;
                    
                    // Pause to show second box color
                    isPaused = true;
                    
                    // Timer to pause the game briefly to display the second box color
                    javax.swing.Timer timer = new javax.swing.Timer(1200, event -> {
                        
                        // Check if the two clicked boxes have the same color
                        if (checkEqualBoxes(firstRow, firstCol, secondRow, secondCol)) 
                        {
                            boxesRemaining -= 2; // If boxes match, decrease the count of remaining boxes
                            if (boxesRemaining == 0) // If no boxes remaining, the game ends
                            {
                                numGames++; // Increment the number of games played
                                endGameScreen(getGraphics(), numTurns, numGames); // Display end game screen
                            }
                        } 
                        else 
                            restoreBoxes(firstRow, firstCol, secondRow, secondCol); // If boxes don't match, restore their original color
                        
                        // Resume the game
                        isPaused = false; 
                    });

                    // Display the current round
                    displayRounds(getGraphics()); 

                    timer.setRepeats(false); // Set the timer to run only once
                    timer.start(); // Start the timer
                }
            }
        }
    }

    // Method to get the color at a specific coordinate
    private Color getColorAt(int x, int y) 
    {
    	try 
        {
            Robot robot = new Robot();
            return robot.getPixelColor(x, y);
        } 
        catch (AWTException ex)      
        {
            ex.printStackTrace();
            return null;
        }
    }
    
    // Method to get the color of a box based on its row and column
    private Color getBoxColor(int row, int col) 
    {
    	int index = row * 7 + col;
        return pairs.get(index);
    }
    
    // Method to check if two boxes have the same color
    private boolean checkEqualBoxes(int row1, int col1, int row2, int col2) 
    {
    	int index1 = row1 * 7 + col1;
        int index2 = row2 * 7 + col2;
        return pairs.get(index1).equals(pairs.get(index2));
    }
    
    // Method to restore the color of two boxes to the default color
    private void restoreBoxes(int row1, int col1, int row2, int col2) 
    {
    	Color lightGreen = new Color(191, 227, 131);
        changeBoxColor(getGraphics(), row1, col1, lightGreen);
        changeBoxColor(getGraphics(), row2, col2, lightGreen);
    }
    
    // Method to display the main menu screen
    public static void mainMenu() 
    {
    	// Create the main menu frame
        JFrame mainMenuFrame = new JFrame();
        mainMenuFrame.setSize(900, 600);
        mainMenuFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mainMenuFrame.setUndecorated(true); 
        
        // Create the main menu panel
        JPanel mainMenuPanel = new JPanel();
        mainMenuPanel.setBackground(Color.WHITE);
        mainMenuPanel.setLayout(null);
        mainMenuFrame.add(mainMenuPanel);
        
        // Create the title & header label
        JLabel titleLabel = new JLabel("Color Matching Game", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 30));
        titleLabel.setBounds(280, 30, 340, 50);
        mainMenuPanel.add(titleLabel);
        
        JLabel titleLabel2 = new JLabel("<html><font size='6'>Mustafa Diab</font></html>", SwingConstants.CENTER);
        titleLabel2.setBounds(280, 100, 340, 50);
        mainMenuPanel.add(titleLabel2);
        
        // Create the play button
        JButton startButton = new JButton("Start Game");
        startButton.setBounds(350, 225, 200, 50);
        startButton.addActionListener(e -> {
            new Color_Matching_Game().initializeGame();
            mainMenuFrame.dispose();
        });
        
        // Create the tutorial button
        JButton tutorialButton = new JButton("Tutorial");
        tutorialButton.setBounds(350, 325, 200, 50);
        tutorialButton.addActionListener(e -> {
            tutorial();
            mainMenuFrame.dispose();
        });
        
        // Create the exit button
        JButton exitButton = new JButton("Exit");
        exitButton.setBounds(350, 425, 200, 50);
        exitButton.addActionListener(e -> {
            System.exit(0);
        });
        
        decorateMainMenu(mainMenuPanel);
        
        // Add all the buttons to the panel
        mainMenuPanel.add(startButton);
        mainMenuPanel.add(tutorialButton);
        mainMenuPanel.add(exitButton);
        
        mainMenuFrame.setVisible(true);
    }
    
    // Method to decorate main menu screen
    public static void decorateMainMenu(JPanel mainMenuPanel)
    {
    	// Decorate main menu with black stripes
    	drawMainMenu(mainMenuPanel, 5, 575, 275, 0, Color.BLACK);
        drawMainMenu(mainMenuPanel, 5, 575, 625, 0, Color.BLACK);
        
        Color lightGreen = new Color(191, 227, 131);
        
        // Set positions to draw boxes to decorate menu
        int[] xPositions = {35, 150, 35, 150, 35, 150, 35, 150, 35, 150};
        int[] yPositions = {20, 20, 130, 130, 240, 240, 350, 350, 460, 460};
        
        // Drawing the boxes at each position shown in the array above
        for (int i = 0; i < xPositions.length; i++) 
        {
            drawMainMenu(mainMenuPanel, 85, 85, xPositions[i], yPositions[i], lightGreen);
        	drawMainMenu(mainMenuPanel, 85, 85, xPositions[i] + 622, yPositions[i], lightGreen);
        }
    }
    
    // Decorate main menu screen with black stripes method
    public static void drawMainMenu(JPanel mainMenu, int width, int height, int x, int y, Color color)
    {
    	// Initializing box 
    	JPanel drawBox = new JPanel();
    	
    	// Setting box color to the color provided
    	drawBox.setBackground(color);
    	
    	// Setting the bounds of the box based of the values provided in the array 
    	drawBox.setBounds(x, y, width, height);
    	
    	// Adding the box to the JPanel
    	mainMenu.add(drawBox);
    }
    
    // Method to display the tutorial screen
    public static void tutorial() 
    {
    	// Create the tutorial menu frame
        JFrame tutorialFrame = new JFrame("Tutorial");
        tutorialFrame.setSize(900, 600);
        tutorialFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        tutorialFrame.setLayout(new BorderLayout());
        tutorialFrame.setUndecorated(true);
        
        // Create the tutorial menu panel
        JPanel tutorialPanel = new JPanel();
        tutorialPanel.setBackground(Color.WHITE);
        tutorialPanel.setLayout(null);
        tutorialFrame.add(tutorialPanel, BorderLayout.CENTER);
        
        // Create the title & header label
        JLabel titleLabel = new JLabel("Tutorial", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 30));
        titleLabel.setBounds(280, 20, 340, 50);
        tutorialPanel.add(titleLabel);
        
        // Create tutorial text for user
        JLabel tutorialLabel = new JLabel("<html>"
                + "<b>1.</b> Click on a card to flip it.<br><br>"
                + "<b>2.</b> Remember the color and <br> try to find its pair.<br><br>"
                + "<b>3.</b> If you find a matching pair <br> they will stay flipped.<br><br>"
                + "<b>4.</b> If not, they will flip back over.<br><br>"
                + "<b>5.</b> Try to match all pairs in <br> the least number of turns.<br><br>"
                + "<b>6.</b> Good luck and have fun!"
                + "</html>", SwingConstants.CENTER);
        
        tutorialLabel.setFont(new Font("Arial", Font.PLAIN, 20));
        tutorialLabel.setBounds(85, 80, 750, 400); 
        tutorialPanel.add(tutorialLabel);
        
        // Creating return to main menu button
        JButton backButton = new JButton("Return to Main Menu");
        backButton.setBounds(350, 490, 200, 50); 
        backButton.addActionListener(e -> {
            tutorialFrame.dispose();
            mainMenu();
        });
        tutorialPanel.add(backButton);
        tutorialFrame.setVisible(true);
        
        // Decorating tutorial menu
        decorateMainMenu(tutorialPanel);
    }
    
    // Method to display the end game screen
    private void endGameScreen(Graphics g, int rounds, int numGames) 
    {	
    	// Saving score of the current round and saving it into 1 big list to be displayed when user no longer plays
    	storeScore.add(rounds);
        currentScore = rounds;
        
        // Only when first game completed is when we set high score to be current round 
        if (numGames == 1)
            highScore = rounds;
        
        // Checking if any current score is better then saved high score
        if (highScore < currentScore)
            highScore = currentScore;
        
        // Creating end game frame
        JFrame mainMenuFrame = new JFrame();
        mainMenuFrame.setSize(900, 600);
        mainMenuFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mainMenuFrame.setUndecorated(true);
        
        // Creating end game panel 
        JPanel mainMenuPanel = new JPanel();
        mainMenuPanel.setBackground(Color.WHITE);
        mainMenuPanel.setLayout(null);
        mainMenuFrame.add(mainMenuPanel);
        
        // Creating title header for end game screen
        JLabel titleLabel = new JLabel("You Win!!!", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 30));
        titleLabel.setBounds(280, 35, 340, 50);
        mainMenuPanel.add(titleLabel);
        
        // Showing current score of game
        JLabel currentScoreLabel = new JLabel("Current Score: " + currentScore, SwingConstants.CENTER);
        currentScoreLabel.setFont(new Font("Arial", Font.PLAIN, 20));
        currentScoreLabel.setBounds(280, 115, 340, 30);
        mainMenuPanel.add(currentScoreLabel);
        
        // Showing high score of all games played
        JLabel highScoreLabel = new JLabel("High Score: " + highScore, SwingConstants.CENTER);
        highScoreLabel.setFont(new Font("Arial", Font.PLAIN, 20));
        highScoreLabel.setBounds(280, 155, 340, 30);
        mainMenuPanel.add(highScoreLabel);
        
        // Create play again button
        JButton playAgainButton = new JButton("Play Again");
        playAgainButton.setBounds(350, 235, 200, 50);
        playAgainButton.addActionListener(e -> {
            resetGame();
            redrawBoard();
            mainMenuFrame.dispose();
        });
        mainMenuPanel.add(playAgainButton);
        
        // Create return to main menu button
        JButton mainMenuButton = new JButton("Main Menu");
        mainMenuButton.setBounds(350, 335, 200, 50);
        mainMenuButton.addActionListener(e -> {
            mainMenu();
            mainMenuFrame.dispose();
        });
        mainMenuPanel.add(mainMenuButton);
        
        // Create exit button
        JButton exitButton = new JButton("Exit");
        exitButton.setBounds(350, 435, 200, 50);
        exitButton.addActionListener(e -> {
            System.exit(0);
        });

        mainMenuPanel.add(exitButton);
        mainMenuFrame.setVisible(true);
        
        decorateMainMenu(mainMenuPanel);
    }
}
