package gameClient;

import Server.Game_Server_Ex2;
import api.*;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.util.List;
import java.util.Scanner;

public class Ex2 implements Runnable{

    private long id;
    private int level = 21;
    private Arena arena;
    private MyFrame frame;
    private List<CL_Agent> agents;
    private List<CL_Pokemon> pokemons;
    private game_service game;
    private directed_weighted_graph g;
    private ForwardingTables t;
    private int agentnum;

    public Ex2(int l, long id)
    {
        level = l;
        this.id = id;
    }

    public static void main(String[] args) {
        InputStream stream = System.in;
        Scanner scanner = new Scanner(stream);
        String input = scanner.next();
        String ID = "";
        int i = 0;
        Long Id = Long.parseLong(input);
        int le = Integer.parseInt(scanner.next());
        scanner.close();
        Thread thread = new Thread(new Ex2(le,Id));
        thread.start();
    }

    @Override
    public void run() {
        game = Game_Server_Ex2.getServer(level);
        game.login(id);
        g = CreateFromJson.graphfromjson(game.getGraph());
        //Initiate everything
        init();
        game.startGame();

        int r = 0;
        while (game.isRunning())
        {
            updateframe();
            updatearena();
            for(int i = 0;i<agents.size();i++)
            {
                CL_Agent agent = agents.get(i);
                //Checks if the agent doesn't have a pokemon to chase
                if(agent.getFdest() == -1)
                    //Assigns the agent a pokemon to chase
                    agentdest(agents.get(i));
                //Moves the agent toward the pokemon it's chasing
                if(agent.getDest()==-1 && agent.getFdest() != -1)
                {
                    game.chooseNextEdge(agent.getID(),t.Nextedge(agent.getSrcNode(),agent.getFdest()));
                }
                //A delay to keep the moves low
                if((agentnum == 1 && r%20==0) || (agentnum >1 && r%27*agentnum==0))
                    game.move();
                r++;
            }
        }
        String res = game.toString();

        System.out.println(res);
        System.exit(0);
    }

    //Updates the frame
    private void updateframe()
    {
        try {
            //Another delay to keep the moves low
            Thread.sleep(1,900000);
            frame.updatetime(game.timeToEnd());
            frame.repaint();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    //Updates the agents and pokemon of the arena
    private void updatearena()
    {
        agents = Arena.getAgents(game.getAgents(),g);
        arena.setAgents(agents);
        setagentrange();
        pokemons = Arena.json2Pokemons(game.getPokemons());
        arena.setPokemons(pokemons);
        updatepokemonsedges();
    }

    //Assigns a pokemon to an agent
    private void agentdest(CL_Agent agent)
    {
        updatearena();
        int j = 0;
        CL_Pokemon pok = pokemons.get(j);

        //Check which is in range
        while (!agent.inrange(pok.get_edge().getDest()) && j < pokemons.size())
        {
            pok = pokemons.get(j);
            j++;
        }

        //If now one in range then get the closest one
        if(j==pokemons.size())
        {
            agentdestnrange(agent);
            return;
        }

        //Find the closest in range
        for(int i = 0; i<pokemons.size();i++)
        {
            CL_Pokemon p = pokemons.get(i);
            EdgeData e = (EdgeData) pok.get_edge();
            //If there one closer in the range than go after him
            if(agent.inrange(e.getSrc())) {
                double d1 = t.Distancetoedge(agent.getSrcNode(), ((EdgeData) pok.get_edge()).getID());
                double d2 = t.Distancetoedge(agent.getSrcNode(), ((EdgeData) p.get_edge()).getID());
                if (d1 > d2)
                    pok = p;
            }
        }
        edge_data e = pok.get_edge();
        pok = maybeanotherone(pok);
        agent.setFdest(((EdgeData) pok.get_edge()).getID());

    }

    //Assigns pokemon to agent with out considering the agent's range
    private void agentdestnrange(CL_Agent agent)
    {
        CL_Pokemon pok = pokemons.get(0);

        for(int i = 0; i<pokemons.size();i++)
        {
            CL_Pokemon p = pokemons.get(i);
            EdgeData e = (EdgeData) pok.get_edge();
            double d1 = t.Distancetoedge(agent.getSrcNode(), ((EdgeData) pok.get_edge()).getID());
            double d2 = t.Distancetoedge(agent.getSrcNode(), ((EdgeData) p.get_edge()).getID());
            if (d1 > d2)
                pok = p;
        }
        edge_data e = pok.get_edge();
        pok = maybeanotherone(pok);
        agent.setFdest(((EdgeData) pok.get_edge()).getID());
    }

    //Checks if the agent can get another pokemon on the next edge
    //If it can the agents will go after the next pokemon
    //Over all tries to avoid the following situation
    //A - an agent, 0 a pokemon, -A-> the route an agent will come from ,-0-> the way you can eat the pokemon
    // -A-> x <-0- y <-0- z
    //Will make the agent go that way:
    // A -> y -> x -> y -> z -> y
    //But the function make him go
    // A-> y -> z -> y -> x
    //Which is one edge less to walk on
    private CL_Pokemon maybeanotherone(CL_Pokemon pok)
    {
        //Gets the pokemons edge
        edge_data e = pok.get_edge();
        int src = e.getSrc();
        int dest = e.getDest();
        //Checks if the agents will have to go backward to catch him
        if(src > dest && pok.getType() < 0 || src < dest && pok.getType() > 0)
        {
            for (int i = 0; i < pokemons.size();i++)
            {
                CL_Pokemon p = pokemons.get(i);
                edge_data ed = p.get_edge();
                int src2 = ed.getSrc();
                int dest2 = ed.getDest();
                //Checks if the new pokemon's edge it's destination is the source of the first pokemon
                //If so by the type of the new pokemon it decides if the agent should chase after him
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

        setagentrange();

        frame.setSize(900,900);
        //Show the frame
        frame.show();
        //Create forwardingtable
        t = new ForwardingTables(g);
        t.calctables();
    }

    //Sets for the agents range that they will prefer to be in.
    //Make a fast agent will rather be in a area where there are no slow agents.
    private void setagentrange()
    {
        //If there only one agent he can go anywhere
        if(agentnum == 1) {
            agents.get(0).setRange(-1, g.nodeSize());
            return;
        }
        //What speed considered to be fast.
        int fast = 3;
        //How many areas are in the map
        int r = g.nodeSize()/agentnum;
        boolean all_fast = true, all_slow = true;

        //Check if all the agents are fast or all slow
        for (int i = 0; i<agentnum;i++)
        {
            CL_Agent agent = agents.get(i);
            if(agent.getSpeed() < fast)
                all_fast = false;
            if(agent.getSpeed() >= fast)
                all_slow = false;
        }
        //If all fast, each assigned to it's on area
        if(all_fast) {
            for (int i = 0; i < agentnum; i++) {
                CL_Agent agent = agents.get(i);
                agent.setRange(i * r, i * r + r - 1);
            }
            return;
        }
        //If all slow, no agent is limited
        if(all_slow)
        {
            for (int i = 0; i < agentnum; i++) {
                CL_Agent agent = agents.get(i);
                agent.setRange(-1, g.nodeSize());
            }
            return;
        }

        //If not all slow or fast make the fast prefer area without slow agents
        int[] agent_range = new int[agentnum];
        boolean[] emptyrange = new boolean[agentnum];

        //Firstly check which area each agent is at
        for (int i = 0; i<agentnum; i++)
        {
            CL_Agent agent = agents.get(i);
            int pos = agent.getSrcNode();
            for(int j = 1; j<= agentnum;j++)
            {
                if(pos < j*r)
                    agent_range[i] = j-1;
            }
        }

        //Check which ranges the slow agents are at
        for(int i = 0; i < agentnum; i++)
        {
            CL_Agent agent = agents.get(i);
            if(agent.getSpeed() < fast)
            {
                emptyrange[agent_range[i]] = true;
            }
        }

        for(int i = 0; i< agentnum; i++)
        {
            CL_Agent agent = agents.get(i);
            if(agent.getSpeed() < fast) {
                agent.setRange(-1, g.nodeSize());
            }
            else
            {
                int j = agent_range[i];
                while (emptyrange[j%agentnum])
                {
                    j++;
                }
                agent.setRange((j%agentnum)*r,(j%agentnum)*r+r-1);
                emptyrange[j%agentnum] = true;
            }
        }

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
        boolean[] b = new boolean[pokemons.size()];
        //Set the first pokemons
        for(int i = 0;i<pokemons.size() && i<agentnum;i++)
        {
            mvp[i] = pokemons.get(i);
            b[i] = true;
        }

        //Update the least valuable pokemon in the array to a more valuable pokemon
        for(int i = 0;i<pokemons.size();i++)
        {
            int j = lvp(mvp);
            if(!b[i] && pokemons.get(i).getValue()>mvp[j].getValue()) {
                mvp[j] = pokemons.get(i);
                b[j] = false;
                b[i] = true;
            }
        }
        return mvp;
    }

    //Finds the index of the least valuable pokemon in an array
    private int lvp(CL_Pokemon arr[])
    {
        int j = 0;
        for(int i = 1;i<pokemons.size() && i<agentnum;i++)
        {
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
