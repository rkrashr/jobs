package job.processing;

import org.junit.jupiter.api.Test;

import job.Job;
import job.JobBuilder;
import job.JobContext;
import job.JobResult;
import job.JobTypeRegistry;

class JobExecutorServiceTest {

	
	static class TestJobContext implements JobContext {
		public int data;
		public TestJobContext(int data) {
			this.data = data;
		}
	}
	
	static class TestJob implements Job {

		private final TestJobContext context;
		
		public TestJob(TestJobContext context) {
			this.context = context;
		}
		
		@Override
		public JobResult get() {
			
			
			try {
				
				System.out.println("running job " + context.data);
				Thread.sleep(1000);
				
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			
			return new TestJobResult(context);
		}
		
	}

	static class TestJobResult implements JobResult {

		private final TestJobContext context;
		
		public TestJobResult(TestJobContext context) {
			this.context = context;
		}
		
		@Override
		public boolean isSuccess() {
			return true;
		}

		@Override
		public Exception getError() {
			return null;
		}

		@Override
		public JobContext getContext() {
			return context;
		}
		
	}	
	
	static class TestJobBuilder implements JobBuilder {

		@Override
		public Job build(JobContext context) {
			return new TestJob((TestJobContext)context);
		}
	}

	static JobTypeRegistry reg = new JobTypeRegistry();
	static TestJobBuilder builder = new TestJobBuilder();
	static {
		reg.add(TestJobContext.class, builder);
	}
	
	void processEmail(JobResult data) {
		TestJobContext context = (TestJobContext)data.getContext();
		
		System.out.println("Process emails for " + context.data);
	
	}

	void processTickets(JobResult data) {
		TestJobContext context = (TestJobContext)data.getContext();

		System.out.println("Process tickets for " +  context.data);

	}

	
	@Test
	void test() throws InterruptedException {

		JobExecutorService service = new JobExecutorService(reg);
		service.addHandler(data -> processEmail(data));
		service.addHandler(data -> processTickets(data));
		
		service.execute(new TestJobContext(1));
		service.execute(new TestJobContext(2));
		service.execute(new TestJobContext(3));
		service.execute(new TestJobContext(4));	
		
		Thread.sleep(2000);
	}

}
