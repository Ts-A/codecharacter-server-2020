package delta.codecharacter.server.service;

import delta.codecharacter.server.controller.request.Notification.CreateNotificationRequest;
import delta.codecharacter.server.model.Notification;
import delta.codecharacter.server.model.User;
import delta.codecharacter.server.repository.NotificationRepository;
import delta.codecharacter.server.repository.UserRepository;
import delta.codecharacter.server.util.enums.Type;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;
import java.util.logging.Logger;

@Service
public class NotificationService {

    private final Logger LOG = Logger.getLogger(NotificationService.class.getName());

    @Autowired
    private NotificationRepository notificationRepository;

    @Autowired
    private UserRepository userRepository;

    /**
     * Create a notification with the given details
     *
     * @param createNotificationRequest Details of notification: title, content and type
     * @return Notification which was created
     */
    @SneakyThrows
    public Notification createNotification(@NotNull CreateNotificationRequest createNotificationRequest) {
        Integer notificationId = getMaxNotificationId() + 1;
        Notification notification = Notification.builder()
                .id(notificationId)
                .userId(createNotificationRequest.getUserId())
                .title(createNotificationRequest.getTitle())
                .content(createNotificationRequest.getContent())
                .type(createNotificationRequest.getType())
                .build();

        notificationRepository.save(notification);

        return notification;
    }

    /**
     * Set isRead of a notification to true
     *
     * @param notificationId NotificationId of the notification to be modified
     */
    @SneakyThrows
    public void setIsReadNotificationById(@NotNull Integer notificationId) {
        Notification notification = notificationRepository.findFirstById(notificationId);
        notification.setIsRead(true);
        notificationRepository.save(notification);
    }

    /**
     * Delete notification with the specified notificationId.
     *
     * @param notificationId NotificationId of the notification to be deleted
     */
    @SneakyThrows
    public void deleteNotificationById(@NotNull Integer notificationId) {
        Notification notification = notificationRepository.findFirstById(notificationId);
        notificationRepository.delete(notification);
    }

    /**
     * Return a page of a specific type of notification for the given user
     *
     * @param type       Type of notification
     * @param userId     UserId of the given user
     * @param pageNumber Page number
     * @param size       Size of the response list
     * @return List of notifications by type for the given user
     */
    @SneakyThrows
    public Page<Notification> getAllNotificationsByTypeAndUserIdPaginated(@NotNull Type type,
                                                                          @NotNull Integer userId,
                                                                          @NotNull int pageNumber,
                                                                          @NotNull int size) {
        Pageable pageable = PageRequest.of(pageNumber - 1, size);
        return notificationRepository.findAllByTypeAndUserId(type, userId, pageable);
    }

    /**
     * Delete all notifications of a specific type for the given user
     *
     * @param type   Type of notification
     * @param userId UserId of the given user
     */
    @SneakyThrows
    public void deleteNotificationsByTypeAndUserId(@NotNull Type type, @NotNull Integer userId) {
        List<Notification> notifications = notificationRepository.findAllByTypeAndUserId(type, userId);
        notificationRepository.deleteAll(notifications);
    }

    /**
     * Return a page of all notifications for the given user
     *
     * @param userId     UserId of the given user
     * @param pageNumber Page number
     * @param size       Size of the response list
     * @return Paginated response of all notifications for the given user
     */
    @SneakyThrows
    public Page<Notification> getAllNotificationsByUserIdPaginated(@NotNull Integer userId,
                                                                   @NotNull @Positive int pageNumber,
                                                                   @NotNull @PositiveOrZero int size) {
        Pageable pageable = PageRequest.of(pageNumber - 1, size);
        return notificationRepository.findAllByUserIdOrderByIdDesc(userId, pageable);
    }

    /**
     * Return a page of all unread notifications for the given user
     *
     * @param userId     UserId of the given user
     * @param pageNumber Page number
     * @param size       Size of the response list
     * @return Paginated response of all unread notifications for the given user
     */
    @SneakyThrows
    public Page<Notification> getAllUnreadNotificationsByUserIdPaginated(@NotNull Integer userId,
                                                                         @NotNull @Positive int pageNumber,
                                                                         @NotNull @PositiveOrZero int size) {
        Pageable pageable = PageRequest.of(pageNumber - 1, size);
        return notificationRepository.findAllByUserIdAndIsReadFalseOrderByIdDesc(userId, pageable);
    }

    /**
     * Check if a user has access to a particular notification
     *
     * @param userId         UserId of the given user
     * @param notificationId NotificationId of the required notification
     * @return True if a user is admin or is the creator of the notification, False otherwise.
     */
    @SneakyThrows
    public boolean checkNotificationAccess(@NotNull Integer userId, @NotNull Integer notificationId) {
        Notification notification = notificationRepository.findFirstById(notificationId);
        User user = userRepository.findByUserId(userId);
        return notification.getUserId().equals(userId) || user.getIsAdmin();
    }

    /**
     * Get the maximum notificationId
     *
     * @return Maximum notificationId
     */
    private Integer getMaxNotificationId() {
        Notification notification = notificationRepository.findFirstByOrderByIdDesc();
        if (notification == null) {
            return 1;
        }
        return notification.getId();
    }

    /**
     * Find and return the details of a notification by ID
     *
     * @param notificationId NotificationId of the required notification
     * @return Details of Notification with the given ID, Null if it doesn't exist
     */
    @SneakyThrows
    public Notification findNotificationById(Integer notificationId) {
        return notificationRepository.findFirstById(notificationId);
    }
}
