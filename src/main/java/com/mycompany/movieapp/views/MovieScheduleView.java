package com.mycompany.movieapp.views;

import com.mycompany.movieapp.models.Movie;
import com.mycompany.movieapp.models.Schedule;
import com.mycompany.movieapp.services.ScheduleService;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.time.format.DateTimeFormatter;
import java.util.List;
import javax.swing.BoxLayout;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;

public class MovieScheduleView extends javax.swing.JDialog {
    
    private Movie movie;
    private HomePage homePage;


    public MovieScheduleView(javax.swing.JFrame parent, Movie movie, HomePage homePage) {
        super(parent, true);
        this.movie = movie;
        this.homePage = homePage;
        initComponents();
        loadSchedules();
    }


    private void loadSchedules() {
        List<Schedule> schedules = ScheduleService.viewSchedules(movie.getMovieId());
        
        titleLabel.setText("Lịch chiếu của: " + movie.getTitle());
        
        schedulesPanel.removeAll();
        
        if (schedules.isEmpty()) {
            javax.swing.JLabel noScheduleLabel = new javax.swing.JLabel("Không có lịch chiếu nào cho phim này.");
            noScheduleLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
            noScheduleLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
            noScheduleLabel.setBorder(new EmptyBorder(20, 20, 20, 20));
            schedulesPanel.add(noScheduleLabel);
        } else {
            DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");
            
            for (Schedule schedule : schedules) {
                javax.swing.JPanel schedulePanel = createSchedulePanel(schedule, dateFormatter, timeFormatter);
                schedulesPanel.add(schedulePanel);
                schedulesPanel.add(javax.swing.Box.createVerticalStrut(10));
            }
        }
        
        schedulesPanel.revalidate();
        schedulesPanel.repaint();
    }
    
    private javax.swing.JPanel createSchedulePanel(Schedule schedule, 
                                                   DateTimeFormatter dateFormatter,
                                                   DateTimeFormatter timeFormatter) {
        javax.swing.JPanel panel = new javax.swing.JPanel();
        panel.setBorder(new CompoundBorder(
            new LineBorder(new Color(200, 200, 200), 1, true),
            new EmptyBorder(12, 15, 12, 15)
        ));
        panel.setBackground(Color.WHITE);
        panel.setLayout(new BorderLayout(10, 5));
        panel.setPreferredSize(new Dimension(0, 80));
        panel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 80));
        
        javax.swing.JPanel timePanel = new javax.swing.JPanel();
        timePanel.setLayout(new BoxLayout(timePanel, BoxLayout.Y_AXIS));
        timePanel.setBackground(Color.WHITE);
        
        javax.swing.JLabel timeLabel = new javax.swing.JLabel(
            schedule.getStartTime().format(timeFormatter) + " - " + 
            schedule.getEndTime().format(timeFormatter)
        );
        timeLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        timeLabel.setForeground(new Color(33, 33, 33));
        timePanel.add(timeLabel);
        
        javax.swing.JLabel dateLabel = new javax.swing.JLabel(
            schedule.getStartTime().format(dateFormatter)
        );
        dateLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        dateLabel.setForeground(new Color(100, 100, 100));
        timePanel.add(dateLabel);
        
        javax.swing.JPanel roomPanel = new javax.swing.JPanel();
        roomPanel.setLayout(new BoxLayout(roomPanel, BoxLayout.Y_AXIS));
        roomPanel.setBackground(Color.WHITE);


        String roomName = schedule.getRoom() != null ? schedule.getRoom().getName() : "N/A";

        javax.swing.JLabel roomLabel = new javax.swing.JLabel("Phòng: " + roomName);
        roomLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        roomLabel.setForeground(new Color(100, 100, 100));
        roomPanel.add(roomLabel);
        
        int availableSeats = schedule.getAvailableSeatsCount();
        javax.swing.JLabel seatsLabel = new javax.swing.JLabel(
            availableSeats + " ghế trống"
        );
        seatsLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        if (availableSeats >= 60) {
            seatsLabel.setForeground(new Color(0, 150, 0));
        } else if (availableSeats > 0) {
            seatsLabel.setForeground(new Color(255, 140, 0));
        } else {
            seatsLabel.setForeground(new Color(200, 0, 0));
        }
        
        panel.add(timePanel, BorderLayout.WEST);
        panel.add(roomPanel, BorderLayout.CENTER);
        panel.add(seatsLabel, BorderLayout.EAST);
        
        panel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                openBookingPage(schedule);
            }
            
            @Override
            public void mouseEntered(MouseEvent e) {
                panel.setBackground(new Color(245, 245, 245));
                panel.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
            }
            
            @Override
            public void mouseExited(MouseEvent e) {
                panel.setBackground(Color.WHITE);
                panel.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
            }
        });
        
        return panel;
    }
    
    private void openBookingPage(Schedule schedule) {
        BookingPage bookingPage = new BookingPage(schedule, homePage);
        bookingPage.setVisible(true);
        this.dispose();
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        titleLabel = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        schedulesPanel = new javax.swing.JPanel();
        closeButton = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Lịch chiếu");
        setSize(new java.awt.Dimension(600, 500));
        setLocationRelativeTo(null);

        titleLabel.setFont(new java.awt.Font("Segoe UI", 1, 16)); // NOI18N
        titleLabel.setText("Lịch chiếu");

        schedulesPanel.setBackground(new java.awt.Color(255, 255, 255));
        schedulesPanel.setLayout(new javax.swing.BoxLayout(schedulesPanel, javax.swing.BoxLayout.Y_AXIS));
        schedulesPanel.setBorder(new javax.swing.border.EmptyBorder(15, 15, 15, 15));
        jScrollPane1.setViewportView(schedulesPanel);

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
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 580, Short.MAX_VALUE)
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
                .addGap(15, 15, 15)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 400, Short.MAX_VALUE)
                .addGap(15, 15, 15)
                .addComponent(closeButton)
                .addContainerGap())
        );

        pack();
    }

    private void closeButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_closeButtonActionPerformed
        this.dispose();
    }//GEN-LAST:event_closeButtonActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton closeButton;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JPanel schedulesPanel;
    private javax.swing.JLabel titleLabel;
    // End of variables declaration//GEN-END:variables
}

