package com.example.rosa.finderseekers;

import java.util.ArrayList;
import java.util.List;

public class ProfileContent {

    public static final List<ContentItem> ITEMS = new ArrayList<ContentItem>();


    public static void addItem(ContentItem item) {
        ITEMS.add(item);
    }

    public static class ContentItem {
        public String name;
        public String email;
        public String seekersNum;
        public String findersNum;

        public String _key;

        public ContentItem(){
            this.name = "";
            this.email = "";
            this.seekersNum = "";
            this.findersNum = "";
        }


        public ContentItem(String name, String email, String seekersNum,
                        String findersNum) {
            this.name = name;
            this.email = email;
            this.seekersNum = seekersNum;
            this.findersNum = findersNum;
        }

        @Override
        public String toString() {
            return "";
            //this.fromVal + " " + this.fromUnits + " = " + this.toVal + " " + this.toUnits;
        }

        public String getName() {
            return name;
        }

        public String getEmail() {
            return email;
        }

        public String getSeekersNum() {
            return seekersNum;
        }

        public String getFindersNum() {
            return findersNum;
        }

        public void setFindersNum(String findersNum) {
            this.findersNum = findersNum;
        }

        public void setSeekersNum(String seekersNum) {
            this.seekersNum = seekersNum;
        }
    }
}
