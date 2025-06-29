import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.*;

public class RotatingPolygonApp {

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new PolygonFrame().setVisible(true));
    }
}

class PolygonFrame extends JFrame {
    private final PolygonPanel polygonPanel;
    private final JTextField inputField;

    public PolygonFrame() {
        setTitle("Rotating Polygon");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(600, 600);
        setLocationRelativeTo(null);

        polygonPanel = new PolygonPanel();

        JLabel prompt = new JLabel("How many sides?");
        inputField = new JTextField("3", 5);
        JButton drawButton = new JButton("Draw");

        drawButton.addActionListener(e -> {
            try {
                int sides = Integer.parseInt(inputField.getText());
                if (sides >= 3) {
                    polygonPanel.setSides(sides);
                } else {
                    JOptionPane.showMessageDialog(this, "Enter 3 or more sides.");
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Invalid number.");
            }
        });

        JPanel controlPanel = new JPanel();
        controlPanel.add(prompt);
        controlPanel.add(inputField);
        controlPanel.add(drawButton);

        add(controlPanel, BorderLayout.NORTH);
        add(polygonPanel, BorderLayout.CENTER);
    }
}

class PolygonPanel extends JPanel implements ActionListener {
    private int sides = 3;
    private double angle = 0;
    private final Timer timer;

    public PolygonPanel() {
        timer = new Timer(20, this); // 50 FPS
        timer.start();
    }

    public void setSides(int sides) {
        this.sides = sides;
        repaint();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        angle += 0.01; // rotation speed
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (sides < 3) return;

        Graphics2D g2d = (Graphics2D) g;
        int width = getWidth();
        int height = getHeight();

        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setColor(Color.BLACK);
        g2d.fillRect(0, 0, width, height);

        g2d.translate(width / 2, height / 2);
        g2d.rotate(angle);

        int radius = Math.min(width, height) / 3;
        Polygon polygon = new Polygon();
        for (int i = 0; i < sides; i++) {
            double theta = 2 * Math.PI * i / sides;
            int x = (int) (radius * Math.cos(theta));
            int y = (int) (radius * Math.sin(theta));
            polygon.addPoint(x, y);
        }

        g2d.setColor(Color.GREEN);
        g2d.fillPolygon(polygon);
    }
}
