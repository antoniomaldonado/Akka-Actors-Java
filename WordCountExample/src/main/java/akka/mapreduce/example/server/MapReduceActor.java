package akka.mapreduce.example.server;

import akka.actor.ActorRef;
import akka.actor.UntypedActor;

public class MapReduceActor extends UntypedActor {

	private ActorRef mapRouter;
	private ActorRef aggregateActor;

	public void onReceive(Object message) {
		if (message instanceof String) {
			if ("DISPLAY_LIST".equals((String) message))
				aggregateActor.tell(message, getSelf());
			else
				mapRouter.tell(message, getSelf());
		}
	}

	public MapReduceActor(ActorRef inAggregateActor, ActorRef inMapRouter) {
		mapRouter = inMapRouter;
		aggregateActor = inAggregateActor;
	}

}
