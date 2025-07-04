CREATE SCHEMA `inventory_sales_db`;

CREATE TABLE `products`(
    `id`          bigint(20) NOT NULL AUTO_INCREMENT,
    `description` varchar(255) DEFAULT NULL,
    `name`        varchar(255) NOT NULL,
    `price` double NOT NULL,
    `quantity`    int(11) NOT NULL,
    PRIMARY KEY (`id`)
);

CREATE TABLE sales(
    id         BIGINT AUTO_INCREMENT PRIMARY KEY,
    product_id BIGINT    NOT NULL,
    quantity   INT       NOT NULL,
    sale_date  TIMESTAMP NOT NULL,
    FOREIGN KEY (product_id) REFERENCES products (id)
);


