    package meumenu.application.meumenu.config;

    import org.springframework.beans.factory.annotation.Autowired;
    import org.springframework.context.annotation.Bean;
    import org.springframework.context.annotation.Configuration;
    import org.springframework.http.HttpMethod;
    import org.springframework.security.authentication.AuthenticationManager;
    import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
    import org.springframework.security.config.annotation.web.builders.HttpSecurity;
    import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
    import org.springframework.security.config.http.SessionCreationPolicy;
    import org.springframework.security.core.Authentication;
    import org.springframework.security.core.AuthenticationException;
    import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
    import org.springframework.security.crypto.password.PasswordEncoder;
    import org.springframework.security.web.SecurityFilterChain;
    import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

    @Configuration
    @EnableWebSecurity
    public class SecurityConfiguration {
        @Autowired
        SecurityFilter securityFilter;
        @Bean
        public SecurityFilterChain securityFilterChain (HttpSecurity httpSecurity) throws Exception{
            return httpSecurity
                    .csrf(csrf -> csrf.disable()).sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))                    .authorizeHttpRequests(authorize -> authorize
                            .requestMatchers("/swagger-ui.html", "/v3/api-docs/**", "/swagger-ui/**").permitAll()
                            .requestMatchers(HttpMethod.POST, "/meumenu/usuarios/cadastrar").permitAll()
                            .requestMatchers(HttpMethod.POST, "/meumenu/usuarios/logar").permitAll()
                            .anyRequest().authenticated())
                    .addFilterBefore(securityFilter, UsernamePasswordAuthenticationFilter.class)
                    .build();
        }

        @Bean
        public AuthenticationManager authenticationManagerBean() throws Exception {
            return new AuthenticationManager(){
                @Override
                public Authentication authenticate(Authentication authentication) throws AuthenticationException {

                    throw new UnsupportedOperationException("Not supported yet.");
                }
            };
        }

        public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration)throws Exception{
            return authenticationConfiguration.getAuthenticationManager();
        }
        @Bean
        public PasswordEncoder passwordEncoder(){
            return new BCryptPasswordEncoder();
        }
    }
