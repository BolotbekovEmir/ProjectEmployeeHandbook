package kg.mega.projectemployeehandbook.services.log;

import kg.mega.projectemployeehandbook.models.dto.log.ChangerDTO;
import kg.mega.projectemployeehandbook.models.dto.log.EntityDTO;
import kg.mega.projectemployeehandbook.models.dto.log.FieldUpdateDTO;
import kg.mega.projectemployeehandbook.models.entities.Admin;
import kg.mega.projectemployeehandbook.repositories.AdminRepository;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static lombok.AccessLevel.PRIVATE;

@Component
@Slf4j
@RequiredArgsConstructor
@FieldDefaults(level = PRIVATE)
public class InfoCollector {
    final AdminRepository adminRepository;

    ChangerDTO changer;
    EntityDTO entity;
    List<FieldUpdateDTO> fieldUpdates = new ArrayList<>();

    public void cleanup() {
        this.changer = new ChangerDTO();
        this.entity = new EntityDTO();
        this.fieldUpdates = new ArrayList<>();
    }

    public void setChangerInfo() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String adminName = authentication.getPrincipal().toString();
        Optional<Admin> optionalAdmin = adminRepository.findByAdminName(adminName);
        if (optionalAdmin.isPresent()) {
            Admin admin = optionalAdmin.get();
            Long id = admin.getId();
            this.changer.setId(id);
            this.changer.setName(adminName);
        }
    }

    public void setEntityInfo(Long id, String name) {
        this.entity.setId(id);
        this.entity.setName(name);
    }

    public void addFieldUpdatesInfo(String fieldName, String oldValue, String newValue) {
        FieldUpdateDTO fieldUpdate = FieldUpdateDTO.builder()
            .name(fieldName)
            .values(Map.of(oldValue, newValue))
            .build();
        this.fieldUpdates.add(fieldUpdate);
    }

    public void writeFullLog(String... additionalInfo) {
        String FULL_MESSAGE_PATTERN = "Инициатор: {}, Сущность: {}, Обновления: {}, Дополнительная информация: {}";
        log.info(
            FULL_MESSAGE_PATTERN,
            this.changer,
            this.entity,
            this.fieldUpdates,
            additionalInfo
        );
    }

    public void writeLog(String... additionalInfo) {
        String MESSAGE_PATTERN = "Инициатор: {}, Дополнительная информация: {}";
        log.info(
            MESSAGE_PATTERN,
            this.changer,
            additionalInfo
        );
    }

    public void writeEntityLog(String... additionalInfo) {
        String MESSAGE_PATTERN = "Инициатор: {}, Сущность: {}, Дополнительная информация: {}";
        log.info(
            MESSAGE_PATTERN,
            this.changer,
            this.entity,
            additionalInfo
        );
    }
}