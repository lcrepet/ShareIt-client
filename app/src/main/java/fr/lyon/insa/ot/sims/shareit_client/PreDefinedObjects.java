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
        list.add(new PredefinedObject("Chat", 1));
        list.add(new PredefinedObject("Chien", 1));
        list.add(new PredefinedObject("Cheval", 1));
        list.add(new PredefinedObject("Poule", 1));
        list.add(new PredefinedObject("Tortue", 1));
        list.add(new PredefinedObject("Cochon d'inde", 1));
        list.add(new PredefinedObject("Lapin", 1));
        list.add(new PredefinedObject("Loutre", 1));
        list.add(new PredefinedObject("Vache", 1));
        list.add(new PredefinedObject("Poisson", 1));
        list.add(new PredefinedObject("Tournevis", 2));
        list.add(new PredefinedObject("Perceuse", 2));
        list.add(new PredefinedObject("Escabeau", 2));
        list.add(new PredefinedObject("Clé à molette", 2));
        list.add(new PredefinedObject("Pinceau", 2));
        list.add(new PredefinedObject("Mètre mesureur", 2));
        list.add(new PredefinedObject("Marteau", 2));
        list.add(new PredefinedObject("Saladier", 3));
        list.add(new PredefinedObject("Casserole", 3));
        list.add(new PredefinedObject("Cocotte minute", 3));
        list.add(new PredefinedObject("Appareil à crêpes", 3));
        list.add(new PredefinedObject("Appareil à fondue", 3));
        list.add(new PredefinedObject("Grille pain", 3));
        list.add(new PredefinedObject("Bouilloire", 3));
        list.add(new PredefinedObject("Tire bouchon", 3));
        list.add(new PredefinedObject("Couverts", 3));
        list.add(new PredefinedObject("Assiettes", 3));
        list.add(new PredefinedObject("Verres", 3));
        list.add(new PredefinedObject("Appareil à raclette", 3));
        list.add(new PredefinedObject("Guirlande", 4));
        list.add(new PredefinedObject("Lampe", 4));
        list.add(new PredefinedObject("Bougies", 4));
        list.add(new PredefinedObject("Guitare", 5));
        list.add(new PredefinedObject("Clavier", 5));
        list.add(new PredefinedObject("Flûte", 5));
        list.add(new PredefinedObject("Violon", 5));
        list.add(new PredefinedObject("Tambour", 5));
        list.add(new PredefinedObject("Pelle", 6));
        list.add(new PredefinedObject("Brouette", 6));
        list.add(new PredefinedObject("Rateau", 6));
        list.add(new PredefinedObject("Tondeuse", 6));
        list.add(new PredefinedObject("Tronconneuse", 6));
        list.add(new PredefinedObject("Taille haie", 6));
        list.add(new PredefinedObject("Bêche", 6));
        list.add(new PredefinedObject("Seau", 6));
        list.add(new PredefinedObject("Arrosoir", 6));
        list.add(new PredefinedObject("DVD", 8));
        list.add(new PredefinedObject("CD", 8));
        list.add(new PredefinedObject("Lecteur DVD", 8));
        list.add(new PredefinedObject("Ordinateur", 8));
        list.add(new PredefinedObject("Enceintes", 8));
        list.add(new PredefinedObject("Télévision", 8));
        list.add(new PredefinedObject("Console de jeux", 8));
        list.add(new PredefinedObject("MP3", 8));
        list.add(new PredefinedObject("Vélo", 9));
        list.add(new PredefinedObject("Trotinette", 9));
        list.add(new PredefinedObject("Rollers", 9));
        list.add(new PredefinedObject("Voiture", 9));
        list.add(new PredefinedObject("Skate", 9));
        list.add(new PredefinedObject("Skis", 9));
        list.add(new PredefinedObject("Luge", 9));
        list.add(new PredefinedObject("Patin", 9));
        list.add(new PredefinedObject("Canoë", 9));
        list.add(new PredefinedObject("Catamaran", 9));
        list.add(new PredefinedObject("Yacht", 9));
        list.add(new PredefinedObject("Jet privé", 9));
        list.add(new PredefinedObject("Veste", 10));
        list.add(new PredefinedObject("Costume", 10));
        list.add(new PredefinedObject("Chaussures", 10));
        list.add(new PredefinedObject("Déguisement", 10));
        list.add(new PredefinedObject("Pantalon", 10));
        list.add(new PredefinedObject("T Shirt", 10));
        list.add(new PredefinedObject("Pull", 10));
        list.add(new PredefinedObject("Robe", 10));
        list.add(new PredefinedObject("Robe de marriée", 10));
        list.add(new PredefinedObject("Jupe", 10));
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
