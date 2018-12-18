package ru.xenya.market;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.autoconfigure.web.servlet.error.ErrorMvcAutoConfiguration;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import ru.xenya.market.app.security.SecurityConfiguration;
import ru.xenya.market.backend.data.entity.User;
import ru.xenya.market.backend.repositories.CustomerRepository;
import ru.xenya.market.backend.repositories.PriceRepository;
import ru.xenya.market.backend.repositories.UserRepository;
import ru.xenya.market.backend.service.CustomerService;
import ru.xenya.market.backend.service.PriceService;
import ru.xenya.market.backend.service.UserService;
import ru.xenya.market.ui.MainView;

//import ru.xenya.market.app.security.SecurityConfiguration;

@SpringBootApplication(scanBasePackageClasses = {SecurityConfiguration.class, MainView.class, MarketApplication.class,
        CustomerService.class, UserService.class, PriceService.class}, exclude = ErrorMvcAutoConfiguration.class)
@EnableJpaRepositories(basePackageClasses = {UserRepository.class/*, CustomerRepository.class, PriceRepository.class*/})
@EntityScan(basePackageClasses = {User.class})
//@EnableJdbcHttpSession

public class MarketApplication extends SpringBootServletInitializer {

    public static void main(String[] args) {
        SpringApplication.run(MarketApplication.class, args);
    }


    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.sources(MarketApplication.class);
    }


}
