package com.example.rosa.finderseekers;

import java.util.ArrayList;
import java.util.List;

public class SpotContent {

    public static final List<SpotItem> ITEMS = new ArrayList<SpotItem>();


    public static void addItem(SpotItem item) {
        ITEMS.add(item);
    }

    public static class SpotItem {
        public final String Name;
        public final String State;
        public final String City;
        public final String Description;
        public final String Directions;
        public final String UserName;

        public String _key;

    /*static {
      DateTime now = DateTime.now();
      addItem(new HistoryItem(2.0, 1.829, "Length", "Yards", "Meters", now.minusDays(1).toString()));
      addItem(new HistoryItem(1.0, 3.785, "Volume", "Gallons", "Liters", now.minusDays(1).toString()));
      addItem(new HistoryItem(2.0, 1.829, "Length", "Yards", "Meters", now.plusDays(1).toString()));
      addItem(new HistoryItem(1.0, 3.785, "Volume", "Gallons", "Liters", now.plusDays(1).toString()));
    }*/

        public SpotItem(){
            this.Name = "";
            this.State = "";
            this.City = "";
            this.Description = "";
            this.Directions = "";
            this.UserName = "";
        }


        public SpotItem(String sname, String sstate, String scity,
                           String sdescription, String sdirections, String susername) {
            this.Name = sname;
            this.State = sstate;
            this.City = scity;
            this.Description = sdescription;
            this.Directions = sdirections;
            this.UserName = susername;
        }

        @Override
        public String toString() {
            return "";
            //this.fromVal + " " + this.fromUnits + " = " + this.toVal + " " + this.toUnits;
        }
    }
}
