package moe.plushie.armourers_workshop.common.items.paintingtool;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

import moe.plushie.armourers_workshop.api.common.painting.IPantable;
import moe.plushie.armourers_workshop.api.common.painting.IPantableBlock;
import moe.plushie.armourers_workshop.common.blocks.ModBlocks;
import moe.plushie.armourers_workshop.common.lib.LibItemNames;
import moe.plushie.armourers_workshop.common.lib.LibSounds;
import moe.plushie.armourers_workshop.common.network.PacketHandler;
import moe.plushie.armourers_workshop.common.network.messages.client.MessageClientToolPaintBlock;
import moe.plushie.armourers_workshop.common.painting.PaintType;
import moe.plushie.armourers_workshop.common.painting.tool.IConfigurableTool;
import moe.plushie.armourers_workshop.common.painting.tool.ToolOption;
import moe.plushie.armourers_workshop.common.painting.tool.ToolOptions;
import moe.plushie.armourers_workshop.common.tileentities.TileEntityArmourer;
import moe.plushie.armourers_workshop.common.undo.UndoManager;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ItemHueTool extends AbstractPaintingTool implements IConfigurableTool {

    public ItemHueTool() {
        super(LibItemNames.HUE_TOOL);
        setSortPriority(13);
    }

    @Override
    public EnumActionResult onItemUse(EntityPlayer player, World worldIn, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
        IBlockState state = worldIn.getBlockState(pos);
        ItemStack stack = player.getHeldItem(hand);

        if (player.isSneaking() & state.getBlock() == ModBlocks.colourMixer) {
            TileEntity te = worldIn.getTileEntity(pos);
            if (te != null && te instanceof IPantable) {
                if (!worldIn.isRemote) {
                    int colour = ((IPantable) te).getColour(0);
                    PaintType paintType = ((IPantable) te).getPaintType(0);
                    setToolColour(stack, colour);
                    setToolPaintType(stack, paintType);
                }
            }
            return EnumActionResult.SUCCESS;
        }

        if (state.getBlock() instanceof IPantableBlock) {
            if (!worldIn.isRemote) {
                UndoManager.begin(player);
            }

            if (ToolOptions.FULL_BLOCK_MODE.getValue(stack)) {
                for (int i = 0; i < 6; i++) {
                    usedOnBlockSide(stack, player, worldIn, pos, state.getBlock(), EnumFacing.VALUES[i]);
                }
            } else {
                usedOnBlockSide(stack, player, worldIn, pos, state.getBlock(), facing);
            }
            if (!worldIn.isRemote) {
                UndoManager.end(player);
                worldIn.playSound(null, pos, new SoundEvent(new ResourceLocation(LibSounds.BURN)), SoundCategory.BLOCKS, 1.0F, worldIn.rand.nextFloat() * 0.1F + 0.9F);
            }

            return EnumActionResult.SUCCESS;
        }

        if (state.getBlock() == ModBlocks.armourerBrain & player.isSneaking()) {
            if (!worldIn.isRemote) {
                TileEntity te = worldIn.getTileEntity(pos);
                if (te != null && te instanceof TileEntityArmourer) {
                    ((TileEntityArmourer) te).toolUsedOnArmourer(this, worldIn, stack, player);
                }
            }
            return EnumActionResult.SUCCESS;
        }

        return EnumActionResult.PASS;
    }

    @SuppressWarnings("deprecation")
    @Override
    public void usedOnBlockSide(ItemStack stack, EntityPlayer player, World world, BlockPos pos, Block block, EnumFacing facing) {
        boolean changeHue = ToolOptions.CHANGE_HUE.getValue(stack);
        boolean changeSaturation = ToolOptions.CHANGE_SATURATION.getValue(stack);
        boolean changeBrightness = ToolOptions.CHANGE_BRIGHTNESS.getValue(stack);
        boolean changePaintType = ToolOptions.CHANGE_PAINT_TYPE.getValue(stack);

        Color toolColour = new Color(getToolColour(stack));
        PaintType paintType = getToolPaintType(stack);
        float[] toolhsb;
        toolhsb = Color.RGBtoHSB(toolColour.getRed(), toolColour.getGreen(), toolColour.getBlue(), null);
        IPantableBlock worldColourable = (IPantableBlock) block;

        if (worldColourable.isRemoteOnly(world, pos, facing) & world.isRemote) {
            int oldColour = worldColourable.getColour(world, pos, facing);
            byte oldPaintType = (byte) worldColourable.getPaintType(world, pos, facing).getKey();
            float[] blockhsb;
            Color blockColour = new Color(oldColour);
            blockhsb = Color.RGBtoHSB(blockColour.getRed(), blockColour.getGreen(), blockColour.getBlue(), null);

            float[] recolour = new float[] { blockhsb[0], blockhsb[1], blockhsb[2] };
            if (changeHue) {
                recolour[0] = toolhsb[0];
            }
            if (changeSaturation) {
                recolour[1] = toolhsb[1];
            }
            if (changeBrightness) {
                recolour[2] = toolhsb[2];
            }

            int newColour = Color.HSBtoRGB(recolour[0], recolour[1], recolour[2]);
            Color c = new Color(newColour);
            byte[] rgbt = new byte[4];
            rgbt[0] = (byte) c.getRed();
            rgbt[1] = (byte) c.getGreen();
            rgbt[2] = (byte) c.getBlue();
            rgbt[3] = oldPaintType;
            if (changePaintType) {
                rgbt[3] = (byte) paintType.getKey();
            }
            MessageClientToolPaintBlock message = new MessageClientToolPaintBlock(pos, facing, rgbt);
            PacketHandler.networkWrapper.sendToServer(message);
        } else if (!worldColourable.isRemoteOnly(world, pos, facing) & !world.isRemote) {
            int oldColour = worldColourable.getColour(world, pos, facing);
            byte oldPaintType = (byte) worldColourable.getPaintType(world, pos, facing).getKey();
            float[] blockhsb;
            Color blockColour = new Color(oldColour);
            blockhsb = Color.RGBtoHSB(blockColour.getRed(), blockColour.getGreen(), blockColour.getBlue(), null);

            float[] recolour = new float[] { blockhsb[0], blockhsb[1], blockhsb[2] };
            if (changeHue) {
                recolour[0] = toolhsb[0];
            }
            if (changeSaturation) {
                recolour[1] = toolhsb[1];
            }
            if (changeBrightness) {
                recolour[2] = toolhsb[2];
            }

            int newColour = Color.HSBtoRGB(recolour[0], recolour[1], recolour[2]);

            UndoManager.blockPainted(player, world, pos, oldColour, oldPaintType, facing);

            ((IPantableBlock) block).setColour(world, pos, newColour, facing);
            if (changePaintType) {
                ((IPantableBlock) block).setPaintType(world, pos, paintType, facing);
            }
        }
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void addInformation(ItemStack stack, World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
        super.addInformation(stack, worldIn, tooltip, flagIn);
        addOpenSettingsInformation(stack, worldIn, tooltip, flagIn);
    }

    @Override
    public void getToolOptions(ArrayList<ToolOption<?>> toolOptionList) {
        toolOptionList.add(ToolOptions.CHANGE_HUE);
        toolOptionList.add(ToolOptions.CHANGE_SATURATION);
        toolOptionList.add(ToolOptions.CHANGE_BRIGHTNESS);
        toolOptionList.add(ToolOptions.CHANGE_PAINT_TYPE);
        toolOptionList.add(ToolOptions.FULL_BLOCK_MODE);
    }
}