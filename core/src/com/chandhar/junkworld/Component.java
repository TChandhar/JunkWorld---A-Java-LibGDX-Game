package com.chandhar.junkworld;


public interface Component
{
    public static final String MESSAGE_TOKEN = ":::::";

    public static enum MESSAGE
    {
        CURRENT_POSITION,
        CURRENT_POSITION_ID,
        NEXT_POSITION,
        INIT_START_POSITION,
        CURRENT_DIRECTION,
        CURRENT_STATE,
        CURRENT_MOTION,
        COLLISION_WITH_MAP,
        COLLISION_WITH_ENTITY,
        LOAD_ANIMATIONS,
        INIT_DIRECTION,
        INIT_STATE,
        INIT_SELECT_ENTITY,
        ENTITY_SELECTED,
        ENTITY_DESELECTED,
        MINE,
        MINING_PHYSICS, //true or false
        MINING_GRAPHICS,
        PLAYER,
        MINE_ANIMATION_FINISHED,
    }

    void dispose();

    void receiveMessage(String message);

}
