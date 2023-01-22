package net.forsteri.createpetroleum.entry;

import com.simibubi.create.AllBlocks;
import com.simibubi.create.content.contraptions.fluids.tank.*;
import com.simibubi.create.content.logistics.block.display.source.BoilerDisplaySource;
import com.simibubi.create.foundation.data.AssetLookup;
import com.simibubi.create.foundation.data.CreateRegistrate;
import com.simibubi.create.foundation.data.SharedProperties;
import com.tterrag.registrate.util.entry.BlockEntityEntry;
import com.tterrag.registrate.util.entry.BlockEntry;
import net.forsteri.createpetroleum.CreatePetroleum;
import net.forsteri.createpetroleum.content.DistillationTankBlock;
import net.forsteri.createpetroleum.content.DistillationTankGenerator;
import net.forsteri.createpetroleum.content.DistillationTankRenderer;
import net.forsteri.createpetroleum.content.DistillationTankTileEntity;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.world.level.block.state.BlockBehaviour;

import static com.simibubi.create.Create.REGISTRATE;
import static com.simibubi.create.content.logistics.block.display.AllDisplayBehaviours.assignDataBehaviour;
import static com.simibubi.create.foundation.data.TagGen.pickaxeOnly;

public class Registration {
    private static final CreateRegistrate REGISTRATE = CreatePetroleum.registrate()
            .get().creativeModeTab(() -> CreativeModMenu.CREATIVE_MODE_TAB);

    public static final BlockEntry<DistillationTankBlock> DISTILLATION_TANK_BLOCK = REGISTRATE.block("distillation_tank", DistillationTankBlock::new)
            .initialProperties(SharedProperties::copperMetal)
            .properties(BlockBehaviour.Properties::noOcclusion)
            .properties(p -> p.isRedstoneConductor((p1, p2, p3) -> true))
            .transform(pickaxeOnly())
            .blockstate(new DistillationTankGenerator()::generate)
            .onRegister(CreateRegistrate.blockModel(() -> FluidTankModel::standard))
            .addLayer(() -> RenderType::cutoutMipped)
            .item(FluidTankItem::new)
            .model(AssetLookup.<FluidTankItem>customBlockItemModel("_", "block_single_window"))
            .build()
            .register();

    public static final BlockEntityEntry<DistillationTankTileEntity> DISTILLATION_TANK_TILE_ENTITY = REGISTRATE
            .tileEntity("distillation_tank", DistillationTankTileEntity::new)
            .validBlocks(AllBlocks.FLUID_TANK)
            .renderer(() -> DistillationTankRenderer::new)
            .register();

    public static void register() {}
}
