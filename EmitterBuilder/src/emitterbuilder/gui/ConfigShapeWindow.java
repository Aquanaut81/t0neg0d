package emitterbuilder.gui;

import emitterbuilder.builder.EmitterBuilder;
import tonegod.gui.core.Screen;

/**
 *
 * @author t0neg0d
 */
public abstract class ConfigShapeWindow extends PropertiesWindow {
	public ConfigShapeWindow(EmitterBuilder builder, Screen screen) {
		super(builder, screen);
	}
	
	protected abstract void createPrimitive();
}
