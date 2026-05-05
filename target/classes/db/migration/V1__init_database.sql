CREATE TABLE payments (
    id SERIAL PRIMARY KEY,
    amount DOUBLE PRECISION NOT NULL,
    category VARCHAR(255),
    description TEXT,
    type VARCHAR(50),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
