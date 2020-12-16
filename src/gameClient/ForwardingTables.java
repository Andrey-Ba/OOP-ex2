package gameClient;

import api.EdgeData;
import api.directed_weighted_graph;
import api.edge_data;
import api.node_data;

import java.util.Arrays;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.PriorityQueue;

public class ForwardingTables {

    private edge_data[] edges;
    private ForwardingTable[][] tables;
    private directed_weighted_graph g;

    public ForwardingTables(directed_weighted_graph gr)
    {
        g = gr;
        tables = new ForwardingTable[g.nodeSize()][g.edgeSize()];
        createedges();
    }

    public int Nextedge(int current_node,int edge_id)
    {
        return tables[current_node][edge_id].getNextnode();
    }

    public double Distancetoedge(int current_node,int edge_id)
    {
        return tables[current_node][edge_id].getDistance();
    }

    private void createedges()
    {
        edges = new edge_data[g.edgeSize()];
        for(int i = 0; i< g.nodeSize();i++)
        {
            Iterator<edge_data> it = g.getE(i).iterator();
            while (it.hasNext())
            {
                EdgeData e = (EdgeData)it.next();
                edges[e.getID()] = e;
            }
        }
    }

    public void calctables()
    {
        for(int i = 0;i<g.nodeSize();i++)
        {
            dijkstra(i);
            updateedges(i);
            for (int j = 0; j<g.edgeSize();j++) {
                EdgeData e = (EdgeData) edges[j];
                tables[i][j] = new ForwardingTable(j,e.gettw(),e.getTag());
            }
            reset();
        }
    }

    private void updateedges(int no)
    {
        for (int i = 0; i< g.edgeSize();i++)
        {
            EdgeData e = (EdgeData) edges[i];
            e.settw(g.getNode(e.getSrc()).getWeight()+e.getWeight());
            int src = e.getSrc();
            int dest = e.getDest();
            if(src == no) {
                e.setTag(dest);
                continue;
            }
            int d = src;
            node_data n = g.getNode(d);
            while (n.getTag()!=no)
            {
                d = n.getTag();
                n = g.getNode(d);
            }
            e.setTag(d);
        }
    }

    private void dijkstra(int src)
    {
        PriorityQueue<node_data> pq = new PriorityQueue<>();
        node_data no = g.getNode(src);
        no.setWeight(0);
        no.setTag(no.getKey());
        pq.add(no);
        while (!pq.isEmpty())
        {
            node_data node = pq.remove();
            node.setInfo("B");
            Iterator<edge_data> it = g.getE(node.getKey()).iterator();
            while (it.hasNext())
            {
                edge_data e = it.next();
                node_data n = g.getNode(e.getDest());
                if(n.getInfo().equals(""))
                {
                    n.setInfo("V");
                    n.setTag(node.getKey());
                    n.setWeight(node.getWeight()+e.getWeight());
                    pq.add(n);
                }
                else if(n.getInfo().equals("V"))
                {
                    double new_weight = node.getWeight()+e.getWeight();
                    if(n.getWeight()>new_weight)
                    {
                        n.setTag(node.getKey());
                        n.setWeight(new_weight);
                        pq.remove(n);
                        pq.add(n);
                    }
                }
            }
        }
    }

    private void reset()
    {
        for(int i = 0; i<g.nodeSize();i++)
        {
            node_data n = g.getNode(i);
            n.setTag(-1);
            n.setInfo("");
            n.setWeight(-1);
        }
        for(int i = 0; i<edges.length;i++)
        {
            EdgeData e = (EdgeData)edges[i];
            e.setTag(-1);
            e.setInfo("");
            e.settw(-1);
        }
    }

    @Override
    public String toString() {
        return "ForwardingTables{" +
                "tables=" + Arrays.toString(tables) +
                '}';
    }
}
