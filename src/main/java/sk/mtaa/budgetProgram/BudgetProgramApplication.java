package sk.mtaa.budgetProgram;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import sk.mtaa.budgetProgram.Filters.AuthentificationFilter;

@SpringBootApplication
public class BudgetProgramApplication {

	public static void main(String[] args) {
		SpringApplication.run(BudgetProgramApplication.class, args);
	}

}
