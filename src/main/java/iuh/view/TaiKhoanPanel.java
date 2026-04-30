package iuh.view;

import iuh.dto.NhanVienDTO;
import iuh.service.impl.NhanVienServiceImpl;

import javax.swing.*;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.border.MatteBorder;
import java.awt.*;

public class TaiKhoanPanel extends JPanel {
    static final Color BG = new Color(0xF4F6FB);
    static final Color WHITE = Color.WHITE;
    static final Color BLUE = new Color(0x3B6FF0);
    static final Color BLUE_DARK = new Color(0x2A5CD4);
    static final Color DARK = new Color(0x1A1A2E);
    static final Color MID = new Color(0x4A5268);
    static final Color GRAY = new Color(0xA0A8B8);
    static final Color BORDER = new Color(0xE4E9F2);
    static final Color INPUT_BG = new Color(0xF0F2F7);
    static final Color SUCCESS_BG = new Color(0xE6F9F0);
    static final Color SUCCESS_FG = new Color(0x1A7A4A);
    static final Color AVATAR_BG = new Color(0x6B9EFF);

    static final Font F_H2 = new Font("Segoe UI", Font.BOLD, 20);
    static final Font F_EMAIL = new Font("Segoe UI", Font.PLAIN, 13);
    static final Font F_LABEL = new Font("Segoe UI", Font.BOLD, 11);
    static final Font F_INPUT = new Font("Segoe UI", Font.PLAIN, 13);
    static final Font F_SECTION = new Font("Segoe UI", Font.BOLD, 11);
    static final Font F_BTN = new Font("Segoe UI", Font.BOLD, 13);
    static final Font F_STATUS = new Font("Segoe UI", Font.BOLD, 11);

    private final NhanVienServiceImpl nhanVienServiceImpl = new NhanVienServiceImpl();

    private JLabel nameLbl;
    private JLabel emailLbl;
    private JLabel statusBadge;

    private JTextField tfHoTen;
    private JTextField tfCCCD;
    private JTextField tfDiaChi;
    private JTextField tfNgaySinh;
    private JTextField tfSoDienThoai;
    private JTextField tfEmail;
    private JTextField tfNgayBatDau;
    private JTextField tfTrangThai;
    private JTextField tfTaiKhoan;
    private JTextField tfMatKhau;
    private JTextField tfGioiTinh;

    public TaiKhoanPanel() {
        setLayout(new BorderLayout());
        setBackground(BG);
        add(buildHeader(), BorderLayout.NORTH);

        JScrollPane scroll = new JScrollPane(buildBody());
        scroll.setBorder(null);
        scroll.getVerticalScrollBar().setUnitIncrement(16);
        scroll.setBackground(BG);
        scroll.getViewport().setBackground(BG);
        add(scroll, BorderLayout.CENTER);

        loadUserData();
    }

    private JPanel buildHeader() {
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(WHITE);
        header.setBorder(new CompoundBorder(
                new MatteBorder(0, 0, 1, 0, BORDER),
                new EmptyBorder(10, 24, 10, 24)
        ));
        header.setPreferredSize(new Dimension(0, 54));

        JLabel title = new JLabel("Tai khoan");
        title.setFont(F_H2);
        title.setForeground(DARK);
        header.add(title, BorderLayout.WEST);
        header.add(makeAvatarCircle(32, "N", 14), BorderLayout.EAST);
        return header;
    }

    private JPanel buildBody() {
        JPanel wrap = new JPanel(new BorderLayout());
        wrap.setBackground(BG);
        wrap.setBorder(new EmptyBorder(24, 24, 24, 24));
        wrap.add(buildContent(), BorderLayout.NORTH);
        return wrap;
    }

    private JPanel buildContent() {
        JPanel root = new JPanel();
        root.setOpaque(false);
        root.setLayout(new BoxLayout(root, BoxLayout.Y_AXIS));

        root.add(buildProfileHero());
        root.add(Box.createVerticalStrut(16));

        tfHoTen = makeField();
        tfCCCD = makeField();
        tfDiaChi = makeField();
        tfNgaySinh = makeField();
        tfSoDienThoai = makeField();
        tfEmail = makeField();
        tfNgayBatDau = makeField();
        tfTrangThai = makeField();
        tfTaiKhoan = makeField();
        tfMatKhau = makeField();
        tfGioiTinh = makeField();

        root.add(buildSection("THONG TIN CA NHAN", new JComponent[]{
                buildFieldGroup("Ho va ten", tfHoTen),
                buildFieldGroup("CCCD", tfCCCD),
                buildFieldGroup("Ngay sinh", tfNgaySinh),
                buildFieldGroup("Gioi tinh", tfGioiTinh),
                buildFieldGroup("So dien thoai", tfSoDienThoai),
                buildFieldGroup("Email", tfEmail),
                buildFieldGroup("Dia chi", tfDiaChi)
        }));
        root.add(Box.createVerticalStrut(16));

        root.add(buildSection("THONG TIN CONG VIEC", new JComponent[]{
                buildFieldGroup("Ngay bat dau", tfNgayBatDau),
                buildFieldGroup("Trang thai", tfTrangThai)
        }));
        root.add(Box.createVerticalStrut(16));

        RoundedCard accountCard = new RoundedCard(12);
        accountCard.setLayout(new BorderLayout(0, 14));
        accountCard.setBorder(new EmptyBorder(20, 28, 24, 28));
        accountCard.setMaximumSize(new Dimension(Integer.MAX_VALUE, 220));
        accountCard.add(buildSectionTitle("TAI KHOAN & BAO MAT"), BorderLayout.NORTH);

        JPanel accountContent = new JPanel(new BorderLayout(16, 0));
        accountContent.setOpaque(false);

        JPanel accountFields = new JPanel(new GridLayout(1, 2, 16, 0));
        accountFields.setOpaque(false);
        accountFields.add(buildFieldGroup("Tai khoan", tfTaiKhoan));
        accountFields.add(buildFieldGroup("Mat khau", tfMatKhau));

        JButton btnChangePassword = makeButton("Doi mat khau", 130, 40, true);
        btnChangePassword.addActionListener(e -> moDialogDoiMatKhau());

        accountContent.add(accountFields, BorderLayout.CENTER);
        accountContent.add(btnChangePassword, BorderLayout.EAST);
        accountCard.add(accountContent, BorderLayout.CENTER);

        root.add(accountCard);
        return root;
    }

    private JPanel buildProfileHero() {
        RoundedCard card = new RoundedCard(16);
        card.setLayout(new BorderLayout());
        card.setBorder(new EmptyBorder(22, 28, 22, 28));
        card.setMaximumSize(new Dimension(Integer.MAX_VALUE, 100));

        JPanel left = new JPanel(new FlowLayout(FlowLayout.LEFT, 16, 0));
        left.setOpaque(false);
        left.add(makeAvatarCircle(68, "N", 26));

        JPanel info = new JPanel();
        info.setOpaque(false);
        info.setLayout(new BoxLayout(info, BoxLayout.Y_AXIS));

        nameLbl = new JLabel("");
        nameLbl.setFont(F_H2);
        nameLbl.setForeground(DARK);

        emailLbl = new JLabel("");
        emailLbl.setFont(F_EMAIL);
        emailLbl.setForeground(GRAY);

        statusBadge = new JLabel("Dang lam viec") {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(getBackground());
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 20, 20);
                g2.dispose();
                super.paintComponent(g);
            }
        };
        statusBadge.setOpaque(false);
        statusBadge.setFont(F_STATUS);
        statusBadge.setForeground(SUCCESS_FG);
        statusBadge.setBackground(SUCCESS_BG);
        statusBadge.setBorder(new EmptyBorder(3, 10, 3, 10));

        info.add(nameLbl);
        info.add(Box.createVerticalStrut(3));
        info.add(emailLbl);
        info.add(Box.createVerticalStrut(8));
        info.add(statusBadge);
        left.add(info);

        card.add(left, BorderLayout.CENTER);
        return card;
    }

    private RoundedCard buildSection(String title, JComponent[] fields) {
        RoundedCard card = new RoundedCard(12);
        card.setLayout(new BorderLayout(0, 14));
        card.setBorder(new EmptyBorder(20, 28, 24, 28));
        card.setMaximumSize(new Dimension(Integer.MAX_VALUE, 500));
        card.add(buildSectionTitle(title), BorderLayout.NORTH);

        JPanel body = new JPanel(new GridLayout(0, 2, 24, 14));
        body.setOpaque(false);
        for (JComponent field : fields) {
            body.add(field);
        }
        card.add(body, BorderLayout.CENTER);
        return card;
    }

    private JComponent buildSectionTitle(String title) {
        JLabel label = new JLabel(title);
        label.setFont(F_SECTION);
        label.setForeground(GRAY);
        label.setBorder(new CompoundBorder(
                new MatteBorder(0, 0, 1, 0, BORDER),
                new EmptyBorder(0, 0, 12, 0)
        ));
        return label;
    }

    private JPanel buildFieldGroup(String labelText, JComponent field) {
        JPanel group = new JPanel(new BorderLayout(0, 5));
        group.setOpaque(false);

        JLabel label = new JLabel(labelText.toUpperCase());
        label.setFont(F_LABEL);
        label.setForeground(GRAY);

        group.add(label, BorderLayout.NORTH);
        group.add(field, BorderLayout.CENTER);
        return group;
    }

    private JTextField makeField() {
        JTextField tf = new JTextField();
        tf.setFont(F_INPUT);
        tf.setForeground(MID);
        tf.setBackground(new Color(0xEAEDF4));
        tf.setEditable(false);
        tf.setBorder(new CompoundBorder(
                new LineBorder(BORDER, 1, true),
                new EmptyBorder(9, 13, 9, 13)
        ));
        tf.setPreferredSize(new Dimension(0, 40));
        return tf;
    }

    private JButton makeButton(String text, int w, int h, boolean primary) {
        JButton btn = new JButton(text) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                if (primary) {
                    g2.setColor(getModel().isRollover() ? BLUE_DARK : BLUE);
                    g2.fillRoundRect(0, 0, getWidth(), getHeight(), 10, 10);
                    g2.setColor(WHITE);
                } else {
                    g2.setColor(WHITE);
                    g2.fillRoundRect(0, 0, getWidth(), getHeight(), 10, 10);
                    g2.setColor(BORDER);
                    g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 10, 10);
                    g2.setColor(MID);
                }
                g2.setFont(F_BTN);
                FontMetrics fm = g2.getFontMetrics();
                g2.drawString(getText(),
                        (getWidth() - fm.stringWidth(getText())) / 2,
                        (getHeight() + fm.getAscent() - fm.getDescent()) / 2);
                g2.dispose();
            }
        };
        btn.setOpaque(false);
        btn.setContentAreaFilled(false);
        btn.setBorderPainted(false);
        btn.setFocusPainted(false);
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btn.setPreferredSize(new Dimension(w, h));
        return btn;
    }

    private JPanel makeAvatarCircle(int size, String letter, int fontSize) {
        JPanel av = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(AVATAR_BG);
                g2.fillOval(0, 0, size, size);
                g2.setColor(WHITE);
                g2.setFont(new Font("Segoe UI", Font.BOLD, fontSize));
                FontMetrics fm = g2.getFontMetrics();
                g2.drawString(letter,
                        size / 2 - fm.stringWidth(letter) / 2,
                        size / 2 + fm.getAscent() / 2 - 2);
                g2.dispose();
            }
        };
        av.setOpaque(false);
        av.setPreferredSize(new Dimension(size, size));
        return av;
    }

    private void loadUserData() {
        CurrentUser currentUser = CurrentUser.getInstance();
        NhanVienDTO nv = currentUser.getNhanVien();
        if (nv == null) {
            return;
        }
        NhanVienDTO fullNv = nv.getMaNhanVien() != null
                ? nhanVienServiceImpl.getNhanVienDTOById(nv.getMaNhanVien())
                : null;
        applyUserData(fullNv != null ? fullNv : nv);
    }

    private void applyUserData(NhanVienDTO nv) {
        if (nv == null) {
            return;
        }

        String maNV = nv.getMaNhanVien() != null ? nv.getMaNhanVien() : "";
        String hoTen = nv.getTenNhanVien() != null ? nv.getTenNhanVien() : "";
        String email = nv.getEmail() != null ? nv.getEmail() : "";
        String trangThai = nv.getTrangThai() != null ? nv.getTrangThai() : "";

        nameLbl.setText(hoTen.isBlank() ? "Nhan vien" : hoTen);
        emailLbl.setText(email + (maNV.isBlank() ? "" : " · " + maNV));

        if ("Đang làm việc".equalsIgnoreCase(trangThai)) {
            statusBadge.setText(trangThai);
            statusBadge.setForeground(SUCCESS_FG);
            statusBadge.setBackground(SUCCESS_BG);
        } else {
            statusBadge.setText(trangThai);
            statusBadge.setForeground(MID);
            statusBadge.setBackground(INPUT_BG);
        }

        tfHoTen.setText(hoTen);
        tfCCCD.setText(nv.getCCCD() != null ? nv.getCCCD() : "");
        tfDiaChi.setText(nv.getDiaChi() != null ? nv.getDiaChi() : "");
        tfNgaySinh.setText(nv.getNgaySinh() != null ? nv.getNgaySinh().toString() : "");
        tfSoDienThoai.setText(nv.getSoDienThoai() != null ? nv.getSoDienThoai() : "");
        tfEmail.setText(email);
        tfNgayBatDau.setText(nv.getNgayBatDau() != null ? nv.getNgayBatDau().toString() : "");
        tfTrangThai.setText(trangThai);
        tfTaiKhoan.setText(nv.getTaiKhoan() != null ? nv.getTaiKhoan() : "");
        tfMatKhau.setText(nhanVienServiceImpl.layMatKhauHienTai(maNV) == null ? "" : "********");
        tfGioiTinh.setText(nv.getGioiTinhText());
    }

    private void moDialogDoiMatKhau() {
        String maNhanVien = CurrentUser.getInstance().getMaNhanVien();
        if (maNhanVien == null || maNhanVien.isBlank()) {
            JOptionPane.showMessageDialog(this, "Khong xac dinh duoc nguoi dung hien tai.", "Loi", JOptionPane.ERROR_MESSAGE);
            return;
        }

        JPasswordField pfMatKhauCu = new JPasswordField();
        JPasswordField pfMatKhauMoi = new JPasswordField();
        JPasswordField pfXacNhan = new JPasswordField();

        JPanel panel = new JPanel(new GridLayout(0, 1, 0, 8));
        panel.add(new JLabel("Mat khau hien tai"));
        panel.add(pfMatKhauCu);
        panel.add(new JLabel("Mat khau moi"));
        panel.add(pfMatKhauMoi);
        panel.add(new JLabel("Xac nhan mat khau moi"));
        panel.add(pfXacNhan);

        int result = JOptionPane.showConfirmDialog(
                this,
                panel,
                "Doi mat khau",
                JOptionPane.OK_CANCEL_OPTION,
                JOptionPane.PLAIN_MESSAGE
        );

        if (result != JOptionPane.OK_OPTION) {
            return;
        }

        String matKhauCu = new String(pfMatKhauCu.getPassword()).trim();
        String matKhauMoi = new String(pfMatKhauMoi.getPassword()).trim();
        String xacNhan = new String(pfXacNhan.getPassword()).trim();

        if (!matKhauMoi.equals(xacNhan)) {
            JOptionPane.showMessageDialog(this, "Xac nhan mat khau moi khong khop.", "Thong bao", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            boolean thanhCong = nhanVienServiceImpl.doiMatKhau(maNhanVien, matKhauCu, matKhauMoi);
            if (!thanhCong) {
                JOptionPane.showMessageDialog(this, "Mat khau hien tai khong dung.", "Thong bao", JOptionPane.WARNING_MESSAGE);
                return;
            }
            tfMatKhau.setText("********");
            JOptionPane.showMessageDialog(this, "Doi mat khau thanh cong.", "Thong bao", JOptionPane.INFORMATION_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Loi", JOptionPane.ERROR_MESSAGE);
        }
    }

    static class RoundedCard extends JPanel {
        private final int radius;

        RoundedCard(int radius) {
            this.radius = radius;
            setOpaque(false);
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(Color.WHITE);
            g2.fillRoundRect(0, 0, getWidth(), getHeight(), radius * 2, radius * 2);
            g2.setColor(BORDER);
            g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, radius * 2, radius * 2);
            g2.dispose();
        }
    }
}
