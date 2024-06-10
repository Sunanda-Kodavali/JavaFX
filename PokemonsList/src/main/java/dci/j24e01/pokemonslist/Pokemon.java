package dci.j24e01.pokemonslist;


import javafx.scene.image.Image;

public class Pokemon
{
    private final String name;
    private final int height;
    private final int weight;
    private final Image sprites;

    public Pokemon(String name, int height, int weight, Image sprites) {
        this.name = name;
        this.height = height;
        this.weight = weight;
        this.sprites = sprites;
    }

    public String getName() {
        return name;
    }

    public int getHeight() {
        return height;
    }

    public int getWeight() {
        return weight;
    }

    public Image getSprites() {
        return sprites;
    }
}
