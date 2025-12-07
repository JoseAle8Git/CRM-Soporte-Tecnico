package com.crm.crmSoporteTecnico.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * Clase de configuración para activar el procesamiento de @Scheduled.
 */
@Configuration
@EnableScheduling
public class SchedulerConfig {

    /**
     * Esto es un ejemplo de cómo se crearía un pool de hilos para el caso de que se requiera que se ejecute más de una tarea
     * al mismo tiempo sin bloquearse una con otra. Esto funciona para tareas programadas con la anotación @Scheduled.
     */
    /**
     * @Bean
     * public ThreadPoolTaskScheduler taskScheduler() {
     *     scheduler.setPoolSize(5); <-- Esto indica la cantidad de hilos que es capaz de crear pueden ser la cantidad que uno quiera.
     *     scheduler.setThreadNamePrefix("scheduled-task-");
     *     scheduler.initialize();
     *     return scheduler;
     * }
     */


}
