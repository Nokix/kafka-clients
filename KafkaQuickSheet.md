# Kafka Quick Sheet

## snap installieren


```
sudo apt update
sudo apt install snapd
```



## Intelliji installieren


```
sudo snap install intellij-idea-community --classic
```

## Java 17 installieren


```
sudo apt update
sudo apt install openjdk-17-jdk
```



## Kafka installieren


```
wget "https://downloads.apache.org/kafka/3.3.1/kafka_2.13-3.3.1.tgz"
tar xfz kafka_2.13-3.3.1.tgz
rm kafka_2.13-3.3.1.tgz
mv kafka_2.13-3.3.1 kafka
```



## Kafka mit KRaft starten

[Link](https://kafka.apache.org/quickstart#quickstart_startserver)


### Single Broker:

In den Ordnern mit den Shell-skripten wechseln:


```
cd ~/kafka/bin
```

Ordner für Cluster erstellen:


```
mkdir ~/kafka/cluster0
```


Properties Datei kopieren


```
cp ~/kafka/config/kraft/server.properties ~/kafka/cluster0/server.properties
```


Datei `server.properties` ändern (Zeile 78): (Achtung: qualifizierte Referenz verwenden! Nicht ~/…)


```
log.dirs=/home/sl01/kafka/cluster0/broker1
```


Cluster-ID generieren:


```
CID="$(./kafka-storage.sh random-uuid)"
```


Metadateien anlegen:


```
./kafka-storage.sh format -t $CID -c ~/kafka/cluster0/server.properties
```


Broker starten:


```
./kafka-server-start.sh ~/kafka/cluster0/server.properties
```



## Topic anlegen


```
./kafka-topics.sh --create \
    --topic test \
    --partitions 1 \
    --replication-factor 1 \
    --bootstrap-server=localhost:9092 \
    --config min.insync.replicas=1
```


cleanup-policy festlegen:


```
--config cleanup.policy=[compact|delete]
```



## alle Topics anzeigen


```
./kafka-topics.sh --list --bootstrap-server=localhost:9092
```


Unterreplizierte Partitionen anzeigen:


```
--under-replicated-partitions
```



## einzelnes Topic anzeigen


```
./kafka-topics.sh --describe --topic test --bootstrap-server:9092
```



## Topic löschen


```
./kafka-topics.sh --delete --topic test --bootstrap-server=localhost:9092
```



## Topiceinstellungen ändern


```
./kafka-topics.sh --alter --topic test \
    --partitions 2 \
    --bootstrap-server=localhost:9092
```



## Console Producer starten


```
./kafka-console-producer.sh --topic test --bootstrap-server localhost:9092
```


Key senden:


```
--property parse.key=true
--property key.seperator=:
```


ACKs einstellen:


```
--producer-property acks=all
```



## Console Consumer starten


```
$ kafka-console-consumer.sh --topic test \
    --bootstrap-server localhost:9092
```


Topic von Anfang an lesen:


```
--from-beginning
```


Keys anzeigen:


```
--property print.key=true
```


Gruppe hinzufügen


```
--group=G1
```


Static Memebership hinzufügen


```
--consumer-property group.instance.id=myid0
```



## Producer-Performance-Test


```
$ kafka-producer-perf-test.sh --topic perftest \
	--num-records 10000 --record-size 1000 --throughput -1 \
	--producer-props bootstrap.servers=localhost9092
```


weiter producer-props (einfach anhängen):


```
batch.size=100000 linger.ms=100
acks=[0|1|-1]
enable.idempotence=[true|false]
compression.type=[none,gzip,zstd,snappy,lz4]
```



## Consumer-Performance-Test


```
./kafka-consumer-perf-test.sh --topic perf-test \
    --messages 10000 \
    --bootstrap-server localhost:9092 \
    --consumer.config ./consumer.properties
```


Einstellungen werden in der Datei `consumer.properties` gespeichert:


```
fetch.min.bytes: 10000
fetch.max.weit.ms: 500
```



## Segmente untersuchen:


```
./kafka-dump-log.sh --files ~kafka/cluster0/brocker1/test-0/0[...]000.log
```



## Kafdrop

Kafdrop von github clonen:


```
git clone https://github.com/obsidiandynamics/kafdrop.git
```


Projekt in Intellij öffnen:

Unter dem Maven Reiter: Reload Projekt

Unter dem Maven Reiter kafdrop → Lifecycle → package

In Console ausführen:


```
java --add-opens=java.base/sun.nio.ch=ALL-UNNAMED -jar ~/kafdrop/target/kafdrop-<version>.jar
```

Rufe im Browser kafdrop unter `localhost:9000` auf.
