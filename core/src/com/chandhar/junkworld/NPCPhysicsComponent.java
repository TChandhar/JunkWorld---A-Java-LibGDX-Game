package com.chandhar.junkworld;

import com.badlogic.gdx.math.Vector2;

public class NPCPhysicsComponent extends PhysicsComponent
{
    private static final String TAG = NPCPhysicsComponent.class.getSimpleName();

    private Entity.State state;

    public NPCPhysicsComponent()
    {
        boundingBoxLocation = BoundingBoxLocation.CENTER;
        initBoundingBox(0.4f, 0.15f);
    }

    @Override
    public void dispose()
    {}

    @Override
    public void receiveMessage(String message)
    {
        String[] string = message.split(Component.MESSAGE_TOKEN);

        if(string.length == 0) return;

        //messages with 1 object payload
        if(string.length == 2)
        {
            if(string[0].equalsIgnoreCase(MESSAGE.INIT_START_POSITION.toString()))
            {
                currentEntityPosition = json.fromJson(Vector2.class, string[1]);
                nextEntityPosition.set(currentEntityPosition.x, currentEntityPosition.y);
            }

            else if(string[0].equalsIgnoreCase(MESSAGE.CURRENT_STATE.toString()))
            {
                state = json.fromJson(Entity.State.class, string[1]);
            }

            else if(string[0].equalsIgnoreCase(MESSAGE.CURRENT_DIRECTION.toString()))
            {
                currentDirection = json.fromJson(Entity.Direction.class, string[1]);
            }
        }
    }

    @Override
    public void update(Entity entity, MapManager mapMgr, float delta)
    {
        updateBoundingBoxPosition(nextEntityPosition);
        if(isEntityFarFromPlayer(mapMgr))
        {
            entity.sendMessage(MESSAGE.ENTITY_DESELECTED);
        }

        if(state == Entity.State.IMMOBILE)
        {
            return;
        }

        if(!isCollisionWithMapLayer(entity, mapMgr) && !isCollisionWithMapEntities(entity, mapMgr) && state == Entity.State.WALKING)
        {
            setNextPositionToCurrent(entity, mapMgr, delta);
        }
        else
        {
            updateBoundingBoxPosition(currentEntityPosition);
        }
        calculateNextPosition(delta);
    }

    private boolean isEntityFarFromPlayer(MapManager mapMgr)
    {
        //Check distance
        selectionRay.set(mapMgr.getPlayer().getCurrentBoundingBox().x, mapMgr.getPlayer().getCurrentBoundingBox().y, 0.0f, boundingBox.x, boundingBox.y, 0.0f);
        float distance = selectionRay.origin.dst(selectionRay.direction);
        if( distance <= selectRayMaximumDistance )
        {
            return false;
        }
        else
        {
            return true;
        }
    }

    @Override
    protected boolean isCollisionWithMapEntities(Entity entity, MapManager mapMgr)
    {
       return false;
    }


}
