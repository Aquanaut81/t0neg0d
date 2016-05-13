package emitterbuilder.builder.shapes;

import com.jme3.animation.AnimControl;
import com.jme3.app.Application;
import com.jme3.app.state.AppStateManager;
import com.jme3.input.event.MouseButtonEvent;
import com.jme3.scene.Geometry;
import com.jme3.scene.Mesh;
import com.jme3.scene.Node;
import emitterbuilder.builder.EmitterBuilder;
import emitterbuilder.builder.datastructures.EmitterStruct;
import emitterbuilder.gui.FileBrowser;
import emitterbuilder.gui.utils.Layout;
import emitterbuilder.gui.PropertiesWindow;
import tonegod.gui.controls.buttons.ButtonAdapter;
import tonegod.gui.controls.text.Label;
import tonegod.gui.controls.text.TextField;
import tonegod.gui.core.Screen;

/**
 *
 * @author t0neg0d
 */
public class LoadMesh extends PropertiesWindow {
	ButtonAdapter setMesh;
	TextField pathToMesh;
	FileBrowser meshBrowser;
	
	public LoadMesh(final EmitterBuilder builder, Screen screen) {
		super(builder, screen);
		
		Layout.dim.set(Layout.lWidthN,Layout.h);
		Label lPathToMesh = Layout.getNewLabel(screen, "Relative File Path: ");
		ic.addChild(lPathToMesh);
		
		Layout.incCol(lPathToMesh);
		Layout.dim.set(Layout.sbWidth,Layout.h);
		pathToMesh = new TextField(screen, "pathToMesh", Layout.pos, Layout.dim);
		pathToMesh.setIsEnabled(false);
		ic.addChild(pathToMesh);
		form.addFormElement(pathToMesh);
		
		Layout.incCol(pathToMesh);
		Layout.dim.set(Layout.bWidth,Layout.h);
		setMesh = new ButtonAdapter(screen, "setMesh", Layout.pos, Layout.dim) {
			@Override
			public void onButtonMouseLeftUp(MouseButtonEvent evt, boolean isToggled) {
				String path = pathToMesh.getText();
				Node n = null;
				try {
					n = (Node)app.getAssetManager().loadModel(path);
				} catch (Exception ex) {
					
				}
				if (n != null) {
					Geometry g = ((Geometry)n.getChild(0));
					Mesh m = g.getMesh();
					
					AnimControl animControl = n.getControl(AnimControl.class);
					if (animControl != null) {
						builder.emitter.setAssetMeshPath(pathToMesh.getText());
						builder.emitter.setEmitterShapeNode(n);
						builder.emitter.setEmitterMesh(m);
						builder.emitter.setAnimControl(animControl);
						builder.emitter.setAnimChannel(animControl.createChannel());
						builder.getShapeProperties().showSetAnimation();
					} else {
						builder.getShapeProperties().applyNewEmitterShape(m);
						unload();
					}
				}
			}
		};
		setMesh.setText("Load");
		ic.addChild(setMesh);
		form.addFormElement(setMesh);
		
		Layout.incRow();
		Layout.dim.set(setMesh.getX()+setMesh.getWidth(),Layout.h*5);
		meshBrowser = new FileBrowser(screen, Layout.pos, Layout.dim, true) {
			@Override
			public void onFileSelect(String path) {
				pathToMesh.setText(path);
			}
		};
		meshBrowser.setValidExtensions(".j3o");
		meshBrowser.setRootFolder("Models");
		ic.addChild(meshBrowser);
		form.addFormElement(meshBrowser);
		
		ic.sizeToContent();
		
		createWindow("Load Mesh Asset");
	}
	
	@Override
	public void loadProperties(EmitterStruct struct) {
		pathToMesh.setText(builder.emitter.getAssetMeshPath());
	}
	
	@Override
	public void initialize(AppStateManager stateManager, Application app) {
		super.initialize(stateManager, app);
		meshBrowser.add3DPreview();
	}
	
	@Override
	public void update(float tpf) {  }
	
	@Override
	public void cleanup() {
		super.cleanup();
		
		meshBrowser.remove3DPreview();
	//	screen.removeElement(win);
	}
}
