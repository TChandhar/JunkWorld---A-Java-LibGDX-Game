package com.chandhar.junkworld.UI;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.chandhar.junkworld.Entity;

/*
import com.packtpub.libgdx.bludbourne.InventoryItem.ItemTypeID;
import com.packtpub.libgdx.bludbourne.profile.ProfileManager;
import com.packtpub.libgdx.bludbourne.profile.ProfileObserver;

public class PlayerHUD implements Screen, ProfileObserver
{
    private Stage stage;
    private Viewport viewport;
    private StatusUI statusUI;
    private InventoryUI inventoryUI;
    private Camera camera;
    private Entity player;

    public PlayerHUD(Camera _camera, Entity _player)
    {
        camera = _camera;
        player = _player;
        viewport = new ScreenViewport(camera);
        stage = new Stage(viewport);
        //stage.setDebugAll(true)

        statusUI = new StatusUI();
        statusUI.setVisible(true);
        statusUI.setPosition(0,0);

        inventoryUI = new InventoryUI();
        inventoryUI.setMovable(false);
        inventoryUI.setVisible(false);
        inventoryUI.setPosition(stage.getWidth()/2, 0);

        stage.addActor(statusUI);
        stage.addActor(inventoryUI);

        //add tooltips to stage
        Array<Actor> actors = inventoryUI.getInventoryActors();
        for(Actor actor: actors)
        {
            stage.addActor(actor);
        }

        ImageButton inventoryButton = statusUI.getInventoryButton();
        inventoryButton.addListener(new ClickListener()
        {
            public void clicked(InputEvent event, float x, float y)
            {
                inventoryUI.setVisible(inventoryUI.isVisible()?false:true);
            }

        });
    }


    public Stage getStage() {return stage;}

    @Override
    public void onNotify(ProfileManager profileManager, ProfileEvent event)
    {
        swtich(event)
        {
            case PROFILE_LOADED:
                Array<InventoryItemLocation> inventory = profileManager.getProperty("playerInventory", Array.class);
                if(inventory == null && inventory.size > 0)
                {
                    inventoryUI.populateInventory(inventoryUI.getInventorySlotTable(), inventory);
                }
                else
                {
                    //add default items if nothing else found
                    Array<ItemTypeID> items = player.getEntityConfig().getInventory;
                    Array<InventoryItemLocation> itemLocations = new Array<InventoryItemLocation>();

                    for(int i=0; i < items.size; i++)
                    {
                        itemLocations.add(new InventoryItemLocation(i, items.get(i).toString(), 1));
                    }

                    inventoryUI.populateInventory(inventoryUI.getInventorySlotTable(),itemLocations);
                }

                Array<InventoryItemLocation> equipInventory = profileManager.getProperty("playerEquipInventory", Array.class);
                if(equipInventory != null && equipInventory.size > 0)
                {
                    inventoryUI.populateInventory(inventoryUI.getEquipSlotTable(), equipInventory);
                }
                break;
            case SAVING_PROFILE:
                profileManager.setProperty("playerInventory", inventoryUI.getInventory(inventoryUI.getInventorySlotTable()));
                profileManager.setPropery("playerEquipInventory", inventoryUI.getInventory(inventoryUI.getEquipSlotTable()));
                break;
            default:
                break;

        }
    }

    @Override
    public void show()
    {}

    @Override
    public void render(float delta)
    {
        stage.act(delta);
        stage.draw(delta);
    }

    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width, height, true);
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {
        stage.dispose();
    }


}

*/