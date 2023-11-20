package kg.mega.projectemployeehandbook.services.employee;

import org.springframework.web.multipart.MultipartFile;

public interface SetEmployeeImageService {
    String setImage(String personalNumber, MultipartFile multipartFile);
}
