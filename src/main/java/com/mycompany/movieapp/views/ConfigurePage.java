package com.mycompany.movieapp.views;

/**
 * Simple placeholder configure page frame.
 */
public class ConfigurePage extends javax.swing.JFrame {
    
    /**
     * Creates new form ConfigurePage
     */
    public ConfigurePage() {
        initComponents();
    }
    
    @SuppressWarnings("unchecked")
    private void initComponents() {
        
        titleLabel = new javax.swing.JLabel();
        
        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Trang Quản Lý");
        setPreferredSize(new java.awt.Dimension(500, 300));
        
        titleLabel.setFont(new java.awt.Font("Segoe UI", 1, 16)); // NOI18N
        titleLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        titleLabel.setText("Configure Page");
        
        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(titleLabel, javax.swing.GroupLayout.DEFAULT_SIZE, 488, Short.MAX_VALUE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(titleLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(254, Short.MAX_VALUE))
        );
        
        pack();
        setLocationRelativeTo(null);
    }
    
    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(() -> new ConfigurePage().setVisible(true));
    }
    
    // Variables declaration
    private javax.swing.JLabel titleLabel;
    // End of variables declaration
}
