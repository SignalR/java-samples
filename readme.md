# ASP.NET SignalR For Java Samples

This projects are sample applications that use the SignalR for Java library.

## Sample Chat
The signalr-sample-chat project is a console application that is prepared to hit a chat server created with this tutorial: http://www.asp.net/signalr/overview/signalr-20/getting-started-with-signalr-20/tutorial-getting-started-with-signalr-20

## Stock Ticker

The stockticker project is an a console application that shows changes in the stock market, using a server built with the following project: https://github.com/SignalR/SignalR-StockTicker


## Building the sources:

This projects require the SignalR Library for Java. To get it, download the sources and build the library from: https://github.com/SignalR/java-client.

After the library is built, copy it inside each sample "lib" directory

Import the following project into Eclipse workspace as Java projects:
	- signalr-sample-chat
	- stockticker

Build the workspace.