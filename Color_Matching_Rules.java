import java.awt.*;  
import javax.swing.*;

public class Color_Matching_Rules extends JFrame
{
    private static final long serialVersionUID = 1L;

    // Constructor for the rules window. Sets up the frame, creates the panel, adds buttons, and makes everything visible.
    public Color_Matching_Rules()
    {
        // Window title
        setTitle("Color Matching Game"); 
        
        // Window size
        setSize(800, 770); 
        
        // Program ends if user closes this window
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); 
        
        // Center window on screen
        setLocationRelativeTo(null);
        
        // Main panel with custom drawing for rules
        JPanel mainMenuPanel = new JPanel() 
        {
            private static final long serialVersionUID = 1L;

            @Override
            protected void paintComponent(Graphics g) 
            {
                super.paintComponent(g); // Clears background
                drawRules(g);            // Custom method to draw rules
            }
        };

        // Set background color to white
        mainMenuPanel.setBackground(Color.WHITE); 
        
        // Use absolute positioning (so we can place button manually)
        mainMenuPanel.setLayout(null);

        // Define design color (same light green from menu)
        Color lightGreen = new Color(144, 238, 144);
        
        // Font for button
        Font font = new Font("Arial", Font.BOLD, 24);

        // Create "Return to Main Menu" button
        JButton returnButton = new JButton("Return to Main Menu");
        returnButton.setFont(font);
        returnButton.setBackground(lightGreen);
        returnButton.setForeground(Color.WHITE);
        returnButton.setBounds(250, 660, 275, 50); // position + size
        returnButton.setBorder(BorderFactory.createEmptyBorder()); 
        returnButton.setFocusPainted(false); // remove focus outline

        // Button action: close this window and reopen the main menu
        returnButton.addActionListener(e -> 
        {
            dispose(); 
            new Color_Matching_Menu(); 
        });
        
        // Add button to panel
        mainMenuPanel.add(returnButton); 

        // Add panel to frame
        add(mainMenuPanel); 
        
        // Show window
        setVisible(true);
    }

    
    // Handles all the drawing for the rules screen. Draws the title, decorative squares, and the actual rules text. 
    private void drawRules(Graphics g)
    {
        // Define light green for decorative elements
        Color lightGreen = new Color(144, 238, 144);

        // Title text styling
        g.setColor(lightGreen);
        g.setFont(new Font("Arial", Font.BOLD, 36));
        String title = "Game Rules";

        // Center the title horizontally
        FontMetrics fm = g.getFontMetrics();
        int titleX = (getWidth() - fm.stringWidth(title)) / 2;
        
        // Draw title near the top
        g.drawString(title, titleX - 10, 60);

        // Coordinates for decorative squares on both sides
        int[] xPositions = {35, 160, 35, 160, 35, 160, 35, 160, 35, 160};
        int[] yPositions = {110, 110, 220, 220, 330, 330, 440, 440, 550, 550};

        // Loop: draw matching left and right squares
        for (int i = 0; i < xPositions.length; i++)
        {
            g.setColor(lightGreen);
            g.fillRect(xPositions[i], yPositions[i], 85, 85); // left side
            g.fillRect(xPositions[i] + 500, yPositions[i], 85, 85); // right side
        }

        // Darker green for rules text
        Color forestGreen = new Color(34, 139, 34);
        g.setFont(new Font("Arial", Font.BOLD, 17));
        g.setColor(forestGreen);

        // Rules text lines
        String[] rules = {
            "1. At the start, all boxes",
            "   are light green and hide",
            "   a color. Your goal is to",
            "   match all the pairs.",
            "",
            "2. Click on any box to flip",
            "   it over and see its color.",
            "",
            "3. Click another box to flip it.",
            "",
            "4. The game pauses for a",
            "    moment so you can",
            "    look at both colors.",
            "      - If they match, they",
            "        stay revealed.",
            "      - If not, they flip",
            "        back to light green.",
            "",
            "5. Keep flipping boxes and",
            "   remembering the colors",
            "   until you match every",
            "   pair and clear the board."
        };

        // Draw each line of rules text (stacked vertically)
        for (int i = 0; i < rules.length; i++)
            g.drawString(rules[i], 280, i * 25 + 110);
    }
}