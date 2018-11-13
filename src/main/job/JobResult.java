package job;

public interface JobResult {

	public boolean isSuccess();
	public Exception getError();
	public JobContext getContext();
	
}
