package com.maxmyfirstws;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.github.czyzby.websocket.WebSocket;
import com.github.czyzby.websocket.WebSocketHandler;
import com.github.czyzby.websocket.WebSocketListener;
import com.github.czyzby.websocket.WebSockets;
import com.github.czyzby.websocket.serialization.impl.ManualSerializer;
import com.maxmyfirstws.MyPackets.Attack;
import com.maxmyfirstws.MyPackets.Heal;
import com.maxmyfirstws.MyPackets.Message;

public class MyFirstWSMain extends Game {
    public static final int WIDTH = 80;
    public static final int HEIGHT = 80;

    WebSocket webSocket;
    ManualSerializer serializer;

    @Override
    public void create() {
        webSocket = WebSockets.newSocket(WebSockets.toWebSocketUrl("localhost", 8777));

        serializer = new ManualSerializer();
        PacketsSerializer.register(serializer);
        webSocket.setSerializer(serializer);
        webSocket.setSendGracefully(true);


        webSocket.addListener(getListener());
        webSocket.connect();
    }

    private WebSocketListener getListener() {
        WebSocketHandler handler = new WebSocketHandler();
        // Registering Ping handler:
        handler.registerHandler(Message.class, new WebSocketHandler.Handler<Message>() {
            @Override
            public boolean handle(final WebSocket webSocket, final Message packet) {
                System.out.println("Message from server: " + packet.message );
                return true;
            }
        });
        return handler;
    }

    void sendAttack(){
        Attack attack = new Attack();
        attack.setAttack(5);
        webSocket.send(attack);
    }

    void sendHeal(){
        Heal heal = new Heal();
        heal.setHeal(5);
        webSocket.send(heal);
    }


    @Override
    public void render() {
        if(Gdx.input.isKeyPressed(Input.Keys.A)){
            sendAttack();
        }
        if(Gdx.input.isKeyPressed(Input.Keys.H)){
            sendHeal();
        }
        super.render();
    }


    @Override
    public void dispose() {
        webSocket.close();
        super.dispose();
    }
}

