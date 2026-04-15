package iuh.view;

import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.*;

/**
 * QuanLyKhuyenMaiPanel – Quản lý Khuyến Mãi (thêm / sửa / xóa)
 *
 * Cột bảng được chọn lọc:
 *   Mã KM | Tên | Ngày bắt đầu | Ngày kết thúc | Hệ số | Tổng tiền tối thiểu | Trạng thái
 *   (tongKhuyenMaiToiDa → chi tiết → chỉ trong modal)
 */
public class QuanLyKhuyenMaiPanel extends JPanel {

    private static final Color BG_MAIN    = QuanLyNhanVienPanel.BG_MAIN;
    private static final Color BG_WHITE   = QuanLyNhanVienPanel.BG_WHITE;
    private static final Color BLUE       = QuanLyNhanVienPanel.BLUE;
    private static final Color BLUE_LIGHT = QuanLyNhanVienPanel.BLUE_LIGHT;
    private static final Color TEXT_DARK  = QuanLyNhanVienPanel.TEXT_DARK;
    private static final Color TEXT_MID   = QuanLyNhanVienPanel.TEXT_MID;
    private static final Color TEXT_GRAY  = QuanLyNhanVienPanel.TEXT_GRAY;
    private static final Color BORDER_COL = QuanLyNhanVienPanel.BORDER_COL;
    private static final Color HEADER_BG  = QuanLyNhanVienPanel.HEADER_BG;
    private static final Color ROW_ALT    = QuanLyNhanVienPanel.ROW_ALT;
    private static final Color ROW_SEL    = QuanLyNhanVienPanel.ROW_SEL;
    private static final Color GREEN      = QuanLyNhanVienPanel.GREEN;
    private static final Color GREEN_BG   = QuanLyNhanVienPanel.GREEN_BG;
    private static final Color RED        = QuanLyNhanVienPanel.RED;
    private static final Color RED_BG     = QuanLyNhanVienPanel.RED_BG;
    private static final Color ORANGE     = QuanLyNhanVienPanel.ORANGE;
    private static final Color ORANGE_BG  = QuanLyNhanVienPanel.ORANGE_BG;

    // Màu riêng cho trạng thái "Sắp diễn ra"
    private static final Color PURPLE    = new Color(0x7B61FF);
    private static final Color PURPLE_BG = new Color(0xF0EEFF);

    private static final Font F_TITLE   = QuanLyNhanVienPanel.F_TITLE;
    private static final Font F_LABEL   = QuanLyNhanVienPanel.F_LABEL;
    private static final Font F_BOLD13  = QuanLyNhanVienPanel.F_BOLD13;
    private static final Font F_TABLE   = QuanLyNhanVienPanel.F_TABLE;
    private static final Font F_TABLE_H = QuanLyNhanVienPanel.F_TABLE_H;

    // ── Dữ liệu mẫu ──────────────────────────────────────────────────────────
    // Cột: Mã KM | Tên | Ngày bắt đầu | Ngày kết thúc | Hệ số (%) | T.tiền tối thiểu | Trạng thái
    private static final Object[][] DATA = {
        {"KM001", "Family Deal",    "01/01/2025", "31/03/2025", "10%", "500.000 đ",   "Đã kết thúc"},
        {"KM002", "Summer Sale",    "01/06/2025", "31/08/2025", "15%", "800.000 đ",   "Đang hoạt động"},
        {"KM003", "Christmas Deal", "20/12/2025", "31/12/2025", "20%", "1.000.000 đ", "Sắp diễn ra"},
        {"KM004", "Black Friday",   "25/11/2024", "30/11/2024", "25%", "700.000 đ",   "Đã kết thúc"},
        {"KM005", "Tet Holiday",    "25/01/2026", "05/02/2026", "12%", "600.000 đ",   "Sắp diễn ra"},
        {"KM006", "Welcome Back",   "01/03/2025", "30/05/2025", "8%",  "300.000 đ",   "Đang hoạt động"},
    };

    private static final String[] COLS = {
        "Mã KM", "Tên chương trình", "Ngày bắt đầu", "Ngày kết thúc", "Hệ số", "T.Tiền tối thiểu", "Trạng thái"
    };

    private DefaultTableModel tableModel;
    private JTextField tfSearch;

    public QuanLyKhuyenMaiPanel() {
        setLayout(new BorderLayout());
        setBackground(BG_MAIN);
        setBorder(new EmptyBorder(28, 28, 28, 28));

        JLabel title = new JLabel("Quản lý khuyến mãi");
        title.setFont(F_TITLE);
        title.setForeground(TEXT_DARK);
        title.setBorder(new EmptyBorder(0, 0, 18, 0));
        add(title, BorderLayout.NORTH);

        JPanel card = new ThanhToanPanel.RoundedPanel(14, BG_WHITE);
        card.setLayout(new BorderLayout());
        card.add(buildToolbar(), BorderLayout.NORTH);
        card.add(buildTable(),   BorderLayout.CENTER);
        add(card, BorderLayout.CENTER);
    }

    private JPanel buildToolbar() {
        JPanel bar = new JPanel(new BorderLayout(12, 0));
        bar.setOpaque(false);
        bar.setBorder(new EmptyBorder(16, 20, 12, 20));

        JPanel left = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0));
        left.setOpaque(false);

        tfSearch = new JTextField(22);
        tfSearch.setFont(F_LABEL);
        tfSearch.setPreferredSize(new Dimension(240, 38));
        tfSearch.setBorder(new CompoundBorder(
            new LineBorder(BORDER_COL, 1, true), new EmptyBorder(0, 12, 0, 12)));
        setPlaceholder(tfSearch, "Tìm theo tên, mã khuyến mãi...");

        JButton btnSearch = QuanLyNhanVienPanel.createButton("Tìm kiếm", BLUE, new Color(0x2D5FD8), Color.WHITE, 100, 38);
        left.add(tfSearch); left.add(btnSearch);

        JButton btnAdd = QuanLyNhanVienPanel.createButton(
            "+ Thêm khuyến mãi", GREEN, new Color(0x1E8449), Color.WHITE, 165, 38);
        btnAdd.addActionListener(e -> openModal(null));

        JPanel right = new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 0));
        right.setOpaque(false); right.add(btnAdd);

        bar.add(left, BorderLayout.WEST); bar.add(right, BorderLayout.EAST);

        JPanel wrap = new JPanel(new BorderLayout()); wrap.setOpaque(false);
        wrap.add(bar, BorderLayout.CENTER);
        wrap.add(new JSeparator() {{ setForeground(BORDER_COL); }}, BorderLayout.SOUTH);
        return wrap;
    }

    private JScrollPane buildTable() {
        tableModel = new DefaultTableModel(DATA, COLS) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };

        JTable table = new JTable(tableModel);
        styleTable(table);
        table.setRowHeight(52);
        table.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);

        // Renderer chung
        DefaultTableCellRenderer base = new DefaultTableCellRenderer() {
            @Override public Component getTableCellRendererComponent(
                    JTable t, Object v, boolean sel, boolean foc, int row, int col) {
                super.getTableCellRendererComponent(t, v, sel, foc, row, col);
                setBackground(sel ? ROW_SEL : (row % 2 == 0 ? BG_WHITE : ROW_ALT));
                setForeground(col == 0 ? BLUE
                            : col == 4 ? GREEN
                            : col == 5 ? ORANGE
                            : TEXT_DARK);
                setFont(col == 0 || col == 4 || col == 5 ? F_BOLD13 : F_TABLE);
                setBorder(new EmptyBorder(0, 16, 0, 8));
                setHorizontalAlignment(col == 4 || col == 5 ? JLabel.CENTER : JLabel.LEFT);
                return this;
            }
        };

        // Renderer trạng thái (3 màu)
        DefaultTableCellRenderer statusR = buildStatusRenderer3();

        for (int i = 0; i < COLS.length - 1; i++)
            table.getColumnModel().getColumn(i).setCellRenderer(base);
        table.getColumnModel().getColumn(COLS.length - 1).setCellRenderer(statusR);

        int[] widths = {75, 180, 115, 115, 70, 150, 130};
        for (int i = 0; i < widths.length; i++)
            table.getColumnModel().getColumn(i).setPreferredWidth(widths[i]);

        table.addMouseListener(new MouseAdapter() {
            @Override public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    int row = table.getSelectedRow();
                    if (row >= 0) openModal(row);
                }
            }
        });
        table.setToolTipText("Double-click để xem / chỉnh sửa khuyến mãi");

        JScrollPane sp = new JScrollPane(table);
        sp.setBorder(BorderFactory.createEmptyBorder());
        sp.getViewport().setBackground(BG_WHITE);
        return sp;
    }

    // Renderer badge 3 trạng thái cho khuyến mãi
    private DefaultTableCellRenderer buildStatusRenderer3() {
        return new DefaultTableCellRenderer() {
            @Override public Component getTableCellRendererComponent(
                    JTable t, Object val, boolean sel, boolean foc, int row, int col) {
                String s = val == null ? "" : val.toString();
                JPanel cell = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
                cell.setBackground(sel ? ROW_SEL : (row % 2 == 0 ? BG_WHITE : ROW_ALT));
                cell.setBorder(new EmptyBorder(11, 16, 11, 8));

                Color bg, fg;
                switch (s) {
                    case "Đang hoạt động" -> { bg = GREEN_BG;  fg = GREEN;  }
                    case "Sắp diễn ra"    -> { bg = PURPLE_BG; fg = PURPLE; }
                    default               -> { bg = RED_BG;    fg = RED;    }
                }

                final Color finalBg = bg, finalFg = fg;
                JLabel pill = new JLabel(s) {
                    @Override protected void paintComponent(Graphics g) {
                        Graphics2D g2 = (Graphics2D) g.create();
                        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                        g2.setColor(finalBg);
                        g2.fillRoundRect(0, 0, getWidth(), getHeight(), 20, 20);
                        g2.dispose(); super.paintComponent(g);
                    }
                };
                pill.setFont(new Font("Segoe UI", Font.PLAIN, 11));
                pill.setForeground(finalFg);
                pill.setOpaque(false);
                pill.setBorder(new EmptyBorder(3, 10, 3, 10));
                cell.add(pill);
                return cell;
            }
        };
    }

    private void openModal(Integer row) {
        Object[] data = null;
        if (row != null) {
            data = new Object[tableModel.getColumnCount()];
            for (int c = 0; c < data.length; c++) data[c] = tableModel.getValueAt(row, c);
        }
        JFrame top = (JFrame) SwingUtilities.getWindowAncestor(this);
        new KhuyenMaiModal(top, data, row == null).setVisible(true);
    }

    private void styleTable(JTable t) {
        t.setFont(F_TABLE); t.setShowVerticalLines(false); t.setShowHorizontalLines(true);
        t.setGridColor(BORDER_COL); t.setBackground(BG_WHITE);
        t.setSelectionBackground(ROW_SEL); t.setSelectionForeground(TEXT_DARK);
        t.setIntercellSpacing(new Dimension(0, 0)); t.setFocusable(false);
        t.getSelectionModel().setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        JTableHeader h = t.getTableHeader();
        h.setFont(F_TABLE_H); h.setForeground(TEXT_MID); h.setBackground(HEADER_BG);
        h.setBorder(new MatteBorder(0, 0, 1, 0, BORDER_COL));
        h.setPreferredSize(new Dimension(0, 42)); h.setReorderingAllowed(false);
        ((DefaultTableCellRenderer) h.getDefaultRenderer()).setHorizontalAlignment(JLabel.LEFT);
    }

    private void setPlaceholder(JTextField tf, String ph) {
        tf.setText(ph); tf.setForeground(TEXT_GRAY);
        tf.addFocusListener(new FocusAdapter() {
            @Override public void focusGained(FocusEvent e) {
                if (tf.getText().equals(ph)) { tf.setText(""); tf.setForeground(TEXT_DARK); }
            }
            @Override public void focusLost(FocusEvent e) {
                if (tf.getText().isEmpty()) { tf.setText(ph); tf.setForeground(TEXT_GRAY); }
            }
        });
    }
}

// =============================================================================
//  MODAL KHUYẾN MÃI
// =============================================================================
class KhuyenMaiModal extends JDialog {

    private static final Color BG_WHITE   = QuanLyNhanVienPanel.BG_WHITE;
    private static final Color BLUE       = QuanLyNhanVienPanel.BLUE;
    private static final Color TEXT_DARK  = QuanLyNhanVienPanel.TEXT_DARK;
    private static final Color TEXT_MID   = QuanLyNhanVienPanel.TEXT_MID;
    private static final Color TEXT_GRAY  = QuanLyNhanVienPanel.TEXT_GRAY;
    private static final Color BORDER_COL = QuanLyNhanVienPanel.BORDER_COL;
    private static final Color GREEN      = QuanLyNhanVienPanel.GREEN;
    private static final Color RED        = QuanLyNhanVienPanel.RED;
    private static final Color PURPLE     = new Color(0x7B61FF);
    private static final Color PURPLE_BG  = new Color(0xF0EEFF);

    KhuyenMaiModal(JFrame owner, Object[] data, boolean isNew) {
        super(owner, isNew ? "Thêm khuyến mãi" : "Chi tiết khuyến mãi", true);
        setSize(560, 540);
        setLocationRelativeTo(owner);
        setResizable(false);

        JPanel root = new JPanel(new BorderLayout());
        root.setBackground(BG_WHITE);
        root.add(buildHeader(isNew, data), BorderLayout.NORTH);
        root.add(buildForm(data, isNew),   BorderLayout.CENTER);
        root.add(buildFooter(isNew),       BorderLayout.SOUTH);
        setContentPane(root);
    }

    private JPanel buildHeader(boolean isNew, Object[] data) {
        JPanel h = new JPanel(new BorderLayout());
        h.setBackground(BG_WHITE);
        h.setBorder(new CompoundBorder(
            new MatteBorder(0, 0, 1, 0, BORDER_COL), new EmptyBorder(18, 24, 16, 16)));

        JPanel left = new JPanel(new FlowLayout(FlowLayout.LEFT, 12, 0));
        left.setOpaque(false);

        JLabel icon = new JLabel() {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(PURPLE_BG);
                g2.fillOval(0, 0, 44, 44);
                g2.setColor(PURPLE);
                g2.setFont(new Font("Segoe UI", Font.BOLD, 15));
                String t = isNew ? "+" : "%";
                FontMetrics fm = g2.getFontMetrics();
                g2.drawString(t, 22 - fm.stringWidth(t)/2, 27);
                g2.dispose();
            }
        };
        icon.setPreferredSize(new Dimension(44, 44));

        JPanel txt = new JPanel(); txt.setLayout(new BoxLayout(txt, BoxLayout.Y_AXIS)); txt.setOpaque(false);
        JLabel t1 = new JLabel(isNew ? "Thêm chương trình khuyến mãi" : "Thông tin khuyến mãi");
        t1.setFont(new Font("Segoe UI", Font.BOLD, 15)); t1.setForeground(TEXT_DARK);
        JLabel t2 = new JLabel(isNew ? "Điền thông tin chương trình" : "Mã: " + s(data, 0));
        t2.setFont(new Font("Segoe UI", Font.PLAIN, 12)); t2.setForeground(TEXT_GRAY);
        txt.add(t1); txt.add(Box.createVerticalStrut(2)); txt.add(t2);
        left.add(icon); left.add(txt);
        h.add(left, BorderLayout.CENTER);
        h.add(makeCloseBtn(), BorderLayout.EAST);
        return h;
    }

    private JPanel buildForm(Object[] d, boolean isNew) {
        JPanel form = new JPanel(new GridBagLayout());
        form.setBackground(BG_WHITE);
        form.setBorder(new EmptyBorder(20, 24, 20, 24));

        GridBagConstraints g = new GridBagConstraints();
        g.fill = GridBagConstraints.HORIZONTAL; g.weighty = 0; g.weightx = 0.5;

        // Hàng 0: Mã KM + Trạng thái
        JComboBox<String> cbTT = new JComboBox<>(new String[]{"Sắp diễn ra", "Đang hoạt động", "Đã kết thúc"});
        styleCombo(cbTT);
        if (!isNew) cbTT.setSelectedItem(s(d, 6));
        addRow2(form, g, 0, "Mã khuyến mãi", field(s(d,0), false),
                            "Trạng thái", cbTT);

        // Hàng 1: Tên chương trình (full)
        addRowFull(form, g, 1, "Tên chương trình", field(s(d,1), true));

        // Hàng 2: Ngày bắt đầu + Ngày kết thúc
        addRow2(form, g, 2, "Ngày bắt đầu", field(s(d,2), true),
                             "Ngày kết thúc", field(s(d,3), true));

        // Hàng 3: Hệ số (%) + Tổng tiền tối thiểu
        addRow2(form, g, 3, "Hệ số giảm (%)", field(s(d,4).replace("%","").trim(), true),
                             "T.Tiền tối thiểu (đ)", field(s(d,5).replace(" đ","").replace(".",""), true));

        // Hàng 4: Giảm tối đa (full) – field này chỉ có trong modal
        addRowFull(form, g, 4, "Giảm tối đa (đ)", field(isNew ? "" : "500.000", true));

        g.gridx=0; g.gridy=10; g.gridwidth=2; g.weighty=1; g.fill=GridBagConstraints.BOTH;
        form.add(Box.createVerticalGlue(), g);
        return form;
    }

    private void addRow2(JPanel form, GridBagConstraints g, int row,
                          String l1, JComponent f1, String l2, JComponent f2) {
        g.gridwidth=1;
        g.insets=new Insets(0,0,4,12); g.gridy=row*2; g.gridx=0; form.add(lbl(l1),g);
        g.insets=new Insets(0,0,4,0);  g.gridx=1; form.add(lbl(l2),g);
        g.insets=new Insets(0,0,14,12); g.gridy=row*2+1; g.gridx=0; form.add(f1,g);
        g.insets=new Insets(0,0,14,0);  g.gridx=1; form.add(f2,g);
    }

    private void addRowFull(JPanel form, GridBagConstraints g, int row, String label, JComponent field) {
        g.gridwidth=2; g.weightx=1; g.gridx=0;
        g.insets=new Insets(0,0,4,0); g.gridy=row*2; form.add(lbl(label),g);
        g.insets=new Insets(0,0,14,0); g.gridy=row*2+1; form.add(field,g);
    }

    private JLabel lbl(String t) {
        JLabel l = new JLabel(t);
        l.setFont(new Font("Segoe UI", Font.BOLD, 12)); l.setForeground(TEXT_MID); return l;
    }

    private JTextField field(String val, boolean editable) {
        JTextField tf = new JTextField(val);
        tf.setFont(new Font("Segoe UI", Font.PLAIN, 13)); tf.setForeground(TEXT_DARK);
        tf.setEditable(editable);
        if (!editable) tf.setBackground(new Color(0xF8F9FD));
        tf.setPreferredSize(new Dimension(0, 38));
        tf.setBorder(new CompoundBorder(new LineBorder(BORDER_COL,1,true), new EmptyBorder(0,12,0,12)));
        return tf;
    }

    private void styleCombo(JComboBox<String> cb) {
        cb.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        cb.setBackground(BG_WHITE); cb.setForeground(TEXT_DARK);
        cb.setPreferredSize(new Dimension(0, 38));
        cb.setBorder(new LineBorder(BORDER_COL, 1, true));
        cb.setFocusable(false);
    }

    private JPanel buildFooter(boolean isNew) {
        JPanel f = new JPanel(new FlowLayout(FlowLayout.RIGHT, 12, 12));
        f.setBackground(BG_WHITE); f.setBorder(new MatteBorder(1,0,0,0, BORDER_COL));
        JButton cancel = QuanLyNhanVienPanel.createButton("Hủy", new Color(0xEEF0F5), new Color(0xDDE0E8), TEXT_MID, 80, 38);
        cancel.addActionListener(e -> dispose());
        if (isNew) {
            JButton save = QuanLyNhanVienPanel.createButton("Lưu", BLUE, new Color(0x2D5FD8), Color.WHITE, 100, 38);
            f.add(cancel); f.add(save);
        } else {
            JButton del = QuanLyNhanVienPanel.createButton("Xóa", RED, RED.darker(), Color.WHITE, 80, 38);
            del.addActionListener(e -> { int c = JOptionPane.showConfirmDialog(this,"Xóa khuyến mãi này?","Xác nhận",JOptionPane.YES_NO_OPTION); if(c==JOptionPane.YES_OPTION) dispose(); });
            JButton upd = QuanLyNhanVienPanel.createButton("Cập nhật", BLUE, new Color(0x2D5FD8), Color.WHITE, 110, 38);
            f.add(del); f.add(cancel); f.add(upd);
        }
        return f;
    }

    private JButton makeCloseBtn() {
        JButton b = new JButton("✕") {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2=(Graphics2D)g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(getModel().isRollover()?new Color(0xFFF0F0):BG_WHITE);
                g2.fillOval(0,0,getWidth(),getHeight()); g2.dispose(); super.paintComponent(g);
            }
        };
        b.setFont(new Font("Segoe UI",Font.PLAIN,13)); b.setForeground(TEXT_MID);
        b.setContentAreaFilled(false); b.setBorderPainted(false); b.setFocusPainted(false);
        b.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        b.setPreferredSize(new Dimension(30,30)); b.addActionListener(e->dispose());
        return b;
    }

    private String s(Object[] a, int i) {
        return (a!=null&&i<a.length&&a[i]!=null)?a[i].toString():"";
    }
}
