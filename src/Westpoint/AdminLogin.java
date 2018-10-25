package Westpoint;

import Intel.connectDB;
import Westpoint.Interface;
import java.awt.Color;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import java.awt.event.WindowEvent;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

public class AdminLogin extends javax.swing.JFrame {

    Connection conn = null;
    ResultSet rs = null;
    PreparedStatement pst = null;

    public AdminLogin() {
        initComponents();
        conn = connectDB.ConnecrDb();
        try {
            Image i = ImageIO.read(getClass().getResource("/image/doubleUU.jpg"));
            setIconImage(i);
        } catch (Exception e) {

        }
    }

    public void close() {
        WindowEvent winClosingEvent = new WindowEvent(this, WindowEvent.WINDOW_CLOSING);
        Toolkit.getDefaultToolkit().getSystemEventQueue().postEvent(winClosingEvent);
    }

    public void loginAdmin() {

        String sql = "select * from accounts where username=? and password=?";
        try {
            pst = conn.prepareStatement(sql);
            pst.setString(1, Aname.getText());
            pst.setString(2, Apass.getText());
            rs = pst.executeQuery();

            if (rs.next()) {
                // When username and password is correct
                rs.close();
                pst.close();
                close();
                AdminPanel Admin = new AdminPanel();
                this.setExtendedState(JFrame.MAXIMIZED_BOTH);
                Admin.setVisible(true);

                Admin.pack();

            } else {
                //When username and password is not correct
                showMessage.setText("Incorrect Admin or Pwd");
                showMessage.setVisible(true);
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

    public void loginUser() {

        String sql = "select * from accounts where username=? and password=?";
        try {
            pst = conn.prepareStatement(sql);
            pst.setString(1, Aname.getText());
            pst.setString(2, Apass.getText());
            rs = pst.executeQuery();

            if (rs.next()) {
                // When username and password is correct
                rs.close();
                pst.close();
                close();
                MainUserPanel user = new MainUserPanel();
                user.setVisible(true);

                user.pack();

            } else {
                //When username and password is not correct
                showMessage.setText("Incorrect User or Pwd");
                showMessage.setVisible(true);
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

    public void loginAccount() {
        String user = Aname.getText().trim();
        String pass = Apass.getText().trim();

        if (user.isEmpty() || pass.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Enter account details");
        } else {

            try {
                String status = "select fullname,status from accounts where username='" + user + "' and password='" + pass + "'";
                pst = conn.prepareStatement(status);
                rs = pst.executeQuery();

                if (rs.next()) {

                    String sta = rs.getString("status");
                    String fname = rs.getString("fullname");

                    if (sta.equalsIgnoreCase("admin")) {
                        rs.close();
                        pst.close();

                        loginAdmin();
                    } else if (sta.equalsIgnoreCase("user")) {
                        
                        rs.close();
                        pst.close();

                        loginUser();
                        MainUserPanel.Cmd_User.setSelectedItem(fname);
                    }

                } else {
                    JOptionPane.showMessageDialog(null, "Account does not exist");
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

    public void checkPassword() {
        // Code that enable user to view password
        if (Checkpass.isSelected()) {
            Apass.setEchoChar((char) 0);
        } else {
            Apass.setEchoChar('*');
        }
    }

    public void reset() {
        try {
            String Adminname = JOptionPane.showInputDialog("Enter previous username");
            String Adminpassword = JOptionPane.showInputDialog("Enter previous password");

            String sql = "select * from accounts where username='" + Adminname + "' and password='" + Adminpassword + "'";
            pst = conn.prepareStatement(sql);
            rs = pst.executeQuery();

            if (rs.next()) {
                String sql1 = "delete from accounts where username='" + Adminname + "' and password='" + Adminpassword + "'";

                try {
                    pst = conn.prepareStatement(sql1);
                    pst.execute();

                    JOptionPane.showMessageDialog(null, "CLICK OK to Continue", "PREVIOUS ADMINNAME AND PASSWORD IS DELETED", JOptionPane.INFORMATION_MESSAGE);

                    String fname = JOptionPane.showInputDialog("Enter fullname");
                    String uname = JOptionPane.showInputDialog("Enter username");
                    String fullName = JOptionPane.showInputDialog("Enter password");
                    String status = JOptionPane.showInputDialog("Enter status");

                    String sql3 = "Insert into accounts (fullname,username,password,status) values (?,?,?,?)";

                    pst = conn.prepareStatement(sql3);
                    pst.setString(1, fname);
                    pst.setString(2, uname);
                    pst.setString(3, fullName);
                    pst.setString(4, status);
                    pst.execute();

                    JOptionPane.showMessageDialog(null, "Account is reset");

                } catch (Exception e) {
                    JOptionPane.showMessageDialog(null, e);
                }

            } else {
                JOptionPane.showMessageDialog(null, "Adminname & password don't match", "SORRY!, TRY AGAIN", JOptionPane.ERROR_MESSAGE);
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

        jMenuBar1 = new javax.swing.JMenuBar();
        jMenu1 = new javax.swing.JMenu();
        jMenu2 = new javax.swing.JMenu();
        jPanel1 = new javax.swing.JPanel();
        jPanel3 = new javax.swing.JPanel();
        jPanel4 = new javax.swing.JPanel();
        Apass = new javax.swing.JPasswordField();
        jSeparator3 = new javax.swing.JSeparator();
        Checkpass = new javax.swing.JCheckBox();
        jLabel4 = new javax.swing.JLabel();
        Aname = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        jSeparator2 = new javax.swing.JSeparator();
        btn_login = new javax.swing.JButton();
        showMessage = new javax.swing.JLabel();
        btn_Back = new javax.swing.JButton();
        jLabel2 = new javax.swing.JLabel();
        jSeparator1 = new javax.swing.JSeparator();
        menubar = new javax.swing.JMenuBar();
        jMenu3 = new javax.swing.JMenu();
        view = new javax.swing.JMenuItem();
        jSeparator5 = new javax.swing.JPopupMenu.Separator();
        finish = new javax.swing.JMenuItem();
        jSeparator6 = new javax.swing.JPopupMenu.Separator();
        exit = new javax.swing.JMenuItem();
        jSeparator7 = new javax.swing.JPopupMenu.Separator();
        jMenu4 = new javax.swing.JMenu();
        back = new javax.swing.JMenuItem();
        Reset = new javax.swing.JMenuItem();
        jSeparator8 = new javax.swing.JPopupMenu.Separator();
        jMenuItem1 = new javax.swing.JMenuItem();

        jMenu1.setText("File");
        jMenuBar1.add(jMenu1);

        jMenu2.setText("Edit");
        jMenuBar1.add(jMenu2);

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("USER LOGIN FRAME");
        setResizable(false);

        jPanel4.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "ENTER ACCOUNT DETAILS", javax.swing.border.TitledBorder.CENTER, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 1, 18), new java.awt.Color(0, 102, 51))); // NOI18N

        Apass.setBackground(new java.awt.Color(204, 204, 204));
        Apass.setFont(new java.awt.Font("Serif", 2, 18)); // NOI18N
        Apass.setForeground(new java.awt.Color(204, 0, 0));
        Apass.setToolTipText("Enter Password here");
        Apass.setBorder(null);
        Apass.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                ApassKeyPressed(evt);
            }
        });

        Checkpass.setFont(new java.awt.Font("Serif", 0, 12)); // NOI18N
        Checkpass.setToolTipText("Tick to see password");
        Checkpass.setBorder(null);
        Checkpass.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                CheckpassActionPerformed(evt);
            }
        });

        jLabel4.setFont(new java.awt.Font("Tahoma", 1, 24)); // NOI18N
        jLabel4.setForeground(new java.awt.Color(0, 0, 51));
        jLabel4.setText("Enter Username");

        Aname.setBackground(new java.awt.Color(204, 204, 204));
        Aname.setFont(new java.awt.Font("Serif", 2, 18)); // NOI18N
        Aname.setForeground(new java.awt.Color(204, 0, 0));
        Aname.setToolTipText("Enter Username here");
        Aname.setBorder(null);
        Aname.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                AnameActionPerformed(evt);
            }
        });

        jLabel3.setFont(new java.awt.Font("Tahoma", 1, 24)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(0, 0, 51));
        jLabel3.setText("Enter Password");
        jLabel3.setToolTipText("");

        btn_login.setIcon(new javax.swing.ImageIcon(getClass().getResource("/image/newLogin.png"))); // NOI18N
        btn_login.setToolTipText("Click to login");
        btn_login.setBorder(null);
        btn_login.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_loginActionPerformed(evt);
            }
        });

        showMessage.setFont(new java.awt.Font("Tahoma", 2, 12)); // NOI18N
        showMessage.setForeground(new java.awt.Color(255, 51, 51));
        showMessage.addMouseMotionListener(new java.awt.event.MouseMotionAdapter() {
            public void mouseMoved(java.awt.event.MouseEvent evt) {
                showMessageMouseMoved(evt);
            }
        });

        btn_Back.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        btn_Back.setIcon(new javax.swing.ImageIcon(getClass().getResource("/image/back.png"))); // NOI18N
        btn_Back.setToolTipText("<< Go Back  ");
        btn_Back.setBorder(null);
        btn_Back.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_BackActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGap(15, 15, 15)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(btn_Back, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(Apass))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(Checkpass, javax.swing.GroupLayout.PREFERRED_SIZE, 17, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jSeparator2)
                    .addComponent(Aname)
                    .addComponent(jSeparator3)
                    .addComponent(jLabel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel4, javax.swing.GroupLayout.DEFAULT_SIZE, 260, Short.MAX_VALUE)
                    .addComponent(showMessage, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btn_login, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addComponent(btn_Back, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(Aname, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jSeparator2, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(Checkpass, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(Apass, javax.swing.GroupLayout.DEFAULT_SIZE, 31, Short.MAX_VALUE))
                .addGap(4, 4, 4)
                .addComponent(jSeparator3, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(showMessage, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btn_login, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        jLabel2.setFont(new java.awt.Font("Tahoma", 1, 24)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(51, 0, 0));
        jLabel2.setText("WESTPOINT COSMETICS");
        jLabel2.setToolTipText("");
        jLabel2.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));

        jSeparator1.setForeground(new java.awt.Color(255, 255, 255));

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(5, 5, 5)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jPanel4, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel2, javax.swing.GroupLayout.DEFAULT_SIZE, 307, Short.MAX_VALUE)
                    .addComponent(jSeparator1))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(2, 2, 2)
                .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 48, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 15, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0))
        );

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(0, 0, 0)
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, 314, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        menubar.setBackground(new java.awt.Color(204, 204, 255));
        menubar.setBorder(null);

        jMenu3.setText("File");
        jMenu3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenu3ActionPerformed(evt);
            }
        });

        view.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_S, java.awt.event.InputEvent.CTRL_MASK));
        view.setIcon(new javax.swing.ImageIcon(getClass().getResource("/image/daily.png"))); // NOI18N
        view.setText("View Daily Sales");
        view.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                viewActionPerformed(evt);
            }
        });
        jMenu3.add(view);
        jMenu3.add(jSeparator5);

        finish.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_I, java.awt.event.InputEvent.CTRL_MASK));
        finish.setIcon(new javax.swing.ImageIcon(getClass().getResource("/image/Update.png"))); // NOI18N
        finish.setText("View Finishing Items");
        finish.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                finishActionPerformed(evt);
            }
        });
        jMenu3.add(finish);
        jMenu3.add(jSeparator6);

        exit.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_E, java.awt.event.InputEvent.SHIFT_MASK | java.awt.event.InputEvent.CTRL_MASK));
        exit.setIcon(new javax.swing.ImageIcon(getClass().getResource("/image/sales2.png"))); // NOI18N
        exit.setText("Exit");
        exit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                exitActionPerformed(evt);
            }
        });
        jMenu3.add(exit);
        jMenu3.add(jSeparator7);

        menubar.add(jMenu3);

        jMenu4.setText("Edit");

        back.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_B, java.awt.event.InputEvent.CTRL_MASK));
        back.setIcon(new javax.swing.ImageIcon(getClass().getResource("/image/back.png"))); // NOI18N
        back.setText("Back");
        back.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                backActionPerformed(evt);
            }
        });
        jMenu4.add(back);

        Reset.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_R, java.awt.event.InputEvent.SHIFT_MASK | java.awt.event.InputEvent.CTRL_MASK));
        Reset.setIcon(new javax.swing.ImageIcon(getClass().getResource("/image/reset.png"))); // NOI18N
        Reset.setText("Reset Password");
        Reset.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ResetActionPerformed(evt);
            }
        });
        jMenu4.add(Reset);
        jMenu4.add(jSeparator8);

        jMenuItem1.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_M, java.awt.event.InputEvent.CTRL_MASK));
        jMenuItem1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/image/load.png"))); // NOI18N
        jMenuItem1.setText("Dev");
        jMenuItem1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem1ActionPerformed(evt);
            }
        });
        jMenu4.add(jMenuItem1);

        menubar.add(jMenu4);

        setJMenuBar(menubar);

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

    private void showMessageMouseMoved(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_showMessageMouseMoved
        showMessage.setText("");
    }//GEN-LAST:event_showMessageMouseMoved

    private void btn_loginActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_loginActionPerformed
        loginAccount();
    }//GEN-LAST:event_btn_loginActionPerformed

    private void AnameActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_AnameActionPerformed
        Apass.requestFocusInWindow();
    }//GEN-LAST:event_AnameActionPerformed

    private void CheckpassActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_CheckpassActionPerformed
        checkPassword();
    }//GEN-LAST:event_CheckpassActionPerformed

    private void ApassKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_ApassKeyPressed
        // Pressing Enter Key to Login
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            loginAccount();
        }
    }//GEN-LAST:event_ApassKeyPressed

    private void btn_BackActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_BackActionPerformed
        try {
            Interface s = new Interface();
            s.setVisible(true);
            this.dispose();
        } catch (Exception e) {

        }
    }//GEN-LAST:event_btn_BackActionPerformed

    private void jMenu3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenu3ActionPerformed
        System.exit(0);
    }//GEN-LAST:event_jMenu3ActionPerformed

    private void ResetActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ResetActionPerformed
        int ask = JOptionPane.showConfirmDialog(null, "Do you want to Reset Account ?", "RESET ACCOUNT", JOptionPane.YES_NO_OPTION);
        if (ask == 0) {
            reset();
        }
    }//GEN-LAST:event_ResetActionPerformed

    private void exitActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_exitActionPerformed
        // TODO add your handling code here:
        System.exit(0);
    }//GEN-LAST:event_exitActionPerformed

    private void backActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_backActionPerformed
        try {
            Interface s = new Interface();
            s.setVisible(true);
            this.dispose();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
        }
    }//GEN-LAST:event_backActionPerformed

    private void viewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_viewActionPerformed
        try {
            SalesForDay Daily = new SalesForDay();
            Daily.setVisible(true);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
        }
    }//GEN-LAST:event_viewActionPerformed

    private void finishActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_finishActionPerformed
        try {
            FinishingItemPane fin = new FinishingItemPane();
            fin.setVisible(true);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
        }
    }//GEN-LAST:event_finishActionPerformed

    private void jMenuItem1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem1ActionPerformed
         try {
            secretLogin s = new secretLogin();
            s.setVisible(true);
            this.dispose();
        } catch (Exception e) {

        }
    }//GEN-LAST:event_jMenuItem1ActionPerformed

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
            java.util.logging.Logger.getLogger(AdminLogin.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(AdminLogin.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(AdminLogin.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(AdminLogin.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        UIManager.setLookAndFeel("com.jtattoo.plaf.aluminium.AluminiumLookAndFeel");
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new AdminLogin().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTextField Aname;
    private javax.swing.JPasswordField Apass;
    private javax.swing.JCheckBox Checkpass;
    private javax.swing.JMenuItem Reset;
    private javax.swing.JMenuItem back;
    private javax.swing.JButton btn_Back;
    private javax.swing.JButton btn_login;
    private javax.swing.JMenuItem exit;
    private javax.swing.JMenuItem finish;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JMenu jMenu1;
    private javax.swing.JMenu jMenu2;
    private javax.swing.JMenu jMenu3;
    private javax.swing.JMenu jMenu4;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JMenuItem jMenuItem1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JSeparator jSeparator2;
    private javax.swing.JSeparator jSeparator3;
    private javax.swing.JPopupMenu.Separator jSeparator5;
    private javax.swing.JPopupMenu.Separator jSeparator6;
    private javax.swing.JPopupMenu.Separator jSeparator7;
    private javax.swing.JPopupMenu.Separator jSeparator8;
    public static javax.swing.JMenuBar menubar;
    private javax.swing.JLabel showMessage;
    private javax.swing.JMenuItem view;
    // End of variables declaration//GEN-END:variables
}
