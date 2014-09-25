# SECURITY-144 Compatibility Library

This small module allows Jenkins plugins to incorporate the SECURITY-144 protection in newer versions of Jenkins core,
while still remain installable on earlier version of Jenkins.


## Problem
SECURITY-144 protection work in Jenkins 1.x/1.y.1 required API changes in the core as well as remoting.
Therefore, for your plugin to take advantages of the new infrastructure, you'll have to bump up the core dependency.
But this pauses a problem:

* You might not want to require 1.x so that people running 1.y.1 can install your newer version of plugins

* Declaring core dependency on 1.y.1 would anyone who accidentally install your plugin on 1.y+1, which
  doesn't have various classes that you now depend on.

* While not recommended, you have some important user/customer who still force you to support affected
  versions (say 1.532) for whatever reasons. Perhaps

## Solution
You can keep whatever core dependency you have, and instead just add this jar as a dependency to your POM:

    <dependency>
      <groupId>org.jenkins-ci</groupId>
      <artifactId>security144-compatibility</artifactId>
      <version>1.0</version>
    </dependency>

This will bring all the relevant newly added classes into your dependency. When you run on newer versions
of Jenkins (such as 1.x), these classes will ensure your code loads into JVM fine. `RoleSensitive.checkRoles()` method
will never be invoked since Jenkins core is oblivious to such an addition.

But when you run on 1.x/1.y.1 or newer versions, the classloader visibility rules ensure that these classes
will never get loaded into JVM, and references from your plugin will instead resolve to the corresponding
classes into the core and remoting. Thus your role checking gets enforced through the core.

When you later eventually bump up the dependencies beyond 1.x, be sure to remove this library from the dependency
list, as this hack becomes unnecessary.

## Caveat
`hudson.remoting.Callable` and `hudson.FilePath.FileCallable` was retrofitted to extend from `RoleSensitive`,
but that is not the case when you run on affected versions of Jenkins.

Therefore, you'll have to perform an explicit case whenever this is necessary. That ensures that your plugin
does not result in bytecode verification failure when run on affected versions of Jenkins, and that cast gets
optimized away when you run on newer versions of Jenkins.