package net.forsteri.createpetroleum.entry;

import com.jozufozu.flywheel.core.PartialModel;
import com.simibubi.create.content.contraptions.fluids.tank.FluidTankGenerator;
import com.simibubi.create.content.contraptions.fluids.tank.FluidTankModel;
import com.simibubi.create.content.contraptions.fluids.tank.FluidTankRenderer;
import com.simibubi.create.foundation.block.BlockStressDefaults;
import com.simibubi.create.foundation.data.*;
import com.tterrag.registrate.util.entry.BlockEntityEntry;
import com.tterrag.registrate.util.entry.BlockEntry;
import com.tterrag.registrate.util.entry.FluidEntry;
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
import net.forsteri.createpetroleum.content.oilRig.kineticInput.KineticInputInstance;
import net.forsteri.createpetroleum.content.oilRig.kineticInput.KineticInputTileEntity;
import net.forsteri.createpetroleum.content.oilRig.master.MasterBlock;
import net.forsteri.createpetroleum.content.oilRig.master.MasterTileEntity;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.Fluid;
import net.minecraftforge.fluids.FluidAttributes;
import net.minecraftforge.fluids.ForgeFlowingFluid;

import static com.simibubi.create.foundation.data.ModelGen.customItemModel;
import static com.simibubi.create.foundation.data.TagGen.pickaxeOnly;

@SuppressWarnings("unused")
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
            .addLayer(() -> RenderType::translucent)
            .blockstate((ctx, prov) -> prov.simpleBlock(ctx.getEntry(), prov.models().getExistingFile(new ResourceLocation("createpetroleum:multiblock_ghost"))))
            .lang("Multiblock Rotational Input")
            .register();

    public static final BlockEntityEntry<KineticInputTileEntity> KINETIC_INPUT_TILE_ENTITY = REGISTRATE
            .tileEntity("kinetic_input", KineticInputTileEntity::new)
            .instance(() -> KineticInputInstance::new)
            .validBlocks(KINETIC_INPUT_BLOCK)
            .register();

    public static final BlockEntry<GhostBlock> GHOST_BLOCK = REGISTRATE.block("ghost", GhostBlock::new)
            .initialProperties(SharedProperties::copperMetal)
            .properties(BlockBehaviour.Properties::noOcclusion)
            .tag(BlockTags.NEEDS_IRON_TOOL)
            .transform(TagGen.pickaxeOnly())
            .addLayer(() -> RenderType::cutoutMipped)
            .blockstate((ctx, prov) -> prov.simpleBlock(ctx.getEntry(), prov.models().getExistingFile(new ResourceLocation("createpetroleum:multiblock_ghost"))))
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
            .addLayer(() -> RenderType::translucent)
            .blockstate((ctx, prov) -> prov.simpleBlock(ctx.getEntry(), prov.models().getExistingFile(new ResourceLocation("createpetroleum:multiblock_ghost"))))
            .lang("Multiblock")
            .register();

    public static final BlockEntityEntry<FluidOutputTileEntity> FLUID_OUTPUT_TILE_ENTITY = REGISTRATE
            .tileEntity("fluid_output", FluidOutputTileEntity::new)
            .validBlocks(FLUID_OUTPUT_BLOCK)
            .register();

    public static final BlockEntry<MasterBlock> MASTER_BLOCK = REGISTRATE.block("oil_rig", MasterBlock::new)
            .initialProperties(SharedProperties::copperMetal)
            .properties(BlockBehaviour.Properties::noOcclusion)
            .tag(BlockTags.NEEDS_IRON_TOOL)
            .transform(TagGen.pickaxeOnly())
            .blockstate((ctx, prov) -> prov.horizontalBlock(ctx.getEntry(), prov.models().getExistingFile(new ResourceLocation("createpetroleum:oil_rig"))))
            .lang("Drilling Rig")
            .item(OilRigItem::new)
            .transform(customItemModel())
            .addLayer(() -> RenderType::translucent)
            .register();

    public static final BlockEntityEntry<MasterTileEntity> MASTER_TILE_ENTITY = REGISTRATE
            .tileEntity("oil_rig", MasterTileEntity::new)
            .validBlocks(MASTER_BLOCK)
            .register();

    public static final PartialModel ROTATING_PART = new PartialModel(new ResourceLocation("createpetroleum", "block/rotating_part"));

    public static final FluidEntry<ForgeFlowingFluid.Flowing> CRUDE_OIL =
            REGISTRATE.standardFluid("crude_oil", NoColorFluidAttributes::new)
                    .lang("Crude Oil")
                    .attributes(b -> b.viscosity(2000)
                            .density(1400))
                    .properties(p -> p.levelDecreasePerBlock(2)
                            .tickRate(25)
                            .slopeFindDistance(3)
                            .explosionResistance(100f))
                    .source(ForgeFlowingFluid.Source::new)
                    .bucket()
                    .build()
                    .register();




    private static class NoColorFluidAttributes extends FluidAttributes {

        protected NoColorFluidAttributes(Builder builder, Fluid fluid) {
            super(builder, fluid);
        }

        @Override
        public int getColor(BlockAndTintGetter world, BlockPos pos) {
            return 0x00ffffff;
        }

    }

    public static TranslatableComponent BIOME_NOT_SUPPORTED = REGISTRATE.addRawLang("tooltip.createpetroleum.oil_rig.biome_not_supported", "This biome is not supported by the oil rig");

    public static TranslatableComponent NO_SPEED = REGISTRATE.addRawLang("tooltip.createpetroleum.oil_rig.not_rotating", "Not Rotating");

    public static TranslatableComponent MENU = REGISTRATE.addRawLang("itemGroup.createpetroleum", "Create: Petro Powered");

    public static void register() {}
}
