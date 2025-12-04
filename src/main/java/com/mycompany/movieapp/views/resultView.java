package com.mycompany.movieapp.views;

import com.mycompany.movieapp.models.Movie;
import com.mycompany.movieapp.services.MovieService;
import java.util.List;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.BoxLayout;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.border.CompoundBorder;

/**
 *
 * @author taova
 */
public class resultView extends javax.swing.JDialog {
    private String searchInput;
    private List<Movie> allMovies;
    
    private static final java.util.logging.Logger logger = java.util.logging.Logger.getLogger(resultView.class.getName());

    /**
     * Creates new form resultView
     */
    public resultView(javax.swing.JFrame parent, String searchInput) {
        super(parent, true);
        this.searchInput = searchInput;
        this.allMovies = com.mycompany.movieapp.utils.DataLoader.getMovies();
        initComponents();
        loadSearchedMovie();
    }
    
    private void loadSearchedMovie() {
        String keyword = searchInput;
        if (keyword == null || keyword.trim().isEmpty() || keyword.equals("Search phim")) {
            keyword = "";
        }
        
        List<Movie> resultMovies = MovieService.searchMovies(keyword, allMovies);

        jPanelMoviesContainer.removeAll();

        if (resultMovies.isEmpty()) {
            javax.swing.JLabel noMoviesLabel = new javax.swing.JLabel("Không tìm thấy phim nào với từ khóa: \"" + keyword + "\"");
            noMoviesLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
            noMoviesLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
            jPanelMoviesContainer.add(noMoviesLabel);
        } else {
            jLabelResult.setText("Tìm thấy " + resultMovies.size() + " phim với từ khóa: \"" + keyword + "\"");
            for (Movie movie : resultMovies) {
                javax.swing.JPanel moviePanel = createMovieContainer(movie);
                jPanelMoviesContainer.add(moviePanel);
                jPanelMoviesContainer.add(javax.swing.Box.createVerticalStrut(15));
            }
        }

        jPanelMoviesContainer.revalidate();
        jPanelMoviesContainer.repaint();
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
        if (description != null && description.length() > 100) {
            description = description.substring(0, 97) + "...";
        }
        javax.swing.JTextArea descArea = new javax.swing.JTextArea(description != null ? description : "");
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
        // Truyền resultView làm parent thay vì HomePage để đảm bảo modal đúng
        javax.swing.JFrame parentFrame = (javax.swing.JFrame) getParent();
        // Ẩn HomePage nếu nó vẫn còn để không che các cửa sổ sau
        if (parentFrame != null && parentFrame.isDisplayable()) {
            parentFrame.setVisible(false);
        }
        MovieScheduleView scheduleView = new MovieScheduleView(parentFrame, movie, null);
        scheduleView.setVisible(true);
        // Đóng resultView sau khi mở lịch chiếu
        this.dispose();
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabelResult = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jPanelMoviesContainer = new javax.swing.JPanel();
        closeButton = new javax.swing.JButton("Đóng");
        closeButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                resultView.this.dispose();
                // Hiện lại HomePage nếu nó vẫn còn (chưa bị dispose)
                javax.swing.JFrame parent = (javax.swing.JFrame) getParent();
                if (parent != null && parent.isDisplayable()) {
                    parent.setVisible(true);
                    parent.toFront();
                }
            }
        });

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Kết quả tìm kiếm");
        setPreferredSize(new java.awt.Dimension(800, 600));

        jLabelResult.setFont(new java.awt.Font("Segoe UI", 1, 16)); // NOI18N
        jLabelResult.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabelResult.setText("KẾT QUẢ TÌM KIẾM");

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
                    .addComponent(jLabelResult, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(closeButton)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 788, Short.MAX_VALUE))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(15, 15, 15)
                .addComponent(jLabelResult)
                .addGap(18, 18, 18)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 542, Short.MAX_VALUE)
                .addGap(15, 15, 15)
                .addComponent(closeButton)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

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
        } catch (ReflectiveOperationException | javax.swing.UnsupportedLookAndFeelException ex) {
            logger.log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        com.mycompany.movieapp.utils.DataLoader.loadDemoData();
        java.awt.EventQueue.invokeLater(() -> {
            resultView dialog = new resultView(null, "");
            dialog.setVisible(true);
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel jLabelResult;
    private javax.swing.JPanel jPanelMoviesContainer;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JButton closeButton;
    // End of variables declaration//GEN-END:variables
}
