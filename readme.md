# ML Simulation in Edge Computing Environment

### Summary
The advent and proliferation of the Internet of Things calls for a paradigm shift toward edge computing, which facilitates collective intelligence of edge nodes. Among several parallel machine learning models, Stale Synchronous Parallel (SSP) model is believed to be well-suited to conduct distributed machine learning with edge nodes. This paper seeks for a versatile solution that warrants robustness in the cost of computation of edge nodes in hostile conditions, and introduces a novel scheme dynamically assigning optimal staleness level to the nodes. This is a JAVA implementation of computer simulation which reveals that the proposed approach substantially outperforms the conventional SSP model of fixed staleness level with respect to computation cost and robustness in various operational conditions. 

### Source Description
- SimpleNode.java
  - Java class for simulated node
- SimpleMLsystem.java
  - Java class for ML simulation, with implementation of simulating unstable and hostile environment
- OptimalLevel.java
  - Java class for obtaining optimal staleness level
- PriceGrapher.java
  - Draws a graph comparing the cost by different staleness levels

### Paper
"Dynamically Adjusting the Stale Synchronous Parallel Model in Edge Computing" have been submitted to [IPDPS 2018](www.ipdps.org) and awaiting acceptance. :sparkles

### Requirements
Apache Commons Math Library 3.6.1 or higher is required to compile the codes.

