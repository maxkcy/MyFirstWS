package com.maxmyfirstws.MyPackets;

import com.github.czyzby.websocket.serialization.SerializationException;
import com.github.czyzby.websocket.serialization.Transferable;
import com.github.czyzby.websocket.serialization.impl.Deserializer;
import com.github.czyzby.websocket.serialization.impl.Serializer;

public class Heal implements Transferable<Heal> {


    private int heal = 5;

    public Heal(int heal) {
        this.heal = heal;
    }

    public Heal() {
    }

    public int getHeal() {
        return heal;
    }

    public void setHeal(int heal) {
        this.heal = heal;
    }

    @Override
    public void serialize(Serializer serializer) throws SerializationException {
        serializer.serializeInt(heal);
    }

    @Override
    public Heal deserialize(Deserializer deserializer) throws SerializationException {
        return new Heal(deserializer.deserializeInt());
    }
}
