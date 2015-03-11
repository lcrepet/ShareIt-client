package fr.lyon.insa.ot.sims.shareit_client;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Gaetan on 10/03/2015.
 */
public class PreDefinedObjects {
    private ArrayList<PredefinedObject> list;

    public PreDefinedObjects(){
        list = new ArrayList<>();
        list.add(new PredefinedObject("Appareil à raclette", 3));
        list.add(new PredefinedObject("Tournevis", 2));
    }

    public ArrayList<PredefinedObject> getList() {
        return list;
    }

    public class PredefinedObject {
        private String name;
        private int category;

        PredefinedObject(String name, int category){
            this.name = name;
            this.category = category;
        }

        public String getName() {
            return name;
        }

        public int getCategory(){
            return category;
        }
    }
}
