package ast;

import java.util.ArrayList;
import java.util.List;

// not an AST node, just a container of nodes to later be unrolled into a flat list of nodes
public class NodeList<T extends AstNode> {
    public T head;
    public NodeList<T> next;

    public NodeList(T head, NodeList<T> next){
        this.head = head;
        this.next = next;
    }

    public List<T> unroll(){
        List<T> l = new ArrayList<>();

        NodeList<T> current = this;

        while (current != null){
            l.add(current.head);
            current = current.next;
        }

        return l;
    }
}
