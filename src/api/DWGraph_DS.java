package api;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

public class DWGraph_DS implements  directed_weighted_graph{
    private HashMap<Integer,node_data> V;
    private HashMap<Integer,HashMap<Integer,edge_data>> E;
    private HashMap<Integer,HashMap<Integer,edge_data>> ER;
    private int edges;
    private int MC;

    public DWGraph_DS()
    {
        V = new HashMap<>();
        E = new HashMap<>();
        ER = new HashMap<>();
        edges = 0;
        MC = 0;
    }

    @Override
    public node_data getNode(int key) {
        return V.get(key);
    }

    @Override
    public edge_data getEdge(int src, int dest) {
        if(!EdgesCheck(src, dest))
            return null;
        return E.get(src).get(dest);
    }

    @Override
    public void addNode(node_data n) {
        V.put(n.getKey(),n);
        E.put(n.getKey(), new HashMap<>());
        ER.put(n.getKey(),new HashMap<>());
        MC++;
    }

    @Override
    public void connect(int src, int dest, double w) {
        if(!NodesCheck(src,dest))
            return;
        if(EdgesCheck(src, dest))
        {
            edge_data e = E.get(src).get(dest);
            if(e.getWeight()==w)
                return;
            ((EdgeData)e).setWeight(w);
            MC++;
            return;
        }
        edge_data e = new EdgeData(src,dest,w);
        E.get(src).put(dest,e);
        ER.get(dest).put(src,e);
        edges++;
        MC++;
    }

    @Override
    public Collection<node_data> getV() {
        return V.values();
    }

    @Override
    public Collection<edge_data> getE(int node_id) {
        return E.get(node_id).values();
    }

    @Override
    public node_data removeNode(int key) {
        //Check if the graph has that edge.
        if (!V.containsKey(key))
            return null;

        //Remove the node from all edges.
        Iterator<Integer> it = ER.get(key).keySet().iterator();
        while (it.hasNext()) {
            E.get(it.next()).remove(key);
            edges--;
        }
        //Remove the node from all reversed edges.
        Iterator<Integer> it2 = E.get(key).keySet().iterator();
        while (it2.hasNext()) {
            ER.get(it2.next()).remove(key);
            edges--;
        }
        node_data n = V.get(key);
        V.remove(key);
        E.remove(key);
        ER.remove(key);
        MC++;
        return n;
    }

    @Override
    public edge_data removeEdge(int src, int dest) {
        if(!NodesCheck(src, dest))
            return null;
        if(!EdgesCheck(src, dest))
            return null;
        edge_data e = E.get(src).get(dest);
        E.get(src).remove(dest);
        ER.get(dest).remove(src);
        edges--;
        MC++;
        return e;
    }

    @Override
    public int nodeSize() {
        return V.size();
    }

    @Override
    public int edgeSize() {
        return edges;
    }

    @Override
    public int getMC() {
        return MC;
    }

    //Checks that the src and dest nodes exists and are not the same.
    private boolean NodesCheck(int src, int dest)
    {
        if(src == dest || !V.containsKey(src) || !V.containsKey(dest))
            return false;
        return true;
    }
    //Checks if an edge already exists.
    private boolean EdgesCheck(int src, int dest)
    {
        return E.get(src).containsKey(dest);
    }
}
