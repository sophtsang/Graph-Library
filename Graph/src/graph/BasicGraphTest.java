package graph;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import java.util.Set;

public class BasicGraphTest {

    @DisplayName("WHEN the graph is empty.")
    @Test
    void testEmptyGraph() {
        BasicGraph<String> g = new BasicGraph<String>();
        assertEquals(0, g.vertexCount());
    }

    @DisplayName("WHEN the graph is sparse.")
    @Test
    void testSparseGraph() {
        // Make an incomingEdge, outgoingEdge factory from text. LabelType is String.
        BasicGraph<String> g = new BasicGraph<>();
        //A
        g.addVertex(new BasicVertex<String>("A"),
                List.of(new BasicEdge<>("B", 3)),
                List.of(new BasicEdge<>("C", 1)));
        //B
        List<BasicEdge<String, Integer>> edgesB = List.of(new BasicEdge<>("A", 3),
                new BasicEdge<>("C", 4), new BasicEdge<>("D", 2));
        g.addVertex(new BasicVertex<String>("B"), List.of(), edgesB);
        //C
        List<BasicEdge<String, Integer>> edgesC = List.of(new BasicEdge<>("A", 1),
                new BasicEdge<>("B", 4), new BasicEdge<>("E", 2));
        g.addVertex(new BasicVertex<String>("C"), edgesC, List.of());
        //D
        List<BasicEdge<String, Integer>> edgesD = List.of(new BasicEdge<>("B", 2),
                new BasicEdge<>("E", 7));
        g.addVertex(new BasicVertex<String>("D"), edgesD, List.of(new BasicEdge<>("E",
                7)));
        //E
        List<BasicEdge<String, Integer>> edgesE = List.of(new BasicEdge<>("C", 2),
                new BasicEdge<>("D", 7));
        g.addVertex(new BasicVertex<String>("E"),
                List.of(new BasicEdge<>("D", 7)), edgesE);
        //F
        g.addVertex(new BasicVertex<String>("F"));

        assertEquals(6, g.vertexCount());
        assertEquals(1, g.getVertex("A").incomingEdges().size());
        assertEquals(3, g.getVertex("B").outgoingEdges().size());
        int i = 0;
        for (String label : g.getVertex("B").outgoingEdges().keySet()) {
            assertEquals(edgesB.get(i).neighbor(), label);
            i++;
        }
        assertTrue(g.getVertex("C").outgoingEdges().keySet().isEmpty());
        assertEquals(3, g.getVertex("C").incomingEdges().keySet().size());
        int j = 0;
        for (String label : g.getVertex("C").incomingEdges().keySet()) {
            assertEquals(edgesC.get(j).neighbor(), label);
            j++;
        }
        assertEquals(2, g.getVertex("D").incomingEdges().size());
        int k = 0;
        for (String label : g.getVertex("D").incomingEdges().keySet()) {
            assertEquals(edgesD.get(k).neighbor(), label);
            k++;
        }
        assertEquals(1, g.getVertex("E").incomingEdges().size());
        assertEquals(7, g.edgeCount());
    }

    @DisplayName("WHEN the graph is dense.")
    @Test
    void testDenseGraph() {
        // graph.addVertex(label, incomingEdges, outgoingEdges)
        BasicGraph<String> g = new BasicGraph<>();
        //A
        List<BasicEdge<String, Integer>> inedgesA = List.of(new BasicEdge<>("B", 3),
                new BasicEdge<>("E", 1));
        List<BasicEdge<String, Integer>> outedgesA = List.of(new BasicEdge<>("B", 3),
                new BasicEdge<>("C", 1), new BasicEdge<>("D", 1),
                new BasicEdge<>("F", 5));
        g.addVertex(new BasicVertex<String>("A"), inedgesA, outedgesA);
        //B
        List<BasicEdge<String, Integer>> inedgesB = List.of(new BasicEdge<>("A", 3),
                new BasicEdge<>("D", 2), new BasicEdge<>("E", 2));
        List<BasicEdge<String, Integer>> outedgesB = List.of(new BasicEdge<>("A", 3),
                new BasicEdge<>("C", 4), new BasicEdge<>("D", 2));
        g.addVertex(new BasicVertex<String>("B"), inedgesB, outedgesB);
        //C
        List<BasicEdge<String, Integer>> inedgesC = List.of(new BasicEdge<>("A", 1),
                new BasicEdge<>("B", 4), new BasicEdge<>("D", 3),
                new BasicEdge<>("E", 2));
        g.addVertex(new BasicVertex<String>("C"), inedgesC,
                List.of(new BasicEdge<>("D", 3)));
        //D
        List<BasicEdge<String, Integer>> inedgesD = List.of(new BasicEdge<>("A", 1),
                new BasicEdge<>("B", 2), new BasicEdge<>("C", 3));
        List<BasicEdge<String, Integer>> outedgesD = List.of(new BasicEdge<>("B", 3),
                new BasicEdge<>("C", 3), new BasicEdge<>("E", 7));
        g.addVertex(new BasicVertex<String>("D"), inedgesD, outedgesD);
        //E
        List<BasicEdge<String, Integer>> inedgesE = List.of(new BasicEdge<>("D", 7),
                new BasicEdge<>("F", 7));
        List<BasicEdge<String, Integer>> outedgesE = List.of(new BasicEdge<>("A", 1),
                new BasicEdge<>("B", 2), new BasicEdge<>("C", 2));
        g.addVertex(new BasicVertex<String>("E"), inedgesE, outedgesE);
        g.addVertex(new BasicVertex<String>("F"),
                List.of(new BasicEdge<>("A", 5)),
                List.of(new BasicEdge<>("E", 7)));

        assertEquals(6, g.vertexCount());
        assertEquals(2, g.getVertex("A").incomingEdges().size());
        assertEquals(3, g.getVertex("B").outgoingEdges().size());
        int i = 0;
        for (String label : g.getVertex("B").incomingEdges().keySet()) {
            assertEquals(inedgesB.get(i).neighbor(), label);
            i++;
        }
        assertEquals(1, g.getVertex("C").outgoingEdges().size());
        assertEquals(4, g.getVertex("C").incomingEdges().size());
        int j = 0;
        for (String label : g.getVertex("C").incomingEdges().keySet()) {
            assertEquals(inedgesC.get(j).neighbor(), label);
            j++;
        }
        assertEquals(3, g.getVertex("D").incomingEdges().size());
        int k = 0;
        for (String label : g.getVertex("D").outgoingEdges().keySet()) {
            assertEquals(outedgesD.get(k).neighbor(), label);
            k++;
        }
        assertEquals(2, g.getVertex("E").incomingEdges().size());
        assertEquals(3, g.getVertex("E").outgoingEdges().size());
        int l = 0;
        for (String label : g.getVertex("E").outgoingEdges().keySet()) {
            assertEquals(outedgesE.get(l).neighbor(), label);
            l++;
        }
        assertEquals(1, g.getVertex("F").incomingEdges().size());
        assertEquals(15, g.edgeCount());
    }

    @DisplayName("WHEN the graph is sparse.")
    @Test
    void testRemoveSparseGraph() {
        // Make an incomingEdge, outgoingEdge factory from text. LabelType is String.
        BasicGraph<String> g = new BasicGraph<>();
        //A
        g.addVertex(new BasicVertex<String>("A"),
                List.of(new BasicEdge<>("B", 3)),
                List.of(new BasicEdge<>("C", 1)));
        //B
        List<BasicEdge<String, Integer>> edgesB = List.of(new BasicEdge<>("A", 3),
                new BasicEdge<>("C", 4), new BasicEdge<>("D", 2));
        g.addVertex(new BasicVertex<String>("B"), List.of(), edgesB);
        //C
        List<BasicEdge<String, Integer>> edgesC = List.of(new BasicEdge<>("A", 1),
                new BasicEdge<>("B", 4), new BasicEdge<>("E", 2));
        g.addVertex(new BasicVertex<String>("C"), edgesC, List.of());
        //D
        List<BasicEdge<String, Integer>> edgesD = List.of(new BasicEdge<>("B", 2),
                new BasicEdge<>("E", 7));
        g.addVertex(new BasicVertex<String>("D"), edgesD, List.of(new BasicEdge<>("E",
                7)));
        //E
        List<BasicEdge<String, Integer>> edgesE = List.of(new BasicEdge<>("C", 2),
                new BasicEdge<>("D", 7));
        g.addVertex(new BasicVertex<String>("E"),
                List.of(new BasicEdge<>("D", 7)), edgesE);
        //F
        g.addVertex(new BasicVertex<String>("F"));

        g.removeVertex("C");
        assertEquals(5, g.vertexCount());
        assertEquals(4, g.edgeCount());
        assertFalse(g.index.containsKey("C"));
        assertFalse(g.getVertex("A").outgoingEdges().containsKey("C"));
        assertTrue(g.getVertex("B").outgoingEdges().containsKey("A"));
        assertFalse(g.getVertex("B").outgoingEdges().containsKey("C"));
        assertFalse(g.getVertex("E").outgoingEdges().containsKey("C"));

        g.removeVertex("D");
        assertEquals(4, g.vertexCount());
        assertEquals(1, g.edgeCount());
        assertTrue(g.getVertex("E").outgoingEdges().isEmpty());
        assertTrue(g.getVertex("E").incomingEdges().isEmpty());
        assertFalse(g.getVertex("B").outgoingEdges().containsKey("D"));

        g.removeVertex("E");
        g.removeVertex("F");
        g.removeVertex("B");
        g.removeVertex("A");
        assertTrue(g.vertices.isEmpty());
        assertTrue(g.index.isEmpty());
        assertEquals(0, g.vertexCount());
    }

    @DisplayName("WHEN the graph is dense.")
    @Test
    void testRemoveDenseGraph() {
        // graph.addVertex(label, incomingEdges, outgoingEdges)
        BasicGraph<String> g = new BasicGraph<>();
        //A
        List<BasicEdge<String, Integer>> inedgesA = List.of(new BasicEdge<>("B", 3),
                new BasicEdge<>("E", 1));
        List<BasicEdge<String, Integer>> outedgesA = List.of(new BasicEdge<>("B", 3),
                new BasicEdge<>("C", 1), new BasicEdge<>("D", 1),
                new BasicEdge<>("F", 5));
        g.addVertex(new BasicVertex<String>("A"), inedgesA, outedgesA);
        //B
        List<BasicEdge<String, Integer>> inedgesB = List.of(new BasicEdge<>("A", 3),
                new BasicEdge<>("D", 2), new BasicEdge<>("E", 2));
        List<BasicEdge<String, Integer>> outedgesB = List.of(new BasicEdge<>("A", 3),
                new BasicEdge<>("C", 4), new BasicEdge<>("D", 2));
        g.addVertex(new BasicVertex<String>("B"), inedgesB, outedgesB);
        //C
        List<BasicEdge<String, Integer>> inedgesC = List.of(new BasicEdge<>("A", 1),
                new BasicEdge<>("B", 4), new BasicEdge<>("D", 3),
                new BasicEdge<>("E", 2));
        g.addVertex(new BasicVertex<String>("C"), inedgesC,
                List.of(new BasicEdge<>("D", 3)));
        //D
        List<BasicEdge<String, Integer>> inedgesD = List.of(new BasicEdge<>("A", 1),
                new BasicEdge<>("B", 2), new BasicEdge<>("C", 3));
        List<BasicEdge<String, Integer>> outedgesD = List.of(new BasicEdge<>("B", 3),
                new BasicEdge<>("C", 3), new BasicEdge<>("E", 7));
        g.addVertex(new BasicVertex<String>("D"), inedgesD, outedgesD);
        //E
        List<BasicEdge<String, Integer>> inedgesE = List.of(new BasicEdge<>("D", 7),
                new BasicEdge<>("F", 7));
        List<BasicEdge<String, Integer>> outedgesE = List.of(new BasicEdge<>("A", 1),
                new BasicEdge<>("B", 2), new BasicEdge<>("C", 2));
        g.addVertex(new BasicVertex<String>("E"), inedgesE, outedgesE);
        g.addVertex(new BasicVertex<String>("F"),
                List.of(new BasicEdge<>("A", 5)),
                List.of(new BasicEdge<>("E", 7)));

        g.removeVertex("F");
        assertEquals(5, g.vertexCount());
        assertEquals(13, g.edgeCount());
        assertFalse(g.getVertex("A").outgoingEdges().containsKey("F"));
        assertFalse(g.getVertex("E").incomingEdges().containsKey("F"));

        g.removeVertex("C");
        assertEquals(4, g.vertexCount());
        assertEquals(8, g.edgeCount());
        assertFalse(g.getVertex("A").outgoingEdges().containsKey("C"));
        assertFalse(g.getVertex("E").outgoingEdges().containsKey("C"));
        assertFalse(g.getVertex("B").outgoingEdges().containsKey("C"));
        assertFalse(g.getVertex("D").outgoingEdges().containsKey("C"));
        assertFalse(g.getVertex("D").incomingEdges().containsKey("C"));

        g.removeVertex("B");
        assertEquals(3, g.vertexCount());
        assertEquals(3, g.edgeCount());
        assertFalse(g.getVertex("D").outgoingEdges().containsKey("B"));
        assertFalse(g.getVertex("D").incomingEdges().containsKey("B"));
        assertFalse(g.getVertex("A").outgoingEdges().containsKey("B"));
        assertFalse(g.getVertex("A").incomingEdges().containsKey("B"));
        assertFalse(g.getVertex("E").outgoingEdges().containsKey("B"));

        assertThrows(AssertionError.class, () -> g.removeVertex("F"));
        assertThrows(AssertionError.class, () -> g.removeVertex("B"));
        assertThrows(AssertionError.class, () -> g.removeVertex("C"));

        g.removeVertex("A");
        g.removeVertex("D");
        assertEquals(1, g.vertexCount());
        assertEquals(0, g.edgeCount());
    }

    @DisplayName("WHEN the graph is empty.")
    @Test
    void testAddEdgeEmptyGraph() {
        BasicGraph<String> g = new BasicGraph<String>();
        g.addVertex(new BasicVertex<>("A"));
        g.addVertex(new BasicVertex<>("B"));
        g.addEdge("A", new BasicEdge<>("B", 3));

        assertEquals(2, g.vertexCount());
        assertEquals(1, g.edgeCount());
        assertTrue(g.getVertex("A").outgoingEdges().containsKey("B"));
        assertTrue(g.getVertex("B").incomingEdges().containsKey("A"));
    }

    @DisplayName("WHEN the graph is sparse.")
    @Test
    void testModifyEdgeSparseGraph() {
        // Make an incomingEdge, outgoingEdge factory from text. LabelType is String.
        BasicGraph<String> g = new BasicGraph<>();
        //A
        g.addVertex(new BasicVertex<String>("A"),
                List.of(new BasicEdge<>("B", 3)),
                List.of(new BasicEdge<>("C", 1)));
        //B
        List<BasicEdge<String, Integer>> edgesB = List.of(new BasicEdge<>("A", 3),
                new BasicEdge<>("D", 2));
        g.addVertex(new BasicVertex<String>("B"), List.of(), edgesB);
        //C
        List<BasicEdge<String, Integer>> edgesC = List.of(new BasicEdge<>("A", 1),
                new BasicEdge<>("E", 2));
        g.addVertex(new BasicVertex<String>("C"), edgesC, List.of());
        //D
        List<BasicEdge<String, Integer>> edgesD = List.of(new BasicEdge<>("B", 2));
        g.addVertex(new BasicVertex<String>("D"), edgesD, List.of());
        //E
        List<BasicEdge<String, Integer>> edgesE = List.of(new BasicEdge<>("C", 2));
        g.addVertex(new BasicVertex<String>("E"), List.of(), edgesE);
        //F
        g.addVertex(new BasicVertex<String>("F"));

        assertEquals(4, g.edgeCount());

        g.addEdge("D", new BasicEdge<>("E", 7));
        assertTrue(g.getVertex("D").outgoingEdges().containsKey("E"));
        assertTrue(g.getVertex("E").incomingEdges().containsKey("D"));

        assertFalse(g.removeEdge("A", "B"));
        assertEquals(5, g.edgeCount());
        assertTrue(g.removeEdge("B", "A"));

        g.removeVertex("C");
        assertEquals(5, g.vertexCount());
        assertEquals(2, g.edgeCount());
        assertThrows(AssertionError.class,
                () -> g.addEdge("E", new BasicEdge<>("C", 3)));
        assertThrows(AssertionError.class, () -> g.removeEdge("E", "C"));
        assertEquals(0, g.getVertex("A").edgeCount());

        g.addEdge("F", new BasicEdge<>("E", 7));
        g.addEdge("E", new BasicEdge<>("A", 1));
        assertEquals(4, g.edgeCount());
        assertEquals(3, g.getVertex("E").edgeCount());
    }

    @DisplayName("WHEN the graph is sparse and acyclic.")
    @Test
    void testTopologicalSortSparseGraph() {
        // Make an incomingEdge, outgoingEdge factory from text. LabelType is String.
        BasicGraph<String> g = new BasicGraph<>();
        //A
        g.addVertex(new BasicVertex<String>("A"),
                List.of(new BasicEdge<>("B", 3)),
                List.of(new BasicEdge<>("C", 1)));
        //B
        List<BasicEdge<String, Integer>> outedgesB = List.of(new BasicEdge<>("A", 3),
                new BasicEdge<>("C", 4), new BasicEdge<>("D", 2));
        g.addVertex(new BasicVertex<String>("B"), List.of(), outedgesB);
        //C
        List<BasicEdge<String, Integer>> inedgesC = List.of(new BasicEdge<>("A", 1),
                new BasicEdge<>("B", 4), new BasicEdge<>("E", 2));
        g.addVertex(new BasicVertex<String>("C"), inedgesC, List.of());
        //D
        List<BasicEdge<String, Integer>> inedgesD = List.of(new BasicEdge<>("B", 2));
        g.addVertex(new BasicVertex<String>("D"), inedgesD, List.of(new BasicEdge<>("E",
                7)));
        //E
        List<BasicEdge<String, Integer>> outedgesE = List.of(new BasicEdge<>("C", 2));
        g.addVertex(new BasicVertex<String>("E"),
                List.of(new BasicEdge<>("D", 7)), outedgesE);
        //F
        g.addVertex(new BasicVertex<String>("F"));

        assertEquals(List.of("B", "D", "E", "F", "A", "C"), g.topologicalSort());
    }

    @DisplayName("WHEN the graph is sparse and cyclic.")
    @Test
    void testTopologicalSortCyclicGraph() {
        // Make an incomingEdge, outgoingEdge factory from text. LabelType is String.
        BasicGraph<String> g = new BasicGraph<>();
        //A
        g.addVertex(new BasicVertex<String>("A"),
                List.of(new BasicEdge<>("E", 1)),
                List.of(new BasicEdge<>("D", 1)));
        //D
        g.addVertex(new BasicVertex<String>("D"),
                List.of(new BasicEdge<>("A", 1)),
                List.of(new BasicEdge<>("E", 3)));
        //E
        g.addVertex(new BasicVertex<String>("E"),
                List.of(new BasicEdge<>("D", 3)),
                List.of(new BasicEdge<>("A", 1)));

        assertNull(g.topologicalSort());
    }

    @DisplayName("WHEN the graph is sparse and acyclic.")
    @Test
    void testTopologicalSortClassGraphA() {
        // Make an incomingEdge, outgoingEdge factory from text. LabelType is String.
        BasicGraph<String> g = new BasicGraph<>();
        g.addVertex(new BasicVertex<String>("Trig"));
        g.addVertex(new BasicVertex<String>("Calc"));
        g.addVertex(new BasicVertex<String>("ODEs"));
        g.addVertex(new BasicVertex<String>("Vectors"));
        g.addVertex(new BasicVertex<String>("NonLin"));
        g.addVertex(new BasicVertex<String>("Rings"));
        g.addVertex(new BasicVertex<String>("Galois"));
        g.addVertex(new BasicVertex<String>("PDEs"));
        g.addEdge("Calc", new BasicEdge("ODEs", 0));
        g.addEdge("Calc", new BasicEdge("Vectors", 0));
        g.addEdge("ODEs", new BasicEdge("NonLin", 0));
        g.addEdge("ODEs", new BasicEdge("PDEs", 0));
        g.addEdge("Vectors", new BasicEdge("NonLin", 0));
        g.addEdge("Vectors", new BasicEdge("Rings", 0));
        g.addEdge("Rings", new BasicEdge("Galois", 0));

        assertEquals(List.of("Trig", "Calc", "ODEs", "Vectors", "NonLin", "Rings", "Galois", "PDEs"),
                g.topologicalSort());
    }

    @DisplayName("WHEN the graph is sparse and acyclic.")
    @Test
    void testTopologicalSortClassGraphB() {
        // Make an incomingEdge, outgoingEdge factory from text. LabelType is String.
        BasicGraph<String> g = new BasicGraph<>();
        g.addVertex(new BasicVertex<String>("Calc"));
        g.addVertex(new BasicVertex<String>("Vectors"));
        g.addVertex(new BasicVertex<String>("Rings"));
        g.addVertex(new BasicVertex<String>("ODEs"));
        g.addVertex(new BasicVertex<String>("PDEs"));
        g.addVertex(new BasicVertex<String>("Trig"));
        g.addVertex(new BasicVertex<String>("NonLin"));
        g.addVertex(new BasicVertex<String>("Galois"));
        g.addEdge("Calc", new BasicEdge("ODEs", 0));
        g.addEdge("Calc", new BasicEdge("Vectors", 0));
        g.addEdge("ODEs", new BasicEdge("NonLin", 0));
        g.addEdge("ODEs", new BasicEdge("PDEs", 0));
        g.addEdge("Vectors", new BasicEdge("NonLin", 0));
        g.addEdge("Vectors", new BasicEdge("Rings", 0));
        g.addEdge("Rings", new BasicEdge("Galois", 0));

        assertEquals(List.of("Calc", "Vectors", "Rings", "ODEs", "PDEs", "Trig", "NonLin", "Galois"),
                g.topologicalSort());
    }

    @DisplayName("WHEN the graph is sparse and acyclic.")
    @Test
    void testTopologicalSortClassGraphC() {
        // Make an incomingEdge, outgoingEdge factory from text. LabelType is String.
        BasicGraph<String> g = new BasicGraph<>();
        //Calc
        g.addVertex(new BasicVertex<String>("Calc"),
                List.of(),
                List.of(new BasicEdge<>("Vectors", 0),
                        new BasicEdge<>("ODEs", 0)));
        //Vectors
        g.addVertex(new BasicVertex<String>("Vectors"),
                List.of(new BasicEdge<>("Calc", 0)),
                List.of(new BasicEdge<>("NonLin", 0),
                        new BasicEdge<>("Rings", 0)));
        //NonLin
        g.addVertex(new BasicVertex<String>("NonLin"),
                List.of(new BasicEdge<>("ODEs", 0),
                        new BasicEdge<>("Vectors", 0)),
                List.of());
        //ODEs
        g.addVertex(new BasicVertex<String>("ODEs"),
                List.of(new BasicEdge<>("Calc", 0)),
                List.of(new BasicEdge<>("NonLin", 0),
                        new BasicEdge<>("PDEs", 0)));
        //PDEs
        g.addVertex(new BasicVertex<String>("PDEs"),
                List.of(new BasicEdge<>("ODEs", 0)),
                List.of());
        //Rings
        g.addVertex(new BasicVertex<String>("Rings"),
                List.of(new BasicEdge<>("Vectors", 0)),
                List.of(new BasicEdge<>("Galois", 0)));
        //Galois
        g.addVertex(new BasicVertex<String>("Galois"),
                List.of(new BasicEdge<>("Rings", 0)),
                List.of());
        //Trig
        g.addVertex(new BasicVertex<String>("Trig"));
        System.out.println(g.edgeCount());
        assertEquals(List.of("Calc", "Vectors", "ODEs", "NonLin", "Rings", "PDEs", "Galois", "Trig"),
                g.topologicalSort());
    }

    @DisplayName("WHEN the graph is sparse and acyclic.")
    @Test
    void testDFSSettlement() {
        // Make an incomingEdge, outgoingEdge factory from text. LabelType is String.
        BasicGraph<String> g = new BasicGraph<>();
        //1
        g.addVertex(new BasicVertex<String>("1"),
                List.of(),
                List.of(new BasicEdge<>("2", 0),
                        new BasicEdge<>("3", 0),
                        new BasicEdge<>("4", 0)));
        //2
        g.addVertex(new BasicVertex<String>("2"),
                List.of(),
                List.of(new BasicEdge<>("5", 0),
                        new BasicEdge<>("6", 0)));
        //3
        g.addVertex(new BasicVertex<String>("3"),
                List.of(),
                List.of(new BasicEdge<>("6", 0),
                        new BasicEdge<>("7", 0)));
        //4
        g.addVertex(new BasicVertex<String>("4"),
                List.of(),
                List.of(new BasicEdge<>("7", 0),
                        new BasicEdge<>("8", 0)));

        assertEquals(8, g.vertexCount());
        assertEquals(9, g.edgeCount());
        assertEquals(List.of("1", "2", "5", "6", "3", "7", "4", "8"),
                     g.dfsTraversal("1", "visit"));
        assertEquals(List.of("5", "6", "2", "7", "3", "8", "4", "1"),
                     g.dfsTraversal("1", "settlement"));
    }

    @DisplayName("WHEN the graph is sparse and acyclic.")
    @Test
    void testBFSSettlement() {
        // Make an incomingEdge, outgoingEdge factory from text. LabelType is String.
        BasicGraph<String> g = new BasicGraph<>();
        //1
        g.addVertex(new BasicVertex<String>("1"),
                List.of(),
                List.of(new BasicEdge<>("2", 0),
                        new BasicEdge<>("3", 0),
                        new BasicEdge<>("4", 0)));
        //2
        g.addVertex(new BasicVertex<String>("2"),
                List.of(),
                List.of(new BasicEdge<>("5", 0),
                        new BasicEdge<>("6", 0)));
        //3
        g.addVertex(new BasicVertex<String>("3"),
                List.of(),
                List.of(new BasicEdge<>("6", 0),
                        new BasicEdge<>("7", 0)));
        //4
        g.addVertex(new BasicVertex<String>("4"),
                List.of(),
                List.of(new BasicEdge<>("7", 0),
                        new BasicEdge<>("8", 0)));

        assertEquals(8, g.vertexCount());
        assertEquals(9, g.edgeCount());
        System.out.println(g.bfsTraversal("1"));
    }

    @DisplayName("WHEN the graph is sparse and acyclic.")
    @Test
    void testBastard() {
        // Make an incomingEdge, outgoingEdge factory from text. LabelType is String.
        int[] horrible = new int[]{-1,0,1,2,-1,-4};
        System.out.println(BasicGraph.sumZero(horrible));
    }

    @DisplayName("WHEN the graph is sparse and acyclic.")
    @Test
    void testCry() {
        Map<Character, String> map = new HashMap<>();
        map.put('1', "abc");
        map.put('2', "def");
        map.put('3', "ghi");

        //BasicGraph.traverseMap(new ArrayList<>(List.of('1', '2', '3')), map, 0, '1');

        System.out.println(BasicGraph.commonChars(new String[]{"cool","lock","cook"}));
        System.out.println(BasicGraph.commonChars(new String[]{"bella","label","roller"}));
    }

}

