package mx.com.bsmexico.customertool.api;


import java.net.URL;

import org.apache.commons.lang3.StringUtils;

/**
 * Layout contains all the information to be shown in the application. This
 * class are inmutable
 * 
 * @author jchr
 *
 */
public class Layout {

	private NavRoute route;
	private String name;
	private URL styleCss;
	private String className;

	/**
	 * 
	 */
	private Layout(Layout.LayoutBuilder builder) {
		this.route = builder.route;
		this.name = builder.layoutName;
		this.styleCss = builder.styleCss;
		this.className = builder.className;
	}

	/**
	 * Builder
	 * 
	 * @author jchr
	 *
	 */
	public static class LayoutBuilder {
		private NavRoute route;
		private String layoutName;
		private URL styleCss;
		private String className;

		public LayoutBuilder(final String layoutName) throws IllegalArgumentException {
			if (StringUtils.isBlank(layoutName)) {
				throw new IllegalArgumentException("Layouts must have a name");
			}
			this.layoutName = layoutName;
		}

		public LayoutBuilder route(final NavRoute route) {
			if (route != null) {
				this.route = route;
			}
			return this;
		}

		public LayoutBuilder style(final URL styleCss, final String className) {
			this.styleCss = styleCss;
			this.className = className;
			return this;
		}

		public Layout build() {
			return new Layout(this);
		}
	}

	/**
	 * @return the route
	 */
	public NavRoute getRoute() {
		return route;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @return the styleCss
	 */
	public URL getStyleCss() {
		return styleCss;
	}

	/**
	 * @return the className
	 */
	public String getClassName() {
		return className;
	}
	
}
