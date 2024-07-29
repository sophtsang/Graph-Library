package graph;

import java.util.Map;

public class BasicEdge<LabelType, Weight> implements Edge<LabelType, Weight> {
    /**
     * BasicEdge is a class the contains the destination vertex and weight of the edge between a
     * source and destination vertex.
     */

    private Weight weight;
    private LabelType neighbor;

    public BasicEdge(LabelType neighbor, Weight weight) {
        this.weight = weight;
        this.neighbor = neighbor;
    }

    @Override
    public LabelType neighbor() {
        return neighbor;
    }

    @Override
    public Weight weight() {
        return weight;
    }
}

