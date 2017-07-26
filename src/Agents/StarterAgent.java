package Agents;


import jade.core.Agent;

import jade.wrapper.AgentController;


public class StarterAgent extends Agent {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	static AgentController ac;
	static AgentController novoAgent ;

	protected void setup() 
	{ 
		System.out.println(getLocalName()); 

			// create agent
			try {

				ac = Starter.Starter.platform.createNewAgent("Manager", "Agents.ManagerG", null);
				ac.start();

			}
			catch (Exception ex) {
				ex.printStackTrace();
			}
	}
	protected void takeDown(){
		System.out.println("Agent "+ getAID().getLocalName() +" finishing.");
		
	}


}
