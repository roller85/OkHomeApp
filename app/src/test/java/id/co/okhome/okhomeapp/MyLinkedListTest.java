package id.co.okhome.okhomeapp;

import org.junit.Test;

/**
 * Created by josongmin on 2016-10-07.
 */

public class MyLinkedListTest {

    public class MyLinkedList<T>{
        Node<T> firstNode = null;
        Node<T> lastNode = null;
        Node<T> beforeNode = null;

        public void add(T data){
            add(new Node(data));
        }

        public void add(Node<T> node){
            //노드는 각 하나의 아이템을 일컫음.
            if(firstNode == null){
                //첫번째 노드라는 뜻.
                firstNode = node;
            }else{
                //두번째부터 이쪽으로..
                beforeNode.right = node;
                node.left = beforeNode;
            }
            //노드 설정이 완료됨.
            //비포노드를 현재 들어온노드로 갱신
            beforeNode = node;
            lastNode = node;
        }


        //첫번째부터 마지막까지
        public void trace(){
            Node<T> cursor = firstNode;
            while(cursor != null){

                System.out.println("순회중입니다!! " + cursor.data);
                cursor = cursor.right;
            }
        }

        public void traceReverse(){
            Node<T> cursor = lastNode;
            while(cursor != null){

                System.out.println("역순회중입니다!! " + cursor.data);
                cursor = cursor.left;
            }
        }

        //마지막부터 첫번째
    }

    public class Node<T>{
        T data;
        Node<T> left = null; //전 노드
        Node<T> right = null; //다음에 올 노드를 설정

        public Node(T data) {
            this.data = data;
        }
    }

    @Test
    public void test(){
        //
        MyLinkedList<String> myLinkedList = new MyLinkedList<>();

        for(int i = 0; i < 100; i++){
            myLinkedList.add("활용왕" + i);
        }

        myLinkedList.traceReverse();

    }

}
