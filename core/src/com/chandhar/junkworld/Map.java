package com.chandhar.junkworld;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.*;
import java.util.Hashtable;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Json;

public class Map
{
    private static final String TAG = Map.class.getSimpleName();

    //Map Layers
    private static final String COLLISION_LAYER = "MAP_COLLISION_LAYER";
    private static final String SPAWNS_LAYER = "MAP_SPAWNS_LAYER";
    private static final String PORTAL_LAYER = "MAP_PORTAL_LAYER";

    //Start locations
    private static final String PLAYER_START = "PLAYER_START";
    private static final String NPC_START = "NPC_START";

    protected Json json;

    private Vector2 playerStart;
    protected Array<Vector2> npcStartPositions;
    protected Hashtable<String, Vector2> specialNPCStartPositions;
    private Vector2 playerStartPositionRect;
    private Vector2 closestPlayerStartPosition;
    private Vector2 convertedUnits;

    private TiledMap currentMap = null;

    private MapLayer collisionLayer = null;
    private MapLayer portalLayer = null;
    private MapLayer spawnsLayer = null;

    public final static float UNIT_SCALE = 1/32f;

    protected MapFactory.MapType currentMapType;
    protected Array<Entity> mapEntities;

    public Map(MapFactory.MapType mapType, String fullMapPath)
    {
        json = new Json();
        mapEntities = new Array<Entity>(10);
        currentMapType = mapType;
        playerStart = new Vector2(0,0);
        playerStartPositionRect = new Vector2(0,0);
        closestPlayerStartPosition = new Vector2(0,0);
        convertedUnits = new Vector2(0,0);

        if(fullMapPath == null || fullMapPath.isEmpty())
        {
            Gdx.app.debug(TAG, "Map is invalid");
            return;
        }

        Util.LoadMapAsset(fullMapPath);

        if(Util.IsAssetLoaded(fullMapPath))
        {
            currentMap = Util.getMapAsset(fullMapPath);
        }
        else
        {
            Gdx.app.debug(TAG, "Map not loaded");
            return;
        }

        collisionLayer = currentMap.getLayers().get(COLLISION_LAYER);
        if(collisionLayer == null)
        {
            Gdx.app.debug(TAG, "No collision layer");
        }

        portalLayer = currentMap.getLayers().get(PORTAL_LAYER);
        if(portalLayer == null)
        {
            Gdx.app.debug(TAG, "No portal layer");
        }

        spawnsLayer = currentMap.getLayers().get(SPAWNS_LAYER);
        if(spawnsLayer == null)
        {
            Gdx.app.debug(TAG, "No spawn layer");
        }

        else {
            setClosestStartPosition(playerStart);
        }
    }

    public Array<Entity> getMapEntities() {
        return mapEntities;
    }

    public MapFactory.MapType getCurrentMapType() {
        return currentMapType;
    }

    public Vector2 getPlayerStart() {
        return playerStart;
    }

    public void setPlayerStart(Vector2 playerStart) {
        this.playerStart = playerStart;
    }

    public void updateMapEntities(MapManager mapMgr, Batch batch, float delta)
    {
        for(int i = 0; i < mapEntities.size; i++)
        {
            mapEntities.get(i).update(mapMgr,batch,delta);
        }
    }

    protected void dispose() {
        for (int i = 0; i < mapEntities.size; i++) {
            mapEntities.get(i).dispose();
        }
    }


    public MapLayer getCollisionLayer()
    {
        return collisionLayer;
    }
    public MapLayer getPortalLayer() {return portalLayer;}
    public MapLayer getSpawnsLayer(){return spawnsLayer;}
    public TiledMap getCurrentTiledMap(){return currentMap;}


    public Vector2 getPlayerStartUnitScaled()
    {
        Vector2 _playerStart = playerStart.cpy();
        //_playerStart.set(playerStart.x * UNIT_SCALE, playerStart.y * UNIT_SCALE);
        _playerStart.set((MathUtils.floor(playerStart.x * UNIT_SCALE)),(MathUtils.floor(playerStart.y * UNIT_SCALE)));
        return _playerStart;
    }

    private Array<Vector2> getNpcStartPositions()
    {
        Array<Vector2> npcStartPositions = new Array<Vector2>();

        for(MapObject object : spawnsLayer.getObjects())
        {
            String objectName = object.getName();
            if(objectName == null || objectName.isEmpty())
            {
                continue;
            }

            if(objectName.equalsIgnoreCase(NPC_START))
            {
                float x = ((RectangleMapObject)object).getRectangle().getX();
                float y = ((RectangleMapObject)object).getRectangle().getY();

                x *= UNIT_SCALE;
                y *= UNIT_SCALE;

                npcStartPositions.add(new Vector2(x,y));
            }
        }
        return npcStartPositions;
    }

    private Hashtable<String, Vector2> getSpecialNPCStartPositions()
    {
        Hashtable<String, Vector2> specialNPCStartPositions = new Hashtable<String, Vector2>();

        for(MapObject object : spawnsLayer.getObjects())
        {
            String objectName = object.getName();
            if(objectName == null || objectName.isEmpty())
            {
                continue;
            }

            if(objectName.equalsIgnoreCase(NPC_START) || objectName.equalsIgnoreCase(PLAYER_START))
            {
                continue;
            }

            float x = ((RectangleMapObject)object).getRectangle().getX();
            float y = ((RectangleMapObject)object).getRectangle().getY();

            x *= UNIT_SCALE;
            y *= UNIT_SCALE;

            specialNPCStartPositions.put(objectName, new Vector2(x,y));
        }

        return specialNPCStartPositions;
    }

    private void setClosestStartPosition(final Vector2 position)
    {
        //Get last known position on this map
        playerStartPositionRect.set(0,0);
        closestPlayerStartPosition.set(0,0);
        float shortestDistance = 0f;

        //go through all player start positions and choose closest to last known

        for(MapObject object: spawnsLayer.getObjects())
        {
            String objectName = object.getName();

            if( objectName == null || objectName.isEmpty() ){
                continue;
            }

            if(object.getName().equalsIgnoreCase(PLAYER_START))
            {
                ((RectangleMapObject)object).getRectangle().getPosition(playerStartPositionRect);
                float distance = position.dst2(playerStartPositionRect);
                Gdx.app.debug(TAG, "DISTANCE: " + distance + " for " + currentMapType.toString());

                if(distance < shortestDistance || shortestDistance == 0)
                {
                    closestPlayerStartPosition.set(playerStartPositionRect);
                    shortestDistance = distance;
                    Gdx.app.debug(TAG, "closest START is: (" + closestPlayerStartPosition.x + "," + closestPlayerStartPosition.y + ") " +  currentMapType.toString());
                }
            }
        }
        playerStart = closestPlayerStartPosition.cpy();
    }

    public void setClosestStartPositionFromScaledUnits(Vector2 position)
    {
        if(UNIT_SCALE <= 0)
            return;

        convertedUnits.set(position.x/UNIT_SCALE, position.y/UNIT_SCALE);
        setClosestStartPosition(convertedUnits);
    }


}
