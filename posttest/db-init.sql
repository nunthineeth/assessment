-- Drop tables if they exist
DROP TABLE IF EXISTS lotteries CASCADE;

CREATE TABLE IF NOT EXISTS lotteries (
    ticket_id VARCHAR(6) PRIMARY KEY,
    price NUMERIC(10, 2),
    amount INTEGER,
    is_deleted BOOLEAN DEFAULT false NOT NULL,
    created_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    last_modified_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

create index on lotteries(ticket_id);

DROP TABLE IF EXISTS user_lotteries CASCADE;

CREATE TABLE IF NOT EXISTS user_lotteries (
    user_lottery_id SERIAL PRIMARY KEY,
    user_id VARCHAR(10) NOT NULL,
    ticket_id VARCHAR(6) NOT NULL,
    price NUMERIC(10, 2),
    amount INTEGER,
    created_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    last_modified_date TIMESTAMP,

    CONSTRAINT fk_user_lotteries_lotteries FOREIGN KEY (ticket_id) REFERENCES lotteries(ticket_id) ON DELETE CASCADE
);

create index on user_lotteries(user_lottery_id);
create index on user_lotteries(user_id, ticket_id);