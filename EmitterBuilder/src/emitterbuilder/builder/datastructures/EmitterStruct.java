package emitterbuilder.builder.datastructures;

import com.jme3.animation.AnimChannel;
import com.jme3.animation.AnimControl;
import com.jme3.scene.Mesh;
import com.jme3.scene.Node;
import emitter.Emitter;
import emitter.particle.ParticleDataMesh;
import emitter.particle.ParticleDataTriMesh;

/**
 *
 * @author t0neg0d
 */
public class EmitterStruct {
	Emitter emitter;
	String shapeType = "Simple Emitter";
	String particleType = "Quad";
	String materialPath = "Common/MatDefs/Misc/Particle.j3md";
	String texturePath = "Textures/default.png";
	String assetMeshPath = "Models/AnimTest.j3o";
	Class particleMeshType = ParticleDataTriMesh.class;
	Class particleShapeMesh = null;
	String particleMeshPath = "Models/StaticTest.j3o";
	Mesh particleMesh = null;
	float f1, f2, f3, pf1, pf2, pf3;
	int i1, i2, pi1, pi2;
	Node emitterShapeNode = null;
	Mesh emitterMesh = null;
	AnimControl animControl = null;
	AnimChannel animChannel = null;
	boolean wasEnabled = true;
	boolean animate = false;
	String animationName = "";
	float animSpeed = 1.0f;
	float blendTime = 1.0f;
	boolean particleAnimate = false;
	String particleAnimationName = "";
	float particleAnimSpeed = 1.0f;
	float particleBlendTime = 1.0f;

	public EmitterStruct(Emitter emitter) {
		this.emitter = emitter;
	}

	public void setEmitter(Emitter emitter) { this.emitter = emitter; }
	public Emitter getEmitter() { return this.emitter; }
	public void setShapeType(String shapeType) { this.shapeType = shapeType; }
	public String getShapeType() { return this.shapeType; }
	public void setParticleType(String particleType) { this.particleType = particleType; }
	public String getParticleType() { return this.particleType; }
	public void setMaterialPath(String materialPath) { this.materialPath = materialPath; }
	public String getMaterialPath() { return this.materialPath; }
	public void setTexturePath(String texturePath) { this.texturePath = texturePath; }
	public String getTexturePath() { return this.texturePath; }
	public void setESF1(float f1) { this.f1 = f1; }
	public float getESF1() { return this.f1; }
	public void setESF2(float f2) { this.f2 = f2; }
	public float getESF2() { return this.f2; }
	public void setESF3(float f3) { this.f3 = f3; }
	public float getESF3() { return this.f3; }
	public void setESI1(int i1) { this.i1 = i1; }
	public int getESI1() { return this.i1; }
	public void setESI2(int i2) { this.i2 = i2; }
	public int getESI2() { return this.i2; }
	public void setPMF1(float pf1) { this.pf1 = pf1; }
	public float getPMF1() { return this.pf1; }
	public void setPMF2(float pf2) { this.pf2 = pf2; }
	public float getPMF2() { return this.pf2; }
	public void setPMF3(float pf3) { this.pf3 = pf3; }
	public float getPMF3() { return this.pf3; }
	public void setPMI1(int pi1) { this.pi1 = pi1; }
	public int getPMI1() { return this.pi1; }
	public void setPMI2(int pi2) { this.pi2 = pi2; }
	public int getPMI2() { return this.pi2; }
	public void setAssetMeshPath(String assetMeshPath) { this.assetMeshPath = assetMeshPath; }
	public String getAssetMeshPath() { return this.assetMeshPath; }
	public void setEmitterShapeNode(Node emitterShapeNode) { this.emitterShapeNode = emitterShapeNode; }
	public Node getEmitterShapeNode() { return this.emitterShapeNode; }
	public void setEmitterMesh(Mesh emitterMesh) { this.emitterMesh = emitterMesh; }
	public Mesh getEmitterMesh() { return this.emitterMesh; }
	public void setAnimControl(AnimControl animControl) { this.animControl = animControl; }
	public AnimControl getAnimControl() { return this.animControl; }
	public void setAnimChannel(AnimChannel animChannel) { this.animChannel = animChannel; }
	public AnimChannel getAnimChannel() { return this.animChannel; }
	public void setParticleMeshType(Class<ParticleDataMesh> t) { this.particleMeshType = t; }
	public Class<ParticleDataMesh> getParticleMeshType() { return this.particleMeshType; }
	public void setParticleShapeMesh(Class t) { this.particleShapeMesh = t; }
	public Class getParticleShapeMesh() { return this.particleShapeMesh; }
	public void setParticleMesh(Mesh mesh) { this.particleMesh = mesh; }
	public Mesh getParticleMesh() { return this.particleMesh; }
	public void setWasEnabled(boolean wasEnabled) { this.wasEnabled = wasEnabled; }
	public boolean getWasEnabled() { return wasEnabled; }
	public void setAnimate(boolean animate) { this.animate = animate; }
	public boolean getAnimate() { return this.animate; }
	public void setAnimationName(String animationName) { this.animationName = animationName; }
	public String getAnimationName() { return this.animationName; }
	public void setAnimSpeed(float animSpeed) { this.animSpeed = animSpeed; }
	public float getAnimSpeed() { return this.animSpeed; }
	public void setBlendTime(float blendTime) { this.blendTime = blendTime; }
	public float getBlendTime() { return this.blendTime; }

	public void setParticleMeshPath(String particleMeshPath) { this.particleMeshPath = particleMeshPath; }
	public String getParticleMeshPath() { return this.particleMeshPath; }
	public void setParticleAnimate(boolean particleAnimate) { this.particleAnimate = particleAnimate; }
	public boolean getParticleAnimate() { return this.particleAnimate; }
	public void setParticleAnimationName(String particleAnimationName) { this.particleAnimationName = particleAnimationName; }
	public String getParticleAnimationName() { return this.particleAnimationName; }
	public void setParticleAnimSpeed(float particleAnimSpeed) { this.particleAnimSpeed = particleAnimSpeed; }
	public float getParticleAnimSpeed() { return this.particleAnimSpeed; }
	public void setParticleBlendTime(float particleBlendTime) { this.particleBlendTime = particleBlendTime; }
	public float getParticleBlendTime() { return this.particleBlendTime; }
}
