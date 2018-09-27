package be.ugent.rml.store;

import be.ugent.rml.term.Term;
import org.eclipse.rdf4j.model.Namespace;

import javax.ws.rs.NotSupportedException;
import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class SimpleQuadStore extends QuadStore {

    private List<Quad> quads;

    public SimpleQuadStore(ArrayList<Quad> quads) {
        this.quads = quads;
    }

    public SimpleQuadStore() {
        quads = new ArrayList<Quad>();
    }

    public void removeDuplicates() {
        List<Quad> quadsWithDuplicates = new ArrayList<>();

        for (Quad q : quads) {
            int i = 0;

            while (i < quadsWithDuplicates.size() && !(quadsWithDuplicates.get(i).getSubject().equals(q.getSubject())
                    && quadsWithDuplicates.get(i).getObject().equals(q.getObject())
                    && quadsWithDuplicates.get(i).getPredicate().equals(q.getPredicate())
                    && !(quadsWithDuplicates.get(i).getGraph() == null && q.getGraph() != null)
                    && !(quadsWithDuplicates.get(i).getGraph() != null && q.getGraph() == null)
                    && ((quadsWithDuplicates.get(i).getGraph() == null && q.getGraph() == null) || quadsWithDuplicates.get(i).getGraph().equals(q.getGraph()))
            )) {
                i++;
            }

            if (i == quadsWithDuplicates.size()) {
                quadsWithDuplicates.add(q);
            }
        }

        quads = quadsWithDuplicates;
    }

    public void addQuad(Term subject, Term predicate, Term object, Term graph) {
        if (subject != null && predicate != null && object != null) {
            quads.add(new Quad(subject, predicate, object, graph));
        }
    }

    public List<Quad> getQuads(Term subject, Term predicate, Term object, Term graph) {
        return quads;
    }

    public List<Quad> getQuads(Term subject, Term predicate, Term object) {
        return getQuads(subject, predicate, object, null);
    }

    @Override
    public boolean isEmpty() {
        return quads.isEmpty();
    }

    @Override
    public int size() {
        return quads.size();
    }

    @Override
    public void toTurtle(Writer out) {
        throw new NotSupportedException();
    }

    @Override
    public void toJSONLD(Writer out) {
        throw new NotSupportedException();
    }

    @Override
    public void toTrix(Writer out) {
        throw new NotSupportedException();
    }

    @Override
    public void toTrig(Writer out) {
        throw new NotSupportedException();
    }

    @Override
    public void toNQuads(Writer out) throws IOException {
        for (Quad q : quads) {
            out.write(getNQuadOfQuad(q) + "\n");
        }
    }

    @Override
    public void setNamespaces(Set<Namespace> namespaces) {

    }

    private String getNQuadOfQuad(Quad q) {
        String str = q.getSubject() + " " + q.getPredicate() + " " + q.getObject();

        if (q.getGraph() != null) {
            str += " " + q.getGraph();
        }

        str += ".";

        return str;
    }
}