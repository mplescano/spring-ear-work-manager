package com.mplescano.workmanager;

import java.util.EnumSet;

import javax.servlet.DispatcherType;
import javax.servlet.Filter;
import javax.servlet.FilterRegistration;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRegistration;
import javax.servlet.FilterRegistration.Dynamic;

import org.springframework.core.Conventions;
import org.springframework.util.Assert;
import org.springframework.util.ObjectUtils;
import org.springframework.web.WebApplicationInitializer;
import org.springframework.web.context.ContextLoader;
import org.springframework.web.context.ContextLoaderListener;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;
import org.springframework.web.filter.CharacterEncodingFilter;
import org.springframework.web.servlet.DispatcherServlet;

import com.mplescano.workmanager.config.mvc.MvcWebConfig;
import com.mplescano.workmanager.config.root.BackEndConfig;

/**
 * @author mplescano
 * 
 * @see http://senthadev.com/sharing-spring-container-between-modules-in-a-web-application.html
 *
 */
public class SpringServletInitializer implements WebApplicationInitializer {
    
	/**
	 * The default servlet name. Can be customized by overriding {@link #getServletName}.
	 */
	public static final String DEFAULT_SERVLET_NAME = "dispatcher";

	public void onStartup(ServletContext servletContext) throws ServletException {
	    servletContext.setInitParameter(ContextLoader.LOCATOR_FACTORY_SELECTOR_PARAM, "classpath*:beanRefContext.xml");
		servletContext.setInitParameter(ContextLoader.LOCATOR_FACTORY_KEY_PARAM, "ear.context");
		//servletContext.setInitParameter(ContextLoader.CONFIG_LOCATION_PARAM, DEFAULT_CONFIG_LOCATION_PREFIX + DEFAULT_SERVLET_NAME + "-servlet" + DEFAULT_CONFIG_LOCATION_SUFFIX);
		
		this.registerContextLoaderListener(servletContext);
		this.registerDispatcherServlet(servletContext);
	}
	
	/**
	 * Register a {@link ContextLoaderListener} against the given servlet context. The
	 * {@code ContextLoaderListener} is initialized with the application context returned
	 * from the {@link #createRootApplicationContext()} template method.
	 * @param servletContext the servlet context to register the listener against
	 */
	protected void registerContextLoaderListener(ServletContext servletContext) {
		WebApplicationContext rootAppContext = this.createRootApplicationContext();
		servletContext.addListener(new ContextLoaderListener(rootAppContext));
	}

	protected WebApplicationContext createRootApplicationContext()
	{
	     Class<?>[] rootConfigClasses = getRootConfigClasses();
	     AnnotationConfigWebApplicationContext rootAppContext = new AnnotationConfigWebApplicationContext();
	     if (!ObjectUtils.isEmpty(rootConfigClasses)) {
	    	 rootAppContext.register(rootConfigClasses);
	     }
	     
	     return rootAppContext;
	}
	
	protected Class<?>[] getRootConfigClasses() {
		return new Class[]{BackEndConfig.class};
	}

	protected Class<?>[] getServletConfigClasses() {
		return new Class[]{MvcWebConfig.class};
	}

	protected String[] getServletMappings() {
		 return new String[]{"/"};
	}
	
    protected Filter[] getServletFilters() {
        // Used to provide the ability to enter Chinese characters inside the Owner Form
        CharacterEncodingFilter characterEncodingFilter = new CharacterEncodingFilter();
        characterEncodingFilter.setEncoding("UTF-8");
        characterEncodingFilter.setForceEncoding(true);
        return new Filter[]{characterEncodingFilter};/*, new OpenEntityManagerInViewFilter()*/
    }

	/**
	 * {@inheritDoc}
	 * <p>This implementation creates an {@link AnnotationConfigWebApplicationContext},
	 * providing it the annotated classes returned by {@link #getServletConfigClasses()}.
	 * @throws IllegalArgumentException if {@link #getServletConfigClasses()} returns
	 * empty or {@code null}
	 */
	protected WebApplicationContext createServletApplicationContext() {
		AnnotationConfigWebApplicationContext servletAppContext =
				new AnnotationConfigWebApplicationContext();
		Class<?>[] servletConfigClasses = this.getServletConfigClasses();
		Assert.notEmpty(servletConfigClasses,
				"getServletConfigClasses() did not return any configuration classes");

		servletAppContext.register(servletConfigClasses);
		return servletAppContext;
	}
	
	/**
	 * Register a {@link DispatcherServlet} against the given servlet context.
	 * <p>This method will create a {@code DispatcherServlet} with the name returned by
	 * {@link #getServletName()}, initializing it with the application context returned
	 * from {@link #createServletApplicationContext()}, and mapping it to the patterns
	 * returned from {@link #getServletMappings()}.
	 * <p>Further customization can be achieved by overriding {@link
	 * #customizeRegistration(ServletRegistration.Dynamic)}.
	 * @param servletContext the context to register the servlet against
	 */
	protected void registerDispatcherServlet(ServletContext servletContext) {
		String servletName = this.getServletName();
		Assert.hasLength(servletName,
				"getServletName() may not return empty or null");

		WebApplicationContext servletAppContext = this.createServletApplicationContext();

		DispatcherServlet dispatcherServlet = new DispatcherServlet(servletAppContext);

		ServletRegistration.Dynamic registration =
				servletContext.addServlet(servletName, dispatcherServlet);

		Assert.notNull(registration,
				"Failed to register servlet with name '" + servletName + "'." +
				"Check if there is another servlet registered under the same name.");

		registration.setLoadOnStartup(1);
		registration.addMapping(getServletMappings());
		registration.setAsyncSupported(isAsyncSupported());

		Filter[] filters = getServletFilters();
		if (!ObjectUtils.isEmpty(filters)) {
			for (Filter filter : filters) {
				registerServletFilter(servletContext, filter);
			}
		}

		this.customizeRegistration(registration);
	}

	/**
	 * Return the name under which the {@link DispatcherServlet} will be registered.
	 * Defaults to {@link #DEFAULT_SERVLET_NAME}.
	 * @see #registerDispatcherServlet(ServletContext)
	 */
	protected String getServletName() {
		return DEFAULT_SERVLET_NAME;
	}
	
	/**
	 * Add the given filter to the ServletContext and map it to the
	 * {@code DispatcherServlet} as follows:
	 * <ul>
	 * <li>a default filter name is chosen based on its concrete type
	 * <li>the {@code asyncSupported} flag is set depending on the
	 * return value of {@link #isAsyncSupported() asyncSupported}
	 * <li>a filter mapping is created with dispatcher types {@code REQUEST},
	 * {@code FORWARD}, {@code INCLUDE}, and conditionally {@code ASYNC} depending
	 * on the return value of {@link #isAsyncSupported() asyncSupported}
	 * </ul>
	 * <p>If the above defaults are not suitable or insufficient, register
	 * filters directly with the {@code ServletContext}.
	 *
	 * @param servletContext the servlet context to register filters with
	 * @param filter the filter to be registered
	 * @return the filter registration
	 */
	protected FilterRegistration.Dynamic registerServletFilter(ServletContext servletContext, Filter filter) {
		String filterName = Conventions.getVariableName(filter);
		Dynamic registration = servletContext.addFilter(filterName, filter);
		registration.setAsyncSupported(isAsyncSupported());
		registration.addMappingForServletNames(getDispatcherTypes(), false, getServletName());
		return registration;
	}

	private EnumSet<DispatcherType> getDispatcherTypes() {
		return isAsyncSupported() ?
			EnumSet.of(DispatcherType.REQUEST, DispatcherType.FORWARD, DispatcherType.INCLUDE, DispatcherType.ASYNC) :
			EnumSet.of(DispatcherType.REQUEST, DispatcherType.FORWARD, DispatcherType.INCLUDE);
	}

	/**
	 * A single place to control the {@code asyncSupported} flag for the
	 * {@code DispatcherServlet} and all filters added via {@link #getServletFilters()}.
	 * <p>The default value is "true".
	 */
	protected boolean isAsyncSupported() {
		return true;
	}

	/**
	 * Optionally perform further registration customization once
	 * {@link #registerDispatcherServlet(ServletContext)} has completed.
	 * @param registration the {@code DispatcherServlet} registration to be customized
	 * @see #registerDispatcherServlet(ServletContext)
	 */
	protected void customizeRegistration(ServletRegistration.Dynamic registration) {
	}
}