package mx.com.bsmexico.customertool.api.layouts.control;

import com.sun.javafx.scene.control.skin.TableHeaderRow;

import javafx.application.Platform;
import javafx.scene.AccessibleAttribute;
import javafx.scene.control.ScrollBar;
import javafx.scene.control.TableCell;
import javafx.scene.layout.Region;
import javafx.scene.paint.Color;

public abstract class LockedTableCell<T, S> extends TableCell<T, S> {
	{
		Platform.runLater(() -> {
			this.setStyle("-fx-background-color: lightgray; -fx-padding: 0; -fx-border-insets:0;");

			ScrollBar sc = (ScrollBar) getTableView()
					.queryAccessibleAttribute(AccessibleAttribute.HORIZONTAL_SCROLLBAR);
			TableHeaderRow thr = (TableHeaderRow) getTableView().queryAccessibleAttribute(AccessibleAttribute.HEADER);
			Region headerNode = thr.getColumnHeaderFor(this.getTableColumn());

			sc.valueProperty().addListener((ob, o, n) -> {
				double doubleValue = n.doubleValue();
				headerNode.setTranslateX(doubleValue);
				headerNode.toFront();
				this.setTranslateX(doubleValue);
				this.toFront();
			});
		});
	}
}
