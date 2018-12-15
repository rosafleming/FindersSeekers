package com.gonzalez.fs.finderseekers;

import java.util.ArrayList;
import java.util.List;

public class SpotContent {

    public static final List<SpotItem> ITEMS = new ArrayList<SpotItem>();


    public static void addItem(SpotItem item) {
        ITEMS.add(item);
    }

    public static class SpotItem {
        public final String name;
        public final String state;
        public final String city;
        public final String description;
        public final String directions;
        public final String username;

        public String _key;

    /*static {
      DateTime now = DateTime.now();
      addItem(new HistoryItem(2.0, 1.829, "Length", "Yards", "Meters", now.minusDays(1).toString()));
      addItem(new HistoryItem(1.0, 3.785, "Volume", "Gallons", "Liters", now.minusDays(1).toString()));
      addItem(new HistoryItem(2.0, 1.829, "Length", "Yards", "Meters", now.plusDays(1).toString()));
      addItem(new HistoryItem(1.0, 3.785, "Volume", "Gallons", "Liters", now.plusDays(1).toString()));
    }*/

        public SpotItem(){
            this.name = "";
            this.state = "";
            this.city = "";
            this.description = "";
            this.directions = "";
            this.username = "";
        }


        public SpotItem(String sname, String sstate, String scity,
                           String sdescription, String sdirections, String susername) {
            this.name = sname;
            this.state = sstate;
            this.city = scity;
            this.description = sdescription;
            this.directions = sdirections;
            this.username = susername;
        }

        @Override
        public String toString() {
            return "Name: " + this.name +" \n"+
                    "State: " + this.state +"\n"+
                    "City: " + this.city +"\n"+
                    "Description: " + this.description +"\n"+
                    "Directions: " + this.directions +"\n"+
                    "UserName: " + this.username +"\n";
            //this.fromVal + " " + this.fromUnits + " = " + this.toVal + " " + this.toUnits;
        }

        public String getName() {
            return name;
        }

        public String getState() {
            return state;
        }

        public String getCity() {
            return city;
        }

        public String getDescription() {
            return description;
        }

        public String getDirections() {
            return directions;
        }

        public String getUsername() {
            return username;
        }


    }
}
