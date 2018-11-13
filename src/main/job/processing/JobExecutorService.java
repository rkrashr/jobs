package job.processing;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;
import java.util.stream.Stream;

import job.Job;
import job.JobContext;
import job.JobResult;
import job.JobTypeRegistry;

public class JobExecutorService {

	JobTypeRegistry jobRegistry;
	
	ExecutorService executor;
	
	List<Consumer<JobResult>> handlers;
	
	public JobExecutorService(JobTypeRegistry registry) {
		this.jobRegistry = registry;
		this.executor = new ThreadPoolExecutor(3, 5, 1, TimeUnit.SECONDS, new LinkedBlockingDeque<Runnable>());
		this.handlers = new ArrayList<Consumer<JobResult>>();
	}

	public void addHandler(Consumer<JobResult> handler) {
		handlers.add(handler);
	}
	
	public void execute(JobContext context) {
		runJob(context)
		.thenAcceptAsync(
				result -> processResult(result), 
				executor);
	}
	
	CompletableFuture<JobResult> runJob(JobContext context) {

		Job job = jobRegistry.makeJobFor(context);
		
		return CompletableFuture.supplyAsync(job, executor);
	}
	
	void processResult(JobResult data) {
		
		for (Consumer<JobResult> handler: handlers) {
			handler.accept(data);
		}
	}
}
