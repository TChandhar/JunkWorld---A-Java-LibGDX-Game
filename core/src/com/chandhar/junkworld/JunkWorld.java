package com.chandhar.junkworld;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Screen;
import com.chandhar.junkworld.screens.MainGameScreen;
import com.chandhar.junkworld.screens.MainMenuScreen;

public class JunkWorld extends Game {
	private static final MainGameScreen mainGameScreen = new MainGameScreen();
	private static MainMenuScreen mainMenuScreen;
	//private static NewGameScreen newGameScreen;

	public static enum ScreenType {
		MainMenu,
		MainGame,
		LoadGame,
		NewGame,
		GameOver,
		WatchIntro,
		Credits
	}

	public Screen getScreenType(ScreenType screenType) {
		switch (screenType) {
			case MainGame:
				return mainGameScreen;
			case MainMenu:
				return mainMenuScreen;
			default:
				return mainMenuScreen;
			//case NewGame:
			//	return newGameScreen;
		}

	}

	@Override
	public void create() {
		//mainGameScreen = new MainGameScreen(this);
		mainMenuScreen = new MainMenuScreen(this);
		setScreen(mainGameScreen);
	}

	@Override
	public void dispose() {
		mainGameScreen.dispose();
		//mainMenuScreen.dispose();

	}

}

