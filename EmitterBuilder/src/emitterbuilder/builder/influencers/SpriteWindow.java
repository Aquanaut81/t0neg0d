package emitterbuilder.builder.influencers;

import com.jme3.font.BitmapFont;
import com.jme3.input.event.MouseButtonEvent;
import emitter.influencers.ParticleInfluencer;
import emitter.influencers.SpriteInfluencer;
import emitterbuilder.builder.EmitterBuilder;
import emitterbuilder.gui.InfluencerWindow;
import emitterbuilder.gui.utils.Layout;
import java.util.StringTokenizer;
import tonegod.gui.controls.buttons.ButtonAdapter;
import tonegod.gui.controls.buttons.CheckBox;
import tonegod.gui.controls.text.Label;
import tonegod.gui.controls.text.TextField;
import tonegod.gui.core.Screen;

/**
 *
 * @author t0neg0d
 */
public class SpriteWindow extends InfluencerWindow {
	TextField spriteSeq, spriteDur;
	CheckBox spriteAnim, spriteEnabled, spriteImgRandom;
	
	public SpriteWindow(EmitterBuilder builder, Screen screen) {
		super(builder,screen);
		populateWindow();
	}
	
	private void populateWindow() {
		Layout.dim.set(Layout.lWidthN,Layout.h);
		Label lSpriteAnim = Layout.getNewLabel(screen, "Animate: ");
		ic.addChild(lSpriteAnim);
		
		Layout.incCol(lSpriteAnim);
		Layout.dim.set(Layout.h,Layout.h);
		spriteAnim = new CheckBox(screen, "spriteAnim", Layout.pos, Layout.dim) {
			@Override
			public void onButtonMouseLeftUp(MouseButtonEvent evt, boolean toggled) {
				SpriteInfluencer si = builder.emitter.getEmitter().getInfluencer(SpriteInfluencer.class);
				si.setAnimate(toggled);
			}
		};
		ic.addChild(spriteAnim);
		form.addFormElement(spriteAnim);
		
		Layout.incRow();
		Layout.dim.set(Layout.lWidthN,Layout.h);
		Label lSpriteSeq = Layout.getNewLabel(screen, "Custom Frame Sequence: ");
		ic.addChild(lSpriteSeq);
		
		Layout.incCol(lSpriteSeq);
		Layout.dim.set(Layout.h,Layout.h);
		Layout.dim.set(Layout.sbWidth/2,Layout.h);
		spriteSeq = new TextField(screen, "spriteSeq", Layout.pos, Layout.dim);
		ic.addChild(spriteSeq);
		form.addFormElement(spriteSeq);
		
		Layout.incCol(spriteSeq);
		Layout.dim.set(Layout.h,Layout.h);
		ButtonAdapter applyFrameSeq = new ButtonAdapter(screen, "applyFrameSeq", Layout.pos, Layout.dim) {
			@Override
			public void onButtonMouseLeftUp(MouseButtonEvent evt, boolean toggled) {
				SpriteInfluencer si = builder.emitter.getEmitter().getInfluencer(SpriteInfluencer.class);
				if (spriteSeq.getText().equals("")) {
					si.clearFrameSequence();
				} else {
					StringTokenizer st = new StringTokenizer(spriteSeq.getText(),",");
					int total = st.countTokens();
					if (total > 1) {
						int[] frames = new int[total];
						int index = 0;
						int count = 0;
						while (st.hasMoreTokens()) {
							String next = st.nextToken();
							if (builder.validateIntegerValue(next)) {
								frames[index] = Integer.valueOf(next);
								count++;
							}
							index++;
						}
						if (count == total) {
							si.setFrameSequence(frames);
						}
					} else {
						si.clearFrameSequence();
					}
				//	if (builder.validateFloatValue(gravityMag.getText())) {
						
				//	}
				}
			}
		};
		applyFrameSeq.setButtonIcon(18, 18, screen.getStyle("Common").getString("arrowRight"));
		ic.addChild(applyFrameSeq);
		form.addFormElement(applyFrameSeq);
		
		Layout.incCol(applyFrameSeq);
		Layout.dim.set(300,Layout.h);
		Label lSpriteSeqExp = Layout.getNewLabel(screen, "NOTE: Comma delimited. 0-based index");
		lSpriteSeqExp.setTextAlign(BitmapFont.Align.Left);
		ic.addChild(lSpriteSeqExp);
		
		Layout.incRow();
		Layout.dim.set(Layout.lWidthN,Layout.h);
		Label lSpriteImgRandom = Layout.getNewLabel(screen, "Random Start Image: ");
		ic.addChild(lSpriteImgRandom);
		
		Layout.incCol(lSpriteImgRandom);
		Layout.dim.set(Layout.h,Layout.h);
		spriteImgRandom = new CheckBox(screen, "spriteImgRandom", Layout.pos, Layout.dim) {
			@Override
			public void onButtonMouseLeftUp(MouseButtonEvent evt, boolean toggled) {
				SpriteInfluencer si = builder.emitter.getEmitter().getInfluencer(SpriteInfluencer.class);
				si.setUseRandomStartImage(toggled);
			}
		};
		ic.addChild(spriteImgRandom);
		form.addFormElement(spriteImgRandom);
		
		Layout.incRow();
		Layout.dim.set(Layout.lWidthN,Layout.h);
		Label lSpriteEnabled = Layout.getNewLabel(screen, "Enabled: ");
		ic.addChild(lSpriteEnabled);
		
		Layout.incCol(lSpriteEnabled);
		Layout.dim.set(Layout.h,Layout.h);
		spriteEnabled = new CheckBox(screen, "spriteEnabled", Layout.pos, Layout.dim) {
			@Override
			public void onButtonMouseLeftUp(MouseButtonEvent evt, boolean toggled) {
				SpriteInfluencer si = builder.emitter.getEmitter().getInfluencer(SpriteInfluencer.class);
				si.setEnabled(toggled);
			}
		};
		ic.addChild(spriteEnabled);
		form.addFormElement(spriteEnabled);
		
		Layout.incRow();
		Layout.dim.set(Layout.lWidthN,Layout.h);
		Label lSpriteDur = Layout.getNewLabel(screen, "Set Fixed Duration: ");
		ic.addChild(lSpriteDur);
		
		Layout.incCol(lSpriteDur);
		Layout.dim.set(Layout.floatW,Layout.h);
		spriteDur = new TextField(screen, "spriteDur", Layout.pos, Layout.dim);
		spriteDur.setType(TextField.Type.NUMERIC);
		ic.addChild(spriteDur);
		form.addFormElement(spriteDur);
		
		Layout.incCol(spriteDur);
		Layout.dim.set(Layout.h,Layout.h);
		ButtonAdapter applySpriteDur = new ButtonAdapter(screen, "applySpriteDur", Layout.pos, Layout.dim) {
			@Override
			public void onButtonMouseLeftUp(MouseButtonEvent evt, boolean toggled) {
				if (builder.validateFloatValue(spriteDur.getText())) {
					SpriteInfluencer si = builder.emitter.getEmitter().getInfluencer(SpriteInfluencer.class);
					si.setFixedDuration(
						Float.valueOf(spriteDur.getText())
					);
				}
			}
		};
		applySpriteDur.setButtonIcon(18, 18, screen.getStyle("Common").getString("arrowRight"));
		ic.addChild(applySpriteDur);
		form.addFormElement(applySpriteDur);
		
		Layout.incCol(applySpriteDur);
		Layout.dim.set(300,Layout.h);
		Label lSpriteDurExp = Layout.getNewLabel(screen, "NOTE: A duration of 0 = fixed duration disabled.");
		lSpriteDurExp.setTextAlign(BitmapFont.Align.Left);
		ic.addChild(lSpriteDurExp);
		
		ic.sizeToContent();
		
		createWindow("Sprite Influencer");
	}
	
	@Override
	public void loadProperties(ParticleInfluencer influencer) {
		SpriteInfluencer inf = (SpriteInfluencer)influencer;
		
		SpriteInfluencer si = builder.emitter.getEmitter().getInfluencer(SpriteInfluencer.class);
		spriteAnim.setIsChecked(si.getAnimate());
		if (si.getFrameSequence() != null) {
			if (si.getFrameSequence().length > 0) {
				int[] frames = si.getFrameSequence();
				String f = "";
				for (int i = 0; i < frames.length; i++) {
					f += String.valueOf(frames[i]);
					if (i < frames.length-1)
						f += ",";
				}
				spriteSeq.setText(f);
			} else
				spriteSeq.setText("");
		}
		spriteImgRandom.setIsChecked(si.getUseRandomStartImage());
		spriteEnabled.setIsChecked(si.isEnabled());
	}

	@Override
	public void showGUIWindow() {
		builder.getApplication().getStateManager().attach(this);
	}
	
}
