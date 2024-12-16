CREATE TABLE IF NOT EXISTS app_users (
    user_id VARCHAR PRIMARY KEY,
    password VARCHAR NOT NULL,
    email_address VARCHAR UNIQUE NOT NULL,
    display_name VARCHAR NOT NULL
);

CREATE TABLE IF NOT EXISTS videos (
    video_id VARCHAR PRIMARY KEY,
    creator_id VARCHAR NOT NULL,
    video_url VARCHAR NOT NULL,
    created_at BIGINT NOT NULL,
    like_count INT DEFAULT 0,
    CONSTRAINT fk_creator_id FOREIGN KEY (creator_id) REFERENCES app_users(user_id)
);

CREATE TABLE IF NOT EXISTS connections (
    connection_id VARCHAR PRIMARY KEY,
    follower_id VARCHAR NOT NULL,
    followee_id VARCHAR NOT NULL,
    CONSTRAINT fk_follower_id FOREIGN KEY (follower_id) REFERENCES app_users(user_id),
    CONSTRAINT fk_followee_id FOREIGN KEY (followee_id) REFERENCES app_users(user_id)
);

CREATE TABLE IF NOT EXISTS reactions (
    reaction_id VARCHAR PRIMARY KEY,
    reacting_user VARCHAR NOT NULL,
    target_video VARCHAR NOT NULL,
    video_owner VARCHAR NOT NULL,
    CONSTRAINT fk_reacting_user FOREIGN KEY (reacting_user) REFERENCES app_users(user_id),
    CONSTRAINT fk_target_video FOREIGN KEY (target_video) REFERENCES videos(video_id),
    CONSTRAINT fk_video_owner FOREIGN KEY (video_owner) REFERENCES app_users(user_id)
);
