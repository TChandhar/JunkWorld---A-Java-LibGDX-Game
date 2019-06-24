package com.chandhar.junkworld;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMapTile;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TiledMapTileSet;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.math.collision.Ray;

import org.omg.IOP.ENCODING_CDR_ENCAPS;

public class PlayerPhysicsComponent extends PhysicsComponent {
    private static final String TAG = PlayerPhysicsComponent.class.getSimpleName();
    private Entity.State state;
    private Entity.Motion motion = Entity.Motion.STANDING;
    private boolean mine = false;
    private Vector2 tileToMine;
    private boolean isMining = false;
    private Vector3 mouseSelectCoordinates;
    private boolean isMouseSelectEnabled = false;
    private Ray selectionRay;
    private float selectRayMaximumDistance = 32.0f;

    public PlayerPhysicsComponent() {
        mouseSelectCoordinates = new Vector3(0, 0, 0);
        selectionRay = new Ray(new Vector3(), new Vector3());
    }

    @Override
    public void dispose() {

    }

    @Override
    public void receiveMessage(String message) {
        String[] string = message.split(Component.MESSAGE_TOKEN);

        if (string.length == 0) return;

        //messages that are just message indicators with no payload
        if (string.length == 1) {
            if (string[0].equalsIgnoreCase(MESSAGE.MINE.toString())) {
                mine = true;
            }
        }
        //specifically for messages with 1 object payload
        if (string.length == 2) {
            if (string[0].equalsIgnoreCase(MESSAGE.INIT_START_POSITION.toString())) {
                currentEntityPosition = json.fromJson(Vector2.class, string[1]);
                nextEntityPosition.set(currentEntityPosition.x, currentEntityPosition.y);
            } else if (string[0].equalsIgnoreCase(MESSAGE.CURRENT_STATE.toString())) {
                state = json.fromJson(Entity.State.class, string[1]);
            } else if (string[0].equalsIgnoreCase(MESSAGE.CURRENT_MOTION.toString())) {
                motion = json.fromJson(Entity.Motion.class, string[1]);
            } else if (string[0].equalsIgnoreCase(MESSAGE.CURRENT_DIRECTION.toString())) {
                currentDirection = json.fromJson(Entity.Direction.class, string[1]);
            }
            else if (string[0].equalsIgnoreCase(MESSAGE.MINING_PHYSICS.toString()))
            {
                isMining = json.fromJson(Boolean.class, string[1]);
            }
            else if (string[0].equalsIgnoreCase(MESSAGE.INIT_SELECT_ENTITY.toString())) {
                mouseSelectCoordinates = json.fromJson(Vector3.class, string[1]);
                isMouseSelectEnabled = true;
            }
        }
    }


    @Override
    public void update(Entity entity, MapManager mapMgr, float delta) {
        /*
        System.out.print(motion);
        System.out.print(" ");
        System.out.print(state);
        System.out.print("\n");
        */

        //we want hitbox to be at feet for a better feel

        updateBoundingBoxPosition(nextEntityPosition);
        updatePortalLayerActivation(mapMgr);

        if (!isCollisionWithMapLayer(entity, mapMgr) && (!isCollision(entity, mapMgr)) && state == Entity.State.WALKING) {
            setNextPositionToCurrent(entity, mapMgr, delta);
            Camera camera = mapMgr.getCamera();
            camera.position.set(currentEntityPosition.x, currentEntityPosition.y, 0f);
            camera.update();
        } else {
            updateBoundingBoxPosition(currentEntityPosition);
            //if direction key has been lifted stop moving

            //TODO bug where if player is walking against a wall switching direction
            //TODO will not break out of the walk
            /*if(motion == Entity.Motion.STANDING)
            {
                entity.sendMessage(MESSAGE.CURRENT_STATE, json.toJson(Entity.State.IDLE));
            }*/
        }

        if (isMining) {
            tileToMine = getFacingTile(entity,mapMgr);
            entity.sendMessage(MESSAGE.MINING_GRAPHICS, json.toJson(tileToMine));
            isMining = false;
        }
        if (motion == Entity.Motion.STANDING) {
            //only call calculate position once
            calculateNextPosition(delta);
        }
        tileToMine = getFacingTile(entity, mapMgr);
        //System.out.print("Tile x " + tileToMine.x + "Tile Y " + tileToMine.y);
    }

    /*
    private void selectMapEntityCandidate(MapManager mapMgr){

        //Convert screen coordinates to world coordinates, then to unit scale coordinates
        mapMgr.getCamera().unproject(mouseSelectCoordinates);
        mouseSelectCoordinates.x /= Map.UNIT_SCALE;
        mouseSelectCoordinates.y /= Map.UNIT_SCALE;

        //Gdx.app.debug(TAG, "Mouse Coordinates " + "(" + _mouseSelectCoordinates.x + "," + _mouseSelectCoordinates.y + ")");

        for( Entity mapEntity : tempEntities ) {
            //Don't break, reset all entities
            mapEntity.sendMessage(MESSAGE.ENTITY_DESELECTED);
            Rectangle mapEntityBoundingBox = mapEntity.getCurrentBoundingBox();
            //Gdx.app.debug(TAG, "Entity Candidate Location " + "(" + mapEntityBoundingBox.x + "," + mapEntityBoundingBox.y + ")");
            if (mapEntity.getCurrentBoundingBox().contains(mouseSelectCoordinates.x, mouseSelectCoordinates.y)) {
                //Check distance
                selectionRay.set(boundingBox.x, boundingBox.y, 0.0f, mapEntityBoundingBox.x, mapEntityBoundingBox.y, 0.0f);
                float distance =  selectionRay.origin.dst(selectionRay.direction);

                if( distance <= selectRayMaximumDistance ){
                    //We have a valid entity selection
                    //Picked/Selected
                    Gdx.app.debug(TAG, "Selected Entity! " + mapEntity.getEntityConfig().getEntityID());
                    mapEntity.sendMessage(MESSAGE.ENTITY_SELECTED);
                    notify(json.toJson(mapEntity.getEntityConfig()), ComponentObserver.ComponentEvent.LOAD_CONVERSATION);
                }
            }
        }
        _tempEntities.clear();
    }
    */

    private boolean updatePortalLayerActivation(MapManager mapMgr) {
        MapLayer mapPortalLayer = mapMgr.getPortalLayer();

        if (mapPortalLayer == null) {
            Gdx.app.debug(TAG, "Portal Layer doesn't exist!");
            return false;
        }

        Rectangle rectangle = null;

        for (MapObject object : mapPortalLayer.getObjects()) {
            if (object instanceof RectangleMapObject) {
                rectangle = ((RectangleMapObject) object).getRectangle();
                if (boundingBox.overlaps(rectangle)) {
                    String mapName = object.getName();
                    if (mapName == null) {
                        return false;
                    }

                    mapMgr.setClosestStartPositionFromScaledUnits(currentEntityPosition);
                    mapMgr.loadMap(MapFactory.MapType.valueOf(mapName));

                    currentEntityPosition.x = mapMgr.getPlayerStartUnitScaled().x;
                    currentEntityPosition.y = mapMgr.getPlayerStartUnitScaled().y;
                    nextEntityPosition.x = mapMgr.getPlayerStartUnitScaled().x;
                    nextEntityPosition.y = mapMgr.getPlayerStartUnitScaled().y;

                    Gdx.app.debug(TAG, "Portal Activated");
                    return true;
                }
            }
        }
        return false;
    }

}


