package Westpoint;

import Intel.connectDB;
import Westpoint.Interface;
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
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.text.MessageFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.Timer;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.text.BadLocationException;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import net.proteanit.sql.DbUtils;

/**
 *
 * @author NyarkoPC
 */
public class AdminPanel extends javax.swing.JFrame {

    Connection conn = null;
    ResultSet rs = null;
    PreparedStatement pst = null;

    Vector originalTableModel2;
    Vector DailySalesItem;
    Vector AllDays;
    Vector originalTableModelKeep;
    Vector OrderTableModel;

    //Converting to three decimal places
    DecimalFormat df2 = new DecimalFormat("####.###");

    public AdminPanel() {
        initComponents();
        //when you want windows to be display in full screen
//        this.setExtendedState(JFrame.MAXIMIZED_BOTH);
        conn = connectDB.ConnecrDb();
        //This try and catch block replace the java icon with custom one
        try {
            Image i = ImageIO.read(getClass().getResource("/image/doubleUU.jpg"));
            setIconImage(i);
        } catch (Exception e) {

        }

        //Calling methods 
        StartTim();     //Time and Date method Calling
        AllItemsTable(); //All Items DB method Calling
        totalItems(); //Total items in the database count
        Quantity_Left(); // Summing Up total number of Items left
        Quantity_Total(); //Total Quantity Saved into database
        TTSellingPrice(); //Total Selling price of items in database
        DailySalesTable();//Daily Sales method call

        sumupDaily();     //Calling method that sumup daily sales
        AllDaysSalesTable();
        sumupAllDaySales();
        Records_table();
        Order_table();
        sumupAllDayQuantity();
       
        FillCombo(); // fill category combobox

        originalTableModel2 = (Vector) ((DefaultTableModel) ItemsTable.getModel()).getDataVector().clone();
        DailySalesItem = (Vector) ((DefaultTableModel) DailySalesTable.getModel()).getDataVector().clone();
        AllDays = (Vector) ((DefaultTableModel) AllDaysTable.getModel()).getDataVector().clone();
        originalTableModelKeep = (Vector) ((DefaultTableModel) table_Arrears.getModel()).getDataVector().clone();
        OrderTableModel = (Vector) ((DefaultTableModel) Order_Table.getModel()).getDataVector().clone();

        TableColor1();
        ViewDailySaleTableColor();
        AllDaysSalesTableColor();
        Order_Colors(); //Order Color Method Call
        Arrears_Colors(); //Arrears Table Color

        accountTable();
        countOrder();

        lbl_pay.setEnabled(false);
        txt_Paid.setEnabled(false);
        lbl_Remain.setEnabled(false);
        txt_Remain.setEnabled(false);

        lbl_Timer.setVisible(false);

        txt_Left.setText("0");
        lblID.setVisible(false);
        id.setVisible(false);
    }

    //All Item DB populate 
    public void AllItemsTable() {

        DefaultTableModel model = (DefaultTableModel) ItemsTable.getModel();
        model.setRowCount(0);

        Object[] object;

        try {
            String sql = "select Items,wholesale,retail,Total_Qty,Qty_Left,Date,Time  from AllItems_Db order by Items ASC";
            pst = conn.prepareStatement(sql);
            rs = pst.executeQuery();

            while (rs.next()) {
                String add1 = rs.getString("Items");
                String add2 = rs.getString("wholesale");
                String add3 = rs.getString("retail");
                String add4 = rs.getString("Total_Qty");
                String add5 = rs.getString("Qty_Left");
                String add6 = rs.getString("Date");
                String add7 = rs.getString("Time");

                object = new Object[]{add1, add2, add3, add4, add5, add6, add7};

                model.addRow(object);
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
        } finally {
            try {
                rs.close();
                pst.close();
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, e);
            }
        }
        totalItems();
        TTSellingPrice();
        Quantity_Left();
        Quantity_Total();
    }

    //Daily Sales Table Populate 
    private void DailySalesTable() {
        String dateC = lbl_Date.getText().toLowerCase();

        DefaultTableModel model = (DefaultTableModel) DailySalesTable.getModel();
        model.setRowCount(0);

        Object[] object;
        try {
            String sql = "select Item,Qty,Cost,Tot_Cost,Date,Time,Sold_by from DailySales_DB where Date='" + dateC + "' order by Date DESC";
            pst = conn.prepareStatement(sql);
            rs = pst.executeQuery();

            while (rs.next()) {
                String add1 = rs.getString("Item");
                String add2 = rs.getString("Qty");
                String add3 = rs.getString("Cost");
                String add4 = rs.getString("Tot_Cost");
                String add5 = rs.getString("Date");
                String add6 = rs.getString("Time");
                String add7 = rs.getString("Sold_by");

                object = new Object[]{add1, add2, add3, add4, add5, add6, add7};

                model.addRow(object);
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

    //method populate data into all day sale table
    private void AllDaysSalesTable() {

        DefaultTableModel model = (DefaultTableModel) AllDaysTable.getModel();
        model.setRowCount(0);

        Object[] object;
        try {
            String sql = "select Item,Cost,Qty,Tot_Cost,Date,Time,Sold_by from Permanent_DB order by Date DESC";
            pst = conn.prepareStatement(sql);
            rs = pst.executeQuery();
            while (rs.next()) {
                String add1 = rs.getString("Item");
                String add2 = rs.getString("Qty");
                String add3 = rs.getString("Cost");
                String add4 = rs.getString("Tot_Cost");
                String add5 = rs.getString("Date");
                String add6 = rs.getString("Time");
                String add7 = rs.getString("Sold_by");

                object = new Object[]{add1, add2, add3, add4, add5, add6, add7};

                model.addRow(object);
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

    //Method populate data into record table
    private void Records_table() {
        DefaultTableModel model = (DefaultTableModel) table_Arrears.getModel();
        model.setRowCount(0);

        Object[] object;

        try {
            String sql = "select id,Name,Item,Amount,Date,Status from Records_DB order by Amount DESC";
            pst = conn.prepareStatement(sql);
            rs = pst.executeQuery();

            while (rs.next()) {
                String add1 = rs.getString("id");
                String add2 = rs.getString("Name");
                String add3 = rs.getString("Item");
                String add4 = rs.getString("Amount");
                String add5 = rs.getString("Date");
                String add6 = rs.getString("Status");

                object = new Object[]{add1, add2, add3, add4, add5, add6};
                model.addRow(object);
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
        } finally {
            try {
                rs.close();
                pst.close();
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, e);
            }
        }
    }

    //Method populate data into account table
    private void accountTable() {

        DefaultTableModel model = (DefaultTableModel) loginTable.getModel();
        model.setRowCount(0);

        Object[] object;

        try {
            String sql = "select fullname,username,status from accounts order by fullname DESC";
            pst = conn.prepareStatement(sql);
            rs = pst.executeQuery();

            while (rs.next()) {
                String add1 = rs.getString("fullname");
                String add2 = rs.getString("username");
                String add3 = rs.getString("status");

                object = new Object[]{add1, add2, add3};
                model.addRow(object);
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
        } finally {
            try {
                rs.close();
                pst.close();
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, e);
            }
        }
    }

    //Method populate data into order table
    private void Order_table() {
        DefaultTableModel model = (DefaultTableModel) Order_Table.getModel();
        model.setRowCount(0);

        Object[] object;

        try {
            String sql = "select id,Item_Type,Cost,Date from Order_DB order by id ASC";
            pst = conn.prepareStatement(sql);
            rs = pst.executeQuery();
            while (rs.next()) {
                String add1 = rs.getString("id");
                String add3 = rs.getString("Item_Type");
                String add4 = rs.getString("Cost");
                String add5 = rs.getString("Date");

                object = new Object[]{add1,add3, add4, add5};
                model.addRow(object);
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
        } finally {
            try {
                rs.close();
                pst.close();
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, e);
            }
        }
    }

    // Method that user name to combo box
    public void FillCombo() {
        try {
            String sql = "select name from Category_DB";
            pst = conn.prepareStatement(sql);
            rs = pst.executeQuery();

            DefaultComboBoxModel cmodel = (DefaultComboBoxModel) txt_Category.getModel();
            cmodel.removeAllElements();
            cmodel.addElement("Select Category");

            while (rs.next()) {
                String name = rs.getString("name");
                cmodel.addElement(name);
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
        } finally {
            try {
                rs.close();
                pst.close();
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, e);
            }
        }
    }

    //Time and date method
    public void StartTim() {
        Timer t = new Timer(1000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss a");
                SimpleDateFormat sdf2 = new SimpleDateFormat("MMM EE d, yyy");
                lbl_Date.setText(sdf2.format(new java.util.Date()));
                lbl_Time.setText(sdf.format(new java.util.Date()));
                lbl_Timer.setText(sdf2.format(new java.util.Date()));
            }
        });
        t.start();
    }

    //Method that calculate total
    private void totalItems() {
        try {
            String sql = "Select count(Items) from AllItems_DB";
            pst = conn.prepareStatement(sql);
            rs = pst.executeQuery();
            if (rs.next()) {
                String sum = rs.getString("count(Items)");
                txt_Tatal.setText(sum);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
        } finally {
            try {
                rs.close();
                pst.close();
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, e);
            }
        }
    }

    //Method that calculate the total quantity
    private void Quantity_Left() {
        try {
            String sql = "Select sum(Qty_left) from AllItems_DB";
            pst = conn.prepareStatement(sql);
            rs = pst.executeQuery();
            if (rs.next()) {
                String sum = rs.getString("sum(Qty_left)");
                tt_left.setText(sum);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
        } finally {
            try {
                rs.close();
                pst.close();
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, e);
            }
        }
    }

    //Total quantity
    private void Quantity_Total() {
        try {
            String sql = "Select sum(Total_Qty) from AllItems_DB";
            pst = conn.prepareStatement(sql);
            rs = pst.executeQuery();
            if (rs.next()) {
                String sum = rs.getString("sum(Total_Qty)");
                tt_item.setText(sum);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
        } finally {
            try {
                rs.close();
                pst.close();
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, e);
            }
        }
    }

    //Method calclate total selling price
    private void TTSellingPrice() {
        try {
            String sql = "Select sum(wholesale * Qty_Left) from AllItems_DB";
            pst = conn.prepareStatement(sql);
            rs = pst.executeQuery();
            if (rs.next()) {
                String sum = rs.getString("sum(wholesale * Qty_Left)");
                tt_SP.setText(sum);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
        } finally {
            try {
                rs.close();
                pst.close();
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, e);
            }
        }
    }

    //Searching for item in table method starts here
    public void searchTableContent2(String SearchString) {
        try {
            DefaultTableModel currtableModel = (DefaultTableModel) ItemsTable.getModel();
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
                rs.close();
                pst.close();
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, e);
            }
        }
    }

    public void searchTableContentKeep(String SearchKeep) {
        try {
            DefaultTableModel currtableModelKeep = (DefaultTableModel) table_Arrears.getModel();
            currtableModelKeep.setRowCount(0);
            for (Object rows2 : originalTableModelKeep) {
                Vector rowVector = (Vector) rows2;

                for (Object column : rowVector) {
                    if (column.toString().contains(SearchKeep)) {
                        currtableModelKeep.addRow(rowVector);
                        break;
                    }
                }
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
        } finally {
            try {
                rs.close();
                pst.close();
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, e);
            }
        }
    }

    public void searchTableOrderCustomer(String OrderSearch) {
        try {
            DefaultTableModel currtableModelKeep = (DefaultTableModel) Order_Table.getModel();
            currtableModelKeep.setRowCount(0);

            for (Object rows2 : OrderTableModel) {
                Vector rowVector = (Vector) rows2;

                for (Object column : rowVector) {
                    if (column.toString().contains(OrderSearch)) {
                        currtableModelKeep.addRow(rowVector);
                        break;
                    }
                }
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
        } finally {
            try {
                rs.close();
                pst.close();
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, e);
            }
        }

    }

    public void TableColor1() {
        ItemsTable.getColumnModel().getColumn(0).setCellRenderer(new CustomRenderer());
        ItemsTable.getColumnModel().getColumn(1).setCellRenderer(new CustomRenderer1());
        ItemsTable.getColumnModel().getColumn(2).setCellRenderer(new CustomRenderer2());
        ItemsTable.getColumnModel().getColumn(3).setCellRenderer(new CustomRenderer3());
        ItemsTable.getColumnModel().getColumn(4).setCellRenderer(new CustomRenderer4());
        ItemsTable.getColumnModel().getColumn(5).setCellRenderer(new CustomRenderer5());
        ItemsTable.getTableHeader().setDefaultRenderer(new HeaderColor());
    }

    public void ViewDailySaleTableColor() {
        DailySalesTable.getColumnModel().getColumn(0).setCellRenderer(new CustomRenderer());
        DailySalesTable.getColumnModel().getColumn(1).setCellRenderer(new CustomRenderer1());
        DailySalesTable.getColumnModel().getColumn(2).setCellRenderer(new CustomRenderer2());
        DailySalesTable.getColumnModel().getColumn(3).setCellRenderer(new CustomRenderer3());
        DailySalesTable.getColumnModel().getColumn(4).setCellRenderer(new CustomRenderer4());
        DailySalesTable.getColumnModel().getColumn(5).setCellRenderer(new CustomRenderer5());
        DailySalesTable.getColumnModel().getColumn(6).setCellRenderer(new CustomRenderer6());
        DailySalesTable.getTableHeader().setDefaultRenderer(new HeaderColor());
    }

    public void AllDaysSalesTableColor() {
        AllDaysTable.getColumnModel().getColumn(0).setCellRenderer(new CustomRenderer());
        AllDaysTable.getColumnModel().getColumn(1).setCellRenderer(new CustomRenderer1());
        AllDaysTable.getColumnModel().getColumn(2).setCellRenderer(new CustomRenderer2());
        AllDaysTable.getColumnModel().getColumn(3).setCellRenderer(new CustomRenderer3());
        AllDaysTable.getColumnModel().getColumn(4).setCellRenderer(new CustomRenderer4());
        AllDaysTable.getColumnModel().getColumn(5).setCellRenderer(new CustomRenderer5());
        AllDaysTable.getColumnModel().getColumn(6).setCellRenderer(new CustomRenderer6());
        AllDaysTable.getTableHeader().setDefaultRenderer(new HeaderColor());
    }

    public void Order_Colors() {
        Order_Table.getColumnModel().getColumn(0).setCellRenderer(new CustomRenderer());
        Order_Table.getColumnModel().getColumn(1).setCellRenderer(new CustomRenderer1());
        Order_Table.getColumnModel().getColumn(2).setCellRenderer(new CustomRenderer2());
        Order_Table.getColumnModel().getColumn(3).setCellRenderer(new CustomRenderer3());
        //Order_Table.getColumnModel().getColumn(4).setCellRenderer(new CustomRenderer4());
        Order_Table.getTableHeader().setDefaultRenderer(new HeaderColor());
    }

    public void Arrears_Colors() {
        table_Arrears.getColumnModel().getColumn(0).setCellRenderer(new CustomRenderer());
        table_Arrears.getColumnModel().getColumn(1).setCellRenderer(new CustomRenderer1());
        table_Arrears.getColumnModel().getColumn(2).setCellRenderer(new CustomRenderer2());
        table_Arrears.getColumnModel().getColumn(3).setCellRenderer(new CustomRenderer3());
        table_Arrears.getColumnModel().getColumn(4).setCellRenderer(new CustomRenderer4());
        table_Arrears.getColumnModel().getColumn(5).setCellRenderer(new CustomRenderer5());
        table_Arrears.getTableHeader().setDefaultRenderer(new HeaderColor());
    }

    public static class CustomRenderer extends DefaultTableCellRenderer {

        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            Component cellComponent = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
            if (column == 0) {
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
                cellComponent.setForeground(Color.BLUE);
                cellComponent.setFont(new java.awt.Font("Tahoma", 1, 12));
            }
            return cellComponent;
        }
    }

    public static class CustomRenderer5 extends DefaultTableCellRenderer {

        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            Component cellComponent = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
            if (column == 5) {
                cellComponent.setForeground(Color.PINK);
                cellComponent.setFont(new java.awt.Font("Tahoma", 1, 12));
            }
            return cellComponent;
        }
    }

    public static class CustomRenderer6 extends DefaultTableCellRenderer {

        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            Component cellComponent = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
            if (column == 6) {
                cellComponent.setForeground(Color.DARK_GRAY);
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
            cellComponent.setBackground(Color.YELLOW);
            cellComponent.setForeground(Color.BLACK);
            cellComponent.setFont(new java.awt.Font("Tahoma", 1, 15));
            return cellComponent;
        }
    }

    public void searchTableDaily(String SearchString) {
        try {
            DefaultTableModel currtableModel = (DefaultTableModel) DailySalesTable.getModel();
            currtableModel.setRowCount(0);

            for (Object rows2 : DailySalesItem) {
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
                rs.close();
                pst.close();
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, e);
            }
        }
    }

    public void searchAllDaySales(String SearchString) {
        try {
            DefaultTableModel currtableModel = (DefaultTableModel) AllDaysTable.getModel();
            currtableModel.setRowCount(0);

            for (Object rows2 : AllDays) {
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
                rs.close();
                pst.close();
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, e);
            }
        }
    }

    public void sumupDaily() {
        double total = 0;
        int rowcount = DailySalesTable.getRowCount();
        double sum = 0;
        for (int i = 0; i < rowcount; i++) {
            sum = sum + Double.parseDouble(DailySalesTable.getValueAt(i, 3).toString());
        }
        total = Double.valueOf(df2.format(sum));
        txt_TotalSales.setText(Double.toString(total));
    }

    public void sumupAllDaySales() {
        double total = 0;
        int rowcount = AllDaysTable.getRowCount();
        double sum = 0;
        for (int i = 0; i < rowcount; i++) {
            sum = sum + Double.parseDouble(AllDaysTable.getValueAt(i, 3).toString());
        }
        total = Double.valueOf(df2.format(sum));
        txt_TotalSales1.setText(Double.toString(total));
    }
    
    public void sumupAllDayQuantity() {
        int total = 0;
        int rowcount = AllDaysTable.getRowCount();
        int sum = 0;
        for (int i = 0; i < rowcount; i++) {
            sum = sum + Integer.parseInt(AllDaysTable.getValueAt(i, 1).toString());
        }
        total = Integer.valueOf(df2.format(sum));
        quantity_baught.setText(Integer.toString(total));
    }
    
    private void countOrder() {
        try {
            String sql = "Select count(id) from Order_DB ";
            pst = conn.prepareStatement(sql);
            rs = pst.executeQuery();
            if (rs.next()) {
                String sum = rs.getString("count(id)");
               Or_id.setText(sum);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
        } finally {
            try {
                rs.close();
                pst.close();
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, e);
            }
        }
    }

    public void Saved() {
        String Item = txt_ItName.getText();

        if (Item.isEmpty() || txt_TTQuantity.getText() == "" || txt_whoelsale.getText() == "" || txt_Category.getSelectedIndex() == 0) {
            JOptionPane.showMessageDialog(null, "Ooops Something want Wrong, Check All Filled", "WARNING", JOptionPane.ERROR_MESSAGE);
        } else {
            try {
                String sql = "Insert into AllItems_DB (Items,wholesale,retail,Total_Qty,Qty_Left,Date,Time,Category) values (?,?,?,?,?,?,?,?)";
                pst = conn.prepareStatement(sql);

                pst.setString(1, txt_ItName.getText().toLowerCase().trim());
                pst.setString(2, txt_whoelsale.getText().toLowerCase().trim());
                pst.setString(3, txt_retail.getText().toLowerCase().trim());
                pst.setString(4, txt_Left.getText().trim());
                pst.setString(5, txt_Left.getText().trim());
                pst.setString(6, lbl_Date.getText().toLowerCase().trim());
                pst.setString(7, lbl_Time.getText().toLowerCase().trim());
                pst.setInt(8, txt_Category.getSelectedIndex());

                pst.executeUpdate();
                JOptionPane.showMessageDialog(null, "Saved");
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, "Database Items Should Have Unique Identity");
            } finally {
                try {
                    rs.close();
                    pst.close();
                } catch (Exception e) {
                    JOptionPane.showMessageDialog(null, e);
                }
            }
        }
    }

    public void Update() {
        try {
            String Val1 = txt_ItName.getText().toLowerCase().trim();
            String Val2 = txt_whoelsale.getText().trim();
            String Val3 = txt_retail.getText().trim();
            String Val4 = txt_Left.getText().trim();
            String Val5 = txt_Left.getText().trim();
            String Val6 = lbl_Date.getText().toLowerCase();
            String Val7 = lbl_Time.getText().toLowerCase();

            String sql = "update AllItems_DB set Items='" + Val1 + "',wholesale='" + Val2 + "',retail='" + Val3 + "',Total_Qty='" + Val4 + "',Qty_left='" + Val5 + "',Date='" + Val6 + "',Time='" + Val7 + "' where Items='" + Val1 + "'";

            pst = conn.prepareStatement(sql);
            pst.executeUpdate();
            JOptionPane.showMessageDialog(null, "Item Updated");
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

    public void clear() {
        int ask = JOptionPane.showConfirmDialog(null, "Do you want to Delete All Items?", "Delete", JOptionPane.YES_NO_OPTION);
        if (ask == 0) {
            String sql = "delete from AllItems_DB";
            try {
                pst = conn.prepareStatement(sql);
                pst.executeUpdate();
                JOptionPane.showMessageDialog(null, "All Items Deleted");
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
        AllItemsTable();
    }

    public void newItem() {
        txt_ItName.setText(" ");
        txt_TTQuantity.setText(" ");
        txt_whoelsale.setText(" ");
        txt_Left.setText(Integer.toString(0));
    }

    public void deleteSingle() {
       

        int row = ItemsTable.getSelectedRow();
        String dateR = (ItemsTable.getModel().getValueAt(row, 5).toString());
        String timeR = (ItemsTable.getModel().getValueAt(row, 6).toString());

        if (dateR.isEmpty() || timeR.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Select Item to Delete");
        } else {
            
             int ask = JOptionPane.showConfirmDialog(null, "Do you want to Delete this Item", "Delete", JOptionPane.YES_NO_OPTION);

            if (ask == 0) {
                String sql = "delete from AllItems_DB where Date='" + dateR + "' and Time='" + timeR + "'";
                try {
                    pst = conn.prepareStatement(sql);
                    pst.execute();
                    JOptionPane.showMessageDialog(null, "Item is Deleted");
                } catch (Exception e) {
                    JOptionPane.showMessageDialog(null, "Select Item to Delete");
                } finally {
                    try {
                        rs.close();
                        pst.close();
                    } catch (Exception e) {
                    }
                }
            }
            AllItemsTable();
            TableColor1();
        }
    }

    public void itemsTableClick() {

        int row = ItemsTable.getSelectedRow();
        String Table_click = (ItemsTable.getModel().getValueAt(row, 0).toString());

        try {
            String sql = "select * from AllItems_DB where Items='" + Table_click + "'";
            pst = conn.prepareStatement(sql);
            rs = pst.executeQuery();
            if (rs.next()) {
                String add1 = rs.getString("Items");
                txt_ItName.setText(add1);
                String add2 = rs.getString("wholesale");
                txt_whoelsale.setText(add2);
                String add3 = rs.getString("retail");
                txt_retail.setText(add3);
                String add4 = rs.getString("Total_Qty");
                txt_TTQuantity.setText(add4);
                String add5 = rs.getString("Qty_left");
                txt_Left.setText(add5);
                String add6 = rs.getString("Date");
                lbl_Date.setText(add6);
                String add7 = rs.getString("Time");
                lbl_Time.setText(add7);

                int add8 = rs.getInt("Category");
                txt_Category.setSelectedIndex(add8);
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

    public void quantityAdd() {
        try {
            int tot = 0;
            int val1 = Integer.parseInt(txt_TTQuantity.getText().trim());

            if (txt_Left.getText().isEmpty()) {
                txt_Left.setText(Integer.toString(val1).trim());
            } else {
                int val2 = Integer.parseInt(txt_Left.getText().trim());
                tot = val1 + val2;
                txt_Left.setText(Integer.toString(tot));
            }

        } catch (Exception e) {
//            JOptionPane.showMessageDialog(null, e);
        }

    }

    public void tableAllItemsKeyPress(KeyEvent evt) {
        if (evt.getKeyCode() == KeyEvent.VK_DOWN || evt.getKeyCode() == KeyEvent.VK_UP) {
            int row = ItemsTable.getSelectedRow();
            String Table_click = (ItemsTable.getModel().getValueAt(row, 0).toString());

            try {
                String sql = "select * from AllItems_DB where Items='" + Table_click + "'";
                pst = conn.prepareStatement(sql);
                rs = pst.executeQuery();
                if (rs.next()) {
                    String add1 = rs.getString("Items");
                    txt_ItName.setText(add1);
                    String add2 = rs.getString("wholesale");
                    txt_whoelsale.setText(add2);
                    String add3 = rs.getString("retail");
                    txt_retail.setText(add3);
                    String add4 = rs.getString("Total_Qty");
                    txt_TTQuantity.setText(add4);
                    String add5 = rs.getString("Qty_left");
                    txt_Left.setText(add5);
                    String add6 = rs.getString("Date");
                    lbl_Date.setText(add6);
                    String add7 = rs.getString("Time");
                    lbl_Time.setText(add7);
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
    }

    public void claerAllDailySales() {
        int ask = JOptionPane.showConfirmDialog(null, "Do you want to Delete All Records?", "Delete", JOptionPane.YES_NO_OPTION);
        if (ask == 0) {
            String sql = "delete from DailySales_DB";
            try {
                pst = conn.prepareStatement(sql);
                pst.executeUpdate();
                JOptionPane.showMessageDialog(null, "All Records Deleted");

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
        DailySalesTable();
        ViewDailySaleTableColor();
    }

    public void deleteSingleDailySales() {
       

        int row = DailySalesTable.getSelectedRow();
        String dateR = (DailySalesTable.getModel().getValueAt(row, 4).toString());
        String timeR = (DailySalesTable.getModel().getValueAt(row, 5).toString());

        if (dateR.isEmpty() || timeR.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Select Item to Delete");
        } else {
            
            int ask = JOptionPane.showConfirmDialog(null, "Do you want to Delete this Record ?", "Delete", JOptionPane.YES_NO_OPTION);
            if (ask == 0) {
                String sql = "delete from DailySales_DB where Date='" + dateR + "' and Time='" + timeR + "'";
                try {
                    pst = conn.prepareStatement(sql);
                    pst.execute();

                    JOptionPane.showMessageDialog(null, "Deleted");

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
        }
        DailySalesTable();
        ViewDailySaleTableColor();
        sumupDaily();
    }

    public void dailySalesTableClick() {
        int row = DailySalesTable.getSelectedRow();
        String Table_click = (DailySalesTable.getModel().getValueAt(row, 0).toString());

        try {
            String sql = "select * from DailySales_DB where Item='" + Table_click + "'";
            pst = conn.prepareStatement(sql);
            rs = pst.executeQuery();
            if (rs.next()) {
                String add1 = rs.getString("Date");
                lblDate.setText(add1);
                String add2 = rs.getString("Time");
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

    public void dailySalesTableKeyReleased(KeyEvent evt) {
        if (evt.getKeyCode() == KeyEvent.VK_DOWN || evt.getKeyCode() == KeyEvent.VK_UP) {
            int row = DailySalesTable.getSelectedRow();
            String Table_click = (DailySalesTable.getModel().getValueAt(row, 0).toString());

            try {
                String sql = "select * from DailySales_DB where Item='" + Table_click + "'";
                pst = conn.prepareStatement(sql);
                rs = pst.executeQuery();
                if (rs.next()) {
                    String add1 = rs.getString("Date");
                    lblDate.setText(add1);
                    String add2 = rs.getString("Time");
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
    }

    public void searchByDate() {

        String val1 = txt_Val4.getText();
        String val2 = txt_Val3.getText();
        try {
            String sql2 = "select Item,Cost,Qty,Tot_Cost,Date,Time,Sold_by from Permanent_DB where Date between '" + val1 + "' and '" + val2 + "' ";
            pst = conn.prepareStatement(sql2);
            rs = pst.executeQuery();
            AllDaysTable.setModel(DbUtils.resultSetToTableModel(rs));
            sumupAllDaySales();

        } catch (Exception e) {

        } finally {
            try {
                rs.close();
                pst.close();
            } catch (Exception e) {
            }
        }
        AllDaysSalesTableColor();
    }

    public void allDaySalesTableClick() {
        int row = AllDaysTable.getSelectedRow();
        String Table_click = (AllDaysTable.getModel().getValueAt(row, 0).toString());

        try {
            String sql = "select * from Permanent_DB where Item='" + Table_click + "'";
            pst = conn.prepareStatement(sql);
            rs = pst.executeQuery();
            if (rs.next()) {
                String add1 = rs.getString("Date");
                lblDate1.setText(add1);
                String add2 = rs.getString("Time");
                lblTime1.setText(add2);
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

    public void orderTableClick() {
        int row = Order_Table.getSelectedRow();
        String Table_click = (Order_Table.getModel().getValueAt(row, 0).toString());
        try {
            String sql = "select id,Item_Type,Cost,Date from Order_DB where id='" + Table_click + "'";
            pst = conn.prepareStatement(sql);
            rs = pst.executeQuery();

            if (rs.next()) {
                String add1 = rs.getString("id");
                id.setText(add1);
                String add3 = rs.getString("Item_Type");
                Or_type.setText(add3);
                String add4 = rs.getString("Cost");
                Or_Cost.setText(add4);
                //setting date into dat chooser
                String dateValue = rs.getString("Date");
                java.util.Date add5 = new SimpleDateFormat("MMM EE d, yyyy").parse(dateValue);
                Or_Date.setDate(add5);
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

    public void orderUpdate() {
        if (Or_type.getText().isEmpty() || Or_Cost.getText().isEmpty() || Or_Date.equals(null)) {
            JOptionPane.showMessageDialog(null, "Select Data To Update");
        } else {
            try {
                String Val1 = id.getText().toLowerCase().trim();
                String Val2 = "customer";
                String Val3 = Or_type.getText().toLowerCase().trim();
                String Val4 = Or_Cost.getText().toLowerCase().trim();
                String Val5 = ((JTextField) Or_Date.getDateEditor().getUiComponent()).getText().toLowerCase().trim();

                String sql = "update Order_DB set id='" + Val1 + "',Name='" + Val2 + "',Item_Type='" + Val3 + "',Cost='" + Val4 + "',Date='" + Val5 + "' where id='" + Val1 + "'";

                pst = conn.prepareStatement(sql);
                pst.execute();
                JOptionPane.showMessageDialog(null, "Data Updated");
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, e);
            } finally {
                try {
                    rs.close();
                    pst.close();
                } catch (Exception e) {
                }
            }
            Order_table();
            Order_Colors();
        }
    }

    public void orderSave() {
        if (Or_type.getText().isEmpty() || Or_Cost.getText().isEmpty() || Or_Date.equals(null)) {
            JOptionPane.showMessageDialog(null, "Please Enter Details Before Saving");
        } else {
            
                 int id = Integer.parseInt(Or_id.getText());
                 int d = id + 1;
            
            try {
               
                String sql = "Insert into Order_DB (id,Name,Item_Type,Cost,Date) values (?,?,?,?,?)";

                pst = conn.prepareStatement(sql);
                pst.setInt(1, d);
                pst.setString(2, "customer");
                pst.setString(3, Or_type.getText().toLowerCase().trim());
                pst.setString(4, Or_Cost.getText().toLowerCase().trim());
                pst.setString(5, ((JTextField) Or_Date.getDateEditor().getUiComponent()).getText().toLowerCase().trim());
                pst.executeUpdate();

                JOptionPane.showMessageDialog(null, "Saved");
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, "Please Enter Details Before Saving");
            } finally {
                try {
                    rs.close();
                    pst.close();
                } catch (Exception e) {
                }
            }
            Order_table();
            Order_Colors();
        }
    }

    public void deleteCustomerOrder() {
        
        int row = Order_Table.getSelectedRow();
        String id = (Order_Table.getModel().getValueAt(row, 0).toString());
        String date = (Order_Table.getModel().getValueAt(row, 3).toString());
        
        if (Or_id.getText().isEmpty()) {
            JOptionPane.showMessageDialog(null, "Select to Delete");
        } else {
            int ask = JOptionPane.showConfirmDialog(null, "Do you want to Delete this Records?", "Delete", JOptionPane.YES_NO_OPTION);
            if (ask == 0) {
                String sql = "delete from Order_DB where id='"+ id +"' and date='"+ date +"'";
                try {
                    pst = conn.prepareStatement(sql);
                    pst.execute();
                    JOptionPane.showMessageDialog(null, "Deleted");

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
            Order_table();
        }
        Order_Colors();
    }

    public void clearOrder() {
        int ask = JOptionPane.showConfirmDialog(null, "Do you want to Delete All Items Saved?", "Delete", JOptionPane.YES_NO_OPTION);
        if (ask == 0) {
            String sql = "delete from Order_DB";
            try {
                pst = conn.prepareStatement(sql);
                pst.execute();
                JOptionPane.showMessageDialog(null, "Deleted");

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
        Order_table();
        Order_Colors();
    }

    public void newArrearsCustomer() {
        txt_Paid.setText("");
        txt_Remain.setText("");

        txt_id.setText("");
        txt_Name.setText("");
        txt_type.setText("");
        txt_Amount.setText("");
        txt_Date.setCalendar(null);
        buttonGroup1.clearSelection();
        Description.setText("");

        lbl_pay.setEnabled(false);
        txt_Paid.setEnabled(false);
        lbl_Remain.setEnabled(false);
        txt_Remain.setEnabled(false);

        txt_id.setEditable(true);
        txt_Name.setEditable(true);
        txt_type.setEditable(true);
        txt_Amount.setEditable(true);
        txt_Remain.setEditable(true);
    }

    public void saveArrearsCustomer() {
        if (txt_id.getText().isEmpty() || txt_Name.getText().isEmpty() || txt_type.getText().isEmpty() || txt_Amount.getText().isEmpty() || Description.getText().isEmpty() || txt_Date.equals(null) || buttonGroup1.getSelection() == null) {
            JOptionPane.showMessageDialog(null, "Please Enter Details Before Saving");
        } else {
            try {
                String sql = "Insert into Records_DB (id,Name,Item,Amount,Date,Status,Description) values (?,?,?,?,?,?,?)";

                pst = conn.prepareStatement(sql);
                pst.setString(1, txt_id.getText().trim());
                pst.setString(2, txt_Name.getText().toLowerCase().trim());
                pst.setString(3, txt_type.getText().toLowerCase().trim());
                pst.setString(4, txt_Amount.getText().toLowerCase().trim());
                pst.setString(5, ((JTextField) txt_Date.getDateEditor().getUiComponent()).getText().toLowerCase().trim());
                pst.setString(6, status.toLowerCase().trim());
                pst.setString(7, Description.getText().toLowerCase().trim());

                pst.executeUpdate();
                JOptionPane.showMessageDialog(null, "Saved");
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, e);
            } finally {
                try {
                    rs.close();
                    pst.close();
                } catch (Exception e) {
                }
            }
            Records_table();
        }
        Arrears_Colors();
    }

    public void deleteArrearsCustomer() {
        int ask = JOptionPane.showConfirmDialog(null, "Do you want to Delete this Record ?", "Delete", JOptionPane.YES_NO_OPTION);
        if (ask == 0) {
            String sql = "delete from Records_DB where id=?";
            try {
                pst = conn.prepareStatement(sql);
                pst.setString(1, txt_id.getText());
                pst.execute();
                JOptionPane.showMessageDialog(null, "Deleted");

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
        Records_table();
        Arrears_Colors();
    }

    public void subTwoValue() {
        DecimalFormat df2 = new DecimalFormat("####.###");
        try {
            double numba1 = 0, numba2 = 0, total = 0;
            numba1 = Double.parseDouble(txt_Paid.getText().toString());
            numba2 = Double.parseDouble(txt_Amount.getText().toString());
            total = Double.valueOf(df2.format(numba2 - numba1));
            txt_Remain.setText(Double.toString(total));
        } catch (Exception e) {

        } finally {
            try {
                rs.close();
                pst.close();
            } catch (Exception e) {
            }
        }
    }

    public void arrearsClear() {
        int ask = JOptionPane.showConfirmDialog(null, "Do you want to Delete All Records Saved?", "Delete", JOptionPane.YES_NO_OPTION);
        if (ask == 0) {
            String sql = "delete from Records_DB";
            try {
                pst = conn.prepareStatement(sql);
                pst.execute();
                JOptionPane.showMessageDialog(null, "Deleted");

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
        Records_table();
    }

    public void updateArrearsCustomers() {
        if (txt_id.getText().isEmpty() || txt_Name.getText().isEmpty() || txt_type.getText().isEmpty() || txt_Amount.getText().isEmpty() || Description.getText().isEmpty() || txt_Date.equals(null) || buttonGroup1.getSelection() == null) {
            JOptionPane.showMessageDialog(null, "Please Select To Update");
        } else {
            double remain = Double.parseDouble(txt_Remain.getText().toString());
            try {

                String Val1 = txt_id.getText().toLowerCase().trim();
                String Val2 = txt_Name.getText().toLowerCase().trim();
                String Val3 = txt_type.getText().toLowerCase().trim();
                String Val4 = txt_Remain.getText().toLowerCase().trim();
                String Val5 = ((JTextField) txt_Date.getDateEditor().getUiComponent()).getText().toLowerCase().trim();
                if (remain <= 0) {
                    status = "Paid";
                } else {
                    status = "Not Paid";
                }

                String Val6 = status.toLowerCase().trim();
                String Val7 = Description.getText().toLowerCase().trim();

                String sql = "update Records_DB set id='" + Val1 + "',Name='" + Val2 + "',Item='" + Val3 + "',Amount='" + Val4 + "',Date='" + Val5 + "',Status='" + Val6 + "',Description='" + Val7 + "' where id='" + Val1 + "'";

                pst = conn.prepareStatement(sql);
                pst.execute();

                JOptionPane.showMessageDialog(null, "Data Updated");

                lbl_pay.setEnabled(false);
                txt_Paid.setEnabled(false);
                lbl_Remain.setEnabled(false);
                txt_Remain.setEnabled(false);
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, e);
            } finally {
                try {
                    rs.close();
                    pst.close();
                } catch (Exception e) {
                }
            }
            Records_table();
            Arrears_Colors();
        }
    }

    public void arrearsPrint() {
        MessageFormat header = new MessageFormat("WESTPOINT COSMETICS ARREARS CUSTOMERS");
        MessageFormat footer = new MessageFormat("Page 0");
        try {
            table_Arrears.print(JTable.PrintMode.FIT_WIDTH, header, footer);
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

    public void orderPrint() {
        MessageFormat header = new MessageFormat("WESTPOINT COSMETICS CUSTOMER'S ORDER");
        MessageFormat footer = new MessageFormat("Page 0");
        try {

            Order_Table.print(JTable.PrintMode.FIT_WIDTH, header, footer);

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

    public void dailyPrint() {
        double ttotal = Double.parseDouble(txt_TotalSales1.getText().toString());
        MessageFormat header = new MessageFormat("WESTPOINT COSMETICS DAILY SALES");
        MessageFormat footer = new MessageFormat("Total Sales of Day Search = " + ttotal);
        try {

            AllDaysTable.print(JTable.PrintMode.FIT_WIDTH, header, footer);

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

    public void deleteSingleOne() {
        int ask = JOptionPane.showConfirmDialog(null, "Do you want to Delete this Record ?", "Delete", JOptionPane.YES_NO_OPTION);

        int row = AllDaysTable.getSelectedRow();
        String dateR = (AllDaysTable.getModel().getValueAt(row, 4).toString());
        String timeR = (AllDaysTable.getModel().getValueAt(row, 5).toString());

        if (dateR.isEmpty() || timeR.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Select Item to Delete");
        } else {
            if (ask == 0) {
                String sql = "delete from Permanent_DB where Date='" + dateR + "' and Time='" + timeR + "'";
                try {
                    pst = conn.prepareStatement(sql);
                    pst.execute();

                    JOptionPane.showMessageDialog(null, "Deleted");

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
        }
        AllDaysSalesTable();
        AllDaysSalesTableColor();
        sumupAllDaySales();
    }

    public void clearAllPermanent() {
        int ask = JOptionPane.showConfirmDialog(null, "Do you want to Delete All Records?", "Delete", JOptionPane.YES_NO_OPTION);
        if (ask == 0) {
            String sql = "delete from Permanent_DB ";
            try {
                pst = conn.prepareStatement(sql);
                pst.execute();
                JOptionPane.showMessageDialog(null, "All Records Deleted");

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
        AllDaysSalesTable();
        AllDaysSalesTableColor();
    }

    public void tableArrearsClick() {

        int row = table_Arrears.getSelectedRow();
        String Table_click = (table_Arrears.getModel().getValueAt(row, 0).toString());
        try {
            String sql = "select id,Name,Item,Amount,Date,Status,Description from Records_DB where id='" + Table_click + "'";
            pst = conn.prepareStatement(sql);
            rs = pst.executeQuery();

            if (rs.next()) {

                String add1 = rs.getString("id");
                txt_id.setText(add1);
                String add2 = rs.getString("Name");
                txt_Name.setText(add2);
                String add3 = rs.getString("Item");
                txt_type.setText(add3);
                String add4 = rs.getString("Amount");
                txt_Amount.setText(add4);

                //setting date into dat chooser
                String dateValue = rs.getString("Date");
                java.util.Date add5 = new SimpleDateFormat("MMM EE d, yyyy").parse(dateValue);
                txt_Date.setDate(add5);

                //setting value into radio button
                if (rs.getString("Status").equals("Paid")) {
                    paid.setSelected(true);
                } else {
                    notPaid.setSelected(true);
                }
                //Setting value into textarea
                String add6 = rs.getString("Description");
                Description.setText(add6);

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

        txt_id.setEditable(false);
        txt_Name.setEditable(false);
        txt_type.setEditable(false);
        txt_Amount.setEditable(false);
        txt_Remain.setEditable(false);

        lbl_pay.setEnabled(true);
        txt_Paid.setEnabled(true);
        lbl_Remain.setEnabled(true);
        txt_Remain.setEnabled(true);

        Arrears_Colors();
    }

    public void preview() {
        PrintPreview ppt = new PrintPreview();
        ppt.printPreviewAll();
        ppt.setVisible(true);
    }

    public void saveaccount() {

        if (fullname.getText().isEmpty() || password.getText().isEmpty() || username.getText().isEmpty() || account.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Please Enter Account Details Before Saving");
        } else {

            try {
                String sql = "Insert into accounts (fullname,username,password,status) values (?,?,?,?)";
                pst = conn.prepareStatement(sql);
                pst.setString(1, fullname.getText().trim());
                pst.setString(2, username.getText().trim());
                pst.setString(3, password.getText().trim());
                pst.setString(4, account.trim());

                pst.execute();
                JOptionPane.showMessageDialog(null, "Account Saved");

            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, "Account already exist");
            } finally {
                try {
                    rs.close();
                    pst.close();
                } catch (Exception e) {
                }
            }
            accountTable();
        }
    }

    public void newaccount() {
        fullname.setText(" ");
        username.setText(" ");
        password.setText(" ");
        buttonGroup2.clearSelection();
    }

    public void accountsClick() {

        int row = loginTable.getSelectedRow();
        String Table_click = (loginTable.getModel().getValueAt(row, 0).toString());

        try {
            String sql = "select id,fullname,username,password,status from accounts where fullname='" + Table_click + "'";
            pst = conn.prepareStatement(sql);
            rs = pst.executeQuery();

            if (rs.next()) {

                String add1 = rs.getString("fullname");
                fullname.setText(add1);
                String add2 = rs.getString("username");
                username.setText(add2);
                String add3 = rs.getString("password");
                password.setText(add3);
                String id = rs.getString("id");
                lblID.setText(id);

                if (rs.getString("status").equals("admin")) {
                    rdAdmin.setSelected(true);
                } else {
                    rdUser.setSelected(true);
                }

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

    public void deleteaccount() {
        int ask = JOptionPane.showConfirmDialog(null, "Do you want to Delete Account", "Delete", JOptionPane.YES_NO_OPTION);
        if (ask == 0) {
            String user = fullname.getText();
            String pass = username.getText();

            if (user.isEmpty() || pass.isEmpty()) {
                JOptionPane.showMessageDialog(null, "Select Account to Delete");
            } else {

                int row = loginTable.getSelectedRow();
                String u = (loginTable.getModel().getValueAt(row, 0).toString());
                String p = (loginTable.getModel().getValueAt(row, 1).toString());

                String sql = "delete from accounts where fullname='" + u + "' and username='" + p + "'";

                try {
                    pst = conn.prepareStatement(sql);
                    pst.execute();
                    JOptionPane.showMessageDialog(null, "Admin is Deleted");
                } catch (Exception e) {
                    JOptionPane.showMessageDialog(null, e);
                } finally {
                    try {
                        rs.close();
                        pst.close();
                    } catch (Exception e) {
                    }
                }

                accountTable();

            }
        }
    }

    public void updateAccounts() {

        if (fullname.getText().isEmpty() || password.getText().isEmpty() || username.getText().isEmpty()) {
            JOptionPane.showMessageDialog(null, "Select Account");
        } else {

            try {
                String id = lblID.getText().trim();
                String val1 = fullname.getText().trim();
                String val2 = username.getText().trim();
                String val3 = password.getText().trim();

                String sql = "update accounts set id='" + id + "',fullname='" + val1 + "',username='" + val2 + "',password='" + val3 + "' where id='" + id + "'";

                pst = conn.prepareStatement(sql);
                pst.executeUpdate();

                JOptionPane.showMessageDialog(null, "Account Updated");

            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, "Ooops something went wrong");
            } finally {
                try {
                    rs.close();
                    pst.close();
                } catch (Exception e) {
                }
            }

            accountTable();
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

        buttonGroup1 = new javax.swing.ButtonGroup();
        jScrollPane7 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        buttonGroup2 = new javax.swing.ButtonGroup();
        jScrollPane8 = new javax.swing.JScrollPane();
        jPanel1 = new javax.swing.JPanel();
        AdminPane = new javax.swing.JTabbedPane();
        jPanel2 = new javax.swing.JPanel();
        NewItem = new javax.swing.JPanel();
        New2 = new javax.swing.JPanel();
        jLabel6 = new javax.swing.JLabel();
        cmb_Update = new javax.swing.JButton();
        cmb_Save = new javax.swing.JButton();
        jLabel5 = new javax.swing.JLabel();
        cmb_Delete = new javax.swing.JButton();
        txt_ItName = new javax.swing.JTextField();
        txt_whoelsale = new javax.swing.JTextField();
        cmb_New = new javax.swing.JButton();
        txt_Search = new javax.swing.JTextField();
        txt_TTQuantity = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        lbl_left = new javax.swing.JLabel();
        txt_Left = new javax.swing.JTextField();
        btn_revert = new javax.swing.JButton();
        jSeparator21 = new javax.swing.JSeparator();
        jLabel25 = new javax.swing.JLabel();
        txt_Category = new javax.swing.JComboBox<>();
        jLabel26 = new javax.swing.JLabel();
        txt_retail = new javax.swing.JTextField();
        Back = new javax.swing.JButton();
        Refresh = new javax.swing.JButton();
        lbl_Date = new javax.swing.JLabel();
        lbl_Time = new javax.swing.JLabel();
        btnRecords = new javax.swing.JButton();
        Money = new javax.swing.JPanel();
        txt_Tatal = new javax.swing.JTextField();
        jLabel18 = new javax.swing.JLabel();
        tt_SP = new javax.swing.JTextField();
        jLabel20 = new javax.swing.JLabel();
        lbl_saved = new javax.swing.JLabel();
        tt_item = new javax.swing.JTextField();
        lbl_tLeft = new javax.swing.JLabel();
        tt_left = new javax.swing.JTextField();
        jPanel13 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        ItemsTable = new javax.swing.JTable();
        jPanel3 = new javax.swing.JPanel();
        jPanel10 = new javax.swing.JPanel();
        jPanel7 = new javax.swing.JPanel();
        jButton1 = new javax.swing.JButton();
        lblDate = new javax.swing.JLabel();
        lblTime = new javax.swing.JLabel();
        txt_Refresh = new javax.swing.JButton();
        jPanel8 = new javax.swing.JPanel();
        jLabel7 = new javax.swing.JLabel();
        txt_Search1 = new javax.swing.JTextField();
        txt_TotalSales = new javax.swing.JTextField();
        btn_AllDelete = new javax.swing.JButton();
        SelectDelete = new javax.swing.JButton();
        btn_PreviewPrint = new javax.swing.JButton();
        lbl_Timer = new javax.swing.JLabel();
        jPanel11 = new javax.swing.JPanel();
        panePreview = new javax.swing.JTabbedPane();
        jPanel24 = new javax.swing.JPanel();
        jPanel26 = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        DailySalesTable = new javax.swing.JTable();
        jPanel4 = new javax.swing.JPanel();
        jPanel9 = new javax.swing.JPanel();
        jButton3 = new javax.swing.JButton();
        lblDate1 = new javax.swing.JLabel();
        lblTime1 = new javax.swing.JLabel();
        txt_Refresh1 = new javax.swing.JButton();
        jPanel12 = new javax.swing.JPanel();
        jLabel8 = new javax.swing.JLabel();
        txt_Val3 = new javax.swing.JTextField();
        txt_Val4 = new javax.swing.JTextField();
        txt_Search2 = new javax.swing.JTextField();
        lbl_To1 = new javax.swing.JLabel();
        txt_TotalSales1 = new javax.swing.JTextField();
        btn_AllDelete1 = new javax.swing.JButton();
        SelectDelete1 = new javax.swing.JButton();
        btnPrint1 = new javax.swing.JButton();
        date_Search = new javax.swing.JButton();
        btn_PreviewAll = new javax.swing.JButton();
        jLabel17 = new javax.swing.JLabel();
        quantity_baught = new javax.swing.JTextField();
        jPanel16 = new javax.swing.JPanel();
        jScrollPane3 = new javax.swing.JScrollPane();
        AllDaysTable = new javax.swing.JTable();
        jPanel6 = new javax.swing.JPanel();
        jPanel17 = new javax.swing.JPanel();
        txt_Name = new javax.swing.JTextField();
        txt_type = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        jLabel13 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        txt_Amount = new javax.swing.JTextField();
        jLabel14 = new javax.swing.JLabel();
        jLabel15 = new javax.swing.JLabel();
        btnNew2 = new javax.swing.JButton();
        btn_Save = new javax.swing.JButton();
        delete = new javax.swing.JButton();
        jScrollPane6 = new javax.swing.JScrollPane();
        Description = new javax.swing.JTextArea();
        jLabel16 = new javax.swing.JLabel();
        txt_id = new javax.swing.JTextField();
        paid = new javax.swing.JRadioButton();
        notPaid = new javax.swing.JRadioButton();
        txt_Paid = new javax.swing.JTextField();
        lbl_pay = new javax.swing.JLabel();
        jButton6 = new javax.swing.JButton();
        lbl_Remain = new javax.swing.JLabel();
        txt_Remain = new javax.swing.JTextField();
        btn_Arrears = new javax.swing.JButton();
        arrearsClear = new javax.swing.JButton();
        txt_update = new javax.swing.JButton();
        txt_searchKeep = new javax.swing.JTextField();
        txt_Date = new com.toedter.calendar.JDateChooser();
        jSeparator22 = new javax.swing.JSeparator();
        jSeparator29 = new javax.swing.JSeparator();
        jPanel29 = new javax.swing.JPanel();
        jScrollPane4 = new javax.swing.JScrollPane();
        table_Arrears = new javax.swing.JTable();
        jPanel27 = new javax.swing.JPanel();
        jPanel14 = new javax.swing.JPanel();
        jPanel15 = new javax.swing.JPanel();
        jLabel9 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        lbl_Date1 = new javax.swing.JLabel();
        Or_Cost = new javax.swing.JTextField();
        Or_type = new javax.swing.JTextField();
        Or_Update = new javax.swing.JButton();
        jLabel12 = new javax.swing.JLabel();
        Or_id = new javax.swing.JTextField();
        Or_Save = new javax.swing.JButton();
        Or_New = new javax.swing.JButton();
        Or_Delete = new javax.swing.JButton();
        Or_Print = new javax.swing.JButton();
        txt_OrSearch = new javax.swing.JTextField();
        Or_Clear = new javax.swing.JButton();
        Or_Refresh = new javax.swing.JButton();
        Or_Date = new com.toedter.calendar.JDateChooser();
        jSeparator23 = new javax.swing.JSeparator();
        jSeparator24 = new javax.swing.JSeparator();
        id = new javax.swing.JLabel();
        jPanel28 = new javax.swing.JPanel();
        jScrollPane5 = new javax.swing.JScrollPane();
        Order_Table = new javax.swing.JTable();
        jPanel25 = new javax.swing.JPanel();
        jPanel30 = new javax.swing.JPanel();
        jPanel31 = new javax.swing.JPanel();
        jLabel27 = new javax.swing.JLabel();
        jSeparator32 = new javax.swing.JSeparator();
        jSeparator33 = new javax.swing.JSeparator();
        username = new javax.swing.JTextField();
        jSeparator34 = new javax.swing.JSeparator();
        password = new javax.swing.JPasswordField();
        jLabel28 = new javax.swing.JLabel();
        jLabel29 = new javax.swing.JLabel();
        fullname = new javax.swing.JTextField();
        chk = new javax.swing.JCheckBox();
        rdUser = new javax.swing.JRadioButton();
        rdAdmin = new javax.swing.JRadioButton();
        jLabel30 = new javax.swing.JLabel();
        anew = new javax.swing.JButton();
        asave = new javax.swing.JButton();
        aupdate = new javax.swing.JButton();
        adelete = new javax.swing.JButton();
        lblID = new javax.swing.JLabel();
        jScrollPane10 = new javax.swing.JScrollPane();
        loginTable = new javax.swing.JTable();
        jLabel1 = new javax.swing.JLabel();
        jMenuBar1 = new javax.swing.JMenuBar();
        jMenu1 = new javax.swing.JMenu();
        txt_New = new javax.swing.JMenuItem();
        jSeparator7 = new javax.swing.JPopupMenu.Separator();
        jMenuItem2 = new javax.swing.JMenuItem();
        jSeparator8 = new javax.swing.JPopupMenu.Separator();
        jMenuItem3 = new javax.swing.JMenuItem();
        jSeparator9 = new javax.swing.JPopupMenu.Separator();
        jMenuItem4 = new javax.swing.JMenuItem();
        jSeparator10 = new javax.swing.JPopupMenu.Separator();
        jSeparator11 = new javax.swing.JPopupMenu.Separator();
        jMenuItem5 = new javax.swing.JMenuItem();
        jSeparator12 = new javax.swing.JPopupMenu.Separator();
        jSeparator13 = new javax.swing.JPopupMenu.Separator();
        jMenuItem6 = new javax.swing.JMenuItem();
        jMenu2 = new javax.swing.JMenu();
        jMenuItem7 = new javax.swing.JMenuItem();
        jSeparator14 = new javax.swing.JPopupMenu.Separator();
        jMenuItem8 = new javax.swing.JMenuItem();
        jSeparator15 = new javax.swing.JPopupMenu.Separator();
        jMenuItem9 = new javax.swing.JMenuItem();
        jSeparator16 = new javax.swing.JPopupMenu.Separator();
        jMenuItem10 = new javax.swing.JMenuItem();
        jSeparator17 = new javax.swing.JPopupMenu.Separator();
        jMenuItem11 = new javax.swing.JMenuItem();
        jSeparator18 = new javax.swing.JPopupMenu.Separator();
        jMenuItem12 = new javax.swing.JMenuItem();
        jSeparator19 = new javax.swing.JPopupMenu.Separator();
        jSeparator20 = new javax.swing.JPopupMenu.Separator();
        jSeparator30 = new javax.swing.JPopupMenu.Separator();
        jMenuItem1 = new javax.swing.JMenuItem();
        jSeparator31 = new javax.swing.JPopupMenu.Separator();
        jSeparator1 = new javax.swing.JPopupMenu.Separator();
        menuClearAll = new javax.swing.JMenuItem();

        jTable1.setModel(new javax.swing.table.DefaultTableModel(
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
        jScrollPane7.setViewportView(jTable1);

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("THE MANAGERS FRAME");
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowActivated(java.awt.event.WindowEvent evt) {
                formWindowActivated(evt);
            }
        });

        AdminPane.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        AdminPane.setForeground(new java.awt.Color(0, 0, 51));
        AdminPane.setFont(new java.awt.Font("Tahoma", 1, 16)); // NOI18N
        AdminPane.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                AdminPaneMouseClicked(evt);
            }
        });

        NewItem.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Enter New Items Into Database", javax.swing.border.TitledBorder.CENTER, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Nirmala UI", 1, 18), new java.awt.Color(102, 0, 0))); // NOI18N

        jLabel6.setFont(new java.awt.Font("Tahoma", 1, 16)); // NOI18N
        jLabel6.setForeground(new java.awt.Color(0, 0, 51));
        jLabel6.setText("Whole sale cost in GH₵ ");

        cmb_Update.setFont(new java.awt.Font("Tahoma", 1, 15)); // NOI18N
        cmb_Update.setForeground(new java.awt.Color(51, 0, 0));
        cmb_Update.setIcon(new javax.swing.ImageIcon(getClass().getResource("/image/edit.png"))); // NOI18N
        cmb_Update.setToolTipText("Click to Update");
        cmb_Update.setBorder(null);
        cmb_Update.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmb_UpdateActionPerformed(evt);
            }
        });
        cmb_Update.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                cmb_UpdateKeyPressed(evt);
            }
            public void keyReleased(java.awt.event.KeyEvent evt) {
                cmb_UpdateKeyReleased(evt);
            }
        });

        cmb_Save.setFont(new java.awt.Font("Tahoma", 1, 15)); // NOI18N
        cmb_Save.setForeground(new java.awt.Color(51, 0, 0));
        cmb_Save.setIcon(new javax.swing.ImageIcon(getClass().getResource("/image/save2U.png"))); // NOI18N
        cmb_Save.setToolTipText("Click to Save");
        cmb_Save.setBorder(null);
        cmb_Save.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmb_SaveActionPerformed(evt);
            }
        });
        cmb_Save.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                cmb_SaveKeyPressed(evt);
            }
            public void keyReleased(java.awt.event.KeyEvent evt) {
                cmb_SaveKeyReleased(evt);
            }
        });

        jLabel5.setFont(new java.awt.Font("Tahoma", 1, 16)); // NOI18N
        jLabel5.setForeground(new java.awt.Color(0, 0, 51));
        jLabel5.setText("Item Name");

        cmb_Delete.setFont(new java.awt.Font("Tahoma", 1, 15)); // NOI18N
        cmb_Delete.setForeground(new java.awt.Color(51, 0, 0));
        cmb_Delete.setIcon(new javax.swing.ImageIcon(getClass().getResource("/image/delete_multiple.png"))); // NOI18N
        cmb_Delete.setToolTipText("Click to Delete");
        cmb_Delete.setBorder(null);
        cmb_Delete.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmb_DeleteActionPerformed(evt);
            }
        });
        cmb_Delete.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                cmb_DeleteKeyPressed(evt);
            }
            public void keyReleased(java.awt.event.KeyEvent evt) {
                cmb_DeleteKeyReleased(evt);
            }
        });

        txt_ItName.setFont(new java.awt.Font("Times New Roman", 1, 15)); // NOI18N
        txt_ItName.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                txt_ItNameFocusGained(evt);
            }
        });
        txt_ItName.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txt_ItNameActionPerformed(evt);
            }
        });
        txt_ItName.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txt_ItNameKeyReleased(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txt_ItNameKeyTyped(evt);
            }
        });

        txt_whoelsale.setFont(new java.awt.Font("Times New Roman", 1, 15)); // NOI18N
        txt_whoelsale.setForeground(new java.awt.Color(51, 0, 0));
        txt_whoelsale.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txt_whoelsaleActionPerformed(evt);
            }
        });
        txt_whoelsale.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txt_whoelsaleKeyReleased(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txt_whoelsaleKeyTyped(evt);
            }
        });

        cmb_New.setFont(new java.awt.Font("Tahoma", 1, 15)); // NOI18N
        cmb_New.setForeground(new java.awt.Color(51, 0, 0));
        cmb_New.setIcon(new javax.swing.ImageIcon(getClass().getResource("/image/new.png"))); // NOI18N
        cmb_New.setToolTipText("Add New Item");
        cmb_New.setBorder(null);
        cmb_New.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmb_NewActionPerformed(evt);
            }
        });
        cmb_New.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                cmb_NewKeyPressed(evt);
            }
            public void keyReleased(java.awt.event.KeyEvent evt) {
                cmb_NewKeyReleased(evt);
            }
        });

        txt_Search.setBackground(new java.awt.Color(240, 240, 240));
        txt_Search.setFont(new java.awt.Font("Tahoma", 0, 13)); // NOI18N
        txt_Search.setText("Search...");
        txt_Search.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                txt_SearchFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                txt_SearchFocusLost(evt);
            }
        });
        txt_Search.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txt_SearchActionPerformed(evt);
            }
        });
        txt_Search.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txt_SearchKeyReleased(evt);
            }
        });

        txt_TTQuantity.setFont(new java.awt.Font("Times New Roman", 1, 15)); // NOI18N
        txt_TTQuantity.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txt_TTQuantityFocusLost(evt);
            }
        });
        txt_TTQuantity.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txt_TTQuantityActionPerformed(evt);
            }
        });
        txt_TTQuantity.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txt_TTQuantityKeyPressed(evt);
            }
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txt_TTQuantityKeyReleased(evt);
            }
        });

        jLabel3.setFont(new java.awt.Font("Tahoma", 1, 16)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(0, 0, 51));
        jLabel3.setText("Quantity of Items");

        lbl_left.setFont(new java.awt.Font("Tahoma", 1, 16)); // NOI18N
        lbl_left.setForeground(new java.awt.Color(0, 0, 51));
        lbl_left.setText("Quantity Left");

        txt_Left.setEditable(false);
        txt_Left.setFont(new java.awt.Font("Times New Roman", 1, 15)); // NOI18N
        txt_Left.setForeground(new java.awt.Color(255, 51, 51));
        txt_Left.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                txt_LeftFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                txt_LeftFocusLost(evt);
            }
        });
        txt_Left.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txt_LeftActionPerformed(evt);
            }
        });

        btn_revert.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        btn_revert.setIcon(new javax.swing.ImageIcon(getClass().getResource("/image/backarrow.png"))); // NOI18N
        btn_revert.setToolTipText("Click to enter reverse details");
        btn_revert.setBorder(null);
        btn_revert.setPreferredSize(new java.awt.Dimension(79, 28));
        btn_revert.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_revertActionPerformed(evt);
            }
        });

        jLabel25.setFont(new java.awt.Font("Tahoma", 1, 15)); // NOI18N
        jLabel25.setText("Category");

        txt_Category.setFont(new java.awt.Font("Times New Roman", 1, 15)); // NOI18N
        txt_Category.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Select Category" }));
        txt_Category.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                txt_CategoryFocusGained(evt);
            }
        });
        txt_Category.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txt_CategoryActionPerformed(evt);
            }
        });

        jLabel26.setFont(new java.awt.Font("Tahoma", 1, 16)); // NOI18N
        jLabel26.setForeground(new java.awt.Color(0, 0, 51));
        jLabel26.setText("Retail cost GH₵ ");

        txt_retail.setFont(new java.awt.Font("Times New Roman", 1, 15)); // NOI18N
        txt_retail.setForeground(new java.awt.Color(51, 0, 0));
        txt_retail.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txt_retailActionPerformed(evt);
            }
        });
        txt_retail.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txt_retailKeyReleased(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txt_retailKeyTyped(evt);
            }
        });

        javax.swing.GroupLayout New2Layout = new javax.swing.GroupLayout(New2);
        New2.setLayout(New2Layout);
        New2Layout.setHorizontalGroup(
            New2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jSeparator21, javax.swing.GroupLayout.Alignment.TRAILING)
            .addGroup(New2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(New2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, New2Layout.createSequentialGroup()
                        .addGroup(New2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel25, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel5, javax.swing.GroupLayout.DEFAULT_SIZE, 172, Short.MAX_VALUE))
                        .addGap(18, 18, 18)
                        .addGroup(New2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(txt_Search)
                            .addComponent(txt_ItName, javax.swing.GroupLayout.DEFAULT_SIZE, 184, Short.MAX_VALUE)
                            .addComponent(txt_Category, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                    .addGroup(New2Layout.createSequentialGroup()
                        .addGroup(New2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel26, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel6, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel3, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, New2Layout.createSequentialGroup()
                                .addComponent(cmb_New, javax.swing.GroupLayout.PREFERRED_SIZE, 57, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(cmb_Save, javax.swing.GroupLayout.PREFERRED_SIZE, 58, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(cmb_Update, javax.swing.GroupLayout.PREFERRED_SIZE, 58, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(0, 0, Short.MAX_VALUE))
                            .addComponent(lbl_left, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addGap(18, 18, 18)
                        .addGroup(New2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(txt_Left)
                            .addComponent(txt_whoelsale)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, New2Layout.createSequentialGroup()
                                .addGap(0, 0, Short.MAX_VALUE)
                                .addComponent(cmb_Delete, javax.swing.GroupLayout.PREFERRED_SIZE, 54, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(btn_revert, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(txt_TTQuantity)
                            .addComponent(txt_retail))))
                .addContainerGap())
        );
        New2Layout.setVerticalGroup(
            New2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(New2Layout.createSequentialGroup()
                .addGap(5, 5, 5)
                .addComponent(txt_Search, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(New2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel25, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(txt_Category, javax.swing.GroupLayout.DEFAULT_SIZE, 28, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(New2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(txt_ItName)
                    .addComponent(jLabel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(New2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txt_TTQuantity, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel3))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(New2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lbl_left)
                    .addComponent(txt_Left, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(New2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(txt_whoelsale)
                    .addComponent(jLabel6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(New2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(txt_retail)
                    .addComponent(jLabel26, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(10, 10, 10)
                .addComponent(jSeparator21, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(New2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(cmb_Update, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(cmb_New, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(cmb_Save, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btn_revert, javax.swing.GroupLayout.DEFAULT_SIZE, 44, Short.MAX_VALUE)
                    .addComponent(cmb_Delete, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );

        Back.setIcon(new javax.swing.ImageIcon(getClass().getResource("/image/back.png"))); // NOI18N
        Back.setToolTipText("Go Back");
        Back.setBorder(null);
        Back.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BackActionPerformed(evt);
            }
        });
        Back.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                BackKeyReleased(evt);
            }
        });

        Refresh.setIcon(new javax.swing.ImageIcon(getClass().getResource("/image/refreshDesign.png"))); // NOI18N
        Refresh.setToolTipText("Click to Refresh");
        Refresh.setBorder(null);
        Refresh.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                RefreshActionPerformed(evt);
            }
        });
        Refresh.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                RefreshKeyReleased(evt);
            }
        });

        lbl_Date.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        lbl_Date.setForeground(new java.awt.Color(102, 0, 0));

        lbl_Time.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        lbl_Time.setForeground(new java.awt.Color(0, 51, 51));

        btnRecords.setFont(new java.awt.Font("Times New Roman", 1, 18)); // NOI18N
        btnRecords.setForeground(new java.awt.Color(51, 0, 0));
        btnRecords.setIcon(new javax.swing.ImageIcon(getClass().getResource("/image/Update.png"))); // NOI18N
        btnRecords.setToolTipText("Click view records");
        btnRecords.setBorder(null);
        btnRecords.setPreferredSize(new java.awt.Dimension(95, 40));
        btnRecords.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRecordsActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout NewItemLayout = new javax.swing.GroupLayout(NewItem);
        NewItem.setLayout(NewItemLayout);
        NewItemLayout.setHorizontalGroup(
            NewItemLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(NewItemLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(Back, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(4, 4, 4)
                .addComponent(btnRecords, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(lbl_Date, javax.swing.GroupLayout.PREFERRED_SIZE, 117, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(lbl_Time, javax.swing.GroupLayout.PREFERRED_SIZE, 106, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(Refresh, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
            .addComponent(New2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        NewItemLayout.setVerticalGroup(
            NewItemLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, NewItemLayout.createSequentialGroup()
                .addGroup(NewItemLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(lbl_Time, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(Refresh, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(lbl_Date, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(Back, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btnRecords, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(New2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        Money.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Details On Cost", javax.swing.border.TitledBorder.CENTER, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("SansSerif", 1, 18), new java.awt.Color(102, 0, 0))); // NOI18N

        txt_Tatal.setEditable(false);
        txt_Tatal.setFont(new java.awt.Font("Times New Roman", 1, 18)); // NOI18N
        txt_Tatal.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));

        jLabel18.setFont(new java.awt.Font("Tahoma", 1, 16)); // NOI18N
        jLabel18.setForeground(new java.awt.Color(0, 51, 0));
        jLabel18.setText("Total Selling Price in GH₵ ");

        tt_SP.setEditable(false);
        tt_SP.setFont(new java.awt.Font("Times New Roman", 1, 18)); // NOI18N
        tt_SP.setForeground(new java.awt.Color(51, 0, 0));
        tt_SP.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));

        jLabel20.setFont(new java.awt.Font("Tahoma", 1, 16)); // NOI18N
        jLabel20.setForeground(new java.awt.Color(0, 51, 0));
        jLabel20.setText("Total Items In Entered");

        lbl_saved.setFont(new java.awt.Font("Tahoma", 1, 16)); // NOI18N
        lbl_saved.setForeground(new java.awt.Color(0, 51, 0));
        lbl_saved.setText("Total  Quantity");

        tt_item.setEditable(false);
        tt_item.setFont(new java.awt.Font("Times New Roman", 1, 18)); // NOI18N
        tt_item.setForeground(new java.awt.Color(0, 0, 51));
        tt_item.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));

        lbl_tLeft.setFont(new java.awt.Font("Tahoma", 1, 16)); // NOI18N
        lbl_tLeft.setForeground(new java.awt.Color(0, 51, 0));
        lbl_tLeft.setText("Total Quantity Left");

        tt_left.setEditable(false);
        tt_left.setFont(new java.awt.Font("Times New Roman", 1, 18)); // NOI18N
        tt_left.setForeground(new java.awt.Color(204, 0, 204));
        tt_left.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));

        javax.swing.GroupLayout MoneyLayout = new javax.swing.GroupLayout(Money);
        Money.setLayout(MoneyLayout);
        MoneyLayout.setHorizontalGroup(
            MoneyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(MoneyLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(MoneyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel18, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel20, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(lbl_tLeft, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(lbl_saved, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(18, 18, 18)
                .addGroup(MoneyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(txt_Tatal)
                    .addComponent(tt_left)
                    .addComponent(tt_item)
                    .addComponent(tt_SP))
                .addContainerGap())
        );
        MoneyLayout.setVerticalGroup(
            MoneyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(MoneyLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(MoneyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel18, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(tt_SP, javax.swing.GroupLayout.DEFAULT_SIZE, 35, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(MoneyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lbl_saved)
                    .addComponent(tt_item, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(MoneyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lbl_tLeft)
                    .addComponent(tt_left, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(MoneyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel20, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txt_Tatal, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel13.setBorder(javax.swing.BorderFactory.createTitledBorder(""));

        ItemsTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Items Name", "Wholesale", "Retail", "Total Qty", "Qty Left", "Date", "Time"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, true, false, false, true, true
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        ItemsTable.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        ItemsTable.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                ItemsTableFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                ItemsTableFocusLost(evt);
            }
        });
        ItemsTable.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                ItemsTableMouseClicked(evt);
            }
        });
        ItemsTable.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                ItemsTableKeyReleased(evt);
            }
        });
        jScrollPane1.setViewportView(ItemsTable);
        if (ItemsTable.getColumnModel().getColumnCount() > 0) {
            ItemsTable.getColumnModel().getColumn(0).setPreferredWidth(200);
            ItemsTable.getColumnModel().getColumn(1).setPreferredWidth(70);
            ItemsTable.getColumnModel().getColumn(2).setPreferredWidth(50);
            ItemsTable.getColumnModel().getColumn(3).setPreferredWidth(70);
            ItemsTable.getColumnModel().getColumn(4).setPreferredWidth(70);
            ItemsTable.getColumnModel().getColumn(5).setPreferredWidth(150);
            ItemsTable.getColumnModel().getColumn(6).setPreferredWidth(150);
        }

        javax.swing.GroupLayout jPanel13Layout = new javax.swing.GroupLayout(jPanel13);
        jPanel13.setLayout(jPanel13Layout);
        jPanel13Layout.setHorizontalGroup(
            jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel13Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 760, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel13Layout.setVerticalGroup(
            jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel13Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 604, Short.MAX_VALUE)
                .addContainerGap())
        );

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(NewItem, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(Money, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel13, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(0, 0, 0)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel13, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(NewItem, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(Money, javax.swing.GroupLayout.PREFERRED_SIZE, 202, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );

        AdminPane.addTab("Add New Items", jPanel2);

        jPanel7.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Date & Time", javax.swing.border.TitledBorder.LEADING, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 1, 14), new java.awt.Color(0, 0, 51))); // NOI18N

        jButton1.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        jButton1.setForeground(new java.awt.Color(51, 0, 0));
        jButton1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/image/back.png"))); // NOI18N
        jButton1.setToolTipText("Go Back");
        jButton1.setBorder(null);
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        lblDate.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        lblDate.setForeground(new java.awt.Color(204, 0, 0));

        lblTime.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        lblTime.setForeground(new java.awt.Color(0, 0, 153));

        txt_Refresh.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        txt_Refresh.setForeground(new java.awt.Color(51, 0, 0));
        txt_Refresh.setIcon(new javax.swing.ImageIcon(getClass().getResource("/image/refreshDesign.png"))); // NOI18N
        txt_Refresh.setToolTipText("Refresh");
        txt_Refresh.setBorder(null);
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
                .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(lblDate, javax.swing.GroupLayout.PREFERRED_SIZE, 172, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(lblTime, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(txt_Refresh, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        jPanel7Layout.setVerticalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lblDate, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(lblTime, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(jPanel7Layout.createSequentialGroup()
                        .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(txt_Refresh, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );

        jPanel8.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Search Panel", javax.swing.border.TitledBorder.CENTER, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 1, 18), new java.awt.Color(51, 0, 0))); // NOI18N

        jLabel7.setFont(new java.awt.Font("Tahoma", 1, 16)); // NOI18N
        jLabel7.setForeground(new java.awt.Color(0, 0, 51));
        jLabel7.setText("Total Sales of Items");

        txt_Search1.setBackground(new java.awt.Color(240, 240, 240));
        txt_Search1.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        txt_Search1.setText("Search Item...");
        txt_Search1.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                txt_Search1FocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                txt_Search1FocusLost(evt);
            }
        });
        txt_Search1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txt_Search1ActionPerformed(evt);
            }
        });
        txt_Search1.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txt_Search1KeyReleased(evt);
            }
        });

        txt_TotalSales.setEditable(false);
        txt_TotalSales.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        txt_TotalSales.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));

        btn_AllDelete.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        btn_AllDelete.setIcon(new javax.swing.ImageIcon(getClass().getResource("/image/deleteCross.png"))); // NOI18N
        btn_AllDelete.setToolTipText("Clear All");
        btn_AllDelete.setBorder(null);
        btn_AllDelete.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_AllDeleteActionPerformed(evt);
            }
        });

        SelectDelete.setFont(new java.awt.Font("Tahoma", 1, 15)); // NOI18N
        SelectDelete.setForeground(new java.awt.Color(51, 0, 0));
        SelectDelete.setIcon(new javax.swing.ImageIcon(getClass().getResource("/image/delete_multiple.png"))); // NOI18N
        SelectDelete.setToolTipText("Delete Selected Item");
        SelectDelete.setBorder(null);
        SelectDelete.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                SelectDeleteActionPerformed(evt);
            }
        });

        btn_PreviewPrint.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        btn_PreviewPrint.setForeground(new java.awt.Color(0, 0, 51));
        btn_PreviewPrint.setText("Preview Print");
        btn_PreviewPrint.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_PreviewPrintActionPerformed(evt);
            }
        });

        lbl_Timer.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N

        javax.swing.GroupLayout jPanel8Layout = new javax.swing.GroupLayout(jPanel8);
        jPanel8.setLayout(jPanel8Layout);
        jPanel8Layout.setHorizontalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel8Layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(txt_Search1)
                            .addGroup(jPanel8Layout.createSequentialGroup()
                                .addComponent(jLabel7, javax.swing.GroupLayout.PREFERRED_SIZE, 213, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(jPanel8Layout.createSequentialGroup()
                                        .addComponent(SelectDelete, javax.swing.GroupLayout.PREFERRED_SIZE, 53, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(btn_AllDelete, javax.swing.GroupLayout.PREFERRED_SIZE, 58, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addComponent(txt_TotalSales)
                                    .addComponent(btn_PreviewPrint, javax.swing.GroupLayout.DEFAULT_SIZE, 177, Short.MAX_VALUE)))))
                    .addComponent(lbl_Timer, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel8Layout.setVerticalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(txt_Search1, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel7, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txt_TotalSales, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(SelectDelete, javax.swing.GroupLayout.DEFAULT_SIZE, 46, Short.MAX_VALUE)
                    .addComponent(btn_AllDelete, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(18, 18, 18)
                .addComponent(btn_PreviewPrint, javax.swing.GroupLayout.PREFERRED_SIZE, 73, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(lbl_Timer, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jPanel10Layout = new javax.swing.GroupLayout(jPanel10);
        jPanel10.setLayout(jPanel10Layout);
        jPanel10Layout.setHorizontalGroup(
            jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel10Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(jPanel7, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel8, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel10Layout.setVerticalGroup(
            jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel10Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel8, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        panePreview.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N

        jPanel26.setBorder(javax.swing.BorderFactory.createTitledBorder(""));

        DailySalesTable.setFont(new java.awt.Font("Tahoma", 0, 13)); // NOI18N
        DailySalesTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Item", "Qty", "Cost", "Total Cost", "Date", "Time", "Sold By"
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
        jScrollPane2.setViewportView(DailySalesTable);
        if (DailySalesTable.getColumnModel().getColumnCount() > 0) {
            DailySalesTable.getColumnModel().getColumn(1).setPreferredWidth(50);
            DailySalesTable.getColumnModel().getColumn(3).setPreferredWidth(70);
        }

        javax.swing.GroupLayout jPanel26Layout = new javax.swing.GroupLayout(jPanel26);
        jPanel26.setLayout(jPanel26Layout);
        jPanel26Layout.setHorizontalGroup(
            jPanel26Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane2, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 709, Short.MAX_VALUE)
        );
        jPanel26Layout.setVerticalGroup(
            jPanel26Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane2, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 590, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout jPanel24Layout = new javax.swing.GroupLayout(jPanel24);
        jPanel24.setLayout(jPanel24Layout);
        jPanel24Layout.setHorizontalGroup(
            jPanel24Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel26, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        jPanel24Layout.setVerticalGroup(
            jPanel24Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel26, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        panePreview.addTab("Daily Sales", jPanel24);

        javax.swing.GroupLayout jPanel11Layout = new javax.swing.GroupLayout(jPanel11);
        jPanel11.setLayout(jPanel11Layout);
        jPanel11Layout.setHorizontalGroup(
            jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel11Layout.createSequentialGroup()
                .addComponent(panePreview)
                .addContainerGap())
        );
        jPanel11Layout.setVerticalGroup(
            jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(panePreview)
        );

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(0, 0, 0)
                .addComponent(jPanel10, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jPanel11, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(0, 0, 0))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel10, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel11, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );

        AdminPane.addTab("View Daily Sales", jPanel3);

        jPanel9.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Date & Time", javax.swing.border.TitledBorder.LEADING, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 1, 14), new java.awt.Color(0, 0, 51))); // NOI18N

        jButton3.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        jButton3.setForeground(new java.awt.Color(51, 0, 0));
        jButton3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/image/back.png"))); // NOI18N
        jButton3.setToolTipText("Go Back");
        jButton3.setBorder(null);
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });

        lblDate1.setFont(new java.awt.Font("Tahoma", 1, 16)); // NOI18N
        lblDate1.setForeground(new java.awt.Color(153, 0, 0));

        lblTime1.setFont(new java.awt.Font("Tahoma", 1, 16)); // NOI18N
        lblTime1.setForeground(new java.awt.Color(0, 51, 153));

        txt_Refresh1.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        txt_Refresh1.setForeground(new java.awt.Color(51, 0, 0));
        txt_Refresh1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/image/refreshDesign.png"))); // NOI18N
        txt_Refresh1.setToolTipText("Refresh");
        txt_Refresh1.setBorder(null);
        txt_Refresh1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txt_Refresh1ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel9Layout = new javax.swing.GroupLayout(jPanel9);
        jPanel9.setLayout(jPanel9Layout);
        jPanel9Layout.setHorizontalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel9Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jButton3, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(lblDate1, javax.swing.GroupLayout.PREFERRED_SIZE, 172, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(lblTime1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(txt_Refresh1, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        jPanel9Layout.setVerticalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel9Layout.createSequentialGroup()
                .addGap(2, 2, 2)
                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lblDate1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(lblTime1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(jPanel9Layout.createSequentialGroup()
                        .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(txt_Refresh1, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jButton3, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addGap(2, 2, 2))
        );

        jPanel12.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Search Panel", javax.swing.border.TitledBorder.CENTER, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 1, 18), new java.awt.Color(0, 0, 51))); // NOI18N

        jLabel8.setFont(new java.awt.Font("Tahoma", 1, 16)); // NOI18N
        jLabel8.setForeground(new java.awt.Color(0, 51, 0));
        jLabel8.setText("Total Sales of Items");

        txt_Val3.setBackground(new java.awt.Color(240, 240, 240));
        txt_Val3.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        txt_Val3.setText("Date...");
        txt_Val3.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                txt_Val3FocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                txt_Val3FocusLost(evt);
            }
        });
        txt_Val3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txt_Val3ActionPerformed(evt);
            }
        });
        txt_Val3.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txt_Val3KeyPressed(evt);
            }
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txt_Val3KeyReleased(evt);
            }
        });

        txt_Val4.setBackground(new java.awt.Color(240, 240, 240));
        txt_Val4.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        txt_Val4.setText("Search Date....");
        txt_Val4.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                txt_Val4FocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                txt_Val4FocusLost(evt);
            }
        });
        txt_Val4.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txt_Val4KeyReleased(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txt_Val4KeyTyped(evt);
            }
        });

        txt_Search2.setBackground(new java.awt.Color(240, 240, 240));
        txt_Search2.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        txt_Search2.setText("Search Item...");
        txt_Search2.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                txt_Search2FocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                txt_Search2FocusLost(evt);
            }
        });
        txt_Search2.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txt_Search2KeyReleased(evt);
            }
        });

        lbl_To1.setFont(new java.awt.Font("Tahoma", 1, 16)); // NOI18N
        lbl_To1.setForeground(new java.awt.Color(251, 188, 5));
        lbl_To1.setText("To");

        txt_TotalSales1.setEditable(false);
        txt_TotalSales1.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        txt_TotalSales1.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        txt_TotalSales1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txt_TotalSales1ActionPerformed(evt);
            }
        });

        btn_AllDelete1.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        btn_AllDelete1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/image/deleteCross.png"))); // NOI18N
        btn_AllDelete1.setToolTipText("Clear All");
        btn_AllDelete1.setBorder(null);
        btn_AllDelete1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_AllDelete1ActionPerformed(evt);
            }
        });

        SelectDelete1.setFont(new java.awt.Font("Tahoma", 1, 15)); // NOI18N
        SelectDelete1.setForeground(new java.awt.Color(51, 0, 0));
        SelectDelete1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/image/delete_multiple.png"))); // NOI18N
        SelectDelete1.setToolTipText("Delete Selected Item");
        SelectDelete1.setBorder(null);
        SelectDelete1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                SelectDelete1ActionPerformed(evt);
            }
        });

        btnPrint1.setFont(new java.awt.Font("Tahoma", 1, 16)); // NOI18N
        btnPrint1.setForeground(new java.awt.Color(51, 0, 0));
        btnPrint1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/image/print2.png"))); // NOI18N
        btnPrint1.setToolTipText("Print Table");
        btnPrint1.setBorder(null);
        btnPrint1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnPrint1ActionPerformed(evt);
            }
        });

        date_Search.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        date_Search.setForeground(new java.awt.Color(51, 0, 0));
        date_Search.setText("Search");
        date_Search.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                date_SearchActionPerformed(evt);
            }
        });

        btn_PreviewAll.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        btn_PreviewAll.setForeground(new java.awt.Color(0, 0, 51));
        btn_PreviewAll.setText("Preview Print");
        btn_PreviewAll.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_PreviewAllActionPerformed(evt);
            }
        });

        jLabel17.setFont(new java.awt.Font("Tahoma", 1, 16)); // NOI18N
        jLabel17.setForeground(new java.awt.Color(0, 51, 0));
        jLabel17.setText("Quantity bought");

        quantity_baught.setEditable(false);
        quantity_baught.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        quantity_baught.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        quantity_baught.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                quantity_baughtActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel12Layout = new javax.swing.GroupLayout(jPanel12);
        jPanel12.setLayout(jPanel12Layout);
        jPanel12Layout.setHorizontalGroup(
            jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel12Layout.createSequentialGroup()
                .addGap(169, 169, 169)
                .addComponent(date_Search, javax.swing.GroupLayout.PREFERRED_SIZE, 91, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(jPanel12Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(txt_Search2)
                    .addGroup(jPanel12Layout.createSequentialGroup()
                        .addGroup(jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel8, javax.swing.GroupLayout.PREFERRED_SIZE, 174, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txt_Val4, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(7, 7, 7)
                        .addComponent(lbl_To1)
                        .addGap(29, 29, 29)
                        .addGroup(jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(btn_PreviewAll, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addGroup(jPanel12Layout.createSequentialGroup()
                                .addComponent(btnPrint1, javax.swing.GroupLayout.PREFERRED_SIZE, 53, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(SelectDelete1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(btn_AllDelete1, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(txt_TotalSales1)
                            .addGroup(jPanel12Layout.createSequentialGroup()
                                .addComponent(txt_Val3, javax.swing.GroupLayout.PREFERRED_SIZE, 163, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(0, 0, Short.MAX_VALUE))))
                    .addGroup(jPanel12Layout.createSequentialGroup()
                        .addComponent(jLabel17, javax.swing.GroupLayout.PREFERRED_SIZE, 174, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(57, 57, 57)
                        .addComponent(quantity_baught)))
                .addContainerGap())
        );
        jPanel12Layout.setVerticalGroup(
            jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel12Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(txt_Search2, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(jPanel12Layout.createSequentialGroup()
                        .addGap(3, 3, 3)
                        .addGroup(jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(txt_Val3, javax.swing.GroupLayout.DEFAULT_SIZE, 35, Short.MAX_VALUE)
                            .addComponent(lbl_To1)))
                    .addComponent(txt_Val4))
                .addGap(19, 19, 19)
                .addComponent(date_Search, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel8, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txt_TotalSales1, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(7, 7, 7)
                .addGroup(jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel17, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(quantity_baught, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(btnPrint1, javax.swing.GroupLayout.DEFAULT_SIZE, 36, Short.MAX_VALUE)
                    .addComponent(SelectDelete1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btn_AllDelete1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(18, 18, 18)
                .addComponent(btn_PreviewAll, javax.swing.GroupLayout.PREFERRED_SIZE, 73, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(142, Short.MAX_VALUE))
        );

        jPanel16.setBorder(javax.swing.BorderFactory.createTitledBorder(""));

        AllDaysTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null}
            },
            new String [] {
                "Item", "Qty", "Cost", "Total Cost", "Date", "Time", "Sold By"
            }
        ));
        AllDaysTable.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                AllDaysTableMouseClicked(evt);
            }
        });
        AllDaysTable.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                AllDaysTableKeyReleased(evt);
            }
        });
        jScrollPane3.setViewportView(AllDaysTable);
        if (AllDaysTable.getColumnModel().getColumnCount() > 0) {
            AllDaysTable.getColumnModel().getColumn(1).setPreferredWidth(50);
            AllDaysTable.getColumnModel().getColumn(3).setPreferredWidth(70);
        }

        javax.swing.GroupLayout jPanel16Layout = new javax.swing.GroupLayout(jPanel16);
        jPanel16.setLayout(jPanel16Layout);
        jPanel16Layout.setHorizontalGroup(
            jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel16Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 720, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel16Layout.setVerticalGroup(
            jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel16Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 604, Short.MAX_VALUE)
                .addContainerGap())
        );

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(jPanel9, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel12, javax.swing.GroupLayout.PREFERRED_SIZE, 424, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addComponent(jPanel16, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGap(0, 0, 0)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel16, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addComponent(jPanel9, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jPanel12, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addContainerGap())
        );

        AdminPane.addTab("View All Sales", jPanel4);

        jPanel17.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Arrears Customers Details", javax.swing.border.TitledBorder.CENTER, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 1, 18), new java.awt.Color(0, 0, 51))); // NOI18N
        jPanel17.setForeground(new java.awt.Color(51, 255, 51));

        txt_Name.setFont(new java.awt.Font("Times New Roman", 1, 15)); // NOI18N
        txt_Name.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txt_NameActionPerformed(evt);
            }
        });

        txt_type.setFont(new java.awt.Font("Times New Roman", 1, 15)); // NOI18N
        txt_type.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txt_typeActionPerformed(evt);
            }
        });

        jLabel2.setFont(new java.awt.Font("Tahoma", 1, 15)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(51, 0, 0));
        jLabel2.setText("Name");

        jLabel13.setFont(new java.awt.Font("Tahoma", 1, 15)); // NOI18N
        jLabel13.setForeground(new java.awt.Color(51, 0, 0));
        jLabel13.setText("Date");

        jLabel4.setFont(new java.awt.Font("Tahoma", 1, 15)); // NOI18N
        jLabel4.setForeground(new java.awt.Color(51, 0, 0));
        jLabel4.setText("Type of Item");

        txt_Amount.setFont(new java.awt.Font("Times New Roman", 1, 15)); // NOI18N
        txt_Amount.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txt_AmountActionPerformed(evt);
            }
        });
        txt_Amount.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txt_AmountKeyPressed(evt);
            }
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txt_AmountKeyReleased(evt);
            }
        });

        jLabel14.setFont(new java.awt.Font("Tahoma", 1, 15)); // NOI18N
        jLabel14.setForeground(new java.awt.Color(51, 0, 0));
        jLabel14.setText("Amount Owning in GH₵ ");

        jLabel15.setFont(new java.awt.Font("Tahoma", 1, 15)); // NOI18N
        jLabel15.setForeground(new java.awt.Color(51, 0, 0));
        jLabel15.setText("Customer description ");

        btnNew2.setFont(new java.awt.Font("Times New Roman", 1, 15)); // NOI18N
        btnNew2.setForeground(new java.awt.Color(51, 0, 0));
        btnNew2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/image/new.png"))); // NOI18N
        btnNew2.setToolTipText("Click to Add New Customer");
        btnNew2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnNew2ActionPerformed(evt);
            }
        });

        btn_Save.setFont(new java.awt.Font("Times New Roman", 1, 15)); // NOI18N
        btn_Save.setForeground(new java.awt.Color(51, 0, 0));
        btn_Save.setIcon(new javax.swing.ImageIcon(getClass().getResource("/image/save2U.png"))); // NOI18N
        btn_Save.setToolTipText("Click to Save ");
        btn_Save.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_SaveActionPerformed(evt);
            }
        });

        delete.setFont(new java.awt.Font("Times New Roman", 1, 15)); // NOI18N
        delete.setForeground(new java.awt.Color(51, 0, 0));
        delete.setIcon(new javax.swing.ImageIcon(getClass().getResource("/image/delete_multiple.png"))); // NOI18N
        delete.setToolTipText("Click to Delete Selected Customer");
        delete.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                deleteActionPerformed(evt);
            }
        });

        Description.setColumns(20);
        Description.setFont(new java.awt.Font("Times New Roman", 1, 15)); // NOI18N
        Description.setLineWrap(true);
        Description.setRows(5);
        Description.setWrapStyleWord(true);
        jScrollPane6.setViewportView(Description);

        jLabel16.setFont(new java.awt.Font("Tahoma", 1, 15)); // NOI18N
        jLabel16.setForeground(new java.awt.Color(51, 0, 0));
        jLabel16.setText("ID");

        txt_id.setFont(new java.awt.Font("Times New Roman", 1, 15)); // NOI18N
        txt_id.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txt_idActionPerformed(evt);
            }
        });

        buttonGroup1.add(paid);
        paid.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        paid.setForeground(new java.awt.Color(0, 0, 51));
        paid.setText("Paid");
        paid.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                paidActionPerformed(evt);
            }
        });

        buttonGroup1.add(notPaid);
        notPaid.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        notPaid.setForeground(new java.awt.Color(0, 0, 51));
        notPaid.setText("Not Paid");
        notPaid.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                notPaidActionPerformed(evt);
            }
        });

        txt_Paid.setBackground(new java.awt.Color(0, 204, 204));
        txt_Paid.setFont(new java.awt.Font("Times New Roman", 1, 15)); // NOI18N
        txt_Paid.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txt_PaidActionPerformed(evt);
            }
        });
        txt_Paid.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txt_PaidKeyPressed(evt);
            }
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txt_PaidKeyReleased(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txt_PaidKeyTyped(evt);
            }
        });

        lbl_pay.setFont(new java.awt.Font("Tahoma", 1, 15)); // NOI18N
        lbl_pay.setForeground(new java.awt.Color(51, 0, 0));
        lbl_pay.setText("Amount Paying in GH₵ ");

        jButton6.setFont(new java.awt.Font("Times New Roman", 1, 15)); // NOI18N
        jButton6.setForeground(new java.awt.Color(51, 0, 0));
        jButton6.setIcon(new javax.swing.ImageIcon(getClass().getResource("/image/refreshDesign.png"))); // NOI18N
        jButton6.setToolTipText("Refresh Table");
        jButton6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton6ActionPerformed(evt);
            }
        });

        lbl_Remain.setFont(new java.awt.Font("Tahoma", 1, 15)); // NOI18N
        lbl_Remain.setForeground(new java.awt.Color(51, 0, 0));
        lbl_Remain.setText("Amount Remain in GH₵ ");

        txt_Remain.setBackground(new java.awt.Color(204, 204, 255));
        txt_Remain.setFont(new java.awt.Font("Times New Roman", 1, 15)); // NOI18N
        txt_Remain.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txt_RemainActionPerformed(evt);
            }
        });
        txt_Remain.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txt_RemainKeyPressed(evt);
            }
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txt_RemainKeyReleased(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txt_RemainKeyTyped(evt);
            }
        });

        btn_Arrears.setFont(new java.awt.Font("Times New Roman", 1, 16)); // NOI18N
        btn_Arrears.setForeground(new java.awt.Color(51, 0, 0));
        btn_Arrears.setIcon(new javax.swing.ImageIcon(getClass().getResource("/image/print2.png"))); // NOI18N
        btn_Arrears.setToolTipText("Click to Print Arrears Customers");
        btn_Arrears.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_ArrearsActionPerformed(evt);
            }
        });

        arrearsClear.setFont(new java.awt.Font("Times New Roman", 1, 15)); // NOI18N
        arrearsClear.setIcon(new javax.swing.ImageIcon(getClass().getResource("/image/deleteCross.png"))); // NOI18N
        arrearsClear.setToolTipText("Click to Clear All Records");
        arrearsClear.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                arrearsClearActionPerformed(evt);
            }
        });

        txt_update.setFont(new java.awt.Font("Times New Roman", 1, 15)); // NOI18N
        txt_update.setForeground(new java.awt.Color(51, 0, 0));
        txt_update.setIcon(new javax.swing.ImageIcon(getClass().getResource("/image/edit.png"))); // NOI18N
        txt_update.setToolTipText("Update Customer Records");
        txt_update.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txt_updateActionPerformed(evt);
            }
        });

        txt_searchKeep.setBackground(new java.awt.Color(240, 240, 240));
        txt_searchKeep.setFont(new java.awt.Font("Tahoma", 0, 15)); // NOI18N
        txt_searchKeep.setText("Search...");
        txt_searchKeep.setToolTipText("Enter to Search Name");
        txt_searchKeep.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                txt_searchKeepFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                txt_searchKeepFocusLost(evt);
            }
        });
        txt_searchKeep.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txt_searchKeepKeyReleased(evt);
            }
        });

        txt_Date.setDateFormatString("MMM EE d, yyyy");

        javax.swing.GroupLayout jPanel17Layout = new javax.swing.GroupLayout(jPanel17);
        jPanel17.setLayout(jPanel17Layout);
        jPanel17Layout.setHorizontalGroup(
            jPanel17Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel17Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel17Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel17Layout.createSequentialGroup()
                        .addGroup(jPanel17Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel17Layout.createSequentialGroup()
                                .addComponent(jLabel15, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addGroup(jPanel17Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(jScrollPane6, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                                    .addGroup(jPanel17Layout.createSequentialGroup()
                                        .addComponent(paid, javax.swing.GroupLayout.PREFERRED_SIZE, 59, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                        .addComponent(notPaid))))
                            .addGroup(jPanel17Layout.createSequentialGroup()
                                .addComponent(txt_searchKeep)
                                .addGap(18, 18, 18)
                                .addComponent(jButton6, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel17Layout.createSequentialGroup()
                                .addGap(0, 0, Short.MAX_VALUE)
                                .addGroup(jPanel17Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel17Layout.createSequentialGroup()
                                        .addComponent(txt_update, javax.swing.GroupLayout.PREFERRED_SIZE, 44, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                        .addComponent(arrearsClear, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(btn_Arrears, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel17Layout.createSequentialGroup()
                                        .addComponent(jLabel13, javax.swing.GroupLayout.PREFERRED_SIZE, 174, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(18, 18, 18)
                                        .addComponent(txt_Date, javax.swing.GroupLayout.PREFERRED_SIZE, 140, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                        .addContainerGap())
                    .addGroup(jPanel17Layout.createSequentialGroup()
                        .addGroup(jPanel17Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel17Layout.createSequentialGroup()
                                .addGroup(jPanel17Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                    .addComponent(jLabel16, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(jLabel2, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(jLabel4, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(lbl_pay, javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel14, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(lbl_Remain, javax.swing.GroupLayout.Alignment.LEADING))
                                .addGap(18, 18, 18)
                                .addGroup(jPanel17Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(jPanel17Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                        .addComponent(txt_Remain, javax.swing.GroupLayout.PREFERRED_SIZE, 142, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(txt_Paid, javax.swing.GroupLayout.PREFERRED_SIZE, 142, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addComponent(txt_Amount, javax.swing.GroupLayout.PREFERRED_SIZE, 142, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(txt_type, javax.swing.GroupLayout.PREFERRED_SIZE, 142, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(txt_Name, javax.swing.GroupLayout.PREFERRED_SIZE, 142, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(txt_id, javax.swing.GroupLayout.PREFERRED_SIZE, 82, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addGroup(jPanel17Layout.createSequentialGroup()
                                .addComponent(btnNew2, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(btn_Save, javax.swing.GroupLayout.PREFERRED_SIZE, 57, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(delete)))
                        .addGap(0, 0, Short.MAX_VALUE))))
            .addComponent(jSeparator22)
            .addComponent(jSeparator29)
        );
        jPanel17Layout.setVerticalGroup(
            jPanel17Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel17Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel17Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jButton6, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                    .addComponent(txt_searchKeep, javax.swing.GroupLayout.DEFAULT_SIZE, 31, Short.MAX_VALUE))
                .addGap(18, 18, 18)
                .addGroup(jPanel17Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel16)
                    .addComponent(txt_id, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel17Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txt_Name, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel2))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel17Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txt_type, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel4))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel17Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel14)
                    .addComponent(txt_Amount, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(13, 13, 13)
                .addGroup(jPanel17Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txt_Paid, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lbl_pay))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel17Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txt_Remain, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lbl_Remain))
                .addGap(12, 12, 12)
                .addGroup(jPanel17Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel13)
                    .addGroup(jPanel17Layout.createSequentialGroup()
                        .addComponent(txt_Date, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel17Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(notPaid)
                            .addComponent(paid))
                        .addGap(4, 4, 4)
                        .addGroup(jPanel17Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel15)
                            .addComponent(jScrollPane6, javax.swing.GroupLayout.PREFERRED_SIZE, 84, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jSeparator22, javax.swing.GroupLayout.PREFERRED_SIZE, 5, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel17Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btn_Arrears, javax.swing.GroupLayout.PREFERRED_SIZE, 44, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(arrearsClear, javax.swing.GroupLayout.PREFERRED_SIZE, 44, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(delete, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 44, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txt_update, javax.swing.GroupLayout.PREFERRED_SIZE, 44, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnNew2, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 44, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btn_Save, javax.swing.GroupLayout.PREFERRED_SIZE, 44, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jSeparator29, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18))
        );

        jPanel29.setBorder(javax.swing.BorderFactory.createTitledBorder(""));

        table_Arrears.setFont(new java.awt.Font("Times New Roman", 0, 15)); // NOI18N
        table_Arrears.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null}
            },
            new String [] {
                "ID", "Name", "Item", "Amount", "Date", "Status"
            }
        ));
        table_Arrears.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                table_ArrearsMouseClicked(evt);
            }
        });
        table_Arrears.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                table_ArrearsKeyReleased(evt);
            }
        });
        jScrollPane4.setViewportView(table_Arrears);

        javax.swing.GroupLayout jPanel29Layout = new javax.swing.GroupLayout(jPanel29);
        jPanel29.setLayout(jPanel29Layout);
        jPanel29Layout.setHorizontalGroup(
            jPanel29Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel29Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane4, javax.swing.GroupLayout.DEFAULT_SIZE, 788, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel29Layout.setVerticalGroup(
            jPanel29Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel29Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane4)
                .addContainerGap())
        );

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel17, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jPanel29, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel29, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel17, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );

        AdminPane.addTab("View Arrears Details", jPanel6);

        jPanel15.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Customer Order Details", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 1, 14), new java.awt.Color(0, 0, 51))); // NOI18N

        jLabel9.setFont(new java.awt.Font("Tahoma", 1, 15)); // NOI18N
        jLabel9.setForeground(new java.awt.Color(51, 0, 0));
        jLabel9.setText("Item Type");

        jLabel10.setFont(new java.awt.Font("Tahoma", 1, 15)); // NOI18N
        jLabel10.setForeground(new java.awt.Color(51, 0, 0));
        jLabel10.setText("Cost Of Item");

        lbl_Date1.setFont(new java.awt.Font("Tahoma", 1, 15)); // NOI18N
        lbl_Date1.setForeground(new java.awt.Color(51, 0, 0));
        lbl_Date1.setText("Date");

        Or_Cost.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N
        Or_Cost.setForeground(new java.awt.Color(51, 0, 0));
        Or_Cost.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                Or_CostActionPerformed(evt);
            }
        });

        Or_type.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N
        Or_type.setForeground(new java.awt.Color(51, 0, 0));
        Or_type.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                Or_typeActionPerformed(evt);
            }
        });

        Or_Update.setFont(new java.awt.Font("Times New Roman", 1, 15)); // NOI18N
        Or_Update.setForeground(new java.awt.Color(51, 0, 0));
        Or_Update.setIcon(new javax.swing.ImageIcon(getClass().getResource("/image/edit.png"))); // NOI18N
        Or_Update.setToolTipText("Click to Update");
        Or_Update.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                Or_UpdateActionPerformed(evt);
            }
        });

        jLabel12.setFont(new java.awt.Font("Tahoma", 1, 15)); // NOI18N
        jLabel12.setForeground(new java.awt.Color(51, 0, 0));
        jLabel12.setText("ID");

        Or_id.setEditable(false);
        Or_id.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N
        Or_id.setForeground(new java.awt.Color(51, 0, 0));
        Or_id.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                Or_idActionPerformed(evt);
            }
        });

        Or_Save.setFont(new java.awt.Font("Times New Roman", 1, 15)); // NOI18N
        Or_Save.setForeground(new java.awt.Color(51, 0, 0));
        Or_Save.setIcon(new javax.swing.ImageIcon(getClass().getResource("/image/save2U.png"))); // NOI18N
        Or_Save.setToolTipText("Click to Save");
        Or_Save.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                Or_SaveActionPerformed(evt);
            }
        });

        Or_New.setFont(new java.awt.Font("Times New Roman", 1, 15)); // NOI18N
        Or_New.setForeground(new java.awt.Color(51, 0, 0));
        Or_New.setIcon(new javax.swing.ImageIcon(getClass().getResource("/image/new.png"))); // NOI18N
        Or_New.setToolTipText("Enter New Order");
        Or_New.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                Or_NewActionPerformed(evt);
            }
        });

        Or_Delete.setFont(new java.awt.Font("Times New Roman", 1, 15)); // NOI18N
        Or_Delete.setForeground(new java.awt.Color(51, 0, 0));
        Or_Delete.setIcon(new javax.swing.ImageIcon(getClass().getResource("/image/delete_multiple.png"))); // NOI18N
        Or_Delete.setToolTipText("Select to Delete");
        Or_Delete.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                Or_DeleteActionPerformed(evt);
            }
        });

        Or_Print.setFont(new java.awt.Font("Times New Roman", 1, 15)); // NOI18N
        Or_Print.setForeground(new java.awt.Color(51, 0, 0));
        Or_Print.setIcon(new javax.swing.ImageIcon(getClass().getResource("/image/print2.png"))); // NOI18N
        Or_Print.setToolTipText("Click to Print");
        Or_Print.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                Or_PrintActionPerformed(evt);
            }
        });

        txt_OrSearch.setBackground(new java.awt.Color(240, 240, 240));
        txt_OrSearch.setFont(new java.awt.Font("Tahoma", 0, 15)); // NOI18N
        txt_OrSearch.setText("Search...");
        txt_OrSearch.setToolTipText("Enter to search");
        txt_OrSearch.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                txt_OrSearchFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                txt_OrSearchFocusLost(evt);
            }
        });
        txt_OrSearch.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txt_OrSearchKeyReleased(evt);
            }
        });

        Or_Clear.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N
        Or_Clear.setForeground(new java.awt.Color(51, 0, 0));
        Or_Clear.setIcon(new javax.swing.ImageIcon(getClass().getResource("/image/deleteCross.png"))); // NOI18N
        Or_Clear.setToolTipText("Click to Clear All Records");
        Or_Clear.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                Or_ClearActionPerformed(evt);
            }
        });

        Or_Refresh.setFont(new java.awt.Font("Times New Roman", 1, 15)); // NOI18N
        Or_Refresh.setForeground(new java.awt.Color(0, 0, 51));
        Or_Refresh.setIcon(new javax.swing.ImageIcon(getClass().getResource("/image/refreshDesign.png"))); // NOI18N
        Or_Refresh.setToolTipText("Refresh Table");
        Or_Refresh.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                Or_RefreshActionPerformed(evt);
            }
        });

        Or_Date.setDateFormatString("MMM EE d, yyyy");

        javax.swing.GroupLayout jPanel15Layout = new javax.swing.GroupLayout(jPanel15);
        jPanel15.setLayout(jPanel15Layout);
        jPanel15Layout.setHorizontalGroup(
            jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
            .addGroup(jPanel15Layout.createSequentialGroup()
                .addGap(2, 2, 2)
                .addGroup(jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel15Layout.createSequentialGroup()
                        .addComponent(txt_OrSearch, javax.swing.GroupLayout.PREFERRED_SIZE, 295, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(Or_Refresh, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap())
                    .addGroup(jPanel15Layout.createSequentialGroup()
                        .addGroup(jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel15Layout.createSequentialGroup()
                                .addGroup(jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                    .addComponent(jLabel10, javax.swing.GroupLayout.DEFAULT_SIZE, 147, Short.MAX_VALUE)
                                    .addComponent(jLabel9, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(lbl_Date1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                .addGap(8, 8, 8)
                                .addGroup(jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(Or_Date, javax.swing.GroupLayout.DEFAULT_SIZE, 202, Short.MAX_VALUE)
                                    .addComponent(Or_Cost)
                                    .addComponent(Or_type, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addGroup(jPanel15Layout.createSequentialGroup()
                                .addComponent(jLabel12, javax.swing.GroupLayout.PREFERRED_SIZE, 147, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(10, 10, 10)
                                .addComponent(Or_id, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(id, javax.swing.GroupLayout.PREFERRED_SIZE, 19, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(10, 10, 10))))
            .addComponent(jSeparator23, javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel15Layout.createSequentialGroup()
                .addComponent(Or_New, javax.swing.GroupLayout.PREFERRED_SIZE, 46, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(Or_Save, javax.swing.GroupLayout.PREFERRED_SIZE, 46, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(Or_Delete, javax.swing.GroupLayout.PREFERRED_SIZE, 48, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(Or_Update, javax.swing.GroupLayout.PREFERRED_SIZE, 52, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(Or_Print, javax.swing.GroupLayout.PREFERRED_SIZE, 56, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(Or_Clear, javax.swing.GroupLayout.PREFERRED_SIZE, 55, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
            .addComponent(jSeparator24, javax.swing.GroupLayout.Alignment.LEADING)
        );
        jPanel15Layout.setVerticalGroup(
            jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel15Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(txt_OrSearch, javax.swing.GroupLayout.DEFAULT_SIZE, 32, Short.MAX_VALUE)
                    .addComponent(Or_Refresh, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel12)
                        .addComponent(Or_id, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(id, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(18, 18, 18)
                .addGroup(jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel9)
                    .addComponent(Or_type, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel10)
                    .addComponent(Or_Cost, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(lbl_Date1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(Or_Date, javax.swing.GroupLayout.DEFAULT_SIZE, 22, Short.MAX_VALUE))
                .addGap(18, 18, 18)
                .addComponent(jSeparator23, javax.swing.GroupLayout.PREFERRED_SIZE, 11, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(Or_Save, javax.swing.GroupLayout.PREFERRED_SIZE, 44, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(Or_Delete, javax.swing.GroupLayout.PREFERRED_SIZE, 44, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(Or_Print, javax.swing.GroupLayout.PREFERRED_SIZE, 44, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(Or_Update, javax.swing.GroupLayout.PREFERRED_SIZE, 44, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(Or_New, javax.swing.GroupLayout.PREFERRED_SIZE, 44, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(Or_Clear, javax.swing.GroupLayout.PREFERRED_SIZE, 44, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(0, 115, Short.MAX_VALUE)
                .addComponent(jSeparator24, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(163, 163, 163))
        );

        jPanel28.setBorder(javax.swing.BorderFactory.createTitledBorder(""));

        Order_Table.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "ID", "Item Type", "Cost", "Date"
            }
        ));
        Order_Table.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                Order_TableMouseClicked(evt);
            }
        });
        Order_Table.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                Order_TableKeyReleased(evt);
            }
        });
        jScrollPane5.setViewportView(Order_Table);
        if (Order_Table.getColumnModel().getColumnCount() > 0) {
            Order_Table.getColumnModel().getColumn(0).setResizable(false);
            Order_Table.getColumnModel().getColumn(0).setPreferredWidth(40);
            Order_Table.getColumnModel().getColumn(2).setResizable(false);
            Order_Table.getColumnModel().getColumn(2).setPreferredWidth(70);
        }

        javax.swing.GroupLayout jPanel28Layout = new javax.swing.GroupLayout(jPanel28);
        jPanel28.setLayout(jPanel28Layout);
        jPanel28Layout.setHorizontalGroup(
            jPanel28Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel28Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane5, javax.swing.GroupLayout.DEFAULT_SIZE, 775, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel28Layout.setVerticalGroup(
            jPanel28Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel28Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane5, javax.swing.GroupLayout.DEFAULT_SIZE, 593, Short.MAX_VALUE)
                .addContainerGap())
        );

        javax.swing.GroupLayout jPanel14Layout = new javax.swing.GroupLayout(jPanel14);
        jPanel14.setLayout(jPanel14Layout);
        jPanel14Layout.setHorizontalGroup(
            jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel14Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel15, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel28, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel14Layout.setVerticalGroup(
            jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel14Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jPanel28, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel15, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );

        javax.swing.GroupLayout jPanel27Layout = new javax.swing.GroupLayout(jPanel27);
        jPanel27.setLayout(jPanel27Layout);
        jPanel27Layout.setHorizontalGroup(
            jPanel27Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel14, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        jPanel27Layout.setVerticalGroup(
            jPanel27Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel14, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        AdminPane.addTab("View Customer Order Details", jPanel27);

        jPanel30.setBackground(new java.awt.Color(153, 153, 153));

        jPanel31.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Enter Account Details", javax.swing.border.TitledBorder.CENTER, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 1, 14), new java.awt.Color(0, 0, 51))); // NOI18N

        jLabel27.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        jLabel27.setForeground(new java.awt.Color(51, 51, 0));
        jLabel27.setText("Password");

        username.setBackground(new java.awt.Color(204, 204, 204));
        username.setFont(new java.awt.Font("Tahoma", 0, 13)); // NOI18N
        username.setForeground(new java.awt.Color(51, 0, 0));
        username.setToolTipText("Enter username here");
        username.setBorder(null);
        username.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                usernameActionPerformed(evt);
            }
        });

        password.setBackground(new java.awt.Color(204, 204, 204));
        password.setFont(new java.awt.Font("Tahoma", 0, 13)); // NOI18N
        password.setForeground(new java.awt.Color(51, 0, 0));
        password.setToolTipText("Enter password here");
        password.setBorder(null);
        password.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                passwordActionPerformed(evt);
            }
        });

        jLabel28.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        jLabel28.setForeground(new java.awt.Color(51, 51, 0));
        jLabel28.setText("Username");

        jLabel29.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        jLabel29.setForeground(new java.awt.Color(51, 51, 0));
        jLabel29.setText("Fullname");

        fullname.setBackground(new java.awt.Color(204, 204, 204));
        fullname.setFont(new java.awt.Font("Tahoma", 0, 13)); // NOI18N
        fullname.setForeground(new java.awt.Color(51, 0, 0));
        fullname.setToolTipText("Enter fullname here");
        fullname.setBorder(null);
        fullname.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                fullnameKeyPressed(evt);
            }
        });

        chk.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                chkActionPerformed(evt);
            }
        });

        buttonGroup2.add(rdUser);
        rdUser.setFont(new java.awt.Font("Tahoma", 1, 15)); // NOI18N
        rdUser.setForeground(new java.awt.Color(51, 51, 0));
        rdUser.setText("User");
        rdUser.setToolTipText("Select as user");
        rdUser.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rdUserActionPerformed(evt);
            }
        });

        buttonGroup2.add(rdAdmin);
        rdAdmin.setFont(new java.awt.Font("Tahoma", 1, 15)); // NOI18N
        rdAdmin.setForeground(new java.awt.Color(51, 51, 0));
        rdAdmin.setText("Admin");
        rdAdmin.setToolTipText("Select as Manager");
        rdAdmin.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rdAdminActionPerformed(evt);
            }
        });

        jLabel30.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        jLabel30.setText("Status");

        anew.setIcon(new javax.swing.ImageIcon(getClass().getResource("/image/snew.png"))); // NOI18N
        anew.setToolTipText("Click to add new user");
        anew.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                anewActionPerformed(evt);
            }
        });

        asave.setIcon(new javax.swing.ImageIcon(getClass().getResource("/image/ssave.png"))); // NOI18N
        asave.setToolTipText("Click to save");
        asave.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                asaveActionPerformed(evt);
            }
        });

        aupdate.setIcon(new javax.swing.ImageIcon(getClass().getResource("/image/supdate.png"))); // NOI18N
        aupdate.setToolTipText("Click to update");
        aupdate.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                aupdateActionPerformed(evt);
            }
        });

        adelete.setIcon(new javax.swing.ImageIcon(getClass().getResource("/image/sdelete.png"))); // NOI18N
        adelete.setToolTipText("Click to delete");
        adelete.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                adeleteActionPerformed(evt);
            }
        });

        lblID.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblID.setText("s");

        javax.swing.GroupLayout jPanel31Layout = new javax.swing.GroupLayout(jPanel31);
        jPanel31.setLayout(jPanel31Layout);
        jPanel31Layout.setHorizontalGroup(
            jPanel31Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel31Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel31Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jSeparator34)
                    .addComponent(jSeparator32)
                    .addComponent(jLabel28, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jSeparator33)
                    .addComponent(fullname)
                    .addGroup(jPanel31Layout.createSequentialGroup()
                        .addComponent(password)
                        .addGap(6, 6, 6)
                        .addComponent(chk))
                    .addComponent(username)
                    .addComponent(jLabel27, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(jPanel31Layout.createSequentialGroup()
                        .addComponent(jLabel30, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(rdUser)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(rdAdmin))
                    .addGroup(jPanel31Layout.createSequentialGroup()
                        .addGroup(jPanel31Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel31Layout.createSequentialGroup()
                                .addComponent(anew, javax.swing.GroupLayout.PREFERRED_SIZE, 51, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(asave, javax.swing.GroupLayout.PREFERRED_SIZE, 51, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(aupdate, javax.swing.GroupLayout.PREFERRED_SIZE, 63, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(adelete))
                            .addGroup(jPanel31Layout.createSequentialGroup()
                                .addComponent(jLabel29, javax.swing.GroupLayout.PREFERRED_SIZE, 208, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(lblID, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanel31Layout.setVerticalGroup(
            jPanel31Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel31Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel31Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel29, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel31Layout.createSequentialGroup()
                        .addComponent(lblID, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGap(1, 1, 1)))
                .addComponent(fullname, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jSeparator33, javax.swing.GroupLayout.PREFERRED_SIZE, 2, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel28, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(username, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jSeparator32, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(2, 2, 2)
                .addComponent(jLabel27)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel31Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(password)
                    .addComponent(chk, javax.swing.GroupLayout.DEFAULT_SIZE, 34, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jSeparator34, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel31Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(jPanel31Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(rdUser)
                        .addComponent(rdAdmin))
                    .addComponent(jLabel30, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel31Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(asave, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(aupdate, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(anew, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(adelete, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
        );

        loginTable.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        loginTable.setForeground(new java.awt.Color(51, 0, 51));
        loginTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null}
            },
            new String [] {
                "Full Name", "Username", "Status"
            }
        ));
        loginTable.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                loginTableMouseClicked(evt);
            }
        });
        loginTable.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                loginTableKeyReleased(evt);
            }
        });
        jScrollPane10.setViewportView(loginTable);
        if (loginTable.getColumnModel().getColumnCount() > 0) {
            loginTable.getColumnModel().getColumn(0).setPreferredWidth(120);
        }

        javax.swing.GroupLayout jPanel30Layout = new javax.swing.GroupLayout(jPanel30);
        jPanel30.setLayout(jPanel30Layout);
        jPanel30Layout.setHorizontalGroup(
            jPanel30Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel30Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel30Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane10, javax.swing.GroupLayout.DEFAULT_SIZE, 560, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel30Layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(jPanel31, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanel30Layout.setVerticalGroup(
            jPanel30Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel30Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel31, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jScrollPane10, javax.swing.GroupLayout.PREFERRED_SIZE, 194, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(42, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jPanel25Layout = new javax.swing.GroupLayout(jPanel25);
        jPanel25.setLayout(jPanel25Layout);
        jPanel25Layout.setHorizontalGroup(
            jPanel25Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel25Layout.createSequentialGroup()
                .addGap(260, 260, 260)
                .addComponent(jPanel30, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(366, Short.MAX_VALUE))
        );
        jPanel25Layout.setVerticalGroup(
            jPanel25Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel25Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel30, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        AdminPane.addTab("Create Account", jPanel25);

        jLabel1.setFont(new java.awt.Font("Tahoma", 1, 34)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(0, 0, 51));
        jLabel1.setText("             WESTPOINT COSMETICS ASANKRANGWA");
        jLabel1.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 977, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
            .addComponent(AdminPane, javax.swing.GroupLayout.Alignment.TRAILING)
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(0, 0, 0)
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(AdminPane))
        );

        jScrollPane8.setViewportView(jPanel1);

        jMenuBar1.setBorder(null);

        jMenu1.setText("File");

        txt_New.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_N, java.awt.event.InputEvent.SHIFT_MASK | java.awt.event.InputEvent.CTRL_MASK));
        txt_New.setIcon(new javax.swing.ImageIcon(getClass().getResource("/image/new.png"))); // NOI18N
        txt_New.setText("New");
        txt_New.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txt_NewActionPerformed(evt);
            }
        });
        jMenu1.add(txt_New);
        jMenu1.add(jSeparator7);

        jMenuItem2.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_S, java.awt.event.InputEvent.CTRL_MASK));
        jMenuItem2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/image/save2U.png"))); // NOI18N
        jMenuItem2.setText("Save");
        jMenuItem2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem2ActionPerformed(evt);
            }
        });
        jMenu1.add(jMenuItem2);
        jMenu1.add(jSeparator8);

        jMenuItem3.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_U, java.awt.event.InputEvent.CTRL_MASK));
        jMenuItem3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/image/edit.png"))); // NOI18N
        jMenuItem3.setText("Update");
        jMenuItem3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem3ActionPerformed(evt);
            }
        });
        jMenu1.add(jMenuItem3);
        jMenu1.add(jSeparator9);

        jMenuItem4.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_D, java.awt.event.InputEvent.CTRL_MASK));
        jMenuItem4.setIcon(new javax.swing.ImageIcon(getClass().getResource("/image/delete_multiple.png"))); // NOI18N
        jMenuItem4.setText("Delete");
        jMenuItem4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem4ActionPerformed(evt);
            }
        });
        jMenu1.add(jMenuItem4);
        jMenu1.add(jSeparator10);
        jMenu1.add(jSeparator11);

        jMenuItem5.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_C, java.awt.event.InputEvent.CTRL_MASK));
        jMenuItem5.setIcon(new javax.swing.ImageIcon(getClass().getResource("/image/deleteCross.png"))); // NOI18N
        jMenuItem5.setText("Clear");
        jMenuItem5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem5ActionPerformed(evt);
            }
        });
        jMenu1.add(jMenuItem5);
        jMenu1.add(jSeparator12);
        jMenu1.add(jSeparator13);

        jMenuItem6.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_E, java.awt.event.InputEvent.SHIFT_MASK | java.awt.event.InputEvent.CTRL_MASK));
        jMenuItem6.setIcon(new javax.swing.ImageIcon(getClass().getResource("/image/sales2.png"))); // NOI18N
        jMenuItem6.setText("Exit");
        jMenuItem6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem6ActionPerformed(evt);
            }
        });
        jMenu1.add(jMenuItem6);

        jMenuBar1.add(jMenu1);

        jMenu2.setText("Edit");

        jMenuItem7.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_V, java.awt.event.InputEvent.CTRL_MASK));
        jMenuItem7.setIcon(new javax.swing.ImageIcon(getClass().getResource("/image/new.png"))); // NOI18N
        jMenuItem7.setText("View Add New Item Pane");
        jMenuItem7.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem7ActionPerformed(evt);
            }
        });
        jMenu2.add(jMenuItem7);
        jMenu2.add(jSeparator14);

        jMenuItem8.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_D, java.awt.event.InputEvent.ALT_MASK | java.awt.event.InputEvent.CTRL_MASK));
        jMenuItem8.setIcon(new javax.swing.ImageIcon(getClass().getResource("/image/daily.png"))); // NOI18N
        jMenuItem8.setText("View Daily Sales Pane");
        jMenuItem8.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem8ActionPerformed(evt);
            }
        });
        jMenu2.add(jMenuItem8);
        jMenu2.add(jSeparator15);

        jMenuItem9.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_A, java.awt.event.InputEvent.CTRL_MASK));
        jMenuItem9.setIcon(new javax.swing.ImageIcon(getClass().getResource("/image/bag.png"))); // NOI18N
        jMenuItem9.setText("View All Days Sales");
        jMenuItem9.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem9ActionPerformed(evt);
            }
        });
        jMenu2.add(jMenuItem9);
        jMenu2.add(jSeparator16);

        jMenuItem10.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_A, java.awt.event.InputEvent.SHIFT_MASK | java.awt.event.InputEvent.CTRL_MASK));
        jMenuItem10.setIcon(new javax.swing.ImageIcon(getClass().getResource("/image/setting.png"))); // NOI18N
        jMenuItem10.setText("Add or Remove Users");
        jMenuItem10.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem10ActionPerformed(evt);
            }
        });
        jMenu2.add(jMenuItem10);
        jMenu2.add(jSeparator17);

        jMenuItem11.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_R, java.awt.event.InputEvent.CTRL_MASK));
        jMenuItem11.setIcon(new javax.swing.ImageIcon(getClass().getResource("/image/recordBlue.png"))); // NOI18N
        jMenuItem11.setText("View Records keeping Details");
        jMenuItem11.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem11ActionPerformed(evt);
            }
        });
        jMenu2.add(jMenuItem11);
        jMenu2.add(jSeparator18);

        jMenuItem12.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_F, java.awt.event.InputEvent.CTRL_MASK));
        jMenuItem12.setIcon(new javax.swing.ImageIcon(getClass().getResource("/image/Update.png"))); // NOI18N
        jMenuItem12.setText("View Finishing Item");
        jMenuItem12.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem12ActionPerformed(evt);
            }
        });
        jMenu2.add(jMenuItem12);
        jMenu2.add(jSeparator19);
        jMenu2.add(jSeparator20);
        jMenu2.add(jSeparator30);

        jMenuItem1.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_C, java.awt.event.InputEvent.SHIFT_MASK | java.awt.event.InputEvent.CTRL_MASK));
        jMenuItem1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/image/cat.png"))); // NOI18N
        jMenuItem1.setText("Add New Category");
        jMenuItem1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem1ActionPerformed(evt);
            }
        });
        jMenu2.add(jMenuItem1);
        jMenu2.add(jSeparator31);
        jMenu2.add(jSeparator1);

        menuClearAll.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_A, java.awt.event.InputEvent.ALT_MASK | java.awt.event.InputEvent.CTRL_MASK));
        menuClearAll.setIcon(new javax.swing.ImageIcon(getClass().getResource("/image/deleteCross.png"))); // NOI18N
        menuClearAll.setText("Clear All Items");
        menuClearAll.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuClearAllActionPerformed(evt);
            }
        });
        jMenu2.add(menuClearAll);

        jMenuBar1.add(jMenu2);

        setJMenuBar(jMenuBar1);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane8, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 1217, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane8, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 722, Short.MAX_VALUE)
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void txt_NewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txt_NewActionPerformed
        newItem();
    }//GEN-LAST:event_txt_NewActionPerformed

    private void jMenuItem2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem2ActionPerformed
        Saved();
        AllItemsTable();
        TableColor1();
    }//GEN-LAST:event_jMenuItem2ActionPerformed

    private void jMenuItem3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem3ActionPerformed
        Update();
        AllItemsTable();
        TableColor1();
    }//GEN-LAST:event_jMenuItem3ActionPerformed

    private void jMenuItem4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem4ActionPerformed
        deleteSingle();
    }//GEN-LAST:event_jMenuItem4ActionPerformed

    private void jMenuItem5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem5ActionPerformed
        clear();
    }//GEN-LAST:event_jMenuItem5ActionPerformed

    private void jMenuItem6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem6ActionPerformed
        System.exit(0);
    }//GEN-LAST:event_jMenuItem6ActionPerformed

    private void jMenuItem7ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem7ActionPerformed
        AdminPane.setSelectedIndex(0);
    }//GEN-LAST:event_jMenuItem7ActionPerformed

    private void jMenuItem8ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem8ActionPerformed
        AdminPane.setSelectedIndex(1);
    }//GEN-LAST:event_jMenuItem8ActionPerformed

    private void jMenuItem9ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem9ActionPerformed
        AdminPane.setSelectedIndex(2);
    }//GEN-LAST:event_jMenuItem9ActionPerformed

    private void jMenuItem10ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem10ActionPerformed
        AdminPane.setSelectedIndex(3);
    }//GEN-LAST:event_jMenuItem10ActionPerformed

    private void jMenuItem11ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem11ActionPerformed
        AdminPane.setSelectedIndex(4);
    }//GEN-LAST:event_jMenuItem11ActionPerformed

    private void jMenuItem12ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem12ActionPerformed
        try {
            FinishingItemPane fin = new FinishingItemPane();
            fin.setVisible(true);
        } catch (Exception e) {

        }
    }//GEN-LAST:event_jMenuItem12ActionPerformed

    private void formWindowActivated(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowActivated

    }//GEN-LAST:event_formWindowActivated

    private void AdminPaneMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_AdminPaneMouseClicked
        if (AdminPane.getSelectedIndex() == 1) {
            DailySalesTable();//Daily Sales method call
            ViewDailySaleTableColor();
            sumupDaily();
        }
    }//GEN-LAST:event_AdminPaneMouseClicked

    private void txt_searchKeepKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txt_searchKeepKeyReleased
        searchTableContentKeep(txt_searchKeep.getText().toString());
    }//GEN-LAST:event_txt_searchKeepKeyReleased

    private void txt_searchKeepFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txt_searchKeepFocusLost
        txt_searchKeep.setText("Search...");
    }//GEN-LAST:event_txt_searchKeepFocusLost

    private void txt_searchKeepFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txt_searchKeepFocusGained
        txt_searchKeep.setText("");
    }//GEN-LAST:event_txt_searchKeepFocusGained

    private void txt_updateActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txt_updateActionPerformed
        updateArrearsCustomers();
    }//GEN-LAST:event_txt_updateActionPerformed

    private void arrearsClearActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_arrearsClearActionPerformed
        arrearsClear();
    }//GEN-LAST:event_arrearsClearActionPerformed

    private void btn_ArrearsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_ArrearsActionPerformed
        arrearsPrint();
    }//GEN-LAST:event_btn_ArrearsActionPerformed

    private void txt_RemainKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txt_RemainKeyTyped
        // TODO add your handling code here:
    }//GEN-LAST:event_txt_RemainKeyTyped

    private void txt_RemainKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txt_RemainKeyReleased
        // TODO add your handling code here:
    }//GEN-LAST:event_txt_RemainKeyReleased

    private void txt_RemainKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txt_RemainKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_txt_RemainKeyPressed

    private void txt_RemainActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txt_RemainActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txt_RemainActionPerformed

    private void jButton6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton6ActionPerformed
        Records_table();
        Arrears_Colors();
    }//GEN-LAST:event_jButton6ActionPerformed

    private void txt_PaidKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txt_PaidKeyTyped
        char c = evt.getKeyChar();
        if (!(Character.isDigit(c) || (c == KeyEvent.VK_BACK_SPACE) || (c == KeyEvent.VK_DELETE) || (c == KeyEvent.VK_PERIOD))) {
            getToolkit().beep();
            evt.consume();
        }
    }//GEN-LAST:event_txt_PaidKeyTyped

    private void txt_PaidKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txt_PaidKeyReleased
        subTwoValue();
    }//GEN-LAST:event_txt_PaidKeyReleased

    private void txt_PaidKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txt_PaidKeyPressed

    }//GEN-LAST:event_txt_PaidKeyPressed

    private void txt_PaidActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txt_PaidActionPerformed
        Description.requestFocusInWindow();
    }//GEN-LAST:event_txt_PaidActionPerformed

    private void notPaidActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_notPaidActionPerformed
        status = "Not Paid";
    }//GEN-LAST:event_notPaidActionPerformed

    private void paidActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_paidActionPerformed
        status = "Paid";
    }//GEN-LAST:event_paidActionPerformed

    private void txt_idActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txt_idActionPerformed
        txt_Name.requestFocusInWindow();
    }//GEN-LAST:event_txt_idActionPerformed

    private void deleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_deleteActionPerformed
        
        if(txt_id.getText().isEmpty()){
        JOptionPane.showMessageDialog(null, "Select Item to delete");
       }else{
          deleteArrearsCustomer();
       }
    }//GEN-LAST:event_deleteActionPerformed

    private void btn_SaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_SaveActionPerformed
        saveArrearsCustomer();
    }//GEN-LAST:event_btn_SaveActionPerformed

    private void btnNew2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnNew2ActionPerformed
        newArrearsCustomer();
    }//GEN-LAST:event_btnNew2ActionPerformed

    private void txt_AmountKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txt_AmountKeyReleased

    }//GEN-LAST:event_txt_AmountKeyReleased

    private void txt_AmountKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txt_AmountKeyPressed

    }//GEN-LAST:event_txt_AmountKeyPressed

    private void txt_AmountActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txt_AmountActionPerformed
        txt_Paid.requestFocusInWindow();
    }//GEN-LAST:event_txt_AmountActionPerformed

    private void txt_typeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txt_typeActionPerformed
        // TODO add your handling code here:
        txt_Amount.requestFocusInWindow();
    }//GEN-LAST:event_txt_typeActionPerformed

    private void txt_NameActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txt_NameActionPerformed
        txt_type.requestFocusInWindow();
    }//GEN-LAST:event_txt_NameActionPerformed

    private void Or_RefreshActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_Or_RefreshActionPerformed
        Order_table();
        Order_Colors();
    }//GEN-LAST:event_Or_RefreshActionPerformed

    private void Or_ClearActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_Or_ClearActionPerformed
        clearOrder();
         countOrder();
    }//GEN-LAST:event_Or_ClearActionPerformed

    private void txt_OrSearchKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txt_OrSearchKeyReleased
        searchTableOrderCustomer(txt_OrSearch.getText().toString());
    }//GEN-LAST:event_txt_OrSearchKeyReleased

    private void txt_OrSearchFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txt_OrSearchFocusLost
        txt_OrSearch.setText("Search...");
    }//GEN-LAST:event_txt_OrSearchFocusLost

    private void txt_OrSearchFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txt_OrSearchFocusGained
        txt_OrSearch.setText("");
    }//GEN-LAST:event_txt_OrSearchFocusGained

    private void Or_PrintActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_Or_PrintActionPerformed
        orderPrint();
    }//GEN-LAST:event_Or_PrintActionPerformed

    private void Or_DeleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_Or_DeleteActionPerformed

        if(id.getText().isEmpty()){
        JOptionPane.showMessageDialog(null, "Select Item to delete");
       }else{
          deleteCustomerOrder();
          countOrder();
       }
    }//GEN-LAST:event_Or_DeleteActionPerformed

    private void Or_NewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_Or_NewActionPerformed

        Or_type.setText("");
        Or_Cost.setText("");
        Or_Date.setCalendar(null);
    }//GEN-LAST:event_Or_NewActionPerformed

    private void Or_SaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_Or_SaveActionPerformed
        orderSave();
         countOrder();
    }//GEN-LAST:event_Or_SaveActionPerformed

    private void Or_idActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_Or_idActionPerformed
        // TODO add your handling code here:
        Or_type.requestFocusInWindow();
    }//GEN-LAST:event_Or_idActionPerformed

    private void Or_UpdateActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_Or_UpdateActionPerformed
        orderUpdate();
    }//GEN-LAST:event_Or_UpdateActionPerformed

    private void Or_typeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_Or_typeActionPerformed
        Or_Cost.requestFocusInWindow();
    }//GEN-LAST:event_Or_typeActionPerformed

    private void Or_CostActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_Or_CostActionPerformed
        Or_Save.requestFocusInWindow();
    }//GEN-LAST:event_Or_CostActionPerformed

    private void Order_TableKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_Order_TableKeyReleased
        if (evt.getKeyCode() == KeyEvent.VK_DOWN || evt.getKeyCode() == KeyEvent.VK_UP) {
            orderTableClick();
        }
    }//GEN-LAST:event_Order_TableKeyReleased

    private void Order_TableMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_Order_TableMouseClicked
        orderTableClick();
    }//GEN-LAST:event_Order_TableMouseClicked

    private void AllDaysTableKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_AllDaysTableKeyReleased
        if (evt.getKeyCode() == KeyEvent.VK_DOWN || evt.getKeyCode() == KeyEvent.VK_UP) {
            allDaySalesTableClick();
        }
    }//GEN-LAST:event_AllDaysTableKeyReleased

    private void AllDaysTableMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_AllDaysTableMouseClicked
        allDaySalesTableClick();
    }//GEN-LAST:event_AllDaysTableMouseClicked

    private void date_SearchActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_date_SearchActionPerformed
        searchByDate();
    }//GEN-LAST:event_date_SearchActionPerformed

    private void btnPrint1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPrint1ActionPerformed
        dailyPrint();
    }//GEN-LAST:event_btnPrint1ActionPerformed

    private void SelectDelete1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_SelectDelete1ActionPerformed
       
       if(lblTime1.getText().isEmpty()){
        JOptionPane.showMessageDialog(null, "Select Item to delete");
       }else{
           deleteSingleOne();
       }
    }//GEN-LAST:event_SelectDelete1ActionPerformed

    private void btn_AllDelete1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_AllDelete1ActionPerformed
        clearAllPermanent();
    }//GEN-LAST:event_btn_AllDelete1ActionPerformed

    private void txt_TotalSales1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txt_TotalSales1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txt_TotalSales1ActionPerformed

    private void txt_Search2KeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txt_Search2KeyReleased
        searchAllDaySales(txt_Search2.getText().toString());
        sumupAllDaySales();
        sumupAllDayQuantity();
        AllDaysSalesTableColor();
    }//GEN-LAST:event_txt_Search2KeyReleased

    private void txt_Search2FocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txt_Search2FocusLost
        txt_Search2.setText("Search Item...");
    }//GEN-LAST:event_txt_Search2FocusLost

    private void txt_Search2FocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txt_Search2FocusGained
        txt_Search2.setText("");
    }//GEN-LAST:event_txt_Search2FocusGained

    private void txt_Val4KeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txt_Val4KeyTyped
        // TODO add your handling code here:
    }//GEN-LAST:event_txt_Val4KeyTyped

    private void txt_Val4KeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txt_Val4KeyReleased
        // TODO add your handling code here:
    }//GEN-LAST:event_txt_Val4KeyReleased

    private void txt_Val4FocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txt_Val4FocusLost
        // TODO add your handling code here:
    }//GEN-LAST:event_txt_Val4FocusLost

    private void txt_Val4FocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txt_Val4FocusGained
        // TODO add your handling code here:
        txt_Val4.setText(" ");
    }//GEN-LAST:event_txt_Val4FocusGained

    private void txt_Val3KeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txt_Val3KeyReleased
        // TODO add your handling code here:
    }//GEN-LAST:event_txt_Val3KeyReleased

    private void txt_Val3KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txt_Val3KeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            searchByDate();
        }
    }//GEN-LAST:event_txt_Val3KeyPressed

    private void txt_Val3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txt_Val3ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txt_Val3ActionPerformed

    private void txt_Val3FocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txt_Val3FocusLost
        // TODO add your handling code here:
    }//GEN-LAST:event_txt_Val3FocusLost

    private void txt_Val3FocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txt_Val3FocusGained
        // TODO add your handling code here:
        txt_Val3.setText("");
    }//GEN-LAST:event_txt_Val3FocusGained

    private void txt_Refresh1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txt_Refresh1ActionPerformed
        AllDaysSalesTable();
        AllDaysSalesTableColor();
        sumupAllDayQuantity();
        sumupAllDaySales();
    }//GEN-LAST:event_txt_Refresh1ActionPerformed

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        try {
            rs.close();
            pst.close();
            Interface inter = new Interface();
            inter.setVisible(true);
            this.dispose();
        } catch (Exception e) {

        }
    }//GEN-LAST:event_jButton3ActionPerformed

    private void DailySalesTableKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_DailySalesTableKeyReleased
        dailySalesTableKeyReleased(evt);
    }//GEN-LAST:event_DailySalesTableKeyReleased

    private void DailySalesTableMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_DailySalesTableMouseClicked
        dailySalesTableClick();
    }//GEN-LAST:event_DailySalesTableMouseClicked

    private void btn_PreviewPrintActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_PreviewPrintActionPerformed
        PrintPreview ppt = new PrintPreview();
        ppt.printPreview();
        ppt.setVisible(true);
        PrintPreview.btnPrintDaily.setVisible(false);
        PrintPreview.btnPrintAdminAll.setVisible(false);
        PrintPreview.btnPrintFinish.setVisible(false);
    }//GEN-LAST:event_btn_PreviewPrintActionPerformed

    private void SelectDeleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_SelectDeleteActionPerformed
       if(lblTime.getText().isEmpty()){
        JOptionPane.showMessageDialog(null, "Select Item to delete");
       }else{
          deleteSingleDailySales();
       }
    }//GEN-LAST:event_SelectDeleteActionPerformed

    private void btn_AllDeleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_AllDeleteActionPerformed
        claerAllDailySales();
    }//GEN-LAST:event_btn_AllDeleteActionPerformed

    private void txt_Search1KeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txt_Search1KeyReleased
        searchTableDaily(txt_Search1.getText());
        sumupDaily();
    }//GEN-LAST:event_txt_Search1KeyReleased

    private void txt_Search1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txt_Search1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txt_Search1ActionPerformed

    private void txt_Search1FocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txt_Search1FocusLost
        txt_Search1.setText("Search Item...");
    }//GEN-LAST:event_txt_Search1FocusLost

    private void txt_Search1FocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txt_Search1FocusGained
        txt_Search1.setText("");
    }//GEN-LAST:event_txt_Search1FocusGained

    private void txt_RefreshActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txt_RefreshActionPerformed
        DailySalesTable();
        ViewDailySaleTableColor();
        sumupDaily();
    }//GEN-LAST:event_txt_RefreshActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        try {
            // conn.close();
            rs.close();
            pst.close();
            Interface inter = new Interface();
            inter.setVisible(true);
            this.dispose();
        } catch (Exception e) {

        }
    }//GEN-LAST:event_jButton1ActionPerformed

    private void ItemsTableKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_ItemsTableKeyReleased
        tableAllItemsKeyPress(evt);
    }//GEN-LAST:event_ItemsTableKeyReleased

    private void ItemsTableMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_ItemsTableMouseClicked
        itemsTableClick();
    }//GEN-LAST:event_ItemsTableMouseClicked

    private void ItemsTableFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_ItemsTableFocusLost

    }//GEN-LAST:event_ItemsTableFocusLost

    private void ItemsTableFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_ItemsTableFocusGained
        lbl_left.setEnabled(true);
        txt_Left.setEnabled(true);
    }//GEN-LAST:event_ItemsTableFocusGained

    private void btnRecordsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRecordsActionPerformed
        try {
            FinishingItemPane fin = new FinishingItemPane();
            fin.setVisible(true);
        } catch (Exception e) {

        }
    }//GEN-LAST:event_btnRecordsActionPerformed

    private void RefreshKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_RefreshKeyReleased
        if (evt.getKeyCode() == KeyEvent.VK_DOWN) {
            txt_Search.requestFocusInWindow();
        } else if (evt.getKeyCode() == KeyEvent.VK_UP) {
            Back.requestFocusInWindow();
        } else if (evt.getKeyCode() == KeyEvent.VK_RIGHT) {
            ItemsTable.requestFocusInWindow();
        } else if (evt.getKeyCode() == KeyEvent.VK_LEFT) {
            Back.requestFocusInWindow();
        }
    }//GEN-LAST:event_RefreshKeyReleased

    private void RefreshActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_RefreshActionPerformed
        AllItemsTable();
        FillCombo();
        TableColor1();
    }//GEN-LAST:event_RefreshActionPerformed

    private void BackKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_BackKeyReleased

        if (evt.getKeyCode() == KeyEvent.VK_DOWN) {
            txt_Search.requestFocusInWindow();
        } else if (evt.getKeyCode() == KeyEvent.VK_UP) {
            ItemsTable.requestFocusInWindow();
        } else if (evt.getKeyCode() == KeyEvent.VK_RIGHT) {
            Refresh.requestFocusInWindow();
        } else if (evt.getKeyCode() == KeyEvent.VK_LEFT) {
            ItemsTable.requestFocusInWindow();
        }
    }//GEN-LAST:event_BackKeyReleased

    private void BackActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BackActionPerformed
        try {
            // conn.close();
            rs.close();
            pst.close();
            Interface inter = new Interface();
            inter.setVisible(true);
            this.dispose();
        } catch (Exception e) {

        }
    }//GEN-LAST:event_BackActionPerformed

    private void btn_revertActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_revertActionPerformed
        int ask = JOptionPane.showConfirmDialog(null, "Do you want to Reverse Item", "Reverse", JOptionPane.YES_NO_OPTION);
        if (ask == 0) {
            try {
                ReverseItem rev = new ReverseItem();
                rev.setVisible(true);
            } catch (Exception e) {
            }
        }
    }//GEN-LAST:event_btn_revertActionPerformed

    private void txt_LeftActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txt_LeftActionPerformed
        //txt_Sell.requestFocusInWindow();
    }//GEN-LAST:event_txt_LeftActionPerformed

    private void txt_LeftFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txt_LeftFocusLost

    }//GEN-LAST:event_txt_LeftFocusLost

    private void txt_LeftFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txt_LeftFocusGained

    }//GEN-LAST:event_txt_LeftFocusGained

    private void txt_TTQuantityKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txt_TTQuantityKeyReleased
        if (evt.getKeyCode() == KeyEvent.VK_DOWN) {
            txt_whoelsale.requestFocusInWindow();
        } else if (evt.getKeyCode() == KeyEvent.VK_UP) {
            txt_ItName.requestFocusInWindow();
        } else if (evt.getKeyCode() == KeyEvent.VK_RIGHT) {
            ItemsTable.requestFocusInWindow();
        } else if (evt.getKeyCode() == KeyEvent.VK_LEFT) {
            ItemsTable.requestFocusInWindow();
        }
    }//GEN-LAST:event_txt_TTQuantityKeyReleased

    private void txt_TTQuantityActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txt_TTQuantityActionPerformed
        txt_whoelsale.requestFocusInWindow();
    }//GEN-LAST:event_txt_TTQuantityActionPerformed

    private void txt_SearchKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txt_SearchKeyReleased
        // Update_table();
        searchTableContent2(txt_Search.getText().toString());

        if (evt.getKeyCode() == KeyEvent.VK_DOWN) {
            txt_ItName.requestFocusInWindow();
        } else if (evt.getKeyCode() == KeyEvent.VK_UP) {
            Refresh.requestFocusInWindow();
        } else if (evt.getKeyCode() == KeyEvent.VK_RIGHT) {
            ItemsTable.requestFocusInWindow();
        } else if (evt.getKeyCode() == KeyEvent.VK_LEFT) {
            ItemsTable.requestFocusInWindow();
        }

    }//GEN-LAST:event_txt_SearchKeyReleased

    private void txt_SearchActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txt_SearchActionPerformed
        txt_ItName.requestFocusInWindow();
        TableColor1();
    }//GEN-LAST:event_txt_SearchActionPerformed

    private void txt_SearchFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txt_SearchFocusLost
        // TODO add your handling code here:
        txt_Search.setText("Search...");
    }//GEN-LAST:event_txt_SearchFocusLost

    private void txt_SearchFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txt_SearchFocusGained
        txt_Search.setText("");
    }//GEN-LAST:event_txt_SearchFocusGained

    private void cmb_NewKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_cmb_NewKeyReleased
        if (evt.getKeyCode() == KeyEvent.VK_DOWN) {
            cmb_Save.requestFocusInWindow();
        } else if (evt.getKeyCode() == KeyEvent.VK_UP) {
            txt_whoelsale.requestFocusInWindow();
        } else if (evt.getKeyCode() == KeyEvent.VK_RIGHT) {
            cmb_Save.requestFocusInWindow();
        } else if (evt.getKeyCode() == KeyEvent.VK_LEFT) {
            Refresh.requestFocusInWindow();
        }
    }//GEN-LAST:event_cmb_NewKeyReleased

    private void cmb_NewKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_cmb_NewKeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            txt_ItName.setText(" ");
            txt_whoelsale.setText(" ");
            txt_TTQuantity.setText(" ");

        }
    }//GEN-LAST:event_cmb_NewKeyPressed

    private void cmb_NewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmb_NewActionPerformed
        newItem();
    }//GEN-LAST:event_cmb_NewActionPerformed

    private void txt_whoelsaleKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txt_whoelsaleKeyTyped
        char c = evt.getKeyChar();
        if (!(Character.isDigit(c) || (c == KeyEvent.VK_BACK_SPACE) || (c == KeyEvent.VK_DELETE) || (c == KeyEvent.VK_PERIOD))) {
            getToolkit().beep();
            evt.consume();
        }
    }//GEN-LAST:event_txt_whoelsaleKeyTyped

    private void txt_whoelsaleKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txt_whoelsaleKeyReleased

        if (evt.getKeyCode() == KeyEvent.VK_DOWN) {
            cmb_Save.requestFocusInWindow();
        } else if (evt.getKeyCode() == KeyEvent.VK_UP) {
            txt_TTQuantity.requestFocusInWindow();
        } else if (evt.getKeyCode() == KeyEvent.VK_RIGHT) {
            ItemsTable.requestFocusInWindow();
        } else if (evt.getKeyCode() == KeyEvent.VK_LEFT) {
            ItemsTable.requestFocusInWindow();
        }
    }//GEN-LAST:event_txt_whoelsaleKeyReleased

    private void txt_whoelsaleActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txt_whoelsaleActionPerformed
        // TODO add your handling code here:
        cmb_Save.requestFocusInWindow();
    }//GEN-LAST:event_txt_whoelsaleActionPerformed

    private void txt_ItNameKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txt_ItNameKeyTyped

    }//GEN-LAST:event_txt_ItNameKeyTyped

    private void txt_ItNameKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txt_ItNameKeyReleased
        if (evt.getKeyCode() == KeyEvent.VK_DOWN) {
            txt_TTQuantity.requestFocusInWindow();
        } else if (evt.getKeyCode() == KeyEvent.VK_UP) {
            txt_Search.requestFocusInWindow();
        } else if (evt.getKeyCode() == KeyEvent.VK_RIGHT) {
            ItemsTable.requestFocusInWindow();
        } else if (evt.getKeyCode() == KeyEvent.VK_LEFT) {
            ItemsTable.requestFocusInWindow();
        }
    }//GEN-LAST:event_txt_ItNameKeyReleased

    private void txt_ItNameActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txt_ItNameActionPerformed
        txt_TTQuantity.requestFocusInWindow();
    }//GEN-LAST:event_txt_ItNameActionPerformed

    private void cmb_DeleteKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_cmb_DeleteKeyReleased
        if (evt.getKeyCode() == KeyEvent.VK_DOWN) {
            cmb_Update.requestFocusInWindow();
        } else if (evt.getKeyCode() == KeyEvent.VK_UP) {
            cmb_Save.requestFocusInWindow();
        } else if (evt.getKeyCode() == KeyEvent.VK_RIGHT) {
            cmb_Update.requestFocusInWindow();
        } else if (evt.getKeyCode() == KeyEvent.VK_LEFT) {
            cmb_Save.requestFocusInWindow();
        }
    }//GEN-LAST:event_cmb_DeleteKeyReleased

    private void cmb_DeleteKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_cmb_DeleteKeyPressed
        deleteSingle();
    }//GEN-LAST:event_cmb_DeleteKeyPressed

    private void cmb_DeleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmb_DeleteActionPerformed
       if(txt_ItName.getText().isEmpty()){
        JOptionPane.showMessageDialog(null, "Select Item to delete");
       }else{
          deleteSingle();
       }
    }//GEN-LAST:event_cmb_DeleteActionPerformed

    private void cmb_SaveKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_cmb_SaveKeyReleased
        if (evt.getKeyCode() == KeyEvent.VK_DOWN) {
            cmb_Delete.requestFocusInWindow();
        } else if (evt.getKeyCode() == KeyEvent.VK_UP) {
            cmb_New.requestFocusInWindow();
        } else if (evt.getKeyCode() == KeyEvent.VK_RIGHT) {
            cmb_Delete.requestFocusInWindow();
        } else if (evt.getKeyCode() == KeyEvent.VK_LEFT) {
            cmb_New.requestFocusInWindow();
        }
    }//GEN-LAST:event_cmb_SaveKeyReleased

    private void cmb_SaveKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_cmb_SaveKeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            String Item = txt_ItName.getText();
            if (Item.isEmpty() || txt_TTQuantity.getText() == "" || txt_whoelsale.getText() == "") {
                JOptionPane.showMessageDialog(null, "Fill Required Spaces Before Saving", "WARNING", JOptionPane.ERROR_MESSAGE);
            } else {
                Update();
                AllItemsTable();
            }

        }
    }//GEN-LAST:event_cmb_SaveKeyPressed

    private void cmb_SaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmb_SaveActionPerformed
        Saved();
        AllItemsTable();
        TableColor1();
    }//GEN-LAST:event_cmb_SaveActionPerformed

    private void cmb_UpdateKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_cmb_UpdateKeyReleased
        if (evt.getKeyCode() == KeyEvent.VK_DOWN) {
            ItemsTable.requestFocusInWindow();
        } else if (evt.getKeyCode() == KeyEvent.VK_UP) {
            txt_whoelsale.requestFocusInWindow();
        } else if (evt.getKeyCode() == KeyEvent.VK_RIGHT) {
            ItemsTable.requestFocusInWindow();
        } else if (evt.getKeyCode() == KeyEvent.VK_LEFT) {
            cmb_Delete.requestFocusInWindow();
        }
    }//GEN-LAST:event_cmb_UpdateKeyReleased

    private void cmb_UpdateKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_cmb_UpdateKeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            Update();
            AllItemsTable();
        }
    }//GEN-LAST:event_cmb_UpdateKeyPressed

    private void cmb_UpdateActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmb_UpdateActionPerformed
        Update();
        AllItemsTable();
        TableColor1();
    }//GEN-LAST:event_cmb_UpdateActionPerformed

    private void txt_TTQuantityKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txt_TTQuantityKeyPressed

    }//GEN-LAST:event_txt_TTQuantityKeyPressed

    private void txt_TTQuantityFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txt_TTQuantityFocusLost
        quantityAdd();
    }//GEN-LAST:event_txt_TTQuantityFocusLost

    private void table_ArrearsKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_table_ArrearsKeyReleased
        if (evt.getKeyCode() == KeyEvent.VK_DOWN || evt.getKeyCode() == KeyEvent.VK_UP) {
            tableArrearsClick();
        }
    }//GEN-LAST:event_table_ArrearsKeyReleased

    private void table_ArrearsMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_table_ArrearsMouseClicked
        tableArrearsClick();
    }//GEN-LAST:event_table_ArrearsMouseClicked

    private void jMenuItem1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem1ActionPerformed
        try {

            rs.close();
            pst.close();
            Category cat = new Category();
            cat.setVisible(true);
            this.dispose();

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Ooops");
        }
    }//GEN-LAST:event_jMenuItem1ActionPerformed

    private void txt_CategoryFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txt_CategoryFocusGained

    }//GEN-LAST:event_txt_CategoryFocusGained

    private void txt_CategoryActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txt_CategoryActionPerformed

    }//GEN-LAST:event_txt_CategoryActionPerformed

    private void txt_retailActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txt_retailActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txt_retailActionPerformed

    private void txt_retailKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txt_retailKeyReleased
        // TODO add your handling code here:
    }//GEN-LAST:event_txt_retailKeyReleased

    private void txt_retailKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txt_retailKeyTyped
        // TODO add your handling code here:
    }//GEN-LAST:event_txt_retailKeyTyped

    private void btn_PreviewAllActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_PreviewAllActionPerformed
        preview();
        PrintPreview.btnPrintDailyAdmin.setVisible(false);
        PrintPreview.btnPrintDaily.setVisible(false);
        PrintPreview.btnPrintFinish.setVisible(false);
    }//GEN-LAST:event_btn_PreviewAllActionPerformed

    private void usernameActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_usernameActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_usernameActionPerformed

    private void passwordActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_passwordActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_passwordActionPerformed

    private void fullnameKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_fullnameKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_fullnameKeyPressed

    private void chkActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_chkActionPerformed
        if (chk.isSelected()) {
            password.setEchoChar((char) 0);
        } else {
            password.setEchoChar('*');
        }
    }//GEN-LAST:event_chkActionPerformed

    private void loginTableMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_loginTableMouseClicked
        accountsClick();
    }//GEN-LAST:event_loginTableMouseClicked

    private void loginTableKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_loginTableKeyReleased
        if (evt.getKeyCode() == KeyEvent.VK_DOWN || evt.getKeyCode() == KeyEvent.VK_UP) {
            accountsClick();
        }
    }//GEN-LAST:event_loginTableKeyReleased

    private void rdUserActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rdUserActionPerformed
        account = "user";
    }//GEN-LAST:event_rdUserActionPerformed

    private void rdAdminActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rdAdminActionPerformed
        account = "admin";
    }//GEN-LAST:event_rdAdminActionPerformed

    private void asaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_asaveActionPerformed
        saveaccount();
    }//GEN-LAST:event_asaveActionPerformed

    private void anewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_anewActionPerformed
        newaccount();
    }//GEN-LAST:event_anewActionPerformed

    private void adeleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_adeleteActionPerformed
        deleteaccount();
    }//GEN-LAST:event_adeleteActionPerformed

    private void aupdateActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_aupdateActionPerformed
        updateAccounts();
    }//GEN-LAST:event_aupdateActionPerformed

    private void txt_ItNameFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txt_ItNameFocusGained
        txt_Left.setText("0");
    }//GEN-LAST:event_txt_ItNameFocusGained

    private void menuClearAllActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuClearAllActionPerformed
        clear();
    }//GEN-LAST:event_menuClearAllActionPerformed

    private void quantity_baughtActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_quantity_baughtActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_quantity_baughtActionPerformed

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
            java.util.logging.Logger.getLogger(AdminPanel.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(AdminPanel.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(AdminPanel.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(AdminPanel.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        UIManager.setLookAndFeel("com.jtattoo.plaf.aluminium.AluminiumLookAndFeel");
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new AdminPanel().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTabbedPane AdminPane;
    public static javax.swing.JTable AllDaysTable;
    private javax.swing.JButton Back;
    public static javax.swing.JTable DailySalesTable;
    private javax.swing.JTextArea Description;
    private javax.swing.JTable ItemsTable;
    private javax.swing.JPanel Money;
    private javax.swing.JPanel New2;
    private javax.swing.JPanel NewItem;
    private javax.swing.JButton Or_Clear;
    private javax.swing.JTextField Or_Cost;
    private com.toedter.calendar.JDateChooser Or_Date;
    private javax.swing.JButton Or_Delete;
    private javax.swing.JButton Or_New;
    private javax.swing.JButton Or_Print;
    private javax.swing.JButton Or_Refresh;
    private javax.swing.JButton Or_Save;
    private javax.swing.JButton Or_Update;
    private javax.swing.JTextField Or_id;
    private javax.swing.JTextField Or_type;
    private javax.swing.JTable Order_Table;
    private javax.swing.JButton Refresh;
    private javax.swing.JButton SelectDelete;
    private javax.swing.JButton SelectDelete1;
    private javax.swing.JButton adelete;
    private javax.swing.JButton anew;
    private javax.swing.JButton arrearsClear;
    private javax.swing.JButton asave;
    private javax.swing.JButton aupdate;
    private javax.swing.JButton btnNew2;
    private javax.swing.JButton btnPrint1;
    private javax.swing.JButton btnRecords;
    private javax.swing.JButton btn_AllDelete;
    private javax.swing.JButton btn_AllDelete1;
    private javax.swing.JButton btn_Arrears;
    private javax.swing.JButton btn_PreviewAll;
    private javax.swing.JButton btn_PreviewPrint;
    private javax.swing.JButton btn_Save;
    private javax.swing.JButton btn_revert;
    private javax.swing.ButtonGroup buttonGroup1;
    private javax.swing.ButtonGroup buttonGroup2;
    private javax.swing.JCheckBox chk;
    private javax.swing.JButton cmb_Delete;
    private javax.swing.JButton cmb_New;
    private javax.swing.JButton cmb_Save;
    private javax.swing.JButton cmb_Update;
    private javax.swing.JButton date_Search;
    private javax.swing.JButton delete;
    private javax.swing.JTextField fullname;
    private javax.swing.JLabel id;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton6;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel20;
    private javax.swing.JLabel jLabel25;
    private javax.swing.JLabel jLabel26;
    private javax.swing.JLabel jLabel27;
    private javax.swing.JLabel jLabel28;
    private javax.swing.JLabel jLabel29;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel30;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JMenu jMenu1;
    private javax.swing.JMenu jMenu2;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JMenuItem jMenuItem1;
    private javax.swing.JMenuItem jMenuItem10;
    private javax.swing.JMenuItem jMenuItem11;
    private javax.swing.JMenuItem jMenuItem12;
    private javax.swing.JMenuItem jMenuItem2;
    private javax.swing.JMenuItem jMenuItem3;
    private javax.swing.JMenuItem jMenuItem4;
    private javax.swing.JMenuItem jMenuItem5;
    private javax.swing.JMenuItem jMenuItem6;
    private javax.swing.JMenuItem jMenuItem7;
    private javax.swing.JMenuItem jMenuItem8;
    private javax.swing.JMenuItem jMenuItem9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel10;
    private javax.swing.JPanel jPanel11;
    private javax.swing.JPanel jPanel12;
    private javax.swing.JPanel jPanel13;
    private javax.swing.JPanel jPanel14;
    private javax.swing.JPanel jPanel15;
    private javax.swing.JPanel jPanel16;
    private javax.swing.JPanel jPanel17;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel24;
    private javax.swing.JPanel jPanel25;
    private javax.swing.JPanel jPanel26;
    private javax.swing.JPanel jPanel27;
    private javax.swing.JPanel jPanel28;
    private javax.swing.JPanel jPanel29;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel30;
    private javax.swing.JPanel jPanel31;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JPanel jPanel9;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane10;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JScrollPane jScrollPane5;
    private javax.swing.JScrollPane jScrollPane6;
    private javax.swing.JScrollPane jScrollPane7;
    private javax.swing.JScrollPane jScrollPane8;
    private javax.swing.JPopupMenu.Separator jSeparator1;
    private javax.swing.JPopupMenu.Separator jSeparator10;
    private javax.swing.JPopupMenu.Separator jSeparator11;
    private javax.swing.JPopupMenu.Separator jSeparator12;
    private javax.swing.JPopupMenu.Separator jSeparator13;
    private javax.swing.JPopupMenu.Separator jSeparator14;
    private javax.swing.JPopupMenu.Separator jSeparator15;
    private javax.swing.JPopupMenu.Separator jSeparator16;
    private javax.swing.JPopupMenu.Separator jSeparator17;
    private javax.swing.JPopupMenu.Separator jSeparator18;
    private javax.swing.JPopupMenu.Separator jSeparator19;
    private javax.swing.JPopupMenu.Separator jSeparator20;
    private javax.swing.JSeparator jSeparator21;
    private javax.swing.JSeparator jSeparator22;
    private javax.swing.JSeparator jSeparator23;
    private javax.swing.JSeparator jSeparator24;
    private javax.swing.JSeparator jSeparator29;
    private javax.swing.JPopupMenu.Separator jSeparator30;
    private javax.swing.JPopupMenu.Separator jSeparator31;
    private javax.swing.JSeparator jSeparator32;
    private javax.swing.JSeparator jSeparator33;
    private javax.swing.JSeparator jSeparator34;
    private javax.swing.JPopupMenu.Separator jSeparator7;
    private javax.swing.JPopupMenu.Separator jSeparator8;
    private javax.swing.JPopupMenu.Separator jSeparator9;
    private javax.swing.JTable jTable1;
    public static javax.swing.JLabel lblDate;
    private javax.swing.JLabel lblDate1;
    private javax.swing.JLabel lblID;
    private javax.swing.JLabel lblTime;
    private javax.swing.JLabel lblTime1;
    public static javax.swing.JLabel lbl_Date;
    private javax.swing.JLabel lbl_Date1;
    private javax.swing.JLabel lbl_Remain;
    private javax.swing.JLabel lbl_Time;
    private javax.swing.JLabel lbl_Timer;
    private javax.swing.JLabel lbl_To1;
    private javax.swing.JLabel lbl_left;
    private javax.swing.JLabel lbl_pay;
    private javax.swing.JLabel lbl_saved;
    private javax.swing.JLabel lbl_tLeft;
    private javax.swing.JTable loginTable;
    private javax.swing.JMenuItem menuClearAll;
    private javax.swing.JRadioButton notPaid;
    private javax.swing.JRadioButton paid;
    private javax.swing.JTabbedPane panePreview;
    private javax.swing.JPasswordField password;
    public static javax.swing.JTextField quantity_baught;
    private javax.swing.JRadioButton rdAdmin;
    private javax.swing.JRadioButton rdUser;
    private javax.swing.JTable table_Arrears;
    private javax.swing.JTextField tt_SP;
    private javax.swing.JTextField tt_item;
    private javax.swing.JTextField tt_left;
    private javax.swing.JTextField txt_Amount;
    public static javax.swing.JComboBox<String> txt_Category;
    private com.toedter.calendar.JDateChooser txt_Date;
    private javax.swing.JTextField txt_ItName;
    private javax.swing.JTextField txt_Left;
    private javax.swing.JTextField txt_Name;
    private javax.swing.JMenuItem txt_New;
    private javax.swing.JTextField txt_OrSearch;
    private javax.swing.JTextField txt_Paid;
    private javax.swing.JButton txt_Refresh;
    private javax.swing.JButton txt_Refresh1;
    private javax.swing.JTextField txt_Remain;
    private javax.swing.JTextField txt_Search;
    private javax.swing.JTextField txt_Search1;
    private javax.swing.JTextField txt_Search2;
    private javax.swing.JTextField txt_TTQuantity;
    private javax.swing.JTextField txt_Tatal;
    public static javax.swing.JTextField txt_TotalSales;
    public static javax.swing.JTextField txt_TotalSales1;
    private javax.swing.JTextField txt_Val3;
    private javax.swing.JTextField txt_Val4;
    private javax.swing.JTextField txt_id;
    private javax.swing.JTextField txt_retail;
    private javax.swing.JTextField txt_searchKeep;
    private javax.swing.JTextField txt_type;
    private javax.swing.JButton txt_update;
    private javax.swing.JTextField txt_whoelsale;
    private javax.swing.JTextField username;
    // End of variables declaration//GEN-END:variables

    private String status;
    private String account;
}
