package emitterbuilder.builder.influencers;

import com.jme3.input.event.MouseButtonEvent;
import emitter.influencers.GravityInfluencer;
import emitter.influencers.GravityInfluencer.GravityAlignment;
import emitter.influencers.ParticleInfluencer;
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
public class GravityWindow extends InfluencerWindow {
	TextField gravityX, gravityY, gravityZ, gravityMag;
	SelectBox gravityAlign;
	CheckBox gravityEnabled;
	
	public GravityWindow(EmitterBuilder builder, Screen screen) {
		super(builder,screen);
		populateWindow();
	}
	
	private void populateWindow() {
		// GravityWindow X label
	//	Layout.pos.set(Layout.x,Layout.y);
		Layout.dim.set(Layout.lWidthN,Layout.h);
		Label lGravX = Layout.getNewLabel(screen, "Gravity X: ");
		ic.addChild(lGravX);
		
		// GravityWindow X TextField
		Layout.incCol(lGravX);
		Layout.dim.set(Layout.floatW,Layout.h);
		gravityX = new TextField(screen, "gravityX", Layout.pos, Layout.dim);
		gravityX.setType(TextField.Type.NUMERIC);
		ic.addChild(gravityX);
		form.addFormElement(gravityX);
		
		Layout.incCol(gravityX);
		Layout.dim.set(Layout.lWidthS,Layout.h);
		Label lGravY = Layout.getNewLabel(screen, "Y: ");
		ic.addChild(lGravY);
		
		Layout.incCol(lGravY);
		Layout.dim.set(Layout.floatW,Layout.h);
		gravityY = new TextField(screen, "gravityY", Layout.pos, Layout.dim);
		gravityY.setType(TextField.Type.NUMERIC);
		ic.addChild(gravityY);
		form.addFormElement(gravityY);
		
		Layout.incCol(gravityY);
		Layout.dim.set(Layout.lWidthS,Layout.h);
		Label lGravZ = Layout.getNewLabel(screen, "Z: ");
		ic.addChild(lGravZ);
		
		Layout.incCol(lGravZ);
		Layout.dim.set(Layout.floatW,Layout.h);
		gravityZ = new TextField(screen, "gravityZ", Layout.pos, Layout.dim);
		gravityZ.setType(TextField.Type.NUMERIC);
		ic.addChild(gravityZ);
		form.addFormElement(gravityZ);
		
		Layout.incCol(gravityZ);
		Layout.dim.set(Layout.h,Layout.h);
		ButtonAdapter applyGravity = new ButtonAdapter(screen, "applyGravity", Layout.pos, Layout.dim) {
			@Override
			public void onButtonMouseLeftUp(MouseButtonEvent evt, boolean toggled) {
				if (builder.validateFloatValue(gravityX.getText()) &&
					builder.validateFloatValue(gravityY.getText()) &&
					builder.validateFloatValue(gravityZ.getText())) {
					GravityInfluencer gi = builder.emitter.getEmitter().getInfluencer(GravityInfluencer.class);
					gi.setGravity(
						Float.valueOf(gravityX.getText()),
						Float.valueOf(gravityY.getText()),
						Float.valueOf(gravityZ.getText())
					);
				}
			}
		};
		applyGravity.setButtonIcon(18, 18, screen.getStyle("Common").getString("arrowRight"));
		ic.addChild(applyGravity);
		form.addFormElement(applyGravity);
		
		Layout.incRow();
		Layout.dim.set(Layout.lWidthN,Layout.h);
		Label lAlign = Layout.getNewLabel(screen, "Gravity Alignment: ");
		ic.addChild(lAlign);
		
		Layout.incCol(lAlign);
		Layout.dim.set(Layout.sbWidth,Layout.h);
		gravityAlign = new SelectBox(screen, "sbAlign", Layout.pos, Layout.dim) {
			@Override
			public void onChange(int selectedIndex, Object value) {
				if (builder.emitter != null) {
					GravityInfluencer gi = builder.emitter.getEmitter().getInfluencer(GravityInfluencer.class);
					gi.setAlignment(GravityAlignment.valueOf(gravityAlign.getSelectedListItem().getCaption()));
				}
			}
		};
		for (GravityAlignment mode : GravityAlignment.values()) {
			gravityAlign.addListItem(mode.name(), mode);
		}
		ic.addChild(gravityAlign);
		form.addFormElement(gravityAlign);
		
		Layout.incRow();
		Layout.dim.set(Layout.lWidthN,Layout.h);
		Label lMag = Layout.getNewLabel(screen, "Megnitude: ");
		ic.addChild(lMag);
		
		Layout.incCol(lMag);
		Layout.dim.set(Layout.w,Layout.h);
		gravityMag = new TextField(screen, "gravityMag", Layout.pos, Layout.dim);
		gravityMag.setType(TextField.Type.NUMERIC);
		ic.addChild(gravityMag);
		form.addFormElement(gravityMag);
		
		Layout.incCol(gravityMag);
		Layout.dim.set(Layout.h,Layout.h);
		ButtonAdapter applyGravityMag = new ButtonAdapter(screen, "applyGravityMag", Layout.pos, Layout.dim) {
			@Override
			public void onButtonMouseLeftUp(MouseButtonEvent evt, boolean toggled) {
				if (builder.validateFloatValue(gravityMag.getText())) {
					GravityInfluencer gi = builder.emitter.getEmitter().getInfluencer(GravityInfluencer.class);
					gi.setMagnitude(Float.valueOf(gravityMag.getText()));
				}
			}
		};
		applyGravityMag.setButtonIcon(18, 18, screen.getStyle("Common").getString("arrowRight"));
		ic.addChild(applyGravityMag);
		form.addFormElement(applyGravityMag);
		
		Layout.incRow();
		Layout.dim.set(Layout.lWidthN,Layout.h);
		Label lGravEnabled = Layout.getNewLabel(screen, "Enabled: ");
		ic.addChild(lGravEnabled);
		
		Layout.incCol(lGravEnabled);
		Layout.dim.set(Layout.h,Layout.h);
		gravityEnabled = new CheckBox(screen, "gravityEnabled", Layout.pos, Layout.dim) {
			@Override
			public void onButtonMouseLeftUp(MouseButtonEvent evt, boolean toggled) {
				GravityInfluencer gi = builder.emitter.getEmitter().getInfluencer(GravityInfluencer.class);
				gi.setEnabled(toggled);
			}
		};
		ic.addChild(gravityEnabled);
		form.addFormElement(gravityEnabled);
		
		ic.sizeToContent();
		
		createWindow("Gravity Influencer");
	}
	
	@Override
	public void loadProperties(ParticleInfluencer influencer) {
		GravityInfluencer inf = (GravityInfluencer)influencer;
		
		gravityX.setText(String.valueOf(inf.getGravity().x));
		gravityY.setText(String.valueOf(inf.getGravity().y));
		gravityZ.setText(String.valueOf(inf.getGravity().z));
		gravityAlign.setSelectedByCaption(inf.getAlignment().name(), false);
		gravityMag.setText(String.valueOf(inf.getMagnitude()));
		gravityEnabled.setIsChecked(inf.isEnabled());
	}
	
	@Override
	public void showGUIWindow() {
		builder.getApplication().getStateManager().attach(this);
	}
	
}
