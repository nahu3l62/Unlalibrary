package com.unla.nahuel;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Configuration
public class WebSecurityConfig extends WebSecurityConfigurerAdapter{
	
	@Autowired
	private DataSource dataSource;
	
	@Autowired
	private BCryptPasswordEncoder passEncoder;
	
	@Autowired
	public void configurerSecurityGlobal(AuthenticationManagerBuilder builder) throws Exception {
		builder.jdbcAuthentication()
		.dataSource(dataSource)
		.passwordEncoder(passEncoder)
		.usersByUsernameQuery("SELECT nombre_de_usuario, contrasena, deshabilitado from usuario where nombre_de_usuario=?")
		.authoritiesByUsernameQuery("SELECT u.nombre_de_usuario, p.rol from perfiles p inner join usuario u on p.id=u.perfiles_id where u.nombre_de_usuario=?");

	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.authorizeRequests()
		.antMatchers("/index","/","/css/**","/images/**","/js/**","/vendor/**").permitAll()
		.antMatchers("/perfiles/").hasAuthority("Administrador")
		.antMatchers("/perfiles/lista").hasAuthority("Administrador")
		.antMatchers("/usuarios/").anonymous()
		.antMatchers("/perfiles/lista/edit/**").hasAuthority("Administrador")
		.antMatchers("/perfiles/lista/delete/**").hasAuthority("Administrador")
		.antMatchers("/usuarios/lista").hasAuthority("Administrador")
		.antMatchers("/usuarios/lista/edit/**").hasAuthority("Administrador")
		.antMatchers("/usuarios/lista/delete/**").hasAuthority("Administrador")
		.antMatchers("/usuarios/lista/activar/**").hasAuthority("Administrador")
		.antMatchers("/usuarios/lista/inactivo").hasAuthority("Administrador")
		.antMatchers("/carreras/lista").authenticated()
		.antMatchers("/carreras/").hasAuthority("Administrador")
		.antMatchers("/carreras/materia").authenticated()
		.antMatchers("/carreras/materia/proyecto").authenticated()
		.anyRequest().authenticated()
		.and()
		.formLogin().permitAll()
		.and()
		.logout().permitAll();
	}
	
}
