package akka.mapreduce.example.server;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import akka.actor.UntypedActor;

public class AggregateActor extends UntypedActor {

	private Map<String, Integer> finalReducedMap = new HashMap<String, Integer>();

	@Override
	public void onReceive(Object message) throws Exception {
		if (message instanceof Map)
			aggregateInMemoryReduce((Map<String, Integer>) message);
		else if (message instanceof String) {
			if (((String) message).compareTo("DISPLAY_LIST") == 0)
				System.out.println(finalReducedMap.toString());
			getSender().tell("SHUTDOWN", getSelf());
		}
	}

	private void aggregateInMemoryReduce(Map<String, Integer> reducedList) {
		Iterator<String> iter = reducedList.keySet().iterator();
		while (iter.hasNext()) {
			String key = iter.next();
			if (finalReducedMap.containsKey(key))
				finalReducedMap.put(key, reducedList.get(key) + finalReducedMap.get(key));
			else
				finalReducedMap.put(key, reducedList.get(key));
		}
	}

}
