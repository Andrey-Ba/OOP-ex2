package gameClient;

import api.DWGraph_DS;
import api.NodeData;
import api.directed_weighted_graph;
import api.node_data;
import gameClient.util.Point3D;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class CreateFromJson {

    public static directed_weighted_graph graphfromjson(String s)
    {
        directed_weighted_graph g = new DWGraph_DS();
        JSONObject jo;
        try {
            jo = new JSONObject(s);
            JSONArray ja = jo.getJSONArray("Nodes");
            for(int i = 0; i<ja.length();i++)
            {
                JSONObject o = ja.getJSONObject(i);
                node_data n = new NodeData(o.getInt("id"));
                String co = o.getString("pos");
                String[] coor = co.split("\\s*[,]\\s*");
                Point3D p = new Point3D(Double.parseDouble(coor[0]),Double.parseDouble(coor[1]),0.0);
                n.setLocation(p);
                g.addNode(n);
            }
            ja = jo.getJSONArray("Edges");
            for(int i =0;i<ja.length();i++)
            {
                JSONObject o = ja.getJSONObject(i);
                g.connect(o.getInt("src"),o.getInt("dest"),o.getDouble("w"));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return g;
    }

}
