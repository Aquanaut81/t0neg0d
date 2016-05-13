package emitterbuilder.builder.scripts;

import com.jme3.input.event.MouseButtonEvent;
import emitterbuilder.builder.EmitterBuilder;
import emitterbuilder.builder.datastructures.EmitterStruct;
import emitterbuilder.builder.scripts.ScriptBuilder.ScriptFunction;
import emitterbuilder.builder.datastructures.ScriptStruct;
import emitterbuilder.gui.utils.Layout;
import emitterbuilder.gui.ScriptWindow;
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
public class EmitScript extends ScriptWindow {
	private SelectBox seEmitters;
	private CheckBox seEmitAll;
	private TextField seEmitNumParts, seDelay;
	private ButtonAdapter esAddScript;
	private ScriptStruct editStruct;
	
	public EmitScript(EmitterBuilder builder, Screen screen) {
		super(builder, screen);
		
		Layout.dim.set(Layout.lWidthN,Layout.h);
		Label lSEEmitters = Layout.getNewLabel(screen, "Emitter: ");
		ic.addChild(lSEEmitters);
		
		Layout.incCol(lSEEmitters);
		Layout.dim.set(Layout.sbWidth, Layout.h);
		seEmitters = new SelectBox(screen, "seEmitters", Layout.pos, Layout.dim) {
			@Override
			public void onChange(int selectedIndex, Object value) {
				
			}
		};
		ic.addChild(seEmitters);
		form.addFormElement(seEmitters);
		
		Layout.incRow();
		Layout.dim.set(Layout.lWidthN,Layout.h);
		Label lSEEmitAll = Layout.getNewLabel(screen, "All Particles: ");
		ic.addChild(lSEEmitAll);
		
		Layout.incCol(lSEEmitAll);
		Layout.dim.set(Layout.h, Layout.h);
		seEmitAll = new CheckBox(screen, "seEmitAll", Layout.pos, Layout.dim);
		ic.addChild(seEmitAll);
		form.addFormElement(seEmitAll);
		
		Layout.incRow();
		Layout.dim.set(Layout.lWidthN,Layout.h);
		Label lSEOr = Layout.getNewLabel(screen, "-OR-");
		ic.addChild(lSEOr);
		
		Layout.incRow();
		Layout.dim.set(Layout.lWidthN,Layout.h);
		Label lSEEmitNumParts = Layout.getNewLabel(screen, "Number of Particles: ");
		ic.addChild(lSEEmitNumParts);
		
		Layout.incCol(lSEEmitNumParts);
		Layout.dim.set(Layout.floatW,Layout.h);
		seEmitNumParts = new TextField(screen, "seEmitNumParts", Layout.pos, Layout.dim);
		seEmitNumParts.setType(TextField.Type.NUMERIC);
		seEmitNumParts.setText("10");
		ic.addChild(seEmitNumParts);
		form.addFormElement(seEmitNumParts);
		
		Layout.incRow();
		Layout.dim.set(Layout.lWidthN,Layout.h);
		Label lSEDelay = Layout.getNewLabel(screen, "Delay for (secoonds): ");
		ic.addChild(lSEDelay);
		
		Layout.incCol(lSEDelay);
		Layout.dim.set(Layout.floatW,Layout.h);
		seDelay = new TextField(screen, "seDelay", Layout.pos, Layout.dim);
		seDelay.setType(TextField.Type.NUMERIC);
		seDelay.setText("1.0");
		ic.addChild(seDelay);
		form.addFormElement(seDelay);
		
		Layout.incRow();
		Layout.x = seEmitters.getX()+seEmitters.getWidth()+Layout.h-Layout.bWidth;
		Layout.updatePos();
		Layout.dim.set(Layout.bWidth,Layout.h);
		esAddScript = new ButtonAdapter(screen, "esAddScript", Layout.pos, Layout.dim) {
			@Override
			public void onButtonMouseLeftUp(MouseButtonEvent evt, boolean isToggled) {
				if (function == ScriptFunction.Add)
					addScript();
				else
					updateScript();
			}
		};
		esAddScript.setText("Add Script");
		ic.addChild(esAddScript);
		form.addFormElement(esAddScript);
		
		ic.sizeToContent();
		
		createWindow("New Emit Script");
	}
	
	private void addScript() {
		if (builder.validateIntegerValue(seEmitNumParts.getText()) &&
			builder.validateFloatValue(seDelay.getText())) {
			ScriptBuilder sb = builder.getScriptBuilder();
			float delayAsFloat = Float.valueOf(seDelay.getText())*1000;
			sb.addEmitScript(
				((EmitterStruct)seEmitters.getSelectedListItem().getValue()).getEmitter(),
				seEmitAll.getIsChecked(),
				Integer.valueOf(seEmitNumParts.getText()),
				(long)delayAsFloat
			);
		}
	}
	
	private void updateScript() {
		if (builder.validateIntegerValue(seEmitNumParts.getText()) &&
			builder.validateFloatValue(seDelay.getText())) {
			editStruct.emitterName = ((EmitterStruct)seEmitters.getSelectedListItem().getValue()).getEmitter().getName();
			editStruct.emitAll = seEmitAll.getIsChecked();
			editStruct.numParticles = Integer.valueOf(seEmitNumParts.getText());
			editStruct.delay = (long)((float)Float.valueOf(seDelay.getText())*1000);
			ScriptBuilder sb = builder.getScriptBuilder();
			sb.updateEmitScript(editStruct);
			editStruct = null;
		}
	}
	
	@Override
	public void loadProperties(ScriptStruct struct) {
		editStruct = struct;
		seEmitters.setSelectedByCaption(struct.emitterName, true);
		seEmitAll.setIsChecked(struct.emitAll);
		seEmitNumParts.setText(String.valueOf(struct.numParticles));
		float delay = (float)((float)struct.delay/1000);
		seDelay.setText(String.valueOf(delay));
	}
	
	public SelectBox getEmitterList() { return this.seEmitters; }
	
	@Override
	public void hookSetFunction() {
		if (getScriptFunction() == ScriptFunction.Add)
			esAddScript.setText("Add");
		else
			esAddScript.setText("Update");
	}
}
