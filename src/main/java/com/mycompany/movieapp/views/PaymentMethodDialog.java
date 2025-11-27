package com.mycompany.movieapp.views;

import com.mycompany.movieapp.enums.BookingStatus;
import com.mycompany.movieapp.models.Booking;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;

/**
 *
 * @author taova
 */
public class PaymentMethodDialog extends JDialog {
    
    private Booking booking;
    private String selectedPaymentMethod;
    private boolean paymentConfirmed;
    private double discountedPrice; // Lưu giá đã giảm (nếu có)
    private boolean discountApplied; // Đánh dấu đã áp dụng mã giảm giá
    
    private JLabel titleLabel;
    private JLabel amountLabel;
    private JRadioButton cashRadio;
    private JRadioButton momoRadio;
    private JRadioButton visaRadio;
    private JRadioButton mastercardRadio;
    private JTextField discountCodeTextField;
    private JButton confirmButton;
    private JButton cancelButton;
    private JButton applyDiscountButton;
    private ButtonGroup paymentMethodGroup;
    
    /**
     * Creates new form PaymentMethodDialog
     */
    public PaymentMethodDialog(java.awt.Frame parent, Booking booking) {
        super(parent, true);
        if (booking == null) {
            throw new IllegalArgumentException("Booking cannot be null");
        }
        this.booking = booking;
        this.paymentConfirmed = false;
        this.selectedPaymentMethod = null;
        this.discountedPrice = booking.getTotalPrice(); // Mặc định bằng giá gốc
        this.discountApplied = false;
        initComponents();
    }
    
    /**
     * Get the selected payment method
     * @return The selected payment method or null if cancelled
     */
    public String getSelectedPaymentMethod() {
        return selectedPaymentMethod;
    }
    
    /**
     * Check if payment was confirmed
     * @return true if confirmed, false otherwise
     */
    public boolean isPaymentConfirmed() {
        return paymentConfirmed;
    }
    
    private void initComponents() {
        setTitle("Chọn phương thức thanh toán");
        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        setPreferredSize(new Dimension(500, 450));
        setResizable(false);
        setLocationRelativeTo(getParent());
        
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBorder(new EmptyBorder(30, 30, 30, 30));
        mainPanel.setBackground(Color.WHITE);
        
        // Title
        titleLabel = new JLabel("CHỌN PHƯƠNG THỨC THANH TOÁN");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
        titleLabel.setAlignmentX(JLabel.CENTER_ALIGNMENT);
        titleLabel.setBorder(new EmptyBorder(0, 0, 20, 0));
        mainPanel.add(titleLabel);
        
        // Amount display
        JPanel amountPanel = new JPanel();
        amountPanel.setLayout(new BoxLayout(amountPanel, BoxLayout.Y_AXIS));
        amountPanel.setBackground(Color.WHITE);
        amountPanel.setBorder(new CompoundBorder(
            new LineBorder(new Color(200, 200, 200), 1, true),
            new EmptyBorder(15, 15, 15, 15)
        ));
        
        JLabel amountTitleLabel = new JLabel("Tổng tiền cần thanh toán:");
        amountTitleLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        amountTitleLabel.setAlignmentX(JLabel.CENTER_ALIGNMENT);
        amountTitleLabel.setHorizontalAlignment(JLabel.CENTER);
        amountPanel.add(amountTitleLabel);
        
        amountPanel.add(Box.createVerticalStrut(5));
        
        amountLabel = new JLabel(String.format("%,.0f VNĐ", booking.getTotalPrice()));
        amountLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
        amountLabel.setForeground(new Color(0, 150, 0));
        amountLabel.setAlignmentX(JLabel.CENTER_ALIGNMENT);
        amountLabel.setHorizontalAlignment(JLabel.CENTER);
        amountPanel.add(amountLabel);
        
        amountPanel.setAlignmentX(JPanel.CENTER_ALIGNMENT);
        mainPanel.add(amountPanel);
        mainPanel.add(Box.createVerticalStrut(20));
        
        // Payment methods
        JLabel methodLabel = new JLabel("Phương thức thanh toán:");
        methodLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        methodLabel.setAlignmentX(JLabel.CENTER_ALIGNMENT);
        methodLabel.setHorizontalAlignment(JLabel.CENTER);
        mainPanel.add(methodLabel);
        
        mainPanel.add(Box.createVerticalStrut(10));
        
        paymentMethodGroup = new ButtonGroup();
        
        // Cash option
        cashRadio = new JRadioButton("Tiền mặt (Thanh toán tại quầy)");
        cashRadio.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        cashRadio.setBackground(Color.WHITE);
        cashRadio.setSelected(true); // Default selection
        cashRadio.setAlignmentX(JRadioButton.CENTER_ALIGNMENT);
        paymentMethodGroup.add(cashRadio);
        mainPanel.add(cashRadio);
        
        // MoMo option
        momoRadio = new JRadioButton("Ví điện tử MoMo");
        momoRadio.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        momoRadio.setBackground(Color.WHITE);
        momoRadio.setAlignmentX(JRadioButton.CENTER_ALIGNMENT);
        paymentMethodGroup.add(momoRadio);
        mainPanel.add(momoRadio);
        
        // Visa option
        visaRadio = new JRadioButton("Thẻ Visa");
        visaRadio.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        visaRadio.setBackground(Color.WHITE);
        visaRadio.setAlignmentX(JRadioButton.CENTER_ALIGNMENT);
        paymentMethodGroup.add(visaRadio);
        mainPanel.add(visaRadio);
        
        // Mastercard option
        mastercardRadio = new JRadioButton("Thẻ Mastercard");
        mastercardRadio.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        mastercardRadio.setBackground(Color.WHITE);
        mastercardRadio.setAlignmentX(JRadioButton.CENTER_ALIGNMENT);
        paymentMethodGroup.add(mastercardRadio);
        mainPanel.add(mastercardRadio);

        mainPanel.add(Box.createVerticalStrut(15));

        discountCodeTextField = new JTextField("Nhập mã giảm giá");
        discountCodeTextField.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        discountCodeTextField.setBackground(Color.WHITE);
        discountCodeTextField.setAlignmentX(JTextField.CENTER_ALIGNMENT);
        mainPanel.add(discountCodeTextField);

        mainPanel.add(Box.createVerticalStrut(15));

        applyDiscountButton = new JButton("Áp dụng mã");
        applyDiscountButton.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        applyDiscountButton.setBackground(Color.WHITE);
        applyDiscountButton.setAlignmentX(JButton.CENTER_ALIGNMENT);
        applyDiscountButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String discountCode = discountCodeTextField.getText().trim();
                double originalPrice = booking.getTotalPrice();
                
                if (discountCode.equals("WELCOMENEWBIE")) {
                    discountedPrice = originalPrice * 0.9; // Giảm 10%
                    discountApplied = true;
                    amountLabel.setText(String.format("%,.0f VNĐ", discountedPrice));
                    JOptionPane.showMessageDialog(PaymentMethodDialog.this, 
                        "Áp dụng mã giảm giá thành công! Giảm 10%", 
                        "Thành công", 
                        JOptionPane.INFORMATION_MESSAGE);
                } else if (discountCode.equals("VALENTINESDAY")) {
                    discountedPrice = originalPrice * 0.8; // Giảm 20%
                    discountApplied = true;
                    amountLabel.setText(String.format("%,.0f VNĐ", discountedPrice));
                    JOptionPane.showMessageDialog(PaymentMethodDialog.this, 
                        "Áp dụng mã giảm giá thành công! Giảm 20%", 
                        "Thành công", 
                        JOptionPane.INFORMATION_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(PaymentMethodDialog.this, 
                        "Mã giảm giá không hợp lệ", 
                        "Lỗi", 
                        JOptionPane.ERROR_MESSAGE);
                    discountApplied = false;
                    discountedPrice = originalPrice; // Reset về giá gốc
                }
            }
        });
        mainPanel.add(applyDiscountButton);

        mainPanel.add(Box.createVerticalStrut(20));
        
        // Buttons panel
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.X_AXIS));
        buttonPanel.setBackground(Color.WHITE);
        buttonPanel.setBorder(new EmptyBorder(10, 0, 0, 0));
        
        cancelButton = new JButton("Hủy");
        cancelButton.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        cancelButton.setPreferredSize(new Dimension(100, 35));
        cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                paymentConfirmed = false;
                selectedPaymentMethod = null;
                dispose();
            }
        });
        
        confirmButton = new JButton("Xác nhận");
        confirmButton.setFont(new Font("Segoe UI", Font.BOLD, 12));
        confirmButton.setPreferredSize(new Dimension(100, 35));
        confirmButton.setBackground(new Color(0, 120, 215));
        confirmButton.setForeground(Color.WHITE);
        confirmButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (cashRadio.isSelected()) {
                    selectedPaymentMethod = "CASH";
                } else if (momoRadio.isSelected()) {
                    selectedPaymentMethod = "MOMO";
                } else if (visaRadio.isSelected()) {
                    selectedPaymentMethod = "VISA";
                } else if (mastercardRadio.isSelected()) {
                    selectedPaymentMethod = "MASTERCARD";
                }
                
                // Cập nhật giá đã giảm vào booking nếu đã áp dụng mã giảm giá
                if (discountApplied) {
                    booking.setTotalPrice(discountedPrice);
                }
                
                paymentConfirmed = true;
                booking.setStatus(BookingStatus.PAID);
                dispose();
            }
        });
        
        buttonPanel.add(Box.createHorizontalGlue());
        buttonPanel.add(cancelButton);
        buttonPanel.add(Box.createHorizontalStrut(10));
        buttonPanel.add(confirmButton);
        
        mainPanel.add(buttonPanel);
        
        getContentPane().add(mainPanel);
        pack();
    }
    
    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(() -> {
            // For testing only
            PaymentMethodDialog dialog = new PaymentMethodDialog(new javax.swing.JFrame(), null);
            dialog.setVisible(true);
        });
    }
}

