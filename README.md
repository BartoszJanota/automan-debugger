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

####Open project

I assume you have you IntelliJ IDEA 14 opened. Now go to *File -> Open...* and choose */automan-debugger* folder.
IntelliJ shoudl open this project automatically as a IntelliJ Platform Plugin. You can validate this - you should see a plugin icon nearby *automan-debugger* in the project View.

####Add *Intellij Platform Plugin Project SDK*

Firstly, you need to add the mentioned SDK to the Intellij Platform - follow [official JetBrains manual](https://www.jetbrains.com/idea/help/configuring-intellij-platform-plugin-sdk.html).
Now you should add *Intellij Platform Plugin Project SDK* to the *automan-debugger* project.
Go to the *automan-debugger Module Settings -> Dependencies* and choose *Module SDK: IntelliJ IDEA Community Edition IC-141.1010.3* - the exact name of my IntelliJ IDEA.
If you add Intellij IDEA Plugin SDK properly, you should be able to import all the classes from `com.intellij.openapi.*` package.

####Choose a proper branch

Most recent version of AID is available on `http-easy-build` branch. Checkout `http-easy-build` branch

####Attach Scala SDK

Go to the *automan-debugger Module Settings -> Libraries*, click on the Green Plus Button (+) and choose *Scala SDK*. You should add (or download) `scala-sdk-2.11.4` library. Other essential libraries are attached to the repository.

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

####Attach AID JAR and dependencies

Ok, open your AutoMan program project. You will use `edu.umass.cs.plasma.automandebugger.automan.AutoManPlugin` class, so now, you should add *AID* Scala dependencies. Go to the *automan-debugger Module Settings -> Libraries*, click on the Green Plus Button (+) and choose *JARs or directories...*

Find *automan-debugger-scala* folder installed in a local directory of your IntelliJ IDEA, on Ubuntu:

```js
~/.IntelliJIdea14/config/plugins/automan-debugger-scala/lib/
```
or on Mac:

```js
~/Library/Application Support/IdeaIC14/automan-debugger-scala/lib/
```

and add thw whole */lib/* directory, it contains the plugin JAR and all required Scala dependencies.

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

Once you have clicked it, *AID* is being initialized. *AID* will show you your current AutoMan program tasks if you have your AutoMan program running or will display an error if your AutoMan program is not working. So then you should run your AutoMan program and click refresh button.

You can lets you see your current AutoMan program state, it shows all the tasks. You can refresh its state whenever you want (in the nearest future it will be refreshing itself periodically, every 20 seconds)
