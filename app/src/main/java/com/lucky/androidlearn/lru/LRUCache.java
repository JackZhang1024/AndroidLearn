package com.lucky.androidlearn.lru;

import java.util.HashMap;

public class LRUCache {

    private Node head;
    private Node end;

    //缓存存储上限
    private int limit;

    private HashMap<String, Node> hashMap;


    public LRUCache(int limit) {
        this.limit = limit;
        hashMap = new HashMap<>();
    }



    // 尾部插入节点
    private void addNode(Node node) {
        if (end != null) {
            end.next = node;
            end.pre = end;
            node.next = null;
        }

        end = node;
        if (head == null) {
            // 有可能是第一个节点
            head = node;
        }
    }

    // 删除节点
    // node 要删除的节点
    private String removeNode(Node node) {
        if (node == end) {
            end = end.pre;
        } else if (node == head) {
            head = head.next;
        } else {
            // 移除中间节点
            node.pre.next = node.next;
            node.next.pre = node.pre;
        }
        return node.key;
    }

    // 刷新被访问的节点
    // node 被访问的节点
    // 将节点移除之后 添加到末尾位置
    private void refreshNode(Node node) {
        //如果访问的是尾节点，无需移动节点
        if (node == end) {
            return;
        }
        // 移除节点
        removeNode(node);
        addNode(node);
    }


    // remove
    public void remove(String key) {
        Node node = hashMap.get(key);
        removeNode(node);
        hashMap.remove(key);
    }


    // put
    public void put(String key, String value) {
        Node node = hashMap.get(key);
        if (node == null) {
            // 如果key 不存在 插入key-value
            if (hashMap.size() >= limit) {
                String oldKey = removeNode(head);
                hashMap.remove(oldKey);
            }
            node = new Node(key, value);
            addNode(node);
            hashMap.put(key, node);
        } else {
            node.value = value;
            refreshNode(node);
        }
    }

    // get
    public String get(String key) {
        Node node = hashMap.get(key);
        if (node == null) {
            return null;
        }
        refreshNode(node);
        return node.value;
    }


}
