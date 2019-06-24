package com.chandhar.junkworld;



public class JunkWorldTestMap extends Map
{
    public static final String TAG = PlayerPhysicsComponent.class.getSimpleName();
    private static String mapPath = "maps/JunkTest.tmx";

    JunkWorldTestMap()
    {
        super(MapFactory.MapType.JUNKWORLD_TEST, mapPath);
    }
}
