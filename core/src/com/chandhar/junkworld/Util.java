package com.chandhar.junkworld;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.TextureLoader;
import com.badlogic.gdx.assets.loaders.resolvers.InternalFileHandleResolver;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

public final class Util {
    public static final String TAG = Util.class.getSimpleName();
    public static final AssetManager assetManager = new AssetManager();
    private static InternalFileHandleResolver filePathResolver = new InternalFileHandleResolver();

    private final static String TITLE_TEXTURE_ATLAS_PATH = "data/title.atlas";
    private final static String UI_TEXTURE_ATLAS_PATH = "data/statusui.atlas";
    private final static String UI_SKIN_PATH = "data/statusui.json";

    public static TextureAtlas TITLE_TEXTURE_ATLAS = new TextureAtlas(TITLE_TEXTURE_ATLAS_PATH);
    public static TextureAtlas UI_TEXTUREATLAS = new TextureAtlas(UI_TEXTURE_ATLAS_PATH);
    public static Skin UI_SKIN = new Skin(Gdx.files.internal(UI_SKIN_PATH), UI_TEXTUREATLAS);

    public static Util instance = new Util();

    public static Texture GetTextureAsset(String textureFilenamePath) {
        Texture texture = null;
        if (assetManager.isLoaded(textureFilenamePath)) {
            texture = assetManager.get(textureFilenamePath, Texture.class);
        } else Gdx.app.debug(TAG, "Texture does not exist!!!:" + textureFilenamePath);

        return texture;
    }

    public static void UnloadAsset(String assetFilenamePath) {
        //Check to see if asset to unload is loaded before attempting to unload it.
        if (assetManager.isLoaded(assetFilenamePath)) {
            assetManager.unload(assetFilenamePath);
        } else Gdx.app.debug(TAG, "Asset is not loaded; Nothing to unload: " + assetFilenamePath);
    }

    public static float LoadCompleted() {
        return assetManager.getProgress();
    }

    public static int NumberAssetsQueued() {
        return assetManager.getQueuedAssets();
    }

    public static boolean UpdateAssetLoading() {
        return assetManager.update();
    }

    public static boolean IsAssetLoaded(String filename) {
        return assetManager.isLoaded(filename);
    }


    public static void LoadTextureAsset(String textureFilenamePath) {
        if (textureFilenamePath == null || textureFilenamePath.isEmpty()) {
            return;
        }

        if (assetManager.isLoaded(textureFilenamePath)) {
            return;
        }

        //load asset
        if (filePathResolver.resolve(textureFilenamePath).exists()) {
            assetManager.setLoader(Texture.class, new TextureLoader(filePathResolver));
            assetManager.load(textureFilenamePath, Texture.class);
            //Until we add loading screen, just block until we load the map
            assetManager.finishLoadingAsset(textureFilenamePath);
        } else {
            Gdx.app.debug(TAG, "Texture doesn't exist!: " + textureFilenamePath);
        }
    }

        //TODO this stuff might not be needed if traditional tilemap isn't used

    public static void LoadMapAsset(String mapFilenamePath)
    {
        if(mapFilenamePath == null || mapFilenamePath.isEmpty())
        {
            return;
        }
        //load map asset
        if(filePathResolver.resolve(mapFilenamePath).exists())
        {
            assetManager.setLoader(TiledMap.class, new TmxMapLoader(filePathResolver));
            assetManager.load(mapFilenamePath,TiledMap.class);

            //until loading screen is added, just block until the map is loaded
            assetManager.finishLoadingAsset(mapFilenamePath);
            Gdx.app.debug(TAG, "Map loaded!: " + mapFilenamePath);
        }

        else
        {
            Gdx.app.debug(TAG, "Map doesn’t exist!: " + mapFilenamePath );
        }
    }

    public static TiledMap getMapAsset(String mapFilenamePath)
    {
        TiledMap map = null;

        //once the asset manager has finished loading
        if(assetManager.isLoaded(mapFilenamePath))
        {
            map = assetManager.get(mapFilenamePath,TiledMap.class);
        }

        else
        {
            Gdx.app.debug(TAG, "Map is not loaded: " + mapFilenamePath );
        }

        return map;
    }

    public static Vector2 getTile(Vector2 coords,int TILE_SIZE)
    {
        Vector2 tile = new Vector2();
        tile.x = MathUtils.floor(coords.x/TILE_SIZE);
        tile.y = MathUtils.floor(coords.y/TILE_SIZE);

        return tile;
    }

}


























/*

    public Vector2 pointToTile(float x,float y, float LEFT, float RIGHT, float BOTTOM, float TOP, Vector2 TILE_SIZE)
    {
        x = MathUtils.clamp(x,LEFT,RIGHT);
        y = MathUtils.clamp(y,BOTTOM,TOP);

        Vector2 tile = new Vector2();
        tile.x = MathUtils.floor(x/TILE_SIZE.x)+1;
        tile.y = MathUtils.ceil(-y/TILE_SIZE.y)+1;
        //tile.y = MathUtils.floor(x/TILE_SIZE.y)+1; for y+ going up not down

        return tile;
    }

    //put get tiles in the map class pass map instance to player when working out their tile ID
    public int GetTileID(float x,float y, float LEFT, float RIGHT, float BOTTOM, float TOP, Vector2 TILE_SIZE, int WORLD_WIDTH)
    {
        Vector2 tile = new Vector2();
        tile = pointToTile(x,y,LEFT,RIGHT,BOTTOM,TOP,TILE_SIZE);

        int i = (int)tile.x -1 + (((int)tile.y-1) * WORLD_WIDTH);

        return i;

    }


    public static Util getInstance()
    {
        if(instance == null)
        {
            instance = new Util();
        }
        return instance;
    }


*/