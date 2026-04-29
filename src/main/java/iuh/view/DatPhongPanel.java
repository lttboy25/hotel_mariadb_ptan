package iuh.view;

import iuh.dto.DatPhongRequestDTO;
import iuh.dto.DatPhongResultDTO;
import iuh.dto.KhachHangDTO;
import iuh.entity.Phong;
import iuh.service.DatPhongService;
import iuh.service.KhachHangService;

import javax.swing.*;
import javax.swing.border.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.List;

public class DatPhongPanel extends JPanel implements ChangeListener {


    static final Color BG      = new Color(0xF4F6FB);
    static final Color WHITE   = Color.WHITE;
    static final Color BLUE    = new Color(0x3B6FF0);
    static final Color BLUE_L  = new Color(0xDCEAFF);
    static final Color GREEN   = new Color(0x22C55E);
    static final Color GREEN_L = new Color(0xDCFCE7);
    static final Color DARK    = new Color(0x1A1A2E);
    static final Color MID     = new Color(0x4A5268);
    static final Color GRAY    = new Color(0xA0A8B8);
    static final Color BORDER  = new Color(0xE4E9F2);
    static final Color SEL_ROW = new Color(0xF0F5FF);

    static final Font F_TITLE   = new Font("Segoe UI", Font.BOLD, 15);
    static final Font F_BOLD13  = new Font("Segoe UI", Font.BOLD, 13);
    static final Font F_BOLD12  = new Font("Segoe UI", Font.BOLD, 12);
    static final Font F_PLAIN13 = new Font("Segoe UI", Font.PLAIN, 13);
    static final Font F_PLAIN12 = new Font("Segoe UI", Font.PLAIN, 12);
    static final Font F_SMALL   = new Font("Segoe UI", Font.PLAIN, 11);
    static final Font F_LABEL   = new Font("Segoe UI", Font.PLAIN, 11);

    // Formatter ngày giờ
    static final DateTimeFormatter FMT_DATETIME = DateTimeFormatter.ofPattern("HH:mm dd/MM/yyyy");
    static final DateTimeFormatter FMT_DATE     = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    @Override
    public void stateChanged(ChangeEvent e) {

    }


    //  MODEL NỘI BỘ – đại diện một phòng trong danh sách
    static class Room {
        String  id, type, floor;
        boolean available;
        int     maxAdults, maxChildren;

        Room(String id, String type, String floor,
             boolean available, int maxAdults, int maxChildren) {
            this.id          = id;
            this.type        = type;
            this.floor       = floor;
            this.available   = available;
            this.maxAdults   = maxAdults;
            this.maxChildren = maxChildren;
        }
    }


    private final List<Room> allRooms      = new ArrayList<>();
    /** Phòng đang được tích chọn */
    private final List<Room> selectedRooms = new ArrayList<>();

    // Widgets cần tham chiếu từ nhiều nơi
    private JPanel            roomTableBody;
    private JPanel            bookingList;
    private JTextField        searchField;
    private JLabel            suggestionLabel;
    private DateTimePickerField checkInPicker;
    private DateTimePickerField checkOutPicker;
    private JSpinner          spAdults;
    private JSpinner          spChildren;

    // Bộ lọc hiện tại
    private String filterType  = "Loại phòng";
    private String filterFloor = "Tầng";

    // Gom logic backend qua service + DTO
    private final KhachHangService khachHangService = new KhachHangService();
    private final DatPhongService datPhongService = new DatPhongService();

    public DatPhongPanel() {
        setLayout(new BorderLayout());
        setBackground(BG);

        // Không tải phòng trống khi khởi tạo; sẽ tải theo tiêu chí khi nhấn Tìm kiếm
        JSplitPane split = new JSplitPane(
                JSplitPane.HORIZONTAL_SPLIT,
                buildLeft(),
                buildRight()
        );
        split.setDividerLocation(620);
        split.setDividerSize(0);
        split.setBorder(null);
        split.setBackground(BG);
        split.setContinuousLayout(true);
        add(split, BorderLayout.CENTER);
    }



    /** Tải danh sách phòng trạng thái "Trống" từ DAO */
    private void loadAvailableRooms() {
        try {
            LocalDateTime checkIn  = (checkInPicker != null) ? checkInPicker.getValue() : null;
            LocalDateTime checkOut = (checkOutPicker != null) ? checkOutPicker.getValue() : null;
            if (checkIn == null || checkOut == null) {
                allRooms.clear();
                return;
            }
            List<Phong> phongs = datPhongService.getDsPhongTrong(checkIn, checkOut);
            allRooms.clear();
            for (Phong p : phongs) {
                allRooms.add(toRoom(p));
            }
        } catch (Exception e) {
            allRooms.clear();
            JOptionPane.showMessageDialog(this,
                    "Không thể tải danh sách phòng trống:\n" + e.getMessage(),
                    "Lỗi kết nối", JOptionPane.ERROR_MESSAGE);
        }
    }

    /** Chuyển entity Phong → model nội bộ Room */
    private Room toRoom(Phong p) {
        String id   = p.getMaPhong();
        String type = (p.getLoaiPhong() == null || p.getLoaiPhong().getTenLoaiPhong() == null)
                ? "Chưa xác định"
                : p.getLoaiPhong().getTenLoaiPhong();
        String floor = (p.getTang() <= 0) ? "" : "Tầng " + p.getTang();
        String tinhTrang = p.getTinhTrang();
        String trangThai = p.getTrangThai();
        boolean available = "Trống".equalsIgnoreCase(tinhTrang)
                || "Sẵn sàng".equalsIgnoreCase(trangThai);
        int maxAdults      = (p.getLoaiPhong() == null) ? 0 : p.getLoaiPhong().getSoNguoiLonToiDa();
        int maxChildren    = (p.getLoaiPhong() == null) ? 0 : p.getLoaiPhong().getSoTreEmToiDa();
        return new Room(id, type, floor, available, maxAdults, maxChildren);
    }
    private JPanel buildLeft() {
        JPanel left = new JPanel(new BorderLayout());
        left.setBackground(BG);
        left.setBorder(new EmptyBorder(16, 20, 16, 10));

        // Phần trên: form điều kiện tìm kiếm + filter bar
        JPanel top = new JPanel();
        top.setLayout(new BoxLayout(top, BoxLayout.Y_AXIS));
        top.setOpaque(false);
        top.add(buildSearchForm());
        top.add(Box.createVerticalStrut(12));
        top.add(buildFilterBar());
        top.add(Box.createVerticalStrut(10));

        left.add(top, BorderLayout.NORTH);

        // Phần giữa: bảng phòng có scroll
        JScrollPane scroll = new JScrollPane(buildRoomTable());
        scroll.setBorder(null);
        scroll.setBackground(WHITE);
        scroll.getViewport().setBackground(WHITE);
        scroll.getVerticalScrollBar().setUnitIncrement(10);
        left.add(scroll, BorderLayout.CENTER);

        return left;
    }
    private JPanel buildSearchForm() {
        JPanel card = card();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBorder(new EmptyBorder(16, 18, 16, 18));
        // Hàng ngày check-in / check-out
        JPanel dateRow = new JPanel(new FlowLayout(FlowLayout.LEFT, 16, 0));
        dateRow.setOpaque(false);
        dateRow.setAlignmentX(LEFT_ALIGNMENT);
        dateRow.setMaximumSize(new Dimension(Integer.MAX_VALUE, 80));

        checkInPicker  = new DateTimePickerField(LocalDateTime.now().plusHours(1));
        checkOutPicker = new DateTimePickerField(LocalDateTime.now().plusDays(1));

        dateRow.add(buildDateField("Check in",  checkInPicker));
        dateRow.add(buildDateField("Check out", checkOutPicker));

        // Hàng số khách: người lớn + trẻ em
        JPanel guestRow = new JPanel(new FlowLayout(FlowLayout.LEFT, 16, 0));
        guestRow.setOpaque(false);
        guestRow.setAlignmentX(LEFT_ALIGNMENT);
        guestRow.setMaximumSize(new Dimension(Integer.MAX_VALUE, 60));
        guestRow.add(buildGuestSpinner("Người lớn", true));
        guestRow.add(buildGuestSpinner("Trẻ em",    false));

        card.add(dateRow);
        card.add(Box.createVerticalStrut(8));
        card.add(guestRow);
        card.add(Box.createVerticalStrut(12));

        // Nút tìm kiếm
        JButton btnSearch = blueBtn("Tìm kiếm", 110, 38);
        btnSearch.setAlignmentX(LEFT_ALIGNMENT);
        btnSearch.addActionListener(e -> applyRoomFilters());
        card.add(btnSearch);
        card.add(Box.createVerticalStrut(8));

        // Nhãn gợi ý kết quả
        suggestionLabel = lbl(
                "Nhập yêu cầu và nhấn Tìm kiếm để gợi ý phòng phù hợp.",
                F_PLAIN12, BLUE);
        suggestionLabel.setAlignmentX(LEFT_ALIGNMENT);
        card.add(suggestionLabel);

        return card;
    }

    /** Wrapper label + DateTimePickerField theo chiều dọc */
    private JPanel buildDateField(String label, DateTimePickerField picker) {
        JPanel p = new JPanel();
        p.setLayout(new BoxLayout(p, BoxLayout.Y_AXIS));
        p.setOpaque(false);

        JLabel lbl = lbl(label, F_LABEL, GRAY);
        lbl.setAlignmentX(LEFT_ALIGNMENT);
        p.add(lbl);
        p.add(Box.createVerticalStrut(4));

        picker.setAlignmentX(LEFT_ALIGNMENT);
        picker.setPreferredSize(new Dimension(260, 38));
        picker.setMaximumSize(new Dimension(260, 38));
        p.add(picker);
        return p;
    }

    /** Wrapper label + JSpinner cho số khách */
    private JPanel buildGuestSpinner(String label, boolean isAdult) {
        JPanel p = new JPanel();
        p.setLayout(new BoxLayout(p, BoxLayout.Y_AXIS));
        p.setOpaque(false);

        JLabel lbl = lbl(label, F_LABEL, GRAY);
        lbl.setAlignmentX(LEFT_ALIGNMENT);
        p.add(lbl);
        p.add(Box.createVerticalStrut(4));

        JSpinner spinner = new JSpinner(
                new SpinnerNumberModel(isAdult ? 1 : 0, 0, 20, 1));
        spinner.setFont(F_PLAIN12);
        spinner.setPreferredSize(new Dimension(84, 36));
        spinner.setMaximumSize(new Dimension(84, 36));
        spinner.setBorder(new CompoundBorder(
                new LineBorder(BORDER, 1, true),
                new EmptyBorder(2, 8, 2, 8)));

        if (isAdult) spAdults   = spinner;
        else         spChildren = spinner;

        p.add(spinner);
        p.setAlignmentX(LEFT_ALIGNMENT);
        return p;
    }

    // ─── Filter bar (ô tìm số phòng + dropdown loại phòng + dropdown tầng) ──

    private JPanel buildFilterBar() {
        JPanel bar = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        bar.setOpaque(false);
        bar.setAlignmentX(LEFT_ALIGNMENT);

        // Ô tìm theo số phòng
        bar.add(buildSearchField());

        // Dropdown lọc loại phòng
        bar.add(buildDropdown(true, "Loại phòng",
                new String[]{"Loại phòng", "Phòng thường", "Phòng Đôi Deluxe",
                        "Phòng Gia Đình Suite", "VIP"}));

        // Dropdown lọc theo tầng
        bar.add(buildDropdown(false, "Tầng",
                new String[]{"Tầng", "Tầng 1", "Tầng 2",
                        "Tầng 3", "Tầng 4", "Tầng 5"}));

        return bar;
    }

    /** Ô nhập số phòng với placeholder */
    private JPanel buildSearchField() {
        // Panel bo tròn làm "khung" cho text field
        JPanel sf = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0)) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                        RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(WHITE);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 20, 20);
                g2.setColor(BORDER);
                g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 20, 20);
                g2.dispose();
            }
        };
        sf.setOpaque(false);
        sf.setPreferredSize(new Dimension(160, 34));

        sf.add(lbl("🔍", new Font("Segoe UI", Font.PLAIN, 11), GRAY));

        searchField = new JTextField("Số phòng");
        searchField.setFont(F_PLAIN12);
        searchField.setForeground(GRAY);
        searchField.setBorder(null);
        searchField.setOpaque(false);
        searchField.setPreferredSize(new Dimension(110, 22));

        // Xử lý placeholder (text mờ khi chưa nhập)
        searchField.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                if ("Số phòng".equals(searchField.getText())) {
                    searchField.setText("");
                    searchField.setForeground(DARK);
                }
            }
            @Override
            public void focusLost(FocusEvent e) {
                if (searchField.getText().isEmpty()) {
                    searchField.setText("Số phòng");
                    searchField.setForeground(GRAY);
                }
            }
        });
        sf.add(searchField);
        return sf;
    }

    private JPanel buildDropdown(boolean isTypeFilter, String defaultText, String[] options) {
        JLabel label = lbl(defaultText, F_PLAIN12, DARK);

        JPanel btn = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0)) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                        RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(WHITE);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 20, 20);
                g2.setColor(BORDER);
                g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 20, 20);
                g2.dispose();
            }
        };
        btn.setOpaque(false);
        btn.setPreferredSize(new Dimension(145, 34));
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btn.add(label);

        // Click → hiện JPopupMenu
        btn.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                JPopupMenu menu = new JPopupMenu();
                menu.setBorder(new LineBorder(BORDER, 1, true));
                for (String opt : options) {
                    JMenuItem item = new JMenuItem(opt);
                    item.setFont(F_PLAIN12);
                    item.setForeground(DARK);
                    item.addActionListener(ae -> {
                        label.setText(opt + " ▾");
                        if (isTypeFilter) filterType  = opt;
                        else             filterFloor  = opt;
                    });
                    menu.add(item);
                }
                menu.show(btn, 0, btn.getHeight());
            }
        });
        return btn;
    }

    //  BẢNG PHÒNG
    /** Khung ngoài bảng (header cố định + body cuộn) */
    private JPanel buildRoomTable() {
        JPanel wrapper = new JPanel(new BorderLayout());
        wrapper.setBackground(WHITE);
        wrapper.setBorder(new CompoundBorder(
                new MatteBorder(1, 1, 1, 1, BORDER),
                new EmptyBorder(0, 0, 0, 0)));

        // Header bảng
        JPanel header = new JPanel(new GridLayout(1, 5));
        header.setBackground(new Color(0xF8F9FE));
        header.setPreferredSize(new Dimension(0, 38));
        header.setBorder(new MatteBorder(0, 0, 1, 0, BORDER));

        for (String col : new String[]{"Trạng thái", "", "Số Phòng", "Loại Phòng", "Tầng"}) {
            JLabel h = lbl(col, F_BOLD12, GRAY);
            h.setBorder(new EmptyBorder(0, 12, 0, 0));
            header.add(h);
        }
        wrapper.add(header, BorderLayout.NORTH);

        // Body bảng (danh sách hàng)
        roomTableBody = new JPanel();
        roomTableBody.setLayout(new BoxLayout(roomTableBody, BoxLayout.Y_AXIS));
        roomTableBody.setBackground(WHITE);
        refreshTable(Collections.emptyList());
        wrapper.add(roomTableBody, BorderLayout.CENTER);

        return wrapper;
    }

    /** Vẽ lại danh sách hàng trong bảng theo danh sách rooms truyền vào */
    private void refreshTable(List<Room> rooms) {
        roomTableBody.removeAll();
        for (Room room : rooms) {
            roomTableBody.add(buildRoomRow(room));
        }
        roomTableBody.add(Box.createVerticalGlue());
        roomTableBody.revalidate();
        roomTableBody.repaint();
    }

    /** Tạo một hàng trong bảng phòng */
    private JPanel buildRoomRow(Room room) {
        boolean isSelected = selectedRooms.contains(room);

        // Nền hàng: xanh nhạt nếu đang chọn, trắng nếu không
        JPanel row = new JPanel(new GridLayout(1, 5)) {
            @Override
            protected void paintComponent(Graphics g) {
                g.setColor(selectedRooms.contains(room) ? SEL_ROW : WHITE);
                g.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        row.setOpaque(false);
        row.setPreferredSize(new Dimension(0, 44));
        row.setMaximumSize(new Dimension(Integer.MAX_VALUE, 44));
        row.setBorder(new MatteBorder(0, 0, 1, 0, BORDER));

        // Cột 1: badge trạng thái
        JPanel statusCell = new JPanel(new FlowLayout(FlowLayout.LEFT, 12, 0));
        statusCell.setOpaque(false);
        JLabel badge = new JLabel(room.available ? "Có sẵn" : "Đang ở") {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                        RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(room.available ? GREEN_L : new Color(0xFFEEEE));
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 10, 10);
                g2.dispose();
                super.paintComponent(g);
            }
        };
        badge.setFont(F_LABEL);
        badge.setForeground(room.available ? GREEN : new Color(0xE04040));
        badge.setOpaque(false);
        badge.setBorder(new EmptyBorder(3, 8, 3, 8));
        statusCell.add(badge);

        // Cột 2: checkbox chọn phòng
        JPanel cbCell = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));
        cbCell.setOpaque(false);
        JCheckBox cb = new JCheckBox();
        cb.setOpaque(false);
        cb.setSelected(isSelected);
        cb.setEnabled(room.available);
        cb.addActionListener(e -> {
            if (!cb.isEnabled()) {
                cb.setSelected(false);
                return;
            }
            if (cb.isSelected()) {
                if (!selectedRooms.contains(room)) selectedRooms.add(room);
            } else {
                selectedRooms.remove(room);
            }
            refreshBookingList(); // cập nhật panel phải
            row.repaint();
        });
        cbCell.add(cb);

        // Cột 3: số phòng
        JPanel numCell = new JPanel(new FlowLayout(FlowLayout.LEFT, 12, 0));
        numCell.setOpaque(false);
        numCell.add(lbl(room.id, F_BOLD13, DARK));

        // Cột 4: loại phòng + sức chứa (có tooltip khi text bị cắt)
        JPanel typeCell = new JPanel(new FlowLayout(FlowLayout.LEFT, 12, 0));
        typeCell.setOpaque(false);
        String typeText = room.type + " (" + room.maxAdults + " NL, " + room.maxChildren + " TE)";
        JLabel typeLabel = lbl(typeText, F_PLAIN13, MID);
        typeLabel.setToolTipText(typeText); // tooltip đầy đủ
        typeCell.add(typeLabel);

        // Cột 5: tầng
        JPanel floorCell = new JPanel(new FlowLayout(FlowLayout.LEFT, 12, 0));
        floorCell.setOpaque(false);
        floorCell.add(lbl(room.floor, F_PLAIN13, MID));

        row.add(statusCell);
        row.add(cbCell);
        row.add(numCell);
        row.add(typeCell);
        row.add(floorCell);
        return row;
    }

    //  LOGIC LỌC PHÒNG
    private void applyRoomFilters() {
        LocalDateTime checkIn  = (checkInPicker  != null) ? checkInPicker.getValue()  : null;
        LocalDateTime checkOut = (checkOutPicker != null) ? checkOutPicker.getValue() : null;

        int adults   = (spAdults != null) ? (int) spAdults.getValue() : 0;
        int children = (spChildren != null) ? (int) spChildren.getValue() : 0;

        if (adults <= 0 && children <= 0) {
            JOptionPane.showMessageDialog(this,
                    "Vui lòng chọn ít nhất 1 người lớn hoặc trẻ em.",
                    "Dữ liệu không hợp lệ", JOptionPane.WARNING_MESSAGE);
            return;
        }

        if (children > 0 && adults <= 0) {
            JOptionPane.showMessageDialog(this,
                    "Trẻ em phải đi kèm ít nhất 1 người lớn.",
                    "Dữ liệu không hợp lệ", JOptionPane.WARNING_MESSAGE);
            return;
        }

        if (checkIn != null && checkIn.isBefore(LocalDateTime.now())) {
            JOptionPane.showMessageDialog(this,
                    "Ngày check-in không được trong quá khứ.",
                    "Dữ liệu không hợp lệ", JOptionPane.WARNING_MESSAGE);
            return;
        }

        if (checkIn != null && checkOut != null && !checkOut.isAfter(checkIn)) {
            JOptionPane.showMessageDialog(this,
                    "Check-out phải sau Check-in.",
                    "Dữ liệu không hợp lệ", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            List<Phong> phongs = datPhongService.getDsPhongTrong(checkIn, checkOut);

            allRooms.clear();
            for (Phong p : phongs) {
                allRooms.add(toRoom(p));
            }

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this,
                    "Lỗi tìm phòng: " + ex.getMessage(),
                    "Lỗi", JOptionPane.ERROR_MESSAGE);
            return;
        }

        selectedRooms.clear();
        refreshBookingList();


        String keyword = normPlaceholder(searchField.getText(), "Số phòng");
        String type  = normPlaceholder(filterType,  "Loại phòng");
        String floor = normPlaceholder(filterFloor, "Tầng");

        List<Room> filtered = new ArrayList<>();
        for (Room r : allRooms) {
            if (matchKeyword(r, keyword)
                    && matchType(r, type)
                    && matchFloor(r, floor)
                    && matchCapacity(r, adults, children)) {
                filtered.add(r);
            }
        }

        filtered.sort(Comparator
                .comparingInt((Room r) -> capacityGap(r, adults, children))
                .thenComparing(r -> r.id));

        refreshTable(filtered);

        if (filtered.isEmpty()) {
            suggestionLabel.setText("Không tìm thấy phòng phù hợp với tiêu chí đã chọn.");
            suggestionLabel.setForeground(new Color(0xE04040));
        } else {
            suggestionLabel.setText("Tìm thấy " + filtered.size() + " phòng phù hợp. Hãy chọn phòng để đặt.");
            suggestionLabel.setForeground(BLUE);
        }
    }


    private String normPlaceholder(String val, String placeholder) {
        if (val == null) return "";
        String t = val.trim();
        return t.equals(placeholder) ? "" : t;
    }

    private boolean matchKeyword(Room r, String kw) {
        if (kw == null || kw.isBlank()) return true;
        String k = kw.toLowerCase();
        return (r.id    != null && r.id.toLowerCase().contains(k))
                || (r.type  != null && r.type.toLowerCase().contains(k))
                || (r.floor != null && r.floor.toLowerCase().contains(k));
    }

    private boolean matchType(Room r, String type) {
        if (type == null || type.isBlank()) return true;
        return r.type != null && r.type.equalsIgnoreCase(type);
    }

    private boolean matchFloor(Room r, String floor) {
        if (floor == null || floor.isBlank()) return true;
        return r.floor != null && r.floor.equalsIgnoreCase(floor);
    }

    private boolean matchCapacity(Room r, int adults, int children) {
        // Cho phép hiển thị tất cả các phòng có sức chứa, khách hàng có thể chọn nhiều phòng cho đoàn đông người
        return r.maxAdults > 0 || r.maxChildren > 0;
    }

    /** Điểm "dư" capacity – nhỏ hơn = phù hợp hơn */
    private int capacityGap(Room r, int adults, int children) {
        if (r.maxAdults >= adults && r.maxChildren >= children) {
            // Nếu phòng chứa đủ: ưu tiên phòng vừa vặn nhất (gap nhỏ nhất)
            return (r.maxAdults - adults) + (r.maxChildren - children);
        } else {
            // Nếu phòng không chứa đủ (cho đoàn đông): ưu tiên phòng lớn nhất để giảm số lượng phòng cần đặt
            // Trả về giá trị lớn hơn 1000 để nằm sau các phòng chứa đủ, nhưng ưu tiên phòng lớn hơn (gap nhỏ hơn)
            return 1000 + (adults - r.maxAdults) + (children - r.maxChildren);
        }
    }
    //  PANEL PHẢI – Tóm tắt đặt phòng đã chọn
    private JPanel buildRight() {
        JPanel right = new JPanel(new BorderLayout());
        right.setBackground(BG);
        right.setBorder(new EmptyBorder(16, 10, 16, 20));

        JPanel inner = new JPanel(new BorderLayout());
        inner.setBackground(WHITE);
        inner.setBorder(new CompoundBorder(
                new LineBorder(BORDER, 1, true),
                new EmptyBorder(0, 0, 0, 0)));

        // Header bảng tóm tắt
        JPanel colHeader = new JPanel(new GridLayout(1, 5));
        colHeader.setBackground(new Color(0xF8F9FE));
        colHeader.setPreferredSize(new Dimension(0, 38));
        colHeader.setBorder(new MatteBorder(0, 0, 1, 0, BORDER));
        for (String c : new String[]{"Số phòng", "Check-in", "Check-out", "Thời gian", "Số khách"}) {
            JLabel cl = lbl(c, F_BOLD12, GRAY);
            cl.setBorder(new EmptyBorder(0, 12, 0, 0));
            colHeader.add(cl);
        }
        inner.add(colHeader, BorderLayout.NORTH);

        // Danh sách phòng đã chọn (scrollable)
        bookingList = new JPanel();
        bookingList.setLayout(new BoxLayout(bookingList, BoxLayout.Y_AXIS));
        bookingList.setBackground(WHITE);
        refreshBookingList();

        JScrollPane scroll = new JScrollPane(bookingList);
        scroll.setBorder(null);
        scroll.getVerticalScrollBar().setUnitIncrement(10);
        inner.add(scroll, BorderLayout.CENTER);

        // Nút Xác nhận → mở dialog khách hàng
        JButton confirmBtn = buildConfirmButton();
        inner.add(confirmBtn, BorderLayout.SOUTH);

        right.add(inner, BorderLayout.CENTER);
        return right;
    }

    /** Tạo nút "Xác nhận" kiểu pill xanh */
    private JButton buildConfirmButton() {
        JButton btn = new JButton("Xác nhận") {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                        RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(getModel().isRollover() ? new Color(0x2A5CD4) : BLUE);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 10, 10);
                g2.setColor(WHITE);
                g2.setFont(new Font("Segoe UI", Font.BOLD, 14));
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
        btn.setForeground(WHITE);
        btn.setPreferredSize(new Dimension(0, 48));
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        btn.addActionListener(e -> {
            if (selectedRooms.isEmpty()) {
                JOptionPane.showMessageDialog(this,
                        "Vui lòng chọn ít nhất một phòng!",
                        "Thông báo", JOptionPane.WARNING_MESSAGE);
                return;
            }
            if (checkInPicker == null || checkOutPicker == null) return;

            // Mở dialog nhập thông tin khách hàng
            openKhachHangDialog(
                    checkInPicker.getValue(),
                    checkOutPicker.getValue()
            );
        });
        return btn;
    }

    /**
     * Làm mới panel tóm tắt đặt phòng bên phải.
     * Chỉ hiện các phòng thực sự đang được chọn (không có data mẫu).
     */
    private void refreshBookingList() {
        bookingList.removeAll();

        if (selectedRooms.isEmpty()) {
            // Hiện placeholder khi chưa chọn phòng nào
            JLabel empty = lbl("Chưa có phòng nào được chọn.", F_PLAIN12, GRAY);
            empty.setBorder(new EmptyBorder(20, 16, 0, 0));
            bookingList.add(empty);
        } else {
            LocalDateTime checkIn  = (checkInPicker  != null) ? checkInPicker.getValue()  : null;
            LocalDateTime checkOut = (checkOutPicker != null) ? checkOutPicker.getValue() : null;

            String ciStr = (checkIn  != null) ? checkIn.format(FMT_DATETIME)  : "--";
            String coStr = (checkOut != null) ? checkOut.format(FMT_DATETIME) : "--";
            String durStr = buildDurationText(checkIn, checkOut);

            int adults = (spAdults != null) ? (int) spAdults.getValue() : 0;
            int children = (spChildren != null) ? (int) spChildren.getValue() : 0;
            List<String> allocation = allocateGuestsForUI(selectedRooms, adults, children);

            for (int i = 0; i < selectedRooms.size(); i++) {
                Room r = selectedRooms.get(i);
                String guestInfo = allocation.get(i);
                bookingList.add(buildBookingRow("#" + r.id, ciStr, coStr, durStr, guestInfo));
            }
        }

        bookingList.add(Box.createVerticalGlue());
        bookingList.revalidate();
        bookingList.repaint();
    }

    /** Tính chuỗi "X ngày Y giờ" từ hai mốc thời gian */
    private String buildDurationText(LocalDateTime from, LocalDateTime to) {
        if (from == null || to == null || !to.isAfter(from)) return "--";
        long hours = ChronoUnit.HOURS.between(from, to);
        long days  = hours / 24;
        long rem   = hours % 24;
        if (days > 0 && rem > 0) return days + " ngày " + rem + " giờ";
        if (days > 0)            return days + " ngày";
        return hours + " giờ";
    }

    /** Tạo một hàng trong panel tóm tắt đặt phòng */
    private JPanel buildBookingRow(String roomId,
                                   String checkIn, String checkOut, String duration, String guestInfo) {
        JPanel row = new JPanel(new GridLayout(1, 5));
        row.setBackground(WHITE);
        row.setPreferredSize(new Dimension(0, 90));
        row.setMaximumSize(new Dimension(Integer.MAX_VALUE, 90));
        row.setBorder(new MatteBorder(0, 0, 1, 0, BORDER));

        // ID phòng + icon check xanh
        JPanel idCell = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        idCell.setOpaque(false);
        idCell.add(buildCheckCircle());
        idCell.add(lbl(roomId, F_BOLD12, DARK));

        // Badge Check-in
        JPanel ciCell = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 8));
        ciCell.setOpaque(false);
        ciCell.add(buildDateBadge("Check-in", checkIn, BLUE_L, BLUE));

        // Badge Check-out
        JPanel coCell = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 8));
        coCell.setOpaque(false);
        coCell.add(buildDateBadge("Check-out", checkOut, GREEN_L, GREEN));

        // Thời gian lưu trú
        JPanel durCell = new JPanel(new FlowLayout(FlowLayout.CENTER));
        durCell.setOpaque(false);
        durCell.add(lbl(duration, F_PLAIN12, MID));

        // Số khách (phân bổ theo phòng)
        JPanel guestCell = new JPanel(new FlowLayout(FlowLayout.CENTER));
        guestCell.setOpaque(false);
        guestCell.add(lbl(guestInfo, F_PLAIN12, MID));

        row.add(idCell);
        row.add(ciCell);
        row.add(coCell);
        row.add(durCell);
        row.add(guestCell);
        return row;
    }

    private JPanel buildCheckCircle() {
        JPanel c = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                        RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(GREEN);
                g2.fillOval(0, 0, 18, 18);
                g2.setColor(WHITE);
                g2.setStroke(new BasicStroke(2f,
                        BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
                g2.drawLine(4, 9, 7, 13);
                g2.drawLine(7, 13, 14, 5);
                g2.dispose();
            }
        };
        c.setOpaque(false);
        c.setPreferredSize(new Dimension(18, 18));
        return c;
    }

    private JPanel buildDateBadge(String label, String date, Color bg, Color fg) {
        JPanel badge = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                        RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(bg);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 10, 10);
                g2.dispose();
            }
        };
        badge.setLayout(new BoxLayout(badge, BoxLayout.Y_AXIS));
        badge.setOpaque(false);
        badge.setBorder(new EmptyBorder(8, 12, 8, 12));

        JPanel topRow = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
        topRow.setOpaque(false);

        JLabel calIcon = new JLabel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                        RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(fg);
                g2.setStroke(new BasicStroke(1.3f,
                        BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
                g2.drawRoundRect(1, 2, 12, 11, 2, 2);
                g2.drawLine(1, 6, 13, 6);
                g2.drawLine(4, 0, 4, 4);
                g2.drawLine(10, 0, 10, 4);
                g2.dispose();
            }
        };
        calIcon.setPreferredSize(new Dimension(14, 16));
        topRow.add(calIcon);
        topRow.add(lbl(label, F_BOLD12, fg));
        topRow.setAlignmentX(LEFT_ALIGNMENT);

        JLabel dateLbl = lbl(date, F_SMALL, fg);
        dateLbl.setAlignmentX(LEFT_ALIGNMENT);

        badge.add(topRow);
        badge.add(dateLbl);
        return badge;
    }

    private List<String> allocateGuestsForUI(List<Room> rooms, int adults, int children) {
        int n = rooms.size();
        int[] adultsAlloc = new int[n];
        int[] childrenAlloc = new int[n];

        int remainingAdults = adults;
        int remainingChildren = children;

        // Bước 1: Mỗi phòng 1 người lớn
        for (int i = 0; i < n && remainingAdults > 0; i++) {
            if (rooms.get(i).maxAdults > 0) {
                adultsAlloc[i] = 1;
                remainingAdults--;
            }
        }

        // Bước 2: Lấp đầy người lớn
        for (int i = 0; i < n && remainingAdults > 0; i++) {
            int canTake = rooms.get(i).maxAdults - adultsAlloc[i];
            int toAdd = Math.min(remainingAdults, canTake);
            adultsAlloc[i] += toAdd;
            remainingAdults -= toAdd;
        }

        // Bước 3: Trẻ em
        for (int i = 0; i < n && remainingChildren > 0; i++) {
            int toAdd = Math.min(remainingChildren, rooms.get(i).maxChildren);
            childrenAlloc[i] += toAdd;
            remainingChildren -= toAdd;
        }

        List<String> result = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            result.add(adultsAlloc[i] + " NL, " + childrenAlloc[i] + " TE");
        }
        return result;
    }

    private void openKhachHangDialog(LocalDateTime checkIn, LocalDateTime checkOut) {

        Window parent = SwingUtilities.getWindowAncestor(this);
        JDialog dialog = (parent instanceof Frame)
                ? new JDialog((Frame) parent, "Thông tin Khách hàng", true)
                : new JDialog((Dialog) parent, "Thông tin Khách hàng", true);
        dialog.setSize(520, 560);
        dialog.setLocationRelativeTo(this);
        dialog.setResizable(false);

        JPanel root = new JPanel(new BorderLayout());
        root.setBackground(BG);
        dialog.setContentPane(root);


        JPanel titleBar = new JPanel(new BorderLayout());
        titleBar.setBackground(WHITE);
        titleBar.setBorder(new CompoundBorder(
                new MatteBorder(0, 0, 1, 0, BORDER),
                new EmptyBorder(14, 20, 14, 20)));

        JLabel titleLbl = lbl("Thông tin Khách hàng", F_TITLE, DARK);
        JLabel subLbl   = lbl("Nhập CCCD để tra cứu hoặc thêm mới khách hàng",
                F_PLAIN12, GRAY);
        JPanel titleTexts = new JPanel();
        titleTexts.setLayout(new BoxLayout(titleTexts, BoxLayout.Y_AXIS));
        titleTexts.setOpaque(false);
        titleTexts.add(titleLbl);
        titleTexts.add(Box.createVerticalStrut(2));
        titleTexts.add(subLbl);
        titleBar.add(titleTexts, BorderLayout.CENTER);
        root.add(titleBar, BorderLayout.NORTH);

        JPanel formPanel = new JPanel();
        formPanel.setLayout(new BoxLayout(formPanel, BoxLayout.Y_AXIS));
        formPanel.setBackground(BG);
        formPanel.setBorder(new EmptyBorder(20, 24, 20, 24));

        // Trạng thái khách hàng (thêm mới / cập nhật)
        JLabel statusLabel = lbl("● Chưa tra cứu", F_BOLD12, GRAY);
        statusLabel.setAlignmentX(LEFT_ALIGNMENT);
        formPanel.add(statusLabel);
        formPanel.add(Box.createVerticalStrut(14));

        // Hàng CCCD + nút Tra cứu
        JPanel cccdRow = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0));
        cccdRow.setOpaque(false);
        cccdRow.setAlignmentX(LEFT_ALIGNMENT);
        cccdRow.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));

        JTextField tfCCCD = buildFormField("Số CCCD / CMND", 220);
        JButton    btnLookup = blueBtn("Tra cứu", 90, 36);
        cccdRow.add(tfCCCD);
        cccdRow.add(btnLookup);
        formPanel.add(buildFormRow("CCCD *", cccdRow));
        formPanel.add(Box.createVerticalStrut(10));

        // Các trường thông tin khách hàng
        JTextField tfMaKH  = buildFormField("Tự động tạo nếu mới", 340);
        JTextField tfHoTen = buildFormField("Họ và tên khách hàng", 340);
        JTextField tfSDT   = buildFormField("Số điện thoại", 340);
        JTextField tfEmail = buildFormField("Email", 340);

        formPanel.add(buildFormRow("Mã khách hàng", tfMaKH));
        formPanel.add(Box.createVerticalStrut(10));
        formPanel.add(buildFormRow("Họ tên *", tfHoTen));
        formPanel.add(Box.createVerticalStrut(10));
        formPanel.add(buildFormRow("Điện thoại", tfSDT));
        formPanel.add(Box.createVerticalStrut(10));
        formPanel.add(buildFormRow("Email", tfEmail));
        formPanel.add(Box.createVerticalStrut(20));

        // Thông tin phòng đã chọn (chỉ đọc)
        JLabel roomsSummary = lbl(
                "Phòng đặt: " + buildSelectedRoomsSummary(), F_PLAIN12, MID);
        roomsSummary.setAlignmentX(LEFT_ALIGNMENT);
        formPanel.add(roomsSummary);

        JScrollPane scrollForm = new JScrollPane(formPanel);
        scrollForm.setBorder(null);
        scrollForm.getVerticalScrollBar().setUnitIncrement(8);
        root.add(scrollForm, BorderLayout.CENTER);

        // Mảng 1 phần tử để lambda có thể ghi (effectively final wrapper)
        final KhachHangDTO[] foundCustomer = {null};
        final boolean[]   isNewCustomer = {true};

        // ── Logic Tra cứu ─────────────────────────────────────────────────────
        btnLookup.addActionListener(e -> {
            String cccd = tfCCCD.getText().trim();
            if (cccd.isEmpty()) {
                JOptionPane.showMessageDialog(dialog,
                        "Vui lòng nhập số CCCD.", "Thiếu thông tin",
                        JOptionPane.WARNING_MESSAGE);
                return;
            }
            try {
                KhachHangDTO kh = khachHangService.timTheoCCCD(cccd);
                if (kh != null) {
                    // Tìm thấy → điền sẵn form
                    foundCustomer[0] = kh;
                    isNewCustomer[0] = false;
                    tfMaKH.setText(kh.getMaKhachHang());
                    tfHoTen.setText(kh.getTenKhachHang());
                    tfSDT.setText(kh.getSoDienThoai() != null ? kh.getSoDienThoai() : "");
                    tfEmail.setText(kh.getEmail() != null ? kh.getEmail() : "");
                    tfMaKH.setEditable(false);   // mã KH không được sửa
                    tfCCCD.setEditable(false);   // CCCD không được sửa sau tra cứu
                    statusLabel.setText("● Khách hàng đã tồn tại – sẽ cập nhật thông tin");
                    statusLabel.setForeground(BLUE);
                } else {
                    // Không tìm thấy → thêm mới
                    foundCustomer[0] = null;
                    isNewCustomer[0] = true;
                    tfMaKH.setText("");       // sẽ tự sinh sau
                    tfHoTen.setText("");
                    tfSDT.setText("");
                    tfEmail.setText("");
                    tfMaKH.setEditable(false);
                    statusLabel.setText("● Khách hàng mới – sẽ được thêm vào hệ thống");
                    statusLabel.setForeground(GREEN);
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(dialog,
                        "Lỗi tra cứu: " + ex.getMessage(),
                        "Lỗi", JOptionPane.ERROR_MESSAGE);
            }
        });


        JPanel footer = new JPanel(new FlowLayout(FlowLayout.RIGHT, 12, 12));
        footer.setBackground(WHITE);
        footer.setBorder(new MatteBorder(1, 0, 0, 0, BORDER));

        JButton btnCancel = new JButton("Hủy");
        btnCancel.setFont(F_BOLD12);
        btnCancel.setForeground(MID);
        btnCancel.setBorder(new CompoundBorder(
                new LineBorder(BORDER, 1, true),
                new EmptyBorder(8, 20, 8, 20)));
        btnCancel.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btnCancel.addActionListener(e -> dialog.dispose());

        JButton btnFinish = blueBtn("Hoàn tất đặt phòng", 180, 38);
        btnFinish.addActionListener(e -> {
            // Validate bắt buộc
            String hoTen = tfHoTen.getText().trim();
            String cccd  = tfCCCD.getText().trim();
            String sdt   = cleanInput(tfSDT.getText(), "Số điện thoại");
            String email = cleanInput(tfEmail.getText(), "Email");

            if (!validateBookingInput(dialog, cccd, hoTen, sdt, email, checkIn, checkOut)) {
                return;
            }

            try {
                KhachHangDTO dto = toKhachHangDTO(
                        isNewCustomer[0] ? null : foundCustomer[0],
                        cccd,
                        hoTen,
                        sdt,
                        email
                );

                if (isNewCustomer[0]) {
                    dto.setMaKhachHang(khachHangService.phatSinhMaMoi());
                    khachHangService.themKhachHang(dto);
                } else {
                    khachHangService.capNhatKhachHang(dto);
                }

                finalizeDatPhong(dto, checkIn, checkOut, dialog);

            } catch (Exception ex) {
                JOptionPane.showMessageDialog(dialog,
                        "Lỗi lưu dữ liệu: " + ex.getMessage(),
                        "Lỗi", JOptionPane.ERROR_MESSAGE);
            }
        });

        footer.add(btnCancel);
        footer.add(btnFinish);
        root.add(footer, BorderLayout.SOUTH);

        dialog.setVisible(true); // modal – block đến khi đóng
    }


    private void finalizeDatPhong(KhachHangDTO kh,
                                  LocalDateTime checkIn, LocalDateTime checkOut,
                                  JDialog dialog) {
        try {
            int adults = (spAdults != null) ? (int) spAdults.getValue() : 1;
            int children = (spChildren != null) ? (int) spChildren.getValue() : 0;
            int soNguoi = adults + children;

            DatPhongRequestDTO request = DatPhongRequestDTO.builder()
                    .maKhachHang(kh.getMaKhachHang())
                    .checkIn(checkIn)
                    .checkOut(checkOut)
                    .soNguoi(Math.max(1, soNguoi))
                    .soNguoiLon(Math.max(0, adults))
                    .soTreEm(Math.max(0, children))
                    .trangThai("Đã đặt")
                    .maPhongs(selectedRooms.stream().map(r -> r.id).toList())
                    .build();

            DatPhongResultDTO result = datPhongService.datPhong(request);

            dialog.dispose();
            JOptionPane.showMessageDialog(this,
                    "Đặt phòng thành công!\n"
                            + "Mã phiếu: " + result.getMaPhieuDatPhong() + "\n"
                            + "Khách hàng: " + kh.getTenKhachHang() + "\n"
                            + "Số phòng đã đặt: " + selectedRooms.size(),
                    "Thành công", JOptionPane.INFORMATION_MESSAGE);
            // Reset màn hình về trạng thái ban đầu
            selectedRooms.clear();
            refreshBookingList();
            refreshTable(Collections.emptyList());

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(dialog,
                    "Lỗi khi lưu đặt phòng: " + ex.getMessage(),
                    "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }

    private boolean validateBookingInput(JDialog dialog,
                                         String cccd,
                                         String hoTen,
                                         String sdt,
                                         String email,
                                         LocalDateTime checkIn,
                                         LocalDateTime checkOut) {
        if (cccd.isEmpty() || "Số CCCD / CMND".equalsIgnoreCase(cccd)) {
            JOptionPane.showMessageDialog(dialog,
                    "CCCD không được để trống.", "Thiếu thông tin",
                    JOptionPane.WARNING_MESSAGE);
            return false;
        }
        if (!cccd.matches("\\d{9,12}")) {
            JOptionPane.showMessageDialog(dialog,
                    "CCCD phải gồm 9-12 chữ số.", "Dữ liệu không hợp lệ",
                    JOptionPane.WARNING_MESSAGE);
            return false;
        }
        if (hoTen.isEmpty() || "Họ và tên khách hàng".equalsIgnoreCase(hoTen)) {
            JOptionPane.showMessageDialog(dialog,
                    "Họ tên không được để trống.", "Thiếu thông tin",
                    JOptionPane.WARNING_MESSAGE);
            return false;
        }
        if (!sdt.isEmpty() && !sdt.matches("0\\d{9,10}")) {
            JOptionPane.showMessageDialog(dialog,
                    "Số điện thoại không hợp lệ.", "Dữ liệu không hợp lệ",
                    JOptionPane.WARNING_MESSAGE);
            return false;
        }
        if (!email.isEmpty() && !email.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$")) {
            JOptionPane.showMessageDialog(dialog,
                    "Email không hợp lệ.", "Dữ liệu không hợp lệ",
                    JOptionPane.WARNING_MESSAGE);
            return false;
        }
        if (checkIn == null || checkOut == null || !checkOut.isAfter(checkIn)) {
            JOptionPane.showMessageDialog(dialog,
                    "Thời gian nhận/trả phòng chưa hợp lệ.", "Dữ liệu không hợp lệ",
                    JOptionPane.WARNING_MESSAGE);
            return false;
        }
        if (selectedRooms.isEmpty()) {
            JOptionPane.showMessageDialog(dialog,
                    "Vui lòng chọn ít nhất một phòng.", "Thiếu thông tin",
                    JOptionPane.WARNING_MESSAGE);
            return false;
        }
        return true;
    }

    private String buildSelectedRoomsSummary() {
        if (selectedRooms.isEmpty()) return "Chưa có phòng nào";
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < selectedRooms.size(); i++) {
            if (i > 0) sb.append(", ");
            sb.append(selectedRooms.get(i).id);
        }
        return sb.toString();
    }

    private KhachHangDTO toKhachHangDTO(KhachHangDTO oldData,
                                        String cccd,
                                        String hoTen,
                                        String sdt,
                                        String email) {
        KhachHangDTO dto = new KhachHangDTO();
        if (oldData != null) {
            dto.setMaKhachHang(oldData.getMaKhachHang());
        }
        dto.setCCCD(cccd);
        dto.setTenKhachHang(hoTen);
        dto.setSoDienThoai(sdt);
        dto.setEmail(email);
        return dto;
    }

    private String cleanInput(String value, String placeholder) {
        if (value == null) return "";
        String v = value.trim();
        return v.equals(placeholder) ? "" : v;
    }

    private JTextField buildFormField(String placeholder, int width) {
        JTextField tf = new JTextField();
        tf.setFont(F_PLAIN12);
        tf.setForeground(GRAY);
        tf.setText(placeholder);
        tf.setPreferredSize(new Dimension(width, 36));
        tf.setBorder(new CompoundBorder(
                new LineBorder(BORDER, 1, true),
                new EmptyBorder(4, 10, 4, 10)));

        tf.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                if (tf.getText().equals(placeholder)) {
                    tf.setText("");
                    tf.setForeground(DARK);
                }
            }
            @Override
            public void focusLost(FocusEvent e) {
                if (tf.getText().isEmpty()) {
                    tf.setText(placeholder);
                    tf.setForeground(GRAY);
                }
            }
        });
        return tf;
    }
    private JPanel buildFormRow(String labelText, JComponent widget) {
        JPanel row = new JPanel(new BorderLayout(12, 0));
        row.setOpaque(false);
        row.setAlignmentX(LEFT_ALIGNMENT);
        row.setMaximumSize(new Dimension(Integer.MAX_VALUE, 42));

        JLabel label = lbl(labelText, F_BOLD12, MID);
        label.setPreferredSize(new Dimension(110, 36));
        label.setHorizontalAlignment(SwingConstants.RIGHT);

        row.add(label,  BorderLayout.WEST);
        row.add(widget, BorderLayout.CENTER);
        return row;
    }
    static JPanel card() {
        JPanel p = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                        RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(WHITE);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 14, 14);
                g2.setColor(new Color(0xE8EEF8));
                g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 14, 14);
                g2.dispose();
            }
        };
        p.setOpaque(false);
        p.setAlignmentX(LEFT_ALIGNMENT);
        return p;
    }

    /** Tạo JLabel nhanh với font và màu chỉ định */
    static JLabel lbl(String text, Font f, Color c) {
        JLabel l = new JLabel(text);
        l.setFont(f);
        l.setForeground(c);
        return l;
    }

    /** Nút xanh bo tròn kiểu pill */
    static JButton blueBtn(String text, int w, int h) {
        JButton btn = new JButton(text) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                        RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(getModel().isRollover()
                        ? new Color(0x2A5CD4) : BLUE);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 8, 8);
                g2.setColor(WHITE);
                g2.setFont(F_BOLD12);
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
        btn.setForeground(WHITE);
        btn.setFont(F_BOLD12);
        btn.setPreferredSize(new Dimension(w, h));
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        return btn;
    }
}
