package Westpoint;

import Intel.connectDB;
import java.awt.Color;
import java.awt.Component;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.WindowEvent;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.MessageFormat;
import java.util.Vector;
import javax.imageio.ImageIO;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import net.proteanit.sql.DbUtils;

/**
 *
 * @author NyarkoPC
 */
public class FinishingItemPane extends javax.swing.JFrame {

    Connection conn = null;
    ResultSet rs = null;
    PreparedStatement pst = null;
    Vector originalTableFinish;

    public FinishingItemPane() {
        initComponents();

        conn = connectDB.ConnecrDb();
        //This try and catch block replace the java icon with custom one
        try {
            Image i = ImageIO.read(getClass().getResource("/image/doubleUU.jpg"));
            setIconImage(i);
        } catch (Exception e) {

        }
        table_Finish();
        originalTableFinish = (Vector) ((DefaultTableModel) finishTable.getModel()).getDataVector().clone();
        tableColor();
    }

    public void tableColor() {
        finishTable.getColumnModel().getColumn(0).setCellRenderer(new FinishingItemPane.CustomRenderer());
        finishTable.getColumnModel().getColumn(1).setCellRenderer(new FinishingItemPane.CustomRenderer1());
       
        finishTable.getTableHeader().setDefaultRenderer(new FinishingItemPane.HeaderColor());

    }

    public void close() {
        WindowEvent winClosingEvent = new WindowEvent(this, WindowEvent.WINDOW_CLOSING);
        Toolkit.getDefaultToolkit().getSystemEventQueue().postEvent(winClosingEvent);
    }

    private void table_Finish() {

        DefaultTableModel dmodel = (DefaultTableModel) finishTable.getModel();

        Object[] object;
        try {
            String sql = "select Items,Qty_left from AllItems_DB where Qty_left <= 5 order by Qty_left DESC";
            pst = conn.prepareStatement(sql);
            rs = pst.executeQuery();

            while (rs.next()) {
                String items = rs.getString("Items");
                String qty = rs.getString("Qty_Left");
              
                object = new Object[]{items, qty};
                dmodel.addRow(object);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
        } finally {
            try {
                rs.close();
                pst.close();
            } catch (Exception e) {
            }
        }

    }

    public static class CustomRenderer extends DefaultTableCellRenderer {

        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            Component cellComponent = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
            if (column == 0) {
                // cellComponent.setBackground(Color.magenta);
                cellComponent.setForeground(Color.BLACK);
                cellComponent.setFont(new java.awt.Font("Tahoma", 1, 12));
            }
            return cellComponent;
        }
    }

    public static class CustomRenderer1 extends DefaultTableCellRenderer {

        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            Component cellComponent = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
            if (column == 1) {
                // cellComponent.setBackground(Color.magenta);
                cellComponent.setForeground(Color.RED);
                cellComponent.setFont(new java.awt.Font("Tahoma", 1, 12));
            }
            return cellComponent;
        }
    }

    public static class CustomRenderer2 extends DefaultTableCellRenderer {

        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            Component cellComponent = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
            if (column == 2) {
                // cellComponent.setBackground(Color.magenta);
                cellComponent.setForeground(Color.BLUE);
                cellComponent.setFont(new java.awt.Font("Tahoma", 1, 12));
            }
            return cellComponent;
        }
    }

    public static class HeaderColor extends DefaultTableCellRenderer {

        public HeaderColor() {
            setOpaque(true);
        }

        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            Component cellComponent = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
            cellComponent.setBackground(Color.ORANGE);
            cellComponent.setForeground(Color.BLACK);
            cellComponent.setFont(new java.awt.Font("Tahoma", 1, 18));
            return cellComponent;
        }
    }

    public void print() {
        MessageFormat header = new MessageFormat("WESTPOINT COSMETICS FINISHING ITEMS");
        MessageFormat footer = new MessageFormat("Page 0");
        try {
            finishTable.print(JTable.PrintMode.FIT_WIDTH, header, footer);
        } catch (java.awt.print.PrinterException e) {
            System.err.format("Cannot print %s%n", e.getMessage());
        } finally {
            try {
                rs.close();
                pst.close();
            } catch (Exception e) {
            }
        }
    }
    
     public void preview() {
        PrintPreview ppt = new PrintPreview();
        ppt.previewFinish();
        ppt.setVisible(true);
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
        jPanel3 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        finishTable = new javax.swing.JTable();
        printFinish = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("ITEMS WITH TOTAL REMAINING < 5 FRAME");
        setBackground(new java.awt.Color(36, 47, 65));
        setResizable(false);

        jPanel3.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "TABLE SHOWING ITEMS BELOW FIVE", javax.swing.border.TitledBorder.CENTER, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 1, 15), new java.awt.Color(102, 0, 0))); // NOI18N

        finishTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Item Name", "Quatity Left"
            }
        ));
        jScrollPane1.setViewportView(finishTable);

        printFinish.setFont(new java.awt.Font("Tahoma", 1, 15)); // NOI18N
        printFinish.setIcon(new javax.swing.ImageIcon(getClass().getResource("/image/preview.png"))); // NOI18N
        printFinish.setText("Preview Print");
        printFinish.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                printFinishActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 522, Short.MAX_VALUE)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(printFinish, javax.swing.GroupLayout.PREFERRED_SIZE, 226, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 380, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(printFinish, javax.swing.GroupLayout.PREFERRED_SIZE, 63, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void printFinishActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_printFinishActionPerformed
        preview();
        PrintPreview.btnPrintDailyAdmin.setVisible(false);
        PrintPreview.btnPrintDaily.setVisible(false);
        PrintPreview.btnPrintAdminAll.setVisible(false);
    }//GEN-LAST:event_printFinishActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) throws ClassNotFoundException, InstantiationException, IllegalAccessException, UnsupportedLookAndFeelException {
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
            java.util.logging.Logger.getLogger(FinishingItemPane.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(FinishingItemPane.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(FinishingItemPane.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(FinishingItemPane.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        UIManager.setLookAndFeel("com.jtattoo.plaf.aluminium.AluminiumLookAndFeel");
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new FinishingItemPane().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    public static javax.swing.JTable finishTable;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JButton printFinish;
    // End of variables declaration//GEN-END:variables
}
