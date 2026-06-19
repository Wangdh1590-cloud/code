-- Mini Mall Seed Data

-- Categories
INSERT OR IGNORE INTO category (id, name, slug) VALUES (1, '手机数码', 'phone-digital');
INSERT OR IGNORE INTO category (id, name, slug) VALUES (2, '电脑办公', 'computer-office');
INSERT OR IGNORE INTO category (id, name, slug) VALUES (3, '家用电器', 'home-appliance');
INSERT OR IGNORE INTO category (id, name, slug) VALUES (4, '服装鞋帽', 'clothing');
INSERT OR IGNORE INTO category (id, name, slug) VALUES (5, '食品生鲜', 'food');

-- Products
INSERT OR IGNORE INTO product (id, name, slug, description, price, image_url, stock, is_active, category_id) VALUES
(1, 'iPhone 15 Pro Max', 'iphone-15-pro-max', 'Apple 最新旗舰手机，A17 Pro 芯片，钛金属设计', 899900, 'https://picsum.photos/seed/iphone15/400/400', 100, 1, 1),
(2, '华为 Mate 60 Pro', 'huawei-mate-60-pro', '华为旗舰手机，卫星通话，超强影像', 699900, 'https://picsum.photos/seed/mate60/400/400', 80, 1, 1),
(3, '小米 14 Ultra', 'xiaomi-14-ultra', '骁龙8Gen3，徕卡光学，专业影像旗舰', 599900, 'https://picsum.photos/seed/mi14/400/400', 120, 1, 1),
(4, 'Sony WH-1000XM5 耳机', 'sony-wh1000xm5', '行业领先降噪，30小时续航，佩戴舒适', 249900, 'https://picsum.photos/seed/sonyxm5/400/400', 60, 1, 1),

(5, 'MacBook Pro 14寸 M3', 'macbook-pro-14-m3', 'Apple M3芯片，Liquid Retina XDR显示屏，18小时续航', 1299900, 'https://picsum.photos/seed/macbook14/400/400', 50, 1, 2),
(6, 'ThinkPad X1 Carbon', 'thinkpad-x1-carbon', '商务旗舰轻薄本，第13代酷睿，1.12kg', 999900, 'https://picsum.photos/seed/x1carbon/400/400', 30, 1, 2),
(7, 'iPad Air M2', 'ipad-air-m2', 'M2芯片，11英寸Liquid Retina，支持Apple Pencil Pro', 479900, 'https://picsum.photos/seed/ipadair/400/400', 70, 1, 2),
(8, '机械革命 极光 Pro', 'mechrevo-aurora-pro', 'i7-13620H + RTX4060，15.6寸高刷电竞屏', 649900, 'https://picsum.photos/seed/gaminglaptop/400/400', 40, 1, 2),

(9, '戴森 V15 Detect 吸尘器', 'dyson-v15-detect', '激光探测微尘，LCD屏实时显示，60分钟续航', 499900, 'https://picsum.photos/seed/dysonv15/400/400', 45, 1, 3),
(10, '米家扫拖机器人 2', 'mijia-robot-2', '激光导航，2800Pa吸力，扫拖一体', 169900, 'https://picsum.photos/seed/robot2/400/400', 90, 1, 3),
(11, '戴森 Airwrap 多功能美发器', 'dyson-airwrap', '康达效应气流，多款配件，轻松造型', 389900, 'https://picsum.photos/seed/airwrap/400/400', 35, 1, 3),
(12, '格力 冷静王+ 空调 1.5匹', 'gree-ac-1-5hp', '新一级能效，变频冷暖，自清洁', 329900, 'https://picsum.photos/seed/greeac/400/400', 25, 1, 3),

(13, 'Nike Air Jordan 1 Retro', 'nike-aj1-retro', '经典复刻，Air Sole气垫，潮流百搭', 129900, 'https://picsum.photos/seed/aj1/400/400', 200, 1, 4),
(14, '优衣库 轻薄羽绒服', 'uniqlo-light-down', '轻量保暖，可收纳设计，秋冬必备', 49900, 'https://picsum.photos/seed/uniqlodown/400/400', 300, 1, 4),
(15, '始祖鸟 Alpha SV 冲锋衣', 'arcteryx-alpha-sv', 'GORE-TEX Pro面料，极致防护，专业户外', 820000, 'https://picsum.photos/seed/arcteryx/400/400', 15, 1, 4),
(16, 'New Balance 2002R', 'nb-2002r', '复古跑鞋，ABZORB缓震，百搭款', 89900, 'https://picsum.photos/seed/nb2002r/400/400', 150, 1, 4),

(17, '三只松鼠 坚果大礼包', 'three-squirrels-nuts', '每日坚果混合装，30袋独立包装，健康零食', 12800, 'https://picsum.photos/seed/nuts/400/400', 500, 1, 5),
(18, '茅台 飞天 53度 500ml', 'maotai-feitian', '贵州茅台酒，酱香型白酒，收藏佳品', 149900, 'https://picsum.photos/seed/maotai/400/400', 10, 1, 5),
(19, '认养一头牛 纯牛奶 250ml*24', 'milk-24-pack', '高端牧场奶源，3.6g优质乳蛋白', 6900, 'https://picsum.photos/seed/milk/400/400', 400, 1, 5),
(20, '智利车厘子 JJ级 2.5kg', 'chile-cherry-jj', '原箱进口，果径28-30mm，新鲜直达', 19900, 'https://picsum.photos/seed/cherry/400/400', 200, 1, 5);
