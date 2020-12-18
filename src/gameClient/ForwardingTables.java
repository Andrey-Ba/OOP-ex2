package gameClient;

import api.EdgeData;
import api.directed_weighted_graph;
import api.edge_data;
import api.node_data;

import java.util.Arrays;
import java.util.Iterator;
import java.util.PriorityQueue;


//This class is used for making Forwarding table for each edge.
public class ForwardingTables {

    private edge_data[] edges;
    private ForwardingTable[][] tables;
    private directed_weighted_graph g;

    public ForwardingTables(directed_weighted_graph gr)
    {
        g = gr;
        //Creates a matrix the size of nodes,edges of tables
        tables = new ForwardingTable[g.nodeSize()][g.edgeSize()];
        //Creates a matrix containing all the edges by their ID
        createedges();
    }

    //Given a node and ID of an edge returns the next step from the node the the given edge.
    public int Nextedge(int current_node,int edge_id)
    {
        return tables[current_node][edge_id].getNextnode();
    }

    //Given a node and ID of an edge returns the minimal distance from the node to travel the edge.
    public double Distancetoedge(int current_node,int edge_id)
    {
        return tables[current_node][edge_id].getDistance();
    }

    //Creates a matrix containing all the edges by their ID
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

    //A function that calculates the forwarding tables for each node using dijkstra
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
            //Reset the temporary data of the edges and nodes
            reset();
        }
    }

    //Called after dijkstra while containing all the data, updating every edge's tw and tag
    //With next node and min dist
    private void updateedges(int no)
    {
        for (int i = 0; i< g.edgeSize();i++)
        {
            EdgeData e = (EdgeData) edges[i];
            //The min distance will be the weight of the source node and the weight of the edge
            e.settw(g.getNode(e.getSrc()).getWeight()+e.getWeight());
            int src = e.getSrc();
            int dest = e.getDest();
            //If the source of the edge is the node I used dijkstra on the next node will just be the dest node.
            if(src == no) {
                e.setTag(dest);
                continue;
            }
            //Otherwise the next node will be gotten by tracing back from the
            //edge's source to the node which is after
            //the node it used dijkstra on.
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

    //Almost the same dijkstra used in graph algorithms but on a specific node with destination.
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
