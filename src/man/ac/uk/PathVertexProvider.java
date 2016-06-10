package man.ac.uk;

import org.jgrapht.ext.VertexNameProvider;

public class PathVertexProvider implements VertexNameProvider<JustificationVertex> {

	public PathVertexProvider(){
	}

	
	@Override
	public String getVertexName(JustificationVertex v) {
		// TODO Auto-generated method stub
		return v.path;
	}
	
	
}
