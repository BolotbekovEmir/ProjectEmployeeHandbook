package kg.mega.projectemployeehandbook.errors.messages;

public class ErrorDescription {
    public static final String

        ADMIN_NAME_PATTERN = "Введенное имя администратора не соответствует требованиям. Допустимы только буквы латинского алфавита и цифры. Длина строки должна быть от 4 до 24 символов",
        ADMIN_NAME_UNIQUE = "Введенное имя администратора уже используется",
        ADMIN_CREATE_INVALID_FORMAT = "Неудачная попытка регистрации администратора: %s",
        ADMIN_EDIT_INVALID_FORMAT = "Неудачная попытка редактирования администратора: %s",
        ADMIN_NOT_FOUND = "Администратора с таким именем не найдено",
        ADMIN_CREATE_INVALID = "Ошибка при создании администратора",
        ADMIN_EDIT_INVALID = "Ошибка при редактировании администратора",
        ADMIN_DISABLE = "Администратор уже отключен",
        ADMIN_ENABLE = "Администратор уже включен",
        ADMIN_AUTH_INVALID = "Неверный логин или пароль",
        THIS_ADMIN_NAME_ALREADY_USED = "У администратора уже установленно данное имя",
        NAME_PATTERN = "Не соответствуют требованиям. Допустимы только буквы латинницы и кириллицы. Длина строки должна быть от 2 до 50 символов",
        PASSWORD_PATTERN = "Пароль должен соответствовать следующим требованиям: Длина пароля должна быть от 8 до 32 символов. Пароль должен содержать хотя бы одну букву латинского алфавита (в верхнем или нижнем регистре) и хотя бы одну цифру. Пароль должен содержать хотя бы один специальный символ (не букву и не цифру)",
        PASSWORDS_EQUAL = "Пароли не совпадают",
        PERSONAL_NUMBER_UNIQUE = "Данный персональный номер уже используется",
        PERSONAL_NUMBER_PATTERN = "Персональный номер не может быть пуст",
        PHONE_PATTERN = "Номер телефона не соответствует требованиям. Должен начинаться с '+996' и содержать девять цифр после без разделителей. Пример: +996123456789",
        PHONE_UNIQUE = "Данный номер телефона уже используется.",
        EMAIL_PATTERN = "Неверный формат адреса электронной почты. Допустимы буквы, цифры и символы: '+', '_', '.', '-' в имени пользователя, после '@' должна быть хотя бы одна точка. Пример: example@email.com",
        EMAIL_UNIQUE = "Данная электронная почта уже используется",
        ADDRESS_PATTERN = "Неверный формат адреса. Адрес должен содержать только буквы, цифры и символы '-', '.', ',' в пределах от 4 до 52 символов",
        DATE_PATTERN = "Неверный формат даты. Дата должна быть в формате 'дд-мм-гггг' и соответствовать календарным датам. Пример: 01-01-2023",
        PHOTO_NULL = "Добавление фотографии является обязательной операцией",
        FAMILY_STATUS = "Семейный статус не соответствует требованиям. Не найдено в базе или передано пустое значение",
        POSITION_NULL = "Данной позиции в базе не существует",
        MAPPING_ERROR = "Ошибка при маппинге. Возвращен 'null'",
        CREATE_ENTITY_ERROR = "Не удалось произвести операцию создания",
        STRUCTURE_ID_NOT_FOUND = "Структуры с таким номером не найдено",
        STRUCTURE_TYPE_NOT_FOUND = "Тип структуры с таким номером не найдено",
        STRUCTURE_TYPE_UNIQUE = "Тип структуры с таким именем уже существует",
        STRUCTURE_UNIQUE = "Структура с таким именем уже существует",
        STRUCTURE_TYPE_ALREADY_ACTIVE = "Тип структуры уже активен",
        STRUCTURE_TYPE_ALREADY_INACTIVE = "Тип структруры уже неактивен",
        STRUCTURE_ALREADY_ACTIVE = "Структура уже активена",
        STRUCTURE_ALREADY_INACTIVE = "Структрура уже неактивена",
        ENTITY_NOT_FOUND = "Не удалось произвести операцию получения сущностей",
        EDIT_ENTITY_ERROR = "Не удалось произвести операцию редактирования",
        STRUCTURE_TYPE_ID_NOT_FOUND = "Тип структуры с данным номером не найдено",
        STRUCTURE_TYPE_EDIT_ERROR_FORMAT = "Неудачная попытка изменить тип структуры: %s",
        STRUCTURE_NAME_ALREADY_HAVE_THIS_NAME = "Новое имя структуры соответсвует нынешнему",
        POSITION_ID_NOT_FOUND = "Позиции с таким номером не найдено",
        POSITION_NAME_ALREADY_HAVE_THIS_NAME = "Новое название позиции соответствует нынешнему",
        POSITION_ALREADY_INACTIVE = "Позиция уже не активна",
        POSITION_ALREADY_ACTIVE = "Позиция уже активна",
        UNKNOWN_EXCEPTION_NAME_FORMAT = "Неизвестное название исключения: %s",
        DETECTED_ERROR_FORMAT = "Обнаружена ошибка. Текущий стек: %s",
        THIS_ADMIN_PERSONAL_NUMBER_ALREADY_USED = "У администратора уже установлен данный персональный номер",
        THIS_PASSWORD_ALREADY_USED = "Пароль не должен соответствовать старому",
        STRUCTURE_TYPE_NAME_IS_EMPTY = "Имя типа структуры не может быть пустым",
        STRUCTURE_TYPE_NAME_ALREADY_USED = "Тип структуры уже имеет данное имя",
        STRUCTURE_ALREADY_THIS_MASTER = "Для структуры уже установлен данный мастер",
        STRUCTURE_ALREADY_THIS_STRUCTURE_TYPE = "Для структуры уже установлен данный тип структуры",
        POSITION_ALREADY_THIS_MASTER = "Для позиции уже установлен данный мастер",
        POSITION_ALREADY_THIS_NAME = "Позиция уже имеет данное имя",
        ENTITY_ID_IS_NULL = "ID не может быть пустым";

}
