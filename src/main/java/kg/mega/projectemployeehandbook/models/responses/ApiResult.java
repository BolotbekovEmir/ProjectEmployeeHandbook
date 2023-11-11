package kg.mega.projectemployeehandbook.models.responses;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;

import java.util.ArrayList;
import java.util.List;

import static lombok.AccessLevel.PRIVATE;

@Getter
@Setter
@FieldDefaults(level = PRIVATE)
public class ApiResult<R> {

    HttpStatus httpStatus;

    int statusCode;

    R response;

    List<String> errors = new ArrayList<>();

    List<String> descriptions = new ArrayList<>();

    public static <R> Builder<R> builder() {
        return new Builder<>();
    }

    @FieldDefaults(level = PRIVATE)
    public static class Builder<R> {
        final ApiResult<R> apiResult;

        public Builder() {
            this.apiResult = new ApiResult<>();
        }

        public Builder<R> httpStatus(HttpStatus httpStatus) {
            this.apiResult.setHttpStatus(httpStatus);
            return this;
        }

        public Builder<R> statusCode(int statusCode) {
            this.apiResult.setStatusCode(statusCode);
            return this;
        }

        public Builder<R> response(R response) {
            this.apiResult.setResponse(response);
            return this;
        }

        public Builder<R> errors(List<String> messages) {
            this.apiResult.setErrors(messages);
            return this;
        }

        public Builder<R> descriptions(List<String> errors) {
            this.apiResult.setDescriptions(errors);
            return this;
        }

        public ApiResult<R> build() {
            return this.apiResult;
        }

    }

}
