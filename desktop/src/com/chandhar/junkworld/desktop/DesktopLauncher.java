package com.chandhar.junkworld.desktop;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.chandhar.junkworld.JunkWorld;

public class 	DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.title = "JunkWorld";
		config.width = 800;
		config.height = 800;
		Application app = new LwjglApplication(new JunkWorld(), config);


		Gdx.app = app;
		Gdx.app.setLogLevel(Application.LOG_DEBUG);

		//TODO fix artefacting by properly importing tilesheets into TMX files with borders + padding (google LIBGDX TiledMapRender lines/bleed)
	}
}
