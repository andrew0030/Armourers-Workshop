package moe.plushie.armourers_workshop.common.holiday;

import java.util.Calendar;

import moe.plushie.armourers_workshop.common.config.ConfigHandler;
import moe.plushie.armourers_workshop.common.lib.LibModInfo;
import moe.plushie.armourers_workshop.utils.TranslateUtils;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class Holiday {
    
    private final String name;
    private Calendar startDate;
    private Calendar endDate;
    private boolean enabled = true;
    
    /**
     * Creates and new holiday that spans x number of hours.
     * @param name Name of the holiday.
     * @param dayOfMonth The day of the month this holiday takes place on.
     * @param month Month this holiday takes place on. Zero based 0 = Jan, 11 = Dec
     * @param lengthInDays Number of days this holiday lasts.
     * @param lengthInHours Number of hours this holiday lasts.
     */
    public Holiday(String name, int dayOfMonth, int month, int lengthInDays, int lengthInHours) {
        this.name = name;
        startDate = Calendar.getInstance();
        startDate.set(Calendar.MINUTE, 0);
        startDate.set(Calendar.HOUR_OF_DAY, 0);
        startDate.set(Calendar.MONTH, month);
        startDate.set(Calendar.DAY_OF_MONTH, dayOfMonth);
        endDate = (Calendar) startDate.clone();
        endDate.add(Calendar.DAY_OF_MONTH, lengthInDays);
        endDate.add(Calendar.HOUR_OF_DAY, lengthInHours);
        ModHolidays.getHolidays().add(this);
    }
    
    public String getName() {
        return name;
    }
    
    public Calendar getStartDate() {
        return startDate;
    }
    
    public void setStartDate(Calendar startDate) {
        this.startDate = startDate;
    }
    
    public Calendar getEndDate() {
        return endDate;
    }
    
    public void setEndDate(Calendar endDate) {
        this.endDate = endDate;
    }
    
    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }
    
    public ItemStack getGift(EntityPlayer player) {
        return ItemStack.EMPTY;
    }
    
    public boolean isEnabled() {
        return enabled;
    }
    
    public boolean isHolidayActive() {
        Calendar current = Calendar.getInstance();
        if (ConfigHandler.disableAllHolidayEvents) {
            return false;
        }
        if (enabled) {
            if (current.after(startDate) & current.before(endDate)) {
                return true;
            }
        }
        return false;
    }
    
    public boolean hasGift() {
        return false;
    }
    
    @SideOnly(Side.CLIENT)
    public String getLocalizedName() {
        return TranslateUtils.translate("holiday." + LibModInfo.ID + ":" + name);
    }
}
