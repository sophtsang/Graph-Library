package graph;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.Set;

public interface Edge<LabelType, Weight>{
    /*
     * Interface defining an Edge between two vertices. Contains the destination vertex and the weight
     * from source vertex to destination vertex. An Edge type is used when creating the source
     * vertex, in which constructing source vertex requires creating incomingEdge and outgoingEdge
     * arrays which contain this Edge type: maps neighboring vertices and their weight to source.
     */

    LabelType neighbor();
    Weight weight();
}
