package mx.com.bsmexico.customertool.api;

/**
 * All class that implements this interface can be a visual and functional part
 * in the Application.
 * 
 * @author jchr
 *
 */
public abstract class Feature {
	
	private MenuNavigator menuNavigator;

	public MenuNavigator getMenuNavigator(){
		return this.menuNavigator;
	}

	public void setMenuNavigator(MenuNavigator menuNavigtor){
		this.menuNavigator = menuNavigtor;
	}
	
	/**
	 * Get the layout of this component to show it within of the application
	 * 
	 * @return the region (Axis, Chart, Control, Pane) to show in the Application
	 */
	public abstract Layout getLayout();
	
	public abstract void launch();
	
	public Desktop getDesktop(){
		return this.menuNavigator.getDesktop();
	}
}
