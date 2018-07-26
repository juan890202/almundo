# almundo

##### How to execute it: Through the test cases specified in the unit test classes inside the [almundo-test package](https://github.com/juan890202/almundo/tree/master/src/test/java/com/almundo/test). Although [ControllerTest](https://github.com/juan890202/almundo/blob/master/src/test/java/com/almundo/test/ControllerTest.java) acts as Integration Test class, providing a test case that executes the entire module in parallel mode for testing concurrency.
* If there are no available employees, the current dispatcher object which is in charge of assigning the current call object to an employee will be enqueued and put on hold.
* Once an employee is available again, a dispatcher object, which was previously put on hold and is also holding the pending call object, will be dequeued and notified immediately so that it can proceed to reach the available employee and assign it to the pending call object.

## RELEASE 1.0

### Description
In this release the project is implemented with the Java SE platform only. 
#### The inversion of control is implemented through:
* The strategy pattern provided by the family of classes created implementing the Abstract Factory pattern inside the 
[com.almundo.test.employee](https://github.com/juan890202/almundo/tree/master/src/main/java/com/almundo/test/employee)
module.
* The flexibility on how the chain of responsibility will be composed from the client side (look the
[Controller](https://github.com/juan890202/almundo/blob/master/src/main/java/com/almundo/test/Controller.java) class constructor).
#### In order to make a concurrent call center module capable of attending multiple calls in parallel:
* The Producer-Consumer pattern has been implemented to put on hold the disposed dispatchers and release them when there is employee availability again. Inside [DispatcherImpl](https://github.com/juan890202/almundo/blob/master/src/main/java/com/almundo/test/service/DispatcherImpl.java) we will find the Consumer logic and inside [EmployeesAvailability](https://github.com/juan890202/almundo/blob/master/src/main/java/com/almundo/test/event/EmployeesAvailability.java) we will find the Producer logic.

### Built with
#### Programming languages
* [Java 8](http://www.oracle.com/technetwork/java/javase/downloads/jdk8-downloads-2133151.html)
* [Vavr (Javaslang)](http://www.vavr.io/) - Library for implementing real functional programming in java
#### Unit testing
* [JUnit 5](https://junit.org/junit5/)
* [Hamcrest](hamcrest.org)
* [Mockito](http://site.mockito.org/)
* [PowerMock](http://powermock.github.io/)
#### Dependency management
* [Maven](https://maven.apache.org/)