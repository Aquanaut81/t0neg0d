package emitterbuilder.gui;

import emitterbuilder.builder.EmitterBuilder;
import emitterbuilder.builder.datastructures.EmitterStruct;
import tonegod.gui.core.Screen;

/**
 *
 * @author t0neg0d
 */
public abstract class PropertiesWindow extends GUIWindow {
	
	public PropertiesWindow(EmitterBuilder builder, Screen screen) {
		super(builder, screen);
	}
	
	public abstract void loadProperties(EmitterStruct struct);
}
