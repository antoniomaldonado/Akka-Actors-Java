package akka.mapreduce.example.client.actors;

import akka.actor.ActorRef;
import akka.actor.UntypedActor;

public class ClientActor extends UntypedActor {

	private ActorRef remoteServer = null;
	@SuppressWarnings("unused")
	private ActorRef fileReadActor = null;
	private long start;

	public ClientActor(ActorRef inRemoteServer) {
		remoteServer = inRemoteServer;
	}

	@Override
	public void onReceive(Object message) throws Exception {
		if (message instanceof String) {
			String msg = (String) message;
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
