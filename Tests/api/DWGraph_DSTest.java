package api;

import org.junit.jupiter.api.Test;

import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

class DWGraph_DSTest {



    @Test
    void basictest()
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
        g.connect(2,1,3.10);//2
        assertEquals(2,g.edgeSize());
        g.connect(1,2,3.3);//2
        assertEquals(2,g.edgeSize());
        g.connect(1,1,5);//2
        assertEquals(2,g.edgeSize());
        g.connect(1,-1,1);//2
        assertEquals(2,g.edgeSize());
        g.connect(-1,100,40);//2
        assertEquals(2,g.edgeSize());

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