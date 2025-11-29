package com.mycompany.movieapp.views;

import com.mycompany.movieapp.enums.BookingStatus;
import com.mycompany.movieapp.models.Booking;
import com.mycompany.movieapp.models.Customer;
import com.mycompany.movieapp.services.BookingService;
import com.mycompany.movieapp.services.UserService;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.awt.BorderLayout;
import javax.swing.BoxLayout;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;

public class BookingHistoryPage extends javax.swing.JFrame {
    
    private Customer customer;

    public BookingHistoryPage() {
        this.customer = (Customer) UserService.getAllUsers().stream()
                .filter(u -> u instanceof Customer)
                .findFirst()
                .orElse(null);
        initComponents();
        loadBookingHistory();
    }
    
    private void loadBookingHistory() {
        if (customer == null) {
            bookingsPanel.add(new javax.swing.JLabel("Vui lòng đăng nhập để xem lịch sử đặt chỗ."));
            return;
        }
        
        List<Booking> bookings = BookingService.getBookingHistory(customer);
        
        bookingsPanel.removeAll();
        
        if (bookings.isEmpty()) {
            javax.swing.JLabel noBookingsLabel = new javax.swing.JLabel("Bạn chưa có đặt chỗ nào.");
            noBookingsLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
            noBookingsLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
            noBookingsLabel.setBorder(new EmptyBorder(20, 20, 20, 20));
            bookingsPanel.add(noBookingsLabel);
        } else {
            DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
            
            for (Booking booking : bookings) {
                javax.swing.JPanel bookingPanel = createBookingPanel(booking, dateFormatter);
                bookingsPanel.add(bookingPanel);
                bookingsPanel.add(javax.swing.Box.createVerticalStrut(15));
            }
        }
        
        bookingsPanel.revalidate();
        bookingsPanel.repaint();
    }
    
    private javax.swing.JPanel createBookingPanel(Booking booking, DateTimeFormatter dateFormatter) {
        javax.swing.JPanel panel = new javax.swing.JPanel();
        panel.setBorder(new CompoundBorder(
            new LineBorder(new Color(200, 200, 200), 1, true),
            new EmptyBorder(15, 15, 15, 15)
        ));
        panel.setBackground(Color.WHITE);
        panel.setLayout(new BorderLayout(15, 0));
        panel.setPreferredSize(new Dimension(0, 150));
        panel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 150));
        
        javax.swing.JPanel infoPanel = new javax.swing.JPanel();
        infoPanel.setBackground(Color.WHITE);
        infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.Y_AXIS));
        
        javax.swing.JLabel idLabel = new javax.swing.JLabel(
            "Mã booking: #" + booking.getBookingId() + " | Trạng thái: " + booking.getStatus().getDisplayName()
        );
        idLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        infoPanel.add(idLabel);
        
        infoPanel.add(javax.swing.Box.createVerticalStrut(8));
        
        javax.swing.JLabel movieLabel = new javax.swing.JLabel(
            "Phim: " + booking.getSchedule().getMovie().getTitle()
        );
        movieLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        infoPanel.add(movieLabel);
        
        javax.swing.JLabel scheduleLabel = new javax.swing.JLabel(
            "Suất chiếu: " + booking.getSchedule().getStartTime().format(dateFormatter) + 
            " | Phòng: " + booking.getSchedule().getRoom().getName()
        );
        scheduleLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        infoPanel.add(scheduleLabel);
        
        StringBuilder seatsText = new StringBuilder("Ghế: ");
        for (int i = 0; i < booking.getSeatList().size(); i++) {
            if (i > 0) seatsText.append(", ");
            seatsText.append(booking.getSeatList().get(i).getSeat().getSeatCode());
        }
        javax.swing.JLabel seatsLabel = new javax.swing.JLabel(seatsText.toString());
        seatsLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        infoPanel.add(seatsLabel);
        
        javax.swing.JLabel priceLabel = new javax.swing.JLabel(
            "Tổng tiền: " + String.format("%,.0f VNĐ", booking.getTotalPrice())
        );
        priceLabel.setFont(new Font("Segoe UI", Font.BOLD, 12));
        priceLabel.setForeground(new Color(0, 100, 0));
        infoPanel.add(priceLabel);
        
        javax.swing.JButton payButton = new javax.swing.JButton("Thanh toán");
        payButton.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        payButton.setPreferredSize(new Dimension(120, 40));
        if(booking.getStatus().equals(BookingStatus.PAID)) {
            payButton.setEnabled(false);
            payButton.setText("Đã thanh toán");
        }
        
        panel.add(infoPanel, BorderLayout.CENTER);
        panel.add(payButton, BorderLayout.EAST);
        payButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                openPaymentMethodDialog(booking);
            }
        });
        
        return panel;
    }
    
    private void openPaymentMethodDialog(Booking booking) {
        PaymentMethodDialog dialog = new PaymentMethodDialog(this, booking);
        dialog.setVisible(true);
        
        if (dialog.isPaymentConfirmed()) {
            String paymentMethod = dialog.getSelectedPaymentMethod();
            
            loadBookingHistory();
        }
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        titleLabel = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        bookingsPanel = new javax.swing.JPanel();
        closeButton = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Lịch sử đặt chỗ");
        setPreferredSize(new java.awt.Dimension(800, 600));

        titleLabel.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        titleLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        titleLabel.setText("LỊCH SỬ ĐẶT CHỖ");

        bookingsPanel.setBackground(new java.awt.Color(245, 245, 245));
        bookingsPanel.setLayout(new javax.swing.BoxLayout(bookingsPanel, javax.swing.BoxLayout.Y_AXIS));
        bookingsPanel.setBorder(new javax.swing.border.EmptyBorder(15, 15, 15, 15));
        jScrollPane1.setViewportView(bookingsPanel);

        closeButton.setText("Đóng");
        closeButton.setFont(new java.awt.Font("Segoe UI", 0, 12)); // NOI18N
        closeButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                closeButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(titleLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 788, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(closeButton)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(titleLabel)
                .addGap(18, 18, 18)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 500, Short.MAX_VALUE)
                .addGap(15, 15, 15)
                .addComponent(closeButton)
                .addContainerGap())
        );

        pack();
        setLocationRelativeTo(null);
    }

    private void closeButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_closeButtonActionPerformed
        this.dispose();
    }//GEN-LAST:event_closeButtonActionPerformed

    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(() -> {
            new BookingHistoryPage().setVisible(true);
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel bookingsPanel;
    private javax.swing.JButton closeButton;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JLabel titleLabel;
    // End of variables declaration//GEN-END:variables
}

