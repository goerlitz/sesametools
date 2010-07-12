package net.fortytwo.sesametools;

import org.openrdf.rio.RDFHandler;
import org.openrdf.rio.RDFHandlerException;
import org.openrdf.model.Statement;
import org.openrdf.sail.Sail;
import org.openrdf.sail.SailConnection;
import org.openrdf.sail.SailException;

/**
 * Author: josh
 * Date: Jul 8, 2008
 * Time: 11:34:28 AM
 */
public class SailWriter implements RDFHandler {
    public enum Action {
        ADD, REMOVE
    }

    private final Sail sail;
    private final Action action;
    private SailConnection sailConnection;

    public SailWriter(final Sail sail, final Action action) {
        this.sail = sail;
        this.action = action;
    }

    public void finalize() throws Throwable {
        super.finalize();

        if (null != sailConnection) {
            sailConnection.close();
        }
    }

    public void startRDF() throws RDFHandlerException {
        try {
            sailConnection = sail.getConnection();
        } catch (SailException e) {
            throw new RDFHandlerException(e);
        }
    }

    public void endRDF() throws RDFHandlerException {
        try {
            sailConnection.commit();
            sailConnection.close();
            sailConnection = null;
        } catch (SailException e) {
            throw new RDFHandlerException(e);
        }
    }

    public void handleNamespace(String s, String s1) throws RDFHandlerException {
        try {
            switch (action) {
                case ADD:
                    sailConnection.setNamespace(s, s1);
                    break;
                case REMOVE:
                    String name = sailConnection.getNamespace(s);
                    if (null != name && name.equals(s1)) {
                        sailConnection.removeNamespace(s);
                    }
                    break;
            }
        } catch (SailException e) {
            throw new RDFHandlerException(e);
        }
    }

    public void handleStatement(Statement statement) throws RDFHandlerException {
        try {
            switch (action) {
                case ADD:
                    sailConnection.addStatement(
                            statement.getSubject(),
                            statement.getPredicate(),
                            statement.getObject(),
                            statement.getContext());
                    break;
                case REMOVE:
                    sailConnection.removeStatements(
                            statement.getSubject(),
                            statement.getPredicate(),
                            statement.getObject(),
                            statement.getContext());
                    break;
            }
        } catch (SailException e) {
            throw new RDFHandlerException(e);
        }
    }

    public void handleComment(String s) throws RDFHandlerException {
        // Do nothing.
    }
}
