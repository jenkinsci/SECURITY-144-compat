package jenkins.security;

import hudson.remoting.Callable;
import org.jenkinsci.remoting.RoleChecker;
import org.jenkinsci.remoting.RoleSensitive;

/**
 * Convenient {@link Callable} that are meant to run on the master (sent by slave/CLI/etc).
 *
 * @author Kohsuke Kawaguchi
 * @deprecated Use a core dependency of 1.580.1 or later rather than this library.
 */
@Deprecated
public abstract class SlaveToMasterCallable<V, T extends Throwable> implements Callable<V,T>, RoleSensitive {
    public void checkRoles(RoleChecker checker) throws SecurityException {
        // no check actually happens when this class definition is used, so this code will never run
        checker.check((RoleSensitive)this,Roles.MASTER);
    }

    private static final long serialVersionUID = 1L;
}
