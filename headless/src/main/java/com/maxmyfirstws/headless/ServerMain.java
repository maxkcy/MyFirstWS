package com.maxmyfirstws.headless;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.utils.Array;
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
import io.vertx.core.net.SocketAddress;

public class ServerMain extends Game {

    Vertx vertx;
    ManualSerializer manualSerializer;
    Player player;
    HttpServer httpServer;
    Array<SocketAddress> connectedPlayerList = new Array<>();
    Array<String> cPList = new Array<>();
    Array<ServerWebSocket> clientWSList = new Array<>();


    @Override
    public void create() {
        vertx = Vertx.vertx();
        manualSerializer = new ManualSerializer();
        PacketsSerializer.register(manualSerializer);
        player = new Player(3000);
        this.launch();
    }

    @Override
    public void dispose() {
        httpServer.close();
        super.dispose();
    }

    @Override
    public void render() {


        super.render();
    }

    private void launch() {
        httpServer = vertx.createHttpServer();
        /*httpServer.webSocketHandler(webSocket -> {
            // Printing received packets to console, sending response:
            webSocket.frameHandler(frame -> handleFrame(webSocket, frame));

        }).listen(8000);*/
        System.out.print("Launching Server...");

        httpServer.webSocketHandler(new Handler<ServerWebSocket>() {
            @Override
            public void handle(ServerWebSocket client) {
                System.out.println("connection from (WS handler)"+ client.textHandlerID());
                cPList.add(client.textHandlerID());
                clientWSList.add(client);

                client.frameHandler(new Handler<WebSocketFrame>() {
                    @Override
                    public void handle(WebSocketFrame frame) {
                        handleFrame(client, frame);
                    }
                });

                client.closeHandler(new Handler<Void>() {
                    @Override
                    public void handle(Void event) {
                        System.out.println("client disconected (WS hander)"+ client.textHandlerID());
                        cPList.removeValue(client.textHandlerID(), false);
                        System.out.println("player removed from list");
                        clientWSList.removeValue(client, true);
                    }
                });
                final Attack response = new Attack();
                response.setAttack(5);
                player.setHealth(player.getHealth() - response.getAttack());
                final Message messageResponse = new Message("ah new player hit me on arrival! now my health is " + player.getHealth());
               for (ServerWebSocket clientTemp : clientWSList){
                clientTemp.writeFinalBinaryFrame(Buffer.buffer(manualSerializer.serialize(messageResponse)));}




            }
        });

        System.out.println("Server Started!");

        httpServer.connectionHandler(new Handler<HttpConnection>() {
            @Override
            public void handle(HttpConnection httpConnection) {
                System.out.println("new connection from: RA " + httpConnection.remoteAddress());
                    connectedPlayerList.add(httpConnection.remoteAddress());
                    httpConnection.closeHandler(new Handler<Void>() {
                        @Override
                        public void handle(Void event) {
                            System.out.println("lost connection with: RA" + httpConnection.remoteAddress());
                            connectedPlayerList.removeValue(httpConnection.remoteAddress(), true);
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


        vertx.setPeriodic(10000, new Handler<Long>() {
            @Override
            public void handle(Long event) {
                Message time = new Message("The current time is " + java.time.LocalDateTime.now());
                for (ServerWebSocket client : clientWSList){

                    client.writeFinalBinaryFrame(Buffer.buffer(manualSerializer.serialize(time)));
                }
            }
        });
    }


    private void handleFrame(ServerWebSocket webSocket, final WebSocketFrame frame) {
        // Deserializing received message:
        final Object request = manualSerializer.deserialize(frame.binaryData().getBytes());
        System.out.println("Received packet: " + request + " from: RA " + webSocket.remoteAddress());//instead i want httpConnection here

        if (request instanceof Attack) {
            final Attack response = new Attack();
            response.setAttack(((Attack) request).getAttack());
            player.setHealth(player.getHealth() - response.getAttack());
            final Message messageResponse = new Message("Ouch! OUCH! OuCh! New health = " + player.getHealth());

            //webSocket.remoteAddress() = connectedPlayerList.get(0); <--line is error, can't do it this way
            webSocket.writeFinalBinaryFrame(Buffer.buffer(manualSerializer.serialize(messageResponse)));
            System.out.println("Data sent to: " + webSocket.remoteAddress());
            //^ I want to write to specific httpConnecitions.



        } else if (request instanceof Heal) {
            final Heal response = new Heal();
            response.setHeal(((Heal) request).getHeal());
            player.setHealth(player.getHealth() + response.getHeal());
            final Message messageResponse = new Message("Thx for helping me! " +
                    "New Heath = " +
                    + player.getHealth());
            webSocket.writeFinalBinaryFrame(Buffer.buffer(manualSerializer.serialize(messageResponse)));
            System.out.println("Data send to: " + webSocket.remoteAddress());
        }
    }

    //I want to send data/write data independent of a request... how?
}
