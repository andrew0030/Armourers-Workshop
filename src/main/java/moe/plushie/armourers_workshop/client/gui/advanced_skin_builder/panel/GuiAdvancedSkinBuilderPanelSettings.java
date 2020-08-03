package moe.plushie.armourers_workshop.client.gui.advanced_skin_builder.panel;

import java.util.ArrayList;

import org.lwjgl.opengl.GL11;

import moe.plushie.armourers_workshop.api.common.skin.data.ISkinIdentifier;
import moe.plushie.armourers_workshop.client.gui.advanced_skin_builder.GuiAdvancedSkinBuilder;
import moe.plushie.armourers_workshop.client.gui.advanced_skin_builder.GuiAdvancedSkinBuilder.GuiTreeViewPartNote;
import moe.plushie.armourers_workshop.client.gui.advanced_skin_builder.GuiAdvancedSkinBuilder.PartNodeSetting;
import moe.plushie.armourers_workshop.client.gui.controls.GuiCheckBox;
import moe.plushie.armourers_workshop.client.gui.controls.GuiCustomSlider;
import moe.plushie.armourers_workshop.client.gui.controls.GuiPanel;
import moe.plushie.armourers_workshop.client.gui.controls.GuiTextFieldCustom;
import moe.plushie.armourers_workshop.client.render.ModRenderHelper;
import moe.plushie.armourers_workshop.client.render.SkinItemRenderHelper;
import moe.plushie.armourers_workshop.common.skin.data.SkinDescriptor;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.fml.client.config.GuiSlider;
import net.minecraftforge.fml.client.config.GuiSlider.ISlider;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class GuiAdvancedSkinBuilderPanelSettings extends GuiPanel implements ISlider {

    private PartNodeSetting nodeSetting = null;

    private GuiTextFieldCustom textSetting;
    private ISkinIdentifier identifierSetting;
    private GuiCheckBox checkBoxSetting;
    private GuiCustomSlider sliderSetting;
    private GuiCustomSlider sliderSettingX;
    private GuiCustomSlider sliderSettingY;
    private GuiCustomSlider sliderSettingZ;

    public GuiAdvancedSkinBuilderPanelSettings(GuiScreen parent, int x, int y, int width, int height) {
        super(parent, x, y, width, height);
        // TODO Auto-generated constructor stub
    }

    @Override
    public void initGui() {
        super.initGui();
        buttonList.clear();

        textSetting = new GuiTextFieldCustom(x + GuiAdvancedSkinBuilder.PADDING, y + GuiAdvancedSkinBuilder.PADDING + 10, 100, 16);

        checkBoxSetting = new GuiCheckBox(0, x + GuiAdvancedSkinBuilder.PADDING, y + GuiAdvancedSkinBuilder.PADDING + 10, "", false);
        checkBoxSetting.setTextColour(0xFFFFFFFF);
        buttonList.add(checkBoxSetting);
        int sliderPosX = x + GuiAdvancedSkinBuilder.PADDING;
        int sliderPosY = y + GuiAdvancedSkinBuilder.PADDING + 10;
        sliderSetting = new GuiCustomSlider(0, sliderPosX, sliderPosY, 100, 10, "Scale:", "", 1, 64, 0, false, true, this).setFineTuneButtons(true);
        sliderSettingX = new GuiCustomSlider(0, sliderPosX, sliderPosY, 100, 10, "X:", "", -64, 64, 0, false, true, this).setFineTuneButtons(true);
        sliderSettingY = new GuiCustomSlider(0, sliderPosX, sliderPosY + 15, 100, 10, "Y:", "", -64, 64, 0, false, true, this).setFineTuneButtons(true);
        sliderSettingZ = new GuiCustomSlider(0, sliderPosX, sliderPosY + 30, 100, 10, "Z:", "", -64, 64, 0, false, true, this).setFineTuneButtons(true);
        buttonList.add(sliderSetting);
        buttonList.add(sliderSettingX);
        buttonList.add(sliderSettingY);
        buttonList.add(sliderSettingZ);
    }

    @Override
    public void update() {
        textSetting.updateCursorCounter();
    }

    @Override
    public boolean keyTyped(char c, int keycode) {
        if (textSetting.keyTyped(c, keycode)) {
            applySetting();
            return true;
        } else {
            return super.keyTyped(c, keycode);
        }
    }

    @Override
    public boolean mouseClicked(int mouseX, int mouseY, int button) {
        textSetting.mouseClicked(mouseX, mouseY, button);
        return super.mouseClicked(mouseX, mouseY, button);
    }
    
    private ArrayList<ISkinIdentifier> getSkinIdentifier() {
        return ((GuiAdvancedSkinBuilder) parent).getListOfSkins();
    }

    @Override
    public void drawBackground(int mouseX, int mouseY, float partialTickTime) {
        if (!visible) {
            return;
        }
        textSetting.drawButton(mc, mouseX, mouseY, partialTickTime);
        fontRenderer.drawString("Settings:", x + GuiAdvancedSkinBuilder.PADDING, y + GuiAdvancedSkinBuilder.PADDING, 0xCCFFFFFF);
    }

    @Override
    public void drawForeground(int mouseX, int mouseY, float partialTickTime) {
        if (!visible) {
            return;
        }
        if (nodeSetting != null && nodeSetting == PartNodeSetting.SKIN) {
            //ModLogger.log("pie");
            ArrayList<ISkinIdentifier> skinIdentifiers = getSkinIdentifier();
            GlStateManager.pushMatrix();
            GlStateManager.pushAttrib();

            RenderHelper.enableGUIStandardItemLighting();
            GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
            GlStateManager.disableNormalize();
            GlStateManager.disableColorMaterial();
            GlStateManager.enableNormalize();
            GlStateManager.enableColorMaterial();
            ModRenderHelper.enableAlphaBlend();
            GlStateManager.enableDepth();

            for (int i = 0; i < skinIdentifiers.size(); i++) {
                GlStateManager.pushMatrix();
                GlStateManager.translate(x + 16 + i * 18, y + 20, 500);

                float scale = 16;
                GlStateManager.scale((-scale), scale, scale);

                GL11.glRotatef(-40, 1.0F, 0.0F, 0.0F);
                float rotation = (float) ((double) System.currentTimeMillis() / 10 % 360);
                GL11.glRotatef(rotation, 0.0F, 1.0F, 0.0F);

                SkinItemRenderHelper.renderSkinAsItem(new SkinDescriptor(skinIdentifiers.get(i)), true, false, 16, 16);
                //SkinItemRenderHelper.renderSkinWithoutHelper(skinIdentifiers.get(i), false);
                GlStateManager.popMatrix();
            }

            ModRenderHelper.enableAlphaBlend();

            GlStateManager.disablePolygonOffset();
            GlStateManager.disableDepth();
            GlStateManager.disableNormalize();
            GlStateManager.disableColorMaterial();
            RenderHelper.disableStandardItemLighting();
            GlStateManager.resetColor();

            GlStateManager.popAttrib();
            GlStateManager.popMatrix();
        }
    }

    @Override
    protected void actionPerformed(GuiButton button) {
        if (button == checkBoxSetting) {
            applySetting();
        }
    }

    @Override
    public void onChangeSliderValue(GuiSlider slider) {
        applySetting();
    }

    @Override
    public void draw(int mouseX, int mouseY, float partialTickTime) {
        // TODO Auto-generated method stub
        super.draw(mouseX, mouseY, partialTickTime);
    }

    public void updateSliders(Vec3d vec3d) {
        updateSliders(vec3d.x, vec3d.y, vec3d.z);
    }

    public void updateSliders(double x, double y, double z) {
        sliderSettingX.visible = true;
        sliderSettingY.visible = true;
        sliderSettingZ.visible = true;

        sliderSettingX.setValue(x);
        sliderSettingY.setValue(y);
        sliderSettingZ.setValue(z);

        sliderSettingX.updateSlider();
        sliderSettingY.updateSlider();
        sliderSettingZ.updateSlider();
    }

    public void updateSetting(PartNodeSetting nodeSetting, GuiTreeViewPartNote treeViewPartNote) {
        textSetting.visible = false;
        identifierSetting = null;
        checkBoxSetting.visible = false;
        sliderSetting.visible = false;
        sliderSettingX.visible = false;
        sliderSettingY.visible = false;
        sliderSettingZ.visible = false;
        this.nodeSetting = null;

        if (nodeSetting == null | treeViewPartNote == null) {
            return;
        }
        
        this.nodeSetting = nodeSetting;

        switch (nodeSetting) {
        case NAME:
            textSetting.setText(treeViewPartNote.getAdvancedPartNode().name);
            textSetting.visible = true;
            textSetting.enabled = !treeViewPartNote.isLocked();
            break;
        case SKIN:
            identifierSetting = treeViewPartNote.getSkinIdentifier();
            break;
        case ENABLED:
            checkBoxSetting.visible = true;
            checkBoxSetting.displayString = "Enabled?";
            checkBoxSetting.setIsChecked(treeViewPartNote.getAdvancedPartNode().enabled);
            break;
        case SCALE:
            sliderSetting.visible = true;
            sliderSetting.setValue(treeViewPartNote.getAdvancedPartNode().scale);
            sliderSetting.updateSlider();
            break;
        case MIRROR:
            checkBoxSetting.visible = true;
            checkBoxSetting.displayString = "Mirror?";
            checkBoxSetting.setIsChecked(treeViewPartNote.getAdvancedPartNode().mirror);
            break;
        case POSITION:
            updateSliders(treeViewPartNote.getAdvancedPartNode().pos);
            break;
        case ROTATION:
            updateSliders(treeViewPartNote.getAdvancedPartNode().rotationAngle);
            break;
        case ROTATION_POSITION:
            updateSliders(treeViewPartNote.getAdvancedPartNode().rotationPos);
            break;
        }
    }

    public void applySetting(PartNodeSetting nodeSetting, GuiTreeViewPartNote treeViewPartNote) {
        if (nodeSetting == null | treeViewPartNote == null) {
            return;
        }

        switch (nodeSetting) {
        case NAME:
            treeViewPartNote.setName(textSetting.getText());
            break;
        case SKIN:
            treeViewPartNote.setSkinIdentifier(identifierSetting);
            break;
        case ENABLED:
            treeViewPartNote.getAdvancedPartNode().enabled = checkBoxSetting.isChecked();
            break;
        case SCALE:
            treeViewPartNote.getAdvancedPartNode().scale = sliderSetting.getValueInt();
            break;
        case MIRROR:
            treeViewPartNote.getAdvancedPartNode().mirror = checkBoxSetting.isChecked();
            break;
        case POSITION:
            treeViewPartNote.getAdvancedPartNode().pos = new Vec3d(sliderSettingX.getValueInt(), sliderSettingY.getValueInt(), sliderSettingZ.getValueInt());
            break;
        case ROTATION:
            treeViewPartNote.getAdvancedPartNode().rotationAngle = new Vec3d(sliderSettingX.getValueInt(), sliderSettingY.getValueInt(), sliderSettingZ.getValueInt());
            break;
        case ROTATION_POSITION:
            treeViewPartNote.getAdvancedPartNode().rotationPos = new Vec3d(sliderSettingX.getValueInt(), sliderSettingY.getValueInt(), sliderSettingZ.getValueInt());
            break;
        }
    }

    private void applySetting() {
        ((GuiAdvancedSkinBuilder) parent).applySetting();
    }
}
