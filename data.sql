MERGE INTO users (username, password, email, reputation, followed_topics) KEY(username) VALUES
('alice', 'pass123', 'alice@insightforge.com', 120, 'Technology,Science'),
('bob', 'pass123', 'bob@insightforge.com', 95, 'Technology,Design'),
('charlie', 'pass123', 'charlie@insightforge.com', 80, 'Science'),
('diana', 'pass123', 'diana@insightforge.com', 60, 'Design,Business'),
('admin', 'admin123', 'admin@insightforge.com', 999, ''),
('admin123', '061110', 'admin123@insightforge.com', 999, '');

MERGE INTO posts (id, title, content, category, author_id, like_count, comment_count, weight) KEY(id) VALUES
(1, 'The Future of AI in 2026', 'Artificial intelligence is evolving rapidly. In this post, we explore the latest trends in large language models, autonomous agents, and multimodal AI systems that are reshaping industries worldwide.', 'Technology', 1, 45, 12, 90.0),
(2, 'Understanding Quantum Computing', 'Quantum computing leverages qubits and superposition to solve problems that classical computers cannot handle efficiently. Let us dive into the key concepts and recent breakthroughs.', 'Science', 1, 38, 8, 76.0),
(3, 'Clean Code Principles Every Developer Should Know', 'Writing clean, maintainable code is an art. This article covers naming conventions, function design, SOLID principles, and practical refactoring techniques.', 'Technology', 2, 52, 15, 104.0),
(4, 'The Art of Minimalist UI Design', 'Minimalism in UI design is about reducing elements to their essential form while maintaining usability. We analyze successful minimalist interfaces and the psychology behind them.', 'Design', 2, 30, 6, 60.0),
(5, 'Building Scalable Microservices with Spring Boot', 'Spring Boot provides a robust foundation for building microservices. This guide covers service discovery, circuit breakers, API gateways, and distributed tracing.', 'Technology', 3, 41, 10, 82.0),
(6, 'Climate Science: What the Latest Data Tells Us', 'Recent climate data reveals accelerating changes in global temperature patterns. We examine the scientific evidence and what it means for the future.', 'Science', 3, 25, 7, 50.0),
(7, 'Design Thinking in Product Management', 'Design thinking is a human-centered approach to innovation. Learn how to apply empathy, ideation, and prototyping to create better products.', 'Business', 4, 28, 5, 56.0),
(8, 'Introduction to Data Visualization with D3.js', 'D3.js is a powerful JavaScript library for creating interactive data visualizations. This tutorial walks through bar charts, scatter plots, and force-directed graphs.', 'Technology', 4, 35, 9, 70.0);

MERGE INTO comments (id, post_id, user_id, content) KEY(id) VALUES
(1, 1, 2, 'Great insights on AI trends! The multimodal section was particularly interesting.'),
(2, 1, 3, 'I wonder how these models will impact education in the coming years.'),
(3, 2, 4, 'Quantum computing is fascinating. Can you explain more about quantum error correction?'),
(4, 3, 1, 'SOLID principles are timeless. Great refresher!'),
(5, 3, 4, 'I would add that naming things is the hardest part of programming.'),
(6, 4, 1, 'Minimalist design is harder than it looks. Well explained!'),
(7, 5, 1, 'Spring Boot has really matured over the years. Excellent guide!'),
(8, 5, 2, 'Would love to see a follow-up on Kubernetes deployment strategies.');

MERGE INTO likes (post_id, user_id) KEY(post_id, user_id) VALUES
(1, 2), (1, 3), (1, 4), (2, 1), (2, 3),
(3, 1), (3, 2), (3, 4), (4, 1), (4, 3),
(5, 1), (5, 2), (6, 1), (7, 2), (8, 1);

MERGE INTO saves (post_id, user_id) KEY(post_id, user_id) VALUES
(1, 2), (3, 1), (5, 2), (3, 4), (8, 2);
