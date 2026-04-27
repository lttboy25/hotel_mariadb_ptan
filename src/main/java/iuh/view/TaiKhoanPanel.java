package iuh.view;

import iuh.dto.NhanVienDTO;
import iuh.service.NhanVienService;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.*;

public class TaiKhoanPanel extends JPanel {

    static final Color BG      = new Color(0xF4F6FB);
    static final Color WHITE   = Color.WHITE;
    static final Color BLUE    = new Color(0x3B6FF0);
    static final Color DARK    = new Color(0x1A1A2E);
    static final Color MID     = new Color(0x4A5268);
    static final Color GRAY    = new Color(0xA0A8B8);
    static final Color BORDER  = new Color(0xE4E9F2);
    static final Color INPUT_BG= new Color(0xF0F2F7);

    static final Font F_NAME   = new Font("Segoe UI", Font.BOLD, 22);
    static final Font F_EMAIL  = new Font("Segoe UI", Font.PLAIN, 13);
    static final Font F_LABEL  = new Font("Segoe UI", Font.PLAIN, 13);
    static final Font F_INPUT  = new Font("Segoe UI", Font.PLAIN, 13);
    static final Font F_BOLD13 = new Font("Segoe UI", Font.BOLD, 13);
    static final Font F_PLAIN12= new Font("Segoe UI", Font.PLAIN, 12);
    static final Font F_BOLD12 = new Font("Segoe UI", Font.BOLD, 12);

    // Reference để lưu trữ field hiển thị
    private JLabel nameLbl, emailLbl;
    private JTextField tfHoTen, tfCCCD, tfDiaChi, tfNgaySinh, tfTaiKhoan, tfEmail;
    private JPanel genderPanel;

    public TaiKhoanPanel() {
        setLayout(new BorderLayout());
        setBackground(BG);
        add(buildHeader(), BorderLayout.NORTH);

        JScrollPane scroll = new JScrollPane(buildBody());
        scroll.setBorder(null);
        scroll.getVerticalScrollBar().setUnitIncrement(12);
        scroll.setBackground(BG);
        scroll.getViewport().setBackground(BG);
        add(scroll, BorderLayout.CENTER);

        // Tải dữ liệu người dùng từ CurrentUser
        loadUserData();
    }

    private void loadUserData() {
        CurrentUser currentUser = CurrentUser.getInstance();
        NhanVienDTO nv = currentUser.getNhanVien();

        if (nv != null) {
            // Cập nhật header (tên + email)
            nameLbl.setText(nv.getTenNhanVien());
            emailLbl.setText(nv.getEmail() != null ? nv.getEmail() : "");

            // Cập nhật form fields
            tfHoTen.setText(nv.getTenNhanVien());
            tfCCCD.setText(nv.getCCCD() != null ? nv.getCCCD() : "");
            tfDiaChi.setText(nv.getDiaChi() != null ? nv.getDiaChi() : "");
            tfNgaySinh.setText(nv.getNgaySinh() != null ? nv.getNgaySinh().toString() : "");
            tfTaiKhoan.setText(nv.getTaiKhoan() != null ? nv.getTaiKhoan() : "");
            tfEmail.setText(nv.getEmail() != null ? nv.getEmail() : "");
        }
    }

    // ── Header ────────────────────────────────────────────────────────────────
    private JPanel buildHeader() {
        JPanel h = new JPanel(new BorderLayout());
        h.setBackground(WHITE);
        h.setBorder(new CompoundBorder(
                new MatteBorder(0, 0, 1, 0, BORDER),
                new EmptyBorder(10, 24, 10, 24)));
        h.setPreferredSize(new Dimension(0, 54));

        JPanel search = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0)) {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(new Color(0xF4F6FB));
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 20, 20);
                g2.setColor(BORDER);
                g2.drawRoundRect(0, 0, getWidth()-1, getHeight()-1, 20, 20);
                g2.dispose();
            }
        };
        search.setOpaque(false);
        search.setPreferredSize(new Dimension(240, 34));
        search.add(lbl("🔍", new Font("Segoe UI", Font.PLAIN, 13), GRAY));
        search.add(lbl("Search for rooms and offers", F_PLAIN12, GRAY));
        h.add(search, BorderLayout.WEST);

        JPanel right = new JPanel(new FlowLayout(FlowLayout.RIGHT, 12, 0));
        right.setOpaque(false);
        right.add(lbl("🔔", new Font("Segoe UI", Font.PLAIN, 18), GRAY));
        JPanel avatar = new JPanel() {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(new Color(0x6B9EFF));
                g2.fillOval(0, 0, 34, 34);
                g2.setColor(WHITE);
                g2.setFont(new Font("Segoe UI", Font.BOLD, 14));
                FontMetrics fm = g2.getFontMetrics();
                g2.drawString("N", 17 - fm.stringWidth("N")/2, 17 + fm.getAscent()/2 - 2);
                g2.dispose();
            }
        };
        avatar.setOpaque(false);
        avatar.setPreferredSize(new Dimension(34, 34));
        right.add(avatar);
        h.add(right, BorderLayout.EAST);
        return h;
    }

    // ── Body ──────────────────────────────────────────────────────────────────
    private JPanel buildBody() {
        JPanel body = new JPanel(new GridBagLayout());
        body.setBackground(WHITE);

        GridBagConstraints gc = new GridBagConstraints();
        gc.gridx = 0; gc.fill = GridBagConstraints.HORIZONTAL;
        gc.weightx = 1.0;

        // Content panel (centered, max width 680)
        JPanel content = buildContent();
        JPanel centerWrap = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 40));
        centerWrap.setBackground(WHITE);
        centerWrap.add(content);

        gc.gridy = 0; gc.weighty = 0;
        body.add(centerWrap, gc);
        gc.gridy = 1; gc.weighty = 1.0;
        body.add(Box.createVerticalGlue(), gc);

        return body;
    }

    private JPanel buildContent() {
        JPanel p = new JPanel();
        p.setLayout(new BoxLayout(p, BoxLayout.Y_AXIS));
        p.setBackground(WHITE);
        p.setPreferredSize(new Dimension(680, 560));

        // Avatar + name
        JPanel nameRow = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        nameRow.setOpaque(false);

        // Avatar circle
        JPanel avatarBig = new JPanel() {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(new Color(0x6B9EFF));
                g2.fillOval(0, 0, 52, 52);
                g2.setColor(WHITE);
                g2.setFont(new Font("Segoe UI", Font.BOLD, 20));
                FontMetrics fm = g2.getFontMetrics();
                g2.drawString("H", 26 - fm.stringWidth("H")/2, 26 + fm.getAscent()/2 - 2);
                g2.dispose();
            }
        };
        avatarBig.setOpaque(false);
        avatarBig.setPreferredSize(new Dimension(52, 52));

        JPanel nameInfo = new JPanel();
        nameInfo.setLayout(new BoxLayout(nameInfo, BoxLayout.Y_AXIS));
        nameInfo.setOpaque(false);
        nameInfo.setBorder(new EmptyBorder(4, 14, 0, 0));
        nameLbl = lbl("", F_NAME, DARK);
        nameLbl.setAlignmentX(LEFT_ALIGNMENT);
        emailLbl = lbl("", F_EMAIL, GRAY);
        emailLbl.setAlignmentX(LEFT_ALIGNMENT);
        nameInfo.add(nameLbl);
        nameInfo.add(Box.createVerticalStrut(2));
        nameInfo.add(emailLbl);

        nameRow.add(avatarBig);
        nameRow.add(nameInfo);
        p.add(nameRow);
        p.add(Box.createVerticalStrut(28));

        // Form fields
        tfHoTen = buildInputField("", false);
        tfCCCD = buildInputField("", false);
        tfDiaChi = buildInputField("", false);
        tfNgaySinh = buildInputField("", false);
        tfTaiKhoan = buildInputField("", false);
        tfEmail = buildInputField("", false);
        p.add(formRow("Họ và tên:", tfHoTen));
        p.add(Box.createVerticalStrut(16));
        p.add(formRow("CCCD:", tfCCCD));
        p.add(Box.createVerticalStrut(16));
        p.add(formRow("Địa Chỉ", tfDiaChi));
        p.add(Box.createVerticalStrut(16));

        // Giới tính row with toggles
        genderPanel = buildGenderRow();
        p.add(formRow("Giới tính", genderPanel));
        p.add(Box.createVerticalStrut(16));

        p.add(formRow("Ngày Sinh", tfNgaySinh));
        p.add(Box.createVerticalStrut(16));

        // Tài khoản + Mật khẩu row
        p.add(formRow("Tài Khoản", buildAccountRow()));
        p.add(Box.createVerticalStrut(16));

        p.add(formRow("Email", tfEmail));
        p.add(Box.createVerticalStrut(40));

        // Lưu button centered
        JPanel btnRow = new JPanel(new FlowLayout(FlowLayout.CENTER));
        btnRow.setOpaque(false);
        btnRow.setAlignmentX(LEFT_ALIGNMENT);
        JButton saveBtn = new JButton("Lưu") {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                Color c = getModel().isRollover() ? new Color(0x2A5CD4) : BLUE;
                g2.setColor(c);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 10, 10);
                g2.setColor(WHITE);
                g2.setFont(new Font("Segoe UI", Font.BOLD, 14));
                FontMetrics fm = g2.getFontMetrics();
                g2.drawString(getText(), (getWidth()-fm.stringWidth(getText()))/2,
                        (getHeight()+fm.getAscent()-fm.getDescent())/2);
                g2.dispose();
            }
        };
        saveBtn.setOpaque(false); saveBtn.setContentAreaFilled(false);
        saveBtn.setBorderPainted(false); saveBtn.setForeground(WHITE);
        saveBtn.setPreferredSize(new Dimension(140, 44));
        saveBtn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        saveBtn.addActionListener(e ->
                JOptionPane.showMessageDialog(this, "Lưu thông tin thành công!", "Thông báo", JOptionPane.INFORMATION_MESSAGE));
        btnRow.add(saveBtn);
        p.add(btnRow);

        return p;
    }

    // ── Form helpers ──────────────────────────────────────────────────────────
    private JPanel formRow(String labelText, JComponent field) {
        JPanel row = new JPanel(new BorderLayout(0, 0));
        row.setOpaque(false);
        row.setMaximumSize(new Dimension(680, 56));
        row.setAlignmentX(LEFT_ALIGNMENT);

        JLabel lbl = new JLabel(labelText);
        lbl.setFont(F_LABEL);
        lbl.setForeground(MID);
        lbl.setPreferredSize(new Dimension(130, 40));
        lbl.setHorizontalAlignment(SwingConstants.LEFT);
        row.add(lbl, BorderLayout.WEST);
        row.add(field, BorderLayout.CENTER);
        return row;
    }

    private JTextField buildInputField(String value, boolean password) {
        JTextField tf = password ? new JPasswordField(value) : new JTextField(value);
        tf.setFont(F_INPUT);
        tf.setForeground(DARK);
        tf.setBackground(INPUT_BG);
        tf.setBorder(new CompoundBorder(
                new LineBorder(INPUT_BG, 1, true),
                new EmptyBorder(8, 14, 8, 14)
        ));
        tf.setPreferredSize(new Dimension(0, 40));
        // Rounded via custom painting
        tf.setOpaque(true);
        tf.addFocusListener(new FocusAdapter() {
            @Override public void focusGained(FocusEvent e) {
                tf.setBackground(WHITE);
                tf.setBorder(new CompoundBorder(
                        new LineBorder(BLUE, 1, true),
                        new EmptyBorder(8, 14, 8, 14)));
            }
            @Override public void focusLost(FocusEvent e) {
                tf.setBackground(INPUT_BG);
                tf.setBorder(new CompoundBorder(
                        new LineBorder(INPUT_BG, 1, true),
                        new EmptyBorder(8, 14, 8, 14)));
            }
        });
        return tf;
    }

    private JPanel buildGenderRow() {
        JPanel p = new JPanel(new FlowLayout(FlowLayout.LEFT, 18, 0));
        p.setOpaque(false);
        p.setPreferredSize(new Dimension(0, 40));

        // Nam toggle
        p.add(buildToggle("Nam", true));
        // Nữ toggle
        p.add(buildToggle("Nữ", false));
        return p;
    }

    private JPanel buildToggle(String label, boolean on) {
        JPanel p = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0));
        p.setOpaque(false);

        // Pill toggle switch
        JPanel toggle = new JPanel() {
            boolean state = on;
            {
                setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
                addMouseListener(new MouseAdapter() {
                    @Override public void mouseClicked(MouseEvent e) {
                        state = !state; repaint();
                    }
                });
            }
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                // Track
                g2.setColor(state ? new Color(0xBBCCFF) : new Color(0xDDDDDD));
                g2.fillRoundRect(0, 3, 34, 16, 16, 16);
                // Knob
                int kx = state ? 18 : 2;
                g2.setColor(state ? BLUE : WHITE);
                g2.fillOval(kx, 1, 20, 20);
                g2.setColor(new Color(0xDDDDDD));
                g2.setStroke(new BasicStroke(1));
                g2.drawOval(kx, 1, 20, 20);
                g2.dispose();
            }
        };
        toggle.setOpaque(false);
        toggle.setPreferredSize(new Dimension(40, 22));

        p.add(toggle);
        p.add(lbl(label, F_LABEL, MID));
        return p;
    }

    private JPanel buildAccountRow() {
        JPanel p = new JPanel(new BorderLayout(12, 0));
        p.setOpaque(false);
        p.setPreferredSize(new Dimension(0, 40));

        // Left: username field
        JPanel left = new JPanel(new BorderLayout(10, 0));
        left.setOpaque(false);
        left.add(buildInputField("", false), BorderLayout.CENTER);

        // Middle: Mật khẩu label + field
        JPanel mid = new JPanel(new BorderLayout(8, 0));
        mid.setOpaque(false);
        JLabel pwdLbl = lbl("Mật Khẩu", F_LABEL, MID);
        pwdLbl.setHorizontalAlignment(SwingConstants.RIGHT);
        pwdLbl.setPreferredSize(new Dimension(72, 40));
        mid.add(pwdLbl, BorderLayout.WEST);
        mid.add(buildInputField("•••••••", false), BorderLayout.CENTER);

        // Right: Đổi mật khẩu button
        JButton changePwdBtn = new JButton("Đổi mật khẩu") {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                Color c = getModel().isRollover() ? new Color(0x2A5CD4) : BLUE;
                g2.setColor(c);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 8, 8);
                g2.setColor(WHITE);
                g2.setFont(F_BOLD12);
                FontMetrics fm = g2.getFontMetrics();
                g2.drawString(getText(), (getWidth()-fm.stringWidth(getText()))/2,
                        (getHeight()+fm.getAscent()-fm.getDescent())/2);
                g2.dispose();
            }
        };
        changePwdBtn.setOpaque(false); changePwdBtn.setContentAreaFilled(false);
        changePwdBtn.setBorderPainted(false); changePwdBtn.setForeground(WHITE);
        changePwdBtn.setFont(F_BOLD12);
        changePwdBtn.setPreferredSize(new Dimension(120, 40));
        changePwdBtn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        changePwdBtn.addActionListener(e ->
                JOptionPane.showMessageDialog(this, "Chức năng đổi mật khẩu!", "Đổi mật khẩu", JOptionPane.INFORMATION_MESSAGE));

        p.add(left, BorderLayout.CENTER);
        p.add(mid, BorderLayout.WEST);

        // We need a different layout: [username] [Mật Khẩu: field] [Đổi btn]
        // Rebuild cleanly
        JPanel row = new JPanel(new GridBagLayout());
        row.setOpaque(false);
        GridBagConstraints gc = new GridBagConstraints();
        gc.fill = GridBagConstraints.HORIZONTAL; gc.gridy = 0;

        gc.gridx = 0; gc.weightx = 0.35;
        row.add(buildInputField("", false), gc);

        gc.gridx = 1; gc.weightx = 0.0; gc.insets = new Insets(0, 12, 0, 0);
        row.add(lbl("Mật Khẩu", F_LABEL, MID), gc);

        gc.gridx = 2; gc.weightx = 0.3; gc.insets = new Insets(0, 8, 0, 0);
        row.add(buildInputField("", false), gc);

        gc.gridx = 3; gc.weightx = 0.0; gc.insets = new Insets(0, 10, 0, 0);
        row.add(changePwdBtn, gc);

        return row;
    }

    static JLabel lbl(String t, Font f, Color c) {
        JLabel l = new JLabel(t); l.setFont(f); l.setForeground(c); return l;
    }
}

