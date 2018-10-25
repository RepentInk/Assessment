package Westpoint;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
import Intel.connectDB;
import java.awt.Color;
import java.awt.Component;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.print.PrinterException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.Timer;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.text.BadLocationException;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import net.proteanit.sql.DbUtils;

/**
 *
 * @author Nyarkoisaac
 */
public class SalesForDay extends javax.swing.JFrame {

    Connection conn = null;
    ResultSet rs = null;
    PreparedStatement pst = null;

    Vector originalTableModel2;
    Vector originalTableModel21;
    private java.awt.Color color = java.awt.Color.LIGHT_GRAY;

    public SalesForDay() {
        initComponents();
        conn = connectDB.ConnecrDb();
        //This try and catch block replace the java icon with custom one
        try {
            Image i = ImageIO.read(getClass().getResource("/image/doubleUU.jpg"));
            setIconImage(i);
        } catch (Exception e) {

        }
        DailySalesTable();
        sumup();
        StartTim();

        originalTableModel2 = (Vector) ((DefaultTableModel) DailySalesTable.getModel()).getDataVector().clone();
        DailyTableColor(); // Daily Table Color

        lbl_Timer.setVisible(false);

    }

    private void DailySalesTable() {

        String com = MainUserPanel.lbl_Time1.getText().toLowerCase();
        try {
            String sql = "select Item,Qty,Tot_Cost,Cost,Date,Time  from DailySales_DB where Date='" + com + "' order by Date DESC";
            pst = conn.prepareStatement(sql);
            rs = pst.executeQuery();
            DailySalesTable.setModel(DbUtils.resultSetToTableModel(rs));
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

    public void DailyTableColor() {
        DailySalesTable.getColumnModel().getColumn(0).setCellRenderer(new SalesForDay.CustomRenderer());
        DailySalesTable.getColumnModel().getColumn(1).setCellRenderer(new SalesForDay.CustomRenderer1());
        DailySalesTable.getColumnModel().getColumn(2).setCellRenderer(new SalesForDay.CustomRenderer2());
        DailySalesTable.getColumnModel().getColumn(3).setCellRenderer(new SalesForDay.CustomRenderer3());
        DailySalesTable.getColumnModel().getColumn(4).setCellRenderer(new SalesForDay.CustomRenderer4());
        DailySalesTable.getTableHeader().setDefaultRenderer(new SalesForDay.HeaderColor());
    }

    public void searchTableDaily(String SearchString) {
        try {
            DefaultTableModel currtableModel = (DefaultTableModel) DailySalesTable.getModel();
            currtableModel.setRowCount(0);
            for (Object rows2 : originalTableModel2) {
                Vector rowVector = (Vector) rows2;

                for (Object column : rowVector) {
                    if (column.toString().contains(SearchString)) {
                        currtableModel.addRow(rowVector);
                        break;
                    }
                }
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
        } finally {
            try {
                // conn.close();
                rs.close();
                pst.close();
            } catch (Exception e) {
            }
        }

    }

    public void sumup() {

        //Converting to three decimal places
        DecimalFormat df2 = new DecimalFormat("####.###");
        double total = 0;

        int rowcount = DailySalesTable.getRowCount();
        double sum = 0;
        for (int i = 0; i < rowcount; i++) {
            sum = sum + Double.parseDouble(DailySalesTable.getValueAt(i, 2).toString());
        }
        total = Double.valueOf(df2.format(sum));
        Total_Daily.setText(Double.toString(total));
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
                cellComponent.setForeground(Color.BLACK);
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

    public static class CustomRenderer3 extends DefaultTableCellRenderer {

        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            Component cellComponent = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
            if (column == 3) {
                // cellComponent.setBackground(Color.magenta);
                cellComponent.setForeground(Color.RED);
                cellComponent.setFont(new java.awt.Font("Tahoma", 1, 12));
            }
            return cellComponent;
        }
    }

    public static class CustomRenderer4 extends DefaultTableCellRenderer {

        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            Component cellComponent = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
            if (column == 4) {
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
            cellComponent.setBackground(Color.yellow);
            cellComponent.setForeground(Color.BLACK);
            cellComponent.setFont(new java.awt.Font("Tahoma", 1, 18));
            return cellComponent;
        }
    }

    public void StartTim() {

        Timer t = new Timer(1000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                SimpleDateFormat sdf2 = new SimpleDateFormat("d MMM EE, yyy");
                lbl_Timer.setText(sdf2.format(new java.util.Date()));
            }
        });
        t.start();
    }

    public void tableClick() {
        int row = DailySalesTable.getSelectedRow();
        String date = (DailySalesTable.getModel().getValueAt(row, 4).toString());
        String time = (DailySalesTable.getModel().getValueAt(row, 5).toString());

        try {
            String sql = "select * from DailySales_DB where date='" + date + "' and Time='" + time + "'";
            pst = conn.prepareStatement(sql);
            rs = pst.executeQuery();
            if (rs.next()) {
                String add1 = rs.getString("Date").toUpperCase();
                lblDate.setText(add1);
                String add2 = rs.getString("Time").toUpperCase();
                lblTime.setText(add2);
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

    public void preview() {
        PrintPreview ppt = new PrintPreview();
        ppt.preview();
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
        panePreview = new javax.swing.JTabbedPane();
        jPanel2 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        DailySalesTable = new javax.swing.JTable();
        jPanel4 = new javax.swing.JPanel();
        jPanel7 = new javax.swing.JPanel();
        lblDate = new javax.swing.JLabel();
        lblTime = new javax.swing.JLabel();
        txt_Refresh = new javax.swing.JButton();
        jPanel3 = new javax.swing.JPanel();
        jLabel4 = new javax.swing.JLabel();
        Total_Daily = new javax.swing.JTextField();
        txt_Search = new javax.swing.JTextField();
        preview = new javax.swing.JButton();
        lbl_Timer = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("DAILY SALES KEEPING FRAME");
        setBackground(new java.awt.Color(36, 47, 65));

        panePreview.setForeground(new java.awt.Color(51, 0, 0));
        panePreview.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N

        jPanel2.setBackground(new java.awt.Color(255, 204, 204));

        DailySalesTable.setBackground(new java.awt.Color(240, 240, 240));
        DailySalesTable.setFont(new java.awt.Font("Tahoma", 0, 13)); // NOI18N
        DailySalesTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        DailySalesTable.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                DailySalesTableMouseClicked(evt);
            }
        });
        DailySalesTable.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                DailySalesTableKeyReleased(evt);
            }
        });
        jScrollPane1.setViewportView(DailySalesTable);

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 397, Short.MAX_VALUE)
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 374, Short.MAX_VALUE)
        );

        panePreview.addTab("Daily Items Bought", jPanel2);

        jPanel7.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Date & Time", javax.swing.border.TitledBorder.LEADING, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 1, 14), new java.awt.Color(51, 0, 0))); // NOI18N

        lblDate.setFont(new java.awt.Font("Tahoma", 1, 16)); // NOI18N
        lblDate.setForeground(new java.awt.Color(102, 0, 0));

        lblTime.setFont(new java.awt.Font("Tahoma", 1, 16)); // NOI18N
        lblTime.setForeground(new java.awt.Color(0, 0, 102));

        txt_Refresh.setFont(new java.awt.Font("Times New Roman", 1, 13)); // NOI18N
        txt_Refresh.setForeground(new java.awt.Color(51, 0, 0));
        txt_Refresh.setIcon(new javax.swing.ImageIcon(getClass().getResource("/image/ref.png"))); // NOI18N
        txt_Refresh.setToolTipText("Refresh");
        txt_Refresh.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txt_RefreshActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel7Layout = new javax.swing.GroupLayout(jPanel7);
        jPanel7.setLayout(jPanel7Layout);
        jPanel7Layout.setHorizontalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(lblTime, javax.swing.GroupLayout.DEFAULT_SIZE, 191, Short.MAX_VALUE)
                    .addComponent(lblDate, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(txt_Refresh, javax.swing.GroupLayout.PREFERRED_SIZE, 49, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel7Layout.setVerticalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(txt_Refresh, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblDate, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(lblTime, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        jPanel3.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Search Pane", javax.swing.border.TitledBorder.LEFT, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 1, 14), new java.awt.Color(51, 0, 51))); // NOI18N

        jLabel4.setBackground(new java.awt.Color(251, 188, 5));
        jLabel4.setFont(new java.awt.Font("Tahoma", 1, 17)); // NOI18N
        jLabel4.setForeground(new java.awt.Color(251, 189, 7));
        jLabel4.setText("Total Search");

        Total_Daily.setEditable(false);
        Total_Daily.setFont(new java.awt.Font("Tahoma", 1, 15)); // NOI18N

        txt_Search.setBackground(new java.awt.Color(240, 240, 240));
        txt_Search.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        txt_Search.setText("Search...");
        txt_Search.setToolTipText("Enter By Date to Search");
        txt_Search.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                txt_SearchFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                txt_SearchFocusLost(evt);
            }
        });
        txt_Search.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txt_SearchKeyReleased(evt);
            }
        });

        preview.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        preview.setForeground(new java.awt.Color(0, 0, 51));
        preview.setText("Preview Print");
        preview.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                previewActionPerformed(evt);
            }
        });

        lbl_Timer.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        lbl_Timer.setForeground(new java.awt.Color(153, 0, 0));

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lbl_Timer, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 135, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(Total_Daily)
                                .addComponent(txt_Search, javax.swing.GroupLayout.DEFAULT_SIZE, 247, Short.MAX_VALUE))
                            .addComponent(preview, javax.swing.GroupLayout.PREFERRED_SIZE, 175, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(txt_Search, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(Total_Daily, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(preview, javax.swing.GroupLayout.PREFERRED_SIZE, 54, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(lbl_Timer, javax.swing.GroupLayout.PREFERRED_SIZE, 19, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jPanel3, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(jPanel7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, 273, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(panePreview)
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addComponent(panePreview))
                .addContainerGap())
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void txt_SearchKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txt_SearchKeyReleased
        searchTableDaily(txt_Search.getText().toString());
        sumup();
        DailyTableColor();
    }//GEN-LAST:event_txt_SearchKeyReleased

    private void DailySalesTableMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_DailySalesTableMouseClicked
        tableClick();
    }//GEN-LAST:event_DailySalesTableMouseClicked

    private void DailySalesTableKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_DailySalesTableKeyReleased
        if (evt.getKeyCode() == KeyEvent.VK_DOWN || evt.getKeyCode() == KeyEvent.VK_UP) {
            tableClick();
        }
    }//GEN-LAST:event_DailySalesTableKeyReleased

    private void txt_RefreshActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txt_RefreshActionPerformed
        DailySalesTable();
        DailyTableColor();
        sumup();
    }//GEN-LAST:event_txt_RefreshActionPerformed

    private void txt_SearchFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txt_SearchFocusGained
        txt_Search.setText("");
    }//GEN-LAST:event_txt_SearchFocusGained

    private void txt_SearchFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txt_SearchFocusLost
        txt_Search.setText("Search...");
    }//GEN-LAST:event_txt_SearchFocusLost

    private void previewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_previewActionPerformed
        preview();
        PrintPreview.btnPrintDailyAdmin.setVisible(false);
        PrintPreview.btnPrintFinish.setVisible(false);
        PrintPreview.btnPrintAdminAll.setVisible(false);
    }//GEN-LAST:event_previewActionPerformed

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
            java.util.logging.Logger.getLogger(SalesForDay.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(SalesForDay.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(SalesForDay.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(SalesForDay.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new SalesForDay().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    public static javax.swing.JTable DailySalesTable;
    public static javax.swing.JTextField Total_Daily;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JLabel lblDate;
    private javax.swing.JLabel lblTime;
    public static javax.swing.JLabel lbl_Timer;
    private javax.swing.JTabbedPane panePreview;
    private javax.swing.JButton preview;
    private javax.swing.JButton txt_Refresh;
    private javax.swing.JTextField txt_Search;
    // End of variables declaration//GEN-END:variables
}
