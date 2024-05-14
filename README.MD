Đoạn code sử dụng ý tưởng của WAL và transation
Để chạy thực hiện run: utils.Mysql
Test case:
Thực hiện run ở server test1 và server test2, đoạn code thực hiện chạy sau sẽ bị skip với cùng một ID
Trong giai đoạn update, việc sửa ID trực tiếp trên root mysql cũng bị từ chối