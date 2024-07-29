package graph;

public interface Graph<VertexType extends Vertex<?>>{

    /**
     * Returns number of vertices in the graph.
     */
    int vertexCount();
}
