package kg.mega.projectemployeehandbook.configuration;

import kg.mega.projectemployeehandbook.models.enums.AdminRole;
import kg.mega.projectemployeehandbook.services.admin.AuthAdminService;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import static lombok.AccessLevel.PRIVATE;

@EnableWebSecurity
@FieldDefaults(level = PRIVATE, makeFinal = true)
public class SecurityConfiguration {
    AuthAdminService authAdminService;
    TokenFilterConfiguration tokenFilter;

    @Autowired
    public SecurityConfiguration(@Lazy AuthAdminService authAdminService, @Lazy TokenFilterConfiguration tokenFilter) {
        this.authAdminService = authAdminService;
        this.tokenFilter = tokenFilter;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http)
        throws Exception {
        http
            .csrf().disable()
            .cors().disable()
            .authorizeRequests()

            .antMatchers("/api/v1/admins/auth").permitAll()
            .antMatchers("/api/v1/admins/find-by").permitAll()
            .antMatchers("/api/v1/employees/find-by").permitAll()
            .antMatchers("/api/v1/structures/find-by").permitAll()
            .antMatchers("/api/v1/structure-types/find-by").permitAll()
            .antMatchers("/api/v1/positions/find-by").permitAll()

            .antMatchers("/api/v1/employees/*").hasAnyAuthority(AdminRole.ADMIN.name(), AdminRole.SYSTEM_ADMIN.name())
            .antMatchers("/api/v1/structures/*").hasAuthority(AdminRole.ADMIN.name())
            .antMatchers("/api/v1/structure-types/*").hasAuthority(AdminRole.ADMIN.name())
            .antMatchers("/api/v1/positions/*").hasAuthority(AdminRole.ADMIN.name())
            .antMatchers("/api/v1/admins/change-password").hasAnyAuthority(AdminRole.SYSTEM_ADMIN.name(), AdminRole.ADMIN.name())

            .antMatchers("/api/v1/*").hasAuthority(AdminRole.SYSTEM_ADMIN.name())

            .and()
            .authenticationProvider(authenticationProvider())
            .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            .and()
            .exceptionHandling()
            .authenticationEntryPoint(new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED))
            .and()
            .addFilterBefore(tokenFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration)
        throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();
        daoAuthenticationProvider.setPasswordEncoder(encoder());
        daoAuthenticationProvider.setUserDetailsService(authAdminService);
        return daoAuthenticationProvider;
    }

    @Bean
    public PasswordEncoder encoder() {
        return new BCryptPasswordEncoder();
    }

}
