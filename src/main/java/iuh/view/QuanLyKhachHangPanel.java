package iuh.view;

import iuh.entity.KhachHang;
import iuh.service.KhachHangService;

import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.*;
import java.time.LocalDate;

public class QuanLyKhachHangPanel extends JPanel {

    private static final Color BG_MAIN    = QuanLyNhanVienPanel.BG_MAIN;
    private static final Color BG_WHITE   = QuanLyNhanVienPanel.BG_WHITE;
    private static final Color BLUE       = QuanLyNhanVienPanel.BLUE;
    private static final Color TEXT_DARK  = QuanLyNhanVienPanel.TEXT_DARK;
    private static final Color TEXT_MID   = QuanLyNhanVienPanel.TEXT_MID;
    private static final Color TEXT_GRAY  = QuanLyNhanVienPanel.TEXT_GRAY;
    private static final Color BORDER_COL = QuanLyNhanVienPanel.BORDER_COL;
    private static final Color HEADER_BG  = QuanLyNhanVienPanel.HEADER_BG;
    private static final Color ROW_ALT    = QuanLyNhanVienPanel.ROW_ALT;
    private static final Color ROW_SEL    = QuanLyNhanVienPanel.ROW_SEL;
    private static final Color GREEN      = QuanLyNhanVienPanel.GREEN;
    private static final Color RED        = QuanLyNhanVienPanel.RED;

    private static final Font F_TITLE   = QuanLyNhanVienPanel.F_TITLE;
    private static final Font F_LABEL   = QuanLyNhanVienPanel.F_LABEL;
    private static final Font F_BOLD13  = QuanLyNhanVienPanel.F_BOLD13;
    private static final Font F_TABLE   = QuanLyNhanVienPanel.F_TABLE;
    private static final Font F_TABLE_H = QuanLyNhanVienPanel.F_TABLE_H;

    private static final String[] COLS = {
            "Mã KH", "Họ và tên", "Số điện thoại", "Email", "Ngày tạo"
    };

    private DefaultTableModel tableModel;
    private JTextField tfSearch;

    private KhachHangService service = new KhachHangService();

    public QuanLyKhachHangPanel() {
        setLayout(new BorderLayout());
        setBackground(BG_MAIN);
        setBorder(new EmptyBorder(28, 28, 28, 28));

        JLabel title = new JLabel("Quản lý khách hàng");
        title.setFont(F_TITLE);
        title.setForeground(TEXT_DARK);
        title.setBorder(new EmptyBorder(0, 0, 18, 0));
        add(title, BorderLayout.NORTH);

        JPanel card = new ThanhToanPanel.RoundedPanel(14, BG_WHITE);
        card.setLayout(new BorderLayout());
        card.add(buildToolbar(), BorderLayout.NORTH);
        card.add(buildTable(),   BorderLayout.CENTER);
        add(card, BorderLayout.CENTER);

        loadDatabase();
    }

    public void loadDatabase() {
        try {
            java.util.List<KhachHang> list = service.loadAll();
            tableModel.setRowCount(0);

            for (KhachHang kh : list) {
                tableModel.addRow(new Object[] {
                        kh.getMaKhachHang(),
                        kh.getTenKhachHang(),
                        kh.getSoDienThoai(),
                        kh.getEmail(),
                        kh.getNgayTao() != null ? kh.getNgayTao().toString() : "N/A"
                });
            }
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Lỗi khi tải dữ liệu khách hàng: " + e.getMessage());
        }
    }

    private JPanel buildToolbar() {
        JPanel bar = new JPanel(new BorderLayout(12, 0));
        bar.setOpaque(false);
        bar.setBorder(new EmptyBorder(16, 20, 12, 20));

        JPanel left = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0));
        left.setOpaque(false);

        tfSearch = new JTextField(22);
        tfSearch.setFont(F_LABEL);
        tfSearch.setForeground(TEXT_DARK);
        tfSearch.setPreferredSize(new Dimension(240, 38));
        tfSearch.setBorder(new CompoundBorder(
                new LineBorder(BORDER_COL, 1, true), new EmptyBorder(0, 12, 0, 12)));
        setPlaceholder(tfSearch, "Tìm theo tên, SĐT, email...");

        JButton btnSearch = QuanLyNhanVienPanel.createButton("Tìm kiếm", BLUE, new Color(0x2D5FD8), Color.WHITE, 100, 38);
        btnSearch.addActionListener(e -> {
            String kw = tfSearch.getText().equals("Tìm theo tên, SĐT, email...") ? "" : tfSearch.getText();
            java.util.List<KhachHang> list = service.timKiem(kw);
            tableModel.setRowCount(0);
            for (KhachHang kh : list) {
                tableModel.addRow(new Object[]{kh.getMaKhachHang(), kh.getTenKhachHang(), kh.getSoDienThoai(), kh.getEmail(), kh.getNgayTao()});
            }
        });

        left.add(tfSearch);
        left.add(btnSearch);

        JButton btnAdd = QuanLyNhanVienPanel.createButton(
                "+ Thêm khách hàng", GREEN, new Color(0x1E8449), Color.WHITE, 165, 38);
        btnAdd.addActionListener(e -> openModal(null));

        JPanel right = new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 0));
        right.setOpaque(false);
        right.add(btnAdd);

        bar.add(left,  BorderLayout.WEST);
        bar.add(right, BorderLayout.EAST);

        JPanel wrap = new JPanel(new BorderLayout());
        wrap.setOpaque(false);
        wrap.add(bar, BorderLayout.CENTER);
        wrap.add(new JSeparator() {{ setForeground(BORDER_COL); }}, BorderLayout.SOUTH);
        return wrap;
    }

    private JScrollPane buildTable() {
        tableModel = new DefaultTableModel(null, COLS) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };

        JTable table = new JTable(tableModel);
        styleTable(table);
        table.setRowHeight(52);
        table.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);

        DefaultTableCellRenderer base = new DefaultTableCellRenderer() {
            @Override public Component getTableCellRendererComponent(
                    JTable t, Object v, boolean sel, boolean foc, int row, int col) {
                super.getTableCellRendererComponent(t, v, sel, foc, row, col);
                setBackground(sel ? ROW_SEL : (row % 2 == 0 ? BG_WHITE : ROW_ALT));
                setForeground(col == 0 ? BLUE : TEXT_DARK);
                setFont(col == 0 ? F_BOLD13 : F_TABLE);
                setBorder(new EmptyBorder(0, 16, 0, 8));
                setHorizontalAlignment(JLabel.LEFT);
                return this;
            }
        };
        for (int i = 0; i < COLS.length; i++)
            table.getColumnModel().getColumn(i).setCellRenderer(base);

        int[] widths = {80, 180, 130, 220, 110};
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
        table.setToolTipText("Double-click để xem / chỉnh sửa");

        JScrollPane sp = new JScrollPane(table);
        sp.setBorder(BorderFactory.createEmptyBorder());
        sp.getViewport().setBackground(BG_WHITE);
        return sp;
    }

    private void openModal(Integer row) {
        Object[] data = null;
        if (row != null) {
            data = new Object[tableModel.getColumnCount()];
            for (int c = 0; c < data.length; c++) data[c] = tableModel.getValueAt(row, c);
        }
        JFrame top = (JFrame) SwingUtilities.getWindowAncestor(this);
        KhachHangModal modal = new KhachHangModal(top, data, row == null);
        modal.setVisible(true);
        loadDatabase(); // Nạp lại bảng sau khi Modal đóng
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

    // =============================================================================
    //  MODAL KHÁCH HÀNG (CRUD)
    // =============================================================================
    class KhachHangModal extends JDialog {

        private JTextField tfMa, tfTen, tfSdt, tfEmail, tfCccd;
        private KhachHangService service = new KhachHangService();
        private Object[] rowData;

        KhachHangModal(JFrame owner, Object[] data, boolean isNew) {
            super(owner, isNew ? "Thêm khách hàng" : "Chi tiết khách hàng", true);
            this.rowData = data;
            setSize(520, 480);
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

            JLabel av = new JLabel() {
                @Override protected void paintComponent(Graphics g) {
                    Graphics2D g2 = (Graphics2D) g.create();
                    g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                    g2.setColor(new Color(0xE8F8EF));
                    g2.fillOval(0, 0, 44, 44);
                    g2.setColor(GREEN);
                    g2.setFont(new Font("Segoe UI", Font.BOLD, 16));
                    String txt = isNew ? "+" : "KH";
                    FontMetrics fm = g2.getFontMetrics();
                    g2.drawString(txt, 22 - fm.stringWidth(txt)/2, 27);
                    g2.dispose();
                }
            };
            av.setPreferredSize(new Dimension(44, 44));

            JPanel txt = new JPanel();
            txt.setLayout(new BoxLayout(txt, BoxLayout.Y_AXIS)); txt.setOpaque(false);
            JLabel t1 = new JLabel(isNew ? "Thêm khách hàng mới" : "Thông tin khách hàng");
            t1.setFont(new Font("Segoe UI", Font.BOLD, 16)); t1.setForeground(TEXT_DARK);
            JLabel t2 = new JLabel(isNew ? "Nhập đầy đủ thông tin bên dưới" : "Mã: " + (data != null ? data[0] : ""));
            t2.setFont(new Font("Segoe UI", Font.PLAIN, 12)); t2.setForeground(TEXT_GRAY);
            txt.add(t1); txt.add(Box.createVerticalStrut(2)); txt.add(t2);

            left.add(av); left.add(txt);
            h.add(left, BorderLayout.CENTER);
            h.add(makeCloseBtn(), BorderLayout.EAST);
            return h;
        }

        private JPanel buildForm(Object[] data, boolean isNew) {
            JPanel form = new JPanel(new GridBagLayout());
            form.setBackground(BG_WHITE);
            form.setBorder(new EmptyBorder(20, 24, 20, 24));

            GridBagConstraints g = new GridBagConstraints();
            g.fill = GridBagConstraints.HORIZONTAL;
            g.weighty = 0; g.weightx = 0.5;

            // QUAN TRỌNG: Gán thẳng vào biến của Class để nút Lưu lấy được dữ liệu
            tfMa = field(isNew ? service.phatSinhMaMoi() : s(data,0), false);
            tfCccd = field("", true);

            addRow2(form, g, 0, "Mã khách hàng", tfMa, "CCCD / CMND", tfCccd);

            tfTen = field(isNew ? "" : s(data,1), true);
            addRowFull(form, g, 1, "Họ và tên", tfTen);

            tfSdt = field(isNew ? "" : s(data,2), true);
            tfEmail = field(isNew ? "" : s(data,3), true);
            addRow2(form, g, 2, "Số điện thoại", tfSdt, "Email", tfEmail);

            JTextField tfDate = field(isNew ? LocalDate.now().toString() : s(data,4), false);
            tfDate.setBackground(new Color(0xF8F9FD));
            addRowFull(form, g, 3, "Ngày tạo (hệ thống)", tfDate);

            g.gridx=0; g.gridy=8; g.gridwidth=2; g.weighty=1; g.fill=GridBagConstraints.BOTH;
            form.add(Box.createVerticalGlue(), g);
            return form;
        }

        private void addRow2(JPanel form, GridBagConstraints g, int row,
                             String l1, JComponent f1, String l2, JComponent f2) {
            g.gridwidth=1;
            g.insets = new Insets(0,0,4,12); g.gridy=row*2; g.gridx=0; form.add(lbl(l1),g);
            g.insets = new Insets(0,0,4,0);  g.gridx=1; form.add(lbl(l2),g);
            g.insets = new Insets(0,0,14,12); g.gridy=row*2+1; g.gridx=0; form.add(f1,g);
            g.insets = new Insets(0,0,14,0);  g.gridx=1; form.add(f2,g);
        }

        private void addRowFull(JPanel form, GridBagConstraints g, int row, String label, JComponent field) {
            g.gridwidth=2; g.weightx=1; g.gridx=0;
            g.insets=new Insets(0,0,4,0); g.gridy=row*2; form.add(lbl(label),g);
            g.insets=new Insets(0,0,14,0); g.gridy=row*2+1; form.add(field,g);
        }

        private JLabel lbl(String t) {
            JLabel l = new JLabel(t);
            l.setFont(new Font("Segoe UI", Font.BOLD, 12));
            l.setForeground(TEXT_MID);
            return l;
        }

        private JTextField field(String val, boolean editable) {
            JTextField tf = new JTextField(val);
            tf.setFont(new Font("Segoe UI", Font.PLAIN, 13));
            tf.setForeground(TEXT_DARK);
            tf.setEditable(editable);
            if (!editable) tf.setBackground(new Color(0xF8F9FD));
            tf.setPreferredSize(new Dimension(0, 38));
            tf.setBorder(new CompoundBorder(new LineBorder(BORDER_COL, 1, true), new EmptyBorder(0,12,0,12)));
            return tf;
        }

        private JPanel buildFooter(boolean isNew) {
            JPanel f = new JPanel(new FlowLayout(FlowLayout.RIGHT, 12, 12));
            f.setBackground(BG_WHITE);
            f.setBorder(new MatteBorder(1, 0, 0, 0, BORDER_COL));

            JButton cancel = QuanLyNhanVienPanel.createButton("Hủy", new Color(0xEEF0F5), new Color(0xDDE0E8), TEXT_MID, 80, 38);
            cancel.addActionListener(e -> dispose());

            if (isNew) {
                // LOGIC THÊM
                JButton save = QuanLyNhanVienPanel.createButton("Lưu", BLUE, new Color(0x2D5FD8), Color.WHITE, 100, 38);
                save.addActionListener(e -> {
                    try {
                        KhachHang kh = KhachHang.builder()
                                .maKhachHang(tfMa.getText())
                                .CCCD(tfCccd.getText().trim())
                                .tenKhachHang(tfTen.getText().trim())
                                .soDienThoai(tfSdt.getText().trim())
                                .email(tfEmail.getText().trim())
                                .ngayTao(LocalDate.now())
                                .build();

                        if (service.themKhachHang(kh)) {
                            JOptionPane.showMessageDialog(this, "Thêm khách hàng thành công!");
                            dispose();
                        } else {
                            JOptionPane.showMessageDialog(this, "Lỗi khi lưu vào CSDL!");
                        }
                    } catch (Exception ex) {
                        JOptionPane.showMessageDialog(this, "Lỗi dữ liệu: " + ex.getMessage());
                    }
                });
                f.add(cancel);
                f.add(save);
            } else {
                // LOGIC XÓA
                JButton del = QuanLyNhanVienPanel.createButton("Xóa", RED, RED.darker(), Color.WHITE, 80, 38);
                del.addActionListener(e -> {
                    int confirm = JOptionPane.showConfirmDialog(this, "Xóa khách hàng " + tfMa.getText() + "?", "Xác nhận", JOptionPane.YES_NO_OPTION);
                    if (confirm == JOptionPane.YES_OPTION) {
                        if (service.xoaKhachHang(tfMa.getText())) {
                            JOptionPane.showMessageDialog(this, "Xóa thành công!");
                            dispose();
                        } else {
                            JOptionPane.showMessageDialog(this, "Không thể xóa khách hàng này (Có thể đang có hóa đơn).");
                        }
                    }
                });

                // LOGIC CẬP NHẬT
                JButton upd = QuanLyNhanVienPanel.createButton("Cập nhật", BLUE, new Color(0x2D5FD8), Color.WHITE, 110, 38);
                upd.addActionListener(e -> {
                    try {
                        KhachHang kh = KhachHang.builder()
                                .maKhachHang(tfMa.getText())
                                .CCCD(tfCccd.getText().trim())
                                .tenKhachHang(tfTen.getText().trim())
                                .soDienThoai(tfSdt.getText().trim())
                                .email(tfEmail.getText().trim())
                                .ngayTao(rowData[4] != null && !rowData[4].toString().equals("N/A")
                                        ? LocalDate.parse(rowData[4].toString()) : LocalDate.now())
                                .build();

                        if (service.capNhatKhachHang(kh)) {
                            JOptionPane.showMessageDialog(this, "Cập nhật thành công!");
                            dispose();
                        } else {
                            JOptionPane.showMessageDialog(this, "Cập nhật thất bại!");
                        }
                    } catch (Exception ex) {
                        JOptionPane.showMessageDialog(this, "Lỗi cập nhật: " + ex.getMessage());
                    }
                });

                f.add(del);
                f.add(cancel);
                f.add(upd);
            }
            return f;
        }

        private JButton makeCloseBtn() {
            JButton b = new JButton("✕") {
                @Override protected void paintComponent(Graphics g) {
                    Graphics2D g2 = (Graphics2D) g.create();
                    g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                    g2.setColor(getModel().isRollover() ? new Color(0xFFF0F0) : BG_WHITE);
                    g2.fillOval(0,0,getWidth(),getHeight()); g2.dispose(); super.paintComponent(g);
                }
            };
            b.setFont(new Font("Segoe UI", Font.PLAIN, 13)); b.setForeground(TEXT_MID);
            b.setContentAreaFilled(false); b.setBorderPainted(false); b.setFocusPainted(false);
            b.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            b.setPreferredSize(new Dimension(30, 30)); b.addActionListener(e -> dispose());
            return b;
        }

        private String s(Object[] a, int i) {
            return (a != null && i < a.length && a[i] != null) ? a[i].toString() : "";
        }
    }
}