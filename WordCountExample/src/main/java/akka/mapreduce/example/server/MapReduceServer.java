package akka.mapreduce.example.server;

import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.ActorSystem.Settings;
import akka.actor.PoisonPill;
import akka.actor.Props;
import akka.actor.UntypedActor;
import akka.actor.UntypedActorFactory;
import akka.dispatch.PriorityGenerator;
import akka.dispatch.UnboundedPriorityMailbox;
import akka.routing.RoundRobinRouter;

public class MapReduceServer {

	private ActorRef mapRouter;
	private ActorRef reduceRouter;
	private ActorRef aggregateActor;
	private ActorSystem system;
	@SuppressWarnings("unused")
	private ActorRef mapReduceActor;

	public static void main(String[] args) {
		new MapReduceServer(1, 1);
	}

	@SuppressWarnings("serial")
	public MapReduceServer(int no_of_reduce_workers, int no_of_map_workers) {

		system = ActorSystem.create("MapReduceApp", ConfigFactory.load().getConfig("MapReduceApp"));

		aggregateActor = system.actorOf(new Props(AggregateActor.class));

		reduceRouter = system.actorOf(new Props(new UntypedActorFactory() {
			public UntypedActor create() {
				return new ReduceActor(aggregateActor);
			}
		}).withRouter(new RoundRobinRouter(no_of_reduce_workers)));

		// create the list of map Actors
		mapRouter = system.actorOf(new Props(new UntypedActorFactory() {
			public UntypedActor create() {
				return new MapActor(reduceRouter);
			}
		}).withRouter(new RoundRobinRouter(no_of_map_workers)));

		// create the overall MapReduce Actor that acts as the remote actor for clients
		mapReduceActor = system.actorOf(new Props(new UntypedActorFactory() {
			public UntypedActor create() {
				return new MapReduceActor(aggregateActor, mapRouter);
			}
		}).withDispatcher("priorityMailBox-dispatcher"), "MapReduceActor");

	}

	public static class MyPriorityMailBox extends UnboundedPriorityMailbox {

		public MyPriorityMailBox(Settings settings, Config config) {
			super(new PriorityGenerator() {
				public int gen(Object message) {
					if (message.equals("DISPLAY_LIST"))
						return 2;
					else if (message.equals(PoisonPill.getInstance()))
						return 3; // lowest priority
					else
						return 1; // highest priority
				}
			});
		}

	}
}
