package com.lucky.androidlearn.json.node;

import java.util.List;

public class NodeData {

    // 子节点的个数
    private int childSize;
    // 该节点下的所有子节点
    private List<ZiRuNode> children;
    private Attribute attribute;
    // 该节点的样式大类 ViewGroup / View
    private String viewType;
    // 该节点的样式
    private Style style;
    // 该节点的样式 VLinearLayout/HLinearLayout
    private String viewName;
    // viewId
    private String viewId;


    public int getChildSize() {
        return childSize;
    }

    public void setChildSize(int childSize) {
        this.childSize = childSize;
    }

    public List<ZiRuNode> getChildren() {
        return children;
    }

    public void setChildren(List<ZiRuNode> children) {
        this.children = children;
    }

    public Attribute getAttribute() {
        return attribute;
    }

    public void setAttribute(Attribute attribute) {
        this.attribute = attribute;
    }

    public String getViewType() {
        return viewType;
    }

    public void setViewType(String viewType) {
        this.viewType = viewType;
    }

    public Style getStyle() {
        return style;
    }

    public void setStyle(Style style) {
        this.style = style;
    }

    public String getViewName() {
        return viewName;
    }

    public void setViewName(String viewName) {
        this.viewName = viewName;
    }

    public String getViewId() {
        return viewId;
    }

    public void setViewId(String viewId) {
        this.viewId = viewId;
    }

    // 节点属性
    public static class Attribute{
        String tag;

        public String getTag() {
            return tag;
        }

        public void setTag(String tag) {
            this.tag = tag;
        }
    }

    // 节点样式
    public static class Style{
        String background;
        String max_height;
        String position;
        String left;
        String top;
        String color;
        int width;
        int height;

        public String getBackground() {
            return background;
        }

        public void setBackground(String background) {
            this.background = background;
        }

        public String getMax_height() {
            return max_height;
        }

        public void setMax_height(String max_height) {
            this.max_height = max_height;
        }

        public String getPosition() {
            return position;
        }

        public void setPosition(String position) {
            this.position = position;
        }

        public String getLeft() {
            return left;
        }

        public void setLeft(String left) {
            this.left = left;
        }

        public String getTop() {
            return top;
        }

        public void setTop(String top) {
            this.top = top;
        }

        public String getColor() {
            return color;
        }

        public void setColor(String color) {
            this.color = color;
        }

        public int getWidth() {
            return width;
        }

        public void setWidth(int width) {
            this.width = width;
        }

        public int getHeight() {
            return height;
        }

        public void setHeight(int height) {
            this.height = height;
        }
    }




}
