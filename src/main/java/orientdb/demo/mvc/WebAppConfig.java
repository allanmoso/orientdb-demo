package orientdb.demo.mvc;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import com.mangofactory.swagger.plugin.EnableSwagger;

@Configuration
@EnableWebMvc
@ComponentScan(basePackages="orientdb.demo")
@EnableTransactionManagement
@EnableSwagger
public class WebAppConfig {

}
