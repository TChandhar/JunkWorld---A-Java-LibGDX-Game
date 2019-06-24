package com.chandhar.junkworld;



public class JunkWorldTownMap extends Map
{
    public static final String TAG = PlayerPhysicsComponent.class.getSimpleName();
    private static String mapPath = "maps/JunkWorldTown.tmx";
    JunkWorldTownMap()
    {
        super(MapFactory.MapType.JUNKWORLD_TOWN, mapPath);
    }
}
