package akka.pi;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import akka.actor.UntypedActor;
import akka.actor.UntypedActorFactory;
import akka.pi.actors.Listener;
import akka.pi.actors.Master;
import akka.pi.messages.Calculate;

public class Pi {

	public static void main(String[] args) {
		Pi pi = new Pi();
		pi.calculate(10, 1000, 1000);
	}

	public void calculate(final int nrOfWorkers, final int nrOfElements, final int nrOfMessages) {

		ActorSystem system = ActorSystem.create("PiSystem");

		final ActorRef listener = system.actorOf(new Props(Listener.class), "listener");
		ActorRef master = system.actorOf(new Props(new UntypedActorFactory() {
			private static final long serialVersionUID = 1L;
			public UntypedActor create() {
				return new Master(nrOfWorkers, nrOfMessages, nrOfElements, listener);
			}
		}), "master");

		master.tell(new Calculate());

	}

}
