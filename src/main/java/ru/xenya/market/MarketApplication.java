package ru.xenya.market;

import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.autoconfigure.web.servlet.error.ErrorMvcAutoConfiguration;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Scope;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

//import ru.xenya.market.app.security.SecurityConfiguration;
import ru.xenya.market.backend.data.entity.User;
import ru.xenya.market.backend.repositories.CustomerRepository;
import ru.xenya.market.backend.repositories.UserRepository;
import ru.xenya.market.backend.service.CustomerService;
import ru.xenya.market.backend.service.UserService;
import ru.xenya.market.ui.MainView;
import ru.xenya.market.ui.crud.CrudEntityPresenter;

@SpringBootApplication(scanBasePackageClasses = {/*SecurityConfiguration.class,*/ MainView.class, MarketApplication.class,
		CustomerService.class, UserService.class}, exclude = ErrorMvcAutoConfiguration.class)
@EnableJpaRepositories(basePackageClasses = { UserRepository.class, CustomerRepository.class})
@EntityScan(basePackageClasses = { User.class })

public class MarketApplication extends SpringBootServletInitializer {

	public static void main(String[] args) {
		SpringApplication.run(MarketApplication.class, args);
	}


	protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
		return application.sources(MarketApplication.class);
	}

//	@Bean
//	public User getUser() {
//		return new User();
//	}
//
//	@Bean
//	@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
//	public CrudEntityPresenter<User> userPresenter(UserService crudService, User currentUser) {
//		return new CrudEntityPresenter<>(crudService, currentUser);
//	}

//	@Bean
//	public CommandLineRunner loadData(OrderRepository orderRepository, CustomerRepository customerRepository) {
//
//
//		return (args)->{
//			orderRepository.save(new Order(LocalDate.now(), Payment.CASH, OrderState.NEW, customerRepository.getOne(2L) ));
//			orderRepository.save(new Order(LocalDate.now().minusDays(1), Payment.NONCASH, OrderState.READY, customerRepository.getOne(2L) ));
//		};
//	}


}
