package emitterbuilder.builder.shapes;

import com.jme3.app.Application;
import com.jme3.app.state.AbstractAppState;
import com.jme3.app.state.AppStateManager;
import com.jme3.scene.Mesh;
import com.jme3.scene.shape.Box;
import com.jme3.scene.shape.Cylinder;
import com.jme3.scene.shape.Dome;
import com.jme3.scene.shape.Quad;
import com.jme3.scene.shape.Sphere;
import com.jme3.scene.shape.Torus;
import emitterbuilder.builder.EmitterBuilder;
import tonegod.gui.controls.windows.Window;
import tonegod.gui.core.Screen;

/**
 *
 * @author t0neg0d
 */
public class Shapes extends AbstractAppState {
	public static enum Function {
		Emitter_Shape,
		Particle_Mesh
	}
	
	private EmitterBuilder builder;
	
	private ConfigBox configBox;
	private ConfigCylinder configCylinder;
	private ConfigDome configDome;
	private ConfigQuad configQuad;
	private ConfigSphere configSphere;
	private ConfigTorus configTorus;
	private LoadMesh loadMeshWin;
	private SetAnimation animWin;
	private SelectPrimitive selectPrimitiveWin;
	
	private Function function = Function.Emitter_Shape;
	
	Window shapeProperties;
	
	public Shapes(EmitterBuilder builder, Screen screen) {
		this.builder = builder;
		
		selectPrimitiveWin = new SelectPrimitive(builder, screen);
		loadMeshWin = new LoadMesh(builder, screen);
		configBox = new ConfigBox(builder, screen);
		configCylinder = new ConfigCylinder(builder, screen);
		configDome = new ConfigDome(builder, screen);
		configQuad = new ConfigQuad(builder, screen);
		configSphere = new ConfigSphere(builder, screen);
		configTorus = new ConfigTorus(builder, screen);
		animWin = new SetAnimation(builder, screen);
		
		createPropertiesWindow();
	}
	
	private void createPropertiesWindow() {
		
	}
	
	public Window getPropertiesWindow() {
		return shapeProperties;
	}
	
	public void setFunction(Function function) {
		this.function = function;
	}
	public Function getFunction() { return this.function; }
	
	public void showSelectPrimitive() {
		selectPrimitiveWin.loadProperties(builder.emitter);
		builder.getApplication().getStateManager().attach(selectPrimitiveWin);
	}
	public void showLoadMesh() {
		loadMeshWin.loadProperties(builder.emitter);
		builder.getApplication().getStateManager().attach(loadMeshWin);
	}
	public void showSetAnimation() {
		animWin.loadProperties(builder.emitter);
		builder.getApplication().getStateManager().attach(animWin);
	}
	public void showConfigWindow(Class c) {
		if (c == Box.class) {
			configBox.loadProperties(builder.emitter);
			builder.getApplication().getStateManager().attach(configBox);
		} else if (c == Cylinder.class) {
			configCylinder.loadProperties(builder.emitter);
			builder.getApplication().getStateManager().attach(configCylinder);
		} else if (c == Dome.class) {
			configDome.loadProperties(builder.emitter);
			builder.getApplication().getStateManager().attach(configDome);
		} else if (c == Quad.class) {
			configQuad.loadProperties(builder.emitter);
			builder.getApplication().getStateManager().attach(configQuad);
		} else if (c == Sphere.class) {
			configSphere.loadProperties(builder.emitter);
			builder.getApplication().getStateManager().attach(configSphere);
		} else if (c == Torus.class) {
			configTorus.loadProperties(builder.emitter);
			builder.getApplication().getStateManager().attach(configTorus);
		}	
	}
	public void closeAllShapeWindows() {
		builder.getApplication().getStateManager().detach(configBox);
		builder.getApplication().getStateManager().detach(configCylinder);
		builder.getApplication().getStateManager().detach(configDome);
		builder.getApplication().getStateManager().detach(configQuad);
		builder.getApplication().getStateManager().detach(configSphere);
		builder.getApplication().getStateManager().detach(configTorus);
		builder.getApplication().getStateManager().detach(loadMeshWin);
		builder.getApplication().getStateManager().detach(animWin);
		builder.getApplication().getStateManager().detach(selectPrimitiveWin);
	}
	
	public void applyNewEmitterShape(Mesh m) {
		builder.startChangeState();
		builder.emitter.getEmitter().setShape(m);
		builder.resetChangeState();
		builder.endChangeState();
	}
	
	@Override
	public void initialize(AppStateManager stateManager, Application app) {
		super.initialize(stateManager, app);
		//TODO: initialize your AppState, e.g. attach spatials to rootNode
		//this is called on the OpenGL thread after the AppState has been attached
	}
	
	@Override
	public void update(float tpf) {  }
	
	@Override
	public void cleanup() {
		super.cleanup();
	}
}
