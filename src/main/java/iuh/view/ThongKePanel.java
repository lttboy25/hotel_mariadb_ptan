package iuh.view;

import iuh.dto.ThongKeDTO;
import iuh.service.ThongKeService;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.text.NumberFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class ThongKePanel extends JPanel {
    private static final Color BG_MAIN = new Color(0xF4F6FB);
    private static final Color BG_WHITE = Color.WHITE;
    private static final Color BLUE = new Color(0x3B6FF0);
    private static final Color GREEN = new Color(0x1F9D72);
    private static final Color ORANGE = new Color(0xF59E0B);
    private static final Color RED = new Color(0xE15241);
    private static final Color PURPLE = new Color(0x7B61FF);
    private static final Color TEXT_DARK = new Color(0x1A1A2E);
    private static final Color TEXT_MID = new Color(0x5A6070);
    private static final Color TEXT_GRAY = new Color(0x8C94A6);
    private static final Color BORDER = new Color(0xE4E9F2);

    private static final Font F_TITLE = new Font("Segoe UI", Font.BOLD, 22);
    private static final Font F_SUB = new Font("Segoe UI", Font.PLAIN, 12);
    private static final Font F_CARD_TITLE = new Font("Segoe UI", Font.PLAIN, 12);
    private static final Font F_CARD_VALUE = new Font("Segoe UI", Font.BOLD, 28);
    private static final Font F_CARD_META = new Font("Segoe UI", Font.PLAIN, 11);
    private static final Font F_SECTION = new Font("Segoe UI", Font.BOLD, 14);

    private static final NumberFormat CURRENCY_FORMAT = NumberFormat.getCurrencyInstance(new Locale("vi", "VN"));
    private static final NumberFormat NUMBER_FORMAT = NumberFormat.getIntegerInstance(new Locale("vi", "VN"));
    private static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    private static final Color[] PIE_COLORS = {BLUE, GREEN, ORANGE, RED, PURPLE, new Color(0x14B8A6)};

    private final ThongKeService thongKeService = new ThongKeService();

    private final JSpinner spTuNgay;
    private final JSpinner spDenNgay;
    private final JButton btnLamMoi;

    private final JLabel lblKhach = new JLabel("0");
    private final JLabel lblKhachMeta = new JLabel("Đang nhận dữ liệu");
    private final JLabel lblLapDay = new JLabel("0%");
    private final JLabel lblLapDayMeta = new JLabel("0 / 0 phòng");
    private final JLabel lblDatPhong = new JLabel("0");
    private final JLabel lblDatPhongMeta = new JLabel("So với kỳ trước");
    private final JLabel lblDoanhThu = new JLabel("0 đ");
    private final JLabel lblDoanhThuMeta = new JLabel("Tổng doanh thu trong kỳ");

    private final JLabel lblTitleBar = new JLabel("Phiếu đặt theo 12 tháng gần nhất");
    private final JLabel lblTitleLine = new JLabel("Doanh thu theo ngày");
    private final JLabel lblTitlePie = new JLabel("Cơ cấu trạng thái đặt phòng");
    private final JLabel lblTitleLoaiPhong = new JLabel("Cơ cấu loại phòng");

    private final BarChartPanel barChartPanel = new BarChartPanel();
    private final LineChartPanel lineChartPanel = new LineChartPanel();
    private final PieChartPanel pieStatusPanel = new PieChartPanel();
    private final PieChartPanel pieLoaiPhongPanel = new PieChartPanel();

    public ThongKePanel() {
        setLayout(new BorderLayout());
        setBackground(BG_MAIN);

        spTuNgay = createDateSpinner(LocalDate.now().minusDays(29));
        spDenNgay = createDateSpinner(LocalDate.now());
        btnLamMoi = createActionButton("Làm mới");
        btnLamMoi.addActionListener(e -> taiDuLieu());

        JScrollPane scrollPane = new JScrollPane(buildContent());
        scrollPane.setBorder(null);
        scrollPane.getVerticalScrollBar().setUnitIncrement(14);
        scrollPane.getViewport().setBackground(BG_MAIN);
        add(scrollPane, BorderLayout.CENTER);

        taiDuLieu();
    }

    private JPanel buildContent() {
        JPanel content = new JPanel();
        content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));
        content.setBackground(BG_MAIN);
        content.setBorder(new EmptyBorder(20, 24, 24, 24));

        content.add(buildHeader());
        content.add(Box.createVerticalStrut(16));
        content.add(buildSummaryGrid());
        content.add(Box.createVerticalStrut(16));
        content.add(buildChartsRow());
        content.add(Box.createVerticalStrut(16));
        content.add(buildBottomRow());

        return content;
    }

    private JPanel buildHeader() {
        JPanel panel = cardPanel(new BorderLayout(16, 0));
        panel.setBorder(new EmptyBorder(16, 18, 16, 18));

        JPanel left = new JPanel();
        left.setOpaque(false);
        left.setLayout(new BoxLayout(left, BoxLayout.Y_AXIS));
        JLabel title = new JLabel("Thống kê");
        title.setFont(F_TITLE);
        title.setForeground(TEXT_DARK);
        JLabel sub = new JLabel("Theo dõi doanh thu, công suất phòng và tình trạng đặt phòng từ dữ liệu hệ thống.");
        sub.setFont(F_SUB);
        sub.setForeground(TEXT_MID);
        left.add(title);
        left.add(Box.createVerticalStrut(4));
        left.add(sub);

        JPanel right = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 0));
        right.setOpaque(false);
        right.add(filterLabel("Từ ngày"));
        right.add(spTuNgay);
        right.add(filterLabel("Đến ngày"));
        right.add(spDenNgay);
        right.add(btnLamMoi);

        panel.add(left, BorderLayout.CENTER);
        panel.add(right, BorderLayout.EAST);
        return panel;
    }

    private JPanel buildSummaryGrid() {
        JPanel grid = new JPanel(new GridLayout(1, 4, 14, 0));
        grid.setOpaque(false);
        grid.add(buildStatCard("Khách đang lưu trú", lblKhach, lblKhachMeta, BLUE));
        grid.add(buildStatCard("Tỷ lệ lấp đầy", lblLapDay, lblLapDayMeta, GREEN));
        grid.add(buildStatCard("Lượt đặt phòng", lblDatPhong, lblDatPhongMeta, ORANGE));
        grid.add(buildStatCard("Doanh thu", lblDoanhThu, lblDoanhThuMeta, PURPLE));
        return grid;
    }

    private JPanel buildChartsRow() {
        JPanel row = new JPanel(new GridLayout(1, 2, 16, 0));
        row.setOpaque(false);
        row.add(buildChartCard(lblTitleBar, barChartPanel));
        row.add(buildChartCard(lblTitleLine, lineChartPanel));
        return row;
    }

    private JPanel buildBottomRow() {
        JPanel row = new JPanel(new GridLayout(1, 2, 16, 0));
        row.setOpaque(false);
        row.add(buildChartCard(lblTitlePie, pieStatusPanel));
        row.add(buildChartCard(lblTitleLoaiPhong, pieLoaiPhongPanel));
        return row;
    }

    private JPanel buildChartCard(JLabel titleLabel, JComponent chart) {
        JPanel card = cardPanel(new BorderLayout(0, 10));
        card.setBorder(new EmptyBorder(16, 18, 18, 18));

        titleLabel.setFont(F_SECTION);
        titleLabel.setForeground(TEXT_DARK);
        card.add(titleLabel, BorderLayout.NORTH);
        card.add(chart, BorderLayout.CENTER);
        return card;
    }

    private JPanel buildStatCard(String title, JLabel valueLabel, JLabel metaLabel, Color accent) {
        JPanel card = cardPanel(new BorderLayout(0, 10));
        card.setBorder(new EmptyBorder(16, 18, 16, 18));

        JPanel dot = new JPanel();
        dot.setPreferredSize(new Dimension(12, 12));
        dot.setBackground(accent);
        dot.setBorder(BorderFactory.createLineBorder(accent));

        JPanel top = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        top.setOpaque(false);
        top.add(dot);
        top.add(Box.createHorizontalStrut(8));
        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(F_CARD_TITLE);
        titleLabel.setForeground(TEXT_MID);
        top.add(titleLabel);

        valueLabel.setFont(F_CARD_VALUE);
        valueLabel.setForeground(TEXT_DARK);
        metaLabel.setFont(F_CARD_META);
        metaLabel.setForeground(TEXT_GRAY);

        JPanel content = new JPanel();
        content.setOpaque(false);
        content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));
        content.add(top);
        content.add(Box.createVerticalStrut(8));
        content.add(valueLabel);
        content.add(Box.createVerticalStrut(6));
        content.add(metaLabel);

        card.add(content, BorderLayout.CENTER);
        return card;
    }

    private void taiDuLieu() {
        try {
            LocalDate tuNgay = spinnerToLocalDate(spTuNgay);
            LocalDate denNgay = spinnerToLocalDate(spDenNgay);
            ThongKeDTO dto = thongKeService.layThongKe(tuNgay, denNgay);
            capNhatGiaoDien(dto);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(
                    this,
                    ex.getMessage(),
                    "Không thể tải thống kê",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }

    private void capNhatGiaoDien(ThongKeDTO dto) {
        lblKhach.setText(NUMBER_FORMAT.format(dto.getSoKhachDangLuuTru()));
        lblKhachMeta.setText(NUMBER_FORMAT.format(dto.getSoPhongDangSuDung()) + " phòng đang sử dụng");

        lblLapDay.setText(formatPercent(dto.getTyLeLapDay()));
        lblLapDayMeta.setText(NUMBER_FORMAT.format(dto.getSoPhongDangSuDung()) + " / " + NUMBER_FORMAT.format(dto.getTongSoPhong()) + " phòng");

        lblDatPhong.setText(NUMBER_FORMAT.format(dto.getSoLuotDatPhong()));
        lblDatPhongMeta.setText(
                formatSignedPercent(dto.getTyLeTangTruongDatPhong()) +
                        " so với kỳ trước (" + NUMBER_FORMAT.format(dto.getSoLuotDatPhongKyTruoc()) + ")"
        );

        lblDoanhThu.setText(CURRENCY_FORMAT.format(dto.getTongDoanhThu()));
        lblDoanhThuMeta.setText(
                "Hủy phòng: " + NUMBER_FORMAT.format(dto.getSoLuotHuyPhong()) +
                        " (" + formatPercent(dto.getTyLeHuyPhong()) + ")"
        );

        lblTitleLine.setText("Doanh thu theo ngày từ " + dto.getTuNgay().format(DATE_FORMAT) + " đến " + dto.getDenNgay().format(DATE_FORMAT));

        barChartPanel.setData(dto.getDsDatPhongTheoThang());
        lineChartPanel.setData(dto.getDsDoanhThuTheoNgay());
        pieStatusPanel.setData(dto.getDsTrangThaiDatPhong());
        pieLoaiPhongPanel.setData(dto.getDsLoaiPhong());
    }

    private static JSpinner createDateSpinner(LocalDate localDate) {
        SpinnerDateModel model = new SpinnerDateModel(Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant()), null, null, java.util.Calendar.DAY_OF_MONTH);
        JSpinner spinner = new JSpinner(model);
        spinner.setEditor(new JSpinner.DateEditor(spinner, "dd/MM/yyyy"));
        spinner.setPreferredSize(new Dimension(110, 32));
        return spinner;
    }

    private static JButton createActionButton(String text) {
        JButton button = new JButton(text);
        button.setFocusPainted(false);
        button.setBackground(BLUE);
        button.setForeground(Color.WHITE);
        button.setBorder(BorderFactory.createEmptyBorder(8, 14, 8, 14));
        return button;
    }

    private static JLabel filterLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(F_SUB);
        label.setForeground(TEXT_MID);
        return label;
    }

    private static JPanel cardPanel(LayoutManager layout) {
        JPanel panel = new JPanel(layout);
        panel.setOpaque(true);
        panel.setBackground(BG_WHITE);
        panel.setBorder(BorderFactory.createLineBorder(BORDER));
        return panel;
    }

    private static LocalDate spinnerToLocalDate(JSpinner spinner) {
        Date date = (Date) spinner.getValue();
        Instant instant = date.toInstant();
        return instant.atZone(ZoneId.systemDefault()).toLocalDate();
    }

    private static String formatPercent(double value) {
        return String.format(Locale.US, "%.1f%%", value);
    }

    private static String formatSignedPercent(double value) {
        return String.format(Locale.US, "%+.1f%%", value);
    }

    private static class BarChartPanel extends JPanel {
        private List<ThongKeDTO.DuLieuCotDTO> data = List.of();

        BarChartPanel() {
            setOpaque(false);
            setPreferredSize(new Dimension(0, 280));
        }

        void setData(List<ThongKeDTO.DuLieuCotDTO> data) {
            this.data = data == null ? List.of() : data;
            repaint();
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            if (data.isEmpty()) {
                drawEmpty(g2, getWidth(), getHeight());
                g2.dispose();
                return;
            }

            int width = getWidth();
            int height = getHeight();
            int padLeft = 42;
            int padRight = 12;
            int padTop = 16;
            int padBottom = 34;
            int chartWidth = width - padLeft - padRight;
            int chartHeight = height - padTop - padBottom;

            double max = data.stream().mapToDouble(ThongKeDTO.DuLieuCotDTO::getGiaTri).max().orElse(1d);
            if (max <= 0d) {
                max = 1d;
            }

            g2.setFont(new Font("Segoe UI", Font.PLAIN, 10));
            for (int i = 0; i <= 4; i++) {
                int y = padTop + (chartHeight * i / 4);
                g2.setColor(new Color(0xEDF1F7));
                g2.drawLine(padLeft, y, width - padRight, y);
                g2.setColor(TEXT_GRAY);
                double tick = max - (max * i / 4.0);
                g2.drawString(NUMBER_FORMAT.format(Math.round(tick)), 4, y + 4);
            }

            int n = data.size();
            int gap = Math.max(6, chartWidth / Math.max(24, n * 2));
            int barWidth = Math.max(10, (chartWidth - gap * (n + 1)) / Math.max(1, n));
            int x = padLeft + gap;
            for (ThongKeDTO.DuLieuCotDTO item : data) {
                int barHeight = (int) Math.round(chartHeight * (item.getGiaTri() / max));
                int y = padTop + chartHeight - barHeight;
                g2.setColor(BLUE);
                g2.fillRoundRect(x, y, barWidth, barHeight, 8, 8);
                g2.setColor(TEXT_GRAY);
                drawCentered(g2, item.getNhan(), x + barWidth / 2, height - 12);
                x += barWidth + gap;
            }

            g2.dispose();
        }
    }

    private static class LineChartPanel extends JPanel {
        private List<ThongKeDTO.DuLieuNgayDTO> data = List.of();

        LineChartPanel() {
            setOpaque(false);
            setPreferredSize(new Dimension(0, 280));
        }

        void setData(List<ThongKeDTO.DuLieuNgayDTO> data) {
            this.data = data == null ? List.of() : data;
            repaint();
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            if (data.isEmpty()) {
                drawEmpty(g2, getWidth(), getHeight());
                g2.dispose();
                return;
            }

            int width = getWidth();
            int height = getHeight();
            int padLeft = 42;
            int padRight = 12;
            int padTop = 16;
            int padBottom = 34;
            int chartWidth = width - padLeft - padRight;
            int chartHeight = height - padTop - padBottom;

            double max = data.stream().mapToDouble(ThongKeDTO.DuLieuNgayDTO::getGiaTri).max().orElse(1d);
            if (max <= 0d) {
                max = 1d;
            }

            g2.setFont(new Font("Segoe UI", Font.PLAIN, 10));
            for (int i = 0; i <= 4; i++) {
                int y = padTop + (chartHeight * i / 4);
                g2.setColor(new Color(0xEDF1F7));
                g2.drawLine(padLeft, y, width - padRight, y);
            }

            List<Point> points = new ArrayList<>();
            int n = data.size();
            for (int i = 0; i < n; i++) {
                int x = padLeft + (int) Math.round(chartWidth * (i / Math.max(1d, n - 1d)));
                int y = padTop + chartHeight - (int) Math.round(chartHeight * (data.get(i).getGiaTri() / max));
                points.add(new Point(x, y));
            }

            g2.setColor(new Color(59, 111, 240, 60));
            Polygon polygon = new Polygon();
            polygon.addPoint(points.get(0).x, padTop + chartHeight);
            for (Point point : points) {
                polygon.addPoint(point.x, point.y);
            }
            polygon.addPoint(points.get(points.size() - 1).x, padTop + chartHeight);
            g2.fillPolygon(polygon);

            g2.setColor(BLUE);
            g2.setStroke(new BasicStroke(2.2f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
            for (int i = 0; i < points.size() - 1; i++) {
                Point p1 = points.get(i);
                Point p2 = points.get(i + 1);
                g2.drawLine(p1.x, p1.y, p2.x, p2.y);
            }

            for (Point point : points) {
                g2.setColor(Color.WHITE);
                g2.fillOval(point.x - 4, point.y - 4, 8, 8);
                g2.setColor(BLUE);
                g2.drawOval(point.x - 4, point.y - 4, 8, 8);
            }

            int labelStep = Math.max(1, n / 6);
            g2.setColor(TEXT_GRAY);
            for (int i = 0; i < n; i += labelStep) {
                Point point = points.get(i);
                String label = data.get(i).getNgay().format(DateTimeFormatter.ofPattern("dd/MM"));
                drawCentered(g2, label, point.x, height - 12);
            }
            if ((n - 1) % labelStep != 0) {
                Point lastPoint = points.get(n - 1);
                String label = data.get(n - 1).getNgay().format(DateTimeFormatter.ofPattern("dd/MM"));
                drawCentered(g2, label, lastPoint.x, height - 12);
            }

            g2.dispose();
        }
    }

    private static class PieChartPanel extends JPanel {
        private List<ThongKeDTO.DuLieuTyTrongDTO> data = List.of();

        PieChartPanel() {
            setOpaque(false);
            setPreferredSize(new Dimension(0, 280));
        }

        void setData(List<ThongKeDTO.DuLieuTyTrongDTO> data) {
            this.data = data == null ? List.of() : data;
            repaint();
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            if (data.isEmpty()) {
                drawEmpty(g2, getWidth(), getHeight());
                g2.dispose();
                return;
            }

            double total = data.stream().mapToDouble(ThongKeDTO.DuLieuTyTrongDTO::getGiaTri).sum();
            if (total <= 0d) {
                drawEmpty(g2, getWidth(), getHeight());
                g2.dispose();
                return;
            }

            int width = getWidth();
            int height = getHeight();
            int size = Math.min(width / 2, height - 40);
            int x = 10;
            int y = Math.max(20, (height - size) / 2);

            float start = 90f;
            for (int i = 0; i < data.size(); i++) {
                ThongKeDTO.DuLieuTyTrongDTO item = data.get(i);
                float arc = (float) (360d * item.getGiaTri() / total);
                g2.setColor(PIE_COLORS[i % PIE_COLORS.length]);
                g2.fillArc(x, y, size, size, Math.round(start), Math.round(-arc));
                start -= arc;
            }

            g2.setColor(BG_WHITE);
            int hole = (int) (size * 0.56);
            g2.fillOval(x + (size - hole) / 2, y + (size - hole) / 2, hole, hole);

            g2.setColor(TEXT_DARK);
            g2.setFont(new Font("Segoe UI", Font.BOLD, 14));
            drawCentered(g2, NUMBER_FORMAT.format(Math.round(total)), x + size / 2, y + size / 2 - 2);
            g2.setFont(new Font("Segoe UI", Font.PLAIN, 11));
            drawCentered(g2, "Tổng lượt", x + size / 2, y + size / 2 + 16);

            int legendX = x + size + 18;
            int legendY = y + 8;
            g2.setFont(new Font("Segoe UI", Font.PLAIN, 11));
            for (int i = 0; i < data.size(); i++) {
                ThongKeDTO.DuLieuTyTrongDTO item = data.get(i);
                g2.setColor(PIE_COLORS[i % PIE_COLORS.length]);
                g2.fillOval(legendX, legendY + i * 24, 10, 10);
                g2.setColor(TEXT_DARK);
                g2.drawString(item.getNhan(), legendX + 18, legendY + 9 + i * 24);
                g2.setColor(TEXT_GRAY);
                String pct = formatPercent(item.getGiaTri() * 100d / total);
                g2.drawString(NUMBER_FORMAT.format(Math.round(item.getGiaTri())) + " - " + pct, legendX + 18, legendY + 21 + i * 24);
            }

            g2.dispose();
        }
    }

    private static void drawEmpty(Graphics2D g2, int width, int height) {
        g2.setColor(TEXT_GRAY);
        g2.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        drawCentered(g2, "Không có dữ liệu", width / 2, height / 2);
    }

    private static void drawCentered(Graphics2D g2, String text, int centerX, int baselineY) {
        FontMetrics fm = g2.getFontMetrics();
        g2.drawString(text, centerX - fm.stringWidth(text) / 2, baselineY);
    }
}
