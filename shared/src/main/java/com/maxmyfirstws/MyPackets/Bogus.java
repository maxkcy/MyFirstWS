package com.maxmyfirstws.MyPackets;


import com.github.czyzby.websocket.serialization.SerializationException;
import com.github.czyzby.websocket.serialization.Transferable;
import com.github.czyzby.websocket.serialization.impl.Deserializer;
import com.github.czyzby.websocket.serialization.impl.Serializer;
import com.github.czyzby.websocket.serialization.impl.Size;

public class Bogus implements Transferable<Bogus> {
    public String message;

    public Bogus(String message) {
        this.message = message;
    }

    public Bogus() {
    }

    @Override
    public void serialize(Serializer serializer) throws SerializationException {
        serializer.serializeString(message);
    }

    @Override
    public Bogus deserialize(Deserializer deserializer) throws SerializationException {
        return new Bogus(deserializer.deserializeString());
    }
}
