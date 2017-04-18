package com.webapp.demo.service;

import java.util.Random;

import org.hamcrest.CoreMatchers;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.webapp.demo.model.DiffDocument;
import com.webapp.demo.model.DiffProcessorReport;

public class DiffProcessorServiceTest {

	private DiffProcessorService diffProcessorService = new DiffProcessorServiceImpl();
	private DiffDocument doc;
	
	@Before
	public void setup()
	{
		doc = new DiffDocument();
	}

	@Test
	public void testExecuteDiffToolEqual()
	{
		String right = "dGhlIGJvb2sgaXMgb24gdGhlIHRhYmxl";
		String left = "dGhlIGJvb2sgaXMgb24gdGhlIHRhYmxl";
		Long id = new Long(2);
		
		doc.setId(id);
		doc.setLeft(left);
		doc.setRight(right);
		
		DiffProcessorReport report = diffProcessorService.executeDiffTool(doc);
		Assert.assertThat(report.getId(), CoreMatchers.equalTo(doc.getId()));
		Assert.assertThat(report.isEqual(), CoreMatchers.equalTo(true));
	}
	
	@Test
	public void testExecuteDiffToolDifferentSizes()
	{
		String right = "test1";
		String left = "test12";
		Long id = new Long(2);
		doc.setId(id);
		doc.setLeft(left);
		doc.setRight(right);
		
		DiffProcessorReport report = diffProcessorService.executeDiffTool(doc);
		Assert.assertThat(report.getId(), CoreMatchers.equalTo(doc.getId()));
		Assert.assertThat(report.isEqual(), CoreMatchers.equalTo(false));
	}

	@Test
	public void testExecuteDiffToolDifferentWithSameSizes()
	{
		String right = "the book is on the table";
		String left =  "thymbook is an the tombe";
		Long id = new Long(2);
		doc.setId(id);
		doc.setLeft(left);
		doc.setRight(right);
		
		DiffProcessorReport report = diffProcessorService.executeDiffTool(doc);
		Assert.assertThat(report.getId(), CoreMatchers.equalTo(doc.getId()));
		Assert.assertThat(report.isEqual(), CoreMatchers.equalTo(false));
		Assert.assertThat(report.getOccurrences().size(), CoreMatchers.equalTo(3));
		verifyPositionAndSize(report, 0, 2, 2);
		verifyPositionAndSize(report, 1, 12, 1);
		verifyPositionAndSize(report, 2, 20, 3);
	}

	private void verifyPositionAndSize(DiffProcessorReport report, int index, int position, int size)
	{
		Assert.assertThat(report.getOccurrences().size() > index, CoreMatchers.equalTo(true));
		Assert.assertThat(report.getOccurrences().get(index), CoreMatchers.notNullValue());
		Assert.assertThat(report.getOccurrences().get(index).getPosition(), CoreMatchers.equalTo(position));
		Assert.assertThat(report.getOccurrences().get(index).getSize(), CoreMatchers.equalTo(size));
	}
	
	@Test
	public void testExecuteDiffToolAllDifferentWithSameSizes()
	{
		String right = "the book is on the table";
		String left =  "fdgkjgsnkjfskjsfhksfdkjf";
		Long id = new Long(2);
		doc.setId(id);
		doc.setLeft(left);
		doc.setRight(right);
		
		DiffProcessorReport report = diffProcessorService.executeDiffTool(doc);
		Assert.assertThat(report.getId(), CoreMatchers.equalTo(doc.getId()));
		Assert.assertThat(report.isEqual(), CoreMatchers.equalTo(false));
		Assert.assertThat(report.getOccurrences().size(), CoreMatchers.equalTo(2));
	}
	
	
	@Test
	public void testExecuteDiffToolEqualLargeCLob_shouldBeSuccessful()
	{
		String right = "CS8qKg0KCSAqIFNjZW5hcmlvOiBzdWJtaXQgbGVmdCB0aGVuIHJpZ2h0IGNvbnRlbnQgb2YgZXF1YWwgZGF0YQ0KCSAqIFJlc3VsdDogZXhwZWN0ZWQgZXF1YWwsIG5vIGVycm9ycw0KCSAqIEB0aHJvd3MgRXhjZXB0aW9uDQoJICovDQoJQFRlc3QNCglwdWJsaWMgdm9pZCB0ZXN0X2NyZWF0ZURpZmZMZWZ0VGhlblJpZ2h0Tm90RXF1YWxTYW1lU2l6ZV9zaG91bGRCZVN1Y2Nlc3NmdWwoKSB0aHJvd3MgRXhjZXB0aW9uIHsNCgkJaW50IGlkID0gNDsNCgkJU3RyaW5nIHBhdGggPSAiL3YxL2RpZmYvIitpZCsiL2xlZnQiOw0KCQlTdHJpbmcgY29udGVudCA9ICJibGFoIjsNCgkJUmVzdWx0QWN0aW9ucyByZXN1bHQgPSBtdmMucGVyZm9ybShNb2NrTXZjUmVxdWVzdEJ1aWxkZXJzLnBvc3QocGF0aCkuY2hhcmFjdGVyRW5jb2RpbmcoInV0ZjgiKS4NCgkJCQljb250ZW50VHlwZSgidGV4dC9wbGFpbiIpLmFjY2VwdChNZWRpYVR5cGUuQVBQTElDQVRJT05fSlNPTikuY29udGVudChjb250ZW50KSkNCgkJLmFuZEV4cGVjdChzdGF0dXMoKS5pc09rKCkpOw0KDQoJCVN0cmluZyBjb250ZW50QXNTdHJpbmcgPSByZXN1bHQuYW5kUmV0dXJuKCkuZ2V0UmVzcG9uc2UoKS5nZXRDb250ZW50QXNTdHJpbmcoKTsNCgkJDQoJCURvY3VtZW50Q29udGV4dCBqc29uID0gSnNvblBhdGgucGFyc2UoY29udGVudEFzU3RyaW5nKTsNCgkJDQoJCUFzc2VydC5hc3NlcnRUaGF0KGpzb24ucmVhZCgiJFsnaWQnXSIpLCBDb3JlTWF0Y2hlcnMuZXF1YWxUbyhpZCkpOw0KCQlBc3NlcnQuYXNzZXJ0VGhhdChqc29uLnJlYWQoIiRbJ2xlZnQnXSIpLCBDb3JlTWF0Y2hlcnMuZXF1YWxUbyhjb250ZW50KSk7DQoNCgkJcGF0aCA9ICIvdjEvZGlmZi8iK2lkKyIvcmlnaHQiOw0KCQljb250ZW50ID0gImJsZWgiOw0KCQlyZXN1bHQgPSBtdmMucGVyZm9ybShNb2NrTXZjUmVxdWVzdEJ1aWxkZXJzLnBvc3QocGF0aCkuY2hhcmFjdGVyRW5jb2RpbmcoInV0ZjgiKS4NCgkJCQljb250ZW50VHlwZSgidGV4dC9wbGFpbiIpLmFjY2VwdChNZWRpYVR5cGUuQVBQTElDQVRJT05fSlNPTikuY29udGVudChjb250ZW50KSkNCgkJLmFuZEV4cGVjdChzdGF0dXMoKS5pc09rKCkpOw0KDQoJCWNvbnRlbnRBc1N0cmluZyA9IHJlc3VsdC5hbmRSZXR1cm4oKS5nZXRSZXNwb25zZSgpLmdldENvbnRlbnRBc1N0cmluZygpOw0KCQkNCgkJanNvbiA9IEpzb25QYXRoLnBhcnNlKGNvbnRlbnRBc1N0cmluZyk7DQoJCQ0KCQlBc3NlcnQuYXNzZXJ0VGhhdChqc29uLnJlYWQoIiRbJ2lkJ10iKSwgQ29yZU1hdGNoZXJzLmVxdWFsVG8oaWQpKTsNCgkJQXNzZXJ0LmFzc2VydFRoYXQoanNvbi5yZWFkKCIkWydsZWZ0J10iKSwgQ29yZU1hdGNoZXJzLmVxdWFsVG8oImJsYWgiKSk7DQoJCUFzc2VydC5hc3NlcnRUaGF0KGpzb24ucmVhZCgiJFsncmlnaHQnXSIpLCBDb3JlTWF0Y2hlcnMuZXF1YWxUbyhjb250ZW50KSk7DQoNCgkJcGF0aCA9ICIvdjEvZGlmZi8iK2lkOw0KCQlyZXN1bHQgPSBtdmMucGVyZm9ybShNb2NrTXZjUmVxdWVzdEJ1aWxkZXJzLmdldChwYXRoKS5jaGFyYWN0ZXJFbmNvZGluZygidXRmOCIpLmFjY2VwdChNZWRpYVR5cGUuQVBQTElDQVRJT05fSlNPTikpDQoJCS5hbmRFeHBlY3Qoc3RhdHVzKCkuaXNPaygpKTsNCgkJY29udGVudEFzU3RyaW5nID0gcmVzdWx0LmFuZFJldHVybigpLmdldFJlc3BvbnNlKCkuZ2V0Q29udGVudEFzU3RyaW5nKCk7DQoNCgkJanNvbiA9IEpzb25QYXRoLnBhcnNlKGNvbnRlbnRBc1N0cmluZyk7DQoJCQ0KCQlBc3NlcnQuYXNzZXJ0VGhhdChqc29uLnJlYWQoIiRbJ2lkJ10iKSwgQ29yZU1hdGNoZXJzLmVxdWFsVG8oaWQpKTsNCgkJQXNzZXJ0LmFzc2VydFRoYXQoanNvbi5yZWFkKCIkWydlcXVhbCddIiksIENvcmVNYXRjaGVycy5lcXVhbFRvKGZhbHNlKSk7DQoJCQ0KCQlBc3NlcnQuYXNzZXJ0VGhhdChqc29uLnJlYWQoIiRbJ29jY3VycmVuY2VzJ10iKSwgQ29yZU1hdGNoZXJzLm5vdE51bGxWYWx1ZSgpKTsNCgkJQXNzZXJ0LmFzc2VydFRoYXQoanNvbi5yZWFkKCIkWydvY2N1cnJlbmNlcyddWzBdIiksIENvcmVNYXRjaGVycy5ub3ROdWxsVmFsdWUoKSk7DQoJCUFzc2VydC5hc3NlcnRUaGF0KGpzb24ucmVhZCgiJFsnb2NjdXJyZW5jZXMnXVswXVsncG9zaXRpb24nXSIpLCBDb3JlTWF0Y2hlcnMuZXF1YWxUbygyKSk7DQoJCUFzc2VydC5hc3NlcnRUaGF0KGpzb24ucmVhZCgiJFsnb2NjdXJyZW5jZXMnXVswXVsnc2l6ZSddIiksIENvcmVNYXRjaGVycy5lcXVhbFRvKDEpKTsNCgkJDQoJfQ0K";
		String left =  "CS8qKg0KCSAqIFNjZW5hcmlvOiBzdWJtaXQgbGVmdCB0aGVuIHJpZ2h0IGNvbnRlbnQgb2YgZXF1YWwgZGF0YQ0KCSAqIFJlc3VsdDogZXhwZWN0ZWQgZXF1YWwsIG5vIGVycm9ycw0KCSAqIEB0aHJvd3MgRXhjZXB0aW9uDQoJICovDQoJQFRlc3QNCglwdWJsaWMgdm9pZCB0ZXN0X2NyZWF0ZURpZmZMZWZ0VGhlblJpZ2h0Tm90RXF1YWxTYW1lU2l6ZV9zaG91bGRCZVN1Y2Nlc3NmdWwoKSB0aHJvd3MgRXhjZXB0aW9uIHsNCgkJaW50IGlkID0gNDsNCgkJU3RyaW5nIHBhdGggPSAiL3YxL2RpZmYvIitpZCsiL2xlZnQiOw0KCQlTdHJpbmcgY29udGVudCA9ICJibGFoIjsNCgkJUmVzdWx0QWN0aW9ucyByZXN1bHQgPSBtdmMucGVyZm9ybShNb2NrTXZjUmVxdWVzdEJ1aWxkZXJzLnBvc3QocGF0aCkuY2hhcmFjdGVyRW5jb2RpbmcoInV0ZjgiKS4NCgkJCQljb250ZW50VHlwZSgidGV4dC9wbGFpbiIpLmFjY2VwdChNZWRpYVR5cGUuQVBQTElDQVRJT05fSlNPTikuY29udGVudChjb250ZW50KSkNCgkJLmFuZEV4cGVjdChzdGF0dXMoKS5pc09rKCkpOw0KDQoJCVN0cmluZyBjb250ZW50QXNTdHJpbmcgPSByZXN1bHQuYW5kUmV0dXJuKCkuZ2V0UmVzcG9uc2UoKS5nZXRDb250ZW50QXNTdHJpbmcoKTsNCgkJDQoJCURvY3VtZW50Q29udGV4dCBqc29uID0gSnNvblBhdGgucGFyc2UoY29udGVudEFzU3RyaW5nKTsNCgkJDQoJCUFzc2VydC5hc3NlcnRUaGF0KGpzb24ucmVhZCgiJFsnaWQnXSIpLCBDb3JlTWF0Y2hlcnMuZXF1YWxUbyhpZCkpOw0KCQlBc3NlcnQuYXNzZXJ0VGhhdChqc29uLnJlYWQoIiRbJ2xlZnQnXSIpLCBDb3JlTWF0Y2hlcnMuZXF1YWxUbyhjb250ZW50KSk7DQoNCgkJcGF0aCA9ICIvdjEvZGlmZi8iK2lkKyIvcmlnaHQiOw0KCQljb250ZW50ID0gImJsZWgiOw0KCQlyZXN1bHQgPSBtdmMucGVyZm9ybShNb2NrTXZjUmVxdWVzdEJ1aWxkZXJzLnBvc3QocGF0aCkuY2hhcmFjdGVyRW5jb2RpbmcoInV0ZjgiKS4NCgkJCQljb250ZW50VHlwZSgidGV4dC9wbGFpbiIpLmFjY2VwdChNZWRpYVR5cGUuQVBQTElDQVRJT05fSlNPTikuY29udGVudChjb250ZW50KSkNCgkJLmFuZEV4cGVjdChzdGF0dXMoKS5pc09rKCkpOw0KDQoJCWNvbnRlbnRBc1N0cmluZyA9IHJlc3VsdC5hbmRSZXR1cm4oKS5nZXRSZXNwb25zZSgpLmdldENvbnRlbnRBc1N0cmluZygpOw0KCQkNCgkJanNvbiA9IEpzb25QYXRoLnBhcnNlKGNvbnRlbnRBc1N0cmluZyk7DQoJCQ0KCQlBc3NlcnQuYXNzZXJ0VGhhdChqc29uLnJlYWQoIiRbJ2lkJ10iKSwgQ29yZU1hdGNoZXJzLmVxdWFsVG8oaWQpKTsNCgkJQXNzZXJ0LmFzc2VydFRoYXQoanNvbi5yZWFkKCIkWydsZWZ0J10iKSwgQ29yZU1hdGNoZXJzLmVxdWFsVG8oImJsYWgiKSk7DQoJCUFzc2VydC5hc3NlcnRUaGF0KGpzb24ucmVhZCgiJFsncmlnaHQnXSIpLCBDb3JlTWF0Y2hlcnMuZXF1YWxUbyhjb250ZW50KSk7DQoNCgkJcGF0aCA9ICIvdjEvZGlmZi8iK2lkOw0KCQlyZXN1bHQgPSBtdmMucGVyZm9ybShNb2NrTXZjUmVxdWVzdEJ1aWxkZXJzLmdldChwYXRoKS5jaGFyYWN0ZXJFbmNvZGluZygidXRmOCIpLmFjY2VwdChNZWRpYVR5cGUuQVBQTElDQVRJT05fSlNPTikpDQoJCS5hbmRFeHBlY3Qoc3RhdHVzKCkuaXNPaygpKTsNCgkJY29udGVudEFzU3RyaW5nID0gcmVzdWx0LmFuZFJldHVybigpLmdldFJlc3BvbnNlKCkuZ2V0Q29udGVudEFzU3RyaW5nKCk7DQoNCgkJanNvbiA9IEpzb25QYXRoLnBhcnNlKGNvbnRlbnRBc1N0cmluZyk7DQoJCQ0KCQlBc3NlcnQuYXNzZXJ0VGhhdChqc29uLnJlYWQoIiRbJ2lkJ10iKSwgQ29yZU1hdGNoZXJzLmVxdWFsVG8oaWQpKTsNCgkJQXNzZXJ0LmFzc2VydFRoYXQoanNvbi5yZWFkKCIkWydlcXVhbCddIiksIENvcmVNYXRjaGVycy5lcXVhbFRvKGZhbHNlKSk7DQoJCQ0KCQlBc3NlcnQuYXNzZXJ0VGhhdChqc29uLnJlYWQoIiRbJ29jY3VycmVuY2VzJ10iKSwgQ29yZU1hdGNoZXJzLm5vdE51bGxWYWx1ZSgpKTsNCgkJQXNzZXJ0LmFzc2VydFRoYXQoanNvbi5yZWFkKCIkWydvY2N1cnJlbmNlcyddWzBdIiksIENvcmVNYXRjaGVycy5ub3ROdWxsVmFsdWUoKSk7DQoJCUFzc2VydC5hc3NlcnRUaGF0KGpzb24ucmVhZCgiJFsnb2NjdXJyZW5jZXMnXVswXVsncG9zaXRpb24nXSIpLCBDb3JlTWF0Y2hlcnMuZXF1YWxUbygyKSk7DQoJCUFzc2VydC5hc3NlcnRUaGF0KGpzb24ucmVhZCgiJFsnb2NjdXJyZW5jZXMnXVswXVsnc2l6ZSddIiksIENvcmVNYXRjaGVycy5lcXVhbFRvKDEpKTsNCgkJDQoJfQ0K";
		Long id = new Long(2);
		doc.setId(id);
		doc.setLeft(left);
		doc.setRight(right);
		
		DiffProcessorReport report = diffProcessorService.executeDiffTool(doc);
		Assert.assertThat(report.getId(), CoreMatchers.equalTo(doc.getId()));
		Assert.assertThat(report.isEqual(), CoreMatchers.equalTo(true));
	}
	
	@Test
	public void test_ExecuteDiffToolEqualLargeCLobRandom_shouldBeSuccessful()
	{
		int size = 12048576;
		byte[] r = new byte[size]; //Means 1MB
		Random rnd = new Random();
		rnd.nextBytes(r);
		String s = new String(r);
		
		String right = s;
		String left =  s;
		Long id = new Long(2);
		doc.setId(id);
		doc.setLeft(left);
		doc.setRight(right);
		
		DiffProcessorReport report = diffProcessorService.executeDiffTool(doc);
		Assert.assertThat(report.getId(), CoreMatchers.equalTo(doc.getId()));
		Assert.assertThat(report.isEqual(), CoreMatchers.equalTo(true));
	}
}
