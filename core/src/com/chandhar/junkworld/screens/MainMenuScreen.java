package com.chandhar.junkworld.screens;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.chandhar.junkworld.JunkWorld;
import com.chandhar.junkworld.Util;

public class MainMenuScreen extends GameScreen{

    private Stage stage;
    private JunkWorld game;

    public MainMenuScreen(JunkWorld newGame)
    {
        game = newGame;

        //creation
        stage = new Stage();
        Table table = new Table();
        table.setFillParent(true);

        Image title = new Image(Util.TITLE_TEXTURE_ATLAS.findRegion("title"));
        TextButton newGameButton = new TextButton("New Game", Util.UI_SKIN);



        //layout
        table.add(title).spaceBottom(75).row();
        table.add(newGameButton).spaceBottom(10).row();
        stage.addActor(table);

        //Listeners
        newGameButton.addListener(new ClickListener() {
                                      @Override
                                      public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                                          return true;
                                      }

                                      @Override
                                      public void touchUp (InputEvent event, float x, float y, int pointer, int button) {
                                          game.setScreen(game.getScreenType(JunkWorld.ScreenType.MainGame));
                                      }
                                  }
        );
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(50, 50, 25, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        stage.act(delta);
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {stage.getViewport().setScreenSize(width, height);
    }

    @Override
    public void show() {

        Gdx.input.setInputProcessor(stage);
    }

    @Override
    public void hide() {
        Gdx.input.setInputProcessor(null);
    }

    @Override
    public void pause() {
    }

    @Override
    public void resume() {
    }

    @Override
    public void dispose() {
        stage.dispose();
    }

}


