import material.Position;

import java.util.*;


/**
 * An implementation of the NAryTree interface using left-child, right-sibling representation.
 *
 * @param <E> the type of elements stored in the tree
 */
public class LCRSTree<E> implements NAryTree<E> {

    private class LCRSnode<T> implements Position<T> {

        private T element;
        private LCRSnode parent;

        private LCRSnode bros;

        private LCRSnode kids;

        public LCRSnode(){

        }

        public LCRSnode(T element, LCRSnode parent) {
            this.element = element;
            this.parent = parent;
        }

        public LCRSnode(T element, LCRSnode parent, LCRSnode bros, LCRSnode kids) {
            this.element = element;
            this.parent = parent;
            this.bros = bros;
            this.kids = kids;
        }

        public LCRSnode getParent() {
            return parent;
        }

        public LCRSnode getBros() {
            return bros;
        }

        public LCRSnode getKids() {
            return kids;
        }

        @Override
        public T getElement() {
           return element;
        }

        public boolean isEmpty(){
            return element == null || kids == null || parent == null || bros == null;
        }

        public void setElement(T element) {
            this.element = element;
        }

        public void setParent(LCRSnode parent) {
            this.parent = parent;
        }

        public void setBros(LCRSnode bros) {
            this.bros = bros;
        }

        public void setKids(LCRSnode kids) {
            this.kids = kids;
        }
    }

    private LCRSnode<E> root;
    private int size;

    @Override
    public Position<E> addRoot(E e) {
        if (root == null && size == 0){
            root = new LCRSnode<E>(e, null);
            size = 1;
            return root;
        }else
             throw new RuntimeException("you already have a root");
    }

    @Override
    public Position<E> add(E element, Position<E> p) {
        LCRSnode<E> padre = checkPosition(p);
        LCRSnode<E> newNode = new LCRSnode<>(element, padre);
        if (padre.getKids() == null) {
            padre.setKids(newNode);
        }else{
        LCRSnode<E> aux = padre.getKids();
            while (aux.getBros() != null){
                aux = aux.getBros();
            }
            aux.setBros(newNode);
        }
        size++;
        return newNode;
    }

    private LCRSnode<E> checkPosition(Position<E> p) {
        if (!(p instanceof LCRSnode<E>)) {
            throw new RuntimeException("The position is invalid");
        }
        return (LCRSnode<E>) p;
    }

    @Override
    public Position<E> add(E element, Position<E> p, int n) {
        LCRSnode<E> padre = checkPosition(p);
        LCRSnode<E> newNode = new LCRSnode<>(element, padre);
        if (padre.getKids() == null){
            return add(element, p);
        }
        if (n == 0){
            LCRSnode<E> kid = padre.getKids();
            padre.setKids(newNode);
            newNode.setBros(kid);
        }else{
            LCRSnode<E> kidPrev = padre.getKids();
            int i = 1;
            while (kidPrev.getBros() !=  null && i <= n){
                kidPrev = kidPrev.getBros();
                i++;
            }
            newNode.setBros(kidPrev.getBros());
        }
        return newNode;
    }

    @Override
    public void swapElements(Position<E> p1, Position<E> p2) {
        E element = p1.getElement();
        replace(p1, p2.getElement());
        replace(p2, element);
    }

    @Override
    public E replace(Position<E> p, E e) {
        LCRSnode<E> original = checkPosition(p);
        E antiguo = original.getElement();
        original.setElement(e);
        return antiguo;
    }

    @Override
    public void remove(Position<E> p) {
       LCRSnode<E> node = checkPosition(p);
        if (node == this.root){
            this.root = null;
            size = 0;
        }else{
            LCRSnode<E> parent = checkPosition(node.getParent());
            if (node.getBros() == null && node.getKids() == null && parent.getKids() == node){
                parent.setKids(null);
                size--;
            }else if(isInternal(p)){
                //indicar el bro anterior, y luego anterio.setbro(anterior.getbro.getbro) y asi desvinculo el del centro




            }
        }
    }

    @Override
    public NAryTree<E> subTree(Position<E> v) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void attach(Position<E> p, NAryTree<E> t) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    @Override
    public Position<E> root() {
        return root;
    }

    @Override
    public Position<E> parent(Position<E> v) {
        return checkPosition(v).getParent();
    }

    @Override
    public Iterable<? extends Position<E>> children(Position<E> v) {
        LinkedHashSet<LCRSnode<E>> pack = new LinkedHashSet<LCRSnode<E>>();
        LCRSnode aux = checkPosition(v);
        LCRSnode hijos = aux.getKids();
        pack.add(hijos);
        while (hijos.getBros() != null){
            pack.add(hijos.getBros());
            hijos = hijos.getBros();
        }
        return pack;
    }

    @Override
    public boolean isInternal(Position<E> v) {
        LCRSnode<E> node = checkPosition(v);
        if (node.getParent() != null && node.getKids() != null){
            return true;
        }else
            return false;
    }

    @Override
    public boolean isLeaf(Position<E> v) {
        return checkPosition(v).getKids().isEmpty();
    }

    @Override
    public boolean isRoot(Position<E> v) {
        return checkPosition(v).getParent() == null;
    }

    @Override
    public Iterator<Position<E>> iterator() {

        List<Position<E>> positions = new ArrayList<>();
        breadthOrder(root, positions);
        return positions.iterator();

    }
    private void breadthOrder(LCRSnode<E> node, List<Position<E>> positions) {
        if (node != null) {
            List<LCRSnode<E>> queue = new ArrayList<>();
            queue.add(node);
            while (!queue.isEmpty()) {
                LCRSnode<E> toExplore = queue.remove(0);
                positions.add(toExplore);
                if (toExplore.getKids() != null) {
                    LCRSnode<E> hijo = toExplore.getKids();
                    queue.add(hijo);
                    while (hijo.getBros() != null) {
                        queue.add(hijo.getBros());
                        hijo = hijo.getBros();
                    }
                }
            }
        }
    }
    public int size() {
        return size;
    }

}
