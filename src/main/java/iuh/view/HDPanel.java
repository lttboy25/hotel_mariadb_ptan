package iuh.view;

import iuh.dto.HoaDonDTO;
import iuh.entity.ChiTietHoaDon;
import iuh.entity.HoaDon;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class HDPanel extends JPanel {

    private NumberFormat formatter = NumberFormat.getInstance(new Locale("vi", "VN"));

    public HDPanel(HoaDonDTO hoaDon) {
        setLayout(new BorderLayout(0, 12));
        setBorder(BorderFactory.createEmptyBorder(20, 24, 12, 24));
        setBackground(Color.WHITE);
        setPreferredSize(new Dimension(400, 400));

        // Icon + tiêu đề
        JLabel icon = new JLabel(UIManager.getIcon("OptionPane.informationIcon"));
        icon.setHorizontalAlignment(JLabel.CENTER);
        add(icon, BorderLayout.NORTH);

        // Nội dung
        JPanel content = new JPanel();
        content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));
        content.setBackground(Color.WHITE);

        List<String> phongTra = new ArrayList<>();
        for (ChiTietHoaDon ct : hoaDon.getChiTietHoaDon()) {
            phongTra.add(ct.getPhong().toString());
        }

        addRow(content, "Thanh toán thành công!");
        addRow(content, "Mã hóa đơn: " + hoaDon.getMaHoaDon());
        addRow(content, "Ngày tạo: " + hoaDon.getNgayTao());
        addRow(content, "Khách hàng: " + hoaDon.getKhachHang().getTenKhachHang());
        addRow(content, "Mã nhân viên: " + hoaDon.getNhanVien().getMaNhanVien());
        addRow(content, "Số phòng đã trả: " + String.join(", ", phongTra));
        addRow(content, "Tổng tiền: " + formatter.format(hoaDon.getTongTien()) + " đ");
        addRow(content, "Tiền khách đưa: " + formatter.format(hoaDon.getTienKhachDua()) + " đ");
        addRow(content, "Tiền thối: " + formatter.format(hoaDon.getTienThoi()) + " đ");

        add(content, BorderLayout.CENTER);
    }

    private void addRow(JPanel panel, String text) {
        JLabel lbl = new JLabel(text);
        lbl.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        lbl.setForeground(new Color(0x1A1A2E));
        lbl.setAlignmentX(Component.LEFT_ALIGNMENT);
        lbl.setBorder(BorderFactory.createEmptyBorder(2, 0, 2, 0));
        panel.add(lbl);
    }

    public void show(Component parent) {
        JFrame parentFrame = (JFrame) SwingUtilities.getWindowAncestor(parent);

        JDialog dialog = new JDialog(parentFrame, "Thành công", true);
        dialog.setLayout(new BorderLayout());
        dialog.add(this, BorderLayout.CENTER);

        // Nút OK
        JButton btnOK = new JButton("OK");
        btnOK.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btnOK.addActionListener(e -> dialog.dispose());

        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        bottomPanel.setBackground(Color.WHITE);
        bottomPanel.add(btnOK);
        dialog.add(bottomPanel, BorderLayout.SOUTH);

        dialog.pack();
        dialog.setLocationRelativeTo(parentFrame);
        dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        dialog.setVisible(true);
    }
}