package kg.mega.projectemployeehandbook.utils;

import kg.mega.projectemployeehandbook.errors.messages.ErrorDescription;
import kg.mega.projectemployeehandbook.models.enums.ExceptionType;
import kg.mega.projectemployeehandbook.errors.ErrorCollector;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Component;

import java.util.Arrays;

import static java.util.List.*;
import static lombok.AccessLevel.*;

/**
 * Общий утилитарный класс для выполнения общих операций с репозиториями и работы с перечислениями.
 */
@Component
@RequiredArgsConstructor
@FieldDefaults(level = PRIVATE, makeFinal = true)
public class CommonRepositoryUtil {
    ErrorCollector errorCollector;

    /**
     * Получает сущность из указанного CrudRepository по её идентификатору.
     *
     * @param <E>            Тип сущности.
     * @param entityId       Идентификатор сущности, которую нужно получить.
     * @param repository     CrudRepository для сущности.
     * @param errorMessage   Сообщение об ошибке для записи в случае неудачи.
     * @return Полученная сущность или null, если не найдена.
     */
    public <E> E getEntityById(
        Long entityId,
        CrudRepository<E, Long> repository,
        String errorMessage
    ) {
        if (entityId == null) {
            errorCollector.addErrorMessages(
                of(ErrorDescription.ENTITY_ID_IS_NULL)
            );
            errorCollector.callException(ExceptionType.GET_ENTITY_EXCEPTION);
            return null;
        }

        return repository.findById(entityId)
            .orElseThrow(() -> {
                errorCollector.addErrorMessages(
                    of(errorMessage)
                );
                errorCollector.callException(ExceptionType.GET_ENTITY_EXCEPTION);
                return null;
            });
    }

    /**
     * Получает элемент перечисления (enum) по его строковому представлению.
     *
     * @param <T>       Тип перечисления.
     * @param enumClass Класс перечисления.
     * @param enumName  Строковое представление элемента перечисления.
     * @return Элемент перечисления с указанным строковым представлением или null, если не найден.
     */
    public <T extends Enum<T>> T getEnumByStringName(Class<T> enumClass, String enumName) {
        String correctName = enumName.toUpperCase();

        return Arrays.stream(enumClass.getEnumConstants())
            .filter(
                e -> e.name().toUpperCase().equals(correctName)
            ).findFirst()
            .orElse(null);
    }
}