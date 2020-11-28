package cn.edu.seu.alumni_server.controller.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AddActivity {

    String title;

    String description;

    String time;

    String expiration;

    String type;

    String location;

    String cost;

    Integer visible;

    String tag;

    MultipartFile[] img;
}
