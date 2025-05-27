-- Create extensions
CREATE EXTENSION IF NOT EXISTS "uuid-ossp";
CREATE EXTENSION IF NOT EXISTS "pgcrypto";

-- Create schema
CREATE SCHEMA IF NOT EXISTS exam;

-- Set search path
SET search_path TO exam, public;

-- Create tables
CREATE TABLE IF NOT EXISTS exam.quiz_attempts (
    id BIGSERIAL PRIMARY KEY,
    quiz_id BIGINT NOT NULL,
    user_id BIGINT NOT NULL,
    start_time TIMESTAMP NOT NULL,
    end_time TIMESTAMP,
    score DOUBLE PRECISION,
    status VARCHAR(20) NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    created_by VARCHAR(255),
    updated_by VARCHAR(255)
);

-- Create indexes
CREATE INDEX IF NOT EXISTS idx_quiz_attempts_quiz_id ON exam.quiz_attempts(quiz_id);
CREATE INDEX IF NOT EXISTS idx_quiz_attempts_user_id ON exam.quiz_attempts(user_id);
CREATE INDEX IF NOT EXISTS idx_quiz_attempts_status ON exam.quiz_attempts(status);
CREATE INDEX IF NOT EXISTS idx_quiz_attempts_created_at ON exam.quiz_attempts(created_at);

-- Create function to update updated_at timestamp
CREATE OR REPLACE FUNCTION exam.update_updated_at_column()
RETURNS TRIGGER AS $$
BEGIN
    NEW.updated_at = CURRENT_TIMESTAMP;
    RETURN NEW;
END;
$$ language 'plpgsql';

-- Create trigger for quiz_attempts
CREATE TRIGGER update_quiz_attempts_updated_at
    BEFORE UPDATE ON exam.quiz_attempts
    FOR EACH ROW
    EXECUTE FUNCTION exam.update_updated_at_column(); 