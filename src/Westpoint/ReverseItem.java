/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Westpoint;

import Intel.connectDB;
import com.sun.glass.events.KeyEvent;
import java.awt.Color;
import java.awt.Component;
import java.awt.Image;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import javax.imageio.ImageIO;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;
import net.proteanit.sql.DbUtils;

/**
 *
 * @author NyarkoPC
 */
public class ReverseItem extends javax.swing.JFrame {

    Connection conn = null;
    ResultSet rs = null;
    PreparedStatement pst = null;

    /**
     * Creates new form ReverseItem
     */
    public ReverseItem() {
        initComponents();
        conn = connectDB.ConnecrDb();
        //This try and catch block replace the java icon with custom one
        try {
            Image i = ImageIO.read(getClass().getResource("/image/doubleUU.jpg"));
            setIconImage(i);
        } catch (Exception e) {

        }

        UpdateReverse_table();
        Reverse_TableColor();
    }

    private void UpdateReverse_table() {
        try {
            String sql = "select Items,Qty_Left,Total_Qty from AllItems_Db order by Items ASC";
            pst = conn.prepareStatement(sql);
            rs = pst.executeQuery();
            Reverse_Table.setModel(DbUtils.resultSetToTableModel(rs));
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

    public void Reverse_TableColor() {
        Reverse_Table.getColumnModel().getColumn(0).setCellRenderer(new ReverseItem.CustomRenderer());
        Reverse_Table.getColumnModel().getColumn(1).setCellRenderer(new ReverseItem.CustomRenderer1());
        Reverse_Table.getColumnModel().getColumn(2).setCellRenderer(new ReverseItem.CustomRenderer2());
        Reverse_Table.getTableHeader().setDefaultRenderer(new ReverseItem.HeaderColor());
    }

    public void revertTableMouse() {
        int row = Reverse_Table.getSelectedRow();
        String Table_click = (Reverse_Table.getModel().getValueAt(row, 0).toString());

        try {

            String sql = "select * from AllItems_DB where Items='" + Table_click + "'";
            pst = conn.prepareStatement(sql);
            rs = pst.executeQuery();
            if (rs.next()) {
                String add1 = rs.getString("Items");
                txt_Itemname.setText(add1);
                String add2 = rs.getString("Total_Qty");
                txt_ItemtQty.setText(add2);
                String add3 = rs.getString("Qty_left");
                txt_QtyL.setText(add3);
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
        btn_revert.setEnabled(true);
        Reverse_TableColor();
    }

    public void revert() {
        try {
            String reverse = txt_Itemname.getText();

            if (reverse.isEmpty()) {
                JOptionPane.showMessageDialog(null, "Select Item to Revert");
            } else {
                int nam = Integer.parseInt(txt_QtyL.getText().toString());
                int numba = Integer.parseInt(txt_Revert.getText().toString());
                nam = nam + numba;
                txt_QtyL.setText(Integer.toString(nam));
                String Val1 = txt_Itemname.getText();

                String sql = "update AllItems_DB set Items='" + Val1 + "',Qty_left='" + nam + "' where Items='" + Val1 + "'";

                pst = conn.prepareStatement(sql);
                pst.execute();

                txt_Revert.setText("");
            }
            UpdateReverse_table();
            btn_revert.setEnabled(false);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, "Please Enter Quantity");
            txt_Revert.setBackground(Color.red);
        } finally {
            try {
                rs.close();
                pst.close();
            } catch (Exception e) {
            }
        }
        Reverse_TableColor();
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
        jScrollPane1 = new javax.swing.JScrollPane();
        Reverse_Table = new javax.swing.JTable();
        jLabel3 = new javax.swing.JLabel();
        txt_Itemname = new javax.swing.JTextField();
        txt_Revert = new javax.swing.JTextField();
        btn_revert = new javax.swing.JButton();
        jLabel4 = new javax.swing.JLabel();
        txt_ItemtQty = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        txt_QtyL = new javax.swing.JTextField();
        jLabel6 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("REVERSE ITEM FORM");
        setResizable(false);

        jPanel1.setBackground(new java.awt.Color(204, 204, 204));

        Reverse_Table.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        Reverse_Table.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                Reverse_TableMouseClicked(evt);
            }
        });
        Reverse_Table.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                Reverse_TableKeyReleased(evt);
            }
        });
        jScrollPane1.setViewportView(Reverse_Table);

        jLabel3.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(0, 0, 51));
        jLabel3.setText("Item Name");

        txt_Itemname.setEditable(false);
        txt_Itemname.setFont(new java.awt.Font("Times New Roman", 1, 17)); // NOI18N
        txt_Itemname.setForeground(new java.awt.Color(51, 0, 0));

        txt_Revert.setFont(new java.awt.Font("Times New Roman", 1, 17)); // NOI18N

        btn_revert.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        btn_revert.setIcon(new javax.swing.ImageIcon(getClass().getResource("/image/backarrow.png"))); // NOI18N
        btn_revert.setToolTipText("Click to revert");
        btn_revert.setBorder(null);
        btn_revert.setPreferredSize(new java.awt.Dimension(79, 28));
        btn_revert.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_revertActionPerformed(evt);
            }
        });

        jLabel4.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel4.setForeground(new java.awt.Color(0, 0, 51));
        jLabel4.setText("Total Qty");

        txt_ItemtQty.setEditable(false);
        txt_ItemtQty.setFont(new java.awt.Font("Times New Roman", 1, 17)); // NOI18N
        txt_ItemtQty.setForeground(new java.awt.Color(51, 0, 0));

        jLabel5.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel5.setForeground(new java.awt.Color(0, 0, 51));
        jLabel5.setText("Total Qty Left");

        txt_QtyL.setEditable(false);
        txt_QtyL.setFont(new java.awt.Font("Times New Roman", 1, 17)); // NOI18N
        txt_QtyL.setForeground(new java.awt.Color(51, 0, 0));

        jLabel6.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel6.setForeground(new java.awt.Color(0, 0, 51));
        jLabel6.setText("Enter Reverse Quantity");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 109, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txt_Itemname, javax.swing.GroupLayout.PREFERRED_SIZE, 193, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 109, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txt_ItemtQty, javax.swing.GroupLayout.PREFERRED_SIZE, 193, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 109, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txt_QtyL, javax.swing.GroupLayout.PREFERRED_SIZE, 193, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 185, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(txt_Revert, javax.swing.GroupLayout.PREFERRED_SIZE, 59, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(btn_revert, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 370, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(25, 25, 25)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(txt_Itemname)
                            .addComponent(jLabel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(txt_ItemtQty)
                            .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(txt_QtyL)
                            .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(28, 28, 28)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(btn_revert, javax.swing.GroupLayout.DEFAULT_SIZE, 30, Short.MAX_VALUE)
                            .addComponent(txt_Revert)
                            .addComponent(jLabel6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 553, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, 466, javax.swing.GroupLayout.PREFERRED_SIZE)
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void btn_revertActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_revertActionPerformed
        revert();
    }//GEN-LAST:event_btn_revertActionPerformed

    private void Reverse_TableMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_Reverse_TableMouseClicked
        revertTableMouse();
    }//GEN-LAST:event_Reverse_TableMouseClicked

    private void Reverse_TableKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_Reverse_TableKeyReleased
        if (evt.getKeyCode() == KeyEvent.VK_DOWN || evt.getKeyCode() == KeyEvent.VK_UP) {
            revertTableMouse();
        }
        Reverse_TableColor();
    }//GEN-LAST:event_Reverse_TableKeyReleased

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
            java.util.logging.Logger.getLogger(ReverseItem.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(ReverseItem.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(ReverseItem.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(ReverseItem.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new ReverseItem().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTable Reverse_Table;
    private javax.swing.JButton btn_revert;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTextField txt_Itemname;
    private javax.swing.JTextField txt_ItemtQty;
    private javax.swing.JTextField txt_QtyL;
    private javax.swing.JTextField txt_Revert;
    // End of variables declaration//GEN-END:variables
}
