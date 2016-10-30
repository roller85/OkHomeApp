package id.co.okhome.okhomeapp;

import org.junit.Test;

/**
 * Created by josongmin on 2016-10-07.
 */

public class Testtt {

    public class LinkedList<T>{
        Node<T> topNode = null;
        Node<T> beforeNode = null;

        public void addFirstNode(Node<T> node){
            topNode = node;
        }

        public void add(Node<T> node){
            if(topNode == null){
                addFirstNode(node);
            }else{
                //두번재부터
                beforeNode.right = node;
                node.left = beforeNode;
            }

            beforeNode = node;

            log("add " + node);
        }

        public void trace(){
            Node<T> pivotNode = topNode;
            while(pivotNode != null){
                log("순회중... " + pivotNode.data);
                pivotNode = pivotNode.right;
            }
        }
    }

    public class Node<T>{
        public Node left;
        public Node right;
        T data;

        public Node(T data) {
            this.data = data;
        }
    }

    public void log(String msg){
        System.out.println(msg);
    }

    @Test
    public void run(){
        LinkedList<String> linkedList = new LinkedList();
        linkedList.add(new Node<String>("헬로"));
        linkedList.add(new Node<String>("헬로1"));
        linkedList.add(new Node<String>("헬로2"));

        linkedList.trace();

    }

}
