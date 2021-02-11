package com.maxmyfirstws;

import com.github.czyzby.websocket.serialization.impl.ManualSerializer;
import com.maxmyfirstws.MyPackets.Attack;
import com.maxmyfirstws.MyPackets.Bogus;
import com.maxmyfirstws.MyPackets.Heal;
import com.maxmyfirstws.MyPackets.Message;

public class PacketsSerializer {

    public PacketsSerializer() {
    }

    public static void register(ManualSerializer serializer){
        serializer.register(new Attack());
        serializer.register(new Heal());
        serializer.register(new Bogus());
        serializer.register(new Message());
    }
}
