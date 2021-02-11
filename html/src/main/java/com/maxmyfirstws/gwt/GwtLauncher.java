package com.maxmyfirstws.gwt;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.backends.gwt.GwtApplication;
import com.badlogic.gdx.backends.gwt.GwtApplicationConfiguration;
import com.github.czyzby.websocket.GwtWebSockets;
import com.maxmyfirstws.MyFirstWSMain;

/** Launches the GWT application. */
public class GwtLauncher extends GwtApplication {
	@Override
	public GwtApplicationConfiguration getConfig() {
		GwtApplicationConfiguration configuration = new GwtApplicationConfiguration(MyFirstWSMain.WIDTH, MyFirstWSMain.HEIGHT);
		return configuration;
	}

	@Override
	public ApplicationListener createApplicationListener() {
		// Initiating GWT web sockets module:
		GwtWebSockets.initiate();
		return new MyFirstWSMain();
	}
}