package emitterbuilder.builder.datastructures;

import emitterbuilder.builder.scripts.ScriptBuilder;
import emitterbuilder.builder.scripts.ScriptBuilder.ScriptType;
import java.util.LinkedList;
import java.util.List;

/**
 *
 * @author t0neg0d
 */
public class ScriptStruct {
	public List<ScriptBuilder.EmitterTask> tasks = new LinkedList();
	public String emitterName;
	public ScriptBuilder.ScriptType type;
	public boolean emitAll = false;
	public int numParticles;
	public int emitPerSec;
	public int partsPerEmit;
	public long delay;
	public long duration;

	public ScriptStruct(String emitterName, ScriptType type) {
		this.emitterName = emitterName;
		this.type = type;
	}
}