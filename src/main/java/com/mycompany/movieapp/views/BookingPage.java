package com.mycompany.movieapp.views;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;
import javax.swing.border.LineBorder;
import javax.swing.JOptionPane;

import com.mycompany.movieapp.models.Schedule;
import com.mycompany.movieapp.models.ShowSeat;
import com.mycompany.movieapp.models.Customer;
import com.mycompany.movieapp.models.Booking;
import com.mycompany.movieapp.services.BookingService;
import com.mycompany.movieapp.services.UserService;

/**
 *
 * @author taova
 */
public class BookingPage extends javax.swing.JFrame {
    
    private Schedule schedule;
    private HomePage homePage;
    private List<ShowSeat> selectedSeats; // Danh sách ghế đã chọn
    private Customer currentCustomer; // Customer hiện tại (cần lấy từ login)

    /**
     * Creates new form BookingPage
     */
    public BookingPage(Schedule schedule, HomePage homePage) {
        this.schedule = schedule;
        this.homePage = homePage;
        this.selectedSeats = new ArrayList<>();
        // TODO: Lấy customer từ session/login - tạm thời lấy customer đầu tiên
        this.currentCustomer = (Customer) UserService.getAllUsers().stream()
                .filter(u -> u instanceof Customer)
                .findFirst()
                .orElse(null);
        initComponents();
        showingAvailableSeats();
        updateSelectedSeatsDisplay();
    }

    /**
     * Display all seats with color coding: white = available, red = booked
     */
    private void showingAvailableSeats() {
        // Clear existing seats
        seatsPanel.removeAll();
        
        // Get all seats from schedule
        List<ShowSeat> allSeats = schedule.getShowSeatInfo();
        
        if (allSeats.isEmpty()) {
            javax.swing.JLabel noSeatsLabel = new javax.swing.JLabel("Không có ghế nào trong phòng này.");
            noSeatsLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
            noSeatsLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
            seatsPanel.add(noSeatsLabel);
        } else {
            // Use GridLayout - adjust columns based on number of seats
            int columns = Math.min(10, allSeats.size());
            int rows = (int) Math.ceil((double) allSeats.size() / columns);
            seatsPanel.setLayout(new GridLayout(rows, columns, 5, 5));
            
            for (ShowSeat showSeat : allSeats) {
                javax.swing.JLabel seatLabel = createSeatLabel(showSeat);
                seatsPanel.add(seatLabel);
            }
        }
        
        seatsPanel.revalidate();
        seatsPanel.repaint();
    }
    
    /**
     * Create a seat label with appropriate color
     */
    private javax.swing.JLabel createSeatLabel(ShowSeat showSeat) {
        final ShowSeat seat = showSeat; // Make effectively final for use in inner class
        javax.swing.JLabel seatLabel = new javax.swing.JLabel(showSeat.getSeat().getSeatCode());
        seatLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        seatLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        seatLabel.setVerticalAlignment(javax.swing.SwingConstants.CENTER);
        seatLabel.setOpaque(true);
        seatLabel.setBorder(new LineBorder(Color.GRAY, 1));
        seatLabel.setPreferredSize(new Dimension(50, 50));
        
        if (showSeat.isAvailable()) {
            seatLabel.setBackground(Color.WHITE);
            seatLabel.setForeground(Color.BLACK);
            
            // Add click listener only for available seats
            seatLabel.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    if (seat.isAvailable()) {
                        // Toggle seat selection
                        if (selectedSeats.contains(seat)) {
                            // Deselect - remove from list
                            selectedSeats.remove(seat);
                            seatLabel.setBackground(Color.WHITE);
                            seatLabel.setForeground(Color.BLACK);
                        } else {
                            // Select - add to list
                            if (selectedSeats.size() >= 10) {
                                JOptionPane.showMessageDialog(BookingPage.this, 
                                    "Chỉ được chọn tối đa 10 ghế!", 
                                    "Thông báo", 
                                    JOptionPane.WARNING_MESSAGE);
                                return;
                            }
                            selectedSeats.add(seat);
                            seatLabel.setBackground(new Color(0, 150, 255)); // Blue for selected
                            seatLabel.setForeground(Color.WHITE);
                        }
                        updateSelectedSeatsDisplay();
                    }
                }
                
                @Override
                public void mouseEntered(MouseEvent e) {
                    if (seat.isAvailable() && !selectedSeats.contains(seat)) {
                        seatLabel.setBackground(new Color(200, 200, 200));
                    }
                }
                
                @Override
                public void mouseExited(MouseEvent e) {
                    if (seat.isAvailable() && !selectedSeats.contains(seat)) {
                        seatLabel.setBackground(Color.WHITE);
                    }
                }
            });
            seatLabel.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        } else {
            seatLabel.setBackground(Color.RED);
            seatLabel.setForeground(Color.WHITE);
        }
        
        return seatLabel;
    }
    
    /**
     * Update display of selected seats
     */
    private void updateSelectedSeatsDisplay() {
        if (selectedSeatsLabel != null) {
            if (selectedSeats.isEmpty()) {
                selectedSeatsLabel.setText("Chưa chọn ghế nào");
            } else {
                StringBuilder seatsText = new StringBuilder("Ghế đã chọn: ");
                for (int i = 0; i < selectedSeats.size(); i++) {
                    if (i > 0) seatsText.append(", ");
                    seatsText.append(selectedSeats.get(i).getSeat().getSeatCode());
                }
                selectedSeatsLabel.setText(seatsText.toString());
            }
        }
        
        if (confirmButton != null) {
            confirmButton.setEnabled(!selectedSeats.isEmpty());
        }
    }
    
    /**
     * Confirm booking and add seats to Booking
     */
    private void confirmBooking() {
        if (currentCustomer == null) {
            JOptionPane.showMessageDialog(this, 
                "Vui lòng đăng nhập để đặt chỗ!", 
                "Lỗi", 
                JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        if (selectedSeats.isEmpty()) {
            JOptionPane.showMessageDialog(this, 
                "Vui lòng chọn ít nhất một ghế!", 
                "Thông báo", 
                JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        try {
            // Create booking using BookingService
            Booking booking = BookingService.createBooking(currentCustomer, schedule, selectedSeats);
            
            // Confirm and process payment (tạm thời dùng CASH)
            boolean success = BookingService.confirmAndPayBooking(booking, "CASH");
            
            if (success) {
                JOptionPane.showMessageDialog(this, 
                    "Đặt chỗ thành công!\nMã booking: " + booking.getBookingId() + 
                    "\nGhế: " + getSelectedSeatsString() +
                    "\nTổng tiền: " + String.format("%,.0f VNĐ", booking.getTotalPrice()), 
                    "Thành công", 
                    JOptionPane.INFORMATION_MESSAGE);
                this.dispose();
            } else {
                JOptionPane.showMessageDialog(this, 
                    "Đặt chỗ thất bại. Vui lòng thử lại!", 
                    "Lỗi", 
                    JOptionPane.ERROR_MESSAGE);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, 
                "Lỗi: " + e.getMessage(), 
                "Lỗi", 
                JOptionPane.ERROR_MESSAGE);
        }
    }
    
    /**
     * Get selected seats as string
     */
    private String getSelectedSeatsString() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < selectedSeats.size(); i++) {
            if (i > 0) sb.append(", ");
            sb.append(selectedSeats.get(i).getSeat().getSeatCode());
        }
        return sb.toString();
    }
    
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        titleLabel = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        seatsPanel = new javax.swing.JPanel();
        infoPanel = new javax.swing.JPanel();
        legendLabel = new javax.swing.JLabel();
        selectedSeatsLabel = new javax.swing.JLabel();
        confirmButton = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Đặt chỗ");
        setPreferredSize(new java.awt.Dimension(900, 700));

        titleLabel.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        titleLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        titleLabel.setText("Chọn ghế");

        seatsPanel.setBackground(new java.awt.Color(245, 245, 245));
        seatsPanel.setBorder(new javax.swing.border.EmptyBorder(20, 20, 20, 20));
        jScrollPane1.setViewportView(seatsPanel);

        infoPanel.setLayout(new java.awt.BorderLayout());

        legendLabel.setFont(new java.awt.Font("Segoe UI", 0, 12)); // NOI18N
        legendLabel.setText("<html><div style='text-align: center;'>"
                + "<span style='background-color: white; color: black; padding: 5px; border: 1px solid gray;'>Trống</span> "
                + "<span style='background-color: blue; color: white; padding: 5px; border: 1px solid gray; margin-left: 10px;'>Đã chọn</span> "
                + "<span style='background-color: red; color: white; padding: 5px; border: 1px solid gray; margin-left: 10px;'>Đã đặt</span>"
                + "</div></html>");
        legendLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        
        selectedSeatsLabel.setFont(new java.awt.Font("Segoe UI", 0, 12)); // NOI18N
        selectedSeatsLabel.setText("Chưa chọn ghế nào");
        selectedSeatsLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        
        confirmButton.setText("Xác nhận đặt chỗ");
        confirmButton.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        confirmButton.setEnabled(false);
        confirmButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                confirmBooking();
            }
        });
        
        infoPanel.setLayout(new java.awt.BorderLayout());
        javax.swing.JPanel topPanel = new javax.swing.JPanel();
        topPanel.setLayout(new java.awt.BorderLayout());
        topPanel.add(legendLabel, java.awt.BorderLayout.CENTER);
        topPanel.add(selectedSeatsLabel, java.awt.BorderLayout.SOUTH);
        infoPanel.add(topPanel, java.awt.BorderLayout.CENTER);
        infoPanel.add(confirmButton, java.awt.BorderLayout.SOUTH);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(titleLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 880, Short.MAX_VALUE)
                    .addComponent(infoPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(titleLabel)
                .addGap(15, 15, 15)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 550, Short.MAX_VALUE)
                .addGap(15, 15, 15)
                .addComponent(infoPanel, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(() -> {
            // This is just for testing - in real usage, use the constructor with parameters
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton confirmButton;
    private javax.swing.JPanel infoPanel;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JLabel legendLabel;
    private javax.swing.JPanel seatsPanel;
    private javax.swing.JLabel selectedSeatsLabel;
    private javax.swing.JLabel titleLabel;
    // End of variables declaration//GEN-END:variables
}
