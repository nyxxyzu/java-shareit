package ru.practicum.shareit.item;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.util.DefaultUriBuilderFactory;
import ru.practicum.shareit.client.BaseClient;

import java.util.Map;

@Service
public class ItemClient extends BaseClient {

	private static final String API_PREFIX = "/items";

	@Autowired
	public ItemClient(@Value("${shareit-server.url}") String serverUrl, RestTemplateBuilder builder) {
		super(
				builder
						.uriTemplateHandler(new DefaultUriBuilderFactory(serverUrl + API_PREFIX))
						.requestFactory(() -> new HttpComponentsClientHttpRequestFactory())
						.build()
		);
	}

	public ResponseEntity<Object> createItem(RequestItemDto dto, long userId) {
		return post("", userId, dto);
	}

	public ResponseEntity<Object> updateItem(RequestItemDto dto, long itemId, long userId) {
		return patch("/" + itemId, userId, dto);
	}

	public ResponseEntity<Object> getItemById(long itemId) {
		return get("/" + itemId);
	}

	public ResponseEntity<Object> getAllItemsByOwner(long userId) {
		return get("", userId);
	}

	public ResponseEntity<Object> searchForItem(String query) {
		Map<String, Object> params = Map.of("text", query);
		return get("/search?text={text}", params);

	}

	public ResponseEntity<Object> createComment(long itemId, RequestCommentDto dto, long userId) {
		return post("/" + itemId + "/comment", userId, dto);
	}
}
