package utils;

import java.io.FileWriter;
import java.io.IOException;

public class File {
    public void WALs(int eventID, String eventStatus){
        String fileName = "logs/myfile.txt";
        String contentToAppend = eventID+" "+eventStatus+"\n";

        try {
            // FileWriter sẽ mở tệp tin để ghi, và nếu tệp tin không tồn tại, nó sẽ tạo một tệp tin mới
            // Tham số thứ hai của FileWriter là true để cho phép ghi vào cuối tệp tin (append)
            FileWriter writer = new FileWriter(fileName, true);
            // Ghi nội dung vào tệp tin
            writer.write(contentToAppend);
            // Đảm bảo rằng dữ liệu được ghi vào tệp tin
            writer.flush();
            // Đóng tệp tin sau khi ghi
            writer.close();
            System.out.println("Content appended to file successfully.");
        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
    }
    public static void main(String[] args) {

    }
}
