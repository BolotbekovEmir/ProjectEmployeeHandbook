package kg.mega.projectemployeehandbook.services.admin.impl;

import kg.mega.projectemployeehandbook.errors.ErrorCollector;
import kg.mega.projectemployeehandbook.errors.messages.ErrorDescription;
import kg.mega.projectemployeehandbook.models.dto.admin.AuthAdminDTO;
import kg.mega.projectemployeehandbook.models.dto.admin.TokenAdminDTO;
import kg.mega.projectemployeehandbook.models.entities.Admin;
import kg.mega.projectemployeehandbook.models.enums.ExceptionType;
import kg.mega.projectemployeehandbook.repositories.AdminRepository;
import kg.mega.projectemployeehandbook.services.admin.AuthAdminService;
import kg.mega.projectemployeehandbook.services.log.InfoCollector;
import kg.mega.projectemployeehandbook.utils.JwtUtil;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static lombok.AccessLevel.PRIVATE;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = PRIVATE, makeFinal = true)
public class AuthAdminServiceImpl implements AuthAdminService {
    AdminRepository       adminRepository;

    ErrorCollector        errorCollector;
    AuthenticationManager authManager;
    JwtUtil               jwtUtil;

    @Override
    @Transactional
    public TokenAdminDTO authentication(AuthAdminDTO authDTO) {
        errorCollector.cleanup();

        String
            adminName = authDTO.getAdminName(),
            password  = authDTO.getPassword();

        try {
            authManager.authenticate(new UsernamePasswordAuthenticationToken(adminName, password));
        } catch (BadCredentialsException e) {
            errorCollector.addErrorMessages(List.of(ErrorDescription.ADMIN_AUTH_INVALID));
            errorCollector.callException(ExceptionType.GET_ENTITY_EXCEPTION);
        }

        UserDetails userDetails = loadUserByUsername(authDTO.getAdminName());
        String token = jwtUtil.generateToken(userDetails);

        return jwtUtil.parseToken(token);
    }

    @Override
    public UserDetails loadUserByUsername(String adminName) throws UsernameNotFoundException {
        Admin admin = adminRepository.findByAdminName(adminName).orElseThrow(
            () -> new UsernameNotFoundException(String.format("Пользователь с именем '%s' не найден", adminName))
        );

        return new org.springframework.security.core.userdetails.User(
            admin.getAdminName(),
            admin.getPassword(),
            List.of(new SimpleGrantedAuthority(admin.getAdminRole().toString().toUpperCase()))
        );
    }
}
