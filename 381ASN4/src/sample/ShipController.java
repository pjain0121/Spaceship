package sample;

import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;

import java.util.ArrayList;
import java.util.Optional;

public class ShipController {
    InteractionModel iModel;
    ShipModel model;
    double prevX, prevY;
    double dX, dY;

    protected enum State {
        READY, DRAGGING, BAND_SELECTION
    }

    protected State currentState;

    public ShipController() {
        currentState = State.READY;
    }

    public void setInteractionModel(InteractionModel newModel) {
        iModel = newModel;
    }

    public void setModel(ShipModel newModel) {
        model = newModel;
    }

    public void handlePressed(double x, double y, MouseEvent event) {
        prevX = x;
        prevY = y;
        switch (currentState) {
            case READY -> {
                // context: on a ship?
                Optional<Groupable> hit = model.detectHit(x, y);
                if (hit.isPresent()) {
                    //if control is pressed
                    if(event.isControlDown()){
                        //check whether the ship exists in the selection set
                        if (!iModel.checkSelection(hit.get())){
                            iModel.setSelected(hit.get());
                            currentState = State.DRAGGING;
                        }
                        else{
                            //if on the ship + control but already in the set
                            iModel.removeASelection(hit.get());
                        }

                    }

                    else{
                    // on ship, (without control ) not selected

                        if(!iModel.checkSelection(hit.get())){

                            iModel.clearSelection();
                            iModel.setSelected(hit.get());
                            currentState= State.DRAGGING;
                        }
                        else{
                            currentState = State.DRAGGING;
                        }

                    }
                }
                else {
                    // on background - is Shift down?
                    if (event.isShiftDown()) {
                        // create ship
                        Groupable newShip = model.createShip(x, y);
                        iModel.setSelected(newShip);
                        currentState = State.DRAGGING;
                    } else {
                        // clear selection
                        if(event.isControlDown()){
                            //create a band as soon as we see a move
                            currentState = State.BAND_SELECTION;
                            iModel.initiateRubberBand(x, y);
                        }
                        else{
                            iModel.clearSelection();
                            iModel.initiateRubberBand(x, y);
                            currentState = State.BAND_SELECTION;
                        }
                    }
                }
            }
        }
    }

    public void handleDragged(double x, double y, MouseEvent event) {
        dX = x - prevX;
        dY = y - prevY;
        prevX = x;
        prevY = y;
        switch (currentState) {
           case DRAGGING -> model.moveShip(iModel.selectedShips, dX, dY);

            case BAND_SELECTION ->{
                iModel.resizeBand(dX, dY);
            }
        }


    }

    public void handleReleased(double x, double y, MouseEvent event) {
        switch (currentState) {
            case DRAGGING -> {
                currentState = State.READY;
            }
            case BAND_SELECTION -> {
                //check whether there are ships inside
                //for when control is not pressed the selection set is first cleared , thus list should be empty
                if(iModel.selectedShips.size()==0){
//                    model.handleBandSelection(iModel.topX, iModel.topX+ iModel.width,iModel.topY, iModel.topY+ iModel.height);
                    //check whether the ships inside model are within the selection band

                    for (int i =0 ; i< model.ships.size(); i++){

                        //if they are add them to selection set
                        if(model.ships.get(i).isContained(iModel.topX, iModel.topX+ iModel.width,iModel.topY, iModel.topY+ iModel.height)){
                            iModel.setSelected(model.ships.get(i));

                        }

                    }
                    iModel.clearBand();
                    currentState = State.READY;

                }
                else{

                    //if control is pressed the selection set will not be empty if it was not empty before
                    for (int i=0 ; i<model.ships.size() ; i++){
                        Groupable ship = model.ships.get(i);
                        //check whether ships are in the band
                        if(ship.isContained(iModel.topX, iModel.topX+ iModel.width,iModel.topY, iModel.topY+ iModel.height)){
                            //if it is than check whether it is selected
                            if(iModel.checkSelection(ship)){
                                iModel.removeASelection(ship);
                            }
                            //if not selected add to the set
                            else{
                                iModel.setSelected(ship);
                            }
                        }
                    }
                    iModel.clearBand();
                    currentState = State.READY;
                }
            }
        }

    }

    public void handleKeyPressed(KeyEvent keyEvent) {
        System.out.println(keyEvent.getCode());
        switch (keyEvent.getCode()) {
            case G ->{
                Groupable g = model.groupSelection(iModel.selectedShips);
                iModel.clearSelection();
                iModel.setSelected(g);
            }
            case U ->{
                if(iModel.selectedShips.size() == 1 && iModel.selectedShips.get(0).hasChildren()){
                    ArrayList<Groupable> g = model.ungroupSelection(iModel.selectedShips);
                    iModel.clearSelection();
                    for(Groupable group : g){
                        iModel.setSelected(group);
                    }
                }
            }

            case X->{
                if(keyEvent.isControlDown() && iModel.selectedShips.size()!=0){
                    iModel.getClipboard().clearData();

                    iModel.getClipboard().setStoredData(iModel.selectedShips);
                    model.cut(iModel.selectedShips);
                    iModel.clearSelection();
                }
            }

            case V->{
                if(keyEvent.isControlDown() && iModel.getClipboard().storedData.size()!=0){
                    iModel.clearSelection();
                    iModel.getClipboard().copiedData.clear();
                    ArrayList<Groupable> copiedData = iModel.getClipboard().getCopiedData();
                    model.paste(copiedData);
                    for(Groupable group : copiedData){
                        iModel.setSelected(group);
                    }
                }
            }

            case C->{
                if(keyEvent.isControlDown() && iModel.selectedShips.size()!=0){
                    iModel.getClipboard().clearData();
                    iModel.getClipboard().setStoredData(iModel.selectedShips);
                }
            }
        }
    }


    public void handleSlider(Number newVal) {
        model.rotateShip(iModel.selectedShips , newVal);
    }

}
