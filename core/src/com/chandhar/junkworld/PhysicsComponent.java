package com.chandhar.junkworld;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.collision.Ray;

import org.omg.Messaging.SYNC_WITH_TRANSPORT;

import aurelienribon.tweenengine.Tween;
import aurelienribon.tweenengine.TweenManager;


public abstract class PhysicsComponent implements Component
{
    private static final String TAG = PhysicsComponent.class.getSimpleName();

    public abstract void update(Entity entity, MapManager mapMgr, float delta);

    protected Vector2 nextEntityPosition;
    protected Vector2 nextEntityPositionID;
    protected Vector2 currentEntityPosition;
    protected Vector2 currentEntityPositionID;
    protected Entity.Direction currentDirection;
    protected Json json;
    protected Vector2 velocity;
    //protected boolean nextPositionReached = true;


    //may not be needed
    public Rectangle boundingBox;
    protected BoundingBoxLocation boundingBoxLocation;
    protected Ray selectionRay;
    protected final float selectRayMaximumDistance = 32.0f;


    public static enum BoundingBoxLocation
    {
        BOTTOM_LEFT,
        BOTTOM_CENTER,
        CENTER,
    }

    PhysicsComponent()
    {
        this.nextEntityPosition = new Vector2();
        this.currentEntityPosition = new Vector2();
        this.nextEntityPositionID = new Vector2();
        this.currentEntityPositionID = new Vector2();
        this.boundingBox = new Rectangle();
        this.velocity = new Vector2(2f,2f);
        this.json = new Json();
        //this.tempEntities = new Array<Entity>();
        boundingBoxLocation = BoundingBoxLocation.BOTTOM_LEFT;
    }

    public boolean isCollision(Entity entity, MapManager map)
    {
        TiledMapTileLayer tile = (TiledMapTileLayer)map.getCurrentTiledMap().getLayers().get(0);
        //passing in this might not work properly!!!
        int mapHeight = tile.getHeight();
        int mapWidth = tile.getWidth();
        int i = 0;
        int j = 0;
        //by default set tile to round to nearest tile
        switch (entity.getCurrentDirection())
        {
            case LEFT:
                i = MathUtils.round(this.nextEntityPosition.x);
                j = MathUtils.round(this.nextEntityPosition.y);
                break;
            case RIGHT:
                i = MathUtils.round(this.nextEntityPosition.x);
                j = MathUtils.round(this.nextEntityPosition.y);
                break;
            case UP:
                i = MathUtils.round(this.nextEntityPosition.x);
                j = MathUtils.round(this.nextEntityPosition.y);
                break;
            case DOWN:
                i = MathUtils.round(this.nextEntityPosition.x);
                j = MathUtils.round(this.nextEntityPosition.y);
                break;
            default:
                break;

        }
        //if the player is going to go off the edge of the map round the next player position up or down to prevent it.
        if(this.nextEntityPosition.x < 0) {i = MathUtils.floor(this.nextEntityPosition.x); }
        if(this.nextEntityPosition.x > mapWidth-1) {i = MathUtils.ceil(this.nextEntityPosition.x);}
        if(this.nextEntityPosition.y < 0) {j = MathUtils.floor(this.nextEntityPosition.y);}
        if(this.nextEntityPosition.y > mapHeight-1) {j = MathUtils.ceil(this.nextEntityPosition.y);}


        if( i >= 0 && i < mapWidth)
        {
            if(j >= 0 && j < mapHeight)
            {
                //now that bounds have been checked, check if the tile is solid or not.
                TiledMapTileLayer.Cell cell = tile.getCell(i, j);

                //System.out.print(cell.getTile().getProperties().get("Name"));
                if(cell.getTile() != null) {
                    boolean isSolid = (Boolean) cell.getTile().getProperties().get("isSolid");
                    //String id = (String)cell.getTile().getProperties().get("ID");
                    if (!isSolid)
                        return false;
                }
            }
        }
        //player has collided to stop movement
        //entity.sendMessage(MESSAGE.CURRENT_MOTION, json.toJson(Entity.Motion.STANDING));
        return true;
    }

    public boolean isCollisionOLD(Entity entity, MapManager map)
    {
        TiledMapTileLayer tile = (TiledMapTileLayer)map.getCurrentTiledMap().getLayers().get(0);
        //passing in this might not work properly!!!
        int mapHeight = tile.getHeight();
        int mapWidth = tile.getWidth();
        //by default set tile to round to nearest tile
        int i = MathUtils.round(this.nextEntityPosition.x);
        int j = MathUtils.round(this.nextEntityPosition.y);

        //if the player is going to go off the edge of the map round the next player position up or down to prevent it.
        if(this.nextEntityPosition.x < 0) {i = MathUtils.floor(this.nextEntityPosition.x); }
        if(this.nextEntityPosition.x > mapWidth-1) {i = MathUtils.ceil(this.nextEntityPosition.x);}
        if(this.nextEntityPosition.y < 0) {j = MathUtils.floor(this.nextEntityPosition.y);}
        if(this.nextEntityPosition.y > mapHeight-1) {j = MathUtils.ceil(this.nextEntityPosition.y);}

        if( i >= 0 && i < mapWidth)
        {

            if(j >= 0 && j < mapHeight)
            {
                //now that bounds have been checked, check if the tile is solid or not.
                TiledMapTileLayer.Cell cell = tile.getCell(i, j);
                boolean isSolid = (Boolean)cell.getTile().getProperties().get("isSolid");
                String id = (String)cell.getTile().getProperties().get("ID");
                if(!isSolid)
                    return false;
            }
        }

        return true;
    }

    protected boolean isCollisionWithMapEntities(Entity entity, MapManager mapMgr)
    {
        Array<Entity> entities = mapMgr.getCurrentMapEntities();
        boolean isCollisionWithMapEntities = false;

        for(Entity mapEntity: entities)
        {
            //check against self
            if(mapEntity.equals(entity))
            {
                continue;
            }

            Rectangle targetRect = mapEntity.getCurrentBoundingBox();
            if(boundingBox.overlaps(targetRect))
            {
                //collision
                entity.sendMessage(MESSAGE.COLLISION_WITH_ENTITY);
                isCollisionWithMapEntities = true;
                break;
            }
        }
        return isCollisionWithMapEntities;
    }

    public boolean isCollisionST(Entity entitySource, Entity entityTarget)
    {
        boolean isCollisionWithMapEntities = false;

        if(entitySource.equals(entityTarget))
        {
            return false;
        }

        if(entitySource.getCurrentBoundingBox().overlaps(entityTarget.getCurrentBoundingBox()))
        {
            //collision
            entitySource.sendMessage(MESSAGE.COLLISION_WITH_ENTITY);
            isCollisionWithMapEntities = true;
        }

        return isCollisionWithMapEntities;
    }

    protected boolean isCollisionWithMapLayer(Entity entity, MapManager mapMgr)
    {
        MapLayer mapCollisionLayer = mapMgr.getCollisionLayer();
        if (mapCollisionLayer == null)
        {

            return false;

        }

        Rectangle rectangle = null;

        for(MapObject object: mapCollisionLayer.getObjects())
        {
            if(object instanceof RectangleMapObject)
            {

                rectangle = ((RectangleMapObject)object).getRectangle();
                System.out.print(boundingBox.x);
                if(boundingBox.overlaps(rectangle))
                {

                    entity.sendMessage(MESSAGE.COLLISION_WITH_MAP);
                    return true;
                }
            }
        }
        return false;
    }

    protected void setNextPositionToCurrent(Entity entity, MapManager mapMgr , float delta){
        //only gets called if player isn't going to collide with wall and will move to next tile successfully
        //Entity.Motion.MOVING is an interim state for being between tiles
        //during this state player cannot be idle
        //idle state will be triggered when player stops pressing keys and player has completed movement to tile
        switch(entity.getCurrentDirection())
        {
            case UP:

                if(currentEntityPosition.y < nextEntityPosition.y)
                {
                    entity.sendMessage(MESSAGE.CURRENT_MOTION,json.toJson(Entity.Motion.MOVING));
                    currentEntityPosition.y += 2 * delta;
                    if (currentEntityPosition.y >= nextEntityPosition.y)
                    {
                        currentEntityPosition.y = nextEntityPosition.y;
                        entity.sendMessage(MESSAGE.CURRENT_MOTION,json.toJson(Entity.Motion.STANDING));

                    }
                }

            break;

            case DOWN:

                    if(currentEntityPosition.y > nextEntityPosition.y) {
                        entity.sendMessage(MESSAGE.CURRENT_MOTION,json.toJson(Entity.Motion.MOVING));
                        currentEntityPosition.y -= 2 * delta;
                        if (currentEntityPosition.y <= nextEntityPosition.y) {
                            currentEntityPosition.y = nextEntityPosition.y;
                            entity.sendMessage(MESSAGE.CURRENT_MOTION,json.toJson(Entity.Motion.STANDING));
                            //entity.sendMessage(MESSAGE.CURRENT_STATE, json.toJson(Entity.State.IDLE));
                        }
                    }
                break;

            case LEFT:

                    if(currentEntityPosition.x > nextEntityPosition.x) {
                        entity.sendMessage(MESSAGE.CURRENT_MOTION,json.toJson(Entity.Motion.MOVING));
                        currentEntityPosition.x -= 2 * delta;
                        if (currentEntityPosition.x <= nextEntityPosition.x) {
                            currentEntityPosition.x = nextEntityPosition.x;
                            entity.sendMessage(MESSAGE.CURRENT_MOTION,json.toJson(Entity.Motion.STANDING));
                            //entity.sendMessage(MESSAGE.CURRENT_STATE, json.toJson(Entity.State.IDLE));
                        }
                    }
                break;

            case RIGHT:

                    if(currentEntityPosition.x < nextEntityPosition.x) {
                        entity.sendMessage(MESSAGE.CURRENT_MOTION,json.toJson(Entity.Motion.MOVING));
                        currentEntityPosition.x += 2 * delta;
                        if (currentEntityPosition.x >= nextEntityPosition.x) {
                            currentEntityPosition.x = nextEntityPosition.x;
                            entity.sendMessage(MESSAGE.CURRENT_MOTION,json.toJson(Entity.Motion.STANDING));
                        }
                    }
                break;
            default:
                break;
        }

        //this.currentEntityPosition.x = nextEntityPosition.x;
        //this.currentEntityPosition.y = nextEntityPosition.y;
        //this.currentEntityPositionID.x = MathUtils.round(currentEntityPosition.x );
        //this.currentEntityPositionID.y = MathUtils.round(currentEntityPosition.y );

        //Gdx.app.debug(TAG, "SETTING Current Position " + entity.getEntityConfig().getEntityID() + ": (" + currentEntityPosition.x + "," + currentEntityPosition.y + ")");
        entity.sendMessage(MESSAGE.CURRENT_POSITION, json.toJson(currentEntityPosition));
        entity.sendMessage(MESSAGE.CURRENT_POSITION_ID, json.toJson(currentEntityPositionID));
    }

    public void calculateNextPosition(float deltaTime)
    {
        if(currentDirection == null)
            return;

        if(deltaTime > .7)
            return;

        float testX = currentEntityPosition.x;
        float testY = currentEntityPosition.y;

        velocity.scl(deltaTime);
        switch (currentDirection) {
            case LEFT :
                testX -= 1; // velocity.x;
                break;
            case RIGHT :
                testX += 1; //velocity.x;
                break;
            case UP :
                testY += 1; //velocity.y;
                break;
            case DOWN :
                testY -= 1; // velocity.y;
            default:
                break;
        }


        nextEntityPosition.x = testX;
        nextEntityPosition.y = testY;
        nextEntityPositionID.x = MathUtils.round(nextEntityPosition.x);
        nextEntityPositionID.y = MathUtils.round(nextEntityPosition.y);


        //velocity
        velocity.scl(1/deltaTime);
    }

    public Vector2 getFacingTile(Entity player, MapManager mapMgr) {
        Vector2 position = new Vector2(currentEntityPosition.x, currentEntityPosition.y);
        Entity.Direction direction = player.getCurrentDirection();

        TiledMapTileLayer tileLayer = (TiledMapTileLayer) (mapMgr.getCurrentTiledMap().getLayers().get(0));
        //TiledMapTileLayer.Cell cell = tileLayer.getCell(Math.round(currentEntityPosition.x), Math.round(currentEntityPosition.y));
        //TiledMapTileSet tileset = mapMgr.getCurrentTiledMap().getTileSets().getTileSet("rocks-dirtstone-all");

        //use direction to set the cell to be mined equal to the tile opposite the one to the player
        //if  player is looking at the edge of the map do nothing.

        if (direction == Entity.Direction.LEFT && currentEntityPosition.x > 0) {
            position.x = currentEntityPosition.x - 1;
            position.y = currentEntityPosition.y;
            //cell = tileLayer.getCell(Math.round(currentEntityPosition.x - 1), Math.round(currentEntityPosition.y));
            return position;
        }

        if (direction == Entity.Direction.RIGHT && currentEntityPosition.x < tileLayer.getWidth() - 1) {
            //cell = tileLayer.getCell(Math.round(currentEntityPosition.x + 1), Math.round(currentEntityPosition.y));
            position.x = currentEntityPosition.x + 1;
            position.y = currentEntityPosition.y;
            return position;
        }

        if (direction == Entity.Direction.UP && currentEntityPosition.y < tileLayer.getHeight() - 1) {
            //cell = tileLayer.getCell(Math.round(currentEntityPosition.x), Math.round(currentEntityPosition.y + 1));
            position.x = currentEntityPosition.x;
            position.y = currentEntityPosition.y + 1;
            return position;
        }

        if (direction == Entity.Direction.DOWN && currentEntityPosition.y > 0) {
            //cell = tileLayer.getCell(Math.round(currentEntityPosition.x), Math.round(currentEntityPosition.y) - 1);
            position.x = currentEntityPosition.x;
            position.y = currentEntityPosition.y - 1;
            return position;
        }

        /*
        //get the tiles pair (each mineable tile has a mined tile to match
        int pair = cell.getTile().getProperties().get("PairTile", int.class);
        boolean isMinable = cell.getTile().getProperties().get("isMinable", boolean.class);
        boolean isMined = cell.getTile().getProperties().get("isMined", boolean.class);

        //TODO mines state is seperated from mine action change it so that state is changed once mine method is called.
        if (isMinable && !isMined) {
            player.sendMessage(MESSAGE.CURRENT_STATE, json.toJson(Entity.State.MINING));


            //TODO find a way to call PlayerGraphics component to run mining animation

            cell.setTile(tileset.getTile(pair + 1));    //pair + 1 for index to account for off by one
        }

        player.sendMessage(MESSAGE.CURRENT_STATE, json.toJson(Entity.State.IDLE));
        */

        return position;
    }

    public void initBoundingBox(float percentageWidthReduced, float percentageHeightReduced)
    {
        //update current bounding box
        float width;
        float height;

        float origWidth = Entity.FRAME_WIDTH;
        float origHeight = Entity.FRAME_HEIGHT;

        float widthReductionAmount = 1.0f - percentageWidthReduced;
        //.8f for 20% (1-.20)
        float heightReductionAmount = 1.0f - percentageHeightReduced;
        //.8f for 20% (1-.20)

        if(widthReductionAmount > 0 && widthReductionAmount < 1)
        {
            width = Entity.FRAME_WIDTH * widthReductionAmount;
        }
        else
            width = Entity.FRAME_WIDTH;

        if(heightReductionAmount > 0 && heightReductionAmount < 1)
        {
            height = Entity.FRAME_HEIGHT * heightReductionAmount;
        }
        else
            height = Entity.FRAME_HEIGHT;

        if( width == 0 || height == 0)
            Gdx.app.debug(TAG,"Width and Height are 0!! " + width +":" + height);

        //Need to account for the unitscale, since the map coordinates
        //will be in pixels
        float minX;
        float minY;
        if( Map.UNIT_SCALE > 0 ) {
            minX = nextEntityPosition.x / Map.UNIT_SCALE;
            minY = nextEntityPosition.y / Map.UNIT_SCALE;
        } else{
            minX = nextEntityPosition.x;
            minY = nextEntityPosition.y;
        }
        boundingBox.setWidth(width);
        boundingBox.setHeight(height);

        switch(boundingBoxLocation)
        {
            case BOTTOM_LEFT:
                boundingBox.set(minX, minY, width, height);
                break;
            case BOTTOM_CENTER:
                boundingBox.setCenter(minX + origWidth/2, minY + origHeight/4);
                break;
            case CENTER:
                boundingBox.setCenter(minX + origHeight/2, minY + origHeight/2);
                break;
        }

    }

    protected void updateBoundingBoxPosition(Vector2 position)
    {
        //need to account for unit scale since map coords are in pixels
        float minX;
        float minY;

        if(Map.UNIT_SCALE > 0)
        {
            minX = position.x / Map.UNIT_SCALE;
            minY = position.y / Map.UNIT_SCALE;
        }
        else
        {
            minX = position.x;
            minY  = position.y;
        }

        switch (boundingBoxLocation)
        {
            case BOTTOM_LEFT:
                boundingBox.set(minX, minY, boundingBox.getWidth(), boundingBox.getHeight());
                break;
            case BOTTOM_CENTER:
                boundingBox.setCenter(minX + Entity.FRAME_WIDTH/2,minY + Entity.FRAME_HEIGHT/4);
                break;
            case CENTER:
                boundingBox.setCenter(minX + Entity.FRAME_WIDTH/2, minY + Entity.FRAME_HEIGHT/2);
                break;
        }
    }
}
