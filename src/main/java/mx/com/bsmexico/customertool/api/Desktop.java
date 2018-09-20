package mx.com.bsmexico.customertool.api;

import javafx.scene.Node;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;
import javafx.stage.Stage;

public abstract class Desktop extends Region {

	MenuNavigator menu;
	Pane workArea = new Pane();
	Stage stage;

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

	public void render() {
		this.menu.render();
	}

	public abstract void hideMenu();

	public abstract void showMenu();

	public abstract void loadWorkArea();
	
	public void updatePleca(String color, String nombre){
		//Do nothing by default
	}

	protected abstract Node buildDesktop();

	public void setStage(Stage primaryStage) {
		this.stage = primaryStage;
	}

	public Stage getStage() {
		return this.stage;
	}
	
	public void opacar(){
		this.stage.setOpacity(0.8);
	}
	
	public void desOpacar(){
		this.stage.setOpacity(1);
	}

}
