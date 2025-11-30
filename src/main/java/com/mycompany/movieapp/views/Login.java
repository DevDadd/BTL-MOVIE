package com.mycompany.movieapp.views;

import javax.swing.*;
import com.mycompany.movieapp.services.UserService;
import com.mycompany.movieapp.models.User;

public class Login extends JFrame {

    public Login() {
        initComponents();
    }

    private void initComponents() {
        jLabel1 = new JLabel("Đăng nhập");
        jLabel1.setHorizontalAlignment(SwingConstants.CENTER);
        jLabel2 = new JLabel("Tên đăng nhập: ");
        jLabel3 = new JLabel("Mật khẩu:");
        jTextField1 = new JTextField();
        jPasswordField1 = new JPasswordField();
        jButtonLogin = new JButton("Đăng nhập");
        signupButton = new JButton("Đăng ký");

        jButtonLogin.addActionListener(evt -> loginAction());
        signupButton.addActionListener(evt -> signupAction());

        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        GroupLayout layout = new GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
                layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addGap(50)
                                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                                        .addComponent(jLabel1, GroupLayout.Alignment.CENTER)
                                        .addGroup(layout.createSequentialGroup()
                                                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                                                        .addComponent(jLabel2)
                                                        .addComponent(jLabel3))
                                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                                                        .addComponent(jTextField1, GroupLayout.PREFERRED_SIZE, 180, GroupLayout.PREFERRED_SIZE)
                                                        .addComponent(jPasswordField1, GroupLayout.PREFERRED_SIZE, 180, GroupLayout.PREFERRED_SIZE))))
                                .addContainerGap(50, Short.MAX_VALUE))
                        .addGroup(GroupLayout.Alignment.CENTER, layout.createSequentialGroup()
                                .addComponent(jButtonLogin)
                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(signupButton)
                        )
        );

        layout.setVerticalGroup(
                layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addGap(20)
                                .addComponent(jLabel1)
                                .addGap(20)
                                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                        .addComponent(jLabel2)
                                        .addComponent(jTextField1, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                                .addGap(10)
                                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                        .addComponent(jLabel3)
                                        .addComponent(jPasswordField1, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                                .addGap(20)
                                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                        .addComponent(jButtonLogin)
                                        .addComponent(signupButton))
                                .addContainerGap(20, Short.MAX_VALUE))
        );

        pack();
        setLocationRelativeTo(null);
    }

    private void loginAction() {
        String username = jTextField1.getText().trim();
        String password = new String(jPasswordField1.getPassword());

        if (username.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Hãy nhập đầy đủ tên và mật khẩu", "Lỗi đăng nhập", JOptionPane.ERROR_MESSAGE);
            return;
        }

        User user = UserService.authenticate(username, password);
        if (user != null) {
            JOptionPane.showMessageDialog(this, "Đăng nhập thành công! Chào mừng, " + user.getUsername(), "Thành công", JOptionPane.INFORMATION_MESSAGE);
            UserService.setCurrentUser(user);

            HomePage home = new HomePage(user);
            home.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            home.setVisible(true);
            this.dispose();
        } else {
            JOptionPane.showMessageDialog(this, "Tên đăng nhập hoặc mật khẩu không hợp lệ.", "Lỗi đăng nhập", JOptionPane.ERROR_MESSAGE);
            jPasswordField1.setText("");
        }
    }

    private void signupAction() {
        Signup signup = new Signup();
        signup.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        signup.setVisible(true);
    }

    public static void main(String[] args) {

        com.mycompany.movieapp.utils.DataLoader.loadDemoData();

        SwingUtilities.invokeLater(() -> new Login().setVisible(true));
    }

    private JButton jButtonLogin;
    private JButton signupButton;
    private JLabel jLabel1, jLabel2, jLabel3;
    private JTextField jTextField1;
    private JPasswordField jPasswordField1;
}
