package sample;

import java.util.ArrayList;

public interface Groupable {
    ArrayList<Groupable> getChildren();
    void addChild(Groupable g);
    boolean hasChildren();
    boolean contains(double x, double y);
    void move(double dx, double dy);
    double getLeft();//for bounding boxes
    double getRight();
    double getTop();
    double getBottom();
    double[] getXDisplayArray();
    double[] getYDisplayArray();
    boolean checkShipWithin(Groupable Ship);
    double getTranslateX();
    double getTranslateY();
    void rotate(double a);
    public void rotate(double a, double cx, double cy);
    boolean isContained(double left, double right, double top, double bottom);

}
