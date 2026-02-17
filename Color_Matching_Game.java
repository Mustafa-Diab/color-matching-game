import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Collections;

public class Color_Matching_Game extends JFrame implements MouseListener 
{
    private static final long serialVersionUID = 1L;

    // Game stats
    private static int highScore = 0;      				 // Best score across all games
    private static int numGames = 0;       				 // Number of games played
    private static int currentScore = 0;   				 // Score of the current game
    
    // Turn/selection tracking
    private int numTurns = 0;              				 // Number of turns taken in current game
    private int firstRow = -1;             				 // Row of first selected box
    private int firstCol = -1;             				 // Column of first selected box
    private int secondRow = -1;            				 // Row of second selected box
    private int secondCol = -1;            				 // Column of second selected box
    private int boxesRemaining = 28;       				 // Total number of boxes left unrevealed
    
    // State flags
    private boolean waitingForSecondClick = false;       // Tracks if waiting for second box
    private boolean isPaused = false;                    // Pauses game briefly between turns
    
    // Colors for the game
    private ArrayList<Color> colors = new ArrayList<>(); // Set of unique colors for the game
    private ArrayList<Color> pairs = new ArrayList<>();  // Duplicated & shuffled pairs
    
    // Tracks which boxes are revealed (true = shown, false = hidden)
    private boolean[][] revealed = new boolean[4][7];

    Color_Matching_Game() 
    {
        setTitle("Color Matching Game"); 
        setSize(900, 600); 
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); 
        setLocationRelativeTo(null);

        // Main panel with custom drawing
        JPanel mainPanel = new JPanel() 
        {
            private static final long serialVersionUID = 1L;

            protected void paintComponent(Graphics g) 
            {
                super.paintComponent(g); 
                paintScreen(g); 
            }
        };

        mainPanel.setBackground(Color.WHITE); 
        mainPanel.setLayout(null); 
        mainPanel.addMouseListener(this); // add mouse listener for clicks

        add(mainPanel); 
        setVisible(true);
        
        // Starts the game once the display has been made
        initializeGame(); 
    }

    private void paintScreen(Graphics g) 
    {
        // Background color
        Color lightGray = new Color(235, 235, 235);
        g.setColor(lightGray);
        g.fillRect(0, 0, 900, 600);

        // Horizontal divider line
        Color lightBlue = new Color(135, 206, 235);
        g.setColor(lightBlue);
        g.fillRect(0, 85, 900, 5);

        // Header background
        g.setColor(Color.WHITE);
        g.fillRect(0, 0, 900, 85);

        // Title text
        Font titleFont = new Font("Arial", Font.BOLD, 30);
        g.setFont(titleFont);
        g.setColor(lightBlue);
        g.drawString("Matching Card Game", 280, 55);

        displayRounds(g); // Show round counter
        drawBoard(g);     // Draw game board
    }

    private void displayRounds(Graphics g) 
    {
        Font titleFont = new Font("Arial", Font.BOLD, 30);
        g.setFont(titleFont);

        // Background behind counter
        Color lightGray = new Color(235, 235, 235);
        g.setColor(lightGray);
        g.fillRect(280, 100, 340, 35);

        // Draw counter text
        Color lightBlue = new Color(135, 206, 235);
        g.setColor(lightBlue);
        g.drawString("Number of Rounds: " + numTurns, 280, 127);
    }

    private void drawBoard(Graphics g) 
    {
        // Color for hidden boxes
        Color lightGreen = new Color(191, 227, 131);

        // Loop through 7x4 grid
        for (int row = 0; row < 4; row++) 
        {
            for (int col = 0; col < 7; col++) 
            {
                int x = 100 + col * 100; // X position of box
                int y = 145 + row * 100; // Y position of box

                Color c;

                // If revealed, show actual color; otherwise show green
                if (revealed[row][col]) 
                    c = getBoxColor(row, col);
                else 
                    c = lightGreen;

                drawBox(g, x, y, 85, 85, c);
            }
        }
    }


    private void drawBox(Graphics g, int x, int y, int width, int height, Color color) 
    {
        g.setColor(color);
        g.fillRect(x, y, width, height);
    }

    private void initializeGame() 
    {
        // Define unique set of 14 colors
        Collections.addAll(colors,
                Color.RED, Color.BLUE, Color.GREEN, Color.YELLOW, Color.ORANGE,
                new Color(128, 0, 128), Color.PINK, new Color(139, 69, 19),
                Color.BLACK, Color.WHITE, Color.CYAN, new Color(0, 128, 128),
                Color.DARK_GRAY, Color.GRAY
        );

        // Duplicate set so we have 28 (pairs)
        pairs.addAll(colors);
        pairs.addAll(colors);

        // Shuffle the pairs randomly
        Collections.shuffle(pairs);

        // Reset game state
        boxesRemaining = 28;
        numTurns = 0;
        waitingForSecondClick = false;
        firstRow = firstCol = secondRow = secondCol = -1;
        revealed = new boolean[4][7];

        repaint();
    }

    public void mouseClicked(MouseEvent e) 
    {
    	// Ignore clicks during pause
        if (isPaused) return; 
        
        // Gets the (x, y) coordinates
        int x = e.getX();
        int y = e.getY();

        // Check if the click is inside the board area
        if (x >= 100 && x < 800 && y >= 145 && y < 545) 
        {
        	// Calculates the row and column based off the coordinates
            int row = (y - 145) / 100;
            int col = (x - 100) / 100;
            
            // Calculates the top-left of the actual box
            int boxX = 100 + col * 100;
            int boxY = 145 + row * 100;

            // Only proceed if inside the 85x85 square (not the white gap)
            if (x < boxX || x > boxX + 85 || y < boxY || y > boxY + 85) 
                return; // Click was in the gap, ignore
            
            // If box is not already revealed
            if (!revealed[row][col]) 
            {
                revealed[row][col] = true;
                repaint();

                // If this is the first click
                if (!waitingForSecondClick) 
                {
                    firstRow = row;
                    firstCol = col;
                    waitingForSecondClick = true;
                } 
                else 
                {
                    // This is the second click
                    secondRow = row;
                    secondCol = col;
                    waitingForSecondClick = false;
                    numTurns++;
                    repaint();

                    // Pause game to let user see both colors
                    isPaused = true;

                    // Timer waits 1 second before checking match
                    javax.swing.Timer timer = new javax.swing.Timer(1000, event -> 
                    {
                        if (checkEqualBoxes(firstRow, firstCol, secondRow, secondCol)) 
                        {
                            boxesRemaining -= 2;     // Two matched boxes removed
                            if (boxesRemaining == 0) // Game over if all cleared 
                                endGameScreen(); 
                        } 
                        else 
                        {
                            // If not a match, flip back both boxes
                            revealed[firstRow][firstCol] = false;
                            revealed[secondRow][secondCol] = false;
                        }
                        isPaused = false;
                        repaint();
                    });
                    
                    // Run only once
                    timer.setRepeats(false); 
                    timer.start();
                }
            }
        }
    }

    private Color getBoxColor(int row, int col) 
    {
    	// Convert row/col to index in pairs
        int index = row * 7 + col; 
        return pairs.get(index);
    }

    private boolean checkEqualBoxes(int row1, int col1, int row2, int col2) 
    {
        // Compare colors of two boxes
        return getBoxColor(row1, col1).equals(getBoxColor(row2, col2));
    }

    private void endGameScreen() 
    {
        numGames++;
        currentScore = numTurns;

        // Update high score
        if (numGames == 1)
            highScore = currentScore;
        else if (currentScore < highScore)
            highScore = currentScore;

        // Show dialog with results
        String message = "You win!\nRounds: " + currentScore + "\nHigh Score: " + highScore;
        String[] options = {"Play Again", "Main Menu"};

        int choice = JOptionPane.showOptionDialog(this, message, "Game Over", 
            JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE, 
            null, options, options[0]);

        // If player chooses "Play Again"
        if (choice == 0) 
            initializeGame();
        // If player chooses "Main Menu"
        else if (choice == 1) 
        {
            dispose(); 
            new Color_Matching_Menu();
        }
    }

    // Unused mouse listener methods (must be defined)
    public void mousePressed(MouseEvent e) {}
    public void mouseReleased(MouseEvent e) {}
    public void mouseEntered(MouseEvent e) {}
    public void mouseExited(MouseEvent e) {}
}
