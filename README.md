# The Night Of The Tentacles
This repository contains the java source code for my tech talk: Frank'n Java ... and the night of the tentacles.

Basically there are two projects:

**EvilTentaclesOfDeath** contains the tentacle framework. This includes the Tentacle interface and the classes needed for the necessary bytecode manipulation.

**SquishyTentacleFun** contains usage examples of the tentacle framework.

So, welcome to the cthulhuoid world of evil tentacles of death and join my "Getting martyred".

## Getting martyred
Be warned fearless adventurer, you will see things beyond this point which may damage your sanity seriously. So turn around and run ...

... or follow me down the rabbithole into a world of cthulhuoid beauty.

1. You will need Maven to compile the projects. So, get and install it.
2. Download the two Maven projects from this repository. Even for the examples you will need both, because the framework is not available in any public Maven repository.
3. Build **EvilTentaclesOfDeath** first using "mvn clean install". Remember, it is not available in public repositories.
4. Now build **SquishyTentacleFun** using "mvn clean install". 

That's it. Now you can start the examples from **SquishyTentacleFun**. All examples have a **main** method which takes no arguments. The following examples are available:

**de.framey.lab.evil.cthulhu.helloworld.HelloWorld** Using a sinister formular to achieve you know which goal.

**de.framey.lab.evil.squishytentaclefun.wikipedia.Pasta1**,
**de.framey.lab.evil.squishytentaclefun.wikipedia.Pasta2**,
**de.framey.lab.evil.squishytentaclefun.wikipedia.Pasta3** Implementation of the Wikipedia examples for spaghetti code.

**de.framey.lab.evil.cthulhu.travellinghenchman.TravellingHenchman** A (nearly) nondertimistic implementation of the well known problem.

If you want to have fun on your own, it's as simple as this:

1. Implement interface **de.framey.lab.evil.eviltentaclesofdeath.Tentacle**
2. Use **GOTO** in your methods to slap your sqishy tentacles whereever you like
3. Use the maven plugin, the postcompiler or the Java agent to compile or run (in case of agent) your code

* For examples of tentacle usage see the examples mentioned above
* For an usage example of the maven plugin see the pom.xml file of project SquishyTentacleFun

### Caveats

* Off course, it is NOT recommended to use this in production code. Except you're off for world domination by insane coding.
* Currently the tentacle statements work only in "normal" methods. Especially they do not work in constructors and in static methods.
* It is very likely that your code won't decompile anymore. Albeit in some cases it does in a surprisingly strange way.
* You may get strange JVM errors at runtime if you do really bad things. Like accessing variables of wrong type by skipping declarations.
* With tentacles you can do things wich are not possible in Java by other means. Some of these are:
    * Jump in and out of blocks. (e.g. right into a loop body or from one loop body to a different one)
    * Jump to dynamic targets specified by variables.
    * Skip variable declarations.
* There are some things even tentacles cannot do:
    * Jump to a target outside of a method. This is not possible due to the JVM specification.
    * Therefore you cannot jump into or out of a Lambda expression. But you can use tentacles within it.
* The the ASM version needs to match the JDK version and both projects need to be compiled with the same JDK version. The current versions match JDK 23.

### Running in an IDE

To run and debug tentaclelized code from within an IDE you're best off using the Java agent provided by **EvilTentaclesOfDeath**.

* For InteliJ goto "Run -> Edit Configurations... -> Defaults -> Application" and add to "VM Options" option "-javaagent:$MAVEN_REPOSITORY$/de/framey/lab/evil/EvilTentaclesOfDeath/0.1.0-SNAPSHOT/EvilTentaclesOfDeath-0.1.0-SNAPSHOT.jar".
* For Eclipse add entry "-javaagent:/Users/meyfarth/.m2/repository/de/framey/lab/evil/EvilTentaclesOfDeath/0.1.0-SNAPSHOT/EvilTentaclesOfDeath-0.1.0-SNAPSHOT.jar" to file "eclipse.ini"
