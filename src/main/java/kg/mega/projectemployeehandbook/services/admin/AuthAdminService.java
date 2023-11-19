package kg.mega.projectemployeehandbook.services.admin;

import kg.mega.projectemployeehandbook.models.dto.admin.AuthAdminDTO;
import kg.mega.projectemployeehandbook.models.dto.admin.TokenAdminDTO;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

public interface AuthAdminService extends UserDetailsService {

    TokenAdminDTO authentication(AuthAdminDTO authDTO);
    UserDetails loadUserByUsername(String username) throws UsernameNotFoundException;

}
