package jenkins;

import hudson.FilePath.FileCallable;
import jenkins.security.Roles;
import org.jenkinsci.remoting.RoleChecker;
import org.jenkinsci.remoting.RoleSensitive;

/**
 * {@link FileCallable}s that are meant to be only used on the master.
 */
public abstract class MasterToSlaveFileCallable<T> implements FileCallable<T>, RoleSensitive {
    public void checkRoles(RoleChecker checker) throws SecurityException {
        // no check actually happens when this class definition is used, so this code will never run
        checker.check((RoleSensitive)this, Roles.SLAVE);
    }
    private static final long serialVersionUID = 1L;
}
