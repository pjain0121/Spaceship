package sample;

import javafx.animation.AnimationTimer;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Slider;
import javafx.scene.effect.Bloom;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;

import java.util.ArrayList;
import java.util.List;

public class ShipView extends StackPane implements ShipModelSubscriber {
    Canvas myCanvas;
    GraphicsContext gc;
    ShipModel model;
    InteractionModel iModel;
    Slider slider;
    public ShipView() {
        slider = new Slider();
        slider.setMin(-180);
        slider.setMax(180);
        slider.setValue(0);
        slider.setTranslateY(-340);

        myCanvas = new Canvas(1000,700);
        gc = myCanvas.getGraphicsContext2D();
        this.getChildren().addAll(myCanvas, slider);
        this.setStyle("-fx-background-color: black");
    }

    public void setModel(ShipModel newModel) {
        model = newModel;
    }

    public void setInteractionModel(InteractionModel newIModel) {
        iModel = newIModel;
    }

    public void setController(ShipController controller) {
        slider.valueProperty().addListener((observable , oldVal, newVal) -> controller.handleSlider(newVal));
        myCanvas.setOnMousePressed(e -> controller.handlePressed(e.getX(),e.getY(), e));
        myCanvas.setOnMouseDragged(e -> controller.handleDragged(e.getX(),e.getY(), e));
        myCanvas.setOnMouseReleased(e -> controller.handleReleased(e.getX(),e.getY(), e));
        myCanvas.setOnKeyPressed(e->controller.handleKeyPressed(e));
    }

    public void draw() {
        gc.clearRect(0, 0, myCanvas.getWidth(), myCanvas.getHeight());
        if(iModel.bandRect){
            gc.setFill(Color.YELLOW);
            gc.fillRect(iModel.topX, iModel.topY, iModel.width, iModel.height);
        }
        model.ships.forEach(group ->{
            if(!group.hasChildren()){
                if (iModel.checkSelection(group)) {
                    gc.setFill(Color.YELLOW);
                    gc.setStroke(Color.CORAL);
                } else {
                    gc.setStroke(Color.YELLOW);
                    gc.setFill(Color.CORAL);
                }
                drawShip(group);
            }
            else{
                if(iModel.checkSelection(group)){
                    gc.setFill(Color.YELLOW);
                    gc.setStroke(Color.CORAL);
                    drawGroup(group.getChildren());
                    drawSelectedBox(group);
                }else{
                    gc.setStroke(Color.YELLOW);
                    gc.setFill(Color.CORAL);
                    drawGroup(group.getChildren());
                }
            }
        });
    }

    private void drawGroup(ArrayList<Groupable> g){
//        ArrayList<Groupable> child = ;
        g.forEach(group -> {
            if(!group.hasChildren()){
                drawShip(group);
            }
            else{
                drawGroup(group.getChildren());
            }
        });
    }

    public void drawShip(Groupable group){
        gc.fillPolygon(group.getXDisplayArray(), group.getYDisplayArray(), group.getXDisplayArray().length);
        gc.strokePolygon(group.getXDisplayArray(), group.getYDisplayArray(), group.getXDisplayArray().length);
    }
    protected void drawSelectedBox(Groupable g){
        gc.setFill(null);
        gc.setStroke(Color.WHITE);
        gc.setLineWidth(2.0);
        gc.strokeRect(g.getLeft(), g.getTop(), g.getRight()-g.getLeft(),g.getBottom()-g.getTop());
    }
    @Override
    public void modelChanged() {
        draw();
    }
}
