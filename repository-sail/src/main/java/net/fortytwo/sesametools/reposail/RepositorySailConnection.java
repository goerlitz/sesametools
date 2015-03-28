package net.fortytwo.sesametools.reposail;

import info.aduna.iteration.CloseableIteration;
import net.fortytwo.sesametools.SailConnectionTripleSource;
import org.openrdf.model.Namespace;
import org.openrdf.model.Resource;
import org.openrdf.model.Statement;
import org.openrdf.model.URI;
import org.openrdf.model.Value;
import org.openrdf.model.ValueFactory;
import org.openrdf.query.BindingSet;
import org.openrdf.query.Dataset;
import org.openrdf.query.QueryEvaluationException;
import org.openrdf.query.algebra.TupleExpr;
import org.openrdf.query.algebra.evaluation.TripleSource;
import org.openrdf.query.algebra.evaluation.federation.FederatedServiceResolverImpl;
import org.openrdf.query.algebra.evaluation.impl.EvaluationStrategyImpl;
import org.openrdf.repository.RepositoryConnection;
import org.openrdf.repository.RepositoryException;
import org.openrdf.sail.SailException;
import org.openrdf.sail.helpers.SailBase;
import org.openrdf.sail.helpers.SailConnectionBase;

/**
 * @author Joshua Shinavier (http://fortytwo.net).
 */
public class RepositorySailConnection extends SailConnectionBase {
    private RepositoryConnection repoConnection;
    private final boolean inferenceDisabled;
    private final ValueFactory valueFactory;

    public RepositorySailConnection(final SailBase sail,
                                    final RepositoryConnection repoConnection,
                                    final boolean inferenceDisabled,
                                    final ValueFactory valueFactory) {
        super(sail);
        this.repoConnection = repoConnection;
        this.inferenceDisabled = inferenceDisabled;
        this.valueFactory = valueFactory;
    }

    protected void addStatementInternal(Resource subj, URI pred, Value obj,
                             Resource... contexts) throws SailException {
        try {
            repoConnection.add(subj, pred, obj, contexts);
        } catch (RepositoryException e) {
            throw new SailException(e);
        }
    }

    protected void clearInternal(Resource... contexts) throws SailException {
        try {
            repoConnection.clear(contexts);
        } catch (RepositoryException e) {
            throw new SailException(e);
        }
    }

    protected void clearNamespacesInternal() throws SailException {
        try {
            repoConnection.clearNamespaces();
        } catch (RepositoryException e) {
            throw new SailException(e);
        }
    }

    protected void closeInternal() throws SailException {
        try {
            repoConnection.close();
        } catch (RepositoryException e) {
            throw new SailException(e);
        }
    }

    protected void commitInternal() throws SailException {
        try {
            repoConnection.commit();
        } catch (RepositoryException e) {
            throw new SailException(e);
        }
    }

    protected CloseableIteration<? extends BindingSet, QueryEvaluationException> evaluateInternal(
            TupleExpr query, Dataset dataset, BindingSet bindings, boolean includeInferred)
            throws SailException {
        try {
            TripleSource tripleSource = new SailConnectionTripleSource(this, valueFactory, includeInferred);
            EvaluationStrategyImpl strategy
                    = new EvaluationStrategyImpl(tripleSource, dataset, new FederatedServiceResolverImpl());
            return strategy.evaluate(query, bindings);
        } catch (QueryEvaluationException e) {
            throw new SailException(e);
        }
    }

    protected CloseableIteration<? extends Resource, SailException> getContextIDsInternal()
            throws SailException {
        try {
            return new RepositoryResourceIteration(repoConnection.getContextIDs());
        } catch (RepositoryException e) {
            throw new SailException(e);
        }
    }

    protected String getNamespaceInternal(String prefix) throws SailException {
        try {
            return repoConnection.getNamespace(prefix);
        } catch (RepositoryException e) {
            throw new SailException(e);
        }
    }

    protected CloseableIteration<? extends Namespace, SailException> getNamespacesInternal()
            throws SailException {
        try {
            return new RepositoryNamespaceIteration(
                    repoConnection.getNamespaces());
        } catch (RepositoryException e) {
            throw new SailException(e);
        }
    }

    protected CloseableIteration<? extends Statement, SailException> getStatementsInternal(
            Resource subj, URI pred, Value obj, boolean includeInferred, Resource... contexts)
            throws SailException {
        try {
            return new RepositoryStatementIteration(
                    repoConnection.getStatements(subj, pred, obj, includeInferred && !inferenceDisabled, contexts));
        } catch (RepositoryException e) {
            throw new SailException(e);
        }
    }

    protected void removeNamespaceInternal(String prefix) throws SailException {
        try {
            repoConnection.removeNamespace(prefix);
        } catch (RepositoryException e) {
            throw new SailException(e);
        }
    }

    protected void removeStatementsInternal(Resource subj, URI pred, Value obj,
                                 Resource... contexts) throws SailException {
        try {
            repoConnection.remove(subj, pred, obj, contexts);
        } catch (RepositoryException e) {
            throw new SailException(e);
        }
    }

    protected void rollbackInternal() throws SailException {
        try {
            repoConnection.rollback();
        } catch (RepositoryException e) {
            throw new SailException(e);
        }
    }

    protected void setNamespaceInternal(String prefix, String name) throws SailException {
        try {
            repoConnection.setNamespace(prefix, name);
        } catch (RepositoryException e) {
            throw new SailException(e);
        }
    }

    protected long sizeInternal(Resource... contexts) throws SailException {
        try {
            return repoConnection.size();
        } catch (RepositoryException e) {
            throw new SailException(e);
        }
    }

    protected void startTransactionInternal() throws SailException {
        try {
            repoConnection.begin();
        } catch (RepositoryException e) {
            throw new SailException(e);
        }
    }
}
