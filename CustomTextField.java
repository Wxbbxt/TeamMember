import javax.swing.JTextField;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;

public class CustomTextField extends JTextField {
    private Color backgroundColor;
    private Color foregroundColor;
    private Color borderColor;
    private float opacity;

    public CustomTextField(int columns) {
        super(columns);
        setOpaque(false);
        initColors();
        opacity = 1.0f;
    }

    private void initColors() {
        backgroundColor = new Color(0, 0, 0, 128);
        foregroundColor = Color.white;
        borderColor = new Color(255, 255, 255, 100);
    }

    public float getOpacity() {
        return opacity;
    }

    public void setOpacity(float opacity) {
        this.opacity = opacity;
        repaint();
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
        g2d.setFont(getFont());
        g2d.drawString(getText(), 5, getHeight() / 2 + getFontMetrics(getFont()).getAscent() / 2);

        g2d.dispose();
    }
}
