/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gui;

import static gui.CustomerRegistration.jTable1;
import static gui.Invoice.jComboBox3;
import static gui.Invoice.jTable1;
import static gui.LoadProductGRN.jTable1;
import static gui.RegisterProducts.loadSubCategories;
import java.awt.Color;
import java.awt.Font;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.Vector;
import java.util.regex.Pattern;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import model.MySQL;

/**
 *
 * @author Home
 */
public class LoadProductInvoice extends javax.swing.JDialog {

    /**
     * Creates new form LoadProductInvoice
     *
     */
    Invoice in;

    public LoadProductInvoice(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();
        jTable1.getTableHeader().setFont(new Font("Arial", Font.BOLD, 17));
        jTable1.setRowHeight(24);

    }

    public LoadProductInvoice(Invoice parent, boolean modal) {
        super(parent, modal);
        initComponents();
        this.in = parent;
        jTable1.getTableHeader().setFont(new Font("Arial", Font.BOLD, 17));
        jTable1.setRowHeight(24);

    }

    public static void loadStocksCode() {

        try {

            String code = Invoice.jTextField2.getText();

            ResultSet rs = MySQL.search("SELECT DISTINCT `stock`.`id`,`products`.`name`,`qty_types`.`name`,`products`.`code`,`category`.`name`,`sub_categories`.`name`,`brand`.`name`,`stock`.`qty`,`stock`.`cost`,`stock`.`selling_price`,`stock`.`mfd`,`stock`.`exd` FROM `stock` INNER JOIN `grn` ON `stock`.`grn_id` = `grn`.`id` INNER JOIN `products` ON `stock`.`product_id` = `products`.`id` INNER JOIN `brand` ON `products`.`brand_id` = `brand`.`id` INNER JOIN `category` ON `products`.`category_id` = `category`.`id` INNER JOIN `sub_categories` ON `products`.`sub_category_id` = `sub_categories`.`id` INNER JOIN `qty_types` ON `products`.`qty_types_id` = `qty_types`.`id` WHERE `products`.`code`='" + code + "' AND `products`.`status_id`='1' AND `stock`.`qty`>'0' AND `stock`.`exd` > NOW() ORDER BY `products`.`name` ASC;");
            DefaultTableModel dtm = (DefaultTableModel) jTable1.getModel();
            dtm.setRowCount(0);

            while (rs.next()) {

                Vector v = new Vector();
                v.add(rs.getString("stock.id"));

                v.add(rs.getString("category.name"));
                v.add(rs.getString("sub_categories.name"));
                v.add(rs.getString("brand.name"));
                v.add(rs.getString("products.name"));
                v.add(rs.getString("products.code"));
                v.add(rs.getString("stock.qty"));

                v.add(rs.getString("qty_types.name"));
                //v.add(rs.getString("stock.cost"));
                v.add(rs.getString("stock.selling_price"));
                v.add(rs.getString("stock.mfd"));
                v.add(rs.getString("stock.exd"));

                dtm.addRow(v);

            }

            jTable1.setModel(dtm);

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public static void loadStocksName() {

        try {

            String pname = Invoice.jComboBox3.getSelectedItem().toString();

            ResultSet rs = MySQL.search("SELECT DISTINCT `stock`.`id`,`products`.`name`,`qty_types`.`name`,`products`.`code`,`category`.`name`,`sub_categories`.`name`,`brand`.`name`,`stock`.`qty`,`stock`.`cost`,`stock`.`selling_price`,`stock`.`mfd`,`stock`.`exd` FROM `stock` INNER JOIN `grn` ON `stock`.`grn_id` = `grn`.`id` INNER JOIN `products` ON `stock`.`product_id` = `products`.`id` INNER JOIN `brand` ON `products`.`brand_id` = `brand`.`id` INNER JOIN `category` ON `products`.`category_id` = `category`.`id` INNER JOIN `sub_categories` ON `products`.`sub_category_id` = `sub_categories`.`id` INNER JOIN `qty_types` ON `products`.`qty_types_id` = `qty_types`.`id` WHERE `products`.`name`='" + pname + "' AND `products`.`status_id`='1' AND `stock`.`qty`>'0' AND `stock`.`exd` > NOW() ORDER BY `products`.`name` ASC;");
            DefaultTableModel dtm = (DefaultTableModel) jTable1.getModel();
            dtm.setRowCount(0);

            while (rs.next()) {

                Vector v = new Vector();
                v.add(rs.getString("stock.id"));

                v.add(rs.getString("category.name"));
                v.add(rs.getString("sub_categories.name"));
                v.add(rs.getString("brand.name"));
                v.add(rs.getString("products.name"));
                v.add(rs.getString("products.code"));
                v.add(rs.getString("stock.qty"));

                v.add(rs.getString("qty_types.name"));
                //v.add(rs.getString("stock.cost"));
                v.add(rs.getString("stock.selling_price"));
                v.add(rs.getString("stock.mfd"));
                v.add(rs.getString("stock.exd"));

                dtm.addRow(v);

            }

            jTable1.setModel(dtm);

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
        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        jLabel21 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        jTable1.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Stock Id", "Category", "Sub-Catgory", "Brand", "Name", "Code", "Quantity", "Qty-Type", "Selling Price", "MFD", "EXD"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jTable1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTable1MouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                jTable1MouseEntered(evt);
            }
        });
        jScrollPane1.setViewportView(jTable1);

        jLabel21.setFont(new java.awt.Font("Arial", 1, 24)); // NOI18N
        jLabel21.setText("Avalibale Stocks :");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 1456, Short.MAX_VALUE)
                .addContainerGap())
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(607, 607, 607)
                .addComponent(jLabel21, javax.swing.GroupLayout.PREFERRED_SIZE, 215, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel21, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(26, 26, 26)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
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
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void jTable1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTable1MouseClicked
        // TODO add your handling code here:
        if (evt.getClickCount() == 2) {

            int r = jTable1.getSelectedRow();
            String stock_id = jTable1.getValueAt(r, 0).toString();
            if (r == -1) {
                JOptionPane.showMessageDialog(this, "Please Select a Stock.", "Warning", JOptionPane.WARNING_MESSAGE);
            } else {

                DefaultTableModel dtm = (DefaultTableModel) Invoice.jTable1.getModel();
                boolean isFound = false;
                int x = -1;

                for (int i = 0; i < dtm.getRowCount(); i++) {
                    String s = Invoice.jTable1.getValueAt(i, 0).toString();

                    if (s.equals(stock_id)) {
                        isFound = true;
                        x = i;
                        break;
                    }
                }
                if (isFound) {

                    JOptionPane.showMessageDialog(this, "This Product Alreday Added to Invoice.", "Warning", JOptionPane.WARNING_MESSAGE);

                } else {

                    String name = jTable1.getValueAt(r, 4).toString();
                    String code = jTable1.getValueAt(r, 5).toString();
                    //String qty_type = jTable1.getValueAt(r, 6).toString();

                    String selling_price = jTable1.getValueAt(r, 8).toString();

                    String qty = jTable1.getValueAt(r, 6).toString();

                    Vector v = new Vector();
                    v.add(stock_id);

                    v.add(name);
                    v.add(code);
                    v.add("1");
                    v.add(Double.parseDouble(selling_price));
                    v.add(Double.parseDouble(selling_price));

                    Invoice.InvoiceItem(v);
                    in.updateTotal();
                    in.jTextField2.setText("");
                    in.jComboBox3.setSelectedIndex(0);
                    this.dispose();

//
                }
//                    }
            }
        }
    }//GEN-LAST:event_jTable1MouseClicked

    private void jTable1MouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTable1MouseEntered
        // TODO add your handling code here:
    }//GEN-LAST:event_jTable1MouseEntered

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
            java.util.logging.Logger.getLogger(LoadProductInvoice.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(LoadProductInvoice.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(LoadProductInvoice.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(LoadProductInvoice.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the dialog */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                LoadProductInvoice dialog = new LoadProductInvoice(new javax.swing.JFrame(), true);
                dialog.addWindowListener(new java.awt.event.WindowAdapter() {
                    @Override
                    public void windowClosing(java.awt.event.WindowEvent e) {
                        System.exit(0);
                    }
                });
                dialog.setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel jLabel21;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private static javax.swing.JTable jTable1;
    // End of variables declaration//GEN-END:variables
}
