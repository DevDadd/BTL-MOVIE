# 🎬 Ứng dụng Bán Vé Xem Phim

## 1. Lý do chọn đề tài
Trong bối cảnh công nghệ thông tin phát triển mạnh mẽ, nhu cầu mua sắm trực tuyến ngày càng tăng cao. Việc tìm kiếm và mua vé xem phim nhanh chóng, tiện lợi đã trở thành thói quen của nhiều người. Nhóm chúng em quyết định chọn đề tài **Ứng dụng bán vé xem phim** vì:

- **Tính ứng dụng thực tế**: Giúp người dùng dễ dàng tìm và đặt vé xem phim yêu thích.
- **Dễ phát triển mở rộng**: Có thể bổ sung các tính năng như gợi ý phim, tương tác xã hội, hoặc truyền hình trực tuyến.
- **Cơ hội học hỏi**: Rèn luyện kỹ năng phân tích yêu cầu, thiết kế OOP, và lập trình ứng dụng thực tế.
- **Sở thích cá nhân**: Cả nhóm đều yêu thích phim ảnh và công nghệ, tạo động lực để phát triển dự án.

---

## 2. Các chức năng chính

### 🎥 Quản lý Phim ( movie ) 
- Hiển thị( show ) danh sách phim theo thể loại.
- Tìm kiếm ( finding ) phim theo tên( title) , thể loại( genre ), năm sản xuất ( produce_year ).
- Xem thông tin chi tiết: tên ( title ) , đạo diễn ( director ), diễn viên ( actor/actress ) , mô tả ( description ).

### 👤 Quản lý Tài khoản ( user )
- Đăng ký và đăng nhập tài khoản. ( sign_up , login ).
- Thông tin tài khoản khi đăng ký gồm: tên ( name ) , số điện thoại / email ( phone number / email ), mật khẩu ( password ).
- Điều kiện mật khẩu: ít nhất 8 ký tự ( at least 8 character ), ít nhất 1 ký tụ viết hoa và 1 ký tự đặc biệt. ( at least 1 uppercase character and 1 special character)
- Lưu thông tin cá nhân và lịch sử mua vé.

### 🎟️ Quản lý Vé ( ticket ) 
- Hiển thị ( show ) địa điểm rạp ( place ), giờ chiếu ( showtime ), số ghế còn trống ( blank_space ).

### 💬 Tương tác Người dùng
- Đặt/hủy vé.( booking / cancel ).
- Thanh Toán ( payment )  
- tra cứu vé đã đặt.( look_up_history)
---

## 3. Bảng phân công công việc

| Giai đoạn | Công việc chi tiết | Người thực hiện |
|-----------|-------------------|-----------------|
| **Giai đoạn 1: Lập kế hoạch & Thiết kế (Tuần 1-2)** | Nghiên cứu và phân tích yêu cầu | Trần Quang Tiến, Tô Thanh Phương |
| | Thiết kế cấu trúc lớp (Class Diagram) và mối quan hệ | Nguyễn Tấn Dũng, Nguyễn Quang Anh |
| | Thiết kế luồng hoạt động của chương trình | Trần Hải Nam |
| **Giai đoạn 2: Phát triển & Cài đặt (Tuần 3-6)** | Phát triển các lớp đối tượng (Movie, User, Ticket, …) | Nguyễn Tấn Dũng |
| | Cài đặt chức năng tìm kiếm, lọc, hiển thị và đặt vé phim | Tô Thanh Phương |
| | Cài đặt chức năng quản lý tài khoản và lịch sử xem | Trần Hải Nam |
| | Xây dựng giao diện người dùng (Console/GUI đơn giản) | Trần Quang Tiến |
| | Cài đặt chức năng quản lý danh sách xem cá nhân | Nguyễn Quang Anh |
| **Giai đoạn 3: Kiểm thử & Hoàn thiện (Tuần 7-8)** | Kiểm thử từng chức năng và toàn bộ ứng dụng | Cả nhóm |
| | Sửa lỗi và tối ưu hiệu suất | Cả nhóm |
| | Chuẩn bị báo cáo dự án | Cả nhóm |

---

## 4. Công nghệ sử dụng
- **Ngôn ngữ lập trình**: Java / C++ / Python (tùy nhóm chọn).
- **Paradigm**: Lập trình Hướng Đối Tượng (OOP).
- **IDE**: IntelliJ IDEA / Eclipse / VS Code.
- **Quản lý mã nguồn**: GitHub/GitLab.

---

## 5. UML Class Diagram (tóm tắt)
- `Movie`: id, title, genre, duration, rating, description.
- `Cinema`: id, name, location, rooms.
- `Room`: id, name, seats.
- `Seat`: id, row, number, status.
- `Showtime`: id, movie, room, startTime, endTime, price.
- `User`: id, name, email, phone, password.
- `Booking`: id, user, showtime, seats, totalPrice, status.
- `Payment`: id, booking, amount, method, status.

(Chi tiết sẽ được thể hiện trong sơ đồ UML của dự án.)

---

## 6. Hướng dẫn chạy chương trình
1. Clone project về máy:
   ```bash
   git clone <repository-url>
