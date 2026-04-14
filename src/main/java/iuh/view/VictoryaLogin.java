package iuh.view;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.*;
import java.awt.image.BufferedImage;

public class VictoryaLogin extends JFrame {

    private static final Color BLUE_PRIMARY = new Color(0x3B6FF0);
    private static final Color BLUE_LIGHT = new Color(0x4A7EFF);
    private static final Color BLUE_DARK = new Color(0x2A5CD4);
    private static final Color TEXT_DARK = new Color(0x1A1A2E);
    private static final Color TEXT_GRAY = new Color(0x9AA0B0);
    private static final Color BORDER_COLOR = new Color(0xDDE3EF);
    private static final Color BG_WHITE = Color.WHITE;
    private static final Font FONT_TITLE = new Font("Segoe UI", Font.BOLD, 38);
    private static final Font FONT_SUBTITLE = new Font("Segoe UI", Font.PLAIN, 22);
    private static final Font FONT_LABEL = new Font("Segoe UI", Font.PLAIN, 14);
    private static final Font FONT_INPUT = new Font("Segoe UI", Font.PLAIN, 14);
    private static final Font FONT_BUTTON = new Font("Segoe UI", Font.BOLD, 15);
    private static final Font FONT_LINK = new Font("Segoe UI", Font.PLAIN, 13);

    public VictoryaLogin() {
        setTitle("Victorya - Đăng nhập");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1100, 680);
        setLocationRelativeTo(null);
        setResizable(false);

        // Main panel: left blue + right white
        JPanel mainPanel = new JPanel(new BorderLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                // White background
                g2.setColor(BG_WHITE);
                g2.fillRect(0, 0, getWidth(), getHeight());
                g2.dispose();
            }
        };

        // Left panel - blue city illustration
        JPanel leftPanel = new CityPanel();
        leftPanel.setPreferredSize(new Dimension(520, 680));

        // Right panel - login form
        JPanel rightPanel = createRightPanel();

        mainPanel.add(leftPanel, BorderLayout.WEST);
        mainPanel.add(rightPanel, BorderLayout.CENTER);

        setContentPane(mainPanel);
        setVisible(true);
    }

    // ─────────────────────────────────────────────────────────────────────────
    // City illustration panel (left side)
    // ─────────────────────────────────────────────────────────────────────────
    static class CityPanel extends JPanel {
        CityPanel() {
            setOpaque(false);
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            int w = getWidth(), h = getHeight();

            // Rounded rectangle (bottom-right corner rounded)
            RoundRectangle2D roundRect = new RoundRectangle2D.Float(0, 0, w + 60, h, 80, 80);
            GradientPaint grad = new GradientPaint(0, 0, new Color(0x3B6FF0), w, h, new Color(0x2550C8));
            g2.setPaint(grad);
            g2.fill(roundRect);

            // Draw city skyline
            drawCity(g2, w, h);

            g2.dispose();
        }

        private void drawCity(Graphics2D g2, int w, int h) {
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            // Clouds - subtle lighter blue ellipses
            Color cloudColor = new Color(0x5B8AF8, true);
            g2.setColor(cloudColor);
            g2.fillOval(30, 60, 120, 40);
            g2.fillOval(80, 50, 90, 35);
            g2.fillOval(200, 80, 100, 30);
            g2.fillOval(260, 70, 70, 28);
            g2.fillOval(350, 55, 130, 38);
            g2.fillOval(410, 45, 80, 30);

            Color buildingBase = new Color(0x2A5CD4);
            Color buildingHighlight = new Color(0x5B8AF8);
            Color windowColor = new Color(255, 255, 255, 50);
            Color windowLit = new Color(255, 255, 255, 120);

            // Draw buildings from back to front
            // Far background buildings
            drawBuilding(g2, w, h, 20, h - 320, 60, 300, buildingBase, buildingHighlight, windowColor, windowLit, 8, 5);
            drawBuilding(g2, w, h, 90, h - 260, 50, 240, buildingBase, buildingHighlight, windowColor, windowLit, 6, 4);
            drawBuilding(g2, w, h, 150, h - 380, 55, 360, buildingBase, buildingHighlight, windowColor, windowLit, 10, 4);
            drawBuilding(g2, w, h, 215, h - 290, 65, 270, buildingBase, buildingHighlight, windowColor, windowLit, 7, 5);
            drawBuilding(g2, w, h, 290, h - 440, 50, 420, buildingBase, buildingHighlight, windowColor, windowLit, 12, 4);
            drawBuilding(g2, w, h, 355, h - 310, 70, 290, buildingBase, buildingHighlight, windowColor, windowLit, 8, 5);
            drawBuilding(g2, w, h, 440, h - 360, 55, 340, buildingBase, buildingHighlight, windowColor, windowLit, 9, 4);

            // Mid buildings (slightly brighter)
            Color midBase = new Color(0x3060DE);
            Color midHighlight = new Color(0x6090FF);
            drawBuilding(g2, w, h, 0, h - 200, 45, 180, midBase, midHighlight, windowColor, windowLit, 5, 3);
            drawBuilding(g2, w, h, 110, h - 230, 55, 210, midBase, midHighlight, windowColor, windowLit, 6, 4);
            drawBuilding(g2, w, h, 180, h - 180, 40, 160, midBase, midHighlight, windowColor, windowLit, 4, 3);
            drawBuilding(g2, w, h, 320, h - 250, 60, 230, midBase, midHighlight, windowColor, windowLit, 6, 4);
            drawBuilding(g2, w, h, 400, h - 210, 50, 190, midBase, midHighlight, windowColor, windowLit, 5, 4);
            drawBuilding(g2, w, h, 460, h - 270, 55, 250, midBase, midHighlight, windowColor, windowLit, 7, 4);

            // Ground
            g2.setColor(new Color(0x1E48B8));
            g2.fillRect(0, h - 30, w, 30);
        }

        private void drawBuilding(Graphics2D g2, int panelW, int panelH,
                                  int x, int y, int bw, int bh,
                                  Color base, Color highlight,
                                  Color winDim, Color winLit,
                                  int winRows, int winCols) {
            // Building body
            g2.setColor(base);
            g2.fillRect(x, y, bw, bh);

            // Highlight edge
            g2.setColor(highlight);
            g2.fillRect(x, y, 4, bh);

            // Windows
            int padX = 6, padY = 10;
            int cellW = (bw - 2 * padX) / winCols;
            int cellH = (bh - 2 * padY) / winRows;
            int ww = Math.max(3, cellW - 4);
            int wh = Math.max(4, cellH - 6);

            for (int row = 0; row < winRows; row++) {
                for (int col = 0; col < winCols; col++) {
                    int wx = x + padX + col * cellW + (cellW - ww) / 2;
                    int wy = y + padY + row * cellH + (cellH - wh) / 2;
                    boolean lit = Math.random() > 0.45;
                    g2.setColor(lit ? winLit : winDim);
                    g2.fillRect(wx, wy, ww, wh);
                }
            }

            // Roof antenna on tall buildings
            if (bh > 300) {
                g2.setColor(highlight);
                g2.setStroke(new BasicStroke(2));
                g2.drawLine(x + bw / 2, y, x + bw / 2, y - 20);
            }
        }
    }

    // ─────────────────────────────────────────────────────────────────────────
    // Right login panel
    // ─────────────────────────────────────────────────────────────────────────
    private JPanel createRightPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(BG_WHITE);
        panel.setBorder(new EmptyBorder(0, 60, 0, 80));

        JPanel form = new JPanel();
        form.setLayout(new BoxLayout(form, BoxLayout.Y_AXIS));
        form.setBackground(BG_WHITE);
        form.setMaximumSize(new Dimension(360, 600));

        // Title
        JLabel title = new JLabel("Victorya");
        title.setFont(FONT_TITLE);
        title.setForeground(BLUE_PRIMARY);
        title.setAlignmentX(Component.LEFT_ALIGNMENT);

        // Subtitle
        JLabel subtitle = new JLabel("Welcome back!");
        subtitle.setFont(FONT_SUBTITLE);
        subtitle.setForeground(TEXT_DARK);
        subtitle.setAlignmentX(Component.LEFT_ALIGNMENT);

        form.add(title);
        form.add(Box.createVerticalStrut(8));
        form.add(subtitle);
        form.add(Box.createVerticalStrut(36));

        // Tài khoản field
        form.add(createLabel("Tài khoản"));
        form.add(Box.createVerticalStrut(6));
        JTextField emailField = createTextField("Enter your email");
        form.add(emailField);
        form.add(Box.createVerticalStrut(20));

        // Mật khẩu field
        form.add(createLabel("Mật khẩu"));
        form.add(Box.createVerticalStrut(6));
        JPasswordField passField = createPasswordField("Name");
        form.add(passField);
        form.add(Box.createVerticalStrut(10));

        // Quên mật khẩu link
        JLabel forgotLink = new JLabel("Quên mật khẩu");
        forgotLink.setFont(FONT_LINK);
        forgotLink.setForeground(BLUE_PRIMARY);
        forgotLink.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        forgotLink.setAlignmentX(Component.LEFT_ALIGNMENT);
        forgotLink.addMouseListener(new MouseAdapter() {
            @Override public void mouseClicked(MouseEvent e) {
                new QuenMatKhauFrame();
            }
            @Override public void mouseEntered(MouseEvent e) {
                forgotLink.setForeground(BLUE_DARK);
            }
            @Override public void mouseExited(MouseEvent e) {
                forgotLink.setForeground(BLUE_PRIMARY);
            }
        });
        form.add(forgotLink);
        form.add(Box.createVerticalStrut(28));

        // Login button
        JButton loginBtn = createLoginButton("Đăng nhập");
        loginBtn.setAlignmentX(Component.LEFT_ALIGNMENT);
        loginBtn.setMaximumSize(new Dimension(360, 48));
        form.add(loginBtn);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.anchor = GridBagConstraints.WEST;
        panel.add(form, gbc);

        return panel;
    }

    private JLabel createLabel(String text) {
        JLabel lbl = new JLabel(text);
        lbl.setFont(FONT_LABEL);
        lbl.setForeground(TEXT_DARK);
        lbl.setAlignmentX(Component.LEFT_ALIGNMENT);
        return lbl;
    }

    private JTextField createTextField(String placeholder) {
        JTextField tf = new JTextField() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                if (getText().isEmpty() && !isFocusOwner()) {
                    Graphics2D g2 = (Graphics2D) g.create();
                    g2.setColor(TEXT_GRAY);
                    g2.setFont(FONT_INPUT);
                    Insets ins = getInsets();
                    g2.drawString(placeholder, ins.left + 2, getHeight() / 2 + g2.getFontMetrics().getAscent() / 2 - 2);
                    g2.dispose();
                }
            }
        };
        styleTextField(tf);
        return tf;
    }

    private JPasswordField createPasswordField(String placeholder) {
        JPasswordField pf = new JPasswordField() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                if (getPassword().length == 0 && !isFocusOwner()) {
                    Graphics2D g2 = (Graphics2D) g.create();
                    g2.setColor(TEXT_GRAY);
                    g2.setFont(FONT_INPUT);
                    Insets ins = getInsets();
                    g2.drawString(placeholder, ins.left + 2, getHeight() / 2 + g2.getFontMetrics().getAscent() / 2 - 2);
                    g2.dispose();
                }
            }
        };
        pf.setEchoChar('\u2022');
        styleTextField(pf);
        return pf;
    }

    private void styleTextField(JTextField tf) {
        tf.setFont(FONT_INPUT);
        tf.setForeground(TEXT_DARK);
        tf.setBackground(BG_WHITE);
        tf.setPreferredSize(new Dimension(360, 44));
        tf.setMaximumSize(new Dimension(360, 44));
        tf.setAlignmentX(Component.LEFT_ALIGNMENT);
        tf.setBorder(new CompoundBorder(
                new LineBorder(BORDER_COLOR, 1, true),
                new EmptyBorder(8, 14, 8, 14)
        ));
        tf.setCaretColor(BLUE_PRIMARY);

        tf.addFocusListener(new FocusAdapter() {
            @Override public void focusGained(FocusEvent e) {
                tf.setBorder(new CompoundBorder(
                        new LineBorder(BLUE_PRIMARY, 2, true),
                        new EmptyBorder(7, 13, 7, 13)
                ));
                tf.repaint();
            }
            @Override public void focusLost(FocusEvent e) {
                tf.setBorder(new CompoundBorder(
                        new LineBorder(BORDER_COLOR, 1, true),
                        new EmptyBorder(8, 14, 8, 14)
                ));
                tf.repaint();
            }
        });
    }

    private JButton createLoginButton(String text) {
        JButton btn = new JButton(text) {
            private boolean hover = false;
            private boolean pressed = false;

            {
                addMouseListener(new MouseAdapter() {
                    @Override public void mouseEntered(MouseEvent e) { hover = true; repaint(); }
                    @Override public void mouseExited(MouseEvent e)  { hover = false; repaint(); }
                    @Override public void mousePressed(MouseEvent e) { pressed = true; repaint(); }
                    @Override public void mouseReleased(MouseEvent e){ pressed = false; repaint(); }
                });
            }

            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                Color col = pressed ? BLUE_DARK : hover ? BLUE_LIGHT : BLUE_PRIMARY;
                g2.setColor(col);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 8, 8);

                g2.setColor(Color.WHITE);
                g2.setFont(FONT_BUTTON);
                FontMetrics fm = g2.getFontMetrics();
                int tx = (getWidth() - fm.stringWidth(getText())) / 2;
                int ty = (getHeight() + fm.getAscent() - fm.getDescent()) / 2;
                g2.drawString(getText(), tx, ty);
                g2.dispose();
            }
        };

        btn.setOpaque(false);
        btn.setContentAreaFilled(false);
        btn.setBorderPainted(false);
        btn.setFocusPainted(false);
        btn.setFont(FONT_BUTTON);
        btn.setForeground(Color.WHITE);
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btn.setPreferredSize(new Dimension(360, 48));

        btn.addActionListener(e -> {
            new VictoryaDashboard();
            dispose();
        });

        return btn;
    }

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ignored) {}

        SwingUtilities.invokeLater(VictoryaLogin::new);
    }
}