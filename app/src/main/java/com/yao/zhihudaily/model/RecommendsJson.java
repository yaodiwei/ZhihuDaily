package com.yao.zhihudaily.model;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by Administrator on 2016/9/16.
 */
public class RecommendsJson {

    private ArrayList<Item> items;
    @SerializedName("item_count")
    private int itemCount;


    public class Item {
        public int index;
        public ArrayList<Recommender> recommenders;
        public Author author;

        public int getIndex() {
            return index;
        }

        public void setIndex(int index) {
            this.index = index;
        }

        public ArrayList<Recommender> getRecommenders() {
            return recommenders;
        }

        public void setRecommenders(ArrayList<Recommender> recommenders) {
            this.recommenders = recommenders;
        }

        public Author getAuthor() {
            return author;
        }

        public void setAuthor(Author author) {
            this.author = author;
        }

        @Override
        public String toString() {
            return "Item{" +
                    "index=" + index +
                    ", recommenders=" + recommenders +
                    ", author=" + author +
                    '}';
        }
    }

    public class Author {
        public String name;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        @Override
        public String toString() {
            return "Author{" +
                    "name='" + name + '\'' +
                    '}';
        }
    }

    public ArrayList<Item> getItems() {
        return items;
    }

    public void setItems(ArrayList<Item> items) {
        this.items = items;
    }

    public int getItemCount() {
        return itemCount;
    }

    public void setItemCount(int itemCount) {
        this.itemCount = itemCount;
    }


    @Override
    public String toString() {
        return "RecommendsJson{" +
                "items=" + items +
                ", itemCount=" + itemCount +
                '}';
    }
}
