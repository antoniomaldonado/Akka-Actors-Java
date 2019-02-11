package akka.mapreduce.example.client.actors;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.UntypedActor;

public class ClientActor extends UntypedActor {

	private ActorSystem system = null;
	private ActorRef remoteServer = null;
	@SuppressWarnings("unused")
	private ActorRef fileReadActor = null;
	private long start;

	public ClientActor(ActorSystem system, ActorRef inRemoteServer) {
		this.remoteServer = inRemoteServer;
		this.system = system;
	}

	@Override
	public void onReceive(Object message) throws Exception {
		if (message instanceof String) {
			String msg = (String) message;
			if ("EOF".equals(msg)) {
				// Thread sleep to wait for the server actor to end of execution before printing the result.
				// TODO We should send a message from the remote actor to the client when execution finishes on the server actor.
				Thread.sleep(5000); 
				remoteServer.tell(String.valueOf("DISPLAY_LIST"), getSelf());
				system.shutdown();
			} else 
				remoteServer.tell(msg, getSelf());
		}
	}

	@Override
	public void preStart() {
		start = System.currentTimeMillis();
	}

	@Override
	public void postStop() {
		System.out.println(String.format("Execution time: \t%s Millisecs", System.currentTimeMillis() - start));
	}
}
