package mau.restaurantapp.event.events;

/**
 * Created by AnwarC on 17/12/2016.
 */

public class AskForDeleteTabeEvent {
    private String id;


    public AskForDeleteTabeEvent(String id){
        this.id = id;
    }

    public String getId() {
        return id;
    }
}
