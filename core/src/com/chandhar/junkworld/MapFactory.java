package com.chandhar.junkworld;

import java.util.Hashtable;

public class MapFactory
{
    //all maps for game
    private static Hashtable<MapType,Map> mapTable = new Hashtable<MapType, Map>();

    public static enum MapType
    {
        JUNKWORLD_TEST,
        JUNKWORLD_TOWN,
        TOOLSHOP,
    }

    static public Map getMap(MapType mapType)
    {
        Map map = null;
        switch(mapType)
        {
            case JUNKWORLD_TEST:
                map = mapTable.get(MapType.JUNKWORLD_TEST);
                if(map == null)
                {
                    map = new JunkWorldTestMap();
                    mapTable.put(MapType.JUNKWORLD_TEST, map);
                }
                break;
            case JUNKWORLD_TOWN:
                map = mapTable.get(MapType.JUNKWORLD_TOWN);
                if(map == null)
                {
                    map = new JunkWorldTownMap();
                    mapTable.put(MapType.JUNKWORLD_TOWN, map);
                }
                break;
            case TOOLSHOP:
                map = mapTable.get(MapType.TOOLSHOP);
                if(map == null)
                {
                    map = new ToolShopMap();
                    mapTable.put(MapType.TOOLSHOP, map);
                }
                break;

            default:
                break;
        }
        return map;
    }

    public static void clearCache(){
        for( Map map: mapTable.values()){
            map.dispose();
        }
        mapTable.clear();
    }
}
