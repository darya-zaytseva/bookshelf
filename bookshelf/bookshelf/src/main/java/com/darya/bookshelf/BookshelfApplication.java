package com.darya.bookshelf;

import com.darya.bookshelf.entity.User;
import com.darya.bookshelf.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.UserDetailsServiceAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@SpringBootApplication(exclude = {UserDetailsServiceAutoConfiguration.class})
@EnableAsync
public class BookshelfApplication {

	public static void main(String[] args) {SpringApplication.run(BookshelfApplication.class, args);}
	@Bean
	CommandLineRunner init(UserRepository userRepository, BCryptPasswordEncoder encoder) {
		return args -> {
			User user = userRepository.findByEmail("darya@test.com");
			if (user != null) {
				user.setPassword(encoder.encode("password123"));
				user.setEnabled(true);
				userRepository.save(user);
				System.out.println("✅ Пароль пользователя darya@test.com обновлён на: password123");
			} else {
				System.out.println("⚠️ Пользователь darya@test.com не найден в базе");
			}
		};
	}
}