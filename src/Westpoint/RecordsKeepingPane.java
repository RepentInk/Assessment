package Westpoint;

import Intel.connectDB;
import java.awt.Color;
import java.awt.Component;
import java.awt.Image;
import java.awt.event.KeyEvent;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.DecimalFormat;
import java.text.MessageFormat;
import java.text.SimpleDateFormat;
import java.util.Vector;
import javax.imageio.ImageIO;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import net.proteanit.sql.DbUtils;

/**
 *
 * @author Nyarkoisaac
 */
public class RecordsKeepingPane extends javax.swing.JFrame {

    Connection conn = null;
    ResultSet rs = null;
    PreparedStatement pst = null;

    Vector originalTableModelKeep;
    Vector OrderTableModel;

    /**
     * Creates new form RecordsKeeping
     */
    public RecordsKeepingPane() {
        initComponents();
        conn = connectDB.ConnecrDb();
        //This try and catch block replace the java icon with custom one
        try {
            Image i = ImageIO.read(getClass().getResource("doubleUU.jpg"));
            setIconImage(i);
        } catch (Exception e) {

        }
        Records_table();
        Order_table();

        Arrears_TableColor();// Arrears table color method
        Order_TableColor(); // Order Table Color Method
        countOrder();
        id.setVisible(false);

        lbl_pay.setEnabled(false);
        txt_Paid.setEnabled(false);
        lbl_Remain.setEnabled(false);
        txt_Remain.setEnabled(false);

        originalTableModelKeep = (Vector) ((DefaultTableModel) Arrears_Table.getModel()).getDataVector().clone();
        OrderTableModel = (Vector) ((DefaultTableModel) Order_Table.getModel()).getDataVector().clone();

    }

    private void Records_table() {
        try {
            String sql = "select id,Name,Item,Amount,Date,Status from Records_DB order by Amount DESC";
            pst = conn.prepareStatement(sql);
            rs = pst.executeQuery();
            Arrears_Table.setModel(DbUtils.resultSetToTableModel(rs));

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

    private void Order_table() {
        try {
            String sql = "select id,Item_Type,Cost,Date from Order_DB order by id ASC";
            pst = conn.prepareStatement(sql);
            rs = pst.executeQuery();
            Order_Table.setModel(DbUtils.resultSetToTableModel(rs));

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


    public void Arrears_TableColor() {
        Arrears_Table.getColumnModel().getColumn(0).setCellRenderer(new RecordsKeepingPane.CustomRenderer());
        Arrears_Table.getColumnModel().getColumn(1).setCellRenderer(new RecordsKeepingPane.CustomRenderer1());
        Arrears_Table.getColumnModel().getColumn(2).setCellRenderer(new RecordsKeepingPane.CustomRenderer2());
        Arrears_Table.getColumnModel().getColumn(3).setCellRenderer(new RecordsKeepingPane.CustomRenderer3());
        Arrears_Table.getColumnModel().getColumn(4).setCellRenderer(new RecordsKeepingPane.CustomRenderer4());
        Arrears_Table.getColumnModel().getColumn(5).setCellRenderer(new RecordsKeepingPane.CustomRenderer5());
        Arrears_Table.getTableHeader().setDefaultRenderer(new RecordsKeepingPane.HeaderColor());
    }

    public void Order_TableColor() {
        Order_Table.getColumnModel().getColumn(0).setCellRenderer(new RecordsKeepingPane.CustomRenderer());
        Order_Table.getColumnModel().getColumn(1).setCellRenderer(new RecordsKeepingPane.CustomRenderer1());
        Order_Table.getColumnModel().getColumn(2).setCellRenderer(new RecordsKeepingPane.CustomRenderer2());
        Order_Table.getColumnModel().getColumn(3).setCellRenderer(new RecordsKeepingPane.CustomRenderer3());
        //Order_Table.getColumnModel().getColumn(4).setCellRenderer(new RecordsKeepingPane.CustomRenderer4());
        Order_Table.getTableHeader().setDefaultRenderer(new RecordsKeepingPane.HeaderColor());
    }

    public void searchTableContentKeep(String SearchKeep) {
        try {
            DefaultTableModel currtableModelKeep = (DefaultTableModel) Arrears_Table.getModel();
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
                // conn.close();
                rs.close();
                pst.close();
            } catch (Exception e) {
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
                // conn.close();
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

    public static class CustomRenderer5 extends DefaultTableCellRenderer {

        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            Component cellComponent = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
            if (column == 5) {
                // cellComponent.setBackground(Color.magenta);
                cellComponent.setForeground(Color.RED);
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

//    record table or page method
    public void recordSave() {
        if (txt_id.getText().isEmpty() || txt_Name.getText().isEmpty() || txt_type.getText().isEmpty() || txt_Amount.getText().isEmpty() || Description.getText().isEmpty() || txt_Date.equals(null) || buttonGroup1.getSelection() == null) {
            JOptionPane.showMessageDialog(null, "Please Enter Details Before Saving");
        } else {
            try {

                String sql = "Insert into Records_DB (id,Name,Item,Amount,Date,Status,Description) values (?,?,?,?,?,?,?)";

                pst = conn.prepareStatement(sql);
                pst.setString(1, txt_id.getText().toLowerCase().trim());
                pst.setString(2, txt_Name.getText().toLowerCase().trim());
                pst.setString(3, txt_type.getText().toLowerCase().trim());
                pst.setString(4, txt_Amount.getText().toLowerCase().trim());
                pst.setString(5, ((JTextField) txt_Date.getDateEditor().getUiComponent()).getText().toLowerCase().trim());
                pst.setString(6, status.toLowerCase().trim());
                pst.setString(7, Description.getText().toLowerCase().trim());

                pst.execute();
                JOptionPane.showMessageDialog(null, "Saved");
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, "Records Already Saved");
            } finally {
                try {
                    rs.close();
                    pst.close();
                } catch (Exception e) {
                }
            }
            Records_table();
        }
        Arrears_TableColor();
    }

    public void recordNew() {
        txt_id.setText("");
        txt_Name.setText("");
        txt_type.setText("");
        txt_Amount.setText("");
        txt_Date.setCalendar(null);
        buttonGroup1.clearSelection();
        Description.setText("");

        txt_id.setEditable(true);
        txt_Name.setEditable(true);
        txt_type.setEditable(true);
        txt_Amount.setEditable(true);

        txt_Paid.setText("");
        txt_Remain.setText("");
        lbl_pay.setEnabled(false);
        txt_Paid.setEnabled(false);
        lbl_Remain.setEnabled(false);
        txt_Remain.setEnabled(false);
    }

    public void subtractValue() {
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

    public void txtUpdate() {
        if (txt_id.getText().isEmpty() || txt_Name.getText().isEmpty() || txt_type.getText().isEmpty() || txt_Amount.getText().isEmpty() || Description.getText().isEmpty() || txt_Date.equals(null) || buttonGroup1.getSelection() == null) {
            JOptionPane.showMessageDialog(null, "Please Select To Update");
        } else {
            double remain = Double.parseDouble(txt_Remain.getText());
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
            Arrears_TableColor();
        }
    }
    
    
    public void arrearsTableClick() {
        int row = Arrears_Table.getSelectedRow();
        String Table_click = (Arrears_Table.getModel().getValueAt(row, 0).toString());
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
        
        lbl_pay.setEnabled(true);
        txt_Paid.setEnabled(true);
        lbl_Remain.setEnabled(true);
        txt_Remain.setEnabled(true);
    
    }

    public void printArrears() {
        if (jTabbedPane1.getSelectedIndex() == 0) {
            MessageFormat header = new MessageFormat("WESTPOINT COSMETICS ARREARS CUSTOMERS");
            MessageFormat footer = new MessageFormat("Page 0");
            try {

                Arrears_Table.print(JTable.PrintMode.FIT_WIDTH, header, footer);

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
    }

    public void color() {
        Arrears_Table.getColumnModel().getColumn(0).setCellRenderer(new RecordsKeepingPane.CustomRenderer());
        Arrears_Table.getColumnModel().getColumn(1).setCellRenderer(new RecordsKeepingPane.CustomRenderer1());
        Arrears_Table.getColumnModel().getColumn(2).setCellRenderer(new RecordsKeepingPane.CustomRenderer2());
        Arrears_Table.getColumnModel().getColumn(3).setCellRenderer(new RecordsKeepingPane.CustomRenderer3());
        Arrears_Table.getColumnModel().getColumn(4).setCellRenderer(new RecordsKeepingPane.CustomRenderer4());
        Arrears_Table.getColumnModel().getColumn(5).setCellRenderer(new RecordsKeepingPane.CustomRenderer5());
        Arrears_Table.getTableHeader().setDefaultRenderer(new RecordsKeepingPane.HeaderColor());
    }

//    Saving order item into db
    public void saveOrder() {
        if (Or_type.getText().isEmpty() || Or_Cost.getText().isEmpty() || Or_Date.equals(null)) {
            JOptionPane.showMessageDialog(null, "Please Enter Details Before Saving");
        } else {
            try {
                 int id = Integer.parseInt(Or_id.getText());
                 int d = id + 1;
                 
                String sql = "Insert into Order_DB (id,Name,Item_Type,Cost,Date) values (?,?,?,?,?)";

                pst = conn.prepareStatement(sql);
                pst.setInt(1, d);
                pst.setString(2, "customer");
                pst.setString(3, Or_type.getText().toLowerCase().trim());
                pst.setString(4, Or_Cost.getText().toLowerCase().trim());
                pst.setString(5, ((JTextField) Or_Date.getDateEditor().getUiComponent()).getText().toLowerCase().trim());

                pst.execute();
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
            Order_TableColor();
        }
    }

    public void orderNew() {
        id.setText("");
        Or_type.setText("");
        Or_Cost.setText("");
        Or_Date.setCalendar(null);
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
            Order_TableColor();
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

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        buttonGroup1 = new javax.swing.ButtonGroup();
        jPanel1 = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jTabbedPane1 = new javax.swing.JTabbedPane();
        jPanel3 = new javax.swing.JPanel();
        jPanel5 = new javax.swing.JPanel();
        jPanel6 = new javax.swing.JPanel();
        txt_Name = new javax.swing.JTextField();
        txt_type = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        txt_Amount = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        btn_New = new javax.swing.JButton();
        btn_Save = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        Description = new javax.swing.JTextArea();
        jLabel7 = new javax.swing.JLabel();
        txt_id = new javax.swing.JTextField();
        paid = new javax.swing.JRadioButton();
        notPaid = new javax.swing.JRadioButton();
        txt_Paid = new javax.swing.JTextField();
        lbl_pay = new javax.swing.JLabel();
        lbl_Remain = new javax.swing.JLabel();
        txt_Remain = new javax.swing.JTextField();
        btn_Arrears = new javax.swing.JButton();
        txt_update = new javax.swing.JButton();
        txt_Date = new com.toedter.calendar.JDateChooser();
        jSeparator1 = new javax.swing.JSeparator();
        jSeparator2 = new javax.swing.JSeparator();
        jPanel7 = new javax.swing.JPanel();
        txt_searchKeep = new javax.swing.JTextField();
        jButton6 = new javax.swing.JButton();
        jPanel9 = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        Arrears_Table = new javax.swing.JTable();
        jPanel4 = new javax.swing.JPanel();
        jScrollPane3 = new javax.swing.JScrollPane();
        Order_Table = new javax.swing.JTable();
        jPanel8 = new javax.swing.JPanel();
        jLabel9 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        lbl_Date = new javax.swing.JLabel();
        Or_Cost = new javax.swing.JTextField();
        Or_type = new javax.swing.JTextField();
        Or_Update = new javax.swing.JButton();
        jLabel12 = new javax.swing.JLabel();
        Or_id = new javax.swing.JTextField();
        Or_Save = new javax.swing.JButton();
        Or_New = new javax.swing.JButton();
        Or_Print = new javax.swing.JButton();
        txt_OrSearch = new javax.swing.JTextField();
        Or_Refresh = new javax.swing.JButton();
        Or_Date = new com.toedter.calendar.JDateChooser();
        id = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("ADOM NTI RECORDS KEEPING FORM ");
        setResizable(false);

        jLabel1.setFont(new java.awt.Font("Tahoma", 1, 24)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(51, 0, 0));
        jLabel1.setText("                                     WESTPOINT COSMETICS ASANKRANGWA");
        jLabel1.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED, java.awt.Color.white, java.awt.Color.white, java.awt.Color.lightGray, java.awt.Color.lightGray));

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 1035, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        jTabbedPane1.setForeground(new java.awt.Color(0, 0, 51));
        jTabbedPane1.setFont(new java.awt.Font("Tahoma", 1, 15)); // NOI18N

        jPanel6.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Arrears Customers Details", javax.swing.border.TitledBorder.LEFT, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 1, 18), new java.awt.Color(0, 51, 51))); // NOI18N

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
        jLabel2.setForeground(new java.awt.Color(51, 0, 51));
        jLabel2.setText("Name");

        jLabel5.setFont(new java.awt.Font("Tahoma", 1, 15)); // NOI18N
        jLabel5.setForeground(new java.awt.Color(51, 0, 51));
        jLabel5.setText("Date");

        jLabel3.setFont(new java.awt.Font("Tahoma", 1, 15)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(51, 0, 51));
        jLabel3.setText("Type of Item");

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

        jLabel4.setFont(new java.awt.Font("Tahoma", 1, 15)); // NOI18N
        jLabel4.setForeground(new java.awt.Color(51, 0, 51));
        jLabel4.setText("Amount Owning in GH₵ ");

        jLabel6.setFont(new java.awt.Font("Tahoma", 1, 15)); // NOI18N
        jLabel6.setForeground(new java.awt.Color(51, 0, 51));
        jLabel6.setText("Customer description ");

        btn_New.setFont(new java.awt.Font("Times New Roman", 1, 15)); // NOI18N
        btn_New.setForeground(new java.awt.Color(51, 0, 0));
        btn_New.setIcon(new javax.swing.ImageIcon(getClass().getResource("/image/new.png"))); // NOI18N
        btn_New.setToolTipText("Click to Add New Customer");
        btn_New.setBorder(null);
        btn_New.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_NewActionPerformed(evt);
            }
        });

        btn_Save.setFont(new java.awt.Font("Times New Roman", 1, 15)); // NOI18N
        btn_Save.setForeground(new java.awt.Color(0, 0, 51));
        btn_Save.setIcon(new javax.swing.ImageIcon(getClass().getResource("/image/save2U.png"))); // NOI18N
        btn_Save.setToolTipText("Click to Save ");
        btn_Save.setBorder(null);
        btn_Save.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_SaveActionPerformed(evt);
            }
        });

        Description.setColumns(20);
        Description.setFont(new java.awt.Font("Times New Roman", 1, 15)); // NOI18N
        Description.setLineWrap(true);
        Description.setRows(5);
        Description.setWrapStyleWord(true);
        jScrollPane1.setViewportView(Description);

        jLabel7.setFont(new java.awt.Font("Tahoma", 1, 15)); // NOI18N
        jLabel7.setForeground(new java.awt.Color(51, 0, 51));
        jLabel7.setText("ID");

        txt_id.setFont(new java.awt.Font("Times New Roman", 1, 15)); // NOI18N
        txt_id.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txt_idActionPerformed(evt);
            }
        });

        buttonGroup1.add(paid);
        paid.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        paid.setForeground(new java.awt.Color(102, 0, 0));
        paid.setText("Paid");
        paid.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                paidActionPerformed(evt);
            }
        });

        buttonGroup1.add(notPaid);
        notPaid.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        notPaid.setForeground(new java.awt.Color(102, 0, 0));
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
        lbl_pay.setForeground(new java.awt.Color(51, 0, 51));
        lbl_pay.setText("Amount Paying in GH₵ ");

        lbl_Remain.setFont(new java.awt.Font("Tahoma", 1, 15)); // NOI18N
        lbl_Remain.setForeground(new java.awt.Color(51, 0, 51));
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
        btn_Arrears.setForeground(new java.awt.Color(51, 0, 51));
        btn_Arrears.setIcon(new javax.swing.ImageIcon(getClass().getResource("/image/print2.png"))); // NOI18N
        btn_Arrears.setToolTipText("Click to Print Arrears Customers");
        btn_Arrears.setBorder(null);
        btn_Arrears.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_ArrearsActionPerformed(evt);
            }
        });

        txt_update.setFont(new java.awt.Font("Times New Roman", 1, 15)); // NOI18N
        txt_update.setForeground(new java.awt.Color(51, 51, 255));
        txt_update.setIcon(new javax.swing.ImageIcon(getClass().getResource("/image/edit.png"))); // NOI18N
        txt_update.setToolTipText("Update Customer Records");
        txt_update.setBorder(null);
        txt_update.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txt_updateActionPerformed(evt);
            }
        });

        txt_Date.setDateFormatString("MMM EE d, yyyy");

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lbl_pay, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel7, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(lbl_Remain, javax.swing.GroupLayout.DEFAULT_SIZE, 195, Short.MAX_VALUE)
                            .addComponent(jLabel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addComponent(btn_New, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGap(18, 18, 18)
                        .addComponent(btn_Save, javax.swing.GroupLayout.PREFERRED_SIZE, 61, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(txt_update, javax.swing.GroupLayout.PREFERRED_SIZE, 63, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(1, 1, 1)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addComponent(paid)
                        .addGap(18, 18, 18)
                        .addComponent(notPaid)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                            .addGroup(jPanel6Layout.createSequentialGroup()
                                .addComponent(txt_id, javax.swing.GroupLayout.PREFERRED_SIZE, 76, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(0, 0, Short.MAX_VALUE))
                            .addComponent(txt_Date, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel6Layout.createSequentialGroup()
                                .addGap(0, 0, Short.MAX_VALUE)
                                .addComponent(btn_Arrears, javax.swing.GroupLayout.PREFERRED_SIZE, 57, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(txt_Remain)
                            .addComponent(txt_Paid)
                            .addComponent(txt_Amount)
                            .addComponent(txt_type)
                            .addComponent(txt_Name))
                        .addContainerGap())))
            .addComponent(jSeparator1)
            .addComponent(jSeparator2)
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addGap(5, 5, 5)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(txt_id)
                    .addComponent(jLabel7, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(15, 15, 15)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txt_Name, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel2))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel3)
                    .addComponent(txt_type, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel4)
                    .addComponent(txt_Amount, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txt_Paid, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lbl_pay))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txt_Remain, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lbl_Remain))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel5)
                    .addComponent(txt_Date, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(paid)
                            .addComponent(notPaid)))
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addGap(35, 35, 35)
                        .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel6)
                            .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 84, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 8, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(btn_New, javax.swing.GroupLayout.DEFAULT_SIZE, 44, Short.MAX_VALUE)
                    .addComponent(btn_Save, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(txt_update, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btn_Arrears, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jSeparator2, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel7.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Enter to Search", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 1, 18), new java.awt.Color(0, 51, 51))); // NOI18N

        txt_searchKeep.setBackground(new java.awt.Color(240, 240, 240));
        txt_searchKeep.setFont(new java.awt.Font("Tahoma", 0, 15)); // NOI18N
        txt_searchKeep.setText("Search...");
        txt_searchKeep.setToolTipText("Enter Name to Search");
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

        jButton6.setFont(new java.awt.Font("Times New Roman", 1, 15)); // NOI18N
        jButton6.setForeground(new java.awt.Color(51, 0, 0));
        jButton6.setIcon(new javax.swing.ImageIcon(getClass().getResource("/image/refreshDesign.png"))); // NOI18N
        jButton6.setBorder(null);
        jButton6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton6ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel7Layout = new javax.swing.GroupLayout(jPanel7);
        jPanel7.setLayout(jPanel7Layout);
        jPanel7Layout.setHorizontalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(txt_searchKeep, javax.swing.GroupLayout.PREFERRED_SIZE, 280, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jButton6, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(79, Short.MAX_VALUE))
        );
        jPanel7Layout.setVerticalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addGap(5, 5, 5)
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jButton6, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txt_searchKeep, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(5, 5, 5))
        );

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel7, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel9.setBorder(javax.swing.BorderFactory.createTitledBorder(""));

        Arrears_Table.setModel(new javax.swing.table.DefaultTableModel(
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
        Arrears_Table.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                Arrears_TableMouseClicked(evt);
            }
        });
        Arrears_Table.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                Arrears_TableKeyReleased(evt);
            }
        });
        jScrollPane2.setViewportView(Arrears_Table);

        javax.swing.GroupLayout jPanel9Layout = new javax.swing.GroupLayout(jPanel9);
        jPanel9.setLayout(jPanel9Layout);
        jPanel9Layout.setHorizontalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel9Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 727, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel9Layout.setVerticalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel9Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane2)
                .addContainerGap())
        );

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jPanel9, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addComponent(jPanel9, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        jTabbedPane1.addTab("Arrears Customers Details", jPanel3);

        Order_Table.setModel(new javax.swing.table.DefaultTableModel(
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
        jScrollPane3.setViewportView(Order_Table);

        jPanel8.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Customer Order Details", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 1, 14), new java.awt.Color(51, 0, 51))); // NOI18N
        jPanel8.setForeground(new java.awt.Color(255, 255, 255));

        jLabel9.setFont(new java.awt.Font("Tahoma", 1, 15)); // NOI18N
        jLabel9.setForeground(new java.awt.Color(51, 0, 51));
        jLabel9.setText("Item Type");

        jLabel10.setFont(new java.awt.Font("Tahoma", 1, 15)); // NOI18N
        jLabel10.setForeground(new java.awt.Color(51, 0, 51));
        jLabel10.setText("Cost Of Item");

        lbl_Date.setFont(new java.awt.Font("Tahoma", 1, 15)); // NOI18N
        lbl_Date.setForeground(new java.awt.Color(51, 0, 51));
        lbl_Date.setText("Date");

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
        Or_Update.setBorder(null);
        Or_Update.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                Or_UpdateActionPerformed(evt);
            }
        });

        jLabel12.setFont(new java.awt.Font("Tahoma", 1, 15)); // NOI18N
        jLabel12.setForeground(new java.awt.Color(51, 0, 51));
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
        Or_Save.setBorder(null);
        Or_Save.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                Or_SaveActionPerformed(evt);
            }
        });

        Or_New.setFont(new java.awt.Font("Times New Roman", 1, 15)); // NOI18N
        Or_New.setForeground(new java.awt.Color(51, 0, 0));
        Or_New.setIcon(new javax.swing.ImageIcon(getClass().getResource("/image/new.png"))); // NOI18N
        Or_New.setToolTipText("Enter New Order");
        Or_New.setBorder(null);
        Or_New.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                Or_NewActionPerformed(evt);
            }
        });

        Or_Print.setFont(new java.awt.Font("Times New Roman", 1, 15)); // NOI18N
        Or_Print.setForeground(new java.awt.Color(51, 0, 0));
        Or_Print.setIcon(new javax.swing.ImageIcon(getClass().getResource("/image/print2.png"))); // NOI18N
        Or_Print.setToolTipText("Click to Print");
        Or_Print.setBorder(null);
        Or_Print.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                Or_PrintActionPerformed(evt);
            }
        });

        txt_OrSearch.setBackground(new java.awt.Color(240, 240, 240));
        txt_OrSearch.setFont(new java.awt.Font("Times New Roman", 1, 15)); // NOI18N
        txt_OrSearch.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txt_OrSearchKeyReleased(evt);
            }
        });

        Or_Refresh.setFont(new java.awt.Font("Times New Roman", 1, 15)); // NOI18N
        Or_Refresh.setForeground(new java.awt.Color(0, 0, 51));
        Or_Refresh.setIcon(new javax.swing.ImageIcon(getClass().getResource("/image/refreshDesign.png"))); // NOI18N
        Or_Refresh.setBorder(null);
        Or_Refresh.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                Or_RefreshActionPerformed(evt);
            }
        });

        Or_Date.setDateFormatString("MMM EE d, yyyy");

        javax.swing.GroupLayout jPanel8Layout = new javax.swing.GroupLayout(jPanel8);
        jPanel8.setLayout(jPanel8Layout);
        jPanel8Layout.setHorizontalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addComponent(Or_New, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(Or_Save, javax.swing.GroupLayout.PREFERRED_SIZE, 44, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(Or_Update, javax.swing.GroupLayout.PREFERRED_SIZE, 51, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(127, 127, 127))
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addGap(2, 2, 2)
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel8Layout.createSequentialGroup()
                        .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(lbl_Date, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel12, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel9, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel10, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 147, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(Or_id, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                .addComponent(Or_Date, javax.swing.GroupLayout.PREFERRED_SIZE, 169, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                    .addComponent(Or_type, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 169, Short.MAX_VALUE)
                                    .addComponent(Or_Cost, javax.swing.GroupLayout.Alignment.LEADING))
                                .addComponent(Or_Print, javax.swing.GroupLayout.PREFERRED_SIZE, 48, javax.swing.GroupLayout.PREFERRED_SIZE))))
                    .addGroup(jPanel8Layout.createSequentialGroup()
                        .addComponent(txt_OrSearch, javax.swing.GroupLayout.PREFERRED_SIZE, 275, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(id, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(Or_Refresh, javax.swing.GroupLayout.DEFAULT_SIZE, 45, Short.MAX_VALUE))))
                .addGap(18, 18, 18))
        );
        jPanel8Layout.setVerticalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(txt_OrSearch)
                    .addComponent(Or_Refresh, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(18, 18, 18)
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel12)
                        .addComponent(Or_id, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(id, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel9)
                    .addComponent(Or_type, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel10)
                    .addComponent(Or_Cost, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(lbl_Date, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(Or_Date, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(20, 20, 20)
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(Or_Save, javax.swing.GroupLayout.DEFAULT_SIZE, 36, Short.MAX_VALUE)
                    .addComponent(Or_Update, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(Or_New, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(Or_Print, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap(295, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel8, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 848, Short.MAX_VALUE))
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane3, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 580, Short.MAX_VALUE)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel8, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jTabbedPane1.addTab("Enter Customer Order Details", jPanel4);

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(0, 0, 0)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jTabbedPane1)))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jTabbedPane1)
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

    private void txt_PaidActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txt_PaidActionPerformed
        Description.requestFocusInWindow();
    }//GEN-LAST:event_txt_PaidActionPerformed

    private void jButton6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton6ActionPerformed
        Records_table();
        Arrears_TableColor();
    }//GEN-LAST:event_jButton6ActionPerformed

    private void paidActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_paidActionPerformed
        status = "Paid";
    }//GEN-LAST:event_paidActionPerformed

    private void notPaidActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_notPaidActionPerformed
        status = "Not Paid";
    }//GEN-LAST:event_notPaidActionPerformed

    private void btn_SaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_SaveActionPerformed
        recordSave();
    }//GEN-LAST:event_btn_SaveActionPerformed

    private void btn_NewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_NewActionPerformed
        recordNew();
    }//GEN-LAST:event_btn_NewActionPerformed

    private void txt_PaidKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txt_PaidKeyReleased
        subtractValue();
    }//GEN-LAST:event_txt_PaidKeyReleased

    private void txt_updateActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txt_updateActionPerformed
        txtUpdate();
    }//GEN-LAST:event_txt_updateActionPerformed

    private void txt_PaidKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txt_PaidKeyTyped
        char c = evt.getKeyChar();
        if (!(Character.isDigit(c) || (c == KeyEvent.VK_BACK_SPACE) || (c == KeyEvent.VK_DELETE) || (c == KeyEvent.VK_PERIOD))) {
            getToolkit().beep();
            evt.consume();
        }
    }//GEN-LAST:event_txt_PaidKeyTyped

    private void txt_PaidKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txt_PaidKeyPressed

    }//GEN-LAST:event_txt_PaidKeyPressed

    private void txt_AmountKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txt_AmountKeyReleased

    }//GEN-LAST:event_txt_AmountKeyReleased

    private void txt_AmountKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txt_AmountKeyPressed

    }//GEN-LAST:event_txt_AmountKeyPressed

    private void txt_RemainActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txt_RemainActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txt_RemainActionPerformed

    private void txt_RemainKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txt_RemainKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_txt_RemainKeyPressed

    private void txt_RemainKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txt_RemainKeyReleased
        // TODO add your handling code here:
    }//GEN-LAST:event_txt_RemainKeyReleased

    private void txt_RemainKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txt_RemainKeyTyped
        // TODO add your handling code here:
    }//GEN-LAST:event_txt_RemainKeyTyped

    private void btn_ArrearsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_ArrearsActionPerformed
        printArrears();
    }//GEN-LAST:event_btn_ArrearsActionPerformed

    private void txt_searchKeepKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txt_searchKeepKeyReleased
        searchTableContentKeep(txt_searchKeep.getText().toString());
        color();
    }//GEN-LAST:event_txt_searchKeepKeyReleased

    private void Or_idActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_Or_idActionPerformed
       Or_type.requestFocusInWindow();
    }//GEN-LAST:event_Or_idActionPerformed

    private void Or_SaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_Or_SaveActionPerformed
        saveOrder();
        countOrder();
    }//GEN-LAST:event_Or_SaveActionPerformed

    private void Or_NewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_Or_NewActionPerformed
        orderNew();
    }//GEN-LAST:event_Or_NewActionPerformed

    private void Or_UpdateActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_Or_UpdateActionPerformed
        orderUpdate();
        countOrder();
    }//GEN-LAST:event_Or_UpdateActionPerformed

    private void Or_PrintActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_Or_PrintActionPerformed
        orderPrint();
    }//GEN-LAST:event_Or_PrintActionPerformed

    private void Order_TableKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_Order_TableKeyReleased
        if (evt.getKeyCode() == KeyEvent.VK_DOWN || evt.getKeyCode() == KeyEvent.VK_UP) {
            orderTableClick();
        }
    }//GEN-LAST:event_Order_TableKeyReleased

    private void Order_TableMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_Order_TableMouseClicked
        orderTableClick();
    }//GEN-LAST:event_Order_TableMouseClicked

    private void Or_RefreshActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_Or_RefreshActionPerformed
        Order_table();
        Order_TableColor();
    }//GEN-LAST:event_Or_RefreshActionPerformed

    private void txt_OrSearchKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txt_OrSearchKeyReleased
        searchTableOrderCustomer(txt_OrSearch.getText().toString());
        Order_TableColor();
    }//GEN-LAST:event_txt_OrSearchKeyReleased

    private void txt_NameActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txt_NameActionPerformed
        txt_type.requestFocusInWindow();
    }//GEN-LAST:event_txt_NameActionPerformed

    private void txt_idActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txt_idActionPerformed
        txt_Name.requestFocusInWindow();
    }//GEN-LAST:event_txt_idActionPerformed

    private void txt_typeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txt_typeActionPerformed
        // TODO add your handling code here:
        txt_Amount.requestFocusInWindow();
    }//GEN-LAST:event_txt_typeActionPerformed

    private void txt_AmountActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txt_AmountActionPerformed
        txt_Paid.requestFocusInWindow();
    }//GEN-LAST:event_txt_AmountActionPerformed

    private void Or_typeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_Or_typeActionPerformed
        Or_Cost.requestFocusInWindow();
    }//GEN-LAST:event_Or_typeActionPerformed

    private void Or_CostActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_Or_CostActionPerformed
        Or_Save.requestFocusInWindow();
    }//GEN-LAST:event_Or_CostActionPerformed

    private void txt_searchKeepFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txt_searchKeepFocusGained
        txt_searchKeep.setText("");
    }//GEN-LAST:event_txt_searchKeepFocusGained

    private void txt_searchKeepFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txt_searchKeepFocusLost
        txt_searchKeep.setText("Search...");
    }//GEN-LAST:event_txt_searchKeepFocusLost

    private void Arrears_TableMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_Arrears_TableMouseClicked
       arrearsTableClick();
    }//GEN-LAST:event_Arrears_TableMouseClicked

    private void Arrears_TableKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_Arrears_TableKeyReleased
       if (evt.getKeyCode() == KeyEvent.VK_DOWN || evt.getKeyCode() == KeyEvent.VK_UP) {
            arrearsTableClick();
        }
    }//GEN-LAST:event_Arrears_TableKeyReleased

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) throws UnsupportedLookAndFeelException, IllegalAccessException, ClassNotFoundException, InstantiationException {
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
            java.util.logging.Logger.getLogger(RecordsKeepingPane.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(RecordsKeepingPane.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(RecordsKeepingPane.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(RecordsKeepingPane.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        UIManager.setLookAndFeel("com.jtattoo.plaf.aluminium.AluminiumLookAndFeel");
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new RecordsKeepingPane().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTable Arrears_Table;
    private javax.swing.JTextArea Description;
    private javax.swing.JTextField Or_Cost;
    private com.toedter.calendar.JDateChooser Or_Date;
    private javax.swing.JButton Or_New;
    private javax.swing.JButton Or_Print;
    private javax.swing.JButton Or_Refresh;
    private javax.swing.JButton Or_Save;
    private javax.swing.JButton Or_Update;
    private javax.swing.JTextField Or_id;
    private javax.swing.JTextField Or_type;
    private javax.swing.JTable Order_Table;
    private javax.swing.JButton btn_Arrears;
    private javax.swing.JButton btn_New;
    private javax.swing.JButton btn_Save;
    private javax.swing.ButtonGroup buttonGroup1;
    private javax.swing.JLabel id;
    private javax.swing.JButton jButton6;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JPanel jPanel9;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JSeparator jSeparator2;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JLabel lbl_Date;
    private javax.swing.JLabel lbl_Remain;
    private javax.swing.JLabel lbl_pay;
    private javax.swing.JRadioButton notPaid;
    private javax.swing.JRadioButton paid;
    private javax.swing.JTextField txt_Amount;
    private com.toedter.calendar.JDateChooser txt_Date;
    private javax.swing.JTextField txt_Name;
    private javax.swing.JTextField txt_OrSearch;
    private javax.swing.JTextField txt_Paid;
    private javax.swing.JTextField txt_Remain;
    private javax.swing.JTextField txt_id;
    private javax.swing.JTextField txt_searchKeep;
    private javax.swing.JTextField txt_type;
    private javax.swing.JButton txt_update;
    // End of variables declaration//GEN-END:variables

    private String status;

}
