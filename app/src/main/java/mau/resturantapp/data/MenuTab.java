package mau.resturantapp.data;

import com.google.firebase.database.Exclude;

/**
 * Created by Yoouughurt on 14-11-2016.
 */

public class MenuTab implements Comparable<MenuTab> {
    private String name;
    private int position;
    private boolean active;
    @Exclude
    private String key;

    public MenuTab(){

    }

    public MenuTab(String name, int position, boolean active){
        this.name = name;
        this.position = position;
        this.active = active;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    @Exclude
    public String getKey() {
        return key;
    }

    @Exclude
    public void setKey(String key) {
        this.key = key;
    }

    @Exclude
    @Override
    public int compareTo(MenuTab menuTab) {
        return position > menuTab.getPosition() ? 1 : (position < menuTab.getPosition() ) ? -1 : 0;
    }
}
