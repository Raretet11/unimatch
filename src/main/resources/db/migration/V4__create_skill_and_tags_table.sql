CREATE TABLE IF NOT EXISTS skills (
    id BIGSERIAL PRIMARY KEY,
    user_id BIGINT NOT NULL REFERENCES users(id),
    is_response BOOLEAN NOT NULL,
    title VARCHAR(255) NOT NULL,
    description TEXT,
    study_format VARCHAR(20),
    session_type VARCHAR(20),
    reward_type VARCHAR(20),
    is_active BOOLEAN NOT NULL,
    reward_amount_rub DECIMAL(12, 2) CHECK (reward_amount_rub >= 0),
    created_at TIMESTAMP WITH TIME ZONE DEFAULT NOW(),
    updated_at TIMESTAMP WITH TIME ZONE DEFAULT NOW()
);

CREATE TABLE IF NOT EXISTS tags (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL UNIQUE,
    formated_name VARCHAR(255) NOT NULL UNIQUE,
    usage_count INTEGER DEFAULT 0,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT NOW(),
    updated_at TIMESTAMP WITH TIME ZONE DEFAULT NOW()
);

CREATE TABLE IF NOT EXISTS request_tags (
    skill_id BIGINT NOT NULL REFERENCES skills(id),
    tag_id BIGINT NOT NULL REFERENCES tags(id),
    created_at TIMESTAMP WITH TIME ZONE DEFAULT NOW(),
    PRIMARY KEY (skill_id, tag_id)
);
