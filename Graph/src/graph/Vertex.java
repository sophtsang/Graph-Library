package graph;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public interface Vertex<LabelType>{
    /*
     *  Interface defining a generic vertex of the graph.
     */

    // Returns the label of the vertex.
    LabelType label();

    // Return the total number of incoming and outgoing edges from the vertex.
    int edgeCount();

    /**
     * Returns an array of Edge types: has attributes 1) vertices adjacent to Vertex, 2) weight
     * between adjacent vertex and Vertex: analogous to single entry Map<Key = [vertices adjacent to
     * Vertex], Value = [weight between corresponding Key vertex and Vertex]>.
     * Note: use outgoingEdges for frontier building, Dijkstra's algorithm.
     */
    Map<LabelType, Integer> outgoingEdges();

    /**
     * Returns an array of Edge types: has attributes 1) vertices that Vertex is adjacent to, 2)
     * weight between Vertex and vertex that it is adjacent to: analogous to single entry Map<Key =
     * [vertices that Vertex is adjacent to], Value = [weight between Key vertex and Vertex]>.
     * Note: use incomingEdges for topological sorting, Kahn's algorithm.
     */
    Map<LabelType, Integer> incomingEdges();
}
