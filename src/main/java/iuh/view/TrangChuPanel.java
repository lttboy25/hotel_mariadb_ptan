package iuh.view;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.*;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

import iuh.dto.NhanVienDTO;
import iuh.dto.ThongKeDTO;

public class TrangChuPanel extends JPanel {

    static final Color BG       = new Color(0xF8FAFC);
    static final Color WHITE    = Color.WHITE;
    static final Color PRIMARY  = new Color(0x1E40AF);
    static final Color SUCCESS  = new Color(0x059669);
    static final Color WARNING  = new Color(0xD97706);
    static final Color TEXT_D   = new Color(0x1E293B);
    static final Color TEXT_M   = new Color(0x64748B);
    static final Color BORDER   = new Color(0xE2E8F0);

    static final Font F_TITLE   = new Font("Segoe UI", Font.BOLD, 18);
    static final Font F_NUM     = new Font("Segoe UI", Font.BOLD, 32);
    static final Font F_PLAIN   = new Font("Segoe UI", Font.PLAIN, 14);
    static final Font F_BOLD14  = new Font("Segoe UI", Font.BOLD, 15);

    private NhanVienDTO nvSession;
    private JLabel lblCheckIn, lblCheckOut, lblTongPhong, lblTrong, lblDangO, lblDoanhThu;
    private JLabel lblEmpName, lblEmpEmail, lblEmpPhone, lblEmpRole;
    private JPanel chartContainer;
    private String avatarLetter = "?";
    private Runnable onSwitchToBooking;

    public TrangChuPanel(NhanVienDTO nv) {
        this.nvSession = nv;
        setupAvatar(nv);

        setLayout(new BorderLayout());
        setBackground(BG);

        add(buildHeader(), BorderLayout.NORTH);

        // --- CẤU TRÚC LẠI CONTENT PANEL ---
        JPanel contentPanel = new JPanel(new GridBagLayout());
        contentPanel.setBackground(BG);
        contentPanel.setBorder(new EmptyBorder(25, 35, 25, 35));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        gbc.gridx = 0;

        // Row 1: Thống kê nhanh
        gbc.gridy = 0;
        gbc.insets = new Insets(0, 0, 25, 0);
        contentPanel.add(buildQuickStatsRow(), gbc);

        // Row 2: Biểu đồ & Nhân viên (Phần này quan trọng nhất)
        gbc.gridy = 1;
        gbc.weighty = 1.0; // Cho phép phần này chiếm không gian còn lại
        gbc.fill = GridBagConstraints.BOTH;
        contentPanel.add(buildDetailsRow(), gbc);

        JScrollPane scroll = new JScrollPane(contentPanel);
        scroll.setBorder(null);
        scroll.getVerticalScrollBar().setUnitIncrement(16);
        add(scroll, BorderLayout.CENTER);

        refreshData();
    }

    private void setupAvatar(NhanVienDTO nv) {
        if (nv != null && nv.getTenNhanVien() != null) {
            String ten = nv.getTenNhanVien().trim();
            String[] parts = ten.split(" ");
            avatarLetter = String.valueOf(parts[parts.length - 1].charAt(0)).toUpperCase();
        }
    }

    public void setOnSwitchToBooking(Runnable callback) { this.onSwitchToBooking = callback; }

    public void refreshData() {
        try {
            LocalDate today = LocalDate.now();
            iuh.network.Request req = iuh.network.Request.builder()
                    .commandType(iuh.network.CommandType.LAY_THONG_KE)
                    .object(new LocalDate[]{today.withDayOfMonth(1), today})
                    .build();
            iuh.network.Response res = iuh.network.ClientConnection.getInstance().sendRequest(req);
            ThongKeDTO data = (ThongKeDTO) res.getObject();

            lblTongPhong.setText(String.valueOf(data.getTongSoPhong()));
            lblDangO.setText(String.valueOf(data.getSoPhongDangSuDung()));
            lblTrong.setText(String.valueOf(data.getTongSoPhong() - data.getSoPhongDangSuDung()));
            lblCheckIn.setText(String.valueOf(data.getSoKhachDangLuuTru()));
            lblCheckOut.setText(String.valueOf(data.getSoLuotHuyPhong()));
            lblDoanhThu.setText(new DecimalFormat("#,###đ").format(data.getTongDoanhThu()));

            if (nvSession != null) {
                lblEmpName.setText(nvSession.getTenNhanVien());
                lblEmpEmail.setText(nvSession.getEmail());
                lblEmpPhone.setText(nvSession.getSoDienThoai());
                lblEmpRole.setText(nvSession.getTaiKhoan() != null ? nvSession.getTaiKhoan().getVaiTro() : "Nhân viên");
            }
            updateChart(data.getDsDatPhongTheoThang());
        } catch (Exception e) { e.printStackTrace(); }
    }

    private JPanel buildHeader() {
        JPanel h = new JPanel(new BorderLayout());
        h.setBackground(WHITE);
        h.setPreferredSize(new Dimension(0, 80));
        h.setBorder(new MatteBorder(0, 0, 1, 0, BORDER));

        JLabel title = new JLabel("  Trang Chủ");
        title.setFont(new Font("Segoe UI", Font.BOLD, 22));
        title.setForeground(TEXT_D);

        JButton btnBooking = createStyledButton(" + Tạo Đặt Phòng Mới ", PRIMARY);
        btnBooking.addActionListener(e -> { if (onSwitchToBooking != null) onSwitchToBooking.run(); });

        JPanel right = new JPanel(new FlowLayout(FlowLayout.RIGHT, 25, 22));
        right.setOpaque(false);
        right.add(new JLabel(LocalDate.now().format(DateTimeFormatter.ofPattern("EEEE, dd/MM/yyyy"))));
        right.add(btnBooking);

        h.add(title, BorderLayout.WEST);
        h.add(right, BorderLayout.EAST);
        return h;
    }

    private JPanel buildQuickStatsRow() {
        JPanel row = new JPanel(new GridLayout(1, 4, 25, 0));
        row.setOpaque(false);
        lblCheckIn = new JLabel("0"); lblDangO = new JLabel("0"); lblTrong = new JLabel("0"); lblDoanhThu = new JLabel("0đ");
        row.add(createStatCard("Khách Đang Ở", lblCheckIn, PRIMARY));
        row.add(createStatCard("Phòng Đang Ở", lblDangO, SUCCESS));
        row.add(createStatCard("Phòng Trống", lblTrong, WARNING));
        row.add(createStatCard("Doanh Thu Tháng", lblDoanhThu, new Color(0x7C3AED)));
        return row;
    }


    private JPanel buildDetailsRow() {
        JPanel row = new JPanel(new GridBagLayout());
        row.setOpaque(false);
        GridBagConstraints gbc = new GridBagConstraints();

        // Biểu đồ (Bên trái)
        chartContainer = createCard();
        chartContainer.setLayout(new BorderLayout());
        chartContainer.setBorder(new EmptyBorder(25, 25, 25, 25));

        gbc.gridx = 0; gbc.gridy = 0;
        gbc.weightx = 0.65; gbc.weighty = 1.0;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.insets = new Insets(0, 0, 0, 25);
        row.add(chartContainer, gbc);

        // Cột thông tin (Bên phải)
        JPanel rightCol = new JPanel(new GridBagLayout());
        rightCol.setOpaque(false);
        GridBagConstraints gbcR = new GridBagConstraints();
        gbcR.fill = GridBagConstraints.BOTH;
        gbcR.weightx = 1.0;
        gbcR.gridx = 0;

        // Card Profile
        JPanel profileCard = createCard();
        profileCard.setLayout(new BorderLayout());
        profileCard.setBorder(new EmptyBorder(25, 25, 25, 25));

        JPanel avtCircle = new JPanel() {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(PRIMARY); g2.fillOval(0, 0, 60, 60);
                g2.setColor(WHITE); g2.setFont(new Font("Segoe UI", Font.BOLD, 26));
                FontMetrics fm = g2.getFontMetrics();
                g2.drawString(avatarLetter, 30 - fm.stringWidth(avatarLetter)/2, 40);
                g2.dispose();
            }
        };
        avtCircle.setPreferredSize(new Dimension(60, 60)); avtCircle.setOpaque(false);

        JPanel info = new JPanel();
        info.setLayout(new BoxLayout(info, BoxLayout.Y_AXIS));
        info.setOpaque(false);
        info.setBorder(new EmptyBorder(0, 20, 0, 0));
        lblEmpName = new JLabel("---"); lblEmpName.setFont(new Font("Segoe UI", Font.BOLD, 17));
        lblEmpRole = new JLabel("---"); lblEmpRole.setForeground(PRIMARY);
        lblEmpEmail = new JLabel("---"); lblEmpEmail.setForeground(TEXT_M);
        lblEmpPhone = new JLabel("---"); lblEmpPhone.setForeground(TEXT_M);
        info.add(lblEmpName); info.add(lblEmpRole); info.add(Box.createVerticalStrut(10));
        info.add(lblEmpEmail); info.add(lblEmpPhone);

        JPanel profileBody = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 15));
        profileBody.setOpaque(false);
        profileBody.add(avtCircle); profileBody.add(info);

        profileCard.add(lbl("Nhân viên trực ca", F_TITLE, TEXT_D), BorderLayout.NORTH);
        profileCard.add(profileBody, BorderLayout.CENTER);

        gbcR.gridy = 0; gbcR.weighty = 0;
        gbcR.insets = new Insets(0, 0, 20, 0);
        rightCol.add(profileCard, gbcR);

        // Card thông số hệ thống
        lblTongPhong = new JLabel("0"); lblCheckOut = new JLabel("0");
        JPanel sysInfo = createCard();
        sysInfo.setLayout(new GridLayout(2, 1, 0, 15));
        sysInfo.setBorder(new EmptyBorder(25, 25, 25, 25));
        sysInfo.add(createMiniInfo("Tổng số lượng phòng:", lblTongPhong));
        sysInfo.add(createMiniInfo("Lượt hủy trong tháng:", lblCheckOut));

        gbcR.gridy = 1; gbcR.weighty = 1;
        gbcR.insets = new Insets(0, 0, 0, 0);
        rightCol.add(sysInfo, gbcR);

        gbc.gridx = 1; gbc.weightx = 0.35;
        gbc.insets = new Insets(0, 0, 0, 0);
        row.add(rightCol, gbc);

        return row;
    }

    private JPanel createStatCard(String title, JLabel value, Color accent) {
        JPanel p = createCard();
        p.setLayout(new BorderLayout());

        // Tạo một panel đệm bên trong để đẩy nội dung cách xa thanh màu decor
        JPanel contentWrapper = new JPanel(new BorderLayout());
        contentWrapper.setOpaque(false);
        contentWrapper.setBorder(new EmptyBorder(20, 25, 20, 15)); // 25 là khoảng cách với thanh màu

        JPanel top = new JPanel(new BorderLayout());
        top.setOpaque(false);
        top.add(lbl(title, F_PLAIN, TEXT_M), BorderLayout.WEST);

        value.setFont(F_NUM);
        value.setForeground(TEXT_D);

        contentWrapper.add(top, BorderLayout.NORTH);
        contentWrapper.add(value, BorderLayout.CENTER);

        // Thanh màu bên trái
        JPanel decor = new JPanel();
        decor.setBackground(accent);
        decor.setPreferredSize(new Dimension(6, 0)); // Tăng độ dày thanh màu một chút cho đẹp

        p.add(decor, BorderLayout.WEST);
        p.add(contentWrapper, BorderLayout.CENTER);

        return p;
    }
    private JPanel createMiniInfo(String label, JLabel val) {
        JPanel p = new JPanel(new BorderLayout());
        p.setOpaque(false);
        p.add(lbl(label, F_PLAIN, TEXT_M), BorderLayout.WEST);
        val.setFont(F_BOLD14);
        p.add(val, BorderLayout.EAST);
        return p;
    }

    private JPanel createCard() {
        return new JPanel() {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(WHITE); g2.fillRoundRect(0, 0, getWidth(), getHeight(), 15, 15);
                g2.setColor(BORDER); g2.drawRoundRect(0, 0, getWidth()-1, getHeight()-1, 15, 15);
                g2.dispose();
            }
        };
    }

    private JButton createStyledButton(String text, Color bg) {
        JButton b = new JButton(text) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                if (getModel().isPressed()) {
                    g2.setColor(bg.darker());
                } else if (getModel().isRollover()) {
                    g2.setColor(bg.brighter());
                } else {
                    g2.setColor(bg);
                }
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 10, 10);
                g2.dispose();
                super.paintComponent(g);
            }
        };
        b.setFont(F_BOLD14);
        b.setForeground(Color.WHITE);
        b.setContentAreaFilled(false);
        b.setBorderPainted(false);
        b.setFocusPainted(false);
        b.setCursor(new Cursor(Cursor.HAND_CURSOR));
        b.setBorder(new EmptyBorder(10, 20, 10, 20));
        return b;
    }

    private void updateChart(List<ThongKeDTO.DuLieuCotDTO> dataList) {
        if (dataList == null || dataList.isEmpty()) return;
        int n = dataList.size();
        int[] vals = new int[n]; String[] labels = new String[n];
        double max = dataList.stream().mapToDouble(ThongKeDTO.DuLieuCotDTO::getGiaTri).max().orElse(1.0);
        for (int i = 0; i < n; i++) {
            labels[i] = dataList.get(i).getNhan();
            vals[i] = (int) ((dataList.get(i).getGiaTri() / max) * 100);
        }
        chartContainer.removeAll();
        chartContainer.add(lbl("Thống kê lượt đặt phòng gần đây", F_TITLE, TEXT_D), BorderLayout.NORTH);
        chartContainer.add(new MiniBarChart(vals, labels), BorderLayout.CENTER);
        chartContainer.revalidate(); chartContainer.repaint();
    }

    static JLabel lbl(String t, Font f, Color c) {
        JLabel l = new JLabel(t); l.setFont(f); l.setForeground(c); return l;
    }
}

class MiniBarChart extends JPanel {
    int[] vals; String[] labels;
    MiniBarChart(int[] vals, String[] labels) { this.vals = vals; this.labels = labels; setOpaque(false); }
    @Override protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        int w = getWidth(), h = getHeight();
        int padL = 45, padR = 15, padT = 20, padB = 35;
        int cw = w - padL - padR, ch = h - padT - padB;
        g2.setFont(new Font("Segoe UI", Font.PLAIN, 10));

        for (int p = 0; p <= 100; p += 25) {
            int y = padT + ch - (int)(ch * p / 100.0);
            g2.setColor(new Color(0xE2E8F0));
            g2.drawLine(padL, y, w - padR, y);
            g2.setColor(new Color(0x64748B));
            g2.drawString(p + "%", 5, y + 5);
        }

        if (vals.length == 0) return;
        int n = vals.length;
        int barW = Math.max(20, (cw / n) - 20);
        int gap = (cw - barW * n) / (n + 1);

        for (int i = 0; i < n; i++) {
            int bh = (int)(ch * vals[i] / 100.0);
            int bx = padL + gap + i * (barW + gap);
            int by = padT + ch - bh;
            g2.setColor(new Color(0x3B82F6));
            g2.fill(new RoundRectangle2D.Float(bx, by, barW, bh, 8, 8));
            g2.setColor(new Color(0x0F172A));
            String label = labels[i];
            g2.drawString(label, bx + barW/2 - g2.getFontMetrics().stringWidth(label)/2, h - 10);
        }
        g2.dispose();
    }
}