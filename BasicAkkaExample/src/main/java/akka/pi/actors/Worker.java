package akka.pi.actors;

import akka.actor.UntypedActor;
import akka.pi.messages.Result;
import akka.pi.messages.Work;


public class Worker extends UntypedActor {

	// The master has split the job into chunks which are sent out to each worker
	// actor to be processed. When each worker has processed its chunk it sends
	// a result back to the master which aggregates the total result.
	public void onReceive(Object message) {
		if (message instanceof Work) {
			Work work = (Work) message;
			double result = calculatePiFor(work.getStart(), work.getNrOfElements());
			getSender().tell(new Result(result), getSelf());
		} else {
			unhandled(message);
		}
	}

	private double calculatePiFor(int start, int nrOfElements) {
		double acc = 0.0;
		for (int i = start * nrOfElements; i <= ((start + 1) * nrOfElements - 1); i++) {
			acc += 4.0 * (1 - (i % 2) * 2) / (2 * i + 1);
		}
		return acc;
	}

}
