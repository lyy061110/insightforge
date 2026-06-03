package com.insightforge.util;

import org.commonmark.node.Node;
import org.commonmark.parser.Parser;
import org.commonmark.renderer.html.HtmlRenderer;

/**
 * Markdown工具类
 * 用于将Markdown文本转换为HTML格式
 */
public class MarkdownUtil {

    /** Markdown解析器，用于解析Markdown文本为抽象语法树 */
    private static final Parser parser = Parser.builder().build();
    
    /** HTML渲染器，用于将抽象语法树渲染为HTML字符串 */
    private static final HtmlRenderer renderer = HtmlRenderer.builder().build();

    /**
     * 将Markdown文本转换为HTML格式
     *
     * @param markdown Markdown格式的文本内容
     * @return 转换后的HTML字符串，如果输入为空则返回空字符串
     */
    public static String markdownToHtml(String markdown) {
        // 空值检查，避免空指针异常
        if (markdown == null || markdown.isEmpty()) {
            return "";
        }
        // 解析Markdown文本为文档节点
        Node document = parser.parse(markdown);
        // 将文档节点渲染为HTML字符串
        return renderer.render(document);
    }
}