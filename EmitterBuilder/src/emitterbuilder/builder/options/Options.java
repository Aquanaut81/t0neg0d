package emitterbuilder.builder.options;

import com.jme3.app.Application;
import com.jme3.app.state.AbstractAppState;
import com.jme3.app.state.AppStateManager;
import com.jme3.input.event.MouseButtonEvent;
import com.jme3.math.Vector2f;
import emitterbuilder.builder.EmitterBuilder;
import emitterbuilder.gui.utils.Layout;
import tonegod.gui.controls.buttons.ButtonAdapter;
import tonegod.gui.controls.windows.Panel;
import tonegod.gui.core.Screen;

/**
 *
 * @author t0neg0d
 */
public class Options extends AbstractAppState {
	EmitterBuilder builder;
	Screen screen;
	Panel optionWin;
	ButtonAdapter addTS, configTS;
	boolean useTestScene = false;
	
	public Options(EmitterBuilder builder, Screen screen) {
		this.builder = builder;
		this.screen = screen;
		populateWindow();
	}
	
	private void populateWindow() {
		Layout.dim.set(100,100);
		optionWin = new Panel(screen, Vector2f.ZERO);
		
		Layout.x = Layout.pad;
		Layout.y = Layout.pad;
		Layout.pos.set(Layout.x,Layout.y);
		Layout.dim.set(150,25);
		ButtonAdapter newProject = new ButtonAdapter(screen,Layout.pos,Layout.dim) {
			@Override
			public void onButtonMouseLeftUp(MouseButtonEvent evt, boolean toggled) {
				builder.getLoader().scrubScene();
			}
		};
		newProject.setText("New Project");
		optionWin.addChild(newProject);
		
		Layout.y += newProject.getHeight();
		Layout.pos.set(Layout.x,Layout.y);
		ButtonAdapter oSave = new ButtonAdapter(screen,Layout.pos,Layout.dim) {
			@Override
			public void onButtonMouseLeftUp(MouseButtonEvent evt, boolean toggled) {
				showSaveProjectWin();
			}
		};
		oSave.setText("Save Project");
		optionWin.addChild(oSave);
		
		Layout.y += oSave.getHeight();
		Layout.pos.set(Layout.x,Layout.y);
		ButtonAdapter oLoad = new ButtonAdapter(screen,Layout.pos,Layout.dim) {
			@Override
			public void onButtonMouseLeftUp(MouseButtonEvent evt, boolean toggled) {
				showLoadProjectWin();
			}
		};
		oLoad.setText("Load Project");
		optionWin.addChild(oLoad);
		
		Layout.y += oLoad.getHeight();
		Layout.pos.set(Layout.x,Layout.y);
		addTS = new ButtonAdapter(screen,Layout.pos,Layout.dim) {
			@Override
			public void onButtonMouseLeftUp(MouseButtonEvent evt, boolean toggled) {
				setUseTestScene(toggled);
			}
		};
		addTS.setIsToggleButton(true);
		addTS.setText("Add Test Scene");
		optionWin.addChild(addTS);
		
		Layout.y += addTS.getHeight();
		Layout.pos.set(Layout.x,Layout.y);
		Layout.dim.set(150,25);
		configTS = new ButtonAdapter(screen,Layout.pos,Layout.dim) {
			@Override
			public void onButtonMouseLeftUp(MouseButtonEvent evt, boolean toggled) {
				
			}
		};
		configTS.setText("Config Test Scene");
		optionWin.addChild(configTS);
		configTS.setIsEnabled(false);
		
		Layout.y += configTS.getHeight();
		Layout.pos.set(Layout.x,Layout.y);
		ButtonAdapter script = new ButtonAdapter(screen,Layout.pos,Layout.dim) {
			@Override
			public void onButtonMouseLeftUp(MouseButtonEvent evt, boolean toggled) {
				if (!builder.getApplication().getStateManager().hasState(builder.getScriptBuilder()))
					builder.getApplication().getStateManager().attach(builder.getScriptBuilder());
			}
		};
		script.setText("Script Editor");
		optionWin.addChild(script);
		
		Layout.y += script.getHeight();
		Layout.pos.set(Layout.x,Layout.y);
		ButtonAdapter code = new ButtonAdapter(screen,Layout.pos,Layout.dim) {
			@Override
			public void onButtonMouseLeftUp(MouseButtonEvent evt, boolean toggled) {
				builder.getCodeOutput().printCode();
			}
		};
		code.setText("Output Code");
		optionWin.addChild(code);
		
		Layout.y += configTS.getHeight();
		Layout.pos.set(Layout.x,Layout.y);
		ButtonAdapter exit = new ButtonAdapter(screen,Layout.pos,Layout.dim) {
			@Override
			public void onButtonMouseLeftUp(MouseButtonEvent evt, boolean toggled) {
				builder.getApplication().stop();
			}
		};
		exit.setText("Exit FX Builder");
		optionWin.addChild(exit);
		
		Layout.y += exit.getHeight();
		Layout.pos.set(Layout.x,Layout.y);
		ButtonAdapter done = new ButtonAdapter(screen,Layout.pos,Layout.dim) {
			@Override
			public void onButtonMouseLeftUp(MouseButtonEvent evt, boolean toggled) {
				toggleOptionWin();
			}
		};
		done.setText("Done");
		optionWin.addChild(done);
		
		optionWin.sizeToContent();
		
		optionWin.setIsResizable(false);
		optionWin.setIsMovable(false);
	}
	
	public void toggleOptionWin() {
		if (builder.getApplication().getStateManager().hasState(this))
			builder.getApplication().getStateManager().detach(this);
		else
			builder.getApplication().getStateManager().attach(this);
			
	}
	public Panel getOptionsWin() { return optionWin; }
	public void setUseTestScene(boolean useTestScene) {
		this.useTestScene = useTestScene;
		if (!useTestScene) {
			if (builder.getApplication().getStateManager().hasState(builder.getTestScene()))
				builder.getApplication().getStateManager().detach(builder.getTestScene());
			addTS.setText("Add Test Scene");
			addTS.setIsToggledNoCallback(false);
			configTS.setIsEnabled(false);
		} else {
			if (!builder.getApplication().getStateManager().hasState(builder.getTestScene()))
				builder.getApplication().getStateManager().attach(builder.getTestScene());
			addTS.setText("Remove Test Scene");
			addTS.setIsToggledNoCallback(true);
			configTS.setIsEnabled(true);
		}
	}
	public boolean getUseTestScene() { return this.useTestScene; }
	private void showSaveProjectWin() {
		builder.getApplication().getStateManager().attach(builder.getSaverWindow());
	}
	private void showLoadProjectWin() {
		builder.getApplication().getStateManager().attach(builder.getLoaderWindow());
	}
	
	@Override
	public void initialize(AppStateManager stateManager, Application app) {
		super.initialize(stateManager, app);
		screen.addElement(optionWin);
		optionWin.centerToParent();
	}
	
	@Override
	public void update(float tpf) {  }
	
	@Override
	public void cleanup() {
		super.cleanup();
		screen.removeElement(optionWin);
	}
}
