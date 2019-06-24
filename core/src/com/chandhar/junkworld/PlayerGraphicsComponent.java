package com.chandhar.junkworld;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TiledMapTileSet;
import com.badlogic.gdx.math.GridPoint2;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.chandhar.junkworld.EntityConfig.AnimationConfig;

import aurelienribon.tweenengine.Tween;

public class PlayerGraphicsComponent extends GraphicsComponent
{
    private static final String TAG = PlayerGraphicsComponent.class.getSimpleName();
    protected Vector2 previousPosition;
    private Entity player;

    public PlayerGraphicsComponent()
    {
        previousPosition = new Vector2(0,0);
    }

    @Override
    public void receiveMessage(String message)
    {
        String[] string  = message.split(MESSAGE_TOKEN);
        if(string.length == 0)
        {return;}

        //Specifically for messages with 1 object payload
        if(string.length == 2)
        {
            if(string[0].equalsIgnoreCase(MESSAGE.CURRENT_POSITION.toString()))
            {
                currentPosition = json.fromJson(Vector2.class, string[1]);
            }
            else if(string[0].equalsIgnoreCase(MESSAGE.CURRENT_POSITION_ID.toString()))
            {
                currentPositionID = json.fromJson(Vector2.class, string[1]);
            }
            else if(string[0].equalsIgnoreCase(MESSAGE.INIT_START_POSITION.toString()))
            {
                currentPosition = json.fromJson(Vector2.class,string[1]);
            }
            else if(string[0].equalsIgnoreCase(MESSAGE.CURRENT_STATE.toString()))
            {
                previousState = currentState;
                currentState = json.fromJson(Entity.State.class, string[1]);
                //TODO change this to implement a statechanged check
                //TODO if previousState != current state then frametime = 0f;
                if(previousState!=currentState)
                {
                    frameTime = 0f;
                }
            }
            else if(string[0].equalsIgnoreCase(MESSAGE.CURRENT_MOTION.toString()))
            {
                currentMotion = json.fromJson(Entity.Motion.class, string[1]);
            }
            else if(string[0].equalsIgnoreCase(MESSAGE.CURRENT_DIRECTION.toString()))
            {
                currentDirection = json.fromJson(Entity.Direction.class, string[1]);
            }
            else if(string[0].equalsIgnoreCase(MESSAGE.NEXT_POSITION.toString()))
            {
                nextPosition = json.fromJson(Vector2.class, string[1]);
            }
            else if (string[0].equalsIgnoreCase(MESSAGE.MINING_GRAPHICS.toString())) {
                tileVector = json.fromJson(Vector2.class, string[1]);
            }
            else if(string[0].equalsIgnoreCase(MESSAGE.LOAD_ANIMATIONS.toString()))
            {
                EntityConfig entityConfig = json.fromJson(EntityConfig.class, string[1]);
                Array<AnimationConfig> animationConfigs = entityConfig.getAnimationConfig();

                for(AnimationConfig animationConfig: animationConfigs)
                {
                    Array<String> textureNames = animationConfig.getTexturePaths();
                    Array<GridPoint2> points = animationConfig.getGridPoints();
                    Entity.AnimationType animationType = animationConfig.getAnimationType();
                    float frameDuration = animationConfig.getFrameDuration();

                    String animationPlayMode = animationConfig.getAnimationPlayMode();

                    Animation animation = null;

                    if(textureNames.size == 1)
                    {
                        animation = loadAnimation(textureNames.get(0), points, frameDuration, animationPlayMode);
                    }

                    else if(textureNames.size == 2)
                    {
                        animation = loadAnimation(textureNames.get(1), points, frameDuration,animationPlayMode);
                    }

                    animations.put(animationType, animation);
                }
            }
        }
    }


    @Override
    public void update(Entity entity, MapManager mapMgr, Batch batch, float delta)
    {
        updateAnimations(delta);
        //tileAnimInit(mapMgr);
/*
        if(currentState == Entity.State.IDLE || currentState == Entity.State.MINING)
        {
            frameTime = 0f;
        }
*/
        if(tileVector!=null)
            tileCell = checkMiningTile(tileVector, entity, mapMgr);

        if(animationFinished)
        {

            mineTile(mapMgr, tileCell);
            entity.sendMessage(MESSAGE.CURRENT_STATE, json.toJson(Entity.State.IDLE));
            animationFinished = false;
        }

        Camera camera = mapMgr.getCamera();
        camera.position.set(currentPosition.x, currentPosition.y, 0f);
        camera.update();
        batch.begin();
        batch.draw(currentFrame, currentPosition.x, currentPosition.y,1,1);
        batch.end();
    }


    @Override
    public void dispose()
    {}


}
