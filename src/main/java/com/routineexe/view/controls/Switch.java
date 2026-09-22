package com.routineexe.view.controls;

import javafx.animation.TranslateTransition;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.scene.control.Skin;
import javafx.scene.control.SkinBase;
import javafx.scene.layout.StackPane;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;

public class Switch extends javafx.scene.control.Control {

    private final BooleanProperty selected = new SimpleBooleanProperty(false);

    public Switch() {
        getStyleClass().add("switch");
        setFocusTraversable(true);
    }

    public Switch(boolean selected) {
        this();
        this.selected.set(selected);
    }

    @Override
    protected Skin<?> createDefaultSkin() {
        return new SwitchSkin(this);
    }

    public final BooleanProperty selectedProperty() {
        return selected;
    }

    public final boolean isSelected() {
        return selected.get();
    }

    public final void setSelected(boolean selected) {
        this.selected.set(selected);
    }

    private static class SwitchSkin extends SkinBase<Switch> {

        private final StackPane track;
        private final Circle thumb;
        private TranslateTransition animation;

        public SwitchSkin(Switch control) {
            super(control);

            track = new StackPane();
            track.getStyleClass().add("switch-track");

            thumb = new Circle();
            thumb.getStyleClass().add("switch-thumb");

            track.getChildren().add(thumb);

            getChildren().add(track);

            updateThumbPosition(control.isSelected(), false);

            control.selectedProperty().addListener((obs, oldVal, newVal) -> {
                updateThumbPosition(newVal, true);
            });

            track.setOnMouseClicked(e -> {
                control.setSelected(!control.isSelected());
            });
        }

        private void updateThumbPosition(boolean selected, boolean animated) {
            double trackWidth = track.getWidth();
            double trackHeight = track.getHeight();
            double thumbRadius = thumb.getRadius();

            if (trackWidth <= 0) {
                trackWidth = 48;
                trackHeight = 28;
                thumbRadius = 10;
            }

            double padding = 4;
            double startX = thumbRadius + padding;
            double endX = trackWidth - thumbRadius - padding;
            double targetX = selected ? endX : startX;

            track.getStyleClass().removeAll("switch-track-on", "switch-track-off");
            track.getStyleClass().add(selected ? "switch-track-on" : "switch-track-off");

            if (animated) {
                if (animation != null) {
                    animation.stop();
                }
                animation = new TranslateTransition(Duration.millis(150), thumb);
                animation.setToX(targetX);
                animation.play();
            } else {
                thumb.setTranslateX(0);
                thumb.setCenterX(targetX);
            }
        }

        @Override
        protected void layoutChildren(double x, double y, double w, double h) {
            track.resizeRelocate(x, y, w, h);
            double thumbRadius = Math.min(h, 20) / 2;
            thumb.setRadius(thumbRadius);
            // Center thumb vertically
            thumb.setCenterY(h / 2);
            updateThumbPosition(getSkinnable().isSelected(), false);
        }

        @Override
        protected double computePrefWidth(double height, double topInset, double rightInset, double bottomInset, double leftInset) {
            return 48;
        }

        @Override
        protected double computePrefHeight(double width, double topInset, double rightInset, double bottomInset, double leftInset) {
            return 28;
        }
    }
}