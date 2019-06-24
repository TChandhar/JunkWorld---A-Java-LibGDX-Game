package com.chandhar.junkworld;



public class ToolShopMap extends Map
{
    public static final String TAG = PlayerPhysicsComponent.class.getSimpleName();
    private static String mapPath = "maps/ToolShop.tmx";

    ToolShopMap()
    {
        super(MapFactory.MapType.TOOLSHOP, mapPath);
    }
}
