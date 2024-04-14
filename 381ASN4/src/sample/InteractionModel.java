package sample;

import javafx.scene.shape.Rectangle;

import java.util.ArrayList;

public class InteractionModel {
    ArrayList<ShipModelSubscriber> subscribers;
//    Ship selectedShip;
    ArrayList<Groupable> selectedShips;
    Boolean bandRect;
    double topX;
    double topY;
    double width;
    double height;
    ShipClipboard clipboard;
    public InteractionModel() {
        subscribers = new ArrayList<>();
        selectedShips = new ArrayList<>();
        bandRect = false;
        clipboard = new ShipClipboard();
    }

    public void clearSelection() {
        selectedShips.clear();
//        selectedShip = null;
        notifySubscribers();
    }

    /**
     * remove an already selected
     * @param S
     */
    public void removeASelection(Groupable S ){
        selectedShips.remove(S);
        notifySubscribers();
    }
    public boolean checkSelection(Groupable S){
        return selectedShips.contains(S);
    }

    public void setSelected(Groupable newSelection) {
        selectedShips.add(newSelection);
//        selectedShip = newSelection;
        notifySubscribers();
    }
    public void initiateRubberBand(double x1, double y1){
        topX =x1;
        topY = y1;
        width =0;
        height =0 ;
        bandRect = true;

    }
    public void resizeBand(double dx, double dy){
        width += dx;
        height += dy;
        notifySubscribers();
    }

    public void clearBand(){
        bandRect = false;
        topX = 0;
        topY = 0;
        width = 0;
        notifySubscribers();
    }

    public ShipClipboard getClipboard(){
        return this.clipboard;
    }


    public void addSubscriber(ShipModelSubscriber aSub) {
        subscribers.add(aSub);
    }

    private void notifySubscribers() {
        subscribers.forEach(sub -> sub.modelChanged());
    }
}
