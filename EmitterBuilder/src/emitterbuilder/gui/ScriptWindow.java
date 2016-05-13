package emitterbuilder.gui;

import emitterbuilder.builder.EmitterBuilder;
import emitterbuilder.builder.scripts.ScriptBuilder.ScriptFunction;
import emitterbuilder.builder.datastructures.ScriptStruct;
import tonegod.gui.core.Screen;

/**
 *
 * @author t0neg0d
 */
public abstract class ScriptWindow extends GUIWindow {
	protected ScriptFunction function = ScriptFunction.Add;
	
	public ScriptWindow(EmitterBuilder builder, Screen screen) {
		super(builder, screen);
	}
	
	public abstract void loadProperties(ScriptStruct struct);
	
	public void setScriptFunction(ScriptFunction function) {
		this.function = function;
		hookSetFunction();
	}
	
	public ScriptFunction getScriptFunction() {
		return this.function;
	}
	
	public void hookSetFunction() {  }
}
