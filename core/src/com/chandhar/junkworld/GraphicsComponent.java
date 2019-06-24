package com.chandhar.junkworld;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.maps.tiled.TiledMapTile;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TiledMapTileSet;
import com.badlogic.gdx.maps.tiled.tiles.AnimatedTiledMapTile;
import com.badlogic.gdx.maps.tiled.tiles.StaticTiledMapTile;
import com.badlogic.gdx.math.GridPoint2;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Json;
import java.util.Hashtable;

public abstract class GraphicsComponent implements Component
{
    protected TextureRegion currentFrame = null;
    protected float frameTime = 0f;
    protected Entity.State currentState;
    protected Entity.State previousState;
    protected Entity.Motion currentMotion;
    protected Entity.Direction currentDirection;
    protected Vector2 currentPosition;
    protected Vector2 nextPosition;
    protected Vector2 currentPositionID;
    protected Vector2 tileVector;
    protected TiledMapTileLayer.Cell tileCell;
    protected Json json;
    protected Hashtable<Entity.AnimationType, Animation<TextureRegion>> animations;
    protected ShapeRenderer shapeRenderer;
    protected boolean animationFinished;

    protected GraphicsComponent()
    {
        currentPosition = new Vector2(0,0);
        currentState = Entity.State.WALKING;
        currentDirection = Entity.Direction.DOWN;
        json = new Json();
        animations = new Hashtable<>();
        shapeRenderer = new ShapeRenderer();
    }

    public abstract void update(Entity entity, MapManager map, Batch batch, float delta);


    protected void updateAnimations(float delta)
    {
        animationFinished = false;
        //frameTime = (frameTime + delta) %5; //avoid overflow?
        frameTime = (frameTime  + delta) %11;
        //look into appropriate variable when changing position
        switch(currentDirection)
        {
            case DOWN:
                if(currentState == Entity.State.WALKING)
                {
                    Animation<TextureRegion> animation = animations.get(Entity.AnimationType.WALK_DOWN);
                    if(animation == null)
                    {return;}
                    currentFrame = animation.getKeyFrame(frameTime);

                }
                else if(currentState == Entity.State.IDLE)
                {
                    Animation<TextureRegion> animation = animations.get(Entity.AnimationType.WALK_DOWN);
                    if(animation == null)
                    {return;}
                    currentFrame = animation.getKeyFrames()[0];
                }
                else if(currentState == Entity.State.MINING)
                {
                    Animation<TextureRegion> animation = animations.get(Entity.AnimationType.MINE_DOWN);
                    if(animation == null)
                    {return;}
                    currentFrame = animation.getKeyFrame(frameTime);
                    if(animation.isAnimationFinished(frameTime))
                    {
                        animationFinished = true;
                    }
                }
                else if(currentState == Entity.State.IMMOBILE)
                {
                    Animation<TextureRegion> animation = animations.get(Entity.AnimationType.IMMOBILE);
                    if(animation == null)
                    {return;}
                    currentFrame = animation.getKeyFrame(frameTime);
                }
                break;

            case LEFT:
                if(currentState == Entity.State.WALKING)
                {
                    Animation<TextureRegion> animation = animations.get(Entity.AnimationType.WALK_LEFT);
                    if(animation == null)
                    {return;}
                    currentFrame = animation.getKeyFrame(frameTime);
                }
                else if(currentState == Entity.State.IDLE)
                {
                    Animation<TextureRegion> animation = animations.get(Entity.AnimationType.WALK_LEFT);
                    if(animation == null)
                    {return;}
                    currentFrame = animation.getKeyFrames()[0];
                }
                else if(currentState == Entity.State.MINING)
                {
                    Animation<TextureRegion> animation = animations.get(Entity.AnimationType.MINE_LEFT);
                    if(animation == null)
                    {return;}
                    currentFrame = animation.getKeyFrame(frameTime);
                    if(animation.isAnimationFinished(frameTime))
                    {
                        animationFinished = true;
                    }
                }
                else if(currentState == Entity.State.IMMOBILE)
                {
                    Animation<TextureRegion> animation = animations.get(Entity.AnimationType.IMMOBILE);
                    if(animation == null)
                    {return;}
                    currentFrame = animation.getKeyFrame(frameTime);
                }
                break;

            case UP:
                if(currentState == Entity.State.WALKING)
                {
                    Animation<TextureRegion> animation = animations.get(Entity.AnimationType.WALK_UP);
                    if(animation == null)
                    {return;}
                    currentFrame = animation.getKeyFrame(frameTime);
                }
                else if(currentState == Entity.State.IDLE)
                {
                    Animation<TextureRegion> animation = animations.get(Entity.AnimationType.WALK_UP);
                    if(animation == null)
                    {return;}
                    currentFrame = animation.getKeyFrames()[0];
                }
                else if(currentState == Entity.State.MINING)
                {
                    Animation<TextureRegion> animation = animations.get(Entity.AnimationType.MINE_UP);
                    if(animation == null)
                    {return;}
                    currentFrame = animation.getKeyFrame(frameTime);
                    if(animation.isAnimationFinished(frameTime))
                    {
                        animationFinished = true;
                    }
                }
                else if(currentState == Entity.State.IMMOBILE)
                {
                    Animation<TextureRegion> animation = animations.get(Entity.AnimationType.IMMOBILE);
                    if(animation == null)
                    {return;}
                    currentFrame = animation.getKeyFrame(frameTime);
                }
                break;

            case RIGHT:
                if(currentState == Entity.State.WALKING)
                {
                    Animation<TextureRegion> animation = animations.get(Entity.AnimationType.WALK_RIGHT);
                    if(animation == null)
                    {return;}
                    currentFrame = animation.getKeyFrame(frameTime);
                }
                else if(currentState == Entity.State.IDLE)
                {
                    Animation<TextureRegion> animation = animations.get(Entity.AnimationType.WALK_RIGHT);
                    if(animation == null)
                    {return;}
                    currentFrame = animation.getKeyFrames()[0];
                }
                else if(currentState == Entity.State.MINING)
                {
                    Animation<TextureRegion> animation = animations.get(Entity.AnimationType.MINE_RIGHT);
                    if(animation == null)
                    {return;}
                    currentFrame = animation.getKeyFrame(frameTime);
                    if(animation.isAnimationFinished(frameTime))
                    {
                        animationFinished = true;
                    }
                }
                else if(currentState == Entity.State.IMMOBILE)
                {
                    Animation<TextureRegion> animation = animations.get(Entity.AnimationType.IMMOBILE);
                    if(animation == null)
                    {return;}
                    currentFrame = animation.getKeyFrame(frameTime);
                }
                break;

            default:
                break;
        }
    }



    //specific to two frame animations where each frame is stored in a seperate texture
    protected Animation loadAnimation(String firstTexture, String secondTexture, Array<GridPoint2> points, float frameDuration, String animationPlayMode)
    {
        Util.LoadTextureAsset(firstTexture);
        Texture texture1 = Util.GetTextureAsset(firstTexture);
        Util.LoadTextureAsset(secondTexture);
        Texture texture2 = Util.GetTextureAsset(secondTexture);

        TextureRegion[][] texture1Frames = TextureRegion.split(texture1,Entity.FRAME_WIDTH, Entity.FRAME_HEIGHT);
        TextureRegion[][] texture2Frames = TextureRegion.split(texture2,Entity.FRAME_WIDTH, Entity.FRAME_HEIGHT);

        //Array<TextureRegion> animationKeyFrames = new Array<TextureRegion>(2);
        GridPoint2 point = points.first();

        Animation animation = new Animation(frameDuration, texture1Frames[point.x][point.y], texture2Frames[point.x][point.y]);
        //animation.setPlayMode(Animation.PlayMode.LOOP);
        //animation.setPlayMode((Animation.PlayMode)animationPlayMode);

        if(animationPlayMode == "LOOP")
            animation.setPlayMode(Animation.PlayMode.LOOP);
        else
            animation.setPlayMode(Animation.PlayMode.NORMAL);


        //animationKeyFrames.add(texture1Frames[point.x][point.y]);
        //animationKeyFrames.add(texture2Frames[point.x][point.y]);

        return animation;
    }

    protected Animation loadAnimation(String textureName, Array<GridPoint2> points, float frameDuration, String animationPlayMode)
    {
        Util.LoadTextureAsset(textureName);
        Texture texture = Util.GetTextureAsset(textureName);

        TextureRegion[][] textureFrames = TextureRegion.split(texture,  Entity.FRAME_WIDTH, Entity.FRAME_HEIGHT);

        //Array<TextureRegion> animationKeyFrames = new Array<TextureRegion>(points.size);
        TextureRegion[] animationKeyFrames = new TextureRegion[points.size];

        for(int i=0; i < points.size; i++)
        {
            animationKeyFrames[i] = textureFrames[points.get(i).x][points.get(i).y];
        }

        Animation animation = new Animation(frameDuration, (Object[])animationKeyFrames);
        //animation.setPlayMode(Animation.PlayMode.LOOP);
        if(animationPlayMode.equalsIgnoreCase("LOOP"))
            animation.setPlayMode(Animation.PlayMode.LOOP);
        else
            animation.setPlayMode(Animation.PlayMode.NORMAL);

        return animation;
    }

    public TiledMapTileLayer.Cell checkMiningTile(Vector2 tile, Entity entity, MapManager mapMgr)
    {
        TiledMapTileLayer tileLayer = (TiledMapTileLayer)(mapMgr.getCurrentTiledMap().getLayers().get(0));
        System.out.print(tileLayer.getName());
        System.out.print(" "+ tile.x + " " + tile.y + " ");
        TiledMapTileLayer.Cell cell = tileLayer.getCell(Math.round(tile.x), Math.round(tile.y));
        System.out.print("\n");
        System.out.print(cell.getTile().getId());
        System.out.print("\n");
        boolean isMinable = false;
        boolean isMined = true;

        //TiledMapTileSet tileset = mapMgr.getCurrentTiledMap().getTileSets().getTileSet("rocks-dirtstone-all");

        //get the tiles pair (each mineable tile has a mined tile to match
        //TODO IMPLEMENT TRY CATCH AS NOT ALL TILES HAVE IS MINED
        try {
            isMinable = cell.getTile().getProperties().get("isMinable", boolean.class);
            isMined = cell.getTile().getProperties().get("isMined", boolean.class);
            }
        catch (NullPointerException e)
        {
            System.out.print("Tile not minable");
        }
            if (isMinable && !isMined)
            {
                entity.sendMessage(MESSAGE.CURRENT_STATE, json.toJson(Entity.State.MINING));
                return cell;
                //cell.setTile(tileset.getTile(pair + 1));    //pair + 1 for index to account for off by one
            }

            else return cell;
            //entity.sendMessage(MESSAGE.CURRENT_STATE, json.toJson(Entity.State.IDLE));

    }

    public void mineTile(MapManager mapMgr, TiledMapTileLayer.Cell cell)
    {
        TiledMapTileSet tileset = mapMgr.getCurrentTiledMap().getTileSets().getTileSet("JunkTiles0.1");

        int pair = cell.getTile().getProperties().get("PairTile", int.class);

        cell.setTile(tileset.getTile(pair + 1));

    }

    public void tileAnimInit(MapManager mapMgr)
    {
        TiledMapTileSet tileset = mapMgr.getCurrentTiledMap().getTileSets().getTileSet("JunkTiles0.1");
        //TiledMapTileSet tileset = mapMgr.getCurrentTiledMap().getTileSets().getTileSet("TilesetAnimated");

        Array<StaticTiledMapTile> copperTiles = new Array<StaticTiledMapTile>();
        Array<AnimatedTiledMapTile> copperTilesInScene = new Array<AnimatedTiledMapTile>();

        for(TiledMapTile tile: tileset)
        {
            Object property = tile.getProperties().get("CopperOreFrame");
            if(property != null)
            {
                copperTiles.add(new StaticTiledMapTile(tile.getTextureRegion()));
            }
        }

        TiledMapTileLayer tileLayer = (TiledMapTileLayer)(mapMgr.getCurrentTiledMap().getLayers().get(0));
        for(int x = 0; x < tileLayer.getWidth(); x++)
        {
            for(int y = 0; y < tileLayer.getHeight(); y++)
            {
                TiledMapTileLayer.Cell cell = tileLayer.getCell(x,y);
                Object property = cell.getTile().getProperties().get("CopperOreFrame");
                if(property != null)
                {
                    cell.setTile(new AnimatedTiledMapTile(3.0f,copperTiles));
                }
            }
        }
    }
}
