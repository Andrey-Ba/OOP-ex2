package gameClient;

import Server.Game_Server_Ex2;
import api.*;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

public class Ex2 implements Runnable{


    private int level = 23;
    private Arena arena;
    private MyFrame frame;
    private List<CL_Agent> agents;
    private List<CL_Pokemon> pokemons;
    private game_service game;
    private directed_weighted_graph g;
    private ForwardingTables t;
    private int agentnum;


    public static void main(String[] args) {
        Thread thread = new Thread(new Ex2());
        thread.start();
    }

    @Override
    public void run() {
        game = Game_Server_Ex2.getServer(level);
        //game.login(324560317);
        g = CreateFromJson.graphfromjson(game.getGraph());
        init();
        game.startGame();
        while (game.isRunning())
        {
            updateframe();
            updatearena();
            for(int i = 0;i<agents.size();i++)
            {
                CL_Agent agent = agents.get(i);
                if(agent.getFdest() == -1)
                {
                    agentdest(agents.get(i));
                    game.move();
                }
                if(agent.getDest()==-1)
                {
                    game.chooseNextEdge(agent.getID(),t.Nextedge(agent.getSrcNode(),agent.getFdest()));
                }
            }
        }
        String res = game.toString();

        System.out.println(res);
        System.exit(0);
    }

    //Updates the frame
    private void updateframe()
    {
//        frame.updatetime(game.timeToEnd());
//        frame.repaint();
        try {
            Thread.sleep(100);
            frame.updatetime(game.timeToEnd());
            frame.repaint();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void updatearena()
    {
        agents = Arena.getAgents(game.getAgents(),g);
        arena.setAgents(agents);
        pokemons = Arena.json2Pokemons(game.getPokemons());
        arena.setPokemons(pokemons);
        updatepokemonsedges();
    }

    private void agentdest(CL_Agent agent)
    {
        updatearena();
        CL_Pokemon pok = pokemons.get(0);
        for(int i = 0; i<pokemons.size();i++)
        {
            CL_Pokemon p = pokemons.get(i);
            EdgeData e = (EdgeData) pok.get_edge();
            double d1 = t.Distancetoedge(agent.getSrcNode(),((EdgeData) pok.get_edge()).getID());
            double d2 = t.Distancetoedge(agent.getSrcNode(),((EdgeData) p.get_edge()).getID());
            if(d1>d2)
                pok = p;
        }
        edge_data e = pok.get_edge();
        pok = maybeanotherone(pok);
        agent.setFdest(((EdgeData) pok.get_edge()).getID());
    }

    private CL_Pokemon maybeanotherone(CL_Pokemon pok)
    {
        edge_data e = pok.get_edge();
        int src = e.getSrc();
        int dest = e.getDest();
        boolean b = src > dest && pok.getType() < 0 || src < dest && pok.getType() > 0;
        if(b)
        {
            for (int i = 0; i < pokemons.size();i++)
            {
                CL_Pokemon p = pokemons.get(i);
                edge_data ed = p.get_edge();
                int src2 = ed.getSrc();
                int dest2 = ed.getDest();
                if(dest2 == src && (src2 > dest2 && p.getType() < 0 || dest2 > src2 && p.getType()>0))
                    pok = p;
            }
        }
        return pok;
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
        //Create forwardingtable
        t = new ForwardingTables(g);
        t.calctables();
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
