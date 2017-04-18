package com.webapp.demo.rest;

import static org.hamcrest.Matchers.equalTo;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import javax.ws.rs.core.MediaType;

import org.hamcrest.CoreMatchers;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.web.util.NestedServletException;

import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class DiffRestControllerTest {

	@Autowired
	private MockMvc mvc;

	@Test
	public void test_getHello() throws Exception {
		mvc.perform(MockMvcRequestBuilders.get("/v1/diff/hello").accept(MediaType.APPLICATION_JSON))
		.andExpect(status().isOk())
		.andExpect(content().string(equalTo("{\"msg\": \"Hello REST World!\"}")));
	}

	/**
	 * Scenario: submit left then right content of equal data
	 * Result: expected equal, no errors
	 * @throws Exception
	 */
	@Test
	public void test_createDiffLeftThenRightEqual_shouldBeSuccessful() throws Exception {
		
		testDiffOperation(2, "blah", "blah", true);
		
	}

	private void testDiffOperation(int id, String left, String right, boolean equal) throws Exception
	{
		// create left side
		String path = "/v1/diff/"+id+"/left";
		String content = left;
		ResultActions result = mvc.perform(MockMvcRequestBuilders.post(path).characterEncoding("utf8").
				contentType("text/plain").accept(MediaType.APPLICATION_JSON).content(content))
		.andExpect(status().isOk());

		String contentAsString = result.andReturn().getResponse().getContentAsString();
		
		DocumentContext json = JsonPath.parse(contentAsString);
		
		Assert.assertThat(json.read("$['id']"), CoreMatchers.equalTo(2));
		Assert.assertThat(json.read("$['left']"), CoreMatchers.equalTo("blah"));

		// create right side
		path = "/v1/diff/"+id+"/right";
		content = right;
		result = mvc.perform(MockMvcRequestBuilders.post(path).characterEncoding("utf8").
				contentType("text/plain").accept(MediaType.APPLICATION_JSON).content(content))
		.andExpect(status().isOk());

		contentAsString = result.andReturn().getResponse().getContentAsString();
		
		json = JsonPath.parse(contentAsString);
		
		Assert.assertThat(json.read("$['id']"), CoreMatchers.equalTo(2));
		Assert.assertThat(json.read("$['left']"), CoreMatchers.equalTo(left));
		Assert.assertThat(json.read("$['right']"), CoreMatchers.equalTo(right));

		// execute diff operation over document
		path = "/v1/diff/"+id;
		result = mvc.perform(MockMvcRequestBuilders.get(path).characterEncoding("utf8").accept(MediaType.APPLICATION_JSON))
		.andExpect(status().isOk());
		contentAsString = result.andReturn().getResponse().getContentAsString();

		json = JsonPath.parse(contentAsString);
		Assert.assertThat(json.read("$['id']"), CoreMatchers.equalTo(2));
		Assert.assertThat(json.read("$['equal']"), CoreMatchers.equalTo(equal));
		
		// retrieve entire doc
		path = "/v1/diff/"+id+"/doc";
		result = mvc.perform(MockMvcRequestBuilders.get(path))
		.andExpect(status().isOk());

		contentAsString = result.andReturn().getResponse().getContentAsString();
		json = JsonPath.parse(contentAsString);
		Assert.assertThat(json.read("$['id']"), CoreMatchers.equalTo(id));
		Assert.assertThat(json.read("$['left']"), CoreMatchers.equalTo(left));
		Assert.assertThat(json.read("$['right']"), CoreMatchers.equalTo(right));

		// delete doc
		path = "/v1/diff/"+id;
		result = mvc.perform(MockMvcRequestBuilders.delete(path))
		.andExpect(status().is2xxSuccessful());
		
		
	}
	
	/**
	 * Scenario: submit left then right content of not-equal data
	 * Result: expected NOT equal, no errors
	 * @throws Exception
	 */
	@Test
	public void test_createDiffLeftThenRightNotEqual_shouldBeSuccessful() throws Exception {
		
		// create left size
		int id = 3;
		String path = "/v1/diff/"+id+"/left";
		String content = "blah";
		ResultActions result = mvc.perform(MockMvcRequestBuilders.post(path).characterEncoding("utf8").
				contentType("text/plain").accept(MediaType.APPLICATION_JSON).content(content))
		.andExpect(status().isOk());

		String contentAsString = result.andReturn().getResponse().getContentAsString();
		
		DocumentContext json = JsonPath.parse(contentAsString);
		
		Assert.assertThat(json.read("$['id']"), CoreMatchers.equalTo(id));
		Assert.assertThat(json.read("$['left']"), CoreMatchers.equalTo(content));

		// create right side
		path = "/v1/diff/"+id+"/right";
		content = "bleeh";
		result = mvc.perform(MockMvcRequestBuilders.post(path).characterEncoding("utf8").
				contentType("text/plain").accept(MediaType.APPLICATION_JSON).content(content))
		.andExpect(status().isOk());

		contentAsString = result.andReturn().getResponse().getContentAsString();
		
		json = JsonPath.parse(contentAsString);
		
		Assert.assertThat(json.read("$['id']"), CoreMatchers.equalTo(id));
		Assert.assertThat(json.read("$['left']"), CoreMatchers.equalTo("blah"));
		Assert.assertThat(json.read("$['right']"), CoreMatchers.equalTo(content));

		// execute diff operation		
		path = "/v1/diff/"+id;
		result = mvc.perform(MockMvcRequestBuilders.get(path).characterEncoding("utf8").accept(MediaType.APPLICATION_JSON))
		.andExpect(status().isOk());
		contentAsString = result.andReturn().getResponse().getContentAsString();

		json = JsonPath.parse(contentAsString);
		
		Assert.assertThat(json.read("$['id']"), CoreMatchers.equalTo(id));
		Assert.assertThat(json.read("$['equal']"), CoreMatchers.equalTo(false));
	}

	/**
	 * Scenario: submit left then right content of equal data
	 * Result: expected equal, no errors
	 * @throws Exception
	 */
	@Test
	public void test_createDiffLeftThenRightNotEqualSameSize_shouldBeSuccessful() throws Exception {
		int id = 4;
		String path = "/v1/diff/"+id+"/left";
		String content = "blah";
		ResultActions result = mvc.perform(MockMvcRequestBuilders.post(path).characterEncoding("utf8").
				contentType("text/plain").accept(MediaType.APPLICATION_JSON).content(content))
		.andExpect(status().isOk());

		String contentAsString = result.andReturn().getResponse().getContentAsString();
		
		DocumentContext json = JsonPath.parse(contentAsString);
		
		Assert.assertThat(json.read("$['id']"), CoreMatchers.equalTo(id));
		Assert.assertThat(json.read("$['left']"), CoreMatchers.equalTo(content));

		path = "/v1/diff/"+id+"/right";
		content = "bleh";
		result = mvc.perform(MockMvcRequestBuilders.post(path).characterEncoding("utf8").
				contentType("text/plain").accept(MediaType.APPLICATION_JSON).content(content))
		.andExpect(status().isOk());

		contentAsString = result.andReturn().getResponse().getContentAsString();
		
		json = JsonPath.parse(contentAsString);
		
		Assert.assertThat(json.read("$['id']"), CoreMatchers.equalTo(id));
		Assert.assertThat(json.read("$['left']"), CoreMatchers.equalTo("blah"));
		Assert.assertThat(json.read("$['right']"), CoreMatchers.equalTo(content));

		path = "/v1/diff/"+id;
		result = mvc.perform(MockMvcRequestBuilders.get(path).characterEncoding("utf8").accept(MediaType.APPLICATION_JSON))
		.andExpect(status().isOk());
		contentAsString = result.andReturn().getResponse().getContentAsString();

		json = JsonPath.parse(contentAsString);
		
		Assert.assertThat(json.read("$['id']"), CoreMatchers.equalTo(id));
		Assert.assertThat(json.read("$['equal']"), CoreMatchers.equalTo(false));
		
		Assert.assertThat(json.read("$['occurrences']"), CoreMatchers.notNullValue());
		Assert.assertThat(json.read("$['occurrences'][0]"), CoreMatchers.notNullValue());
		Assert.assertThat(json.read("$['occurrences'][0]['position']"), CoreMatchers.equalTo(2));
		Assert.assertThat(json.read("$['occurrences'][0]['size']"), CoreMatchers.equalTo(1));
		
	}
	
	/**
	 * Scenario: submit left then right content of equal data
	 * Result: expected equal, no errors
	 * @throws Exception
	 */
	@Test
	public void test_createDiffLeftThenRightEqual_shouldBeSuccessful2() throws Exception {
		int id = 4;
		String path = "/v1/diff/"+id+"/left";
		String content1 = "CS8qKg0KCSAqIFNjZW5hcmlvOiBzdWJtaXQgbGVmdCB0aGVuIHJpZ2h0IGNvbnRlbnQgb2YgZXF1YWwgZGF0YQ0KCSAqIFJlc3VsdDogZXhwZWN0ZWQgZXF1YWwsIG5vIGVycm9ycw0KCSAqIEB0aHJvd3MgRXhjZXB0aW9uDQoJICovDQoJQFRlc3QNCglwdWJsaWMgdm9pZCB0ZXN0X2NyZWF0ZURpZmZMZWZ0VGhlblJpZ2h0Tm90RXF1YWxTYW1lU2l6ZV9zaG91bGRCZVN1Y2Nlc3NmdWwoKSB0aHJvd3MgRXhjZXB0aW9uIHsNCgkJaW50IGlkID0gNDsNCgkJU3RyaW5nIHBhdGggPSAiL3YxL2RpZmYvIitpZCsiL2xlZnQiOw0KCQlTdHJpbmcgY29udGVudCA9ICJibGFoIjsNCgkJUmVzdWx0QWN0aW9ucyByZXN1bHQgPSBtdmMucGVyZm9ybShNb2NrTXZjUmVxdWVzdEJ1aWxkZXJzLnBvc3QocGF0aCkuY2hhcmFjdGVyRW5jb2RpbmcoInV0ZjgiKS4NCgkJCQljb250ZW50VHlwZSgidGV4dC9wbGFpbiIpLmFjY2VwdChNZWRpYVR5cGUuQVBQTElDQVRJT05fSlNPTikuY29udGVudChjb250ZW50KSkNCgkJLmFuZEV4cGVjdChzdGF0dXMoKS5pc09rKCkpOw0KDQoJCVN0cmluZyBjb250ZW50QXNTdHJpbmcgPSByZXN1bHQuYW5kUmV0dXJuKCkuZ2V0UmVzcG9uc2UoKS5nZXRDb250ZW50QXNTdHJpbmcoKTsNCgkJDQoJCURvY3VtZW50Q29udGV4dCBqc29uID0gSnNvblBhdGgucGFyc2UoY29udGVudEFzU3RyaW5nKTsNCgkJDQoJCUFzc2VydC5hc3NlcnRUaGF0KGpzb24ucmVhZCgiJFsnaWQnXSIpLCBDb3JlTWF0Y2hlcnMuZXF1YWxUbyhpZCkpOw0KCQlBc3NlcnQuYXNzZXJ0VGhhdChqc29uLnJlYWQoIiRbJ2xlZnQnXSIpLCBDb3JlTWF0Y2hlcnMuZXF1YWxUbyhjb250ZW50KSk7DQoNCgkJcGF0aCA9ICIvdjEvZGlmZi8iK2lkKyIvcmlnaHQiOw0KCQljb250ZW50ID0gImJsZWgiOw0KCQlyZXN1bHQgPSBtdmMucGVyZm9ybShNb2NrTXZjUmVxdWVzdEJ1aWxkZXJzLnBvc3QocGF0aCkuY2hhcmFjdGVyRW5jb2RpbmcoInV0ZjgiKS4NCgkJCQljb250ZW50VHlwZSgidGV4dC9wbGFpbiIpLmFjY2VwdChNZWRpYVR5cGUuQVBQTElDQVRJT05fSlNPTikuY29udGVudChjb250ZW50KSkNCgkJLmFuZEV4cGVjdChzdGF0dXMoKS5pc09rKCkpOw0KDQoJCWNvbnRlbnRBc1N0cmluZyA9IHJlc3VsdC5hbmRSZXR1cm4oKS5nZXRSZXNwb25zZSgpLmdldENvbnRlbnRBc1N0cmluZygpOw0KCQkNCgkJanNvbiA9IEpzb25QYXRoLnBhcnNlKGNvbnRlbnRBc1N0cmluZyk7DQoJCQ0KCQlBc3NlcnQuYXNzZXJ0VGhhdChqc29uLnJlYWQoIiRbJ2lkJ10iKSwgQ29yZU1hdGNoZXJzLmVxdWFsVG8oaWQpKTsNCgkJQXNzZXJ0LmFzc2VydFRoYXQoanNvbi5yZWFkKCIkWydsZWZ0J10iKSwgQ29yZU1hdGNoZXJzLmVxdWFsVG8oImJsYWgiKSk7DQoJCUFzc2VydC5hc3NlcnRUaGF0KGpzb24ucmVhZCgiJFsncmlnaHQnXSIpLCBDb3JlTWF0Y2hlcnMuZXF1YWxUbyhjb250ZW50KSk7DQoNCgkJcGF0aCA9ICIvdjEvZGlmZi8iK2lkOw0KCQlyZXN1bHQgPSBtdmMucGVyZm9ybShNb2NrTXZjUmVxdWVzdEJ1aWxkZXJzLmdldChwYXRoKS5jaGFyYWN0ZXJFbmNvZGluZygidXRmOCIpLmFjY2VwdChNZWRpYVR5cGUuQVBQTElDQVRJT05fSlNPTikpDQoJCS5hbmRFeHBlY3Qoc3RhdHVzKCkuaXNPaygpKTsNCgkJY29udGVudEFzU3RyaW5nID0gcmVzdWx0LmFuZFJldHVybigpLmdldFJlc3BvbnNlKCkuZ2V0Q29udGVudEFzU3RyaW5nKCk7DQoNCgkJanNvbiA9IEpzb25QYXRoLnBhcnNlKGNvbnRlbnRBc1N0cmluZyk7DQoJCQ0KCQlBc3NlcnQuYXNzZXJ0VGhhdChqc29uLnJlYWQoIiRbJ2lkJ10iKSwgQ29yZU1hdGNoZXJzLmVxdWFsVG8oaWQpKTsNCgkJQXNzZXJ0LmFzc2VydFRoYXQoanNvbi5yZWFkKCIkWydlcXVhbCddIiksIENvcmVNYXRjaGVycy5lcXVhbFRvKGZhbHNlKSk7DQoJCQ0KCQlBc3NlcnQuYXNzZXJ0VGhhdChqc29uLnJlYWQoIiRbJ29jY3VycmVuY2VzJ10iKSwgQ29yZU1hdGNoZXJzLm5vdE51bGxWYWx1ZSgpKTsNCgkJQXNzZXJ0LmFzc2VydFRoYXQoanNvbi5yZWFkKCIkWydvY2N1cnJlbmNlcyddWzBdIiksIENvcmVNYXRjaGVycy5ub3ROdWxsVmFsdWUoKSk7DQoJCUFzc2VydC5hc3NlcnRUaGF0KGpzb24ucmVhZCgiJFsnb2NjdXJyZW5jZXMnXVswXVsncG9zaXRpb24nXSIpLCBDb3JlTWF0Y2hlcnMuZXF1YWxUbygyKSk7DQoJCUFzc2VydC5hc3NlcnRUaGF0KGpzb24ucmVhZCgiJFsnb2NjdXJyZW5jZXMnXVswXVsnc2l6ZSddIiksIENvcmVNYXRjaGVycy5lcXVhbFRvKDEpKTsNCgkJDQoJfQ0K";
		
		ResultActions result = mvc.perform(MockMvcRequestBuilders.post(path).characterEncoding("utf8").
				contentType("text/plain").accept(MediaType.APPLICATION_JSON).content(content1))
		.andExpect(status().isOk());

		String contentAsString = result.andReturn().getResponse().getContentAsString();
		
		DocumentContext json = JsonPath.parse(contentAsString);
		
		Assert.assertThat(json.read("$['id']"), CoreMatchers.equalTo(id));
		Assert.assertThat(json.read("$['left']"), CoreMatchers.equalTo(content1));

		path = "/v1/diff/"+id+"/right";
		result = mvc.perform(MockMvcRequestBuilders.post(path).characterEncoding("utf8").
				contentType("text/plain").accept(MediaType.APPLICATION_JSON).content(content1))
		.andExpect(status().isOk());

		contentAsString = result.andReturn().getResponse().getContentAsString();
		
		json = JsonPath.parse(contentAsString);
		
		Assert.assertThat(json.read("$['id']"), CoreMatchers.equalTo(id));
		Assert.assertThat(json.read("$['left']"), CoreMatchers.equalTo(content1));
		Assert.assertThat(json.read("$['right']"), CoreMatchers.equalTo(content1));

		path = "/v1/diff/"+id;
		result = mvc.perform(MockMvcRequestBuilders.get(path).characterEncoding("utf8").accept(MediaType.APPLICATION_JSON))
		.andExpect(status().isOk());
		contentAsString = result.andReturn().getResponse().getContentAsString();

		json = JsonPath.parse(contentAsString);
		
		Assert.assertThat(json.read("$['id']"), CoreMatchers.equalTo(id));
		Assert.assertThat(json.read("$['equal']"), CoreMatchers.equalTo(true));
		
		path = "/v1/diff/"+id+"/doc";
		result = mvc.perform(MockMvcRequestBuilders.get(path))
		.andExpect(status().isOk());

		contentAsString = result.andReturn().getResponse().getContentAsString();
		json = JsonPath.parse(contentAsString);
		Assert.assertThat(json.read("$['id']"), CoreMatchers.equalTo(id));
		Assert.assertThat(json.read("$['left']"), CoreMatchers.equalTo(content1));
		Assert.assertThat(json.read("$['right']"), CoreMatchers.equalTo(content1));

		path = "/v1/diff/"+id;
		result = mvc.perform(MockMvcRequestBuilders.delete(path))
		.andExpect(status().is2xxSuccessful());
		
		
		
	}
	
	/**
	 * Scenario: submit left empty
	 * Result: server error
	 * @throws Exception
	 */
	@Test (expected=NestedServletException.class)
	public void test_createDiffLeftNull_shouldThrowError() throws Exception {
		int id = 4;
		String path = "/v1/diff/"+id+"/left";
		String content1 = " ";
		
		mvc.perform(MockMvcRequestBuilders.post(path).characterEncoding("utf8").
				contentType("text/plain").accept(MediaType.APPLICATION_JSON).content(content1))
		.andExpect(status().is4xxClientError());

	}

	/**
	 * Scenario: submit right empty
	 * Result: server error
	 * @throws Exception
	 */
	@Test (expected=NestedServletException.class)
	public void test_createDiffRightNull_shouldThrowError() throws Exception{
		int id = 5;
		String path = "/v1/diff/"+id+"/right";
		String content1 = " ";
		
		mvc.perform(MockMvcRequestBuilders.post(path).characterEncoding("utf8").
				contentType("text/plain").accept(MediaType.APPLICATION_JSON).content(content1))
		.andExpect(status().is4xxClientError());

	}

}
