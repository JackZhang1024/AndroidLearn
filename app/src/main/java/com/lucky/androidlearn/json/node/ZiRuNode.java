package com.lucky.androidlearn.json.node;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;

public class ZiRuNode {
    private static final String TAG = "ZiRuNode";
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
    public static ZiRuNode createZiRuNode(JSONObject jsonObject, ZiRuNode parentNode) {
        ZiRuNode currentZiRuNode = new ZiRuNode();
        NodeData nodeData = new NodeData();
        try {
            String viewId = jsonObject.optString("viewId");
            nodeData.setViewId(viewId);
            String tag = jsonObject.optString("tag");
            nodeData.setTag(tag);
            int childSize = jsonObject.optInt("childSize");
            nodeData.setChildSize(childSize);
            String viewType = jsonObject.optString("viewType");
            nodeData.setViewType(viewType);
            String viewName = jsonObject.optString("viewName");
            nodeData.setViewName(viewName);
            JSONObject styleJSONObj = jsonObject.getJSONObject("style");
            NodeData.Style style = createNodeDataStyle(styleJSONObj);
            nodeData.setStyle(style);
            JSONObject attributeJSONObj = jsonObject.getJSONObject("attribute");
            NodeData.Attribute attribute = createNodeDataAttribute(attributeJSONObj);
            nodeData.setAttribute(attribute);
            currentZiRuNode.setNodeData(nodeData);
            currentZiRuNode.setParentNode(parentNode);
            createNodeDataChildren(jsonObject, currentZiRuNode, childSize);
            if (parentNode != null) {
                Log.e(TAG, "createZiRuNode: nonRootNode");
                parentNode.getNodeData().addChild(currentZiRuNode);
            } else {
                parentNode = currentZiRuNode;
                Log.e(TAG, "createZiRuNode: rootNode "+currentZiRuNode.getNodeData().getViewId());
                return parentNode;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return currentZiRuNode;
    }

    private static NodeData.Style createNodeDataStyle(JSONObject jsonObject) {
        NodeData.Style style = new NodeData.Style();
        style.setBackground(jsonObject.optString(NodeData.Style.BACKGROUND));
        style.setHeight(jsonObject.optInt(NodeData.Style.HEIGHT));
        style.setWidth(jsonObject.optInt(NodeData.Style.WIDTH));
        return style;
    }

    private static NodeData.Attribute createNodeDataAttribute(JSONObject jsonObject) {
        NodeData.Attribute attribute = new NodeData.Attribute();
        attribute.setTag(jsonObject.optString(NodeData.Attribute.TAG));
        return attribute;
    }

    private static void createNodeDataChildren(JSONObject jsonObject, ZiRuNode parentNode, int childSize) {
        if (childSize > 0) {
            try {
                JSONArray childrenJSONArray = jsonObject.getJSONArray("children");
                for (int i = 0; i < childrenJSONArray.length(); i++) {
                    JSONObject childJSONObj = childrenJSONArray.getJSONObject(i);
                    createZiRuNode(childJSONObj, parentNode);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

}
