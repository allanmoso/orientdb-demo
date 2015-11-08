package orientdb.demo.mvc;

import javax.servlet.Filter;
import javax.servlet.ServletRegistration.Dynamic;

import org.springframework.web.filter.CharacterEncodingFilter;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.support.AbstractAnnotationConfigDispatcherServletInitializer;

import com.mangofactory.swagger.plugin.EnableSwagger;

import orientdb.demo.MainConfig;

@EnableWebMvc
@EnableSwagger
public class WebAppInit extends AbstractAnnotationConfigDispatcherServletInitializer {
	@Override
	protected void customizeRegistration(final Dynamic registration) {
		// Enable handling of HTTP OPTION
		registration.setInitParameter("dispatchOptionsRequest", "true");
	}

	@Override
	protected Class<?>[] getRootConfigClasses() {
		return new Class<?>[] { MainConfig.class };
	}

	@Override
	protected Class<?>[] getServletConfigClasses() {
		return new Class<?>[] { WebAppConfig.class };
	}

	@Override
	protected Filter[] getServletFilters() {
		final CharacterEncodingFilter characterEncodingFilter = new CharacterEncodingFilter();
		characterEncodingFilter.setEncoding("UTF-8");

		return new Filter[] { characterEncodingFilter };
	}

	@Override
	protected String[] getServletMappings() {
		return new String[] { "/", "*.htm" };
	}

	@Override
	protected String getServletName() {
		return "OrientDb Demo";
	}
}
