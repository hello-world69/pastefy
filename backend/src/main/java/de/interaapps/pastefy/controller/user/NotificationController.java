package de.interaapps.pastefy.controller.user;

import de.interaapps.pastefy.controller.HttpController;
import de.interaapps.pastefy.model.database.Notification;
import de.interaapps.pastefy.model.database.User;
import de.interaapps.pastefy.model.responses.ActionResponse;
import org.javawebstack.httpserver.Exchange;
import org.javawebstack.httpserver.router.annotation.PathPrefix;
import org.javawebstack.httpserver.router.annotation.With;
import org.javawebstack.httpserver.router.annotation.params.Attrib;
import org.javawebstack.httpserver.router.annotation.verbs.Get;
import org.javawebstack.httpserver.router.annotation.verbs.Post;
import org.javawebstack.orm.Repo;
import org.javawebstack.orm.query.Query;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@PathPrefix("/api/v2/user/notification")
public class NotificationController extends HttpController {
    /**
     * Send the logged in user a notification
     */
    @Post
    @With("auth")
    public ActionResponse add(@Attrib("user") User user) {
        ActionResponse response = new ActionResponse();
        //Notification notification = new Notification();
        //notification.setMessage();
        //user.sendNotification(notification);

        List<String> list = new ArrayList<>();


        return response;
    }

    @Get
    @With("auth")
    public List<Notification> getNotifications(Exchange exchange, @Attrib("user") User user) {
        Query<Notification> query = Repo.get(Notification.class).where("userId", user.getId());

        if (exchange.rawRequest().getParameter("not_received") != null)
            query.where("received", false);


        if (exchange.rawRequest().getParameter("not_read") != null)
            query.where("alreadyRead", false);

        List<Notification> notifications = query.all();
        if (notifications.size() > 0)
            query.update(new HashMap() {{
                put("received", true);
            }});

        return notifications;
    }

    @Get("/readall")
    @With("auth")
    public ActionResponse readAll(@Attrib("user") User user) {
        Repo.get(Notification.class).query().where("userId", user.getId()).where("already_read", false).update(new HashMap() {{
            put("received", true);
            put("already_read", true);
        }});
        return new ActionResponse(true);
    }
}
