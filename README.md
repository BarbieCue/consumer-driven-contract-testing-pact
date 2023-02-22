# Consumer Driven Contract Testing with Pact.io

The following scenario is implemented here.

![scenario](docs/overview.jpg)

The term **contract** is synonymous with **pact**.

The _frontend_ as a consumer has a contract with the _backend_ as a provider
and a contract broker (pact broker) is used as the exchange and documentation place.
The frontend requests fruits and the backend responds with a possibly empty list of fruits.
The important thing with CDC is, that the tests are focusing on the exact definitions of both,
the request and the response.

In general, the following applies to CDC: 
The contract covers only the communication between two participants, 
describing requests and responses. No functional testing is applied.
The consumer defines the exact request it will make and the exact response it wants to receive.
This forms the contract.
The provider then gets the contract and verifies that it fulfills the contract.



# Prerequisites

- Java (Version >=17)
- Docker Compose V2 to run the Pact Broker



# Get started


## Start the broker

Start the pact broker (`docker-compose.yml`) 
and then access it via **http://localhost**.

```shell
  docker compose up
  ```


## Create and publish the contract


### Create

The consumers CDC test creates the contract file
`consumer/build/pacts/Frontend-Backend.json` at runtime.
So, we only need to run the test,
easily by building the consumer project for the first time via gradle.
Building via gradle is enough, because the gradle `build` task also 
calls the gradle `test` task.

```shell
consumer/gradlew -p consumer build
```

![contract creation](docs/contract_file_creation.jpg)

If gradle skips the `build`, you can run the test explicitly.
```shell
consumer/gradlew -p consumer test
```

The Frontend CDC test does the following:

- it defines the contract
- tests the Frontend against a contract-compliant mock provider
- and if the test passed, it creates the contract file


### Publish 

The pact.io gradle plugin publishes the contract to the broker.

```shell
consumer/gradlew -p consumer pactPublish
```
![publish contract](docs/contract_publish.jpg)


## Build and verify the provider

As mentioned above, the gradle `build` task also calls the `test` task.
Verifying the provider means calling its CDC test.
Thus, building the provider will also test (verify) it.
And due to our configuration, the verification result is 
immediately published to the broker.

```shell
provider/gradlew -p provider build
```

![contract verification](docs/contract_verification.jpg)

If gradle skips the `build`, you can run the test explicitly.
```shell
provider/gradlew -p provider test
```

## Can I deploy?

[_Can I Deploy_](https://docs.pact.io/pact_broker/can_i_deploy) 
is a broker tool, to check if a service can be deployed without breaking the contract partner.
The check is based on previous verifications of two contract partners and under specification 
of corresponding version numbers of the contract partners.
In a real life project we would additionally specify a deployment or release
[environment](https://docs.pact.io/pact_broker/can_i_deploy).
But for the sake of simplicity, we only check the compatibility of service versions in general here.
Without inclusion of environments.

You have several ways to interact with the broker in order to use the tool. Here are two of them.


### [Broker CLI](https://docs.pact.io/pact_broker/client_cli)

```shell
docker run --rm --network="host" -e PACT_BROKER_BASE_URL=http://localhost \
  pactfoundation/pact-cli:latest \
  pact-broker can-i-deploy \
  --pacticipant Backend --version 0.0.1 \
  --pacticipant Frontend --version 0.0.1
```


### [Gradle Plugin](https://docs.pact.io/implementation_guides/jvm/provider/gradle)

```shell
consumer/gradlew -p consumer canideploy -P pacticipant='Frontend' -P latest=true
```

```shell
provider/gradlew -p provider canideploy -P pacticipant='Backend' -P latest=true
```

In this example, we are always using the _latest_ version of the consumer and producer.
You can select another existing version with the `-P pacticipantVersion=<version>` flag.



# A few notes about CDC and Pact


## Teams have to talk to each other

As can be seen here, CDC aims, among other things,
to ensure that the contractual partners,
i.e. the consumer team and the provider team, talk to each other.
CDC brings people to the table.


## Contracts

- Tests should have a very small scope
- Only the communication itself is of interest
- Responses may have extra fields, but requests may not ([Postel's law](https://docs.pact.io/getting_started/matching/gotchas))
- You can make use of [provider states](https://docs.pact.io/getting_started/provider_states)
- Use [helpful versioning](https://docs.pact.io/getting_started/versioning_in_the_pact_broker#best-practices)


## Pact Broker

- The Pact Broker exposes a User Interface, on http://localhost in our case
- You can also interact with the broker via [CLI](https://docs.pact.io/pact_broker/client_cli)
  i.e. to [create environments](https://docs.pact.io/pact_broker/recording_deployments_and_releases#environments) 
  or to [tag resources](https://docs.pact.io/pact_broker/tags)



# References
- [Pact](https://.pact.io)
- [Writing Consumer Tests](https://docs.pact.io/consumer)
- [Code With Engineering Playbook about CDC Testing](https://microsoft.github.io/code-with-engineering-playbook/automated-testing/cdc-testing/)
- [Microservice Testing (martinfowler.com)](https://martinfowler.com/articles/microservice-testing/#testing-contract-introduction)