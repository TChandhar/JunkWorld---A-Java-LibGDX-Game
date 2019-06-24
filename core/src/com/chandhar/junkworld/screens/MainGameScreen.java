package com.chandhar.junkworld.screens;

 import com.badlogic.gdx.Gdx;
 import com.badlogic.gdx.Input;
 import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
 import com.badlogic.gdx.graphics.g2d.Sprite;
 import com.badlogic.gdx.graphics.g2d.TextureRegion;
 import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
 import com.badlogic.gdx.maps.tiled.TiledMapTileSet;
 import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
 import com.badlogic.gdx.utils.Json;


 import com.chandhar.junkworld.JunkWorld;
 import com.chandhar.junkworld.MapFactory;
 import com.chandhar.junkworld.MapManager;
 import com.chandhar.junkworld.Entity;
 import com.chandhar.junkworld.EntityFactory;
 import com.chandhar.junkworld.Map;
 import com.chandhar.junkworld.Component;


public class MainGameScreen extends GameScreen
{
    public static final String TAG = MainGameScreen.class.getSimpleName();

    public static class VIEWPORT
    {
        public static float viewportWidth;              //width and Height of the viewport (what will be seen on the screen at any one time
        public static float viewportHeight;
        public static float virtualWidth;
        public static float virtualHeight;
        public static float physicalWidth;
        public static float physicalHeight;
        public static float aspectRatio;
    }

    public static enum GameState                        //state machine for the various game states
    {
        SAVING,
        LOADING,
        RUNNING,
        PAUSED,
        GAME_OVER
    }

    private static GameState gameState;

    protected OrthogonalTiledMapRenderer mapRenderer = null;
    protected OrthographicCamera camera = null;
    protected OrthographicCamera hudCamera = null;
    protected InputMultiplexer multiplexer;
    //protected static PlayerHUD playerHUD;
    private static MapManager mapMgr;
    private Json json;

    private JunkWorld game;
    private Entity player;

    public MainGameScreen()
    {
        //hudCamera = new OrthographicCamera();
        //hudCamera.setToOrtho(false, VIEWPORT.physicalWidth, VIEWPORT.physicalHeight);
        //playerHUD = new PlayerHud(hudCamera, player);

        //multiplexer new InputMultiplexer();
        //multiplexer.addProcessor(playerHUD.getStage());
        //multiplexer.addProcessor(player.getInputProcessor());
        //Gdx.Se
        mapMgr = new MapManager();
        json = new Json();
    }

    @Override
    public void show()
    {
        //main camera setup
        setupViewport(15,15);

        //get camera size;
        camera = new OrthographicCamera();
        camera.setToOrtho(false, VIEWPORT.viewportWidth,VIEWPORT.viewportHeight);

        mapRenderer = new OrthogonalTiledMapRenderer(mapMgr.getCurrentTiledMap(), Map.UNIT_SCALE);
        mapRenderer.setView(camera);

        mapMgr.setCamera(camera);
        Gdx.app.debug(TAG, "UnitScale value is: " + mapRenderer.getUnitScale());

        player = EntityFactory.getInstance().getEntity(EntityFactory.EntityType.PLAYER);
        mapMgr.setPlayer(player);

    }

    @Override
    public void hide() {

    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        mapRenderer.setView(camera);

        if( mapMgr.hasMapChanged() ) {
            mapRenderer.setMap(mapMgr.getCurrentTiledMap());
            player.sendMessage(Component.MESSAGE.INIT_START_POSITION, json.toJson(mapMgr.getPlayerStartUnitScaled()));


            camera.position.set(mapMgr.getPlayerStartUnitScaled().x, mapMgr.getPlayerStartUnitScaled().y, 0f);
            camera.update();

            mapMgr.setMapChanged(false);
            //mapRenderer.setView(camera);
        }


        mapRenderer.render();

        player.update(mapMgr, mapRenderer.getBatch(), delta);


        /*
        if(Gdx.input.isKeyJustPressed(Input.Keys.J)) {
            player.Mine(player, mapMgr);
        }*/

    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void pause() {
        setGameState(GameState.SAVING);

    }

    @Override
    public void resume() {
        setGameState(GameState.LOADING);

    }

    @Override
    public void dispose() {
        if (player != null) {
            player.dispose();
        }

        if (mapRenderer != null) {
            mapRenderer.dispose();
        }
    }



    public static void setGameState(GameState gameState){
        switch(gameState){
            case RUNNING:
                gameState = GameState.RUNNING;
                break;
            case LOADING:
                //ProfileManager.getInstance().loadProfile();       //load json with values of save
                gameState = GameState.RUNNING;
                break;
            case SAVING:
                //ProfileManager.getInstance().saveProfile();       //save json with values of save
                gameState = GameState.PAUSED;
                break;
            case PAUSED:
                if(gameState == GameState.PAUSED)
                {
                    gameState = GameState.RUNNING;
                }
                else if( gameState == GameState.RUNNING )
                {
                    gameState = GameState.PAUSED;
                }
                break;
            case GAME_OVER:
                gameState = GameState.GAME_OVER;
                break;
            default:
                gameState = GameState.RUNNING;
                break;
        }

    }

    private void setupViewport(int width, int height)
    {
        //todo RE-WRITE VIEWPORT TO SOLVE ARTIFACTING
        //Make the viewport a percentage of the total display area
        VIEWPORT.virtualWidth = width;
        VIEWPORT.virtualHeight = height;

        //Current viewport dimensions
        VIEWPORT.viewportWidth = VIEWPORT.virtualWidth;
        VIEWPORT.viewportHeight = VIEWPORT.virtualHeight;

        //pixel dimensions of display
        VIEWPORT.physicalWidth = Gdx.graphics.getWidth();
        VIEWPORT.physicalHeight = Gdx.graphics.getHeight();

        //aspect ratio for current viewport
        VIEWPORT.aspectRatio = (VIEWPORT.virtualWidth / VIEWPORT.virtualHeight);

        //update viewport if there could be skewing
        if( VIEWPORT.physicalWidth / VIEWPORT.physicalHeight >= VIEWPORT.aspectRatio)
        {
            //Letterbox left and right
            VIEWPORT.viewportWidth = VIEWPORT.viewportHeight * (VIEWPORT.physicalWidth/VIEWPORT.physicalHeight);
            VIEWPORT.viewportHeight = VIEWPORT.virtualHeight;
        }

        else
        {
            //letterbox above and below
            VIEWPORT.viewportWidth = VIEWPORT.virtualWidth;
            VIEWPORT.viewportHeight = VIEWPORT.viewportWidth * (VIEWPORT.physicalHeight/VIEWPORT.physicalWidth);
        }

        Gdx.app.debug(TAG, "WorldRenderer: virtual: (" + VIEWPORT.virtualWidth + "," + VIEWPORT.virtualHeight + ")" );
        Gdx.app.debug(TAG, "WorldRenderer: viewport: (" + VIEWPORT.viewportWidth + "," + VIEWPORT.viewportHeight + ")" );
        Gdx.app.debug(TAG, "WorldRenderer: physical: (" + VIEWPORT.physicalWidth + "," + VIEWPORT.physicalHeight + ")" );
    }
}
