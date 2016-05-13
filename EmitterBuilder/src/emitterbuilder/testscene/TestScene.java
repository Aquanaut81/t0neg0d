package emitterbuilder.testscene;

import com.jme3.app.Application;
import com.jme3.app.state.AbstractAppState;
import com.jme3.app.state.AppStateManager;
import com.jme3.asset.AssetManager;
import com.jme3.light.AmbientLight;
import com.jme3.light.DirectionalLight;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.texture.Texture;
import emitter.influencers.PhysicsInfluencer;
import emitterbuilder.builder.EmitterBuilder;
import emitterbuilder.builder.datastructures.EmitterStruct;

/**
 *
 * @author t0neg0d
 */
public class TestScene extends AbstractAppState {
	EmitterBuilder builder;
	AssetManager assetManager;
	Node stand;
	Material mat;
	Texture tex;
	AmbientLight al;
	DirectionalLight dl;
	
	public TestScene(EmitterBuilder builder) {
		this.builder = builder;
		this.assetManager = builder.getApplication().getAssetManager();
		
		al = new AmbientLight();
		al.setColor(ColorRGBA.White);
		
		dl = new DirectionalLight();
		dl.setDirection(new Vector3f(-0.5f,-1f,0f).normalizeLocal());
		dl.setColor(ColorRGBA.White);
		
		stand = (Node)assetManager.loadModel("Models/TestScene/Stand.j3o");
		
		mat = new Material(assetManager, "Common/MatDefs/Light/Lighting.j3md");
		mat.setBoolean("UseMaterialColors", false);
		tex = assetManager.loadTexture("Textures/TestScene/Stand.png");
		mat.setTexture("DiffuseMap", tex);
		stand.setMaterial(mat);
		
		stand.setLocalScale(0.5f);
		stand.setLocalTranslation(0,-1.5f,0);
	}
	
	public Geometry getGeom() {
		return (Geometry)stand.getChild(0);
	}
	
	@Override
	public void initialize(AppStateManager stateManager, Application app) {
		super.initialize(stateManager, app);
		
		builder.getRootNode().attachChild(stand);
		builder.getRootNode().addLight(al);
		builder.getRootNode().addLight(dl);
		
		for (EmitterStruct struct : builder.getEmitters().values()) {
			PhysicsInfluencer pi = struct.getEmitter().getInfluencer(PhysicsInfluencer.class);
			if (pi != null) {
				pi.addCollidable((Geometry)stand.getChild(0));
			}
		}
	}
	
	@Override
	public void update(float tpf) {
		//TODO: implement behavior during runtime
	}
	
	@Override
	public void cleanup() {
		super.cleanup();
		
		stand.removeFromParent();
		builder.getRootNode().removeLight(al);
		builder.getRootNode().removeLight(dl);
		
		for (EmitterStruct struct : builder.getEmitters().values()) {
			PhysicsInfluencer pi = struct.getEmitter().getInfluencer(PhysicsInfluencer.class);
			if (pi != null) {
				pi.removeCollidable((Geometry)stand.getChild(0));
			}
		}
	}
}
