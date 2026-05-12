package com.example.worker_service.service;

import com.example.worker_service.entity.Notification;
import com.example.worker_service.repository.NotificationRepository;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.*;

@Service
public class NotificationService {

    private final NotificationRepository repository;

    private final BlockingQueue<Long> pendingQueue = new LinkedBlockingQueue<>();

    private final ExecutorService workerPool = Executors.newFixedThreadPool(4);

    private volatile boolean running = true;

    public NotificationService(NotificationRepository repository) {
        this.repository = repository;
    }

    @PostConstruct
    public void startWorkers() {

        for (int i = 0; i < 4; i++) {
            workerPool.submit(new Worker());
        }

        System.out.println("Запущено 4 воркера");
    }

    @PreDestroy
    public void gracefulShutdown() {

        System.out.println("Остановка воркеров...");

        running = false;

        workerPool.shutdown();

        try {

            if (!workerPool.awaitTermination(30, TimeUnit.SECONDS)) {
                workerPool.shutdownNow();
            }

        } catch (InterruptedException e) {
            workerPool.shutdownNow();
        }

        System.out.println("Воркеры остановлены");
    }

    public Long addNotification(Long tripId, String recipientType, Long recipientId, String message) {

        Notification notification = new Notification(
                null,
                tripId,
                recipientType,
                recipientId,
                message,
                "PENDING",
                0,
                null
        );

        Notification savedNotification = repository.save(notification);

        pendingQueue.offer(savedNotification.getId());

        System.out.println("Добавлено уведомление " + savedNotification.getId());

        return savedNotification.getId();
    }

    public List<Notification> getNotificationsByTrip(Long tripId) {
        return repository.findByTripId(tripId);
    }

    private class Worker implements Runnable {

        @Override
        public void run() {

            while (running) {

                try {

                    Long id = pendingQueue.poll(5, TimeUnit.SECONDS);

                    if (id == null) {
                        continue;
                    }

                    processNotification(id);

                } catch (InterruptedException e) {

                    Thread.currentThread().interrupt();
                    break;
                }
            }
        }

        private void processNotification(Long id) {

            Notification notification = repository.findById(id).orElse(null);

            if (notification == null) {
                return;
            }

            if (!"PENDING".equals(notification.getStatus())) {
                return;
            }

            notification.setStatus("PROCESSING");

            repository.save(notification);

            try {

                System.out.println(
                        "Воркер "
                                + Thread.currentThread().getName()
                                + " отправляет уведомление: "
                                + notification.getMessage()
                );

                Thread.sleep(1000);

                notification.setStatus("SENT");

                repository.save(notification);

                System.out.println("Уведомление " + id + " отправлено");

            } catch (Exception e) {

                int attempts = notification.getAttempts() + 1;

                notification.setAttempts(attempts);

                if (attempts >= 3) {
                    notification.setStatus("FAILED");
                } else {
                    notification.setStatus("PENDING");
                    pendingQueue.offer(id);
                }

                repository.save(notification);
            }
        }
    }
}