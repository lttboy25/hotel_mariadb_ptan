package iuh.view;

import iuh.dto.CaDTO;
import iuh.dto.CaLamViecNhanVienDTO;
import iuh.network.ClientConnection;
import iuh.network.CommandType;
import iuh.network.Request;
import iuh.network.Response;

import javax.swing.*;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.border.MatteBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class CaLamViecPanel extends JPanel {

    // Constants from DatPhongPanel for consistency
    static final Color BG = new Color(0xF4F6FB);
    static final Color WHITE = Color.WHITE;
    static final Color BLUE = new Color(0x3B6FF0);
    static final Color GREEN = new Color(0x22C55E);
    static final Color DARK = new Color(0x1A1A2E);
    static final Color MID = new Color(0x4A5268);
    static final Color GRAY = new Color(0xA0A8B8);
    static final Color BORDER = new Color(0xE4E9F2);

    static final Font F_TITLE = new Font("Segoe UI", Font.BOLD, 20);
    static final Font F_BOLD14 = new Font("Segoe UI", Font.BOLD, 14);
    static final Font F_PLAIN13 = new Font("Segoe UI", Font.PLAIN, 13);
    static final Font F_BOLD12 = new Font("Segoe UI", Font.BOLD, 12);

    static final DateTimeFormatter FMT_DATE = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    private final ClientConnection clientConnection = ClientConnection.getInstance();

    private CaLamViecNhanVienDTO activeShift;

    // UI components
    private JLabel lblStatus;
    private JTextField tfTienMoCa;
    private JComboBox<String> cbCa;
    private JButton btnAction;
    private JLabel lblTienMoCaVal, lblTongThuVal, lblTongChiVal, lblCurrentBalanceVal;
    private JTextField tfTienKetCa;
    private JTable historyTable;
    private DefaultTableModel tableModel;

    public CaLamViecPanel() {
        setLayout(new BorderLayout());
        setBackground(BG);
        setBorder(new EmptyBorder(24, 30, 24, 30));

        // Header
        add(buildHeader(), BorderLayout.NORTH);

        // Content
        JPanel content = new JPanel(new GridBagLayout());
        content.setOpaque(false);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weighty = 1.0;

        // Left side: Active Shift
        gbc.gridx = 0;
        gbc.weightx = 0.4;
        gbc.insets = new Insets(0, 0, 0, 20);
        content.add(buildActiveShiftCard(), gbc);

        // Right side: History
        gbc.gridx = 1;
        gbc.weightx = 0.6;
        gbc.insets = new Insets(0, 0, 0, 0);
        content.add(buildHistoryCard(), gbc);

        add(content, BorderLayout.CENTER);

        refreshData();
    }

    private JPanel buildHeader() {
        JPanel header = new JPanel(new BorderLayout());
        header.setOpaque(false);
        header.setBorder(new EmptyBorder(0, 0, 20, 0));

        JLabel title = new JLabel("Quản lý Ca làm việc");
        title.setFont(F_TITLE);
        title.setForeground(DARK);

        JLabel date = new JLabel("Hôm nay: " + LocalDate.now().format(FMT_DATE));
        date.setFont(F_PLAIN13);
        date.setForeground(MID);

        header.add(title, BorderLayout.WEST);
        header.add(date, BorderLayout.EAST);

        return header;
    }

    private JPanel buildActiveShiftCard() {
        JPanel card = DatPhongPanel.card();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBorder(new EmptyBorder(24, 24, 24, 24));

        JLabel title = new JLabel("Ca làm việc hiện tại");
        title.setFont(F_BOLD14);
        title.setForeground(DARK);
        title.setAlignmentX(LEFT_ALIGNMENT);
        card.add(title);
        card.add(Box.createVerticalStrut(16));

        // Status Badge
        lblStatus = new JLabel("ĐANG ĐÓNG") {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(getText().equals("ĐANG LÀM") ? new Color(0xDCFCE7) : new Color(0xFFEEEE));
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 10, 10);
                g2.dispose();
                super.paintComponent(g);
            }
        };
        lblStatus.setFont(F_BOLD12);
        lblStatus.setForeground(GRAY);
        lblStatus.setBorder(new EmptyBorder(4, 12, 4, 12));
        lblStatus.setAlignmentX(LEFT_ALIGNMENT);
        card.add(lblStatus);
        card.add(Box.createVerticalStrut(20));

        // Form fields
        tfTienMoCa = createStyledTextField("Nhập tiền mở ca...");
        cbCa = new JComboBox<>();
        cbCa.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        loadCaList();

        // Info labels (visible when shift is active)
        lblTienMoCaVal = createInfoLabel("Tiền mở ca:", "0 VNĐ");
        lblTongThuVal = createInfoLabel("Tổng thu:", "0 VNĐ");
        lblTongChiVal = createInfoLabel("Tổng chi:", "0 VNĐ");
        lblCurrentBalanceVal = createInfoLabel("Số dư hiện tại:", "0 VNĐ");
        tfTienKetCa = createStyledTextField("Nhập tiền kết ca...");

        // Container for dynamic parts
        JPanel dynamicPanel = new JPanel();
        dynamicPanel.setLayout(new BoxLayout(dynamicPanel, BoxLayout.Y_AXIS));
        dynamicPanel.setOpaque(false);
        dynamicPanel.setAlignmentX(LEFT_ALIGNMENT);
        card.add(dynamicPanel);
        card.add(Box.createVerticalGlue());

        btnAction = DatPhongPanel.blueBtn("Mở ca làm việc", 200, 45);
        btnAction.setAlignmentX(LEFT_ALIGNMENT);
        btnAction.addActionListener(e -> handleAction());
        card.add(btnAction);

        return card;
    }

    private JPanel buildHistoryCard() {
        JPanel card = DatPhongPanel.card();
        card.setLayout(new BorderLayout());
        card.setBorder(new EmptyBorder(24, 24, 24, 24));

        JLabel title = new JLabel("Lịch sử ca làm việc");
        title.setFont(F_BOLD14);
        title.setForeground(DARK);
        card.add(title, BorderLayout.NORTH);

        String[] cols = { "Mã Ca", "Nhân viên", "Ngày", "Tiền mở", "Tiền kết", "Tổng thu", "Tổng chi", "Trạng thái" };
        tableModel = new DefaultTableModel(cols, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        historyTable = new JTable(tableModel);
        historyTable.setFont(F_PLAIN13);
        historyTable.setRowHeight(35);
        historyTable.setShowGrid(false);
        historyTable.setIntercellSpacing(new Dimension(0, 0));
        historyTable.getTableHeader().setBackground(new Color(0xF8F9FE));
        historyTable.getTableHeader().setFont(F_BOLD12);
        historyTable.getTableHeader().setForeground(GRAY);
        historyTable.getTableHeader().setBorder(new MatteBorder(0, 0, 1, 0, BORDER));

        DefaultTableCellRenderer renderer = new DefaultTableCellRenderer();
        renderer.setHorizontalAlignment(JLabel.CENTER);
        for (int i = 0; i < historyTable.getColumnCount(); i++) {
            historyTable.getColumnModel().getColumn(i).setCellRenderer(renderer);
        }

        JScrollPane scroll = new JScrollPane(historyTable);
        scroll.setBorder(new LineBorder(BORDER));
        scroll.getViewport().setBackground(WHITE);
        card.add(scroll, BorderLayout.CENTER);

        return card;
    }

    private JTextField createStyledTextField(String placeholder) {
        JTextField tf = new JTextField();
        tf.setFont(F_PLAIN13);
        tf.setBorder(new CompoundBorder(new LineBorder(BORDER, 1, true), new EmptyBorder(8, 12, 8, 12)));
        tf.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        return tf;
    }

    private JLabel createInfoLabel(String label, String value) {
        JLabel l = new JLabel(label + " " + value);
        l.setFont(F_PLAIN13);
        l.setForeground(MID);
        l.setBorder(new EmptyBorder(5, 0, 5, 0));
        l.setAlignmentX(LEFT_ALIGNMENT);
        return l;
    }

    private void refreshData() {
        String maNV = CurrentUser.getInstance().getMaNhanVien();
        Request request = Request.builder()
                .commandType(CommandType.GET_ACTIVE_SHIFT)
                .object(maNV)
                .build();
        Response response = clientConnection.sendRequest(request);
        activeShift = (CaLamViecNhanVienDTO) response.getObject();

        updateUIState();
        loadHistory();
    }

    private void updateUIState() {
        JPanel card = (JPanel) getComponent(1);
        JPanel leftCard = (JPanel) ((JPanel) card).getComponent(0);
        JPanel dynamicPanel = (JPanel) leftCard.getComponent(4);
        dynamicPanel.removeAll();

        if (activeShift == null) {
            lblStatus.setText("ĐANG ĐÓNG");
            lblStatus.setForeground(new Color(0xE04040));

            dynamicPanel.add(new JLabel("Tiền mở ca:"));
            dynamicPanel.add(Box.createVerticalStrut(5));
            dynamicPanel.add(tfTienMoCa);
            dynamicPanel.add(Box.createVerticalStrut(15));
            dynamicPanel.add(new JLabel("Chọn Ca:"));
            dynamicPanel.add(Box.createVerticalStrut(5));
            dynamicPanel.add(cbCa);

            btnAction.setText("Mở ca làm việc");
        } else {
            lblStatus.setText("ĐANG LÀM");
            lblStatus.setForeground(GREEN);

            lblTienMoCaVal.setText("Tiền mở ca: " + String.format("%,.0f VNĐ", activeShift.getTienMoCa()));
            lblTongThuVal.setText("Tổng thu: " + String.format("%,.0f VNĐ", activeShift.getTongThu()));
            lblTongChiVal.setText("Tổng chi: " + String.format("%,.0f VNĐ", activeShift.getTongChi()));
            double balance = activeShift.getTienMoCa() + activeShift.getTongThu() - activeShift.getTongChi();
            lblCurrentBalanceVal.setText("Số dư dự kiến: " + String.format("%,.0f VNĐ", balance));

            dynamicPanel.add(lblTienMoCaVal);
            dynamicPanel.add(lblTongThuVal);
            dynamicPanel.add(lblTongChiVal);
            dynamicPanel.add(lblCurrentBalanceVal);
            dynamicPanel.add(Box.createVerticalStrut(20));
            dynamicPanel.add(new JLabel("Tiền kết ca thực tế:"));
            dynamicPanel.add(Box.createVerticalStrut(5));
            dynamicPanel.add(tfTienKetCa);

            btnAction.setText("Kết thúc ca làm");
        }

        dynamicPanel.revalidate();
        dynamicPanel.repaint();
    }

    private void loadHistory() {
        tableModel.setRowCount(0);
        String maNV = CurrentUser.getInstance().getMaNhanVien();
        Request request = Request.builder()
                .commandType(CommandType.GET_SHIFT_HISTORY)
                .object(maNV)
                .build();
        Response response = clientConnection.sendRequest(request);
        List<CaLamViecNhanVienDTO> history = (List<CaLamViecNhanVienDTO>) response.getObject();

        if (history != null) {
            for (CaLamViecNhanVienDTO dto : history) {
                // Chỉ hiển thị ca đã kết thúc
                if ("DA_KET_THUC".equals(dto.getTrangThai())) {
                    tableModel.addRow(new Object[] {
                            dto.getMaCaLamViec(),
                            dto.getTenNhanVien(),
                            dto.getNgay().format(FMT_DATE),
                            String.format("%,.0f", (double) dto.getTienMoCa()),
                            String.format("%,.0f", dto.getTienKetCa()),
                            String.format("%,.0f", dto.getTongThu()),
                            String.format("%,.0f", dto.getTongChi()),
                            dto.getTrangThai()
                    });
                }
            }
        }
    }

    private void loadCaList() {
        try {
            Request request = Request.builder()
                    .commandType(CommandType.GET_ALL_CA)
                    .build();
            Response response = clientConnection.sendRequest(request);
            List<CaDTO> cas = (List<CaDTO>) response.getObject();
            cbCa.removeAllItems();
            if (cas == null || cas.isEmpty()) {
                cbCa.addItem("Ca 1");
                cbCa.addItem("Ca 2");
                cbCa.addItem("Ca 3");
            } else {
                for (CaDTO c : cas) {
                    cbCa.addItem(c.getMaCa());
                }
            }
        } catch (Exception e) {
            cbCa.addItem("Ca 1");
            cbCa.addItem("Ca 2");
            cbCa.addItem("Ca 3");
        }
    }

    private void handleAction() {
        try {
            if (activeShift == null) {
                // Open shift
                String tienStr = tfTienMoCa.getText().trim();
                if (tienStr.isEmpty()) {
                    JOptionPane.showMessageDialog(this, "Vui lòng nhập tiền mở ca.");
                    return;
                }
                double tienMoCa = Double.parseDouble(tienStr);
                String maCa = cbCa.getSelectedItem().toString();
                if (maCa.contains("("))
                    maCa = maCa.split(" ")[0] + maCa.split(" ")[1]; // Simple parse "Ca 1"

                String maNV = CurrentUser.getInstance().getMaNhanVien();
                Request request = Request.builder()
                        .commandType(CommandType.MO_CA)
                        .object(new Object[]{maNV, maCa, tienMoCa})
                        .build();
                clientConnection.sendRequest(request);
                JOptionPane.showMessageDialog(this, "Mở ca thành công!");
            } else {
                // Close shift
                String tienStr = tfTienKetCa.getText().trim();
                if (tienStr.isEmpty()) {
                    JOptionPane.showMessageDialog(this, "Vui lòng nhập tiền kết ca thực tế.");
                    return;
                }
                double tienKetCa = Double.parseDouble(tienStr);
                Request request = Request.builder()
                        .commandType(CommandType.KET_CA)
                        .object(new Object[]{activeShift.getMaCaLamViec(), tienKetCa})
                        .build();
                clientConnection.sendRequest(request);
                JOptionPane.showMessageDialog(this, "Kết ca thành công!");
            }
            refreshData();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Lỗi: " + ex.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }
}
