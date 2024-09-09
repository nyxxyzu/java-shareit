CREATE TABLE IF NOT EXISTS users (
  id BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
  name VARCHAR(255) NOT NULL,
  email VARCHAR(512) NOT NULL,
  CONSTRAINT pk_user PRIMARY KEY (id),
  CONSTRAINT UQ_USER_EMAIL UNIQUE (email)
);

CREATE TABLE IF NOT EXISTS requests (
  id BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
  description VARCHAR(512),
  requestor_id BIGINT REFERENCES users(id),
  date_created TIMESTAMP WITHOUT TIME ZONE NOT NULL,
  CONSTRAINT pk_requests PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS items (
  id BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
  name VARCHAR(255) NOT NULL,
  description VARCHAR(512) NOT NULL,
  is_available BOOLEAN NOT NULL,
  owner_id BIGINT REFERENCES users(id) NOT NULL,
  request_id BIGINT REFERENCES requests(id),
  CONSTRAINT pk_item PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS bookings (
  id BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
  start_date TIMESTAMP WITHOUT TIME ZONE NOT NULL,
  end_date TIMESTAMP WITHOUT TIME ZONE NOT NULL,
  item_id BIGINT REFERENCES items(id) NOT NULL,
  booker_id BIGINT REFERENCES users(id) NOT NULL,
  status VARCHAR(50) NOT NULL,
  CONSTRAINT pk_booking PRIMARY KEY (id)
);



CREATE TABLE IF NOT EXISTS comments (
  id BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
  text VARCHAR(512) NOT NULL,
  item_id BIGINT REFERENCES items(id) NOT NULL,
  author_id BIGINT REFERENCES users(id) NOT NULL,
  date_created TIMESTAMP WITHOUT TIME ZONE NOT NULL,
  CONSTRAINT pk_comments PRIMARY KEY (id)
);