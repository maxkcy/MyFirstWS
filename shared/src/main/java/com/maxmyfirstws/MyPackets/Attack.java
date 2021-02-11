package com.maxmyfirstws.MyPackets;

import com.github.czyzby.websocket.serialization.SerializationException;
import com.github.czyzby.websocket.serialization.Transferable;
import com.github.czyzby.websocket.serialization.impl.Deserializer;
import com.github.czyzby.websocket.serialization.impl.Serializer;

public class Attack implements Transferable<Attack> {

    private int attack = 5;

    public Attack(int attack) {
        this.attack = attack;
    }

    public Attack() {
    }

    @Override
    public void serialize(Serializer serializer) throws SerializationException {
            serializer.serializeInt(attack);
    }

    @Override
    public Attack deserialize(Deserializer deserializer) throws SerializationException {
        return new Attack(deserializer.deserializeInt());
    }

    public int getAttack() {
        return attack;
    }

    public void setAttack(int attack) {
        this.attack = attack;
    }

}
