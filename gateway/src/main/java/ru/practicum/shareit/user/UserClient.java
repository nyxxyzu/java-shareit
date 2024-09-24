package ru.practicum.shareit.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.util.DefaultUriBuilderFactory;
import ru.practicum.shareit.client.BaseClient;

@Service
public class UserClient extends BaseClient {

	private static final String API_PREFIX = "/users";

	@Autowired
	public UserClient(@Value("${shareit-server.url}") String serverUrl, RestTemplateBuilder builder) {
		super(
				builder
						.uriTemplateHandler(new DefaultUriBuilderFactory(serverUrl + API_PREFIX))
						.requestFactory(() -> new HttpComponentsClientHttpRequestFactory())
						.build()
		);
	}

	public ResponseEntity<Object> getAllUsers() {
		return get("");
	}

	public ResponseEntity<Object> create(RequestUserDto dto) {
		return post("", dto);
	}

	public ResponseEntity<Object> update(RequestUserDto dto, long id) {
		return patch("/" + id, dto);
	}

	public ResponseEntity<Object> getUserById(long id) {
		return get("/" + id);
	}

	public ResponseEntity<Object> deleteUser(long id) {
		return delete("/" + id);
	}
}
