package net.forsteri.createpetroleum.entry;

import com.simibubi.create.content.contraptions.fluids.tank.FluidTankGenerator;
import com.simibubi.create.content.contraptions.fluids.tank.FluidTankModel;
import com.simibubi.create.content.contraptions.fluids.tank.FluidTankRenderer;
import com.simibubi.create.content.contraptions.relays.encased.ShaftInstance;
import com.simibubi.create.foundation.block.BlockStressDefaults;
import com.simibubi.create.foundation.data.AssetLookup;
import com.simibubi.create.foundation.data.CreateRegistrate;
import com.simibubi.create.foundation.data.SharedProperties;
import com.simibubi.create.foundation.data.TagGen;
import com.tterrag.registrate.util.entry.BlockEntityEntry;
import com.tterrag.registrate.util.entry.BlockEntry;
import net.forsteri.createpetroleum.CreatePetroleum;
import net.forsteri.createpetroleum.content.distillation.DistillationTankBlock;
import net.forsteri.createpetroleum.content.distillation.DistillationTankItem;
import net.forsteri.createpetroleum.content.distillation.DistillationTankTileEntity;
import net.forsteri.createpetroleum.content.oilRig.OilRigItem;
import net.forsteri.createpetroleum.content.oilRig.fluidOutput.FluidOutputBlock;
import net.forsteri.createpetroleum.content.oilRig.fluidOutput.FluidOutputTileEntity;
import net.forsteri.createpetroleum.content.oilRig.ghost.GhostBlock;
import net.forsteri.createpetroleum.content.oilRig.ghost.GhostTileEntity;
import net.forsteri.createpetroleum.content.oilRig.kineticInput.KineticInputBlock;
import net.forsteri.createpetroleum.content.oilRig.kineticInput.KineticInputTileEntity;
import net.forsteri.createpetroleum.content.oilRig.master.MasterBlock;
import net.forsteri.createpetroleum.content.oilRig.master.MasterTileEntity;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.level.block.state.BlockBehaviour;

import static com.simibubi.create.foundation.data.TagGen.pickaxeOnly;

public class Registration {
    private static final CreateRegistrate REGISTRATE = CreatePetroleum.registrate()
            .get().creativeModeTab(() -> CreativeModMenu.CREATIVE_MODE_TAB);

    public static final BlockEntry<DistillationTankBlock> DISTILLATION_TANK_BLOCK = REGISTRATE.block("distillation_tank", DistillationTankBlock::new)
            .initialProperties(SharedProperties::copperMetal)
            .properties(BlockBehaviour.Properties::noOcclusion)
            .transform(pickaxeOnly())
            .blockstate(new FluidTankGenerator()::generate)
            .onRegister(CreateRegistrate.blockModel(() -> FluidTankModel::standard))
            .addLayer(() -> RenderType::cutoutMipped)
            .item(DistillationTankItem::new)
            .model(AssetLookup.customBlockItemModel("_", "block_single_window"))
            .build()
            .register();

    public static final BlockEntityEntry<DistillationTankTileEntity> DISTILLATION_TANK_TILE_ENTITY = REGISTRATE
            .tileEntity("distillation_tank", DistillationTankTileEntity::new)
            .validBlocks(Registration.DISTILLATION_TANK_BLOCK)
            .renderer(() -> FluidTankRenderer::new)
            .register();

    public static final BlockEntry<KineticInputBlock> KINETIC_INPUT_BLOCK = REGISTRATE.block("kinetic_input", KineticInputBlock::new)
            .initialProperties(SharedProperties::copperMetal)
            .properties(BlockBehaviour.Properties::noOcclusion)
            .transform(BlockStressDefaults.setImpact(8))
            .tag(BlockTags.NEEDS_IRON_TOOL)
            .transform(TagGen.pickaxeOnly())
            .blockstate((ctx, prov) -> prov.simpleBlock(ctx.getEntry(), prov.models().getBuilder("kinetic_in")
    //        .texture("particle", new ResourceLocation("create","block/brass_casing"))
            ))
            .lang("Multiblock Rotational Input")
            .register();

    public static final BlockEntityEntry<KineticInputTileEntity> KINETIC_INPUT_TILE_ENTITY = REGISTRATE
            .tileEntity("kinetic_input", KineticInputTileEntity::new)
            .instance(() -> ShaftInstance::new)
            .validBlocks(KINETIC_INPUT_BLOCK)
            .register();

    public static final BlockEntry<GhostBlock> GHOST_BLOCK = REGISTRATE.block("ghost", GhostBlock::new)
            .initialProperties(SharedProperties::copperMetal)
            .properties(BlockBehaviour.Properties::noOcclusion)
            .tag(BlockTags.NEEDS_IRON_TOOL)
            .transform(TagGen.pickaxeOnly())
            .blockstate((ctx, prov) -> prov.simpleBlock(ctx.getEntry(), prov.models().getBuilder("multiblock_ghost")
    //        .texture("particle", new ResourceLocation("create:block/brass_casing"))
            ))
            .lang("Multiblock")
            .register();

    public static final BlockEntityEntry<GhostTileEntity> GHOST_TILE_ENTITY = REGISTRATE
            .tileEntity("ghost", GhostTileEntity::new)
            .validBlocks(GHOST_BLOCK)
            .register();

    public static final BlockEntry<FluidOutputBlock> FLUID_OUTPUT_BLOCK = REGISTRATE.block("fluid_output", FluidOutputBlock::new)
            .initialProperties(SharedProperties::copperMetal)
            .properties(BlockBehaviour.Properties::noOcclusion)
            .tag(BlockTags.NEEDS_IRON_TOOL)
            .transform(TagGen.pickaxeOnly())
            .blockstate((ctx, prov) -> prov.simpleBlock(ctx.getEntry(), prov.models().getBuilder("multiblock_ghost")
    //        .texture("particle", new ResourceLocation("create:block/brass_casing"))
            ))
            .lang("Multiblock")
            .register();

    public static final BlockEntityEntry<FluidOutputTileEntity> FLUID_OUTPUT_TILE_ENTITY = REGISTRATE
            .tileEntity("fluid_output", FluidOutputTileEntity::new)
            .validBlocks(FLUID_OUTPUT_BLOCK)
            .register();

    public static final BlockEntry<MasterBlock> MASTER_BLOCK = REGISTRATE.block("master", MasterBlock::new)
            .initialProperties(SharedProperties::copperMetal)
            .properties(BlockBehaviour.Properties::noOcclusion)
            .tag(BlockTags.NEEDS_IRON_TOOL)
            .transform(TagGen.pickaxeOnly())
            .blockstate((ctx, prov) -> prov.simpleBlock(ctx.getEntry(), prov.models().getBuilder("multiblock_ghost")
    //        .texture("particle", new ResourceLocation("create:block/brass_casing"))
            ))
            .lang("Multiblock")
            .item(OilRigItem::new)
            .build()
            .register();

    public static final BlockEntityEntry<MasterTileEntity> MASTER_TILE_ENTITY = REGISTRATE
            .tileEntity("master", MasterTileEntity::new)
            .validBlocks(MASTER_BLOCK)
            .register();

    public static void register() {}
}
