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
public abstract class CollapsableWindow extends AbstractAppState {
	protected EmitterBuilder builder;
	protected Screen screen;
	
	protected Form form;
	protected Window win, parent = null;
	protected Element ic;
	ButtonAdapter close;
	
	public CollapsableWindow(EmitterBuilder builder, Screen screen) {
		this.builder = builder;
		this.screen = screen;
		
		buildIC();
	}
	
	private void buildIC() {
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
		
		populateWindow();
	}
	
	protected abstract void populateWindow();
	public abstract void loadProperties();
	
	public Window getWindow() { return this.win; }
	public void setParentWindow(Window parent) {
		this.parent = parent;
	}
	public void createWindow(String title) {
		Layout.dim.set(
			(Layout.pad*2)+ic.getWidth(),
			30+(Layout.pad*2)+ic.getHeight()
		);
		Layout.pos.set(screen.getWidth()-Layout.dim.x,0);
		win = new Window(screen, UIDUtil.getUID(), Layout.pos, Layout.dim);
		win.addChild(ic);
		win.setIsMovable(false);
		win.setIsResizable(false);
		win.setWindowTitle(title);
		
		Layout.pos.set(win.getDragBar().getWidth()-Layout.h-3,3);
		Layout.dim.set(Layout.h,Layout.h);
		close = new ButtonAdapter(screen, UIDUtil.getUID(), Layout.pos, Layout.dim) {
			@Override
			public void onButtonMouseLeftUp(MouseButtonEvent evt, boolean toggled) {
				builder.collapse(win, false);
			}
		};
		close.setButtonIcon(18,18,screen.getStyle("Common").getString("arrowDown"));
		win.getDragBar().addChild(close);
		win.getDragBar().setIsMovable(false);
	}
	
	public void configWindow(boolean collapsed) {
		if (collapsed) {
			ic.hide();
			win.setHeight(40);
			win.getDragBar().setY(win.getHeight()-win.getDragBarHeight()-7);
			if (parent  != null)win.setY(parent.getY()-win.getHeight());
			else				win.setY(screen.getHeight()-win.getHeight());
			close.setButtonIcon(18,18,screen.getStyle("Common").getString("arrowDown"));
		} else {
			ic.show();
			win.setHeight(win.getOrgDimensions().y);
			win.getDragBar().setY(win.getHeight()-win.getDragBarHeight()-7);
			if (parent  != null)win.setY(parent.getY()-win.getHeight());
			else				win.setY(screen.getHeight()-win.getHeight());
			close.setButtonIcon(18,18,screen.getStyle("Common").getString("arrowUp"));
			ic.show();
		}
		
	}
	
	@Override
	public void initialize(AppStateManager stateManager, Application app) {
		super.initialize(stateManager, app);
		screen.addElement(win);
		win.hide();
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
