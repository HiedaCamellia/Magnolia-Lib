package org.hiedacamellia.magnolialib.world.item;

import net.minecraft.world.item.DiggerItem;
import net.minecraft.world.item.Tier;
import org.hiedacamellia.magnolialib.util.PenguinTags;

public class SickleItem extends DiggerItem {
    public SickleItem(Tier tier, Properties properties) {
        super(tier, PenguinTags.MINEABLE_SICKLE, properties.attributes(DiggerItem.createAttributes(tier, 2.0F,-2.6F)));
    }
}
