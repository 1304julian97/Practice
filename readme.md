# Practice

This project was developed with the goal of having a good reference for using a set of tecnlogies/libraries as base code.


This project is made in 3 modules (so far)

## core: 

`````Located in ./src/...
This module contains the next list of tecnologies:
* Kafka Producer.
* Akka for http server
* DDD for arquictecure.
* Task of Monix to handle effects.
`````
References:

* S4n Campus
* [Dzone Article][Dzone]
* [Notion Personal Article][NotionAticle]
* [DDD, Hexagonal, Onion, Clean, CQRS, … How I put it all together][DDD, Hexagonal, Onion, Clean, CQRS, … How I put it all together]
* For Task and Akka implemention I used this [GitHub Project][GitHub Project] 


## Kafka-consumer:

`````Located in ./PracticeKafka/...
This project contains the next list of tecnologies:
* Kafka Consumer.
* Akka for http server
* DDD for arquictecure.
* Task of Monix to handle effects.
`````

This module contains the next list of tecnologies:
* Kafka Consumer.
* Akka for http server
* DDD for arquictecure.
* Task of Monix to handle effects.
* H2 for database

References:
* For Task, Akka and H2 implemention I used this [GitHub Project][GitHub Project]
* The rest of the references are the same as before.


## cache:

`````Located in ./cacheV2/...
This project contains the next list of tecnologies:
* http4s for http server and client
* DDD for arquictecure.
* Cats-effect to handle effects.
* Scaffeine for caching
`````

References:
* Java [caching][caching] using caffeine
* [Quick start][QuickStarthttp4s] documentation for http4s
* This [GitHub][http4sCampusRepository] repository for http4s
* Scala exercises for [cats-effect][scala-exercises]
* The rest of the references are the same as before.

# Some considerations:
```
1. `core` and `PracticeKafka` modules **_depend on_** the same sbt file
2. `cache` module only **_depends on_** its own sbt file.
```

[Dzone]: https://dzone.com/articles/hands-on-apache-kafka-with-scala#:~:text=Kafka%20Producer%20is%20the%20client,published%20messages%20in%20real%2Dtime

[NotionAticle]: https://www.notion.so/Study-Time-0d896b98388b4ac3bd70f2b8caabf401

[DDD, Hexagonal, Onion, Clean, CQRS, … How I put it all together]: https://herbertograca.com/2017/11/16/explicit-architecture-01-ddd-hexagonal-onion-clean-cqrs-how-i-put-it-all-together/

[GitHub Project]: https://github.com/1304julian97/bankaccount

[caching]: https://www.baeldung.com/java-caching-caffeine

[http4sCampusRepository]: https://github.com/s4n-campus/fpwithscala-users/blob/main/src/main/scala/co/s4ncampus/fpwithscala/users/domain/UserService.scala

[scala-exercises]: https://www.scala-exercises.org/cats/

[QuickStarthttp4s]: https://http4s.org/v0.20/