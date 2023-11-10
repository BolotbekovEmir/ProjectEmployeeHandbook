package kg.mega.projectemployeehandbook.models.responses;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static lombok.AccessLevel.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = PRIVATE)
public class RestResponse<E> {
    HttpStatus httpStatus;
    E response;
    int statusCode;
    List<String> errorDescriptions = new ArrayList<>();

    public void addErrorDescription(String... errorDescriptions) {
        this.errorDescriptions.addAll(Arrays.stream(errorDescriptions).toList());
    }

    public void setHttpResponse(HttpStatus httpStatus, int statusCode) {
        this.httpStatus = httpStatus;
        this.statusCode = statusCode;
    }

}