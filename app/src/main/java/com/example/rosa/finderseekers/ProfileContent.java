package com.example.rosa.finderseekers;

import java.util.ArrayList;
import java.util.List;

public class ProfileContent {

    public static final List<ContentItem> ITEMS = new ArrayList<ContentItem>();


    public static void addItem(ContentItem item) {
        ITEMS.add(item);
    }

    public static class ContentItem {
        public String Name;
        public String Email;
        public String SeekersNum;
        public String FindersNum;

        public String _key;


        public ContentItem(){
            this.Name = "";
            this.Email = "";
            this.SeekersNum = "";
            this.FindersNum = "";
        }


        public ContentItem(String Name, String Email, String SeekersNum,
                        String FindersNum, String sdirections, String susername) {
            this.Name = Name;
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
