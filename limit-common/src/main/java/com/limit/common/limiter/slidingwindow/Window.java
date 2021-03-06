package com.limit.common.limiter.slidingwindow;

import java.util.LinkedList;

public class Window {

    // 唯一标识
    private String name;
    // 滑动窗口
    private LinkedList<Node> slots;
    // 时间间隔
    private long intervalNanos;
    // 窗口大小
    private long windowSize;
    // 流量限制
    private long limit;

    public Window(String name, LinkedList<Node> slots, long intervalNanos, long windowSize, long limit) {
        this.name = name;
        this.slots = slots;
        this.intervalNanos = intervalNanos;
        this.windowSize = windowSize;
        this.limit = limit;
    }

    // 尝试获取
    public boolean tryAcquire(long tokens) {
        long now = System.nanoTime();
        // 删除已经过时的节点
        long earliestWindowStartTime = now - intervalNanos * windowSize;
        while (!slots.isEmpty() && slots.getFirst().getEndTime() < earliestWindowStartTime) {
            slots.removeFirst();
        }
        long count = 0;
        // 当前所有窗口的计数
        for (Node node : slots) {
            count += node.getCount();
        }
        // 如果达到计数限制，返回 false，表示获取失败
        if (count + tokens > limit) {
            return false;
        }
        // 允许获取，更新计数
        // 如果当前时间点已经有了节点，在其所属节点（最后一个节点）上累加，否则先创建一个再累加。
        Node lastNode = slots.isEmpty() ? null : slots.getLast();
        long lastEndTime = (lastNode == null) ? now : lastNode.getEndTime();
        if (now >= lastEndTime) {
            long startTime = now - (now - lastEndTime) % intervalNanos;
            long endTime = startTime + intervalNanos;
            slots.add(new Node(startTime, endTime, tokens));
        } else {
            lastNode.addCount(tokens);
        }
        return true;
    }

    // 尝试获取一个资源
    public boolean tryAcquire() {
        return tryAcquire(1L);
    }

    public LinkedList<Node> getSlots() {
        return slots;
    }

    public void setSlots(LinkedList<Node> slots) {
        this.slots = slots;
    }

    public long getIntervalNanos() {
        return intervalNanos;
    }

    public void setIntervalNanos(long intervalNanos) {
        this.intervalNanos = intervalNanos;
    }

    public long getWindowSize() {
        return windowSize;
    }

    public void setWindowSize(long windowSize) {
        this.windowSize = windowSize;
    }

    public long getLimit() {
        return limit;
    }

    public void setLimit(long limit) {
        this.limit = limit;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
