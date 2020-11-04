package lab.idioglossia.gitblog;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
@Slf4j
public class GitBlogApplication {
	private static ConfigurableApplicationContext context;

	public static void main(String[] args) {
		context = SpringApplication.run(GitBlogApplication.class, args);
	}


	public synchronized static void restart() {
		ApplicationArguments args = context.getBean(ApplicationArguments.class);

		Thread thread = new Thread(() -> {
			try {
				Thread.sleep(2000);
			} catch (InterruptedException e) {
				log.error("Interrupted while sleeping before restart", e);
			}
			context.close();
			main(args.getSourceArgs());
		});

		thread.setDaemon(false);
		thread.start();
	}

}
