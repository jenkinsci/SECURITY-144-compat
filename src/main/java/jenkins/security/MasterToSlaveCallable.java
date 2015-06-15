package jenkins.security;

import hudson.remoting.Callable;
import org.jenkinsci.remoting.RoleChecker;
import org.jenkinsci.remoting.RoleSensitive;

/**
 * Convenient {@link Callable} meant to be run on slave.
 *
 * @author Kohsuke Kawaguchi
 * @deprecated Use a core dependency of 1.580.1 or later rather than this library.
 */
public abstract class MasterToSlaveCallable<V, T extends Throwable> implements Callable<V,T>, RoleSensitive {
    public void checkRoles(RoleChecker checker) throws SecurityException {
        // no check actually happens when this class definition is used, so this code will never run
        checker.check((RoleSensitive)this,Roles.SLAVE);
    }

    private static final long serialVersionUID = 1L;
}
