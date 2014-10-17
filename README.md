# SECURITY-144 Compatibility Library
This small module allows Jenkins plugins to incorporate the SECURITY-144 protection in newer versions of Jenkins core,
while still remaining installable on earlier version of Jenkins.

## Problem
The SECURITY-144 protection work in Jenkins 1.x/1.y.1 required API changes in the core as well as the `remoting` library.
Therefore, for your plugin to take advantages of the new infrastructure, you'll have to bump up the core dependency.
But this poses a problem:

* You might not want to require 1.x, so that people running 1.y.1 can install your plugin update.

* Declaring a core dependency on 1.y.1 would break anyone who accidentally installs your plugin on 1.y+1,
  which doesn't have various classes that you now depend on.

* While not recommended, you may have some important users/customers who still force you to support vulnerable
  versions (say, 1.532) for whatever reason.

## Solution
You can keep whatever core dependency you have, and instead just add this JAR as a dependency to your POM:

    <dependency>
      <groupId>org.jenkins-ci</groupId>
      <artifactId>SECURITY-144-compat</artifactId>
      <version>1.0</version>
    </dependency>

This will bring all the relevant newly added classes into your dependency. When you run on newer versions
of Jenkins (such as 1.x), these classes will ensure your code loads into JVM fine.
The `RoleSensitive.checkRoles()` method will never be invoked since Jenkins core is oblivious to such an addition.

But when you run on 1.x/1.y.1 or newer versions, the classloader visibility rules ensure that these classes
will never get loaded into JVM, and references from your plugin will instead resolve to the corresponding
classes into the core and remoting. Thus your role checking gets enforced through the core.

When you later eventually bump up the dependencies beyond 1.x, be sure to remove this library from the dependency
list, as this hack becomes unnecessary.

## Caveat
`hudson.remoting.Callable` and `hudson.FilePath.FileCallable` was retrofitted to extend from `RoleSensitive`,
but that is not the case when you run on affected versions of Jenkins.

Therefore, you'll have to perform an explicit cast whenever this is necessary. That ensures that your plugin
does not result in a bytecode verification failure when run on vulnerable versions of Jenkins, and that cast gets
optimized away when you run on newer versions of Jenkins.
