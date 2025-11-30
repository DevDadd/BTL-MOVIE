package com.mycompany.movieapp.views;

import com.mycompany.movieapp.Movieapp;
import com.mycompany.movieapp.services.MovieService;
import com.mycompany.movieapp.services.UserService;
import com.mycompany.movieapp.models.Movie;
import com.mycompany.movieapp.models.User;
import com.mycompany.movieapp.models.Staff;
import java.util.List;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.BoxLayout;
import javax.swing.JOptionPane;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.border.CompoundBorder;

public class HomePage extends javax.swing.JFrame {
    
    private static final java.util.logging.Logger logger = java.util.logging.Logger.getLogger(HomePage.class.getName());
    private final User currentUser;

    public HomePage() {
        this(UserService.getCurrentUser());
    }

    public HomePage(User currentUser) {
        this.currentUser = currentUser;
        initComponents();
        configureAdminAccess();
        loadNowShowingMovies();
    }
    
    private void loadNowShowingMovies() {

        List<Movie> allMovies = Movieapp.getMovies();
        List<Movie> nowShowingMovies = MovieService.getNowShowingMovies(allMovies);
        
        jPanelMoviesContainer.removeAll();
        
        if (nowShowingMovies.isEmpty()) {
            javax.swing.JLabel noMoviesLabel = new javax.swing.JLabel("Không có phim nào đang chiếu.");
            noMoviesLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
            noMoviesLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
            jPanelMoviesContainer.add(noMoviesLabel);
        } else {
            for (Movie movie : nowShowingMovies) {
                javax.swing.JPanel moviePanel = createMovieContainer(movie);
                jPanelMoviesContainer.add(moviePanel);
                jPanelMoviesContainer.add(javax.swing.Box.createVerticalStrut(15));
            }
        }
        
        jPanelMoviesContainer.revalidate();
        jPanelMoviesContainer.repaint();
    }

    public void refreshMovies() {
        loadNowShowingMovies();
    }
    
    private javax.swing.JPanel createMovieContainer(Movie movie) {
        javax.swing.JPanel container = new javax.swing.JPanel();
        container.setBorder(new CompoundBorder(
            new LineBorder(new Color(200, 200, 200), 1, true),
            new EmptyBorder(10, 10, 10, 10)
        ));
        container.setBackground(Color.WHITE);
        container.setLayout(new BorderLayout(15, 10));
        container.setPreferredSize(new Dimension(0, 130));
        container.setMaximumSize(new Dimension(Integer.MAX_VALUE, 130));
        
       
        container.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                showMovieSchedules(movie);
            }
            
            @Override
            public void mouseEntered(MouseEvent e) {
                container.setBackground(new Color(250, 250, 250));
                container.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
            }
            
            @Override
            public void mouseExited(MouseEvent e) {
                container.setBackground(Color.WHITE);
                container.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
            }
        });
        
        javax.swing.JPanel infoPanel = new javax.swing.JPanel();
        infoPanel.setBackground(Color.WHITE);
        infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.Y_AXIS));
        infoPanel.setBorder(new EmptyBorder(10, 15, 10, 15));
        
        javax.swing.JLabel titleLabel = new javax.swing.JLabel(movie.getTitle());
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
        titleLabel.setForeground(new Color(33, 33, 33));
        infoPanel.add(titleLabel);
        
        infoPanel.add(javax.swing.Box.createVerticalStrut(8));
        
        javax.swing.JLabel genreLabel = new javax.swing.JLabel("Thể loại: " + movie.getGenre());
        genreLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        genreLabel.setForeground(new Color(100, 100, 100));
        infoPanel.add(genreLabel);
        
        javax.swing.JLabel ratingLabel = new javax.swing.JLabel("Rating: " + movie.getRating());
        ratingLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        ratingLabel.setForeground(new Color(100, 100, 100));
        infoPanel.add(ratingLabel);
        
        javax.swing.JLabel dateLabel = new javax.swing.JLabel("Khởi chiếu: " + movie.getReleaseDate());
        dateLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        dateLabel.setForeground(new Color(100, 100, 100));
        infoPanel.add(dateLabel);
        
        javax.swing.JPanel rightPanel = new javax.swing.JPanel();
        rightPanel.setBackground(Color.WHITE);
        rightPanel.setLayout(new BorderLayout());
        rightPanel.setBorder(new EmptyBorder(10, 15, 10, 15));
        rightPanel.setPreferredSize(new Dimension(200, 0));
        
        javax.swing.JLabel durationLabel = new javax.swing.JLabel("Thời lượng: " + movie.getDuration());
        durationLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        durationLabel.setForeground(new Color(100, 100, 100));
        rightPanel.add(durationLabel, BorderLayout.NORTH);
        
        String description = movie.getDescription();
        if (description.length() > 100) {
            description = description.substring(0, 97) + "...";
        }
        javax.swing.JTextArea descArea = new javax.swing.JTextArea(description);
        descArea.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        descArea.setForeground(new Color(80, 80, 80));
        descArea.setBackground(Color.WHITE);
        descArea.setEditable(false);
        descArea.setLineWrap(true);
        descArea.setWrapStyleWord(true);
        descArea.setBorder(null);
        rightPanel.add(descArea, BorderLayout.CENTER);
        
        container.add(infoPanel, BorderLayout.CENTER);
        container.add(rightPanel, BorderLayout.EAST);
        
        return container;
    }
    
    private void showMovieSchedules(Movie movie) {
        MovieScheduleView scheduleView = new MovieScheduleView(this, movie, this);
        scheduleView.setVisible(true);
    }
    
    private void historyButtonActionPerformed(java.awt.event.ActionEvent evt) {
        BookingHistoryPage historyPage = new BookingHistoryPage();
        historyPage.setVisible(true);
    }

    private void adminButtonActionPerformed(java.awt.event.ActionEvent evt) {
        if (!isAdminUser()) {
            JOptionPane.showMessageDialog(this,
                    "Bạn không có quyền truy cập chức năng này.",
                    "Access Denied",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }
        Configure configurePage = new Configure(this);
        configurePage.setVisible(true);
    }

    private boolean isAdminUser() {
        if (currentUser instanceof Staff) {
            String role = ((Staff) currentUser).getRole();
            return role != null && role.equalsIgnoreCase("Admin");
        }
        return false;
    }

    private void configureAdminAccess() {
        boolean admin = isAdminUser();
        if (adminButton != null) {
            adminButton.setEnabled(admin);
            if (!admin) {
                adminButton.setToolTipText("Chỉ dành cho admin");
            } else {
                adminButton.setToolTipText(null);
            }
        }
    }

    @SuppressWarnings("unchecked")
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jPanelMoviesContainer = new javax.swing.JPanel();
        historyButton = new javax.swing.JButton();
        adminButton = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setPreferredSize(new java.awt.Dimension(800, 600));

        jLabel1.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setText("PHIM ĐANG CHIẾU");

        adminButton.setText("Quản lý");
        adminButton.setFont(new java.awt.Font("Segoe UI", 0, 12)); // NOI18N
        adminButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                adminButtonActionPerformed(evt);
            }
        });

        historyButton.setText("Vé của tôi");
        historyButton.setFont(new java.awt.Font("Segoe UI", 0, 12)); // NOI18N
        historyButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                historyButtonActionPerformed(evt);
            }
        });

        jPanelMoviesContainer.setBackground(new java.awt.Color(245, 245, 245));
        jPanelMoviesContainer.setLayout(new javax.swing.BoxLayout(jPanelMoviesContainer, javax.swing.BoxLayout.Y_AXIS));
        jPanelMoviesContainer.setBorder(new javax.swing.border.EmptyBorder(15, 15, 15, 15));
        jScrollPane1.setViewportView(jPanelMoviesContainer);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(historyButton)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(adminButton))
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 788, Short.MAX_VALUE))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(historyButton)
                    .addComponent(adminButton))
                .addGap(18, 18, 18)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 542, Short.MAX_VALUE)
                .addContainerGap())
        );

        pack();
    }

    public static void main(String args[]) {
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ReflectiveOperationException | javax.swing.UnsupportedLookAndFeelException ex) {
            logger.log(java.util.logging.Level.SEVERE, null, ex);
        }
        java.awt.EventQueue.invokeLater(() -> new HomePage().setVisible(true));
    }

    private javax.swing.JButton historyButton;
    private javax.swing.JButton adminButton;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JPanel jPanelMoviesContainer;
    private javax.swing.JScrollPane jScrollPane1;
}
