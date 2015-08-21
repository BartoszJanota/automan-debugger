# automan-debugger repository info

This repository contains 3 autonomous projects:

 * *automan-debugger* - the main IntelliJ Idea Plugin - Scala project
 * *automan-debugger-frontend* - visual layer - scala.js project
 * *automan-debugger-server* - AutoMan `Plugin` trait implementation - `sbt` project
 
##Project files

###*automan-debugger*

 * `automan-debugger/src/edu/umass/cs/plasma/automandebugger/idea/AutomanDebugger.scala` - main AID class, extends `ToolWindowFactory`, 
 definies Plugin layout and location, implements Plugin life-cycle and run `WebView` with `automan-debugger-frontend` HTML and JavaScript files
 * `automan-debugger/META-INF/plugin.xml` - IntelliJ Idea Plugin configuration - AID tool window is defined there
 * `automan-debugger/META-INF/resources/` - resources dir, all the files from this dir are stored at at the top of ZIP file after plugin deployment, it contains
 application configuration, HTML file, generated JavaScript files and AutoMan logo file
 * `/automan-debugger-scala.zip` - compiled and built AID plugin, ready to be installed
 
###*automan-debugger-frontend*

 * `automan-debugger/automan-debugger-frontend/src/main/resources/index.html` - HTML file - defines DOM for AID visual layer
 * `automan-debugger/automan-debugger-frontend/src/main/scala/front/AIDBrowserScripts.scala` - scala.js application, defines AID layout and behaviour, it can be compiled to JavaScrip
 * `automan-debugger/automan-debugger-frontend/src/main/scala/front/components/piechart.scala` - controls piechart rendering, scala.js implementation of Path.js Pie Chart in react.js
 * `automan-debugger/automan-debugger-frontend/src/main/scala/front/components/chartColors.scala` - controls piechart colors
 * `automan-debugger/automan-debugger-frontend/src/main/scala/front/components/stateColors.scala` - controls AutoMan Tasks states displayed on timeline
 * `automan-debugger/automan-debugger-frontend/src/main/scala/front/components/htmlDOMUtils.scala` - utilities for HTML DOM components rendering
 * `automan-debugger/automan-debugger-frontend/src/main/scala/front/models/TaskSnapshotResponse.scala` - models for WebSocket communication
 
###*automan-debugger-server*

 * `automan-debugger/automan-debugger-server/src/main/scala/edu/umass/cs/plasma/automandebugger/automan/AutoManPlugin.scala - AutoMan `Plugin` trait implementation, manages WebSocket connection
 * `automan-debugger/automan-debugger-server/src/main/scala/edu/umass/cs/plasma/automandebugger/automan/ws/WebSocketUtils.scala` - implementation of WebSocketServer client
 * `automan-debugger/automan-debugger-server/src/main/scala/edu/umass/cs/plasma/automandebugger/models/TaskSnapshotResponse.scala` - models for WebSocket communication