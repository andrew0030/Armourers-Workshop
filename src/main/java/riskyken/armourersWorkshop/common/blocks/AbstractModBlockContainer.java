package riskyken.armourersWorkshop.common.blocks;

import net.minecraft.block.BlockContainer;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import riskyken.armourersWorkshop.ArmourersWorkshop;
import riskyken.armourersWorkshop.common.lib.LibModInfo;

public abstract class AbstractModBlockContainer extends BlockContainer {

    public AbstractModBlockContainer(String name) {
        super(Material.IRON);
        setCreativeTab(ArmourersWorkshop.tabArmorersWorkshop);
        setHardness(3.0F);
        setSoundType(SoundType.METAL);
        setUnlocalizedName(name);
    }
    
    public AbstractModBlockContainer(String name, Material material, SoundType soundType, boolean addCreativeTab) {
        super(material);
        if (addCreativeTab) {
            setCreativeTab(ArmourersWorkshop.tabArmorersWorkshop);
        }
        setHardness(3.0F);
        setSoundType(SoundType.METAL);
        setUnlocalizedName(name);
    }
    
    @Override
    public String getUnlocalizedName() {
        return getModdedUnlocalizedName(super.getUnlocalizedName());
    }

    protected String getModdedUnlocalizedName(String unlocalizedName) {
        String name = unlocalizedName.substring(unlocalizedName.indexOf(".") + 1);
        return "tile." + LibModInfo.ID.toLowerCase() + ":" + name;
    }
}
