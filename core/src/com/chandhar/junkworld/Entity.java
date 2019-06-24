package com.chandhar.junkworld;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TiledMapTileSet;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.JsonValue;
import java.util.ArrayList;

import aurelienribon.tweenengine.Tween;
import aurelienribon.tweenengine.TweenManager;

public class Entity
{
    private static final String TAG = Entity.class.getSimpleName();

    private Json json;
    private EntityConfig entityConfig;

    public static enum Direction
    {
        UP,
        RIGHT,
        DOWN,
        LEFT;

        static public Direction getRandomNext() {
            return Direction.values()[MathUtils.random(Direction.values().length - 1)];
        }

        public Direction getOpposite() {
            if( this == LEFT){
                return RIGHT;
            }else if( this == RIGHT){
                return LEFT;
            }else if( this == UP){
                return DOWN;
            }else{
                return UP;
            }
        }
    }
    public static enum State
    {
        IDLE,
        WALKING,
        MINING,
        IMMOBILE; //this should always be last

        static public State getRandomNext() {
            //Ignore IMMOBILE which should be last state
            return State.values()[MathUtils.random(State.values().length - 2)];
        }
    }

    public static enum Motion
    {
        MOVING,
        STANDING;
    }
    public static enum AnimationType
    {
        WALK_LEFT,
        WALK_RIGHT,
        WALK_UP,
        WALK_DOWN,
        MINE_RIGHT,
        MINE_LEFT,
        MINE_UP,
        MINE_DOWN,
        IDLE,
        IMMOBILE
    }

    public static final int FRAME_WIDTH = 32;
    public static final int FRAME_HEIGHT = 32;
    public TweenManager tweenManager;
    private static final int MAX_COMPONENTS =5;
    private Array<Component> components;
    private InputComponent inputComponent;
    private GraphicsComponent graphicsComponent;
    private PhysicsComponent physicsComponent;

    public Entity(InputComponent _inputComponent, PhysicsComponent _physicsComponent, GraphicsComponent _graphicsComponent)
    {
        entityConfig = new EntityConfig();
        json = new Json();
        components = new Array<Component>(MAX_COMPONENTS);
        tweenManager = new TweenManager();
        Tween.registerAccessor(Vector2.class, new Vector2Accessor());
        Tween.registerAccessor(float.class, new FloatAccessor());
        inputComponent = _inputComponent;
        physicsComponent = _physicsComponent;
        graphicsComponent = _graphicsComponent;

        components.add(inputComponent);
        components.add(physicsComponent);
        components.add(graphicsComponent);
    }
    public void setEntityConfig(EntityConfig entityConfig)
    {
        this.entityConfig = entityConfig;
    }

    public EntityConfig getEntityConfig() {
        return entityConfig;
    }

    static public EntityConfig getEntityConfig(String configFilePath)
    {
        Json json = new Json();
        return json.fromJson(EntityConfig.class, Gdx.files.internal(configFilePath));
    }

    public InputProcessor getInputProcessor()
    {
        return inputComponent;
    }

    static public Array<EntityConfig> getEntityConfigs(String configFilePath)
    {
        Json json = new Json();
        Array<EntityConfig> configs = new Array<EntityConfig>();
        ArrayList<JsonValue> list = json.fromJson(ArrayList.class,Gdx.files.internal(configFilePath));

        for(JsonValue jsonVal : list)
        {
            configs.add(json.readValue(EntityConfig.class, jsonVal));
        }

        return configs;

    }

    public void sendMessage(Component.MESSAGE messageType, String ... args)
    {
        String fullMessage = messageType.toString();

        for(String string : args)
        {
            fullMessage += Component.MESSAGE_TOKEN + string;
        }

        for(Component component: components)
        {
            component.receiveMessage(fullMessage);
        }
    }

    public void update(MapManager mapMgr, Batch batch, float delta)
    {
        inputComponent.update(this,delta);
        physicsComponent.update(this, mapMgr, delta);
        graphicsComponent.update(this, mapMgr, batch, delta);


    }

    public Rectangle getCurrentBoundingBox()
    {
        return physicsComponent.boundingBox;
    }

    public Vector2 getCurrentPosition()
    {
        return physicsComponent.currentEntityPosition;
    }

    public Vector2 getCurrentPositionID()
    {
        return physicsComponent.currentEntityPositionID;
    }

    public Direction getCurrentDirection()
    {
        return physicsComponent.currentDirection;
    }

    public State getCurrentState()
    {
        return graphicsComponent.currentState;
    }


    public void dispose()
    {
        for(Component component: components)
        {
            component.dispose();
        }
    }

}
