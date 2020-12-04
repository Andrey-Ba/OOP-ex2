package api;

import org.junit.jupiter.api.Test;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

class DWGraph_DSTest {



    @Test
    void node_edge_insert_test()
    {
        directed_weighted_graph g = new DWGraph_DS();
        //Create a graph with 10 edges
        for(int i = 1; i<=10;i++)
        {
            node_data n = new NodeData(i);
            g.addNode(n);
        }
        assertEquals(10,g.nodeSize());

        g.connect(1,2,3.2);//1
        assertEquals(1,g.edgeSize());
        assertEquals(3.2,g.getEdge(1,2).getWeight());
        g.connect(2,1,3.1);//2
        assertEquals(2,g.edgeSize());
        assertEquals(3.1,g.getEdge(2,1).getWeight());
        g.connect(1,2,3.3);//2
        assertEquals(3.3,g.getEdge(1,2).getWeight());
        assertEquals(2,g.edgeSize());
        g.connect(1,1,5);//2
        assertEquals(2,g.edgeSize());
        g.connect(1,-1,1);//2
        assertEquals(2,g.edgeSize());
        g.connect(-1,100,40);//2
        assertEquals(2,g.edgeSize());
    }

    @Test
    void node_edge_deletion_test()
    {
        directed_weighted_graph g = new DWGraph_DS();
        for(int i = 1; i<=10;i++)
        {
            node_data n = new NodeData(i);
            g.addNode(n);
        }
        g.connect(1,2,3);//1
        g.connect(2,1,5);//2
        g.connect(1,3,6);//3
        g.connect(1,4,8);//4
        g.removeEdge(1,2);//3
        assertEquals(3,g.edgeSize());
        g.removeEdge(1,2);//3
        assertEquals(3,g.edgeSize());
        g.removeEdge(0,1);//3
        assertEquals(3,g.edgeSize());
        g.removeEdge(1,1);//3
        assertEquals(3,g.edgeSize());
        g.removeEdge(0,100);//3
        assertEquals(3,g.edgeSize());

        g.connect(1,2,21);//4
        assertEquals(4,g.edgeSize());
        g.removeNode(1);//0
        assertEquals(0,g.edgeSize());
        assertEquals(9,g.nodeSize());
        g.removeNode(1);
        assertEquals(9,g.nodeSize());
        g.removeNode(-5);
        assertEquals(9,g.nodeSize());
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