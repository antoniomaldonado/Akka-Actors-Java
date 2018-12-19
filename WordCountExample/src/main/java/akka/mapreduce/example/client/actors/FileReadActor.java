package akka.mapreduce.example.client.actors;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import akka.actor.UntypedActor;

public class FileReadActor extends UntypedActor {

	@Override
	public void onReceive(Object message) throws Exception {
		if (message instanceof String) {
			String fileName = (String) message;
			try {
				BufferedReader reader = getReader(fileName);
				String line = null;
				int count = 0;
				while ((line = reader.readLine()) != null) {
					getSender().tell(line.replaceAll("[^A-Za-z ]", ""), getSelf());
				}
				getSender().tell(String.valueOf("EOF"), getSelf());
			} catch (IOException x) {
				System.err.format("IOException: %s%n", x);
			}
		} else
			throw new IllegalArgumentException("Unknown message [" + message + "]");
	}

	private BufferedReader getReader(String fileName) throws IOException {
		return new BufferedReader(new InputStreamReader(
			Thread.currentThread().getContextClassLoader().getResource(fileName).openStream()));
	}

}