package job.processing;

import java.util.stream.Stream;

import job.JobResult;

public interface JobResultToEmailTransform extends JobResultTransform< Stream<Email> >{

}
