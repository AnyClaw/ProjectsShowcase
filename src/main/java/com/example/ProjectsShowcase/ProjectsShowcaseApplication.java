package com.example.ProjectsShowcase;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.example.ProjectsShowcase.models.ProjectFullInfo;
import com.example.ProjectsShowcase.models.ProjectFullInfo.Status;
import com.example.ProjectsShowcase.repositories.ProjectRepository;
import com.example.ProjectsShowcase.repositories.UserRepository;

@SpringBootApplication
public class ProjectsShowcaseApplication implements WebMvcConfigurer {

	public static void main(String[] args) {
		SpringApplication.run(ProjectsShowcaseApplication.class, args);
	}

	@Override
	public void addViewControllers(ViewControllerRegistry viewControllerRegistry) {
		viewControllerRegistry.addViewController("/").setViewName("index.html");
	}

	@Bean
    CommandLineRunner dataLoader(ProjectRepository projectRepository, UserRepository userRepository) {
		return new CommandLineRunner() {
			@Override
			public void run(String... args) throws Exception {
				projectRepository.save(new ProjectFullInfo(
					0L, "Витрина проектной деятельности ИУЦТ, УТБиИС", "УТБиИС", Status.FREE, 
					"Создать более удобный инструмент, реализующий необходимый функционал витрины проектной деятельности, для студентов, преподавателей и администрации ИУЦТ",
					"Нет единой автоматизированной системы для создания и обработки заявок, брони тем проектов с витрины, создания команд",
					"Существующее решение, созданное в рамках ИУЦТ, не является удобным инструментом ни для студентов, ни для администрации по причине полного отсутствия автоматизации. Аналоги из других ВУЗов используются в рамках их внутренней экосистемы и не предназначены для внедрения в другие университеты")
				);
				projectRepository.save(new ProjectFullInfo(
					1L, "Локомотива нет - еще не конец!, ЖДСТУ", "ЖДСТУ", Status.ON_VERIFICATION, 
					"Создать более удобный инструмент, реализующий необходимый функционал витрины проектной деятельности, для студентов, преподавателей и администрации ИУЦТ",
					"", "")
				);
				projectRepository.save(new ProjectFullInfo(
					2L, "Кто рулит поездом?, ЖДСТУ", "ЖДСТУ", Status.CANCELED, 
					"Создать более удобный инструмент, реализующий необходимый функционал витрины проектной деятельности, для студентов, преподавателей и администрации ИУЦТ",
					"", "")
				);
				projectRepository.save(new ProjectFullInfo(
					3L, "Оптимальный ремонт вагона при любой сезонности, ЦТУТП", "ЦТУТП", Status.ON_WORK, 
					"Создать более удобный инструмент, реализующий необходимый функционал витрины проектной деятельности, для студентов, преподавателей и администрации ИУЦТ",
					"", "")
				);
				projectRepository.save(new ProjectFullInfo(
					4L, "Поставка плодоовощной продукции без отказов, ЛТСТ", "ЛТСТ", Status.COMPLETED, 
					"Создать более удобный инструмент, реализующий необходимый функционал витрины проектной деятельности, для студентов, преподавателей и администрации ИУЦТ",
					"", "")
				);
			}
  		};
	}
}
