# Java Project - Gas Station Simulator

- The project file is in the directory "Simulator".
- To run the project, run "./Simulator/src/test/java/Simulator".
- To change the time-consuming of every service point, change parameters in MyEngine "servicePoints[0]~[5]".
- To change the arrival rate of customers, change "arrivalProcess = new ArrivalProcess" in MyEngine.



## Dependencies

### JavaFX for MacOS 14 Sonoma
On MacOS 14 Sonoma, JavaFX 20.0.1 has a bug that causes the application window
not to be activated when the application is launched. 

- Solution: Upgrade to JavaFX 21.0.1 or later. The bug has been fixed in JavaFX 22 version, and backported to JavaFX 21.0.1、JavaFX 21.0.2
- See More：https://bugs.openjdk.org/browse/JDK-8315657

