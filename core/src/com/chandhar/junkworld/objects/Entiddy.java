package com.chandhar.junkworld.objects;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;

public class Entiddy
{
    public Vector2 position;
    public Vector2 dimension;
    public Vector2 origin;
    public Vector2 scale;
    public float rotation;
    public Texture texture;


    public Entiddy(String textureLocation){
        texture = new Texture(Gdx.files.internal(textureLocation));
        position = new Vector2();
        dimension = new Vector2(texture.getWidth(),texture.getHeight()); //width and height of the object
        origin = new Vector2(dimension.x/2,dimension.y/2);
        scale = new Vector2(1,1);
        rotation = 0;

    }





}
