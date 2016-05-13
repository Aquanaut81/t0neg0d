package emitterbuilder.builder;

import emitterbuilder.builder.influencers.Influencers;
import emitterbuilder.builder.shapes.Shapes;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;
import com.jme3.scene.shape.Box;
import com.jme3.scene.shape.Cylinder;
import com.jme3.scene.shape.Dome;
import com.jme3.scene.shape.Quad;
import com.jme3.scene.shape.Sphere;
import com.jme3.scene.shape.Torus;
import emitter.Emitter;
import emitter.Interpolation;
import emitter.influencers.AlphaInfluencer;
import emitter.influencers.ColorInfluencer;
import emitter.influencers.DestinationInfluencer;
import emitter.influencers.GravityInfluencer;
import emitter.influencers.ImpulseInfluencer;
import emitter.influencers.ParticleInfluencer;
import emitter.influencers.RadialVelocityInfluencer;
import emitter.influencers.RotationInfluencer;
import emitter.influencers.SizeInfluencer;
import emitterbuilder.builder.datastructures.EmitterStruct;
import emitterbuilder.gui.utils.InterpolationUtil;

/**
 *
 * @author t0neg0d
 */
public class CodeOutput {
	EmitterBuilder builder;
	Shapes sp;
	Influencers ip;
	
	public CodeOutput(EmitterBuilder builder, Shapes sp, Influencers ip) {
		this.builder = builder;
		this.sp = sp;
		this.ip = ip;
	}
	
	public void printCode() {
		if (builder.emitter != null) {
			for (EmitterStruct struct : builder.getEmitters().values()) {
				Emitter em = struct.getEmitter();
				String name = em.getName();
				
				System.out.println("Emitter " + name + " = new Emitter(");
				System.out.print("	\"" + name + "\",");
				System.out.print(" assetManager,");
				System.out.print(" " + em.getMaxParticles());
				for (ParticleInfluencer pi : em.getInfluencers()) {
					System.out.println(",");
					System.out.print("	new " + pi.getClass().getSimpleName() + "()");
				}
				System.out.println("");
				System.out.println(");");
				
				if (struct.getShapeType().equals("Simple Emitter"))
					System.out.println(name + ".setShapeSimpleEmitter();");
				else if (struct.getShapeType().equals("JME Primitive Emitter")) {
					if (em.getShape().getMesh().getClass() == Box.class) {
						Box b = (Box)struct.getEmitterMesh();
						System.out.print("Box " + name + "ES = new Box(");
						System.out.print(String.valueOf(struct.getESF1()) + "f,");
						System.out.print(" " + String.valueOf(struct.getESF2()) + "f,");
						System.out.print(" " + String.valueOf(struct.getESF3()) + "f");
						System.out.println(");");
					} else if (em.getShape().getMesh().getClass() == Cylinder.class) {
						Box b = (Box)struct.getEmitterMesh();
						System.out.print("Cylinder " + name + "ES = new Cylinder(");
						System.out.print(String.valueOf(struct.getESI1()) + ",");
						System.out.print(" " + String.valueOf(struct.getESI2()) + ",");
						System.out.print(" " + String.valueOf(struct.getESF1()) + "f,");
						System.out.print(" " + String.valueOf(struct.getESF2()) + "f");
						System.out.println(");");
					} else if (em.getShape().getMesh().getClass() == Dome.class) {
						Box b = (Box)struct.getEmitterMesh();
						System.out.print("Dome " + name + "ES = new Dome(");
						System.out.print(String.valueOf(struct.getESI1()) + ",");
						System.out.print(" " + String.valueOf(struct.getESI2()) + ",");
						System.out.print(" " + String.valueOf(struct.getESF1()) + "f");
						System.out.println(");");
					} else if (em.getShape().getMesh().getClass() == Quad.class) {
						Box b = (Box)struct.getEmitterMesh();
						System.out.print("Quad " + name + "ES = new Quad(");
						System.out.print(String.valueOf(struct.getESF1()) + "f,");
						System.out.print(" " + String.valueOf(struct.getESF2()) + "f");
						System.out.println(");");
					} else if (em.getShape().getMesh().getClass() == Sphere.class) {
						Box b = (Box)struct.getEmitterMesh();
						System.out.print("Sphere " + name + "ES = new Sphere(");
						System.out.print(String.valueOf(struct.getESI1()) + ",");
						System.out.print(" " + String.valueOf(struct.getESI2()) + ",");
						System.out.print(" " + String.valueOf(struct.getESF1()) + "f");
						System.out.println(");");
					} else if (em.getShape().getMesh().getClass() == Torus.class) {
						Box b = (Box)struct.getEmitterMesh();
						System.out.print("Torus " + name + "ES = new Torus(");
						System.out.print(String.valueOf(struct.getESI1()) + ",");
						System.out.print(" " + String.valueOf(struct.getESI2()) + ",");
						System.out.print(" " + String.valueOf(struct.getESF1()) + "f,");
						System.out.print(" " + String.valueOf(struct.getESF2()) + "f");
						System.out.println(");");
					}
					System.out.println(name + ".setShape(" + name + "ES);");
				} else {
					System.out.println("// To animate emitter shape:");
					System.out.println("// >> Add node to scene >> Get AnimControl >> Create AnimChannel >> Etc, etc... ");
					System.out.println("Node " + name + "ESNode = (Node)assetManager.loadModel(\"" + struct.getAssetMeshPath() + "\");");
					System.out.println("Mesh " + name + "ESMesh = ((Geometry)" + name + "ESNode.getChild(0)).getMesh();");
					System.out.println(name + ".setShape(" + name + "ESMesh);");
				}
				System.out.println(name + ".getShape().setDirectionType(DirectionType." + em.getDirectionType().name() + ");");
				System.out.println(name + ".setEmissionsPerSecond(" + String.valueOf(em.getEmissionsPerSecond()) + ");");
				System.out.println(name + ".setParticlesPerEmission(" + String.valueOf(em.getParticlesPerEmission()) + ");");
				if (em.getUseRandomEmissionPoint())
					System.out.println(name + ".setUseRandomEmissionPoint(" + String.valueOf(em.getUseRandomEmissionPoint()) + ");");
				if (em.getUseSequentialEmissionFace())
					System.out.println(name + ".setUseSequentialEmissionFace(" + String.valueOf(em.getUseSequentialEmissionFace()) + ");");
				if (em.getUseSequentialSkipPattern())
					System.out.println(name + ".setUseSequentialSkipPattern(" + String.valueOf(em.getUseSequentialSkipPattern()) + ");");
				if (em.getEmitterTestModeShape() || em.getEmitterTestModeParticles())
					System.out.println(name + ".setEmitterTestMode(" + 
						String.valueOf(em.getEmitterTestModeShape()) + "," +
						String.valueOf(em.getEmitterTestModeParticles()) + 
						");");
				System.out.println(name +".initParticles(ParticleDataTriMesh.class, null);");
				System.out.println(name +".setBillboardMode(BillboardMode." + em.getBillboardMode().name() + ");");
				System.out.println(name +".setForceMinMax(" + 
						String.valueOf(em.getForceMin()) + "f," +
						String.valueOf(em.getForceMax()) + "f" +
						");");
				System.out.println(name +".setLifeMinMax(" + 
						String.valueOf(em.getLifeMin()) + "f," +
						String.valueOf(em.getLifeMax()) + "f" +
						");");
				if (em.getParticlesFollowEmitter())
					System.out.println(name + ".setParticlesFollowEmitter(" + String.valueOf(em.getParticlesFollowEmitter()) + ");");
				if (em.getUseStaticParticles())
					System.out.println(name + ".setUseStaticParticles(" + String.valueOf(em.getUseStaticParticles()) + ");");
				if (em.getUseVelocityStretching()) {
					System.out.println(name + ".setUseVelocityStretching(" + String.valueOf(em.getUseVelocityStretching()) + ");");
					System.out.println(name + ".setVelocityStretchFactor(" + String.valueOf(em.getVelocityStretchFactor()) + "f);");
				}
				System.out.println(name + ".setSprite(\"" + struct.getTexturePath() + "\", " + String.valueOf(em.getSpriteColCount()) + ", " + String.valueOf(em.getSpriteRowCount()) + ");");
				System.out.println("");
				for (ParticleInfluencer inf : em.getInfluencers()) {
					String infBase = name + ".getInfluencer(" + inf.getClass().getSimpleName() + ".class)";
					String infType = inf.getClass().getSimpleName();
					infType = infType.substring(0, infType.indexOf("Influencer"));
					infType += " Influencer";
					System.out.println("// " + infType);
					if (inf.getClass() == GravityInfluencer.class) {
						if(((GravityInfluencer)inf).getAlignment() == GravityInfluencer.GravityAlignment.World) {
							System.out.print(infBase + ".setGravity(");
							System.out.print("new Vector3f(" + ((GravityInfluencer)inf).getGravity().x + "f,");
							System.out.print(((GravityInfluencer)inf).getGravity().y + "f,");
							System.out.println(((GravityInfluencer)inf).getGravity().z + "f));");
						} else {
							System.out.println(infBase + ".setAlignment(GravityAlignment." + ((GravityInfluencer)inf).getAlignment().name() + ");");
							System.out.println(infBase + ".setMagnitude(" + ((GravityInfluencer)inf).getMagnitude() + "f);");
						}
						if(!((GravityInfluencer)inf).isEnabled())
							System.out.println(infBase + ".setEnabled(false);");
						System.out.println("");
					} else if (inf.getClass() == ColorInfluencer.class) {
						ColorRGBA[] colors = ((ColorInfluencer)inf).getColors();
						Interpolation[] colorIn = ((ColorInfluencer)inf).getInterpolations();
						for (int i = 0; i < colors.length; i++)  {
							System.out.print(infBase + ".addColor(");
							System.out.print("new ColorRGBA(" + colors[i].r + "f,");
							System.out.print(colors[i].g + "f,");
							System.out.print(colors[i].b + "f,");
							System.out.print(colors[i].a + "f)");
							String colorInt = InterpolationUtil.getInterpolationName(colorIn[i]);
							if (!colorInt.equals("linear"))
								System.out.println(", Interpolation." + colorInt + ");");
							else
								System.out.println(");");
						}
						if (((ColorInfluencer)inf).getUseRandomStartColor())
							System.out.println(infBase + ".setUseRandomStartColor(true);");
						if (!((ColorInfluencer)inf).isEnabled())
							System.out.println(infBase + ".setEnabled(false);");
						if (((ColorInfluencer)inf).getFixedDuration() != 0)
							System.out.println(infBase + ".setFixedDuration(" + ((ColorInfluencer)inf).getFixedDuration() + "f);");
						System.out.println("");
					} else if (inf.getClass() == AlphaInfluencer.class) {
						Float[] alphas = ((AlphaInfluencer)inf).getAlphas();
						Interpolation[] alphaIn = ((AlphaInfluencer)inf).getInterpolations();
						for (int i = 0; i < alphas.length; i++)  {
							System.out.print(infBase + ".addAlpha(");
							System.out.print(alphas[i] + "f");
							String alphaInt = InterpolationUtil.getInterpolationName(alphaIn[i]);
							if (!alphaInt.equals("linear"))
								System.out.println(", Interpolation." + alphaInt + ");");
							else
								System.out.println(");");
						}
						if (!((AlphaInfluencer)inf).isEnabled())
							System.out.println(infBase + ".setEnabled(false);");
						if (((AlphaInfluencer)inf).getFixedDuration() != 0)
							System.out.println(infBase + ".setFixedDuration(" + ((AlphaInfluencer)inf).getFixedDuration() + "f);");
						System.out.println("");
					} else if (inf.getClass() == SizeInfluencer.class) {
						Vector3f[] sizes = ((SizeInfluencer)inf).getSizes();
						Interpolation[] sizeIn = ((SizeInfluencer)inf).getInterpolations();
						for (int i = 0; i < sizes.length; i++)  {
							System.out.print(infBase + ".addSize(");
							System.out.print("new Vector3f(" + sizes[i].x + "f,");
							System.out.print(sizes[i].y + "f,");
							System.out.print(sizes[i].z + "f)");
							String sizeInt = InterpolationUtil.getInterpolationName(sizeIn[i]);
							if (!sizeInt.equals("linear"))
								System.out.println(", Interpolation." + sizeInt + ");");
							else
								System.out.println(");");
						}
						if (((SizeInfluencer)inf).getUseRandomSize())
							System.out.println(infBase + ".setUseRandomSize(true);");
						if (!((SizeInfluencer)inf).isEnabled())
							System.out.println(infBase + ".setEnabled(false);");
						if (((SizeInfluencer)inf).getFixedDuration() != 0)
							System.out.println(infBase + ".setFixedDuration(" + ((SizeInfluencer)inf).getFixedDuration() + "f);");
						System.out.println("");
					} else if (inf.getClass() == ImpulseInfluencer.class) {
						System.out.println(infBase + ".setChance(" + ((ImpulseInfluencer)inf).getChance() + "f);");
						System.out.println(infBase + ".setStrength(" + ((ImpulseInfluencer)inf).getStrength() + "f);");
						System.out.println(infBase + ".setMagnitude(" + ((ImpulseInfluencer)inf).getMagnitude() + "f);");
						if (!((ImpulseInfluencer)inf).isEnabled())
							System.out.println(infBase + ".setEnabled(false);");
						System.out.println("");
					} else if (inf.getClass() == RadialVelocityInfluencer.class) {
						System.out.println(infBase + ".setTangentForce(" + ((RadialVelocityInfluencer)inf).getTangentForce() + "f);");
						System.out.println(infBase + ".setRadialPull(" + ((RadialVelocityInfluencer)inf).getRadialPull() + "f);");
						System.out.println(infBase + ".setRadialPullAlignment(RadialPullAlignment." + ((RadialVelocityInfluencer)inf).getRadialPullAlignment().name() + ");");
						if (((RadialVelocityInfluencer)inf).getUseRandomDirection())
							System.out.println(infBase + ".setUseRandomDirection(true);");
						if (!((RadialVelocityInfluencer)inf).isEnabled())
							System.out.println(infBase + ".setEnabled(false);");
						System.out.println("");
					} else if (inf.getClass() == RotationInfluencer.class) {
						Vector3f[] rots = ((RotationInfluencer)inf).getRotations();
						Interpolation[] rotIn = ((RotationInfluencer)inf).getInterpolations();
						for (int i = 0; i < rots.length; i++)  {
							System.out.print(infBase + ".addRotationSpeed(");
							System.out.print("new Vector3f(" + rots[i].x + "f,");
							System.out.print(rots[i].y + "f,");
							System.out.print(rots[i].z + "f)");
							String rotInt = InterpolationUtil.getInterpolationName(rotIn[i]);
							if (!rotInt.equals("linear"))
								System.out.println(", Interpolation." + rotInt + ");");
							else
								System.out.println(");");
						}
						if (((RotationInfluencer)inf).getUseRandomStartRotationX() ||
							((RotationInfluencer)inf).getUseRandomStartRotationY() ||
							((RotationInfluencer)inf).getUseRandomStartRotationZ()) {
							System.out.println(infBase + ".setUseRandomStartRotation(" +
								((RotationInfluencer)inf).getUseRandomStartRotationX() + "," +
								((RotationInfluencer)inf).getUseRandomStartRotationY() + "," +
								((RotationInfluencer)inf).getUseRandomStartRotationZ() + 
								");");
						}
						System.out.println(infBase + ".setUseRandomDirection(" + ((RotationInfluencer)inf).getUseRandomDirection() + ");");
						System.out.println(infBase + ".setUseRandomSpeed(" + ((RotationInfluencer)inf).getUseRandomSpeed() + ");");
						if (!((RotationInfluencer)inf).isEnabled())
							System.out.println(infBase + ".setEnabled(false);");
						System.out.println("");
					} else if (inf.getClass() == DestinationInfluencer.class) {
						Vector3f[] dests = ((DestinationInfluencer)inf).getDestinations();
						Float[] weights = ((DestinationInfluencer)inf).getWeights();
						Interpolation[] destIn = ((DestinationInfluencer)inf).getInterpolations();
						for (int i = 0; i < dests.length; i++)  {
							System.out.print(infBase + ".addDestination(");
							System.out.print("new Vector3f(" + dests[i].x + "f,");
							System.out.print(dests[i].y + "f,");
							System.out.print(dests[i].z + "f), ");
							System.out.print(weights[i] + "f");
							String destInt = InterpolationUtil.getInterpolationName(destIn[i]);
							if (!destInt.equals("linear"))
								System.out.println(", Interpolation." + destInt + ");");
							else
								System.out.println(");");
						}
						System.out.println(infBase + ".setUseRandomStartDestination(" + ((DestinationInfluencer)inf).getUseRandomStartDestination() + ");");
						if (!((DestinationInfluencer)inf).isEnabled())
							System.out.println(infBase + ".setEnabled(false);");
						if (((DestinationInfluencer)inf).getFixedDuration() != 0)
							System.out.println(infBase + ".setFixedDuration(" + ((DestinationInfluencer)inf).getFixedDuration() + "f);");
						System.out.println("");
					}
				}
				System.out.println("// Set transforms and add to scene");
				Vector3f loc = em.getLocalTranslation();
				System.out.println(name + ".setLocalTranslation(" +
						loc.x +"f, " +
						loc.y +"f, " +
						loc.z +"f);");
				Vector3f scale = em.getLocalScale();
				System.out.println(name + ".setLocalScale(" +
						scale.x +"f, " +
						scale.y +"f, " +
						scale.z +"f);");
				Quaternion rot = em.getLocalRotation();
				float[] angles = new float[3];
				rot.toAngles(angles);
				System.out.println(name + ".setLocalRotation(" + 
						"new Quaternion().fromAngles(" + 
							angles[0] + "f," + angles[1] + "f," + angles[2] +
						"f));");
				System.out.println("rootNode.addControl(" + name + ");");
				System.out.println(name + ".setEnabled(true);");
				System.out.println("");
			}
		}
	}
}
