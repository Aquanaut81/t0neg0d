package emitterbuilder.gui;

import emitterbuilder.gui.utils.Layout;
import com.jme3.app.Application;
import com.jme3.app.state.AbstractAppState;
import com.jme3.app.state.AppStateManager;
import com.jme3.input.event.MouseButtonEvent;
import com.jme3.math.Vector4f;
import emitterbuilder.builder.EmitterBuilder;
import tonegod.gui.controls.buttons.ButtonAdapter;
import tonegod.gui.controls.form.Form;
import tonegod.gui.controls.windows.Window;
import tonegod.gui.core.Element;
import tonegod.gui.core.Screen;
import tonegod.gui.core.utils.UIDUtil;

/**
 *
 * @author t0neg0d
 */
public class GUIWindow extends AbstractAppState {
	protected EmitterBuilder builder;
	protected Screen screen;
	
	protected Form form;
	protected Window win;
	protected Element ic;
	
	public GUIWindow(EmitterBuilder builder, Screen screen) {
		this.builder = builder;
		this.screen = screen;
		
		form = new Form(screen);
		
		Layout.x = 0;
		Layout.y = 0;
		Layout.pos.set(Layout.pad,30+Layout.pad);
		Layout.dim.set(100,100);
		ic = new Element(screen, UIDUtil.getUID(), Layout.pos, Layout.dim, Vector4f.ZERO, null);
		ic.setAsContainerOnly();
		ic.setIsMovable(false);
		ic.setIsResizable(false);
		
		Layout.reset();
	}
	
	public void createWindow(String title) {
		Layout.dim.set(
			(Layout.pad*2)+ic.getWidth(),
			30+(Layout.pad*2)+ic.getHeight()
		);
		Layout.pos.set(0,0);
		win = new Window(screen, UIDUtil.getUID(), Layout.pos, Layout.dim);
		win.addChild(ic);
		win.setIsMovable(true);
		win.setIsResizable(false);
		win.setWindowTitle(title);
		
		Layout.pos.set(win.getDragBar().getWidth()-Layout.h-3,3);
		Layout.dim.set(Layout.h,Layout.h);
		ButtonAdapter close = new ButtonAdapter(screen, UIDUtil.getUID(), Layout.pos, Layout.dim) {
			@Override
			public void onButtonMouseLeftUp(MouseButtonEvent evt, boolean toggled) {
				unload();
			}
		};
		close.setText("X");
		win.getDragBar().addChild(close);
	}
	
	@Override
	public void initialize(AppStateManager stateManager, Application app) {
		super.initialize(stateManager, app);
		screen.addElement(win);
		win.centerToParent();
	}
	
	@Override
	public void update(float tpf) {  }
	
	@Override
	public void cleanup() {
		super.cleanup();
		screen.removeElement(win);
	}
	
	protected void unload() {
		builder.getApplication().getStateManager().detach(this);
	}
}
