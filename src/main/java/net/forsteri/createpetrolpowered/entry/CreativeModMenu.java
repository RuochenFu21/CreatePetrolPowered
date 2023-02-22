package net.forsteri.createpetrolpowered.entry;

import net.forsteri.createindustrialchemistry.entry.registers.substances.SolidSubstances;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

public class CreativeModMenu {
    public static final CreativeModeTab CREATIVE_MODE_TAB = new CreativeModeTab("createpetrolpowered") {
        @Override
        public @NotNull ItemStack makeIcon() {
            return new ItemStack(SolidSubstances.SODIUM_HYDROXIDE.get());
        }
    };

}
