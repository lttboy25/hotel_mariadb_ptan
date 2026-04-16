package iuh.view;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.*;
import java.util.ArrayList;
import java.util.List;

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
    private String activeCard = "trangchu";

    // Track all nav rows for highlight switching
    private final List<NavRowEntry> navRows = new ArrayList<>();

    public VictoryaDashboard() {
        setTitle("Victorya - Dashboard");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1280, 780);
        setLocationRelativeTo(null);
        setResizable(true);

        JPanel root = new JPanel(new BorderLayout());
        root.setBackground(BG_MAIN);

        // Content cards
        cardLayout = new CardLayout();
        contentCards = new JPanel(cardLayout);
        contentCards.setBackground(BG_MAIN);
        contentCards.add(new TrangChuPanel(), "trangchu");
        contentCards.add(new ThongKePanel(),  "thongke");
        contentCards.add(new DatPhongPanel(), "datphong");
        contentCards.add(new HuyPhongPanel(), "huyphong");
        contentCards.add(new DoiPhongPanel(), "doiphong");
        contentCards.add(new TaiKhoanPanel(), "taikhoan");
        contentCards.add(new QuanLyKhachHangPanel(), "quanlykhachhang");
        contentCards.add(new QuanLyNhanVienPanel(), "quanlynhanvien");
        contentCards.add(new QuanLyDichVuPanel(), "quanlydichvu");
        contentCards.add(new QuanLyHoaDonPanel(), "quanlyhoadon");
        contentCards.add(new QuanLyKhuyenMaiPanel(), "quanlykhuyenmai");
        contentCards.add(new GiaHanPhongPanel(), "giahanphong");


        // Sidebar (built after contentCards so it can reference it)
        JPanel sidebar = buildSidebar();
        sidebar.setPreferredSize(new Dimension(228, 0));

        root.add(sidebar, BorderLayout.WEST);
        root.add(contentCards, BorderLayout.CENTER);

        // Show trang chu first
        cardLayout.show(contentCards, "trangchu");

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

        // Nav rows
        sidebar.add(navRow(NavIcon.TRANG_CHU,  "Trang chủ",         0, true,  "trangchu"));
        // Phòng expand group
        sidebar.add(buildPhongGroup(sidebar));
        sidebar.add(navRow(NavIcon.KHUYEN_MAI, "Khuyến Mãi",        0, false, "quanlykhuyenmai"));
        sidebar.add(navRow(NavIcon.THONG_KE,   "Thống Kê",          0, false, "thongke"));
        sidebar.add(navRow(NavIcon.CA_LAM_VIEC,"Ca làm việc",       0, false, null));
        sidebar.add(navRow(NavIcon.TAI_KHOAN,  "Tài Khoản",         0, false, "taikhoan"));
        sidebar.add(navRow(NavIcon.KHACH_HANG, "Quản lý Khách hàng", 0, false, "quanlykhachhang"));
        sidebar.add(navRow(NavIcon.NHAN_VIEN,  "Quản lý nhân viên", 0, false, "quanlynhanvien"));
        sidebar.add(navRow(NavIcon.DICH_VU,    "Quản lý dịch vụ",   0, false, "quanlydichvu"));
        sidebar.add(navRow(NavIcon.HOA_DON,    "Quản lý hóa đơn",   0, false, "quanlyhoadon"));

        sidebar.add(Box.createVerticalGlue());
        sidebar.add(navRow(NavIcon.CAU_HINH, "Cấu hình hệ thống", 0, false, null));
        sidebar.add(navRow(NavIcon.LOGOUT,   "Logout",             0, false, null));
        sidebar.add(Box.createVerticalStrut(14));

        return sidebar;
    }

    // Phòng expandable group
    private JPanel buildPhongGroup(JPanel sidebar) {
        JPanel group = new JPanel();
        group.setLayout(new BoxLayout(group, BoxLayout.Y_AXIS));
        group.setOpaque(false);
        group.setMaximumSize(new Dimension(228, 800));

        boolean[] expanded = {false};

        // Child rows (initially hidden)
        Object[][] children = {
                {NavIcon.DAT_PHONG, "Đặt Phòng",     "datphong"},
                {NavIcon.GIA_HAN,   "Gia Hạn Phòng", "giahanphong"},
                {NavIcon.HUY_PHONG, "Hủy Phòng",     "huyphong"},
                {NavIcon.DAT_PHONG, "Đổi phòng",     "doiphong"},
        };

        JPanel[] childPanels = new JPanel[children.length];
        for (int i = 0; i < children.length; i++) {
            childPanels[i] = navRow((int)children[i][0], (String)children[i][1], 1, false, (String)children[i][2]);
            childPanels[i].setVisible(false);
            group.add(childPanels[i]);
        }

        // Parent row (with arrow)
        boolean[] parentActive = {false};
        JPanel parentRow = new JPanel(new BorderLayout()) {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(parentActive[0] ? BLUE_LIGHT : getBackground());
                g2.fillRect(0, 0, getWidth(), getHeight());
                if (parentActive[0]) { g2.setColor(BLUE); g2.fillRect(0, 0, 3, getHeight()); }
                g2.dispose(); super.paintComponent(g);
            }
        };
        parentRow.setOpaque(false);
        parentRow.setBackground(BG_WHITE);
        parentRow.setMaximumSize(new Dimension(228, 42));
        parentRow.setPreferredSize(new Dimension(228, 42));
        parentRow.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        JPanel inner = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        inner.setOpaque(false);
        inner.setBorder(new EmptyBorder(0, 16, 0, 0));
        NavIconPanel phongIcon = new NavIconPanel(NavIcon.PHONG, TEXT_MID, 16);
        phongIcon.setPreferredSize(new Dimension(20, 42));
        JLabel phongLbl = new JLabel("  Phòng");
        phongLbl.setFont(F_NAV);
        phongLbl.setForeground(TEXT_DARK);
        inner.add(phongIcon);
        inner.add(phongLbl);
        parentRow.add(inner, BorderLayout.CENTER);

        // Arrow indicator
        JLabel arrow = new JLabel("›") {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(TEXT_GRAY);
                g2.setFont(new Font("Segoe UI", Font.PLAIN, 14));
                // Rotate 90 if expanded
                if (expanded[0]) {
                    g2.rotate(Math.PI/2, getWidth()/2.0, getHeight()/2.0);
                }
                FontMetrics fm = g2.getFontMetrics();
                g2.drawString("›", getWidth()/2 - fm.stringWidth("›")/2, getHeight()/2 + fm.getAscent()/2 - 1);
                g2.dispose();
            }
        };
        arrow.setPreferredSize(new Dimension(20, 42));
        parentRow.add(arrow, BorderLayout.EAST);
        parentRow.setBorder(new EmptyBorder(0, 0, 0, 8));

        // Click to toggle children
        MouseAdapter toggle = new MouseAdapter() {
            @Override public void mouseClicked(MouseEvent e) {
                expanded[0] = !expanded[0];
                for (JPanel child : childPanels) child.setVisible(expanded[0]);
                group.revalidate();
                group.repaint();
                arrow.repaint();
                sidebar.revalidate();
                sidebar.repaint();
            }
            @Override public void mouseEntered(MouseEvent e) {
                if (!parentActive[0]) { parentRow.setBackground(new Color(0xF4F7FF)); parentRow.repaint(); }
            }
            @Override public void mouseExited(MouseEvent e) {
                if (!parentActive[0]) { parentRow.setBackground(BG_WHITE); parentRow.repaint(); }
            }
        };
        parentRow.addMouseListener(toggle);

        // Insert parent row at top of group
        group.add(parentRow, 0);

        // Register parent in navRows so it can be deactivated
        navRows.add(new NavRowEntry(parentRow, phongIcon, phongLbl, parentActive, false, null));

        return group;
    }

    private JPanel navRow(int iconType, String label, int indent, boolean initActive, String cardKey) {
        boolean isChild = indent > 0;
        int rowH = isChild ? 36 : 42;

        // Mutable active state via array trick
        boolean[] active = { initActive };

        JPanel row = new JPanel(new BorderLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                if (active[0]) {
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
        if (cardKey != null) row.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        JPanel inner = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        inner.setOpaque(false);
        inner.setBorder(new EmptyBorder(0, 16 + indent * 20, 0, 0));

        NavIconPanel icon = new NavIconPanel(iconType, active[0] ? BLUE : TEXT_MID, 16);
        icon.setPreferredSize(new Dimension(20, rowH));

        JLabel lbl = new JLabel("  " + label);
        lbl.setFont(active[0] ? F_NAV_SEL : isChild ? F_NAV_SUB : F_NAV);
        lbl.setForeground(active[0] ? BLUE : isChild ? TEXT_MID : TEXT_DARK);

        inner.add(icon);
        inner.add(lbl);
        row.add(inner, BorderLayout.CENTER);

        // Register for selection management
        navRows.add(new NavRowEntry(row, icon, lbl, active, isChild, cardKey));

        // Click handler
        if (cardKey != null) {
            row.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    // Deactivate all
                    for (NavRowEntry entry : navRows) {
                        entry.active[0] = false;
                        entry.icon.setColor(TEXT_MID);
                        entry.lbl.setFont(entry.isChild ? F_NAV_SUB : F_NAV);
                        entry.lbl.setForeground(entry.isChild ? TEXT_MID : TEXT_DARK);
                        entry.row.setBackground(BG_WHITE);
                        entry.row.repaint();
                    }
                    // Activate this
                    active[0] = true;
                    icon.setColor(BLUE);
                    lbl.setFont(F_NAV_SEL);
                    lbl.setForeground(BLUE);
                    row.repaint();
                    // Switch content
                    cardLayout.show(contentCards, cardKey);
                }

                @Override
                public void mouseEntered(MouseEvent e) {
                    if (!active[0]) { row.setBackground(new Color(0xF4F7FF)); row.repaint(); }
                }

                @Override
                public void mouseExited(MouseEvent e) {
                    if (!active[0]) { row.setBackground(BG_WHITE); row.repaint(); }
                }
            });
        } else {
            row.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseEntered(MouseEvent e) {
                    if (!active[0]) { row.setBackground(new Color(0xF4F7FF)); row.repaint(); }
                }
                @Override
                public void mouseExited(MouseEvent e) {
                    if (!active[0]) { row.setBackground(BG_WHITE); row.repaint(); }
                }
            });
        }

        return row;
    }

    // Simple record for nav row state
    static class NavRowEntry {
        JPanel row; NavIconPanel icon; JLabel lbl; boolean[] active; boolean isChild; String cardKey;
        NavRowEntry(JPanel row, NavIconPanel icon, JLabel lbl, boolean[] active, boolean isChild, String cardKey) {
            this.row = row; this.icon = icon; this.lbl = lbl; this.active = active;
            this.isChild = isChild; this.cardKey = cardKey;
        }
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
    static final int KHACH_HANG  = 14;
}

// =============================================================================
//  Custom icon painter
// =============================================================================
class NavIconPanel extends JPanel {
    private final int type;
    private Color col;
    private final int sz;

    NavIconPanel(int type, Color col, int sz) {
        this.type = type; this.col = col; this.sz = sz;
        setOpaque(false);
    }

    void setColor(Color c) { this.col = c; repaint(); }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_PURE);
        g2.setColor(col);

        int cx = getWidth() / 2;
        int cy = getHeight() / 2;
        float r = sz / 2f;

        g2.setStroke(new BasicStroke(1.55f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));

        switch (type) {
            case NavIcon.TRANG_CHU -> {
                int roofH = (int)(r * 1.0f), wallH = (int)(r * 0.9f), halfW = (int)(r * 1.0f);
                g2.drawLine(cx - halfW, cy - roofH + wallH/2, cx, cy - roofH - wallH/2 + 2);
                g2.drawLine(cx, cy - roofH - wallH/2 + 2, cx + halfW, cy - roofH + wallH/2);
                int wx = cx - (int)(r * 0.75f), wy = cy - roofH + wallH/2;
                int ww = (int)(r * 1.5f), wh = (int)(r * 1.1f);
                g2.drawRect(wx, wy, ww, wh);
                int dw = (int)(r * 0.42f), dh = (int)(r * 0.6f);
                int dx = cx - dw / 2, dy = wy + wh - dh;
                g2.drawRect(dx, dy, dw, dh);
            }
            case NavIcon.PHONG -> {
                int bx = cx - (int)r, by = cy - (int)(r * 0.3f);
                int bw = (int)(r * 2), bh = (int)(r * 0.9f);
                g2.drawRoundRect(bx, by, bw, bh, 4, 4);
                int hbl = bx + (int)(bw * 0.32f);
                g2.drawLine(hbl, by, hbl, by + bh);
                g2.drawLine(bx + 3, by + bh, bx + 3, by + bh + (int)(r * 0.4f));
                g2.drawLine(bx + bw - 3, by + bh, bx + bw - 3, by + bh + (int)(r * 0.4f));
                g2.fillRoundRect(bx + 4, by + 3, (int)(bw * 0.24f), (int)(bh * 0.55f), 3, 3);
            }
            case NavIcon.DAT_PHONG -> {
                int dx = cx - (int)(r * 0.85f), dy = cy - (int)r;
                int dw = (int)(r * 1.15f), dh = (int)(r * 1.9f);
                g2.drawRoundRect(dx, dy, dw, dh, 3, 3);
                g2.drawLine(dx + 3, dy + (int)(dh * 0.38f), dx + dw - 3, dy + (int)(dh * 0.38f));
                g2.drawLine(dx + 3, dy + (int)(dh * 0.58f), dx + dw - 3, dy + (int)(dh * 0.58f));
                g2.drawLine(dx + 3, dy + (int)(dh * 0.76f), dx + (int)(dw * 0.65f), dy + (int)(dh * 0.76f));
                int px = cx + (int)(r * 0.18f), py = cy + (int)(r * 0.05f);
                int pw = (int)(r * 0.36f), ph = (int)(r * 0.95f);
                g2.drawRoundRect(px, py, pw, ph, 2, 2);
                int[] tipx = {px, px + pw, px + pw/2};
                int[] tipy = {py + ph, py + ph, py + ph + (int)(r * 0.25f)};
                g2.drawPolyline(tipx, tipy, 3);
            }
            case NavIcon.GIA_HAN -> {
                int cax = cx - (int)r, cay = cy - (int)(r * 0.85f);
                int caw = (int)(r * 2), cah = (int)(r * 1.85f);
                g2.drawRoundRect(cax, cay, caw, cah, 4, 4);
                g2.drawLine(cax, cay + (int)(cah * 0.3f), cax + caw, cay + (int)(cah * 0.3f));
                int kw = 3, kh = (int)(r * 0.35f);
                g2.fillRoundRect(cx - (int)(r * 0.5f) - kw/2, cay - kh/2, kw, kh, 2, 2);
                g2.fillRoundRect(cx + (int)(r * 0.5f) - kw/2, cay - kh/2, kw, kh, 2, 2);
                for (int row = 0; row < 2; row++)
                    for (int c = 0; c < 3; c++) {
                        int dotx = cax + (int)(caw * (c + 0.8f) / 3.8f);
                        int doty = cay + (int)(cah * (row + 1.5f) / 3.5f);
                        g2.fillOval(dotx - 2, doty - 2, 4, 4);
                    }
            }
            case NavIcon.HUY_PHONG -> {
                g2.drawOval(cx - (int)r, cy - (int)r, (int)(r * 2), (int)(r * 2));
                int d = (int)(r * 0.55f);
                g2.drawLine(cx - d, cy - d, cx + d, cy + d);
                g2.drawLine(cx + d, cy - d, cx - d, cy + d);
            }
            case NavIcon.KHUYEN_MAI -> {
                float hw = r * 1.05f, hh = r;
                Path2D tag = new Path2D.Float();
                tag.moveTo(cx - hw * 0.4f, cy - hh);
                tag.lineTo(cx + hw * 0.6f, cy - hh);
                tag.lineTo(cx + hw, cy);
                tag.lineTo(cx + hw * 0.6f, cy + hh);
                tag.lineTo(cx - hw * 0.4f, cy + hh);
                tag.closePath();
                g2.draw(tag);
                int hr2 = 3;
                g2.drawOval(cx - (int)(hw * 0.15f) - hr2, cy - hr2, hr2 * 2, hr2 * 2);
            }
            case NavIcon.THONG_KE -> {
                g2.drawOval(cx - (int)r, cy - (int)r, (int)(r * 2), (int)(r * 2));
                g2.setStroke(new BasicStroke(1.4f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
                g2.drawLine(cx, cy - (int)(r * 0.6f), cx, cy + (int)(r * 0.6f));
                g2.drawArc(cx - (int)(r * 0.42f), cy - (int)(r * 0.56f), (int)(r * 0.84f), (int)(r * 0.58f), 0, 200);
                g2.drawArc(cx - (int)(r * 0.42f), cy - (int)(r * 0.06f), (int)(r * 0.84f), (int)(r * 0.58f), 180, 200);
            }
            case NavIcon.DICH_VU -> {
                g2.setStroke(new BasicStroke(1.6f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
                int wr = (int)(r * 0.55f);
                int wx = cx - (int)(r * 0.55f), wy = cy - (int)r;
                g2.drawOval(wx, wy, wr * 2, wr * 2);
                int hx1 = wx + wr + (int)(wr * 0.45f), hy1 = wy + wr * 2 - (int)(wr * 0.45f);
                g2.setStroke(new BasicStroke(3.5f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
                g2.drawLine(hx1, hy1, cx + (int)(r * 0.8f), cy + (int)(r * 0.8f));
                g2.setStroke(new BasicStroke(1.5f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
                g2.drawLine(cx + (int)(r * 0.1f), cy - (int)(r * 0.8f), cx + (int)r, cy + (int)(r * 0.1f));
                g2.drawLine(cx - (int)(r * 0.3f), cy - (int)(r * 0.55f), cx + (int)(r * 0.7f), cy + (int)(r * 0.35f));
            }
            case NavIcon.NHAN_VIEN -> {
                // Person + small ID badge bottom-right to distinguish from KHACH_HANG
                g2.setStroke(new BasicStroke(1.5f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
                int hr = (int)(r * 0.42f);
                // Head
                g2.drawOval(cx - hr, cy - (int)r, hr * 2, hr * 2);
                // Shoulders
                g2.drawArc(cx - (int)(r * 0.95f), cy - (int)(r * 0.1f), (int)(r * 1.9f), (int)(r * 1.2f), 0, 180);
                // ID badge (small rect bottom-right)
                int bx = cx + (int)(r * 0.35f), by = cy + (int)(r * 0.5f);
                int bw = (int)(r * 0.7f), bh = (int)(r * 0.55f);
                g2.setStroke(new BasicStroke(1.3f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
                g2.drawRoundRect(bx, by, bw, bh, 3, 3);
                g2.drawLine(bx + 3, by + (int)(bh * 0.38f), bx + bw - 3, by + (int)(bh * 0.38f));
                g2.drawLine(bx + 3, by + (int)(bh * 0.65f), bx + (int)(bw * 0.6f), by + (int)(bh * 0.65f));
                // Small circle on badge
                g2.fillOval(bx - 4, by + (int)(bh * 0.1f), 7, 7);
            }
            case NavIcon.TAI_KHOAN -> {
                int hr2 = (int)(r * 0.44f);
                g2.drawOval(cx - hr2, cy - (int)r, hr2 * 2, hr2 * 2);
                g2.drawArc(cx - (int)r, cy - (int)(r * 0.06f), (int)(r * 2), (int)(r * 1.2f), 0, 180);
            }
            case NavIcon.CA_LAM_VIEC -> {
                g2.drawOval(cx - (int)r, cy - (int)r, (int)(r * 2), (int)(r * 2));
                g2.drawLine(cx, cy, cx + (int)(r * 0.5f), cy + (int)(r * 0.35f));
                g2.drawLine(cx, cy, cx, cy - (int)(r * 0.7f));
                g2.fillOval(cx - 2, cy - 2, 4, 4);
            }
            case NavIcon.HOA_DON -> {
                int ddx = cx - (int)(r * 0.72f), ddy = cy - (int)r;
                int ddw = (int)(r * 1.44f), ddh = (int)(r * 2);
                int fold = (int)(r * 0.48f);
                int[] px2 = {ddx, ddx + ddw - fold, ddx + ddw, ddx + ddw, ddx};
                int[] py2 = {ddy, ddy, ddy + fold, ddy + ddh, ddy + ddh};
                g2.drawPolyline(px2, py2, 5);
                g2.drawLine(ddx, ddy + ddh, ddx, ddy);
                g2.drawLine(ddx + ddw - fold, ddy, ddx + ddw - fold, ddy + fold);
                g2.drawLine(ddx + ddw - fold, ddy + fold, ddx + ddw, ddy + fold);
                g2.drawLine(ddx + 3, ddy + (int)(ddh * 0.45f), ddx + ddw - 3, ddy + (int)(ddh * 0.45f));
                g2.drawLine(ddx + 3, ddy + (int)(ddh * 0.62f), ddx + ddw - 3, ddy + (int)(ddh * 0.62f));
                g2.drawLine(ddx + 3, ddy + (int)(ddh * 0.79f), ddx + (int)(ddw * 0.6f), ddy + (int)(ddh * 0.79f));
            }
            case NavIcon.KHACH_HANG -> {
                // Person + star to distinguish from NHAN_VIEN
                g2.setStroke(new BasicStroke(1.5f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
                int hr = (int)(r * 0.40f);
                // Head (shifted slightly left)
                g2.drawOval(cx - hr - (int)(r*0.1f), cy - (int)r, hr * 2, hr * 2);
                // Shoulders
                g2.drawArc(cx - (int)(r * 0.95f), cy - (int)(r * 0.1f), (int)(r * 1.9f), (int)(r * 1.2f), 0, 180);
                // Star top-right (5-point mini star)
                int sx = cx + (int)(r * 0.5f), sy = cy - (int)(r * 0.85f);
                float sr = r * 0.32f, sr2 = sr * 0.45f;
                java.awt.geom.Path2D star = new java.awt.geom.Path2D.Float();
                for (int i = 0; i < 10; i++) {
                    double angle = Math.PI * i / 5 - Math.PI / 2;
                    float rad = (i % 2 == 0) ? sr : sr2;
                    float px = sx + (float)(rad * Math.cos(angle));
                    float py = sy + (float)(rad * Math.sin(angle));
                    if (i == 0) star.moveTo(px, py); else star.lineTo(px, py);
                }
                star.closePath();
                g2.fill(star);
            }
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
            case NavIcon.LOGOUT -> {
                g2.setStroke(new BasicStroke(1.6f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
                int dax = cx - (int)(r * 0.85f), day = cy - (int)r;
                int daw = (int)(r * 1.1f), dah = (int)(r * 2);
                g2.drawLine(dax + daw / 2, day, dax, day);
                g2.drawLine(dax, day, dax, day + dah);
                g2.drawLine(dax, day + dah, dax + daw / 2, day + dah);
                g2.drawLine(dax + daw / 2, day, dax + daw, day + (int)(r * 0.4f));
                g2.drawLine(dax + daw, day + (int)(r * 0.4f), dax + daw, day + dah - (int)(r * 0.4f));
                g2.drawLine(dax + daw, day + dah - (int)(r * 0.4f), dax + daw / 2, day + dah);
                g2.fillOval(dax + daw - 5, cy - 2, 4, 4);
                int ax = cx + (int)(r * 0.3f), ay = cy;
                g2.drawLine(ax - (int)(r * 0.1f), ay, cx + (int)r, ay);
                g2.drawLine(cx + (int)(r * 0.6f), ay - (int)(r * 0.38f), cx + (int)r, ay);
                g2.drawLine(cx + (int)(r * 0.6f), ay + (int)(r * 0.38f), cx + (int)r, ay);
            }
        }
        g2.dispose();
    }
}