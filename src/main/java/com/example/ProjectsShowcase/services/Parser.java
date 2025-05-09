package com.example.ProjectsShowcase.services;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.ProjectsShowcase.models.MyUser;
import com.example.ProjectsShowcase.models.ProjectFullInfo;
import com.example.ProjectsShowcase.models.ProjectFullInfo.Status;
import com.example.ProjectsShowcase.repositories.ProjectRepository;
import com.example.ProjectsShowcase.repositories.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class Parser {
    private final ProjectRepository projectRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder encoder;

    public void loadData() {
        List<List<String>> data = new ArrayList<>();

        try {
            File input = new File("D:\\Java\\ProjectsShowcase\\src\\main\\resources\\static\\parser\\data.html");
            Document doc = Jsoup.parse(input, "UTF-8");

            Element table = doc.selectFirst("table");
            if (table != null) {
                Element tbody = table.selectFirst("tbody");
                if (tbody != null) {
                    Elements rows = tbody.select("tr");
                    
                    for (Element row : rows) {
                        List<String> rowData = new ArrayList<>();
                        Elements cols = row.select("td");

                        for (Element col : cols) {
                            rowData.add(col.text().trim());
                        }

                        data.add(rowData);
                    }
                } else {
                    System.out.println("Элемент <tbody> не найден.");
                }
            } else {
                System.out.println("Элемент <table> не найден.");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        long i = 0;
        String password = encoder.encode("cust");
        for (List<String> project : data) {
            if (!(project.get(0).equals("191") || project.get(0).equals("213") ||
                project.get(0).equals("214") || project.get(0).equals("215"))) {

                MyUser customer = new MyUser(null, project.get(2), "null", "null",
                "null", "null" + i++, password, "ROLE_USER, ROLE_CUSTOMER", project.get(2));
                if (!userRepository.existsByPost(customer.getName())) 
                    userRepository.save(customer);

                ProjectFullInfo projectFullInfo = new ProjectFullInfo(
                    null,
                    project.get(1),
                    project.get(8),
                    Status.FREE,
                    project.get(3),
                    project.get(4),
                    project.get(5),
                    project.get(6),
                    project.get(7),
                    userRepository.findByMail(customer.getMail()).get()
                );
                projectRepository.save(projectFullInfo);
            }
        }
    }
}
