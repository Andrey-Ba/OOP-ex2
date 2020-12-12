package gameClient;

import Server.Game_Server_Ex2;
import api.*;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.LinkedList;
import java.util.List;
import java.util.PriorityQueue;

public class Ex2 implements Runnable{


    private int level = 1;
    private Arena arena;
    private MyFrame frame;
    private List<CL_Agent> agents;
    private List<CL_Pokemon> pokemons;
    private game_service game;
    private directed_weighted_graph g;
    private int agentnum;


    public static void main(String[] args) {
        Thread thread = new Thread(new Ex2());
        thread.start();
    }

    @Override
    public void run() {
        int r = 0;
        game = Game_Server_Ex2.getServer(level);
        //game.login(324560317);
        g = CreateFromJson.graphfromjson(game.getGraph());
        init();
        game.startGame();
        while (game.isRunning())
        {
            update();
            makelist();
            for(int i=0;i<agents.size();i++) {
                CL_Agent agent = agents.get(i);
                //if(agent.getDest() == -1) {
                    followpath(agent, agent.GetPath());
                //}
                if(r%1==0) {
                    Arena.getAgents(game.move(), g);
                    arena.setAgents(agents);
                }
                r++;
            }
        }
        String res = game.toString();

        System.out.println(res);
        System.exit(0);
    }

    //Updates the frame
    private void update()
    {
//        agents = Arena.getAgents(game.move(),g);
//        arena.setAgents(agents);
//        pokemons = Arena.json2Pokemons(game.getPokemons());
 //       arena.setPokemons(pokemons);
        try {
            Thread.sleep(100);
            frame.updatetime(game.timeToEnd());
            frame.repaint();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void makelist()
    {
        agents = Arena.getAgents(game.getAgents(),g);
        arena.setAgents(agents);
        pokemons = Arena.json2Pokemons(game.getPokemons());
        arena.setPokemons(pokemons);
        updatepokemonsedges();

        for(int i = 0; i<agentnum;i++)
        {
            CL_Agent agent = agents.get(i);
            if(agent.getDest() == -1 ){//&& agent.newpath()) {
                PriorityQueue<CL_Pokemon> pq = createpqpok(i);
                dw_graph_algorithms ga = new DWGraph_Algo(g);
                CL_Pokemon pok = pq.peek();
                edge_data e = pok.get_edge();
                List<node_data> ls;
                int mi = Math.min(e.getSrc(),e.getDest());
                int ma = Math.max(e.getSrc(),e.getDest());
                System.out.println("MAX: " + ma + " MIN: "+mi);
                if(pok.getType()<0)
                {
                    ls = ga.shortestPath(agent.getSrcNode(),ma);
                    if(!ls.contains(g.getNode(mi)))
                        ls.add(g.getNode(mi));
                }
                else {
                    ls = ga.shortestPath(agent.getSrcNode(),mi);
                    if(!ls.contains(g.getNode(ma)))
                    ls.add(g.getNode(ma));
                }
                agent.setPath(ls);
            }

        }
    }

    private PriorityQueue<CL_Pokemon> createpqpok(int i)
    {
        PriorityQueue<CL_Pokemon> pq = new PriorityQueue<>();
        CL_Agent agent = agents.get(i);
        dw_graph_algorithms ga = new DWGraph_Algo(g);
        for(int j = 0; j<pokemons.size();j++)
        {
            CL_Pokemon pok = pokemons.get(j);
            System.out.println(pok.get_edge());
            edge_data e = pok.get_edge();
            int m;
            if(pok.getType() < 0)
                m = Math.max(e.getSrc(),e.getDest());
            else m = Math.min(e.getSrc(),e.getDest());
            pok.setMin_dist(ga.shortestPathDist(agent.getSrcNode(),m)+e.getWeight());
            System.out.println(pok.getMin_dist());
            pq.add(pok);
        }
        agent.set_curr_fruit(pq.peek());
        return pq;
    }

    //Given a list and an agent, makes the agent to walk on the path of the list
    private void followpath(CL_Agent agent, List<node_data> lst)
    {
        if(!lst.isEmpty() && agent.getSrcNode()==lst.get(0).getKey())
            lst.remove(0);
        System.out.println(lst.toString());
        if (!lst.isEmpty() && agent.getDest()==-1){
            int i = lst.remove(0).getKey();
            if(i!=agent.getDest())
                System.out.println(agent.setNextNode(i));
            game.chooseNextEdge(agent.getID(), agent.getNextNode());
        }
    }

    //Create the arena, frame and place the agents
    private void init()
    {
        //Init arena
        arena = new Arena();
        //Set arena's graph
        arena.setGraph(g);
        //Init frame
        frame = new MyFrame("Game");
        //Set frame's arena
        frame.update(arena);
        //Set the pokemons list
        pokemons = Arena.json2Pokemons(game.getPokemons());
        //Set pokemons list for the arena
        arena.setPokemons(pokemons);
        //Update the edges of the pokemons
        updatepokemonsedges();
        //Deploy agents next to the most valuable pokemons
        deployagents();
        //Sets the agents list
        agents = Arena.getAgents(game.getAgents(),g);
        //Set agents list for the arena
        arena.setAgents(agents);
        //Set the frame size
        frame.setSize(900,900);
        //Show the frame
        frame.show();
    }

    //Update all pokemon's edges
    private void updatepokemonsedges() {
        for (int i = 0; i < pokemons.size(); i++)
        {
            Arena.updateEdge(pokemons.get(i),g);
        }
    }

    //Finds the most valuable pokemons and deploys an agent next to them
    private void deployagents()
    {
        //Get how many agents are allowed
        setAgentnum();
        //Find the most valuable poekmons
        CL_Pokemon[] mvp = mvpok();
        //Deploy the agents so their first move will be a valuable pokemon
        for(int i=0;i<mvp.length;i++)
        {
            edge_data e = mvp[i].get_edge();
            int ma = Math.max(e.getSrc(),e.getDest());
            int mi = Math.min(e.getSrc(),e.getDest());
            if(mvp[i].getType()<0)
            {
                game.addAgent(ma);
            }
            else
                game.addAgent(mi);
        }
    }

    //Find most valuable pokemons
    private CL_Pokemon[] mvpok()
    {

        CL_Pokemon[] mvp = new CL_Pokemon[agentnum];
        //Set the first pokemons
        for(int i = 0;i<pokemons.size() && i<agentnum;i++)
        {
            mvp[i]=pokemons.get(i);
        }

        //Update the least valuable pokemon in the array to a more valuable pokemon
        for(int i = 0;i<pokemons.size();i++)
        {
            int j = lvp(mvp);
            if(pokemons.get(i).getValue()>mvp[j].getValue())
                mvp[j] = pokemons.get(i);
        }
        return mvp;
    }

    //Finds the index of the least valuable pokemon in an array
    private int lvp(CL_Pokemon arr[])
    {
        int j = 0;
        for(int i = 1;i<pokemons.size() && i<agentnum;i++)
        {
            j=i;
            if(arr[i].getValue()<arr[j].getValue())
                j=i;
        }
        return j;
    }

    //Get how many agents can run at once
    private void setAgentnum()
    {
        String s = game.toString();
        JSONObject o;
        try {
            o = new JSONObject(s);
            JSONObject o2 = o.getJSONObject("GameServer");
            agentnum = o2.getInt("agents");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
