package emitterbuilder.builder.project;

import com.jme3.asset.AssetManager;
import emitter.Emitter;
import emitter.influencers.AlphaInfluencer;
import emitter.influencers.ColorInfluencer;
import emitter.influencers.DestinationInfluencer;
import emitter.influencers.GravityInfluencer;
import emitter.influencers.ImpulseInfluencer;
import emitter.influencers.ParticleInfluencer;
import emitter.influencers.PhysicsInfluencer;
import emitter.influencers.RadialVelocityInfluencer;
import emitter.influencers.RotationInfluencer;
import emitter.influencers.SizeInfluencer;
import emitter.influencers.SpriteInfluencer;
import emitterbuilder.builder.EmitterBuilder;
import emitterbuilder.builder.datastructures.EmitterStruct;
import emitterbuilder.builder.datastructures.ScriptStruct;
import emitterbuilder.gui.utils.InterpolationUtil;
import java.io.File;
import java.util.Collection;
import java.util.LinkedList;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 *
 * @author t0neg0d
 */
public class SaveProject {
	EmitterBuilder builder;
	AssetManager assetManager;
//	AssetInfo info;
//	AssetKey key;
	String path;
	Document doc;
	
	public  SaveProject(EmitterBuilder builder) {
		this.builder = builder;
		assetManager = builder.getApplication().getAssetManager();
		
		try {
			path = getClass().getClassLoader().getResource(
				"emitterbuilder/builder/project/locator.xml"
			).toString();
			path = path.substring(path.indexOf("/")+1, path.indexOf("build"));
			path += "assets/XML/saves/";
		} catch (Exception ex1) {  }
	}
	
	public void save(String projectName, Collection<EmitterStruct> emitters, LinkedList<ScriptStruct> scripts) {
		File f = null;
		String pathToFile = path + projectName + ".xml";
		
		try {
			f = new File(pathToFile);
			f.createNewFile();
		} catch (Exception ex) {
			System.out.println("Failed to create new file");
			System.out.println(ex);
		}
		
		if (f != null) {
			try {
				DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
				DocumentBuilder docBuilder = docFactory.newDocumentBuilder();

				doc = docBuilder.newDocument();
				Element rootElement = doc.createElement("root");
				doc.appendChild(rootElement);

				Element project = doc.createElement("project");
				rootElement.appendChild(project);
				addAttribute(project, "name", projectName);
				
				addNameValueParam(project, "testScene", String.valueOf(builder.getOptions().getUseTestScene()));
				
				Element param1 = doc.createElement("param");
				project.appendChild(param1);
				addAttribute(param1, "name", "camLoc");
				addAttribute(param1, "x", String.valueOf(builder.getApplication().getCamera().getLocation().x));
				addAttribute(param1, "y", String.valueOf(builder.getApplication().getCamera().getLocation().y));
				addAttribute(param1, "z", String.valueOf(builder.getApplication().getCamera().getLocation().z));
				
				float[] angles = new  float[3];
				builder.getApplication().getCamera().getRotation().toAngles(angles);
				
				Element param2 = doc.createElement("param");
				project.appendChild(param2);
				addAttribute(param2, "name", "camRot");
				addAttribute(param2, "x", String.valueOf(angles[0]));
				addAttribute(param2, "y", String.valueOf(angles[1]));
				addAttribute(param2, "z", String.valueOf(angles[2]));
				
				Element ems = doc.createElement("emitters");
				project.appendChild(ems);
				
				for (EmitterStruct struct : emitters) {
					Emitter e = struct.getEmitter();
					
					Element em = doc.createElement("emitter");
					ems.appendChild(em);
					addAttribute(em, "name", e.getName());
					
					parseEmitter(struct, em);
					
					Element shape = doc.createElement("shape");
					em.appendChild(shape);
					
					parseShape(struct, shape);
					
					Element particle = doc.createElement("particle");
					em.appendChild(particle);
					
					parseParticleMesh(struct, particle);
					
					Element mat = doc.createElement("material");
					em.appendChild(mat);
					
					parseMaterial(struct, mat);
					
					Element infs = doc.createElement("influencers");
					em.appendChild(infs);
					
					for (ParticleInfluencer pi : e.getInfluencers()) {
						Element inf = doc.createElement("influencer");
						infs.appendChild(inf);
						addAttribute(inf, "type", pi.getClass().getSimpleName());
						
						if (pi.getClass().getSimpleName().equals("GravityInfluencer"))
							parseGravityInfluencer(pi, inf);
						else if (pi.getClass().getSimpleName().equals("ColorInfluencer"))
							parseColorInfluencer(pi, inf);
						else if (pi.getClass().getSimpleName().equals("AlphaInfluencer"))
							parseAlphaInfluencer(pi, inf);
						else if (pi.getClass().getSimpleName().equals("SizeInfluencer"))
							parseSizeInfluencer(pi, inf);
						else if (pi.getClass().getSimpleName().equals("RotationInfluencer"))
							parseRotationInfluencer(pi, inf);
						else if (pi.getClass().getSimpleName().equals("DestinationInfluencer"))
							parseDestinationInfluencer(pi, inf);
						else if (pi.getClass().getSimpleName().equals("SpriteInfluencer"))
							parseSpriteInfluencer(pi, inf);
						else if (pi.getClass().getSimpleName().equals("RadialVelocityInfluencer"))
							parseRadialVelocityInfluencer(pi, inf);
						else if (pi.getClass().getSimpleName().equals("PhysicsInfluencer"))
							parsePhysicsInfluencer(pi, inf);
						else if (pi.getClass().getSimpleName().equals("ImpulseInfluencer"))
							parseImpulseInfluencer(pi, inf);
					}
				}
				
				Element scrs = doc.createElement("scripts");
				project.appendChild(scrs);
				
				for (ScriptStruct struct : scripts) {
					Element scr = doc.createElement("script");
					scrs.appendChild(scr);
					
					parseScript(struct, scr);
				}
				
				TransformerFactory transformerFactory = TransformerFactory.newInstance();
				Transformer transformer = transformerFactory.newTransformer();
				DOMSource source = new DOMSource(doc);
				StreamResult result = new StreamResult(f);

				transformer.transform(source, result);
			} catch (Exception ex) {
				System.out.println(ex);
			}
		}
	}
	
	private void parseEmitter(EmitterStruct struct, Element el) {
		Emitter e = struct.getEmitter();
		addNameValueParam(el, "testModeShape", String.valueOf(e.getEmitterTestModeShape()));
		addNameValueParam(el, "maxParticles", String.valueOf(e.getMaxParticles()));
		addNameValueParam(el, "emissionsPerSecond", String.valueOf(e.getEmissionsPerSecond()));
		addNameValueParam(el, "particlesPerEmission", String.valueOf(e.getParticlesPerEmission()));
		addNameValueParam(el, "useRandomEmissionPoint", String.valueOf(e.getUseRandomEmissionPoint()));
		addNameValueParam(el, "useSequentialEmissionFace", String.valueOf(e.getUseSequentialEmissionFace()));
		addNameValueParam(el, "useSequentialSkipPattern", String.valueOf(e.getUseSequentialSkipPattern()));
		addNameValueParam(el, "interpolation", String.valueOf(InterpolationUtil.getInterpolationName(e.getInterpolation())));
		
		Element loc = doc.createElement("param");
		el.appendChild(loc);
		addAttribute(loc, "name", "translation");
		addAttribute(loc, "x", String.valueOf(e.getLocalTranslation().x));
		addAttribute(loc, "y", String.valueOf(e.getLocalTranslation().y));
		addAttribute(loc, "z", String.valueOf(e.getLocalTranslation().z));
		
		Element scale = doc.createElement("param");
		el.appendChild(scale);
		addAttribute(scale, "name", "scale");
		addAttribute(scale, "x", String.valueOf(e.getLocalScale().x));
		addAttribute(scale, "y", String.valueOf(e.getLocalScale().y));
		addAttribute(scale, "z", String.valueOf(e.getLocalScale().z));
		
		float[] angles = new float[3];
		e.getLocalRotation().toAngles(angles);
		
		Element rot = doc.createElement("param");
		el.appendChild(rot);
		addAttribute(rot, "name", "rotation");
		addAttribute(rot, "x", String.valueOf(angles[0]));
		addAttribute(rot, "y", String.valueOf(angles[1]));
		addAttribute(rot, "z", String.valueOf(angles[2]));
		
	}
	private void parseMaterial(EmitterStruct struct, Element el) {
		Emitter e = struct.getEmitter();
		addNameValueParam(el, "matDef", String.valueOf(struct.getMaterialPath()));
		addNameValueParam(el, "blendMode", String.valueOf("AlphaAdditive"));
		addNameValueParam(el, "texturePath", String.valueOf(struct.getTexturePath()));
		addNameValueParam(el, "spriteColCount", String.valueOf(e.getSpriteColCount()));
		addNameValueParam(el, "spriteRowCount", String.valueOf(e.getSpriteRowCount()));
	}
	private void parseShape(EmitterStruct struct, Element el) {
		Emitter e = struct.getEmitter();
		
		addNameValueParam(el, "shapeType", String.valueOf(struct.getShapeType()));
		addNameValueParam(el, "shapeClass", e.getShape().getMesh().getClass().getSimpleName());
		addNameValueParam(el, "directionType", e.getDirectionType().name());
		
		if (struct.getShapeType().equals("JME Primitive Emitter")) {
			if (e.getShape().getMesh().getClass().getSimpleName().equals("Box")) {
				addNameValueParam(el, "ESF1", String.valueOf(struct.getESF1()));
				addNameValueParam(el, "ESF2", String.valueOf(struct.getESF2()));
				addNameValueParam(el, "ESF3", String.valueOf(struct.getESF3()));
			} else if (e.getShape().getMesh().getClass().getSimpleName().equals("Cylinder")) {
				addNameValueParam(el, "ESI1", String.valueOf(struct.getESI1()));
				addNameValueParam(el, "ESI2", String.valueOf(struct.getESI2()));
				addNameValueParam(el, "ESF1", String.valueOf(struct.getESF1()));
				addNameValueParam(el, "ESF2", String.valueOf(struct.getESF2()));
			} else if (e.getShape().getMesh().getClass().getSimpleName().equals("Dome")) {
				addNameValueParam(el, "ESI1", String.valueOf(struct.getESI1()));
				addNameValueParam(el, "ESI2", String.valueOf(struct.getESI2()));
				addNameValueParam(el, "ESF1", String.valueOf(struct.getESF1()));
			} else if (e.getShape().getMesh().getClass().getSimpleName().equals("Quad")) {
				addNameValueParam(el, "ESF1", String.valueOf(struct.getESF1()));
				addNameValueParam(el, "ESF2", String.valueOf(struct.getESF2()));
			} else if (e.getShape().getMesh().getClass().getSimpleName().equals("Sphere")) {
				addNameValueParam(el, "ESI1", String.valueOf(struct.getESI1()));
				addNameValueParam(el, "ESI2", String.valueOf(struct.getESI2()));
				addNameValueParam(el, "ESF1", String.valueOf(struct.getESF1()));
			} else if (e.getShape().getMesh().getClass().getSimpleName().equals("Torus")) {
				addNameValueParam(el, "ESI1", String.valueOf(struct.getESI1()));
				addNameValueParam(el, "ESI2", String.valueOf(struct.getESI2()));
				addNameValueParam(el, "ESF1", String.valueOf(struct.getESF1()));
				addNameValueParam(el, "ESF2", String.valueOf(struct.getESF2()));
			}
		} else if (struct.getShapeType().equals("Mesh-base Emitter")) {
			addNameValueParam(el, "shapePath", struct.getAssetMeshPath());
			addNameValueParam(el, "animate", String.valueOf(struct.getAnimate()));
			addNameValueParam(el, "animationName", struct.getAnimationName());
			addNameValueParam(el, "animSpeed", String.valueOf(struct.getAnimSpeed()));
			addNameValueParam(el, "blendTime", String.valueOf(struct.getBlendTime()));
		}
	}
	private void parseParticleMesh(EmitterStruct struct, Element el) {
		Emitter e = struct.getEmitter();
		
		addNameValueParam(el, "testModeParticles", String.valueOf(e.getEmitterTestModeParticles()));
		addNameValueParam(el, "particleEmissionPoint", String.valueOf(e.getParticleEmissionPoint().name()));
		addNameValueParam(el, "billboardMode", String.valueOf(e.getBillboardMode().name()));
		addNameValueParam(el, "forceMin", String.valueOf(e.getForceMin()));
		addNameValueParam(el, "forceMax", String.valueOf(e.getForceMax()));
		addNameValueParam(el, "lifeMin", String.valueOf(e.getLifeMin()));
		addNameValueParam(el, "lifeMax", String.valueOf(e.getLifeMax()));
		addNameValueParam(el, "particlesFollowEmitter", String.valueOf(e.getParticlesFollowEmitter()));
		addNameValueParam(el, "useStaticParticles", String.valueOf(e.getUseStaticParticles()));
		addNameValueParam(el, "useVelocityStretching", String.valueOf(e.getUseVelocityStretching()));
		addNameValueParam(el, "velocityStretchFactor", String.valueOf(e.getVelocityStretchFactor()));
		addNameValueParam(el, "forcedStretchAxis", String.valueOf(e.getForcedStretchAxis()));
		
		String partType = struct.getParticleType();
		addNameValueParam(el, "particleType", partType);
		
		addNameValueParam(el, "particleMeshType", struct.getParticleMeshType().getSimpleName());
		
		if (partType.equals("Primitive As Template")) {
			String partMesh = struct.getParticleMesh().getClass().getSimpleName();
			addNameValueParam(el, "particleMesh", partMesh);
			
			if (partMesh.equals("Box")) {
				addNameValueParam(el, "PMF1", String.valueOf(struct.getPMF1()));
				addNameValueParam(el, "PMF2", String.valueOf(struct.getPMF2()));
				addNameValueParam(el, "PMF3", String.valueOf(struct.getPMF3()));
			} else if (partMesh.equals("Cylinder")) {
				addNameValueParam(el, "PMI1", String.valueOf(struct.getPMI1()));
				addNameValueParam(el, "PMI2", String.valueOf(struct.getPMI2()));
				addNameValueParam(el, "PMF1", String.valueOf(struct.getPMF1()));
				addNameValueParam(el, "PMF2", String.valueOf(struct.getPMF2()));
			} else if (partMesh.equals("Dome")) {
				addNameValueParam(el, "PMI1", String.valueOf(struct.getPMI1()));
				addNameValueParam(el, "PMI2", String.valueOf(struct.getPMI2()));
				addNameValueParam(el, "PMF1", String.valueOf(struct.getPMF1()));
			} else if (partMesh.equals("Quad")) {
				addNameValueParam(el, "PMF1", String.valueOf(struct.getPMF1()));
				addNameValueParam(el, "PMF2", String.valueOf(struct.getPMF2()));
			} else if (partMesh.equals("Sphere")) {
				addNameValueParam(el, "PMI1", String.valueOf(struct.getPMI1()));
				addNameValueParam(el, "PMI2", String.valueOf(struct.getPMI2()));
				addNameValueParam(el, "PMF1", String.valueOf(struct.getPMF1()));
			} else if (partMesh.equals("Torus")) {
				addNameValueParam(el, "PMI1", String.valueOf(struct.getPMI1()));
				addNameValueParam(el, "PMI2", String.valueOf(struct.getPMI2()));
				addNameValueParam(el, "PMF1", String.valueOf(struct.getPMF1()));
				addNameValueParam(el, "PMF2", String.valueOf(struct.getPMF2()));
			}
		} else if (partType.equals("Mesh As Template")) {
			String partMesh = struct.getParticleMesh().getClass().getSimpleName();
			addNameValueParam(el, "particleMesh", partMesh);
			
			addNameValueParam(el, "shapePath", struct.getParticleMeshPath());
			addNameValueParam(el, "animate", String.valueOf(struct.getParticleAnimate()));
			addNameValueParam(el, "animationName", struct.getParticleAnimationName());
			addNameValueParam(el, "animSpeed", String.valueOf(struct.getParticleAnimSpeed()));
			addNameValueParam(el, "blendTime", String.valueOf(struct.getParticleBlendTime()));
		}
	}
	private void parseColorInfluencer(ParticleInfluencer pi, Element el) {
		ColorInfluencer inf = (ColorInfluencer)pi;
		addNameValueParam(el, "useRandomStartColor", String.valueOf(inf.getUseRandomStartColor()));
		addNameValueParam(el, "fixedDuration", String.valueOf(inf.getFixedDuration()));
		addNameValueParam(el, "enabled", String.valueOf(inf.isEnabled()));
		
		Element colors = doc.createElement("colors");
		el.appendChild(colors);
		Element interps = doc.createElement("interps");
		el.appendChild(interps);
		
		for (int i = 0; i < inf.getColors().length; i++) {
			Element c = doc.createElement("color");
			colors.appendChild(c);
			addAttribute(c, "r", String.valueOf(inf.getColors()[i].r));
			addAttribute(c, "g", String.valueOf(inf.getColors()[i].g));
			addAttribute(c, "b", String.valueOf(inf.getColors()[i].b));
			addAttribute(c, "a", String.valueOf(inf.getColors()[i].a));
			
			Element in = doc.createElement("interp");
			interps.appendChild(in);
			addAttribute(in, "value", InterpolationUtil.getInterpolationName(inf.getInterpolations()[i]));
		}
	}
	private void parseAlphaInfluencer(ParticleInfluencer pi, Element el) {
		AlphaInfluencer inf = (AlphaInfluencer)pi;
		addNameValueParam(el, "fixedDuration", String.valueOf(inf.getFixedDuration()));
		addNameValueParam(el, "enabled", String.valueOf(inf.isEnabled()));
		
		Element alphas = doc.createElement("alphas");
		el.appendChild(alphas);
		Element interps = doc.createElement("interps");
		el.appendChild(interps);
		
		for (int i = 0; i < inf.getAlphas().length; i++) {
			Element a = doc.createElement("alpha");
			alphas.appendChild(a);
			addAttribute(a, "value", String.valueOf(inf.getAlphas()[i]));
			
			Element in = doc.createElement("interp");
			interps.appendChild(in);
			addAttribute(in, "value", InterpolationUtil.getInterpolationName(inf.getInterpolations()[i]));
		}
	}
	private void parseSizeInfluencer(ParticleInfluencer pi, Element el) {
		SizeInfluencer inf = (SizeInfluencer)pi;
		addNameValueParam(el, "fixedDuration", String.valueOf(inf.getFixedDuration()));
		addNameValueParam(el, "enabled", String.valueOf(inf.isEnabled()));
		addNameValueParam(el, "randomSizeTolerance", String.valueOf(inf.getRandomSizeTolerance()));
		addNameValueParam(el, "useRandomSize", String.valueOf(inf.getUseRandomSize()));
		
		Element sizes = doc.createElement("sizes");
		el.appendChild(sizes);
		Element interps = doc.createElement("interps");
		el.appendChild(interps);
		
		for (int i = 0; i < inf.getSizes().length; i++) {
			Element s = doc.createElement("size");
			sizes.appendChild(s);
			addAttribute(s, "x", String.valueOf(inf.getSizes()[i].x));
			addAttribute(s, "y", String.valueOf(inf.getSizes()[i].y));
			addAttribute(s, "z", String.valueOf(inf.getSizes()[i].z));
			
			Element in = doc.createElement("interp");
			interps.appendChild(in);
			addAttribute(in, "value", InterpolationUtil.getInterpolationName(inf.getInterpolations()[i]));
		}
	}
	private void parseRotationInfluencer(ParticleInfluencer pi, Element el) {
		RotationInfluencer inf = (RotationInfluencer)pi;
		addNameValueParam(el, "useRandomSpeed", String.valueOf(inf.getUseRandomSpeed()));
		addNameValueParam(el, "enabled", String.valueOf(inf.isEnabled()));
		addNameValueParam(el, "useRandomStartRotationX", String.valueOf(inf.getUseRandomStartRotationX()));
		addNameValueParam(el, "useRandomStartRotationY", String.valueOf(inf.getUseRandomStartRotationY()));
		addNameValueParam(el, "useRandomStartRotationZ", String.valueOf(inf.getUseRandomStartRotationZ()));
		addNameValueParam(el, "useRandomDirection", String.valueOf(inf.getUseRandomDirection()));
		
		Element rots = doc.createElement("rots");
		el.appendChild(rots);
		Element interps = doc.createElement("interps");
		el.appendChild(interps);
		
		for (int i = 0; i < inf.getRotations().length; i++) {
			Element r = doc.createElement("rot");
			rots.appendChild(r);
			addAttribute(r, "x", String.valueOf(inf.getRotations()[i].x));
			addAttribute(r, "y", String.valueOf(inf.getRotations()[i].y));
			addAttribute(r, "z", String.valueOf(inf.getRotations()[i].z));
			
			Element in = doc.createElement("interp");
			interps.appendChild(in);
			addAttribute(in, "value", InterpolationUtil.getInterpolationName(inf.getInterpolations()[i]));
		}
	}
	private void parseDestinationInfluencer(ParticleInfluencer pi, Element el) {
		DestinationInfluencer inf = (DestinationInfluencer)pi;
		addNameValueParam(el, "fixedDuration", String.valueOf(inf.getFixedDuration()));
		addNameValueParam(el, "enabled", String.valueOf(inf.isEnabled()));
		addNameValueParam(el, "useRandomStartDestination", String.valueOf(inf.getUseRandomStartDestination()));
		
		Element dests = doc.createElement("dests");
		el.appendChild(dests);
		Element weights = doc.createElement("weights");
		el.appendChild(weights);
		Element interps = doc.createElement("interps");
		el.appendChild(interps);
		
		for (int i = 0; i < inf.getDestinations().length; i++) {
			Element d = doc.createElement("dest");
			dests.appendChild(d);
			addAttribute(d, "x", String.valueOf(inf.getDestinations()[i].x));
			addAttribute(d, "y", String.valueOf(inf.getDestinations()[i].y));
			addAttribute(d, "z", String.valueOf(inf.getDestinations()[i].z));
			
			Element w = doc.createElement("weight");
			weights.appendChild(w);
			addAttribute(w, "value", String.valueOf(inf.getWeights()[i]));
			
			Element in = doc.createElement("interp");
			interps.appendChild(in);
			addAttribute(in, "value", InterpolationUtil.getInterpolationName(inf.getInterpolations()[i]));
		}
	}
	private void parseGravityInfluencer(ParticleInfluencer pi, Element el) {
		GravityInfluencer inf = (GravityInfluencer)pi;
		
		Element gravity = doc.createElement("param");
		el.appendChild(gravity);
		addAttribute(gravity, "name", "gravity");
		addAttribute(gravity, "x", String.valueOf(inf.getGravity().x));
		addAttribute(gravity, "y", String.valueOf(inf.getGravity().y));
		addAttribute(gravity, "z", String.valueOf(inf.getGravity().z));
		
		addNameValueParam(el, "GravityAlignment", inf.getAlignment().name());
		addNameValueParam(el, "magnitude", String.valueOf(inf.getMagnitude()));
		addNameValueParam(el, "enabled", String.valueOf(inf.isEnabled()));
	}
	private void parseSpriteInfluencer(ParticleInfluencer pi, Element el) {
		SpriteInfluencer inf = (SpriteInfluencer)pi;
		addNameValueParam(el, "fixedDuration", String.valueOf(inf.getFixedDuration()));
		addNameValueParam(el, "enabled", String.valueOf(inf.isEnabled()));
		addNameValueParam(el, "animate", String.valueOf(inf.getAnimate()));
		addNameValueParam(el, "useRandomStartImage", String.valueOf(inf.getUseRandomStartImage()));
		
		int[] frames = inf.getFrameSequence();
		String fs = "";
		if (frames != null) {
			for (int i = 0; i < frames.length; i++) {
				if (!frames.equals(""))
					fs += ",";
				fs += String.valueOf(frames[i]);
			}
		}
		
		addNameValueParam(el, "frameSequence", fs);
	}
	private void parseRadialVelocityInfluencer(ParticleInfluencer pi, Element el) {
		RadialVelocityInfluencer inf = (RadialVelocityInfluencer)pi;
		addNameValueParam(el, "useRandomDirection", String.valueOf(inf.getUseRandomDirection()));
		addNameValueParam(el, "enabled", String.valueOf(inf.isEnabled()));
		addNameValueParam(el, "radialPull", String.valueOf(inf.getRadialPull()));
		addNameValueParam(el, "tangentForce", String.valueOf(inf.getTangentForce()));
		addNameValueParam(el, "radialPullAlignment", inf.getRadialPullAlignment().name());
		addNameValueParam(el, "radialPullCenter", inf.getRadialPullCenter().name());
		addNameValueParam(el, "radialUpAlignment", inf.getRadialUpAlignment().name());
	}
	private void parsePhysicsInfluencer(ParticleInfluencer pi, Element el) {
		PhysicsInfluencer inf = (PhysicsInfluencer)pi;
		addNameValueParam(el, "collisionReaction", inf.getCollisionReaction().name());
		addNameValueParam(el, "restitution", String.valueOf(inf.getRestitution()));
		addNameValueParam(el, "enabled", String.valueOf(inf.isEnabled()));
	}
	private void parseImpulseInfluencer(ParticleInfluencer pi, Element el) {
		ImpulseInfluencer inf = (ImpulseInfluencer)pi;
		addNameValueParam(el, "chance", String.valueOf(inf.getChance()));
		addNameValueParam(el, "magnitude", String.valueOf(inf.getMagnitude()));
		addNameValueParam(el, "strength", String.valueOf(inf.getStrength()));
	}
	private void parseScript(ScriptStruct struct, Element el) {
		addNameValueParam(el, "emitterName", struct.emitterName);
		addNameValueParam(el, "ScriptType", struct.type.name());
		switch (struct.type) {
			case Emit:
				addNameValueParam(el, "emitAll", String.valueOf(struct.emitAll));
				addNameValueParam(el, "numParticles", String.valueOf(struct.numParticles));
				addNameValueParam(el, "delay", String.valueOf(struct.delay));
				break;
			case Emit_For_Duration:
				addNameValueParam(el, "emitPerSec", String.valueOf(struct.emitPerSec));
				addNameValueParam(el, "partsPerEmit", String.valueOf(struct.partsPerEmit));
				addNameValueParam(el, "delay", String.valueOf(struct.delay));
				addNameValueParam(el, "duration", String.valueOf(struct.duration));
				break;
		}
	}
	
	private void addNameValueParam(Element el, String name, String value) {
		Element param = doc.createElement("param");
		el.appendChild(param);
		addAttribute(param, "name", name);
		addAttribute(param, "value", value);
	}
	private void addAttribute(Element el, String name, String value) {
		Attr attr = doc.createAttribute(name);
		attr.setValue(value);
		el.setAttributeNode(attr);
	}
}
