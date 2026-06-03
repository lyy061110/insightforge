package com.insightforge.engine;

import com.insightforge.entity.Post;
import com.insightforge.entity.Report;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.PriorityQueue;

/**
 * 优先队列引擎
 * 使用优先队列数据结构管理热门帖子和举报审核队列
 * 按权重和严重程度进行优先级排序
 */
@Component
public class PriorityQueueEngine {

    /**
     * 热门帖子队列
     * 按帖子权重降序排列，权重高的帖子排在前面
     */
    private final PriorityQueue<Post> trendingQueue = new PriorityQueue<>(
            (a, b) -> Double.compare(b.getWeight() != null ? b.getWeight() : 0.0,
                    a.getWeight() != null ? a.getWeight() : 0.0));

    /**
     * 举报审核队列
     * 按举报严重程度降序排列，严重程度高的举报优先处理
     */
    private final PriorityQueue<Report> reviewQueue = new PriorityQueue<>(
            (a, b) -> Integer.compare(b.getSeverity() != null ? b.getSeverity() : 0,
                    a.getSeverity() != null ? a.getSeverity() : 0));

    /**
     * 将帖子添加到热门队列
     *
     * @param post 要添加的帖子对象
     */
    public void addToTrending(Post post) {
        // 空值检查，确保只添加有效帖子
        if (post != null) {
            trendingQueue.offer(post);
        }
    }

    /**
     * 从热门队列中移除帖子
     *
     * @param post 要移除的帖子对象
     */
    public void removeFromTrending(Post post) {
        if (post != null) {
            trendingQueue.remove(post);
        }
    }

    /**
     * 获取前N个热门帖子
     * 返回权重最高的N个帖子列表
     *
     * @param n 要获取的帖子数量
     * @return 按权重降序排列的帖子列表
     */
    public List<Post> getTopTrending(int n) {
        List<Post> result = new ArrayList<>();
        // 参数校验，n小于等于0时返回空列表
        if (n <= 0) {
            return result;
        }
        // 将优先队列转换为列表并重新排序，确保正确顺序
        List<Post> sorted = new ArrayList<>(trendingQueue);
        sorted.sort((a, b) -> Double.compare(
                b.getWeight() != null ? b.getWeight() : 0.0,
                a.getWeight() != null ? a.getWeight() : 0.0));
        // 取前n个帖子，避免数组越界
        int limit = Math.min(n, sorted.size());
        for (int i = 0; i < limit; i++) {
            result.add(sorted.get(i));
        }
        return result;
    }

    /**
     * 刷新热门队列
     * 清空当前队列并重新添加帖子列表
     *
     * @param posts 新的帖子列表
     */
    public void refreshTrending(List<Post> posts) {
        // 清空现有队列
        trendingQueue.clear();
        // 添加新帖子列表
        if (posts != null) {
            trendingQueue.addAll(posts);
        }
    }

    /**
     * 获取热门队列的大小
     *
     * @return 队列中帖子的数量
     */
    public int getTrendingSize() {
        return trendingQueue.size();
    }

    /**
     * 将举报添加到审核队列
     *
     * @param report 要添加的举报对象
     */
    public void addToReviewQueue(Report report) {
        if (report != null) {
            reviewQueue.offer(report);
        }
    }

    /**
     * 获取下一个待审核的举报
     * 从队列中取出并移除优先级最高的举报
     *
     * @return 优先级最高的举报对象，队列为空时返回null
     */
    public Report getNextReview() {
        return reviewQueue.poll();
    }

    /**
     * 获取审核队列的大小
     *
     * @return 队列中举报的数量
     */
    public int getReviewQueueSize() {
        return reviewQueue.size();
    }
}