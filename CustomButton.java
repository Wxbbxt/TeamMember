import javax.swing.JButton;
import javax.swing.border.EmptyBorder;
import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;

public class CustomButton extends JButton {
    private Color backgroundColor;
    private Color foregroundColor;
    private Color borderColor;
    private Color hoverBackgroundColor;
    private Color hoverForegroundColor;
    private Color hoverBorderColor;
    private float opacity; // New instance variable for opacity

    public CustomButton(String text, int width, int height) {
        super(text);
        setFocusPainted(false);
        setPreferredSize(new Dimension(width, height));
        setOpaque(false); // Make the button transparent
        adjustButtonWidth();
        initColors();
    }

    private void adjustButtonWidth() {
        int textWidth = getFontMetrics(getFont()).stringWidth(getText());
        int extraWidth = 20; // Additional width for padding

        int newWidth = textWidth + extraWidth;
        int currentHeight = getPreferredSize().height;

        setPreferredSize(new Dimension(newWidth, currentHeight));
    }

    private void initColors() {
        backgroundColor = new Color(0, 0, 0, 128); // Adjust the alpha value (0-255) for transparency
        foregroundColor = Color.white;
        borderColor = new Color(255, 255, 255, 100); // Adjust the alpha value (0-255) for transparency
        hoverBackgroundColor = new Color(255, 255, 255, 200); // Adjust the alpha value (0-255) for transparency
        hoverForegroundColor = Color.black;
        hoverBorderColor = new Color(0, 0, 0, 100); // Adjust the alpha value (0-255) for transparency
        opacity = 1.0f; // Default opacity value
    }

    // Getter and setter for opacity
    public float getOpacity() {
        return opacity;
    }

    public void setOpacity(float opacity) {
        this.opacity = opacity;
        repaint(); // Redraw the button with the new opacity
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2d = (Graphics2D) g.create();
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        g2d.setColor(new Color(
                backgroundColor.getRed(),
                backgroundColor.getGreen(),
                backgroundColor.getBlue(),
                (int) (backgroundColor.getAlpha() * opacity)
        ));
        g2d.fillRect(0, 0, getWidth(), getHeight());
        g2d.setColor(borderColor);
        g2d.drawRect(0, 0, getWidth() - 1, getHeight() - 1);
        g2d.setColor(foregroundColor);
        drawPixelatedText(g2d, getText(), 5, getHeight() / 2 + 5);

        g2d.dispose();
    }

    private void drawPixelatedText(Graphics2D g2d, String text, int x, int y) {
        Font pixelFont = new Font("VCR OSD Mono", Font.PLAIN, 20);
        g2d.setFont(pixelFont);

        for (int i = 0; i < text.length(); i++) {
            char c = text.charAt(i);
            g2d.drawString(String.valueOf(c), x + i * 8, y);
        }
    }
}
