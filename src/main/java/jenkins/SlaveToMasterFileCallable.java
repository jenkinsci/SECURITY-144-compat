package jenkins;

import hudson.FilePath.FileCallable;
import jenkins.security.Roles;
import org.jenkinsci.remoting.RoleChecker;
import org.jenkinsci.remoting.RoleSensitive;

/**
 * {@link FileCallable}s that can be executed on the master, sent by the slave.
 * @deprecated Use a core dependency of 1.580.1 or later rather than this library.
 */
public abstract class SlaveToMasterFileCallable<T> implements FileCallable<T>, RoleSensitive {
    public void checkRoles(RoleChecker checker) throws SecurityException {
        // no check actually happens when this class definition is used, so this code will never run
        checker.check((RoleSensitive)this, Roles.MASTER);
    }
    private static final long serialVersionUID = 1L;
}
