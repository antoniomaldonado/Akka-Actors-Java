package akka.mapreduce.example.client;

import com.typesafe.config.ConfigFactory;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import akka.actor.UntypedActor;
import akka.actor.UntypedActorFactory;
import akka.mapreduce.example.client.actors.ClientActor;
import akka.mapreduce.example.client.actors.FileReadActor;

public class Client {

	public static void main(String[] args) throws InterruptedException {

		final ActorSystem system = ActorSystem.create("ClientApp", ConfigFactory.load().getConfig("MapReduceClientApp"));

		final ActorRef fileReadActor = system.actorOf(new Props(FileReadActor.class));
		final ActorRef remoteActor = system.actorFor("akka://MapReduceApp@localhost:2552/user/MapReduceActor");

		ActorRef actor = system.actorOf(new Props(new UntypedActorFactory() {
			private static final long serialVersionUID = 1L;

			public UntypedActor create() {
				return new ClientActor(system, remoteActor);
			}
		}));

		fileReadActor.tell("TheArtOfLoving.txt", actor);

	}

}
