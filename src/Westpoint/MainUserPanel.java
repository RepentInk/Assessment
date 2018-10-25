package Westpoint;

import static Westpoint.AdminPanel.txt_Category;
import Intel.connectDB;
import Westpoint.Interface;
import java.awt.Color;
import java.awt.Component;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Vector;
import javax.imageio.ImageIO;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.Timer;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import net.proteanit.sql.DbUtils;

/**
 *
 * @author NyarkoPC
 */
public class MainUserPanel extends javax.swing.JFrame {

    Connection conn = null;
    ResultSet rs = null;
    PreparedStatement pst = null;
    Vector originalTableModel;
    Vector wholesaleTM;
    Vector tryPrintPanel;

    public MainUserPanel() {
        initComponents();
        //when you want windows to be display in full screen
//        this.setExtendedState(JFrame.MAXIMIZED_BOTH);
        conn = connectDB.ConnecrDb();

        try {
            Image i = ImageIO.read(getClass().getResource("/image/doubleUU.jpg"));
            setIconImage(i);
        } catch (Exception e) {

        }

        TotDaily(); //Method to add daily sales
        //Calling methods created in program
        StartTim();

        Update_table();
        FillCombo();    //Populating Combobox with names of users
        TTCostP(); // Method to sumup

        TempTable();
        FillCategory(); // Filling the category in User Panel

        wholesaleTM = (Vector) ((DefaultTableModel) UItemsTable.getModel()).getDataVector().clone();

        tryPrintPanel = (Vector) ((DefaultTableModel) temp_Table.getModel()).getDataVector().clone();
        TableColor(); // Table Color Method
        timeBought.setVisible(false);

        Receipt.setEnabled(false);

        txt_Itemname.setText("Item Name");
        txt_cost.setText("0.0");
    }

    //Method to set time and date
    public void StartTim() {

        Timer t = new Timer(1000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                SimpleDateFormat sdf = new SimpleDateFormat(" HH:mm:ss a");
                SimpleDateFormat sdf2 = new SimpleDateFormat("MMM EE d, yyy");
                lbl_Time1.setText(sdf2.format(new java.util.Date()));
                tt_time.setText(sdf.format(new java.util.Date()));
            }
        });
        t.start();
    }

    //Method to provide colors to table
    public void TableColor() {
        UItemsTable.getColumnModel().getColumn(0).setCellRenderer(new MainUserPanel.CustomRenderer());
        UItemsTable.getColumnModel().getColumn(1).setCellRenderer(new MainUserPanel.CustomRenderer1());
        UItemsTable.getColumnModel().getColumn(2).setCellRenderer(new MainUserPanel.CustomRenderer2());
        UItemsTable.getTableHeader().setDefaultRenderer(new MainUserPanel.HeaderColor());

        temp_Table.getTableHeader().setDefaultRenderer(new MainUserPanel.HeaderColor());
    }

    //Method that populate sales table with data
    public void Update_table() {

        DefaultTableModel model = (DefaultTableModel) UItemsTable.getModel();
        model.setRowCount(0);

        Object[] object;

        try {

            if (rretail.isSelected()) {

                String sql = "select * from AllItems_Db order by Items ASC";
                pst = conn.prepareStatement(sql);
                rs = pst.executeQuery();

                while (rs.next()) {
                    String add1 = rs.getString("Items");
                    String add2 = rs.getString("retail");
                    String add3 = rs.getString("wholesale");
                    object = new Object[]{add1, add2, add3};

                    model.addRow(object);
                }

            } else {

                String sql = "select * from AllItems_Db order by Items ASC";
                pst = conn.prepareStatement(sql);
                rs = pst.executeQuery();

                while (rs.next()) {
                    String add1 = rs.getString("Items");
                    String add2 = rs.getString("retail");
                    String add3 = rs.getString("wholesale");
                    object = new Object[]{add1, add2, add3};

                    model.addRow(object);
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

    //Method that populate items buying table with data
    private void TempTable() {
        DefaultTableModel model = (DefaultTableModel) temp_Table.getModel();
        model.setRowCount(0);

        Object[] object;

        try {
            String sql = "select Item,Qty,Cost from temp_DB order by Item ASC";
            pst = conn.prepareStatement(sql);
            rs = pst.executeQuery();

            while (rs.next()) {
                String add1 = rs.getString("Item");
                String add2 = rs.getString("Qty");
                String add3 = rs.getString("Cost");

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

    //Searching for item in table method starts here
    public void searchWholesale(String SearchString) {
        try {

            Update_table();

            DefaultTableModel currtableModel = (DefaultTableModel) UItemsTable.getModel();
            currtableModel.setRowCount(0);

            for (Object rows2 : wholesaleTM) {
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

    // Method that set color to table
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

    // Method that set color to table
    public static class CustomRenderer1 extends DefaultTableCellRenderer {

        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            Component cellComponent = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
            if (column == 1) {
                cellComponent.setForeground(Color.BLUE);
                cellComponent.setFont(new java.awt.Font("Tahoma", 1, 12));
            }
            return cellComponent;
        }
    }

    // Method that set color to table
    public static class CustomRenderer2 extends DefaultTableCellRenderer {

        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            Component cellComponent = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
            if (column == 2) {
                cellComponent.setForeground(Color.RED);
                cellComponent.setFont(new java.awt.Font("Tahoma", 1, 12));
            }
            return cellComponent;
        }
    }

    // Method that set color to table
    public static class CustomRenderer3 extends DefaultTableCellRenderer {

        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            Component cellComponent = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
            if (column == 3) {
                cellComponent.setForeground(Color.BLUE);
                cellComponent.setFont(new java.awt.Font("Tahoma", 1, 12));
            }
            return cellComponent;
        }
    }

    // Method that set color to table
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

    // Method that set color to table
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

    // Method that set color to table header
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

    //method that refreshes quantity
    public void UpdateQty() {

        String Val1 = txt_Itemname.getText();
        String Val2 = txt_QtyL.getText();
        try {
            String sql = "update AllItems_DB set Items='" + Val1 + "',Qty_left='" + Val2 + "' where Items='" + Val1 + "'";
            pst = conn.prepareStatement(sql);
            pst.executeUpdate();

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
    private void FillCombo() {
        
        String user = "user";
        try {
            String sql = "select fullname from accounts where status='"+ user +"' order by fullname ASC";
            pst = conn.prepareStatement(sql);
            rs = pst.executeQuery();

            while (rs.next()) {
                String name = rs.getString("fullname");
                Cmd_User.addItem(name);
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
    public void FillCategory() {
        try {
            String sql = "select name from Category_DB";
            pst = conn.prepareStatement(sql);
            rs = pst.executeQuery();

            DefaultComboBoxModel cmodel = (DefaultComboBoxModel) cmd_Category.getModel();
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

    //Method that populate sales table with data
    public void UpdateCategory() {

        DefaultTableModel model = (DefaultTableModel) UItemsTable.getModel();
        model.setRowCount(0);

        int cat = cmd_Category.getSelectedIndex();

        Object[] object;

        if (cat == 0) {

        } else {

            try {

                if (rwhole.isSelected()) {

                    String sql = "select Items,wholesale,retail from AllItems_Db WHERE Category='" + cat + "' order by Items ASC";
                    pst = conn.prepareStatement(sql);
                    rs = pst.executeQuery();

                    while (rs.next()) {
                        String add1 = rs.getString("Items");
                        String add2 = rs.getString("retail");
                        String add3 = rs.getString("wholesale");

                        object = new Object[]{add1, add2, add3};

                        model.addRow(object);
                    }

                } else if (rretail.isSelected()) {

                    String sql = "select Items,retail,wholesale from AllItems_Db WHERE Category='" + cat + "' order by Items ASC";
                    pst = conn.prepareStatement(sql);
                    rs = pst.executeQuery();

                    while (rs.next()) {
                        String add1 = rs.getString("Items");
                        String add2 = rs.getString("retail");
                        String add3 = rs.getString("wholesale");

                        object = new Object[]{add1, add2, add3};

                        model.addRow(object);
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
    }

    //Method that sum up cost and set it to txt_TotalBought
    private void TTCostP() {
        try {
            String sql = "Select sum(Cost) from temp_DB";
            pst = conn.prepareStatement(sql);
            rs = pst.executeQuery();
            if (rs.next()) {
                String sum = rs.getString("sum(Cost)");
                txt_TotalBought.setText(sum);
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

    //Method that sum up all Items bought
    private void TotDaily() {
        String dateA = lbl_Time1.getText().toLowerCase().trim();
        try {
            String sql = "Select sum(Tot_Cost) from DailySales_DB where Date='" + dateA + "'";
            pst = conn.prepareStatement(sql);
            rs = pst.executeQuery();
            if (rs.next()) {
                String sum = rs.getString("sum(Tot_Cost)");
                TTS_Day.setText(sum);
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

    //Method that delete selected item from temp table
    public void deleteTemp(String timeBought) {
        String sql = "delete from temp_DB where Time='" + timeBought + "'";
        try {
            pst = conn.prepareStatement(sql);
            pst.executeUpdate();

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

    //Method that delete selected item from daily table
    public void deleteDaily(String timeBought) {
        String sqlDaily = "delete from DailySales_DB where Time='" + timeBought + "'";
        try {
            pst = conn.prepareStatement(sqlDaily);
            pst.executeUpdate();

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

    //Method that delete selected item from permanent table
    public void deletePermanent(String timeBought) {
        String sqlPer = "delete from Permanent_DB where Time='" + timeBought + "'";
        try {
            pst = conn.prepareStatement(sqlPer);
            pst.executeUpdate();
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

    //Calling all delete methods
    public void DeleteSelect() {
        String timB = timeBought.getText();

        if (timB.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Select Item from table to reverse");
        } else {
            deleteTemp(timB);// delete from temp db method call
            deleteDaily(timB); // delete from daily db method call
            deletePermanent(timB);// deletee from permanent table method call
            TempTable();
            TTCostP();
            TotDaily();
        }
    }

    //Method that refresh balance
    public void BalanceRefresh() {
        DecimalFormat df2 = new DecimalFormat("####.###");
        txt_TotalPaid.setBackground(Color.white);

        double numba1 = 0, numba2 = 0, total = 0;
        numba1 = Double.parseDouble(txt_TotalBought.getText());
        numba2 = Double.parseDouble(txt_TotalPaid.getText());
        total = Double.valueOf(df2.format(numba2 - numba1));
        txt_balance.setText(Double.toString(total));
    }

    //Clearing all data from temp db
    public void clearAllFromTemp() {
        String sql = "delete from temp_DB";
        try {
            pst = conn.prepareStatement(sql);
            pst.execute();

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

    //New customer method
    public void NewCustomer() {
        TablesPane.setSelectedIndex(0);

        if (txt_TotalBought.getText().isEmpty() && txt_TotalPaid.getText().isEmpty() && txt_balance.getText().isEmpty()) {
            JOptionPane.showMessageDialog(null, "No Sales Made Yet", "New Customer", JOptionPane.INFORMATION_MESSAGE);
        } else {
            tt_cost.setText("");
            txt_balance.setText("");
            txt_TotalPaid.setText("");
            txt_TotalBought.setText("");
        }
        clearAllFromTemp();
        TempTable();
        TTCostP();
    }

    //Return quantity left
    public String returnQuantity() {
        String add1 = null;
        try {
            int row = temp_Table.getSelectedRow();
            String Val1 = temp_Table.getModel().getValueAt(row, 0).toString();

            String val = "select Qty_Left from AllItems_DB where Items='" + Val1 + "'";
            pst = conn.prepareStatement(val);
            rs = pst.executeQuery();
            add1 = rs.getString("Qty_Left").trim();

            return add1;

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
        return add1;
    }

    //updating quantity
    public void returnFinial() {
        try {
            int row = temp_Table.getSelectedRow();
            String Val1 = temp_Table.getModel().getValueAt(row, 0).toString();
            String Val2 = temp_Table.getModel().getValueAt(row, 1).toString();

            String add1 = returnQuantity();
            int addMe = Integer.parseInt(Val2) + Integer.parseInt(add1);

            String sql = "update AllItems_DB set Items='" + Val1 + "',Qty_left='" + addMe + "' where Items='" + Val1 + "'";
            pst = conn.prepareStatement(sql);
            pst.executeUpdate();

        } catch (Exception e) {
//            JOptionPane.showMessageDialog(null, "Ooops Select Item");
        } finally {
            try {
                rs.close();
                pst.close();
            } catch (Exception e) {
//                JOptionPane.showMessageDialog(null, "Ooops Select Item");
            }
        }
    }

    //Adding the sale process method
    public void AddUp() {
        returnFinial();
        TableColor();
    }

    //Method that help you to decide wheather to print receipt or not
    public void checkBtn() {
        if (check.isSelected()) {
            Receipt.setEnabled(true);
            btn_Customer.setEnabled(false);
        } else {
            Receipt.setEnabled(false);
            btn_Customer.setEnabled(true);
        }
    }

    //Item table mouse click
    public void tableMouseClick() {
        int row = UItemsTable.getSelectedRow();
        String Table_click = (UItemsTable.getModel().getValueAt(row, 0).toString());

        try {
            String sql = "select * from AllItems_DB where Items='" + Table_click + "'";
            pst = conn.prepareStatement(sql);
            rs = pst.executeQuery();
            if (rs.next()) {
                String add2 = "";
                String add1 = rs.getString("Items");
                txt_Itemname.setText(add1);

                if (rwhole.isSelected()) {
                    add2 = rs.getString("wholesale");
                } else if (rretail.isSelected()) {
                    add2 = rs.getString("retail");
                } else {
                    add2 = rs.getString("wholesale");
                }

                txt_cost.setText(add2);
                String add3 = rs.getString("Qty_left");
                txt_QtyL.setText(add3);
                int add4 = rs.getInt("Category");
                cmd_Category.setSelectedIndex(add4);

                cmd_quentity.setSelectedIndex(0);
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

    //method that show finish item form
    public void viewFinish() {
        try {
            FinishingItemPane fin = new FinishingItemPane();
            fin.setVisible(true);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
        }
    }

    //method that show all records form
    public void viewRecords() {
        try {
            rs.close();
            pst.close();
            RecordsKeepingPane records = new RecordsKeepingPane();
            records.setVisible(true);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
        }
    }

    //method tha show daily sales form
    public void viewDailySales() {
        try {
            SalesForDay DailySales = new SalesForDay();
            DailySales.setVisible(true);
        } catch (Exception e) {

        }
    }

    //saving item into permanent
    public void savePermanent() {
        try {
            String sql = "Insert into Permanent_DB (Item,Cost,Qty,Tot_Cost,Date,Sold_by,Time) values (?,?,?,?,?,?,?)";
            pst = conn.prepareStatement(sql);

            pst.setString(1, txt_Itemname.getText().toLowerCase().trim());
            pst.setString(2, txt_cost.getText().trim());
            pst.setString(3, cmd_quentity.getSelectedItem().toString().trim());
            pst.setString(4, tt_cost.getText().trim());
            pst.setString(5, lbl_Time1.getText().toLowerCase());
            pst.setString(6, Cmd_User.getSelectedItem().toString().toLowerCase().trim());
            pst.setString(7, tt_time.getText().toLowerCase().trim());

            pst.execute();

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

    //Saving into temp db
    public void saveTempDB() {
        try {
            String sql = "Insert into temp_DB (Item, Qty, Cost, Time) values (?,?,?,?)";
            pst = conn.prepareStatement(sql);

            String name = txt_Itemname.getText().toLowerCase().trim();
            pst.setString(1, name.toLowerCase().trim());
            pst.setString(2, cmd_quentity.getSelectedItem().toString().trim());
            pst.setString(3, tt_cost.getText().trim());
            pst.setString(4, tt_time.getText().toLowerCase().trim());

            pst.execute();

            TempTable();

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

    //Saving into daily db
    public void saveDailyDB() {
        try {
            String sql = "Insert into DailySales_DB (Item,Cost,Qty,Tot_Cost,Date,Sold_by,Time) values (?,?,?,?,?,?,?)";
            pst = conn.prepareStatement(sql);

            String name1 = txt_Itemname.getText().trim();
            String Subname1 = "";
            if (name1.length() > 12) {
                Subname1 = name1.substring(0, 13);
            } else {
                Subname1 = name1;
            }
            pst.setString(1, Subname1.toLowerCase().trim());
            pst.setString(2, txt_cost.getText().trim());
            pst.setString(3, cmd_quentity.getSelectedItem().toString().trim());
            pst.setString(4, tt_cost.getText().trim());
            pst.setString(5, lbl_Time1.getText().toLowerCase().trim());
            pst.setString(6, Cmd_User.getSelectedItem().toString().toLowerCase().trim());
            pst.setString(7, tt_time.getText().toLowerCase().trim());

            pst.execute();

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

    //Quantity Action Performed
    public void quantityP() {
        String nameItem = txt_Itemname.getText();
        if (nameItem.isEmpty() || txt_cost.getText().isEmpty() || txt_QtyL.getText().isEmpty() && cmd_quentity.getSelectedIndex() == 0) {
            cmd_quentity.setSelectedIndex(-1);

        } else {
            double Selling = 0, totalBought = 0;
            int numba, Value2, valQ;
            DecimalFormat df2 = new DecimalFormat("###.##");
            try {
                Value2 = Integer.parseInt(txt_QtyL.getText());
                valQ = Integer.parseInt(cmd_quentity.getSelectedItem().toString());
                if (Value2 <= 0) {
                    JOptionPane.showMessageDialog(null, "Item is finish!");
                    cmd_quentity.setSelectedIndex(0);
                } else if (valQ > Value2) {
                    JOptionPane.showMessageDialog(null, "Item Selected is more than Items Left");
                } else {
                    if (Cmd_User.getSelectedIndex() == 0) {
                        Cmd_User.setBackground(Color.red);
                        cmd_quentity.setSelectedIndex(0);
                        JOptionPane.showMessageDialog(null, "Select User Name", "Alert!!", JOptionPane.ERROR_MESSAGE);
                    } else {
                        Selling = Double.parseDouble(txt_cost.getText());
                        numba = Integer.parseInt(cmd_quentity.getSelectedItem().toString());

                        totalBought = Double.valueOf(df2.format(Selling * numba));
                        tt_cost.setText(Double.toString(totalBought));

                        int total = Value2 - numba;
                        txt_QtyL.setText(Integer.toString(total));

                        saveTempDB();
                        saveDailyDB();
                        savePermanent();
                    }

                }
            } catch (Exception e) {

            } finally {
                try {
                    rs.close();
                    pst.close();
                } catch (Exception e) {
                }
            }
            UpdateQty();
            TTCostP();
            TotDaily();
        }
    }

    //Call the print
    public void printR() {
        if (txt_TotalPaid.getText().isEmpty() || Cmd_User.getSelectedIndex() == 0) {
            if (txt_TotalPaid.getText().isEmpty()) {
                txt_TotalPaid.setBackground(Color.red);
            } else {
                Cmd_User.setBackground(Color.red);
            }
        } else {
            ReceipForm rcpt = new ReceipForm();
            // rcpt.setVisible(true);
            // rcpt.printOut();
            rcpt.invoice_job();
            NewCustomer();
        }
    }

    //subtract
    public void totalPaid() {
        DecimalFormat df2 = new DecimalFormat("####.###");
        txt_TotalPaid.setBackground(Color.white);
        try {
            double numba1 = 0, numba2 = 0, total = 0;
            numba1 = Double.parseDouble(txt_TotalBought.getText());
            numba2 = Double.parseDouble(txt_TotalPaid.getText());
            total = Double.valueOf(df2.format(numba2 - numba1));
            txt_balance.setText(Double.toString(total));
        } catch (Exception e) {

        } finally {
            try {
                rs.close();
                pst.close();
            } catch (Exception e) {
            }
        }
    }

    //temp tabl mouse click
    public void tempMouseClick() {
        int row = temp_Table.getSelectedRow();
        String Table_click = (temp_Table.getModel().getValueAt(row, 0).toString());

        try {

            String sql = "select * from temp_DB where Item='" + Table_click + "'";
            pst = conn.prepareStatement(sql);
            rs = pst.executeQuery();
            if (rs.next()) {
                String add1 = rs.getString("Time");
                timeBought.setText(add1);
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

    public void back() {
        try {
            Interface inter = new Interface();
            inter.setVisible(true);
            this.dispose();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
        }
    }

    public void showSpecific() {
        try {

            String item = txt_Itemname.getText();
            int catego = cmd_Category.getSelectedIndex();

            if (item.isEmpty() || catego == 0) {

            } else {

                String sql = "select * from AllItems_DB where Items='" + item + "' and Category='" + catego + "'";
                pst = conn.prepareStatement(sql);
                rs = pst.executeQuery();
                if (rs.next()) {
                    String add2 = "";

                    if (rwhole.isSelected()) {
                        add2 = rs.getString("wholesale");
                    } else if (rretail.isSelected()) {
                        add2 = rs.getString("retail");
                    } else {
                        add2 = rs.getString("wholesale");
                    }

                    txt_cost.setText(add2);
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

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        buttonGroup1 = new javax.swing.ButtonGroup();
        jPanel3 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        btnRecords = new javax.swing.JButton();
        btnDaily = new javax.swing.JButton();
        lbl_Time1 = new javax.swing.JLabel();
        tt_time = new javax.swing.JLabel();
        Cmd_User = new javax.swing.JComboBox();
        jLabel8 = new javax.swing.JLabel();
        btn_Back = new javax.swing.JButton();
        btn_Finish = new javax.swing.JButton();
        jPanel4 = new javax.swing.JPanel();
        jPanel1 = new javax.swing.JPanel();
        TTS_Day = new javax.swing.JTextField();
        txt_TotalBought = new javax.swing.JTextField();
        jLabel12 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        ItemBought = new javax.swing.JLabel();
        txt_TotalPaid = new javax.swing.JTextField();
        txt_balance = new javax.swing.JTextField();
        jLabel7 = new javax.swing.JLabel();
        jPanel10 = new javax.swing.JPanel();
        cmd_quentity = new javax.swing.JComboBox();
        tt_cost = new javax.swing.JTextField();
        txt_cost = new javax.swing.JTextField();
        btn_Customer = new javax.swing.JButton();
        txt_Itemname = new javax.swing.JTextField();
        jScrollPane3 = new javax.swing.JScrollPane();
        temp_Table = new javax.swing.JTable();
        deleteSelect = new javax.swing.JButton();
        timeBought = new javax.swing.JLabel();
        Receipt = new javax.swing.JButton();
        check = new javax.swing.JCheckBox();
        TablesPane = new javax.swing.JTabbedPane();
        jPanel6 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        UItemsTable = new javax.swing.JTable();
        txt_QtyL = new javax.swing.JTextField();
        txt_Search = new javax.swing.JTextField();
        refresh = new javax.swing.JButton();
        cmd_Category = new javax.swing.JComboBox<>();
        search = new javax.swing.JButton();
        rwhole = new javax.swing.JRadioButton();
        rretail = new javax.swing.JRadioButton();
        newcustomer = new javax.swing.JMenuBar();
        jMenu1 = new javax.swing.JMenu();
        newCustomer = new javax.swing.JMenuItem();
        jSeparator1 = new javax.swing.JPopupMenu.Separator();
        receiptt = new javax.swing.JMenuItem();
        jSeparator2 = new javax.swing.JPopupMenu.Separator();
        exit = new javax.swing.JMenuItem();
        jMenu2 = new javax.swing.JMenu();
        viewFinish = new javax.swing.JMenuItem();
        jSeparator3 = new javax.swing.JPopupMenu.Separator();
        jSeparator4 = new javax.swing.JPopupMenu.Separator();
        dailySales = new javax.swing.JMenuItem();
        jSeparator5 = new javax.swing.JPopupMenu.Separator();
        jSeparator6 = new javax.swing.JPopupMenu.Separator();
        addRecords = new javax.swing.JMenuItem();
        jSeparator7 = new javax.swing.JPopupMenu.Separator();
        jSeparator8 = new javax.swing.JPopupMenu.Separator();
        backMn = new javax.swing.JMenuItem();
        jSeparator9 = new javax.swing.JPopupMenu.Separator();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("MAIN USER PANEL");
        setBackground(new java.awt.Color(36, 47, 65));
        setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));

        jLabel1.setFont(new java.awt.Font("Aharoni", 1, 24)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(51, 0, 0));
        jLabel1.setText("                  WESTPOINT COSMETICS ASANKRANGWA");
        jLabel1.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 764, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(jLabel1))
        );

        jPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder(""));

        btnRecords.setFont(new java.awt.Font("Times New Roman", 1, 18)); // NOI18N
        btnRecords.setForeground(new java.awt.Color(51, 0, 0));
        btnRecords.setIcon(new javax.swing.ImageIcon(getClass().getResource("/image/recordBlue.png"))); // NOI18N
        btnRecords.setToolTipText("Click to enter records");
        btnRecords.setBorder(null);
        btnRecords.setPreferredSize(new java.awt.Dimension(95, 40));
        btnRecords.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRecordsActionPerformed(evt);
            }
        });

        btnDaily.setFont(new java.awt.Font("Tahoma", 1, 15)); // NOI18N
        btnDaily.setForeground(new java.awt.Color(51, 0, 0));
        btnDaily.setIcon(new javax.swing.ImageIcon(getClass().getResource("/image/daily.png"))); // NOI18N
        btnDaily.setToolTipText("Click to view daily sales");
        btnDaily.setBorder(null);
        btnDaily.setPreferredSize(new java.awt.Dimension(157, 40));
        btnDaily.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDailyActionPerformed(evt);
            }
        });

        lbl_Time1.setFont(new java.awt.Font("Tahoma", 1, 24)); // NOI18N
        lbl_Time1.setForeground(new java.awt.Color(255, 0, 0));

        tt_time.setFont(new java.awt.Font("Tahoma", 1, 24)); // NOI18N
        tt_time.setForeground(new java.awt.Color(0, 102, 0));

        Cmd_User.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        Cmd_User.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Select User Name" }));
        Cmd_User.setBorder(null);
        Cmd_User.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                Cmd_UserFocusGained(evt);
            }
        });

        jLabel8.setIcon(new javax.swing.ImageIcon(getClass().getResource("/image/user.png"))); // NOI18N

        btn_Back.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        btn_Back.setForeground(new java.awt.Color(51, 0, 0));
        btn_Back.setIcon(new javax.swing.ImageIcon(getClass().getResource("/image/back.png"))); // NOI18N
        btn_Back.setToolTipText("Go Back");
        btn_Back.setBorder(null);
        btn_Back.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_BackActionPerformed(evt);
            }
        });

        btn_Finish.setIcon(new javax.swing.ImageIcon(getClass().getResource("/image/Update.png"))); // NOI18N
        btn_Finish.setToolTipText("Click to View Finishing Items");
        btn_Finish.setBorder(null);
        btn_Finish.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_FinishActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(btn_Back, javax.swing.GroupLayout.PREFERRED_SIZE, 44, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(btnDaily, javax.swing.GroupLayout.PREFERRED_SIZE, 44, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(btnRecords, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jLabel8)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(Cmd_User, javax.swing.GroupLayout.PREFERRED_SIZE, 191, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(btn_Finish, javax.swing.GroupLayout.PREFERRED_SIZE, 44, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(tt_time, javax.swing.GroupLayout.PREFERRED_SIZE, 181, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(lbl_Time1, javax.swing.GroupLayout.PREFERRED_SIZE, 228, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(lbl_Time1, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(tt_time, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btnRecords, javax.swing.GroupLayout.DEFAULT_SIZE, 41, Short.MAX_VALUE)
                    .addComponent(btnDaily, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(Cmd_User)
                    .addComponent(jLabel8, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btn_Back, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btn_Finish, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(0, 0, 0))
        );

        jPanel4.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "", javax.swing.border.TitledBorder.CENTER, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 1, 14), new java.awt.Color(51, 0, 0))); // NOI18N

        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Transaction Details", javax.swing.border.TitledBorder.LEFT, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 1, 18), new java.awt.Color(102, 0, 0))); // NOI18N
        jPanel1.setPreferredSize(new java.awt.Dimension(541, 205));

        TTS_Day.setEditable(false);
        TTS_Day.setFont(new java.awt.Font("Mangal", 1, 36)); // NOI18N
        TTS_Day.setForeground(new java.awt.Color(51, 0, 51));
        TTS_Day.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));

        txt_TotalBought.setEditable(false);
        txt_TotalBought.setFont(new java.awt.Font("Times New Roman", 1, 36)); // NOI18N
        txt_TotalBought.setForeground(new java.awt.Color(255, 51, 51));
        txt_TotalBought.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        txt_TotalBought.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txt_TotalBoughtActionPerformed(evt);
            }
        });

        jLabel12.setFont(new java.awt.Font("Tahoma", 1, 16)); // NOI18N
        jLabel12.setForeground(new java.awt.Color(51, 0, 51));
        jLabel12.setText("Balance in GH₵");

        jLabel11.setFont(new java.awt.Font("Tahoma", 1, 16)); // NOI18N
        jLabel11.setForeground(new java.awt.Color(51, 0, 51));
        jLabel11.setText("Total Amount Paid in GH₵");

        ItemBought.setFont(new java.awt.Font("Tahoma", 1, 16)); // NOI18N
        ItemBought.setForeground(new java.awt.Color(51, 0, 51));
        ItemBought.setText("Total Items Bought in GH₵");

        txt_TotalPaid.setFont(new java.awt.Font("Times New Roman", 1, 18)); // NOI18N
        txt_TotalPaid.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        txt_TotalPaid.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                txt_TotalPaidMouseClicked(evt);
            }
        });
        txt_TotalPaid.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txt_TotalPaidActionPerformed(evt);
            }
        });
        txt_TotalPaid.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txt_TotalPaidKeyPressed(evt);
            }
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txt_TotalPaidKeyReleased(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txt_TotalPaidKeyTyped(evt);
            }
        });

        txt_balance.setEditable(false);
        txt_balance.setFont(new java.awt.Font("Times New Roman", 1, 18)); // NOI18N
        txt_balance.setForeground(new java.awt.Color(0, 102, 0));
        txt_balance.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        txt_balance.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txt_balanceActionPerformed(evt);
            }
        });

        jLabel7.setFont(new java.awt.Font("Mangal", 1, 20)); // NOI18N
        jLabel7.setForeground(new java.awt.Color(191, 29, 13));
        jLabel7.setText("Day Total Sales");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(ItemBought, javax.swing.GroupLayout.PREFERRED_SIZE, 275, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel12, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel7, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel11, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(6, 6, 6)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(TTS_Day, javax.swing.GroupLayout.DEFAULT_SIZE, 189, Short.MAX_VALUE)
                    .addComponent(txt_TotalPaid, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 189, Short.MAX_VALUE)
                    .addComponent(txt_TotalBought, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 189, Short.MAX_VALUE)
                    .addComponent(txt_balance, javax.swing.GroupLayout.Alignment.TRAILING))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(ItemBought, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(txt_TotalBought, javax.swing.GroupLayout.DEFAULT_SIZE, 58, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel11, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txt_TotalPaid, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel12, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txt_balance, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(TTS_Day)
                    .addComponent(jLabel7, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap(17, Short.MAX_VALUE))
        );

        jPanel10.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Item Details", javax.swing.border.TitledBorder.LEFT, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 1, 15), new java.awt.Color(102, 0, 0))); // NOI18N

        cmd_quentity.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        cmd_quentity.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Select Qty", "1", "2", "3", "4", "5" }));
        cmd_quentity.setToolTipText("Select quantity here");
        cmd_quentity.setPreferredSize(new java.awt.Dimension(98, 30));
        cmd_quentity.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                cmd_quentityMouseClicked(evt);
            }
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                cmd_quentityMouseReleased(evt);
            }
        });
        cmd_quentity.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmd_quentityActionPerformed(evt);
            }
        });

        tt_cost.setEditable(false);
        tt_cost.setFont(new java.awt.Font("Times New Roman", 1, 15)); // NOI18N
        tt_cost.setForeground(new java.awt.Color(0, 153, 102));
        tt_cost.setToolTipText("Total cost is shown here");
        tt_cost.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));

        txt_cost.setEditable(false);
        txt_cost.setFont(new java.awt.Font("Times New Roman", 1, 17)); // NOI18N
        txt_cost.setForeground(new java.awt.Color(0, 0, 255));
        txt_cost.setToolTipText("Item cost is shown here");
        txt_cost.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        txt_cost.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txt_costActionPerformed(evt);
            }
        });

        btn_Customer.setFont(new java.awt.Font("Times New Roman", 1, 18)); // NOI18N
        btn_Customer.setForeground(new java.awt.Color(51, 0, 0));
        btn_Customer.setText("No Receipt");
        btn_Customer.setToolTipText("New Customer Button");
        btn_Customer.setBorder(null);
        btn_Customer.setPreferredSize(new java.awt.Dimension(147, 36));
        btn_Customer.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                btn_CustomerFocusGained(evt);
            }
        });
        btn_Customer.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_CustomerActionPerformed(evt);
            }
        });

        txt_Itemname.setEditable(false);
        txt_Itemname.setFont(new java.awt.Font("Times New Roman", 1, 17)); // NOI18N
        txt_Itemname.setForeground(new java.awt.Color(51, 0, 0));
        txt_Itemname.setToolTipText("Item name is shown here");
        txt_Itemname.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));

        temp_Table.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Item Name", "Quantity Buying", "Total Cost"
            }
        ));
        temp_Table.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                temp_TableMouseClicked(evt);
            }
        });
        jScrollPane3.setViewportView(temp_Table);

        deleteSelect.setIcon(new javax.swing.ImageIcon(getClass().getResource("/image/delete_multiple.png"))); // NOI18N
        deleteSelect.setToolTipText("Delete Selected Item");
        deleteSelect.setBorder(null);
        deleteSelect.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                deleteSelectActionPerformed(evt);
            }
        });

        Receipt.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        Receipt.setForeground(new java.awt.Color(0, 51, 0));
        Receipt.setIcon(new javax.swing.ImageIcon(getClass().getResource("/image/receipt.png"))); // NOI18N
        Receipt.setText("  Receipt");
        Receipt.setToolTipText("Contact the developer on 0544474706 or 0500383888");
        Receipt.setBorder(null);
        Receipt.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ReceiptActionPerformed(evt);
            }
        });
        Receipt.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                ReceiptKeyReleased(evt);
            }
        });

        check.setToolTipText("Check to Activate the receipt button");
        check.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                checkActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel10Layout = new javax.swing.GroupLayout(jPanel10);
        jPanel10.setLayout(jPanel10Layout);
        jPanel10Layout.setHorizontalGroup(
            jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel10Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel10Layout.createSequentialGroup()
                        .addComponent(txt_Itemname, javax.swing.GroupLayout.PREFERRED_SIZE, 194, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txt_cost)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(cmd_quentity, javax.swing.GroupLayout.PREFERRED_SIZE, 106, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(tt_cost, javax.swing.GroupLayout.PREFERRED_SIZE, 74, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jScrollPane3, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 470, Short.MAX_VALUE)
                    .addGroup(jPanel10Layout.createSequentialGroup()
                        .addComponent(timeBought, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(15, 15, 15)
                        .addComponent(deleteSelect, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(48, 48, 48)
                        .addComponent(check, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(Receipt, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(btn_Customer, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        jPanel10Layout.setVerticalGroup(
            jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel10Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(txt_cost)
                    .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(txt_Itemname, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(tt_cost, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(cmd_quentity, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 133, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(timeBought, javax.swing.GroupLayout.PREFERRED_SIZE, 16, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                        .addComponent(check)
                        .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(deleteSelect, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(btn_Customer, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(Receipt, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
                .addContainerGap())
        );

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(jPanel10, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, 502, Short.MAX_VALUE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addComponent(jPanel10, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, 259, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        TablesPane.setForeground(new java.awt.Color(51, 0, 0));
        TablesPane.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N

        UItemsTable.setFont(new java.awt.Font("Tahoma", 0, 15)); // NOI18N
        UItemsTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Items Name", "Retail Price", "Wholesale Price"
            }
        ));
        UItemsTable.setToolTipText("Contact the Developer on 0544474706 or 0500383888");
        UItemsTable.setSelectionMode(javax.swing.ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        UItemsTable.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                UItemsTableFocusGained(evt);
            }
        });
        UItemsTable.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                UItemsTableMouseClicked(evt);
            }
        });
        UItemsTable.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                UItemsTableKeyPressed(evt);
            }
            public void keyReleased(java.awt.event.KeyEvent evt) {
                UItemsTableKeyReleased(evt);
            }
        });
        jScrollPane1.setViewportView(UItemsTable);
        if (UItemsTable.getColumnModel().getColumnCount() > 0) {
            UItemsTable.getColumnModel().getColumn(0).setPreferredWidth(300);
        }

        txt_QtyL.setEditable(false);
        txt_QtyL.setFont(new java.awt.Font("Times New Roman", 1, 18)); // NOI18N
        txt_QtyL.setForeground(new java.awt.Color(204, 0, 51));
        txt_QtyL.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        txt_QtyL.setToolTipText("Quantity Left is shown here");
        txt_QtyL.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));

        txt_Search.setBackground(new java.awt.Color(240, 240, 240));
        txt_Search.setFont(new java.awt.Font("Times New Roman", 0, 14)); // NOI18N
        txt_Search.setText("Search...");
        txt_Search.setToolTipText("Enter item name to search");
        txt_Search.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                txt_SearchFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                txt_SearchFocusLost(evt);
            }
        });
        txt_Search.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                txt_SearchMouseClicked(evt);
            }
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                txt_SearchMouseReleased(evt);
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

        refresh.setFont(new java.awt.Font("Times New Roman", 1, 18)); // NOI18N
        refresh.setForeground(new java.awt.Color(51, 0, 0));
        refresh.setIcon(new javax.swing.ImageIcon(getClass().getResource("/image/ref.png"))); // NOI18N
        refresh.setToolTipText("Click to refresh");
        refresh.setBorder(null);
        refresh.setPreferredSize(new java.awt.Dimension(91, 40));
        refresh.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                refreshActionPerformed(evt);
            }
        });

        cmd_Category.setFont(new java.awt.Font("Times New Roman", 1, 15)); // NOI18N
        cmd_Category.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Select Category" }));
        cmd_Category.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                cmd_CategoryMouseReleased(evt);
            }
        });
        cmd_Category.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmd_CategoryActionPerformed(evt);
            }
        });

        search.setFont(new java.awt.Font("Times New Roman", 1, 18)); // NOI18N
        search.setForeground(new java.awt.Color(51, 0, 0));
        search.setIcon(new javax.swing.ImageIcon(getClass().getResource("/image/search.png"))); // NOI18N
        search.setToolTipText("Click to search");
        search.setBorder(null);
        search.setPreferredSize(new java.awt.Dimension(91, 40));
        search.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                searchActionPerformed(evt);
            }
        });

        buttonGroup1.add(rwhole);
        rwhole.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        rwhole.setForeground(new java.awt.Color(255, 0, 0));
        rwhole.setText("WSP");
        rwhole.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rwholeActionPerformed(evt);
            }
        });

        buttonGroup1.add(rretail);
        rretail.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        rretail.setForeground(new java.awt.Color(0, 0, 153));
        rretail.setSelected(true);
        rretail.setText("RSP");
        rretail.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rretailActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 672, Short.MAX_VALUE)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(txt_QtyL, javax.swing.GroupLayout.PREFERRED_SIZE, 58, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txt_Search, javax.swing.GroupLayout.PREFERRED_SIZE, 186, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(cmd_Category, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(search, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(refresh, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(rretail)
                .addGap(18, 18, 18)
                .addComponent(rwhole, javax.swing.GroupLayout.PREFERRED_SIZE, 63, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel6Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addComponent(cmd_Category, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel6Layout.createSequentialGroup()
                                .addGap(0, 0, Short.MAX_VALUE)
                                .addComponent(txt_QtyL, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(search, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                            .addComponent(refresh, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                            .addComponent(rretail, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(rwhole, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                    .addComponent(txt_Search, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 465, Short.MAX_VALUE))
        );

        TablesPane.addTab("The Table Below shows Item Name and their Cost", jPanel6);

        newcustomer.setBorder(null);

        jMenu1.setText("File");

        newCustomer.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_N, java.awt.event.InputEvent.SHIFT_MASK | java.awt.event.InputEvent.CTRL_MASK));
        newCustomer.setIcon(new javax.swing.ImageIcon(getClass().getResource("/image/customer.png"))); // NOI18N
        newCustomer.setText("New Customer");
        newCustomer.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                newCustomerActionPerformed(evt);
            }
        });
        jMenu1.add(newCustomer);
        jMenu1.add(jSeparator1);

        receiptt.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_R, java.awt.event.InputEvent.CTRL_MASK));
        receiptt.setIcon(new javax.swing.ImageIcon(getClass().getResource("/image/receipt.png"))); // NOI18N
        receiptt.setText("Receipt");
        receiptt.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                receipttActionPerformed(evt);
            }
        });
        jMenu1.add(receiptt);
        jMenu1.add(jSeparator2);

        exit.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_E, java.awt.event.InputEvent.CTRL_MASK));
        exit.setIcon(new javax.swing.ImageIcon(getClass().getResource("/image/sales2.png"))); // NOI18N
        exit.setText("Exit");
        exit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                exitActionPerformed(evt);
            }
        });
        jMenu1.add(exit);

        newcustomer.add(jMenu1);

        jMenu2.setText("Edit");

        viewFinish.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_V, java.awt.event.InputEvent.CTRL_MASK));
        viewFinish.setIcon(new javax.swing.ImageIcon(getClass().getResource("/image/Update.png"))); // NOI18N
        viewFinish.setText("View Finishing Item");
        viewFinish.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                viewFinishActionPerformed(evt);
            }
        });
        jMenu2.add(viewFinish);
        jMenu2.add(jSeparator3);
        jMenu2.add(jSeparator4);

        dailySales.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_D, java.awt.event.InputEvent.CTRL_MASK));
        dailySales.setIcon(new javax.swing.ImageIcon(getClass().getResource("/image/daily.png"))); // NOI18N
        dailySales.setText("Daily Sales");
        dailySales.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                dailySalesActionPerformed(evt);
            }
        });
        jMenu2.add(dailySales);
        jMenu2.add(jSeparator5);
        jMenu2.add(jSeparator6);

        addRecords.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_R, java.awt.event.InputEvent.CTRL_MASK));
        addRecords.setIcon(new javax.swing.ImageIcon(getClass().getResource("/image/recordBlue.png"))); // NOI18N
        addRecords.setText("Add Records");
        addRecords.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addRecordsActionPerformed(evt);
            }
        });
        jMenu2.add(addRecords);
        jMenu2.add(jSeparator7);
        jMenu2.add(jSeparator8);

        backMn.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_B, java.awt.event.InputEvent.CTRL_MASK));
        backMn.setIcon(new javax.swing.ImageIcon(getClass().getResource("/image/back.png"))); // NOI18N
        backMn.setText("Back");
        backMn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                backMnActionPerformed(evt);
            }
        });
        jMenu2.add(backMn);
        jMenu2.add(jSeparator9);

        newcustomer.add(jMenu2);

        setJMenuBar(newcustomer);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addComponent(TablesPane))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(TablesPane)
                    .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void viewFinishActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_viewFinishActionPerformed
        viewFinish();
    }//GEN-LAST:event_viewFinishActionPerformed

    private void btnRecordsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRecordsActionPerformed
        viewRecords();
    }//GEN-LAST:event_btnRecordsActionPerformed

    private void btnDailyActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDailyActionPerformed
        viewDailySales();
    }//GEN-LAST:event_btnDailyActionPerformed

    private void refreshActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_refreshActionPerformed

        Update_table();
        TableColor();
    }//GEN-LAST:event_refreshActionPerformed

    private void txt_costActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txt_costActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txt_costActionPerformed

    private void txt_SearchActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txt_SearchActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txt_SearchActionPerformed

    private void txt_SearchKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txt_SearchKeyReleased

        searchWholesale(txt_Search.getText().toString());
        TableColor();
    }//GEN-LAST:event_txt_SearchKeyReleased

    private void cmd_quentityMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_cmd_quentityMouseClicked

    }//GEN-LAST:event_cmd_quentityMouseClicked

    private void cmd_quentityMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_cmd_quentityMouseReleased

    }//GEN-LAST:event_cmd_quentityMouseReleased

    private void cmd_quentityActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmd_quentityActionPerformed
        quantityP();
        TableColor();
    }//GEN-LAST:event_cmd_quentityActionPerformed

    private void ReceiptActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ReceiptActionPerformed
        printR();
    }//GEN-LAST:event_ReceiptActionPerformed

    private void ReceiptKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_ReceiptKeyReleased

    }//GEN-LAST:event_ReceiptKeyReleased

    private void txt_balanceActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txt_balanceActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txt_balanceActionPerformed

    private void txt_TotalPaidActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txt_TotalPaidActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txt_TotalPaidActionPerformed

    private void txt_TotalPaidKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txt_TotalPaidKeyPressed

    }//GEN-LAST:event_txt_TotalPaidKeyPressed

    private void txt_TotalPaidKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txt_TotalPaidKeyReleased
        totalPaid();
    }//GEN-LAST:event_txt_TotalPaidKeyReleased

    private void txt_TotalPaidKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txt_TotalPaidKeyTyped

    }//GEN-LAST:event_txt_TotalPaidKeyTyped

    private void txt_TotalBoughtActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txt_TotalBoughtActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txt_TotalBoughtActionPerformed

    private void btn_CustomerActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_CustomerActionPerformed
        NewCustomer();
    }//GEN-LAST:event_btn_CustomerActionPerformed

    private void txt_SearchMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_txt_SearchMouseClicked

    }//GEN-LAST:event_txt_SearchMouseClicked

    private void txt_SearchMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_txt_SearchMouseReleased
        // TODO add your handling code here:

    }//GEN-LAST:event_txt_SearchMouseReleased

    private void txt_SearchFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txt_SearchFocusLost
        // TODO add your handling code here:
        txt_Search.setText("Search...");
    }//GEN-LAST:event_txt_SearchFocusLost

    private void txt_SearchFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txt_SearchFocusGained
        txt_Search.setText("");
    }//GEN-LAST:event_txt_SearchFocusGained

    private void Cmd_UserFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_Cmd_UserFocusGained
        Cmd_User.setBackground(Color.LIGHT_GRAY);
    }//GEN-LAST:event_Cmd_UserFocusGained

    private void temp_TableMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_temp_TableMouseClicked
        tempMouseClick();
    }//GEN-LAST:event_temp_TableMouseClicked

    private void deleteSelectActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_deleteSelectActionPerformed

        try {
            if (txt_TotalPaid.getText().isEmpty()) {
                AddUp();
                DeleteSelect();
            } else {
                AddUp();
                DeleteSelect();
                BalanceRefresh();
            }

        } catch (Exception e) {

        }
    }//GEN-LAST:event_deleteSelectActionPerformed

    private void btn_BackActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_BackActionPerformed
        back();
    }//GEN-LAST:event_btn_BackActionPerformed

    private void btn_FinishActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_FinishActionPerformed
        try {
            FinishingItemPane fin = new FinishingItemPane();
            fin.setVisible(true);
        } catch (Exception e) {

        }
    }//GEN-LAST:event_btn_FinishActionPerformed

    private void exitActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_exitActionPerformed
        System.exit(0);
    }//GEN-LAST:event_exitActionPerformed

    private void receipttActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_receipttActionPerformed
        // rcpt.PrintReceipt();
    }//GEN-LAST:event_receipttActionPerformed

    private void newCustomerActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_newCustomerActionPerformed
        NewCustomer();
    }//GEN-LAST:event_newCustomerActionPerformed

    private void dailySalesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_dailySalesActionPerformed
        viewDailySales();
    }//GEN-LAST:event_dailySalesActionPerformed

    private void addRecordsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addRecordsActionPerformed
        viewRecords();
    }//GEN-LAST:event_addRecordsActionPerformed

    private void backMnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_backMnActionPerformed
        back();
    }//GEN-LAST:event_backMnActionPerformed

    private void btn_CustomerFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_btn_CustomerFocusGained
        Update_table();
        TableColor();
    }//GEN-LAST:event_btn_CustomerFocusGained

    private void UItemsTableKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_UItemsTableKeyReleased
        if (evt.getKeyCode() == KeyEvent.VK_DOWN || evt.getKeyCode() == KeyEvent.VK_UP) {
            tableMouseClick();
        }
    }//GEN-LAST:event_UItemsTableKeyReleased

    private void UItemsTableKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_UItemsTableKeyPressed

    }//GEN-LAST:event_UItemsTableKeyPressed

    private void UItemsTableMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_UItemsTableMouseClicked
        tableMouseClick();
    }//GEN-LAST:event_UItemsTableMouseClicked

    private void UItemsTableFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_UItemsTableFocusGained

    }//GEN-LAST:event_UItemsTableFocusGained

    private void txt_TotalPaidMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_txt_TotalPaidMouseClicked
        txt_TotalPaid.setBackground(Color.WHITE);
    }//GEN-LAST:event_txt_TotalPaidMouseClicked

    private void checkActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_checkActionPerformed
        checkBtn();
    }//GEN-LAST:event_checkActionPerformed

    private void cmd_CategoryActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmd_CategoryActionPerformed

    }//GEN-LAST:event_cmd_CategoryActionPerformed

    private void cmd_CategoryMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_cmd_CategoryMouseReleased

    }//GEN-LAST:event_cmd_CategoryMouseReleased

    private void searchActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_searchActionPerformed
        UpdateCategory();
    }//GEN-LAST:event_searchActionPerformed

    private void rretailActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rretailActionPerformed
        Update_table();
        showSpecific();

        cmd_quentity.removeAllItems();
        cmd_quentity.addItem("Select Qty");

        for (int i = 1; i < 6; i++) {
            cmd_quentity.addItem(i);
        }

    }//GEN-LAST:event_rretailActionPerformed

    private void rwholeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rwholeActionPerformed
        Update_table();
        showSpecific();

        cmd_quentity.removeAllItems();
        cmd_quentity.addItem("Select Qty");

        for (int i = 6; i <= 1000; i++) {
            cmd_quentity.addItem(i);
        }
    }//GEN-LAST:event_rwholeActionPerformed

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
            java.util.logging.Logger.getLogger(MainUserPanel.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(MainUserPanel.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(MainUserPanel.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(MainUserPanel.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        UIManager.setLookAndFeel("com.jtattoo.plaf.aluminium.AluminiumLookAndFeel");
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new MainUserPanel().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    public static javax.swing.JComboBox Cmd_User;
    private javax.swing.JLabel ItemBought;
    public static javax.swing.JButton Receipt;
    private javax.swing.JTextField TTS_Day;
    private javax.swing.JTabbedPane TablesPane;
    private javax.swing.JTable UItemsTable;
    private javax.swing.JMenuItem addRecords;
    private javax.swing.JMenuItem backMn;
    private javax.swing.JButton btnDaily;
    private javax.swing.JButton btnRecords;
    private javax.swing.JButton btn_Back;
    private javax.swing.JButton btn_Customer;
    private javax.swing.JButton btn_Finish;
    private javax.swing.ButtonGroup buttonGroup1;
    private javax.swing.JCheckBox check;
    private javax.swing.JComboBox<String> cmd_Category;
    private javax.swing.JComboBox cmd_quentity;
    private javax.swing.JMenuItem dailySales;
    private javax.swing.JButton deleteSelect;
    private javax.swing.JMenuItem exit;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JMenu jMenu1;
    private javax.swing.JMenu jMenu2;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel10;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JPopupMenu.Separator jSeparator1;
    private javax.swing.JPopupMenu.Separator jSeparator2;
    private javax.swing.JPopupMenu.Separator jSeparator3;
    private javax.swing.JPopupMenu.Separator jSeparator4;
    private javax.swing.JPopupMenu.Separator jSeparator5;
    private javax.swing.JPopupMenu.Separator jSeparator6;
    private javax.swing.JPopupMenu.Separator jSeparator7;
    private javax.swing.JPopupMenu.Separator jSeparator8;
    private javax.swing.JPopupMenu.Separator jSeparator9;
    public static javax.swing.JLabel lbl_Time1;
    private javax.swing.JMenuItem newCustomer;
    private javax.swing.JMenuBar newcustomer;
    private javax.swing.JMenuItem receiptt;
    private javax.swing.JButton refresh;
    private javax.swing.JRadioButton rretail;
    private javax.swing.JRadioButton rwhole;
    private javax.swing.JButton search;
    public static javax.swing.JTable temp_Table;
    private javax.swing.JLabel timeBought;
    private javax.swing.JTextField tt_cost;
    public static javax.swing.JLabel tt_time;
    private javax.swing.JTextField txt_Itemname;
    private javax.swing.JTextField txt_QtyL;
    private javax.swing.JTextField txt_Search;
    public static javax.swing.JTextField txt_TotalBought;
    public static javax.swing.JTextField txt_TotalPaid;
    public static javax.swing.JTextField txt_balance;
    private javax.swing.JTextField txt_cost;
    private javax.swing.JMenuItem viewFinish;
    // End of variables declaration//GEN-END:variables
}
