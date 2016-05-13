package emitterbuilder.builder.scripts;

import com.jme3.input.event.MouseButtonEvent;
import emitterbuilder.builder.EmitterBuilder;
import emitterbuilder.builder.datastructures.EmitterStruct;
import emitterbuilder.builder.datastructures.ScriptStruct;
import emitterbuilder.gui.utils.Layout;
import emitterbuilder.gui.ScriptWindow;
import tonegod.gui.controls.buttons.ButtonAdapter;
import tonegod.gui.controls.lists.SelectBox;
import tonegod.gui.controls.text.Label;
import tonegod.gui.controls.text.TextField;
import tonegod.gui.core.Screen;

/**
 *
 * @author t0neg0d
 */
public class EmitForDurationScript extends ScriptWindow {
	SelectBox sedEmitters;
	TextField sedEmitPerSec, sedPartsPerEmit, sedDelay, sedDuration;
	ButtonAdapter sedAddScript;
	private ScriptStruct editStruct;
	
	public EmitForDurationScript(EmitterBuilder builder, Screen screen) {
		super(builder, screen);
		
		Layout.dim.set(Layout.lWidthN,Layout.h);
		Label lSEDEmitters = Layout.getNewLabel(screen, "Emitter: ");
		lSEDEmitters.setHeight(Layout.h);
		ic.addChild(lSEDEmitters);
		
		Layout.incCol(lSEDEmitters);
		Layout.dim.set(Layout.sbWidth, Layout.h);
		sedEmitters = new SelectBox(screen, "sedEmitters", Layout.pos, Layout.dim) {
			@Override
			public void onChange(int selectedIndex, Object value) {
				
			}
		};
		ic.addChild(sedEmitters);
		form.addFormElement(sedEmitters);
		
		Layout.incRow();
		Layout.dim.set(Layout.lWidthN,Layout.h);
		Label lSEDEmitsPerSec = Layout.getNewLabel(screen, "Emissions Per Sec: ");
		ic.addChild(lSEDEmitsPerSec);
		
		Layout.incCol(lSEDEmitsPerSec);
		Layout.dim.set(Layout.floatW,Layout.h);
		sedEmitPerSec = new TextField(screen, "sedEmitPerSec", Layout.pos, Layout.dim);
		sedEmitPerSec.setType(TextField.Type.NUMERIC);
		sedEmitPerSec.setText("10");
		ic.addChild(sedEmitPerSec);
		form.addFormElement(sedEmitPerSec);
		
		Layout.incRow();
		Layout.dim.set(Layout.lWidthN,Layout.h);
		Label lSEDPartsPerSec = Layout.getNewLabel(screen, "Particles Per Emit: ");
		ic.addChild(lSEDPartsPerSec);
		
		Layout.incCol(lSEDPartsPerSec);
		Layout.dim.set(Layout.floatW,Layout.h);
		sedPartsPerEmit = new TextField(screen, "sedPartsPerEmit", Layout.pos, Layout.dim);
		sedPartsPerEmit.setType(TextField.Type.NUMERIC);
		sedPartsPerEmit.setText("10");
		ic.addChild(sedPartsPerEmit);
		form.addFormElement(sedPartsPerEmit);
		
		Layout.incRow();
		Layout.dim.set(Layout.lWidthN,Layout.h);
		Label lSEDDelay = Layout.getNewLabel(screen, "Delay for (secoonds): ");
		ic.addChild(lSEDDelay);
		
		Layout.incCol(lSEDDelay);
		Layout.dim.set(Layout.floatW,Layout.h);
		sedDelay = new TextField(screen, "sedDelay", Layout.pos, Layout.dim);
		sedDelay.setType(TextField.Type.NUMERIC);
		sedDelay.setText("1.0");
		ic.addChild(sedDelay);
		form.addFormElement(sedDelay);
		
		Layout.incRow();
		Layout.dim.set(Layout.lWidthN,Layout.h);
		Label lSEDDuration = Layout.getNewLabel(screen, "Run for (secoonds): ");
		ic.addChild(lSEDDuration);
		
		Layout.incCol(lSEDDuration);
		Layout.dim.set(Layout.floatW,Layout.h);
		sedDuration = new TextField(screen, "sedDuration", Layout.pos, Layout.dim);
		sedDuration.setType(TextField.Type.NUMERIC);
		sedDuration.setText("1.0");
		ic.addChild(sedDuration);
		form.addFormElement(sedDuration);
		
		Layout.incRow();
		Layout.x = sedEmitters.getX()+sedEmitters.getWidth()+Layout.h-Layout.bWidth;
		Layout.updatePos();
		Layout.dim.set(Layout.bWidth,Layout.h);
		sedAddScript = new ButtonAdapter(screen, "sedAddScript", Layout.pos, Layout.dim) {
			@Override
			public void onButtonMouseLeftUp(MouseButtonEvent evt, boolean isToggled) {
				if (function == ScriptBuilder.ScriptFunction.Add)
					addScript();
				else
					updateScript();
			}
		};
		sedAddScript.setText("Add Script");
		ic.addChild(sedAddScript);
		form.addFormElement(sedAddScript);
		
		ic.sizeToContent();
		
		createWindow("New Emit For Duration Script");
	}
	
	private void addScript() {
		if (builder.validateIntegerValue(sedEmitPerSec.getText()) &&
			builder.validateIntegerValue(sedPartsPerEmit.getText()) &&
			builder.validateFloatValue(sedDelay.getText()) &&
			builder.validateFloatValue(sedDuration.getText())) {
			ScriptBuilder sb = builder.getScriptBuilder();
			float delayAsFloat = Float.valueOf(sedDelay.getText())*1000;
			float durAsFloat = Float.valueOf(sedDuration.getText())*1000;
			sb.addDurationScript(
				((EmitterStruct)sedEmitters.getSelectedListItem().getValue()).getEmitter(),
				Integer.valueOf(sedEmitPerSec.getText()),
				Integer.valueOf(sedPartsPerEmit.getText()),
				(long)delayAsFloat,
				(long)(delayAsFloat+durAsFloat)
			);
		}
	}
	
	private void updateScript() {
		if (builder.validateIntegerValue(sedEmitPerSec.getText()) &&
			builder.validateIntegerValue(sedPartsPerEmit.getText()) &&
			builder.validateFloatValue(sedDelay.getText()) &&
			builder.validateFloatValue(sedDuration.getText())) {
			editStruct.emitterName = ((EmitterStruct)sedEmitters.getSelectedListItem().getValue()).getEmitter().getName();
			editStruct.emitPerSec = Integer.valueOf(sedEmitPerSec.getText());
			editStruct.partsPerEmit = Integer.valueOf(sedPartsPerEmit.getText());
			editStruct.delay = (long)((float)Float.valueOf(sedDelay.getText())*1000);
			editStruct.duration = (long)((float)Float.valueOf(sedDuration.getText())*1000);
			ScriptBuilder sb = builder.getScriptBuilder();
			sb.updateDurationScript(editStruct);
			editStruct = null;
		}
	}
	
	@Override
	public void loadProperties(ScriptStruct struct) {
		editStruct = struct;
		sedEmitters.setSelectedByCaption(struct.emitterName, true);
		sedEmitPerSec.setText(String.valueOf(struct.emitPerSec));
		sedPartsPerEmit.setText(String.valueOf(struct.partsPerEmit));
		float delay = (float)((float)struct.delay/1000);
		sedDelay.setText(String.valueOf(delay));
		float dur = (float)((float)struct.duration/1000);
		sedDuration.setText(String.valueOf(dur));
	}
	
	public SelectBox getEmitterList() { return this.sedEmitters; }
	
	@Override
	public void hookSetFunction() {
		if (getScriptFunction() == ScriptBuilder.ScriptFunction.Add)
			sedAddScript.setText("Add");
		else
			sedAddScript.setText("Update");
	}
}
