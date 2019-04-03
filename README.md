# akka actors with java

BasicAkkaExample: Basic example to learn the basics about akka actors
- To compile the project: mvn clean compile
- To run the project: mvn exec:java -Dexec.mainClass="akka.pi.Pi"
- More info: https://doc.akka.io/docs/akka/2.0.1/intro/getting-started-first-java.html

WordCountExample: Word count example using remote actors
- To compile the project: mvn clean compile
- Run the server in one terminal: mvn exec:java -Dexec.mainClass="akka.mapreduce.example.server.MapReduceServer"
- Run the client in another terminal: mvn exec:java -Dexec.mainClass="akka.mapreduce.example.client.Client"
