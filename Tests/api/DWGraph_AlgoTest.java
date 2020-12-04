package api;

import org.junit.jupiter.api.Test;

import java.util.Collection;
import java.util.Iterator;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

class DWGraph_AlgoTest {

    @Test
    void CopyTest()
    {
        edge_data e1 = new EdgeData(1,2,5);
        edge_data e2 = new EdgeData(1,2,5);
        assertEquals(e1,e2);
        assertNotSame(e1,e2);
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