import javax.swing.*;  
import java.awt.*;      

public class Color_Matching_Menu extends JFrame
{
    private static final long serialVersionUID = 1L;
    
    // The main panel where everything (buttons + background drawing) will be added.
    JPanel mainPanel; 
    
    public static void main(String[] args) {new Color_Matching_Menu();}
    
    // Constructor for the menu window. Sets up the frame, creates the panel, adds buttons, and makes everything visible.
    public Color_Matching_Menu() 
    {
        // Set window title
        setTitle("Color Matching Menu"); 
        
        // Define the size of the window
        setSize(800, 770); 
        
        // Ensure the program closes fully when the window is closed
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); 
        
        // Position the window in the center of the screen
        setLocationRelativeTo(null);
        
        // Create a custom JPanel to draw the menu background (title text + decorative squares).
        mainPanel = new JPanel() 
        {
            private static final long serialVersionUID = 1L;

            @Override
            protected void paintComponent(Graphics g) 
            {
                super.paintComponent(g); // Ensures background is cleared first
                mainMenu(g);             // Calls custom method to draw menu
            }
        };

        // Set background color of the panel
        mainPanel.setBackground(Color.WHITE); 
        
        // Use null layout (absolute positioning of components)
        mainPanel.setLayout(null);

        // Define a reusable "light green" color for the design
        Color lightGreen = new Color(144, 238, 144);
        
        // Define the font style for buttons
        Font font = new Font("Arial", Font.BOLD, 24);

        // Add three main buttons to the menu with specific positions/sizes
        addButton("Start Game", lightGreen, font, 300, 200, 180, 50);
        addButton("Rules", lightGreen, font, 300, 350, 180, 50);
        addButton("Exit", lightGreen, font, 300, 500, 180, 50);

        // Add panel to the JFrame
        add(mainPanel); 
        
        // Make the window visible to the user
        setVisible(true);
    }
    
    // Adds a button to the menu panel with specific styling and behavior.
    private void addButton(String title, Color color, Font font, int x, int y, int width, int height)
    {
        // Create button with the given label text
        JButton button = new JButton(title); 
        
        // Apply styling: font, colors, size, position
        button.setFont(font); 
        button.setBackground(color); 
        button.setForeground(Color.WHITE);
        button.setBounds(x, y, width, height); 
        
        // Remove border & focus outline for a cleaner look
        button.setBorder(BorderFactory.createEmptyBorder()); 
        button.setFocusPainted(false); 

        // Define what happens when this button is clicked
        button.addActionListener(e -> 
        {            
            // Close the current menu window
            dispose();
            
            // Depending on which button was pressed, do different actions
            if (title.equals("Start Game"))
                new Color_Matching_Game(); // Launch the game window
            else if (title.equals("Rules"))
                new Color_Matching_Rules(); // Open the rules window
            else
                System.exit(0); // Exit program for "Exit" button
        });
        
        // Add button to the main panel
        mainPanel.add(button);
    }
    
    
    // Handles drawing the background of the main menu, including the title text and decorative squares.
    private void mainMenu(Graphics g) 
    {
        // Define the same green color used for buttons
        Color lightGreen = new Color(144, 238, 144);
        
        // Get the width of the entire window
        int width = getWidth();
        
        // Fill the background with white
        g.setColor(Color.WHITE);
        g.fillRect(0, 0, getWidth(), getHeight());

        // Set color & font for the title text
        g.setColor(lightGreen); 
        g.setFont(new Font("Arial", Font.BOLD, 30));

        // Define title text (two lines)
        String titleLine1 = "Color Matching";
        String titleLine2 = "Game";

        // Calculate text widths for proper centering
        FontMetrics fm = g.getFontMetrics();
        int title1Width = fm.stringWidth(titleLine1) + 20;
        int title2Width = fm.stringWidth(titleLine2) + 20;

        // Draw the title strings near the top of the screen
        g.drawString(titleLine1, (width - title1Width) / 2, 50);
        g.drawString(titleLine2, (width - title2Width) / 2, 90);

        // Define positions for decorative squares on left & right sides
        int[] xPositions = {35, 160, 35, 160, 35, 160, 35, 160, 35, 160};
        int[] yPositions = {110, 110, 220, 220, 330, 330, 440, 440, 550, 550};

        // Loop through arrays and draw paired squares on both sides
        for (int i = 0; i < xPositions.length; i++) 
        {
            g.setColor(lightGreen);
            
            // Draw left-side square
            g.fillRect(xPositions[i], yPositions[i], 85, 85); 
            
            // Draw matching right-side square (500px offset to the right)
            g.fillRect(xPositions[i] + 500, yPositions[i], 85, 85); 
        }
    }
}