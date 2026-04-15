package iuh.view;

import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.*;
import java.awt.image.BufferedImage;

/**
 * ThanhToanPanel - Giao diện trang Thanh Toán
 * Gồm 2 phần chính:
 * - Bên trái: Tìm kiếm theo CCCD + bảng thông tin phòng + tổng tiền
 * - Bên phải: Chọn phương thức thanh toán (Tiền mặt / Momo) + QR code + nút
 * Thanh Toán
 */
public class ThanhToanPanel extends JPanel {

    // ── Màu sắc chủ đạo (kế thừa từ VictoryaDashboard) ──────────────────────
    private static final Color BG_MAIN = new Color(0xF4F6FB);
    private static final Color BG_WHITE = Color.WHITE;
    private static final Color BLUE = new Color(0x3B6FF0);
    private static final Color BLUE_LIGHT = new Color(0xEBF0FF);
    private static final Color TEXT_DARK = new Color(0x1A1A2E);
    private static final Color TEXT_MID = new Color(0x4A5268);
    private static final Color TEXT_GRAY = new Color(0xA0A8B8);
    private static final Color BORDER_COL = new Color(0xE4E9F2);
    private static final Color GREEN = new Color(0x27AE60);
    private static final Color GREEN_DARK = new Color(0x1E8449);
    private static final Color ORANGE = new Color(0xF39C12);

    // ── Font ─────────────────────────────────────────────────────────────────
    private static final Font F_TITLE = new Font("Segoe UI", Font.BOLD, 18);
    private static final Font F_LABEL = new Font("Segoe UI", Font.PLAIN, 13);
    private static final Font F_BOLD13 = new Font("Segoe UI", Font.BOLD, 13);
    private static final Font F_BOLD14 = new Font("Segoe UI", Font.BOLD, 14);
    private static final Font F_BOLD16 = new Font("Segoe UI", Font.BOLD, 16);
    private static final Font F_SMALL = new Font("Segoe UI", Font.PLAIN, 12);
    private static final Font F_TABLE = new Font("Segoe UI", Font.PLAIN, 13);
    private static final Font F_TABLE_H = new Font("Segoe UI", Font.BOLD, 13);

    // ── Dữ liệu mẫu cho bảng ─────────────────────────────────────────────────
    private static final Object[][] SAMPLE_DATA = {
            { "101", "Thường", "Ăn sáng", "36 giờ", "1.000.000" },
            { "201", "VIP", "Tất cả dịch vụ", "36 giờ", "1.000.000" },
            { "301", "VIP", "Tất cả dịch vụ", "8 giờ", "300.000" },
    };

    private static final String[] COLUMNS = {
            "Phòng", "Loại Phòng", "Dịch vụ", "Thời gian lưu trú", "Tổng tiền"
    };

    public ThanhToanPanel() {
        setLayout(new BorderLayout(0, 0));
        setBackground(BG_MAIN);
        setBorder(new EmptyBorder(28, 28, 28, 28));

        // Chia layout thành 2 cột: trái (bảng) và phải (thanh toán)
        JPanel mainRow = new JPanel(new GridBagLayout());
        mainRow.setBackground(BG_MAIN);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.BOTH;
        gbc.insets = new Insets(0, 0, 0, 16);
        gbc.gridy = 0;
        gbc.weighty = 1.0;

        // ── Cột trái ──
        gbc.gridx = 0;
        gbc.weightx = 0.65;
        mainRow.add(buildLeftPanel(), gbc);

        // ── Cột phải ──
        gbc.gridx = 1;
        gbc.weightx = 0.35;
        gbc.insets = new Insets(0, 0, 0, 0);
        mainRow.add(buildRightPanel(), gbc);

        add(mainRow, BorderLayout.CENTER);
    }

    // =========================================================================
    // PANEL TRÁI – Tìm kiếm + Bảng + Tổng tiền
    // =========================================================================
    private JPanel buildLeftPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(BG_MAIN);

        // 1. Thanh tìm kiếm CCCD
        panel.add(buildSearchBar());
        panel.add(Box.createVerticalStrut(18));

        // 2. Bảng thông tin phòng
        panel.add(buildTableCard());
        panel.add(Box.createVerticalStrut(12));

        // 3. Khu vực tổng tiền
        panel.add(buildSummaryRow());

        return panel;
    }

    /** Thanh nhập CCCD + nút tìm kiếm */
    private JPanel buildSearchBar() {
        JPanel card = new RoundedPanel(12, BG_WHITE);
        card.setLayout(new FlowLayout(FlowLayout.LEFT, 12, 12));
        card.setBorder(new EmptyBorder(4, 4, 4, 4));
        card.setMaximumSize(new Dimension(Integer.MAX_VALUE, 62));

        // TextField nhập CCCD
        JTextField tfCCCD = new JTextField(22);
        tfCCCD.setFont(F_LABEL);
        tfCCCD.setForeground(TEXT_MID);
        tfCCCD.setBorder(new CompoundBorder(
                new LineBorder(BORDER_COL, 1, true),
                new EmptyBorder(6, 12, 6, 12)));
        tfCCCD.setPreferredSize(new Dimension(220, 36));

        // Placeholder text
        tfCCCD.setText("Nhập CCCD");
        tfCCCD.setForeground(TEXT_GRAY);
        tfCCCD.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                if (tfCCCD.getText().equals("Nhập CCCD")) {
                    tfCCCD.setText("");
                    tfCCCD.setForeground(TEXT_DARK);
                }
            }

            @Override
            public void focusLost(FocusEvent e) {
                if (tfCCCD.getText().isEmpty()) {
                    tfCCCD.setText("Nhập CCCD");
                    tfCCCD.setForeground(TEXT_GRAY);
                }
            }
        });

        // Nút Tìm kiếm
        JButton btnSearch = new JButton("Tìm kiếm") {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(getModel().isPressed() ? BLUE.darker() : BLUE);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 8, 8);
                g2.dispose();
                super.paintComponent(g);
            }
        };
        btnSearch.setFont(F_BOLD13);
        btnSearch.setForeground(Color.WHITE);
        btnSearch.setContentAreaFilled(false);
        btnSearch.setBorderPainted(false);
        btnSearch.setFocusPainted(false);
        btnSearch.setPreferredSize(new Dimension(100, 36));
        btnSearch.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        card.add(tfCCCD);
        card.add(btnSearch);
        return card;
    }

    /** Bảng hiển thị thông tin phòng */
    private JPanel buildTableCard() {
        JPanel card = new RoundedPanel(12, BG_WHITE);
        card.setLayout(new BorderLayout());
        card.setBorder(new EmptyBorder(0, 0, 0, 0));

        // Tạo TableModel không cho chỉnh sửa
        DefaultTableModel model = new DefaultTableModel(SAMPLE_DATA, COLUMNS) {
            @Override
            public boolean isCellEditable(int row, int col) {
                return false;
            }
        };

        JTable table = new JTable(model);
        table.setFont(F_TABLE);
        table.setForeground(TEXT_DARK);
        table.setRowHeight(56);
        table.setShowVerticalLines(false);
        table.setShowHorizontalLines(true);
        table.setGridColor(BORDER_COL);
        table.setBackground(BG_WHITE);
        table.setSelectionBackground(BLUE_LIGHT);
        table.setSelectionForeground(TEXT_DARK);
        table.setIntercellSpacing(new Dimension(0, 0));
        table.setFocusable(false);

        // Header bảng
        JTableHeader header = table.getTableHeader();
        header.setFont(F_TABLE_H);
        header.setForeground(TEXT_MID);
        header.setBackground(BG_WHITE);
        header.setBorder(new MatteBorder(0, 0, 2, 0, BORDER_COL));
        header.setPreferredSize(new Dimension(0, 42));
        ((DefaultTableCellRenderer) header.getDefaultRenderer()).setHorizontalAlignment(JLabel.CENTER);

        // Căn giữa tất cả các cột
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        centerRenderer.setBorder(new EmptyBorder(0, 8, 0, 8));

        // Cột "Tổng tiền" tô màu cam nổi bật
        DefaultTableCellRenderer moneyRenderer = new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(
                    JTable t, Object val, boolean sel, boolean focus, int row, int col) {
                super.getTableCellRendererComponent(t, val, sel, focus, row, col);
                setForeground(sel ? TEXT_DARK : ORANGE);
                setFont(F_BOLD13);
                setHorizontalAlignment(JLabel.CENTER);
                return this;
            }
        };

        for (int i = 0; i < table.getColumnCount(); i++) {
            if (i == table.getColumnCount() - 1) {
                table.getColumnModel().getColumn(i).setCellRenderer(moneyRenderer);
            } else {
                table.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
            }
        }

        // Độ rộng cột
        int[] colWidths = { 70, 110, 160, 160, 120 };
        for (int i = 0; i < colWidths.length; i++) {
            table.getColumnModel().getColumn(i).setPreferredWidth(colWidths[i]);
        }

        // Dòng so le màu (zebra striping)
        table.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(
                    JTable t, Object val, boolean sel, boolean focus, int row, int col) {
                Component c = centerRenderer.getTableCellRendererComponent(t, val, sel, focus, row, col);
                if (!sel) {
                    c.setBackground(row % 2 == 0 ? BG_WHITE : new Color(0xFAFBFD));
                }
                // Cột tổng tiền
                if (col == t.getColumnCount() - 1) {
                    ((JLabel) c).setForeground(sel ? TEXT_DARK : ORANGE);
                    ((JLabel) c).setFont(F_BOLD13);
                }
                return c;
            }
        });

        // Tái áp dụng renderer tiền cho cột cuối
        table.getColumnModel().getColumn(COLUMNS.length - 1).setCellRenderer(moneyRenderer);

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        scrollPane.getViewport().setBackground(BG_WHITE);
        scrollPane.setPreferredSize(new Dimension(0, 230));

        card.add(scrollPane, BorderLayout.CENTER);
        return card;
    }

    /** Khu vực tổng kết tiền (Tổng tiền / Khuyến mãi / VAT / Total) */
    private JPanel buildSummaryRow() {
        JPanel wrapper = new RoundedPanel(12, BG_WHITE);
        wrapper.setLayout(new BorderLayout());
        wrapper.setMaximumSize(new Dimension(Integer.MAX_VALUE, 130));

        // Panel chứa các dòng tổng – canh về phía phải
        JPanel summaryBox = new JPanel();
        summaryBox.setOpaque(false);
        summaryBox.setLayout(new BoxLayout(summaryBox, BoxLayout.Y_AXIS));
        summaryBox.setBorder(new EmptyBorder(16, 24, 16, 32));

        summaryBox.add(buildSummaryLine("Tổng tiền :", "2.300.000", TEXT_MID, F_LABEL, F_BOLD13));
        summaryBox.add(Box.createVerticalStrut(6));
        summaryBox.add(buildSummaryLine("Khuyến mãi :", "0%", GREEN, F_LABEL, F_BOLD13));
        summaryBox.add(Box.createVerticalStrut(6));
        summaryBox.add(buildSummaryLine("VAT :", "10%", TEXT_MID, F_LABEL, F_BOLD13));

        // Đường kẻ phân cách
        JSeparator sep = new JSeparator();
        sep.setForeground(BORDER_COL);
        sep.setMaximumSize(new Dimension(Integer.MAX_VALUE, 1));

        summaryBox.add(Box.createVerticalStrut(8));
        summaryBox.add(sep);
        summaryBox.add(Box.createVerticalStrut(8));
        summaryBox.add(buildSummaryLine("Total :", "2.530.000", BLUE, F_BOLD14, F_BOLD16));

        wrapper.add(summaryBox, BorderLayout.EAST);
        return wrapper;
    }

    /**
     * Tạo 1 dòng tổng kết dạng: [nhãn] [giá trị]
     * 
     * @param label      Tên nhãn
     * @param value      Giá trị hiển thị
     * @param valueColor Màu chữ giá trị
     * @param labelFont  Font nhãn
     * @param valueFont  Font giá trị
     */
    private JPanel buildSummaryLine(String label, String value,
            Color valueColor, Font labelFont, Font valueFont) {
        JPanel row = new JPanel(new BorderLayout(60, 0));
        row.setOpaque(false);
        row.setMaximumSize(new Dimension(Integer.MAX_VALUE, 24));

        JLabel lblKey = new JLabel(label);
        lblKey.setFont(labelFont);
        lblKey.setForeground(TEXT_MID);

        JLabel lblVal = new JLabel(value);
        lblVal.setFont(valueFont);
        lblVal.setForeground(valueColor);
        lblVal.setHorizontalAlignment(JLabel.RIGHT);

        row.add(lblKey, BorderLayout.WEST);
        row.add(lblVal, BorderLayout.EAST);
        return row;
    }

    // =========================================================================
    // PANEL PHẢI – Chọn phương thức + QR + Nút Thanh Toán
    // =========================================================================
    private JPanel buildRightPanel() {
        JPanel panel = new RoundedPanel(14, BG_WHITE);
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(new EmptyBorder(28, 24, 24, 24));

        // Tiêu đề
        JLabel title = new JLabel("Thanh Toán");
        title.setFont(F_TITLE);
        title.setForeground(TEXT_DARK);
        title.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.add(title);
        panel.add(Box.createVerticalStrut(20));

        // Phụ đề chọn phương thức
        JLabel subTitle = new JLabel("Chọn phương thức thanh toán:");
        subTitle.setFont(F_BOLD13);
        subTitle.setForeground(TEXT_MID);
        subTitle.setAlignmentX(Component.LEFT_ALIGNMENT);
        panel.add(subTitle);
        panel.add(Box.createVerticalStrut(10));

        // Radio buttons
        ButtonGroup bg = new ButtonGroup();

        JRadioButton rbTienMat = createRadio("Tiền mặt", false);
        JRadioButton rbMomo = createRadio("Momo", true);
        bg.add(rbTienMat);
        bg.add(rbMomo);

        panel.add(rbTienMat);
        panel.add(Box.createVerticalStrut(6));
        panel.add(rbMomo);
        panel.add(Box.createVerticalStrut(20));

        // QR code panel (hiện khi chọn Momo)
        JPanel qrWrapper = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));
        qrWrapper.setOpaque(false);
        qrWrapper.setAlignmentX(Component.CENTER_ALIGNMENT);

        QRCodePanel qrPanel = new QRCodePanel(180);
        qrWrapper.add(qrPanel);
        panel.add(qrWrapper);

        // Ẩn/hiện QR theo radio
        rbMomo.addActionListener(e -> qrPanel.setVisible(true));
        rbTienMat.addActionListener(e -> qrPanel.setVisible(false));

        panel.add(Box.createVerticalGlue());

        // Nút Thanh Toán
        JButton btnPay = buildPayButton();
        btnPay.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnPay.setMaximumSize(new Dimension(Integer.MAX_VALUE, 46));
        panel.add(btnPay);

        return panel;
    }

    /** Radio button tùy chỉnh màu xanh lá khi chọn Momo */
    private JRadioButton createRadio(String text, boolean selected) {
        JRadioButton rb = new JRadioButton(text, selected);
        rb.setFont(F_LABEL);
        rb.setForeground(TEXT_DARK);
        rb.setOpaque(false);
        rb.setAlignmentX(Component.LEFT_ALIGNMENT);
        rb.setFocusPainted(false);
        rb.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        // Đặt màu icon khi được chọn
        rb.setIcon(createRadioIcon(false));
        rb.setSelectedIcon(createRadioIcon(true));
        return rb;
    }

    /** Vẽ icon hình tròn radio */
    private Icon createRadioIcon(boolean checked) {
        return new Icon() {
            @Override
            public int getIconWidth() {
                return 18;
            }

            @Override
            public int getIconHeight() {
                return 18;
            }

            @Override
            public void paintIcon(Component c, Graphics g, int x, int y) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(BORDER_COL);
                g2.fillOval(x, y, 17, 17);
                g2.setColor(checked ? GREEN : Color.WHITE);
                g2.fillOval(x + 1, y + 1, 15, 15);
                if (checked) {
                    g2.setColor(Color.WHITE);
                    g2.fillOval(x + 5, y + 5, 7, 7);
                } else {
                    g2.setColor(BORDER_COL);
                    g2.setStroke(new BasicStroke(1.5f));
                    g2.drawOval(x + 1, y + 1, 15, 15);
                }
                g2.dispose();
            }
        };
    }

    /** Nút Thanh Toán màu xanh lá, bo góc */
    private JButton buildPayButton() {
        JButton btn = new JButton("Thanh Toán") {
            private boolean hovered = false;
            {
                addMouseListener(new MouseAdapter() {
                    @Override
                    public void mouseEntered(MouseEvent e) {
                        hovered = true;
                        repaint();
                    }

                    @Override
                    public void mouseExited(MouseEvent e) {
                        hovered = false;
                        repaint();
                    }
                });
            }

            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                Color bg = getModel().isPressed() ? GREEN_DARK : (hovered ? new Color(0x1E9E50) : GREEN);
                g2.setColor(bg);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 10, 10);
                g2.dispose();
                super.paintComponent(g);
            }
        };
        btn.setFont(F_BOLD14);
        btn.setForeground(Color.WHITE);
        btn.setContentAreaFilled(false);
        btn.setBorderPainted(false);
        btn.setFocusPainted(false);
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btn.setPreferredSize(new Dimension(200, 46));
        return btn;
    }

    // =========================================================================
    // QR Code Panel – tự vẽ QR giả lập (black & white pixel pattern)
    // =========================================================================
    static class QRCodePanel extends JPanel {
        private final int size;
        private final BufferedImage qrImage;

        QRCodePanel(int size) {
            this.size = size;
            setOpaque(false);
            setPreferredSize(new Dimension(size, size));
            qrImage = generateFakeQR(size);
        }

        /** Sinh ảnh QR mô phỏng (pattern ngẫu nhiên có viền định vị) */
        private BufferedImage generateFakeQR(int px) {
            int modules = 25; // số ô trong QR
            int cellSize = px / modules;
            int actualSize = cellSize * modules;
            BufferedImage img = new BufferedImage(actualSize, actualSize, BufferedImage.TYPE_INT_ARGB);
            Graphics2D g2 = img.createGraphics();
            g2.setColor(Color.WHITE);
            g2.fillRect(0, 0, actualSize, actualSize);

            // Seed cố định để QR không đổi mỗi lần vẽ
            java.util.Random rng = new java.util.Random(0xABCDEF);
            boolean[][] grid = new boolean[modules][modules];
            for (int r = 0; r < modules; r++)
                for (int c = 0; c < modules; c++)
                    grid[r][c] = rng.nextBoolean();

            // Vẽ các ô dữ liệu
            for (int r = 0; r < modules; r++) {
                for (int c = 0; c < modules; c++) {
                    if (isFinderOrSeparator(r, c, modules))
                        continue;
                    if (grid[r][c]) {
                        g2.setColor(Color.BLACK);
                        g2.fillRect(c * cellSize, r * cellSize, cellSize - 1, cellSize - 1);
                    }
                }
            }

            // Vẽ 3 ô định vị góc (finder patterns)
            drawFinderPattern(g2, 0, 0, cellSize);
            drawFinderPattern(g2, 0, (modules - 7) * cellSize, cellSize);
            drawFinderPattern(g2, (modules - 7) * cellSize, 0, cellSize);

            g2.dispose();
            return img;
        }

        /** Vẽ finder pattern (hình vuông 3 lớp đen-trắng-đen) tại (px, py) */
        private void drawFinderPattern(Graphics2D g2, int py, int px, int cell) {
            // Outer 7x7 đen
            g2.setColor(Color.BLACK);
            g2.fillRect(px, py, cell * 7, cell * 7);
            // Inner 5x5 trắng
            g2.setColor(Color.WHITE);
            g2.fillRect(px + cell, py + cell, cell * 5, cell * 5);
            // Core 3x3 đen
            g2.setColor(Color.BLACK);
            g2.fillRect(px + cell * 2, py + cell * 2, cell * 3, cell * 3);
        }

        /** Kiểm tra ô có nằm trong vùng finder pattern + separator không */
        private boolean isFinderOrSeparator(int r, int c, int n) {
            // Góc trên trái
            if (r <= 7 && c <= 7)
                return true;
            // Góc trên phải
            if (r <= 7 && c >= n - 8)
                return true;
            // Góc dưới trái
            if (r >= n - 8 && c <= 7)
                return true;
            return false;
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_NEAREST_NEIGHBOR);
            // Vẽ viền trắng + border nhẹ
            g2.setColor(Color.WHITE);
            g2.fillRoundRect(0, 0, size, size, 8, 8);
            g2.setColor(new Color(0xDDE3EE));
            g2.setStroke(new BasicStroke(1.5f));
            g2.drawRoundRect(0, 0, size - 1, size - 1, 8, 8);
            // Vẽ QR
            g2.drawImage(qrImage, 6, 6, size - 12, size - 12, null);
            g2.dispose();
        }
    }

    // =========================================================================
    // RoundedPanel – JPanel có bo góc
    // =========================================================================
    static class RoundedPanel extends JPanel {
        private final int arc;
        private final Color bg;

        RoundedPanel(int arc, Color bg) {
            this.arc = arc;
            this.bg = bg;
            setOpaque(false);
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(bg);
            g2.fillRoundRect(0, 0, getWidth(), getHeight(), arc, arc);
            // Đổ bóng nhẹ
            g2.setColor(new Color(0, 0, 0, 12));
            g2.setStroke(new BasicStroke(1f));
            g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, arc, arc);
            g2.dispose();
            super.paintComponent(g);
        }
    }
}