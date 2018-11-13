package job;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;

import job.processing.JobResultToEmailTransform;
import job.processing.JobResultTransform;
import job.processing.JobResultToTicketTransform;

public class JobTypeRegistry {

	
	private final Map<Class<? extends JobContext>, JobBuilder> jobRegistry;
	private final Map<Class<? extends JobContext>, JobResultToEmailTransform> emailRegistry;
	private final Map<Class<? extends JobContext>, JobResultToTicketTransform> ticketRegistry;
	
	public JobTypeRegistry() {
		jobRegistry = new HashMap<Class<? extends JobContext>, JobBuilder>();
		emailRegistry = new HashMap<Class<? extends JobContext>, JobResultToEmailTransform>();
		ticketRegistry = new HashMap<Class<? extends JobContext>, JobResultToTicketTransform>();
	}
	
	public void add(Class<? extends JobContext> clazz, JobBuilder builder) {
		jobRegistry.put(clazz, builder);
	}
	
	public void add(Class<JobContext> clazz, JobResultToEmailTransform toEmail) {
		emailRegistry.put(clazz, toEmail);
	}

	public void add(Class<JobContext> clazz, JobResultToTicketTransform toTicket) {
		ticketRegistry.put(clazz, toTicket);
	}
	
	public Job makeJobFor(JobContext context) {
		JobBuilder builder = jobRegistry.get(context.getClass());
		return builder.build(context);
	}
	
	public JobResultToEmailTransform makeEmailFor(JobContext context) {
		return emailRegistry.get(context.getClass());
	}

	public JobResultToTicketTransform makeTicketFor(JobContext context) {
		return ticketRegistry.get(context.getClass());
	}
}
