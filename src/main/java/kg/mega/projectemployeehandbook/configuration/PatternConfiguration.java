package kg.mega.projectemployeehandbook.configuration;

public class PatternConfiguration {

    public static final String

        ADMIN_NAME_PATTERN             = "^[A-Za-z\\d_]{4,24}$",
        EDIT_ADMIN_NAME_PATTERN        = "^(|[A-Za-z\\d]{4,24})$",
        EMPLOYEE_NAME_PATTERN          = "^[A-Za-zА-Яа-я]{2,50}$",
        EMPLOYEE_OPTIONAL_NAME_PATTERN = "^(|[A-Za-zА-Яа-я]{2,50})$",
        PASSWORD_PATTERN               = "^(?=.*[A-Za-z\\d])(?=.*[^A-Za-z\\d]).{8,32}$",
        PASSWORD_OPTIONAL_PATTERN      = "^(|(?=.*[A-Za-z\\d])(?=.*[^A-Za-z\\d]).{8,32})$",
        PHONE_PATTERN                  = "^\\+996\\d{9}$",
        EMAIL_PATTERN                  = "^[A-Za-z\\d+_.-]+@(.+)$",
        ADDRESS_PATTERN                = "^[A-Za-zА-Яа-я\\d-.,\\s]{4,52}$",
        DATE_PATTERN                   = "^(0[1-9]|[12][0-9]|3[01])-(0[1-9]|1[0-2])-(\\d{4})$",
        DATE_OPTIONAL_PATTERN          = "^(|(0[1-9]|[12][0-9]|3[01])-(0[1-9]|1[0-2])-(\\d{4}))$";

}
