package emitterbuilder.builder.shapes;

import com.jme3.animation.LoopMode;
import com.jme3.input.event.MouseButtonEvent;
import com.jme3.scene.Node;
import emitterbuilder.builder.EmitterBuilder;
import emitterbuilder.builder.datastructures.EmitterStruct;
import emitterbuilder.gui.utils.Layout;
import emitterbuilder.gui.PropertiesWindow;
import tonegod.gui.controls.buttons.ButtonAdapter;
import tonegod.gui.controls.buttons.CheckBox;
import tonegod.gui.controls.lists.SelectBox;
import tonegod.gui.controls.text.Label;
import tonegod.gui.controls.text.TextField;
import tonegod.gui.core.Screen;

/**
 *
 * @author t0neg0d
 */
public class SetAnimation extends PropertiesWindow {
	TextField animSpeed, animBlend;
	CheckBox useAnim;
	ButtonAdapter closeAnim, setAnim;
	SelectBox animNames;
	
	public SetAnimation(final EmitterBuilder builder, Screen screen) {
		super(builder, screen);
		
		Layout.dim.set(Layout.lWidthN,Layout.h);
		Label lUseAnim = Layout.getNewLabel(screen, "Animate: ");
		ic.addChild(lUseAnim);
		
		Layout.incCol(lUseAnim);
		Layout.dim.set(Layout.h,Layout.h);
		useAnim = new CheckBox(screen, "useAnim", Layout.pos, Layout.dim);
		useAnim.setIsChecked(true);
		ic.addChild(useAnim);
		form.addFormElement(useAnim);
		
		Layout.incRow();
		Layout.dim.set(Layout.lWidthN,Layout.h);
		Label lAnimNames = Layout.getNewLabel(screen, "Select Animation: ");
		ic.addChild(lAnimNames);
		
		Layout.incCol(lAnimNames);
		Layout.dim.set(Layout.sbWidth,Layout.h);
		animNames = new SelectBox(screen, "animNames", Layout.pos, Layout.dim) {
			@Override
			public void onChange(int selectedIndex, Object value) {
				
			}
		};
		ic.addChild(animNames);
		form.addFormElement(animNames);
		
		Layout.incRow();
		Layout.dim.set(Layout.lWidthN,Layout.h);
		Label lAnimSpeed = Layout.getNewLabel(screen, "Set Speed: ");
		ic.addChild(lAnimSpeed);
		
		Layout.incCol(lAnimSpeed);
		Layout.dim.set(Layout.floatW,Layout.h);
		animSpeed = new TextField(screen, "animSpeed", Layout.pos, Layout.dim);
		animSpeed.setType(TextField.Type.NUMERIC);
		animSpeed.setText("1");
		ic.addChild(animSpeed);
		form.addFormElement(animSpeed);
		
		Layout.incCol(animSpeed);
		Layout.dim.set(Layout.lWidthM,Layout.h);
		Label lAnimBlend = Layout.getNewLabel(screen, "Blend: ");
		ic.addChild(lAnimBlend);
		
		Layout.incCol(lAnimBlend);
		Layout.dim.set(Layout.floatW,Layout.h);
		animBlend = new TextField(screen, "animBlend", Layout.pos, Layout.dim);
		animBlend.setType(TextField.Type.NUMERIC);
		animBlend.setText("1");
		ic.addChild(animBlend);
		form.addFormElement(animBlend);
		
		Layout.incRow();
		Layout.dim.set(Layout.bWidth,Layout.h);
		closeAnim = new ButtonAdapter(screen, "closeAnim", Layout.pos, Layout.dim) {
			@Override
			public void onButtonMouseLeftUp(MouseButtonEvent evt, boolean isToggled) {
				builder.getShapeProperties().closeAllShapeWindows();
			}
		};
		closeAnim.setText("Done");
		ic.addChild(closeAnim);
		form.addFormElement(closeAnim);
		
		ic.sizeToContent();
		
		Layout.pos.setX(ic.getWidth()+Layout.h-Layout.bWidth);
		Layout.dim.set(Layout.bWidth,Layout.h);
		setAnim = new ButtonAdapter(screen, "setAnim", Layout.pos, Layout.dim) {
			@Override
			public void onButtonMouseLeftUp(MouseButtonEvent evt, boolean isToggled) {
				if (useAnim.getIsChecked()) {
					if (builder.validateFloatValue(animBlend.getText()) &&
						builder.validateFloatValue(animSpeed.getText())) {
						Node n = builder.emitter.getEmitterShapeNode();
						builder.getRootNode().attachChild(n);
						builder.getShapeProperties().applyNewEmitterShape(builder.emitter.getEmitterMesh());
						builder.emitter.getAnimChannel().setAnim(
							animNames.getSelectedListItem().getCaption(),
							Float.valueOf(animBlend.getText())
						);
						builder.emitter.getAnimChannel().setSpeed(Float.valueOf(animSpeed.getText()));
						builder.emitter.getAnimChannel().setLoopMode(LoopMode.Loop);
						
						builder.emitter.setAnimate(true);
						builder.emitter.setAnimationName(animNames.getSelectedListItem().getCaption());
						builder.emitter.setAnimSpeed(Float.valueOf(animSpeed.getText()));
						builder.emitter.setBlendTime(Float.valueOf(animBlend.getText()));
					}
				} else {
					builder.getShapeProperties().applyNewEmitterShape(builder.emitter.getEmitterMesh());
					builder.emitter.setAnimate(false);
					builder.emitter.setAnimationName("");
					builder.emitter.setAnimSpeed(1.0f);
					builder.emitter.setBlendTime(1.0f);
				}
			}
		};
		setAnim.setText("Set");
		ic.addChild(setAnim);
		form.addFormElement(setAnim);
		
		ic.sizeToContent();
		
		createWindow("Set Emitter Shape Animation");
	}
	
	@Override
	public void loadProperties(EmitterStruct struct) {
		animNames.removeAllListItems();
		for (String animName : struct.getAnimControl().getAnimationNames()) {
			animNames.addListItem(animName, animName);
		}
	}
}
