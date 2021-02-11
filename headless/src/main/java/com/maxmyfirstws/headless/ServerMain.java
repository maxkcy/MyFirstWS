package com.maxmyfirstws.headless;

import com.badlogic.gdx.Game;
import com.github.czyzby.websocket.serialization.impl.ManualSerializer;
import com.maxmyfirstws.MyPackets.Attack;
import com.maxmyfirstws.MyPackets.Heal;
import com.maxmyfirstws.MyPackets.Message;
import com.maxmyfirstws.PacketsSerializer;
import com.maxmyfirstws.headless.Player.Player;

import io.vertx.core.AsyncResult;
import io.vertx.core.Handler;
import io.vertx.core.Vertx;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.http.HttpConnection;
import io.vertx.core.http.HttpServer;
import io.vertx.core.http.ServerWebSocket;
import io.vertx.core.http.WebSocket;
import io.vertx.core.http.WebSocketFrame;

public class ServerMain extends Game {

    Vertx vertx;
    ManualSerializer manualSerializer;
    Player player;

    @Override
    public void create() {
        vertx = Vertx.vertx();
        manualSerializer = new ManualSerializer();
        PacketsSerializer.register(manualSerializer);
        player = new Player(3000);
        this.launch();
    }

    @Override
    public void render() {


        super.render();
    }

    private void launch() {
        HttpServer httpServer = vertx.createHttpServer();
        /*httpServer.webSocketHandler(webSocket -> {
            // Printing received packets to console, sending response:
            webSocket.frameHandler(frame -> handleFrame(webSocket, frame));

        }).listen(8000);*/
        System.out.print("Launching Server...");

        httpServer.webSocketHandler(new Handler<ServerWebSocket>() {
            @Override
            public void handle(ServerWebSocket webSocket) {
                webSocket.frameHandler(new Handler<WebSocketFrame>() {
                    @Override
                    public void handle(WebSocketFrame frame) {
                        handleFrame(webSocket, frame);
                    }
                });
            }
        });


        System.out.println("Server Started!");
        httpServer.connectionHandler(new Handler<HttpConnection>() {
            @Override
            public void handle(HttpConnection httpConnection) {
                System.out.println("new connection from: " + httpConnection);

                    httpConnection.closeHandler(new Handler<Void>() {
                        @Override
                        public void handle(Void event) {
                            System.out.println("lost connection with: " + httpConnection);
                        }
                    });
            }
        });


        /*httpServer.close(new Handler<AsyncResult<Void>>() {
            @Override
            public void handle(AsyncResult<Void> lostConnetion) {
                System.out.println("Connection lost with: " + lostConnetion);
            }
        });*/


        httpServer.listen(8777);
    }


    private void handleFrame(final ServerWebSocket webSocket, final WebSocketFrame frame) {
        // Deserializing received message:
        final Object request = manualSerializer.deserialize(frame.binaryData().getBytes());
        System.out.println("Received packet: " + request);

        if (request instanceof Attack) {
            final Attack response = new Attack();
            response.setAttack(((Attack) request).getAttack());
            player.setHealth(player.getHealth() - response.getAttack());
            final Message messageResponse = new Message("Oh no, the player have been hurt. New health = " + player.getHealth());

            webSocket.writeFinalBinaryFrame(Buffer.buffer(manualSerializer.serialize(messageResponse)));
        } else if (request instanceof Heal) {
            final Heal response = new Heal();
            response.setHeal(((Heal) request).getHeal());
            player.setHealth(player.getHealth() + response.getHeal());
            final Message messageResponse = new Message("Thank you very much kind person, " +
                    "New Heath = " +
                    + player.getHealth());
            webSocket.writeFinalBinaryFrame(Buffer.buffer(manualSerializer.serialize(messageResponse)));
        }
    }

    
}
