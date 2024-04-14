package sample;

import java.util.ArrayList;
import java.util.Optional;

public class ShipModel {
    public ArrayList<Groupable> ships;
    ArrayList<ShipModelSubscriber> subscribers;

    public ShipModel() {
        subscribers = new ArrayList<>();
        ships = new ArrayList<>();
    }

    public Groupable createShip(double x, double y) {
        Groupable s = new Ship(x,y);
        ships.add(s);
        notifySubscribers();
        return s;
    }

    public Optional<Groupable> detectHit(double x, double y) {
        System.out.println(ships.stream().filter(s -> s.contains(x, y)).reduce((first, second) -> second));
        return ships.stream().filter(s -> s.contains(x, y)).reduce((first, second) -> second);
    }

    public void moveShip(ArrayList<Groupable> b, double dX, double dY) {
        //move every ship in selected set
//        b.moveShip(dX,dY);
        b.forEach(ship -> ship.move(dX, dY));
        notifySubscribers();
    }

    public void addSubscriber (ShipModelSubscriber aSub) {
        subscribers.add(aSub);
    }

    public Groupable groupSelection(ArrayList<Groupable> g){
        Groupable group = new ShipGroup();
        for(int i = 0; i < g.size(); i++){
            group.addChild(g.get(i));
            this.removeGroupable(g.get(i));
        }
        this.addGroupable(group);
        notifySubscribers();
        return group;
    }

    public void addGroupable(Groupable group) {
        this.ships.add(group);
        notifySubscribers();
    }

    public void cut(ArrayList<Groupable> selected){
        for(Groupable group : selected){
            removeGroupable(group);
        }
        notifySubscribers();
    }

    public void paste(ArrayList<Groupable> copiedData){
        for(Groupable group : copiedData){
            addGroupable(group);
        }
        notifySubscribers();
    }

    public void removeGroupable(Groupable g){
        this.ships.remove(g);
        notifySubscribers();
    }
    private void notifySubscribers() {
        subscribers.forEach(sub -> sub.modelChanged());
    }

    public Groupable findGroup(Groupable group){
        for(Groupable g: ships){
            if(g.equals(group)){
                return g;
            }
        }
        return null;
    }


    public ArrayList<Groupable> ungroupSelection(ArrayList<Groupable> selectedShips) {
        Groupable g = findGroup(selectedShips.get(0));
        removeGroupable(g);
        for(int i=0; i< g.getChildren().size(); i++){
            addGroupable(g.getChildren().get(i));
        }
        return g.getChildren();
    }

    public void rotateShip(ArrayList<Groupable> selectedShips, Number newVal) {
        for(Groupable ships: selectedShips){
            ships.rotate(newVal.doubleValue());
        }
        notifySubscribers();
    }


}
