package api;

import java.util.Collection;
import java.util.HashMap;

public class DWGraph_DS implements  directed_weighted_graph{
    private HashMap<Integer,node_data> V;
    private int MC;

    public DWGraph_DS()
    {
        V = new HashMap<>();
        MC = 0;
    }

    @Override
    public node_data getNode(int key) {
        return V.get(key);
    }

    @Override
    public edge_data getEdge(int src, int dest) {
        return null;
    }

    @Override
    public void addNode(node_data n) {
        V.put(n.getKey(),n);
    }

    @Override
    public void connect(int src, int dest, double w) {

    }

    @Override
    public Collection<node_data> getV() {
        return V.values();
    }

    @Override
    public Collection<edge_data> getE(int node_id) {
        return null;
    }

    @Override
    public node_data removeNode(int key) {
        return null;
    }

    @Override
    public edge_data removeEdge(int src, int dest) {
        return null;
    }

    @Override
    public int nodeSize() {
        return V.size();
    }

    @Override
    public int edgeSize() {
        return 0;
    }

    @Override
    public int getMC() {
        return MC;
    }

    private boolean NodesCheck(int src, int dest)
    {
        if(src == dest || !V.containsKey(src) || !V.containsKey(dest))
            return false;
        return true;
    }
}
