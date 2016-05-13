package emitterbuilder.builder.influencers;

import com.jme3.input.event.MouseButtonEvent;
import emitter.influencers.ImpulseInfluencer;
import emitter.influencers.ParticleInfluencer;
import emitterbuilder.builder.EmitterBuilder;
import emitterbuilder.gui.InfluencerWindow;
import emitterbuilder.gui.utils.Layout;
import tonegod.gui.controls.buttons.ButtonAdapter;
import tonegod.gui.controls.buttons.CheckBox;
import tonegod.gui.controls.text.Label;
import tonegod.gui.controls.text.TextField;
import tonegod.gui.core.Screen;

/**
 *
 * @author t0neg0d
 */
public class ImpulseWindow extends InfluencerWindow {
	TextField impChance, impStr, impMag;
	CheckBox impEnabled;
	
	public ImpulseWindow(EmitterBuilder builder, Screen screen) {
		super(builder,screen);
		populateWindow();
	}
	
	private void populateWindow() {
		Layout.dim.set(Layout.lWidthN,Layout.h);
		Label lImpChance = Layout.getNewLabel(screen, "Chance: ");
		ic.addChild(lImpChance);
		
		Layout.incCol(lImpChance);
		Layout.dim.set(Layout.w,Layout.h);
		impChance = new TextField(screen, "impChance", Layout.pos, Layout.dim);
		impChance.setType(TextField.Type.NUMERIC);
		ic.addChild(impChance);
		form.addFormElement(impChance);
		
		Layout.incCol(impChance);
		Layout.dim.set(Layout.h,Layout.h);
		ButtonAdapter applyImpChance = new ButtonAdapter(screen, "applyImpChance", Layout.pos, Layout.dim) {
			@Override
			public void onButtonMouseLeftUp(MouseButtonEvent evt, boolean toggled) {
				if (builder.validateFloatValue(impChance.getText())) {
					ImpulseInfluencer ii = builder.emitter.getEmitter().getInfluencer(ImpulseInfluencer.class);
					ii.setChance(Float.valueOf(impChance.getText()));
				}
			}
		};
		applyImpChance.setButtonIcon(18, 18, screen.getStyle("Common").getString("arrowRight"));
		ic.addChild(applyImpChance);
		form.addFormElement(applyImpChance);
		
		Layout.incRow();
		Layout.dim.set(Layout.lWidthN,Layout.h);
		Label lImpStr = Layout.getNewLabel(screen, "Strength: ");
		ic.addChild(lImpStr);
		
		Layout.incCol(lImpStr);
		Layout.dim.set(Layout.w,Layout.h);
		impStr = new TextField(screen, "impStr", Layout.pos, Layout.dim);
		impStr.setType(TextField.Type.NUMERIC);
		ic.addChild(impStr);
		form.addFormElement(impStr);
		
		Layout.incCol(impStr);
		Layout.dim.set(Layout.h,Layout.h);
		ButtonAdapter applyImpStr = new ButtonAdapter(screen, "applyImpStr", Layout.pos, Layout.dim) {
			@Override
			public void onButtonMouseLeftUp(MouseButtonEvent evt, boolean toggled) {
				if (builder.validateFloatValue(impStr.getText())) {
					ImpulseInfluencer ii = builder.emitter.getEmitter().getInfluencer(ImpulseInfluencer.class);
					ii.setStrength(Float.valueOf(impStr.getText()));
				}
			}
		};
		applyImpStr.setButtonIcon(18, 18, screen.getStyle("Common").getString("arrowRight"));
		ic.addChild(applyImpStr);
		form.addFormElement(applyImpStr);
		
		Layout.incRow();
		Layout.dim.set(Layout.lWidthN,Layout.h);
		Label lImpMag = Layout.getNewLabel(screen, "Magnitude: ");
		ic.addChild(lImpMag);
		
		Layout.incCol(lImpMag);
		Layout.dim.set(Layout.w,Layout.h);
		impMag = new TextField(screen, "impMag", Layout.pos, Layout.dim);
		impMag.setType(TextField.Type.NUMERIC);
		ic.addChild(impMag);
		form.addFormElement(impMag);
		
		Layout.incCol(impMag);
		Layout.dim.set(Layout.h,Layout.h);
		ButtonAdapter applyImpMag = new ButtonAdapter(screen, "applyImpMag", Layout.pos, Layout.dim) {
			@Override
			public void onButtonMouseLeftUp(MouseButtonEvent evt, boolean toggled) {
				if (builder.validateFloatValue(impMag.getText())) {
					ImpulseInfluencer ii = builder.emitter.getEmitter().getInfluencer(ImpulseInfluencer.class);
					ii.setMagnitude(Float.valueOf(impMag.getText()));
				}
			}
		};
		applyImpMag.setButtonIcon(18, 18, screen.getStyle("Common").getString("arrowRight"));
		ic.addChild(applyImpMag);
		form.addFormElement(applyImpMag);
		
		Layout.incRow();
		Layout.dim.set(Layout.lWidthN,Layout.h);
		Label lImpEnabled = Layout.getNewLabel(screen, "Enabled: ");
		ic.addChild(lImpEnabled);
		
		Layout.incCol(lImpEnabled);
		Layout.dim.set(Layout.h,Layout.h);
		impEnabled = new CheckBox(screen, "impEnabled", Layout.pos, Layout.dim) {
			@Override
			public void onButtonMouseLeftUp(MouseButtonEvent evt, boolean toggled) {
				ImpulseInfluencer ii = builder.emitter.getEmitter().getInfluencer(ImpulseInfluencer.class);
				ii.setEnabled(toggled);
			}
		};
		ic.addChild(impEnabled);
		form.addFormElement(impEnabled);
		
		ic.sizeToContent();
		
		createWindow("Impulse Influencer");
	}

	@Override
	public void loadProperties(ParticleInfluencer influencer) {
		ImpulseInfluencer inf = (ImpulseInfluencer)influencer;
		
		impChance.setText(String.valueOf(inf.getChance()));
		impStr.setText(String.valueOf(inf.getStrength()));
		impMag.setText(String.valueOf(inf.getMagnitude()));
		impEnabled.setIsChecked(inf.isEnabled());
	}

	@Override
	public void showGUIWindow() {
		builder.getApplication().getStateManager().attach(this);
	}
}
