/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package emitterbuilder.builder.influencers;

import com.jme3.input.event.MouseButtonEvent;
import emitter.influencers.ParticleInfluencer;
import emitter.influencers.RadialVelocityInfluencer;
import emitter.influencers.RadialVelocityInfluencer.RadialPullAlignment;
import emitter.influencers.RadialVelocityInfluencer.RadialPullCenter;
import emitter.influencers.RadialVelocityInfluencer.RadialUpAlignment;
import emitterbuilder.builder.EmitterBuilder;
import emitterbuilder.gui.InfluencerWindow;
import emitterbuilder.gui.utils.Layout;
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
public class RadialWindow extends InfluencerWindow {
	SelectBox radAlign, radCenter, radUpAlign;
	CheckBox radDirRandom, radEnable;
	TextField radPull, tangForce;
	
	public RadialWindow(EmitterBuilder builder, Screen screen) {
		super(builder, screen);
		populateWindow();
	}
	
	private void populateWindow() {
		Layout.dim.set(Layout.lWidthN,Layout.h);
		Label lRadAlign = Layout.getNewLabel(screen, "Pull Alignment: ");
		ic.addChild(lRadAlign);
		
		Layout.incCol(lRadAlign);
		Layout.dim.set(Layout.bWidth,Layout.h);
		radAlign = new SelectBox(screen, "radAlign", Layout.pos, Layout.dim) {
			@Override
			public void onChange(int selectedIndex, Object value) {
				if (builder.emitter != null) {
					RadialVelocityInfluencer rvi = builder.emitter.getEmitter().getInfluencer(RadialVelocityInfluencer.class);
					rvi.setRadialPullAlignment(RadialPullAlignment.valueOf(radAlign.getSelectedListItem().getCaption()));
				}
			}
		};
		for (RadialPullAlignment mode : RadialPullAlignment.values()) {
			radAlign.addListItem(mode.name(), mode);
		}
		ic.addChild(radAlign);
		form.addFormElement(radAlign);
		
		Layout.incRow();
		Layout.dim.set(Layout.lWidthN,Layout.h);
		Label lRadCenter = Layout.getNewLabel(screen, "Pull Center: ");
		ic.addChild(lRadCenter);
		
		Layout.incCol(lRadCenter);
		Layout.dim.set(Layout.bWidth,Layout.h);
		radCenter = new SelectBox(screen, "radCenter", Layout.pos, Layout.dim) {
			@Override
			public void onChange(int selectedIndex, Object value) {
				if (builder.emitter != null) {
					RadialVelocityInfluencer rvi = builder.emitter.getEmitter().getInfluencer(RadialVelocityInfluencer.class);
					rvi.setRadialPullCenter(RadialPullCenter.valueOf(radCenter.getSelectedListItem().getCaption()));
				}
			}
		};
		for (RadialPullCenter mode : RadialPullCenter.values()) {
			radCenter.addListItem(mode.name(), mode);
		}
		ic.addChild(radCenter);
		form.addFormElement(radCenter);
		
		Layout.incRow();
		Layout.dim.set(Layout.lWidthN,Layout.h);
		Label lRadPull = Layout.getNewLabel(screen, "Radial Pull: ");
		ic.addChild(lRadPull);
		
		Layout.incCol(lRadPull);
		Layout.dim.set(Layout.floatW,Layout.h);
		radPull = new TextField(screen, "radPull", Layout.pos, Layout.dim);
		radPull.setType(TextField.Type.NUMERIC);
		ic.addChild(radPull);
		form.addFormElement(radPull);
		
		Layout.incCol(radPull);
		Layout.dim.set(Layout.h,Layout.h);
		ButtonAdapter applyRadPull = new ButtonAdapter(screen, "applyRadPull", Layout.pos, Layout.dim) {
			@Override
			public void onButtonMouseLeftUp(MouseButtonEvent evt, boolean toggled) {
				if (builder.validateFloatValue(radPull.getText())) {
					RadialVelocityInfluencer rvi = builder.emitter.getEmitter().getInfluencer(RadialVelocityInfluencer.class);
					rvi.setRadialPull(
						Float.valueOf(radPull.getText())
					);
				}
			}
		};
		applyRadPull.setButtonIcon(18, 18, screen.getStyle("Common").getString("arrowRight"));
		ic.addChild(applyRadPull);
		form.addFormElement(applyRadPull);
		
		Layout.incRow();
		Layout.dim.set(Layout.lWidthN,Layout.h);
		Label lRadUpAlign = Layout.getNewLabel(screen, "Up Alignment: ");
		ic.addChild(lRadUpAlign);
		
		Layout.incCol(lRadUpAlign);
		Layout.dim.set(Layout.bWidth,Layout.h);
		radUpAlign = new SelectBox(screen, "radUpAlign", Layout.pos, Layout.dim) {
			@Override
			public void onChange(int selectedIndex, Object value) {
				if (builder.emitter != null) {
					RadialVelocityInfluencer rvi = builder.emitter.getEmitter().getInfluencer(RadialVelocityInfluencer.class);
					rvi.setRadialUpAlignment(RadialUpAlignment.valueOf(radUpAlign.getSelectedListItem().getCaption()));
				}
			}
		};
		for (RadialUpAlignment mode : RadialUpAlignment.values()) {
			radUpAlign.addListItem(mode.name(), mode);
		}
		ic.addChild(radUpAlign);
		form.addFormElement(radUpAlign);
		
		Layout.incRow();
		Layout.dim.set(Layout.lWidthN,Layout.h);
		Label lTangForce = Layout.getNewLabel(screen, "Tangent Force: ");
		ic.addChild(lTangForce);
		
		Layout.incCol(lTangForce);
		Layout.dim.set(Layout.floatW,Layout.h);
		tangForce = new TextField(screen, "tangForce", Layout.pos, Layout.dim);
		tangForce.setType(TextField.Type.NUMERIC);
		ic.addChild(tangForce);
		form.addFormElement(tangForce);
		
		Layout.incCol(tangForce);
		Layout.dim.set(Layout.h,Layout.h);
		ButtonAdapter applyTangForce = new ButtonAdapter(screen, "applyTangForce", Layout.pos, Layout.dim) {
			@Override
			public void onButtonMouseLeftUp(MouseButtonEvent evt, boolean toggled) {
				if (builder.validateFloatValue(tangForce.getText())) {
					RadialVelocityInfluencer rvi = builder.emitter.getEmitter().getInfluencer(RadialVelocityInfluencer.class);
					rvi.setTangentForce(
						Float.valueOf(tangForce.getText())
					);
				}
			}
		};
		applyTangForce.setButtonIcon(18, 18, screen.getStyle("Common").getString("arrowRight"));
		ic.addChild(applyTangForce);
		form.addFormElement(applyTangForce);
		
		Layout.incRow();
		Layout.dim.set(Layout.lWidthN,Layout.h);
		Label lRadDirRandom = Layout.getNewLabel(screen, "Random Direction: ");
		ic.addChild(lRadDirRandom);
		
		Layout.incCol(lRadDirRandom);
		Layout.dim.set(Layout.h,Layout.h);
		radDirRandom = new CheckBox(screen, "radDirRandom", Layout.pos, Layout.dim) {
			@Override
			public void onButtonMouseLeftUp(MouseButtonEvent evt, boolean toggled) {
				RadialVelocityInfluencer rvi = builder.emitter.getEmitter().getInfluencer(RadialVelocityInfluencer.class);
				rvi.setUseRandomDirection(toggled);
			}
		};
		ic.addChild(radDirRandom);
		form.addFormElement(radDirRandom);
		
		Layout.incRow();
		Layout.dim.set(Layout.lWidthN,Layout.h);
		Label lRadEnable = Layout.getNewLabel(screen, "Enabled: ");
		ic.addChild(lRadEnable);
		
		Layout.incCol(lRadEnable);
		Layout.dim.set(Layout.h,Layout.h);
		radEnable = new CheckBox(screen, "radEnable", Layout.pos, Layout.dim) {
			@Override
			public void onButtonMouseLeftUp(MouseButtonEvent evt, boolean toggled) {
				RadialVelocityInfluencer rvi = builder.emitter.getEmitter().getInfluencer(RadialVelocityInfluencer.class);
				rvi.setEnabled(toggled);
			}
		};
		ic.addChild(radEnable);
		form.addFormElement(radEnable);
		
		ic.sizeToContent();
		ic.setWidth(ic.getWidth()+Layout.h);
		
		createWindow("Radial Velocity Influencer");
	}
	
	@Override
	public void loadProperties(ParticleInfluencer influencer) {
		RadialVelocityInfluencer inf = (RadialVelocityInfluencer)influencer;
		
		radAlign.setSelectedByCaption(inf.getRadialPullAlignment().name(), true);
		radPull.setText(String.valueOf(inf.getRadialPull()));
		radUpAlign.setSelectedByCaption(inf.getRadialUpAlignment().name(), true);
		tangForce.setText(String.valueOf(inf.getTangentForce()));
		radDirRandom.setIsChecked(inf.getUseRandomDirection());
		radEnable.setIsChecked(inf.isEnabled());
	}

	@Override
	public void showGUIWindow() {
		builder.getApplication().getStateManager().attach(this);
	}
	
}
