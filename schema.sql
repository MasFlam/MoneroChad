-- MoneroChad PostgreSQL database schema definition.

CREATE TABLE tipjars (
	user_id BIGINT PRIMARY KEY,
	xmr_address VARCHAR NOT NULL
);
