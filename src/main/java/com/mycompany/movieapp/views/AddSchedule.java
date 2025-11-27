/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package com.mycompany.movieapp.views;

import com.mycompany.movieapp.models.Movie;
import com.mycompany.movieapp.models.Room;
import com.mycompany.movieapp.models.Schedule;
import com.mycompany.movieapp.services.MovieService;
import com.mycompany.movieapp.services.ScheduleService;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Trang quản lý lịch chiếu:
 * - Nhập ID phim để xem danh sách lịch chiếu hiện có
 * - Thêm mới lịch chiếu cho phim đã tồn tại
 * - Chọn 1 lịch để sửa hoặc xóa
 */
public class AddSchedule extends javax.swing.JFrame {

    private static final Logger logger = Logger.getLogger(AddSchedule.class.getName());

    // Định dạng thời gian nhập vào
    private static final DateTimeFormatter DATE_TIME_FORMATTER =
            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    // UI components
    private JTextField movieIdField;
    private JButton loadSchedulesButton;
    private DefaultListModel<Schedule> scheduleListModel;
    private JList<Schedule> scheduleList;

    private JTextField scheduleIdField;
    private JComboBox<Room> roomComboBox;
    private JTextField startTimeField;
    private JTextField endTimeField;

    private JButton addButton;
    private JButton updateButton;
    private JButton deleteButton;
    private JButton closeButton;

    // Data
    private List<Schedule> currentSchedules = new ArrayList<>();
    private List<Room> allRooms = new ArrayList<>();

    /**
     * Creates new form AddSchedule
     */
    public AddSchedule() {
        initComponents();
        loadAllRooms();
    }

    /**
     * Khởi tạo UI cho trang quản lý lịch chiếu.
     */
    @SuppressWarnings("unchecked")
    private void initComponents() {
        setTitle("Quản lý lịch chiếu");
        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        // ====== TOP: nhập movieId và nút load ======
        JLabel movieIdLabel = new JLabel("Movie ID:");
        movieIdField = new JTextField(10);
        loadSchedulesButton = new JButton("Tải lịch chiếu");
        loadSchedulesButton.addActionListener(e -> onLoadSchedules());

        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        topPanel.add(movieIdLabel);
        topPanel.add(movieIdField);
        topPanel.add(loadSchedulesButton);

        // ====== CENTER LEFT: danh sách schedule ======
        scheduleListModel = new DefaultListModel<>();
        scheduleList = new JList<>(scheduleListModel);
        scheduleList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        scheduleList.addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                onScheduleSelected();
            }
        });
        JScrollPane listScrollPane = new JScrollPane(scheduleList);
        listScrollPane.setBorder(BorderFactory.createTitledBorder("Các lịch chiếu của phim"));

        // ====== CENTER RIGHT: form chi tiết schedule ======
        JLabel scheduleIdLabel = new JLabel("Schedule ID:");
        scheduleIdField = new JTextField(10);
        scheduleIdField.setEditable(false);

        JLabel roomLabel = new JLabel("Phòng chiếu:");
        roomComboBox = new JComboBox<>();

        JLabel startTimeLabel = new JLabel("Start (yyyy-MM-dd HH:mm):");
        startTimeField = new JTextField(16);

        JLabel endTimeLabel = new JLabel("End (yyyy-MM-dd HH:mm):");
        endTimeField = new JTextField(16);

        JPanel formPanel = new JPanel();
        formPanel.setBorder(BorderFactory.createTitledBorder("Chi tiết lịch chiếu"));
        formPanel.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(4, 4, 4, 4);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        gbc.gridx = 0; gbc.gridy = 0;
        formPanel.add(scheduleIdLabel, gbc);
        gbc.gridx = 1;
        formPanel.add(scheduleIdField, gbc);

        gbc.gridx = 0; gbc.gridy = 1;
        formPanel.add(roomLabel, gbc);
        gbc.gridx = 1;
        formPanel.add(roomComboBox, gbc);

        gbc.gridx = 0; gbc.gridy = 2;
        formPanel.add(startTimeLabel, gbc);
        gbc.gridx = 1;
        formPanel.add(startTimeField, gbc);

        gbc.gridx = 0; gbc.gridy = 3;
        formPanel.add(endTimeLabel, gbc);
        gbc.gridx = 1;
        formPanel.add(endTimeField, gbc);

        // ====== BOTTOM: các nút thao tác ======
        addButton = new JButton("Thêm lịch");
        addButton.addActionListener(e -> onAddSchedule());

        updateButton = new JButton("Cập nhật");
        updateButton.addActionListener(e -> onUpdateSchedule());

        deleteButton = new JButton("Xóa");
        deleteButton.addActionListener(e -> onDeleteSchedule());

        closeButton = new JButton("Đóng");
        closeButton.addActionListener(e -> dispose());

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.add(addButton);
        buttonPanel.add(updateButton);
        buttonPanel.add(deleteButton);
        buttonPanel.add(closeButton);

        // ====== MAIN LAYOUT ======
        JPanel centerPanel = new JPanel(new GridLayout(1, 2, 10, 0));
        centerPanel.add(listScrollPane);
        centerPanel.add(formPanel);

        getContentPane().setLayout(new BorderLayout(10, 10));
        getContentPane().add(topPanel, BorderLayout.NORTH);
        getContentPane().add(centerPanel, BorderLayout.CENTER);
        getContentPane().add(buttonPanel, BorderLayout.SOUTH);

        setSize(800, 500);
        setLocationRelativeTo(null);
    }

    /**
     * Load danh sách tất cả phòng từ các schedule đang có,
     * dùng cho combobox chọn phòng khi tạo/sửa lịch.
     */
    private void loadAllRooms() {
        allRooms.clear();
        roomComboBox.removeAllItems();

        List<Schedule> allSchedules = ScheduleService.getAllSchedules();
        for (Schedule s : allSchedules) {
            Room r = s.getRoom();
            if (r == null) continue;
            boolean exists = allRooms.stream().anyMatch(x -> x.getRoomId() == r.getRoomId());
            if (!exists) {
                allRooms.add(r);
                roomComboBox.addItem(r);
            }
        }
    }

    /**
     * Khi ấn nút "Tải lịch chiếu"
     */
    private void onLoadSchedules() {
        Integer movieId = parseInteger(movieIdField.getText(), "Movie ID");
        if (movieId == null) {
            return;
        }

        Movie movie = MovieService.getMovieById(movieId);
        if (movie == null) {
            JOptionPane.showMessageDialog(this,
                    "Không tìm thấy phim với ID = " + movieId,
                    "Lỗi",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        currentSchedules = new ArrayList<>();
        scheduleListModel.clear();

        for (Schedule s : ScheduleService.getAllSchedules()) {
            if (s.getMovie() != null && s.getMovie().getMovieId() == movieId) {
                currentSchedules.add(s);
                scheduleListModel.addElement(s);
            }
        }

        if (currentSchedules.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "Phim này hiện chưa có lịch chiếu nào.",
                    "Thông báo",
                    JOptionPane.INFORMATION_MESSAGE);
        }
    }

    /**
     * Khi chọn 1 schedule bên trái, fill dữ liệu vào form để sửa/xóa.
     */
    private void onScheduleSelected() {
        Schedule selected = scheduleList.getSelectedValue();
        if (selected == null) {
            clearForm();
            return;
        }
        scheduleIdField.setText(String.valueOf(selected.getScheduleId()));
        startTimeField.setText(selected.getStartTime().format(DATE_TIME_FORMATTER));
        endTimeField.setText(selected.getEndTime().format(DATE_TIME_FORMATTER));

        // chọn đúng room trong combobox
        Room scheduleRoom = selected.getRoom();
        if (scheduleRoom != null) {
            for (int i = 0; i < roomComboBox.getItemCount(); i++) {
                Room r = roomComboBox.getItemAt(i);
                if (r.getRoomId() == scheduleRoom.getRoomId()) {
                    roomComboBox.setSelectedIndex(i);
                    break;
                }
            }
        }
    }

    /**
     * Thêm lịch mới cho phim.
     */
    private void onAddSchedule() {
        Integer movieId = parseInteger(movieIdField.getText(), "Movie ID");
        if (movieId == null) {
            return;
        }
        Movie movie = MovieService.getMovieById(movieId);
        if (movie == null) {
            JOptionPane.showMessageDialog(this,
                    "Không tìm thấy phim với ID = " + movieId,
                    "Lỗi",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        Room room = (Room) roomComboBox.getSelectedItem();
        if (room == null) {
            JOptionPane.showMessageDialog(this,
                    "Vui lòng chọn phòng chiếu.",
                    "Thiếu thông tin",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        LocalDateTime startTime = parseDateTime(startTimeField.getText(), "Start time");
        LocalDateTime endTime = parseDateTime(endTimeField.getText(), "End time");
        if (startTime == null || endTime == null) {
            return;
        }

        Schedule schedule = new Schedule(0, movie, room, startTime, endTime);
        boolean ok = ScheduleService.addSchedule(schedule);
        if (ok) {
            JOptionPane.showMessageDialog(this,
                    "Thêm lịch chiếu thành công.",
                    "Thành công",
                    JOptionPane.INFORMATION_MESSAGE);
            onLoadSchedules();
        } else {
            JOptionPane.showMessageDialog(this,
                    "Thêm lịch chiếu thất bại. Kiểm tra lại thời gian/phòng.",
                    "Lỗi",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Cập nhật lịch chiếu đã chọn.
     */
    private void onUpdateSchedule() {
        Integer scheduleId = parseInteger(scheduleIdField.getText(), "Schedule ID");
        if (scheduleId == null) {
            JOptionPane.showMessageDialog(this,
                    "Vui lòng chọn một lịch chiếu trong danh sách để cập nhật.",
                    "Thiếu Schedule ID",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        Integer movieId = parseInteger(movieIdField.getText(), "Movie ID");
        if (movieId == null) {
            return;
        }
        Movie movie = MovieService.getMovieById(movieId);
        if (movie == null) {
            JOptionPane.showMessageDialog(this,
                    "Không tìm thấy phim với ID = " + movieId,
                    "Lỗi",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        Room room = (Room) roomComboBox.getSelectedItem();
        if (room == null) {
            JOptionPane.showMessageDialog(this,
                    "Vui lòng chọn phòng chiếu.",
                    "Thiếu thông tin",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        LocalDateTime startTime = parseDateTime(startTimeField.getText(), "Start time");
        LocalDateTime endTime = parseDateTime(endTimeField.getText(), "End time");
        if (startTime == null || endTime == null) {
            return;
        }

        Schedule updated = new Schedule(0, movie, room, startTime, endTime);
        boolean ok = ScheduleService.updateSchedule(scheduleId, updated);
        if (ok) {
            JOptionPane.showMessageDialog(this,
                    "Cập nhật lịch chiếu thành công.",
                    "Thành công",
                    JOptionPane.INFORMATION_MESSAGE);
            onLoadSchedules();
        } else {
            JOptionPane.showMessageDialog(this,
                    "Cập nhật lịch chiếu thất bại. Kiểm tra lại dữ liệu.",
                    "Lỗi",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Xóa lịch chiếu đã chọn.
     */
    private void onDeleteSchedule() {
        Integer scheduleId = parseInteger(scheduleIdField.getText(), "Schedule ID");
        if (scheduleId == null) {
            JOptionPane.showMessageDialog(this,
                    "Vui lòng chọn một lịch chiếu trong danh sách để xóa.",
                    "Thiếu Schedule ID",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(this,
                "Bạn có chắc muốn xóa lịch chiếu ID = " + scheduleId + " ?",
                "Xác nhận xóa",
                JOptionPane.YES_NO_OPTION);
        if (confirm != JOptionPane.YES_OPTION) {
            return;
        }

        boolean ok = ScheduleService.deleteSchedule(scheduleId);
        if (ok) {
            JOptionPane.showMessageDialog(this,
                    "Xóa lịch chiếu thành công.",
                    "Thành công",
                    JOptionPane.INFORMATION_MESSAGE);
            onLoadSchedules();
        } else {
            JOptionPane.showMessageDialog(this,
                    "Xóa lịch chiếu thất bại. Có thể ID không tồn tại.",
                    "Lỗi",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private void clearForm() {
        scheduleIdField.setText("");
        startTimeField.setText("");
        endTimeField.setText("");
    }

    private Integer parseInteger(String text, String fieldName) {
        if (text == null || text.trim().isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "Vui lòng nhập " + fieldName + ".",
                    "Thiếu dữ liệu",
                    JOptionPane.WARNING_MESSAGE);
            return null;
        }
        try {
            return Integer.parseInt(text.trim());
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this,
                    fieldName + " phải là số nguyên hợp lệ. Giá trị hiện tại: " + text,
                    "Sai định dạng",
                    JOptionPane.ERROR_MESSAGE);
            return null;
        }
    }

    private LocalDateTime parseDateTime(String text, String fieldName) {
        if (text == null || text.trim().isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "Vui lòng nhập " + fieldName + " theo định dạng yyyy-MM-dd HH:mm.",
                    "Thiếu dữ liệu",
                    JOptionPane.WARNING_MESSAGE);
            return null;
        }
        try {
            return LocalDateTime.parse(text.trim(), DATE_TIME_FORMATTER);
        } catch (DateTimeParseException ex) {
            JOptionPane.showMessageDialog(this,
                    fieldName + " sai định dạng. Định dạng đúng: yyyy-MM-dd HH:mm\nVí dụ: 2025-12-01 19:30",
                    "Sai định dạng thời gian",
                    JOptionPane.ERROR_MESSAGE);
            return null;
        }
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException | InstantiationException |
                 IllegalAccessException | javax.swing.UnsupportedLookAndFeelException ex) {
            logger.log(Level.SEVERE, null, ex);
        }

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(() -> new AddSchedule().setVisible(true));
    }
}
