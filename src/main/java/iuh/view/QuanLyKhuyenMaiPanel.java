package iuh.view;

import com.toedter.calendar.JDateChooser;
import iuh.entity.KhuyenMai;
import iuh.entity.TrangThai;
import iuh.service.KhuyenMaiService;

import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.*;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;

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
    private static final Color PURPLE     = new Color(0x7B61FF);
    private static final Color PURPLE_BG  = new Color(0xF0EEFF);

    private static final Font F_TITLE   = QuanLyNhanVienPanel.F_TITLE;
    private static final Font F_LABEL   = QuanLyNhanVienPanel.F_LABEL;
    private static final Font F_BOLD13  = QuanLyNhanVienPanel.F_BOLD13;
    private static final Font F_TABLE   = QuanLyNhanVienPanel.F_TABLE;
    private static final Font F_TABLE_H = QuanLyNhanVienPanel.F_TABLE_H;

    private static final DateTimeFormatter DISPLAY_DT = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
    private static final String SEARCH_PLACEHOLDER = "Tìm theo mã hoặc tên khuyến mãi...";

    private final KhuyenMaiService khuyenMaiService = new KhuyenMaiService();

    private DefaultTableModel tableModel;
    private JTable table;
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
        card.add(buildTable(), BorderLayout.CENTER);
        add(card, BorderLayout.CENTER);

        SwingUtilities.invokeLater(this::refreshTable);
    }

    private JPanel buildToolbar() {
        JPanel bar = new JPanel(new BorderLayout(12, 0));
        bar.setOpaque(false);
        bar.setBorder(new EmptyBorder(16, 20, 12, 20));

        JPanel left = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0));
        left.setOpaque(false);

        tfSearch = new JTextField(22);
        tfSearch.setFont(F_LABEL);
        tfSearch.setForeground(TEXT_GRAY);
        tfSearch.setPreferredSize(new Dimension(240, 38));
        tfSearch.setBorder(new CompoundBorder(new LineBorder(BORDER_COL, 1, true), new EmptyBorder(0, 12, 0, 12)));
        setPlaceholder(tfSearch, SEARCH_PLACEHOLDER);
        tfSearch.addActionListener(e -> refreshTable());

        JButton btnSearch = QuanLyNhanVienPanel.createButton("Tìm kiếm", BLUE, new Color(0x2D5FD8), Color.WHITE, 100, 38);
        btnSearch.addActionListener(e -> refreshTable());

        JButton btnReset = QuanLyNhanVienPanel.createButton("Làm mới", new Color(0xEEF0F5), new Color(0xDDE0E8), TEXT_MID, 95, 38);
        btnReset.addActionListener(e -> {
            tfSearch.setText(SEARCH_PLACEHOLDER);
            tfSearch.setForeground(TEXT_GRAY);
            refreshTable();
        });

        left.add(tfSearch);
        left.add(btnSearch);
        left.add(btnReset);

        JButton btnAdd = QuanLyNhanVienPanel.createButton("+ Thêm khuyến mãi", GREEN, new Color(0x1E8449), Color.WHITE, 165, 38);
        btnAdd.addActionListener(e -> openModal(null, true));

        JPanel right = new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 0));
        right.setOpaque(false);
        right.add(btnAdd);

        bar.add(left, BorderLayout.WEST);
        bar.add(right, BorderLayout.EAST);

        JPanel wrap = new JPanel(new BorderLayout());
        wrap.setOpaque(false);
        wrap.add(bar, BorderLayout.CENTER);
        wrap.add(new JSeparator() {{ setForeground(BORDER_COL); }}, BorderLayout.SOUTH);
        return wrap;
    }

    private JScrollPane buildTable() {
        tableModel = new DefaultTableModel(new Object[0][0], new String[]{
            "Mã KM", "Tên chương trình", "Ngày bắt đầu", "Ngày kết thúc", "Hệ số", "T.Tiền tối thiểu", "Trạng thái"
        }) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };

        table = new JTable(tableModel);
        styleTable(table);
        table.setRowHeight(52);
        table.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);

        DefaultTableCellRenderer base = new DefaultTableCellRenderer() {
            @Override public Component getTableCellRendererComponent(JTable t, Object v, boolean sel, boolean foc, int row, int col) {
                super.getTableCellRendererComponent(t, v, sel, foc, row, col);
                setBackground(sel ? ROW_SEL : (row % 2 == 0 ? BG_WHITE : ROW_ALT));
                setForeground(col == 0 ? BLUE : col == 4 ? GREEN : col == 5 ? ORANGE : TEXT_DARK);
                setFont(col == 0 || col == 4 || col == 5 ? F_BOLD13 : F_TABLE);
                setBorder(new EmptyBorder(0, 16, 0, 8));
                setHorizontalAlignment(col == 4 || col == 5 ? JLabel.CENTER : JLabel.LEFT);
                return this;
            }
        };

        DefaultTableCellRenderer statusRenderer = new DefaultTableCellRenderer() {
            @Override public Component getTableCellRendererComponent(JTable t, Object val, boolean sel, boolean foc, int row, int col) {
                String text = val == null ? "" : val.toString();
                JPanel cell = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
                cell.setBackground(sel ? ROW_SEL : (row % 2 == 0 ? BG_WHITE : ROW_ALT));
                cell.setBorder(new EmptyBorder(11, 16, 11, 8));

                Color bg;
                Color fg;
                switch (text) {
                    case "Đang hoạt động" -> { bg = GREEN_BG; fg = GREEN; }
                    case "Sắp diễn ra" -> { bg = PURPLE_BG; fg = PURPLE; }
                    default -> { bg = RED_BG; fg = RED; }
                }

                JLabel pill = new JLabel(text) {
                    @Override protected void paintComponent(Graphics g) {
                        Graphics2D g2 = (Graphics2D) g.create();
                        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                        g2.setColor(bg);
                        g2.fillRoundRect(0, 0, getWidth(), getHeight(), 20, 20);
                        g2.dispose();
                        super.paintComponent(g);
                    }
                };
                pill.setFont(new Font("Segoe UI", Font.PLAIN, 11));
                pill.setForeground(fg);
                pill.setOpaque(false);
                pill.setBorder(new EmptyBorder(3, 10, 3, 10));
                cell.add(pill);
                return cell;
            }
        };

        for (int i = 0; i < 6; i++) {
            table.getColumnModel().getColumn(i).setCellRenderer(base);
        }
        table.getColumnModel().getColumn(6).setCellRenderer(statusRenderer);

        int[] widths = {90, 200, 125, 125, 70, 150, 140};
        for (int i = 0; i < widths.length; i++) {
            table.getColumnModel().getColumn(i).setPreferredWidth(widths[i]);
        }

        table.addMouseListener(new MouseAdapter() {
            @Override public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    int row = table.getSelectedRow();
                    if (row >= 0) {
                        String maKhuyenMai = String.valueOf(tableModel.getValueAt(row, 0));
                        khuyenMaiService.getKhuyenMaiById(maKhuyenMai).ifPresentOrElse(
                            km -> openModal(km, false),
                            () -> JOptionPane.showMessageDialog(QuanLyKhuyenMaiPanel.this,
                                "Không tìm thấy khuyến mãi đã chọn.",
                                "Thông báo", JOptionPane.WARNING_MESSAGE)
                        );
                    }
                }
            }
        });
        table.setToolTipText("Double-click để xem / chỉnh sửa khuyến mãi");

        JScrollPane sp = new JScrollPane(table);
        sp.setBorder(BorderFactory.createEmptyBorder());
        sp.getViewport().setBackground(BG_WHITE);
        return sp;
    }

    private void refreshTable() {
        java.util.List<KhuyenMai> list = getCurrentData();
        tableModel.setRowCount(0);
        for (KhuyenMai km : list) {
            tableModel.addRow(toRow(km));
        }
    }

    private java.util.List<KhuyenMai> getCurrentData() {
        String keyword = getSearchKeyword();
        if (keyword.isBlank()) {
            return khuyenMaiService.getAllKhuyenMai();
        }
        return khuyenMaiService.searchKhuyenMai(keyword);
    }

    private String getSearchKeyword() {
        String text = tfSearch == null ? "" : tfSearch.getText().trim();
        return SEARCH_PLACEHOLDER.equals(text) ? "" : text;
    }

    private Object[] toRow(KhuyenMai km) {
        return new Object[] {
            km.getMaKhuyenMai(),
            km.getTenKhuyenMai(),
            formatDateTime(km.getNgayBatDau()),
            formatDateTime(km.getNgayKetThuc()),
            formatPercent(km.getHeSo()),
            formatMoney(km.getTongTienToiThieu()),
            toDisplayStatus(km.getTrangThai())
        };
    }

    private void openModal(KhuyenMai khuyenMai, boolean isNew) {
        JFrame top = (JFrame) SwingUtilities.getWindowAncestor(this);
        new KhuyenMaiModal(top, khuyenMai, isNew, this::refreshTable).setVisible(true);
    }

    private void styleTable(JTable t) {
        t.setFont(F_TABLE);
        t.setShowVerticalLines(false);
        t.setShowHorizontalLines(true);
        t.setGridColor(BORDER_COL);
        t.setBackground(BG_WHITE);
        t.setSelectionBackground(ROW_SEL);
        t.setSelectionForeground(TEXT_DARK);
        t.setIntercellSpacing(new Dimension(0, 0));
        t.setFocusable(false);
        t.getSelectionModel().setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        JTableHeader h = t.getTableHeader();
        h.setFont(F_TABLE_H);
        h.setForeground(TEXT_MID);
        h.setBackground(HEADER_BG);
        h.setBorder(new MatteBorder(0, 0, 1, 0, BORDER_COL));
        h.setPreferredSize(new Dimension(0, 42));
        h.setReorderingAllowed(false);
        ((DefaultTableCellRenderer) h.getDefaultRenderer()).setHorizontalAlignment(JLabel.LEFT);
    }

    private void setPlaceholder(JTextField tf, String ph) {
        tf.setText(ph);
        tf.setForeground(TEXT_GRAY);
        tf.addFocusListener(new FocusAdapter() {
            @Override public void focusGained(FocusEvent e) {
                if (tf.getText().equals(ph)) {
                    tf.setText("");
                    tf.setForeground(TEXT_DARK);
                }
            }

            @Override public void focusLost(FocusEvent e) {
                if (tf.getText().isEmpty()) {
                    tf.setText(ph);
                    tf.setForeground(TEXT_GRAY);
                }
            }
        });
    }

    private String toDisplayStatus(TrangThai trangThai) {
        if (trangThai == null) {
            return "";
        }
        return switch (trangThai) {
            case DANG_AP_DUNG -> "Đang hoạt động";
            case CHUA_AP_DUNG -> "Sắp diễn ra";
            case KET_THUC -> "Đã kết thúc";
        };
    }

    private String formatDateTime(LocalDateTime dateTime) {
        return dateTime == null ? "" : DISPLAY_DT.format(dateTime);
    }

    private String formatPercent(float heSo) {
        return String.format("%.0f%%", heSo * 100);
    }

    private String formatMoney(float value) {
        return String.format("%,.0f đ", value).replace(',', '.');
    }
}

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
    private static final DateTimeFormatter DISPLAY_DT = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

    private final KhuyenMaiService khuyenMaiService = new KhuyenMaiService();
    private final Runnable onChanged;
    private final boolean isNew;
    private final KhuyenMai current;

    private JTextField tfMa;
    private JTextField tfTen;
    private DateTimePickerField dtNgayBatDau;
    private DateTimePickerField dtNgayKetThuc;
    private JComboBox<TrangThai> cbTrangThai;
    private JTextField tfHeSo;
    private JTextField tfTongTienToiThieu;
    private JTextField tfTongKhuyenMaiToiDa;

    KhuyenMaiModal(JFrame owner, KhuyenMai khuyenMai, boolean isNew, Runnable onChanged) {
        super(owner, isNew ? "Thêm khuyến mãi" : "Chi tiết khuyến mãi", true);
        this.isNew = isNew;
        this.current = khuyenMai == null ? new KhuyenMai() : khuyenMai;
        this.onChanged = onChanged;

        setSize(640, 620);
        setLocationRelativeTo(owner);
        setResizable(false);

        JPanel root = new JPanel(new BorderLayout());
        root.setBackground(BG_WHITE);
        root.add(buildHeader(), BorderLayout.NORTH);
        root.add(buildForm(), BorderLayout.CENTER);
        root.add(buildFooter(), BorderLayout.SOUTH);
        setContentPane(root);
    }

    private JPanel buildHeader() {
        JPanel h = new JPanel(new BorderLayout());
        h.setBackground(BG_WHITE);
        h.setBorder(new CompoundBorder(new MatteBorder(0, 0, 1, 0, BORDER_COL), new EmptyBorder(18, 24, 16, 16)));

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
                g2.drawString(t, 22 - fm.stringWidth(t) / 2, 27);
                g2.dispose();
            }
        };
        icon.setPreferredSize(new Dimension(44, 44));

        JPanel txt = new JPanel();
        txt.setLayout(new BoxLayout(txt, BoxLayout.Y_AXIS));
        txt.setOpaque(false);
        JLabel t1 = new JLabel(isNew ? "Thêm chương trình khuyến mãi" : "Thông tin khuyến mãi");
        t1.setFont(new Font("Segoe UI", Font.BOLD, 15));
        t1.setForeground(TEXT_DARK);
        JLabel t2 = new JLabel(isNew ? "Nhập thông tin để lưu vào database" : "Mã: " + current.getMaKhuyenMai());
        t2.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        t2.setForeground(TEXT_GRAY);
        txt.add(t1);
        txt.add(Box.createVerticalStrut(2));
        txt.add(t2);
        left.add(icon);
        left.add(txt);

        h.add(left, BorderLayout.CENTER);
        h.add(makeCloseBtn(), BorderLayout.EAST);
        return h;
    }

    private JPanel buildForm() {
        JPanel form = new JPanel(new GridBagLayout());
        form.setBackground(BG_WHITE);
        form.setBorder(new EmptyBorder(20, 24, 20, 24));

        GridBagConstraints g = new GridBagConstraints();
        g.fill = GridBagConstraints.HORIZONTAL;
        g.weighty = 0;
        g.weightx = 0.5;

        tfMa = field(current.getMaKhuyenMai(), isNew);
        tfTen = field(current.getTenKhuyenMai(), true);
        dtNgayBatDau = new DateTimePickerField(current.getNgayBatDau());
        dtNgayKetThuc = new DateTimePickerField(current.getNgayKetThuc());
        tfHeSo = field(current.getHeSo() == 0 ? "" : String.valueOf(current.getHeSo() * 100), true);
        tfTongTienToiThieu = field(current.getTongTienToiThieu() == 0 ? "" : String.valueOf((long) current.getTongTienToiThieu()), true);
        tfTongKhuyenMaiToiDa = field(current.getTongKhuyenMaiToiDa() == 0 ? "" : String.valueOf((long) current.getTongKhuyenMaiToiDa()), true);

        cbTrangThai = new JComboBox<>(TrangThai.values());
        styleCombo(cbTrangThai);
        cbTrangThai.setRenderer(new DefaultListCellRenderer() {
            @Override public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                setText(toStatusLabel((TrangThai) value));
                return this;
            }
        });
        cbTrangThai.setSelectedItem(current.getTrangThai() == null ? TrangThai.CHUA_AP_DUNG : current.getTrangThai());

        addRow2(form, g, 0, "Mã khuyến mãi", tfMa, "Trạng thái", cbTrangThai);
        addRowFull(form, g, 1, "Tên khuyến mãi", tfTen);
        addRow2(form, g, 2, "Ngày bắt đầu", dtNgayBatDau,
                "Ngày kết thúc", dtNgayKetThuc);
        addRow2(form, g, 3, "Hệ số (%)", tfHeSo, "Tổng tiền tối thiểu", tfTongTienToiThieu);
        addRowFull(form, g, 4, "Tổng khuyến mãi tối đa", tfTongKhuyenMaiToiDa);

        g.gridx = 0;
        g.gridy = 10;
        g.gridwidth = 2;
        g.weighty = 1;
        g.fill = GridBagConstraints.BOTH;
        form.add(Box.createVerticalGlue(), g);
        return form;
    }

    private void addRow2(JPanel form, GridBagConstraints g, int row, String l1, JComponent f1, String l2, JComponent f2) {
        g.gridwidth = 1;
        g.insets = new Insets(0, 0, 4, 12);
        g.gridy = row * 2;
        g.gridx = 0;
        form.add(lbl(l1), g);
        g.insets = new Insets(0, 0, 4, 0);
        g.gridx = 1;
        form.add(lbl(l2), g);
        g.insets = new Insets(0, 0, 14, 12);
        g.gridy = row * 2 + 1;
        g.gridx = 0;
        form.add(f1, g);
        g.insets = new Insets(0, 0, 14, 0);
        g.gridx = 1;
        form.add(f2, g);
    }

    private void addRowFull(JPanel form, GridBagConstraints g, int row, String label, JComponent field) {
        g.gridwidth = 2;
        g.weightx = 1;
        g.gridx = 0;
        g.insets = new Insets(0, 0, 4, 0);
        g.gridy = row * 2;
        form.add(lbl(label), g);
        g.insets = new Insets(0, 0, 14, 0);
        g.gridy = row * 2 + 1;
        form.add(field, g);
    }

    private JLabel lbl(String text) {
        JLabel l = new JLabel(text);
        l.setFont(new Font("Segoe UI", Font.BOLD, 12));
        l.setForeground(TEXT_MID);
        return l;
    }

    private JTextField field(String val, boolean editable) {
        JTextField tf = new JTextField(val == null ? "" : val);
        tf.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        tf.setForeground(TEXT_DARK);
        tf.setEditable(editable);
        if (!editable) {
            tf.setBackground(new Color(0xF8F9FD));
        }
        tf.setPreferredSize(new Dimension(0, 38));
        tf.setBorder(new CompoundBorder(new LineBorder(BORDER_COL, 1, true), new EmptyBorder(0, 12, 0, 12)));
        return tf;
    }

    private void styleCombo(JComboBox<TrangThai> cb) {
        cb.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        cb.setBackground(BG_WHITE);
        cb.setForeground(TEXT_DARK);
        cb.setPreferredSize(new Dimension(0, 38));
        cb.setBorder(new LineBorder(BORDER_COL, 1, true));
        cb.setFocusable(false);
    }

    private JPanel buildFooter() {
        JPanel f = new JPanel(new FlowLayout(FlowLayout.RIGHT, 12, 12));
        f.setBackground(BG_WHITE);
        f.setBorder(new MatteBorder(1, 0, 0, 0, BORDER_COL));

        JButton cancel = QuanLyNhanVienPanel.createButton("Hủy", new Color(0xEEF0F5), new Color(0xDDE0E8), TEXT_MID, 80, 38);
        cancel.addActionListener(e -> dispose());
        f.add(cancel);

        if (isNew) {
            JButton save = QuanLyNhanVienPanel.createButton("Lưu", BLUE, new Color(0x2D5FD8), Color.WHITE, 100, 38);
            save.addActionListener(e -> onSave());
            f.add(save);
        } else {
            JButton del = QuanLyNhanVienPanel.createButton("Xóa", RED, RED.darker(), Color.WHITE, 80, 38);
            del.addActionListener(e -> onDelete());

            JButton upd = QuanLyNhanVienPanel.createButton("Cập nhật", BLUE, new Color(0x2D5FD8), Color.WHITE, 110, 38);
            upd.addActionListener(e -> onUpdate());

            f.add(del);
            f.add(upd);
        }
        return f;
    }

    private void onSave() {
        try {
            KhuyenMai km = collectFormData();
            if (khuyenMaiService.getKhuyenMaiById(km.getMaKhuyenMai()).isPresent()) {
                JOptionPane.showMessageDialog(this, "Mã khuyến mãi đã tồn tại.", "Lỗi", JOptionPane.ERROR_MESSAGE);
                return;
            }
            khuyenMaiService.addKhuyenMai(km);
            notifyChangedAndClose("Đã thêm khuyến mãi.");
        } catch (Exception ex) {
            showError(ex);
        }
    }

    private void onUpdate() {
        try {
            KhuyenMai km = collectFormData();
            khuyenMaiService.updateKhuyenMai(km);
            notifyChangedAndClose("Đã cập nhật khuyến mãi.");
        } catch (Exception ex) {
            showError(ex);
        }
    }

    private void onDelete() {
        int result = JOptionPane.showConfirmDialog(this, "Xóa khuyến mãi này?", "Xác nhận", JOptionPane.YES_NO_OPTION);
        if (result != JOptionPane.YES_OPTION) {
            return;
        }
        try {
            khuyenMaiService.deleteKhuyenMai(tfMa.getText().trim());
            notifyChangedAndClose("Đã xóa khuyến mãi.");
        } catch (Exception ex) {
            showError(ex);
        }
    }

    private KhuyenMai collectFormData() {
        String ma = tfMa.getText().trim();
        String ten = tfTen.getText().trim();
        LocalDateTime ngayBatDau = dtNgayBatDau.getValue();
        LocalDateTime ngayKetThuc = dtNgayKetThuc.getValue();
        TrangThai trangThai = (TrangThai) cbTrangThai.getSelectedItem();
        float heSo = parseHeSo(tfHeSo.getText().trim());
        float tongTienToiThieu = parseMoney(tfTongTienToiThieu.getText().trim());
        float tongKhuyenMaiToiDa = parseMoney(tfTongKhuyenMaiToiDa.getText().trim());

        if (ma.isBlank()) {
            throw new IllegalArgumentException("Mã khuyến mãi không được để trống.");
        }
        if (ten.isBlank()) {
            throw new IllegalArgumentException("Tên khuyến mãi không được để trống.");
        }
        if (ngayBatDau == null || ngayKetThuc == null) {
            throw new IllegalArgumentException("Ngày bắt đầu và ngày kết thúc phải hợp lệ.");
        }
        if (!ngayKetThuc.isAfter(ngayBatDau)) {
            throw new IllegalArgumentException("Ngày kết thúc phải sau ngày bắt đầu.");
        }

        return KhuyenMai.builder()
                .maKhuyenMai(ma)
                .tenKhuyenMai(ten)
                .ngayBatDau(ngayBatDau)
                .ngayKetThuc(ngayKetThuc)
                .trangThai(trangThai)
                .heSo(heSo)
                .tongTienToiThieu(tongTienToiThieu)
                .tongKhuyenMaiToiDa(tongKhuyenMaiToiDa)
                .build();
    }

    private float parseHeSo(String text) {
        if (text.isBlank()) {
            throw new IllegalArgumentException("Hệ số không được để trống.");
        }
        float value = Float.parseFloat(text.replace(',', '.'));
        if (value > 1f) {
            value = value / 100f;
        }
        if (value < 0f || value > 1f) {
            throw new IllegalArgumentException("Hệ số phải nằm trong khoảng 0 đến 100.");
        }
        return value;
    }

    private float parseMoney(String text) {
        if (text.isBlank()) {
            return 0f;
        }
        String cleaned = text.replace(".", "").replace(",", ".");
        return Float.parseFloat(cleaned);
    }

    private String toStatusLabel(TrangThai trangThai) {
        if (trangThai == null) {
            return "";
        }
        return switch (trangThai) {
            case DANG_AP_DUNG -> "Đang hoạt động";
            case CHUA_AP_DUNG -> "Sắp diễn ra";
            case KET_THUC -> "Đã kết thúc";
        };
    }

    private void notifyChangedAndClose(String message) {
        JOptionPane.showMessageDialog(this, message, "Thông báo", JOptionPane.INFORMATION_MESSAGE);
        if (onChanged != null) {
            onChanged.run();
        }
        dispose();
    }

    private void showError(Exception ex) {
        JOptionPane.showMessageDialog(this,
                ex.getMessage() == null ? ex.toString() : ex.getMessage(),
                "Lỗi", JOptionPane.ERROR_MESSAGE);
    }

    private JButton makeCloseBtn() {
        JButton b = new JButton("✕") {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(getModel().isRollover() ? new Color(0xFFF0F0) : BG_WHITE);
                g2.fillOval(0, 0, getWidth(), getHeight());
                g2.dispose();
                super.paintComponent(g);
            }
        };
        b.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        b.setForeground(TEXT_MID);
        b.setContentAreaFilled(false);
        b.setBorderPainted(false);
        b.setFocusPainted(false);
        b.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        b.setPreferredSize(new Dimension(30, 30));
        b.addActionListener(e -> dispose());
        return b;
    }

}

class DateTimePickerField extends JPanel {


    private final JDateChooser dateChooser;
    private final JSpinner timeSpinner;

    DateTimePickerField(LocalDateTime initialValue) {
        setLayout(new BorderLayout(8, 0));
        setOpaque(false);

        dateChooser = new JDateChooser();
        dateChooser.setDateFormatString("dd/MM/yyyy");
        dateChooser.setPreferredSize(new Dimension(0, 38));

        JTextField dateEditor = (JTextField) dateChooser.getDateEditor().getUiComponent();
        dateEditor.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        dateEditor.setForeground(QuanLyNhanVienPanel.TEXT_DARK);
        dateEditor.setBorder(new CompoundBorder(new LineBorder(QuanLyNhanVienPanel.BORDER_COL, 1, true), new EmptyBorder(0, 12, 0, 12)));

        timeSpinner = new JSpinner(new SpinnerDateModel());
        timeSpinner.setPreferredSize(new Dimension(96, 38));
        timeSpinner.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        JSpinner.DateEditor editor = new JSpinner.DateEditor(timeSpinner, "HH:mm");
        timeSpinner.setEditor(editor);
        editor.getTextField().setBorder(new CompoundBorder(new LineBorder(QuanLyNhanVienPanel.BORDER_COL, 1, true), new EmptyBorder(0, 8, 0, 8)));

        add(dateChooser, BorderLayout.CENTER);
        add(timeSpinner, BorderLayout.EAST);

        setValue(initialValue);
    }

    LocalDateTime getValue() {
        Date selectedDate = dateChooser.getDate();
        if (selectedDate == null) {
            return null;
        }
        LocalDateTime datePart = LocalDateTime.ofInstant(selectedDate.toInstant(), ZoneId.systemDefault());
        Date selectedTime = (Date) timeSpinner.getValue();
        LocalDateTime timePart = LocalDateTime.ofInstant(selectedTime.toInstant(), ZoneId.systemDefault());
        return datePart.withHour(timePart.getHour()).withMinute(timePart.getMinute()).withSecond(0).withNano(0);
    }

    void setValue(LocalDateTime value) {
        LocalDateTime actual = value == null ? LocalDateTime.now().withSecond(0).withNano(0) : value.withSecond(0).withNano(0);
        Date date = Date.from(actual.atZone(ZoneId.systemDefault()).toInstant());
        dateChooser.setDate(date);
        timeSpinner.setValue(date);
    }
}

