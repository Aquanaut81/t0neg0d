package emitterbuilder.builder.options;

import com.jme3.app.Application;
import com.jme3.app.state.AppStateManager;
import com.jme3.input.event.MouseButtonEvent;
import emitterbuilder.builder.EmitterBuilder;
import emitterbuilder.gui.GUIWindow;
import emitterbuilder.gui.utils.Layout;
import java.io.File;
import tonegod.gui.controls.buttons.ButtonAdapter;
import tonegod.gui.controls.lists.SelectList;
import tonegod.gui.controls.text.Label;
import tonegod.gui.core.Screen;

/**
 *
 * @author t0neg0d
 */
public class LoadProjectDialog extends GUIWindow {
	SelectList lpFileList;
	ButtonAdapter lpOk, lpCancel;
	String path;
	String fileToLoad = "";
	
	public LoadProjectDialog(EmitterBuilder builder, Screen screen) {
		super(builder, screen);
		
		try {
			path = getClass().getClassLoader().getResource(
				"emitterbuilder/builder/project/locator.xml"
			).toString();
			path = path.substring(path.indexOf("/")+1, path.indexOf("build"));
			path += "assets/XML/saves/";
		} catch (Exception ex1) {  }
		
		populateWindow();
	}
	
	private void populateWindow() {
		Layout.dim.set(Layout.lWidthN,Layout.h);
		Label lLPSelect = Layout.getNewLabel(screen, "Select Project: ");
		ic.addChild(lLPSelect);
		
		Layout.incCol(lLPSelect);
		Layout.dim.set(Layout.sbWidth*2,Layout.h*5);
		lpFileList = new SelectList(screen, "lpFileList", Layout.pos, Layout.dim) {
			@Override
			public void onChange() {
				parseFilePath((String)getListItem(getSelectedIndex()).getValue());
				lpOk.setIsEnabled(true);
			}
		};
		lpFileList.setIsMultiselect(false);
		populateFileList();
		ic.addChild(lpFileList);
		form.addFormElement(lpFileList);
		
		Layout.incRow();
		Layout.incRow();
		Layout.incRow();
		Layout.incRow();
		Layout.dim.set(Layout.bWidth,Layout.h);
		lpOk = new ButtonAdapter(screen, "lpOk", Layout.pos, Layout.dim) {
			@Override
			public void onButtonMouseLeftUp(MouseButtonEvent evt, boolean isToggled) {
				builder.getLoader().load(fileToLoad);
				unload();
				if (builder.getApplication().getStateManager().hasState(builder.getOptions()))
					builder.getOptions().toggleOptionWin();
			}
		};
		lpOk.setText("Load");
		ic.addChild(lpOk);
		form.addFormElement(lpOk);
		
		Layout.dim.set(Layout.bWidth,Layout.h);
		lpCancel = new ButtonAdapter(screen, "lpCancel", Layout.pos, Layout.dim) {
			@Override
			public void onButtonMouseLeftUp(MouseButtonEvent evt, boolean isToggled) {
				unload();
			}
		};
		lpCancel.setText("Cancel");
		ic.addChild(lpCancel);
		form.addFormElement(lpCancel);
		
		ic.sizeToContent();
	//	ic.setWidth(ic.getWidth()+Layout.h);
		
		lpOk.setX(ic.getWidth()-lpOk.getWidth());
		lpOk.setIsEnabled(false);
		;
		createWindow("Load Project");
	}
	
	@Override
	public void initialize(AppStateManager stateManager, Application app) {
		super.initialize(stateManager, app);
		populateFileList();
	}
	
	@Override
	public void cleanup() {
		super.cleanup();
		lpOk.setIsEnabled(false);
	}
	
	private void populateFileList() {
		try {
			File f = new File(path);
			File[] files = f.listFiles();
			
			lpFileList.removeAllListItems();
			
			for (int i = 0; i < files.length; i++) {
				String caption = files[i].getAbsolutePath();
				caption = caption.substring(caption.indexOf("XML"),caption.length());
				lpFileList.addListItem(caption,files[i].getName());
			}
		} catch (Exception ex) {  }
	}
	
	private void parseFilePath(String fileName) {
		fileToLoad = fileName;
	}
}
