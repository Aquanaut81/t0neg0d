package emitterbuilder.builder.influencers;

import com.jme3.input.event.MouseButtonEvent;
import emitter.influencers.ParticleInfluencer;
import emitter.influencers.PhysicsInfluencer;
import emitter.influencers.PhysicsInfluencer.CollisionReaction;
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
public class PhysicsWindow extends InfluencerWindow {
	SelectBox phyCol;
	TextField phyRest;
	CheckBox phyEnabled;
	
	public PhysicsWindow(EmitterBuilder builder, Screen screen) {
		super(builder, screen);
		populateWindow();
	}
	
	private void populateWindow() {
		Layout.reset();
		Layout.dim.set(Layout.lWidthN,Layout.h);
		Label lPhyCol = Layout.getNewLabel(screen, "Collision Reaction: ");
		ic.addChild(lPhyCol);
		
		Layout.incCol(lPhyCol);
		Layout.dim.set(Layout.bWidth,Layout.h);
		phyCol = new SelectBox(screen, "phyCol", Layout.pos, Layout.dim) {
			@Override
			public void onChange(int selectedIndex, Object value) {
				if (builder.emitter != null) {
					PhysicsInfluencer pi = builder.emitter.getEmitter().getInfluencer(PhysicsInfluencer.class);
					pi.setCollisionReaction(CollisionReaction.valueOf(phyCol.getSelectedListItem().getCaption()));
				}
			}
		};
		for (CollisionReaction mode : CollisionReaction.values()) {
			phyCol.addListItem(mode.name(), mode);
		}
		ic.addChild(phyCol);
		form.addFormElement(phyCol);
		
		Layout.incRow();
		Layout.dim.set(Layout.lWidthN,Layout.h);
		Label lPhyRest = Layout.getNewLabel(screen, "Restitution: ");
		ic.addChild(lPhyRest);
		
		Layout.incCol(lPhyRest);
		Layout.dim.set(Layout.floatW,Layout.h);
		phyRest = new TextField(screen, "phyRest", Layout.pos, Layout.dim);
		phyRest.setType(TextField.Type.NUMERIC);
		ic.addChild(phyRest);
		form.addFormElement(phyRest);
		
		Layout.incCol(phyRest);
		Layout.dim.set(Layout.h,Layout.h);
		ButtonAdapter applyPhyRest = new ButtonAdapter(screen, "applyPhyRest", Layout.pos, Layout.dim) {
			@Override
			public void onButtonMouseLeftUp(MouseButtonEvent evt, boolean toggled) {
				if (builder.validateFloatValue(phyRest.getText())) {
					PhysicsInfluencer pi = builder.emitter.getEmitter().getInfluencer(PhysicsInfluencer.class);
					pi.setRestitution(
						Float.valueOf(phyRest.getText())
					);
				}
			}
		};
		applyPhyRest.setButtonIcon(18, 18, screen.getStyle("Common").getString("arrowRight"));
		ic.addChild(applyPhyRest);
		form.addFormElement(applyPhyRest);
		
		Layout.incRow();
		Layout.dim.set(Layout.lWidthN,Layout.h);
		Label lPhyEnable = Layout.getNewLabel(screen, "Enabled: ");
		ic.addChild(lPhyEnable);
		
		Layout.incCol(lPhyEnable);
		Layout.dim.set(Layout.h,Layout.h);
		phyEnabled = new CheckBox(screen, "phyEnabled", Layout.pos, Layout.dim) {
			@Override
			public void onButtonMouseLeftUp(MouseButtonEvent evt, boolean toggled) {
				PhysicsInfluencer pi = builder.emitter.getEmitter().getInfluencer(PhysicsInfluencer.class);
				pi.setEnabled(toggled);
			}
		};
		ic.addChild(phyEnabled);
		form.addFormElement(phyEnabled);
		
		ic.sizeToContent();
		ic.setWidth(ic.getWidth()+Layout.h);
		
		createWindow("Physics Influencer");
	}
	
	@Override
	public void loadProperties(ParticleInfluencer influencer) {
		PhysicsInfluencer inf = (PhysicsInfluencer)influencer;
		
		phyCol.setSelectedByCaption(inf.getCollisionReaction().name(), true);
		phyRest.setText(String.valueOf(inf.getRestitution()));
		phyEnabled.setIsChecked(inf.isEnabled());
	}

	@Override
	public void showGUIWindow() {
		builder.getApplication().getStateManager().attach(this);
	}
}
