package mau.restaurantapp.data.types;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by anwar on 10/15/16.
 */

public class MenuItem {

    private int ID;
    private String beskrivelse;
    private String navn;
    private float pris;
    private String underMenu;
    private ArrayList<String> tilbehør = new ArrayList<String>();

    public MenuItem(int id, String navn, float pris, String underMenu) {
        this.underMenu = underMenu;
        this.ID = id;
        this.navn = navn;
        this.pris = pris;
        tilbehør.add("Der er ikke nogen tilbør");
    }

    public float getPris() {
        return pris;
    }

    public int getID() {
        return ID;
    }

    public String getNavn() {
        return navn;
    }

    public String getBeskrivelse() {
        if (this.beskrivelse != null)
            return beskrivelse;

        else {
            return "Der er ingen beskrivelse til denne varrer";
        }
    }

    public void setBeskrivelse(String beskrivelse) {
        this.beskrivelse = beskrivelse;
    }

    public void addtilbehør(String tilbehør) {
        this.tilbehør.add(tilbehør);

    }

    public List<String> getTilbehør() {
        return tilbehør;
    }
}
