package org.hiedacamellia.magnolialib.util.registry;

import net.minecraft.network.protocol.PacketFlow;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface Packet {
    PacketFlow value();
}
