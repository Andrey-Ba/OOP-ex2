package api;

import com.google.gson.JsonParser;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.*;
import java.util.*;

public class DWGraph_Algo implements dw_graph_algorithms{

    private directed_weighted_graph g;

    public DWGraph_Algo()
    {
        g = new DWGraph_DS();
    }

    public DWGraph_Algo(directed_weighted_graph graph)
    {
        g = graph;
    }

    @Override
    public void init(directed_weighted_graph g) {
        this.g = g;
    }

    @Override
    public directed_weighted_graph getGraph() {
        return g;
    }

    @Override
    public directed_weighted_graph copy() {
        return new DWGraph_DS(g);
    }

    @Override
    public boolean isConnected() {
        if(g.nodeSize()<2)
            return true;
        if(g.edgeSize()<0 && g.edgeSize()/2< g.nodeSize()-1)
            return false;
        //Get the first node of the graph.
        Iterator<node_data> ite = g.getV().iterator();
        node_data n = ite.next();
        LinkedList<node_data> lst = new LinkedList<>();
        if(dfs(n,lst)<g.nodeSize())
            return false;
        resetnodes(lst);
        lst = new LinkedList<>();
        int c = Rdfs(n,lst);
        resetnodes(lst);
        return c==g.nodeSize();
    }

    private int dfs(node_data n, LinkedList<node_data> lst)
    {
        //List to reset the all used nodes information.
        lst.add(n);
        //How many nodes the algorithms went through.
        int c = 0;
        //Mark as grey.
        n.setInfo("g");
        //Iterator for the node's connections.
        Iterator<edge_data> it = g.getE(n.getKey()).iterator();
        while (it.hasNext())
        {
            edge_data e = it.next();
            node_data nd = g.getNode(e.getDest());
            //Check that the next connected node is not grey or black.
            if(!nd.getInfo().equals("g") && !nd.getInfo().equals("b"))
            {
                c += dfs(nd,lst);
            }
        }
        //Mark it as black
        n.setInfo("b");
        return c+1;
    }

    private int Rdfs(node_data n, LinkedList<node_data> lst)
    {
        lst.add(n);
        int c = 0;
        n.setInfo("g");
        //Iterator on the reversed edges of n.
        Iterator<edge_data> it = ((DWGraph_DS)g).getER(n.getKey()).iterator();
        while (it.hasNext())
        {
            edge_data e = it.next();
            node_data nd = g.getNode(e.getSrc());
            if(!nd.getInfo().equals("g") && !nd.getInfo().equals("b"))
            {
                c += Rdfs(nd,lst);
            }
        }
        n.setInfo("b");
        return c+1;
    }

    @Override
    public double shortestPathDist(int src, int dest) {
        PriorityQueue<node_data> pq = new PriorityQueue<>();
        //List to reset Tag and weight after the algorithm
        LinkedList<node_data> lst = new LinkedList<>();
        g.getNode(src).setWeight(0);
        pq.add(g.getNode(src));
        while (!pq.isEmpty())
        {
            node_data node = pq.remove();
            node.setInfo("B");
            lst.add(node);
            Iterator<edge_data> it = g.getE(node.getKey()).iterator();
            while (it.hasNext())
            {
                edge_data e = it.next();
                node_data n = g.getNode(e.getDest());
                //If the neighbor of the node is dest, update the weight if it is smaller.
                if(e.getDest()==dest)
                {
                    double new_weight = node.getWeight()+e.getWeight();
                    if(n.getWeight()>new_weight || n.getWeight()==-1)
                        n.setWeight(new_weight);
                }
                //If the neighbor wasn't visited add it to the priority queue and update it's weight and tag.
                else if(n.getInfo().equals(""))
                {
                    n.setInfo("V");
                    n.setWeight(node.getWeight()+e.getWeight());
                    pq.add(n);
                }
                else if(n.getInfo().equals("V"))
                {
                    double new_weight = node.getWeight()+e.getWeight();
                    if(n.getWeight()>new_weight)
                    {
                        n.setWeight(new_weight);
                        pq.remove(n);
                        pq.add(n);
                    }
                }
            }
        }
        lst.add(g.getNode(dest));
        double d = g.getNode(dest).getWeight();
        resetnodes(lst);
        return d;
    }

    @Override
    public List<node_data> shortestPath(int src, int dest) {
        PriorityQueue<node_data> pq = new PriorityQueue<>();
        //List to reset Tag and weight after the algorithm
        LinkedList<node_data> lst = new LinkedList<>();
        g.getNode(src).setWeight(0);
        pq.add(g.getNode(src));
        while (!pq.isEmpty())
        {
            node_data node = pq.remove();
            node.setInfo("B");
            lst.add(node);
            Iterator<edge_data> it = g.getE(node.getKey()).iterator();
            while (it.hasNext())
            {
                edge_data e = it.next();
                node_data n = g.getNode(e.getDest());
                //If the neighbor of the node is dest, update the weight if it is smaller.
                if(e.getDest()==dest)
                {
                    double new_weight = node.getWeight()+e.getWeight();
                    if(n.getWeight()>new_weight || n.getWeight()==-1) {
                        n.setWeight(new_weight);
                        n.setTag(node.getKey());
                    }
                }
                //If the neighbor wasn't visited add it to the priority queue and update it's weight and tag.
                else if(n.getInfo().equals(""))
                {
                    n.setInfo("V");
                    n.setWeight(node.getWeight()+e.getWeight());
                    n.setTag(node.getKey());
                    pq.add(n);
                }
                else if(n.getInfo().equals("V"))
                {
                    double new_weight = node.getWeight()+e.getWeight();
                    if(n.getWeight()>new_weight)
                    {
                        n.setWeight(new_weight);
                        pq.remove(n);
                        pq.add(n);
                        n.setTag(node.getKey());
                        //System.out.println(n);
                    }
                }
            }
        }
        LinkedList<node_data> l = new LinkedList<>();
        int k = dest;
        if(g.getNode(dest).getWeight() >0) {
            l.add(g.getNode(k));
            while (k != src) {
                k = g.getNode(k).getTag();
                l.addFirst(g.getNode(k));
            }
        }
        lst.add(g.getNode(dest));
        resetnodes(lst);
        return l;
    }

    @Override
    public boolean save(String file) {

        JSONObject o = new JSONObject();
        JSONArray ja2 = new JSONArray();
        try {
            o.put("Nodes",g.getV());
            for(int i = 0; i<g.nodeSize();i++)
            {
                Iterator<edge_data> it = g.getE(i).iterator();
                while (it.hasNext())
                {
                    edge_data e = it.next();
                    JSONObject ob = new JSONObject();
                    ob.put("src",e.getSrc());
                    ob.put("dest",e.getDest());
                    ob.put("w", e.getWeight());
                    ja2.put(ob);
                }
            }
            o.put("Edges", ja2);
        }
        catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            FileWriter f = new FileWriter(file);
            f.write(o.toString());
            f.flush();
            f.close();
            return true;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean load(String file) {
        directed_weighted_graph graph = new DWGraph_DS();
        String s = "";
        try {
            FileReader f = new FileReader(file);
            s = new JsonParser().parse(f).toString();
            f.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            JSONObject o = new JSONObject(s);
            JSONArray ja = o.getJSONArray("Nodes");
            JSONArray ja2 = o.getJSONArray("Edges");
            for(int i = 0; i<ja.length();i++) {
                JSONObject ob = ja.getJSONObject(i);
                graph.addNode(new NodeData(ob.getInt("key")));
            }
            for(int i = 0; i<ja2.length();i++)
            {
                JSONObject ob = ja2.getJSONObject(i);
                graph.connect(ob.getInt("src"),ob.getInt("dest"),ob.getDouble("w"));
            }
            g = graph;
            return true;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return false;
    }

    private void resetnodes(List<node_data> lst)
    {
        Iterator<node_data> it = lst.iterator();
        while (it.hasNext())
        {
            node_data n = it.next();
            n.setWeight(-1);
            n.setInfo("");
            n.setTag(-1);
        }
    }
}
