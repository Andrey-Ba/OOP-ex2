About the project
This project has two parts.
Part 1 - Making a class that implements a directed weighted graph and algorithms on it.

Algorithms including: 
- Returning a deep copy of a graph.
- Checking if a graph is connected.
- Finding the shortest distance between two given nodes.
- Finding the shortest path between two given nodes and returning it as a list.
- Given a file path, saving a graph as a json file.
- Given a file path, loading a graph from a json file.

Notes on implementation:
In the graph I used to save the reverse edges of every edge. I used it so I could remove a node without checking with out checking all the edges. And used in DFS to check if a graph is connected. 

Used data structure:
Hashmap - For it's unique ability to return a value of a given key at time complexity of O(1).

Used algorithms:
Dijkstra's Algorithm - An algorithm that finds the shortest path from 2 given nodes in a weighted graph. Was used in my project for finding the shortest path and distance between 2 nodes. First given starting node key, src and destantion key, dest. Using a min heap, a marking for each node if it was visited or got out of the min heap, marking for the distance and Adding the first node to min heap setting it's distance as 0. For the first node, going through all of it's neighbors marking them as visited setting their distance to be the weight of their edge with the first node, adding them to the min heap which places them by their distance and marking them as visited, "V" marking the first node as done "B". For any other node removed from the min heap, until the min heap is empty, marking it as done, going through it's neighbors, If the neighbor is done (marked by "B" in their Info) skips it. If the neighbor is marked as visited "V" checking if the distance from the src through the node is shorter than the neighbor's existing distance, if so it updates the distance, Updating the min heap to reset it's new position. If the neighbor is the destination node, it checks the same as a node marked by "V" but without adding it to the min heap. At the end dest's Info will contain the minimal distance from the starting node.

The running time of the algorithm is O(n+e), when n = number of node and e = number of edges. O(n+e) because if the graph is connceted it will go through the whole graph -> n operations. It goes through each node and check it's neighbours that means 2e neighbours (1 check from each side). Over all O(n+e).

DFS - An algoritm that goes over they graph and updates it's nodes with informaion which can be used to have a good idea about the graph's structure. The algorithm was used in my project to check if a given graph is connected by counting how many nodes can be reach in the graph and how many nodes can be reached in the graph if the edges were reversed. If in both it counts the same as the number of nodes in the graph we can know for sure that the graph is connected.The version of dfs used in my project: First creates a int c which will save how many nodes have been reached. Starting at the first node of the graph it marks it as "g" for grey, and goes recursively on it's first neighbor, after it returns from the recursion c will add how many nodes were reached by the first neighbor. Than going through the next none black node adding the amount of it's neighbors. Once all neighbors of the first node are black, it will be colored black returning c+1 for the amount of nodes that were reached including itself. For anyother node it will first color it as grey, then will go through it's white neighbors adding how many nodes have been discovered by each one of them. Once all grey or black, the node gets colored black and returns how many nodes have been reached by it's neighbor + 1 for itself.

The running time of the algorithm is O(n+e), when n = number of node and e = number of edges. O(n+e) because if the graph is connceted it will go through the whole graph -> n operations. It goes through each node and check it's neighbours that means 2e neighbours (1 check from each side). Over all O(n+e).


Implementation of the graph
Using 4 classes

- NodeData 
Variables:
key - A unique integer key for each node to identify it.
weight - A double for saving temporary data about the node.
Info - A String for saving temporary data about the node.
Tag - A integer for saving temporary data about the node.
location - Used in part 2 of the project for drawing the node on a graph given a geo location.

Building functions:
1. Given an integer, creates a new node with the integer as key and the rest with defualt value.
2. Given a node_data, creates a deep copy of it.

Functions:
geo_location getLocation() - Returning the current geo loaction of the node.
setLocation(geo_location p) - Given a geo location p, sets it as the geo location of the node.
double getWeight() - Returns the node's wight.
void setWeight(double w) - Given double w, sets the node's weight as w.
String getInfo() - Returns the node's Info.
void setInfo(String s) - Given a string s, sets the node's info as s.
int getTag() - Returns the node's tag.
void setTag(int t) - Given an integer t, sets the node's tag as t.
String toString() - Returns a string with the node's information.
equals(Object o) - Given another node, returns true if they have they same key.
compareTo(@NotNull node_data o) - Given another node, returns which node has bigger weight.

- EdgeData
Varibles:
int ID - Gives each node a unique ID. Used in the second part of the project.
int src - The key of the source node.
int dest - They key of the destination node.
double weight - They weight of the edge.
String info - Temporary string.
int tag - Temorary integer.
double tw - Temoorary double. Used in the second part of the project.
int id - Static variable that saves how many edges have been created.

Building functions:
1. Given a source and destination node.
2. Given a source and destination node and weight for edge.

Functions:
Get functions for src, dest, weight, Info, Tag, ID and Tw.
Set functions for Info, Tag, weight and tw.
toString.
boolean equals(Object o) - Given another edge, returns true if their src, dest and weight are the same.

- DWGraph_DS
Variables:
HashMap<Integer,node_data> V - Hashmap containing the nodes of the graph.
HashMap<Integer,HashMap<Integer,edge_data>> E - Hashmap containing a hash map for each node with it's edges.
HashMap<Integer,HashMap<Integer,edge_data>> ER - Hashmap containig a hash map for each node with it's reversed edges.
int edges - amount of edges.
int MC - amount of structure changing operations on the graph.

Building functions:
1. Default building function.
2. Deep copy building function.

Functions:
node_data getNode(int key) - Returns the node with given key.
edge_data getEdge(int src, int dest) - Returns the edge of two given nodes.
void addNode(node_data n) - Given a node, adds it to the graph.
connect(int src, int dest, double w) - Given two nodes, making an edge between them that start with src and ends at dest with a given weight.
Collection<node_data> getV() - Returns a collection with the nodes of the graph.
Collection<edge_data> getE(int node_id) - Given a key of a node, returns a collection with it's edges.
Collection<edge_data> getER(int node_id) - Given a key of a node, returns a collection with it's reversed edges.
node_data removeNode(int key) - Given a key, removes it from the graph.
edge_data removeEdge(int src, int dest) - Given two nodes removes the edge which starts at src and ends at dest.
int nodeSize() - Returns how many nodes are in the graph.
int edgeSize() - Returns how many edges are in the graph.
int getMC() - Returns MC.
boolean NodesCheck(int src, int dest) - Returns true if src and dest are keys in the graph.
boolean EdgeCheck(int src, int dest) - Returns true if a given src and dest have edge.
boolean equals(Object o) - Returns true if a given graph is equal to the graph.

- DWGraph_Algo
Variables:
directed_weighted_graph g - Graph which the algorithms is going to work on.
Building functions:
1. Default building function with an empty graph.
2. Given a weighted graph, sets g as it.

Functions:
void init(directed_weighted_graph g) - Given a graph, sets g as it.
directed_weighted_graph getGraph() - Returns g.
directed_weighted_graph copy() - Returns a deep copy of g.
boolean isConnected() - Using dfs to return if a graph is connected.
int dfs(node_data n, LinkedList<node_data> lst) - Using DFS returns how many nodes can be reached with the edges.
int Rdfs(node_data n, LinkedList<node_data> lst) - Using DFS returns how many nodes can be reached with the reversed edges.
double shortestPathDist(int src, int dest) - Using Dijkstra's Algorithm, returns the shortest distance of two nodes, given their keys.
List<node_data> shortestPath(int src, int dest) - Using Dijkstra's Algorithm, returns list containing the shortest path of two nodes, given their keys. Using a hashmap to save what is the origin of each node to create that path.
boolean save(String file) - Saves a graph to a json file.
boolean load(String file) - Loads a graph from a json file.
void resetnodes(List<node_data> lst) - Given a list of nodes, resets it's weight, info and tag.


Part 2 - Making a game using the implementation of directed weighted graph that I created.
About the game - 
Given a directed weighted graph as a map and a list of pokemons. Using agents we have to make them chase after the pokemons and collect as many as pokemon as we can before the time is running out.

Classes -
Arena - A class to save the current state of the game in order to show the game in graphic way. Also to convert agents and pokemos from json string.
CL_Agent - A class repressenting an agent in the game.
CL_Pokemon - A class repressenting a pokemon in the game.
CreateFromJson - A class the creates a directed weighted graph from a json string.
MyFrame - A class for showing the state of the game in a graphic way.
ForwardingTable - A class of forwarding table that saves for each node in the graph how far is it from a edge and which neighbor node should be walked to inorder to get to a given edge.
ForwardingTables - A class that saves for each node it's forwarding tables towards each edge in the graph as well as creates a forwarding table for each node using dijkstra's algorithm.
Ex2 - The main class which executes the game.

-Strategy of Ex2
After getting the game level and creating graph, arena and frame I check which pokemons are the most valuable, to deploy an agent next to them and than I create forwarding tables. After starting the game I set to each agent range of nodes that they will prefer to go to. If they are slow they can go anywhere but once they get faster I make them to prefer going to range without slow agents. Once all of them are fast I assign an area to each of them where they will prefer to collect pokemons.
The agents are first of all looking for pokemons in the range they are assigned to but if there are none, they will go to the nearest pokemons to them which they can track using forwarding tables. Once they know which pokemon they want to chase after they get directed by the forwarding tables to which node they should go in the next step.