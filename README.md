# Queues Management Application  
# Technical Description  
A multithreaded queue management system built in Java, designed to simulate and optimize real-world waiting lines. This application evaluates different client allocation strategies (Shortest Queue vs. Best Time) to minimize average waiting times.  
It demonstrates strong principles of Object-Oriented Design, applying Design Patterns to ensure a robust and scalable architecture.  

<div align="center">
  <video src="https://github.com/user-attachments/assets/e7ef357d-f57d-4681-b932-5cd35addc6e5
" width="800" controls></video>
</div>
  

# Architecture and logic control  
The architecture relies on an asynchronous simulation engine where every queue is independently managed by its own thread, ensuring thread-safe operations.  
The business logic is deeply decoupled from the graphical interface and a dynamic dispatching system assigns randomly generated clients (characterized by an ID, arrival time and service time) to the optimal queue based on a runtime-selected strategy.  

# Objectives  
The primary objective was to design a scalable multithreaded environment capable of:  
* **Concurrent Execution:** Managing multiple queues simultaneously using Java `Thread` instances, avoiding race conditions via atomic variables (`AtomicInteger`) and thread-safe data structures (`BlockingQueue`).
* **Design Patterns Implementation:** Using the `Strategy Pattern` to encapsulate and interchange client dispatch logic dynamically without altering the core scheduler logic.
* **Deterministic Simulation:** Generating specific edge-case scenarios (with predefined constraints for arrival and service times) and precisely logging the real-time status of each queue over a given simulation timeframe.
* **Graphical Monitoring:** Providing a Java Swing GUI for configuring simulation parameters (Number of Clients, Queues, Max Time, etc.) and visually tracking the real-time evolution of the system.

# System Logic & OOP Design
  The system's intelligence and reliability are centered around two main engineering assets:
  ## 1. Multithreading & Thread Safety
  Every `Server` (Queue) processes clients asynchronously. The system relies on `LinkedBlockingQueue<Task>` to inherently handle the complex wait/notify mechanics, ensuring that tasks are queued and dequeued safely across the main simulation loop and the individual queue threads without throwing `ConcurrentModificationException`.

  ## 2. Architectural Design Patterns (UML)
  Designed following strict OOP principles, the application architecture is visually documented and segmented into specific layers (Model, BusinessLogic, GUI).  

<img width="1831" height="994" alt="Classes_Diagram drawio" src="https://github.com/user-attachments/assets/f5eee487-fd2d-4adf-a47b-c00e9b36c43b" />  


  
  * **Strategy Pattern (`BusinessLogic`):** The `Strategy` interface defines the contract for task allocation. Implementations like `ShortestQueueStrategy` (evaluates queue size) and `TimeStrategy` (evaluates total processing time) are injected into the `Scheduler`, allowing behavior changes at runtime.
  * **Data Encapsulation (`Model`):** The `Task` and `Server` classes maintain strict access modifiers, exposing only necessary telemetry (e.g., `getWaitingPeriod()`, `getQueueSize()`) to the simulation manager.  

# Skills
This project demonstrates proficiency in:  
**→ `Java Fundamentals` and strictly typed Object-Oriented Programming.**  
**→ `Multithreading`, mastering synchronization, thread lifecycle, and thread-safe collections (`java.util.concurrent`).**  
**→ `Software Architecture`, effectively mapping system requirements to Class, Package, and Use Case UML diagrams.**  
**→ `Design Patterns`, specifically identifying where to apply the Strategy Pattern to eliminate hardcoded logic trees.**  
**→ `Desktop UI Development`, utilizing Java Swing and AWT for responsive dashboard creation.**  

# Testing  
The logic was rigorously validated against predefined test suites. The simulation logs (`simulation_log.txt`) prove accurate task dispatching, precise decrementing of service times per tick, and correct calculation of final telemetry: `Average Waiting Time`, `Average Service Time` and the `Peak Hour` (maximum concurrent clients).  

# Key Technologies  
`Java 21`, `Multithreading (Runnable, Threads)`, `Java Concurrency (BlockingQueue, AtomicInteger)`, `Java Swing / AWT`, `Design Patterns (Strategy)`, `UML Data Modeling`.

  

