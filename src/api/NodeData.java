package api;

import java.util.Objects;

public class NodeData implements node_data
{
    private int key;
    private double weight;
    private String Info;
    private int Tag;
    geo_location location;
    @Override
    public int getKey() {
        return key;
    }

    public NodeData(int k)
    {
        key = k;
        weight = 0;
        Info = "";
        Tag = 0;
        location = null;
    }

    public NodeData(node_data n)
    {
        key = n.getKey();
        weight = n.getWeight();
        Info = n.getInfo();
        Tag = n.getTag();
        location = null;
    }

    @Override
    public geo_location getLocation() {
        return location;
    }

    @Override
    public void setLocation(geo_location p) {
        location = p;
    }

    @Override
    public double getWeight() {
        return weight;
    }

    @Override
    public void setWeight(double w) {
        weight = w;
    }

    @Override
    public String getInfo() {
        return  Info;
    }

    @Override
    public void setInfo(String s) {
        Info=s;
    }

    @Override
    public int getTag() {
        return Tag;
    }

    @Override
    public void setTag(int t) {
        Tag = t;
    }

    @Override
    public String toString() {
        return "NodeData{" +
                "key=" + key +
                ", weight=" + weight +
                ", Info='" + Info + '\'' +
                ", Tag=" + Tag +
                ", location=" + location +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        NodeData nodeData = (NodeData) o;
        return key == nodeData.key && Double.compare(nodeData.weight, weight) == 0;
    }

    @Override
    public int hashCode() {
        return Objects.hash(key, weight);
    }
}
