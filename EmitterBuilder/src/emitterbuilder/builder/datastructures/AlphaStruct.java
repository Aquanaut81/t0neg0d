package emitterbuilder.builder.datastructures;

import emitter.Interpolation;

/**
 *
 * @author t0neg0d
 */
public class AlphaStruct {
	float alpha = 0;
	Interpolation interpolation;

	public AlphaStruct(float alpha, Interpolation interpolation) {
		this.alpha = alpha;
		this.interpolation = interpolation;
	}

	public void setAlpha(float alpha) {
		this.alpha = alpha;
	}

	public float getAlpha() { return this.alpha; }

	public void setInterpolation(Interpolation interpolation) {
		this.interpolation = interpolation;
	}

	public Interpolation getInterpolation() { return this.interpolation; }
}