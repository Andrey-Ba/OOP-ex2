package gameClient;

import Server.Game_Server_Ex2;
import api.directed_weighted_graph;
import api.edge_data;
import api.game_service;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.LinkedList;
import java.util.List;

public class Ex2 implements Runnable{


    private int level = 5;
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
        List<Integer> lst = new LinkedList<>();
        lst.add(4);
        game = Game_Server_Ex2.getServer(level);
        g = CreateFromJson.graphfromjson(game.getGraph());
        init();
        game.startGame();
        while (game.isRunning())
        {
            update();
            followpath(agents.get(0),lst);
            //game.chooseNextEdge(0,4);
        }

    }


    //Updates the frame
    private void update()
    {
        agents = Arena.getAgents(game.move(),g);
        arena.setAgents(agents);
        pokemons = Arena.json2Pokemons(game.getPokemons());
        arena.setPokemons(pokemons);
        try {
            Thread.sleep(100);
            frame.updatetime(game.timeToEnd());
            frame.repaint();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void followpath(CL_Agent agent, List<Integer> lst)
    {
        if (!lst.isEmpty() && agent.getDest()==-1){
            agent.setNextNode(lst.remove(0));
            game.chooseNextEdge(agent.getID(), agent.getNextNode());
            game.move();
        }
    }

    //Create the arena, frame and place the agents
    private void init()
    {
        arena = new Arena();
        arena.setGraph(g);
        frame = new MyFrame("Game");
        frame.update(arena);
        pokemons = Arena.json2Pokemons(game.getPokemons());
        arena.setPokemons(pokemons);
        updatepokemonsedges();
        deployagents();
        agents = Arena.getAgents(game.getAgents(),g);
        arena.setAgents(agents);
        frame.setSize(900,900);
        frame.show();
    }

    private void updatepokemonsedges() {
        for (int i = 0; i < pokemons.size(); i++)
        {
            Arena.updateEdge(pokemons.get(i),g);
        }
    }

    //Finds the most valuable pokemons and deploys an agent next to them
    private void deployagents()
    {
        setAgentnum();
        CL_Pokemon[] mvp = mvpok();
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

    private CL_Pokemon[] mvpok()
    {
        CL_Pokemon[] mvp = new CL_Pokemon[agentnum];
        for(int i = 0;i<pokemons.size() && i<agentnum;i++)
        {
            mvp[i]=pokemons.get(i);
        }

        for(int i = 0;i<pokemons.size();i++)
        {
            int j = lvp(mvp);
            if(pokemons.get(i).getValue()>mvp[j].getValue())
                mvp[j] = pokemons.get(i);
        }
        return mvp;
    }

    //Finds the least valuable pokemon in an array
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
