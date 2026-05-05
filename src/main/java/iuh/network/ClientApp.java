/*
 * @ (#) ClientApp.java     1.0    5/5/2026
 *
 * Copyright (c) 2026 IUH. All rights reserved.
 */
package iuh.network;


/*
 * @description
 * @author:NguyenTruong
 * @date:  5/5/2026
 * @version:    1.0
 */

import iuh.view.VictoryaLogin;

import javax.swing.*;

public class ClientApp {
    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ignored) {
        }

        SwingUtilities.invokeLater(VictoryaLogin::new);
    }
}
