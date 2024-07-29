package graph;

import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

public class BasicVertex<LabelType> implements Vertex<LabelType>{
    /**
     * Creating a new vertex:
     * incomingEdges = {new BasicEdge(Vertex object, weight), ...}
     * outgoingEdges = {new BasicEdge(Vertex object, weight), ...}
     * Vertex<T> vertex = new Vertex(label, outgoingEdges, incomingEdges)
     */

    private LabelType label;
    private Map<LabelType, Integer> outgoingEdges;
    private Map<LabelType, Integer> incomingEdges;

    // Use this constructor if incoming and outgoing edges already known.
    public BasicVertex(LabelType label, LinkedHashMap<LabelType, Integer> in,
            LinkedHashMap<LabelType, Integer> out) {
        this.label = label;
        this.outgoingEdges = out;
        this.incomingEdges = in;
    }

    // Use this constructor if incoming and outgoing edges will be assigned later.
    public BasicVertex(LabelType label) {
        this.label = label;
        this.outgoingEdges = new LinkedHashMap<LabelType, Integer>();
        this.incomingEdges = new LinkedHashMap<LabelType, Integer>();
    }

    @Override
    public LabelType label() {
        return label;
    }

    @Override
    public Map<LabelType, Integer> outgoingEdges() {
        return outgoingEdges;
    }

    @Override
    public Map<LabelType, Integer> incomingEdges() {
        return incomingEdges;
    }

    public int edgeCount() {
        return outgoingEdges.size() + incomingEdges.size();
    }
}
