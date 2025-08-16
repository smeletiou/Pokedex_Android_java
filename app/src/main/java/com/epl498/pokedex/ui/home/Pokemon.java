package com.epl498.pokedex.ui.home;

import java.util.List;

public class Pokemon {

    private String name;
    private int id;
    private List<String> types;
    private List<String> abilities;
    private List<String> heldItems;
    private List<String> moves;
    private int weight;
    private int height;
    private int baseExperience;

    private String imageFront; // For regular front image (used in HomeFragment)
    private String frontGifUrl;
    private String frontShinyGifUrl;
    private String backGifUrl;
    private String backShinyGifUrl;

    public Pokemon(String name, int id, List<String> types, List<String> abilities, List<String> heldItems,
                   List<String> moves, int weight, int height, int baseExperience, String imageFront,
                   String frontGifUrl, String frontShinyGifUrl, String backGifUrl, String backShinyGifUrl) {
        this.name = name;
        this.id = id;
        this.types = types;
        this.abilities = abilities;
        this.heldItems = heldItems;
        this.moves = moves;
        this.weight = weight;
        this.height = height;
        this.baseExperience = baseExperience;
        this.imageFront = imageFront;
        this.frontGifUrl = frontGifUrl;
        this.frontShinyGifUrl = frontShinyGifUrl;
        this.backGifUrl = backGifUrl;
        this.backShinyGifUrl = backShinyGifUrl;
    }

    public String getName() { return name; }
    public int getId() { return id; }
    public String getTypesString() { return String.join(", ", types); }
    public List<String> getAbilities() { return abilities; }
    public List<String> getHeldItems() { return heldItems; }
    public List<String> getMoves() { return moves; }
    public int getWeight() { return weight; }
    public int getHeight() { return height; }
    public int getBaseExperience() { return baseExperience; }

    public String getImageFront() { return imageFront; }
    public String getFrontGifUrl() { return frontGifUrl; }
    public String getFrontShinyGifUrl() { return frontShinyGifUrl; }
    public String getBackGifUrl() { return backGifUrl; }
    public String getBackShinyGifUrl() { return backShinyGifUrl; }
}
