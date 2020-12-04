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
        ga.save("Jsonfile.json");
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