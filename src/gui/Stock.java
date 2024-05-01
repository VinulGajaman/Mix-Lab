/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gui;

import static gui.CustomerRegistration.jTable1;
import static gui.ProductUpdate.jComboBox2;
import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.sql.ResultSet;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Date;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Pattern;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import model.MySQL;
import org.jdesktop.swingx.calendar.DateUtils;

/**
 *
 * @author Home
 */
public class Stock extends javax.swing.JPanel {

    /**
     * Creates new form Stock
     */
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
    
    public Stock() {
        
        try {
            ResultSet rs8 = MySQL.search("SELECT * FROM `user` WHERE `id`='" + SignIn.userId + "'");
            rs8.next();
            
            String role_id = rs8.getString("role_id");
            
            initComponents();
            if (role_id.equals("2")) {
                
                jPanel3.setVisible(false);
            }
            jTable1.getTableHeader().setFont(new Font("Arial", Font.BOLD, 16));
            jTable1.setRowHeight(25);
            jTable1.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
                
                public Component getTableCellRendererComponent(JTable table,
                        Object value, boolean isSelected, boolean hasFocus, int row, int col) {
                    
                    Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, col);
                    
                    String exd_date = (String) table.getModel().getValueAt(row, 8);
                    //Date newDate = DateUtils.addMonths(new Date(), 1);
                    LocalDate futureDate = LocalDate.now().plusMonths(1);
                    
                    try {
                        
                        Date dt = new SimpleDateFormat("yyyy-MM-dd").parse(exd_date);
                        Date ft = new SimpleDateFormat("yyyy-MM-dd").parse(futureDate.toString());
                        
                        if (ft.after(dt)) {
                            c.setBackground(Color.RED);
                            c.setForeground(Color.WHITE);
                            
                        } else {
                            c.setBackground(new Color(35, 63, 64));
                            c.setForeground(Color.WHITE);
                            
                        }

                        //System.out.println(super.getBackground());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    //System.out.println(status);

                    return this;
                }
            });
            
            loadCategories();
            loadBrands();
            loadStocks();
            jComboBox4.setEnabled(false);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public void loadSubCategories() {
        
        String selectedCategory = jComboBox1.getSelectedItem().toString();
        
        System.out.println(selectedCategory);
        
        try {
            
            ResultSet rs = MySQL.search("SELECT * FROM `sub_categories` INNER JOIN `category` ON `sub_categories`.`category_id`=`category`.`id` WHERE `category`.`name`='" + selectedCategory + "'");
            
            Vector v = new Vector();
            v.add("Select");
            
            while (rs.next()) {
                
                v.add(rs.getString("sub_categories.name"));
                
            }
            
            jComboBox4.setModel(new DefaultComboBoxModel(v));
            
        } catch (Exception e) {
            
            e.printStackTrace();
            
        }
        
    }
    
    public void loadCategories() {
        
        try {
            
            ResultSet rs = MySQL.search("SELECT `name` FROM `category` ORDER BY `name` ASC");
            
            Vector v = new Vector();
            v.add("Select");
            
            while (rs.next()) {
                v.add(rs.getString("name"));
            }
            
            jComboBox1.setModel(new DefaultComboBoxModel(v));
            
        } catch (Exception e) {
            
            e.printStackTrace();
        }
        
    }
    
    public void loadBrands() {
        
        try {
            
            ResultSet rs = MySQL.search("SELECT `name` FROM `brand` ORDER BY `name` ASC");
            
            Vector v = new Vector();
            v.add("Select");
            
            while (rs.next()) {
                v.add(rs.getString("name"));
            }
            
            jComboBox3.setModel(new DefaultComboBoxModel(v));
            
        } catch (Exception e) {
            
            e.printStackTrace();
        }
        
    }
    
    public void reset() {
        
        jComboBox1.setSelectedIndex(0);
//        s
        jComboBox3.setSelectedIndex(0);
        jTextField2.setText("");
        jTextField3.setText("");
        jTextField4.setText("");
        jDateChooser1.setDate(null);
        jDateChooser2.setDate(null);
        jDateChooser3.setDate(null);
        jDateChooser4.setDate(null);
        jComboBox2.setSelectedIndex(0);
        jLabel10.setText("0.00");
        jTextField1.setText("");
        
        jTable1.clearSelection();
        
        loadStocks();
    }
    
    public void loadStocks() {
        
        try {
            
            ResultSet rs = MySQL.search("SELECT DISTINCT `stock`.`id`,`products`.`name`,`qty_types`.`name`,`products`.`code`,`category`.`name`,`sub_categories`.`name`,`brand`.`name`,`stock`.`qty`,`stock`.`cost`,`stock`.`selling_price`,`stock`.`mfd`,`stock`.`exd` FROM `stock` INNER JOIN `grn` ON `stock`.`grn_id` = `grn`.`id` INNER JOIN `products` ON `stock`.`product_id` = `products`.`id` INNER JOIN `brand` ON `products`.`brand_id` = `brand`.`id` INNER JOIN `category` ON `products`.`category_id` = `category`.`id` INNER JOIN `sub_categories` ON `products`.`sub_category_id` = `sub_categories`.`id` INNER JOIN `qty_types` ON `products`.`qty_types_id` = `qty_types`.`id` ORDER BY `products`.`name` ASC;");
            DefaultTableModel dtm = (DefaultTableModel) jTable1.getModel();
            dtm.setRowCount(0);
            
            while (rs.next()) {

                //System.out.println(rs.getString("qty_types.name"));
                Vector v = new Vector();
                v.add(rs.getString("stock.id"));
                v.add(rs.getString("products.name") + "-" + rs.getString("products.code"));
                v.add(rs.getString("category.name") + "/" + rs.getString("sub_categories.name"));
                v.add(rs.getString("brand.name"));
                
                v.add(rs.getString("stock.qty") + "/ " + rs.getString("qty_types.name"));
                v.add(rs.getString("stock.cost"));
                v.add(rs.getString("stock.selling_price"));
                v.add(rs.getString("stock.mfd"));
                v.add(rs.getString("stock.exd"));
                
                dtm.addRow(v);
                
            }
            
            jTable1.setModel(dtm);
            //jTable1.removeColumn(jTable1.getColumnModel().getColumn(5));

        } catch (Exception e) {
            
            e.printStackTrace();
        }
        
    }
    
    public void searchStocks() {
        
        try {
            String category = jComboBox1.getSelectedItem().toString();

            //String sub_category = jComboBox4.getSelectedItem().toString();
            String sub_category = "Select";
            if (jComboBox4.isEnabled()) {
                sub_category = jComboBox4.getSelectedItem().toString();
            }
            String brand = jComboBox3.getSelectedItem().toString();
            String name = jTextField2.getText();
            String sp_min = jTextField3.getText();
            String sp_max = jTextField4.getText();
            
            String mfd_fr = null;
            String mfd_to = null;
            String exd_fr = null;
            String exd_to = null;
            
            if (jDateChooser1.getDate() != null) {
                
                mfd_fr = sdf.format(jDateChooser1.getDate());
                
            }
            if (jDateChooser2.getDate() != null) {
                
                mfd_to = sdf.format(jDateChooser2.getDate());
                
            }
            if (jDateChooser3.getDate() != null) {
                
                exd_fr = sdf.format(jDateChooser3.getDate());
                
            }
            if (jDateChooser4.getDate() != null) {
                
                exd_to = sdf.format(jDateChooser4.getDate());
            }
            
            int sort = jComboBox2.getSelectedIndex();
            
            Vector queryVector = new Vector();

            //category
            if (category.equals("Select")) {
                
            } else {
                queryVector.add("`category`.`name`='" + category + "'");
                
            }

            //sub_category
            if (sub_category.equals("Select")) {
                
            } else {
                queryVector.add("`sub_categories`.`name`='" + sub_category + "'");
                
            }

            //brand
            if (brand.equals("Select")) {
                
            } else {
                
                queryVector.add("`brand`.`name`='" + brand + "'");
            }

            //name
            if (name.isEmpty()) {
                
            } else {
                
                queryVector.add("`products`.`name` LIKE '%" + name + "%'");
            }

            //selling Price max
            if (!sp_min.isEmpty()) {
//min empty

                if (sp_max.isEmpty()) {
//min not empty //max empty
                    queryVector.add("`stock`.`selling_price` >= '" + sp_min + "'");
                } else {
//min not empty //max not empty
                    queryVector.add("`stock`.`selling_price` >= '" + sp_min + "' AND `stock`.`selling_price` <= '" + sp_max + "'");
                }
            }

            //selling Price min
            if (!sp_max.isEmpty()) {
//max empty

                if (sp_min.isEmpty()) {
//min empty //max not empty
                    queryVector.add("`stock`.`selling_price` >= '" + sp_max + "'");
                    
                }
            }

            //mfd from
            if (mfd_fr != null) {
                
                if (mfd_to == null) {
                    
                    queryVector.add("`stock`.`mfd` >= '" + mfd_fr + "'");
                } else {
                    
                    queryVector.add("`stock`.`mfd` >= '" + mfd_fr + "' AND `stock`.`mfd` <= '" + mfd_to + "'");
                }
            }

            //mfd to
            if (mfd_to != null) {
                
                if (mfd_fr == null) {
                    
                    queryVector.add("`stock`.`mfd` <= '" + mfd_to + "'");
                    
                }
            }

            //exd from
            if (exd_fr != null) {
                
                if (exd_to == null) {
                    
                    queryVector.add("`stock`.`exd` >= '" + exd_fr + "'");
                } else {
                    
                    queryVector.add("`stock`.`exd` >= '" + exd_fr + "' AND `stock`.`exd` <= '" + exd_to + "'");
                }
            }

            //mfd to
            if (exd_to != null) {
                
                if (exd_fr == null) {
                    
                    queryVector.add("`stock`.`exd` <= '" + exd_to + "'");
                    
                }
            }
            String whereQuery = "";
            if (queryVector.size() > 0) {
                whereQuery = "WHERE";
            }
            
            for (int i = 0; i < queryVector.size(); i++) {
                whereQuery += " ";
                whereQuery += queryVector.get(i);
                whereQuery += " ";
                if (i != queryVector.size() - 1) {
                    whereQuery += "AND";
                }
                
            }

            //order by query part
            String sortquery;
            
            if (sort == 0) {
                sortquery = "`products`.`name` ASC";
                
            } else if (sort == 1) {
                sortquery = "`products`.`name` DESC";
                
            } else if (sort == 2) {
                sortquery = "`stock`.`selling_price` ASC";
                
            } else if (sort == 3) {
                sortquery = "`stock`.`selling_price` DESC";
                
            } else if (sort == 4) {
                sortquery = "`stock`.`qty` ASC";
                
            } else if (sort == 5) {
                sortquery = "`stock`.`qty` DESC";
                
            } else if (sort == 6) {
                sortquery = "`stock`.`exd` ASC";
                
            } else {
                
                sortquery = "`stock`.`exd` DESC";
            }
            //System.out.println("SELECT DISTINCT `stock`.`id`,`products`.`name`,`products`.`code`,`category`.`name`,`sub_categories`.`name`,`brand`.`name`,`stock`.`qty`,`stock`.`cost`,`stock`.`selling_price`,`stock`.`mfd`,`stock`.`exd` FROM `stock` INNER JOIN `products` ON `stock`.`product_id` = `products`.`id` INNER JOIN `brand` ON `products`.`brand_id` = `brand`.`id` INNER JOIN `category` ON `products`.`category_id` = `category`.`id` INNER JOIN `sub_categories` ON `products`.`sub_category_id` = `sub_categories`.`id` " + whereQuery + " ORDER BY " + sortquery + "");

            ResultSet rs = MySQL.search("SELECT DISTINCT `stock`.`id`,`products`.`name`,`products`.`code`,`category`.`name`,`sub_categories`.`name`,`brand`.`name`,`stock`.`qty`,`stock`.`cost`,`stock`.`selling_price`,`stock`.`mfd`,`stock`.`exd`,`qty_types`.`name` FROM `stock` INNER JOIN `products` ON `stock`.`product_id` = `products`.`id` INNER JOIN `brand` ON `products`.`brand_id` = `brand`.`id` INNER JOIN `category` ON `products`.`category_id` = `category`.`id` INNER JOIN `sub_categories` ON `products`.`sub_category_id` = `sub_categories`.`id` INNER JOIN `qty_types` ON `products`.`qty_types_id` = `qty_types`.`id` " + whereQuery + " ORDER BY " + sortquery + "");
            DefaultTableModel dtm = (DefaultTableModel) jTable1.getModel();
            dtm.setRowCount(0);
            
            while (rs.next()) {
                
                Vector v = new Vector();
                v.add(rs.getString("stock.id"));
                v.add(rs.getString("products.name") + "-" + rs.getString("products.code"));
                v.add(rs.getString("category.name") + "/" + rs.getString("sub_categories.name"));
                v.add(rs.getString("brand.name"));
                String sq = rs.getString("stock.qty");

//                if (rs.getString("qty_types.name").equals("Pcs")) {
//
//                    sq = String.valueOf(sq).split("\\.")[0];
//                }
                v.add(rs.getString("stock.qty") + "/ " + rs.getString("qty_types.name"));
                v.add(rs.getString("stock.cost"));
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

        jPanel2 = new javax.swing.JPanel();
        jLabel7 = new javax.swing.JLabel();
        jComboBox1 = new javax.swing.JComboBox<>();
        jLabel8 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        jTextField2 = new javax.swing.JTextField();
        jComboBox3 = new javax.swing.JComboBox<>();
        jLabel11 = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        jTextField3 = new javax.swing.JTextField();
        jLabel13 = new javax.swing.JLabel();
        jTextField4 = new javax.swing.JTextField();
        jLabel14 = new javax.swing.JLabel();
        jDateChooser1 = new com.toedter.calendar.JDateChooser();
        jDateChooser2 = new com.toedter.calendar.JDateChooser();
        jLabel15 = new javax.swing.JLabel();
        jDateChooser3 = new com.toedter.calendar.JDateChooser();
        jDateChooser4 = new com.toedter.calendar.JDateChooser();
        jLabel16 = new javax.swing.JLabel();
        jLabel17 = new javax.swing.JLabel();
        jLabel18 = new javax.swing.JLabel();
        jComboBox2 = new javax.swing.JComboBox<>();
        jButton1 = new javax.swing.JButton();
        jLabel19 = new javax.swing.JLabel();
        jComboBox4 = new javax.swing.JComboBox<>();
        jSeparator1 = new javax.swing.JSeparator();
        jSeparator2 = new javax.swing.JSeparator();
        jSeparator3 = new javax.swing.JSeparator();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        jPanel3 = new javax.swing.JPanel();
        jLabel5 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jTextField1 = new javax.swing.JTextField();
        jButton2 = new javax.swing.JButton();

        jLabel7.setFont(new java.awt.Font("Arial", 1, 17)); // NOI18N
        jLabel7.setText("Category  :");

        jComboBox1.setFont(new java.awt.Font("Arial", 0, 17)); // NOI18N
        jComboBox1.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                jComboBox1ItemStateChanged(evt);
            }
        });

        jLabel8.setFont(new java.awt.Font("Arial", 1, 17)); // NOI18N
        jLabel8.setText("Brand   :");

        jLabel9.setFont(new java.awt.Font("Arial", 1, 17)); // NOI18N
        jLabel9.setText("Name :");

        jTextField2.setFont(new java.awt.Font("Arial", 0, 17)); // NOI18N
        jTextField2.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jTextField2KeyReleased(evt);
            }
        });

        jComboBox3.setFont(new java.awt.Font("Arial", 0, 17)); // NOI18N
        jComboBox3.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                jComboBox3ItemStateChanged(evt);
            }
        });

        jLabel11.setFont(new java.awt.Font("Arial", 1, 17)); // NOI18N
        jLabel11.setText("Selling Price :");

        jLabel12.setFont(new java.awt.Font("Arial", 1, 17)); // NOI18N
        jLabel12.setText("Min   :");

        jTextField3.setFont(new java.awt.Font("Arial", 0, 17)); // NOI18N
        jTextField3.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jTextField3KeyReleased(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                jTextField3KeyTyped(evt);
            }
        });

        jLabel13.setFont(new java.awt.Font("Arial", 1, 17)); // NOI18N
        jLabel13.setText("Max  :");

        jTextField4.setFont(new java.awt.Font("Arial", 0, 17)); // NOI18N
        jTextField4.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jTextField4KeyReleased(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                jTextField4KeyTyped(evt);
            }
        });

        jLabel14.setFont(new java.awt.Font("Arial", 1, 17)); // NOI18N
        jLabel14.setText("MFD :");

        jDateChooser1.setDateFormatString("yyyy-MM-dd");
        jDateChooser1.addPropertyChangeListener(new java.beans.PropertyChangeListener() {
            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                jDateChooser1PropertyChange(evt);
            }
        });

        jDateChooser2.setDateFormatString("yyyy-MM-dd");
        jDateChooser2.addPropertyChangeListener(new java.beans.PropertyChangeListener() {
            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                jDateChooser2PropertyChange(evt);
            }
        });

        jLabel15.setFont(new java.awt.Font("Arial", 1, 17)); // NOI18N
        jLabel15.setText("EXD :");

        jDateChooser3.setDateFormatString("yyyy-MM-dd");
        jDateChooser3.addPropertyChangeListener(new java.beans.PropertyChangeListener() {
            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                jDateChooser3PropertyChange(evt);
            }
        });

        jDateChooser4.setDateFormatString("yyyy-MM-dd");
        jDateChooser4.addPropertyChangeListener(new java.beans.PropertyChangeListener() {
            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                jDateChooser4PropertyChange(evt);
            }
        });

        jLabel16.setFont(new java.awt.Font("Arial", 1, 17)); // NOI18N
        jLabel16.setText("to");

        jLabel17.setFont(new java.awt.Font("Arial", 1, 17)); // NOI18N
        jLabel17.setText("to");

        jLabel18.setFont(new java.awt.Font("Arial", 1, 17)); // NOI18N
        jLabel18.setText("Sort Product By :");

        jComboBox2.setFont(new java.awt.Font("Arial", 0, 17)); // NOI18N
        jComboBox2.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Name ASC", "Name DESC", "Price ASC", "Price DESC", "Quantity ASC", "Quantity DESC", "EXD ASC", "EXD DESC" }));
        jComboBox2.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                jComboBox2ItemStateChanged(evt);
            }
        });

        jButton1.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        jButton1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resources/undo.png"))); // NOI18N
        jButton1.setText("   Reset");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jLabel19.setFont(new java.awt.Font("Arial", 1, 17)); // NOI18N
        jLabel19.setText("Sub-Category :");

        jComboBox4.setFont(new java.awt.Font("Arial", 0, 17)); // NOI18N
        jComboBox4.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                jComboBox4ItemStateChanged(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jSeparator1)
                        .addContainerGap())
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addComponent(jLabel7, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jComboBox1, javax.swing.GroupLayout.PREFERRED_SIZE, 220, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(49, 49, 49)
                                .addComponent(jLabel19)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(jPanel2Layout.createSequentialGroup()
                                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                            .addComponent(jLabel11, javax.swing.GroupLayout.PREFERRED_SIZE, 124, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(jLabel18))
                                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addGroup(jPanel2Layout.createSequentialGroup()
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(jLabel12)
                                                .addGap(18, 18, 18)
                                                .addComponent(jTextField3, javax.swing.GroupLayout.PREFERRED_SIZE, 146, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addGap(68, 68, 68)
                                                .addComponent(jLabel13)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                                .addComponent(jTextField4, javax.swing.GroupLayout.PREFERRED_SIZE, 165, javax.swing.GroupLayout.PREFERRED_SIZE))
                                            .addGroup(jPanel2Layout.createSequentialGroup()
                                                .addGap(27, 27, 27)
                                                .addComponent(jComboBox2, javax.swing.GroupLayout.PREFERRED_SIZE, 289, javax.swing.GroupLayout.PREFERRED_SIZE))))
                                    .addGroup(jPanel2Layout.createSequentialGroup()
                                        .addComponent(jComboBox4, javax.swing.GroupLayout.PREFERRED_SIZE, 243, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(54, 54, 54)
                                        .addComponent(jLabel8)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(jComboBox3, javax.swing.GroupLayout.PREFERRED_SIZE, 226, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(37, 37, 37)
                                        .addComponent(jLabel9))))
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addComponent(jLabel14)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jDateChooser1, javax.swing.GroupLayout.PREFERRED_SIZE, 164, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(7, 7, 7)
                                .addComponent(jLabel16)
                                .addGap(18, 18, 18)
                                .addComponent(jDateChooser2, javax.swing.GroupLayout.PREFERRED_SIZE, 174, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addComponent(jLabel15)
                                .addGap(14, 14, 14)
                                .addComponent(jDateChooser3, javax.swing.GroupLayout.PREFERRED_SIZE, 164, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jLabel17)
                                .addGap(18, 18, 18)
                                .addComponent(jDateChooser4, javax.swing.GroupLayout.PREFERRED_SIZE, 177, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(jSeparator2, javax.swing.GroupLayout.PREFERRED_SIZE, 1173, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                                .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 137, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(35, 35, 35))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                                .addComponent(jTextField2, javax.swing.GroupLayout.PREFERRED_SIZE, 238, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addContainerGap())))))
            .addComponent(jSeparator3)
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addGap(51, 51, 51)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel9)
                        .addComponent(jTextField2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel7)
                        .addComponent(jComboBox1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel19)
                        .addComponent(jComboBox4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(3, 3, 3)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel8)
                            .addComponent(jComboBox3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addGap(18, 34, Short.MAX_VALUE)
                .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 5, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(24, 24, 24)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jDateChooser2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel16)
                            .addComponent(jLabel14)
                            .addComponent(jDateChooser1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(jLabel11)
                                .addComponent(jLabel12)
                                .addComponent(jTextField3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(jLabel13)
                                .addComponent(jTextField4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                                .addComponent(jSeparator2, javax.swing.GroupLayout.PREFERRED_SIZE, 5, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(20, 20, 20)
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel15, javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(jDateChooser3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(jLabel17))
                                    .addComponent(jDateChooser4, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(jLabel18)
                                        .addComponent(jComboBox2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                .addGap(16, 16, 16))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                                .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 84, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(11, 11, 11)))))
                .addComponent(jSeparator3, javax.swing.GroupLayout.PREFERRED_SIZE, 5, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(8, 8, 8))
        );

        jTable1.setFont(new java.awt.Font("Arial", 0, 15)); // NOI18N
        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Stock Id", "Product ", "Category", "Brand", "Quantity", "Buying Price", "Selling Price", "MFD", "EXD"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                true, false, false, false, false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jTable1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTable1MouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(jTable1);

        jLabel5.setFont(new java.awt.Font("Arial", 1, 17)); // NOI18N
        jLabel5.setText("Buying Price :");

        jLabel10.setFont(new java.awt.Font("Arial", 0, 17)); // NOI18N
        jLabel10.setText("0.00");

        jLabel6.setFont(new java.awt.Font("Arial", 1, 17)); // NOI18N
        jLabel6.setText("New Price    :");

        jTextField1.setFont(new java.awt.Font("Arial", 0, 17)); // NOI18N
        jTextField1.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                jTextField1KeyTyped(evt);
            }
        });

        jButton2.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        jButton2.setText("Update Stock");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(21, 21, 21)
                .addComponent(jLabel5)
                .addGap(26, 26, 26)
                .addComponent(jLabel10, javax.swing.GroupLayout.PREFERRED_SIZE, 195, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(73, 73, 73)
                .addComponent(jLabel6)
                .addGap(20, 20, 20)
                .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, 244, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(99, 99, 99)
                .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 313, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(249, Short.MAX_VALUE))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel6)
                    .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel10)
                    .addComponent(jLabel5))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel2, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1)
                .addContainerGap())
            .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 356, Short.MAX_VALUE)
                .addContainerGap())
        );
    }// </editor-fold>//GEN-END:initComponents

    private void jComboBox1ItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_jComboBox1ItemStateChanged
        // TODO add your handling code here:
        // 
        String selectedCategory = jComboBox1.getSelectedItem().toString();
        //System.out.println(selectedCategory);

        if (selectedCategory == ("Select")) {
            
            jComboBox4.setEnabled(false);
            searchStocks();
        } else {
            loadSubCategories();
            jComboBox4.setEnabled(true);
            searchStocks();
            
        }

    }//GEN-LAST:event_jComboBox1ItemStateChanged

    private void jTextField2KeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextField2KeyReleased
        // TODO add your handling code here:
        searchStocks();
    }//GEN-LAST:event_jTextField2KeyReleased

    private void jComboBox3ItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_jComboBox3ItemStateChanged
        // TODO add your handling code here:
        searchStocks();
    }//GEN-LAST:event_jComboBox3ItemStateChanged

    private void jTextField3KeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextField3KeyReleased
        // TODO add your handling code here:
        searchStocks();
    }//GEN-LAST:event_jTextField3KeyReleased

    private void jTextField3KeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextField3KeyTyped
        // TODO add your handling code here:
        String qty = jTextField3.getText();
        String text = qty + evt.getKeyChar();
        
        if (!Pattern.compile("(0|0[.]|0[.][0-9]*)|[1-9]|[1-9][0-9]*|[1-9]|[1-9][0-9]*[.]|[1-9][0-9]*[.][0-9]*").matcher(text).matches()) {
            evt.consume();
        }
    }//GEN-LAST:event_jTextField3KeyTyped

    private void jTextField4KeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextField4KeyReleased
        // TODO add your handling code here:
        searchStocks();
    }//GEN-LAST:event_jTextField4KeyReleased

    private void jTextField4KeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextField4KeyTyped
        // TODO add your handling code here:
        String qty = jTextField4.getText();
        String text = qty + evt.getKeyChar();
        
        if (!Pattern.compile("(0|0[.]|0[.][0-9]*)|[1-9]|[1-9][0-9]*|[1-9]|[1-9][0-9]*[.]|[1-9][0-9]*[.][0-9]*").matcher(text).matches()) {
            evt.consume();
        }
    }//GEN-LAST:event_jTextField4KeyTyped

    private void jDateChooser1PropertyChange(java.beans.PropertyChangeEvent evt) {//GEN-FIRST:event_jDateChooser1PropertyChange
        // TODO add your handling code here:
        searchStocks();
    }//GEN-LAST:event_jDateChooser1PropertyChange

    private void jDateChooser2PropertyChange(java.beans.PropertyChangeEvent evt) {//GEN-FIRST:event_jDateChooser2PropertyChange
        // TODO add your handling code here:
        searchStocks();
    }//GEN-LAST:event_jDateChooser2PropertyChange

    private void jDateChooser3PropertyChange(java.beans.PropertyChangeEvent evt) {//GEN-FIRST:event_jDateChooser3PropertyChange
        // TODO add your handling code here:
        searchStocks();
    }//GEN-LAST:event_jDateChooser3PropertyChange

    private void jDateChooser4PropertyChange(java.beans.PropertyChangeEvent evt) {//GEN-FIRST:event_jDateChooser4PropertyChange
        // TODO add your handling code here:
        searchStocks();
    }//GEN-LAST:event_jDateChooser4PropertyChange

    private void jComboBox2ItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_jComboBox2ItemStateChanged
        // TODO add your handling code here:
        searchStocks();
    }//GEN-LAST:event_jComboBox2ItemStateChanged

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        // TODO add your handling code here:
        reset();
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jTable1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTable1MouseClicked
        // TODO add your handling code here:

        int selectedRow = jTable1.getSelectedRow();
        
        if (selectedRow != -1) {
            
            String buying_price = jTable1.getValueAt(selectedRow, 5).toString();
            jLabel10.setText(buying_price);
        }
    }//GEN-LAST:event_jTable1MouseClicked

    private void jComboBox4ItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_jComboBox4ItemStateChanged
        // TODO add your handling code here:
        searchStocks();
    }//GEN-LAST:event_jComboBox4ItemStateChanged

    private void jTextField1KeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextField1KeyTyped
        // TODO add your handling code here:
        String qty = jTextField1.getText();
        String text = qty + evt.getKeyChar();
        
        if (!Pattern.compile("(0|0[.]|0[.][0-9]*)|[1-9]|[1-9][0-9]*|[1-9]|[1-9][0-9]*[.]|[1-9][0-9]*[.][0-9]*").matcher(text).matches()) {
            evt.consume();
        }
    }//GEN-LAST:event_jTextField1KeyTyped

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        // TODO add your handling code here:
        String buyingPrice = jLabel10.getText();
        String newPrice = jTextField1.getText();
        int selectedRow = jTable1.getSelectedRow();
        
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please Select a Stock.", "Warning", JOptionPane.WARNING_MESSAGE);
            
        } else if (!Pattern.compile("(0)|([1-9][0-9]*)|([0][.][0-9]+)|([1-9][0-9]*[.][0-9]+)").matcher(newPrice).matches()) {
            JOptionPane.showMessageDialog(this, "Invalid Selling Price.", "Warning", JOptionPane.WARNING_MESSAGE);
            
        } else {
            
            String stock_id = jTable1.getValueAt(selectedRow, 0).toString();
            
            if (Double.parseDouble(newPrice) <= Double.parseDouble(buyingPrice)) {
                
                int x = JOptionPane.showConfirmDialog(this, "New Selling Price is Less than Buying Price. Do you want to Continue?", "Warning", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
                
                if (x == JOptionPane.YES_OPTION) {
                    MySQL.iud("UPDATE `stock` SET `selling_price`='" + newPrice + "' WHERE `id`='" + stock_id + "'");
                    
                }
                
            } else {
                MySQL.iud("UPDATE `stock` SET `selling_price`='" + newPrice + "' WHERE `id`='" + stock_id + "'");
                
            }
            JOptionPane.showMessageDialog(this, "Selling Price Updated.", "Success", JOptionPane.INFORMATION_MESSAGE);
            loadStocks();
            reset();
        }
    }//GEN-LAST:event_jButton2ActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JComboBox<String> jComboBox1;
    private javax.swing.JComboBox<String> jComboBox2;
    private javax.swing.JComboBox<String> jComboBox3;
    private javax.swing.JComboBox<String> jComboBox4;
    private com.toedter.calendar.JDateChooser jDateChooser1;
    private com.toedter.calendar.JDateChooser jDateChooser2;
    private com.toedter.calendar.JDateChooser jDateChooser3;
    private com.toedter.calendar.JDateChooser jDateChooser4;
    public javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel19;
    public javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel2;
    public javax.swing.JPanel jPanel3;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JSeparator jSeparator2;
    private javax.swing.JSeparator jSeparator3;
    private javax.swing.JTable jTable1;
    private javax.swing.JTextField jTextField1;
    private javax.swing.JTextField jTextField2;
    private javax.swing.JTextField jTextField3;
    private javax.swing.JTextField jTextField4;
    // End of variables declaration//GEN-END:variables
}
