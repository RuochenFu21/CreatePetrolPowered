package net.forsteri.createpetrolpowered.entry;

import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

public class CreativeModMenu {
    public static final CreativeModeTab CREATIVE_MODE_TAB = new CreativeModeTab("createpetrolpowered") {
        @Override
        public @NotNull ItemStack makeIcon() {
            return new ItemStack(Registration.CRUDE_OIL.get().getBucket().asItem());
        }
    };

}
