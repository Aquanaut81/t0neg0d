package emitterbuilder.gui;

import emitter.influencers.ParticleInfluencer;
import emitterbuilder.builder.EmitterBuilder;
import tonegod.gui.core.Screen;

/**
 *
 * @author t0neg0d
 */
public abstract class InfluencerWindow extends GUIWindow {
	
	public InfluencerWindow(EmitterBuilder builder, Screen screen) {
		super(builder, screen);
	}
	
	public abstract void loadProperties(ParticleInfluencer influencer);
	
	public abstract void showGUIWindow();
}
