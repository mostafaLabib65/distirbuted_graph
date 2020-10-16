# Introduction

In this assignment we were introduced to Remote Method Invocation (RMI). We used RMI to alter the state of a graph (shared resource). In the following sections, we will discuss our implementation details, testing process, and obtained results


# Implementation Details

In this section we will state our used algorithms, implementation, optimization, concurrency, network organization, and batches creation

**Algorithms**

In our implementation, we used two shortest path algorithms: BFS and Floyd-Warshall. BFS sounds reasonable since all weights are equal. Thus, querying a node in the BFS search tree will guarantee a shortest path (optimal). Floyd-Warshall calculates the all-pairs shortest path between all the nodes in a graph.

**Implementation**

The two algorithms were used in the graph implementation. Floyd-Warshall had the edge over BFS, more in the optimization section. To implement the RMI, we created a stub method that was registered in the local registry at the server. The call was implemented by the client. The actual invocation happened over the network, more on that later. Each RMI was processed by a BatchProcessor at the server. The batch processor is just a parser to determine the operation to be done on the graph. Since the graph is a shared resource, we needed to add some sort of concurrency control, more on that later. The BatchProcessor takes the return values, according to the type of the operation, and stores them in the appropriate data structure as  indicated by the RMI stub. The client’s RMI is a blocking call; thus the client waits for the results to be returned. Every client sleeps a specific amount of time between batch calls. Batches are generated randomly using a batch generator.

**Optimization**

We did two optimizations:


1. Using lazy updates

2. Using Floyd-Warshall algorithm

Lazy updates help decrease the graph latency. Querying is easy and straightforward since there is no state change. On the other hand, updates (add and delete) involve more logic in the implementation. Since this is a shared resource, the logic will be blocking. The lazy updates will improve the performance because of the choice of concurrency control mechanism. This assumption was implicit and did not need a proof.

The other optimization, which was proved by the results, was using a different algorithm. Floyd-Warshall’s algorithm is very efficient when it comes to querying. In the BFS search tree, we would need to query from the root of the graph everytime. On the other hand, Floyd-Warshall has all pairs stored with a querying access time of O(1). The downside was the storage. Floyd-Warshall stores a bigger data structure than BFS. We note in the results that performance degrades as the graph size increases. Despite the fact, response time is still better by a huge factor from BFS.

**Concurrency**

In our implementation of concurrency, we used the readers-writers approach. Readers are unblocked while writers are blocked. This assures that no two writes will be applied at the same time. Although the readers-writers don’t insure the order between writes of different batches committed at the same time, it will ensure that the writes of the same batch are done in the same order. The batches are not looked upon as transactions.

It is a typical application of Readers-writers problem with priority to writers as in **[stallings 2001]**

**Network Organization**

We face challenges in our network implementation. Locally, everything was working quite well. To help understand, we will depend on the following figure.

As we can see, there are clients trying to connect to the server. The problem with RMI is that it is assumed to be a single request. This was wrong as the following workflow occurs. 

(1)The client hits the registry to search for the stub. 

(2) the client hits the server with the method invocation. 

This requires that the server process and local registry process, at the server, to be port forwarded. This means that the gateway, the server’s router in this case, needs to be configured with proper ports. Also the clients’ IPs need to be registered. This is because we implemented it on a home router. There will be no need for IP registering if it is a static router on the edge of the network. Also, clients need to be aware of the server’s IP, which is intuitive in order to invoke the RMI. Clients can run various processes each will be treated independently at the server since processes at the client can are identified by the client’s IP and a random port.

**Batches Creation**

To help us in stress testing the server, we created a batch generator. The batch generator takes the following parameters:



*   Maximum Batch Size
*   Minimum Batch Size
*   Add, Delete, and Query percentages in the batch (should add to 1)
*   Probability of adding a new node

With the help of these probabilities, we uniformly sample the batch. Changing these parameters, we can determine the behaviour of the graph and the response time. 


# Testing Details

In order to test our implementation, we needed to carry out two types of tests. Smoke tests, which were carried over the graph algorithms to insure that the logic was correct. Stress test to see how far we can push the response time. 

In order to test our implementation, we start N clients on M nodes with K parameters. We observe how the response time, the metric to optimize for, changes with the change in N and in the K parameters value. 

Tests were not carried locally because that would be a biased test. In any ordinary RMI, some latencies occur. These latencies include the network latency, registry lookup latency, marshalling and unmarshalling latencies, and processing latencies, since server’s specs can be different from client’s specs.
