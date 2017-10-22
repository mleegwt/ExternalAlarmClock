package test;

import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Collections;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.commons.fileupload.FileItemIterator;
import org.apache.commons.fileupload.FileItemStream;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.io.IOUtils;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.common.xcontent.NamedXContentRegistry;
import org.elasticsearch.common.xcontent.XContentFactory;
import org.elasticsearch.common.xcontent.XContentParser;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.index.query.QueryParseContext;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchModule;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.transport.client.PreBuiltTransportClient;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class StoreAttachmentsTest {
	private TransportClient client;

	@SuppressWarnings("resource")
	@Before
	public void setUp() throws UnknownHostException {
		Settings settings = Settings.builder().put("cluster.name", "sonar").build();
		client = new PreBuiltTransportClient(settings)
				.addTransportAddress(new InetSocketTransportAddress(InetAddress.getByName("192.168.178.31"), 9300));
	}

	@Test
	public void testSearchAttachments() throws IOException, FileUploadException {
		String queryString = getQueryString();
		SearchResponse response = getSearchResponse(queryString);

		for (SearchHit hit : response.getHits()) {
			String body = getHttpRequestBody(hit);
			
			FileItemIterator it = delight.fileupload.FileUpload.parse(body.getBytes(), getHttpContentType(hit));
			
			do {
				FileItemStream fileItem = it.next(); 
				
				IOUtils.copy(fileItem.openStream(), new FileOutputStream("test.zip"));
			} while (it.hasNext());
		}
	}

	@SuppressWarnings("unchecked")
	private String getHttpContentType(SearchHit hit) {
		Map<String, Object> httpMap = (Map<String, Object>) hit.getSourceAsMap().get("http");
		Map<String, Object> requestMap = (Map<String, Object>) httpMap.get("request");
		Map<String, Object> headersMap = (Map<String, Object>) requestMap.get("headers");
		return (String)headersMap.get("content-type");
	}

	@SuppressWarnings("unchecked")
	private String getHttpRequestBody(SearchHit hit) {
		Map<String, Object> httpMap = (Map<String, Object>) hit.getSourceAsMap().get("http");
		Map<String, Object> requestMap = (Map<String, Object>) httpMap.get("request");
		return (String)requestMap.get("body");
	}

	private SearchResponse getSearchResponse(String queryString) throws IOException {
		SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
		SearchModule searchModule = new SearchModule(Settings.EMPTY, false, Collections.emptyList());
		try (XContentParser parser = XContentFactory.xContent(XContentType.JSON)
				.createParser(new NamedXContentRegistry(searchModule.getNamedXContents()), queryString)) {
			searchSourceBuilder.parseXContent(new QueryParseContext(parser));
		}
		SearchResponse response = client.prepareSearch("packetbeat-*").setSource(searchSourceBuilder)
				.setFetchSource(true).addStoredField("_source.http.request.body").get();
		return response;
	}

	private String getQueryString() {
		InputStream inputStream = StoreAttachmentsTest.class.getResourceAsStream("/search.json");
		String queryString = new BufferedReader(new InputStreamReader(inputStream)).lines()
				.collect(Collectors.joining("\n"));
		return queryString;
	}

	@After
	public void tearDown() {
		client.close();
	}
}
