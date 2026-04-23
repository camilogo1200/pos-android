CREATE TABLE IF NOT EXISTS authentication_session (
    session_id INTEGER NOT NULL,
    access_token TEXT NOT NULL,
    refresh_token TEXT,
    token_type TEXT NOT NULL,
    expires_in_seconds INTEGER NOT NULL,
    refresh_expires_in_seconds INTEGER,
    scope TEXT,
    username TEXT NOT NULL,
    PRIMARY KEY(session_id)
)
