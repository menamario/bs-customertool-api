package mx.com.bsmexico.customertool.api;

import javafx.scene.Node;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;

public abstract class Desktop extends Region {
	
	MenuNavigator menu;
	Pane workArea = new Pane();
	
	
	public Desktop(final MenuNavigator menu) {
		super();
		this.menu = menu;
		getChildren().add(buildDesktop());
	}	
	
	protected MenuNavigator getMenu() {
		return menu;
	}

	protected void setMenu(MenuNavigator menu) {
		this.menu = menu;
	}


	public Pane getWorkArea() {
		return workArea;
	}


	public void setWorkArea(Pane workArea) {
		this.workArea = workArea;
		loadWorkArea();
	}
	
	public void render(){
		this.menu.render();
	}
	
	public abstract void hideMenu();
	public abstract void showMenu();
	public abstract void loadWorkArea();
	
	protected abstract Node buildDesktop();

}
