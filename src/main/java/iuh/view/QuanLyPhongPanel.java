package iuh.view;

import iuh.entity.LoaiPhong;
import iuh.entity.Phong;
import iuh.service.LoaiPhongService;
import iuh.service.PhongService;

import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.*;

public class QuanLyPhongPanel extends JPanel {

    private static final Color BG_MAIN = QuanLyNhanVienPanel.BG_MAIN;
    private static final Color BG_WHITE = QuanLyNhanVienPanel.BG_WHITE;
    private static final Color BLUE = QuanLyNhanVienPanel.BLUE;
    private static final Color TEXT_DARK = QuanLyNhanVienPanel.TEXT_DARK;
    private static final Color TEXT_MID = QuanLyNhanVienPanel.TEXT_MID;
    private static final Color TEXT_GRAY = QuanLyNhanVienPanel.TEXT_GRAY;
    private static final Color BORDER_COL = QuanLyNhanVienPanel.BORDER_COL;
    private static final Color HEADER_BG = QuanLyNhanVienPanel.HEADER_BG;
    private static final Color ROW_ALT = QuanLyNhanVienPanel.ROW_ALT;
    private static final Color ROW_SEL = QuanLyNhanVienPanel.ROW_SEL;
    private static final Color GREEN = QuanLyNhanVienPanel.GREEN;
    private static final Color GREEN_BG = QuanLyNhanVienPanel.GREEN_BG;
    private static final Color ORANGE = QuanLyNhanVienPanel.ORANGE;
    private static final Color ORANGE_BG = QuanLyNhanVienPanel.ORANGE_BG;
    private static final Color RED = QuanLyNhanVienPanel.RED;
    private static final Color RED_BG = QuanLyNhanVienPanel.RED_BG;
    private static final Color BLUE_LIGHT = QuanLyNhanVienPanel.BLUE_LIGHT;

    private static final Font F_TITLE = QuanLyNhanVienPanel.F_TITLE;
    private static final Font F_LABEL = QuanLyNhanVienPanel.F_LABEL;
    private static final Font F_BOLD13 = QuanLyNhanVienPanel.F_BOLD13;
    private static final Font F_TABLE = QuanLyNhanVienPanel.F_TABLE;
    private static final Font F_TABLE_H = QuanLyNhanVienPanel.F_TABLE_H;

    private static final String SEARCH_PLACEHOLDER = "Tìm theo mã phòng, số phòng, loại...";

    private final PhongService phongService = new PhongService();
    private final LoaiPhongService loaiPhongService = new LoaiPhongService();

    private DefaultTableModel tableModel;
    private JTable table;
    private JTextField tfSearch;

    public QuanLyPhongPanel() {
        setLayout(new BorderLayout());
        setBackground(BG_MAIN);
        setBorder(new EmptyBorder(28, 28, 28, 28));

        JLabel title = new JLabel("Quản lý phòng");
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

        JButton btnSearch = QuanLyNhanVienPanel.createButton("Tìm kiếm", BLUE, new Color(0x2D5FD8), Color.WHITE, 100,
                38);
        btnSearch.addActionListener(e -> refreshTable());

        JButton btnReset = QuanLyNhanVienPanel.createButton("Làm mới", new Color(0xEEF0F5), new Color(0xDDE0E8),
                TEXT_MID, 95, 38);
        btnReset.addActionListener(e -> {
            tfSearch.setText(SEARCH_PLACEHOLDER);
            tfSearch.setForeground(TEXT_GRAY);
            refreshTable();
        });

        left.add(tfSearch);
        left.add(btnSearch);
        left.add(btnReset);

        JButton btnAdd = QuanLyNhanVienPanel.createButton("+ Thêm phòng", GREEN, new Color(0x1E8449), Color.WHITE, 140,
                38);
        btnAdd.addActionListener(e -> openModal(null, true));

        JPanel right = new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 0));
        right.setOpaque(false);
        right.add(btnAdd);

        bar.add(left, BorderLayout.WEST);
        bar.add(right, BorderLayout.EAST);

        JPanel wrap = new JPanel(new BorderLayout());
        wrap.setOpaque(false);
        wrap.add(bar, BorderLayout.CENTER);
        wrap.add(new JSeparator() {
            {
                setForeground(BORDER_COL);
            }
        }, BorderLayout.SOUTH);
        return wrap;
    }

    private JScrollPane buildTable() {
        tableModel = new DefaultTableModel(new Object[0][0], new String[] {
                "Mã phòng", "Số phòng", "Tầng", "Loại phòng", "Mô tả", "Tình trạng", "Trạng thái"
        }) {
            @Override
            public boolean isCellEditable(int r, int c) {
                return false;
            }
        };

        table = new JTable(tableModel);
        styleTable(table);
        table.setRowHeight(52);
        table.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);

        // === RENDERER CHO CÁC CỘT THƯỜNG (Mã, Số, Tầng, Loại, Mô tả) ===
        DefaultTableCellRenderer base = new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable t, Object v, boolean sel, boolean foc, int row,
                    int col) {
                super.getTableCellRendererComponent(t, v, sel, foc, row, col);
                setBackground(sel ? ROW_SEL : BG_WHITE);
                setForeground(col == 0 ? BLUE : TEXT_DARK);
                setFont(col == 0 ? F_BOLD13 : F_TABLE);
                setBorder(new EmptyBorder(0, 16, 0, 8));
                setHorizontalAlignment(col == 2 ? JLabel.CENTER : JLabel.LEFT);
                return this;
            }
        };

        // === RENDERER CHO CỘT "TÌNH TRẠNG" (Trống/Đang thuê) ===
        DefaultTableCellRenderer tinhTrangRenderer = new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable t, Object val, boolean sel, boolean foc, int row,
                    int col) {
                String text = val == null ? "" : val.toString();
                JPanel cell = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
                cell.setBackground(sel ? ROW_SEL : BG_WHITE);
                cell.setBorder(new EmptyBorder(11, 16, 11, 8));

                Color bg, fg;
                if ("Trống".equals(text)) {
                    bg = GREEN_BG;
                    fg = GREEN;
                } else {
                    bg = ORANGE_BG;
                    fg = ORANGE;
                }

                final Color finalBg = bg;
                final Color finalFg = fg;
                JLabel pill = new JLabel(text) {
                    @Override
                    protected void paintComponent(Graphics g) {
                        Graphics2D g2 = (Graphics2D) g.create();
                        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                        g2.setColor(finalBg);
                        g2.fillRoundRect(0, 0, getWidth(), getHeight(), 20, 20);
                        g2.dispose();
                        super.paintComponent(g);
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

        // === RENDERER CHO CỘT "TRẠNG THÁI" (Sẵn sàng/Đang sửa) ===
        DefaultTableCellRenderer trangThaiRenderer = new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable t, Object val, boolean sel, boolean foc, int row,
                    int col) {
                String text = val == null ? "" : val.toString();
                JPanel cell = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
                cell.setBackground(sel ? ROW_SEL : BG_WHITE);
                cell.setBorder(new EmptyBorder(11, 16, 11, 8));

                Color bg, fg;
                if ("Sẵn sàng".equals(text)) {
                    bg = BLUE_LIGHT;
                    fg = BLUE;
                } else {
                    bg = RED_BG;
                    fg = RED;
                }

                final Color finalBg = bg;
                final Color finalFg = fg;
                JLabel pill = new JLabel(text) {
                    @Override
                    protected void paintComponent(Graphics g) {
                        Graphics2D g2 = (Graphics2D) g.create();
                        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                        g2.setColor(finalBg);
                        g2.fillRoundRect(0, 0, getWidth(), getHeight(), 20, 20);
                        g2.dispose();
                        super.paintComponent(g);
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

        // Áp dụng renderer cho các cột
        for (int i = 0; i < 5; i++) {
            table.getColumnModel().getColumn(i).setCellRenderer(base);
        }
        table.getColumnModel().getColumn(5).setCellRenderer(tinhTrangRenderer);
        table.getColumnModel().getColumn(6).setCellRenderer(trangThaiRenderer);

        // Cài đặt độ rộng cột
        int[] widths = { 80, 80, 55, 160, 200, 110, 130 };
        for (int i = 0; i < widths.length; i++) {
            table.getColumnModel().getColumn(i).setPreferredWidth(widths[i]);
        }

        // Double-click để xem/sửa
        table.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    int row = table.getSelectedRow();
                    if (row >= 0) {
                        String maPhong = String.valueOf(tableModel.getValueAt(row, 0));
                        java.util.Optional<Phong> phongOpt = phongService.getRoomById(maPhong);
                        if (phongOpt.isPresent()) {
                            openModal(phongOpt.get(), false);
                        }
                    }
                }
            }
        });
        table.setToolTipText("Double-click để xem / chỉnh sửa phòng");

        JScrollPane sp = new JScrollPane(table);
        sp.setBorder(BorderFactory.createEmptyBorder());
        sp.getViewport().setBackground(BG_WHITE);
        return sp;
    }

    private void refreshTable() {
        java.util.List<Phong> list = getCurrentData();
        tableModel.setRowCount(0);
        for (Phong p : list) {
            tableModel.addRow(toRow(p));
        }
    }

    private java.util.List<Phong> getCurrentData() {
        String keyword = getSearchKeyword();
        if (keyword.isBlank()) {
            return phongService.getAllRoom();
        }
        return phongService.getRoomByKeyword(keyword);
    }

    private String getSearchKeyword() {
        String text = tfSearch == null ? "" : tfSearch.getText().trim();
        return SEARCH_PLACEHOLDER.equals(text) ? "" : text;
    }

    private Object[] toRow(Phong p) {
        return new Object[] {
                p.getMaPhong(),
                p.getSoPhong(),
                p.getTang(),
                p.getLoaiPhong() != null ? p.getLoaiPhong().getTenLoaiPhong() : "",
                p.getMoTa(),
                p.getTinhTrang() != null ? p.getTinhTrang().toString() : "",
                p.getTrangThai() != null ? p.getTrangThai().toString() : ""
        };
    }

    private void openModal(Phong phong, boolean isNew) {
        JFrame top = (JFrame) SwingUtilities.getWindowAncestor(this);
        new PhongModal(top, phong, isNew, this::refreshTable).setVisible(true);
    }

    private void styleTable(JTable t) {
        t.setFont(F_TABLE);
        t.setShowVerticalLines(true);
        t.setShowHorizontalLines(true);
        t.setGridColor(BORDER_COL);
        t.setBackground(BG_WHITE);
        t.setSelectionBackground(ROW_SEL);
        t.setSelectionForeground(TEXT_DARK);
        // ✅ QUAN TRỌNG: Đặt spacing = (1, 1) để viền dọc VÀ ngang hiển thị rõ ràng ở
        // tất cả ô
        t.setIntercellSpacing(new Dimension(1, 1));
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
            @Override
            public void focusGained(FocusEvent e) {
                if (tf.getText().equals(ph)) {
                    tf.setText("");
                    tf.setForeground(TEXT_DARK);
                }
            }

            @Override
            public void focusLost(FocusEvent e) {
                if (tf.getText().isEmpty()) {
                    tf.setText(ph);
                    tf.setForeground(TEXT_GRAY);
                }
            }
        });
    }
}

class PhongModal extends JDialog {

    private static final Color BG_WHITE = QuanLyNhanVienPanel.BG_WHITE;
    private static final Color BLUE = QuanLyNhanVienPanel.BLUE;
    private static final Color TEXT_DARK = QuanLyNhanVienPanel.TEXT_DARK;
    private static final Color TEXT_MID = QuanLyNhanVienPanel.TEXT_MID;
    private static final Color TEXT_GRAY = QuanLyNhanVienPanel.TEXT_GRAY;
    private static final Color BORDER_COL = QuanLyNhanVienPanel.BORDER_COL;
    private static final Color GREEN = QuanLyNhanVienPanel.GREEN;
    private static final Color RED = QuanLyNhanVienPanel.RED;
    private static final Color BLUE_LIGHT = QuanLyNhanVienPanel.BLUE_LIGHT;

    private final PhongService phongService = new PhongService();
    private final Runnable onChanged;
    private final boolean isNew;
    private final Phong current;

    private JTextField tfMa;
    private JTextField tfSo;
    private JComboBox<Integer> cbTang;
    private JComboBox<LoaiPhong> cbLoaiPhong;
    private JTextField tfMoTa;
    private JComboBox<String> cbTinhTrang;
    private JComboBox<String> cbTrangThai;
    private LoaiPhongService loaiPhongService = new LoaiPhongService();

    PhongModal(JFrame owner, Phong phong, boolean isNew, Runnable onChanged) {
        super(owner, isNew ? "Thêm phòng" : "Chi tiết phòng", true);
        this.isNew = isNew;
        this.current = phong == null ? new Phong() : phong;
        this.onChanged = onChanged;

        setSize(560, 600);
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
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(BLUE_LIGHT);
                g2.fillOval(0, 0, 44, 44);
                g2.setColor(BLUE);
                g2.setFont(new Font("Segoe UI", Font.BOLD, 15));
                String t = isNew ? "+" : "🏠";
                FontMetrics fm = g2.getFontMetrics();
                g2.drawString(t, 22 - fm.stringWidth(t) / 2, 27);
                g2.dispose();
            }
        };
        icon.setPreferredSize(new Dimension(44, 44));

        JPanel txt = new JPanel();
        txt.setLayout(new BoxLayout(txt, BoxLayout.Y_AXIS));
        txt.setOpaque(false);
        JLabel t1 = new JLabel(isNew ? "Thêm phòng mới" : "Thông tin phòng");
        t1.setFont(new Font("Segoe UI", Font.BOLD, 15));
        t1.setForeground(TEXT_DARK);
        JLabel t2 = new JLabel(isNew ? "Nhập thông tin phòng mới" : "Mã: " + current.getMaPhong());
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

        tfMa = field(current.getMaPhong(), false);
        tfSo = field(current.getSoPhong(), false);

        cbTang = new JComboBox<>();
        for (int t : phongService.getAllTang()) {
            cbTang.addItem(t);
        }
        styleCombo(cbTang);
        if (!isNew)
            cbTang.setSelectedItem(current.getTang());

        cbLoaiPhong = new JComboBox<>();
        for (LoaiPhong lp : loaiPhongService.getAll()) {
            cbLoaiPhong.addItem(lp);
        }
        styleCombo(cbLoaiPhong);

        tfMoTa = field(current.getMoTa(), true);

        cbTinhTrang = new JComboBox<>();
        for (String tinhTrang : phongService.getAllTinhTrang()) {
            cbTinhTrang.addItem(tinhTrang);
        }
        styleCombo(cbTinhTrang);
        if (!isNew)
            cbTinhTrang.setSelectedItem(current.getTinhTrang() != null ? current.getTinhTrang().toString() : "Trống");

        cbTrangThai = new JComboBox<>();
        for (String trangThai : phongService.getAllTrangThai()) {
            cbTrangThai.addItem(trangThai);
        }
        styleCombo(cbTrangThai);
        if (!isNew)
            cbTrangThai
                    .setSelectedItem(current.getTrangThai() != null ? current.getTrangThai().toString() : "Sẵn sàng");

        addRow2(form, g, 0, "Mã phòng", tfMa, "Số phòng", tfSo);
        addRow2(form, g, 1, "Tầng", cbTang, "Loại phòng", cbLoaiPhong);
        addRowFull(form, g, 2, "Mô tả", tfMoTa);
        addRow2(form, g, 3, "Tình trạng", cbTinhTrang, "Trạng thái", cbTrangThai);

        g.gridx = 0;
        g.gridy = 10;
        g.gridwidth = 2;
        g.weighty = 1;
        g.fill = GridBagConstraints.BOTH;
        form.add(Box.createVerticalGlue(), g);
        return form;
    }

    private void addRow2(JPanel form, GridBagConstraints g, int row, String l1, JComponent f1, String l2,
            JComponent f2) {
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
        tf.setPreferredSize(new Dimension(40, 44));
        tf.setBorder(new CompoundBorder(new LineBorder(BORDER_COL, 1, true), new EmptyBorder(0, 12, 0, 12)));
        return tf;
    }

    private void styleCombo(JComboBox<?> cb) {
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

        JButton cancel = QuanLyNhanVienPanel.createButton("Hủy", new Color(0xEEF0F5), new Color(0xDDE0E8), TEXT_MID, 80,
                38);
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

    public Phong getThisForm() {
        int tang = (Integer) cbTang.getSelectedItem();
        LoaiPhong loaiPhong = (LoaiPhong) cbLoaiPhong.getSelectedItem();
        String moTa = tfMoTa.getText();
        String tinhTrang = cbTinhTrang.getSelectedItem().toString();
        String trangThai = cbTrangThai.getSelectedItem().toString();

        Phong phongMoi = Phong.builder()
                .tang(tang)
                .moTa(moTa)
                .loaiPhong(loaiPhong)
                .tinhTrang(tinhTrang)
                .trangThai(trangThai)
                .build();
        if (phongService.checkNull(phongMoi)) {
            return phongMoi;
        }
        return null;
    }

    private void onSave() {
        try {
            Phong phongMoi = getThisForm();
            phongService.createPhong(phongMoi);
            JOptionPane.showMessageDialog(this, "Đã thêm phòng.", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
            if (onChanged != null)
                onChanged.run();
            dispose();
        } catch (Exception ex) {
            showError(ex);
        }
    }

    private void onUpdate() {
        try {
            String maPhong = tfMa.getText().trim();
            String soPhong = tfSo.getText().trim();
            Phong phongMoi = getThisForm();
            phongMoi.setMaPhong(maPhong);
            phongMoi.setSoPhong(soPhong);
            phongService.updatePhong(phongMoi);

            // TODO: Validate and update
            JOptionPane.showMessageDialog(this, "Đã cập nhật phòng.", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
            if (onChanged != null)
                onChanged.run();
            dispose();
        } catch (Exception ex) {
            showError(ex);
        }
    }

    private void onDelete() {
        int result = JOptionPane.showConfirmDialog(this, "Xóa phòng này?", "Xác nhận", JOptionPane.YES_NO_OPTION);
        if (result != JOptionPane.YES_OPTION)
            return;
        try {
            // TODO: Delete room
            String maPhong = tfMa.getText().trim();
            if (phongService.deletePhong(maPhong))
                JOptionPane.showMessageDialog(this, "Đã xóa phòng.", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
            else
                JOptionPane.showMessageDialog(this, "Đã xóa phòng thất bại!.", "Thông báo", JOptionPane.ERROR_MESSAGE);

            if (onChanged != null)
                onChanged.run();
            dispose();
        } catch (Exception ex) {
            showError(ex);
        }
    }

    private void showError(Exception ex) {
        JOptionPane.showMessageDialog(this,
                ex.getMessage() == null ? ex.toString() : ex.getMessage(),
                "Lỗi", JOptionPane.ERROR_MESSAGE);
    }

    private JButton makeCloseBtn() {
        JButton b = new JButton("✕") {
            @Override
            protected void paintComponent(Graphics g) {
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
