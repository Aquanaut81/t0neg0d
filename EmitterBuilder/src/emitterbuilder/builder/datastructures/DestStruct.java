package emitterbuilder.builder.datastructures;

import com.jme3.math.Vector3f;
import emitter.Interpolation;

/**
 *
 * @author t0neg0d
 */
public class DestStruct {
	Vector3f destination = new Vector3f();
	float weight;
	Interpolation interpolation;

	public DestStruct(Vector3f destination, float weight, Interpolation interpolation) {
		this.destination.set(destination);
		this.weight = weight;
		this.interpolation = interpolation;
	}

	public void setDestination(Vector3f destination) {
		this.destination.set(destination);
	}
	
	public void setDestination(float x, float y, float z) {
		this.destination.set(x,y,z);
	}
	
	public Vector3f getDestination() { return this.destination; }

	public void setWeight(float weight) { this.weight = weight; }

	public float getWeight() { return this.weight; }

	public void setInterpolation(Interpolation interpolation) {
		this.interpolation = interpolation;
	}

	public Interpolation getInterpolation() { return this.interpolation; }
}
