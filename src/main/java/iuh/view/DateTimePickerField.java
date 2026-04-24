package iuh.view;

import com.toedter.calendar.JDateChooser;

import javax.swing.*;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

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
        dateEditor.setBorder(new CompoundBorder(
                new LineBorder(QuanLyNhanVienPanel.BORDER_COL, 1, true),
                new EmptyBorder(0, 12, 0, 12)));

        timeSpinner = new JSpinner(new SpinnerDateModel());
        timeSpinner.setPreferredSize(new Dimension(96, 38));
        timeSpinner.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        JSpinner.DateEditor editor = new JSpinner.DateEditor(timeSpinner, "HH:mm");
        timeSpinner.setEditor(editor);
        editor.getTextField().setBorder(new CompoundBorder(
                new LineBorder(QuanLyNhanVienPanel.BORDER_COL, 1, true),
                new EmptyBorder(0, 8, 0, 8)));

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
        return datePart.withHour(timePart.getHour())
                .withMinute(timePart.getMinute())
                .withSecond(0)
                .withNano(0);
    }

    void setValue(LocalDateTime value) {
        LocalDateTime actual = value == null
                ? LocalDateTime.now().withSecond(0).withNano(0)
                : value.withSecond(0).withNano(0);
        Date date = Date.from(actual.atZone(ZoneId.systemDefault()).toInstant());
        dateChooser.setDate(date);
        timeSpinner.setValue(date);
    }
}

