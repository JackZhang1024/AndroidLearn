package com.lucky.androidlearn.json.node;

import org.json.JSONObject;

public class ZiRuNode {

    // 当前node数据
    private NodeData nodeData;

    // 当前node的父node节点
    private ZiRuNode parentNode;

    public NodeData getNodeData() {
        return nodeData;
    }

    public void setNodeData(NodeData nodeData) {
        this.nodeData = nodeData;
    }

    public ZiRuNode getParentNode() {
        return parentNode;
    }

    public void setParentNode(ZiRuNode parentNode) {
        this.parentNode = parentNode;
    }


    // 创建ZiRuNode对象
    public ZiRuNode createZiRuNode(JSONObject jsonObject){
        NodeData nodeData = new NodeData();
        //String viewId = jsonObject.getString();
        return null;
    }



}
