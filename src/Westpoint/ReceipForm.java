package Westpoint;

import java.awt.Color;
import java.awt.Image;
import java.awt.print.PageFormat;
import java.awt.print.Paper;
import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JTextPane;
import javax.swing.text.BadLocationException;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;

public class ReceipForm extends javax.swing.JFrame {

    public String customerName;

    public ReceipForm() {
        initComponents();
        //when you want windows to be display in full screen
        // this.setExtendedState(JFrame.MAXIMIZED_BOTH);
        customerName = JOptionPane.showInputDialog("Enter Customer Name Here");
        PrintReceipt();

        try {
            Image i = ImageIO.read(getClass().getResource("/image/doubleUU.jpg"));
            setIconImage(i);
        } catch (Exception e) {

        }
    }

    public void PrintReceipt() {
        if (MainUserPanel.Cmd_User.getSelectedIndex() == 0) {
            MainUserPanel.Cmd_User.setBackground(Color.red);
            JOptionPane.showMessageDialog(null, "Select User Name", "Alert!!", JOptionPane.ERROR_MESSAGE);
        } else {

            Double totalBought = Double.parseDouble(MainUserPanel.txt_TotalBought.getText().toString());
            Double amountPaid = Double.parseDouble(MainUserPanel.txt_TotalPaid.getText().toString());
            Double Balance = Double.parseDouble(MainUserPanel.txt_balance.getText().toString());
            String User = MainUserPanel.Cmd_User.getSelectedItem().toString();
            String date = MainUserPanel.lbl_Time1.getText();
            String time = MainUserPanel.tt_time.getText();

            SimpleAttributeSet title = new SimpleAttributeSet();
            title = new SimpleAttributeSet();
            StyleConstants.setBold(title, true);
            StyleConstants.setFontSize(title, 15);
            StyleConstants.setFontFamily(title, "Tahoma");

            SimpleAttributeSet Subtitle = new SimpleAttributeSet();
            Subtitle = new SimpleAttributeSet();
            StyleConstants.setFontSize(Subtitle, 12);
            StyleConstants.setFontFamily(Subtitle, "Tahoma");

            SimpleAttributeSet loc = new SimpleAttributeSet();
            loc = new SimpleAttributeSet();
            StyleConstants.setFontSize(loc, 12);
            StyleConstants.setBold(loc, true);
            StyleConstants.setFontFamily(loc, "Tahoma");

            SimpleAttributeSet tel = new SimpleAttributeSet();
            tel = new SimpleAttributeSet();
            StyleConstants.setFontSize(tel, 12);
            StyleConstants.setFontFamily(tel, "Tahoma");

            SimpleAttributeSet user = new SimpleAttributeSet();
            user = new SimpleAttributeSet();
            StyleConstants.setBold(user, true);
            StyleConstants.setFontSize(user, 12);
            StyleConstants.setFontFamily(user, "Tahoma");

            SimpleAttributeSet loc2 = new SimpleAttributeSet();
            loc2 = new SimpleAttributeSet();
            StyleConstants.setFontSize(loc2, 12);
            StyleConstants.setBold(loc2, true);
            StyleConstants.setUnderline(loc2, true);
            StyleConstants.setFontFamily(loc2, "Tahoma");

            SimpleAttributeSet sim1 = new SimpleAttributeSet();
            StyleConstants.setFontSize(sim1, 12);

            SimpleAttributeSet tank = new SimpleAttributeSet();
            tank = new SimpleAttributeSet();
            StyleConstants.setFontSize(tank, 12);
            StyleConstants.setItalic(tank, true);
            StyleConstants.setFontFamily(tank, "Times New Roman");

            SimpleAttributeSet dev = new SimpleAttributeSet();
            dev = new SimpleAttributeSet();
            StyleConstants.setFontSize(dev, 10);
            StyleConstants.setItalic(dev, true);
            StyleConstants.setFontFamily(dev, "Calibri");

            try {
                ReceiptTextPane.getStyledDocument().insertString(ReceiptTextPane.getStyledDocument().getLength(), " " + "WESTPOINT COSMETICS", title);
                ReceiptTextPane.getStyledDocument().insertString(ReceiptTextPane.getStyledDocument().getLength(), "\n" + " " + "Sales of Wholesale $ Retail Product\n", Subtitle);
                ReceiptTextPane.getStyledDocument().insertString(ReceiptTextPane.getStyledDocument().getLength(), ".............................................\n", title);
                ReceiptTextPane.getStyledDocument().insertString(ReceiptTextPane.getStyledDocument().getLength(), "Loc : ", loc);
                ReceiptTextPane.getStyledDocument().insertString(ReceiptTextPane.getStyledDocument().getLength(), "Asanco Junction Adj City Centre\n", tel);
                ReceiptTextPane.getStyledDocument().insertString(ReceiptTextPane.getStyledDocument().getLength(), "Tel : ", loc);
                ReceiptTextPane.getStyledDocument().insertString(ReceiptTextPane.getStyledDocument().getLength(), "0243372923 / 0244251853\n", tel);
                ReceiptTextPane.getStyledDocument().insertString(ReceiptTextPane.getStyledDocument().getLength(), "Date : ", loc);
                ReceiptTextPane.getStyledDocument().insertString(ReceiptTextPane.getStyledDocument().getLength(), date + " " + time + "\n", tel);
                ReceiptTextPane.getStyledDocument().insertString(ReceiptTextPane.getStyledDocument().getLength(), "\n", loc);
                ReceiptTextPane.getStyledDocument().insertString(ReceiptTextPane.getStyledDocument().getLength(), "Teller Name : ", user);
                ReceiptTextPane.getStyledDocument().insertString(ReceiptTextPane.getStyledDocument().getLength(), User + "\n", tel);
                ReceiptTextPane.getStyledDocument().insertString(ReceiptTextPane.getStyledDocument().getLength(), ".............................................................\n", tel);
                ReceiptTextPane.getStyledDocument().insertString(ReceiptTextPane.getStyledDocument().getLength(), "Customer: ", user);
                ReceiptTextPane.getStyledDocument().insertString(ReceiptTextPane.getStyledDocument().getLength(), customerName + "\n", tel);
                ReceiptTextPane.getStyledDocument().insertString(ReceiptTextPane.getStyledDocument().getLength(), ".............................................................\n", tel);
                ReceiptTextPane.getStyledDocument().insertString(ReceiptTextPane.getStyledDocument().getLength(), "\n", loc);

                String space = (" " + " " + " " + " " + " " + " " + " " + " " + " " + " " + " " + " " + " ");
//                String space1 = (" " + " " + " " + " " + " " + " " + " " + " " + " " + " ");
                String space1 = null;

                String spac = (" " + " " + " " + " " + " " + " ");
                String spac1 = (" " + " " + " " + " " + " " + " ");

                ReceiptTextPane.getStyledDocument().insertString(ReceiptTextPane.getStyledDocument().getLength(), "Item" + "\t" + space + "Qty" + "\t" + spac + "Cost\n", loc2);

                int row = MainUserPanel.temp_Table.getRowCount();
                for (int j = 0; j < row; j++) {

                    String name = MainUserPanel.temp_Table.getValueAt(j, 0).toString().trim();
                    String Subname = "";
                    if (name.length() > 18) {
                        Subname = name.substring(0, 18);
                        space1 = (" " + " " + " " + " " + " " + " " + " ");
                    } else if (name.length() == 17) {
                        Subname = name;
                        space1 = (" " + " " + " " + " " + " " + " " + " " + " " + " ");
                    } else if (name.length() == 16) {
                        Subname = name;
                        space1 = (" " + " " + " " + " " + " " + " " + " " + " " + " " + " " + " ");
                    } else if (name.length() == 15) {
                        Subname = name;
                        space1 = (" " + " " + " " + " " + " " + " " + " " + " " + " " + " " + " " + " ");
                    } else if (name.length() == 14) {
                        Subname = name;
                        space1 = (" " + " " + " " + " " + " " + " " + " " + " " + " " + " " + " " + " " + " ");
                    } else if (name.length() == 13) {
                        Subname = name;
                        space1 = (" " + " " + " " + " " + " " + " " + " " + " " + " " + " " + " " + " " + " " + " " + " ");
                    } else if (name.length() == 12) {
                        Subname = name;
                        space1 = (" " + " " + " " + " " + " " + " " + " " + " " + " " + " " + " " + " " + " " + " " + " " + " ");
                    } else {
                        Subname = name;
                        space1 = ("\t" + " " + " " + " " + " " + " " + " " + " " + " " + " " + " " + " " + " " + " " + " " + " " + " ");
                    }

                    ReceiptTextPane.getStyledDocument().insertString(ReceiptTextPane.getStyledDocument().getLength(), Subname + space1 + MainUserPanel.temp_Table.getValueAt(j, 1).toString() + "\t" + spac1 + MainUserPanel.temp_Table.getValueAt(j, 2).toString(), sim1);

                    ReceiptTextPane.getStyledDocument().insertString(ReceiptTextPane.getStyledDocument().getLength(), "\n", sim1);
                }

                ReceiptTextPane.getStyledDocument().insertString(ReceiptTextPane.getStyledDocument().getLength(), "..............................................................\n", tel);
                ReceiptTextPane.getStyledDocument().insertString(ReceiptTextPane.getStyledDocument().getLength(), "Total Cost Ghc : \t", loc);
                ReceiptTextPane.getStyledDocument().insertString(ReceiptTextPane.getStyledDocument().getLength(), spac1 + totalBought + "\n", tel);
                ReceiptTextPane.getStyledDocument().insertString(ReceiptTextPane.getStyledDocument().getLength(), "Amount Paid Ghc : \t", loc);
                ReceiptTextPane.getStyledDocument().insertString(ReceiptTextPane.getStyledDocument().getLength(), spac1 + amountPaid + "\n", tel);
                ReceiptTextPane.getStyledDocument().insertString(ReceiptTextPane.getStyledDocument().getLength(), "..............................................................\n", tel);
                ReceiptTextPane.getStyledDocument().insertString(ReceiptTextPane.getStyledDocument().getLength(), "Balance Remain Ghc : \t", loc);
                ReceiptTextPane.getStyledDocument().insertString(ReceiptTextPane.getStyledDocument().getLength(), spac1 + Balance + "\n", tel);
                ReceiptTextPane.getStyledDocument().insertString(ReceiptTextPane.getStyledDocument().getLength(), "..............................................................\n", tel);
                ReceiptTextPane.getStyledDocument().insertString(ReceiptTextPane.getStyledDocument().getLength(), "\n..............................................................\n", tel);
                ReceiptTextPane.getStyledDocument().insertString(ReceiptTextPane.getStyledDocument().getLength(), "\t" + "Signature\n", loc);
                ReceiptTextPane.getStyledDocument().insertString(ReceiptTextPane.getStyledDocument().getLength(), " " + " " + " " + " " + " " + " " + " " + " " + "Items Sold Are Not Returnable\n", sim1);
                ReceiptTextPane.getStyledDocument().insertString(ReceiptTextPane.getStyledDocument().getLength(), " " + " " + " " + " " + " " + " " + " " + " " + " Call Developer on 0544474706/0500383888", dev);

            } catch (BadLocationException e) {
                e.printStackTrace();
            }

        }
    }

    public void invoice_job() {
        try {
            int rows = MainUserPanel.temp_Table.getRowCount();
            PrinterJob printerJob = PrinterJob.getPrinterJob();
            PageFormat pageFormat = printerJob.defaultPage();
            Paper paper = new Paper();
            paper.setSize(600, (double) (paper.getHeight() + rows * 2));
            paper.setImageableArea(rows, rows, paper.getWidth() - rows * 20, paper.getHeight() - rows * 1);
            pageFormat.setPaper(paper);
            pageFormat.setOrientation(PageFormat.PORTRAIT);
            printerJob.setPrintable(ReceiptTextPane.getPrintable(null, null), pageFormat);
            printerJob.print();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jScrollPane5 = new javax.swing.JScrollPane();
        ReceiptTextPane = new javax.swing.JTextPane();
        jPanel2 = new javax.swing.JPanel();
        btnPrint = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Receipt Form");
        setBackground(new java.awt.Color(36, 47, 65));
        setResizable(false);

        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder(""));

        ReceiptTextPane.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jScrollPane5.setViewportView(ReceiptTextPane);

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane5, javax.swing.GroupLayout.DEFAULT_SIZE, 241, Short.MAX_VALUE)
                .addGap(7, 7, 7))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane5, javax.swing.GroupLayout.PREFERRED_SIZE, 356, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder(""));

        btnPrint.setBackground(new java.awt.Color(36, 47, 65));
        btnPrint.setFont(new java.awt.Font("Mangal", 1, 14)); // NOI18N
        btnPrint.setForeground(new java.awt.Color(51, 0, 0));
        btnPrint.setIcon(new javax.swing.ImageIcon(getClass().getResource("/image/print2.png"))); // NOI18N
        btnPrint.setToolTipText("Click to Print Receipt");
        btnPrint.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnPrintActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(btnPrint)
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(btnPrint, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jPanel2, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0))
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void btnPrintActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPrintActionPerformed
        invoice_job();
    }//GEN-LAST:event_btnPrintActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(ReceipForm.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(ReceipForm.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(ReceipForm.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(ReceipForm.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new ReceipForm().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    public static javax.swing.JTextPane ReceiptTextPane;
    private javax.swing.JButton btnPrint;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane5;
    // End of variables declaration//GEN-END:variables
}
