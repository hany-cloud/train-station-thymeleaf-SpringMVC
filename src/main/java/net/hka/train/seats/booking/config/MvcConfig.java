package net.hka.train.seats.booking.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Description;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import org.thymeleaf.extras.java8time.dialect.Java8TimeDialect;
import org.thymeleaf.spring5.SpringTemplateEngine;
import org.thymeleaf.spring5.templateresolver.SpringResourceTemplateResolver;
import org.thymeleaf.spring5.webflow.view.AjaxThymeleafViewResolver;
import org.thymeleaf.spring5.webflow.view.FlowAjaxThymeleafView;
import org.thymeleaf.templatemode.TemplateMode;
import org.thymeleaf.templateresolver.ITemplateResolver;
import org.thymeleaf.templateresolver.UrlTemplateResolver;

import nz.net.ultraq.thymeleaf.LayoutDialect;

@Configuration
@ComponentScan(basePackages = { "net.hka.common", "net.hka.train.seats.booking" })
class MvcConfig implements WebMvcConfigurer {

	// Thymeleaf html views path.
	private static final String VIEWS = "classpath:/templates/";

	// MVC wep application static resources path and mapping handler pattern.
	private static final String RESOURCES_LOCATION = "classpath:/static/";
	private static final String RESOURCES_HANDLER = "/resources/" + "**";

	// Global character encoding.
	private static final String CHARACTER_ENCODING = "UTF-8";

	// -----------------------------------------------------------------------------------------------------------------------------
	// Thymeleaf Configuration Section with Spring and AJAX integration
	// -----------------------------------------------------------------------------------------------------------------------------

	/**
	 * Sets Thymeleaf AJAX view resolver.
	 * 
	 * @return {@link AjaxThymeleafViewResolver}.
	 */
	@Bean
	@Description("Thymeleaf AJAX view resolver")
	public AjaxThymeleafViewResolver viewResolver() {

		AjaxThymeleafViewResolver viewResolver = new AjaxThymeleafViewResolver();
		viewResolver.setViewClass(FlowAjaxThymeleafView.class);
		viewResolver.setTemplateEngine(this.templateEngine());
		viewResolver.setCharacterEncoding(CHARACTER_ENCODING);
		return viewResolver;
	}

	/**
	 * Sets Thymeleaf template engine with Spring integration.
	 * 
	 * @return {@link SpringTemplateEngine}.
	 */
	@Bean
	@Description("Thymeleaf template engine with Spring integration")
	public SpringTemplateEngine templateEngine() {

		SpringTemplateEngine templateEngine = new SpringTemplateEngine(); // establishes by default an instance of
																			// {@link SpringStandardDialect}
																			// It also configures a {@link
																			// SpringMessageResolver} for MessageSource.

		templateEngine.addTemplateResolver(new UrlTemplateResolver()); // creates {@link UrlTemplateResource} instances
																		// for template resources.

		templateEngine.addTemplateResolver(this.templateResolver()); // set template defaults, makes the html is the
																		// first template in the chain,
																		// you can add more template resolver in chain.

		templateEngine.addDialect(new LayoutDialect()); // A dialect for Thymeleaf that lets you build layouts and
														// reusable templates in
														// order to improve code reuse.

		templateEngine.addDialect(new Java8TimeDialect()); // Thymeleaf Dialect to format and create Java 8 Time
															// objects.

		return templateEngine;
	}

	/**
	 * Sets Spring resource template resolver.
	 * 
	 * @return {@link ITemplateResolver}.
	 */
	@Bean
	public ITemplateResolver templateResolver() {

		SpringResourceTemplateResolver resolver = new SpringResourceTemplateResolver();
		resolver.setPrefix(VIEWS);
		resolver.setSuffix(".html");
		resolver.setTemplateMode(TemplateMode.HTML);
		resolver.setCharacterEncoding(CHARACTER_ENCODING);
		resolver.setCacheable(false);
		return resolver;
	}

	// -----------------------------------------------------------------------------------------------------------------------------
	// General beans, and static resources configuration section
	// -----------------------------------------------------------------------------------------------------------------------------

	/**
	 * Register the static resource handler with the relative resource path, since
	 * the auto configuration annotation such as @EnableWebMvc is not used.
	 */
	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		registry.addResourceHandler(RESOURCES_HANDLER).addResourceLocations(RESOURCES_LOCATION);
	}
}
