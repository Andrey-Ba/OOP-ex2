package api;

import com.google.gson.Gson;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

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
        return false;
    }

    @Override
    public double shortestPathDist(int src, int dest) {
        return 0;
    }

    @Override
    public List<node_data> shortestPath(int src, int dest) {
        return null;
    }

    @Override
    public boolean save(String file) {
        JSONObject obj = new JSONObject();
        Gson gs = new Gson();
        try {
            obj.put("Graph",gs.toJson(g));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            FileWriter f = new FileWriter(file);
            f.write(obj.toString());
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
        return false;
    }
}
