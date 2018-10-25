package Westpoint;

import static Westpoint.ReceipForm.ReceiptTextPane;
import Intel.connectDB;
import java.awt.Image;
import java.awt.print.PageFormat;
import java.awt.print.Paper;
import java.awt.print.PrinterJob;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import javax.imageio.ImageIO;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.text.BadLocationException;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;

/**
 *
 * @author Ink
 */
public class PrintPreview extends javax.swing.JFrame {

    Connection conn = null;
    ResultSet rs = null;
    PreparedStatement pst = null;

    public PrintPreview() {
        initComponents();

        conn = connectDB.ConnecrDb();

        try {
            Image i = ImageIO.read(getClass().getResource("/image/doubleUU.jpg"));
            setIconImage(i);
        } catch (Exception e) {

        }
    }

//  Daily sales method to print
    public void preview() {
        PrintPreview.setText("");
        Double totalBought = Double.parseDouble(SalesForDay.Total_Daily.getText().toString());
        String time = SalesForDay.lbl_Timer.getText();

        SimpleAttributeSet title = new SimpleAttributeSet();
        title = new SimpleAttributeSet();
        StyleConstants.setBold(title, true);
        StyleConstants.setFontSize(title, 14);
        StyleConstants.setFontFamily(title, "Tahoma");

        SimpleAttributeSet title2 = new SimpleAttributeSet();
        title2 = new SimpleAttributeSet();
        StyleConstants.setBold(title2, true);
        StyleConstants.setFontSize(title2, 12);
        StyleConstants.setFontFamily(title2, "Tahoma");

        SimpleAttributeSet Subtitle = new SimpleAttributeSet();
        Subtitle = new SimpleAttributeSet();
        StyleConstants.setFontSize(Subtitle, 11);
        StyleConstants.setFontFamily(Subtitle, "Tahoma");
        StyleConstants.setBold(Subtitle, true);
        StyleConstants.setUnderline(Subtitle, true);

        SimpleAttributeSet loc = new SimpleAttributeSet();
        loc = new SimpleAttributeSet();
        StyleConstants.setFontSize(loc, 10);
        //StyleConstants.setFontFamily(loc, "Times New Roman");

        SimpleAttributeSet loc1 = new SimpleAttributeSet();
        loc1 = new SimpleAttributeSet();
        StyleConstants.setFontSize(loc1, 10);
        StyleConstants.setFontFamily(loc1, "Tahoma");
        StyleConstants.setBold(loc1, true);

        String spe = (" " + " " + " " + " " + " " + " ");
        String spec = (" " + " " + " " + " " + " " + " " + " " + " " + " " + " ");
        String spec1 = (" " + " " + " " + " " + " " + " " + " " + " ");

        String spec11 = (" " + " " + " " + " " + " " + " " + " " + " ");
        String spec33 = (" " + " " + " " + " " + " " + " " + " " + " " + " " + " ");
        String spec44 = (" " + " " + " " + " " + " " + " ");

        try {
            PrintPreview.getStyledDocument().insertString(PrintPreview.getStyledDocument().getLength(), " " + " " + " " + "DAILY SALES REPORT\n", title);
            PrintPreview.getStyledDocument().insertString(PrintPreview.getStyledDocument().getLength(), "............................................\n", title);
            PrintPreview.getStyledDocument().insertString(PrintPreview.getStyledDocument().getLength(), "Date : \t", loc1);
            PrintPreview.getStyledDocument().insertString(PrintPreview.getStyledDocument().getLength(), time + "\n\n", loc);
            PrintPreview.getStyledDocument().insertString(PrintPreview.getStyledDocument().getLength(), "Item" + "\t" + spe + "Qty " + " " + " " + " " + "Cost" + " " + " " + " " + " " + "Total\n", Subtitle);

            int row = SalesForDay.DailySalesTable.getRowCount(); //column = DailySalesTable.getColumnCount();
            for (int j = 0; j < row; j++) {

                String qty = SalesForDay.DailySalesTable.getValueAt(j, 1).toString();
                String cost = SalesForDay.DailySalesTable.getValueAt(j, 3).toString();

                String spec2 = null;
                if (qty.length() > 1) {
                    spec2 = spec1;
                } else {
                    spec2 = spec;
                }

                String spec22 = null;
                if (cost.length() < 4) {
                    spec22 = spec33;
                } else if (cost.length() == 4) {
                    spec22 = spec11;
                } else {
                    spec22 = spec44;
                }

                PrintPreview.getStyledDocument().insertString(PrintPreview.getStyledDocument().getLength(), SalesForDay.DailySalesTable.getValueAt(j, 0).toString() + "\t" + spe + SalesForDay.DailySalesTable.getValueAt(j, 1).toString() + spec2 + SalesForDay.DailySalesTable.getValueAt(j, 3).toString() + spec22 + SalesForDay.DailySalesTable.getValueAt(j, 2).toString(), loc);

                PrintPreview.getStyledDocument().insertString(PrintPreview.getStyledDocument().getLength(), "\n", loc);
            }

            PrintPreview.getStyledDocument().insertString(PrintPreview.getStyledDocument().getLength(), "....................................................................\n", loc);
            PrintPreview.getStyledDocument().insertString(PrintPreview.getStyledDocument().getLength(), "Total Sales Ghc : \t", title2);
            PrintPreview.getStyledDocument().insertString(PrintPreview.getStyledDocument().getLength(), totalBought + "\n", title2);

        } catch (BadLocationException e) {
            e.printStackTrace();
        }

    }

//  Daily sales preview from Admin Method
    public void printPreview() {
        PrintPreview.setText("");
        Double totalBought = Double.parseDouble(AdminPanel.txt_TotalSales.getText().toString());
        String time = AdminPanel.lbl_Date.getText();

        SimpleAttributeSet title = new SimpleAttributeSet();
        title = new SimpleAttributeSet();
        StyleConstants.setBold(title, true);
        StyleConstants.setFontSize(title, 14);
        StyleConstants.setFontFamily(title, "Tahoma");

        SimpleAttributeSet title2 = new SimpleAttributeSet();
        title2 = new SimpleAttributeSet();
        StyleConstants.setBold(title2, true);
        StyleConstants.setFontSize(title2, 14);
        StyleConstants.setFontFamily(title2, "Tahoma");

        SimpleAttributeSet Subtitle = new SimpleAttributeSet();
        Subtitle = new SimpleAttributeSet();
        StyleConstants.setFontSize(Subtitle, 11);
        StyleConstants.setFontFamily(Subtitle, "Tahoma");
        StyleConstants.setBold(Subtitle, true);
        StyleConstants.setUnderline(Subtitle, true);

        SimpleAttributeSet loc = new SimpleAttributeSet();
        loc = new SimpleAttributeSet();
        StyleConstants.setFontSize(loc, 10);
        //StyleConstants.setFontFamily(loc, "Times New Roman");

        SimpleAttributeSet loc1 = new SimpleAttributeSet();
        loc1 = new SimpleAttributeSet();
        StyleConstants.setFontSize(loc1, 10);
        StyleConstants.setFontFamily(loc1, "Tahoma");
        StyleConstants.setBold(loc1, true);

        String spe = (" " + " " + " " + " " + " " + " " + " " + " ");
        String spec = (" " + " " + " " + " " + " " + " " + " " + " " + " " + " ");
        String spec1 = (" " + " " + " " + " " + " " + " " + " " + " ");

        String spec11 = (" " + " " + " " + " " + " " + " " + " " + " ");
        String spec33 = (" " + " " + " " + " " + " " + " " + " " + " " + " " + " ");
        String spec44 = (" " + " " + " " + " " + " " + " ");

        try {
            PrintPreview.getStyledDocument().insertString(PrintPreview.getStyledDocument().getLength(), " " + "WESTPOINT COSMETICS\n", title);
            PrintPreview.getStyledDocument().insertString(PrintPreview.getStyledDocument().getLength(), "................................................\n", title);
            PrintPreview.getStyledDocument().insertString(PrintPreview.getStyledDocument().getLength(), "Date : \t", loc1);
            PrintPreview.getStyledDocument().insertString(PrintPreview.getStyledDocument().getLength(), time + "\n\n", loc);
            PrintPreview.getStyledDocument().insertString(PrintPreview.getStyledDocument().getLength(), "Item" + "\t" + spe + "Qty" + " " + " " + " " + " " + " " + "Cost" + " " + " " + " " + " " + " " + "Total\n", Subtitle);

            int row = AdminPanel.DailySalesTable.getRowCount(); //column = DailySalesTable.getColumnCount();
            for (int j = 0; j < row; j++) {

                String qty = AdminPanel.DailySalesTable.getValueAt(j, 1).toString();
                String cost = AdminPanel.DailySalesTable.getValueAt(j, 3).toString();

                String spec2 = null;
                if (qty.length() > 1) {
                    spec2 = spec1;
                } else {
                    spec2 = spec;
                }

                String spec22 = null;
                if (cost.length() < 4) {
                    spec22 = spec33;
                } else if (cost.length() == 4) {
                    spec22 = spec11;
                } else {
                    spec22 = spec44;
                }

                PrintPreview.getStyledDocument().insertString(PrintPreview.getStyledDocument().getLength(), AdminPanel.DailySalesTable.getValueAt(j, 0).toString() + "\t" + spe + AdminPanel.DailySalesTable.getValueAt(j, 1).toString() + spec2 + AdminPanel.DailySalesTable.getValueAt(j, 2).toString() + spec22 + AdminPanel.DailySalesTable.getValueAt(j, 3).toString(), loc);

                PrintPreview.getStyledDocument().insertString(PrintPreview.getStyledDocument().getLength(), "\n", loc);
            }

            PrintPreview.getStyledDocument().insertString(PrintPreview.getStyledDocument().getLength(), ".......................................................................\n", loc);
            PrintPreview.getStyledDocument().insertString(PrintPreview.getStyledDocument().getLength(), "Total Sales Ghc : \t", title2);
            PrintPreview.getStyledDocument().insertString(PrintPreview.getStyledDocument().getLength(), totalBought + "\n", title2);

        } catch (BadLocationException e) {
            e.printStackTrace();
        }
    }

    public void previewFinish() {
        PrintPreview.setText("");

        String time = MainUserPanel.lbl_Time1.getText();

        SimpleAttributeSet title = new SimpleAttributeSet();
        title = new SimpleAttributeSet();
        StyleConstants.setBold(title, true);
        StyleConstants.setFontSize(title, 15);
        StyleConstants.setFontFamily(title, "Tahoma");

        SimpleAttributeSet Subtitle = new SimpleAttributeSet();
        Subtitle = new SimpleAttributeSet();
        StyleConstants.setFontSize(Subtitle, 12);
        StyleConstants.setFontFamily(Subtitle, "Tahoma");
        StyleConstants.setBold(Subtitle, true);
        StyleConstants.setUnderline(Subtitle, true);

        SimpleAttributeSet loc = new SimpleAttributeSet();
        loc = new SimpleAttributeSet();
        StyleConstants.setFontSize(loc, 10);
        //StyleConstants.setFontFamily(loc, "Times New Roman");

        SimpleAttributeSet loc1 = new SimpleAttributeSet();
        loc1 = new SimpleAttributeSet();
        StyleConstants.setFontSize(loc1, 12);
        StyleConstants.setFontFamily(loc1, "Tahoma");
        StyleConstants.setBold(loc1, true);

        try {
            String spe;
            String spec = ("\t\t");
            String spec1 = ("\t");

            PrintPreview.getStyledDocument().insertString(PrintPreview.getStyledDocument().getLength(), "WESTPOINT FINISH ITEMS\n", title);
            PrintPreview.getStyledDocument().insertString(PrintPreview.getStyledDocument().getLength(), "........................................\n", title);
            PrintPreview.getStyledDocument().insertString(PrintPreview.getStyledDocument().getLength(), "Date : \t", loc1);
            PrintPreview.getStyledDocument().insertString(PrintPreview.getStyledDocument().getLength(), time + "\n\n", loc);

            PrintPreview.getStyledDocument().insertString(PrintPreview.getStyledDocument().getLength(), "Item" + "\t\t" + " " + " " + " " + " " + " " + " " + "Qty\n", Subtitle);

            int row = FinishingItemPane.finishTable.getRowCount(); //column = DailySalesTable.getColumnCount();
            for (int j = 0; j < row; j++) {
                
                
                String qty = FinishingItemPane.finishTable.getValueAt(j, 0).toString();
                String cost = FinishingItemPane.finishTable.getValueAt(j, 1).toString();

                if (qty.length() > 13) {
                    spe = spec1;
                } else {
                    spe = spec;
                }

                PrintPreview.getStyledDocument().insertString(PrintPreview.getStyledDocument().getLength(), qty + spe + " " + " " + " " + " " + " " + " " + " " + " " + " " + cost, loc);

                PrintPreview.getStyledDocument().insertString(PrintPreview.getStyledDocument().getLength(), "\n", loc);
            }

        } catch (BadLocationException e) {
            e.printStackTrace();
        }
    }

    public void printPreviewAll() {
        PrintPreview.setText("");
        Double totalBought = Double.parseDouble(AdminPanel.txt_TotalSales1.getText().toString());
        String time = AdminPanel.lbl_Date.getText();

        SimpleAttributeSet title = new SimpleAttributeSet();
        title = new SimpleAttributeSet();
        StyleConstants.setBold(title, true);
        StyleConstants.setFontSize(title, 15);
        StyleConstants.setFontFamily(title, "Tahoma");

        SimpleAttributeSet title2 = new SimpleAttributeSet();
        title2 = new SimpleAttributeSet();
        StyleConstants.setBold(title2, true);
        StyleConstants.setFontSize(title2, 12);
        StyleConstants.setFontFamily(title2, "Tahoma");

        SimpleAttributeSet Subtitle = new SimpleAttributeSet();
        Subtitle = new SimpleAttributeSet();
        StyleConstants.setFontSize(Subtitle, 11);
        StyleConstants.setFontFamily(Subtitle, "Tahoma");
        StyleConstants.setBold(Subtitle, true);
        StyleConstants.setUnderline(Subtitle, true);

        SimpleAttributeSet loc = new SimpleAttributeSet();
        loc = new SimpleAttributeSet();
        StyleConstants.setFontSize(loc, 10);
        //StyleConstants.setFontFamily(loc, "Times New Roman");

        SimpleAttributeSet loc1 = new SimpleAttributeSet();
        loc1 = new SimpleAttributeSet();
        StyleConstants.setFontSize(loc1, 10);
        StyleConstants.setFontFamily(loc1, "Tahoma");
        StyleConstants.setBold(loc1, true);

        String spe = (" " + " " + " ");

        try {
            PrintPreview.getStyledDocument().insertString(PrintPreview.getStyledDocument().getLength(), " " + " " + "All SALES REPORT\n", title);
            PrintPreview.getStyledDocument().insertString(PrintPreview.getStyledDocument().getLength(), "........................................\n", title);
            PrintPreview.getStyledDocument().insertString(PrintPreview.getStyledDocument().getLength(), "Date : \t", loc1);
            PrintPreview.getStyledDocument().insertString(PrintPreview.getStyledDocument().getLength(), time + "\n\n", loc);

            PrintPreview.getStyledDocument().insertString(PrintPreview.getStyledDocument().getLength(), "Item" + "\t" + spe + "Qty" + " " + " " + " " + "Cst" + " " + " " + " " + " " + " " + "Total\n", Subtitle);

            int row = AdminPanel.AllDaysTable.getRowCount(); //column = DailySalesTable.getColumnCount();
            for (int j = 0; j < row; j++) {

                String item = AdminPanel.AllDaysTable.getValueAt(j, 0).toString();
                String qty = AdminPanel.AllDaysTable.getValueAt(j, 1).toString();
                String cost = AdminPanel.AllDaysTable.getValueAt(j, 3).toString();

                String spec2 = null;
                String items = null;

                if (item.length() >= 13) {
                    items = item.substring(0, 13);
                } else {
                    items = item;
                }

                if (qty.length() == 1) {
                    spec2 = " " + " " + " " + " " + " " + " " + " " + " ";
                } else if (qty.length() == 2) {
                    spec2 = " " + " " + " " + " " + " " + " " + " ";
                } else if (qty.length() >= 3) {
                    spec2 = " " + " " + " " + " " + " ";
                }

                String spec22 = null;
                if (cost.length() == 3) {
                    spec22 = " " + " " + " " + " " + " " + " " + " ";
                } else if (cost.length() == 4) {
                    spec22 = " " + " " + " " + " " + " " + " ";
                } else if (cost.length() >= 5) {
                    spec22 = " " + " " + " " + " " + " ";
                }

                PrintPreview.getStyledDocument().insertString(PrintPreview.getStyledDocument().getLength(), items + "\t" + spe + AdminPanel.AllDaysTable.getValueAt(j, 1).toString() + spec2 + AdminPanel.AllDaysTable.getValueAt(j, 2).toString() + spec22 + AdminPanel.AllDaysTable.getValueAt(j, 3).toString(), loc);

                PrintPreview.getStyledDocument().insertString(PrintPreview.getStyledDocument().getLength(), "\n", loc);
            }

            PrintPreview.getStyledDocument().insertString(PrintPreview.getStyledDocument().getLength(), "......................................................................\n", loc);
            PrintPreview.getStyledDocument().insertString(PrintPreview.getStyledDocument().getLength(), "Total Sales Ghc : " + " " + " " + " " + " " + " " + " ", title2);
            PrintPreview.getStyledDocument().insertString(PrintPreview.getStyledDocument().getLength(), totalBought + "\n", title2);

        } catch (BadLocationException e) {
            e.printStackTrace();
        }
    }

    public void printDaily() {
        try {
            int rows = SalesForDay.DailySalesTable.getRowCount();
            PrinterJob printerJob = PrinterJob.getPrinterJob();
            PageFormat pageFormat = printerJob.defaultPage();
            Paper paper = new Paper();
            paper.setSize(600, (double) (paper.getHeight() + rows * 2));
            paper.setImageableArea(rows, rows, paper.getWidth() - rows * 20, paper.getHeight() - rows * 1);
            pageFormat.setPaper(paper);
            pageFormat.setOrientation(PageFormat.PORTRAIT);
            printerJob.setPrintable(PrintPreview.getPrintable(null, null), pageFormat);
            printerJob.print();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void printDailyAdmin() {
        try {
            int rows = AdminPanel.DailySalesTable.getRowCount();
            PrinterJob printerJob = PrinterJob.getPrinterJob();
            PageFormat pageFormat = printerJob.defaultPage();
            Paper paper = new Paper();
            paper.setSize(600, (double) (paper.getHeight() + rows * 2));
            paper.setImageableArea(rows, rows, paper.getWidth() - rows * 20, paper.getHeight() - rows * 1);
            pageFormat.setPaper(paper);
            pageFormat.setOrientation(PageFormat.PORTRAIT);
            printerJob.setPrintable(PrintPreview.getPrintable(null, null), pageFormat);
            printerJob.print();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void printDailyFinish() {
        try {
            int rows = FinishingItemPane.finishTable.getRowCount();
            PrinterJob printerJob = PrinterJob.getPrinterJob();
            PageFormat pageFormat = printerJob.defaultPage();
            Paper paper = new Paper();
            paper.setSize(600, (double) (paper.getHeight() + rows * 2));
            paper.setImageableArea(rows, rows, paper.getWidth() - rows * 20, paper.getHeight() - rows * 1);
            pageFormat.setPaper(paper);
            pageFormat.setOrientation(PageFormat.PORTRAIT);
            printerJob.setPrintable(PrintPreview.getPrintable(null, null), pageFormat);
            printerJob.print();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public void printAdminAll() {
        try {
            int rows = AdminPanel.AllDaysTable.getRowCount();
            PrinterJob printerJob = PrinterJob.getPrinterJob();
            PageFormat pageFormat = printerJob.defaultPage();
            Paper paper = new Paper();
            paper.setSize(600, (double) (paper.getHeight() + rows * 2));
            paper.setImageableArea(rows, rows, paper.getWidth() - rows * 20, paper.getHeight() - rows * 1);
            pageFormat.setPaper(paper);
            pageFormat.setOrientation(PageFormat.PORTRAIT);
            printerJob.setPrintable(PrintPreview.getPrintable(null, null), pageFormat);
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

        jScrollPane1 = new javax.swing.JScrollPane();
        PrintPreview = new javax.swing.JTextPane();
        btnPrintDaily = new javax.swing.JButton();
        btnPrintDailyAdmin = new javax.swing.JButton();
        btnPrintFinish = new javax.swing.JButton();
        btnPrintAdminAll = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("PRINTING PREVIEW");
        setResizable(false);

        PrintPreview.setEditable(false);
        jScrollPane1.setViewportView(PrintPreview);

        btnPrintDaily.setIcon(new javax.swing.ImageIcon(getClass().getResource("/image/print2.png"))); // NOI18N
        btnPrintDaily.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnPrintDailyActionPerformed(evt);
            }
        });

        btnPrintDailyAdmin.setIcon(new javax.swing.ImageIcon(getClass().getResource("/image/printAd.png"))); // NOI18N
        btnPrintDailyAdmin.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnPrintDailyAdminActionPerformed(evt);
            }
        });

        btnPrintFinish.setIcon(new javax.swing.ImageIcon(getClass().getResource("/image/printFin.png"))); // NOI18N
        btnPrintFinish.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnPrintFinishActionPerformed(evt);
            }
        });

        btnPrintAdminAll.setIcon(new javax.swing.ImageIcon(getClass().getResource("/image/printFin.png"))); // NOI18N
        btnPrintAdminAll.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnPrintAdminAllActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1)
            .addGroup(layout.createSequentialGroup()
                .addComponent(btnPrintAdminAll)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(btnPrintFinish)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnPrintDailyAdmin, javax.swing.GroupLayout.PREFERRED_SIZE, 56, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnPrintDaily)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 346, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(btnPrintFinish, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btnPrintDailyAdmin, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btnPrintDaily, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnPrintAdminAll, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(0, 0, 0))
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void btnPrintDailyActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPrintDailyActionPerformed
        printDaily();
    }//GEN-LAST:event_btnPrintDailyActionPerformed

    private void btnPrintDailyAdminActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPrintDailyAdminActionPerformed
        printDailyAdmin();
    }//GEN-LAST:event_btnPrintDailyAdminActionPerformed

    private void btnPrintFinishActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPrintFinishActionPerformed
        printDailyFinish();
    }//GEN-LAST:event_btnPrintFinishActionPerformed

    private void btnPrintAdminAllActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPrintAdminAllActionPerformed
        printAdminAll();
    }//GEN-LAST:event_btnPrintAdminAllActionPerformed

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
            java.util.logging.Logger.getLogger(PrintPreview.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(PrintPreview.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(PrintPreview.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(PrintPreview.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        UIManager.setLookAndFeel("com.jtattoo.plaf.aluminium.AluminiumLookAndFeel");
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new PrintPreview().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTextPane PrintPreview;
    public static javax.swing.JButton btnPrintAdminAll;
    public static javax.swing.JButton btnPrintDaily;
    public static javax.swing.JButton btnPrintDailyAdmin;
    public static javax.swing.JButton btnPrintFinish;
    private javax.swing.JScrollPane jScrollPane1;
    // End of variables declaration//GEN-END:variables
}
