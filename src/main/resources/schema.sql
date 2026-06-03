CREATE TABLE IF NOT EXISTS users (
    id          BIGINT AUTO_INCREMENT PRIMARY KEY,
    username    VARCHAR(50)  NOT NULL UNIQUE,
    password    VARCHAR(100) NOT NULL,
    email       VARCHAR(100),
    reputation  INT          DEFAULT 0,
    followed_topics VARCHAR(500) DEFAULT '',
    created_at  TIMESTAMP    DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS posts (
    id            BIGINT AUTO_INCREMENT PRIMARY KEY,
    title         VARCHAR(200)  NOT NULL,
    content       CLOB          NOT NULL,
    category      VARCHAR(50)   NOT NULL,
    author_id     BIGINT        NOT NULL,
    status        VARCHAR(20)   DEFAULT 'PUBLISHED',
    like_count    INT           DEFAULT 0,
    comment_count INT           DEFAULT 0,
    save_count    INT           DEFAULT 0,
    report_count  INT           DEFAULT 0,
    weight        DOUBLE        DEFAULT 0.0,
    created_at    TIMESTAMP     DEFAULT CURRENT_TIMESTAMP,
    updated_at    TIMESTAMP     DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (author_id) REFERENCES users(id)
);

CREATE TABLE IF NOT EXISTS comments (
    id         BIGINT AUTO_INCREMENT PRIMARY KEY,
    post_id    BIGINT       NOT NULL,
    user_id    BIGINT       NOT NULL,
    content    VARCHAR(1000) NOT NULL,
    created_at TIMESTAMP    DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (post_id) REFERENCES posts(id) ON DELETE CASCADE,
    FOREIGN KEY (user_id) REFERENCES users(id)
);

CREATE TABLE IF NOT EXISTS likes (
    id         BIGINT AUTO_INCREMENT PRIMARY KEY,
    post_id    BIGINT NOT NULL,
    user_id    BIGINT NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    UNIQUE (post_id, user_id),
    FOREIGN KEY (post_id) REFERENCES posts(id) ON DELETE CASCADE,
    FOREIGN KEY (user_id) REFERENCES users(id)
);

CREATE TABLE IF NOT EXISTS saves (
    id         BIGINT AUTO_INCREMENT PRIMARY KEY,
    post_id    BIGINT NOT NULL,
    user_id    BIGINT NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    UNIQUE (post_id, user_id),
    FOREIGN KEY (post_id) REFERENCES posts(id) ON DELETE CASCADE,
    FOREIGN KEY (user_id) REFERENCES users(id)
);

CREATE TABLE IF NOT EXISTS reports (
    id          BIGINT AUTO_INCREMENT PRIMARY KEY,
    post_id     BIGINT        NOT NULL,
    user_id     BIGINT        NOT NULL,
    reason      VARCHAR(100)  NOT NULL,
    description VARCHAR(500),
    status      VARCHAR(20)   DEFAULT 'PENDING',
    severity    INT           DEFAULT 1,
    created_at  TIMESTAMP     DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (post_id) REFERENCES posts(id) ON DELETE CASCADE,
    FOREIGN KEY (user_id) REFERENCES users(id)
);

CREATE TABLE IF NOT EXISTS browse_history (
    id         BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id    BIGINT NOT NULL,
    post_id    BIGINT NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(id),
    FOREIGN KEY (post_id) REFERENCES posts(id) ON DELETE CASCADE
);
