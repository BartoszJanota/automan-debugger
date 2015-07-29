# automan-debugger
Repository for AutoMan IntelliJ Debugger (*AID*). *AID* is a plugin for *IntelliJ IDEA*, written in Scala mostly. It is designed to make developing AutoMan tasks easier.

###Let's try AID

####Prerequisites

Following things you will need to build, deploy and install *AID*:
 * IntelliJ IDEA 14.* - Community or Ultimate
 * scala-sdk-2.11.4 jar library

####Clone repo

Clone this repo to your local disk, you can use SSH or HTTPS, it doesn't matter.
If you clone to your home folder, you should see a plugin folder like this:
```js
/home/bartoszjanota/automan-debugger
```
This is the root folder for `automan-debugger` project. It is a IntelliJ Platform Plugin which contains two sub-modules, both of them are autonomous SBT projects:
```js
/home/bartoszjanota/automan-debugger-server
```
and
```js
/home/bartoszjanota/automan-debugger-frontend
```
I'll describe them later.

####Open project

I assume you have you IntelliJ IDEA 14 opened. Now go to *File -> Open...* and choose */automan-debugger* folder.
IntelliJ shoudl open this project automatically as an IntelliJ Platform Plugin. You can validate this - you should see a plugin icon nearby *automan-debugger* in the project View.

####Add *Intellij Platform Plugin Project SDK*

Firstly, you need to add the mentioned SDK to the Intellij Platform - follow [official JetBrains manual](https://www.jetbrains.com/idea/help/configuring-intellij-platform-plugin-sdk.html).
Now you should add *Intellij Platform Plugin Project SDK* to the *automan-debugger* project.
Go to the *automan-debugger Module Settings -> Dependencies* and choose *Module SDK: IntelliJ IDEA Community Edition IC-141.1010.3* - the exact name of my IntelliJ IDEA.
If you add Intellij IDEA Plugin SDK properly, you should be able to import all the classes from `com.intellij.openapi.*` package.

####Choose a proper branch

Most recent version of AID is available on `ws-and-browser-poc` branch. Checkout `ws-and-browser-poc` branch

####Attach Scala SDK

Go to the *automan-debugger Module Settings -> Libraries*, click on the Green Plus Button (+) and choose *Scala SDK*. You should add (or download) `scala-sdk-2.11.4` library. Other essential libraries are attached to the repository.

####automan-debugger-server

This module is an SBT project. It can be developed independently. 
The main class of this module implements AutoMan API:
```Scala
import edu.umass.cs.automan.core.logging.TaskSnapshot
import edu.umass.cs.automan.core.{AutomanAdapter, Plugin}
...

class AutoManPlugin extends Plugin with WebSocketUtils {
  ...

  override def startup(adapter: AutomanAdapter): Unit = {
    ...
  }

  override def shutdown(): Unit = {
    ...
  }

  override def state_updates(tasks: List[TaskSnapshot[_]]): Unit = {
    ...
  }
}
```

Object of this class should be passed to your AutoMan program, more configuration details will be descrobed later. It is a WebSocket server and implements a callback method `state_updates` which emits updates of your AutoMan program to the opened WS connection, thanks to that - AID clients are able to receive most recent data.

It is an SBT project, so if you want to have all the dependencies downloaded, it should be resolved by Idea as an SBT project. Idea probably will automatically find this module as an SBT and will open a Dialog which will ask you to *Import SBT project*, if not, just open *build.sbt* file, the same Dialog must be then opened.

As it is an SBT project and if you want to use it, it should be deployed somewhere. For development purposes it can be deployed locally:
```js
$ cd automan-debugger-server
$ sbt publishLocal
```
Check your `automan-debugger-server` module version in `build.sbt` file, i.e.,
```Scala
...

name := "automan-debugger-server"

version := "1.0-SNAPSHOT"

organization := "edu.umass.cs.plasma"

scalaVersion := "2.11.4"
...
```
Then you would add a dependecy to your AutoMan program like this:
```Scala
"edu.umass.cs.plasma"  %%  "automan-debugger-server" % "1.0-SNAPSHOT"
```
Now your AutoMan program is able to use your `automan-debugger-server` classes.

####automan-debugger-frontend

This module is a ScalaJS project, as it is an SBT project too, it must be resolved by Idea, have a look at the last parapgraph to see more details.

`automan-debugger` is an IntelliJ Platform Plugin, its graphcical layer is a JavaFx WebView, so in fact it is built on the top of web development components - HTML with CSS and JS.

ScalaJS allows to develop web components using Scala only. This module contains an `index.html` which is a static template and many Scala files which are compiled to JS files then. To do it, just follow these steps:
```js
$ cd automan-debugger-frontend
$ sbt fastOptJS
```
After that, check */target/scala-2.11/* folder, you should find there all the necessary files:
* *classes/index.html*
* *automan-debugger-frontend-fastopt.js*
* *automan-debugger-frontend-jsdeps.js*
* *automan-debugger-frontend-launcher.js*

If you want to see what it looks like, just simply *Run* the *index.hmtl* file. Idea will open a browser and this web page.

These four files are very important, all of them must be copied to the */META-INF/resources/* folder in `automan-debugger` project. Only this way they can be packed with plugin and then used. Do it right now, please. You can overwrite existing files.

Now your plugin is ready to be deployed!

####Deploy *automan-debugger* plugin locally

Right-click the *automan-debugger* in the *Project view* and select *Prepare Plugin Module 'automan-debugger-scala' For Deployment* in the context menu. As a result, IntelliJ IDEA will create the necessary ZIP archive. It will be stored on the root path of the project. This archive contains the plugin JAR and all the necessary libraries:

```js
/home/bartoszjanota/automan-debugger/automan-debugger-scala.zip
```

####Install *AID* plugin in your IntelliJ IDEA

Once you have created ZIP archive, now you shuld add AID to InelliJ IDEA. Open *Settings -> Plugins* tab and then click *Install plugin from disk...*, now find your ZIP archive:

```js
/home/bartoszjanota/automan-debugger/automan-debugger-scala.zip
```

and click OK. You will be promted to restart IntelliJ, just do it please.

####Attach AID Server dependencies

Ok, open your AutoMan program project. You will use `edu.umass.cs.plasma.automandebugger.automan.AutoManPlugin` class, so now, you should add *AID* Scala dependencies.

Open `build.sbt` file and add something like that:
```Scala
libraryDependencies ++=
  Seq(
    ...
    "edu.umass.cs.plasma"  %%  "automan-debugger-server" % "1.0-SNAPSHOT"
  )
```
version number is up to you, it must be deployed somewhere. You probably deployed it locally few steps before:

After that you should be able to add AutoManPlugin to your program:

```Scala
import edu.umass.cs.plasma.automandebugger.automan.AutoManPlugin
...

val a = MTurkAdapter { mt =>
    mt.plugins = List(AutoManPlugin.plugin)
    mt.access_key_id = opts('key)
    mt.secret_access_key = opts('secret)
    mt.sandbox_mode = opts('sandbox).toBoolean
}
```

If you followed all the steps, now you can run program and enjoy AID.

### How to use AID plugin once you have it installed

If you followed all the steps above, you should have *AID* plugin installed properly. *AID* is a *Tool Window*, so you can find it in the list of your IntelliJ Tool Windows.

To initialize AID, just go to the list of your Tool Windows (point your mouse on the left bottom corner of your IntelliJ) and click on *AutoMan IntelliJ Debugger*.

Once you have clicked it, *AID* is being initialized. *AID* will show you your current AutoMan program questions and tasks if you have your AutoMan program running or will display an error if your AutoMan program is not working. So then you should run your AutoMan program and click refresh button.

IT lets you see your current AutoMan program state, it shows all the tasks per question and general overview. It refreshes its state everytime when new update is emited to the WebSocket channel.
