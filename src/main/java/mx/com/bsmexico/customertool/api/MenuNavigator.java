package mx.com.bsmexico.customertool.api;

import java.util.List;

import javafx.scene.layout.Region;
import mx.com.bsmexico.customertool.api.NavRoute;

/**
 * 
 * 
 * @author jchr
 *
 */
public abstract class MenuNavigator extends Region {
	
	Desktop desktop;


	/**
	 * Create a default Navigator
	 */
	public MenuNavigator() {
		super();
	}
	
	public MenuNavigator(final List<NavRoute> routes) throws IllegalArgumentException {
		super();
		if (routes == null || routes.isEmpty()) {
			throw new IllegalArgumentException("List routes is empty or null. Use default constructor");
		}
		managedProperty().bind(visibleProperty());
		builLogicNavigationTree(routes);
		layoutChildren();
	}
	
	
	
	public Desktop getDesktop() {
		return this.desktop;
	}

	public void setDesktop(Desktop desktop) {
		this.desktop = desktop;
	}

	public void hide(){
		getDesktop().hideMenu();
	}
	
	public void show(){
		getDesktop().showMenu();
	}
	
	protected abstract void render();

	protected abstract void builLogicNavigationTree(List<NavRoute> routes);
	
}
