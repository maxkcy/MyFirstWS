package com.maxmyfirstws.lwjgl3;

import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;
import com.github.czyzby.websocket.CommonWebSockets;
import com.maxmyfirstws.MyFirstWSMain;

/** Launches the desktop (LWJGL3) application. */
public class Lwjgl3Launcher {
	public static void main(final String[] args) {
		// Initiating web sockets module:
		CommonWebSockets.initiate();
		createApplication();
	}

	private static Lwjgl3Application createApplication() {
		return new Lwjgl3Application(new MyFirstWSMain(), getDefaultConfiguration());
	}

	private static Lwjgl3ApplicationConfiguration getDefaultConfiguration() {
		final Lwjgl3ApplicationConfiguration configuration = new Lwjgl3ApplicationConfiguration();
		configuration.setTitle("MyFirstWS");
		configuration.setWindowedMode(MyFirstWSMain.WIDTH, MyFirstWSMain.HEIGHT);
		return configuration;
	}
}