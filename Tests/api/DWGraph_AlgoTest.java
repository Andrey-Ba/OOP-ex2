package api;

import org.junit.jupiter.api.Test;

import java.util.Collection;
import java.util.Iterator;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

class DWGraph_AlgoTest {

    @Test
    void SaveTest()
    {
        directed_weighted_graph g = new DWGraph_DS();
        for (int i = 0; i<100;i++)
            g.addNode(new NodeData(i));
        while (g.edgeSize()<1000)
            g.connect(nextRnd(1,101),nextRnd(1,101),nextRnd(0.1,10));
        dw_graph_algorithms ga = new DWGraph_Algo(g);
        ga.save("file.json");
        ga.load("file.json");
        assertEquals(g,ga.getGraph());
        assertNotSame(g,ga.getGraph());
    }

    @Test
    void ShortestPathTest()
    {
        directed_weighted_graph g = new DWGraph_DS();
        for(int i=0;i<100;i++)
            g.addNode(new NodeData(i));
        while (g.edgeSize()<1000)
            g.connect(nextRnd(1,101),nextRnd(1,101),nextRnd(0.5,10));
        g.connect(1,5,10);
        g.connect(1,2,0.1);
        g.connect(2,5,0.1);
        dw_graph_algorithms ga = new DWGraph_Algo(g);
        assertEquals(0.2,ga.shortestPathDist(1,5));
        directed_weighted_graph g2 = new DWGraph_DS();
        for(int i=0;i<10;i++)
            g2.addNode(new NodeData(i));
        ga.init(g2);
        assertEquals(-1,ga.shortestPathDist(1,2));
    }

    @Test
    void isconnectedTest()
    {
        directed_weighted_graph g = new DWGraph_DS();
        dw_graph_algorithms ga = new DWGraph_Algo(g);
        for(int i = 0; i<10; i++)
            g.addNode(new NodeData(i));
        assertFalse(ga.isConnected());
        while (g.edgeSize()<90)
            g.connect(nextRnd(0,10),nextRnd(0,10),nextRnd(0.5,10));
        assertTrue(ga.isConnected());

        directed_weighted_graph g2 = new DWGraph_DS();
        for(int i = 0; i<5; i++)
            g2.addNode(new NodeData(i));
        ga.init(g2);
        g2.connect(0,1,2);
        g2.connect(1,2,1);
        g2.connect(2,3,11);
        g2.connect(3,0,12);
        g2.connect(0,4,7);
        g2.connect(4,0,13);
        assertTrue(ga.isConnected());
        g2.removeEdge(4,0);
        assertFalse(ga.isConnected());
    }

    @Test
    void CopyTest()
    {
        directed_weighted_graph g = new DWGraph_DS();
        dw_graph_algorithms ga = new DWGraph_Algo(g);
        for(int i = 0; i<50;i++)
            g.addNode(new NodeData(i));
        g.connect(1,2,3);
        while (g.edgeSize()<2000)
            g.connect(nextRnd(0,50),nextRnd(0,50),nextRnd(0.5,10));
        directed_weighted_graph g2 = ga.copy();
        assertEquals(g,g2);
        g2.removeNode(5);
        assertNotEquals(g,g2);
        g2 = ga.copy();
        assertEquals(g,g2);
        g2.removeEdge(1,2);
        assertNotEquals(g,g2);
        g.removeEdge(1,2);
        g2.connect(1,2,3);
        assertNotEquals(g,g2);
    }

    public static void collprint(Collection col)
    {
        Iterator<Object> it = col.iterator();
        while (it.hasNext())
            System.out.println(it.next().toString());
    }

    static Random _rnd = new Random(42);

    public static int nextRnd(int min, int max) {
        double v = nextRnd(0.0+min, (double)max);
        int ans = (int)v;
        return ans;
    }

    public static double nextRnd(double min, double max) {
        double d = _rnd.nextDouble();
        double dx = max-min;
        double ans = d*dx+min;
        return ans;
    }
}