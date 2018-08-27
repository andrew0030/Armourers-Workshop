package riskyken.armourersWorkshop.common.painting.tool;

import net.minecraftforge.fml.client.config.GuiSlider;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.nbt.NBTTagCompound;

public class ToolOptionIntensity extends AbstractToolOption {
    
    public ToolOptionIntensity(String optionName) {
        super(optionName);
    }

    @SideOnly(Side.CLIENT)
    @Override
    public int getDisplayWidth() {
        return 150;
    }

    @SideOnly(Side.CLIENT)
    @Override
    public int getDisplayHeight() {
        return 20;
    }

    @SideOnly(Side.CLIENT)
    @Override
    public GuiButton getGuiControl(int id, int x, int y, NBTTagCompound compound) {
        GuiSlider sliderControl = new GuiSlider(id, x, y, getLocalisedLabel() + " ", 1, 64, (Integer) readFromNBT(compound), null);
        sliderControl.showDecimal = false;
        return sliderControl;
    }

    @Override
    public Object readFromNBT(NBTTagCompound compound) {
        return readFromNBT(compound, 16);
    }
    
    @Override
    public Object readFromNBT(NBTTagCompound compound, Object value) {
        int intensityValue = (Integer) value;
        if (compound != null && compound.hasKey(optionName)) {
            intensityValue = compound.getInteger(optionName);
        }
        return intensityValue;
    }

    @Override
    public void writeToNBT(NBTTagCompound compound, GuiButton control) {
        GuiSlider sliderControl = (GuiSlider) control;
        writeToNBT(compound, sliderControl.getValueInt());
    }

    @Override
    public void writeToNBT(NBTTagCompound compound, Object value) {
        compound.setInteger(optionName, (Integer) value);
    }
}
