/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package emitterbuilder.builder.options;

import com.jme3.app.Application;
import com.jme3.app.state.AppStateManager;
import com.jme3.input.event.KeyInputEvent;
import com.jme3.input.event.MouseButtonEvent;
import emitterbuilder.builder.EmitterBuilder;
import emitterbuilder.gui.GUIWindow;
import emitterbuilder.gui.utils.Layout;
import tonegod.gui.controls.buttons.ButtonAdapter;
import tonegod.gui.controls.text.Label;
import tonegod.gui.controls.text.TextField;
import tonegod.gui.core.Screen;

/**
 *
 * @author t0neg0d
 */
public class SaveProjectDialog extends GUIWindow {
	TextField spName;
	ButtonAdapter spOk, spCancel;
	
	public SaveProjectDialog(EmitterBuilder builder,Screen screen) {
		super(builder,screen);
		
		populateWindow();
	}
	
	private void populateWindow() {
		Layout.dim.set(Layout.lWidthN,Layout.h);
		Label lSPName = Layout.getNewLabel(screen, "Project Name: ");
		ic.addChild(lSPName);
		
		Layout.incCol(lSPName);
		Layout.dim.set(Layout.sbWidth,Layout.h);
		spName = new TextField(screen, "spName", Layout.pos, Layout.dim) {
			@Override
			public void controlKeyPressHook(KeyInputEvent evt, String text) {
				if (!getText().equals("") && !spOk.getIsEnabled()) {
					spOk.setIsEnabled(true);
				} else if (getText().equals("") && spOk.getIsEnabled()) {
					spOk.setIsEnabled(false);
				}
			}
		};
		spName.setType(TextField.Type.ALPHANUMERIC_NOSPACE);
		spName.setText(builder.getProjectName());
		ic.addChild(spName);
		form.addFormElement(spName);
		
		Layout.incRow();
		
		Layout.dim.set(Layout.bWidth,Layout.h);
		spOk = new ButtonAdapter(screen, "spOk", Layout.pos, Layout.dim) {
			@Override
			public void onButtonMouseLeftUp(MouseButtonEvent evt, boolean isToggled) {
				builder.getSaver().save(spName.getText(), builder.getEmitters().values(), builder.getScriptBuilder().getTasks());
				builder.setProjectName(spName.getText());
				unload();
				if (builder.getApplication().getStateManager().hasState(builder.getOptions()))
					builder.getOptions().toggleOptionWin();
			}
		};
		spOk.setText("Save");
		ic.addChild(spOk);
		form.addFormElement(spOk);
		
		Layout.dim.set(Layout.bWidth,Layout.h);
		spCancel = new ButtonAdapter(screen, "spCancel", Layout.pos, Layout.dim) {
			@Override
			public void onButtonMouseLeftUp(MouseButtonEvent evt, boolean isToggled) {
				unload();
			}
		};
		spCancel.setText("Cancel");
		ic.addChild(spCancel);
		form.addFormElement(spCancel);
		
		ic.sizeToContent();
		
		spOk.setX(ic.getWidth()-spOk.getWidth());
		spOk.setIsEnabled(false);
		
		createWindow("Save Project");
	}
	
	@Override
	public void initialize(AppStateManager stateManager, Application app) {
		super.initialize(stateManager, app);
		spName.setText(builder.getProjectName());
		if (builder.getProjectName().equals(""))
			spOk.setIsEnabled(false);
		else
			spOk.setIsEnabled(true);
	}
	
	@Override
	public void cleanup() {
		super.cleanup();
		spOk.setIsEnabled(false);
	}
}
