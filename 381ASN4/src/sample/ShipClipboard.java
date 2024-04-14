package sample;

import java.util.ArrayList;

public class ShipClipboard {
    //where copy of data would be stored
    ArrayList<Groupable> copiedData;

    //would store the data to be copied
    ArrayList<Groupable> storedData;
    public ShipClipboard(){
        copiedData = new ArrayList<>();
        storedData = new ArrayList<>();

    }

    private void copy(){
        for(Groupable selected: storedData){
            if(!selected.hasChildren()){
                copiedData.add(copyShip(selected));

            }else{
                copiedData.add(copyGroup(selected));
            }
        }
    }

public Groupable copyGroup(Groupable group){
    Groupable newGroup = new ShipGroup();
    for(Groupable g : group.getChildren()){
        if(!g.hasChildren() && g instanceof Ship){
            newGroup.addChild(copyShip(g));
        }
        else{
            newGroup.addChild(copyGroup(g));
        }
    }
    return newGroup;
}


    public Groupable copyShip(Groupable ogShip){
        Groupable ship = new Ship(ogShip.getTranslateX(), ogShip.getTranslateY());
        //to preserve the rotation after copying
        for(int i = 0; i<ogShip.getXDisplayArray().length; i++){
            ship.getXDisplayArray()[i]= ogShip.getXDisplayArray()[i];
            ship.getYDisplayArray()[i]= ogShip.getYDisplayArray()[i];
        }
        return ship;
    }

    public void clearData(){
        storedData.clear();
        copiedData.clear();
    }
    public ArrayList<Groupable> getCopiedData(){
        copy();
        //inatead of sending the data thats stored on the clipboard we send a copy
        return copiedData;
    }

    /**
     *
     * @param g Selection set in iModel
     */
    public void setStoredData(ArrayList<Groupable> g){
        for(Groupable group : g){
            storedData.add(group);
        }
    }
}
