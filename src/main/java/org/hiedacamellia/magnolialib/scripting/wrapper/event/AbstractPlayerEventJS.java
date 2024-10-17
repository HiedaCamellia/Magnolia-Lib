package org.hiedacamellia.magnolialib.scripting.wrapper.event;

import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import org.hiedacamellia.magnolialib.scripting.wrapper.WrapperRegistry;
import org.hiedacamellia.magnolialib.scripting.wrapper.PlayerJS;

public class AbstractPlayerEventJS<C extends PlayerEvent> extends AbstractEventJS<C> {
    public AbstractPlayerEventJS(C object) {
        super(object);
    }

    public PlayerJS player() {
        return WrapperRegistry.wrap(penguinScriptingObject.getEntity());
    }
}
