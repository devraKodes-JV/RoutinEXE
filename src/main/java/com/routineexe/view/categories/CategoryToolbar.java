package com.routineexe.view.categories;

import com.routineexe.util.FontAwesomeIcons;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;

public class CategoryToolbar extends HBox {

    private final Button createBtn = new Button();
    private final Label createIcon = new Label(FontAwesomeIcons.FOLDER_PLUS);
    private final Label createLabel = new Label("Create Category");

    public CategoryToolbar() {
        getStyleClass().add("button-row");
        setSpacing(12);
        setAlignment(Pos.CENTER_RIGHT);

        createIcon.setStyle("-fx-font-family: 'Font Awesome 6 Free'; -fx-font-weight: 900; -fx-font-size: 14px; -fx-padding: 0 8 0 0; -fx-text-fill: white;");

        HBox btnContent = new HBox(createIcon, createLabel);
        btnContent.setAlignment(Pos.CENTER);
        btnContent.setSpacing(4);

        createBtn.getStyleClass().addAll("btn", "btn-primary");
        createBtn.setDefaultButton(true);
        createBtn.setGraphic(btnContent);

        getChildren().addAll(createBtn);
    }

    public Button getCreateButton() {
        return createBtn;
    }
}