package iuh.view;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.*;

public class VictoryaDashboard extends JFrame {

    static final Color BG_MAIN    = new Color(0xF4F6FB);
    static final Color BG_WHITE   = Color.WHITE;
    static final Color BLUE       = new Color(0x3B6FF0);
    static final Color BLUE_LIGHT = new Color(0xEBF0FF);
    static final Color TEXT_DARK  = new Color(0x1A1A2E);
    static final Color TEXT_MID   = new Color(0x4A5268);
    static final Color TEXT_GRAY  = new Color(0xA0A8B8);
    static final Color BORDER     = new Color(0xE4E9F2);
    static final Color RED_LIGHT  = new Color(0xFFF0F0);
    static final Color RED_TEXT   = new Color(0xE04040);
    static final Color YELLOW     = new Color(0xF5B942);
    static final Color PINK       = new Color(0xE86FA8);
    static final Color PURPLE     = new Color(0x7B61FF);

    static final Font F_LOGO    = new Font("Segoe UI", Font.BOLD, 20);
    static final Font F_NAV     = new Font("Segoe UI", Font.PLAIN, 13);
    static final Font F_NAV_SEL = new Font("Segoe UI", Font.BOLD, 13);
    static final Font F_NAV_SUB = new Font("Segoe UI", Font.PLAIN, 12);
    static final Font F_H1      = new Font("Segoe UI", Font.BOLD, 36);
    static final Font F_LABEL   = new Font("Segoe UI", Font.PLAIN, 12);
    static final Font F_SMALL   = new Font("Segoe UI", Font.PLAIN, 11);
    static final Font F_BOLD12  = new Font("Segoe UI", Font.BOLD, 12);

    private CardLayout cardLayout;
    private JPanel contentCards;

    public VictoryaDashboard() {
        setTitle("Victorya - Dashboard");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1280, 780);
        setLocationRelativeTo(null);
        setResizable(true);

        JPanel root = new JPanel(new BorderLayout());
        root.setBackground(BG_MAIN);

        JPanel sidebar = buildSidebar();
        sidebar.setPreferredSize(new Dimension(228, 0));

        cardLayout = new CardLayout();
        contentCards = new JPanel(cardLayout);
        contentCards.setBackground(BG_MAIN);
        contentCards.add(new ThongKePanel(), "thongke");

        root.add(sidebar, BorderLayout.WEST);
        root.add(contentCards, BorderLayout.CENTER);

        setContentPane(root);
        setVisible(true);
    }

    // =========================================================================
    //  SIDEBAR
    // =========================================================================
    private JPanel buildSidebar() {
        JPanel sidebar = new JPanel();
        sidebar.setLayout(new BoxLayout(sidebar, BoxLayout.Y_AXIS));
        sidebar.setBackground(BG_WHITE);
        sidebar.setBorder(new MatteBorder(0, 0, 0, 1, BORDER));

        // Logo
        JPanel logoPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 20, 16));
        logoPanel.setBackground(BG_WHITE);
        logoPanel.setMaximumSize(new Dimension(228, 58));
        JLabel logo = new JLabel("Victorya");
        logo.setFont(F_LOGO);
        logo.setForeground(BLUE);
        logoPanel.add(logo);
        sidebar.add(logoPanel);
        sidebar.add(Box.createVerticalStrut(4));

        // Nav rows: (iconType, label, indent, active)
        sidebar.add(navRow(NavIcon.TRANG_CHU,   "Trang chủ",          0, false));
        sidebar.add(navRow(NavIcon.PHONG,        "Phòng",              0, false));
        sidebar.add(navRow(NavIcon.DAT_PHONG,    "Đặt Phòng",          1, false));
        sidebar.add(navRow(NavIcon.GIA_HAN,      "Gia Hạn Phòng",      1, false));
        sidebar.add(navRow(NavIcon.HUY_PHONG,    "Hủy Phòng",          1, false));
        sidebar.add(navRow(NavIcon.KHUYEN_MAI,   "Khuyến Mãi",         0, false));
        sidebar.add(navRow(NavIcon.THONG_KE,     "Thống Kê",           0, true));
        sidebar.add(navRow(NavIcon.DICH_VU,      "Quản lý dịch vụ",    0, false));
        sidebar.add(navRow(NavIcon.NHAN_VIEN,    "Quản lý nhân viên",  0, false));
        sidebar.add(navRow(NavIcon.TAI_KHOAN,    "Tài Khoản",          0, false));
        sidebar.add(navRow(NavIcon.CA_LAM_VIEC,  "Ca làm việc",        0, false));
        sidebar.add(navRow(NavIcon.HOA_DON,      "Quản lý hóa đơn",    0, false));

        sidebar.add(Box.createVerticalGlue());

        sidebar.add(navRow(NavIcon.CAU_HINH,     "Cấu hình hệ thống",  0, false));
        sidebar.add(navRow(NavIcon.LOGOUT,       "Logout",             0, false));
        sidebar.add(Box.createVerticalStrut(14));

        return sidebar;
    }

    private JPanel navRow(int iconType, String label, int indent, boolean active) {
        boolean isChild = indent > 0;
        int rowH = isChild ? 36 : 42;

        JPanel row = new JPanel(new BorderLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                if (active) {
                    g2.setColor(BLUE_LIGHT);
                    g2.fillRect(0, 0, getWidth(), getHeight());
                    g2.setColor(BLUE);
                    g2.fillRect(0, 0, 3, getHeight());
                } else {
                    g2.setColor(getBackground());
                    g2.fillRect(0, 0, getWidth(), getHeight());
                }
                g2.dispose();
                super.paintComponent(g);
            }
        };
        row.setOpaque(false);
        row.setBackground(BG_WHITE);
        row.setMaximumSize(new Dimension(228, rowH));
        row.setPreferredSize(new Dimension(228, rowH));
        row.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        JPanel inner = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        inner.setOpaque(false);
        int leftMargin = 16 + indent * 20;
        inner.setBorder(new EmptyBorder(0, leftMargin, 0, 0));

        // Icon
        Color iconColor = active ? BLUE : TEXT_MID;
        NavIconPanel icon = new NavIconPanel(iconType, iconColor, 16);
        icon.setPreferredSize(new Dimension(20, rowH));

        // Label
        JLabel lbl = new JLabel("  " + label);
        lbl.setFont(active ? F_NAV_SEL : isChild ? F_NAV_SUB : F_NAV);
        lbl.setForeground(active ? BLUE : isChild ? TEXT_MID : TEXT_DARK);

        inner.add(icon);
        inner.add(lbl);
        row.add(inner, BorderLayout.CENTER);

        row.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                if (!active) { row.setBackground(new Color(0xF4F7FF)); row.repaint(); }
            }
            @Override
            public void mouseExited(MouseEvent e) {
                if (!active) { row.setBackground(BG_WHITE); row.repaint(); }
            }
        });
        return row;
    }

    public static void main(String[] args) {
        try { UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName()); }
        catch (Exception ignored) {}
        SwingUtilities.invokeLater(VictoryaDashboard::new);
    }
}

// =============================================================================
//  Icon type IDs
// =============================================================================
class NavIcon {
    static final int TRANG_CHU   = 0;
    static final int PHONG       = 1;
    static final int DAT_PHONG   = 2;
    static final int GIA_HAN     = 3;
    static final int HUY_PHONG   = 4;
    static final int KHUYEN_MAI  = 5;
    static final int THONG_KE    = 6;
    static final int DICH_VU     = 7;
    static final int NHAN_VIEN   = 8;
    static final int TAI_KHOAN   = 9;
    static final int CA_LAM_VIEC = 10;
    static final int HOA_DON     = 11;
    static final int CAU_HINH    = 12;
    static final int LOGOUT      = 13;
}

// =============================================================================
//  Custom icon painter
// =============================================================================
class NavIconPanel extends JPanel {
    private final int type;
    private final Color col;
    private final int sz;

    NavIconPanel(int type, Color col, int sz) {
        this.type = type; this.col = col; this.sz = sz;
        setOpaque(false);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_PURE);
        g2.setColor(col);

        int cx = getWidth() / 2;
        int cy = getHeight() / 2;
        float r = sz / 2f;  // half-size radius

        g2.setStroke(new BasicStroke(1.55f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));

        switch (type) {

            // HOME / Trang chủ
            case NavIcon.TRANG_CHU -> {
                int roofH = (int)(r * 1.0f);
                int wallH = (int)(r * 0.9f);
                int halfW = (int)(r * 1.0f);
                // Roof
                g2.drawLine(cx - halfW, cy - roofH + wallH/2, cx, cy - roofH - wallH/2 + 2);
                g2.drawLine(cx, cy - roofH - wallH/2 + 2, cx + halfW, cy - roofH + wallH/2);
                // Wall
                int wx = cx - (int)(r * 0.75f), wy = cy - roofH + wallH/2;
                int ww = (int)(r * 1.5f), wh = (int)(r * 1.1f);
                g2.drawRect(wx, wy, ww, wh);
                // Door
                int dw = (int)(r * 0.42f), dh = (int)(r * 0.6f);
                int dx = cx - dw / 2, dy = wy + wh - dh;
                g2.drawRect(dx, dy, dw, dh);
            }

            // BED / Phòng
            case NavIcon.PHONG -> {
                int bx = cx - (int)r, by = cy - (int)(r * 0.3f);
                int bw = (int)(r * 2), bh = (int)(r * 0.9f);
                // Frame
                g2.drawRoundRect(bx, by, bw, bh, 4, 4);
                // Headboard line
                int hbl = bx + (int)(bw * 0.32f);
                g2.drawLine(hbl, by, hbl, by + bh);
                // Legs
                g2.drawLine(bx + 3, by + bh, bx + 3, by + bh + (int)(r * 0.4f));
                g2.drawLine(bx + bw - 3, by + bh, bx + bw - 3, by + bh + (int)(r * 0.4f));
                // Pillow
                g2.fillRoundRect(bx + 4, by + 3, (int)(bw * 0.24f), (int)(bh * 0.55f), 3, 3);
            }

            // PENCIL-DOC / Đặt Phòng
            case NavIcon.DAT_PHONG -> {
                // Document (slightly left)
                int dx = cx - (int)(r * 0.85f), dy = cy - (int)r;
                int dw = (int)(r * 1.15f), dh = (int)(r * 1.9f);
                g2.drawRoundRect(dx, dy, dw, dh, 3, 3);
                // Lines on doc
                g2.drawLine(dx + 3, dy + (int)(dh * 0.38f), dx + dw - 3, dy + (int)(dh * 0.38f));
                g2.drawLine(dx + 3, dy + (int)(dh * 0.58f), dx + dw - 3, dy + (int)(dh * 0.58f));
                g2.drawLine(dx + 3, dy + (int)(dh * 0.76f), dx + (int)(dw * 0.65f), dy + (int)(dh * 0.76f));
                // Pencil (bottom-right)
                int px = cx + (int)(r * 0.18f), py = cy + (int)(r * 0.05f);
                int pw = (int)(r * 0.36f), ph = (int)(r * 0.95f);
                g2.drawRoundRect(px, py, pw, ph, 2, 2);
                // Tip
                int[] tipx = {px, px + pw, px + pw/2};
                int[] tipy = {py + ph, py + ph, py + ph + (int)(r * 0.25f)};
                g2.drawPolyline(tipx, tipy, 3);
            }

            // CALENDAR / Gia Hạn
            case NavIcon.GIA_HAN -> {
                int cax = cx - (int)r, cay = cy - (int)(r * 0.85f);
                int caw = (int)(r * 2), cah = (int)(r * 1.85f);
                g2.drawRoundRect(cax, cay, caw, cah, 4, 4);
                // Top bar
                g2.drawLine(cax, cay + (int)(cah * 0.3f), cax + caw, cay + (int)(cah * 0.3f));
                // Hanger knobs
                int kw = 3, kh = (int)(r * 0.35f);
                g2.fillRoundRect(cx - (int)(r * 0.5f) - kw/2, cay - kh/2, kw, kh, 2, 2);
                g2.fillRoundRect(cx + (int)(r * 0.5f) - kw/2, cay - kh/2, kw, kh, 2, 2);
                // Grid dots
                for (int row = 0; row < 2; row++) {
                    for (int c = 0; c < 3; c++) {
                        int dotx = cax + (int)(caw * (c + 0.8f) / 3.8f);
                        int doty = cay + (int)(cah * (row + 1.5f) / 3.5f);
                        g2.fillOval(dotx - 2, doty - 2, 4, 4);
                    }
                }
            }

            // X / Hủy Phòng
            case NavIcon.HUY_PHONG -> {
                g2.drawOval(cx - (int)r, cy - (int)r, (int)(r * 2), (int)(r * 2));
                int d = (int)(r * 0.55f);
                g2.drawLine(cx - d, cy - d, cx + d, cy + d);
                g2.drawLine(cx + d, cy - d, cx - d, cy + d);
            }

            // TAG / Khuyến Mãi
            case NavIcon.KHUYEN_MAI -> {
                float hw = r * 1.05f, hh = r;
                Path2D tag = new Path2D.Float();
                tag.moveTo(cx - hw * 0.4f, cy - hh);
                tag.lineTo(cx + hw * 0.6f, cy - hh);
                tag.lineTo(cx + hw, cy);
                tag.lineTo(cx + hw * 0.6f, cy + hh);
                tag.lineTo(cx - hw * 0.4f, cy + hh);
                tag.lineTo(cx - hw * 0.4f, cy - hh);
                tag.closePath();
                g2.draw(tag);
                // Hole
                int hr2 = 3;
                g2.drawOval(cx - (int)(hw * 0.15f) - hr2, cy - hr2, hr2 * 2, hr2 * 2);
            }

            // DOLLAR-CIRCLE / Thống Kê
            case NavIcon.THONG_KE -> {
                g2.drawOval(cx - (int)r, cy - (int)r, (int)(r * 2), (int)(r * 2));
                // $ sign
                g2.setStroke(new BasicStroke(1.4f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
                // Vertical bar
                g2.drawLine(cx, cy - (int)(r * 0.6f), cx, cy + (int)(r * 0.6f));
                // Top arch
                g2.drawArc(cx - (int)(r * 0.42f), cy - (int)(r * 0.56f), (int)(r * 0.84f), (int)(r * 0.58f), 0, 200);
                // Bottom arch
                g2.drawArc(cx - (int)(r * 0.42f), cy - (int)(r * 0.06f), (int)(r * 0.84f), (int)(r * 0.58f), 180, 200);
            }

            // TOOLS / Dịch vụ
            case NavIcon.DICH_VU -> {
                // Wrench
                g2.setStroke(new BasicStroke(1.6f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
                int wr = (int)(r * 0.55f);
                int wx = cx - (int)(r * 0.55f), wy = cy - (int)r;
                g2.drawOval(wx, wy, wr * 2, wr * 2);
                // Handle diagonal
                int hx1 = wx + wr + (int)(wr * 0.45f), hy1 = wy + wr * 2 - (int)(wr * 0.45f);
                int hx2 = cx + (int)(r * 0.8f), hy2 = cy + (int)(r * 0.8f);
                g2.setStroke(new BasicStroke(3.5f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
                g2.drawLine(hx1, hy1, hx2, hy2);
                // Screwdriver cross
                g2.setStroke(new BasicStroke(1.5f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
                g2.drawLine(cx + (int)(r * 0.1f), cy - (int)(r * 0.8f), cx + (int)r, cy + (int)(r * 0.1f));
                g2.drawLine(cx - (int)(r * 0.3f), cy - (int)(r * 0.55f), cx + (int)(r * 0.7f), cy + (int)(r * 0.35f));
            }

            // PEOPLE / Nhân viên
            case NavIcon.NHAN_VIEN -> {
                g2.setStroke(new BasicStroke(1.5f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
                int hr = (int)(r * 0.38f);
                // Person 1 (right/back)
                g2.drawOval(cx + (int)(r * 0.1f), cy - (int)(r * 0.95f), hr * 2, hr * 2);
                g2.drawArc(cx - (int)(r * 0.2f), cy - (int)(r * 0.12f), (int)(r * 1.45f), (int)(r * 1.1f), 0, 180);
                // Person 2 (left/front)
                g2.setColor(new Color(col.getRed(), col.getGreen(), col.getBlue(), 210));
                g2.drawOval(cx - (int)(r * 0.55f), cy - (int)(r * 0.95f), hr * 2, hr * 2);
                g2.drawArc(cx - (int)r, cy - (int)(r * 0.12f), (int)(r * 1.45f), (int)(r * 1.1f), 0, 180);
            }

            // PERSON-CIRCLE / Tài Khoản
            case NavIcon.TAI_KHOAN -> {
                int hr2 = (int)(r * 0.44f);
                g2.drawOval(cx - hr2, cy - (int)r, hr2 * 2, hr2 * 2);
                // Shoulders arc
                g2.drawArc(cx - (int)r, cy - (int)(r * 0.06f), (int)(r * 2), (int)(r * 1.2f), 0, 180);
            }

            // CLOCK / Ca làm việc
            case NavIcon.CA_LAM_VIEC -> {
                g2.drawOval(cx - (int)r, cy - (int)r, (int)(r * 2), (int)(r * 2));
                // Hour hand
                g2.drawLine(cx, cy, cx + (int)(r * 0.5f), cy + (int)(r * 0.35f));
                // Minute hand
                g2.drawLine(cx, cy, cx, cy - (int)(r * 0.7f));
                // Center dot
                g2.fillOval(cx - 2, cy - 2, 4, 4);
            }

            // DOCUMENT-FOLD / Hóa đơn
            case NavIcon.HOA_DON -> {
                int ddx = cx - (int)(r * 0.72f), ddy = cy - (int)r;
                int ddw = (int)(r * 1.44f), ddh = (int)(r * 2);
                int fold = (int)(r * 0.48f);
                // Body (with fold corner)
                int[] px2 = {ddx, ddx + ddw - fold, ddx + ddw, ddx + ddw, ddx};
                int[] py2 = {ddy, ddy, ddy + fold, ddy + ddh, ddy + ddh};
                g2.drawPolyline(px2, py2, 5);
                g2.drawLine(ddx, ddy + ddh, ddx, ddy); // left side close
                // Fold crease
                g2.drawLine(ddx + ddw - fold, ddy, ddx + ddw - fold, ddy + fold);
                g2.drawLine(ddx + ddw - fold, ddy + fold, ddx + ddw, ddy + fold);
                // Lines
                g2.drawLine(ddx + 3, ddy + (int)(ddh * 0.45f), ddx + ddw - 3, ddy + (int)(ddh * 0.45f));
                g2.drawLine(ddx + 3, ddy + (int)(ddh * 0.62f), ddx + ddw - 3, ddy + (int)(ddh * 0.62f));
                g2.drawLine(ddx + 3, ddy + (int)(ddh * 0.79f), ddx + (int)(ddw * 0.6f), ddy + (int)(ddh * 0.79f));
            }

            // GEAR / Cấu hình
            case NavIcon.CAU_HINH -> {
                int teeth = 8;
                double outerR = r, innerR = r * 0.72, holeR = r * 0.35;
                Path2D gear = new Path2D.Float();
                for (int i = 0; i < teeth * 2; i++) {
                    double angle = Math.PI * i / teeth - Math.PI / 2;
                    double radius = (i % 2 == 0) ? outerR : innerR;
                    float px2 = (float)(cx + radius * Math.cos(angle));
                    float py2 = (float)(cy + radius * Math.sin(angle));
                    if (i == 0) gear.moveTo(px2, py2); else gear.lineTo(px2, py2);
                }
                gear.closePath();
                g2.setStroke(new BasicStroke(1.2f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
                g2.draw(gear);
                g2.drawOval((int)(cx - holeR), (int)(cy - holeR), (int)(holeR * 2), (int)(holeR * 2));
            }

            // DOOR-ARROW / Logout
            case NavIcon.LOGOUT -> {
                g2.setStroke(new BasicStroke(1.6f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
                // Door frame (left part)
                int dax = cx - (int)(r * 0.85f), day = cy - (int)r;
                int daw = (int)(r * 1.1f), dah = (int)(r * 2);
                g2.drawLine(dax + daw / 2, day, dax, day);
                g2.drawLine(dax, day, dax, day + dah);
                g2.drawLine(dax, day + dah, dax + daw / 2, day + dah);
                // Steps to indicate open door
                g2.drawLine(dax + daw / 2, day, dax + daw, day + (int)(r * 0.4f));
                g2.drawLine(dax + daw, day + (int)(r * 0.4f), dax + daw, day + dah - (int)(r * 0.4f));
                g2.drawLine(dax + daw, day + dah - (int)(r * 0.4f), dax + daw / 2, day + dah);
                // Door knob
                g2.fillOval(dax + daw - 5, cy - 2, 4, 4);
                // Arrow exit
                int ax = cx + (int)(r * 0.3f), ay = cy;
                g2.drawLine(ax - (int)(r * 0.1f), ay, cx + (int)r, ay);
                g2.drawLine(cx + (int)(r * 0.6f), ay - (int)(r * 0.38f), cx + (int)r, ay);
                g2.drawLine(cx + (int)(r * 0.6f), ay + (int)(r * 0.38f), cx + (int)r, ay);
            }
        }

        g2.dispose();
    }
}