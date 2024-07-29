package graph;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.NoSuchElementException;
import java.util.Queue;
import java.util.Set;
import java.util.Stack;
import java.util.function.Consumer;

public class BasicGraph<LabelType> implements Graph<BasicVertex<LabelType>>{
    /**
     * BasicGraph is a class that implements the Graph ADT. The graph contains no duplicate
     * vertices, and only edges between two distinct vertices can exist - no edges that originate
     * and end at the same vertex.
     */

    public static List<String> output = new ArrayList<>();
    // ArrayList 'vertices' stores all vertex in the graph.
    public final List<Vertex<LabelType>> vertices;
    // Map 'index' maps the label of a vertex to its index in ArrayList 'vertices'.
    public final Map<LabelType, Integer> index;

    /**
     * Queue of vertex IDs currently known to be reachable from the starting vertex but for whom the
     * shortest possible path has not yet been determined.  Ordered by weight of the shortest known
     * path from the starting vertex.
     */
    private final PriorityQueue<LabelType> frontier;

    private int size;
    private int edges;

    /**
     * Initializes a BasicGraph instance that represents an empty graph with no vertices or edges.
     */
    public BasicGraph() {
        vertices = new ArrayList<>();
        index = new HashMap<>();
        frontier = new MinQueue<>();
    }

    /**
     * Initializes a BasicGraph instance with already defined vertices, index, and heap fields.
     */
    public BasicGraph(List<Vertex<LabelType>> vertices, Map<LabelType, Integer> index) {
        this.vertices = vertices;
        this.index = index;
        frontier = new MinQueue<>();
    }

    @Override
    public int vertexCount() {
        return vertices.size();
    }

    public int edgeCount() {
        return edges;
    }

    public boolean containsVertex(LabelType label) {
        return index.containsKey(label);
    }

    /**
     * Returns vertex with label 'label' if it exists in the graph, else returns null.
     */
    public Vertex<LabelType> getVertex(LabelType label) {
        if (!containsVertex(label)) {
            return null;
        }
        return vertices.get(index.get(label));
    }

    /**
     *  Adds a new vertex to graph without any incoming or outgoing edges if 'vertex' does not
     *  already exist in the graph.
     */
    public void addVertex(Vertex<LabelType> vertex) {
        if (!index.containsKey(vertex.label())) {
            index.putIfAbsent(vertex.label(), vertexCount());
            vertices.add(vertex);
            size++;
        }
    }

    /**
     *  Invoke graph.addVertex(Vertex). Need to update other vertices' incoming and outgoing edges.
     *  If other vertex is an outgoing edge of Vertex, add Vertex to other vertex's incomingEdges.
     *  If other vertex is an incoming edge of Vertex, add Vertex to other vertex's outgoingEdges.
     *  Lists 'incoming' and 'outgoing' are lists containing Edge elements: Edge(neighbor, weight).
     *  Requires that vertices in outgoingEdges and incomingEdges must already exist as a vertex in
     *  BasicGraph.
     */
    public void addVertex(Vertex<LabelType> vertex, List<BasicEdge<LabelType, Integer>>
            incoming, List<BasicEdge<LabelType, Integer>> outgoing) {
        // Map vertex to the next index in ArrayList vertices: next index = verticesCount();
        addVertex(vertex);

        // If 'in' is an incoming edge of Vertex, add Vertex to 'in's outgoingEdges.
        for (BasicEdge<LabelType, Integer> in : incoming) {
            if (!index.containsKey(in.neighbor())) {
                addVertex(new BasicVertex<>(in.neighbor()));
            }
            if (!getVertex(in.neighbor()).outgoingEdges().containsKey(vertex.label())) {
                getVertex(vertex.label()).incomingEdges().putIfAbsent(in.neighbor(), in.weight());
                getVertex(in.neighbor()).outgoingEdges().putIfAbsent(vertex.label(), in.weight());
                edges++;
            }
        }

        // If label in incoming, outgoing edges does not exist in vertices, create new vertex.
        for (BasicEdge<LabelType, Integer> out : outgoing) {
            if (!index.containsKey(out.neighbor())) {
                addVertex(new BasicVertex<>(out.neighbor()));
            }
            if (!getVertex(out.neighbor()).incomingEdges().containsKey(vertex.label())) {
                getVertex(vertex.label()).outgoingEdges().putIfAbsent(out.neighbor(), out.weight());
                getVertex(out.neighbor()).incomingEdges().putIfAbsent(vertex.label(), out.weight());
                edges++;
            }
        }
    }

    /**
     * Returns and removes vertex with label 'label' from the graph by swapping vertex to be removed
     * with the last vertex in ArrayList 'vertices' and removing this last element in 'vertices'.
     * Requires that vertex with label 'label' exists in the graph.
     * O(|E|) algorithm.
     */
    public Vertex<LabelType> removeVertex(LabelType label) {
        assert getVertex(label) != null;

        Vertex<LabelType> remove = getVertex(label);
        // Replaces the value of vertices.getLast() with the index of the vertex to be removed.
        index.replace(vertices.getLast().label(), index.get(remove.label()));
        // Replaces the value for the vertex to be removed with the last index in vertices.
        vertices.set(vertices.indexOf(remove), vertices.getLast());

        // For all vertex 'in' that 'remove' is adjacent to: 'in' -> 'remove', remove vertex
        // 'remove' from 'in's outgoingEdges.
        for (LabelType in : remove.incomingEdges().keySet()) {
            getVertex(in).outgoingEdges().remove(label);
            edges--;
        }
        // For all vertex 'out' that are adjacent to 'remove': 'out' <- 'remove', remove vertex
        // 'remove' from 'out's incomingEdges.
        for (LabelType out: remove.outgoingEdges().keySet()) {
            getVertex(out).incomingEdges().remove(label);
            edges--;
        }

        // Remove last vertex in vertices: which is now vertex with label 'label'.
        index.remove(label);
        vertices.removeLast();
        size--;
        return remove;
    }

    /**
     * Add the edge: 'label' -> 'edge'.neighbor() if edge doesn't already exist. Returns true is
     * edge is added, else return false.
     * Example: to form the edge A -> B, 'label' = "A", 'edge' = new BasicEdge<>("B", weight).
     * To form the edge A <- B, 'label' = "B", 'edge' = new BasicEdge<>("A", weight).
     */
    public boolean addEdge(LabelType label, BasicEdge<LabelType, Integer> edge) {
        assert containsVertex(label) && containsVertex(edge.neighbor());

        if (!getVertex(label).outgoingEdges().containsKey(edge.neighbor())) {
            getVertex(label).outgoingEdges().put(edge.neighbor(), edge.weight());
            getVertex(edge.neighbor()).incomingEdges().put(label, edge.weight());
            edges++;
        }
        return false;
    }

    /**
     * Remove the edge: 'sourceLabel' -> 'destLabel', returns true if 'sourceLabel' has outgoingEdge
     * = 'destLabel', and 'destLabel' has incomingEdge = 'sourceLabel'.
     * Requires 'sourceLabel' and 'destLabel' vertices exist in graph.
     */
    public boolean removeEdge(LabelType sourceLabel, LabelType destLabel) {
        assert containsVertex(sourceLabel) && containsVertex(destLabel);

        if (getVertex(sourceLabel).outgoingEdges().remove(destLabel) != null) {
            getVertex(destLabel).incomingEdges().remove(sourceLabel);
            edges--;
            return true;
        }
        return false;
    }

    /**
     * Kahn's algorithm for topological sorting:
     * Continue to remove vertices with indegree = 0 until either graph is empty, or all
     * remaining vertices have at least one incoming edge. If there are no vertices remaining,
     * the graph is not cyclic and the order at which the vertices were removed represents
     * topological sorting. If there are remaining vertices that cannot be removed, then the
     * graph is cyclic, cannot be topologically sorted, and topologicalSort() returns null.
     */
    public List<LabelType> topologicalSort() {
        // Make a copy of the current graph.
        BasicGraph<LabelType> topCopy = new BasicGraph<>(new ArrayList<>(vertices),
                new HashMap<LabelType, Integer>(index));
        List<LabelType> inFrontier = new ArrayList<>();
        // if size of inFrontier doesn't change
        while (inFrontier.size() != this.vertexCount()) {
            int size = inFrontier.size();
            for (Vertex<LabelType> vertex : this.vertices) {
                if (!inFrontier.contains(vertex.label()) &&
                        topCopy.getVertex(vertex.label()).incomingEdges().isEmpty()) {
                    inFrontier.add(topCopy.removeVertex(vertex.label()).label());
                }
            }
            // If nothing is added to inFrontier after 1 for loop iteration, return null.
            if (size == inFrontier.size()) { return null; }
        }
        // Graph is topologically sorted only if all vertices are added to the new inFrontier list.
        return (inFrontier.size() == this.vertexCount()) ? inFrontier : null;
    }

    /**
     * Returns either the settlement or visitation order of vertices in the graph after depth
     * first traversal depending on whether 'order' is "settlement" or "visit", respectively.
     * If 'order' is neither "settlement" or "visit", returns null.
     */
    public List<LabelType> dfsTraversal(LabelType start, String order) {
        List<LabelType> discovered = new ArrayList<>(List.of(start));
        List<LabelType> settled = new ArrayList<>();
        dfsWalk(start, discovered, settled::add);
        return order.equals("settlement") ? settled : (order.equals("visit") ? discovered : null);
    }

    /**
     * Implements depth first search traversal of the graph from a starting vertex with label
     * 'start' in dfs settlement order.
     * Vertex is settled when all of its neighbors have already been visited: all outgoing
     * edge vertices have already been visited.
     * Do dfs recursively.
     */
    private void dfsWalk(LabelType start, List<LabelType> discovered, Consumer<LabelType> settled) {
        // Once the end of the for loop is reached (or if outgoingEdges is empty, loop will be
        // skipped), means that all neighbors of 'vertex' are visited and 'vertex' can be settled.
        for (LabelType vertex : getVertex(start).outgoingEdges().keySet()) {
            if (!discovered.contains(vertex)) {
                discovered.add(vertex);
                dfsWalk(vertex, discovered, settled);
            }
        }
        settled.accept(start);
    }

    /**
     * Breadth first search: put discovered vertices in a queue, such that the first vertex to be
     * discovered is the first vertex to be visited. Settle a vertex when all of its neighbors
     * have been discovered.
     */
    public List<LabelType> bfsTraversal(LabelType start) {
        // When removing vertex from discovered, always remove first vertex and visit.
        Queue<LabelType> frontier = new LinkedList<>(List.of(start));
        List<LabelType> discovered = new ArrayList<>();
        List<LabelType> settled = new ArrayList<>();
        while (!frontier.isEmpty()) {
            LabelType vertex = frontier.remove();
            settled.add(vertex);
            for (LabelType neighbors : getVertex(vertex).outgoingEdges().keySet()) {
                if (!discovered.contains(neighbors)) {
                    frontier.add(neighbors);
                    discovered.add(neighbors);
                }
            }
        }
        return settled;
    }

    // Start priority is 0.
    public void shortestPath(LabelType start) {
        frontier.addOrUpdate(start, 0);
        List<LabelType> discovered = new ArrayList<>();
        while (!frontier.isEmpty()) {
            LabelType vertex = frontier.remove();
            for (LabelType neighbor : getVertex(vertex).outgoingEdges().keySet()) {
                if (!discovered.contains(neighbor)) {
                    discovered.add(neighbor);
                    frontier.addOrUpdate(neighbor, getVertex(vertex).outgoingEdges().get(neighbor));
                }
            }
        }
    }

    // Find 3 numbers in array that sum to zero.
    public static List<Integer> sumZero(int[] nums) {
        int i = 0;
        int j = nums.length - 1;
        Arrays.sort(nums);
        while (i != j) {
            int sum = nums[i] + nums[j];
            if (sum <= 0) {
                for (int k = nums.length - 1; nums[k] >= 0; k--) {
                    if (k != i && k != j && nums[k] == -sum) {
                        return List.of(nums[i], nums[j], nums[k]);
                    }
                }
                i++;
            }
            if (sum >= 0) {
                for (int k = 0; nums[k] <= 0; k++) {
                    if (k != i && k != j && nums[k] == -sum) {
                        return List.of(nums[i], nums[j], nums[k]);
                    }
                }
                j--;
            }
        }
        return List.of();
    }

//    public static void traverseMap(String input, Map<Character, String> map, int index) {
//        // at the leaf node, index = height of tree = input.length(), print out path to get to the
//        // leaf -> path is input
//        if (input.length() == index) {
//            System.out.println(input);
//        } else {
//            // input = "123", index = 0, i = 0: mV = mapped character of character @ index 'index'
//            // first value: index = 0 -> "1", i = 0 -> "a": mV = map.get(input.charAt(index)).charAt(i)
//            // increments i of node when all child nodes have already been visited.
//            for (int i = 0; i < input.length(); i++) {
//                char mappedValue = map.get(input.charAt(index)).charAt(i);
//                // replace character at index 'index' with mV: index = 1: "a" + mV + "3"
//                String str = input.substring(0, index) + mappedValue + input.substring(index + 1);
//                traverseMap(str, map, index + 1);
//            }
//        }
//    }

    public static void traverseMap(ArrayList<Character> input, Map<Character, String> map, int index, char back) {
        // at the leaf node, index = height of tree = input.length(), print out path to get to the
        // leaf -> path is input
        if (input.size() == index) {
            System.out.println(input);
        } else {
            // input = "123", index = 0, i = 0: mV = mapped character of character @ index 'index'
            // first value: index = 0 -> "1", i = 0 -> "a": mV = map.get(input.charAt(index)).charAt(i)
            // increments i of node when all child nodes have already been visited.
            for (int i = 0; i < input.size(); i++) {
                char mappedValue = map.get(input.get(index)).charAt(i);
                // replace character at index 'index' with mV: index = 1: "a" + mV + "3"
                char oldChar = input.get(index);
                input.set(index, mappedValue);
                traverseMap(input, map, index + 1, oldChar);
                input.set(index, oldChar);
            }
        }
    }

    public static List<String> commonChars(String[] words) {
        List<String> letters = new ArrayList<>();
        Map<String, Integer> map = new HashMap<>();
        for (String letter : List.of(words[0].split(""))) {
            if (!map.containsKey(letter)) {
                map.put(letter, 1);
            } else {
                map.replace(letter, map.get(letter) + 1);
            }
        }
        for (String word : words) {
            List<String> splitWord = new ArrayList<>(List.of(word.split("")));
            Map<String, Integer> compare = new HashMap<>();
            for (String letter : splitWord) {
                if (!compare.containsKey(letter)) {
                    compare.put(letter, 1);
                } else {
                    compare.replace(letter, compare.get(letter) + 1);
                }
            }
            for (String mapLetter : map.keySet()) {
                if (!compare.containsKey(mapLetter)) {
                    map.replace(mapLetter, 0);
                }
            }
            for (String chara : compare.keySet()) {
                if (map.containsKey(chara) && compare.get(chara) < map.get(chara)) {
                    map.replace(chara, compare.get(chara));
                }
            }
        }
        for (String chara : map.keySet()) {
            for (int i = 0; i < map.get(chara); i++) {
                letters.add(chara);
            }
        }
        return letters;
    }
}

