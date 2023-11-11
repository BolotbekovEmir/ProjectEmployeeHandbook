package kg.mega.projectemployeehandbook.services;

import kg.mega.projectemployeehandbook.errors.CreateEntityException;
import kg.mega.projectemployeehandbook.errors.EditEntityException;
import kg.mega.projectemployeehandbook.errors.GetEntityException;
import kg.mega.projectemployeehandbook.errors.messages.ErrorDescription;
import kg.mega.projectemployeehandbook.models.enums.ExceptionType;
import kg.mega.projectemployeehandbook.services.log.LoggingService;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

import static java.lang.String.*;
import static kg.mega.projectemployeehandbook.errors.messages.ErrorDescription.UNKNOWN_EXCEPTION_NAME_FORMAT;
import static lombok.AccessLevel.*;

/**
 * Сервис для сбора и управления ошибками в приложении.

 * Этот сервис позволяет собирать ошибки и выбрасывать пользовательские исключения на основе типа ошибки.
 * Он также поддерживает логгирование ошибок и информационных сообщений.
 *
 */
@Service
@RequiredArgsConstructor
@FieldDefaults(level = PRIVATE)
public class ErrorCollectorService {
    /**
     * Сервис логгирования для записи информационных и ошибочных сообщений.
     */
    final LoggingService loggingService;

    /**
     * Список сообщений об ошибках.
     */
    List<String> errorMessages = new ArrayList<>();

    /**
     * Флаг, указывающий на наличие ошибок.
     */
    boolean isErrorOccurred;

    /**
     * Очищает коллектор, сбрасывая сообщения и флаг ошибок.
     * Рекомендуется вызывать данный метод перед использованием коллектора.
     */
    public void cleanup() {
        this.errorMessages = new ArrayList<>();
        this.isErrorOccurred = false;
    }

    /**
     * Добавляет сообщения об ошибках в коллектор.
     * Также производит логгирование текущего стека ошибок.
     *
     * @param errorMessages Список сообщений об ошибках для добавления.
     */
    public void addErrorMessages(final List<String> errorMessages) {
        this.errorMessages.addAll(errorMessages);
        this.isErrorOccurred = true;
        loggingService.logError(
            format(ErrorDescription.DETECTED_ERROR_FORMAT, this.errorMessages)
        );
    }

    /**
     * Возвращает флаг, указывающий, были ли ошибки зарегистрированы в коллекторе.
     *
     * @return true, если ошибки были зарегистрированы, в противном случае - false.
     */
    public boolean getErrorOccurred() {
        return this.isErrorOccurred;
    }

    /**
     * Вызывает пользовательское исключение на основе указанного имени исключения.
     * Если имя исключения не соответствует ни одному из предопределенных типов, выбрасывается IllegalArgumentException.
     *
     * @param exceptionName Имя исключения для выбрасывания.
     * @throws CreateEntityException Если exceptionName равен "CREATE_ENTITY_EXCEPTION".
     * @throws EditEntityException Если exceptionName равен "EDIT_ENTITY_EXCEPTION".
     * @throws GetEntityException Если exceptionName равен "GET_ENTITY_EXCEPTION".
     * @throws IllegalArgumentException Если exceptionName не соответствует ни одному из предопределенных типов.
     */
    public void callException(final ExceptionType exceptionName) {
        String unknownExceptionMessage = format(UNKNOWN_EXCEPTION_NAME_FORMAT, exceptionName);
        switch (exceptionName) {
            case CREATE_ENTITY_EXCEPTION -> throw new CreateEntityException(this.errorMessages);
            case EDIT_ENTITY_EXCEPTION   -> throw new EditEntityException(this.errorMessages);
            case GET_ENTITY_EXCEPTION    -> throw new GetEntityException(this.errorMessages);
            default -> {
                loggingService.logError(unknownExceptionMessage);
                throw new IllegalArgumentException(unknownExceptionMessage);
            }
        }
    }
}