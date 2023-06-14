import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.JPanel;

class CustomComponent extends JPanel {
    private int sizeCircle; // Size of the circle
    private Color color = Color.LIGHT_GRAY; // Default color is light gray
    private boolean canChangeColor = true; // Flag to enable/disable color change
    private Color[] colorSequence = {Color.RED, Color.BLUE, Color.GREEN, Color.ORANGE, Color.YELLOW, Color.PINK};
    private int colorIndex = 0;
    
    public CustomComponent() {
        this(50); // Default size of the circle is 50 pixels
    }

    public CustomComponent(int sizeCircle) {
        this.sizeCircle = sizeCircle;
        setPreferredSize(new Dimension(sizeCircle, sizeCircle));

        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                changeColor();
            }
        });
    }

    public void setColor(Color color) {
        this.color = color;
        repaint();
    }
    
    public Color getColor() {
    	return color;
    }

    public void setCanChangeColor(boolean canChange) {
        this.canChangeColor = canChange;
    }
    
    public void changeColorSequence() {
        if (canChangeColor) {
            colorIndex = (colorIndex + 1) % colorSequence.length;
            color = colorSequence[colorIndex];
            repaint();
        }
    }

    private void changeColor() {
        if (canChangeColor) {
            if (color == Color.BLACK) {
                color = Color.WHITE;
            } else if (color == Color.WHITE) {
                color = Color.LIGHT_GRAY;
            } else if (color == Color.LIGHT_GRAY) {
                color = Color.BLACK;
            }
            repaint();
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        // Calculate the center position to draw the circle
        int x = (getWidth() - sizeCircle) / 2;
        int y = (getHeight() - sizeCircle) / 2;

        // Draw the filled circle with the chosen color
        g.setColor(color);
        g.fillOval(x, y, sizeCircle, sizeCircle);

        // Draw the outline (border) of the circle
        g.setColor(Color.WHITE);
        g.drawOval(x, y, sizeCircle, sizeCircle);
    }
}