package net.forsteri.createpetroleum.entry;

import net.forsteri.createindustrialchemistry.entry.registers.substances.SolidSubstances;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;

public class CreativeModMenu {
    public static final CreativeModeTab CREATIVE_MODE_TAB = new CreativeModeTab("createpetroleum") {
        @Override
        public ItemStack makeIcon() {
            return new ItemStack(SolidSubstances.SODIUM_HYDROXIDE.get());
        }
    };

}
