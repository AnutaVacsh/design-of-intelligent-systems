package ru.lama.expertCookingSystem.drools.config;

import org.kie.api.KieServices;
import org.kie.api.builder.KieBuilder;
import org.kie.api.builder.KieFileSystem;
import org.kie.api.builder.KieModule;
import org.kie.api.runtime.KieContainer;
import org.kie.internal.io.ResourceFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Arrays;
import java.util.List;

@Configuration
public class DroolsConfig {

  private final KieServices kieServices = KieServices.Factory.get();

  private static final List<String> DRL_FILES = Arrays.asList(
      "drools/rules/cooking-rules-skip.drl",
      "drools/rules/cooking-rules-core.drl"
//      ,
//      "drools/old/SessionFlowRules.drl"
  );

  @Bean
  public KieContainer kieContainer() {
    KieFileSystem kieFileSystem = kieServices.newKieFileSystem();

    // Загружаем файлы правил
    for (String drlFile : DRL_FILES) {
      try {
        kieFileSystem.write(ResourceFactory.newClassPathResource(drlFile));
        System.out.println("Loaded DRL file: " + drlFile);
      } catch (Exception e) {
        System.out.println("DRL file not found!!!: " + drlFile);
      }
    }

    KieBuilder kieBuilder = kieServices.newKieBuilder(kieFileSystem);
    kieBuilder.buildAll();

    if (kieBuilder.getResults().hasMessages(org.kie.api.builder.Message.Level.ERROR)) {
      throw new RuntimeException("Build Errors:\n" + kieBuilder.getResults().toString());
    }

    KieModule kieModule = kieBuilder.getKieModule();
    return kieServices.newKieContainer(kieModule.getReleaseId());
  }
}