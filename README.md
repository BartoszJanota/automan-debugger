# automan-debugger
Repository for AutoMan IntelliJ Debugger (AID). AID is a plugin for IntelliJ IDEA, written in Scala mostly. It is designed to make developing AutoMan tasks easier.

###Runtime errors

If something is is wrong with Scala classes compilation, check if you have Scala library attached to your project.

It is something like here on the screen: 

https://devnet.jetbrains.com/servlet/JiveServlet/download/5546566-22258/Screen%20Shot%202015-06-18%20at%2017.08.41.png

###Let's try AID

Most recent version of AID is availabel on `http-easy-build` branch. Checkout `http-easy-build` branch and try this plugin.

####Attach Scala SDK

Just righ-click the plugin module in the Project view and select Open Module Settings, choose . Click on the Green Plus Button (+) and choose Scala SDK. You should add (or download) `scala-sdk-2.11.4` library. Other essentail libraries are attached to the repository.

####Create a plugin archive

Right-click the plugin module in the Project view and select Prepare Plugin Module 'automan-debugger-scala' For Deployment in the context menu. As a result, IntelliJ IDEA will create the necessary ZIP archive. It will be stored on the root path of the project. This archive contains the plugin JAR and all necessry libraries.

###Install AID plugin

Once you have created ZIP archive, now you shuld add AID to InelliJ IDEA. Open Settings and Find Plugins tab and then click Install plugin from disk..., now find your ZIP archive and click OK. You will be promted to restart IntelliJ IDEA, just do it.

###Attach AID JAR and dependencies

Ok, open your AutoMan program project. Now you should be able to see AutoMan Debugger tab and AutoMan IntelliJ Debugger tool window. If that happens, everything is allright.

You will use `edu.umass.cs.plasma.automandebugger.automan.AutoManPlugin` class, so now, you should add AID JARs. Just righ-click the plgin module in the Project view and select Open Module Settings. Click on the Green Plus Button (+) and choose JARs or directories...

Find 'automan-debugger-scala' installed in a local directory of IntelliJ IDEA (it is something like ~/.IntelliJIdea14/config/plugins/automan-debugger-scala/lib) and add /lib directory, it contains the plugin JAR and all required dependencies.

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

If you followed all the steps, now run you program and enjoy AID.
