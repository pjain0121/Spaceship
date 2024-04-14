package sample;

import java.util.ArrayList;

public class ShipGroup implements Groupable{
    ArrayList<Groupable> children;
    boolean hasChildren;
    public ShipGroup(){
        this.children = new ArrayList<>();
        hasChildren =true;
    }

    @Override
    public ArrayList<Groupable> getChildren() {
        return children;
    }

    @Override
    public void addChild(Groupable g) {
        this.children.add(g);
    }

    @Override
    public boolean hasChildren() {
        return hasChildren;
    }

    @Override
    public boolean contains(double x, double y) {
        System.out.println("Checking contains for group");
        ArrayList<Groupable> child = getChildren();
        for(int i=0; i < child.size(); i++){
            if(!child.get(i).hasChildren()){
                System.out.println(child.get(i).contains(x, y));
                if(child.get(i).contains(x, y)){
                    return true;
                }
            }else{
                boolean result = child.get(i).contains(x, y);
                if(result){
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public void move(double dx, double dy) {
        for(Groupable group : children){
            if(!group.hasChildren()){
                group.move(dx, dy);
            }
            else{
                group.move(dx, dy);
            }
        }
    }

    @Override
    public double getLeft() {
        double leftValue = Integer.MAX_VALUE;
        for(int i =0 ; i< getChildren().size(); i++){
            if(!getChildren().get(i).hasChildren()){
                if(getChildren().get(i).getLeft() < leftValue){
                    leftValue = getChildren().get(i).getLeft();
                }
            }else{
                double result = getChildren().get(i).getLeft();
                if(result < leftValue){
                    leftValue = result;
                }
            }
        }
        return leftValue;
    }

    @Override
    public double getRight() {
        double rightValue = Integer.MIN_VALUE;
        for(int i =0 ; i< getChildren().size(); i++){
            if(!getChildren().get(i).hasChildren()){
                if(getChildren().get(i).getRight() > rightValue){
                    rightValue = getChildren().get(i).getRight();
                }
            }else{
                double result = getChildren().get(i).getRight();
                if(result > rightValue){
                    rightValue = result;
                }
            }
        }
        return rightValue;
    }

    @Override
    public double getTop() {
        double topValue = Integer.MAX_VALUE;
        for(int i =0 ; i< getChildren().size(); i++){
            if(!getChildren().get(i).hasChildren()){
                if(getChildren().get(i).getTop() < topValue){
                    topValue = getChildren().get(i).getTop();
                }
            }else{
                double result = getChildren().get(i).getTop();
                if(result < topValue){
                    topValue = result;
                }
            }
        }
        return topValue;
    }

    @Override
    public double getBottom() {
        double bottomValue = Integer.MIN_VALUE;
        for(int i =0 ; i< getChildren().size(); i++){
            if(!getChildren().get(i).hasChildren()){
                if(getChildren().get(i).getBottom() > bottomValue){
                    bottomValue = getChildren().get(i).getBottom();
                }
            }else{
                double result = getChildren().get(i).getBottom();
                if(result > bottomValue){
                    bottomValue = result;
                }
            }
        }
        return bottomValue;
    }

    @Override
    public double[] getXDisplayArray() {
        return null;
    }

    @Override
    public double[] getYDisplayArray() {
        return null;
    }

    @Override
    public boolean checkShipWithin(Groupable Ship) {
        if(!Ship.hasChildren()){
            if(this.children.contains(Ship)){
                return true;
            }else{
                for(int i=0; i<children.size();i++){
                    boolean result= checkShipWithin(children.get(i));
                    if(result){
                        return true;
                    }
                }
            }
        }
        return false;
    }

    @Override
    public double getTranslateX() {
        return 0;
    }

    @Override
    public double getTranslateY() {
        return 0;
    }

    @Override
    public void rotate(double a) {
        double centerX = this.getLeft() + (this.getRight()- this.getLeft())/2;
        double centerY = this.getTop() + (this.getBottom()- this.getTop())/2;
        for(Groupable g: getChildren()){
            if(!g.hasChildren()){
                g.rotate(a, centerX, centerY);
            }
            else{
                g.rotate(a);
            }
        }
    }

    @Override
    public void rotate(double a, double cx, double cy) {

    }

    @Override
    public boolean isContained(double left, double right, double top, double bottom) {
        ArrayList<Groupable> child = getChildren();
        for (Groupable groupable : child) {
            if (!groupable.hasChildren()) {
                if (!groupable.isContained(left, right, top, bottom)) {
                    return false;
                }
            } else {
                boolean result = groupable.isContained(left, right, top, bottom);
                if (!result) {
                    return false;
                }
            }
        }
        return true;
    }
}
