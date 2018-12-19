package akka.mapreduce.example.server;

import java.util.Iterator;
import java.util.List;
import java.util.NavigableMap;
import java.util.concurrent.ConcurrentSkipListMap;

import akka.actor.ActorRef;
import akka.actor.UntypedActor;

public class ReduceActor extends UntypedActor {
	private ActorRef actor = null;

	public ReduceActor(ActorRef inAggregateActor) {
		actor = inAggregateActor;
	}

	@Override
	public void onReceive(Object message) throws Exception {
		if (message instanceof List) {
			NavigableMap<String, Integer> reducedList = reduce((List<Result>) message);
			actor.tell(reducedList, getSelf());
		} else
			throw new IllegalArgumentException("Unknown message [" + message + "]");
	}

	private NavigableMap<String, Integer> reduce(List<Result> list) {
		NavigableMap<String, Integer> reducedMap = new ConcurrentSkipListMap<String, Integer>();
		Iterator<Result> iter = list.iterator();
		while (iter.hasNext()) {
			Result result = iter.next();
			reducedMap.put(result.getWord(), reducedMap.getOrDefault(result.getWord(), 0) + 1);
		}
		return reducedMap;
	}

}
