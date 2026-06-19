-- Mini Mall Schema

CREATE TABLE IF NOT EXISTS user (
    id          INTEGER PRIMARY KEY AUTOINCREMENT,
    email       VARCHAR(100) NOT NULL UNIQUE,
    password    VARCHAR(200) NOT NULL,
    name        VARCHAR(50)  NOT NULL,
    role        VARCHAR(20)  NOT NULL DEFAULT 'CUSTOMER',
    created_at  DATETIME     NOT NULL DEFAULT (datetime('now','localtime')),
    updated_at  DATETIME     NOT NULL DEFAULT (datetime('now','localtime'))
);

CREATE TABLE IF NOT EXISTS category (
    id          INTEGER PRIMARY KEY AUTOINCREMENT,
    name        VARCHAR(50)  NOT NULL UNIQUE,
    slug        VARCHAR(50)  NOT NULL UNIQUE,
    created_at  DATETIME     NOT NULL DEFAULT (datetime('now','localtime'))
);

CREATE TABLE IF NOT EXISTS product (
    id          INTEGER PRIMARY KEY AUTOINCREMENT,
    name        VARCHAR(200) NOT NULL,
    slug        VARCHAR(200) NOT NULL UNIQUE,
    description TEXT,
    price       INTEGER      NOT NULL,
    image_url   VARCHAR(500),
    stock       INTEGER      NOT NULL DEFAULT 999,
    is_active   TINYINT      NOT NULL DEFAULT 1,
    category_id INTEGER      NOT NULL,
    created_at  DATETIME     NOT NULL DEFAULT (datetime('now','localtime')),
    updated_at  DATETIME     NOT NULL DEFAULT (datetime('now','localtime')),
    FOREIGN KEY (category_id) REFERENCES category(id)
);

CREATE TABLE IF NOT EXISTS cart_item (
    id          INTEGER PRIMARY KEY AUTOINCREMENT,
    user_id     INTEGER      NOT NULL,
    product_id  INTEGER      NOT NULL,
    quantity    INTEGER      NOT NULL DEFAULT 1,
    created_at  DATETIME     NOT NULL DEFAULT (datetime('now','localtime')),
    updated_at  DATETIME     NOT NULL DEFAULT (datetime('now','localtime')),
    FOREIGN KEY (user_id)    REFERENCES user(id)    ON DELETE CASCADE,
    FOREIGN KEY (product_id) REFERENCES product(id) ON DELETE CASCADE,
    UNIQUE(user_id, product_id)
);

CREATE TABLE IF NOT EXISTS t_order (
    id           INTEGER PRIMARY KEY AUTOINCREMENT,
    user_id      INTEGER      NOT NULL,
    status       VARCHAR(20)  NOT NULL DEFAULT 'PENDING',
    total_amount INTEGER      NOT NULL,
    created_at   DATETIME     NOT NULL DEFAULT (datetime('now','localtime')),
    updated_at   DATETIME     NOT NULL DEFAULT (datetime('now','localtime')),
    FOREIGN KEY (user_id) REFERENCES user(id)
);

CREATE TABLE IF NOT EXISTS order_item (
    id            INTEGER PRIMARY KEY AUTOINCREMENT,
    order_id      INTEGER      NOT NULL,
    product_id    INTEGER      NOT NULL,
    product_name  VARCHAR(200) NOT NULL,
    product_price INTEGER      NOT NULL,
    quantity      INTEGER      NOT NULL,
    FOREIGN KEY (order_id)   REFERENCES t_order(id) ON DELETE CASCADE,
    FOREIGN KEY (product_id) REFERENCES product(id)
);
