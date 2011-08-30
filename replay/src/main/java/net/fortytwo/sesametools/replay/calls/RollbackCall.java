
package net.fortytwo.sesametools.replay.calls;

import net.fortytwo.sesametools.replay.SailConnectionCall;
import org.openrdf.sail.SailConnection;
import org.openrdf.sail.SailException;

import java.util.StringTokenizer;

/**
 * @author Joshua Shinavier (http://fortytwo.net).
 */
public class RollbackCall extends SailConnectionCall<SailConnection, Object> {
    public RollbackCall(final String id) {
        super(id, Type.ROLLBACK);
    }

    public String toString() {
        StringBuilder sb = new StringBuilder(id).append(DELIM).append(type);

        return sb.toString();
    }

    public RollbackCall(final String id,
                        final Type type,
                        final StringTokenizer tok) {
        super(id, type);
    }

    public Object execute(final SailConnection sc) throws SailException {
        sc.rollback();
        return null;
    }
}
