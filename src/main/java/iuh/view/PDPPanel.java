/*
 * @ (#) PDPPanel.java       1.0
 *
 * Copyright (c) 2026 IUH. All rights reserved
 */
package iuh.view;


import iuh.entity.PhieuDatPhong;
import iuh.service.PhieuDatPhongService;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/*
 * @description:
 * @author: Duc Thuan
 * @date: 23/4/2026
 * @version:    1.0
 * @created:
 */
public class PDPPanel extends JPanel {
    private JTable table;
    private DefaultTableModel model;

    public PDPPanel() {
        setLayout(new BorderLayout());

        String[] cols = {"Mã PDP", "Ngày tạo", "Tiền đặt cọc", "Trạng thái"};
        model = new DefaultTableModel(cols, 0);

        table = new JTable(model);
        JScrollPane scroll = new JScrollPane(table);

        add(scroll, BorderLayout.CENTER);

        loadData();


        table.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int row = table.getSelectedRow();
                if (row >= 0) {
                    String maPDP = table.getValueAt(row, 0).toString();

                    moManHinhDoiPhong(maPDP);
                }
            }
        });
    }

    private void loadData() {
        PhieuDatPhongService service = new PhieuDatPhongService();

        List<PhieuDatPhong> list = service.getAll();

        for (PhieuDatPhong p : list) {
            model.addRow(new Object[]{
                    p.getMaPhieuDatPhong(),
                    p.getNgayTao(),
                    p.getTienDatCoc(),
                    p.getTrangThai()
            });
        }
    }



    private void moManHinhDoiPhong(String maPDP) {
        JFrame frame = (JFrame) SwingUtilities.getWindowAncestor(this);

        frame.setContentPane(new DoiPhongPanel(maPDP));
        frame.revalidate();
    }
}
