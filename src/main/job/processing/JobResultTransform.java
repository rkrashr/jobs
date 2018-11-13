package job.processing;

import java.util.function.Function;

import job.JobResult;

public interface JobResultTransform<Transformed> extends Function<JobResult, Transformed>{

}
